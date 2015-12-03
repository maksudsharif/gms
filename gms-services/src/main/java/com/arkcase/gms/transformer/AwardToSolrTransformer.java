package com.arkcase.gms.transformer;

import com.arkcase.gms.model.Award;
import com.armedia.acm.plugins.complaint.model.Complaint;
import com.armedia.acm.plugins.complaint.service.ComplaintToSolrTransformer;
import com.armedia.acm.services.search.model.solr.SolrAdvancedSearchDocument;
import com.armedia.acm.services.search.model.solr.SolrDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by riste.tutureski on 11/26/2015.
 */
public class AwardToSolrTransformer extends ComplaintToSolrTransformer
{
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Override
    public SolrAdvancedSearchDocument toSolrAdvancedSearch(Complaint in)
    {
        SolrAdvancedSearchDocument solr = null;

        if (in instanceof Award)
        {
            Award award = (Award) in;
            solr = super.toSolrAdvancedSearch(award);

            if (solr != null)
            {
                mapOrderProperties(award, solr.getAdditionalProperties());
            }
        }
        else
        {
            LOG.error("Could not send to advanced search class name {}!.", in.getClass().getName());
        }

        return solr;
    }

    @Override
    public SolrDocument toSolrQuickSearch(Complaint in)
    {
        SolrDocument solr = null;

        if (in instanceof Award)
        {
            Award award = (Award) in;
            solr = super.toSolrQuickSearch(award);

            if (solr != null)
            {
                mapOrderProperties(award, solr.getAdditionalProperties());
            }
        }
        else
        {
            LOG.error("Could not send to quick search class name {}!.", in.getClass().getName());
        }

        return solr;
    }

    @Override
    public boolean isAcmObjectTypeSupported(Class acmObjectType)
    {
        return Award.class.equals(acmObjectType);
    }

    private void mapOrderProperties(Award award, Map<String, Object> aps)
    {
        aps.put("object_sub_type_s", "AWARD");

        if (award != null)
        {
            if (award.getGrant() != null && award.getGrant().getId() != null)
            {
                aps.put("grant_id_l", award.getGrant().getId());
            }
        }
    }
}
