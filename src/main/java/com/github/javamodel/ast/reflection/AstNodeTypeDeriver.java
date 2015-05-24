package com.github.javamodel.ast.reflection;

import com.github.javamodel.Java8Parser;
import com.github.javamodel.annotations.AttributeMapping;
import com.github.javamodel.annotations.RelationMapping;
import com.github.javamodel.annotations.RuleMapping;
import com.github.javamodel.ast.AstNode;
import com.github.javamodel.ast.common.*;
import com.github.javamodel.ast.filelevel.PackageDeclaration;
import com.github.javamodel.ast.typedecls.*;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;


/**
 * It derives AstNodeType from Antlr RuleContext classes.
 */
public final class AstNodeTypeDeriver {

    private AstNodeTypeDeriver(){
        // prevent instantiation
    }

    /**
     * In some cases we want to associate to a simple token a class different from a String,
     * like an enum.
     */
    static Map<Class, Class<? extends Enum>> ruleClassesHostingTokenToNodeTypes = ImmutableMap.of(
            Java8Parser.ClassModifierContext.class, Modifier.class
    );

    /**
     * There are types which just represent alternatives: we want to ignore them.
     */
    static Set<Class> alternativeTypes = ImmutableSet.of(
            Java8Parser.TypeDeclarationContext.class,
            Java8Parser.ClassDeclarationContext.class,
            Java8Parser.ClassBodyContext.class,
            Java8Parser.ClassBodyDeclarationContext.class,
            Java8Parser.ClassMemberDeclarationContext.class,
            Java8Parser.UnannTypeContext.class,
            //Java8Parser.UnannPrimitiveTypeContext.class,
            Java8Parser.FieldModifierContext.class,
            Java8Parser.VariableDeclaratorListContext.class,
            Java8Parser.ClassModifierContext.class,
            Java8Parser.ClassBodyDeclarationContext.class);

    /**
     * There are types having just one element: we want to ignore them.
     */
    static Set<Class> wrappingTypes = ImmutableSet.of(
            Java8Parser.TypeParametersContext.class,
            Java8Parser.TypeParameterListContext.class,
            Java8Parser.SuperinterfacesContext.class,
            Java8Parser.SuperclassContext.class,
            Java8Parser.InterfaceTypeListContext.class,
            Java8Parser.ClassBodyContext.class,
            Java8Parser.ElementValuePairListContext.class,
            Java8Parser.VariableDeclaratorListContext.class,
            Java8Parser.TypeArgumentContext.class,
            Java8Parser.TypeArgumentListContext.class);

    // This force the classes to be loaded
    private static Set<Object> nodeClasses = ImmutableSet.of(
            TypeParameter.NODE_TYPE,
            InterfaceTypeRef.NODE_TYPE,
            AnnotationValuePair.NODE_TYPE,
            AnnotationValue.NODE_TYPE,
            AnnotationUsageNode.NODE_TYPE,
            ClassTypeRef.NODE_TYPE,
            MarkerAnnotationUsage.NODE_TYPE,
            SingleElementAnnotationUsage.NODE_TYPE,
            MultipleElementsAnnotationUsage.NODE_TYPE,
            PrimitiveTypeRef.NODE_TYPE,
            VariableDeclarator.NODE_TYPE,
            FieldDeclaration.NODE_TYPE,
            ClassElement.NODE_TYPE,
            TypeDeclaration.NODE_TYPE, ClassDeclaration.NODE_TYPE,
            AnnotationUsageNode.NODE_TYPE, PackageDeclaration.NODE_TYPE);

    private static Map<Class, AstNodeType> ruleClassesToNodeTypes;

    private static RelationMapping getRelationMapping(Field field){
        return ReflectionUtils.getSingleAnnotation(field, RelationMapping.class);
    }

    private static AttributeMapping getAttributeMapping(Field field){
        return ReflectionUtils.getSingleAnnotation(field, AttributeMapping.class);
    }

    private static Relation deriveRelation(RelationMapping relationMapping, Field field, Class<? extends ParserRuleContext> ctxClass){
        Class<?> typeClass = getType(relationMapping, field, ctxClass);
        if (typeClass == null){
            throw new RuntimeException("Finding type for field "+field);
        }
        return new Relation(typeClass, isMultipleRelation(field), field.getName());
    }

    private static Attribute deriveAttribute(AttributeMapping attributeMapping, Field field, Class<? extends ParserRuleContext> ctxClass){
        return new Attribute(isMultipleAttribute(field), field.getName(), getType(attributeMapping, field, ctxClass));
    }

