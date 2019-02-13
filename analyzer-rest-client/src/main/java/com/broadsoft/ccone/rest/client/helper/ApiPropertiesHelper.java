/**
 *
 */
package com.broadsoft.ccone.rest.client.helper;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import com.broadsoft.ccone.rest.client.exception.ConfigurationException;

/**
 * @author svytla
 */
@Component
public class ApiPropertiesHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiPropertiesHelper.class);

    private final static Map<String, Object> properteis = new LinkedHashMap<>();

    public void init(final String content) throws ConfigurationException {
        loadProperties(content);
    }

    @SuppressWarnings("unchecked")
    private void loadProperties(final String content) throws ConfigurationException {

        LOGGER.info("Loading properties with {}", content);

        if (StringUtils.isBlank(content)) {
            LOGGER.warn("conent is null, couldn't load yml properties ...");
            throw new ConfigurationException("conent is null, couldn't load yml properties ...");
        }

        properteis.clear();

        try {
            final Yaml yaml = new Yaml();
            final Map<String, Object> map = (Map<String, Object>) yaml.load(content);
            if (map != null) {
                buildFlattenedMap(properteis, map, null);
            }
        } catch (final Exception e) {
            LOGGER.error("Exception in loading yml properties from {}", content, e);
            throw new ConfigurationException(String.format("Exception in loading yml properties from %s", content));
        }
    }

    public Map<String, Object> getProperteis() {
        return properteis;
    }

    public String getYmlProperty(final String key) {
        return StringUtils.trim(String.valueOf(properteis.get(key)));
    }

    public Properties getYmlPropertiesByPrefix(final String prefix) {
        final Properties props = new Properties();
        for (final Map.Entry<String, Object> entry : properteis.entrySet()) {
            if (StringUtils.startsWithIgnoreCase(entry.getKey(), prefix)) {
                props.put(StringUtils.removeStartIgnoreCase(entry.getKey(), prefix),
                        StringUtils.trim(String.valueOf(entry.getValue())));
            }
        }
        return props;
    }

    @SuppressWarnings("unchecked")
    private void buildFlattenedMap(final Map<String, Object> result, final Map<String, Object> source,
            final String path) {

        for (final Entry<String, Object> entry : source.entrySet()) {

            String key = entry.getKey();
            if (StringUtils.isNotBlank(path)) {
                if (key.startsWith("[")) {
                    key = path + key;
                } else {
                    key = path + "." + key;
                }
            }

            final Object value = entry.getValue();
            if (value instanceof String) {
                result.put(key, StringUtils.trim(String.valueOf(value)));
            } else if (value instanceof Map) {
                // Need a compound key
                final Map<String, Object> map = (Map<String, Object>) value;
                buildFlattenedMap(result, map, key);
            } else if (value instanceof Collection) {
                // Need a compound key
                final Collection<Object> collection = (Collection<Object>) value;
                int count = 0;
                for (final Object object : collection) {
                    buildFlattenedMap(result, Collections.singletonMap("[" + count++ + "]", object), key);
                }
            } else {
                result.put(key, value == null ? "" : value);
            }
        }
    }

}
