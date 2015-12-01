package com.arkcase.gms.model;

import com.armedia.acm.plugins.casefile.model.Disposition;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by riste.tutureski on 11/27/2015.
 */
@Entity
@DiscriminatorValue("com.arkcase.gms.model.Decision")
public class Decision extends Disposition
{
    @Column(name = "gms_award_value")
    private Long awardValue;

    public Long getAwardValue()
    {
        return awardValue;
    }

    public void setAwardValue(Long awardValue)
    {
        this.awardValue = awardValue;
    }
}
