package com.github.javamodel.parsing;

import com.github.javamodel.Java8Lexer;
import com.github.javamodel.Java8Parser;
import com.github.javamodel.ast.filelevel.CompilationUnit;
import com.github.javamodel.ast.AstNode;
import com.github.javamodel.ast.filelevel.PackageDeclaration;
import com.github.javamodel.ast.typedecls.ClassDeclaration;
import org.antlr.v4.runtime.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ParserCli
{
    
    private Java8Parser.CompilationUnitContext getAntlrRoot(String code) {
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
        return ctx;
    }

    public AstNode parse(String code){
        Java8Parser.CompilationUnitContext ctx = getAntlrRoot(code);
        return CompilationUnit.NODE_TYPE.fromAntlrNode(ctx, null);
    }

    public static void main( String[] args ) throws IOException, NoSuchMethodException {
        String code = "package a.b.c; public class A { class B {class C {} } }";
        AstNode root = new ParserCli().parse(code);
        //StringBuffer stringBuffer = new StringBuffer();
        //NodeTree.printTree(root, "root", 0, stringBuffer);
        System.out.println("ROOT="+root);
        System.out.println("CompilationUnit node type="+CompilationUnit.NODE_TYPE);
        System.out.println("ClassDeclaration node type="+ ClassDeclaration.NODE_TYPE);
        System.out.println("PackageDeclaration node type="+ PackageDeclaration.NODE_TYPE);
    }
}
