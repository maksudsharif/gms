package com.arkcase.gms.pipeline.postsave;

import com.arkcase.gms.model.GmsConstants;
import com.arkcase.gms.model.Grant;
import com.armedia.acm.plugins.casefile.model.CaseFile;
import com.armedia.acm.plugins.casefile.pipeline.CaseFilePipelineContext;
import com.armedia.acm.plugins.casefile.pipeline.postsave.CaseFileFolderStructureHandler;
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


public class AwardFolderStructureHandler extends CaseFileFolderStructureHandler
{
    @Override
    public void execute(CaseFile entity, CaseFilePipelineContext pipelineContext) throws PipelineProcessException
    {
        if (entity != null && entity instanceof Grant)
        {
            Grant grant = (Grant) entity;
            if (GmsConstants.TYPE_AWARDED_GRANT.equalsIgnoreCase(grant.getGrantType()))
            {
                super.execute(entity, pipelineContext);
            }
        }
    }
}
