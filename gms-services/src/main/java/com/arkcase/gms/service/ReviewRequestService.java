package com.arkcase.gms.service;

import com.arkcase.gms.model.Submission;
import com.arkcase.gms.model.Decision;
import com.arkcase.gms.model.GmsConstants;
import com.arkcase.gms.model.Grant;
import com.armedia.acm.plugins.complaint.model.CloseComplaintRequest;
import com.armedia.acm.plugins.complaint.service.CloseComplaintRequestService;
import com.armedia.acm.services.pipeline.exception.PipelineProcessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by riste.tutureski on 11/30/2015.
 */
public class ReviewRequestService extends CloseComplaintRequestService
{
    private Logger LOG = LoggerFactory.getLogger(getClass());

    @Transactional
    public void handleCloseComplaintRequestApproved(Long complaintId, Long closeComplaintRequestId, String user, Date approvalDate) throws PipelineProcessException
    {
        if (complaintId != null && closeComplaintRequestId != null)
        {
            Submission submission = (Submission) getComplaintDao().find(complaintId);
            CloseComplaintRequest request = getCloseComplaintRequestDao().find(closeComplaintRequestId);

            if (request.getDisposition() != null && request.getDisposition() instanceof Decision)
            {
                Decision decision = (Decision) request.getDisposition();
                submission.setRating(decision.getRating());
                getComplaintDao().save(submission);
            }

            if (isAward(request))
            {
                Long awardValue = null;
                if (request.getDisposition() != null && request.getDisposition() instanceof Decision)
                {
                    Decision decision = (Decision) request.getDisposition();
                    awardValue = decision.getAwardValue();
                }

                Grant grant = new Grant();
                grant.setGrantType(GmsConstants.TYPE_AWARDED_GRANT);
                grant.setAwardValue(awardValue);
                Grant newGrant = (Grant) openFullInvestigation(submission, user, grant, GmsConstants.SUMBISSION);
                addReferenceToComplaint(submission, newGrant, GmsConstants.AWARD);
            }
        }

        super.handleCloseComplaintRequestApproved(complaintId, closeComplaintRequestId, user, approvalDate);
    }

    private boolean isAward(CloseComplaintRequest request)
    {
        if (request == null)
        {
            LOG.debug("No provided request");
            return false;
        }

        if (request.getDisposition() == null || (request.getDisposition().getDispositionType() == null || request.getDisposition().getDispositionType().isEmpty()))
        {
            LOG.debug("No disposition for request ID '{}'", request.getId());
            return false;
        }

        return "award".equalsIgnoreCase(request.getDisposition().getDispositionType());
    }
}
