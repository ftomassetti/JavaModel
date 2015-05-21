package com.github.javamodel;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ParserCli
{
    private static List<Class> transparentTypes = ImmutableList.of(Java8Parser.TypeDeclarationContext.class, Java8Parser.ClassDeclarationContext.class);

    @Data
    @AllArgsConstructor
    static class Relation {
        private Class<?> type;
        private boolean multiple;
        private String name;
        private Method method;
    }

    @Data
    static class NodeType {
        private List<Relation> relations = new LinkedList<>();
        private String name;

        public NodeType(String name){
            this.name = name;
        }
    }
    
    static <C extends ParserRuleContext> NodeType getNodeType(Class<C> ruleContextClass) throws NoSuchMethodException {
        if (!ruleContextClass.getSimpleName().endsWith("Context")){
            throw new RuntimeException("Unexpected name: "+ruleContextClass.getSimpleName());
        }
        NodeType nodeType = new NodeType(ruleContextClass.getSimpleName().substring(0, ruleContextClass.getSimpleName().length() - 7));
        for (Method method : ruleContextClass.getDeclaredMethods()) {
            Class<?> returnType = method.getReturnType();
            if (method.getName().equals("getRuleIndex")) {
            } else if (method.getParameterCount() != 0) {
            } else if ("void".equals(returnType.getTypeName())) {
            } else if (List.class.isAssignableFrom(returnType)) {
                // It return a list, but we cannot retrieve the type of the element. We need to find a method with the
                // same name but getting a parameter:
                // ex.
                // 		List<ImportDeclarationContext> importDeclaration()
                //      ImportDeclarationContext importDeclaration(int i)
                Method singleElementMethod = ruleContextClass.getDeclaredMethod(method.getName(), int.class);
                nodeType.getRelations().add(new Relation(singleElementMethod.getReturnType(), true, method.getName(), method));
            } else {
                if (returnType.getCanonicalName().equals(TerminalNode.class.getCanonicalName())) {
                    nodeType.getRelations().add(new Relation(returnType, false, method.getName(), method));
                } else if (ParserRuleContext.class.isAssignableFrom(returnType)){
                    nodeType.getRelations().add(new Relation(returnType, false, method.getName(), method));
                } else {
                    throw new RuntimeException("unexpected method " + method);
                }
            }
        }
        return nodeType;
    }
    
    static void printTree(ParserRuleContext ctx, int indentation) throws NoSuchMethodException {
        NodeType nodeType = getNodeType(ctx.getClass());
        boolean isTransparent = transparentTypes.contains(ctx.getClass());

        if (isTransparent) {
            for (int i = 0; i < ctx.getChildCount(); i++) {
                ParseTree child = ctx.getChild(i);
                if (child instanceof ParserRuleContext) {
                    printTree((ParserRuleContext) ctx.getChild(i), indentation);
                } else if (child instanceof TerminalNodeImpl) {
                    for (int j = 0; j < indentation; j++) System.out.print("  ");
                    TerminalNodeImpl terminalNode = (TerminalNodeImpl) child;
                    System.out.println(terminalNode.getText());
                } else {
                    for (int j = 0; j < indentation; j++) System.out.print("  ");
                    System.out.println("? " + ctx.getChild(i).getClass());
                }
            }
        } else {
            for (int j = 0; j < indentation; j++) System.out.print("  ");
            System.out.println("[" + nodeType.getName() + "]");
            for (int i = 0; i < ctx.getChildCount(); i++) {
                ParseTree child = ctx.getChild(i);
                if (child instanceof ParserRuleContext) {
                    printTree((ParserRuleContext) ctx.getChild(i), indentation + 1);
                } else if (child instanceof TerminalNodeImpl) {
                    for (int j = 0; j < indentation + 1; j++) System.out.print("  ");
                    TerminalNodeImpl terminalNode = (TerminalNodeImpl) child;
                    System.out.println(terminalNode.getText());
                } else {
                    for (int j = 0; j < indentation + 1; j++) System.out.print("  ");
                    System.out.println("? " + ctx.getChild(i).getClass());
                }
            }
        }
    }
    
    
    public static void main( String[] args ) throws IOException, NoSuchMethodException {
        String code = "class A {}";
        InputStream is = new ByteArrayInputStream(code.getBytes(StandardCharsets.UTF_8));
        Java8Lexer lexer = new Java8Lexer(new ANTLRInputStream(is));
        Java8Parser parser = new Java8Parser(new CommonTokenStream(lexer));
        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                throw new IllegalStateException("failed to parse at line " + line + " due to " + msg, e);
            }
        });
        Java8Parser.CompilationUnitContext ctx = parser.compilationUnit();
        printTree(ctx, 0);
    }
}
