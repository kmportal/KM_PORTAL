package com.ibm.km.actions;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;

import com.ibm.km.common.Constants;
import com.ibm.km.common.FileCopy;
import com.ibm.km.common.PropertyReader;
import com.ibm.km.dao.KmSchemesAndBenefitsDao;
import com.ibm.km.dao.impl.KmSchemesAndBenefitsDaoImpl;
import com.ibm.km.dto.KmDocumentMstr;
import com.ibm.km.dto.KmElementMstr;
import com.ibm.km.dto.KmSchemesAndBenefitsDto;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.KmSchemesAndBenefitsDetailsFormBean;
import com.ibm.km.search.IndexFiles;
import com.ibm.km.services.AddFileService;
import com.ibm.km.services.BulkUploadDetailsService;
import com.ibm.km.services.KmDocumentService;
import com.ibm.km.services.KmElementMstrService;
import com.ibm.km.services.impl.AddFileServiceImpl;
import com.ibm.km.services.impl.BulkUploadDetailsServiceImpl;
import com.ibm.km.services.impl.KmDocumentServiceImpl;
import com.ibm.km.services.impl.KmElementMstrServiceImpl;

public class KmUploadSchemesAndBenefitsAction extends Action
{
	private static Logger logger = Logger.getLogger(KmUploadSchemesAndBenefitsAction.class.getName());
	/**
	 * This function uploads the file and updates the grid
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
			ActionMessages messages= new ActionMessages();
			ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
			
			KmSchemesAndBenefitsDetailsFormBean formBean = (KmSchemesAndBenefitsDetailsFormBean) form;
			KmSchemesAndBenefitsDto iPortalDTO = new KmSchemesAndBenefitsDto();
			KmSchemesAndBenefitsDao iPortalDaoImpl = new KmSchemesAndBenefitsDaoImpl();
			KmUploadSchemesAndBenefitsActionUtil util = new KmUploadSchemesAndBenefitsActionUtil();
			KmElementMstrService elementService=new KmElementMstrServiceImpl();
			
			String insertedBy = null;
			
			try {
				//User Id To be taken from Session
				KmUserMstr userBean = (KmUserMstr) request.getSession().getAttribute("USER_INFO");
				insertedBy = userBean.getUserLoginId();
				formBean.setInsertedBy(insertedBy);
				logger.info("Initiated by "+ formBean.getInsertedBy());
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH-mm-ss");
				String date = sdf.format(new java.util.Date());
				
				if(formBean != null ){
					BeanUtils.copyProperties(iPortalDTO, formBean);
					
					FormFile file = formBean.getUploadFile();
					if(file != null && !file.getFileName().equals("")){	
						
						//Validating File at server side
						boolean isValidUpload = util.validateFileUpload(formBean, userBean, request, errors, messages);
						
						if(isValidUpload){
							//Appending Date to file name
							String fileName = file.getFileName();
							if (fileName.equals("")) {
								file = (FormFile) request.getSession().getAttribute("file");
								fileName = file.getFileName();
							}
							
							String extn = fileName.substring(fileName.indexOf(".") + 1);
							fileName = fileName.substring(0, fileName.indexOf("."));
							fileName = fileName + "_" + date + "." + extn;
							
							formBean.setFileName(fileName);
							
							//Uploading File to temp Loc
							boolean result = util.uploadMstr(formBean, userBean, errors, messages);
							
							if(result){
								String path = formBean.getFilePath();
								
								//Reading File
								ReadExcel readExcel=new ReadExcel();
								ArrayList records = readExcel.readFromExcelFile(path);
								if(records.size()!=0){
								//Validating File
								boolean flag1, flag2 = false;
								flag1 = readExcel.validateExcelRows(records);
								if (flag1 == false) {
									messages.add("msg1",new ActionMessage("excel.rowValidation.error"));
								}
								logger.info("Validating Rows Successful");
								
								if (flag1 == true) {
									flag2 = readExcel.validateExcelColumns(records);
									if (flag2 == false) {
										messages.add("msg2",new ActionMessage("excel.colValidation.error"));
									}
								}
								iPortalDTO.setSheetList(records);
								logger.info("Validating Columns Successful");
								
								if(flag1 && flag2){
									//Creating a folder
									String newPath = Constants.BULK_FILE_PATH;
									String existingPath = bundle.getString("folder.path");
									logger.info("Calling createNewFolder()");
									util.createNewFolder(newPath, existingPath);
									
									logger.info("File Name :: " + fileName + " path :: " + path);
									
									IndexFiles indexObject = new IndexFiles();
									AddFileService addFileService = new AddFileServiceImpl();
									
									String appendedPath = "/1/" + newPath;
									path = bundle.getString("folder.path") + appendedPath;
									String documentPath = path + "/" + fileName;
									String parentId = Constants.SNB_PARENT_ID;
									addFileService.saveFile(documentPath, file, parentId);
									
									//Save uploaded file details in DB
									SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
									String date2 = sdf2.format(new java.util.Date(new java.util.Date().getTime() - Constants.MILLIS_IN_A_DAY));
									
									KmDocumentMstr documentDTO= new KmDocumentMstr();
									documentDTO.setDocumentName(fileName);
									documentDTO.setDocumentDisplayName(file.getFileName());
									documentDTO.setDocumentDesc(file.getFileName());
									documentDTO.setApprovalStatus("A");
									documentDTO.setUploadedBy(userBean.getUserId());
									documentDTO.setUpdatedBy(userBean.getUserId());
									documentDTO.setPublishingStartDate(date2);
									documentDTO.setPublishingEndDate(date2);
									documentDTO.setDocumentPath(appendedPath);
									documentDTO.setDocName(fileName);
									documentDTO.setDocType(Constants.DOC_TYPE_SNB);
									logger.info("Document Details Set");
									
									KmElementMstr elementMstrDto= new KmElementMstr();
									elementMstrDto.setElementName(file.getFileName());
									elementMstrDto.setElementDesc(file.getFileName());
									elementMstrDto.setParentId(parentId);
									elementMstrDto.setElementLevel("0");
									elementMstrDto.setPanStatus("N");
									elementMstrDto.setStatus("A");
									elementMstrDto.setCreatedBy(userBean.getUserId());
									elementMstrDto.setUpdatedBy(userBean.getUserId());
									String elementId = elementService.createElement(elementMstrDto);
									documentDTO.setElementId(elementId);
									logger.info("Element Details Set");
									
									String docId = addFileService.saveFileInfoInDB(documentDTO);
									logger.info("documentId after saving in DB" + docId);
									
									//Deleting and Inserting Records
									boolean isPresent = iPortalDaoImpl.isPresent();
									if(isPresent){
										boolean flag3 = iPortalDaoImpl.deleteDetails();
										if(flag3){
											boolean flag4 = readExcel.getSheetForExcelUpload(iPortalDTO);
											if(flag4){
												messages.add("msg3",new ActionMessage("excel.upload.success"));
											}
										}
									}else{
										boolean flag4 = readExcel.getSheetForExcelUpload(iPortalDTO);
										if(flag4){
											messages.add("msg4",new ActionMessage("excel.upload.success"));
										}
									}
								}
							}
								else{
								messages.add("msg1",new ActionMessage("excel.xlsValidation.error"));
								}
						}
						
						saveMessages(request, messages);
					}
				}
			}
				}
			catch (Exception exp) {
				errors.add("uploadError", new ActionError(""));
				saveErrors(request, errors);
				return mapping.findForward("FAILURE");
			}	
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
				forward = mapping.findForward("FAILURE");
			}
			else {
				forward = mapping.findForward("SUCCESS");
			}
			return (forward);
		}
}
