package com.github.javamodel.ast;

import com.github.javamodel.Java8Parser;
import com.sun.org.apache.xpath.internal.operations.Mult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ftomassetti on 27/05/15.
 */
public class ClassDeclaration extends TypeDeclaration {
    private final MultipleRelation<ClassDeclaration, AnnotationUsage> annotations = new MultipleRelation<>(this);
    private List<Modifier> modifiers = new ArrayList<>();
    private String name;
    private final MultipleRelation<ClassDeclaration, TypeParameter> typeParameters = new MultipleRelation<>(this);
    private final SingleRelation<ClassDeclaration, TypeRef> superclass = new SingleRelation<>(this);
    private final MultipleRelation<ClassDeclaration, TypeRef> interfaces = new MultipleRelation<>(this);

    public static ClassDeclaration fromAntlrNode(Java8Parser.NormalClassDeclarationContext antlrNode) {
        ClassDeclaration instance = new ClassDeclaration();
        antlrNode.classModifier().forEach((cm)-> {
            if (null == cm.annotation()){
                instance.modifiers.add(Modifier.fromAntlrNode(cm));    
            } else {
                instance.annotations.add(AnnotationUsage.fromAntlrNode(cm.annotation()));
            }
        });
        instance.name = antlrNode.Identifier().getText();
        if (antlrNode.typeParameters() != null && antlrNode.typeParameters().typeParameterList() != null) {
            antlrNode.typeParameters().typeParameterList().typeParameter().forEach((an) -> instance.typeParameters.add(TypeParameter.fromAntlrNode(an)));
        }
        if (antlrNode.superclass() != null) {
            instance.superclass.set(TypeRef.fromAntlrNode(antlrNode.superclass().classType()));
        }
        if (antlrNode.superinterfaces() != null) {
            antlrNode.superinterfaces().interfaceTypeList().interfaceType().forEach((an) -> instance.interfaces.add(TypeRef.fromAntlrNode(an.classType())));
        }
        return instance;
    }
}
