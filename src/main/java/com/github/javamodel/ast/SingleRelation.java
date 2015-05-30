package com.github.javamodel.ast;

import com.github.javamodel.Node;
import com.github.javamodel.Relation;
import com.google.common.collect.ImmutableList;

import java.util.Collection;

/**
 * Created by ftomassetti on 27/05/15.
 */
public class SingleRelation<P extends Node, C extends Node> extends Relation<P,C> {
    
    private C child;
    
    public SingleRelation(String name, P parent, C child){
        this(name, parent);
        set(child);
    }

    public SingleRelation(String name, P parent){
        super(name, parent);
    }

    @Override
    public void removeChild(Node node) {
        if (child == node) {
            child = null;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Collection<? extends Node> getChildren() {
        if (child == null){
            return ImmutableList.of();
        } else {
            return ImmutableList.of(child);
        }
    }

    public void set(C child){
        if (this.child != null) {
            this.child.setContainer(this);
        }
        this.child = child;
        if (child != null) {
            child.setContainer(this);
        }
    }
}
