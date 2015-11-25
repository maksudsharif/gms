package com.arkcase.gms.service.form;

import com.arkcase.gms.model.GmsConstants;
import com.arkcase.gms.model.UnawardedGrantForm;
import com.armedia.acm.form.casefile.service.CaseFileService;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by riste.tutureski on 11/19/2015.
 */
public class UnawardedGrantFormService extends CaseFileService
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
        UnawardedGrantForm unawardedGrantForm = new UnawardedGrantForm();

        unawardedGrantForm.setCreatedDate(new Date());

        JSONObject json = createResponse(unawardedGrantForm);

        return json;
    }

    @Override
    public String getFormName()
    {
        return GmsConstants.FORM_NAME_UNAWARDED_GRANT;
    }

    @Override
    public Class<?> getFormClass()
    {
        return UnawardedGrantForm.class;
    }
}
