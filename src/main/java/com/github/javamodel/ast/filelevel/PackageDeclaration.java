package com.github.javamodel.ast.filelevel;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.annotations.AttributeMapping;
import com.github.javamodel.annotations.RuleMapping;
import com.github.javamodel.ast.AstNode;
import com.github.javamodel.ast.reflection.AstNodeType;
import com.github.javamodel.ast.reflection.AstNodeTypeDeriver;
import lombok.Data;

import java.util.List;

@Data
@RuleMapping(rule= Java8Parser.PackageDeclarationContext.class)
public class PackageDeclaration extends AstNode {

    @AttributeMapping
    private List<String> identifiers;

    public static final AstNodeType NODE_TYPE = AstNodeTypeDeriver.deriveFromNodeClass(PackageDeclaration.class);

    public PackageDeclaration(AstNode parent) {
        super(NODE_TYPE, parent);
    }
    
}
