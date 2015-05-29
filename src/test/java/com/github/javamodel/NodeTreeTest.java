package com.github.javamodel;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by ftomassetti on 29/05/15.
 */
public class NodeTreeTest {
    
    private String toTree(String code){
        Node root = new ParserCli().parse(code);
        StringBuffer stringBuffer = new StringBuffer();
        ParserCli.printTree(root, 0, stringBuffer);
        return stringBuffer.toString().trim();
    }
    
    @Test
    public void testFieldDeclarations(){
        assertEquals("CompilationUnit{}\n" +
                "  ClassDeclaration{name='A'}\n" +
                "    FieldDeclaration{}\n" +
                "      PrimitiveTypeRef{INT}\n" +
                "      VariableDeclaration{name='a'}\n" +
                "      VariableDeclaration{name='b'}\n" +
                "      VariableDeclaration{name='c'}\n" +
                "    ClassDeclaration{name='B'}\n" +
                "      ClassDeclaration{name='C'}", toTree("class A { int a,b,c; class B { class C {} } }"));
        
    }

    @Test
    public void testInternalClasses(){
        assertEquals("CompilationUnit{}\n" +
                "  ClassDeclaration{name='A'}\n" +
                "    ClassDeclaration{name='B'}\n" +
                "      ClassDeclaration{name='C'}", toTree("class A { class B { class C {} }}"));

    }

    @Test
    public void testAddition(){
        assertEquals("CompilationUnit{}\n" +
                "  ClassDeclaration{name='A'}\n" +
                "    FieldDeclaration{}\n" +
                "      PrimitiveTypeRef{INT}\n" +
                "      VariableDeclaration{name='a'}\n" +
                "        ExpressionVariableInitializater{}\n" +
                "          BinaryExpression{operator=PLUS}\n" +
                "            IntegerLiteralExpression{text='1'}\n" +
                "            IntegerLiteralExpression{text='10'}", toTree("class A { int a = 1 + 10; }"));

    }

    @Test
    public void testMultiplication(){
        assertEquals("CompilationUnit{}\n" +
                "  ClassDeclaration{name='A'}\n" +
                "    FieldDeclaration{}\n" +
                "      PrimitiveTypeRef{INT}\n" +
                "      VariableDeclaration{name='a'}\n" +
                "        ExpressionVariableInitializater{}\n" +
                "          BinaryExpression{operator=MULT}\n" +
                "            IntegerLiteralExpression{text='1'}\n" +
                "            IntegerLiteralExpression{text='10'}", toTree("class A { int a = 1 * 10; }"));

    }

    @Test
    public void testNameReference(){
        assertEquals("CompilationUnit{}\n" +
                "  ClassDeclaration{name='A'}\n" +
                "    FieldDeclaration{}\n" +
                "      PrimitiveTypeRef{INT}\n" +
                "      VariableDeclaration{name='a'}\n" +
                "        ExpressionVariableInitializater{}\n" +
                "          NameReferenceExpression{name='b'}", toTree("class A { int a = b; }"));

    }

    @Test
    public void testSimplestClassMethod(){
        assertEquals("CompilationUnit{}\n" +
                "  ClassDeclaration{name='A'}\n" +
                "    MethodDeclaration{name='foo', modifiers=[]}\n" +
                "      VoidTypeRef{}\n" +
                "      BlockStatement{}", toTree("class A { void foo(){} }"));

    }
}
