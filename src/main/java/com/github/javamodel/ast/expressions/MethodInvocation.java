package com.github.javamodel.ast.expressions;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.ast.MultipleRelation;
import com.github.javamodel.ast.SingleRelation;
import com.github.javamodel.ast.statements.ExpressionStatement;
import com.github.javamodel.ast.types.TypeRef;

/**
 * Created by federico on 31/05/15.
 */
public class MethodInvocation extends Expression {
    private String name;
    private MultipleRelation<MethodInvocation, Expression> actualParameters = new MultipleRelation<>("actualParameters", this);
    private String typeName;

    public static MethodInvocation fromAntlrNode(Java8Parser.MethodInvocationContext antlrNode) {
        MethodInvocation instance = new MethodInvocation();
        if (antlrNode.argumentList() != null){
            antlrNode.argumentList().expression().forEach((an)->
                instance.actualParameters.add(Expression.fromAntlrNode(an)));
        }
        if (antlrNode.expressionName() != null){
            throw new UnsupportedOperationException();
        }
        if (antlrNode.methodName() != null){
            instance.name = antlrNode.methodName().getText();
        } else {
            instance.name = antlrNode.Identifier().getText();
        }
        if (antlrNode.typeArguments() != null){
            throw new UnsupportedOperationException();
        }
        if (antlrNode.typeName() != null){
            instance.typeName = antlrNode.typeName().getText();
        }

        return instance;
    }
}
