package com.github.javamodel;

import com.github.javamodel.ast.common.AnnotationUsageNode;
import com.github.javamodel.ast.common.ClassTypeRef;
import com.github.javamodel.ast.common.InterfaceTypeRef;
import com.github.javamodel.ast.common.Modifier;
import com.github.javamodel.ast.filelevel.CompilationUnit;
import com.github.javamodel.ast.filelevel.PackageDeclaration;
import com.github.javamodel.ast.reflection.Attribute;
import com.github.javamodel.ast.reflection.NodeType;
import com.github.javamodel.ast.reflection.Relation;
import com.github.javamodel.ast.typedecls.ClassDeclaration;
import com.github.javamodel.ast.typedecls.ClassElement;
import com.github.javamodel.ast.typedecls.TypeDeclaration;
import com.github.javamodel.ast.typedecls.TypeParameter;
import org.junit.Test;

import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for simple App.
 */
public class NodeTypeDerivationTest
{
    @Test
    public void forPackageDeclaration()
    {
        assertEquals(new NodeType.Builder("PackageDeclaration", PackageDeclaration.class)
                .addAttribute(Attribute.multiple("identifiers", String.class))
                .build(), PackageDeclaration.NODE_TYPE);
    }

    @Test
    public void forClassDeclaration()
    {
        assertEquals(new NodeType.Builder("ClassDeclaration", ClassDeclaration.class)
                .addRelation(Relation.multiple("annotations", AnnotationUsageNode.class))
                .addAttribute(Attribute.multiple("modifiers", Modifier.class))
                .addAttribute(Attribute.single("name", String.class))
                .addRelation(Relation.multiple("typeParameters", TypeParameter.class))
                .addRelation(Relation.single("superclass", ClassTypeRef.class))
                .addRelation(Relation.multiple("interfaces", InterfaceTypeRef.class))
                .addRelation(Relation.multiple("elements", ClassElement.class))
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
