package com.github.javamodel.ast.expressions;

import com.github.javamodel.ast.Expression;
import com.github.javamodel.ast.SingleRelation;

import java.util.Optional;

/**
 * Created by federico on 29/05/15.
 */
public class BinaryExpression extends Expression {

    public static enum Operator {
        PLUS("+"),
        MINUS("-"),
        MULT("*"),
        DIV("/");

        private String text;

        private Operator(String text){
            this.text = text;
        }

        public static Operator fromText(String text){
            for (Operator operator : values()){
                if (operator.text.equals(text)){
                    return operator;
                }
            }
            throw new IllegalArgumentException();
        }

    }

    private Operator operator;
    private SingleRelation<BinaryExpression, Expression> leftOperand = new SingleRelation<BinaryExpression, Expression>(this);
    private SingleRelation<BinaryExpression, Expression> rightOperand = new SingleRelation<BinaryExpression, Expression>(this);

    @Override
    public String toString() {
        return "BinaryExpression{" +
                "operator=" + operator +
                '}';
    }

    public BinaryExpression(Operator operator, Expression left, Expression right){
        this.operator = operator;
        this.leftOperand.set(left);
        this.rightOperand.set(right);
    }



}
