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
}
