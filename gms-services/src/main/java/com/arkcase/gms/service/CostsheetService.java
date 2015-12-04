package com.arkcase.gms.service;

import org.springframework.security.core.Authentication;

/**
 * Created by manoj.dhungana on 12/3/2015.
 */
public interface CostsheetService extends com.armedia.acm.services.costsheet.service.CostsheetService {
    public String getObjectsWithSubTypesFromSolr(String objectType, String objectSubType, Authentication authentication, int startRow, int maxRows, String sortParams, String userId);
}
