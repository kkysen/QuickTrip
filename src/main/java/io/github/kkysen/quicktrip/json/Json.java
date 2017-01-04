package io.github.kkysen.quicktrip.json;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 
 * 
 * @author Khyber Sen
 */
@Inherited
@Documented
@Retention(SOURCE)
@Target(TYPE)
public @interface Json {}
