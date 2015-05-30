package com.github.javamodel.ast.types;

import com.github.javamodel.Java8Parser;

/**
 * Created by federico on 29/05/15.
 */
public class DeclaredTypeRef extends TypeRef {

    private String name;

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
}
