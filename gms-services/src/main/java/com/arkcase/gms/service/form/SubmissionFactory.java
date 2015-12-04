package com.arkcase.gms.service.form;

import com.arkcase.gms.model.Grant;
import com.arkcase.gms.model.Submission;
import com.arkcase.gms.model.form.SubmissionForm;
import com.arkcase.gms.utils.BeanUtilsBean;
import com.armedia.acm.plugins.casefile.dao.CaseFileDao;
import com.armedia.acm.plugins.casefile.model.CaseFile;
import com.armedia.acm.plugins.complaint.model.Complaint;
import com.armedia.acm.plugins.complaint.model.complaint.ComplaintForm;
import com.armedia.acm.plugins.complaint.service.ComplaintFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by riste.tutureski on 11/25/2015.
 */
public class SubmissionFactory extends ComplaintFactory
{
    private Logger LOG = LoggerFactory.getLogger(getClass());

    private BeanUtilsBean beanUtilsBean;
    private CaseFileDao caseFileDao;

    @Override
    public Complaint asAcmComplaint(ComplaintForm formComplaint)
    {
        Complaint _complaint = super.asAcmComplaint(formComplaint);
        Submission submission = null;

        if (_complaint != null)
        {
            submission = new Submission();

            try
            {
                getBeanUtilsBean().copyProperties(submission, _complaint);
            } catch (IllegalAccessException | InvocationTargetException e)
            {
                LOG.error("Cannot copy properties values to the Grant object.", e);
            }

            SubmissionForm submissionForm = (SubmissionForm) formComplaint;

            Grant grant = getGrantForNumber(submissionForm.getGrantNumber());
            submission.setGrant(grant);
        }

        return submission;
    }

    @Override
    public ComplaintForm asFrevvoComplaint(Complaint complaint, ComplaintForm complaintForm)
    {
        ComplaintForm _form = super.asFrevvoComplaint(complaint, complaintForm);
        SubmissionForm retval = null;

        if (_form instanceof SubmissionForm)
        {
            retval = (SubmissionForm) _form;
        }
        else
        {
            try
            {
                retval = new SubmissionForm();
                getBeanUtilsBean().copyProperties(retval, _form);
            } catch (IllegalAccessException | InvocationTargetException e)
            {
                LOG.error("Cannot copy properties values to the Grant object.", e);
            }
        }

        if (retval != null && complaint != null && complaint instanceof Submission)
        {
            Submission submission = (Submission) complaint;
            Grant grant = submission.getGrant();

            if (grant != null)
            {
                retval.setGrantNumber(grant.getCaseNumber());
            }
        }

        return retval;
    }

    private Grant getGrantForNumber(String grantNumber)
    {
        Grant grant = null;

        if (grantNumber != null)
        {
            CaseFile caseFile = getCaseFileDao().findByCaseNumber(grantNumber);

            if (caseFile instanceof Grant)
            {
                grant = (Grant) caseFile;
            }
        }

        return grant;
    }

    public BeanUtilsBean getBeanUtilsBean()
    {
        return beanUtilsBean;
    }

    public void setBeanUtilsBean(BeanUtilsBean beanUtilsBean)
    {
        this.beanUtilsBean = beanUtilsBean;
    }

    public CaseFileDao getCaseFileDao()
    {
        return caseFileDao;
    }

    public void setCaseFileDao(CaseFileDao caseFileDao)
    {
        this.caseFileDao = caseFileDao;
    }
}
