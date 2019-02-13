/**
 *
 */
package com.broadsoft.ccone.rest.client.pojo;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @author svytla
 */
public class CacheKey implements Comparable<CacheKey> {

    private String key;

    /*
     * (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(final CacheKey key) {
        final CompareToBuilder builder = new CompareToBuilder();
        builder.append(key.getKey(), getKey());
        return builder.toComparison();
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        final CacheKey key1 = (CacheKey) obj;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.append(key, key1.key);
        return builder.isEquals();
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(key);
        return builder.hashCode();
    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key
     *            the key to set
     */
    public void setKey(final String key) {
        this.key = key;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CacheKey [key=" + key + "]";
    }

}
