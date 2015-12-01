package com.arkcase.gms.service.form;

import com.arkcase.gms.model.GmsConstants;
import com.arkcase.gms.model.form.ReviewForm;
import com.armedia.acm.form.closecomplaint.service.CloseComplaintService;

/**
 * Created by riste.tutureski on 11/27/2015.
 */
public class ReviewFormService extends CloseComplaintService
{
    @Override
    public String getFormName()
    {
        return GmsConstants.FORM_NAME_REVIEW;
    }

    @Override
    public Class<?> getFormClass()
    {
        return ReviewForm.class;
    }
}
