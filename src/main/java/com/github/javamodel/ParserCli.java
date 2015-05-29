package com.github.javamodel;

import com.github.javamodel.ast.CompilationUnit;
import org.antlr.v4.runtime.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ParserCli {

    public CompilationUnit parse(String code) {
        InputStream is = new ByteArrayInputStream(code.getBytes(StandardCharsets.UTF_8));
        Java8Lexer lexer = null;
        try {
            lexer = new Java8Lexer(new ANTLRInputStream(is));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Java8Parser parser = new Java8Parser(new CommonTokenStream(lexer));
        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                throw new IllegalStateException("failed to parse at line " + line + " due to " + msg, e);
            }
        });
        Java8Parser.CompilationUnitContext ctx = parser.compilationUnit();
        CompilationUnit compilationUnit = CompilationUnit.fromAntlrNode(ctx);
        return compilationUnit;
    }

    public static void printTree(Node node, int indentation, StringBuffer stringBuffer) {
        if (node == null) return;
        for (int j = 0; j < indentation; j++) stringBuffer.append("  ");
        stringBuffer.append(node);
        stringBuffer.append("\n");
        for (Node child : node.getChildren()) {
            printTree(child, indentation + 1, stringBuffer);
        }
    }

    public static String treeString(Node node, int indentation) {
        StringBuffer sb = new StringBuffer();
        printTree(node,  indentation, sb);
        return sb.toString();
    }

    public static void main( String[] args ) throws IOException, NoSuchMethodException {
        String code = "class A { int a = 1 + 10; }";
        Node root = new ParserCli().parse(code);
        StringBuffer stringBuffer = new StringBuffer();
        printTree(root, 0, stringBuffer);
        System.out.println(stringBuffer.toString());
    }
}
