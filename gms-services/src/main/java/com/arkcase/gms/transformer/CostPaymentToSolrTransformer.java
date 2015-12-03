package com.arkcase.gms.transformer;

import com.arkcase.gms.model.CostBudget;
import com.arkcase.gms.model.CostPayment;
import com.arkcase.gms.model.GmsConstants;
import com.armedia.acm.services.costsheet.model.AcmCostsheet;
import com.armedia.acm.services.costsheet.service.CostsheetToSolrTransformer;
import com.armedia.acm.services.search.model.solr.SolrDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by riste.tutureski on 12/3/2015.
 */
public class CostPaymentToSolrTransformer extends CostsheetToSolrTransformer
{
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Override
    public SolrDocument toSolrQuickSearch(AcmCostsheet in)
    {
        SolrDocument solr = null;

        if (in instanceof CostBudget)
        {
            CostPayment costPayment = (CostPayment) in;
            solr = super.toSolrQuickSearch(costPayment);

            if (solr != null)
            {
                mapOrderProperties(costPayment, solr.getAdditionalProperties());
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
        return CostPayment.class.equals(acmObjectType);
    }

    private void mapOrderProperties(CostPayment costPayment, Map<String, Object> aps)
    {
        aps.put("object_sub_type_s", GmsConstants.FORM_NAME_COSTSHEET_PAYMENT.toUpperCase());
    }
}
