package com.github.javamodel.ast.expressions;

import com.github.javamodel.ast.SingleRelation;

/**
 * Created by federico on 29/05/15.
 */
public class BinaryExpression extends Expression {

    public static enum Operator {
        PLUS("+"),
        MINUS("-"),
        MULT("*"),
        DIV("/"),
        MODULE("%");

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
    private SingleRelation<BinaryExpression, Expression> leftOperand = new SingleRelation<BinaryExpression, Expression>("leftOperand", this);
    private SingleRelation<BinaryExpression, Expression> rightOperand = new SingleRelation<BinaryExpression, Expression>("rightOperand", this);

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
