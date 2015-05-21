package com.github.javamodel;

import org.antlr.v4.runtime.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ParserCli
{

    public Node parse(String code){
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
        Node root = Node.wrap(ctx);
        return root;
    }

    public static void main( String[] args ) throws IOException, NoSuchMethodException {
        String code = "class A { int a, b; long i, j;}";
        Node root = new ParserCli().parse(code);
        StringBuffer stringBuffer = new StringBuffer();
        NodeTree.printTree(root, "root", 0, stringBuffer);
        System.out.println(stringBuffer.toString());
    }
}
