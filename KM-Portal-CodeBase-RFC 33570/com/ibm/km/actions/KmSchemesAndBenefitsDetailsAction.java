package com.ibm.km.actions;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ibm.km.dao.KmSchemesAndBenefitsDao;
import com.ibm.km.dao.impl.KmSchemesAndBenefitsDaoImpl;
import com.ibm.km.dto.KmSchemesAndBenefitsDto;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.forms.KmSchemesAndBenefitsDetailsFormBean;

public class KmSchemesAndBenefitsDetailsAction extends Action {
/**
	 * This function returns the details to be shown in the grid
	 * 
	 * @param mapping
	 *            defines a path that is matched against the request URI of the
	 *            incoming request and usually specifies the fully qualified
	 *            class name of an Action class.
	 * @param form
	 *            class makes it easy to store and validate the data for your
	 *            application's input forms.
	 * @param request
	 *            This interface is for getting data from the client to service
	 *            the request
	 * @param response
	 *            Interface for sending MIME data to the client.
	 * 
	 * @return Forward
	 * @throws Exception
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
	        HttpServletRequest request, HttpServletResponse response)
	        throws Exception {
			
			ActionErrors errors = new ActionErrors();
			ActionForward forward = new ActionForward();
			
			try {
				KmUserMstr userBean = (KmUserMstr) request.getSession().getAttribute("USER_INFO");
				
				KmSchemesAndBenefitsDetailsFormBean formBean = (KmSchemesAndBenefitsDetailsFormBean) form;
				KmSchemesAndBenefitsDto dto = new KmSchemesAndBenefitsDto();
				KmSchemesAndBenefitsDao dao = new KmSchemesAndBenefitsDaoImpl();
				
				BeanUtils.copyProperties(formBean , dto);
				
				List recordList = dao.getDetails(dto);
				formBean.setRecordList(recordList);
			}
			catch (Exception exp) {
				errors.add("getDetails", new ActionError(""));
				saveErrors(request, errors);
				return mapping.findForward("FAILURE");
			}

			forward = mapping.findForward("SUCCESS");
			return (forward);
		}
}