package com.arkcase.gms.model.form;

import com.arkcase.gms.model.GmsConstants;
import com.armedia.acm.plugins.complaint.model.complaint.ComplaintForm;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by riste.tutureski on 11/25/2015.
 */
@XmlRootElement(name = "form_" + GmsConstants.FORM_NAME_SUBMISSION, namespace = GmsConstants.FORM_NAMESPACE_SUBMISSION)
public class SubmissionForm extends ComplaintForm
{
    private String grantNumber;

    @XmlElement(name = "grantNumber")
    public String getGrantNumber()
    {
        return grantNumber;
    }

    public void setGrantNumber(String grantNumber)
    {
        this.grantNumber = grantNumber;
    }
}
