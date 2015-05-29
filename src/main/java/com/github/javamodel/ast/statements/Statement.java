package com.github.javamodel.ast.statements;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.Node;

/**
 * Created by federico on 29/05/15.
 */
public abstract class Statement extends Node {
    public static Statement fromAntlrNode(Java8Parser.BlockStatementContext s) {
        if (s.classDeclaration() != null){
            throw new UnsupportedOperationException();
        } else if (s.localVariableDeclarationStatement() != null){
            throw new UnsupportedOperationException();
        } else if (s.statement() != null){
            throw new UnsupportedOperationException();
        } else {
            throw new UnsupportedOperationException();
        }
    }
}
