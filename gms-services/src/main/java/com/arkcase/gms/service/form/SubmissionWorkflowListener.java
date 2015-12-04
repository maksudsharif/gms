package com.arkcase.gms.service.form;

import com.arkcase.gms.model.GmsConstants;
import com.armedia.acm.plugins.complaint.model.Complaint;
import com.armedia.acm.plugins.ecm.model.EcmFile;
import com.armedia.acm.plugins.ecm.service.impl.FileWorkflowBusinessRule;
import com.armedia.acm.plugins.ecm.workflow.EcmFileWorkflowConfiguration;
import com.armedia.acm.services.participants.model.AcmParticipant;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;

import java.util.*;

/**
 * Created by maksud.sharif on 12/2/2015.
 */
public class SubmissionWorkflowListener implements ApplicationListener<SubmissionCreatedEvent> {
    private FileWorkflowBusinessRule fileWorkflowBusinessRule;
    private RuntimeService activitiRuntimeService;
    private String reviewSubmissionTaskName;

    private String defaultApproversString;
    private List<String> defaultApprovers;

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void onApplicationEvent(SubmissionCreatedEvent submissionCreatedEvent) {
        String eventType = submissionCreatedEvent.getEventType();
        if("com.armedia.acm.submission.created".equals(eventType)) {
            handleNewComplaintCreated(submissionCreatedEvent);
        }
    }

    private void handleNewComplaintCreated(SubmissionCreatedEvent submissionCreatedEvent) {
        EcmFile pdfRendition = submissionCreatedEvent.getFrevvoUploadedFiles().getPdfRendition();
        EcmFileWorkflowConfiguration configuration = new EcmFileWorkflowConfiguration();
        configuration.setEcmFile(pdfRendition);

        configuration = getFileWorkflowBusinessRule().applyRules(configuration);

        if(configuration.isStartProcess()) {
            startBusinessProcess(submissionCreatedEvent, configuration);
        }
    }

    private void startBusinessProcess(SubmissionCreatedEvent submissionCreatedEvent, EcmFileWorkflowConfiguration configuration) {
        String processName = configuration.getProcessName();

        String author = submissionCreatedEvent.getUserId();
        List<String> reviewers = findReviewers(submissionCreatedEvent);

        String taskName = "Task " + submissionCreatedEvent.getComplaintNumber();

        if(getReviewSubmissionTaskName() != null && !getReviewSubmissionTaskName().isEmpty()) {
            taskName = String.format(getReviewSubmissionTaskName(), submissionCreatedEvent.getComplaintNumber());
        }

        Map<String, Object> pvars = new HashMap<>();
        pvars.put("reviewers", reviewers);
        pvars.put("taskName", taskName);
        pvars.put("documentAuthor", author);
        pvars.put("pdfRenditionId", submissionCreatedEvent.getFrevvoUploadedFiles().getPdfRendition().getFileId());
        pvars.put("formXmlId", submissionCreatedEvent.getFrevvoUploadedFiles().getFormXml().getFileId());

        pvars.put("OBJECT_TYPE", submissionCreatedEvent.getObjectType());
        pvars.put("OBJECT_ID", submissionCreatedEvent.getObjectId());
        pvars.put("OBJECT_NAME", submissionCreatedEvent.getComplaintNumber());
        pvars.put("COMPLAINT", submissionCreatedEvent.getObjectId());

        log.debug("starting process: " + processName);
        ProcessInstance pi = getActivitiRuntimeService().startProcessInstanceByKey(processName, pvars);
        log.debug("process ID: " + pi.getId());
    }

    private List<String> findReviewers(SubmissionCreatedEvent submissionCreatedEvent) {
        List<String> reviewers = new ArrayList<>();

        /**
         * Work around for no approvers in form.
         */
        getDefaultApprovers().forEach(approver -> {
           reviewers.add(approver);
        });

        Complaint complaint = (Complaint) submissionCreatedEvent.getSource();
        for ( AcmParticipant participant : complaint.getParticipants() )
        {
            if ( "approver".equals(participant.getParticipantType() ) )
            {
                // Make sure there are no duplicates
                if(!reviewers.contains(participant.getParticipantLdapId()))
                {
                    reviewers.add(participant.getParticipantLdapId());
                }
            }
        }

        return reviewers;
    }

    public FileWorkflowBusinessRule getFileWorkflowBusinessRule() {
        return fileWorkflowBusinessRule;
    }

    public void setFileWorkflowBusinessRule(FileWorkflowBusinessRule fileWorkflowBusinessRule) {
        this.fileWorkflowBusinessRule = fileWorkflowBusinessRule;
    }

    public RuntimeService getActivitiRuntimeService() {
        return activitiRuntimeService;
    }

    public void setActivitiRuntimeService(RuntimeService activitiRuntimeService) {
        this.activitiRuntimeService = activitiRuntimeService;
    }

    public String getReviewSubmissionTaskName() {
        return reviewSubmissionTaskName;
    }

    public void setReviewSubmissionTaskName(String reviewSubmissionTaskName) {
        this.reviewSubmissionTaskName = reviewSubmissionTaskName;
    }

    public String getDefaultApproversString() {
        return defaultApproversString;
    }

    public void setDefaultApproversString(String defaultApproversString) {
        this.defaultApproversString = defaultApproversString;

        ArrayList<String> approverList = new ArrayList(Arrays.asList(defaultApproversString.split(",")));
        setDefaultApprovers(approverList);

    }

    public List<String> getDefaultApprovers() {
        return defaultApprovers;
    }

    public void setDefaultApprovers(List<String> defaultApprovers) {
        this.defaultApprovers = defaultApprovers;
    }
}
