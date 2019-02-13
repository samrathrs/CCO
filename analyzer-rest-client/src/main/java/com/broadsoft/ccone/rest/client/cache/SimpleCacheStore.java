/**
 *
 */
package com.broadsoft.ccone.rest.client.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

import com.broadsoft.ccone.rest.client.constants.CoreConstants;
import com.broadsoft.ccone.rest.client.exception.CacheException;
import com.broadsoft.ccone.rest.client.pojo.CacheKey;

/**
 * @author svytla
 * @param <T>
 */
public abstract class SimpleCacheStore<T> {

    protected static final Map<CacheKey, Object> map = new ConcurrentHashMap<>();

    public boolean containsKey(final Object... keys) {
        final CacheKey key = buildKey(keys);
        return this.containsKey(key);
    }

    public T get(final Object... keys) throws CacheException {
        final CacheKey key = buildKey(keys);
        return this.get(key);
    }

    public void put(final T value, final Object... keys) {
        final CacheKey key = buildKey(keys);
        this.put(key, value);
    }

    public void remove(final Object... keys) {
        final CacheKey key = buildKey(keys);
        this.remove(key);
    }

    public void clear() {
        map.clear();
    }

    @SuppressWarnings("unchecked")
    public List<T> getAll() {
        return (ArrayList<T>) new ArrayList<>(map.values());
    }

    protected boolean containsKey(final CacheKey key) {
        return map.containsKey(key);
    }

    @SuppressWarnings("unchecked")
    protected T get(final CacheKey key) throws CacheException {
        if (!map.containsKey(
                key)) { throw new CacheException(String.format("Object not found with key %s ", key.toString())); }
        return (T) map.get(key);
    }

    protected void put(final CacheKey key, final T value) {
        map.put(key, value);
    }

    protected void remove(final CacheKey key) {
        map.remove(key);
    }

    protected CacheKey buildKey(final Object... keys) {
        final String key = StringUtils.join(keys, CoreConstants.CACHE_KEY_SEPERATOR);
        final CacheKey cacheKey = new CacheKey();
        cacheKey.setKey(key);
        return cacheKey;
    }

}
