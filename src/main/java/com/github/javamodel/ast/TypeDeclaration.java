package com.github.javamodel.ast;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.Node;

/**
 * Created by ftomassetti on 27/05/15.
 */
public abstract class TypeDeclaration extends Node {

    public static TypeDeclaration fromAntlrNode(Java8Parser.TypeDeclarationContext antlrNode) {
        if (antlrNode.classDeclaration() != null ){
            return fromAntlrNode(antlrNode.classDeclaration());
        } else {
            return InterfaceDeclaration.fromAntlrNode(antlrNode.interfaceDeclaration());
        }
    }

    public static TypeDeclaration fromAntlrNode(Java8Parser.ClassDeclarationContext antlrNode) {
        if (antlrNode.enumDeclaration() != null) {
            return EnumDeclaration.fromAntlrNode(antlrNode.enumDeclaration());
        } else {
            return ClassDeclaration.fromAntlrNode(antlrNode.normalClassDeclaration());
        }
    }
}
