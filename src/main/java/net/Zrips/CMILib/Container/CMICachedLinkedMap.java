package net.Zrips.CMILib.Container;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public final class CMICachedLinkedMap<K, V> {

    private final Map<K, V> map;

    public CMICachedLinkedMap(int maxSize) {
        this.map = new LinkedHashMap<K, V>(maxSize, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > maxSize;
            }
        };
    }

    public synchronized V get(K key) {
        return map.get(key);
    }

    public synchronized void put(K key, V value) {
        map.put(key, value);
    }

    public synchronized int size() {
        return map.size();
    }

    public synchronized void clear() {
        map.clear();
    }

    public synchronized boolean containsKey(K key) {
        return map.containsKey(key);
    }

    public synchronized boolean containsValue(V value) {
        return map.containsValue(value);
    }

    public synchronized Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    public synchronized Collection<V> values() {
        return map.values();
    }
}
