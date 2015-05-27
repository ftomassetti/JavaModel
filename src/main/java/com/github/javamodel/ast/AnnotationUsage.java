package com.github.javamodel.ast;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.Node;

/**
 * Created by ftomassetti on 27/05/15.
 */
public class AnnotationUsage extends Node {

    public static AnnotationUsage fromAntlrNode(Java8Parser.AnnotationContext antlrNode){
        AnnotationUsage instance = new AnnotationUsage();
        return instance;
    }
}
