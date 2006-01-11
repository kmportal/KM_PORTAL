package com.ibm.km.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

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

import com.ibm.km.dto.BulkMsgDto;
import com.ibm.km.dto.BulkUploadDetailsDTO;
import com.ibm.km.dto.KmDocumentMstr;
import com.ibm.km.dto.KmElementMstr;
import com.ibm.km.dto.KmFileDto;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.BulkUploadDetailsFormBean;
import com.ibm.km.forms.KmAddFileFormBean;
import com.ibm.km.forms.KmScrollerMstrFormBean;
import com.ibm.km.scheduler.SchedulerTrigger;
import com.ibm.km.search.IndexFiles;
import com.ibm.km.services.AddFileService;
import com.ibm.km.services.BulkUploadDetailsService;
import com.ibm.km.services.BulkUserService;
import com.ibm.km.services.KmDocumentService;
import com.ibm.km.services.KmElementMstrService;
import com.ibm.km.services.impl.AddFileServiceImpl;
import com.ibm.km.services.impl.BulkUploadDetailsServiceImpl;
import com.ibm.km.services.impl.BulkUserServiceImpl;
import com.ibm.km.services.impl.KmDocumentServiceImpl;
import com.ibm.km.services.impl.KmElementMstrServiceImpl;
import com.ibm.km.common.Constants;
import com.ibm.km.common.FileCopy;
import com.ibm.km.common.PropertyReader;


/**
 * @author Aman
 * 
 */
public class BulkUploadDetailsAction extends DispatchAction {
	private static Logger logger = Logger.getLogger(BulkUploadDetailsAction.class
			.getName());

	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		

		ActionForward forward = new ActionForward(); // return value
		KmUserMstr userBean = (KmUserMstr) request.getSession().getAttribute(
				"USER_INFO");
		BulkUploadDetailsFormBean formBean = (BulkUploadDetailsFormBean) form;
		//formBean.setErrorFlag(false);
		ArrayList bulkUploadDetails = null;
		
		HttpSession session = request.getSession();
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
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
			String date = sdf.format(new java.util.Date());
			date = date.substring(0,10);
			
