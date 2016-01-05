package com.arkcase.gms.service.form;

import com.arkcase.gms.model.GmsConstants;
import com.arkcase.gms.model.form.CostBudgetForm;
import com.arkcase.gms.service.CostsheetService;
import com.armedia.acm.form.cost.service.CostService;
import com.armedia.acm.services.search.model.SearchConstants;

/**
 * Created by riste.tutureski on 12/1/2015.
 */
public class CostBudgetService extends CostService
{
    private CostsheetService costsheetServiceExt;

    @Override
    public String getSolrResponse(String objectType)
    {
        String jsonResults = getCostsheetServiceExt().getObjectsWithSubTypesFromSolr(objectType, GmsConstants.AWARD, getAuthentication(), 0, 25,
                SearchConstants.PROPERTY_NAME + " " + SearchConstants.SORT_DESC, null);

        return jsonResults;
    }

    @Override
    public String getFormName()
    {
        return GmsConstants.FORM_NAME_COSTSHEET_BUDGET;
    }

    @Override
    public Class<?> getFormClass()
    {
        return CostBudgetForm.class;
    }

    public CostsheetService getCostsheetServiceExt()
    {
        return costsheetServiceExt;
    }

    public void setCostsheetServiceExt(CostsheetService costsheetServiceExt)
    {
        this.costsheetServiceExt = costsheetServiceExt;
    }
}
