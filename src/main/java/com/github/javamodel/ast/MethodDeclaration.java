package com.github.javamodel.ast;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.Node;

/**
 * Created by ftomassetti on 27/05/15.
 */
public class MethodDeclaration extends Node {

    public static MethodDeclaration fromAntlrNode(Java8Parser.MethodDeclarationContext antlrNode){
        MethodDeclaration instance = new MethodDeclaration();
        throw new UnsupportedOperationException();
    }
}