    private static Class<?> skipWrappingType(Class<?> type){
        if (wrappingTypes.contains(type)){
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
                        m = type.getDeclaredMethod(m.getName(), int.class);
                        return skipWrappingType(m.getReturnType());
                    } else {
                        return skipWrappingType(methods.iterator().next().getReturnType());
                    }
                } catch (NoSuchMethodException e){
                    throw new RuntimeException(e);
                }
            } else {
                throw new RuntimeException("skipWrappingType " + type.getName() + ", looking for methods " + methods);
            }
        } else {
            return type;
        }
    }

    private static Class<?> getType(RelationMapping relationMapping, Field field, Class<? extends ParserRuleContext> ctxClass) {
        if (AstNode.class != relationMapping.type()){
            return relationMapping.type();
        }
        if (isMultipleRelation(field)){
            List<String> methodNames = ctxAccessorName(field);
            // first look for methods with 1 param
            for (String methodName : methodNames){
                for (Method method : ctxClass.getDeclaredMethods()){
                    if (method.getName().equals(methodName) && method.getParameterCount() == 1){
                        Class<?> type = skipWrappingType(method.getReturnType());
                        Class<?> clazz = findCorrespondingNodeClass(type);
                        if (clazz == null){
                            throw new RuntimeException("No correspondence found for "+type);
                        }
                        return clazz;
                    }
                }
            }
            // first look for methods with 0 param
            for (String methodName : methodNames){
                for (Method method : ctxClass.getDeclaredMethods()){
                    if (method.getName().equals(methodName) && method.getParameterCount() == 0){
                        Class<?> type = skipWrappingType(method.getReturnType());
                        Class<?> clazz = findCorrespondingNodeClass(type);
                        if (clazz == null){
                            throw new RuntimeException("No correspondence found for "+type);
                        }
                        return clazz;
                    }
                }
            }
            throw new RuntimeException("Unable to find type for "+field+", methodNames "+methodNames+" in class "+ctxClass.getName());
        } else {
            return field.getType();
        }
    }

    private static Class<?> getType(AttributeMapping relationMapping, Field field, Class<? extends ParserRuleContext> ctxClass) {
        if (isMultipleAttribute(field)){
            List<String> methodNames = ctxAccessorName(field);
            for (String methodName : methodNames){
                for (Method method : ctxClass.getDeclaredMethods()){
                    if (method.getName().equals(methodName) && method.getParameterCount() == 1){
                        if (ruleClassesHostingTokenToNodeTypes.containsKey(method.getReturnType())){
                            return ruleClassesHostingTokenToNodeTypes.get(method.getReturnType());
                        } else {
                            return String.class;
                        }
                    }
                }
            }
            throw new RuntimeException();
        } else {
            return field.getType();
        }
    }

    public static <N extends AstNode> AstNodeType deriveFromNodeClass(Class<N> nodeClass) {
        AstNodeType nodeType = new AstNodeType(nodeClass.getSimpleName(), nodeClass);

        if (!nodeClass.getSuperclass().equals(AstNode.class)){
            try {
                AstNodeType superNodeType = (AstNodeType)nodeClass.getSuperclass().getDeclaredField("NODE_TYPE").get(null);
                nodeType.getAttributes().addAll(superNodeType.getAttributes());
                nodeType.getRelations().addAll(superNodeType.getRelations());
            } catch (NoSuchFieldException | IllegalAccessException e){
                throw new RuntimeException(e);
            }
        }

        RuleMapping[] ruleMappings = nodeClass.getAnnotationsByType(RuleMapping.class);
        if (1 != ruleMappings.length){
            throw new RuntimeException("Expected one RuleMapping on class " + nodeClass);
        }
        if (ruleClassesToNodeTypes == null) {
            ruleClassesToNodeTypes = new HashMap<>();
            ruleClassesToNodeTypes.put(Java8Parser.PrimitiveTypeContext.class, PrimitiveTypeRef.NODE_TYPE);
        }
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
                return ImmutableList.of(singleName, singleNameLowercase, singleName + "s", singleNameLowercase + "s");
            } else {
                return ImmutableList.of(field.getName());
            }
        } else {
            return ImmutableList.of(accessorName);
        }
    }

    static Method ctxAccessor(Field field, Class<? extends RuleContext> ruleContextClass){
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

    static boolean isMultipleRelation(Field field){
        return List.class.isAssignableFrom(field.getType());
    }

    static boolean isMultipleAttribute(Field field){
        return List.class.isAssignableFrom(field.getType());
    }

    static AstNodeType findCorrespondingNodeType(Class ruleContextClass){
        AstNodeType nodeType = ruleClassesToNodeTypes.get(ruleContextClass);
        if (nodeType == null){
            if (ruleContextClass.getSuperclass() == null || ruleContextClass.getSuperclass() == ParserRuleContext.class){
                throw new RuntimeException("no corresponding nodeType for "+ruleContextClass);
            }
            try {
                return findCorrespondingNodeType(ruleContextClass.getSuperclass());
            } catch (RuntimeException e){
                throw new RuntimeException("no corresponding nodeType for "+ruleContextClass, e);
            }
        }
        return nodeType;
    }

    private static Class<?> findCorrespondingNodeClass(Class ruleContextClass){
        AstNodeType nodeType = ruleClassesToNodeTypes.get(ruleContextClass);
        if (nodeType == null){
            return ruleClassesHostingTokenToNodeTypes.get(ruleContextClass);
        }
        if (nodeType.getNodeClass() == null){
            throw new RuntimeException();
        }
        return nodeType.getNodeClass();
    }

    static Object skipAlternativeValues(Object value){
        if (alternativeTypes.contains(value.getClass())){
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
            return skipAlternativeValues(node);
        } else {
            return value;
        }
    }

}
