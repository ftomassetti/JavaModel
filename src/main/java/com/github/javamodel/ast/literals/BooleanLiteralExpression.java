package com.github.javamodel.ast.literals;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.ast.Expression;

/**
 * Created by ftomassetti on 29/05/15.
 */
public class BooleanLiteralExpression extends LiteralExpression {

    private boolean value;
    
    public static Expression fromAntlrNode(Java8Parser.LiteralContext antlrNode){
        BooleanLiteralExpression instance = new BooleanLiteralExpression();
        instance.value = antlrNode.BooleanLiteral().getText().equals("true");
        return instance;
    }
}
