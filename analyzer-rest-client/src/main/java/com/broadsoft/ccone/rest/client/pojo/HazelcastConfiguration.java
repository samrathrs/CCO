/**
 *
 */
package com.broadsoft.ccone.rest.client.pojo;

import java.util.List;

/**
 * @author svytla
 */
public class HazelcastConfiguration extends BasePojo {

    private String name;
    private String password;
    private String members;
    private List<String> addresses;

    private boolean smartRouting;
    private boolean redoOperations;

    private int connectionTimeout;
    private int connectionAttemptLimit;
    private int connectionAttemptPeriod;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return the pwd
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param pwd
     *            the pwd to set
     */
    public void setPassword(final String pwd) {
        password = pwd;
    }

    /**
     * @return the members
     */
    public String getMembers() {
        return members;
    }

    /**
     * @param members
     *            the members to set
     */
    public void setMembers(final String members) {
        this.members = members;
    }

    /**
     * @return the addresses
     */
    public List<String> getAddresses() {
        return addresses;
    }

    /**
     * @param addresses
     *            the addresses to set
     */
    public void setAddresses(final List<String> addresses) {
        this.addresses = addresses;
    }

    /**
     * @return the smartRouting
     */
    public boolean isSmartRouting() {
        return smartRouting;
    }

    /**
     * @param smartRouting
     *            the smartRouting to set
     */
    public void setSmartRouting(final boolean smartRouting) {
        this.smartRouting = smartRouting;
    }

    /**
     * @return the redoOperations
     */
    public boolean isRedoOperations() {
        return redoOperations;
    }

    /**
     * @param redoOperations
     *            the redoOperations to set
     */
    public void setRedoOperations(final boolean redoOperations) {
        this.redoOperations = redoOperations;
    }

    /**
     * @return the connTimeout
     */
    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    /**
     * @param connTimeout
     *            the connTimeout to set
     */
    public void setConnectionTimeout(final int connTimeout) {
        connectionTimeout = connTimeout;
    }

    /**
     * @return the connAttemptLimit
     */
    public int getConnectionAttemptLimit() {
        return connectionAttemptLimit;
    }

    /**
     * @param connAttemptLimit
     *            the connAttemptLimit to set
     */
    public void setConnectionAttemptLimit(final int connAttemptLimit) {
        connectionAttemptLimit = connAttemptLimit;
    }

    /**
     * @return the connAttemptPeriod
     */
    public int getConnectionAttemptPeriod() {
        return connectionAttemptPeriod;
    }

    /**
     * @param connAttemptPeriod
     *            the connAttemptPeriod to set
     */
    public void setConnectionAttemptPeriod(final int connAttemptPeriod) {
        connectionAttemptPeriod = connAttemptPeriod;
    }

}
