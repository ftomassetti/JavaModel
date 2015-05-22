package com.github.javamodel.ast.typedecls;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.RuleMapping;
import com.github.javamodel.ast.Node;
import com.github.javamodel.ast.NodeType;

/**
 * Created by ftomassetti on 22/05/15.
 */
@RuleMapping(rule= Java8Parser.TypeDeclarationContext.class)
public abstract class TypeDeclaration extends Node {

    public static final NodeType NODE_TYPE = NodeType.deriveFromNodeClass(TypeDeclaration.class);

    public TypeDeclaration(NodeType nodeType, Node parent) {
        super(nodeType, parent);
    }
    
}
