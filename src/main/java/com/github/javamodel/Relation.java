package com.github.javamodel;

import java.util.Collection;

/**
 * Created by federico on 30/05/15.
 */
public abstract class Relation<P extends Node, C extends Node> {
    protected final P parent;
    private String name;

    public String getName() {
        return name;
    }

    protected Relation(String name, P parent){
        this.name = name;
        this.parent = parent;
        parent.addRelations(this);
    }

    public abstract void removeChild(Node node);

    public abstract Collection<? extends Node> getChildren();
}
