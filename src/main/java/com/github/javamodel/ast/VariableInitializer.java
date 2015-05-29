package com.github.javamodel.ast;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.Node;

/**
 * Created by ftomassetti on 29/05/15.
 */
public abstract class VariableInitializer extends Node {

    public static VariableInitializer fromAntlrNode(Java8Parser.VariableInitializerContext antlrNode){
        if (antlrNode.arrayInitializer() != null){
            return ArrayInitializer.fromAntlrNode(antlrNode.arrayInitializer());
        } else if (antlrNode.expression() != null){
            return ExpressionVariableInitializater.fromAntlrNode(antlrNode.expression());
        } else {
            throw new UnsupportedOperationException();
        }
    }
}
