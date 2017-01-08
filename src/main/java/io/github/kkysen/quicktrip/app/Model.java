package io.github.kkysen.quicktrip.app;

import io.github.kkysen.quicktrip.Constants;
import io.github.kkysen.quicktrip.io.MyFiles;
import io.github.kkysen.quicktrip.reflect.annotations.AnnotationUtils;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 
 * 
 * @author Khyber Sen
 */
public interface Model {
    
    /**
     * validates all the input, placing all valid input into the real,
     * non-transient model
     * validates all no-arg methods marked with {@link Validation}
     * 
     * @return true if the input was valid
     * @throws InputError if there's any invalid input
     */
    public default boolean validate() throws InputError {
        boolean validated = true;
        for (final Method method : getClass().getDeclaredMethods()) {
            method.setAccessible(true);
            final Validation anno = AnnotationUtils.getAnnotation(method, Validation.class);
            if (anno == null || !anno.value() || method.getParameterCount() != 0) {
                continue;
            }
            Object retValue;
            try {
                retValue = method.invoke(this);
            } catch (final IllegalAccessException e) {
                throw new RuntimeException(e); // shouldn't happen
            } catch (final IllegalArgumentException e) {
                throw new RuntimeException(e); // shouldn't happen
            } catch (final InvocationTargetException e) {
                final Throwable cause = e.getCause();
                if (cause instanceof InputError) {
                    throw (InputError) cause;
                } else {
                    throw new RuntimeException(cause);
                }
            }
            if (method.getReturnType().equals(Boolean.TYPE)) {
                validated = validated && (boolean) retValue;
            }
        }
        return validated;
    }
    
    public default void serialize(final Path path) throws IOException {
        final String json = QuickTripConstants.GSON.toJson(this);
        MyFiles.write(path, json);
    }
    
    public static <T extends Model> T deserialize(final Path path, final Class<? extends T> type)
            throws IOException {
        final Reader reader = Files.newBufferedReader(path, Constants.CHARSET);
        return QuickTripConstants.GSON.fromJson(reader, type);
    }
    
}
