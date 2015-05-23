package com.github.javamodel;

import com.github.javamodel.ast.common.*;
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
    public void forAnnotationUsageNode()
    {
        assertEquals(new NodeType.Builder("AnnotationUsageNode", AnnotationUsageNode.class)
                .addAttribute(Attribute.single("typeName", String.class))
                .build(), AnnotationUsageNode.NODE_TYPE);
    }

    @Test
    public void forMarkerAnnotationUsage()
    {
        assertEquals(new NodeType.Builder("MarkerAnnotationUsage", MarkerAnnotationUsage.class)
                .addAttribute(Attribute.single("typeName", String.class))
                .build(), MarkerAnnotationUsage.NODE_TYPE);
    }

    @Test
    public void forSingleElementAnnotationUsage()
    {
        assertEquals(new NodeType.Builder("SingleElementAnnotationUsage", SingleElementAnnotationUsage.class)
                .addAttribute(Attribute.single("typeName", String.class))
                .addRelation(Relation.single("value", AnnotationValue.class))
                .build(), SingleElementAnnotationUsage.NODE_TYPE);
    }

    @Test
    public void forMultipleElementsAnnotationUsage()
    {
        assertEquals(new NodeType.Builder("MultipleElementsAnnotationUsage", MultipleElementsAnnotationUsage.class)
                .addAttribute(Attribute.single("typeName", String.class))
                .addRelation(Relation.multiple("values", AnnotationValuePair.class))
                .build(), MultipleElementsAnnotationUsage.NODE_TYPE);
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
