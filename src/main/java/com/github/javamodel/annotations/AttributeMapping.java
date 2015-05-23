package com.github.javamodel.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by ftomassetti on 22/05/15.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AttributeMapping {
    String ctxAccessorName() default "";
    // Filter is the name of the method to invoke: if it returns null the value should not be considered
    // the filter can be negated, if it starts with a bang (!)
    String filter() default "";
}
