package com.github.javamodel.ast;

import com.github.javamodel.ast.reflection.AstNodeType;
import com.github.javamodel.ast.reflection.Relation;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by ftomassetti on 22/05/15.
 */
public class AstNode {
    
    private AstNodeType nodeType;
    private AstNode parent;
    
    protected AstNode(AstNodeType nodeType, AstNode parent){
        this.nodeType = nodeType;
        this.parent = parent;
    }
    
    public AstNodeType nodeType(){
        return nodeType;
    }

    public List<AstNode> getChildren(){
        List<AstNode> children = new LinkedList<>();
        for (Relation relation : (Set<Relation>)nodeType.getRelations()){
            children.addAll(relation.get(this));
        }
        return children;
    }

    public List<? extends AstNode> getChildren(Relation relation){
        return relation.get(this);
    }
}
