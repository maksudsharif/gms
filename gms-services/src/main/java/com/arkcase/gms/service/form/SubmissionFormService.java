package com.arkcase.gms.service.form;

import com.arkcase.gms.model.GmsConstants;
import com.arkcase.gms.model.Grant;
import com.arkcase.gms.model.Submission;
import com.armedia.acm.frevvo.config.FrevvoFormName;
import com.armedia.acm.frevvo.model.FrevvoUploadedFiles;
import com.armedia.acm.plugins.casefile.dao.CaseFileDao;
import com.armedia.acm.plugins.casefile.model.CaseFile;
import com.armedia.acm.plugins.complaint.dao.ComplaintDao;
import com.armedia.acm.plugins.complaint.model.Complaint;
import com.armedia.acm.plugins.complaint.model.complaint.ComplaintForm;
import com.arkcase.gms.model.form.SubmissionForm;
import com.armedia.acm.plugins.complaint.service.ComplaintService;
import com.armedia.acm.plugins.objectassociation.model.ObjectAssociation;
import com.armedia.acm.services.users.model.AcmUserActionName;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Optional;

/**
 * Created by riste.tutureski on 11/25/2015.
 */
public class SubmissionFormService extends ComplaintService
{
    private Logger LOG = LoggerFactory.getLogger(getClass());
    private ApplicationEventPublisher applicationEventPublisher;
    private ComplaintDao complaintDao;
    private CaseFileDao caseFileDao;

    @Override
    public boolean save(String xml, MultiValueMap<String, MultipartFile> attachments) throws Exception
    {
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

        addReferences(acmComplaint.getComplaintId());

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
        SubmissionForm submissionForm = new SubmissionForm();
        submissionForm.setDate(new Date());

        JSONObject json = createResponse(submissionForm);

        return json;
    }

    private void addReferences(Long submissionId)
    {
        if (submissionId != null)
        {
            Submission submission = (Submission) getComplaintDao().find(submissionId);

            if (submission != null && submission.getComplaintId() != null)
            {
                Grant grant = submission.getGrant();

                if (grant != null)
                {
                    Optional<ObjectAssociation> foundSubmissionAssociation = grant.getChildObjects().stream().filter(oa -> oa.getParentId() != null && oa.getParentId().equals(submission.getComplaintId())).findFirst();

                    if (foundSubmissionAssociation == null || !foundSubmissionAssociation.isPresent())
                    {
                        ObjectAssociation grantAssociation = makeObjectAssociation(grant.getId(), grant.getCaseNumber(), GmsConstants.GRANT, grant.getTitle());
                        submission.addChildObject(grantAssociation);
                        try
                        {
                            getComplaintDao().save(submission);
                        } catch (Exception e)
                        {
                            LOG.error("Cannot save Submission while adding reference.", e);
                        }
                    }

                    Optional<ObjectAssociation> foundGrantAssociation = submission.getChildObjects().stream().filter(oa -> oa.getParentId() != null && oa.getParentId().equals(grant.getId())).findFirst();

                    if (foundGrantAssociation == null || !foundGrantAssociation.isPresent())
                    {
                        ObjectAssociation submissionAssociation = this.makeObjectAssociation(submission.getComplaintId(), submission.getComplaintNumber(), GmsConstants.SUMBISSION, submission.getComplaintTitle());
                        grant.addChildObject(submissionAssociation);
                        try
                        {
                            getCaseFileDao().save(grant);
                        } catch (Exception e)
                        {
                            LOG.error("Cannot save Grant while adding reference.", e);
                        }
                    }
                }
            }
        }
    }

    private ObjectAssociation makeObjectAssociation(Long id, String number, String type, String title)
    {
        ObjectAssociation oa = new ObjectAssociation();
        oa.setTargetId(id);
        oa.setTargetName(number);
        oa.setTargetType(type);
        oa.setTargetTitle(title);
        oa.setAssociationType("REFERENCE");
        return oa;
    }

    public ComplaintDao getComplaintDao()
    {
        return complaintDao;
    }

    public void setComplaintDao(ComplaintDao complaintDao)
    {
        this.complaintDao = complaintDao;
    }

    public CaseFileDao getCaseFileDao()
    {
        return caseFileDao;
    }

    public void setCaseFileDao(CaseFileDao caseFileDao)
    {
        this.caseFileDao = caseFileDao;
    }

    public ApplicationEventPublisher getApplicationEventPublisher() {
        return applicationEventPublisher;
    }

    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher)
    {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public String getFormName()
    {
        return GmsConstants.FORM_NAME_SUBMISSION;
    }

    @Override
    public Class<?> getFormClass()
    {
        return SubmissionForm.class;
    }
}
