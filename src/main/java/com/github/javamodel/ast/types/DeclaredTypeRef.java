package com.github.javamodel.ast.types;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.ast.MultipleRelation;

/**
 * Created by federico on 29/05/15.
 */
public class DeclaredTypeRef extends TypeRef {

    private String name;
    private MultipleRelation<DeclaredTypeRef, TypeRef> typeArguments = new MultipleRelation<>("typeArguments", this);

    @Override
    public String toString() {
        return "DeclaredTypeRef{" +
                "name='" + name + '\'' +
                '}';
    }

    public static TypeRef fromAntlrNode(Java8Parser.ClassTypeContext antlrNode){
        if (antlrNode.classOrInterfaceType() != null){
            throw new UnsupportedOperationException();
        }
        DeclaredTypeRef instance = new DeclaredTypeRef();
        instance.name = antlrNode.Identifier().getText();
        if (antlrNode.typeArguments() != null){
            antlrNode.typeArguments().typeArgumentList().typeArgument().forEach((an) -> {
                if (an.referenceType() != null) {
                    instance.typeArguments.add(TypeRef.fromAntlrNode(an.referenceType()));
                } else if (an.wildcard() != null) {
                    instance.typeArguments.add(WildcardTypeArgument.fromAntlrNode(an.wildcard()));
                } else {
                    throw new UnsupportedOperationException();
                }
            });
        }
        return instance;
    }

    public static DeclaredTypeRef fromAntlrNode(Java8Parser.UnannClassType_lfno_unannClassOrInterfaceTypeContext antlrNode){
        DeclaredTypeRef instance = new DeclaredTypeRef();
        if (antlrNode.typeArguments() != null){
            throw new UnsupportedOperationException();
        }
        instance.name = antlrNode.Identifier().getText();
        return instance;
    }

    public static TypeRef fromAntlrNode(Java8Parser.ClassOrInterfaceTypeContext antlrNode) {
        if (antlrNode.classType_lf_classOrInterfaceType() != null && antlrNode.classType_lf_classOrInterfaceType().size() > 0) {
            if (antlrNode.classType_lf_classOrInterfaceType().size() == 1) {
                throw new UnsupportedOperationException();
            } else {
                throw new UnsupportedOperationException();
            }
        } else if (antlrNode.classType_lfno_classOrInterfaceType() != null) {
            return DeclaredTypeRef.fromAntlrNode(antlrNode.classType_lfno_classOrInterfaceType());
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private static TypeRef fromAntlrNode(Java8Parser.ClassType_lfno_classOrInterfaceTypeContext antlrNode) {
        DeclaredTypeRef instance = new DeclaredTypeRef();
        if (antlrNode.annotation() != null){
            if (antlrNode.annotation().size() >0){
                throw new UnsupportedOperationException();
            }
        }
        if (antlrNode.typeArguments() != null){
            throw new UnsupportedOperationException();
        }
        instance.name = antlrNode.Identifier().getText();
        return instance;
    }
}
