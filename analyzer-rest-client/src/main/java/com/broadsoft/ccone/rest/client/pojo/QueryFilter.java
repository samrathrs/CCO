/**
 *
 */
package com.broadsoft.ccone.rest.client.pojo;

public abstract class QueryFilter {

    protected String columnName;

    protected String value;

    protected String[] values;

    protected OperatorType operator;

    protected boolean includeIfColumnNotPresent;

    public QueryFilter() {
        super();
    }

    public QueryFilter(final String columnName, final String value, final OperatorType operator,
            final boolean includeIfColumnNotPresent) {
        super();
        this.columnName = columnName;
        this.value = value;
        this.operator = operator;
        this.includeIfColumnNotPresent = includeIfColumnNotPresent;
    }

    /**
     * @return the columnName
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * @param columnName
     *            the columnName to set
     */
    public void setColumnName(final String columnName) {
        this.columnName = columnName;
    }

    /**
     * @return the value1
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value1
     *            the value1 to set
     */
    public void setValue(final String value1) {
        value = value1;
    }

    /**
     * @return the operator
     */
    public OperatorType getOperator() {
        return operator;
    }

    /**
     * @param operator
     *            the operator to set
     */
    public void setOperator(final OperatorType operator) {
        this.operator = operator;
    }

    /**
     * @return the includeIfColumnNotPresent
     */
    public boolean isIncludeIfColumnNotPresent() {
        return includeIfColumnNotPresent;
    }

    /**
     * @param includeIfColumnNotPresent
     *            the includeIfColumnNotPresent to set
     */
    public void setIncludeIfColumnNotPresent(final boolean includeIfColumnNotPresent) {
        this.includeIfColumnNotPresent = includeIfColumnNotPresent;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        boolean b = false;
        if (obj != null && obj instanceof QueryFilter) {
            final QueryFilter filter = (QueryFilter) obj;
            if (columnName.equals(filter.columnName) && value.equals(filter.value) && operator == filter.operator
                    && includeIfColumnNotPresent == filter.includeIfColumnNotPresent) {
                b = true;
            }
        }

        return b;
    }

    @Override
    public String toString() {
        return "QueryFilter [columnName=" + columnName + ", value=" + value + ", operator=" + operator
                + ", includeIfColumnNotPresent=" + includeIfColumnNotPresent + "]";
    }

    public enum OperatorType {
        EQUAL, LESS_THAN, GREATER_THAN, LESS_THAN_OR_EQUAL, GREATER_THAN_OR_EQUAL, NOT_EQUAL, REGEX, IN, NOT_IN
    }

    /**
     * @return the values
     */
    public String[] getValues() {
        return values;
    }

    /**
     * @param values
     *            the values to set
     */
    public void setValues(final String[] values) {
        this.values = values;
    }
}
