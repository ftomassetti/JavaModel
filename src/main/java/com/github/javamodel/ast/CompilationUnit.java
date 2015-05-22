package com.github.javamodel.ast;

/**
 * Created by ftomassetti on 22/05/15.
 */
public class CompilationUnit extends Node {
    
    public static final NodeType NODE_TYPE = NodeType.deriveFromNodeClass(CompilationUnit.class);

    protected CompilationUnit() {
        super(NODE_TYPE, null);
    }
}
