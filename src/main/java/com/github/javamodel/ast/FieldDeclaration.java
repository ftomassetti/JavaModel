package com.github.javamodel.ast;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.Node;

/**
 * Created by ftomassetti on 27/05/15.
 */
public class FieldDeclaration extends Node {

    public static FieldDeclaration fromAntlrNode(Java8Parser.FieldDeclarationContext antlrNode){
        FieldDeclaration instance = new FieldDeclaration();
        throw new UnsupportedOperationException();
    }
}
