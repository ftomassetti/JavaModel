package com.github.javamodel.ast.reflection;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.annotations.AttributeMapping;
import com.github.javamodel.annotations.RelationMapping;
import com.github.javamodel.annotations.RuleMapping;
import com.github.javamodel.ast.common.*;
import com.github.javamodel.ast.AstNode;
import com.github.javamodel.ast.filelevel.PackageDeclaration;
import com.github.javamodel.ast.typedecls.ClassDeclaration;
import com.github.javamodel.ast.typedecls.ClassElement;
import com.github.javamodel.ast.typedecls.TypeDeclaration;
import com.github.javamodel.ast.typedecls.TypeParameter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import lombok.Data;
import lombok.Getter;
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
public class AstNodeType<N extends AstNode> {

    @Getter
    private Set<Relation> relations = new HashSet<>();
    @Getter
    private Set<Attribute> attributes = new HashSet<>();
    private String name;
    private Class<N> nodeClass;

    public List<Attribute> getSortedAttributes(){
        List<Attribute> as = new LinkedList<>();
        as.addAll(attributes);
        as.sort((a,b)->a.getName().compareTo(b.getName()));
        return as;
    }

    public List<Relation> getSortedRelations(){
        List<Relation> as = new LinkedList<>();
        as.addAll(relations);
        as.sort((a,b)->a.getName().compareTo(b.getName()));
        return as;
    }

    AstNodeType(String name, Class<N> nodeClass){
        this.name = name;
        this.nodeClass = nodeClass;
    }

    public static class Builder<N extends AstNode> {

        private String name;
        private List<Relation> relations = new ArrayList<>();
        private List<Attribute> attributes = new ArrayList<>();
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

        public AstNodeType<N> build(){
            AstNodeType nodeType = new AstNodeType(name, nodeClass);
            nodeType.relations.addAll(relations);
            nodeType.attributes.addAll(attributes);
            return nodeType;
        }
    }

    public N fromAntlrNode(ParserRuleContext ruleContext, AstNode parentNode){
        try {
            N node = null;
            if (parentNode == null) {
                node = nodeClass.newInstance();
            } else {
                node = nodeClass.getConstructor(AstNode.class).newInstance(parentNode);
            }
            System.out.println("\n=== AntlrNode "+ruleContext.getClass().getSimpleName()+" ===\n");
            for (Field field : nodeClass.getDeclaredFields()){
                if (field.isAnnotationPresent(RelationMapping.class)){
                    RelationMapping[] relationMappings = field.getAnnotationsByType(RelationMapping.class);
                    if (relationMappings.length != 1) throw new RuntimeException();
                    System.out.println("Considering field " + field.getName());
                    try {
                        Method ctxAccessor = AstNodeTypeDeriver.ctxAccessor(field, ruleContext.getClass());
                        Object value = ctxAccessor.invoke(ruleContext);
                        System.out.println("  Value obtained is " + value);
                        if (AstNodeTypeDeriver.isMultipleRelation(field)){
                            System.out.println("  multiple relation");
                            if (value != null) {
                                value = unwrapValue(value);
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
                        Method ctxAccessor = AstNodeTypeDeriver.ctxAccessor(field, ruleContext.getClass());
                        Object value = ctxAccessor.invoke(ruleContext);
                        System.out.println("  Value obtained is " + value);
                        if (AstNodeTypeDeriver.isMultipleAttribute(field)){
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

    private Object convertValue(Object originalValueElement, AstNode node){
        Object valueElement = AstNodeTypeDeriver.getTransparent(originalValueElement);
        if (AstNodeTypeDeriver.ruleClassesHostingTokenToNodeTypes.values().contains(valueElement.getClass())) {
            Class<? extends Enum> correspondingType = AstNodeTypeDeriver.ruleClassesHostingTokenToNodeTypes.get(originalValueElement.getClass());
            System.out.println("  converted to enum type " + correspondingType);
            ParserRuleContext ctx = (ParserRuleContext) originalValueElement;
            Object convertedValue = Enum.valueOf(correspondingType, ctx.getStart().getText().toUpperCase());
            System.out.println("  converted to enum value " + convertedValue);
            return convertedValue;
        } else {
            AstNodeType elementNodeType = AstNodeTypeDeriver.findCorrespondingNodeType(valueElement.getClass());
            AstNode correspondingElementValue = elementNodeType.fromAntlrNode((ParserRuleContext) valueElement, node);
            System.out.println("  converted to " + correspondingElementValue);
            return correspondingElementValue;
        }
    }

    private Object convertAttributeValue(Object originalValueElement, AstNode node){
        Object valueElement = AstNodeTypeDeriver.getTransparent(originalValueElement);
        if (AstNodeTypeDeriver.ruleClassesHostingTokenToNodeTypes.values().contains(valueElement.getClass())) {
            Class<? extends Enum> correspondingType = AstNodeTypeDeriver.ruleClassesHostingTokenToNodeTypes.get(originalValueElement.getClass());
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

    private Object unwrapValue(Object value){
        if (AstNodeTypeDeriver.wrappingTypes.contains(value.getClass())){
            Class type = value.getClass();
            Set<Method> methods = new HashSet<>();
            for (Method method : type.getDeclaredMethods()){
                if (method.getParameterCount() == 0 && !method.getName().equals("getRuleIndex")){
                    methods.add(method);
                }
            }
            if (methods.size() == 1){
                Method m = methods.iterator().next();
                try {
                    if (List.class.equals(m.getReturnType())) {
                        m = type.getDeclaredMethod(m.getName());
                        return unwrapValue(m.invoke(value));
                    } else {
                        return unwrapValue(m.invoke(value));
                    }
                } catch (IllegalAccessException | InvocationTargetException |NoSuchMethodException e){
                    throw new RuntimeException(e);
                }
            } else {
                throw new RuntimeException("skipWrappingType " + type.getName() + ", looking for methods " + methods);
            }
        } else return value;
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
}
