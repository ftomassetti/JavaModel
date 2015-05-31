package com.github.javamodel.ast.statements;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.ast.SingleRelation;
import com.github.javamodel.ast.expressions.Expression;

/**
 * Created by federico on 31/05/15.
 */
public class ReturnStatement extends Statement {
    private SingleRelation<ReturnStatement, Expression> value = new SingleRelation<ReturnStatement, Expression>("value", this);

    public static ReturnStatement fromAntlrNode(Java8Parser.ReturnStatementContext antlrNode){
        ReturnStatement instance = new ReturnStatement();
        if (antlrNode.expression() != null) {
            instance.value.set(Expression.fromAntlrNode(antlrNode.expression()));
        }
        return instance;
    }

}
