package com.github.javamodel.ast;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.RelationMapping;
import com.github.javamodel.RuleMapping;
import com.github.javamodel.ast.typedecls.TypeDeclaration;
import lombok.Data;

import java.util.List;

@Data
@RuleMapping(rule= Java8Parser.CompilationUnitContext.class)
public class CompilationUnit extends Node {
    
    public static final NodeType NODE_TYPE = NodeType.deriveFromNodeClass(CompilationUnit.class);
    
    @RelationMapping
    private PackageDeclaration packageDeclaration;

    @RelationMapping(ctxAccessorName = "typeDeclaration")
    private List<TypeDeclaration> topTypesDeclarations;

    public CompilationUnit() {
        super(NODE_TYPE, null);
    }
    
}
