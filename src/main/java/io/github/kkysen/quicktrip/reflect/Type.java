package io.github.kkysen.quicktrip.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringJoiner;

/**
 * reflection utils
 * 
 * @author Khyber Sen
 * @param <T> type
 */
class Type<T> {
    
    private static final String SEP = "=";
    
    private final Class<?> type;
    
    public Type(final Class<?> type) {
        this.type = type;
    }
    
    public List<Field> getFields() {
        final List<Field> fields = new ArrayList<>();
        Class<?> tempType = type;
        // gets declared fields of all superclasses
        while (tempType != null) {
            fields.addAll(0, Arrays.asList(tempType.getDeclaredFields()));
            tempType = tempType.getSuperclass();
        }
        fields.forEach(field -> field.setAccessible(true));
        return fields;
    }
    
    public List<Field> getInstanceVars() {
        final List<Field> instanceVars = getFields();
        instanceVars.removeIf(field -> Modifier.isStatic(field.getModifiers()));
        return instanceVars;
    }
    
    public Map<Field, Object> getFieldEntries(final List<Field> fields, final T t) {
        final Map<Field, Object> fieldEntries = new LinkedHashMap<>(fields.size());
        for (final Field field : fields) {
            try {
                fieldEntries.put(field, field.get(t));
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new RuntimeException(e); // should never happen
            }
        }
        return fieldEntries;
    }
    
    public Map<Field, Object> getFieldEntries(final T t) {
        return getFieldEntries(getFields(), t);
    }
    
    public Map<Field, Object> getInstanceVarEntries(final T t) {
        return getFieldEntries(getInstanceVars(), t);
    }
    
    public Map<String, Object> getStringFieldEntries(final List<Field> fields, final T t) {
        final Map<Field, Object> fieldEntries = getFieldEntries(fields, t);
        final Map<String, Object> stringFieldEntries = new LinkedHashMap<>(fields.size());
        for (final Entry<Field, Object> entry : fieldEntries.entrySet()) {
            stringFieldEntries.put(entry.getKey().getName(), entry.getValue());
        }
        return stringFieldEntries;
    }
    
    public Map<String, Object> getStringFieldEntries(final T t) {
        return getStringFieldEntries(getFields(), t);
    }
    
    public Map<String, Object> getStringInstanceVarEntries(final T t) {
        return getStringFieldEntries(getInstanceVars(), t);
    }
    
    public Map<String, String> getAllStringFieldEntries(final List<Field> fields, final T t) {
        final Map<Field, Object> fieldEntries = getFieldEntries(fields, t);
        final Map<String, String> stringFieldEntries = new LinkedHashMap<>(fields.size());
        for (final Entry<Field, Object> entry : fieldEntries.entrySet()) {
            stringFieldEntries.put(entry.getKey().getName(), entry.getValue().toString());
        }
        return stringFieldEntries;
    }
    
    public Map<String, String> getAllStringFieldEntries(final T t) {
        return getAllStringFieldEntries(getFields(), t);
    }
    
    public Map<String, String> getAllStringInstanceVarEntries(final T t) {
        return getAllStringFieldEntries(getInstanceVars(), t);
    }
    
    public List<String> toStringList(final List<Field> fields, final T t, final String sep) {
        final Set<Map.Entry<String, String>> stringFieldEntries = getAllStringFieldEntries(fields, t).entrySet();
        final List<String> stringList = new ArrayList<>(fields.size());
        for (final Map.Entry<String, String> entry : stringFieldEntries) {
            stringList.add(entry.getKey() + sep + entry.getValue());
        }
        return stringList;
    }
    
    public List<String> toStringList(final List<Field> fields, final T t) {
        return toStringList(fields, t, SEP);
    }
    
    private StringJoiner sj() {
        return new StringJoiner(", ", type.getSimpleName() + " [", "]");
    }
    
    public String joinStringList(final List<String> stringList) {
        final StringJoiner sj = sj();
        stringList.forEach(sj::add);
        return sj.toString();
    }
    
    public String joinStringMap(final Map<String, Object> augmentedFields, final String sep) {
        final StringJoiner sj = sj();
        for (final Map.Entry<String, Object> entry : augmentedFields.entrySet()) {
            sj.add(entry.getKey() + sep + entry.getValue());
        }
        return sj.toString();
    }
    
    public String joinStringMap(final Map<String, Object> augmentedFields) {
        return joinStringMap(augmentedFields, SEP);
    }
    
    public String toString(final List<Field> fields, final T t, final String sep) {
        return joinStringList(toStringList(fields, t, sep));
    }
    
    public String toString(final List<Field> fields, final T t) {
        return toString(fields, t, SEP);
    }
    
    public String toString(final T t, final String sep) {
        return toString(getInstanceVars(), t, sep);
    }
    
    public String toString(final T t) {
        return toString(t, SEP);
    }
    
}
