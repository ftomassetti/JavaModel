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

    public Relation getContainer() {
        return container;
    }

    // Relation in which this node is the child
    private Relation container;
    // Relations in which this node is the parent
    private List<Relation> containments = new LinkedList<>();
    
    public void setContainer(Relation container){
        if (this.container != null){
            this.container.removeChild(this);
        }
        this.container = container;
    }

    public List<Node> getChildren() {
        List<Node> children = new ArrayList<>();
        for (Relation r : containments){
            children.addAll(r.getChildren());
        }
        return children;
    }

    public void addRelations(Relation relation) {
        containments.add(relation);
    }
}
