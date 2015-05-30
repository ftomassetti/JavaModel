package com.github.javamodel.ast.types;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.Node;

/**
 * Created by federico on 30/05/15.
 */
public class WildcardTypeArgument extends TypeRef {

    public static WildcardTypeArgument fromAntlrNode(Java8Parser.WildcardContext antlrNode) {
        WildcardTypeArgument instance = new WildcardTypeArgument();
        return instance;
    }
}
