package io.github.kkysen.quicktrip.util.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class AnnotationUtils {
    
    /**
     * @param element
     * @param annotation
     * @return the Annotation of that class, null if none exist
     */
    @SuppressWarnings("unchecked")
    public static <T extends Annotation> T getAnnotation(final AnnotatedElement element,
            final Class<T> annotation) {
        final Annotation[] annotations = element.getDeclaredAnnotations();
        for (final Annotation anno : annotations) {
            if (anno.annotationType().equals(annotation)) {
                return (T) anno;
            }
        }
        return null;
    }
    
}
