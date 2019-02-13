/**
 *
 */
package com.broadsoft.ccone.rest.client.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.broadsoft.ccone.rest.client.constants.CoreConstants;
import com.broadsoft.ccone.rest.client.exception.CacheException;
import com.broadsoft.ccone.rest.client.pojo.CacheKey;
import com.broadsoft.ccone.rest.client.pojo.EntityRecord;

/**
 * @author svytla
 * @param <T>
 */
public abstract class CacheStore<T> {

    protected static final Map<String, Map<CacheKey, Object>> tenantMap = new ConcurrentHashMap<>();

    public boolean containsKey(final String tenantId, final Object... keys) {
        final CacheKey key = buildKey(keys);
        return this.containsKey(tenantId, key);
    }

    public T get(final String tenantId, final Object... keys) throws CacheException {
        final CacheKey key = buildKey(keys);
        return this.get(tenantId, key);
    }

    public void put(final T value, final String tenantId, final Object... keys) {
        final CacheKey key = buildKey(keys);
        this.put(tenantId, key, value);
    }

    public void remove(final String tenantId, final Object... keys) {
        final CacheKey key = buildKey(keys);
        this.remove(tenantId, key);
    }

    public void removeByKey(final String tenantId, final CacheKey key) {
        this.remove(tenantId, key);
    }

    public void clear(final String tenantId) {
        final Map<CacheKey, Object> map = tenantMap.get(tenantId);
        if (map != null) {
            map.clear();
        }
    }

    public void clearAll() {
        tenantMap.clear();
    }

    @SuppressWarnings("unchecked")
    public void putAll(final String tenantId, final List<EntityRecord> list) {

        if (CollectionUtils.isEmpty(list)) { return; }

        for (final EntityRecord record : list) {
            this.put((T) record, tenantId, record.getId());
        }
    }

    @SuppressWarnings("unchecked")
    public List<T> getByTenantId(final String tenantId) {
        final Map<CacheKey, Object> map = tenantMap.get(tenantId);
        return map != null ? (ArrayList<T>) new ArrayList<>(map.values()) : new ArrayList<>();
    }

    public Map<String, Set<CacheKey>> getKeys() {
        final Map<String, Set<CacheKey>> tenantKeys = new HashMap<>();

        if (MapUtils.isNotEmpty(tenantMap)) {
            Set<CacheKey> keys;
            for (final Map.Entry<String, Map<CacheKey, Object>> entry : tenantMap.entrySet()) {
                keys = tenantKeys.get(entry.getKey());
                if (keys == null) {
                    keys = new TreeSet<>();
                    tenantKeys.put(entry.getKey(), keys);
                }
                keys.addAll(entry.getValue().keySet());
            }
        }

        return tenantKeys;
    }

    @SuppressWarnings("unchecked")
    public Map<CacheKey, T> getByKeyStartsWith(final String tenantId, final Object... keys) {
        final Map<CacheKey, T> result = new HashMap<>();
        final CacheKey key = buildKey(keys);
        final Map<CacheKey, Object> map = tenantMap.get(tenantId);
        if (MapUtils.isNotEmpty(map)) {
            for (final Map.Entry<CacheKey, Object> entry : map.entrySet()) {
                if (StringUtils.startsWith(entry.getKey().getKey(), key.getKey())) {
                    result.put(entry.getKey(), (T) entry.getValue());
                }
            }
        }
        return result;
    }

    protected boolean containsKey(final String tenantId, final CacheKey key) {
        final Map<CacheKey, Object> map = tenantMap.get(tenantId);
        return map != null && map.containsKey(key);
    }

    @SuppressWarnings("unchecked")
    protected T get(final String tenantId, final CacheKey key) throws CacheException {
        final Map<CacheKey, Object> map = tenantMap.get(tenantId);
        if (map == null || !map.containsKey(key)) { throw new CacheException(
                String.format("Object not found for tenant %s with key %s ", tenantId, key.toString())); }
        return (T) map.get(key);
    }

    protected void put(final String tenantId, final CacheKey key, final T value) {
        Map<CacheKey, Object> map = tenantMap.get(tenantId);
        if (map == null) {
            map = new ConcurrentHashMap<>();
            tenantMap.put(tenantId, map);
        }
        map.put(key, value);
    }

    protected void remove(final String tenantId, final CacheKey key) {
        final Map<CacheKey, Object> map = tenantMap.get(tenantId);
        if (map != null) {
            map.remove(key);
        }
    }

    protected CacheKey buildKey(final Object... keys) {
        final String key = StringUtils.join(keys, CoreConstants.CACHE_KEY_SEPERATOR);
        final CacheKey cacheKey = new CacheKey();
        cacheKey.setKey(key);
        return cacheKey;
    }

}
