package com.github.javamodel;

import com.github.javamodel.ast.CompilationUnit;
import org.antlr.v4.runtime.*;

import java.io.*;
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
        if (node.getContainer() != null){
            stringBuffer.append(node.getContainer().getName() +": ");
        } else {
            stringBuffer.append("<ROOT> ");
        }
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

    private static void explore(File dir) throws IOException {
        for (File child : dir.listFiles()){
            if (child.isDirectory()) {
                explore(child);
            } else if (child.isFile() && child.getName().endsWith(".java")){
                System.out.println("Explore "+child);
                String code = readFile(child);
                Node root = new ParserCli().parse(code);
                StringBuffer stringBuffer = new StringBuffer();
                printTree(root, 0, stringBuffer);
                System.out.println(stringBuffer.toString());
                explore(new File("src"));
            }
        }
    }

    private static String readFile( File file ) throws IOException {
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

    public static void main( String[] args ) throws IOException, NoSuchMethodException {
        String code = "class BinaryExpression<P extends Node> { }";
        Node root = new ParserCli().parse(code);
        StringBuffer stringBuffer = new StringBuffer();
        printTree(root, 0, stringBuffer);
        System.out.println(stringBuffer.toString());
        explore(new File("src"));
    }
}
