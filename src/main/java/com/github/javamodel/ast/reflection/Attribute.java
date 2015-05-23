package com.github.javamodel.ast.reflection;

import com.github.javamodel.ast.Node;
import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.antlr.v4.runtime.ParserRuleContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

/**
* Created by federico on 21/05/15.
*/
@Data
@AllArgsConstructor
public class Attribute {
    private boolean multiple;
    private String name;
    private Class<?> type;

    public static Attribute single(String name, Class<?> type){
        return new Attribute(false, name, type);
    }

    public static Attribute multiple(String name, Class<?> type){
        return new Attribute(true, name, type);
    }

    public List<?> get(Node node) {
        try {
            Object result = node.getClass().getDeclaredMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1)).invoke(node);
            if (result instanceof List) return (List)result;
            else if (result==null){
                return ImmutableList.of();
            } else {
                return ImmutableList.of(result);
            }
        } catch (IllegalAccessException | InvocationTargetException| NoSuchMethodException e) {
            throw new RuntimeException("Unable to get value of relation "+name+" for node "+node, e);
        }
    }
}
