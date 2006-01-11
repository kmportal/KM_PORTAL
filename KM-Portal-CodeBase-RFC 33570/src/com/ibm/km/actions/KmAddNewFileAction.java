/*
 * Created on Feb 11, 2008
 *
 * This class is used by the user to add a new file in the DB and in workspace.
 * These files are then submitted and approved for viewing.
 * It also contains method for category and sub-category fetching to
 * add the file at the desired location.
 * 
 */
package com.ibm.km.actions;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.json.JSONObject;

import com.ibm.km.common.Constants;
import com.ibm.km.common.PropertyReader;
import com.ibm.km.dto.KmDocumentMstr;
import com.ibm.km.dto.KmElementMstr;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.KmAddFileFormBean;
import com.ibm.km.search.IndexFiles;
import com.ibm.km.services.AddFileService;
import com.ibm.km.services.ApproveFileService;
import com.ibm.km.services.KmDocumentService;
import com.ibm.km.services.KmElementMstrService;
import com.ibm.km.services.impl.AddFileServiceImpl;
import com.ibm.km.services.impl.ApproveFileServiceImpl;
import com.ibm.km.services.impl.KmDocumentServiceImpl;
import com.ibm.km.services.impl.KmElementMstrServiceImpl;

/**
 * @author Vipin - 11/02/2008 
 * 	updated by Varun Agarwal 22 May, 2008
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class KmAddNewFileAction extends DispatchAction{
	
 private static Logger logger = Logger.getLogger(KmAddNewFileAction.class);
	
	/**
	 * This method initializes the add file page and fills up the category list 
	 * to be used to get sub-category and load the file. 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward initAddFile(ActionMapping mapping,ActionForm form,
	HttpServletRequest request,HttpServletResponse response)
	{
		
		KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
		KmAddFileFormBean formBean = (KmAddFileFormBean)form;
		logger.info(userBean.getUserLoginId() + " entered initAddFile method");
		ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");

		KmElementMstrService kmElementService = new KmElementMstrServiceImpl();
		formBean.setInitialSelectBox("-1");
		List firstDropDown=null;
		try {		
			if (userBean.getKmActorId().equals(bundle.getString("LOBAdmin"))||userBean.getKmActorId().equals(bundle.getString("Superadmin"))) {
				firstDropDown = kmElementService.getAllChildren(userBean.getElementId());
			} 
			else {
				firstDropDown = kmElementService.getChildren(userBean.getElementId());
			}
			
			if (firstDropDown!=null && firstDropDown.size()!=0){
			
				formBean.setInitialLevel(((KmElementMstr)firstDropDown.get(0)).getElementLevel());
			}
			else{
						
				int initialLevel=Integer.parseInt(kmElementService.getElementLevelId(userBean.getElementId()));
				initialLevel++;
				formBean.setInitialLevel(initialLevel+"");
			}
			/*			added by harpreet the default date in KM Phase 2   */ 
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
			String date = sdf.format(new java.util.Date());
			date = date.substring(0,10);
			
			if(formBean.getPublishDate().equals(""))
				formBean.setPublishDate(date);
				if(formBean.getPublishEndDate().equals(""))
				formBean.setPublishEndDate(date);
           
			
			/*	added by harpreet the default date in KM Phase 2   */ 
			formBean.setParentId(userBean.getElementId());			
			request.setAttribute("elementLevelNames",kmElementService.getAllElementLevelNames());
			request.setAttribute("allParentIdString",kmElementService.getAllParentIdString("1",userBean.getElementId()));
			request.setAttribute("firstList",firstDropDown);

		} catch (KmException e) {
			logger.error("Exception in Loading page for Upload File: "+e.getMessage());
		}

		logger.info(userBean.getUserLoginId() + " exited initAddFile method");
		return mapping.findForward("initialize");
	}
	
	public ActionForward loadElementDropDown(ActionMapping mapping,ActionForm form,
		HttpServletRequest request,HttpServletResponse response){
		String parentId = request.getParameter("ParentId");
		KmElementMstrService kmElementService = new KmElementMstrServiceImpl();

		try {
			JSONObject json = kmElementService.getElementsAsJson(parentId);
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Content-Type", "application/x-json");
			response.getWriter().print(json);		
		} catch (IOException e) {
			logger.error("IOException in Loading Element Dropdown: "+e.getMessage());
		
		} catch (Exception e) {
			logger.error("Exception in Loading Element Dropdown: "+e.getMessage());
		}
		
		return null;
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
			HttpServletRequest request,HttpServletResponse response)
	{
		KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
		KmAddFileFormBean formBean = (KmAddFileFormBean)form;
		HttpSession session = request.getSession();
		FormFile file = formBean.getNewFile();	
		ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
		
		logger.info(userBean.getUserLoginId() + " entered insertFile method");
		
		ActionErrors errors = new ActionErrors();
		ActionMessages messages =new ActionMessages();
		 
		KmElementMstrService elementService=new KmElementMstrServiceImpl();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH-mm-ss");
		String date = sdf.format(new java.util.Date());
		KmDocumentMstr dto=null;
		
		String elementID="";
		try
		{

			String fileName = file.getFileName();

			if(fileName.equals("")){
				
				file = (FormFile)request.getSession().getAttribute("file");
				fileName=file.getFileName();
			}
			logger.info("File Name: "+fileName);
			String extn = fileName.substring(fileName.indexOf(".")+1);
			fileName = fileName.substring(0,fileName.indexOf("."));


			fileName = fileName + "_" + date + "." + extn;  
			
			String path=formBean.getDocumentPath();
			logger.info("path"+ path);
			String parentId=formBean.getDocumentPath().substring(formBean.getDocumentPath().lastIndexOf("/")+1);
			String elementLevelId=elementService.getElementLevelId(parentId);
			boolean lobWise=formBean.isLobCheck();
			
			logger.info("lobWise flag"+ lobWise);
			
			Map<String,String> serviceData=new HashMap();
			ArrayList<String> circleList=new ArrayList();
			if (lobWise)
			{
				logger.info("In  serviceIdPath");
			ArrayList<String> serviceIds=elementService.getServiceIds(parentId);
			String serviceIdPath=elementService.getServiceIdPath(parentId);
			String servicePath=elementService.getServicePath(parentId);
		
			logger.info("Service Id serviceIdPath"+ serviceIdPath);
			logger.info("Service Id servicePath"+ servicePath);
			logger.info("Service Id arrayList"+ serviceIds.size());

			String arrayServicePath[]= servicePath.split("/");
			
			if (arrayServicePath.length>2){
				 String lob=arrayServicePath[0];
				 String circle=arrayServicePath[1];
				
			}
			
			for (String id:serviceIds){
				
				logger.info("for  serviceIds"+id);
				String newServiceIdPath=elementService.getServiceIdPath(id);
				String newServicePath=elementService.getServicePath(id);
				String newArrayServicePath[]= newServicePath.split("/");
				String newArrayServiceIdPath[]= newServiceIdPath.split("/");
				
	
				logger.info("arrayServicePath length"+arrayServicePath.length);
				logger.info("newArrayServicePath length "+newArrayServicePath.length);

				
				logger.info("arrayServicePath  "+arrayServicePath);
				logger.info("newArrayServiceIdPath  "+newArrayServiceIdPath);
				if (arrayServicePath.length==newArrayServicePath.length)
				{
					
					for (int i=3;i<arrayServicePath.length;i++)
					{
						logger.info("loop value  "+i);
						
						logger.info("arrayServicePath "+arrayServicePath[2]);
						logger.info("newArrayServicePath  "+newArrayServicePath[2]);
						if (arrayServicePath[i].equals(newArrayServicePath[i])){
							if (i==arrayServicePath.length-1){
								logger.info("circle name "+newArrayServiceIdPath[2]);
								logger.info("newServiceIdPath name "+newServiceIdPath);
								serviceData.put(newArrayServiceIdPath[2], newServiceIdPath);
								circleList.add(newArrayServicePath[2]);
							}
						}else
						{
							logger.info("Invalid service path for : id "+id);
							continue;
						}
						
					}
				}
				else
				{
				logger.info("Invalid service path for : id "+id);
				continue;
					
				}
				
			}
		}
		else
		{
			logger.info("In else path is  "+path);
			serviceData.put(elementService.extractCircleId(formBean.getDocumentPath(),3), path);		
		}
			int i=0;
			formBean.setCircleList(circleList);
			for(String circle:serviceData.keySet())
			{
				i++;
				
				String newPath=serviceData.get(circle);
				logger.info("File newPath for : id "+newPath);
				parentId=newPath.substring(newPath.lastIndexOf("/")+1);
				path=null;
				logger.info("File path for : id "+path);
				formBean.setDocumentPath(newPath);
				path=bundle.getString("folder.path")+newPath;
				logger.info("File path for : id "+path);
			//Checking for level of uploading
			if(Integer.parseInt(elementLevelId)<=3){
				formBean.setStatus("L");
			}else{
			//Added by Anil for rectifying the content search
			
				
				
			String circleId=circle;
			////System.out.println(formBean.getDocumentPath());
			////System.out.println("Circle Id of the uploaded document : "+ circleId);
		
			
			formBean.setCircleId(circleId);
			
			String documentPath=path+"/"+fileName;
			int maximumFileSize=Integer.parseInt(bundle.getString("km.file.size"));
			if (file.getFileSize() == 0){
				logger.error("Empty file found.Response send back to user.");
				errors.add("",new ActionError("file.empty"));
			}else if (file.getFileSize() > maximumFileSize){
						logger.error("File size exceeds 5MB.Response send back to user.");
						errors.add("",new ActionError("km.file.size.exceeds"));
			}else{
				AddFileService service = new AddFileServiceImpl();
				String approvalStatus=null;
				if(formBean.getStatus()!=null&&formBean.getStatus().equals("A")){
					
	
				}else if(formBean.getStatus()!=null&&formBean.getStatus().equals("P")){
					
					//Update File From the workspace
					String oldFileName = formBean.getCompleteFileName();
					String oldPath=path+"/"+oldFileName;// old file name I will get here with path
					logger.info("Old File Name: "+oldFileName+" New  File Name: "+fileName);
					KmDocumentMstr docDto=new KmDocumentMstr();
					docDto.setKeyword(formBean.getKeyword());
					docDto.setDocumentDesc(formBean.getDocDesc());
					docDto.setPublishDt(formBean.getPublishDate());
					docDto.setPublishEndDt(formBean.getPublishEndDate());
					docDto.setOldFileName(oldFileName);
					docDto.setFileName(fileName);
					//System.out.println("Old File Name  = "+oldFileName   +"Updated file name : "+fileName);
					docDto.setUserId(userBean.getUserId());
					service.updateFile(oldPath,documentPath,file);
					service.updateDocumentName(docDto);
					KmDocumentService documentService= new KmDocumentServiceImpl();
					String documentId=documentService.getDocumentId(fileName);
					
					//Code to update the Index for the above document in Pending state
					IndexFiles indexObject = new IndexFiles();
					
					if(PropertyReader.getAppValue("SINGLE.INDEX").equals("Y")){	
						
						//System.out.println("Calling index creation method : initIndex");
						indexObject.initIndex(new File(documentPath), documentId,circleId);
					}	
					else{
						//System.out.println("Calling index creation method : initIndexNew");
						indexObject.initIndexNew(new File(documentPath), documentId,circleId);
					}
						
						
					formBean.setStatus("SUCCESS");
					formBean.setKeyword("");
					formBean.setDocDesc("");
					formBean.setPublishDate("");
					formBean.setPublishEndDate("");
					formBean.setDocDisplayName("");
					if(i==1)
					messages.add("msg1",new ActionMessage("file.updated"));					
				}else{
				
					dto=service.checkFile(file,parentId);
					if(dto!=null){
						approvalStatus=dto.getStatus();
						elementID=dto.getElementId();
						formBean.setStatus("V");
						logger.info("status : "+approvalStatus+"    eID"+dto.getElementId());
					}
					
				}
				
				logger.info("Before saving file");
				// Adding file details in the database
				if(dto==null&&!formBean.getStatus().equals("SUCCESS")){
					KmDocumentService documentService=new KmDocumentServiceImpl();
					String approverId=documentService.getCircleApprover(userBean.getElementId());
					if(approverId==null){
						approverId=userBean.getUserId();
						logger.info("Approver : "+approverId); 
					}
					logger.info("Circle Approver = "+approverId);
					service.saveFile(documentPath,file,parentId);
					KmDocumentMstr documentDTO = getDocumentMstrDTO(formBean);
					logger.info("Circle Approver3 = "+approverId);
					//System.out.println("Publ St Dt "+documentDTO.getPublishingStartDate());
					
					documentDTO.setUploadedBy(userBean.getUserId());
					documentDTO.setDocumentName(fileName);
					documentDTO.setDocName(file.getFileName());
					//Km Phase 2
					if(formBean.getDocDisplayName()==null){
						documentDTO.setDocumentDisplayName(file.getFileName());
					}
					if(formBean.getDocDisplayName().equals("")){
						documentDTO.setDocumentDisplayName(file.getFileName());
					}else{
					
					documentDTO.setDocumentDisplayName(formBean.getDocDisplayName());		
					}
					logger.info("Before saving in DB3");
					documentDTO.setUploadedBy(userBean.getUserId());
					documentDTO.setUpdatedBy(userBean.getUserId());	
					documentDTO.setApprovalRejectionBy(approverId);		
					documentDTO.setDocType(Constants.DOC_TYPE_FILE);
					//documentDTO.setApprovalRejectionBy(userBean)
					if(formBean.getStatus()!=null&&formBean.getStatus().equals("A")){
						logger.info("Before saving in DB1");
						documentDTO.setElementId(formBean.getElementId());
						KmElementMstr ele =new KmElementMstr();
						ele.setElementId(documentDTO.getElementId());
						ele.setElementName(formBean.getDocDisplayName());
						elementService.editElement(ele);
						//System.out.println("Approval Status while version creation "+formBean.getStatus()+"   "+formBean.getPublishEndDate());
						if(i==1)
						messages.add("msg1",new ActionMessage("file.addedVersion"));
						}else{
						logger.info("Before saving in DB2");				
						KmElementMstr elementMstrDTO=getElementMstrDTO(formBean);
						
						//KM Phase 2
						elementMstrDTO.setElementName(formBean.getDocDisplayName());
						//elementMstrDTO.setElementName(file.getFileName());
						elementMstrDTO.setCreatedBy(userBean.getUserId());
						elementMstrDTO.setUpdatedBy(userBean.getUserId());
						String elementId=elementService.createElement(elementMstrDTO);
						documentDTO.setElementId(elementId);
						if(i==1)
						messages.add("msg1",new ActionMessage("file.added"));
					}
						
					logger.info("Before saving in DB");
					String documentId=service.saveFileInfoInDB(documentDTO);
					logger.info("documentId after saving in DB"+documentId);
//					Code to update the Index for the above document in Pending state
					IndexFiles indexObject = new IndexFiles();
					if(PropertyReader.getAppValue("SINGLE.INDEX").equals("Y")){	
						
						//System.out.println("Calling index creation method : initIndex");
						indexObject.initIndex(new File(documentPath), documentId,circleId);
					}	
					else{
						//System.out.println("Calling index creation method : initIndexNew");
						indexObject.initIndexNew(new File(documentPath), documentId,circleId);
					}
				//	}
					
					
					
					if(userBean.getKmActorId().equals(Constants.SUPER_ADMIN)||userBean.getKmActorId().equals(Constants.CIRCLE_ADMIN)||userBean.getKmActorId().equals(Constants.LOB_ADMIN)){
						autoApproveFile(documentId,userBean.getUserId());	
					}   
					if(userBean.getKmActorId().equals(Constants.CIRCLE_USER)){
						ApproveFileService approveService  = new ApproveFileServiceImpl();
						//approveService.updateEscalationTime(documentId,userBean.getUserId());
						approveService.updateEscalationTime(documentId,approverId);
					} 
					if(i==serviceData.size()){
					formBean.setDocDisplayName("");
					formBean.setStatus("");
					formBean.setKeyword("");
					formBean.setDocDesc("");
					formBean.setPublishDate("");
					formBean.setPublishEndDate("");
					}
				//	messages.add("msg1",new ActionMessage("file.added"));
					logger.info("File added successfully");
					
					
					
					
				}else if(approvalStatus!=null&&approvalStatus.equals("P")){
					formBean.setStatus("P");
					formBean.setCompleteFileName(dto.getDocumentName());
					session.setAttribute("file",file);
					logger.error("Duplicate Pending file found.Response send back to user.");
					messages.add("msg1",new ActionMessage("pendingFile.exists"));
				}else if(approvalStatus!=null&&approvalStatus.equals("A")){
					logger.info("DTO NOT NULL");
					formBean.setElementId(elementID);
					formBean.setStatus("A");
					formBean.setCompleteFileName(file.getFileName());
					session.setAttribute("file",file);
					logger.error("Duplicate Approved file found.Response send back to user.");
					messages.add("msg1",new ActionMessage("approvedFile.exists"));
					
				}
			}
			}
			}
		}
		catch(KmException io){
			
			logger.error("KmException occured in insertFile method " + io.getMessage());
			io.printStackTrace();
			formBean.setStatus("FAILURE");
			formBean.setDocDesc("");
			formBean.setKeyword("");
			formBean.setPublishDate("");
			formBean.setPublishEndDate("");
			formBean.setDocDisplayName("");
			formBean.setInitialSelectBox("-1");
			formBean.setCompleteFileName("");
			errors.add("",new ActionError("file.ioexception"));
			saveMessages(request,messages);
			saveErrors(request,errors);
		//	return initAddFile(mapping,form,request,response);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("Exception occured in insertFile method " + e.getMessage());
			formBean.setStatus("FAILURE");
			formBean.setDocDesc("");
			formBean.setKeyword("");
			formBean.setPublishDate("");
			formBean.setPublishEndDate("");
			formBean.setDocDisplayName("");
			formBean.setInitialSelectBox("-1");
			formBean.setCompleteFileName("");
			errors.add("",new ActionError("file.exception"));
			saveMessages(request,messages);
			saveErrors(request,errors);			
		}
		saveMessages(request,messages);
		saveErrors(request,errors);
		logger.info(userBean.getUserLoginId() + " existed insertFile method");
		return initAddFile(mapping, formBean, request, response);
	}	
	
	/**
	 * @param formBean
	 * @return
	 */
	private KmElementMstr getElementMstrDTO(KmAddFileFormBean formBean) throws ParseException, KmException {
		KmElementMstr elementMstrDto= new KmElementMstr();
		elementMstrDto.setElementDesc(formBean.getDocDesc());
		elementMstrDto.setParentId(formBean.getDocumentPath().substring(formBean.getDocumentPath().lastIndexOf("/")+1));
		elementMstrDto.setPanStatus("N");
		elementMstrDto.setElementLevel("0");
		elementMstrDto.setStatus("A");
		return elementMstrDto;
	}
	/**
	 * @param formBean
	 * @return
	 */
	private KmDocumentMstr getDocumentMstrDTO(KmAddFileFormBean formBean) throws ParseException {
		KmDocumentMstr documentDTO= new KmDocumentMstr();
		documentDTO.setDocumentDesc(formBean.getDocDesc());
		documentDTO.setKeyword(formBean.getKeyword());
		documentDTO.setApprovalStatus("P");
		documentDTO.setDocumentPath(formBean.getDocumentPath());
		
// add publishing date and publishing end date of the document		
		documentDTO.setPublishingStartDate(formBean.getPublishDate());
		documentDTO.setPublishingEndDate(formBean.getPublishEndDate());
		return documentDTO;
	}
	/**
	 * @param documentId
	 * @param userId
	 */
	private void autoApproveFile(String documentId, String userId){
		String documentIds[]={documentId};
		String comment[]={"AutoApproved"};
		try {
			ApproveFileService service = new ApproveFileServiceImpl();
			service.approveFiles(documentIds,comment,userId);
		} catch (KmException e) {
			logger.error("Exception in Approving Files: "+e.getMessage());
			
		}
	}

	
		
}

