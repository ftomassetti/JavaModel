package com.github.javamodel;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by federico on 21/05/15.
 */
public class Node {
    private Node parent;
    private List<Node> children = new ArrayList<>();
    
    public void setParent(Node parent){
        if (this.parent != null){
            this.parent.removeChild(this);
        }
        parent.addChild(this);
        this.parent = parent;
    }
    
    private void addChild(Node child){
        children.add(child);
    }
    
    private void removeChild(Node child){
        children.remove(child);
    }

    public List<Node> getChildren() {
        return children;
    }
}
