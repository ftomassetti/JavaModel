package com.github.javamodel.ast.types;

import com.github.javamodel.Java8Parser;

/**
 * Created by federico on 29/05/15.
 */
public class DeclaredTypeRef extends TypeRef {

    private String name;

    public static DeclaredTypeRef fromAntlrNode(Java8Parser.UnannClassType_lfno_unannClassOrInterfaceTypeContext antlrNode){
        DeclaredTypeRef instance = new DeclaredTypeRef();
        if (antlrNode.typeArguments() != null){
            throw new UnsupportedOperationException();
        }
        instance.name = antlrNode.Identifier().getText();
        return instance;
    }
}
