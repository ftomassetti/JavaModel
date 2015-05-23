package com.github.javamodel.ast.common;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.annotations.RelationMapping;
import com.github.javamodel.annotations.RuleMapping;
import com.github.javamodel.ast.AstNode;
import com.github.javamodel.ast.reflection.AstNodeType;
import lombok.Data;

import java.util.List;

/**
 * Created by federico on 23/05/15.
 */
@Data
@RuleMapping(rule= Java8Parser.NormalAnnotationContext.class)
public class MultipleElementsAnnotationUsage extends AnnotationUsageNode {

    public static final AstNodeType NODE_TYPE = AstNodeType.deriveFromNodeClass(MultipleElementsAnnotationUsage.class);

    @RelationMapping(ctxAccessorName = "elementValuePairList")
    private List<AnnotationValuePair> values;

    protected MultipleElementsAnnotationUsage(AstNode parent) {
        super(NODE_TYPE, parent);
    }
}
