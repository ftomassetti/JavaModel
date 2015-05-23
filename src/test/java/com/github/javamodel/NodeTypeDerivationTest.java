package com.github.javamodel;

import com.github.javamodel.ast.common.AnnotationUsageNode;
import com.github.javamodel.ast.common.Modifier;
import com.github.javamodel.ast.filelevel.CompilationUnit;
import com.github.javamodel.ast.filelevel.PackageDeclaration;
import com.github.javamodel.ast.reflection.Attribute;
import com.github.javamodel.ast.reflection.NodeType;
import com.github.javamodel.ast.reflection.Relation;
import com.github.javamodel.ast.typedecls.ClassDeclaration;
import com.github.javamodel.ast.typedecls.TypeDeclaration;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for simple App.
 */
public class NodeTypeDerivationTest
{
    @Test
    public void forClassDeclaration()
    {
        assertEquals(new NodeType.Builder("ClassDeclaration", ClassDeclaration.class)
                .addRelation(Relation.multiple("annotations", AnnotationUsageNode.class))
                .addAttribute(Attribute.multiple("modifiers", Modifier.class))
                .build(), ClassDeclaration.NODE_TYPE);
    }

    @Test
    public void forCompilationUnit()
    {
        assertEquals(new NodeType.Builder("CompilationUnit", CompilationUnit.class)
                .addRelation(Relation.single("packageDeclaration", PackageDeclaration.class))
                .addRelation(Relation.multiple("topTypesDeclarations", TypeDeclaration.class))
                .build(), CompilationUnit.NODE_TYPE);
    }


}
