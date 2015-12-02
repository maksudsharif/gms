package com.arkcase.gms.service.form;

import com.arkcase.gms.model.form.CostBudgetForm;
import com.arkcase.gms.utils.BeanUtilsBean;
import com.armedia.acm.form.config.xml.ApproverItem;
import com.armedia.acm.form.cost.model.CostForm;
import com.armedia.acm.form.cost.model.CostItem;
import com.armedia.acm.form.cost.service.CostService;
import com.armedia.acm.frevvo.model.FrevvoForm;
import com.armedia.acm.plugins.ecm.model.AcmContainer;
import com.armedia.acm.services.costsheet.model.AcmCostsheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * Created by riste.tutureski on 12/2/2015.
 */
public abstract class CostAbstractService extends CostService
{
    private Logger LOG = LoggerFactory.getLogger(getClass());

    private BeanUtilsBean beanUtilsBean;

    @Override
    public Object init()
    {
        Object result = "";

        if (getDocUriParameters() == null || "".equals(getDocUriParameters()))
        {
            return result;
        }

        String userId = getAuthentication().getName();
        String objectId = getDocUriParameter("objectId");
        String objectType = getDocUriParameter("objectType");

        CostForm form = getInstanceFromCaseForm();
        AcmCostsheet costsheet = null;

        if (objectId != null && !"".equals(objectId))
        {
            try
            {
                Long objectIdLong = Long.parseLong(objectId);
                costsheet = getAcmCostsheetDao().findByUserIdObjectIdAndType(userId, objectIdLong, objectType);
                form.setObjectId(objectIdLong);
            }
            catch(Exception e)
            {
                LOG.error("Cannot parse " + objectId + " to Long type. Empty form will be created.", e);
            }
        }

        if (costsheet != null)
        {
            CostForm costForm = getCostFactory().asFrevvoCostForm(costsheet);
            if (costForm != null)
            {
                try
                {
                    getBeanUtilsBean().copyProperties(form, costForm);
                } catch (IllegalAccessException | InvocationTargetException e)
                {
                    LOG.error("Cannot copy properties values to the Grant object.", e);
                }
            }
            form = getInstanceFromEditCaseForm(form, costsheet.getContainer(), getFormName());
        }
        else
        {
            form.setItems(Arrays.asList(new CostItem()));
        }

        form.setObjectType(objectType);
        form.setUser(userId);
        form.setBalanceTable(Arrays.asList(new String()));

        if (form.getApprovers() == null || form.getApprovers().size() == 0)
        {
            form.setApprovers(Arrays.asList(new ApproverItem()));
        }

        form.setDocUriParameters(getDocUriParameters());

        result = convertFromObjectToXML(form);

        return result;
    }

    public abstract CostForm getInstanceFromCaseForm();
    public abstract CostForm getInstanceFromEditCaseForm(FrevvoForm form, AcmContainer container, String formName);

    public BeanUtilsBean getBeanUtilsBean()
    {
        return beanUtilsBean;
    }

    public void setBeanUtilsBean(BeanUtilsBean beanUtilsBean)
    {
        this.beanUtilsBean = beanUtilsBean;
    }
}
