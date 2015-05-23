package com.github.javamodel.ast.common;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.annotations.AttributeMapping;
import com.github.javamodel.annotations.RuleMapping;
import com.github.javamodel.ast.Node;
import com.github.javamodel.ast.reflection.NodeType;
import lombok.Data;

/**
 * Created by federico on 22/05/15.
 */
@Data
@RuleMapping(rule= Java8Parser.AnnotationContext.class)
public abstract class AnnotationUsageNode extends Node {

    public static final NodeType NODE_TYPE = NodeType.deriveFromNodeClass(AnnotationUsageNode.class);

    @AttributeMapping
    private String typeName;

    protected AnnotationUsageNode(NodeType nodeType, Node parent) {
        super(nodeType, parent);
    }
}
