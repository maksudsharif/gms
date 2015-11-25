package com.arkcase.gms.model;

import com.armedia.acm.form.casefile.model.CaseFileForm;
import com.armedia.acm.objectonverter.adapter.DateFrevvoAdapter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

/**
 * Created by riste.tutureski on 11/19/2015.
 */
@XmlRootElement(name = "form_" + GmsConstants.FORM_NAME_UNAWARDED_GRANT, namespace = GmsConstants.FORM_NAMESPACE_UNAWARDED_GRANT)
public class UnawardedGrantForm extends CaseFileForm
{
    private Long totalAnticipatedFunds;
    private Date createdDate;
    private Date expectedAwardDate;
    private Date applicationDueDate;

    @XmlElement(name = "totalAnticipatedFunds")
    public Long getTotalAnticipatedFunds() {
        return totalAnticipatedFunds;
    }

    public void setTotalAnticipatedFunds(Long totalAnticipatedFunds) {
        this.totalAnticipatedFunds = totalAnticipatedFunds;
    }

    @XmlJavaTypeAdapter(value = DateFrevvoAdapter.class)
    @XmlElement(name = "createdDate")
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @XmlJavaTypeAdapter(value = DateFrevvoAdapter.class)
    @XmlElement(name = "expectedAwardDate")
    public Date getExpectedAwardDate() {
        return expectedAwardDate;
    }

    public void setExpectedAwardDate(Date expectedAwardDate) {
        this.expectedAwardDate = expectedAwardDate;
    }

    @XmlJavaTypeAdapter(value = DateFrevvoAdapter.class)
    @XmlElement(name = "applicationDueDate")
    public Date getApplicationDueDate() {
        return applicationDueDate;
    }

    public void setApplicationDueDate(Date applicationDueDate) {
        this.applicationDueDate = applicationDueDate;
    }
}
