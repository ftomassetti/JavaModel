package com.github.javamodel.annotations;

import com.github.javamodel.ast.AstNode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark a field as representing an {@link com.github.javamodel.ast.reflection.Relation} of an AstNode.
 *
 * @author Federico Tomassetti
 * @since May 2015
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RelationMapping {

    /**
     * By default the name of the corresponding accessor in the AntlrNode is derived by the field name.
     */
    String ctxAccessorName() default "";

    /**
     * Filter is the name of the method to invoke: if it returns null the value should not be considered
     * the filter can be negated, if it starts with a bang (!)
     */
    String filter() default "";

    /**
     * The type of the AstNode target of this relation can be always determined by the field type for single
     * relations, it is not always the case for relations returning a List (because of generic erasure, damn Java!)
     */
    Class<? extends AstNode> type() default AstNode.class;
}
