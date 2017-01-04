package io.github.kkysen.quicktrip.apis;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import io.github.kkysen.quicktrip.apis.CachedApiRequest.QueryParams;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * signifies that a field is a @QueryField
 * will be found through reflection and added to the
 * {@link QueryParams} in {@link CachedApiRequest}
 * if {@link #include()} = false, then it will not be automatically added and must be done
 * in {@link CachedApiRequest#modifyQuery(java.util.Map)}
 * 
 * @see CachedApiRequest#modifyQuery(java.util.Map)
 * @see CachedApiRequest#reflectQuery()
 * 
 * @author Khyber Sen
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface QueryField {
    
    public boolean include() default true;
    
    public boolean encode() default true;
    
    public String name() default "";
    
}
