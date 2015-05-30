package com.github.javamodel.ast;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.Node;

/**
 * Created by federico on 30/05/15.
 */
public class TypeBound extends Node {
    private String name;

    @Override
    public String toString() {
        return "TypeBound{" +
                "name='" + name + '\'' +
                '}';
    }

    public static TypeBound fromAntlrNode(Java8Parser.TypeVariableContext antlrNode) {
        if (antlrNode.annotation() != null && antlrNode.annotation().size() > 0){
            throw new UnsupportedOperationException();
        }
        TypeBound instance = new TypeBound();
        instance.name = antlrNode.Identifier().getText();
        return instance;
    }
}
