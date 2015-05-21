package com.github.javamodel;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by federico on 21/05/15.
 */
public class Node {
    private ParserRuleContext wrapped;

    private Node(ParserRuleContext wrapped){
        this.wrapped = wrapped;
    }

    public static Node wrap(ParserRuleContext wrapped){
        if (wrapped == null) throw new IllegalArgumentException("Wrapped should be not null");
        return new Node(wrapped);
    }

    public List<Node> getAllChildren(){
        List<Node> children = new LinkedList<>();
        for (Relation relation : getNodeType().getRelations()){
            children.addAll(relation.getChildren(wrapped));
        }
        return children;
    }

    public NodeType getNodeType(){
        return NodeType.get(wrapped.getClass());
    }

    public Object getSingleValue(Attribute attribute) {
        return attribute.getSingleValue(wrapped);
    }
}
