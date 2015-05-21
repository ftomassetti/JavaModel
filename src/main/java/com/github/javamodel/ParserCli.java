package com.github.javamodel;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ParserCli
{
    static class NodeType {
        
    }
    
    static <C extends ParserRuleContext> NodeType getNodeType(Class<C> ruleContextClass) {
        NodeType nodeType = new NodeType();
        System.out.println("class "+ruleContextClass);
        for (Method method : ruleContextClass.getDeclaredMethods()){
            Class<?> returnType = method.getReturnType();
            if ("void".equals(returnType.getTypeName())) {
                //System.out.println("  VOID");
            } else if (List.class.isAssignableFrom(returnType)){
                System.out.println("* "+method);
                System.out.println("  LIST");
            } else {
                System.out.println("* "+method);
                if (returnType.getCanonicalName().equals(TerminalNode.class.getCanonicalName())){
                    System.out.println("  SINGLE TERMINAL");
                    
                } else {
                    System.out.println("  NOT LIST " +returnType.getTypeName());
                }
            }
        }
        return nodeType;
    }
    
    static void printTree(ParserRuleContext ctx, int indentation){
        NodeType nodeType = getNodeType(ctx.getClass());
        
        for (int j=0; j<indentation; j++) System.out.print("  ");
        System.out.println("[" + ctx.getClass().getSimpleName() + "]");
        for (int i=0; i<ctx.getChildCount(); i++){
            ParseTree child = ctx.getChild(i);
            if (child instanceof ParserRuleContext) {
                printTree((ParserRuleContext) ctx.getChild(i), indentation + 1);
            } else if (child instanceof TerminalNodeImpl) {
                for (int j=0; j<indentation+1; j++) System.out.print("  ");
                TerminalNodeImpl terminalNode = (TerminalNodeImpl)child;
                System.out.println(terminalNode.getText());
            } else {
                for (int j=0; j<indentation+1; j++) System.out.print("  ");
                System.out.println("? " +ctx.getChild(i).getClass());
            }
        }
    }
    
    
    public static void main( String[] args ) throws IOException {
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
