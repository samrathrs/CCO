/**
 *
 */
package com.broadsoft.ccone.rest.client.pojo;

import java.util.List;

/**
 * @author svytla
 */
public class ApiMetadataResponse {

    private ResponseMetadata auxiliaryMetadata;

    private List<PlatformMetadata> auxiliaryDataList;

    public ResponseMetadata getAuxiliaryMetadata() {
        return auxiliaryMetadata;
    }

    public void setAuxiliaryMetadata(final ResponseMetadata auxiliaryMetadata) {
        this.auxiliaryMetadata = auxiliaryMetadata;
    }

    public List<PlatformMetadata> getAuxiliaryDataList() {
        return auxiliaryDataList;
    }

    public void setAuxiliaryDataList(final List<PlatformMetadata> auxiliaryDataList) {
        this.auxiliaryDataList = auxiliaryDataList;
    }

}
