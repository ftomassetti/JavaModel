package com.github.javamodel.ast.types;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.Node;

/**
 * Created by ftomassetti on 27/05/15.
 */
public class TypeRef extends Node {

    public static TypeRef fromAntlrNode(Java8Parser.ClassTypeContext antlrNode){
        TypeRef instance = new TypeRef();
        throw new UnsupportedOperationException();
    }

    public static TypeRef fromAntlrNode(Java8Parser.UnannTypeContext antlrNode) {
        if (antlrNode == null){
            throw new NullPointerException();
        }
        if (antlrNode.unannPrimitiveType() != null){
            return PrimitiveTypeRef.fromAntlrNode(antlrNode.unannPrimitiveType());
        } else {
            return fromAntlrNode(antlrNode.unannReferenceType());
        }
    }

    private static TypeRef fromAntlrNode(Java8Parser.UnannReferenceTypeContext antlrNode) {
        if (antlrNode.unannArrayType()!=null){
            return ArrayTypeRef.fromAntlrNode(antlrNode.unannArrayType());
        } else if (antlrNode.unannClassOrInterfaceType() != null){
            if (antlrNode.unannClassOrInterfaceType().unannClassType_lf_unannClassOrInterfaceType() != null && !antlrNode.unannClassOrInterfaceType().unannClassType_lf_unannClassOrInterfaceType().isEmpty()){
                throw new UnsupportedOperationException();
            } else if (antlrNode.unannClassOrInterfaceType().unannClassType_lfno_unannClassOrInterfaceType() != null) {
                return DeclaredTypeRef.fromAntlrNode(antlrNode.unannClassOrInterfaceType().unannClassType_lfno_unannClassOrInterfaceType());
            } else if (antlrNode.unannClassOrInterfaceType().unannInterfaceType_lf_unannClassOrInterfaceType() != null){
                throw new UnsupportedOperationException();
            } else if (antlrNode.unannClassOrInterfaceType().unannInterfaceType_lfno_unannClassOrInterfaceType() != null){
                throw new UnsupportedOperationException();
            } else {
                throw new UnsupportedOperationException();
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

}
