package com.github.javamodel.generation;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.antlr.runtime.*;
import org.antlr.runtime.tree.CommonTree;
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

    private static List<String> interestingNames = ImmutableList.of("compilationUnit", "packageDeclaration");

    public static void processNonTerminal(GrammarAST nonTerminalTree) throws IOException, TemplateException {
        String name = nonTerminalTree.getChild(0).getText();
        if (interestingNames.contains(name)){
            System.out.println("Rule found: " + nonTerminalTree.getChild(0));
            printTree(nonTerminalTree, 0);
            boolean isAlternative = nonTerminalTree.getChild(1).getChildCount() > 1;
            boolean isSequence = !isAlternative;
            if (isSequence){
                processSequence(nonTerminalTree);
            }
            //System.out.println("  sequence? "+isSequence);
            //printTree(nonTerminalTree, 0);
        }
    }

    private static String ruleName(GrammarAST tree){
        return tree.getChild(0).getText();
    }

    private static String fieldName(GrammarAST tree, boolean multiple){
        if (tree.getChild(0).getChild(0).getChild(0).getType() == 57){
            String name = tree.getChild(0).getChild(0).getChild(0).getText();
            return multiple ? pluralize(name) : name;
        } else if (tree.getChild(0).getChild(0).getChild(0).getType() == 46) {
            return tree.getChild(0).getChild(0).getChild(0).getChild(0).getText();
        } else {
            throw new RuntimeException("Name for field "+tree.getType());
        }
    }

    private static String fieldType(GrammarAST tree){
        if (tree.getChild(0).getChild(0).getChild(0).getType() == 57){
            return tree.getChild(0).getChild(0).getChild(0).getText();
        } else if (tree.getChild(0).getChild(0).getChild(0).getType() == 46) {
            return tree.getChild(0).getChild(0).getChild(0).getChild(1).getText();
        } else {
            throw new RuntimeException();
        }
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
            System.out.println("EOF FIELD " + tree);
            return Optional.empty();
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
