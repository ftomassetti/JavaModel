package com.github.javamodel.ast.reflection;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
* Created by federico on 21/05/15.
*/
@Data
@AllArgsConstructor
class Relation {
    private Class<?> type;
    private boolean multiple;
    private String name;

}
