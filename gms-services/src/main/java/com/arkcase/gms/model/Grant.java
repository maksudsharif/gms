package com.arkcase.gms.model;

import com.armedia.acm.plugins.casefile.model.CaseFile;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Created by riste.tutureski on 11/18/2015.
 */
@Entity
@DiscriminatorValue("com.arkcase.gms.model.Grant")
public class Grant extends CaseFile
{
    @Column(name = "gms_total_anticipated_funds")
    private Long totalAnticipatedFunds;

    @Column(name = "gms_expected_award_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expectedAwardDate;

    @Column(name = "gms_grant_type")
    private String grantType;

    public Long getTotalAnticipatedFunds()
    {
        return totalAnticipatedFunds;
    }

    public void setTotalAnticipatedFunds(Long totalAnticipatedFunds)
    {
        this.totalAnticipatedFunds = totalAnticipatedFunds;
    }

    public Date getExpectedAwardDate() {
        return expectedAwardDate;
    }

    public void setExpectedAwardDate(Date expectedAwardDate) {
        this.expectedAwardDate = expectedAwardDate;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }
}