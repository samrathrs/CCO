/**
 *
 */
package com.broadsoft.ccone.rest.client.pojo;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;

import com.broadsoft.ccone.rest.client.constants.MessageKeys;
import com.broadsoft.ccone.rest.client.exception.ConversionException;

/**
 * @author svytla
 */
public class UserRecord extends EntityRecord {

    private String login;
    private String emailAddress;
    private String password;
    private String firstName;
    private String lastName;

    public UserRecord() {
    }

    public UserRecord(final EntityRecord record, final String login, final String emailAddress, final String password)
            throws ConversionException {

        try {
            BeanUtils.copyProperties(this, record);
        } catch (IllegalAccessException | InvocationTargetException e) {
            final ErrorMessageInfo info = new ErrorMessageInfo(MessageKeys.ERROR_INTERNAL_SERVER);
            throw new ConversionException("Error in creating user model.", 101, info, e);
        }

        this.login = login;
        this.emailAddress = emailAddress;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(final String login) {
        this.login = login;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(final String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }
}
