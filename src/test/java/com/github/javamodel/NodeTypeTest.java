package com.github.javamodel;

import com.github.javamodel.ast.AstNode;
import com.github.javamodel.parsing.NodeTree;
import com.github.javamodel.parsing.ParserCli;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */
public class NodeTypeTest
{
    private String toTreeString(String code){
        AstNode root = new ParserCli().parse(code);
        return NodeTree.treeString(root, "root", 0).trim();
    }

    @Test
    public void parseClassWithFields()
    {
        assertEquals("root : CompilationUnit\n" +
                "  topTypes : ClassDeclaration { modifiers=[] name=[A] }\n" +
                "    elements : FieldDeclaration { modifiers=[] }\n" +
                "      type : PrimitiveTypeRef { primitiveType=[INT] }\n" +
                "      variables : VariableDeclarator { name=[a] }\n" +
                "      variables : VariableDeclarator { name=[b] }\n" +
                "    elements : FieldDeclaration { modifiers=[] }\n" +
                "      type : PrimitiveTypeRef { primitiveType=[LONG] }\n" +
                "      variables : VariableDeclarator { name=[i] }\n" +
                "      variables : VariableDeclarator { name=[j] }", toTreeString("class A { int a, b; long i, j;}"));
    }

    @Test
    public void parseInternalClasses()
    {
        assertEquals("root : CompilationUnit\n" +
                "  topTypes : ClassDeclaration { modifiers=[] name=[A] }\n" +
                "    elements : ClassDeclaration { modifiers=[] name=[B] }\n" +
                "      elements : ClassDeclaration { modifiers=[] name=[C] }", toTreeString("class A { class B {class C {} } }"));
    }

    @Test
    public void parseSimplestClass()
    {
        assertEquals("root : CompilationUnit\n" +
                "  topTypes : ClassDeclaration { modifiers=[] name=[A] }", toTreeString("class A { }"));
    }
}
