/**
 *
 */
package com.broadsoft.ccone.rest.client.helper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import com.broadsoft.ccone.rest.client.pojo.FilterGroup;
import com.broadsoft.ccone.rest.client.pojo.FilterGroup.Operator;
import com.broadsoft.ccone.rest.client.pojo.QueryFilter.OperatorType;
import com.broadsoft.ccone.rest.client.pojo.ValueQueryFilter;

/**
 * @author svytla
 */
public class FliterGroupBuilder {

    private final Map<String, Object> attributes;

    private Map<String, Object> primitiveAttributes = new ConcurrentHashMap<>();

    private Map<String, Object> arrayAttributes = new ConcurrentHashMap<>();

    public static FliterGroupBuilder builder(final Map<String, Object> attributes) {
        return new FliterGroupBuilder(attributes);
    }

    public FilterGroup[] build() {

        final List<FilterGroup> list = new ArrayList<>();

        arrayAttributes = attributes.entrySet().stream()
                .filter(map -> map.getValue() instanceof Collection<?> || map.getValue() instanceof Array)
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));

        primitiveAttributes = attributes.entrySet().stream().filter(map -> !arrayAttributes.containsKey(map.getKey()))
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));

        addPrimitiveFilters(list);

        addCollectionFilters(list);

        return list.toArray(new FilterGroup[] {});
    }

    private void addCollectionFilters(final List<FilterGroup> list) {
        if (MapUtils.isNotEmpty(arrayAttributes)) {

            arrayAttributes.entrySet().forEach(entry -> {

                final Object value = entry.getValue();

                final Collection<Object> values = getValues(value);

                if (values != null) {
                    final FilterGroup filterGroup = createFilterGroup(entry, values);
                    list.add(filterGroup);
                }

            });

        }
    }

    private void addPrimitiveFilters(final List<FilterGroup> list) {
        if (MapUtils.isNotEmpty(primitiveAttributes)) {

            final List<ValueQueryFilter> valueFilters = new ArrayList<>();

            primitiveAttributes.entrySet().forEach(entry -> {

                final ValueQueryFilter filter = new ValueQueryFilter();
                filter.setColumnName(entry.getKey());
                filter.setValue(String.valueOf(entry.getValue()));
                filter.setOperator(OperatorType.EQUAL);
                filter.setIncludeIfColumnNotPresent(false);
                valueFilters.add(filter);

            });

            final FilterGroup filterGroup = new FilterGroup();
            filterGroup.setOperator(Operator.AND);
            filterGroup.setValueFilters(valueFilters.toArray(new ValueQueryFilter[] {}));

            list.add(filterGroup);
        }
    }

    @SuppressWarnings("unchecked")
    private Collection<Object> getValues(final Object value) {

        Collection<Object> values = CollectionUtils.EMPTY_COLLECTION;

        if (value instanceof List) {
            values = (List<Object>) value;
        } else if (value instanceof Set) {
            values = (Set<Object>) value;
        } else if (value instanceof Array) {
            values = Arrays.asList(value);
        }

        return values;
    }

    private FilterGroup createFilterGroup(final Entry<String, Object> entry, final Collection<Object> values) {

        final List<ValueQueryFilter> valueFilters = new ArrayList<>();

        final String[] filterValues = new String[values.size()];

        int index = 0;
        for (final Object filterVal : values) {
            filterValues[index++] = String.valueOf(filterVal);
        }

        final ValueQueryFilter filter = new ValueQueryFilter();
        filter.setColumnName(entry.getKey());
        filter.setValues(filterValues);
        filter.setOperator(OperatorType.IN);
        filter.setIncludeIfColumnNotPresent(false);
        valueFilters.add(filter);

        final FilterGroup filterGroup = new FilterGroup();
        filterGroup.setOperator(Operator.OR);
        filterGroup.setValueFilters(valueFilters.toArray(new ValueQueryFilter[] {}));

        return filterGroup;
    }

    FliterGroupBuilder(final Map<String, Object> attributes) {
        this.attributes = attributes;
    }

}
