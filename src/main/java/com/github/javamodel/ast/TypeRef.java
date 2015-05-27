package com.github.javamodel.ast;

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
}
