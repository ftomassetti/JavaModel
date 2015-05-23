package com.github.javamodel.ast;

import com.github.javamodel.ast.reflection.Attribute;
import com.github.javamodel.ast.reflection.NodeType;
import com.github.javamodel.ast.reflection.Relation;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by ftomassetti on 22/05/15.
 */
public class Node {
    
    private NodeType nodeType;
    private Node parent;
    
    protected Node(NodeType nodeType, Node parent){
        this.nodeType = nodeType;
        this.parent = parent;
    }
    
    public NodeType nodeType(){
        return nodeType;
    }

    public List<Node> getChildren(){
        List<Node> children = new LinkedList<>();
        for (Relation relation : (Set<Relation>)nodeType.getRelations()){
            children.addAll(relation.get(this));
        }
        return children;
    }

    public List<? extends Node> getChildren(Relation relation){
        return relation.get(this);
    }
}
