package com.github.javamodel.ast;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.Node;
import com.github.javamodel.ast.types.TypeRef;

/**
 * Created by federico on 31/05/15.
 */
public class FormalParameter extends Node {

    private String name;
    private SingleRelation<FormalParameter, TypeRef> type = new SingleRelation<FormalParameter, TypeRef>("type", this);

    public static FormalParameter fromAntlrNode(Java8Parser.FormalParameterContext an) {
        FormalParameter instance = new FormalParameter();
        instance.type.set(TypeRef.fromAntlrNode(an.unannType()));
        if (an.variableDeclaratorId().dims() != null){
            throw new UnsupportedOperationException();
        }
        instance.name = an.variableDeclaratorId().Identifier().getText();

        if (an.variableModifier() != null){
            throw new UnsupportedOperationException();
        }
        return instance;
    }

}
