package com.arkcase.gms.model;

import com.armedia.acm.plugins.complaint.model.Complaint;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created by riste.tutureski on 11/25/2015.
 */
@Entity
@DiscriminatorValue("com.arkcase.gms.model.Submission")
public class Submission extends Complaint
{
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "gms_grant_id", referencedColumnName = "cm_case_id")
    private Grant grant;

    public Grant getGrant()
    {
        return grant;
    }

    public void setGrant(Grant grant)
    {
        this.grant = grant;
    }
}
