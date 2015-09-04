/**
 * This class is used to show the list of all the rejected files with
 * their comments to the user.
 */
package com.ibm.km.actions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.ibm.km.common.KmDisplayFile;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.forms.KmApproveFileFormBean;
import com.ibm.km.forms.KmRejectedFileFormBean;
import com.ibm.km.services.RejectedFileService;
import com.ibm.km.services.impl.RejectedFileServiceImpl;


/**
 * @version 	1.0
 * @author Vipin 13/02/08
 */
public class KmRejectedFileAction extends DispatchAction {
	
	Logger logger = Logger.getLogger(KmRejectedFileAction.class);
	
	/**
	 * Method to initialize view rejected file screen.
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
				/*			added by harpreet the default date in KM Phase 2   */ 
				KmRejectedFileFormBean formBean = (KmRejectedFileFormBean)form;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
				String date = sdf.format(new java.util.Date());
				date = date.substring(0,10);
				formBean.setFromDate(date);
				formBean.setToDate(date);
				return mapping.findForward("fileList");
			}
			
	/**
	 * Method to get list of all rejected files.
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
		ActionMessages messages = new ActionMessages();
		ActionForward forward = new ActionForward(); // return value
		RejectedFileService service = new RejectedFileServiceImpl();
		KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
		KmRejectedFileFormBean formBean = (KmRejectedFileFormBean)form;
		
		ArrayList fileList;
		
		logger.info("User " + userBean.getUserLoginId() +"entered getFileList method ");
		
		try {
			
			String actorId=userBean.getKmActorId();
			fileList = service.getFileList(userBean.getUserId(),formBean.getFromDate(),formBean.getToDate(),userBean.getElementId(),actorId);
			
			formBean.setFileList(fileList);
		
			if(fileList.size()==0)
			{
				messages.add("msg1",new ActionMessage("km.no.file"));
				saveMessages(request,messages);
			//	errors.add("name",new ActionError("km.no.file"));
			}
					
			forward = mapping.findForward("fileList");

		} catch (Exception e) {
			
			logger.error("Error occured in getFileList method " + e.getMessage());
			// Report the error using the appropriate name and ID.
			errors.add("name", new ActionError("id"));

		}

		// If a message is required, save the specified key(s)
		// into the request for use by the <struts:errors> tag.

		if (!errors.isEmpty()) {
			saveErrors(request, errors);

			// Forward control to the appropriate 'failure' URI (change name as desired)
			//	forward = mapping.findForward("failure");

		} else {

			// Forward control to the appropriate 'success' URI (change name as desired)
			// forward = mapping.findForward("success");

		}

		// Finish with
		logger.info("User " + userBean.getUserLoginId() +"exits getFileList method ");
		return (forward);
	}
	
	public ActionForward displayFile(
				ActionMapping mapping,
				ActionForm form,
				HttpServletRequest request,
				HttpServletResponse response)
				throws Exception {
				
		KmRejectedFileFormBean formBean = (KmRejectedFileFormBean)form;
		KmDisplayFile fileDisplay = new KmDisplayFile();
		ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
		ActionErrors errors = new ActionErrors();
		
		StringBuffer displayFilePath  = new StringBuffer(bundle.getString("folder.path"));
		displayFilePath.append(formBean.getDisplayFilePath());
		
		StringBuffer tempFilePath = new StringBuffer(request.getSession().getServletContext().getRealPath("/Docs/"));
		
		String returnPath = null;
		
		try
		{
			returnPath = fileDisplay.displayFile(displayFilePath.toString(),tempFilePath.toString());
			formBean.setShowFile("true");
			
			if(returnPath == null)
			{
				logger.error("No File Found in displayFile Method");
				errors.add("name",new ActionError("km.file.exception"));
				saveErrors(request,errors);
				formBean.setShowFile("false");
				logger.info("Restuen path is null");
			}
		}
		catch(Exception e)
		{
			logger.error("IOException occured in displayFile " + e.getMessage());
			errors.add("name",new ActionError("km.file.exception"));
			saveErrors(request,errors);
		}
		
		
		
		
//		String returnPath = fileDisplay.displayFile(displayFilePath.toString(),tempFilePath.toString());
//						
//		formBean.setShowFile("true");
		formBean.setFilePath(returnPath);
					
		return mapping.findForward("fileList");
	}
}
