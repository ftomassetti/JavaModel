package com.github.javamodel;

import org.antlr.v4.runtime.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ParserCli
{
    static void printTree(Node node, int indentation) throws NoSuchMethodException {

        for (int j = 0; j < indentation; j++) System.out.print("  ");
        System.out.print("[" + node.getNodeType().getName());
        for (Attribute attribute : node.getNodeType().getAttributes()){
            System.out.print(" "+attribute.getName()+"="+node.getSingleValue(attribute));
        }
        System.out.println("]");
        for (Node child : node.getAllChildren()){
           printTree(child, indentation + 1);
        }
    }

    public static void main( String[] args ) throws IOException, NoSuchMethodException {
        String code = "class A { int a; }";
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
        Node root = Node.wrap(ctx);
        printTree(root, 0);
    }
}
