package com.github.javamodel.ast.expressions;

import com.github.javamodel.Java8Parser;

import java.util.List;

/**
 * Created by federico on 29/05/15.
 */
public class NameReferenceExpression extends Expression {

    private String name;

    @Override
    public String toString() {
        return "NameReferenceExpression{" +
                "name='" + name + '\'' +
                '}';
    }

    public static NameReferenceExpression fromAntlrNode(Java8Parser.ExpressionNameContext antlrNode){
        if (antlrNode.ambiguousName() != null){
            throw new UnsupportedOperationException();
        } else {
            NameReferenceExpression instance = new NameReferenceExpression();
            instance.name = antlrNode.Identifier().getText();
            return instance;
        }
    }

}