			if(formBean.getPublishDate().equals(""))
				formBean.setPublishDate(date);
				if(formBean.getPublishEndDate().equals(""))
				formBean.setPublishEndDate(date);
			formBean.setParentId(userBean.getElementId());			
			request.setAttribute("elementLevelNames",kmElementService.getAllElementLevelNames());
			request.setAttribute("allParentIdString",kmElementService.getAllParentIdString("1",userBean.getElementId()));
			request.setAttribute("firstList",firstDropDown);
			//logger.debug("Ab to ho ja gadhe !!! ");

		} catch (KmException e) {
			logger.error("Exception in Loading page for Upload File: "+e.getMessage());
		}
		forward = mapping.findForward("bulkDetailsUpload");

		// Finish with
		return (forward);

	}

	
	public ActionForward uploadFile(ActionMapping mapping, ActionForm form,
	HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		BulkUploadDetailsFormBean formBean = (BulkUploadDetailsFormBean) form;
		
		ActionForward forward = new ActionForward();
		KmUserMstr userBean = (KmUserMstr) request.getSession().getAttribute(
				"USER_INFO");
		
		ResourceBundle bundle = ResourceBundle
				.getBundle("ApplicationResources");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH-mm-ss");
		String date = sdf.format(new java.util.Date());
		BulkUploadDetailsService service = new BulkUploadDetailsServiceImpl();
		FormFile file = formBean.getNewFile();
		
		KmElementMstrService elementService=new KmElementMstrServiceImpl();
		String elementID="";
		Map<String,String> serviceData=new HashMap();
		ArrayList<String> circleList=new ArrayList();
		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();
		//added for content search 
		
		boolean isValidUpload=validateFileUpload(formBean, userBean, request, errors, messages);
		if (isValidUpload) {
			logger.info("is Valid Upload");
			try {
				String fileName = file.getFileName();
				if (fileName.equals("")) {
					file = (FormFile) request.getSession().getAttribute("file");
					fileName = file.getFileName();
				}
				logger.info("File Name: " + fileName);
				String extn = fileName.substring(fileName.indexOf(".") + 1);
				fileName = fileName.substring(0, fileName.indexOf("."));
				fileName = fileName + "_" + date + "." + extn;

				String path = formBean.getDocumentPath();
				String existingPath = bundle.getString("folder.path");
				logger.info("Calling createNewFolder()");
				FileCopy.createNewFolder(path, existingPath);
				
				logger.info("File Name :: " + fileName + " path :: " + path);

				String parentId = formBean.getDocumentPath().substring(
						formBean.getDocumentPath().lastIndexOf("/") + 1);
				String elementLevelId = elementService
						.getElementLevelId(parentId);

				serviceData.put(elementService.extractCircleId(formBean
						.getDocumentPath(), 3), path);
				int i = 0;
				formBean.setCircleList(circleList);
				for (String circle : serviceData.keySet()) {
					i++;
					String newPath = serviceData.get(circle);
					logger.info("File newPath  " + newPath);
					parentId = newPath.substring(newPath.lastIndexOf("/") + 1);
					path = null;
					logger.info("File path for : id " + path);
					formBean.setDocumentPath(newPath);
					path = bundle.getString("folder.path") + newPath;
					logger.info("File path for : id " + path);

					String circleId = circle;
					logger.info(formBean.getDocumentPath());
					logger.info("Circle Id of the uploaded document : "
							+ circleId);
					formBean.setCircleId(circleId);

					KmDocumentService documentService = new KmDocumentServiceImpl();
					AddFileService addFileService = new AddFileServiceImpl();
					String documentId = documentService.getDocumentId(fileName);
					String documentPath = path + "/" + fileName;
					int maximumFileSize = Integer.parseInt(bundle
							.getString("km.file.size"));
					IndexFiles indexObject = new IndexFiles();

					if (file.getFileSize() == 0) {
						logger
								.error("Empty file found.Response send back to user.");
						messages.add("msg", new ActionMessage("file.empty"));
					} else if (file.getFileSize() > maximumFileSize) {
						logger
								.error("File size exceeds 5MB.Response send back to user.");
						messages.add("msg", new ActionMessage(
								"km.file.size.exceeds"));
					}
					addFileService.saveFile(documentPath, file, parentId);
					KmDocumentMstr documentDTO = getDocumentMstrDTO(formBean);
					documentDTO.setUploadedBy(userBean.getUserId());
					documentDTO.setDocumentName(fileName);
					documentDTO.setDocName(file.getFileName());
					documentDTO.setDocumentDisplayName(file.getFileName());
					logger.info("Before saving details in database");
					documentDTO.setUpdatedBy(userBean.getUserId());
					documentDTO.setDocType(Constants.DOC_TYPE_FILE);

					logger.info("Before saving in DB2");
					KmElementMstr elementMstrDTO = getElementMstrDTO(formBean);

					// KM Phase 2
					//elementMstrDTO.setElementName(formBean.getDocDisplayName()
					// );
					elementMstrDTO.setElementName(file.getFileName());
					elementMstrDTO.setCreatedBy(userBean.getUserId());
					elementMstrDTO.setUpdatedBy(userBean.getUserId());
					String elementId = elementService
							.createElement(elementMstrDTO);
					documentDTO.setElementId(elementId);
					//if (i == 1)														//two lines commented to avoid extra message 
						//messages.add("msg1", new ActionMessage("file.added"));
					String docId = addFileService.saveFileInfoInDB(documentDTO);
					logger.info("documentId after saving in DB" + docId);

					if (PropertyReader.getAppValue("SINGLE.INDEX").equals("Y")) {

						logger
								.info("Calling index creation method : initIndex");
						indexObject.initIndex(new File(documentPath), docId,
								circleId);
					} else {
						logger
								.info("Calling index creation method : initIndexNew");
						indexObject.initIndexNew(new File(documentPath), docId,
								circleId);
					}
				}
			} catch (KmException io) {

				logger.error("KmException occured in insertFile method "
						+ io.getMessage());
				io.printStackTrace();
			}
		}
		//forward = mapping.findForward("bulkDetailsUpload");
		//return forward;
		//return null;
		return init(mapping, formBean, request, response);

	}
	
	public ActionForward openErrLog(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		BulkUploadDetailsFormBean formBean = (BulkUploadDetailsFormBean) form;
		HttpSession session = request.getSession();
		java.util.Date date = new java.util.Date();
		String filePath="",fileNameNew="";
		KmUserMstr userBean = (KmUserMstr) request.getSession().getAttribute("USER_INFO");

		try
		  {
			
			
			filePath=(String)request.getParameter("filePath");
			logger.info("filePath::::::::::::"+filePath);
			fileNameNew = filePath.substring(filePath.lastIndexOf("/")+1, filePath.length());
			
			response.setContentType("text/csv");
			response.addHeader("Content-Disposition", "attachment; filename="+fileNameNew);	
			
			java.io.File uFile= new java.io.File(filePath);
			int fSize=(int)uFile.length();
			java.io.FileInputStream fis = new java.io.FileInputStream(uFile);
			java.io.PrintWriter pw = response.getWriter();
			int c=-1;
			// Loop to read and write bytes.
			while ((c = fis.read()) != -1){
				pw.print((char)c);
			}
			// Close output and input resources.
			fis.close();
			pw.flush();
			pw=null;
		}
		catch(Exception e)
		{
			logger.error("Error while writing logs::",e);
			e.printStackTrace();
		}

		return (null);

	}
	
	public ActionForward loadElementDropDown(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response){
			String parentId = request.getParameter("ParentId");
			KmElementMstrService kmElementService = new KmElementMstrServiceImpl();
			logger.info("Loading Elements ....");
			logger.info("parentId ::: "+parentId);
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
	private KmDocumentMstr getDocumentMstrDTO(BulkUploadDetailsFormBean formBean) throws ParseException {
		KmDocumentMstr documentDTO= new KmDocumentMstr();
		documentDTO.setDocumentDesc(formBean.getDocDesc());
		documentDTO.setDocumentPath(formBean.getDocumentPath());
		documentDTO.setPublishingStartDate(formBean.getPublishDate());
		documentDTO.setPublishingEndDate(formBean.getPublishEndDate());
		documentDTO.setApprovalStatus("A");
		return documentDTO;
	}
	private KmElementMstr getElementMstrDTO(BulkUploadDetailsFormBean formBean) throws ParseException, KmException {
		KmElementMstr elementMstrDto= new KmElementMstr();
		elementMstrDto.setElementDesc(formBean.getDocDesc());
		elementMstrDto.setParentId(formBean.getDocumentPath().substring(formBean.getDocumentPath().lastIndexOf("/")+1));
		elementMstrDto.setPanStatus("N");
		elementMstrDto.setElementLevel("0");
		elementMstrDto.setStatus("A");
		return elementMstrDto;
	}
	
	public boolean validateFileUpload(BulkUploadDetailsFormBean formBean, KmUserMstr userBean, HttpServletRequest request, ActionErrors errors, ActionMessages messages){
		BulkUploadDetailsDTO bulkUploadDetailsDTO=new BulkUploadDetailsDTO();
		BulkUploadDetailsService service = new BulkUploadDetailsServiceImpl();
		FormFile file = formBean.getNewFile();
		try {
			
			bulkUploadDetailsDTO.setDocType(formBean.getDocType());
			bulkUploadDetailsDTO.setUploadType(formBean.getUploadType());
			logger.info("docType::::::::::"+bulkUploadDetailsDTO.getDocType());
			logger.info("asa::filename:::"+file.getFileName().toString());
			
			String arr= (file.getFileName().toString()).substring(file.getFileName().toString().lastIndexOf(".")+1,file.getFileName().toString().length());
			if(!arr.equalsIgnoreCase("xls") && !arr.equalsIgnoreCase("xlsx"))
			{

				messages.add("msg1",new ActionMessage("km.bulk.assignment.excel.only"));
				formBean.setErrorFlag(false);
			}
			else
			{
				
				BulkMsgDto  msgDto=service.uploadMstr(file,formBean,userBean);
				
				
				switch(msgDto.getMsgId())
				{
				case Constants.BULK_UPLOAD_INVALID_EXCEL :
					logger.info("INVALID_EXCEL::::::::::::::action");
					messages.add("msg",new ActionMessage("km.bulk.upload.invalid.excel"));
					formBean.setErrorFlag(false);
					break;
					
				case Constants.BULK_UPLOAD_BLANK_EXCEL :
					logger.info("blank excel:::::action");
					messages.add("msg",new ActionMessage("km.bulk.upload.blank.excel"));
					formBean.setErrorFlag(false);
					break;
					
				case Constants.BULK_UPLOAD_FAIL :
					logger.info("upload failed:::::action");
					messages.add("msg",new ActionMessage("km.bulk.upload.fail"));
					formBean.setErrorFlag(false);
					break;
					
				case Constants.BULK_UPLOAD_SUCCESS :
					logger.info("upload success:::::action");
					messages.add("msg",new ActionMessage("km.bulk.upload.success"));
					request.setAttribute("filePath",msgDto.getFilePath());
					formBean.setErrorFlag(true);
					break;
					
				case Constants.INVALID_FILESIZE :
					logger.info("invalid file size:::::action");
					messages.add("msg",new ActionMessage("km.bulk.upload.invalid.filesize"));
					request.setAttribute("filePath",msgDto.getFilePath());
					formBean.setErrorFlag(false);
					break;
					
				case Constants.BULK_UPLOAD_INVALID_HEADER :
					logger.info("Header(s) are not correct !!:::::action");
					messages.add("msg",new ActionMessage("km.bulk.upload.invalid.header"));
					request.setAttribute("filePath",msgDto.getFilePath());
					formBean.setErrorFlag(false);
					break;	
					
				}
				logger.info("asa::::::iserrorflag:::::"+formBean.isErrorFlag());
				logger.info("asa::msg::"+messages.get("msg").toString());
				logger.info("asa::msg::"+messages.get("msg").toString());
				
				
			}
			saveMessages(request, messages);
		} 
		
		
		catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.info("upload failed:::::action::catch");
			messages.add("msg",new ActionMessage("km.bulk.upload.fail"));
			formBean.setErrorFlag(false);
			errors.add("", new ActionError("km.bulk.upload.fail"));
			logger.error("Error while uploading::",e);
			//errors.add("", new ActionError("file.exception"));
			saveErrors(request, errors);
			e.printStackTrace();
		
		}
		
		catch (Exception e) {
			e.printStackTrace();
			logger.error("Error while uploading::",e);
			logger.info("upload failed:::::action::catch");
			messages.add("msg",new ActionMessage("km.bulk.upload.fail"));
			formBean.setErrorFlag(false);
			errors.add("", new ActionError("km.bulk.upload.fail"));
			//errors.add("", new ActionError("file.exception"));
			saveErrors(request, errors);
			e.printStackTrace();
		}
		
		
		
		return formBean.isErrorFlag();
		
	}
	
	

}
