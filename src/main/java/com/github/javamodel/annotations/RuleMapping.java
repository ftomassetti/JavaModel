package com.github.javamodel.annotations;

import org.antlr.v4.runtime.ParserRuleContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by ftomassetti on 22/05/15.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RuleMapping {
    Class<? extends ParserRuleContext> rule();
}
