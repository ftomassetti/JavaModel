package com.github.javamodel.ast.literals;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.ast.expressions.Expression;

/**
 * Created by ftomassetti on 29/05/15.
 */
public class FloatingPointLiteralExpression extends LiteralExpression {

    public static Expression fromAntlrNode(Java8Parser.LiteralContext antlrNode){
        throw new UnsupportedOperationException();
    }
}
