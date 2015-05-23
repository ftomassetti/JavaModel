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
class Attribute {
    private boolean multiple;
    private String name;
    private Class<?> type;

    public Attribute(boolean multiple, String name, Class<?> type) {
        this.multiple = multiple;
        this.name = name;
        this.type = type;
    }
}
