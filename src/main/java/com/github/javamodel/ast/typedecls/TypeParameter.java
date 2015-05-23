package com.github.javamodel.ast.typedecls;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.annotations.RuleMapping;
import com.github.javamodel.ast.AstNode;
import com.github.javamodel.ast.reflection.AstNodeType;
import com.github.javamodel.ast.reflection.AstNodeTypeDeriver;
import lombok.Data;

/**
 * Created by federico on 23/05/15.
 */
@Data
@RuleMapping(rule= Java8Parser.TypeParameterContext.class)
public class TypeParameter extends AstNode {

    public static final AstNodeType NODE_TYPE = AstNodeTypeDeriver.deriveFromNodeClass(TypeParameter.class);

    public TypeParameter(AstNode parent) {
        super(NODE_TYPE, parent);
    }
}
