package org.tealsk12.tealsmodloader.util.registry;

import java.util.HashMap;

/**
 * @author Connor Hollasch
 * https://github.com/CHollasch
 */
public abstract class Registry<T> {

    /**
     * Map in which items registered are stored
     */
    private HashMap<String, T> map = new HashMap<String, T>();

    /**
     * Puts an item inside the registered map and registers that specific item.
     *
     * @param key   Key used to find registered item
     * @param value Value in which key is associated with
     * @param args  Arguments used for register method
     * @return Registered instance for method chaining
     */
    public T newInstance(String key, T value, Object... args) {
        register(key, value, args);
        map.put(key.toLowerCase(), value);
        return value;
    }

    /**
     * Gets a specific registered instance by name.
     *
     * @param key Key in which to find value associated with
     * @return Value the key is associated with
     */
    public T get(String key) {
        return map.get(key.toLowerCase());
    }

    /**
     * Called when a key value pair is put into this map
     *
     * @param type Value put into the map for registration
     */
    protected abstract void register(String key, T type, Object... args);
}
