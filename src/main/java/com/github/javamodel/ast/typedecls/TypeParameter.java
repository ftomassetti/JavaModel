package com.github.javamodel.ast.typedecls;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.annotations.RuleMapping;
import com.github.javamodel.ast.Node;
import com.github.javamodel.ast.reflection.NodeType;
import lombok.Data;

/**
 * Created by federico on 23/05/15.
 */
@Data
@RuleMapping(rule= Java8Parser.TypeParameterContext.class)
public class TypeParameter extends Node {

    public static final NodeType NODE_TYPE = NodeType.deriveFromNodeClass(TypeParameter.class);

    public TypeParameter(Node parent) {
        super(NODE_TYPE, parent);
    }
}
