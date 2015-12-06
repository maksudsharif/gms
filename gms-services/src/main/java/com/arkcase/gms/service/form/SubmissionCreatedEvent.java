package com.arkcase.gms.service.form;

import com.armedia.acm.frevvo.model.FrevvoUploadedFiles;
import com.armedia.acm.plugins.complaint.model.Complaint;
import com.armedia.acm.plugins.complaint.model.ComplaintCreatedEvent;

/**
 * Created by maksud.sharif on 12/2/2015.
 */
public class SubmissionCreatedEvent extends ComplaintCreatedEvent {

    private static final String EVENT_TYPE = "com.armedia.gms.submission.created";
    private FrevvoUploadedFiles frevvoUploadedFiles;

    public SubmissionCreatedEvent(Complaint source, FrevvoUploadedFiles files, String ipAddress, boolean succeeded) {
        super(source);

        setEventType(EVENT_TYPE);
        setComplaintNumber(source.getComplaintNumber());
        setIpAddress(ipAddress);
        setObjectId(source.getId());

        setSucceeded(succeeded);

        setFrevvoUploadedFiles(files);
    }

    @Override
    public String getEventType()
    {
        return EVENT_TYPE;
    }

    public FrevvoUploadedFiles getFrevvoUploadedFiles() {
        return frevvoUploadedFiles;
    }

    public void setFrevvoUploadedFiles(FrevvoUploadedFiles frevvoUploadedFiles) {
        this.frevvoUploadedFiles = frevvoUploadedFiles;
    }
}
