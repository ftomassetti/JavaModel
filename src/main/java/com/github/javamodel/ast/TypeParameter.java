package com.github.javamodel.ast;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.Node;

/**
 * Created by ftomassetti on 27/05/15.
 */
public class TypeParameter extends Node {

    public static TypeParameter fromAntlrNode(Java8Parser.TypeParameterContext antlrNode){
        TypeParameter instance = new TypeParameter();
        throw new UnsupportedOperationException();
    }
}
