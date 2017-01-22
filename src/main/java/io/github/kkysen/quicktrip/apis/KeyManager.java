package io.github.kkysen.quicktrip.apis;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

/**
 *
 *
 * @author Khyber Sen
 */
public final class KeyManager implements Iterable<String> {
    
    private final Deque<String> keys = new ArrayDeque<>();
    
    public KeyManager() {}
    
    public KeyManager(final Collection<String> keys) {
        if (keys == null) {
            return;
        }
        keys.forEach(this::addKey);
    }
    
    public KeyManager(final String... keys) {
        if (keys == null) {
            return;
        }
        for (final String key : keys) {
            addKey(key);
        }
    }
    
    public boolean addKey(final String key) {
        if (keys.contains(key)) {
            return false;
        }
        keys.add(key);
        return true;
    }
    
    public String get() {
        return keys.getFirst();
    }
    
    public void rotate() {
        keys.addFirst(keys.pollLast());
    }
    
    public String next() {
        rotate();
        return get();
    }
    
    @Override
    public Iterator<String> iterator() {
        return keys.iterator();
    }
    
    public boolean isEmpty() {
        return keys.isEmpty();
    }
    
}