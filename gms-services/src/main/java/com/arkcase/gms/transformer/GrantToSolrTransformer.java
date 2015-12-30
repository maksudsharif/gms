package com.arkcase.gms.transformer;

import com.arkcase.gms.model.GmsConstants;
import com.arkcase.gms.model.Grant;
import com.armedia.acm.plugins.casefile.model.CaseFile;
import com.armedia.acm.plugins.casefile.service.CaseFileToSolrTransformer;
import com.armedia.acm.plugins.objectassociation.model.ObjectAssociation;
import com.armedia.acm.services.search.model.solr.SolrAdvancedSearchDocument;
import com.armedia.acm.services.search.model.solr.SolrDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by riste.tutureski on 11/24/2015.
 */
public class GrantToSolrTransformer extends CaseFileToSolrTransformer
{
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Override
    public SolrAdvancedSearchDocument toSolrAdvancedSearch(CaseFile in)
    {
        SolrAdvancedSearchDocument solr = null;

        if (in instanceof Grant)
        {
            Grant grant = (Grant) in;
            solr = super.toSolrAdvancedSearch(grant);

            if (solr != null)
            {
                mapOrderProperties(grant, solr.getAdditionalProperties());
            }
        }
        else
        {
            LOG.error("Could not send to advanced search class name {}!.", in.getClass().getName());
        }

        return solr;
    }

    @Override
    public SolrDocument toSolrQuickSearch(CaseFile in)
    {
        SolrDocument solr = null;

        if (in instanceof Grant)
        {
            Grant grant = (Grant) in;
            solr = super.toSolrQuickSearch(grant);

            if (solr != null)
            {
                mapOrderProperties(grant, solr.getAdditionalProperties());
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
        return Grant.class.equals(acmObjectType);
    }

    private void mapOrderProperties(Grant grant, Map<String, Object> aps)
    {
        aps.put("object_sub_type_s", GmsConstants.GRANT);

        if (grant != null)
        {
            if (grant.getTotalAnticipatedFunds() != null)
            {
                aps.put("total_anticipated_founds_l", grant.getTotalAnticipatedFunds());
            }

            if (grant.getExpectedAwardDate() != null)
            {
                aps.put("expected_award_date_tdt", grant.getExpectedAwardDate());
            }

            if (grant.getGrantType() != null)
            {
                aps.put("grant_type_s", grant.getGrantType());

                if (GmsConstants.TYPE_UNAWARDED_GRANT.equalsIgnoreCase(grant.getGrantType()))
                {
                    aps.put("submissions_count_i", getNumberOfSubmissions(grant));
                }
            }

            if (grant.getAwardValue() != null)
            {
                aps.put("award_value_l", grant.getAwardValue());
            }
        }
    }

    private int getNumberOfSubmissions(Grant grant)
    {
        if (grant != null && grant.getChildObjects() != null)
        {
            List<ObjectAssociation> submissionAssociations = grant.getChildObjects().stream().filter(oa -> oa.getTargetType() != null && GmsConstants.SUMBISSION.equalsIgnoreCase(oa.getTargetType())).collect(Collectors.toList());

            if (submissionAssociations != null)
            {
                return submissionAssociations.size();
            }
        }

        return 0;
    }
}
