package com.arkcase.gms.service.form;

import com.arkcase.gms.model.CostBudget;
import com.arkcase.gms.model.form.CostBudgetForm;
import com.armedia.acm.form.cost.model.CostForm;
import com.armedia.acm.services.costsheet.model.AcmCostsheet;

/**
 * Created by riste.tutureski on 12/3/2015.
 */
public class CostBudgetFactory extends CostAbstractFactory
{
    @Override
    public AcmCostsheet createCostsheetInstance()
    {
        return new CostBudget();
    }

    @Override
    public CostForm createCostFormInstance()
    {
        return new CostBudgetForm();
    }
}
