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

    public static PrimitiveTypeRef fromAntlrNode(AstNode parent, Object antlrNode){
        if (antlrNode instanceof  Java8Parser.UnannPrimitiveTypeContext) {
            Java8Parser.UnannPrimitiveTypeContext unannPrimitiveTypeContext = (Java8Parser.UnannPrimitiveTypeContext)antlrNode;
            PrimitiveTypeRef instance = new PrimitiveTypeRef(parent);
            if (unannPrimitiveTypeContext.numericType() != null) {
                instance.primitiveType = PrimitiveType.valueOf(unannPrimitiveTypeContext.getText().toUpperCase());
            } else {
                instance.primitiveType = PrimitiveType.valueOf(unannPrimitiveTypeContext.getText().toUpperCase());
            }
            return instance;
        } else if (antlrNode instanceof  Java8Parser.PrimitiveTypeContext) {
            Java8Parser.PrimitiveTypeContext primitiveTypeContext = (Java8Parser.PrimitiveTypeContext) antlrNode;
            PrimitiveTypeRef instance = new PrimitiveTypeRef(parent);
            if (primitiveTypeContext.numericType() != null) {
                throw new UnsupportedOperationException();
            } else {
                instance.primitiveType = PrimitiveType.valueOf(primitiveTypeContext.getText().toUpperCase());
            }
            return instance;
        } else throw new RuntimeException();
    }
}
