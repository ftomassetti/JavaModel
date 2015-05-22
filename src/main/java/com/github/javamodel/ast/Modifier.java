package com.github.javamodel.ast;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.RuleMapping;
import lombok.Data;

/**
 * Created by federico on 22/05/15.
 */
@Data
@RuleMapping(rule= Java8Parser.ClassModifierContext.class)
public class Modifier extends Node {

    public static final NodeType NODE_TYPE = NodeType.deriveFromNodeClass(Modifier.class);

    private Value value;

    public static enum Value {
        PUBLIC,
        PROTECTED,
        PRIVATE,
        ABSTRACT,
        STATIC,
        FINAL,
        STRICTFP
    }

    public Modifier(Node parent) {
        super(NODE_TYPE, parent);
    }
}
