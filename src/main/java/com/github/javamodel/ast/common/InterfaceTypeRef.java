package com.github.javamodel.ast.common;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.annotations.RuleMapping;
import com.github.javamodel.ast.AstNode;
import com.github.javamodel.ast.reflection.AstNodeType;
import lombok.Data;

/**
 * Created by federico on 23/05/15.
 */
@Data
@RuleMapping(rule= Java8Parser.InterfaceTypeContext.class)
public class InterfaceTypeRef extends AstNode {

    public static final AstNodeType NODE_TYPE = AstNodeType.deriveFromNodeClass(InterfaceTypeRef.class);

    protected InterfaceTypeRef(AstNode parent) {
        super(NODE_TYPE, parent);
    }
}
