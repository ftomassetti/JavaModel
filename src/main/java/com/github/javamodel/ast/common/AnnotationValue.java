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
@RuleMapping(rule= Java8Parser.ElementValueContext.class)
public class AnnotationValue extends AstNode {

    public static final AstNodeType NODE_TYPE = AstNodeTypeDeriver.deriveFromNodeClass(AnnotationValue.class);

    protected AnnotationValue(AstNode parent) {
        super(NODE_TYPE, parent);
    }
}
