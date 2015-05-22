package com.github.javamodel.ast;

import lombok.Data;

@Data
public class CompilationUnit extends Node {
    
    public static final NodeType NODE_TYPE = NodeType.deriveFromNodeClass(CompilationUnit.class);

    private PackageDeclaration packageDeclaration;

    public CompilationUnit() {
        super(NODE_TYPE, null);
    }
    
}
