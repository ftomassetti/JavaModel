package com.github.javamodel.ast.common;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.annotations.RelationMapping;
import com.github.javamodel.annotations.RuleMapping;
import com.github.javamodel.ast.Node;
import com.github.javamodel.ast.reflection.NodeType;
import lombok.Data;

/**
 * Created by federico on 23/05/15.
 */
@Data
@RuleMapping(rule= Java8Parser.SingleElementAnnotationContext.class)
public class SingleElementAnnotationUsage extends AnnotationUsageNode {

    public static final NodeType NODE_TYPE = NodeType.deriveFromNodeClass(SingleElementAnnotationUsage.class);

    @RelationMapping(ctxAccessorName = "elementValue")
    private AnnotationValue value;

    protected SingleElementAnnotationUsage(Node parent) {
        super(NODE_TYPE, parent);
    }
}
