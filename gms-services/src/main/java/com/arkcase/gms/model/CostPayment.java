package com.arkcase.gms.model;

import com.armedia.acm.services.costsheet.model.AcmCostsheet;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by riste.tutureski on 12/3/2015.
 */
@Entity
@DiscriminatorValue("com.arkcase.gms.model.CostBudget")
public class CostPayment extends AcmCostsheet
{
}
