package com.arkcase.gms.service.form;

import com.arkcase.gms.model.GmsConstants;
import com.arkcase.gms.model.form.AwardForm;
import com.armedia.acm.plugins.complaint.service.ComplaintService;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by riste.tutureski on 11/25/2015.
 */
public class AwardFormService extends ComplaintService
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
        AwardForm awardForm = new AwardForm();
        awardForm.setDate(new Date());

        JSONObject json = createResponse(awardForm);

        return json;
    }

    @Override
    public String getFormName()
    {
        return GmsConstants.FORM_NAME_AWARD;
    }

    @Override
    public Class<?> getFormClass()
    {
        return AwardForm.class;
    }
}
