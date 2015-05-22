package com.github.javamodel.ast.filelevel;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.annotations.RuleMapping;
import com.github.javamodel.ast.Node;
import com.github.javamodel.ast.reflection.NodeType;
import lombok.Data;

@Data
@RuleMapping(rule= Java8Parser.PackageDeclarationContext.class)
public class PackageDeclaration extends Node {

    public static final NodeType NODE_TYPE = NodeType.deriveFromNodeClass(PackageDeclaration.class);

    public PackageDeclaration(Node parent) {
        super(NODE_TYPE, parent);
    }
    
}
