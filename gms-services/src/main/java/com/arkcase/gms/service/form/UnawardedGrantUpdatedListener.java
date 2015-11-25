/**
 * 
 */
package com.arkcase.gms.service.form;

import com.arkcase.gms.model.GmsConstants;
import com.arkcase.gms.model.UnawardedGrantForm;
import com.armedia.acm.form.ebrief.model.EbriefForm;
import com.armedia.acm.form.ebrief.service.EbriefService;
import com.armedia.acm.frevvo.config.FrevvoFormName;
import com.armedia.acm.plugins.casefile.model.CaseEvent;
import com.armedia.acm.plugins.casefile.model.CaseFileConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;

import java.util.Properties;

/**
 * @author riste.tutureski
 *
 */
public class UnawardedGrantUpdatedListener implements ApplicationListener<CaseEvent> {

	private final Logger LOG = LoggerFactory.getLogger(getClass());
	
	private Properties properties;
	private UnawardedGrantFormService unawardedGrantFormService;
	
	@Override
	public void onApplicationEvent(CaseEvent event) {
		if ("com.armedia.acm.casefile.event.updated".equals(event.getEventType().toLowerCase()))
		{
			LOG.debug("Updating Frevvo XML file ...");
			
			if (getProperties() != null)
			{
				if (getProperties().containsKey(CaseFileConstants.ACTIVE_CASE_FORM_KEY))
				{
					String activeFormName = (String) getProperties().get(CaseFileConstants.ACTIVE_CASE_FORM_KEY);
					
					if (GmsConstants.FORM_NAME_UNAWARDED_GRANT.equals(activeFormName))
					{
						getUnawardedGrantFormService().updateXML(event.getCaseFile(), event.getEventUser(), UnawardedGrantForm.class);
					}
				}
			}
		}
	}

	
	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public UnawardedGrantFormService getUnawardedGrantFormService() {
		return unawardedGrantFormService;
	}

	public void setUnawardedGrantFormService(UnawardedGrantFormService unawardedGrantFormService) {
		this.unawardedGrantFormService = unawardedGrantFormService;
	}
}
