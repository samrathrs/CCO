/**
 *
 */
package com.broadsoft.ccone.rest.client.pojo;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;

/**
 * @author svytla
 */
public class EntityInfo extends BasePojo implements Comparable<EntityInfo> {

    protected String id;
    protected String dbId;
    protected String text;

    public EntityInfo() {
        super();
    }

    public EntityInfo(final String id, final String text) {
        super();
        this.id = id;
        this.text = text;
    }

    public EntityInfo(final String dbId, final String id, final String text) {
        super();
        this.dbId = dbId;
        this.id = id;
        this.text = text;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text
     *            the text to set
     */
    public void setText(final String text) {
        this.text = text;
    }

    public String getDbId() {
        return dbId;
    }

    public void setDbId(final String analyzerId) {
        dbId = analyzerId;
    }

    @Override
    public int compareTo(final EntityInfo info) {
        final CompareToBuilder builder = new CompareToBuilder();
        builder.append(StringUtils.lowerCase(text), StringUtils.lowerCase(info.text));
        return builder.toComparison();
    }

}
