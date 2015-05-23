package com.github.javamodel.parsing;

import com.github.javamodel.ast.AstNode;
import com.github.javamodel.ast.reflection.Attribute;
import com.github.javamodel.ast.reflection.Relation;

import java.util.List;

/**
 * Created by federico on 21/05/15.
 */
public class NodeTree {

    public static void printTree(AstNode node, String relationName, int indentation, StringBuffer stringBuffer) {
        for (int j = 0; j < indentation; j++) stringBuffer.append("  ");
        stringBuffer.append(relationName + " : " +node.nodeType().getName());
        boolean attributes = false;
        for (Attribute attribute : (List<Attribute>)node.nodeType().getSortedAttributes()){
            if (!attributes){
                attributes = true;
                stringBuffer.append(" {");
            }
            stringBuffer.append(" " + attribute.getName() + "=" + attribute.get(node));
        }
        if (attributes) {
            stringBuffer.append(" }");
            stringBuffer.append("\n");
        } else {
            stringBuffer.append("\n");
        }
        for (Relation relation : (List<Relation>)node.nodeType().getSortedRelations()){
            for (AstNode child : node.getChildren(relation)) {
                printTree(child, relation.getName(), indentation + 1, stringBuffer);
            }
        }
    }

    public static String treeString(AstNode node, String relationName, int indentation) {
        StringBuffer sb = new StringBuffer();
        printTree(node, relationName, indentation, sb);
        return sb.toString();
    }
}
