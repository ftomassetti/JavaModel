package com.github.javamodel.ast.common;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.annotations.AttributeMapping;
import com.github.javamodel.annotations.RuleMapping;
import com.github.javamodel.ast.AstNode;
import com.github.javamodel.ast.reflection.AstNodeType;
import lombok.Data;

/**
 * Created by federico on 22/05/15.
 */
@Data
@RuleMapping(rule= Java8Parser.AnnotationContext.class)
public abstract class AnnotationUsageNode extends AstNode {

    public static final AstNodeType NODE_TYPE = AstNodeType.deriveFromNodeClass(AnnotationUsageNode.class);

    @AttributeMapping
    private String typeName;

    protected AnnotationUsageNode(AstNodeType nodeType, AstNode parent) {
        super(nodeType, parent);
    }
}
