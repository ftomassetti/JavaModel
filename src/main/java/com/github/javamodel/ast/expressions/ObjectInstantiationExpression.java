package com.github.javamodel.ast.expressions;

import com.github.javamodel.Java8Parser;

/**
 * Created by federico on 29/05/15.
 */
public class ObjectInstantiationExpression extends Expression {

    private String name;

    public static ObjectInstantiationExpression fromAntlrNode(Java8Parser.ClassInstanceCreationExpression_lfno_primaryContext antlrNode){
        ObjectInstantiationExpression instance = new ObjectInstantiationExpression();
        if (antlrNode.expressionName() != null){
            throw new UnsupportedOperationException();
        }
        if (antlrNode.typeArguments() != null){
            throw new UnsupportedOperationException();
        }
        if (antlrNode.annotation() != null && !antlrNode.annotation().isEmpty()){
            throw new UnsupportedOperationException();
        }
        // TODO consider the whole expression
        // 	:	'new' typeArguments? annotation* Identifier ('.' annotation* Identifier)* typeArgumentsOrDiamond? '(' argumentList? ')' classBody?
        // |	expressionName '.' 'new' typeArguments? annotation* Identifier typeArgumentsOrDiamond? '(' argumentList? ')' classBody?
        return instance;
    }
}
