package com.github.javamodel.generation;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.antlr.runtime.*;
import org.antlr.runtime.tree.Tree;
import org.antlr.v4.parse.*;
import org.antlr.v4.tool.ast.GrammarAST;

import java.io.*;
import java.util.*;

/**
 * Created by federico on 24/05/15.
 */
public class AstNodesGenerator {

    private static List<String> interestingNames = ImmutableList.of("floatingPointType");
    private static int T_BLOCK = 77;
    private static int T_ALT = 73;
    private static int T_STRING_LITERAL = 62;
    private static int T_TERMINAL = 66;
    private static int T_NON_TERMINAL = 57;
    private static int T_ADD_ASSIGN = 46;
    private static Map<String, GrammarAST> rules = new HashMap<>();
    private static Map<String, Object> declarations = new HashMap<String, Object>();
    private static Configuration freeMarkerConfiguration;

    private static String readFile(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");

        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }

        return stringBuilder.toString();
    }

    public static ANTLRParser.grammarSpec_return source(String source) throws IOException,
            RecognitionException {
        ANTLRStringStream in = new ANTLRStringStream(source);
        GrammarASTAdaptor re = new GrammarASTAdaptor(in);
        ANTLRLexer lexer = new ANTLRLexer(in);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        lexer.tokens = tokens;
        ANTLRParser p = new ANTLRParser(tokens);
        p.setTreeAdaptor(re);
        return p.grammarSpec();
    }

    public static void printTree(GrammarAST tree, int indentation) {
        if (tree == null) return;
        for (int i = 0; i < indentation; i++) System.out.print("  ");
        System.out.println(tree + " " + tree.getType());
        if (tree.getChildren() == null) return;
        for (Object child : tree.getChildren()) {
            if (child instanceof GrammarAST) {
                printTree((GrammarAST) child, indentation + 1);
            } else {
                System.out.println(" * " + child + " (" + child.getClass() + ")");
            }
        }
    }

    private static String stripQuotes(String s) {
        return s.substring(1, s.length() - 1);
    }

    public static String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1, s.length());
    }

    private static void processEnum(GrammarAST tree) throws IOException, TemplateException {
        System.out.println("Generating enum");
        List<Map<String, Object>> values = new ArrayList<>();
        for (int i = 0; i < tree.getChild(1).getChildCount(); i++) {
            Tree value = tree.getChild(1).getChild(i).getChild(0);
            values.add(ImmutableMap.of(
                    "name", stripQuotes(value.getText()),
                    "literal", stripQuotes(value.getText())
            ));
        }
        Map<String, Object> data = ImmutableMap.of(
                "name", ruleName(tree),
                "antlrNodeClass", ruleName(tree) + "Context",
                "values", values
        );
        Template template = freeMarkerConfiguration.getTemplate("EnumAstNode.ftl");
        StringWriter sw = new StringWriter();
        template.process(data, sw);
        System.out.println(sw.toString());
    }

    private static boolean isEnum(GrammarAST tree) {
        if (tree.getChildCount() != 2) return false;
        if (tree.getChild(1).getType() != T_BLOCK) return false;
        if (tree.getChild(1).getChildCount() < 2) return false;
        for (int i = 0; i < tree.getChild(1).getChildCount(); i++) {
            Tree child = tree.getChild(1).getChild(i);
            if (child.getType() != T_ALT) return false;
            if (child.getChildCount() != 1) return false;
            if (child.getChild(0).getType() != T_STRING_LITERAL) return false;
        }
        return true;
    }

    private static String ruleName(GrammarAST tree) {
        return tree.getChild(0).getText();
    }

    private static boolean isSingleElement(GrammarAST tree) {
        if (tree.getType() == T_BLOCK && tree.getChildCount() == 1 && tree.getChild(0).getType() == T_ALT){
            // We can ignore fields of type 62
            int fieldsCount = 0;
            for (int i=0;i<tree.getChild(0).getChildCount();i++){
                if (tree.getChild(0).getChild(i).getType() != T_STRING_LITERAL){
                    fieldsCount += 1;
                }
            }
            return fieldsCount == 1;
        } else {
            return false;
        }
    }

    private static boolean isSingleSequence(GrammarAST tree) {
        return tree.getType() == T_BLOCK && tree.getChildCount() == 1 && tree.getChild(0).getType() == T_ALT && tree.getChild(0).getChildCount() > 1;
    }

    private static boolean isAlternative(GrammarAST tree) {
        if (tree.getType() == T_BLOCK && tree.getChildCount() >= 1) {
            for (Object astObj : tree.getChildren()) {
                GrammarAST ast = (GrammarAST) astObj;
                if (ast.getType() != T_ALT) return false;
                if (ast.getChildCount() != 1) return false;
                if (ast.getChild(0).getType() != T_NON_TERMINAL && ast.getChild(0).getType() != T_STRING_LITERAL) return false;
            }
            return true;
        } else {
            return false;
        }
    }

    private static String fieldName(GrammarAST tree, boolean multiple) {
        // * 79
        //   BLOCK 77
        //     ALT 73
        //       packageModifier 57
        if (isSingleElement((GrammarAST) tree.getChild(0)) && tree.getChild(0).getChild(0).getChild(0).getType() == 57) {
            String name = tree.getChild(0).getChild(0).getChild(0).getText();
            return multiple ? pluralize(name) : name;
            // * 79
            //   BLOCK 77
            //     ALT 73
            //       += 46
            //         topTypes 28
            //         typeDeclaration 57
        } else if (isSingleElement((GrammarAST) tree.getChild(0)) && tree.getChild(0).getChild(0).getChild(0).getType() == 46) {
            return tree.getChild(0).getChild(0).getChild(0).getChild(0).getText();
        } else if (isSingleSequence((GrammarAST) tree.getChild(0))) {
            // if the sequence is made by two elements and the first one is just a string
            // like '.' or ',' we are just considering the second element
            GrammarAST alt = (GrammarAST) tree.getChild(0).getChild(0);
            if (alt.getChildCount() == 2 && alt.getChild(0).getType() == 62) {
                if (alt.getChild(1).getType() == T_ADD_ASSIGN){
                    return alt.getChild(1).getChild(0).getText();
                } else {
                    return alt.getChild(1).getText();
                }
            }
        }
        printTree(tree, 0);
        throw new RuntimeException("Name for field " + tree.getType());
    }

    private static String fieldType(GrammarAST tree) {
        if (isSingleElement((GrammarAST) tree.getChild(0)) && tree.getChild(0).getChild(0).getChild(0).getType() == 57) {
            return tree.getChild(0).getChild(0).getChild(0).getText();
        } else if (isSingleElement((GrammarAST) tree.getChild(0)) && tree.getChild(0).getChild(0).getChild(0).getType() == 46) {
            return tree.getChild(0).getChild(0).getChild(0).getChild(1).getText();
        } else if (isSingleSequence((GrammarAST) tree.getChild(0))) {
            // if the sequence is made by two elements and the first one is just a string
            // like '.' or ',' we are just considering the second element
            GrammarAST alt = (GrammarAST) tree.getChild(0).getChild(0);
            if (alt.getChildCount() == 2 && alt.getChild(0).getType() == 62) {
                if (alt.getChild(1).getType() == T_ADD_ASSIGN){
                    return alt.getChild(1).getChild(1).getText();
                } else {
                    return alt.getChild(1).getText();
                }
            }
        }
        throw new RuntimeException();

    }

    private static String pluralize(String name) {
        return name + "s";
    }

    private static Optional<Map<String, Object>> processField(GrammarAST tree) {
        //System.out.println("PROCESSING FIELD "+tree);
        //printTree(tree, 3);
        if (tree.getType() == 88) {
            String name = fieldName(tree, false);
            //System.out.println("Optional FIELD "+name);
            return Optional.of(ImmutableMap.of(
                    "name", name,
                    "type", fieldType(tree),
                    "multiple", false
            ));
        } else if (tree.getType() == 79) {
            String name = fieldName(tree, true);
            System.out.println("Multiplesss FIELD " + name);
            printTree(tree, 10);
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
        } else if (tree.getType() == T_STRING_LITERAL) {
            return Optional.empty();
        } else if (tree.getType() == T_NON_TERMINAL) {
            return Optional.of(ImmutableMap.of(
                    "name", tree.getText(),
                    "type", tree.getText(),
                    "multiple", false
            ));
        } else if (tree.getType() == T_ADD_ASSIGN) {
            //System.out.println("ADD ASSIGN");
            //printTree(tree, 0);
            return processField((GrammarAST)tree.getChild(1));
        } else {
            printTree(tree, 0);
            throw new RuntimeException("type is " + tree.getType());
        }
    }

    private static ClassDef processSequence(String name, GrammarAST nonTerminalTree) throws IOException, TemplateException {
        List<Map<String, Object>> fields = new ArrayList<>();
        // We expect to have just children in the form BLOCK / ALT
        for (int i = 0; i < nonTerminalTree.getChild(0).getChildCount(); i++) {
            processField((GrammarAST) nonTerminalTree.getChild(0).getChild(i)).ifPresent(
                    (field) -> {
                        if (!name.equals(field.get("type"))) {
                            try {
                                processDeclaration((String) field.get("type"));
                            } catch (IOException | TemplateException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        fields.add(field);
                    }
            );
        }

        ClassDef classDef = new ClassDef(name, name + "Context", fields);
        declarations.put(name, classDef);
        generateTemplate("AstNode.ftl", classDef.toMap());
        return classDef;
    }

    private static InterfaceDef processAlternative(String name, GrammarAST nonTerminalTree) throws IOException, TemplateException {
        //List<String> fields = new ArrayList<>();
        //printTree(nonTerminalTree, 0);
        // We expect to have just children in the form BLOCK / ALT
        for (int i = 0; i < nonTerminalTree.getChildCount(); i++) {
            if (T_NON_TERMINAL == nonTerminalTree.getChild(i).getChild(0).getType()) {
                String element = nonTerminalTree.getChild(i).getChild(0).getText();
                try {
                    processDeclaration(element);
                } catch (IOException | TemplateException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        InterfaceDef interfaceDef = new InterfaceDef(name, name + "Context");
        declarations.put(name, interfaceDef);
        generateTemplate("InterfaceNode.ftl", interfaceDef.toMap());
        return interfaceDef;
    }

    private static void generateTemplate(String templateName, Object data) throws IOException, TemplateException {
        Template template = freeMarkerConfiguration.getTemplate(templateName);
        StringWriter sw = new StringWriter();
        template.process(data, sw);
        //System.out.println(sw.toString());
    }

    public static void processGrammar(GrammarAST tree, String startingSymbol) throws IOException, TemplateException {
        //printTree(tree, 0);

        GrammarAST rulesNode = (GrammarAST) tree.getChild(2);
        for (Object ruleNodeObj : rulesNode.getChildren()) {
            GrammarAST ruleNode = (GrammarAST) ruleNodeObj;
            String ruleName = ruleNode.getChild(0).getText();
            int ruleType = ruleNode.getChild(0).getType();
            //System.out.println(ruleName +" "+ruleType);
            rules.put(ruleName, ruleNode);
        }

        processDeclaration(startingSymbol);
    }

    private static boolean isTransparent(GrammarAST declaration) {
        return isSingleElement(declaration);
    }

    private static Object processDeclaration(String symbol) throws IOException, TemplateException {
        if (declarations.containsKey(symbol)) {
            return declarations.get(symbol);
        }
        try {
            System.out.println("SYMBOL " + symbol);
            GrammarAST declaration = rules.get(symbol);
            //printTree(declaration, 0);

            int type = declaration.getChild(0).getType();
            if (type == T_NON_TERMINAL) {
                GrammarAST block = (GrammarAST) declaration.getChild(1);
                if (isSingleSequence(block)) {
                    System.out.println("  single sequence");
                    return processSequence(symbol, block);
                } else if (isTransparent(block)) {
                    System.out.println("  transparent");
                    String aliasedName = block.getChild(0).getChild(0).getText();
                    declarations.put(ruleName(declaration), declarations.get(aliasedName));
                    return declarations.get(ruleName(declaration));
                } else if (isAlternative(block)) {
                    System.out.println("  interface");
                    return processAlternative(symbol, block);
                } else {
                    throw new RuntimeException("Do not know how to process declaration " + symbol);
                }
            } else if (type == T_TERMINAL) {
                Terminal terminal = new Terminal(symbol);
                declarations.put(symbol, terminal);
                return terminal;
            } else {
                throw new RuntimeException("Do not know how to process declaration " + symbol + " : type " + type);
            }
        } catch (RuntimeException e){
            throw new RuntimeException("processDeclaration "+symbol, e);
        }
    }

    public static void main(String[] args) throws IOException, RecognitionException, TemplateException {
        freeMarkerConfiguration = new Configuration();
        freeMarkerConfiguration.setTemplateLoader(new ClassTemplateLoader(AstNodesGenerator.class, "/"));

        String antlrCode = readFile("src/main/antlr4/com/github/javamodel/Java8.g4");
        ANTLRParser.grammarSpec_return result = source(antlrCode);
        processGrammar(result.getTree(), "compilationUnit");
    }

}
