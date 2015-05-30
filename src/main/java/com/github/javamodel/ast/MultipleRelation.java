package com.github.javamodel.ast;

import com.github.javamodel.Node;
import com.github.javamodel.Relation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by ftomassetti on 27/05/15.
 */
public class MultipleRelation<P extends Node, C extends Node>  extends Relation<P,C>  {

    private List<C> children = new ArrayList<>();

    public MultipleRelation(String name, P parent) {
        super(name, parent);
    }

    @Override
    public void removeChild(Node node) {
        children.remove(node);
    }

    @Override
    public Collection<? extends Node> getChildren() {
        return children;
    }

    public void add(C child) {
        children.add(child);
        if (child != null) {
            child.setContainer(this);
        }
    }
}
