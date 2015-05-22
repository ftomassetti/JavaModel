package com.github.javamodel.ast.typedecls;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.RuleMapping;
import com.github.javamodel.ast.Node;
import com.github.javamodel.ast.NodeType;

/**
 * Created by ftomassetti on 22/05/15.
 */
@RuleMapping(rule= Java8Parser.NormalClassDeclarationContext.class)
public class ClassDeclaration extends TypeDeclaration {

    public static final NodeType NODE_TYPE = NodeType.deriveFromNodeClass(ClassDeclaration.class);

    protected ClassDeclaration(Node parent) {
        super(NODE_TYPE, parent);
    }
}
