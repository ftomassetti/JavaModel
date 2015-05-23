package com.github.javamodel.ast.common;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.annotations.RuleMapping;
import com.github.javamodel.ast.AstNode;
import com.github.javamodel.ast.reflection.AstNodeType;
import com.github.javamodel.ast.reflection.AstNodeTypeDeriver;
import lombok.Data;

/**
 * Created by federico on 23/05/15.
 */
@Data
@RuleMapping(rule= Java8Parser.ElementValuePairContext.class)
public class AnnotationValuePair extends AstNode {

    public static final AstNodeType NODE_TYPE = AstNodeTypeDeriver.deriveFromNodeClass(AnnotationValuePair.class);

    protected AnnotationValuePair(AstNode parent) {
        super(NODE_TYPE, parent);
    }

    private String name;

    private AnnotationValue value;
}
