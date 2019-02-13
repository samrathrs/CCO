/**
 *
 */
package com.broadsoft.ccone.rest.client.pojo;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author svytla
 */
public class ApiKeyInfo implements Comparable<ApiKeyInfo> {

    private String tenantId;
    private String issuedTo;
    private String key;
    private String signature;

    public ApiKeyInfo(final String tenantId, final String issuedTo, final String key) {
        super();
        this.tenantId = tenantId;
        this.issuedTo = issuedTo;
        this.key = key;
    }

    @Override
    public int compareTo(final ApiKeyInfo o) {
        final CompareToBuilder builder = new CompareToBuilder();
        builder.append(tenantId, o.tenantId);
        builder.append(issuedTo, o.issuedTo);
        return builder.toComparison();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }

    public String getIssuedTo() {
        return issuedTo;
    }

    public void setIssuedTo(final String issuedTo) {
        this.issuedTo = issuedTo;
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(final String signature) {
        this.signature = signature;
    }
}
