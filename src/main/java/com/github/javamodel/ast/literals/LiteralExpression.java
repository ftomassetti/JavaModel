package com.github.javamodel.ast.literals;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.ast.Expression;

/**
 * Created by ftomassetti on 29/05/15.
 */
public class LiteralExpression extends Expression {

    public static Expression fromAntlrNode(Java8Parser.LiteralContext antlrNode){
        if (antlrNode.BooleanLiteral() != null){
            return BooleanLiteralExpression.fromAntlrNode(antlrNode);
        } else if (antlrNode.CharacterLiteral() != null){
            return CharLiteralExpression.fromAntlrNode(antlrNode);
        } else if (antlrNode.FloatingPointLiteral() != null){
            return FloatingPointLiteralExpression.fromAntlrNode(antlrNode);
        } else if (antlrNode.IntegerLiteral() != null) {
            return IntegerLiteralExpression.fromAntlrNode(antlrNode);
        } else if (antlrNode.NullLiteral() != null) {
            return NullLiteralExpression.fromAntlrNode(antlrNode);
        } else if (antlrNode.StringLiteral() != null) {
            return StringLiteralExpression.fromAntlrNode(antlrNode);
        } else {
            throw new UnsupportedOperationException();
        }
    }
}
