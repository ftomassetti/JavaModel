package com.github.javamodel.ast.statements;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.ast.SingleRelation;
import com.github.javamodel.ast.expressions.Expression;
import com.github.javamodel.ast.expressions.MethodInvocation;

/**
 * Created by federico on 31/05/15.
 */
public class ExpressionStatement extends Statement {
    private SingleRelation<ExpressionStatement, Expression> expression = new SingleRelation<ExpressionStatement, Expression>("expression", this);

    public static ExpressionStatement fromAntlrNode(Java8Parser.ExpressionStatementContext antlrNode){
        ExpressionStatement instance = new ExpressionStatement();
        if (antlrNode.statementExpression().assignment() != null){
            throw new UnsupportedOperationException();
        } else if (antlrNode.statementExpression().classInstanceCreationExpression() != null){
            throw new UnsupportedOperationException();
        } else if (antlrNode.statementExpression().methodInvocation() != null){
            instance.expression.set(MethodInvocation.fromAntlrNode(antlrNode.statementExpression().methodInvocation()));
        } else if (antlrNode.statementExpression().postDecrementExpression() != null){
            throw new UnsupportedOperationException();
        } else if (antlrNode.statementExpression().postIncrementExpression() != null){
            throw new UnsupportedOperationException();
        } else if (antlrNode.statementExpression().preDecrementExpression() != null){
            throw new UnsupportedOperationException();
        } else if (antlrNode.statementExpression().preIncrementExpression() != null){
            throw new UnsupportedOperationException();
        } else {
            throw new UnsupportedOperationException();
        }
        return instance;
    }
}
