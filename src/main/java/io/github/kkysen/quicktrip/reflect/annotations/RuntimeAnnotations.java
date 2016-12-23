package io.github.kkysen.quicktrip.reflect.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.function.Consumer;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class RuntimeAnnotations {
    
    public static void processMethods(final Class<?> type, final Consumer<Method> methodProcessor) {
        for (final Method method : type.getDeclaredMethods()) {
            methodProcessor.accept(method);
        }
    }
    
    public static void processMethodsWithAnnotation(final Class<?> type,
            final Class<? extends Annotation> annotation, final Consumer<Method> methodProcessor) {
        processMethods(type, method -> {
            if (method.isAnnotationPresent(annotation)) {
                methodProcessor.accept(method);
            }
        });
    }
    
}
