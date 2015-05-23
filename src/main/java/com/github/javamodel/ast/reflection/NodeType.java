package com.github.javamodel.ast.reflection;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.annotations.AttributeMapping;
import com.github.javamodel.annotations.RelationMapping;
import com.github.javamodel.annotations.RuleMapping;
import com.github.javamodel.ast.common.AnnotationUsageNode;
import com.github.javamodel.ast.common.Modifier;
import com.github.javamodel.ast.Node;
import com.github.javamodel.ast.filelevel.PackageDeclaration;
import com.github.javamodel.ast.typedecls.ClassDeclaration;
import com.github.javamodel.ast.typedecls.TypeDeclaration;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import lombok.Data;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.lang.annotation.Annotation;
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

    private static Map<Class, Class<? extends Enum>> ruleClassesHostingTokenToNodeTypes = ImmutableMap.of(
            Java8Parser.ClassModifierContext.class, Modifier.class
    );

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
            AnnotationUsageNode.NODE_TYPE, PackageDeclaration.NODE_TYPE);
    
    private static Map<Class, Class> ruleClassesToNodeClasses;
    private static Map<Class, NodeType> ruleClassesToNodeTypes;

    private static  <A extends Annotation> A getSingleAnnotation(Field field, Class<A> annotationClass){
        A[] annotations = field.getAnnotationsByType(annotationClass);
        if (1 != annotations.length){
            throw new RuntimeException("Expected one "+annotationClass.getName()+" on field " + field);
        }
        return annotations[0];
    }

    private static RelationMapping getRelationMapping(Field field){
        return getSingleAnnotation(field, RelationMapping.class);
    }

    private static AttributeMapping getAttributeMapping(Field field){
        return getSingleAnnotation(field, AttributeMapping.class);
    }

    private static Relation deriveRelation(RelationMapping relationMapping, Field field, Class<? extends ParserRuleContext> ctxClass){
        return new Relation(getType(relationMapping, field, ctxClass), isMultipleRelation(field), field.getName(), null);
    }

    private static Attribute deriveAttribute(AttributeMapping attributeMapping, Field field, Class<? extends ParserRuleContext> ctxClass){
        return new Attribute(isMultipleAttribute(field), field.getName(), (Method)null);
    }

    private static Class<?> getType(RelationMapping relationMapping, Field field, Class<? extends ParserRuleContext> ctxClass) {
        if (isMultipleRelation(field)){
            List<String> methodNames = ctxAccessorName(field);
            for (String methodName : methodNames){
                for (Method method : ctxClass.getDeclaredMethods()){
                    if (method.getName().equals(methodName) && method.getParameterCount() == 1){
                        return findCorrespondingNodeClass(method.getReturnType());
                    }
                }
            }
            throw new RuntimeException();
        } else {
            return field.getType();
        }
    }

    public static <N extends  Node> NodeType deriveFromNodeClass(Class<N> nodeClass) {
        //System.out.println("DERIVING "+nodeClass);
        NodeType nodeType = new NodeType(nodeClass.getSimpleName(), nodeClass);
        RuleMapping[] ruleMappings = nodeClass.getAnnotationsByType(RuleMapping.class);
        if (1 != ruleMappings.length){
            throw new RuntimeException("Expected one RuleMapping on class " + nodeClass);
        }
        if (ruleClassesToNodeClasses == null) ruleClassesToNodeClasses = new HashMap<>();
        if (ruleClassesToNodeTypes == null) ruleClassesToNodeTypes = new HashMap<>();
        ruleClassesToNodeClasses.put(ruleMappings[0].rule(), nodeClass);
        ruleClassesToNodeTypes.put(ruleMappings[0].rule(), nodeType);

        for (Field field : nodeClass.getDeclaredFields()){
            if (field.isAnnotationPresent(RelationMapping.class)){
                RelationMapping relationMapping = getRelationMapping(field);
                nodeType.getRelations().add(deriveRelation(relationMapping, field, ruleMappings[0].rule()));
            } else if (field.isAnnotationPresent(AttributeMapping.class)){
                AttributeMapping attributeMapping = getAttributeMapping(field);
                nodeType.getAttributes().add(deriveAttribute(attributeMapping, field, ruleMappings[0].rule()));
            }
        }

        return nodeType;
    }
    
    private static List<String> ctxAccessorName(Field field){
        RelationMapping[] relationMappings = field.getAnnotationsByType(RelationMapping.class);
        AttributeMapping[] attributeMappings = field.getAnnotationsByType(AttributeMapping.class);
        String accessorName = "";
        if (relationMappings.length > 0) {
            if (relationMappings.length != 1) throw new RuntimeException();
            accessorName = relationMappings[0].ctxAccessorName();
        } else if (attributeMappings.length > 0) {
            if (attributeMappings.length != 1) throw new RuntimeException();
            accessorName = attributeMappings[0].ctxAccessorName();
        } else throw new RuntimeException();
        if (accessorName.isEmpty()) {
            if (isMultipleRelation(field) && field.getName().endsWith("s")){
                String singleName = field.getName().substring(0, field.getName().length() - 1);
                String singleNameLowercase = singleName.substring(0, 1).toUpperCase() + singleName.substring(1);
                return ImmutableList.of(singleName, singleNameLowercase);
            } else {
                return ImmutableList.of(field.getName());
            }
        } else {
            return ImmutableList.of(accessorName);
        }
    }

    private Method ctxAccessor(Field field, Class<? extends RuleContext> ruleContextClass){
        List<String> candidates = ctxAccessorName(field);
        for (String candidateName : candidates){
            for (Method method : ruleContextClass.getDeclaredMethods()){
                if (method.getName().equals(candidateName) && method.getParameterCount() == 0){
                    return method;
                }
            }
        }
        throw new RuntimeException("Class "+ruleContextClass+" was expected to have one method in "+candidates);
    }
    
    private static boolean isMultipleRelation(Field field){
        return List.class.isAssignableFrom(field.getType());
    }

    private static boolean isMultipleAttribute(Field field){
        return List.class.isAssignableFrom(field.getType());
    }
    
    private static NodeType findCorrespondingNodeType(Class ruleContextClass){
        System.out.println("  looking for corresponding class for "+ruleContextClass.getSimpleName());
        NodeType nodeType = ruleClassesToNodeTypes.get(ruleContextClass);
        System.out.println("  node type found is " + nodeType);
        if (nodeType == null){
            throw new RuntimeException("no corresponding nodeType for "+ruleContextClass);
        }
        return nodeType;
    }

    private static Class<?> findCorrespondingNodeClass(Class ruleContextClass){
        System.out.println("  looking for corresponding class for "+ruleContextClass.getSimpleName());
        NodeType nodeType = ruleClassesToNodeTypes.get(ruleContextClass);
        System.out.println("  node type found is " + nodeType);
        if (nodeType == null){
            return ruleClassesHostingTokenToNodeTypes.get(ruleContextClass);
        }
        return nodeType.getNodeClass();
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
                    Class<? extends Enum> enumClazz = ruleClassesHostingTokenToNodeTypes.get(value.getClass());
                    if (enumClazz == null){
                        throw new RuntimeException();
                    }
                    Object enumValue = Enum.valueOf(enumClazz, ctx.getStart().getText().toUpperCase());
                    return enumValue;
                } else {
                    throw new RuntimeException("No alternative found for the transparent type " + value.getClass());
                }
            }
            return getTransparent(node);
        } else {
            return value;
        }
    }

    private Object convertValue(Object originalValueElement, Node node){
        Object valueElement = getTransparent(originalValueElement);
        if (ruleClassesHostingTokenToNodeTypes.values().contains(valueElement.getClass())) {
            Class<? extends Enum> correspondingType = ruleClassesHostingTokenToNodeTypes.get(originalValueElement.getClass());
            System.out.println("  converted to enum type " + correspondingType);
            ParserRuleContext ctx = (ParserRuleContext) originalValueElement;
            Object convertedValue = Enum.valueOf(correspondingType, ctx.getStart().getText().toUpperCase());
            System.out.println("  converted to enum value " + convertedValue);
            return convertedValue;
        } else {
            NodeType elementNodeType = findCorrespondingNodeType(valueElement.getClass());
            Node correspondingElementValue = elementNodeType.fromAntlrNode((ParserRuleContext) valueElement, node);
            System.out.println("  converted to " + correspondingElementValue);
            return correspondingElementValue;
        }
    }

    private Object convertAttributeValue(Object originalValueElement, Node node){
        Object valueElement = getTransparent(originalValueElement);
        if (ruleClassesHostingTokenToNodeTypes.values().contains(valueElement.getClass())) {
            Class<? extends Enum> correspondingType = ruleClassesHostingTokenToNodeTypes.get(originalValueElement.getClass());
            System.out.println("  converted to enum type " + correspondingType);
            ParserRuleContext ctx = (ParserRuleContext) originalValueElement;
            Object convertedValue = Enum.valueOf(correspondingType, ctx.getStart().getText().toUpperCase());
            System.out.println("  converted to enum value " + convertedValue);
            return convertedValue;
        } else if (TerminalNode.class.isAssignableFrom(valueElement.getClass())) {
            TerminalNode terminalNode = (TerminalNode)valueElement;
            return terminalNode.getText();
        } else {
            throw new RuntimeException();
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
                    try {
                        Method ctxAccessor = ctxAccessor(field, ruleContext.getClass());
                        Object value = ctxAccessor.invoke(ruleContext);
                        System.out.println("  Value obtained is " + value);
                        if (isMultipleRelation(field)){
                            System.out.println("  multiple relation");
                            if (value != null) {
                                List valueAsList = (List) value;
                                List convertedValues = new ArrayList<>();
                                for (Object originalValueElement : filter(valueAsList, relationMappings[0])){
                                    // TODO if the class is "transparent" we should get the "concrete" one and use that one to find the element type
                                    System.out.println("  value element "+originalValueElement.getClass());
                                    convertedValues.add(convertValue(originalValueElement, node));
                                }
                                field.setAccessible(true);
                                field.set(node, convertedValues);
                                field.setAccessible(false);
                            }
                        } else {
                            System.out.println("  single relation");
                            if (value != null) {
                                Object convertedValue = convertValue(value, node);
                                field.setAccessible(true);
                                field.set(node, convertedValue);
                                field.setAccessible(false);
                            }
                        }
                    } catch (InvocationTargetException e){
                        throw new RuntimeException("Because "+nodeClass.getCanonicalName()+" has field "+field.getName()+" we expect "+ruleContext.getClass(), e);
                    }
                } else if (field.isAnnotationPresent(AttributeMapping.class)){
                        AttributeMapping[] attributeMappings = field.getAnnotationsByType(AttributeMapping.class);
                        if (attributeMappings.length != 1) throw new RuntimeException();
                        System.out.println("Considering field " + field.getName());
                        try {
                            Method ctxAccessor = ctxAccessor(field, ruleContext.getClass());
                            Object value = ctxAccessor.invoke(ruleContext);
                            System.out.println("  Value obtained is " + value);
                            if (isMultipleAttribute(field)){
                                System.out.println("  multiple attribute");
                                if (value != null) {
                                    List valueAsList = (List) value;
                                    List convertedValues = new ArrayList<>();
                                    for (Object originalValueElement : filter(valueAsList, attributeMappings[0])){
                                        // TODO if the class is "transparent" we should get the "concrete" one and use that one to find the element type
                                        System.out.println("  value element "+originalValueElement.getClass());
                                        convertedValues.add(convertAttributeValue(originalValueElement, node));
                                    }
                                    field.setAccessible(true);
                                    field.set(node, convertedValues);
                                    field.setAccessible(false);
                                }
                            } else {
                                System.out.println("  single attribute");
                                if (value != null) {
                                    Object convertedValue = convertAttributeValue(value, node);
                                    field.setAccessible(true);
                                    field.set(node, convertedValue);
                                    field.setAccessible(false);
                                }
                            }
                        } catch (InvocationTargetException e){
                            throw new RuntimeException("Because "+nodeClass.getCanonicalName()+" has field "+field.getName()+" we expect "+ruleContext.getClass(), e);
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

    private Iterable<? extends Object> filter(List<Object> valueAsList, AttributeMapping attributeMapping) {
        if (attributeMapping.filter().isEmpty()){
            return valueAsList;
        } else {
            boolean negated = attributeMapping.filter().startsWith("!");
            final String methodName = negated ? attributeMapping.filter().substring(1) : attributeMapping.filter();
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
