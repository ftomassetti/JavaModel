package com.github.javamodel.ast.reflection;

import lombok.AllArgsConstructor;
import lombok.Data;

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
}
