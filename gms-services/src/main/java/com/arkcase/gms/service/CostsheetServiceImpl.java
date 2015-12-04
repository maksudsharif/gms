package com.arkcase.gms.service;

import com.armedia.acm.services.search.model.SolrCore;
import org.mule.api.MuleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;

/**
 * Created by manoj.dhungana on 12/3/2015.
 */
public class CostsheetServiceImpl extends com.armedia.acm.services.costsheet.service.CostsheetServiceImpl implements CostsheetService{

    private Logger LOG = LoggerFactory.getLogger(getClass());


    @Override
    public String getObjectsWithSubTypesFromSolr(String objectType, String objectSubType, Authentication authentication, int startRow, int maxRows, String sortParams, String userId) {
        String retval = null;

        LOG.debug("Taking objects from Solr for object type = {}", objectType);
        LOG.debug("Taking objects from Solr for object sub-type = {}", objectSubType);

        String authorQuery = "";
        if (userId != null)
        {
            authorQuery = " AND author_s:" + userId;
        }

        String query = "object_type_s:" + objectType + authorQuery + " AND -status_s:DELETE AND object_sub_type_s:" + objectSubType;

        try
        {
            retval = getExecuteSolrQuery().getResultsByPredefinedQuery(authentication, SolrCore.QUICK_SEARCH, query, startRow, maxRows, sortParams);

            LOG.debug("Objects was retrieved.");
        }
        catch (MuleException e)
        {
            LOG.error("Cannot retrieve objects from Solr.", e);
        }

        return retval;
    }
}
