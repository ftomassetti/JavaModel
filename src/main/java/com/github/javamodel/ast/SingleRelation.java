package com.github.javamodel.ast;

import com.github.javamodel.Node;

/**
 * Created by ftomassetti on 27/05/15.
 */
public class SingleRelation<P extends Node, C extends Node> {
    
    private C child;
    private final P parent;
    
    public SingleRelation(P parent, C child){
        this(parent);
        set(child);
    }

    public SingleRelation(P parent){
        this.parent = parent;
    }
    
    public void set(C child){
        this.child = child;
        
    }
}
