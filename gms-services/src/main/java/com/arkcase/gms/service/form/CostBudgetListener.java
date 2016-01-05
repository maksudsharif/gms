package com.arkcase.gms.service.form;

import com.arkcase.gms.model.GmsConstants;
import com.arkcase.gms.model.Grant;
import com.arkcase.gms.service.CostsheetService;
import com.armedia.acm.plugins.casefile.dao.CaseFileDao;
import com.armedia.acm.plugins.casefile.model.CaseFile;
import com.armedia.acm.services.costsheet.model.AcmCost;
import com.armedia.acm.services.costsheet.model.AcmCostsheet;
import com.armedia.acm.services.costsheet.model.AcmCostsheetEvent;
import org.springframework.context.ApplicationListener;

import java.util.List;

/**
 * Created by riste.tutureski on 1/5/2016.
 */
public class CostBudgetListener implements ApplicationListener<AcmCostsheetEvent>
{
    private CaseFileDao caseFileDao;
    private CostsheetService costsheetService;

    @Override
    public void onApplicationEvent(AcmCostsheetEvent event)
    {
        if (event != null && event.getSource() != null)
        {
            AcmCostsheet costsheet = (AcmCostsheet) event.getSource();
            Long awardId = costsheet.getParentId();
            Grant grant = getGrant(awardId);

            if (grant != null)
            {
                calculateRemainingAwardValue(grant);
            }
        }
    }

    private Grant getGrant(Long awardId)
    {
        if (awardId != null)
        {
            CaseFile caseFile = getCaseFileDao().find(awardId);

            if (caseFile != null && caseFile instanceof Grant && GmsConstants.TYPE_AWARDED_GRANT.equalsIgnoreCase(((Grant) caseFile).getGrantType()))
            {
                return (Grant) caseFile;
            }
        }

        return null;
    }

    private void calculateRemainingAwardValue(Grant grant)
    {
        if (grant != null)
        {
            Long awardValue = grant.getAwardValue();
            Long awardCost = 0L;

            List<AcmCostsheet> costsheets;

            int start = 0;
            int max = 50;
            do
            {
                costsheets = getCostsheetService().getByObjectIdAndType(grant.getId(), grant.getObjectType(), start, max, null);

                if (costsheets != null)
                {
                    awardCost += costsheets.stream().mapToLong(costsheet -> calculateCosts(costsheet)).sum();
                }

                start += max;
            }while (costsheets != null && costsheets.size() > 0);

            if (awardValue != null && awardCost != null)
            {
                grant.setAwardRemainingValue(awardValue - awardCost);
                getCaseFileDao().save(grant);
            }
        }
    }

    private Long calculateCosts(AcmCostsheet costsheet)
    {
        if (costsheet != null && costsheet.getCosts() != null)
        {
            return costsheet.getCosts().stream().mapToLong(cost -> costAsLong(cost)).sum();
        }

        return 0L;
    }

    private Long costAsLong(AcmCost cost)
    {
        if (cost != null && cost.getValue() != null)
        {
            return cost.getValue().longValue();
        }

        return 0L;
    }

    public CaseFileDao getCaseFileDao()
    {
        return caseFileDao;
    }

    public void setCaseFileDao(CaseFileDao caseFileDao)
    {
        this.caseFileDao = caseFileDao;
    }

    public CostsheetService getCostsheetService()
    {
        return costsheetService;
    }

    public void setCostsheetService(CostsheetService costsheetService)
    {
        this.costsheetService = costsheetService;
    }
}