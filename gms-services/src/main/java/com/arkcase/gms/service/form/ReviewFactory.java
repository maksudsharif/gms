package com.arkcase.gms.service.form;

import com.arkcase.gms.model.Decision;
import com.arkcase.gms.model.form.ReviewForm;
import com.arkcase.gms.utils.BeanUtilsBean;
import com.armedia.acm.form.closecomplaint.model.CloseComplaintForm;
import com.armedia.acm.form.closecomplaint.service.CloseComplaintRequestFactory;
import com.armedia.acm.plugins.complaint.model.CloseComplaintRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by riste.tutureski on 11/27/2015.
 */
public class ReviewFactory extends CloseComplaintRequestFactory
{
    private Logger LOG = LoggerFactory.getLogger(getClass());

    private BeanUtilsBean beanUtilsBean;

    @Override
    public CloseComplaintRequest fromFormXml(CloseComplaintForm form, Authentication auth)
    {
        CloseComplaintRequest req = super.fromFormXml(form, auth);

        if (req != null && req.getDisposition() != null)
        {
            Decision decision = new Decision();

            try
            {
                getBeanUtilsBean().copyProperties(decision, req.getDisposition());
            } catch (IllegalAccessException | InvocationTargetException e)
            {
                LOG.error("Cannot copy properties values to the Decision object.", e);
            }

            if (form != null && form instanceof ReviewForm)
            {
                ReviewForm reviewForm = (ReviewForm) form;
                decision.setAwardValue(reviewForm.getAwardValue());
                req.setDisposition(decision);
            }
        }

        return req;
    }

    public BeanUtilsBean getBeanUtilsBean()
    {
        return beanUtilsBean;
    }

    public void setBeanUtilsBean(BeanUtilsBean beanUtilsBean)
    {
        this.beanUtilsBean = beanUtilsBean;
    }
}
