package com.github.javamodel.ast.literals;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.ast.expressions.Expression;

/**
 * Created by ftomassetti on 29/05/15.
 */
public class IntegerLiteralExpression extends LiteralExpression {

    private String text;

    @Override
    public String toString() {
        return "IntegerLiteralExpression{" +
                "text='" + text + '\'' +
                '}';
    }

    public static Expression fromAntlrNode(Java8Parser.LiteralContext antlrNode){
        IntegerLiteralExpression instance = new IntegerLiteralExpression();
        instance.text = antlrNode.IntegerLiteral().getText();
        return instance;
    }
}
