package com.arkcase.gms.service.form;

import com.arkcase.gms.model.GmsConstants;
import com.arkcase.gms.model.form.CostBudgetForm;
import com.arkcase.gms.utils.BeanUtilsBean;
import com.armedia.acm.form.config.xml.ApproverItem;
import com.armedia.acm.form.cost.model.CostForm;
import com.armedia.acm.form.cost.model.CostItem;
import com.armedia.acm.form.cost.service.CostService;
import com.armedia.acm.frevvo.model.FrevvoForm;
import com.armedia.acm.plugins.ecm.model.AcmContainer;
import com.armedia.acm.services.costsheet.model.AcmCostsheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * Created by riste.tutureski on 12/1/2015.
 */
public class CostBudgetService extends CostService
{
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
}
