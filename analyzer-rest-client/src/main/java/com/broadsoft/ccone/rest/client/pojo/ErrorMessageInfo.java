/**
 *
 */
package com.broadsoft.ccone.rest.client.pojo;

/**
 * @author svytla
 */
public class ErrorMessageInfo extends BasePojo {

    private String key;
    private Object[] args;

    public ErrorMessageInfo(final String key) {
        super();
        this.key = key;
    }

    public ErrorMessageInfo(final String key, final Object[] args) {
        super();
        this.key = key;
        this.args = args;
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public Object[] getArgs() {
        if (args == null) {
            args = new Object[0];
        }
        return args;
    }

    public void setArgs(final Object[] args) {
        this.args = args;
    }

}
