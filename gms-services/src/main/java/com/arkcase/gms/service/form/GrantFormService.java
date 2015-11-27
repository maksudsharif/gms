package com.arkcase.gms.service.form;

import com.arkcase.gms.model.GmsConstants;
import com.arkcase.gms.model.form.GrantForm;
import com.armedia.acm.form.casefile.service.CaseFileService;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by riste.tutureski on 11/19/2015.
 */
public class GrantFormService extends CaseFileService
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
        GrantForm grantForm = new GrantForm();

        grantForm.setCreatedDate(new Date());

        JSONObject json = createResponse(grantForm);

        return json;
    }

    @Override
    public String getFormName()
    {
        return GmsConstants.FORM_NAME_GRANT;
    }

    @Override
    public Class<?> getFormClass()
    {
        return GrantForm.class;
    }
}
