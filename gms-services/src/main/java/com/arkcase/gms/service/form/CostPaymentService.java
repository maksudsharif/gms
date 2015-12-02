package com.arkcase.gms.service.form;

import com.arkcase.gms.model.GmsConstants;
import com.arkcase.gms.model.form.CostPaymentForm;
import com.armedia.acm.form.cost.model.CostForm;
import com.armedia.acm.frevvo.model.FrevvoForm;
import com.armedia.acm.plugins.ecm.model.AcmContainer;

/**
 * Created by riste.tutureski on 12/1/2015.
 */
public class CostPaymentService extends CostAbstractService
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

    @Override
    public CostForm getInstanceFromCaseForm()
    {
        return new CostPaymentForm();
    }

    @Override
    public CostForm getInstanceFromEditCaseForm(FrevvoForm form, AcmContainer container, String formName)
    {
        return (CostPaymentForm) populateEditInformation(form, container, formName);
    }
}
