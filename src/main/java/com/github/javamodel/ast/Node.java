package com.github.javamodel.ast;

import com.github.javamodel.ast.reflection.NodeType;

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
}
