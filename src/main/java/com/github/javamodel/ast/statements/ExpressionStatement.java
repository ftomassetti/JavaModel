package com.github.javamodel.ast.statements;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.ast.SingleRelation;
import com.github.javamodel.ast.expressions.Expression;

/**
 * Created by federico on 31/05/15.
 */
public class ExpressionStatement extends Statement {
    private SingleRelation<ExpressionStatement, Expression> expression = new SingleRelation<ExpressionStatement, Expression>("expression", this);

    public static ExpressionStatement fromAntlrNode(Java8Parser.ExpressionStatementContext antlrNode){
        throw new UnsupportedOperationException();
    }
}
