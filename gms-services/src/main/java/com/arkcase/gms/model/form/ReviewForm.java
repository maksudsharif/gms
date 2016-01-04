package com.arkcase.gms.model.form;

import com.arkcase.gms.model.GmsConstants;
import com.arkcase.gms.model.form.xml.ReviewResolveInformation;
import com.armedia.acm.form.closecomplaint.model.CloseComplaintForm;
import com.armedia.acm.form.config.ResolveInformation;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by riste.tutureski on 11/27/2015.
 */
@XmlRootElement(name = "form_" + GmsConstants.FORM_NAME_REVIEW, namespace = GmsConstants.FORM_NAMESPACE_REVIEW)
public class ReviewForm extends CloseComplaintForm
{
    private Long awardValue;
    private Long rating;

    @XmlElement(name = "awardValue")
    public Long getAwardValue()
    {
        return awardValue;
    }

    public void setAwardValue(Long awardValue)
    {
        this.awardValue = awardValue;
    }

    @XmlElement(name = "rating")
    public Long getRating()
    {
        return rating;
    }

    public void setRating(Long rating)
    {
        this.rating = rating;
    }

    @XmlElement(name = "information", type=ReviewResolveInformation.class)
    @Override
    public ResolveInformation getInformation() {
        return super.getInformation();
    }
}
