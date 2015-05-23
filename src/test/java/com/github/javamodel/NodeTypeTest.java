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

    /*@Test
    public void parseClassWithFields()
    {
        assertEquals("root : CompilationUnit\n" +
                "  typeDeclaration : NormalClassDeclaration { Identifier=A }\n" +
                "    classElements : FieldDeclaration\n" +
                "      variableDeclaratorList : VariableDeclarator\n" +
                "        id : VariableDeclaratorId { Identifier=a }\n" +
                "      variableDeclaratorList : VariableDeclarator\n" +
                "        id : VariableDeclaratorId { Identifier=b }\n" +
                "      type : IntegralType { value=int }\n" +
                "    classElements : FieldDeclaration\n" +
                "      variableDeclaratorList : VariableDeclarator\n" +
                "        id : VariableDeclaratorId { Identifier=i }\n" +
                "      variableDeclaratorList : VariableDeclarator\n" +
                "        id : VariableDeclaratorId { Identifier=j }\n" +
                "      type : IntegralType { value=long }", toTreeString("class A { int a, b; long i, j;}"));
    }*/

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
