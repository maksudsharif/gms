package com.arkcase.gms.pipeline.postsave;

import com.arkcase.gms.model.GmsConstants;
import com.arkcase.gms.model.Grant;
import com.armedia.acm.plugins.casefile.model.CaseFile;
import com.armedia.acm.plugins.casefile.pipeline.CaseFilePipelineContext;
import com.armedia.acm.plugins.casefile.utility.CaseFileEventUtility;
import com.armedia.acm.plugins.ecm.model.AcmContainer;
import com.armedia.acm.plugins.ecm.service.AcmFolderService;
import com.armedia.acm.plugins.ecm.service.EcmFileService;
import com.armedia.acm.services.pipeline.exception.PipelineProcessException;
import com.armedia.acm.services.pipeline.handler.PipelineHandler;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;

import java.util.Date;


public class CaseFileFolderStructureHandler extends com.armedia.acm.plugins.casefile.pipeline.postsave.CaseFileFolderStructureHandler
{
    /**
     * Case File event utility.
     */
    private CaseFileEventUtility caseFileEventUtility;

    /**
     * Case File folder structure.
     */
    private String folderStructureAsString;

    /**
     * CMIS service.
     */
    private EcmFileService ecmFileService;

    /**
     * ACM folder service.
     */
    private AcmFolderService acmFolderService;

    /**
     * Logger instance.
     */
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void execute(CaseFile entity, CaseFilePipelineContext pipelineContext) throws PipelineProcessException
    {
        if (entity != null && entity instanceof Grant)
        {
            Grant grant = (Grant) entity;
            if (GmsConstants.TYPE_AWARDED_GRANT.equalsIgnoreCase(grant.getGrantType()))
            {
                Authentication auth = pipelineContext.getAuthentication();

                if (pipelineContext.isNewCase())
                {
                    createFolderStructure(entity);
                    caseFileEventUtility.raiseEvent(entity, entity.getStatus(), new Date(), pipelineContext.getIpAddress(), auth.getName(), auth);
                } else
                {
                    caseFileEventUtility.raiseEvent(entity, "updated", new Date(), pipelineContext.getIpAddress(), auth.getName(), auth);
                }
            }
        }
    }


    private void createFolderStructure(CaseFile caseFile)
    {
        if (folderStructureAsString != null && !folderStructureAsString.isEmpty())
        {
            try
            {
                log.debug("Folder Structure [{}]", folderStructureAsString);
                JSONArray folderStructure = new JSONArray(folderStructureAsString);
                AcmContainer container = ecmFileService.getOrCreateContainer(caseFile.getObjectType(), caseFile.getId());
                acmFolderService.addFolderStructure(container, container.getFolder(), folderStructure);
            } catch (Exception e)
            {
                log.error("Cannot create folder structure.", e);
            }
        }
    }

    public CaseFileEventUtility getCaseFileEventUtility()
    {
        return caseFileEventUtility;
    }

    public void setCaseFileEventUtility(CaseFileEventUtility caseFileEventUtility)
    {
        this.caseFileEventUtility = caseFileEventUtility;
    }

    public String getFolderStructureAsString()
    {
        return folderStructureAsString;
    }

    public void setFolderStructureAsString(String folderStructureAsString)
    {
        this.folderStructureAsString = folderStructureAsString;
    }

    public EcmFileService getEcmFileService()
    {
        return ecmFileService;
    }

    public void setEcmFileService(EcmFileService ecmFileService)
    {
        this.ecmFileService = ecmFileService;
    }

    public AcmFolderService getAcmFolderService()
    {
        return acmFolderService;
    }

    public void setAcmFolderService(AcmFolderService acmFolderService)
    {
        this.acmFolderService = acmFolderService;
    }
}
