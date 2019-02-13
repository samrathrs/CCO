/**
 *
 */
package com.broadsoft.ccone.rest.client.pojo;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class FilterGroup {

    private Operator operator = Operator.AND;

    private ValueQueryFilter[] valueFilters;

    /**
     * @return the operator
     */
    public Operator getOperator() {
        return operator;
    }

    /**
     * @param operator
     *            the operator to set
     */
    public void setOperator(final Operator operator) {
        this.operator = operator;
    }

    /**
     * @return the valueFilters
     */
    public ValueQueryFilter[] getValueFilters() {
        return valueFilters;
    }

    /**
     * @param valueFilters
     *            the valueFilters to set
     */
    public void setValueFilters(final ValueQueryFilter[] valueFilters) {
        this.valueFilters = valueFilters;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        boolean b = false;
        if (obj != null && obj instanceof FilterGroup) {
            final FilterGroup fg = (FilterGroup) obj;
            if (fg.getOperator() == operator) {
                if (valueFilters == null && fg.valueFilters == null) {
                    b = true;
                } else if (valueFilters != null && fg.valueFilters != null) {
                    if (valueFilters.length == fg.valueFilters.length) {
                        final int vfSize = valueFilters.length;
                        boolean unmatched = false;
                        for (int i = 0; i < vfSize; i++) {
                            if (!valueFilters[i].equals(fg.valueFilters[i])) {
                                unmatched = true;
                                break;
                            }
                        }

                        if (!unmatched) {
                            b = true;
                        }
                    }
                }
            }
        }

        return b;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, false);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final ReflectionToStringBuilder builder = new ReflectionToStringBuilder(this);
        return builder.toString();
    }

    public enum Operator {
        AND, OR
    }
}
