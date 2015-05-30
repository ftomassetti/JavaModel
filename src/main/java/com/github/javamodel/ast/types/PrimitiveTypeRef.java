package com.github.javamodel.ast.types;

import com.github.javamodel.Java8Parser;

/**
 * Created by ftomassetti on 27/05/15.
 */
public class PrimitiveTypeRef extends TypeRef {

    public static TypeRef fromAntlrNode(Java8Parser.PrimitiveTypeContext antlrNode) {
        PrimitiveTypeRef instance = new PrimitiveTypeRef();
        instance.primitiveType = PrimitiveType.valueOf(antlrNode.getText().toUpperCase());
        return instance;
    }

    public static enum PrimitiveType {
        BOOLEAN,
        INT,
        CHAR,
        LONG,
        FLOAT,
        DOUBLE,
        BYTE
    }
    
    private PrimitiveType primitiveType;

    public static PrimitiveTypeRef fromAntlrNode(Java8Parser.UnannPrimitiveTypeContext antlrNode) {
        PrimitiveTypeRef instance = new PrimitiveTypeRef();
        instance.primitiveType = PrimitiveType.valueOf(antlrNode.getText().toUpperCase());
        return instance;
    }

    @Override
    public String toString() {
        return "PrimitiveTypeRef{" + primitiveType +  "}";
    }
}
