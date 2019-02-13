/**
 *
 */
package com.broadsoft.ccone.rest.client.pojo;

import java.util.List;

/**
 * @author svytla
 */
public class ApiRecordResponse {

    private ResponseMetadata auxiliaryMetadata;
    private List<EntityRecord> auxiliaryDataList;

    public ResponseMetadata getAuxiliaryMetadata() {
        return auxiliaryMetadata;
    }

    public void setAuxiliaryMetadata(final ResponseMetadata auxiliaryMetadata) {
        this.auxiliaryMetadata = auxiliaryMetadata;
    }

    public List<EntityRecord> getAuxiliaryDataList() {
        return auxiliaryDataList;
    }

    public void setAuxiliaryDataList(final List<EntityRecord> auxiliaryDataList) {
        this.auxiliaryDataList = auxiliaryDataList;
    }

}
