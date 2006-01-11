/*
 * Created on 13/02/08
 * 
 * This class is used to initialize approve file page for the Circle Admins.
 * It also fetched the list of file to approve for the selected date range by the user.
 * Finally it approves or rejects the selected files along with comment as per user input.
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
import com.ibm.km.forms.KmApproveFileFormBean;
import com.ibm.km.services.ApproveFileService;
import com.ibm.km.services.KmUserMstrService;
import com.ibm.km.services.impl.ApproveFileServiceImpl;
import com.ibm.km.services.impl.KmUserMstrServiceImpl;


/**
 * @version 	1.0
 * @author Vipin 13/02/08
 */
public class KmApproveFilesAction extends DispatchAction {

	Logger logger = Logger.getLogger(KmApproveFilesAction.class);
	
	/**
	 * This method is used to initialize the Approve file screen.
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
				
				KmApproveFileFormBean formBean = (KmApproveFileFormBean)form;
				formBean.setStatus("init");
				return mapping.findForward("fileList");
				
			}
	
	/**
	 * This method returns the list of files to approve in the selected date range 
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
		ActionMessages messages = new ActionMessages();
		ActionForward forward = new ActionForward(); // return value
		ApproveFileService service = new ApproveFileServiceImpl();
		KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
		KmApproveFileFormBean formBean = (KmApproveFileFormBean)form;
		
		logger.info(userBean.getUserLoginId() + " entered getFileList method");
		ArrayList fileList;
		String []circleUsers;
		try {
			
			KmUserMstrService userService = new KmUserMstrServiceImpl();
			
			fileList = service.getFileList(userBean.getUserId());
			formBean.setFileList(fileList);
			
			if(fileList.size()==0)
			{
				logger.error("No file found for approving.Response send back to user.");
				messages.add("msg1",new ActionMessage("km.no.file"));
			    saveMessages(request,messages);
			//	errors.add("name",new ActionError("km.no.file"));
			}
		//	request.getSession().setAttribute("fileList",fileList);
		//	request.setAttribute("fileList",fileList);
			forward = mapping.findForward("fileList");

		} catch (Exception e) {

			// Report the error using the appropriate name and ID.
			errors.add("name", new ActionError("id"));
			logger.error("Exception occured in getFileList method " + e.getMessage());

		}

		// If a message is required, save the specified key(s)
		// into the request for use by the <struts:errors> tag.

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		} 
		
		// Finish with
		logger.info(userBean.getUserLoginId() + " exited getFileList method");
		return (forward);
	}
	
	public ActionForward approveFiles(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
				
				ActionErrors errors = new ActionErrors();
				ActionMessages messages = new ActionMessages();
				KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
				KmApproveFileFormBean formBean = (KmApproveFileFormBean)form;
				String fileIds[] = formBean.getApproveFileList();
				String commentList[] = formBean.getCommentList();
				String comment[] = new String[fileIds.length];
				ArrayList fileList = new ArrayList();
				int j = 0;
				String userId = userBean.getUserId();
				ApproveFileService service = new ApproveFileServiceImpl();
				
				logger.info(userBean.getUserLoginId() + " entered approveFiles method");
							
				try
				{
				//Bug resolved MASDB00064820	
				for(int i=0;i<fileIds.length;i++)
				{
					if(commentList[i]==null){
						commentList[i]="";
					}
					if(("").equals(commentList[i]))
					{	continue; } 
					comment[j] = commentList[i];
					j++;
					
				}
				
							
				int i = 0;

				service.approveFiles(fileIds,comment,userId);
				String []circleUsers;
				KmUserMstrService userService = new KmUserMstrServiceImpl();
				circleUsers=userService.getElementUsers(userBean.getElementId());
				fileList = service.getFileList(userBean.getUserId());
				formBean.setFileList(fileList);
			//	service.approveFiles(fileList);
			    messages.add("msg2",new ActionMessage("km.file.approved"));
			//	errors.add("name",new ActionError("km.file.approved"));
				logger.info("Files approved successfully.Response send back to user.");
				}
				catch(KmException e)
				{	
					logger.error("Exception occured approveFiles method " + e.getMessage());
					errors.add("name",new ActionError("km.file.approval.error"));
					
				}
				saveMessages(request,messages);
				saveErrors(request,errors);
				logger.info(userBean.getUserLoginId() + " exited approveFiles method");
				return mapping.findForward("fileList");
			}
		
		
	public ActionForward rejectFiles(
				ActionMapping mapping,
				ActionForm form,
				HttpServletRequest request,
				HttpServletResponse response)
				throws Exception {
				
					ActionErrors errors = new ActionErrors();
					ActionMessages messages = new ActionMessages();
					KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
					KmApproveFileFormBean formBean = (KmApproveFileFormBean)form;
					String fileIds[] = formBean.getApproveFileList();
					String commentList[] = formBean.getCommentList();
					String comment[] = new String[fileIds.length];
					ArrayList fileList = new ArrayList();
					String userId = userBean.getUserId();
					ApproveFileService service = new ApproveFileServiceImpl();
					int j = 0;
					
					try
					{
					logger.info(userBean.getUserLoginId() + " entered rejectFiles method");
				
					for(int i=0;i<fileIds.length;i++)
					{
						if(commentList[i].equals(""))
							continue;
						comment[j] = commentList[i];
						j++;
					}
					
					int i = 0;
	
					service.rejectFiles(fileIds,comment,userId);
					String []circleUsers;
					KmUserMstrService userService = new KmUserMstrServiceImpl();
					circleUsers=userService.getElementUsers(userBean.getElementId());
					fileList = service.getFileList(userBean.getUserId());
					formBean.setFileList(fileList);
		
	
					messages.add("msg3",new ActionMessage("km.file.rejected"));
				//	errors.add("name",new ActionError("km.file.rejected"));
				
					}
					catch(KmException e)
					{
						logger.error("Exception occured rejectFiles method " + e.getMessage());
						errors.add("name",new ActionError("km.file.approval.error"));
					}
					saveMessages(request,messages);
					saveErrors(request,errors);
					logger.info(userBean.getUserLoginId() + " exited rejectFiles method");
					return mapping.findForward("fileList");
				}	
}