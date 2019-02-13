/**
 *
 */
package com.broadsoft.ccone.rest.client.pojo;

/**
 * @author svytla
 */
public class ResponseMetadata {

    private String id;
    private Query query;
    private String[] columns;
    private long lastAccessTimestamp;
    private long numberOfRecords;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(final Query query) {
        this.query = query;
    }

    public String[] getColumns() {
        return columns;
    }

    public void setColumns(final String[] columns) {
        this.columns = columns;
    }

    public long getLastAccessTimestamp() {
        return lastAccessTimestamp;
    }

    public void setLastAccessTimestamp(final long lastAccessTimestamp) {
        this.lastAccessTimestamp = lastAccessTimestamp;
    }

    public long getNumberOfRecords() {
        return numberOfRecords;
    }

    public void setNumberOfRecords(final long numberOfRecords) {
        this.numberOfRecords = numberOfRecords;
    }

}
