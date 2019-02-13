/**
 *
 */
package com.broadsoft.ccone.rest.client.helper;

import java.security.SignatureException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.broadsoft.ccone.rest.client.pojo.ApiKeyInfo;
import com.transerainc.tam.config.AccessDetails;
import com.transerainc.tam.config.AccessDetails.AnalyzerAccessDetails;

/**
 * @author svytla
 */
@Component
public class AnalyzerAccessHelper {

    @Autowired
    private SignatureHelper signatureHelper;

    private static AnalyzerAccessDetails analyzerAccessDetails;

    private static ApiKeyInfo info;

    public void refreshAccessDetails(final AccessDetails accessDetails) throws SignatureException {

        analyzerAccessDetails = accessDetails.getAnalyzerAccessDetails();

        info = new ApiKeyInfo(analyzerAccessDetails.getTenantId(), analyzerAccessDetails.getFrom(),
                analyzerAccessDetails.getApiKey());

        final String signature = signatureHelper.computeSignature(info.getIssuedTo(), info.getKey());

        info.setSignature(signature);
    }

    public ApiKeyInfo getInfo() {
        return info;
    }

    /**
     * @return the analyzerAccessDetails
     */
    public AnalyzerAccessDetails getAnalyzerAccessDetails() {
        return analyzerAccessDetails;
    }

}
