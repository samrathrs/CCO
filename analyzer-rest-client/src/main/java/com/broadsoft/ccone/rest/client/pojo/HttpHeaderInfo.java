/**
 *
 */
package com.broadsoft.ccone.rest.client.pojo;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author svytla
 */
public class HttpHeaderInfo extends BasePojo {

    private String tenantId;
    private String loginName;
    private boolean byUserName = false;
    private boolean delayResonse = false;

    private Set<String> uniqueueColumns;
    private ApiKeyInfo keyInfo;

    public HttpHeaderInfo(final String tenantId, final boolean addDelay) {
        this.tenantId = tenantId;
        delayResonse = addDelay;
    }

    public HttpHeaderInfo(final String loginName, final boolean byUserName, final boolean addDelay) {
        super();
        this.loginName = loginName;
        this.byUserName = byUserName;
        delayResonse = addDelay;
    }

    public HttpHeaderInfo(final String tenantId, final String loginName, final boolean addDelay) {
        super();
        this.tenantId = tenantId;
        this.loginName = loginName;
        delayResonse = addDelay;
    }

    public HttpHeaderInfo(final String tenantId, final String loginName, final boolean addDelay,
            final Set<String> uniqueueColumns) {
        super();
        this.tenantId = tenantId;
        this.loginName = loginName;
        delayResonse = addDelay;
        this.uniqueueColumns = uniqueueColumns;
    }

    public HttpHeaderInfo(final String loginName, final boolean addDelay, final Set<String> uniqueueColumns) {
        this.loginName = loginName;
        delayResonse = addDelay;
        this.uniqueueColumns = uniqueueColumns;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(final String loginName) {
        this.loginName = loginName;
    }

    public boolean isByUserName() {
        return byUserName;
    }

    public void setByUserName(final boolean byLoginName) {
        byUserName = byLoginName;
    }

    public Set<String> getUniqueueColumns() {
        if (uniqueueColumns == null) {
            uniqueueColumns = new LinkedHashSet<>();
        }
        return uniqueueColumns;
    }

    public void setUniqueueColumns(final Set<String> uniqueueColumns) {
        this.uniqueueColumns = uniqueueColumns;
    }

    public ApiKeyInfo getKeyInfo() {
        return keyInfo;
    }

    public void setKeyInfo(final ApiKeyInfo keyInfo) {
        this.keyInfo = keyInfo;
    }

    /**
     * @return the addDelay
     */
    public boolean isDelayResonse() {
        return delayResonse;
    }

    /**
     * @param addDelay
     *            the addDelay to set
     */
    public void setDelayResonse(final boolean addDelay) {
        delayResonse = addDelay;
    }

}
