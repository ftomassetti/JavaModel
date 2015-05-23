package com.github.javamodel.annotations;

import org.antlr.v4.runtime.ParserRuleContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Define the association between an AstNode class and an Antlr node class.
 *
 * @author Federico Tomassetti
 * @since May 2015
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RuleMapping {

    /**
     * Type of the corresponding Antlr node.
     */
    Class<? extends ParserRuleContext> rule();
}
