package com.github.javamodel.ast;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.Node;

/**
 * Created by ftomassetti on 27/05/15.
 */
public class ImportDeclaration extends Node {

    private String typeName;
    private boolean static_;
    private boolean generic;

    public static ImportDeclaration fromAntlrNode(Java8Parser.ImportDeclarationContext antlrNode) {
        ImportDeclaration instance = new ImportDeclaration();
        if (antlrNode.singleStaticImportDeclaration() != null) {
            instance.typeName = antlrNode.singleStaticImportDeclaration().typeName().getText();
            instance.static_ = true;
            instance.generic = false;
        } else if (antlrNode.staticImportOnDemandDeclaration() != null){
            instance.typeName = antlrNode.staticImportOnDemandDeclaration().typeName().getText();
            instance.static_ = true;
            instance.generic = true;
        } else if (antlrNode.typeImportOnDemandDeclaration() != null){
            throw new UnsupportedOperationException();
        } else if (antlrNode.singleTypeImportDeclaration() != null){
            instance.typeName = antlrNode.singleTypeImportDeclaration().typeName().getText();
            instance.static_ = false;
            instance.generic = false;
        } else {
            throw new UnsupportedOperationException();
        }
        return instance;
    }
    
}
