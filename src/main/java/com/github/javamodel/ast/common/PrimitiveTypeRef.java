package com.github.javamodel.ast.common;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.annotations.AttributeMapping;
import com.github.javamodel.annotations.RuleMapping;
import com.github.javamodel.ast.AstNode;
import com.github.javamodel.ast.reflection.AstNodeType;
import com.github.javamodel.ast.reflection.AstNodeTypeDeriver;
import lombok.Data;

/**
 * Created by federico on 23/05/15.
 */
@Data
@RuleMapping(rule= Java8Parser.UnannPrimitiveTypeContext.class)
public class PrimitiveTypeRef extends TypeRef {

    public static final AstNodeType NODE_TYPE = AstNodeTypeDeriver.deriveFromNodeClass(PrimitiveTypeRef.class);

    @AttributeMapping
    private PrimitiveType primitiveType;

    public PrimitiveTypeRef(AstNode parent) {
        super(NODE_TYPE, parent);
    }
}
