package com.github.javamodel.ast;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.Node;

/**
 * Created by ftomassetti on 28/05/15.
 */
public class VariableDeclaration extends Node {
    
    private String name;
    private final SingleRelation<VariableDeclaration, VariableInitializer> initializer = new SingleRelation<VariableDeclaration, VariableInitializer>("initializer", this);

    @Override
    public String toString() {
        return "VariableDeclaration{" +
                "name='" + name + '\'' +
                '}';
    }

    public static VariableDeclaration fromAntlrNode(Java8Parser.VariableDeclaratorContext antlrNode) {
        VariableDeclaration instance = new VariableDeclaration();
        if (antlrNode.variableDeclaratorId().dims() != null){
            throw new UnsupportedOperationException();
        }
        instance.name = antlrNode.variableDeclaratorId().Identifier().getText();
        if (antlrNode.variableInitializer() != null){
            instance.initializer.set(VariableInitializer.fromAntlrNode(antlrNode.variableInitializer()));
        }
        return instance;
    }
}
