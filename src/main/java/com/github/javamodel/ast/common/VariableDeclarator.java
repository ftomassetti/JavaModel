package com.github.javamodel.ast.common;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.annotations.AttributeMapping;
import com.github.javamodel.annotations.RuleMapping;
import com.github.javamodel.ast.AstNode;
import com.github.javamodel.ast.reflection.AstNodeType;
import com.github.javamodel.ast.reflection.AstNodeTypeDeriver;
import lombok.Data;

/**
 * Created by federico on 23/05/15.
 */
@Data
@RuleMapping(rule= Java8Parser.VariableDeclaratorContext.class)
public class VariableDeclarator extends AstNode {

    public static final AstNodeType NODE_TYPE = AstNodeTypeDeriver.deriveFromNodeClass(VariableDeclarator.class);

    @AttributeMapping
    private String name;

    public VariableDeclarator(AstNode parent) {
        super(NODE_TYPE, parent);
    }

    public static VariableDeclarator fromAntlrNode(AstNode parent, Java8Parser.VariableDeclaratorContext antlrNode){
        VariableDeclarator instance = new VariableDeclarator(parent);
            instance.name = antlrNode.variableDeclaratorId().Identifier().getText();
            return instance;

    }
}
