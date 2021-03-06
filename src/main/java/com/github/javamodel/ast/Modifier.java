package com.github.javamodel.ast;

import com.github.javamodel.Java8Parser;

/**
 * Created by ftomassetti on 27/05/15.
 */
public enum Modifier {
    PUBLIC,
    PROTECTED,
    PRIVATE,
    STATIC,
    FINAL,
    STRICTFP,
    TRANSIENT,
    VOLATILE,
    ABSTRACT,
    SYNCHRONIZED,
    NATIVE;

    public static Modifier fromAntlrNode(Java8Parser.ClassModifierContext antlrNode) {
        return Modifier.valueOf(antlrNode.getText().toUpperCase());
    }

    public static Modifier fromAntlrNode(Java8Parser.FieldModifierContext antlrNode) {
        return Modifier.valueOf(antlrNode.getText().toUpperCase());
    }

    public static Modifier fromAntlrNode(Java8Parser.MethodModifierContext antlrNode) {
        return Modifier.valueOf(antlrNode.getText().toUpperCase());
    }

    public static Modifier fromAntlrNode(Java8Parser.VariableModifierContext antlrNode) {
        return Modifier.valueOf(antlrNode.getText().toUpperCase());
    }
}
