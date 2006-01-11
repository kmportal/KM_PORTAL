/*
 * Created on May 19, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;

import com.ibm.km.dto.EmployeeAppreciationDTO;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.KmEmployeeAppreciationFormBean;
import com.ibm.km.services.KmEmployeeAppreciationService;
import com.ibm.km.services.impl.KmEmployeeAppreciationServiceImpl;

/**
 * @author Kundan Kumar
 * @since 9th Oct, 2012
 */
public class KmEmployeeAppreciationAction extends DispatchAction {
	private static final Logger logger;
	static {
		logger = Logger.getLogger(KmEmployeeAppreciationAction.class);
	}

	public ActionForward init(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		 		saveToken(request);
				ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
				KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
				KmEmployeeAppreciationFormBean formBean = (KmEmployeeAppreciationFormBean)form;
				logger.info(userBean.getUserLoginId() + " entered init method for Create Appreciation page.");
				formBean.reset(mapping,request);
				List<EmployeeAppreciationDTO> employeeAppreciationList = new ArrayList<EmployeeAppreciationDTO>();
				
				for( int i=0; i<50; i++ )
				{
					employeeAppreciationList.add(new EmployeeAppreciationDTO());
				}
				formBean.setEmployeeAppreciationList(employeeAppreciationList);
				
				
				try {				
					formBean.setCreatedBy(userBean.getElementId());
				} catch (Exception e) {
					logger.error("Exception in Loading page for Init Create ELement: "+e.getMessage());
				}
				logger.info(userBean.getUserLoginId() + " exited init method for Create Appreciation page.");
				return mapping.findForward("initialize");
				
		}
	public ActionForward insert(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
			throws Exception { 
		if ( !isTokenValid(request) ) {
			return init(mapping, form, request, response);
	    }
		KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
		ActionErrors errors = new ActionErrors();
		ActionForward forward = new ActionForward(); // return value
		ActionMessages messages = new ActionMessages();
		KmEmployeeAppreciationFormBean kmEmployeeAppreciationForm = (KmEmployeeAppreciationFormBean)form;
		kmEmployeeAppreciationForm.setCreatedBy(userBean.getUserId());		
		KmEmployeeAppreciationService kmEmpAppreciationService = new KmEmployeeAppreciationServiceImpl();		
		logger.info(userBean.getUserLoginId() + " intered into insert method for Create Appreciation page."); 
		try {
			    String msg = kmEmpAppreciationService.insertAppreciation(kmEmployeeAppreciationForm);
				messages.add("msg1",new ActionMessage("appreciation.created"));
				logger.info(msg);
				saveMessages(request, messages);
				resetToken(request);
		} catch (Exception e) {
		  e.printStackTrace();
			logger.error("Exception occured while creating Appreciation :" + e);	
			errors.add("error",new ActionError("appreciation.not.created"));
			saveErrors(request, errors);
		}
		
		return init(mapping, form, request, response);
	}
	
	public ActionForward viewAppreciation(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
				KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
			//	KmEmployeeAppreciationFormBean formBean = (KmEmployeeAppreciationFormBean)form;
				logger.info(userBean.getUserLoginId() + " entered viewElements method");
				KmEmployeeAppreciationService employeeAppreciationService=new KmEmployeeAppreciationServiceImpl();
				try {
					List<EmployeeAppreciationDTO> employeeAppreciationList=null;
					employeeAppreciationList=employeeAppreciationService.getEmployeeAppreciationList();
					request.setAttribute("EMP_APPRECIATION_LIST",employeeAppreciationList);
				} catch (KmException e) {
					logger.error("Exception in listing employee appreciation, "+e.getMessage());
				}
				logger.info(userBean.getUserLoginId() + " exited elementListing ");
				
				if(userBean.isCsr())
				{
					return mapping.findForward("viewEmployeeAppreciationCsr");	
				}
				return mapping.findForward("viewEditEmployeeAppreciation");
		}					
}
