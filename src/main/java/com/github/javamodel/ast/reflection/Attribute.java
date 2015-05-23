package com.github.javamodel.ast.reflection;

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
}
