package com.github.javamodel.ast;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.Node;

/**
 * Created by ftomassetti on 27/05/15.
 */
public class PrimitiveTypeRef extends TypeRef {

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
