package com.arkcase.gms.service.form;

import com.arkcase.gms.model.GmsConstants;
import com.arkcase.gms.model.form.SubmissionForm;
import com.armedia.acm.plugins.complaint.service.ComplaintService;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by riste.tutureski on 11/25/2015.
 */
public class SubmissionFormService extends ComplaintService
{
    @Override
    public Object get(String action)
    {
        Object result = super.get(action);

        if (result == null && action != null)
        {
            if ("init-extensibility".equals(action))
            {
                result = initExtensibility();
            }
        }

        return result;
    }

    private Object initExtensibility()
    {
        SubmissionForm submissionForm = new SubmissionForm();
        submissionForm.setDate(new Date());

        JSONObject json = createResponse(submissionForm);

        return json;
    }

    @Override
    public String getFormName()
    {
        return GmsConstants.FORM_NAME_SUBMISSION;
    }

    @Override
    public Class<?> getFormClass()
    {
        return SubmissionForm.class;
    }
}
