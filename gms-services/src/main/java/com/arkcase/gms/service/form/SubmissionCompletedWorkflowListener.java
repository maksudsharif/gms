package com.arkcase.gms.service.form;

import com.arkcase.gms.model.GmsConstants;
import com.armedia.acm.plugins.ecm.service.impl.FileWorkflowBusinessRule;
import com.armedia.acm.plugins.ecm.workflow.EcmFileWorkflowConfiguration;
import com.armedia.acm.plugins.task.exception.AcmTaskException;
import com.armedia.acm.plugins.task.model.AcmApplicationTaskEvent;
import com.armedia.acm.plugins.task.model.AcmTask;
import com.armedia.acm.plugins.task.service.TaskDao;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricProcessInstance;
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

    private HistoryService historyService;

    private TaskDao taskDao;

    @Override
    public void onApplicationEvent(AcmApplicationTaskEvent acmApplicationTaskEvent) {
        String eventType = acmApplicationTaskEvent.getEventType();
        if("com.armedia.acm.app.task.complete".equals(eventType)) {
            handleSubmissionCompleted(acmApplicationTaskEvent);
        }
    }

    /**
     * This method will be called if the raised event is <pre>com.armedia.acm.app.task.complete</pre>
     *
     * @param acmApplicationTaskEvent - task event
     */
    private void handleSubmissionCompleted(AcmApplicationTaskEvent acmApplicationTaskEvent) {
        String businessProcessId = null;

        // Take process id
        if (acmApplicationTaskEvent != null && acmApplicationTaskEvent.getAcmTask() != null && acmApplicationTaskEvent.getAcmTask().getBusinessProcessId() != null)
        {
            businessProcessId = acmApplicationTaskEvent.getAcmTask().getBusinessProcessId().toString();
        }

        // Check if this task event is for submission approval
        if (isSubmissionApproval(businessProcessId))
        {
            // Take outcome
            String outcomeName = acmApplicationTaskEvent.getAcmTask().getOutcomeName();
            String outcome = getOutcome(businessProcessId, outcomeName);

            if ("APPROVE".equalsIgnoreCase(outcome))
            {
                // Take approval count and increment it
                Integer approvalCount = getApprovalCount(businessProcessId);
                approvalCount++;
                // Save approval count (be aware of that if this task complete is the last one, the approval count will not be updated
                // because the process is finished)
                setApprovalCount(businessProcessId, approvalCount);
                if (approvalCount == 2)
                {
                    // If all reviewers complete their tasks, then create
                    createAdHockTask(acmApplicationTaskEvent);
                }
            }
        }
    }

    /**
     * Create ad-hock task
     *
     * @param acmApplicationTaskEvent
     */
    private void createAdHockTask(AcmApplicationTaskEvent acmApplicationTaskEvent)
    {
        // Taking author and reviewers
        String author = acmApplicationTaskEvent.getUserId();
        List<String> reviewers = findReviewers(acmApplicationTaskEvent);

        if (reviewers != null)
        {
            // For each reviewer, create task
            reviewers.stream().forEach(reviewer -> createAdHockTaskForReviewer(author, reviewer, acmApplicationTaskEvent));
        }
    }

    /**
     * Create ad-hock task for given reviewer
     *
     * @param author - the owner of the task creation
     * @param reviewer - reviewer name
     * @param acmApplicationTaskEvent - task event
     */
    private void createAdHockTaskForReviewer(String author, String reviewer, AcmApplicationTaskEvent acmApplicationTaskEvent)
    {
        if (author != null && reviewer != null && acmApplicationTaskEvent != null)
        {
            String taskName = "Task " + acmApplicationTaskEvent.getParentObjectName();

            // Create task title
            if(getPanelReviewSubmissionTaskName() != null && !getPanelReviewSubmissionTaskName().isEmpty()) {
                taskName = String.format(getPanelReviewSubmissionTaskName(), acmApplicationTaskEvent.getParentObjectName());
            }

            // Create start date
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());

            // Create end date
            Date startDate = calendar.getTime();
            calendar.add(Calendar.DATE, 3);
            Date endDate = calendar.getTime();

            // Create task object
            AcmTask task = new AcmTask();
            task.setAdhocTask(true);
            task.setTitle(taskName);
            task.setOwner(author);
            task.setAssignee(reviewer);
            task.setStatus("Active");
            task.setTaskStartDate(startDate);
            task.setDueDate(endDate);
            task.setPriority("Medium");
            task.setAttachedToObjectId(acmApplicationTaskEvent.getParentObjectId());
            task.setAttachedToObjectName(acmApplicationTaskEvent.getParentObjectName());
            task.setAttachedToObjectType(acmApplicationTaskEvent.getParentObjectType());

            try
            {
                // Create the task
                getTaskDao().createAdHocTask(task);
            }
            catch (AcmTaskException e)
            {
                log.error("Cannot create task [{}]", taskName, e);
            }
        }
    }

    /**
     * Take reviewers
     *
     * @param acmApplicationTaskEvent
     * @return
     */
    private List<String> findReviewers(AcmApplicationTaskEvent acmApplicationTaskEvent) {
        List<String> reviewers = new ArrayList<>();
        getDefaultApprovers().forEach(approver -> {
            reviewers.add(approver);
        });

        return reviewers;
    }

    /**
     * Check if the event is approval event for submission
     *
     * @param businessProcessId - process id
     * @return - true/false
     */
    private boolean isSubmissionApproval(String businessProcessId)
    {
        // Return false if the process id is not provided
        if (businessProcessId == null)
        {
            return false;
        }

        try
        {
            // Try to find variable in the process. If the process not already closed, this will throw exception
            return getActivitiRuntimeService().hasVariable(businessProcessId, GmsConstants.GMS_APPROVAL_COUNT);
        }
        catch (Exception e)
        {
            log.warn("Cannot check if it's submission approval. ProcessID: [{}]. Try to find in history.", businessProcessId);
        }

        try
        {
            // Try to find the variable in the history of the process
            List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().processInstanceId(businessProcessId).includeProcessVariables().orderByProcessInstanceEndTime().desc().listPage(0, 1);
            if (list != null && list.size() == 1)
            {
                Map<String, Object> vars = list.get(0).getProcessVariables();
                if (vars != null && vars.containsKey(GmsConstants.GMS_APPROVAL_COUNT))
                {
                    return true;
                }
            }
        }
        catch (Exception e)
        {
            log.warn("Cannot check if it's submission approval in tasks history. ProcessID: [{}]", businessProcessId);
        }

        return false;
    }

    /**
     * Take outcome of the task
     *
     * @param businessProcessId
     * @param outcomeName
     * @return
     */
    private String getOutcome(String businessProcessId, String outcomeName)
    {
        try
        {
            // Try to find in the process
            return (String) getActivitiRuntimeService().getVariable(businessProcessId, outcomeName);
        }
        catch (Exception e)
        {
            log.warn("Cannot find outcome. ProcessID: [{}], Outcome Name: [{}]. Try to find in history.", businessProcessId, outcomeName);
        }

        try
        {
            // Try to find in the history of the process if the process is not active
            List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().processInstanceId(businessProcessId).includeProcessVariables().orderByProcessInstanceEndTime().desc().listPage(0, 1);
            if (list != null && list.size() == 1)
            {
                Map<String, Object> vars = list.get(0).getProcessVariables();
                if (vars != null && vars.containsKey(outcomeName))
                {
                    return (String) vars.get(outcomeName);
                }
            }
        }
        catch (Exception e)
        {
            log.error("Cannot find outcome in tasks history. ProcessID: [{}], Outcome Name: [{}].", businessProcessId, outcomeName, e);
        }

        return null;
    }

    /**
     * Take approval count
     *
     * @param businessProcessId
     * @return
     */
    private Integer getApprovalCount(String businessProcessId)
    {
        try
        {
            // Try to find in the process
            return (Integer) getActivitiRuntimeService().getVariable(businessProcessId, GmsConstants.GMS_APPROVAL_COUNT);
        }
        catch (Exception e)
        {
            log.warn("Cannot find approval count. ProcessID: [{}]. Try to find in history.", businessProcessId);
        }

        try
        {
            // Try to find in the process history if the process is not active
            List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().processInstanceId(businessProcessId).includeProcessVariables().orderByProcessInstanceEndTime().desc().listPage(0, 1);
            if (list != null && list.size() == 1)
            {
                Map<String, Object> vars = list.get(0).getProcessVariables();
                if (vars != null && vars.containsKey(GmsConstants.GMS_APPROVAL_COUNT))
                {
                    return (Integer) vars.get(GmsConstants.GMS_APPROVAL_COUNT);
                }
            }
        }
        catch (Exception e)
        {
            log.warn("Cannot find approval count in tasks history. ProcessID: [{}].", businessProcessId);
        }

        return 0;
    }

    /**
     * Set approval count in the process variable
     *
     * @param businessProcessId
     * @param approvalCount
     */
    private void setApprovalCount(String businessProcessId, Integer approvalCount)
    {
        try
        {
            getActivitiRuntimeService().setVariable(businessProcessId, GmsConstants.GMS_APPROVAL_COUNT, approvalCount);
        }
        catch (Exception e)
        {
            log.error("The process with ProcessID [{}] is already finished or not exist.", businessProcessId, e);
        }
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

    public HistoryService getHistoryService()
    {
        return historyService;
    }

    public void setHistoryService(HistoryService historyService)
    {
        this.historyService = historyService;
    }

    public TaskDao getTaskDao()
    {
        return taskDao;
    }

    public void setTaskDao(TaskDao taskDao)
    {
        this.taskDao = taskDao;
    }
}
