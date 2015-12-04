package com.arkcase.gms.service.form;

import com.arkcase.gms.utils.BeanUtilsBean;
import com.armedia.acm.form.cost.model.CostForm;
import com.armedia.acm.form.cost.service.CostFactory;
import com.armedia.acm.services.costsheet.model.AcmCostsheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by riste.tutureski on 12/3/2015.
 */
public abstract class CostAbstractFactory extends CostFactory
{
    private Logger LOG = LoggerFactory.getLogger(getClass());

    private BeanUtilsBean beanUtilsBean;

    @Override
    public AcmCostsheet asAcmCostsheet(CostForm form)
    {
        AcmCostsheet retval = null;
        AcmCostsheet costsheet = super.asAcmCostsheet(form);

        if (costsheet != null)
        {
            retval = createCostsheetInstance();

            try
            {
                getBeanUtilsBean().copyProperties(retval, costsheet);
            } catch (IllegalAccessException | InvocationTargetException e)
            {
                LOG.error("Cannot copy properties values to the Grant object.", e);
            }
        }

        return retval;
    }

    @Override
    public CostForm asFrevvoCostForm(AcmCostsheet costsheet)
    {
        CostForm retval = null;
        CostForm costForm = super.asFrevvoCostForm(costsheet);

        if (costForm != null)
        {
            retval = createCostFormInstance();
            try
            {
                getBeanUtilsBean().copyProperties(retval, costForm);
            } catch (IllegalAccessException | InvocationTargetException e)
            {
                LOG.error("Cannot copy properties values to the Grant object.", e);
            }
        }

        return retval;
    }

    public abstract AcmCostsheet createCostsheetInstance();
    public abstract CostForm createCostFormInstance();

    public BeanUtilsBean getBeanUtilsBean()
    {
        return beanUtilsBean;
    }

    public void setBeanUtilsBean(BeanUtilsBean beanUtilsBean)
    {
        this.beanUtilsBean = beanUtilsBean;
    }
}
