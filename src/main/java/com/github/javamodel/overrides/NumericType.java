package com.github.javamodel.overrides;

import com.github.javamodel.Java8Parser;

/**
 * Created by federico on 24/05/15.
 */
public enum NumericType {
    BYTE,
    SHORT,
    INT,
    LONG,
    CHAR,
    FLOAT,
    DOUBLE;

    public static NumericType fromAntlrNode(Java8Parser.NumericTypeContext antlrNode){
        return NumericType.valueOf(antlrNode.getText().toUpperCase());
    }
}
