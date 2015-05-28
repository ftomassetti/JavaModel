package com.github.javamodel.ast;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.Node;

/**
 * Created by ftomassetti on 28/05/15.
 */
public class VariableDeclaration extends Node {
    
    private String name;

    public static VariableDeclaration fromAntlrNode(Java8Parser.VariableDeclaratorContext antlrNode) {
        VariableDeclaration instance = new VariableDeclaration();
        if (antlrNode.variableDeclaratorId().dims() != null){
            throw new UnsupportedOperationException();
        }
        instance.name = antlrNode.variableDeclaratorId().Identifier().getText();
        if (antlrNode.variableInitializer() != null){
            throw new UnsupportedOperationException();
        }
        return instance;
    }
}
