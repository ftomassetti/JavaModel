package com.github.javamodel.ast.reflection;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.annotations.AttributeMapping;
import com.github.javamodel.annotations.RelationMapping;
import com.github.javamodel.ast.AstNode;
import lombok.Data;
import lombok.Getter;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

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

    /**
     * Get the attributes sorted by name.
     */
    public List<Attribute> getSortedAttributes() {
        List<Attribute> as = new LinkedList<>();
        as.addAll(attributes);
        as.sort((a, b) -> a.getName().compareTo(b.getName()));
        return as;
    }

    /**
     * Get the relations sorted by name.
     */
    public List<Relation> getSortedRelations() {
        List<Relation> as = new LinkedList<>();
        as.addAll(relations);
        as.sort((a, b) -> a.getName().compareTo(b.getName()));
        return as;
    }

    AstNodeType(String name, Class<N> nodeClass) {
        this.name = name;
        this.nodeClass = nodeClass;
    }

    public static class Builder<N extends AstNode> {

        private String name;
        private List<Relation> relations = new ArrayList<>();
        private List<Attribute> attributes = new ArrayList<>();
        private Class<N> nodeClass;

        public Builder(String name, Class<N> nodeClass) {
            this.name = name;
            this.nodeClass = nodeClass;
        }

        public Builder addRelation(Relation relation) {
            relations.add(relation);
            return this;
        }

        public Builder addAttribute(Attribute attribute) {
            attributes.add(attribute);
            return this;
        }

        public AstNodeType<N> build() {
            AstNodeType nodeType = new AstNodeType(name, nodeClass);
            nodeType.relations.addAll(relations);
            nodeType.attributes.addAll(attributes);
            return nodeType;
        }
    }

    private void debug(String msg) {
        //System.out.println(msg);
    }

    private void assignRelation(N node, Field field, ParserRuleContext antlrNode) {
        RelationMapping relationMapping = ReflectionUtils.getSingleAnnotation(field, RelationMapping.class);
        Object antlrValue =null;
        try {
            Method ctxAccessor = AstNodeTypeDeriver.ctxAccessor(field, antlrNode.getClass());
            antlrValue = ctxAccessor.invoke(antlrNode);
            if (antlrValue == null) return;
            antlrValue = unwrapValue(antlrValue);
            if (AstNodeTypeDeriver.isMultipleRelation(field)) {
                List antlrValueList = (List) antlrValue;
                List astValueList = new ArrayList<>();
                for (Object antlrSingleValue : filter(antlrValueList, relationMapping)) {
                    astValueList.add(antlrToAstRelationValue(antlrSingleValue, node));
                }
                ReflectionUtils.assignField(field, node, astValueList);
            } else {
                Object astValue = antlrToAstRelationValue(antlrValue, node);
                ReflectionUtils.assignField(field, node, astValue);
            }
        } catch (RuntimeException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException("Assigning value "+antlrValue.getClass()+" relation to field " + field.getName() + " nodeClass " + nodeClass.getCanonicalName()  + " we expect " + antlrNode.getClass(), e);
        }
    }

    private void assignAttribute(N node, Field field, ParserRuleContext antlrNode) {
        AttributeMapping attributeMapping = ReflectionUtils.getSingleAnnotation(field, AttributeMapping.class);
        try {
            Method ctxAccessor = AstNodeTypeDeriver.ctxAccessor(field, antlrNode.getClass());
            Object antlrValue = ctxAccessor.invoke(antlrNode);
            if (antlrValue == null) return;
            if (AstNodeTypeDeriver.isMultipleAttribute(field)) {
                List antlrValueList = (List) antlrValue;
                List astValueList = new ArrayList<>();
                for (Object antlrSingleValue : filter(antlrValueList, attributeMapping)) {
                    astValueList.add(antlrToAstAttributeValue(antlrSingleValue, node));
                }
                ReflectionUtils.assignField(field, node, astValueList);
            } else {
                Object astValue = antlrToAstAttributeValue(antlrValue, node);
                ReflectionUtils.assignField(field, node, astValue);
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException("Because " + nodeClass.getCanonicalName() + " has field " + field.getName() + " we expect " + antlrNode.getClass(), e);
        }
    }

    /**
     * Given an Antlr node it derives an AstNode of type N.
     */
    public N fromAntlrNode(ParserRuleContext antlrNode, AstNode astParentNode) {
        try {
            N node;

            debug("\n=== AntlrNode " + antlrNode.getClass().getSimpleName() + " ===\n");

            // Instantiation
            if (antlrNode instanceof Java8Parser.CompilationUnitContext) {
                // This should be the case only for the CompilationUnit which has no parent
                node = nodeClass.newInstance();
            } else {
                node = nodeClass.getConstructor(AstNode.class).newInstance(astParentNode);
            }

            for (Field field : nodeClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(RelationMapping.class)) {
                    assignRelation(node, field, antlrNode);
                } else if (field.isAnnotationPresent(AttributeMapping.class)) {
                    assignAttribute(node, field, antlrNode);
                }
            }
            return node;
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException | RuntimeException e) {
            throw new RuntimeException("Transforming AntlrNode "+antlrNode.getClass(), e);
        }
    }

    private static Class<? extends Enum> getCorrespondingTypeEnum(Class clazz){
        if (AstNodeTypeDeriver.ruleClassesHostingTokenToNodeTypes.keySet().contains(clazz)){
            return AstNodeTypeDeriver.ruleClassesHostingTokenToNodeTypes.get(clazz);
        }
        if (clazz.getSuperclass() == null){
            return null;
        }
        return getCorrespondingTypeEnum(clazz.getSuperclass());
    }

    static boolean hasCorrespondingTypeEnum(Class clazz){
        return getCorrespondingTypeEnum(clazz) != null;
    }

    static Object antlrToAstRelationValue(Object antlrValue, AstNode astNode) {
        try {
            antlrValue = AstNodeTypeDeriver.skipAlternativeValues(antlrValue);
            if (hasCorrespondingTypeEnum(antlrValue.getClass())) {
                Class<? extends Enum> correspondingType = getCorrespondingTypeEnum(antlrValue.getClass());
                ParserRuleContext ctx = (ParserRuleContext) antlrValue;
                Object convertedValue = Enum.valueOf(correspondingType, ctx.getStart().getText().toUpperCase());
                return convertedValue;
            } else {
                AstNodeType elementNodeType = AstNodeTypeDeriver.findCorrespondingNodeType(antlrValue.getClass());
                Method instantiationMethod = ReflectionUtils.methodByName(elementNodeType.getNodeClass(), "fromAntlrNode");
                if (instantiationMethod != null) {
                    try {
                        return instantiationMethod.invoke(null, astNode, antlrValue);
                    } catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException e) {
                        throw new RuntimeException("invoking " + instantiationMethod + "with " + antlrValue.getClass() + ". AntlrValue " + antlrValue.getClass() + ". Element node type " + elementNodeType.getNodeClass(), e);
                    }
                }
                AstNode correspondingElementValue = elementNodeType.fromAntlrNode((ParserRuleContext) antlrValue, astNode);
                return correspondingElementValue;
            }
        } catch (Exception e){
            throw new RuntimeException("antlrToAstRelationValue "+antlrValue.getClass(), e);
        }
    }

    static Object antlrToAstAttributeValue(Object antlrValue, AstNode astNode) {
        Object valueElement = AstNodeTypeDeriver.skipAlternativeValues(antlrValue);
        if (AstNodeTypeDeriver.ruleClassesHostingTokenToNodeTypes.values().contains(valueElement.getClass())) {
            Class<? extends Enum> correspondingType = AstNodeTypeDeriver.ruleClassesHostingTokenToNodeTypes.get(antlrValue.getClass());
            ParserRuleContext ctx = (ParserRuleContext) antlrValue;
            Object convertedValue = Enum.valueOf(correspondingType, ctx.getStart().getText().toUpperCase());
            return convertedValue;
        } else if (TerminalNode.class.isAssignableFrom(valueElement.getClass())) {
            TerminalNode terminalNode = (TerminalNode) valueElement;
            return terminalNode.getText();
        } else {
            throw new RuntimeException("ValueElement class "+valueElement.getClass());
        }
    }

    private Object unwrapValue(Object value) {
        if (AstNodeTypeDeriver.wrappingTypes.contains(value.getClass())) {
            Class type = value.getClass();
            Set<Method> methods = new HashSet<>();
            for (Method method : type.getDeclaredMethods()) {
                if (method.getParameterCount() == 0 && !method.getName().equals("getRuleIndex")) {
                    methods.add(method);
                }
            }
            if (methods.size() == 1) {
                Method m = methods.iterator().next();
                try {
                    if (List.class.equals(m.getReturnType())) {
                        m = type.getDeclaredMethod(m.getName());
                        return unwrapValue(m.invoke(value));
                    } else {
                        return unwrapValue(m.invoke(value));
                    }
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new RuntimeException("skipWrappingType " + type.getName() + ", looking for methods " + methods);
            }
        } else return value;
    }

    private Iterable<? extends Object> filter(List<Object> valueAsList, RelationMapping relationMapping) {
        if (relationMapping.filter().isEmpty()) {
            return valueAsList;
        } else {
            boolean negated = relationMapping.filter().startsWith("!");
            final String methodName = negated ? relationMapping.filter().substring(1) : relationMapping.filter();
            return valueAsList.stream().filter((value) -> {
                try {
                    Method method = value.getClass().getDeclaredMethod(methodName);
                    boolean isNull = null == method.invoke(value);
                    return isNull == negated;
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
        }
    }

    private Iterable<? extends Object> filter(List<Object> valueAsList, AttributeMapping attributeMapping) {
        if (attributeMapping.filter().isEmpty()) {
            return valueAsList;
        } else {
            boolean negated = attributeMapping.filter().startsWith("!");
            final String methodName = negated ? attributeMapping.filter().substring(1) : attributeMapping.filter();
            return valueAsList.stream().filter((value) -> {
                try {
                    Method method = value.getClass().getDeclaredMethod(methodName);
                    boolean isNull = null == method.invoke(value);
                    return isNull == negated;
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
        }
    }
}
