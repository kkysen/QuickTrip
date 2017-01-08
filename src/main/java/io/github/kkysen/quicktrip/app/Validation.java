package io.github.kkysen.quicktrip.app;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;


@Documented
@Retention(RUNTIME)
@Target(METHOD)
/**
 * 
 * 
 * @author Khyber Sen
 */
public @interface Validation {
    
    public boolean value() default true;
    
}
