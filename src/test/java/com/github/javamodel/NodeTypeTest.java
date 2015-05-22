package com.github.javamodel;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */
public class NodeTypeTest
{
    private String toTreeString(String code){
        //NodeOld root = new ParserCli().parse(code);
        //return NodeTree.treeString(root, "root", 0).trim();
        return null;
    }

    @Test
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
    }

    @Test
    public void parseInternalClasses()
    {
        assertEquals("root : CompilationUnit\n" +
                "  typeDeclaration : NormalClassDeclaration { Identifier=A }\n" +
                "    classElements : NormalClassDeclaration { Identifier=B }\n" +
                "      classElements : NormalClassDeclaration { Identifier=C }", toTreeString("class A { class B {class C {} } }"));
    }
}
