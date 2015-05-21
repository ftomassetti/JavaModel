package com.github.javamodel;

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
class Attribute {
    private boolean multiple;
    private String name;
    private Method method;
    private Function<ParserRuleContext, String> function;

    public Attribute(boolean multiple, String name, Method method) {
        this.multiple = multiple;
        this.name = name;
        this.method = method;
    }

    public Attribute(boolean multiple, String name, Function<ParserRuleContext, String> function) {
        this.multiple = multiple;
        this.name = name;
        this.function = function;
    }

    public Object getSingleValue(ParserRuleContext wrapped){
        if (multiple) throw new IllegalStateException();
        if (function != null) {
            return function.apply(wrapped);
        }
        try {
            return method.invoke(wrapped);
        } catch (IllegalAccessException e){
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
