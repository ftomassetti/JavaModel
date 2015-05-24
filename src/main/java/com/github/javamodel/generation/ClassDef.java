package com.github.javamodel.generation;

import com.google.common.collect.ImmutableMap;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
* Created by federico on 24/05/15.
*/
class ClassDef {
    private String name;
    private String antlrNodeClass;
    private List<Map<String, Object>> fields;

    public ClassDef(String name, String antlrNodeClass, List<Map<String, Object>> fields) {
        this.name = name;
        this.antlrNodeClass = antlrNodeClass;
        this.fields = fields;
    }

    public String getName() {
        return name;
    }

    public String getAntlrNodeClass() {
        return antlrNodeClass;
    }

    public List<Map<String, Object>> getFields() {
        return fields;
    }

    public Map<String, Object> toMap() {
        Set<String> imports = new HashSet<>();
        for (Map<String, Object> field : fields) {
            imports.add("com.github.javamodel." + AstNodesGenerator.capitalize((String) field.get("type")));
        }
        return ImmutableMap.of(
                "name", name,
                "antlrNodeClass", antlrNodeClass,
                "fields", fields,
                "imports", imports);
    }
}
