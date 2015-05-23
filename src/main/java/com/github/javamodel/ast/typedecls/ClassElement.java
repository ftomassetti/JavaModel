package com.github.javamodel.ast.typedecls;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.annotations.RuleMapping;
import com.github.javamodel.ast.AstNode;
import com.github.javamodel.ast.reflection.AstNodeType;
import com.github.javamodel.ast.reflection.AstNodeTypeDeriver;

/**
 * Created by federico on 23/05/15.
 */
@RuleMapping(rule = Java8Parser.ClassBodyDeclarationContext.class)
public abstract class ClassElement extends AstNode {

    public static final AstNodeType NODE_TYPE = AstNodeTypeDeriver.deriveFromNodeClass(ClassElement.class);

    public ClassElement(AstNode parent) {
        super(NODE_TYPE, parent);
    }
}
