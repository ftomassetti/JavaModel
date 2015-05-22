package com.github.javamodel.ast;

import lombok.Data;

@Data
public class PackageDeclaration extends Node {

    public static final NodeType NODE_TYPE = NodeType.deriveFromNodeClass(PackageDeclaration.class);

    public PackageDeclaration(Node parent) {
        super(NODE_TYPE, parent);
    }
    
}
