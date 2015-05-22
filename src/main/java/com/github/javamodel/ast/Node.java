package com.github.javamodel.ast;

/**
 * Created by ftomassetti on 22/05/15.
 */
public class Node {
    
    private NodeType nodeType;
    
    protected Node(NodeType nodeType){
        this.nodeType = nodeType;
    }
    
    public NodeType nodeType(){
        return nodeType;
        
    }
}
