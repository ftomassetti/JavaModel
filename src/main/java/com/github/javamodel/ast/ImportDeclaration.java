package com.github.javamodel.ast;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.Node;

/**
 * Created by ftomassetti on 27/05/15.
 */
public class ImportDeclaration extends Node {

    public static ImportDeclaration fromAntlrNode(Java8Parser.ImportDeclarationContext antlrNode) {
        ImportDeclaration instance = new ImportDeclaration();
        throw new UnsupportedOperationException();
    }
    
}
