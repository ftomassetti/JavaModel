package com.github.javamodel.generation;

import com.google.common.collect.ImmutableMap;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
* Created by federico on 24/05/15.
*/
class InterfaceDef {
    private String name;
    private String antlrNodeClass;

    public InterfaceDef(String name, String antlrNodeClass) {
        this.name = name;
        this.antlrNodeClass = antlrNodeClass;
    }

    public Map<String, Object> toMap() {
        Set<String> imports = new HashSet<>();
        /*for (Map<String, Object> field : fields) {
            imports.add("com.github.javamodel." + capitalize((String) field.get("type")));
        }*/
        return ImmutableMap.of(
                "name", name,
                "antlrNodeClass", antlrNodeClass,
                "imports", imports);
    }
}
