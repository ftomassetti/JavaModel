package com.github.javamodel.ast;

import com.github.javamodel.Java8Parser;

/**
 * Created by ftomassetti on 29/05/15.
 */
public class ExpressionVariableInitializater extends VariableInitializer {
    
    private final SingleRelation<ExpressionVariableInitializater, Expression> expression = new SingleRelation<ExpressionVariableInitializater, Expression>(this);

    public static ExpressionVariableInitializater fromAntlrNode(Java8Parser.ExpressionContext antlrNode){
        ExpressionVariableInitializater instance = new ExpressionVariableInitializater();
        instance.expression.set(Expression.fromAntlrNode(antlrNode));
        return instance;
    }
}
