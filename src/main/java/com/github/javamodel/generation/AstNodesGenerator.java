package com.github.javamodel.generation;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.antlr.runtime.*;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.antlr.v4.parse.*;
import org.antlr.v4.tool.ast.GrammarAST;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by federico on 24/05/15.
 */
public class AstNodesGenerator {

    private static String readFile( String file ) throws IOException {
        BufferedReader reader = new BufferedReader( new FileReader (file));
        String         line = null;
        StringBuilder  stringBuilder = new StringBuilder();
        String         ls = System.getProperty("line.separator");

        while( ( line = reader.readLine() ) != null ) {
            stringBuilder.append( line );
            stringBuilder.append( ls );
        }

        return stringBuilder.toString();
    }

    public static ANTLRParser.grammarSpec_return source(String source) throws IOException,
            RecognitionException {
        ANTLRStringStream in = new ANTLRStringStream(source);
        /*ANTLRLexer lexer = new ANTLRLexer(in);
        BufferedTokenStream stream = new BufferedTokenStream(lexer);
        ANTLRParser parser = new ANTLRParser(stream);

        Object grammarDef = parser.grammarSpec();

        return grammarDef;*/

       // InputStream in = new FileInputStream(path);
        GrammarASTAdaptor re = new GrammarASTAdaptor(in);
        ANTLRLexer lexer = new ANTLRLexer(in);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        lexer.tokens = tokens;
        ANTLRParser p = new ANTLRParser(tokens);
        p.setTreeAdaptor(re);
        return p.grammarSpec();
    }

    public static void printTree(GrammarAST tree, int indentation){
        if (tree == null) return;
        for (int i=0;i<indentation;i++)System.out.print("  ");
        System.out.println(tree + " "+tree.getType());
        if (tree.getChildren() == null) return;
        for (Object child : tree.getChildren()){
            if (child instanceof GrammarAST){
                printTree((GrammarAST)child, indentation+1);
            } else {
                System.out.println(" * " + child +" ("+child.getClass()+")");
            }
        }
    }

    private static List<String> interestingNames = ImmutableList.of("floatingPointType");
    //"compilationUnit", "packageDeclaration");

    public static void processNonTerminal(GrammarAST nonTerminalTree) throws IOException, TemplateException {
        String name = nonTerminalTree.getChild(0).getText();
        if (interestingNames.contains(name)){
            System.out.println("Rule found: " + nonTerminalTree.getChild(0));
            //printTree(nonTerminalTree, 0);
            boolean isAlternative = nonTerminalTree.getChild(1).getChildCount() > 1;
            boolean isSequence = !isAlternative;
            if (isEnum(nonTerminalTree)) {
                processEnum(nonTerminalTree);
            } else if (isSequence){
                processSequence(nonTerminalTree);
            }
            //System.out.println("  sequence? "+isSequence);
            //printTree(nonTerminalTree, 0);
        }
    }

    private static String stripQuotes(String s){
        return s.substring(1, s.length()-1);
    }

    private static void processEnum(GrammarAST tree) throws IOException, TemplateException {
        System.out.println("Generating enum");
        List<Map<String, Object>> values = new ArrayList<>();
        for (int i=0;i<tree.getChild(1).getChildCount();i++) {
            Tree value = tree.getChild(1).getChild(i).getChild(0);
            values.add(ImmutableMap.of(
                    "name", stripQuotes(value.getText()),
                            "literal", stripQuotes(value.getText())
                    ));
        }
        Map<String,Object> data = ImmutableMap.of(
                "name", ruleName(tree),
                "antlrNodeClass", ruleName(tree)+"Context",
                "values", values
        );
        Template template = freeMarkerConfiguration.getTemplate("EnumAstNode.ftl");
        StringWriter sw = new StringWriter();
        template.process(data, sw);
        System.out.println(sw.toString());
    }

    private static int T_BLOCK = 77;
    private static int T_ALT = 73;
    private static int T_STRING_LITERAL = 62;

    private static boolean isEnum(GrammarAST tree) {
        if (tree.getChildCount() != 2) return false;
        if (tree.getChild(1).getType() != T_BLOCK) return false;
        if (tree.getChild(1).getChildCount() < 2) return false;
        for (int i=0;i<tree.getChild(1).getChildCount();i++){
            Tree child = tree.getChild(1).getChild(i);
            if (child.getType() != T_ALT) return false;
            if (child.getChildCount() != 1) return false;
            if (child.getChild(0).getType() != T_STRING_LITERAL) return false;
        }
        return true;
    }

    private static String ruleName(GrammarAST tree){
        return tree.getChild(0).getText();
    }

    private static boolean isSingleElement(GrammarAST tree){
        return tree.getType() == 77 && tree.getChildCount() == 1 && tree.getChild(0).getType() == 73  && tree.getChild(0).getChildCount() == 1;
    }

    private static boolean isSingleSequence(GrammarAST tree){
        return tree.getType() == 77 && tree.getChildCount() == 1 && tree.getChild(0).getType() == 73  && tree.getChild(0).getChildCount() > 1;
    }

