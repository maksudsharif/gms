package com.arkcase.gms.service.form;

import com.arkcase.gms.model.GmsConstants;
import com.arkcase.gms.model.form.CostPaymentForm;
import com.armedia.acm.form.cost.service.CostService;

/**
 * Created by riste.tutureski on 12/1/2015.
 */
public class CostPaymentService extends CostService
{
    @Override
    public String getFormName()
    {
        return GmsConstants.FORM_NAME_COSTSHEET_PAYMENT;
    }

    @Override
    public Class<?> getFormClass()
    {
        return CostPaymentForm.class;
    }
}
