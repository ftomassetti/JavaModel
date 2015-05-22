package com.github.javamodel;

import com.github.javamodel.ast.Attribute;
import com.github.javamodel.ast.Relation;

/**
 * Created by federico on 21/05/15.
 */
public class NodeTree {

    public static void printTree(Node node, String relationName, int indentation, StringBuffer stringBuffer) {
        for (int j = 0; j < indentation; j++) stringBuffer.append("  ");
        stringBuffer.append(relationName + " : " +node.getNodeType().getName());
        boolean attributes = false;
        for (Attribute attribute : node.getNodeType().getAttributes()){
            if (!attributes){
                attributes = true;
                stringBuffer.append(" {");
            }
            stringBuffer.append(" " + attribute.getName() + "=" + node.getSingleValue(attribute));
        }
        if (attributes) {
            stringBuffer.append(" }");
            stringBuffer.append("\n");
        } else {
            stringBuffer.append("\n");
        }
        for (Relation relation : node.getNodeType().getRelations()){
            for (Node child : node.getChildren(relation)) {
                printTree(child, relation.getName(), indentation + 1, stringBuffer);
            }
        }
    }

    public static String treeString(Node node, String relationName, int indentation) {
        StringBuffer sb = new StringBuffer();
        printTree(node, relationName, indentation, sb);
        return sb.toString();
    }
}