    private static String fieldName(GrammarAST tree, boolean multiple){
        // * 79
        //   BLOCK 77
        //     ALT 73
        //       packageModifier 57
        if (isSingleElement((GrammarAST)tree.getChild(0)) && tree.getChild(0).getChild(0).getChild(0).getType() == 57){
            String name = tree.getChild(0).getChild(0).getChild(0).getText();
            return multiple ? pluralize(name) : name;
        // * 79
        //   BLOCK 77
        //     ALT 73
        //       += 46
        //         topTypes 28
        //         typeDeclaration 57
        } else if (isSingleElement((GrammarAST)tree.getChild(0)) && tree.getChild(0).getChild(0).getChild(0).getType() == 46) {
            return tree.getChild(0).getChild(0).getChild(0).getChild(0).getText();
        } else if (isSingleSequence((GrammarAST)tree.getChild(0))){
            // if the sequence is made by two elements and the first one is just a string
            // like '.' or ',' we are just considering the second element
            GrammarAST alt = (GrammarAST)tree.getChild(0).getChild(0);
            if (alt.getChildCount()==2 && alt.getChild(0).getType() == 62){
                return alt.getChild(1).getText();
            }
        }
        throw new RuntimeException("Name for field "+tree.getType());
    }

    private static String fieldType(GrammarAST tree){
        if (isSingleElement((GrammarAST) tree.getChild(0)) && tree.getChild(0).getChild(0).getChild(0).getType() == 57){
            return tree.getChild(0).getChild(0).getChild(0).getText();
        } else if (isSingleElement((GrammarAST) tree.getChild(0)) && tree.getChild(0).getChild(0).getChild(0).getType() == 46) {
            return tree.getChild(0).getChild(0).getChild(0).getChild(1).getText();
        } else if (isSingleSequence((GrammarAST)tree.getChild(0))){
            // if the sequence is made by two elements and the first one is just a string
            // like '.' or ',' we are just considering the second element
            GrammarAST alt = (GrammarAST)tree.getChild(0).getChild(0);
            if (alt.getChildCount()==2 && alt.getChild(0).getType() == 62){
                return alt.getChild(1).getText();
            }
        }
        throw new RuntimeException();

    }

    private static String pluralize(String name) {
        return name+"s";
    }

    private static Optional<Map<String, Object>> processField(GrammarAST tree){
        if (tree.getType()==88){
            String name = fieldName(tree, false);
            System.out.println("Optional FIELD "+name);
            return Optional.of(ImmutableMap.of(
                    "name", name,
                    "type", fieldType(tree),
                    "multiple", false
            ));
        } else if (tree.getType()==79) {
            String name = fieldName(tree, true);
            System.out.println("Multiple FIELD " + name);
            return Optional.of(ImmutableMap.of(
                    "name", name,
                    "type", fieldType(tree),
                    "multiple", true
            ));
        } else if (tree.getType() == 66) {
            if (tree.getText().equals("EOF")) {
                return Optional.empty();
            } else {
                return Optional.of(ImmutableMap.of(
                        "name", tree.getText(),
                        "type", tree.getText(),
                        "multiple", false
                ));
            }
        } else if (tree.getType() == 62) {
            System.out.println("TERMINAL FIELD " + tree);
            return Optional.empty();
        } else {
            throw new RuntimeException("type is "+tree.getType());
        }
    }

    private static void processSequence(GrammarAST nonTerminalTree) throws IOException, TemplateException {
        System.out.println("  processing as sequence");

        List<Map<String,Object>> fields = new ArrayList<>();
        // We expect to have just children in the form BLOCK / ALT
        for (int i=0;i<nonTerminalTree.getChild(1).getChild(0).getChildCount();i++){
            processField((GrammarAST) nonTerminalTree.getChild(1).getChild(0).getChild(i)).ifPresent(
                    (field) -> fields.add(field)
            );
        }

        Map<String,Object> data = ImmutableMap.of(
                "name", ruleName(nonTerminalTree),
                "antlrNodeClass", ruleName(nonTerminalTree)+"Context",
                "fields", fields
        );
        Template template = freeMarkerConfiguration.getTemplate("AstNode.ftl");
        StringWriter sw = new StringWriter();
        template.process(data, sw);
        System.out.println(sw.toString());
    }

    public static void processGrammar(GrammarAST tree) throws IOException, TemplateException {
        if (tree == null) return;

        if (tree.getType() == 93){
            if (tree.getChild(0).getType() == 57) {
                //System.out.println("Rule found: " + tree.getChild(0));
                //printTree(tree, 0);
                processNonTerminal(tree);
            }
        }

        if (tree.getChildren() == null) return;
        for (Object child : tree.getChildren()){
            if (child instanceof GrammarAST){
                processGrammar((GrammarAST)child);
            }
        }
    }

    private static Configuration freeMarkerConfiguration;

    We should start from the root (CompilationUnit) and look into the other rules as we go.

    public static void main(String[] args) throws IOException, RecognitionException, TemplateException {
        freeMarkerConfiguration = new Configuration();
        freeMarkerConfiguration.setTemplateLoader(new ClassTemplateLoader(AstNodesGenerator.class, "/"));

        String antlrCode = readFile("src/main/antlr4/com/github/javamodel/Java8.g4");
        ANTLRParser.grammarSpec_return  result = source(antlrCode);
        //System.out.println("Parsed "+result.getTree());
        //printTree(result.getTree(), 0);
        processGrammar(result.getTree());
    }

}
