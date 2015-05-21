package com.github.javamodel;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.antlr.v4.runtime.ParserRuleContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
* Created by federico on 21/05/15.
*/
@Data
@AllArgsConstructor
class Relation {
    private Class<?> type;
    private boolean multiple;
    private String name;
    private Method method;

    public List<Node> getChildren(ParserRuleContext parent) {
        List<Node> rawChildren = getChildrenRaw(parent);
        List<Node> children = new LinkedList<>();
        for (Node rawChild : rawChildren){
            if (rawChild.getNodeType().isTransparent()){
                children.addAll(rawChild.getAllChildren());
            } else {
                children.add(rawChild);
            }
        }
        return children;
    }

    private List<Node> getChildrenRaw(ParserRuleContext parent) {
        if (multiple){
            try {
                List<? extends ParserRuleContext> toBeWrapped = (List<? extends ParserRuleContext>) method.invoke(parent);
                return toBeWrapped.stream().map((raw)->Node.wrap(raw)).collect(Collectors.toList());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                ParserRuleContext result = (ParserRuleContext) method.invoke(parent);
                if (result == null) {
                    return ImmutableList.of();
                } else {
                    return ImmutableList.of(Node.wrap(result));
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
