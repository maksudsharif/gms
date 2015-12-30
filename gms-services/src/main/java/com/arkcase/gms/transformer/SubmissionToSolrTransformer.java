package com.arkcase.gms.transformer;

import com.arkcase.gms.model.GmsConstants;
import com.arkcase.gms.model.Submission;
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
public class SubmissionToSolrTransformer extends ComplaintToSolrTransformer
{
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Override
    public SolrAdvancedSearchDocument toSolrAdvancedSearch(Complaint in)
    {
        SolrAdvancedSearchDocument solr = null;

        if (in instanceof Submission)
        {
            Submission submission = (Submission) in;
            solr = super.toSolrAdvancedSearch(submission);

            if (solr != null)
            {
                mapOrderProperties(submission, solr.getAdditionalProperties());
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

        if (in instanceof Submission)
        {
            Submission submission = (Submission) in;
            solr = super.toSolrQuickSearch(submission);

            if (solr != null)
            {
                mapOrderProperties(submission, solr.getAdditionalProperties());
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
        return Submission.class.equals(acmObjectType);
    }

    private void mapOrderProperties(Submission submission, Map<String, Object> aps)
    {
        aps.put("object_sub_type_s", GmsConstants.SUMBISSION);

        if (submission != null)
        {
            if (submission.getGrant() != null && submission.getGrant().getId() != null)
            {
                aps.put("grant_id_l", submission.getGrant().getId());
            }
        }
    }
}
