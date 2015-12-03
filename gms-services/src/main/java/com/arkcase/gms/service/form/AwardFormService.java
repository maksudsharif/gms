package com.arkcase.gms.service.form;

import com.arkcase.gms.model.GmsConstants;
import com.arkcase.gms.model.form.AwardForm;
import com.armedia.acm.frevvo.config.FrevvoFormName;
import com.armedia.acm.frevvo.model.FrevvoUploadedFiles;
import com.armedia.acm.plugins.complaint.model.Complaint;
import com.armedia.acm.plugins.complaint.model.complaint.ComplaintForm;
import com.armedia.acm.plugins.complaint.service.ComplaintService;
import com.armedia.acm.services.users.model.AcmUserActionName;
import org.json.JSONObject;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * Created by riste.tutureski on 11/25/2015.
 */
public class AwardFormService extends ComplaintService
{
    private ApplicationEventPublisher applicationEventPublisher;
    @Override
    public boolean save(String xml, MultiValueMap<String, MultipartFile> attachments) throws Exception {
        ComplaintForm complaint = (ComplaintForm) convertFromXMLToObject(cleanXML(xml), getFormClass());

        complaint = saveComplaint(complaint);

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

        /**
         * Need the complaint to create the event, is there a better way to do this?
         */
        Complaint acmComplaint = getComplaintFactory().asAcmComplaint(complaint);

        SubmissionCreatedEvent event = new SubmissionCreatedEvent(acmComplaint, uploadedFiles, getUserIpAddress(), true);
        getApplicationEventPublisher().publishEvent(event);
        return true;
    }

    @Override
    public Object get(String action)
    {
        Object result = super.get(action);

        if (result == null && action != null)
        {
            if ("init-extensibility".equals(action))
            {
                result = initExtensibility();
            }
        }

        return result;
    }

    private Object initExtensibility()
    {
        AwardForm awardForm = new AwardForm();
        awardForm.setDate(new Date());

        JSONObject json = createResponse(awardForm);

        return json;
    }

    public ApplicationEventPublisher getApplicationEventPublisher() {
        return applicationEventPublisher;
    }

    @Override
    public String getFormName()
    {
        return GmsConstants.FORM_NAME_AWARD;
    }

    @Override
    public Class<?> getFormClass()
    {
        return AwardForm.class;
    }
}
