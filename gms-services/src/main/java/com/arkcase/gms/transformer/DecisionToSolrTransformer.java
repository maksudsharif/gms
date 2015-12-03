package com.arkcase.gms.transformer;

import com.arkcase.gms.model.Decision;
import com.armedia.acm.plugins.casefile.model.Disposition;
import com.armedia.acm.plugins.casefile.service.DispositionToSolrTransformer;
import com.armedia.acm.services.search.model.solr.SolrDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by riste.tutureski on 11/30/2015.
 */
public class DecisionToSolrTransformer extends DispositionToSolrTransformer
{
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Override
    public SolrDocument toSolrQuickSearch(Disposition in)
    {
        SolrDocument solr = null;

        if (in instanceof Decision)
        {
            Decision decision = (Decision) in;
            solr = super.toSolrQuickSearch(decision);

            if (solr != null)
            {
                mapOrderProperties(decision, solr.getAdditionalProperties());
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
        return Decision.class.equals(acmObjectType);
    }

    private void mapOrderProperties(Decision decision, Map<String, Object> aps)
    {
        aps.put("object_sub_type_s", "DECISION");

        if (decision != null && decision.getAwardValue() != null)
        {
            aps.put("award_value_l", decision.getAwardValue());
        }
    }
}
