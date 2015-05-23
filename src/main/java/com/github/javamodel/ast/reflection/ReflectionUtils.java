package com.github.javamodel.ast.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Created by federico on 23/05/15.
 */
public class ReflectionUtils {

    public static  <A extends Annotation> A getSingleAnnotation(Field field, Class<A> annotationClass){
        A[] annotations = field.getAnnotationsByType(annotationClass);
        if (1 != annotations.length){
            throw new RuntimeException("Expected one "+annotationClass.getName()+" on field " + field);
        }
        return annotations[0];
    }

    public static void assignField(Field field, Object instance, Object value){
        field.setAccessible(true);
        try {
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        field.setAccessible(false);
    }
}
