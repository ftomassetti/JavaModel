package com.github.javamodel.ast.reflection;

import com.github.javamodel.ast.Node;
import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;

/**
* Created by federico on 21/05/15.
*/
@Data
@AllArgsConstructor
public class Relation {
    private Class<?> type;
    private boolean multiple;
    private String name;

    public static Relation single(String name, Class<?> type){
        return new Relation(type, false, name);
    }

    public static Relation multiple(String name, Class<?> type){
        return new Relation(type, true, name);
    }

    public List<? extends Node> get(Node node) {
        try {
            Object result = node.getClass().getDeclaredMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1)).invoke(node);
            if (result instanceof List) return (List<? extends Node>)result;
            else if (result==null){
                return ImmutableList.of();
            } else {
                return ImmutableList.of((Node)result);
            }
        } catch (IllegalAccessException | InvocationTargetException| NoSuchMethodException e) {
            throw new RuntimeException("Unable to get value of relation "+name+" for node "+node, e);
        }
    }
}
