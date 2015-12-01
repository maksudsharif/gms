package com.arkcase.gms.model.form.xml;

import com.armedia.acm.form.config.xml.ComplaintResolveInformation;

import javax.xml.bind.annotation.XmlElement;

/**
 * Created by riste.tutureski on 11/30/2015.
 */
public class ReviewResolveInformation extends ComplaintResolveInformation
{
    private String title;
    private String submitter;

    @XmlElement(name = "complaintTitle")
    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    @XmlElement(name = "submitter")
    public String getSubmitter()
    {
        return submitter;
    }

    public void setSubmitter(String submitter)
    {
        this.submitter = submitter;
    }
}
