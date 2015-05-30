package com.github.javamodel.ast.types;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.ast.MultipleRelation;

/**
 * Created by federico on 29/05/15.
 */
public class DeclaredTypeRef extends TypeRef {

    private String name;
    private MultipleRelation<DeclaredTypeRef, TypeRef> typeArguments = new MultipleRelation<>(this);

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
        if (antlrNode.classType_lf_classOrInterfaceType() != null) {
            throw new UnsupportedOperationException();
        } else {
            throw new UnsupportedOperationException();
        }
    }
}
