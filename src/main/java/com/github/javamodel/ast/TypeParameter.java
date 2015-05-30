package com.github.javamodel.ast;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.Node;

/**
 * Created by ftomassetti on 27/05/15.
 */
public class TypeParameter extends Node {

    private String name;
    private MultipleRelation<TypeParameter, TypeBound> bounds = new MultipleRelation<>("bounds", this);

    @Override
    public String toString() {
        return "TypeParameter{" +
                "name='" + name + '\'' +
                '}';
    }

    public static TypeParameter fromAntlrNode(Java8Parser.TypeParameterContext antlrNode){
        TypeParameter instance = new TypeParameter();
        if (antlrNode.typeBound() != null){
            if (antlrNode.typeBound().additionalBound() != null && antlrNode.typeBound().additionalBound().size() > 0){
                throw new UnsupportedOperationException();
            }
            if (antlrNode.typeBound().typeVariable() != null){
                instance.bounds.add(TypeBound.fromAntlrNode(antlrNode.typeBound().typeVariable()));
            } else if (antlrNode.typeBound().classOrInterfaceType() != null){
                throw new UnsupportedOperationException();
            } else {
                throw new UnsupportedOperationException();
            }
        }
        if (antlrNode.typeParameterModifier()!=null && antlrNode.typeParameterModifier().size()>0){
            throw new UnsupportedOperationException();
        }
        instance.name = antlrNode.Identifier().getText();
        return instance;
    }
}
