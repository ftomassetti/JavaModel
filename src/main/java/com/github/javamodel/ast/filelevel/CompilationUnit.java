package com.github.javamodel.ast.filelevel;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.annotations.RelationMapping;
import com.github.javamodel.annotations.RuleMapping;
import com.github.javamodel.ast.AstNode;
import com.github.javamodel.ast.reflection.AstNodeType;
import com.github.javamodel.ast.typedecls.TypeDeclaration;
import lombok.Data;

import java.util.List;

@Data
@RuleMapping(rule= Java8Parser.CompilationUnitContext.class)
public class CompilationUnit extends AstNode {
    
    public static final AstNodeType NODE_TYPE = AstNodeType.deriveFromNodeClass(CompilationUnit.class);
    
    @RelationMapping
    private PackageDeclaration packageDeclaration;

    @RelationMapping(ctxAccessorName = "typeDeclaration")
    private List<TypeDeclaration> topTypes;

    public CompilationUnit() {
        super(NODE_TYPE, null);
    }
    
}
