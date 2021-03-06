package com.github.javamodel.ast;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.ast.types.DeclaredTypeRef;
import com.github.javamodel.ast.types.TypeRef;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ftomassetti on 27/05/15.
 */
public class ClassDeclaration extends TypeDeclaration {
    private final MultipleRelation<ClassDeclaration, AnnotationUsage> annotations = new MultipleRelation<>("annotations", this);
    private List<Modifier> modifiers = new ArrayList<>();
    private String name;
    private final MultipleRelation<ClassDeclaration, TypeParameter> typeParameters = new MultipleRelation<>("typeParameters", this);
    private final SingleRelation<ClassDeclaration, TypeRef> superclass = new SingleRelation<>("superclass", this);
    private final MultipleRelation<ClassDeclaration, TypeRef> interfaces = new MultipleRelation<>("interfaces", this);
    private final MultipleRelation<ClassDeclaration, FieldDeclaration> fields = new MultipleRelation<>("fields", this);
    private final MultipleRelation<ClassDeclaration, MethodDeclaration> methods = new MultipleRelation<>("methods", this);
    private final MultipleRelation<ClassDeclaration, TypeDeclaration> internalTypes = new MultipleRelation<>("internalTypes", this);

    @Override
    public String toString() {
        return "ClassDeclaration{" +
                "modifiers=" + modifiers +
                ", name='" + name + '\'' +
                '}';
    }

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
            instance.superclass.set(DeclaredTypeRef.fromAntlrNode(antlrNode.superclass().classType()));
        }
        if (antlrNode.superinterfaces() != null) {
            antlrNode.superinterfaces().interfaceTypeList().interfaceType().forEach((an) -> instance.interfaces.add(DeclaredTypeRef.fromAntlrNode(an.classType())));
        }
        
        
        antlrNode.classBody().classBodyDeclaration().forEach((an)->{
            if (an.instanceInitializer() != null){
                // TODO consider instance initializers        
            } else if (an.staticInitializer() != null) {
                // TODO consider static initializers        
            } else if (an.constructorDeclaration() != null) {
                // TODO consider constructors
            } else {
                if (an.classMemberDeclaration().classDeclaration() != null){
                    instance.internalTypes.add(TypeDeclaration.fromAntlrNode(an.classMemberDeclaration().classDeclaration()));
                } else if (an.classMemberDeclaration().fieldDeclaration() != null){
                    instance.fields.add(FieldDeclaration.fromAntlrNode(an.classMemberDeclaration().fieldDeclaration()));
                } else if (an.classMemberDeclaration().interfaceDeclaration() != null) {
                    instance.internalTypes.add(InterfaceDeclaration.fromAntlrNode(an.classMemberDeclaration().interfaceDeclaration()));
                } else if (an.classMemberDeclaration().methodDeclaration() != null) {
                    instance.methods.add(MethodDeclaration.fromAntlrNode(an.classMemberDeclaration().methodDeclaration()));
                }
            }
        });
        
        return instance;
    }
}
