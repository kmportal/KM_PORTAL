package com.ibm.km.actions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import com.ibm.km.common.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;

import com.ibm.km.common.KmDisplayFile;
import com.ibm.km.dto.DocumentPath;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.dto.KmWhatsNew;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.KmWhatsNewFormBean;
import com.ibm.km.services.KmElementMstrService;
import com.ibm.km.services.KmUserMstrService;
import com.ibm.km.services.KmWhatsNewService;
import com.ibm.km.services.impl.KmElementMstrServiceImpl;
import com.ibm.km.services.impl.KmUserMstrServiceImpl;
import com.ibm.km.services.impl.KmWhatsNewServiceImpl;

/**
 * @version 	1.0
 * @author		Anil
 */
public class KmWhatsNewAction extends DispatchAction {
	/* Logger for the class */
	
	Logger logger = Logger.getLogger(KmWhatsNewAction.class);
	
	String docDesc="";
	public ActionForward init(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		ActionErrors errors = new ActionErrors();
		ActionForward forward = new ActionForward(); // return value
		KmWhatsNewFormBean kmWhatsNewFormBean = (KmWhatsNewFormBean) form;
		KmElementMstrService elementService=new KmElementMstrServiceImpl();
		KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
		
		kmWhatsNewFormBean.setCategoryList(elementService.getChildren(userBean.getElementId()));
		kmWhatsNewFormBean.setCategoryId("");
		logger.info(userBean.getUserLoginId() + " entered initAddFile method");
		String[] circleId = {userBean.getElementId()};
		
		return mapping.findForward("initWhatsNew");
	}
	/**
	 *  This method is used to add the file physically in the workspace as well as
	 *  the link of the file in the database.It also checks if the same file in submitted state 
	 *  already exists in the database and if yes, then sends a updation confirmation message back to 
	 *  user. 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */	
	public ActionForward insertFile(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response)throws Exception
	{
		KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
		KmWhatsNewFormBean formBean = (KmWhatsNewFormBean) form;
		HttpSession session = request.getSession();
		FormFile file = formBean.getNewFile();	
		ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
		ActionForward forward=mapping.findForward("initialize");
	
		logger.info(userBean.getUserLoginId() + " entered insertFile method of KmWhatsNewAction");
		session.setAttribute("file",file);
		ActionErrors errors = new ActionErrors();
		ActionMessages messages =new ActionMessages();
		KmWhatsNewService service = new KmWhatsNewServiceImpl(); 
		KmWhatsNew dto = new KmWhatsNew();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH-mm-ss");
		String date = sdf.format(new java.util.Date());


		try
		{
			
		//Getting the document from  file form
			
			String fileName = file.getFileName();
			String extn = fileName.substring(fileName.indexOf(".")+1);
			fileName = fileName.substring(0,fileName.indexOf("."));
			fileName = fileName + "_" + date + "." + extn;  
			
			DocumentPath pathObject = new DocumentPath();
			if(formBean.getCategoryId().equals(""))	{
			
				pathObject.setCircleId(userBean.getElementId());
			}else{
				pathObject.setCircleId(formBean.getCategoryId());
			}
			pathObject.setDocumentName(fileName);		
			if (file.getFileSize() == 0)
			{
				logger.error("Empty file found.Response send back to user.");
				errors.add("",new ActionError("file.empty"));
			}
			else if (file.getFileSize() > (5120*1024))
			{
						logger.error("File size exceeds 5MB.Response send back to user.");
						errors.add("",new ActionError("km.file.size.exceeds"));
						
			}
			else
			{
				
				//Creating the folder for uploading the whats new document
				String newFolderPath =bundle.getString("whatsnewfolder.path");
				newFolderPath=newFolderPath+pathObject.getCircleId();
				logger.info("New Folder Path : "+newFolderPath);
				String directory = newFolderPath;
				File f = new File(directory);
				f.mkdirs();  
				
				String compfileName = service.saveFile(pathObject,file);

				// Adding file details in the database
				if(("").equals(compfileName))
				{
				//Setting dto	
					
					dto.setCircleId(pathObject.getCircleId());
					
					logger.info("ELEMENT ID :: "+dto.getCircleId());
					dto.setDocumentName(fileName);
					dto.setDocumentDisplayName(file.getFileName());
					dto.setDocumentDesc(formBean.getDocumentDesc());
					
					dto.setUploadedBy(userBean.getUserId());
					service.saveFileInfoInDB(dto);
				

					messages.add("msg1",new ActionMessage("whatsNew.added"));
				
					logger.info("File added successfully");
					formBean.setDocumentDesc("");
				}
				else
				{
					//Duplicate document
					formBean.setStatus("DUPLICATE");
					formBean.setCompleteFileName(compfileName);
					
					logger.error("Duplicate file found.Response send back to user.");
					messages.add("msg2",new ActionMessage("whatsNew.duplicate"));
					docDesc=formBean.getDocumentDesc();
					formBean.setDocumentDesc("");
					
				//	errors.add("",new ActionError("file.exists"));
				}
			
			}
			
			
	
		}
		catch(KmException io){
			logger.error("Exception occured adding Whats New Document " + io.getMessage());
			
			formBean.setDocumentDesc("");
			
			errors.add("",new ActionError("file.ioexception"));
		//	forward=initAddFile(mapping,form,request,response);
		}
		
		saveMessages(request,messages);
		saveErrors(request,errors);
		logger.info(userBean.getUserLoginId() + " existed insertFile method");
		formBean.setCategoryId("");
		return mapping.findForward("createWhatsNew");
				
	}	
	/**
	 *  This method is used to update a file if the user wants to add the same 
	 *  file which already exists in submitted state in the DB.It updates the files
	 *  both in the DB and in the workspace.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updateFile(ActionMapping mapping,ActionForm form,
				HttpServletRequest request,HttpServletResponse response)throws Exception
		{
			
			KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
			String[] circleId = {userBean.getElementId()};	
			KmWhatsNewFormBean formBean = (KmWhatsNewFormBean)form;
		//	FormFile file = formBean.getNewFile();
			
			logger.info(userBean.getUserLoginId() + " entered updateFile method");
			
			FormFile file = (FormFile)request.getSession().getAttribute("file");
			
			ActionErrors errors = new ActionErrors();
			ActionMessages messages =new ActionMessages();
			KmWhatsNewService service = new KmWhatsNewServiceImpl(); 
			KmWhatsNew dto = new KmWhatsNew();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH-mm-ss");
			String date = sdf.format(new java.util.Date());
			String oldFile = formBean.getCompleteFileName();
			int userId = Integer.parseInt(userBean.getUserId());
			ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
			
			try
			{
				String fileName = file.getFileName();
				String extn = fileName.substring(fileName.indexOf(".")+1);
				fileName = fileName.substring(0,fileName.indexOf("."));
				fileName = fileName + "_" + date + "." + extn;  
			
			//	String newPath = request.getSession().getServletContext().getRealPath("/Docs/" + userBean.getCircleId() +"/"+formBean.getCategoryId() + "/" + formBean.getSubCategoryId() + "/" + fileName);
				String newPath = bundle.getString("whatsnewfolder.path") +formBean.getCircleId() + "/" + fileName;
				
				//	String oldPath = request.getSession().getServletContext().getRealPath("/Docs/" + userBean.getCircleId() +"/"+formBean.getCategoryId() + "/" + formBean.getSubCategoryId() + "/" + oldFile);
				String oldPath = bundle.getString("whatsnewfolder.path") + formBean.getCircleId() + "/" + oldFile;
					
				if (file.getFileSize() == 0)
				{
					logger.error("Empty file found.Response send back to user.");
					errors.add("Empty file found. Please try another file.",new ActionError("file.empty"));
				}
				else if (file.getFileSize() > (5120*1024))
				{
					logger.error("File size exceeds 5MB.Response send back to user.");
					errors.add("File size exceeds 5MB. Please try another file.",new ActionError("km.file.size.exceeds"));
				}
				else
				{
					//Update File From the workspace
					service.updateFile(oldPath,newPath,file);
					
					logger.info(docDesc);
					service.updateFileInfoInDB(oldFile,fileName,userId,docDesc);
					
					
					// Adding file details in the database
						
						formBean.setStatus("UPDATED");
						formBean.setDocumentDesc("");
						messages.add("msg3",new ActionMessage("file.updated"));

					logger.info("File updated successfully");
					
					
				}
	
				
			
			}
			catch(KmException io){
				
				formBean.setDocumentDesc("");
				
				logger.error("Exception occured in update file method " + io.getMessage());
				errors.add("",new ActionError("file.ioexception"));
			
			}
		
			saveMessages(request,messages);
			saveErrors(request,errors);
		//	request.getSession().removeAttribute("file");
			
			logger.info(userBean.getUserLoginId() + " existed updateFile method");
			formBean.setCategoryId("");
			return mapping.findForward("createWhatsNew");
				
		}	
	/**
	 * List the Wahts New documents for CSR user
	 */
	public ActionForward viewFiles(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {

			ActionErrors errors = new ActionErrors();
			ActionForward forward = new ActionForward(); // return value
			KmWhatsNewFormBean formBean = (KmWhatsNewFormBean) form;
			HttpSession session = request.getSession();
			KmUserMstr sessionUserBean = (KmUserMstr) session.getAttribute("USER_INFO");	
			KmWhatsNewService service= new KmWhatsNewServiceImpl();
			KmUserMstr userDto = new KmUserMstr();
			KmUserMstrService userService=new KmUserMstrServiceImpl();
			KmElementMstrService eleService = new KmElementMstrServiceImpl();
			KmWhatsNewService whatsService=new KmWhatsNewServiceImpl();
			ArrayList categoryList=new ArrayList();
			try {
				logger.info(sessionUserBean.getUserLoginId()+" Entered into the viewFiles method of KmWhatsNewAction");
				String circleId=sessionUserBean.getElementId();
				ArrayList whatsNewList=new ArrayList();
				String []categoryId=whatsService.getCategories(sessionUserBean.getElementId());
				
				String favCategoryId=sessionUserBean.getFavCategoryId();
				
				for(int i=0;i<categoryId.length;i++)
					logger.info(categoryId[i]);
				logger.info("CAT ID:"+categoryId);
				String[] circleIds=new String[categoryId.length+1];
				circleIds[0]=circleId;
				for(int i=1,j=0;i<categoryId.length+1;i++,j++){
					circleIds[i]=categoryId[j];
				}
					
				if(sessionUserBean.getKmActorId().equals(Constants.CIRCLE_CSR)){
				
					whatsNewList=service.viewFiles(circleIds,sessionUserBean.getKmActorId());
				}
				else if(sessionUserBean.getKmActorId().equals(Constants.CATEGORY_CSR)){
					if(favCategoryId==null||favCategoryId.equals("")){
						errors.add("",new ActionError("favCat.error"));
						saveErrors(request,errors);
						return mapping.findForward("listWhatsNew");
					}
					String[] favCat={favCategoryId};
					whatsNewList=service.viewFiles(favCat,sessionUserBean.getKmActorId());
				}
				request.setAttribute("WHATS_NEW_LIST",whatsNewList);
				logger.info("Listing files");
				logger.info("Listing the files");	
			} catch (Exception e) {
				logger.error("Exception occured while Whats New files :"+e);
				
			}
			// Finish with
			return mapping.findForward("listWhatsNew");

		}
	/*Dispalys the document content on clicking the document name
	*/
	public ActionForward displayDocument(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
				
				
				ActionErrors errors = new ActionErrors();
				ActionForward forward = new ActionForward();
				HttpSession session = request.getSession();
				KmUserMstr sessionUserBean = (KmUserMstr) session.getAttribute("USER_INFO");	
				KmWhatsNewFormBean formBean = (KmWhatsNewFormBean) form;
				
				KmDisplayFile fileDisplay = new KmDisplayFile();
				ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
				 
				StringBuffer displayFilePath = new StringBuffer(bundle.getString("whatsnewfolder.path"));
				StringBuffer tempFilePath; 
				String returnPath = null;
		
					try
					{
						logger.info(sessionUserBean.getUserLoginId()+" Entered into the displayDocument method of KmWhatsNewAction");
						String documentPath=request.getParameter("documentPath");
						logger.info(documentPath);
						displayFilePath.append(documentPath);
						logger.info("DISPLAY FILE PATH : "+displayFilePath);
						tempFilePath = new StringBuffer(request.getSession().getServletContext().getRealPath("/Docs/"));
						returnPath = fileDisplay.displayFile(displayFilePath.toString(),tempFilePath.toString());
						request.setAttribute("CURRENT_PAGE","DOCUMENT_HOME");
						forward = mapping.findForward("displayWhatsNewDocument");
						
						formBean.setFilePath(returnPath);
						
						logger.info("RETURN PATH : - "+returnPath );
						
						if(returnPath == null)
						{
							logger.error("No File Found in displayFile Method");
							//File is not in the work space
							errors.add("name",new ActionError("km.file.exception"));
							saveErrors(request,errors);
							logger.info("Restuen path is null");
							request.setAttribute("CURRENT_PAGE","DOCUMENT_ERROR");
							forward = mapping.findForward("displayWhatsNewDocument");
							
						}
			
						
						logger.info("return path value is " + returnPath);
			
					}
					catch(Exception e)
					{
						logger.error("IOException occured in displayFile " + e.getMessage());
						errors.add("name",new ActionError("km.file.exception"));
						saveErrors(request,errors);
						request.setAttribute("CURRENT_PAGE","DOCUMENT_ERROR");
						forward = mapping.findForward("displayWhatsNewDocument");
						
					}
					
					
			return forward;
									
			
	} 
	

}
