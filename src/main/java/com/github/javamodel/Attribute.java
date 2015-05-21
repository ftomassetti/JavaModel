package com.github.javamodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.antlr.v4.runtime.ParserRuleContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
* Created by federico on 21/05/15.
*/
@Data
@AllArgsConstructor
class Attribute {
    private boolean multiple;
    private String name;
    private Method method;

    public Object getSingleValue(ParserRuleContext wrapped){
        if (multiple) throw new IllegalStateException();
        try {
            return method.invoke(wrapped);
        } catch (IllegalAccessException e){
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
