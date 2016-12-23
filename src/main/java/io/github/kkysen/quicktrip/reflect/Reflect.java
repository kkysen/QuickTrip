package io.github.kkysen.quicktrip.reflect;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * static versions of methods in Type
 * 
 * @see Type
 * 
 * @author Khyber Sen
 */
public class Reflect {
    
    private static <T> Type<T> type(final T t) {
        return new Type<T>(t.getClass());
    }
    
    public static <T> List<Field> getFields(final T t) {
        return type(t).getFields();
    }
    
    public static <T> List<Field> getInstanceVars(final T t) {
        return type(t).getInstanceVars();
    }
    
    public static <T> Map<Field, Object> getFieldEntries(final List<Field> fields, final T t) {
        return type(t).getFieldEntries(fields, t);
    }
    
    public static <T> Map<Field, Object> getFieldEntries(final T t) {
        return type(t).getFieldEntries(t);
    }
    
    public static <T> Map<Field, Object> getInstanceVarEntries(final T t) {
        return type(t).getInstanceVarEntries(t);
    }
    
    public static <T> Map<String, Object> getStringFieldEntries(final List<Field> fields,
            final T t) {
        return type(t).getStringFieldEntries(fields, t);
    }
    
    public static <T> Map<String, Object> getStringFieldEntries(final T t) {
        return type(t).getStringFieldEntries(t);
    }
    
    public static <T> Map<String, Object> getStringInstanceVarEntries(final T t) {
        return type(t).getStringInstanceVarEntries(t);
    }
    
    public static <T> Map<String, String> getAllStringFieldEntries(final List<Field> fields,
            final T t) {
        return type(t).getAllStringFieldEntries(fields, t);
    }
    
    public static <T> Map<String, String> getAllStringFieldEntries(final T t) {
        return type(t).getAllStringFieldEntries(t);
    }
    
    public static <T> Map<String, String> getAllStringInstanceVarEntries(final T t) {
        return type(t).getAllStringInstanceVarEntries(t);
    }
    
    public static <T> List<String> toStringList(final List<Field> fields, final T t,
            final String sep) {
        return type(t).toStringList(fields, t, sep);
    }
    
    public static <T> List<String> toStringList(final List<Field> fields, final T t) {
        return type(t).toStringList(fields, t);
    }
    
    public static <T> String joinStringList(final List<String> stringList, final T t) {
        return type(t).joinStringList(stringList);
    }
    
    public static <T> String joinStringMap(final Map<String, Object> augmentedFields, final T t,
            final String sep) {
        return type(t).joinStringMap(augmentedFields, sep);
    }
    
    public static <T> String joinStringMap(final Map<String, Object> augmentedFields, final T t) {
        return type(t).joinStringMap(augmentedFields);
    }
    
    public static <T> String toString(final List<Field> fields, final T t, final String sep) {
        return type(t).toString(fields, t, sep);
    }
    
    public static <T> String toString(final List<Field> fields, final T t) {
        return type(t).toString(fields, t);
    }
    
    public static <T> String toString(final T t, final String sep) {
        return type(t).toString(t, sep);
    }
    
    public static <T> String toString(final T t) {
        return type(t).toString(t);
    }
    
}
