package com.github.javamodel.ast.common;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.annotations.RelationMapping;
import com.github.javamodel.annotations.RuleMapping;
import com.github.javamodel.ast.Node;
import com.github.javamodel.ast.reflection.NodeType;
import lombok.Data;

import java.util.List;

/**
 * Created by federico on 23/05/15.
 */
@Data
@RuleMapping(rule= Java8Parser.NormalAnnotationContext.class)
public class MultipleElementsAnnotationUsage extends AnnotationUsageNode {

    public static final NodeType NODE_TYPE = NodeType.deriveFromNodeClass(MultipleElementsAnnotationUsage.class);

    @RelationMapping(ctxAccessorName = "elementValuePairList")
    private List<AnnotationValuePair> values;

    protected MultipleElementsAnnotationUsage(Node parent) {
        super(NODE_TYPE, parent);
    }
}
