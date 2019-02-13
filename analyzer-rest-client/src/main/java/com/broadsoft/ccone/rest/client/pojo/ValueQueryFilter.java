/**
 *
 */
package com.broadsoft.ccone.rest.client.pojo;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class ValueQueryFilter extends QueryFilter {

    public ValueQueryFilter() {
        super();
    }

    public ValueQueryFilter(final String columnName, final String value, final OperatorType operator,
            final boolean includeIfColumnNotPresent) {
        super(columnName, value, operator, includeIfColumnNotPresent);
    }

    public ValueQueryFilter(final String columnName, final Object value, final OperatorType operator,
            final boolean includeIfColumnNotPresent) {
        super(columnName, String.valueOf(value), operator, includeIfColumnNotPresent);
    }

    @Override
    public String toString() {
        final ReflectionToStringBuilder builder = new ReflectionToStringBuilder(this);
        return builder.toString();
    }
}
