package com.github.javamodel.ast.typedecls;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.annotations.RuleMapping;
import com.github.javamodel.ast.AstNode;
import com.github.javamodel.ast.reflection.AstNodeType;
import com.github.javamodel.ast.reflection.AstNodeTypeDeriver;

/**
 * Created by ftomassetti on 22/05/15.
 */
@RuleMapping(rule= Java8Parser.TypeDeclarationContext.class)
public abstract class TypeDeclaration extends AstNode {

    public static final AstNodeType NODE_TYPE = AstNodeTypeDeriver.deriveFromNodeClass(TypeDeclaration.class);

    public TypeDeclaration(AstNodeType nodeType, AstNode parent) {
        super(nodeType, parent);
    }
    
}
