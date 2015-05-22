package com.github.javamodel.ast;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.RelationMapping;
import com.github.javamodel.RuleMapping;
import com.github.javamodel.ast.typedecls.ClassDeclaration;
import com.github.javamodel.ast.typedecls.TypeDeclaration;
import com.google.common.collect.ImmutableSet;
import lombok.Data;
import org.antlr.v4.runtime.ParserRuleContext;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
* Created by federico on 21/05/15.
*/
@Data
public class NodeType<N extends Node> {
    
    private List<Relation> relations = new LinkedList<>();
    private List<Attribute> attributes = new LinkedList<>();
    private String name;
    private Class<N> nodeClass;

    private static Set<Class> transparentTypes = ImmutableSet.of(
            Java8Parser.TypeDeclarationContext.class,
            Java8Parser.ClassDeclarationContext.class,
            Java8Parser.ClassBodyContext.class,
            Java8Parser.ClassBodyDeclarationContext.class,
            Java8Parser.ClassMemberDeclarationContext.class,
            Java8Parser.UnannTypeContext.class,
            Java8Parser.UnannPrimitiveTypeContext.class,
            Java8Parser.NumericTypeContext.class,
            Java8Parser.FieldModifierContext.class,
            Java8Parser.VariableDeclaratorListContext.class,
            Java8Parser.ClassModifierContext.class);

    private NodeType(String name, Class<N> nodeClass){
        this.name = name;
        this.nodeClass = nodeClass;
    }
    
    // This force the classes to be loaded
    private static Set<Object> nodeClasses = ImmutableSet.of(
            TypeDeclaration.NODE_TYPE, ClassDeclaration.NODE_TYPE,
            AnnotationUsageNode.NODE_TYPE, Modifier.NODE_TYPE);
    
    private static Map<Class, Class> ruleClassesToNodeClasses;
    private static Map<Class, NodeType> ruleClassesToNodeTypes;

    public static <N extends  Node> NodeType deriveFromNodeClass(Class<N> nodeClass) {
        //System.out.println("DERIVING "+nodeClass);
        NodeType nodeType = new NodeType(nodeClass.getSimpleName(), nodeClass);
        RuleMapping[] ruleMappings = nodeClass.getAnnotationsByType(RuleMapping.class);
        if (1 != ruleMappings.length){
            throw new RuntimeException();
        }
        if (ruleClassesToNodeClasses == null) ruleClassesToNodeClasses = new HashMap<>();
        if (ruleClassesToNodeTypes == null) ruleClassesToNodeTypes = new HashMap<>();
        ruleClassesToNodeClasses.put(ruleMappings[0].rule(), nodeClass);
        ruleClassesToNodeTypes.put(ruleMappings[0].rule(), nodeType);
        //System.out.println("  known relations are  " + ruleClassesToNodeClasses);
        return nodeType;
    }
    
    private String ctxAccessorName(Field field){
        RelationMapping[] relationMappings = field.getAnnotationsByType(RelationMapping.class);
        if (relationMappings.length != 1) throw new RuntimeException();
        if (relationMappings[0].ctxAccessorName().isEmpty()) {
            return field.getName();
        } else {
            return relationMappings[0].ctxAccessorName();
        }
    }
    
    private boolean isMultipleRelation(Field field){
        return List.class.isAssignableFrom(field.getType());
    }
    
    private NodeType findCorrespondingNodeType(Class ruleContextClass){
        System.out.println("  looking for corresponding class for "+ruleContextClass.getSimpleName());
        System.out.println("  known relations are  " + ruleClassesToNodeClasses);
        NodeType nodeType = ruleClassesToNodeTypes.get(ruleContextClass);
        System.out.println("  node type found is " + nodeType);
        if (nodeType == null){
            throw new RuntimeException("no corresponding nodeType for "+ruleContextClass);
        }
        return nodeType;
    }

    private Object getTransparent(Object value){
        if (transparentTypes.contains(value.getClass())){
            Object node = null;
            for (Method method : value.getClass().getDeclaredMethods()){
                if (method.getParameterCount() == 0){
                    if (ParserRuleContext.class.isAssignableFrom(method.getReturnType())){
                        try {
                            Object result = method.invoke(value);
                            if (result != null){
                                if (node != null){
                                    throw new RuntimeException();
                                } else {
                                    node = result;
                                }
                            }
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (List.class.isAssignableFrom(method.getReturnType())) {
                        throw new RuntimeException("Transparent types should not return list");
                    }
                }
            }
            if (node == null){
                ParserRuleContext ctx = (ParserRuleContext)value;
                if (ctx.getStart() != null) {
                    return ctx.getStart();
                } else {
                    throw new RuntimeException("No alternative found for the transparent type " + value.getClass());
                }
            }
            return getTransparent(node);
        } else {
            return value;
        }
    }
    
    public N fromAntlrNode(ParserRuleContext ruleContext, Node parentNode){
        try {
            N node = null;
            if (parentNode == null) {
                node = nodeClass.newInstance();
            } else {
                node = nodeClass.getConstructor(Node.class).newInstance(parentNode);
            }
            System.out.println("\n=== AntlrNode "+ruleContext.getClass().getSimpleName()+" ===\n");
            for (Field field : nodeClass.getDeclaredFields()){
                if (field.isAnnotationPresent(RelationMapping.class)){
                    RelationMapping[] relationMappings = field.getAnnotationsByType(RelationMapping.class);
                    if (relationMappings.length != 1) throw new RuntimeException();
                    System.out.println("Considering field " + field.getName());
                    String ctxAccessorName = ctxAccessorName(field);
                    try {
                        Method ctxAccessor = ruleContext.getClass().getDeclaredMethod(ctxAccessorName);
                        Object value = ctxAccessor.invoke(ruleContext);
                        System.out.println("  Value obtained is " + value);
                        if (isMultipleRelation(field)){
                            System.out.println("  multiple relation");
                            if (value != null) {
                                List valueAsList = (List) value;
                                for (Object valueElement : filter(valueAsList, relationMappings[0])){
                                    // TODO if the class is "transparent" we should get the "concrete" one and use that one to find the element type
                                    System.out.println("  value element "+valueElement.getClass());
                                    valueElement = getTransparent(valueElement);
                                    NodeType elementNodeType = findCorrespondingNodeType(valueElement.getClass());
                                    Node correspondingElementValue = elementNodeType.fromAntlrNode((ParserRuleContext)valueElement, node);
                                    System.out.println("  converted to "+correspondingElementValue);
                                }
                            }
                        } else {
                            System.out.println("  single relation");
                            if (value != null) {
                                field.setAccessible(true);
                                //field.set(field, value);
                                field.setAccessible(false);
                            }
                        }
                    } catch (NoSuchMethodException | InvocationTargetException e){
                        throw new RuntimeException("Because "+nodeClass.getCanonicalName()+" has field "+field.getName()+" we expect "+ruleContext.getClass()+" to have method "+ctxAccessorName, e);
                    }
                }
            }
            return node;
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e){
            throw new RuntimeException(e);
        }
    }

    private Iterable<? extends Object> filter(List<Object> valueAsList, RelationMapping relationMapping) {
        if (relationMapping.filter().isEmpty()){
            return valueAsList;
        } else {
            boolean negated = relationMapping.filter().startsWith("!");
            final String methodName = negated ? relationMapping.filter().substring(1) : relationMapping.filter();
            return valueAsList.stream().filter((value)->{
                try {
                    Method method = value.getClass().getDeclaredMethod(methodName);
                    boolean isNull = null == method.invoke(value);
                    return isNull == negated;
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e){
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
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
