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
            return LocalVariableDeclarationStatement.fromAntlrNode(s.localVariableDeclarationStatement());
        } else if (s.statement() != null){
            return Statement.fromAntlrNode(s.statement());
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private static Statement fromAntlrNode(Java8Parser.StatementContext antlrNode) {
        if (antlrNode.forStatement() != null){
            throw new UnsupportedOperationException();
        } else if (antlrNode.ifThenElseStatement() != null){
            throw new UnsupportedOperationException();
        } else if (antlrNode.ifThenStatement() != null){
            throw new UnsupportedOperationException();
        } else if (antlrNode.labeledStatement() != null){
            throw new UnsupportedOperationException();
        } else if (antlrNode.statementWithoutTrailingSubstatement() != null){
            return Statement.fromAntlrNode(antlrNode.statementWithoutTrailingSubstatement());
        } else if (antlrNode.whileStatement() != null){
            throw new UnsupportedOperationException();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private static Statement fromAntlrNode(Java8Parser.StatementWithoutTrailingSubstatementContext antlrNode) {
        if (antlrNode.assertStatement() != null){
            throw new UnsupportedOperationException();
        } else if (antlrNode.block() != null){
            throw new UnsupportedOperationException();
        } else if (antlrNode.breakStatement() != null){
            throw new UnsupportedOperationException();
        } else if (antlrNode.continueStatement() != null){
            throw new UnsupportedOperationException();
        } else if (antlrNode.doStatement() != null){
            throw new UnsupportedOperationException();
        } else if (antlrNode.emptyStatement() != null){
            throw new UnsupportedOperationException();
        } else if (antlrNode.expressionStatement() != null){
            throw new UnsupportedOperationException();
        } else if (antlrNode.returnStatement() != null) {
            throw new UnsupportedOperationException();
        } else if (antlrNode.switchStatement() != null){
            throw new UnsupportedOperationException();
        } else if (antlrNode.synchronizedStatement() != null){
            throw new UnsupportedOperationException();
        } else if (antlrNode.throwStatement() != null) {
            throw new UnsupportedOperationException();
        } else if (antlrNode.tryStatement() != null) {
            throw new UnsupportedOperationException();
        } else {
            throw new UnsupportedOperationException();
        }
    }

}
