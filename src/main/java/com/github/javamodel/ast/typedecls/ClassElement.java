package com.github.javamodel.ast.typedecls;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.annotations.RuleMapping;
import com.github.javamodel.ast.Node;
import com.github.javamodel.ast.reflection.NodeType;

/**
 * Created by federico on 23/05/15.
 */
@RuleMapping(rule = Java8Parser.ClassBodyDeclarationContext.class)
public abstract class ClassElement extends Node {

    public static final NodeType NODE_TYPE = NodeType.deriveFromNodeClass(ClassElement.class);

    public ClassElement(Node parent) {
        super(NODE_TYPE, parent);
    }
}
