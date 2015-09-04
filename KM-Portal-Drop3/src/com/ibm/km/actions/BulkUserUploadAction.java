/*
 * Created on Oct 14, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
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
import org.apache.struts.upload.FormFile;

import com.ibm.km.dto.KmElementMstr;
import com.ibm.km.dto.KmFileDto;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.BulkUserUploadFormBean;
import com.ibm.km.forms.KmScrollerMstrFormBean;
import com.ibm.km.scheduler.SchedulerTrigger;
import com.ibm.km.services.BulkUserService;
import com.ibm.km.services.KmElementMstrService;
import com.ibm.km.services.impl.BulkUserServiceImpl;
import com.ibm.km.services.impl.KmElementMstrServiceImpl;
import com.ibm.km.common.Constants;


/**
 * @author Anil
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class BulkUserUploadAction extends DispatchAction {
	private static Logger logger = Logger.getLogger(BulkUserUploadAction.class
			.getName());

	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionForward forward = new ActionForward(); // return value
		KmUserMstr userBean = (KmUserMstr) request.getSession().getAttribute(
				"USER_INFO");
		BulkUserUploadFormBean formBean = (BulkUserUploadFormBean) form;
		ArrayList bulkUploadDetails = null;
		forward = mapping.findForward("bulkUserUpload");

		try {
			initializeParameter(request, userBean, formBean);
			formBean.setCircleId(userBean.getElementId());
			formBean.setLoginActorId(userBean.getKmActorId());
			request.setAttribute("BULK_USER", bulkUploadDetails);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Finish with
		return (forward);

	}
	
	private void initializeParameter(HttpServletRequest request,
			KmUserMstr userBean,
			BulkUserUploadFormBean formBean) throws KmException {
		ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");

		KmElementMstrService kmElementService = new KmElementMstrServiceImpl();
		List firstDropDown;
		try{
		if (userBean.getKmActorId().equals(bundle.getString("LOBAdmin"))||userBean.getKmActorId().equals(bundle.getString("Superadmin"))) {
			firstDropDown = kmElementService.getAllChildren(userBean.getElementId());
		}else{
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
		formBean.setParentId(userBean.getElementId());
		formBean.setCircleElementId("");
		formBean.setInitialSelectBox("-1");
	
		request.setAttribute("elementLevelNames",kmElementService.getAllElementLevelNames());
		request.setAttribute("allParentIdString",kmElementService.getAllParentIdString("1",userBean.getElementId()));
		request.setAttribute("firstList",firstDropDown);
		}catch(Exception e){
			logger.info("Exception occured while initializing the Bulk User Upload Action page ");
			
			
		}
	}

	public ActionForward uploadFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		KmUserMstr userBean = (KmUserMstr) request.getSession().getAttribute(
				"USER_INFO");
		BulkUserUploadFormBean formBean = (BulkUserUploadFormBean) form;
		List circleList = new ArrayList();
		ResourceBundle bundle = ResourceBundle
				.getBundle("ApplicationResources");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH-mm-ss");
		String date = sdf.format(new java.util.Date());
		BulkUserService service = new BulkUserServiceImpl();
		FormFile file = formBean.getNewFile();
		
		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();
		try {

			formBean.setCircleList(circleList);

			String fileName = file.getFileName();
			String extn = fileName.substring(fileName.indexOf(".") + 1);
			fileName = fileName.substring(0, fileName.indexOf("."));
			fileName = fileName + "_" + date + "." + extn;
			//change path by vishwas 
			String filePath = bundle.getString("csv.path") + "/" + fileName;
			//end by vishwas
			File f = new File(filePath);
			
			byte[] fileData = file.getFileData();
			logger.info("datalength" + fileData.length);
			RandomAccessFile raf = new RandomAccessFile(f, "rw");
			raf.write(fileData);
			raf.close();
			if(f.length()==0){
				errors.add("", new ActionError("file.empty"));
				saveErrors(request, errors);
				return init(mapping, formBean, request, response);
			}
			// Inserting file details into DB.
			KmFileDto fileDto = new KmFileDto();
			fileDto.setFilePath(filePath);
			fileDto.setFileName(fileName);
			fileDto.setUploadedBy(userBean.getUserId());
			
			if(userBean.getKmActorId().equals(Constants.SUPER_ADMIN))
			{
				fileDto.setElementId(formBean.getCircleElementId());
			
			}
			else if(userBean.getKmActorId().equals(Constants.LOB_ADMIN))
			   fileDto.setElementId(formBean.getInitialSelectBox());
			else if(userBean.getKmActorId().equals(Constants.CIRCLE_ADMIN))
				fileDto.setElementId(userBean.getElementId());
			
			fileDto.setStatus("U");
			
			if (formBean.getUploadType().equals(Constants.BULK_USER_CREATION)) {
				fileDto.setFileType("C");
			} else if (formBean.getUploadType().equals(Constants.BULK_USER_UPDATION)) {
				fileDto.setFileType("U");
			}
			else if (formBean.getUploadType().equals(Constants.BULK_USER_DELETION)) {
				fileDto.setFileType("D");
			}
			
			int fileId = service.uploadFile(fileDto);

			if (formBean.getUploadType().equals(Constants.BULK_USER_CREATION))
				messages.add("msg1", new ActionMessage("bu.success.creation"));
			else if (formBean.getUploadType().equals(Constants.BULK_USER_UPDATION))
				messages.add("msg1", new ActionMessage("bu.success.updation"));
			else if (formBean.getUploadType().equals(Constants.BULK_USER_DELETION))
				messages.add("msg1", new ActionMessage("bu.success.deletion"));
			saveMessages(request, messages);
		} catch (Exception e) {
			e.printStackTrace();
			errors.add("", new ActionError("file.exception"));
			saveErrors(request, errors);
			e.printStackTrace();
		}
		// Finish with
		// added by ajil to start Bulk User Processing 
	/*	try{
			BulkUserService bulkUserService=new BulkUserServiceImpl();
			bulkUserService.bulkUpload();
		}catch (IOException e) {
			//e.printStackTrace();
			// TODO: handle exception
		}*/
		return init(mapping, formBean, request, response);

	}

	public ActionForward initBulkUserDelete(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		KmUserMstr userBean = (KmUserMstr) request.getSession().getAttribute(
				"USER_INFO");
		// List circleList=new ArrayList();
		ArrayList bulkDeleteDetails = null;
		logger.info(userBean.getUserLoginId()
				+ "  Entered into initBulkUserDelete method");
		try {

			// circleList=elementService.getAllChildren(userBean.getElementId(),Constants.CIRCLE_USER);
			// formBean.setCircleList(circleList);
			// formBean.setLoginActorId(userBean.getKmActorId());
			request.setAttribute("BULK_USER", bulkDeleteDetails);

			return mapping.findForward("bulkUserDelete");
		} catch (Exception e) {
			e.printStackTrace();
			return mapping.findForward("bulkUserDelete");
		}

	}

	public ActionForward bulkUserDelete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		KmUserMstr userBean = (KmUserMstr) request.getSession().getAttribute(
				"USER_INFO");
		BulkUserUploadFormBean formBean = (BulkUserUploadFormBean) form;
		ResourceBundle bundle = ResourceBundle
				.getBundle("ApplicationResources");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH-mm-ss");
		BulkUserService service = new BulkUserServiceImpl();
		ArrayList bulkDeleteDetails = null;
		String date = sdf.format(new java.util.Date());
		FormFile file = formBean.getNewFile();
		
		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();
		try {

			

			// //System.out.println(file.getFileName());
			String fileName = file.getFileName();
			String extn = fileName.substring(fileName.indexOf(".") + 1);
			fileName = fileName.substring(0, fileName.indexOf("."));
			fileName = fileName + "_" + date + "." + extn;
			String filePath = bundle.getString("csv.deletepath") + "/"
					+ fileName;
			File f = new File(filePath);
			byte[] fileData = file.getFileData();
			logger.info("datalength" + fileData.length);
			RandomAccessFile raf = new RandomAccessFile(f, "rw");
			raf.write(fileData);
			raf.close();

			// Bulk uploading
			if (userBean.getKmActorId().equals(Constants.CIRCLE_ADMIN)) {
				formBean.setCircleId(userBean.getElementId());
			}

			// com.ibm.km.common.CsvReader csvReaderObj=new
			// com.ibm.km.common.CsvReader();
			KmFileDto dto = service.bulkDelete(filePath);
			if (dto.getStatus().equals("S")) {
				messages.add("msg1", new ActionMessage("bu.success"));
			} else if (dto.getStatus().equals("P")) {
				messages.add("msg2", new ActionMessage("bu.userError"));
			} else {
				messages.add("msg3", new ActionMessage("bu.error"));
			}
			bulkDeleteDetails = service.getBulkDeleteDetails(dto.getFileId()
					.trim());
			request.setAttribute("BULK_USER", bulkDeleteDetails);
			saveMessages(request, messages);
		} catch (Exception e) {
			errors.add("", new ActionError("file.exception"));
			saveErrors(request, errors);
			e.printStackTrace();
		}
		// Finish with
		return mapping.findForward("bulkUserDelete");

	}

	public ActionForward initViewBulkUploadFiles(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		BulkUserUploadFormBean formBean = (BulkUserUploadFormBean) form;
		KmUserMstr userBean = (KmUserMstr) request.getSession().getAttribute(
				"USER_INFO");
		logger.info(userBean.getUserLoginId()
				+ "  Entered into viewBulkUploadFiles method");
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
			String date = sdf.format(new java.util.Date());
			date = date.substring(0, 10);
			formBean.setDate(date);
			formBean.setInitStatus("init");

		} catch (Exception e) {
			logger
					.error("Exception occured while initializing the view Bulk User Files Page");

		}
		return mapping.findForward("viewBulkUserFiles");
	}

	public ActionForward viewBulkUploadFiles(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		BulkUserUploadFormBean formBean = (BulkUserUploadFormBean) form;
		KmUserMstr userBean = (KmUserMstr) request.getSession().getAttribute(
				"USER_INFO");
		BulkUserService service = new BulkUserServiceImpl();
		ArrayList fileList = new ArrayList();
		logger.info(userBean.getUserLoginId()
				+ "  Entered into viewBulkUploadFiles method");
		String date = "";
		try {
			date = formBean.getDate();
			
			fileList = service.getBulkUserFiles(userBean.getElementId(),date);
			
			formBean.setInitStatus("notInit");
			request.setAttribute("FILE_LIST", fileList);
		} catch (KmException e) {

		} catch (Exception e) {
			logger
					.error("Exception occured while initializing the view Bulk User Files Page");

		}
		return mapping.findForward("viewBulkUserFiles");
	}

	public ActionForward openFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ResourceBundle bundle = ResourceBundle
				.getBundle("ApplicationResources");
		BulkUserUploadFormBean formBean = (BulkUserUploadFormBean) form;
		String filePath = (String) request.getParameter("filePath");
		
		//System.out.println("isLog"+request.getParameter("isLog"));
		if (request.getParameter("isLog") != null) {

			filePath = filePath.substring(0, filePath.lastIndexOf("."))
					+ ".log";
		}
		String fileName = filePath.substring((filePath.lastIndexOf("/") + 1),
				filePath.length());
		File f = new File(filePath);
		response.setContentType("application/vnd.ms-excel");
		response.setContentLength((int) f.length());
		response.setHeader("Content-Disposition", "attachment; filename="
				+ fileName);
		response.setHeader("Cache-Control", "no-cache");
		OutputStream outStream = response.getOutputStream();
		FileInputStream inStream = new FileInputStream(f);
		try {

			byte[] buf = new byte[8192];

			int sizeRead = 0;

			while ((sizeRead = inStream.read(buf, 0, buf.length)) > 0) {
				log.debug("size:" + sizeRead);
				outStream.write(buf, 0, sizeRead);
			}
			inStream.close();
			outStream.close();
		} catch (IllegalStateException ignoredException) {
			if (outStream != null) {
				outStream.close();
			}
			if (inStream != null) {
				inStream.close();
			}
		//	ignoredException.printStackTrace();
		}
		return null;
		//return mapping.findForward("openfile");

	}

}
