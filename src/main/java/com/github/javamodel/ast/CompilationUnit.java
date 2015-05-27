package com.github.javamodel.ast;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.Node;

public class CompilationUnit extends Node {
    
    private SingleRelation<CompilationUnit, PackageDeclaration> packageDeclaration = new SingleRelation<>(this);
    private MultipleRelation<CompilationUnit, ImportDeclaration> imports = new MultipleRelation<>(this);
    
    public static CompilationUnit fromAntlrNode(Java8Parser.CompilationUnitContext antlrNode){
        CompilationUnit instance = new CompilationUnit();
        instance.packageDeclaration.set(PackageDeclaration.fromAntlrNode(antlrNode.packageDeclaration()));
        antlrNode.importDeclaration().forEach((an)->instance.imports.add(ImportDeclaration.fromAntlrNode(an)));
        return instance;
    }
    
}
