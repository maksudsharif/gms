package com.arkcase.gms.service.form;

import com.armedia.acm.plugins.ecm.service.impl.FileWorkflowBusinessRule;
import com.armedia.acm.plugins.ecm.workflow.EcmFileWorkflowConfiguration;
import com.armedia.acm.plugins.task.model.AcmApplicationTaskEvent;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;

import java.util.*;

/**
 * Created by maksud.sharif on 12/4/2015.
 */
public class SubmissionCompletedWorkflowListener implements ApplicationListener<AcmApplicationTaskEvent> {
    private FileWorkflowBusinessRule fileWorkflowBusinessRule;
    private RuntimeService activitiRuntimeService;
    private String panelReviewSubmissionTaskName;

    private String defaultApproversString;
    private List<String> defaultApprovers;

    private final Logger log = LoggerFactory.getLogger(getClass());


    @Override
    public void onApplicationEvent(AcmApplicationTaskEvent acmApplicationTaskEvent) {
        String eventType = acmApplicationTaskEvent.getEventType();
        if("com.armedia.acm.app.task.complete".equals(eventType)) {
            handleSubmissionCompleted(acmApplicationTaskEvent);
        }
    }

    private void handleSubmissionCompleted(AcmApplicationTaskEvent acmApplicationTaskEvent) {
        String outcome = acmApplicationTaskEvent.getAcmTask().getTaskOutcome().getName();
        String businessProcessId = acmApplicationTaskEvent.getAcmTask().getBusinessProcessId().toString();
        String processVariable = "gms_approvalCount";

        Integer approvalCount;
        if ("APPROVE".equals(outcome)) { // Is this the correct outcome string?
            /**
             *  TODO: Get activiti proccess and update "gms_approvalCount" variable
             *  Assume that a process with the variable 'gms_approvalCount' is one for
             *  a submission review.
             *
             *  Not sure if this is 100% valid
             */
            if (getActivitiRuntimeService().hasVariable(businessProcessId, processVariable)) {
                approvalCount = (Integer) getActivitiRuntimeService().getVariable(businessProcessId, processVariable);
                approvalCount++;
                getActivitiRuntimeService().setVariable(businessProcessId, processVariable, approvalCount);

                if (approvalCount == 2) { //Stop multiple tasks from going out
                    EcmFileWorkflowConfiguration configuration = new EcmFileWorkflowConfiguration();
                    startBusinessProcess(acmApplicationTaskEvent, configuration);
                }
            } else {
                //Process is not a submission review task
            }
        }
    }

    private void startBusinessProcess(AcmApplicationTaskEvent acmApplicationTaskEvent, EcmFileWorkflowConfiguration configuration) {
        String processName = configuration.getProcessName();

        String author = acmApplicationTaskEvent.getUserId();
        List<String> reviewers = findReviewers(acmApplicationTaskEvent);

        String taskName = "Task " + acmApplicationTaskEvent.getTaskName();

        if(getPanelReviewSubmissionTaskName() != null && !getPanelReviewSubmissionTaskName().isEmpty()) {
            taskName = String.format(getPanelReviewSubmissionTaskName(), acmApplicationTaskEvent.getTaskName());
        }
        
        Map<String, Object> pvars = new HashMap<>();
        pvars.put("reviewers", reviewers);
        pvars.put("taskName", taskName);
        pvars.put("documentAuthor", author);
        pvars.put("OBJECT_TYPE", acmApplicationTaskEvent.getObjectType());
        pvars.put("OBJECT_ID", acmApplicationTaskEvent.getObjectId());
        pvars.put("OBJECT_NAME", acmApplicationTaskEvent.getTaskName());

        log.debug("starting process: " + processName);
        ProcessInstance pi = getActivitiRuntimeService().startProcessInstanceByKey(processName, pvars);
        log.debug("process ID: " + pi.getId());
    }

    private List<String> findReviewers(AcmApplicationTaskEvent acmApplicationTaskEvent) {
        List<String> reviewers = new ArrayList<>();
        getDefaultApprovers().forEach(approver -> {
            reviewers.add(approver);
        });

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

    public String getPanelReviewSubmissionTaskName() {
        return panelReviewSubmissionTaskName;
    }

    public void setPanelReviewSubmissionTaskName(String panelReviewSubmissionTaskName) {
        this.panelReviewSubmissionTaskName = panelReviewSubmissionTaskName;
    }

    public String getDefaultApproversString() {
        return defaultApproversString;
    }

    public void setDefaultApproversString(String defaultApproversString) {
        this.defaultApproversString = defaultApproversString;

        ArrayList<String> approverList = new ArrayList<String>(Arrays.asList(defaultApproversString.split(",")));
        setDefaultApprovers(approverList);
    }

    public List<String> getDefaultApprovers() {
        return defaultApprovers;
    }

    public void setDefaultApprovers(List<String> defaultApprovers) {
        this.defaultApprovers = defaultApprovers;
    }
}
