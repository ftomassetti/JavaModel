package com.github.javamodel.ast;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.RuleMapping;
import lombok.Data;

/**
 * Created by federico on 22/05/15.
 */
@Data
@RuleMapping(rule= Java8Parser.AnnotationContext.class)
public abstract class AnnotationUsageNode extends Node {

    public static final NodeType NODE_TYPE = NodeType.deriveFromNodeClass(AnnotationUsageNode.class);

    protected AnnotationUsageNode(NodeType nodeType, Node parent) {
        super(nodeType, parent);
    }
}
