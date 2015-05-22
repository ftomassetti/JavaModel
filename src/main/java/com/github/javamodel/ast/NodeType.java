package com.github.javamodel.ast;

import com.github.javamodel.Java8Parser;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import lombok.Data;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.lang.reflect.Method;
import java.util.*;

/**
* Created by federico on 21/05/15.
*/
@Data
public class NodeType<N extends Node> {
    
    private List<Relation> relations = new LinkedList<>();
    private List<Attribute> attributes = new LinkedList<>();
    private String name;
    private Class<N> nodeClass;

    private NodeType(String name, Class<N> nodeClass){
        this.name = name;
        this.nodeClass = nodeClass;
    }

    public static <N extends  Node> NodeType deriveFromNodeClass(Class<N> nodeClass) {
        NodeType nodeType = new NodeType(nodeClass.getSimpleName(), nodeClass);
        return nodeType;
    }
    
    public N fromAntlrNode(ParserRuleContext ruleContext){
        try {
            N node = nodeClass.newInstance();
            return node;
        } catch (IllegalAccessException | InstantiationException e){
            throw new RuntimeException(e);
        }
    }

    public static class Builder<N extends Node> {
        
        private String name;
        private List<Relation> relations;
        private List<Attribute> attributes;
        private Class<N> nodeClass;
        
        public Builder(String name, Class<N> nodeClass){
            this.name = name;
            this.nodeClass = nodeClass;
        }
        
        public Builder addRelation(Relation relation){
            relations.add(relation);
            return this;
        }

        public Builder addAttribute(Attribute attribute){
            attributes.add(attribute);
            return this;
        }
        
        public NodeType<N> build(){
            NodeType nodeType = new NodeType(name, nodeClass);
            nodeType.relations.addAll(relations);
            nodeType.attributes.addAll(attributes);
            return nodeType;
        }
    }
    
    /*private static Set<Class> transparentTypes = ImmutableSet.of(
            Java8Parser.TypeDeclarationContext.class,
            Java8Parser.ClassDeclarationContext.class,
            Java8Parser.ClassBodyContext.class,
            Java8Parser.ClassBodyDeclarationContext.class,
            Java8Parser.ClassMemberDeclarationContext.class,
            Java8Parser.UnannTypeContext.class,
            Java8Parser.UnannPrimitiveTypeContext.class,
            Java8Parser.NumericTypeContext.class,
            Java8Parser.FieldModifierContext.class,
            Java8Parser.VariableDeclaratorListContext.class);

    private static Set<Class> classesWithValues = ImmutableSet.of(Java8Parser.IntegralTypeContext.class);

    private static Map<Method, String> aliases = ImmutableMap.of(
            method(Java8Parser.FieldDeclarationContext.class, "unannType"), "type",
            method(Java8Parser.NormalClassDeclarationContext.class, "classBody"), "classElements",
            method(Java8Parser.VariableDeclaratorContext.class, "variableDeclaratorId"), "id"
    );

    private static Method method(Class c, String methodName){
        try {
            return c.getMethod(methodName);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean transparent;

    private static Map<Class<?>, NodeType> nodeTypes = new HashMap<>();*/

    /*public static <C extends ParserRuleContext> NodeType get(Class<C> parserRuleContextClass){
        if (!nodeTypes.containsKey(parserRuleContextClass)){
            try {
                nodeTypes.put(parserRuleContextClass, deriveNodeType(parserRuleContextClass));
            } catch (NoSuchMethodException e){
                throw new RuntimeException(e);
            }
        }
        return nodeTypes.get(parserRuleContextClass);
    }

    private static String toPropertyName(Method method){
        if (aliases.containsKey(method)){
            return aliases.get(method);
        } else {
            return method.getName();
        }
    }*/

    /*private static <C extends ParserRuleContext> NodeType deriveNodeType(Class<C> ruleContextClass) throws NoSuchMethodException {
        if (!ruleContextClass.getSimpleName().endsWith("Context")){
            throw new RuntimeException("Unexpected name: "+ruleContextClass.getSimpleName());
        }
        NodeType nodeType = new NodeType(ruleContextClass.getSimpleName().substring(0, ruleContextClass.getSimpleName().length() - 7));
        nodeType.setTransparent(transparentTypes.contains(ruleContextClass));
        for (Method method : ruleContextClass.getDeclaredMethods()) {
            Class<?> returnType = method.getReturnType();
            if (method.getName().equals("getRuleIndex")) {
            } else if (method.getParameterCount() != 0) {
            } else if ("void".equals(returnType.getTypeName())) {
            } else if (List.class.isAssignableFrom(returnType)) {
                // It return a list, but we cannot retrieve the type of the element. We need to find a method with the
                // same name but getting a parameter:
                // ex.
                // 		List<ImportDeclarationContext> importDeclaration()
                //      ImportDeclarationContext importDeclaration(int i)
                Method singleElementMethod = ruleContextClass.getDeclaredMethod(method.getName(), int.class);
                if (singleElementMethod.getReturnType().getCanonicalName().equals(TerminalNode.class.getCanonicalName())) {
                    nodeType.getAttributes().add(new Attribute(true, toPropertyName(method), method));
                } else if (ParserRuleContext.class.isAssignableFrom(singleElementMethod.getReturnType())){
                    nodeType.getRelations().add(new Relation(singleElementMethod.getReturnType(), true, toPropertyName(method), method));
                } else {
                    throw new RuntimeException("unexpected method " + method);
                }
            } else {
                if (returnType.getCanonicalName().equals(TerminalNode.class.getCanonicalName())) {
                    if (!method.getName().equals("EOF")) {
                        nodeType.getAttributes().add(new Attribute(false, toPropertyName(method), method));
                    }
                } else if (ParserRuleContext.class.isAssignableFrom(returnType)){
                    nodeType.getRelations().add(new Relation(returnType, false, toPropertyName(method), method));
                } else {
                    throw new RuntimeException("unexpected method " + method);
                }
            }
        }
        if (classesWithValues.contains(ruleContextClass)){
            nodeType.getAttributes().add(new Attribute(false, "value", (v) -> v.getStart().getText()));
        }
        return nodeType;
    }*/
}
