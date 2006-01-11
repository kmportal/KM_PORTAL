/*
 * This class is used to list the files which are in submitted state 
 * and needs to be put in approval pending state.
 * it also submit files for approval.
 */
package com.ibm.km.actions;

import java.util.ArrayList;

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

import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.KmAddFileFormBean;
import com.ibm.km.forms.KmSubmitFileFormBean;
import com.ibm.km.services.SubmitFileService;
import com.ibm.km.services.impl.SubmitFileServiceImpl;

/**
 * @version 	1.0
 * @author Vipin 13/02/08
 */
public class SubmitFileForApprovalAction extends DispatchAction {
	
	Logger logger = Logger.getLogger(SubmitFileForApprovalAction.class);
	
	/**
	 * Method to initialize Submit file for Approval screen.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward init(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
			
		return mapping.findForward("initialize");	
	}
	
	/**
		 * This method returns the list of files to submit for approval in the selected date range 
		 * of the logged in user.
		 * by the user.
		 * @param mapping
		 * @param form
		 * @param request
		 * @param response
		 * @return
		 * @throws Exception
		 */
	public ActionForward getFileList(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		ActionErrors errors = new ActionErrors();
		ActionMessages messages= new ActionMessages();
		ActionForward forward = new ActionForward(); // return value
		SubmitFileService service = new SubmitFileServiceImpl();
		KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
		KmSubmitFileFormBean formBean = (KmSubmitFileFormBean)form;
		ArrayList fileList;
		
		logger.info(userBean.getUserLoginId() + " entered getFileList method");
		
		try {
			
			fileList = service.getFileList(userBean.getUserId(),formBean.getFromDate(),formBean.getToDate());
			
			if(fileList.size() == 0)
			{	
				logger.error("No file found for submission.Response send back to user.");
				messages.add("msg1",new ActionMessage("km.no.file"));
				saveMessages(request,messages);
				//errors.add("name",new ActionError("km.no.file"));
			}
			formBean.setFileList(fileList);
			
			request.setAttribute("fileList",fileList);
			

		} catch (Exception e) {

			// Report the error using the appropriate name and ID.
			
			errors.add("name", new ActionError("id"));
			logger.error("Exception occured in getFileList method " + e.getMessage());

		}

		// If a message is required, save the specified key(s)
		// into the request for use by the <struts:errors> tag.

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			// Forward control to the appropriate 'failure' URI (change name as desired)
			//	forward = mapping.findForward("failure");
		} 

		// Finish with
		logger.info(userBean.getUserLoginId() + " exited getFileList method");
		return mapping.findForward("fileList");
	}
	
	public ActionForward submitFiles(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
				
				ActionErrors errors = new ActionErrors();
				ActionMessages messages= new ActionMessages();
				KmSubmitFileFormBean formBean = (KmSubmitFileFormBean)form;
				String fileIds[] = formBean.getSubmitFileList();
				SubmitFileService service = new SubmitFileServiceImpl();
				KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
				
				logger.info(userBean.getUserLoginId() + " entered submitFiles method");
				
				try
				{
			
				service.submitFile(fileIds);
			    messages.add("msg2",new ActionMessage("km.file.submitted"));
			//	errors.add("name",new ActionError("km.file.submitted"));
				logger.info("Files submitted successfully.Response send back to user.");
				}
				catch(KmException e)
				{
					logger.error("Exception occured submitFiles method " + e.getMessage());
					errors.add("name",new ActionError("km.file.submition.error"));
				
				}
				saveMessages(request,messages);
				saveErrors(request,errors);
				logger.info(userBean.getUserLoginId() + " exited submitFiles method");
				return mapping.findForward("fileList");
			}
}
