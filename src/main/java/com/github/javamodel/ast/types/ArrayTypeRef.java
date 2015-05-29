package com.github.javamodel.ast.types;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.Node;
import com.github.javamodel.ast.types.TypeRef;

/**
 * Created by ftomassetti on 27/05/15.
 */
public class ArrayTypeRef extends Node {
    public static TypeRef fromAntlrNode(Java8Parser.UnannArrayTypeContext antlrNode) {
        throw new UnsupportedOperationException();
    }
}
