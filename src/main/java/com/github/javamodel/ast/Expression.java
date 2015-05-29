package com.github.javamodel.ast;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.Node;

/**
 * Created by ftomassetti on 29/05/15.
 */
public abstract class Expression extends Node {

    public static Expression fromAntlrNode(Java8Parser.ExpressionContext antlrNode){
        if (antlrNode.assignmentExpression() != null){
            if (antlrNode.assignmentExpression().assignment() != null) {
                throw new UnsupportedOperationException();
            } else if (antlrNode.assignmentExpression().conditionalExpression() != null){
                if (antlrNode.assignmentExpression().conditionalExpression().conditionalExpression() != null){
                    throw new UnsupportedOperationException();
                } else if (antlrNode.assignmentExpression().conditionalExpression().conditionalOrExpression() != null){
                    if (antlrNode.assignmentExpression().conditionalExpression().conditionalOrExpression().conditionalAndExpression() != null) {
                        if (antlrNode.assignmentExpression().conditionalExpression().conditionalOrExpression().conditionalAndExpression().conditionalAndExpression() != null){
                            throw new UnsupportedOperationException();
                        } else if (antlrNode.assignmentExpression().conditionalExpression().conditionalOrExpression().conditionalAndExpression().inclusiveOrExpression() != null){
                            if (antlrNode.assignmentExpression().conditionalExpression().conditionalOrExpression().conditionalAndExpression().inclusiveOrExpression().exclusiveOrExpression() != null){
                                if (antlrNode.assignmentExpression().conditionalExpression().conditionalOrExpression().conditionalAndExpression().inclusiveOrExpression().exclusiveOrExpression().andExpression() != null) {
                                    throw new UnsupportedOperationException();
                                } else if (antlrNode.assignmentExpression().conditionalExpression().conditionalOrExpression().conditionalAndExpression().inclusiveOrExpression().exclusiveOrExpression().exclusiveOrExpression() != null){
                                    throw new UnsupportedOperationException();
                                } else {
                                    throw new UnsupportedOperationException();    
                                }
                            } else if (antlrNode.assignmentExpression().conditionalExpression().conditionalOrExpression().conditionalAndExpression().inclusiveOrExpression().inclusiveOrExpression() != null){
                                throw new UnsupportedOperationException();
                            } else {
                                throw new UnsupportedOperationException();
                            }
                        } else {
                            throw new UnsupportedOperationException();
                        }
                    } else if (antlrNode.assignmentExpression().conditionalExpression().conditionalOrExpression().conditionalOrExpression() != null){
                        throw new UnsupportedOperationException();
                    } else {
                        throw new UnsupportedOperationException();
                    }
                } else {
                    throw new UnsupportedOperationException();
                }
            } else {
                throw new UnsupportedOperationException();
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }
}
