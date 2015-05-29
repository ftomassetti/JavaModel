package com.github.javamodel.ast;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.Node;

/**
 * Created by ftomassetti on 29/05/15.
 */
public abstract class Expression extends Node {

    private static Expression fromAntlrNode(Java8Parser.AndExpressionContext antlrNode){
        if (antlrNode.equalityExpression() != null){
            if (antlrNode.equalityExpression().relationalExpression() != null){
                if (antlrNode.equalityExpression().relationalExpression().shiftExpression() != null){
                    if (antlrNode.equalityExpression().relationalExpression().shiftExpression().additiveExpression() != null){
                        if (antlrNode.equalityExpression().relationalExpression().shiftExpression().additiveExpression().multiplicativeExpression() != null){
                            if (antlrNode.equalityExpression().relationalExpression().shiftExpression().additiveExpression().multiplicativeExpression().unaryExpression() != null){
                                if (antlrNode.equalityExpression().relationalExpression().shiftExpression().additiveExpression().multiplicativeExpression().unaryExpression().unaryExpressionNotPlusMinus() != null) {
                                    Java8Parser.UnaryExpressionNotPlusMinusContext unary = antlrNode.equalityExpression().relationalExpression().shiftExpression().additiveExpression().multiplicativeExpression().unaryExpression().unaryExpressionNotPlusMinus();
                                    if (unary.castExpression() != null){
                                        throw new UnsupportedOperationException();
                                    } else if (unary.postfixExpression() != null){
                                        if (unary.postfixExpression().expressionName() != null) {
                                            throw new UnsupportedOperationException();
                                        } else if (unary.postfixExpression().primary() != null){
                                            if (unary.postfixExpression().primary().arrayCreationExpression() != null) {
                                                throw new UnsupportedOperationException();
                                            } else if (unary.postfixExpression().primary().primaryNoNewArray_lfno_primary() != null){
                                                if (unary.postfixExpression().primary().primaryNoNewArray_lfno_primary().arrayAccess_lfno_primary() != null){
                                                    throw new UnsupportedOperationException();
                                                } else if (unary.postfixExpression().primary().primaryNoNewArray_lfno_primary().classInstanceCreationExpression_lfno_primary() != null){
                                                    throw new UnsupportedOperationException();
                                                } else if (unary.postfixExpression().primary().primaryNoNewArray_lfno_primary().literal() != null){
                                                    throw new UnsupportedOperationException();
                                                } else if (unary.postfixExpression().primary().primaryNoNewArray_lfno_primary().fieldAccess_lfno_primary() != null){
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
                                    return fromAntlrNode(antlrNode.assignmentExpression().conditionalExpression().conditionalOrExpression().conditionalAndExpression().inclusiveOrExpression().exclusiveOrExpression().andExpression());
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
