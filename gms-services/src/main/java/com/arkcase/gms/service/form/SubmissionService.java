package com.arkcase.gms.service.form;

import com.armedia.acm.frevvo.config.FrevvoFormName;
import com.armedia.acm.frevvo.model.FrevvoUploadedFiles;
import com.armedia.acm.plugins.complaint.model.Complaint;
import com.armedia.acm.plugins.complaint.model.complaint.ComplaintForm;
import com.armedia.acm.plugins.complaint.service.ComplaintService;
import com.armedia.acm.services.pipeline.exception.PipelineProcessException;
import com.armedia.acm.services.users.model.AcmUserActionName;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by maksud.sharif on 12/3/2015.
 */
public class SubmissionService extends ComplaintService {
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public boolean save(String xml, MultiValueMap<String, MultipartFile> attachments) throws Exception {
        ComplaintForm complaint = (ComplaintForm) convertFromXMLToObject(cleanXML(xml), getFormClass());

        complaint = saveComplaint(complaint);

        Complaint acmComplaint = getComplaintFactory().asAcmComplaint(complaint); //To create event

        // Update Frevvo XML (with object ids) after saving the object
        updateXMLAttachment(attachments, getFormName(), complaint);

        FrevvoUploadedFiles uploadedFiles = saveAttachments(
                attachments,
                complaint.getCmisFolderId(),
                FrevvoFormName.COMPLAINT.toUpperCase(),
                complaint.getComplaintId());

        if (null != complaint && null != complaint.getComplaintId())
        {
            getUserActionExecutor().execute(complaint.getComplaintId(), AcmUserActionName.LAST_COMPLAINT_CREATED, getAuthentication().getName());
        }

        SubmissionCreatedEvent event = new SubmissionCreatedEvent(acmComplaint, uploadedFiles, getUserIpAddress(), true);
        getApplicationEventPublisher().publishEvent(event);
        return true;
    }

    public ApplicationEventPublisher getApplicationEventPublisher() {
        return applicationEventPublisher;
    }
}
