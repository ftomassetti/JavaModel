package com.github.javamodel.ast;

import com.github.javamodel.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ftomassetti on 27/05/15.
 */
public class MultipleRelation<P extends Node, C extends Node> {

    private List<C> children = new ArrayList<>();
    private final P parent;

    public MultipleRelation(P parent){
        this.parent = parent;
    }

    public void add(C child) {
        children.add(child);
    }
}
