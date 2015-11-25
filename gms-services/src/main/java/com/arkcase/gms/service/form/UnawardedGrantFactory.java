package com.arkcase.gms.service.form;

import com.arkcase.gms.model.GmsConstants;
import com.arkcase.gms.model.Grant;
import com.arkcase.gms.model.UnawardedGrantForm;
import com.arkcase.gms.utils.BeanUtilsBean;
import com.armedia.acm.form.casefile.model.CaseFileForm;
import com.armedia.acm.form.casefile.service.CaseFileFactory;
import com.armedia.acm.frevvo.config.FrevvoFormAbstractService;
import com.armedia.acm.plugins.casefile.model.CaseFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * Created by riste.tutureski on 11/19/2015.
 */
public class UnawardedGrantFactory extends CaseFileFactory
{
    private Logger LOG = LoggerFactory.getLogger(getClass());

    private BeanUtilsBean beanUtilsBean;

    @Override
    public CaseFile asAcmCaseFile(CaseFileForm form, CaseFile caseFile)
    {
        CaseFile _caseFile = super.asAcmCaseFile(form, caseFile);
        Grant grant = null;

        if (_caseFile != null)
        {
            grant = new Grant();
            grant.setGrantType(GmsConstants.TYPE_UNAWARDED_GRANT);

            try
            {
                getBeanUtilsBean().copyProperties(grant, _caseFile);
            } catch (IllegalAccessException | InvocationTargetException e)
            {
                LOG.error("Cannot copy properties values to the Grant object.", e);
            }

            UnawardedGrantForm unawardedGrantForm = (UnawardedGrantForm) form;

            grant.setExpectedAwardDate(unawardedGrantForm.getExpectedAwardDate());
            grant.setTotalAnticipatedFunds(unawardedGrantForm.getTotalAnticipatedFunds());
            grant.setDueDate(unawardedGrantForm.getApplicationDueDate());
        }

        return grant;
    }

    @Override
    public CaseFileForm asFrevvoCaseFile(CaseFile caseFile, CaseFileForm form, FrevvoFormAbstractService formService)
    {
        UnawardedGrantForm retval = (UnawardedGrantForm) super.asFrevvoCaseFile(caseFile, form, formService);

        if (retval != null && caseFile != null && caseFile instanceof Grant)
        {
            Grant grant = (Grant) caseFile;

            retval.setExpectedAwardDate(grant.getExpectedAwardDate());
            retval.setTotalAnticipatedFunds(grant.getTotalAnticipatedFunds());
            retval.setApplicationDueDate(grant.getDueDate());
        }

        return retval;
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
