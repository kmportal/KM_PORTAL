package com.ibm.km.actions;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;

import com.ibm.km.common.FileCopy;
import com.ibm.km.dto.BulkMsgDto;
import com.ibm.km.dto.BulkUploadDetailsDTO;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.KmSchemesAndBenefitsDetailsFormBean;
import com.ibm.km.services.impl.BulkUploadDetailsServiceImpl;

public class KmUploadSchemesAndBenefitsActionUtil
{
	private static Logger logger = Logger.getLogger(KmUploadSchemesAndBenefitsActionUtil.class.getName());
	
	/**
	 * This function validates the file.
	 */
	public boolean validateFileUpload(KmSchemesAndBenefitsDetailsFormBean formBean, KmUserMstr userBean, 
			HttpServletRequest request, ActionErrors errors, ActionMessages messages){
	{
		logger.info("Start of validateFileUpload");
		ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
		boolean isValid = true;
		try{
			FormFile file = formBean.getUploadFile();
			//Checking if file present
			if(file != null && !file.getFileName().equals("")){	
				//Checking if xls file
				String arr= (file.getFileName().toString()).substring(file.getFileName().toString().lastIndexOf(".")+1,file.getFileName().toString().length());
				if(!arr.equalsIgnoreCase("xls"))
				{
					logger.error("File not an xls file.Response send back to user.");
					messages.add("msg2",new ActionMessage("km.bulk.assignment.excel.only"));
					isValid = false;
				}
				else{
					//Checking file size
					int maximumFileSize = Integer.parseInt(bundle.getString("km.file.size"));
					if (file.getFileSize() == 0) {
						logger.error("Empty file found.Response send back to user.");
						messages.add("msg3", new ActionMessage("file.empty"));
						isValid = false;
					} 
					else if (file.getFileSize() > maximumFileSize) {
						logger.error("File size exceeds 5MB.Response send back to user.");
						messages.add("msg3", new ActionMessage("km.file.size.exceeds"));
						isValid = false;
					}
				}
			}
			else{
				logger.error("No file found.Response send back to user.");
				messages.add("msg1", new ActionMessage("km.no.file"));
				isValid = false;
			}
		}
		catch(Exception e){
			logger.error("Error in upload file validation "+ e.getMessage());
			e.printStackTrace();
		}
		logger.info("End of validateFileUpload");
		return isValid;
	}
	}
	
	/**
	 * This function performs the actual upload.
	 */
	public boolean uploadMstr(KmSchemesAndBenefitsDetailsFormBean formBean, KmUserMstr userBean, ActionErrors errors, ActionMessages messages)
			throws FileNotFoundException, KmException {
		
		logger.info("Start of uploadMstr");
		
		File newFile = null;
		BulkUploadDetailsServiceImpl serviceImpl = new BulkUploadDetailsServiceImpl();
		ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
		ArrayList<BulkUploadDetailsDTO> listPath = new ArrayList<BulkUploadDetailsDTO>();
		String uploadPath = "";
		String flag = "";
		String path = "";
		boolean result = false;
		
		try{
			FormFile myfile = formBean.getUploadFile();
			
			//Getting File Name
			String fileName = formBean.getFileName();
			
			//Getting File Data
			byte[] fileData = myfile.getFileData();
			
			//Getting path from DB
			listPath = serviceImpl.getPath();
			
			//Selecting appropriate Path
			for (Iterator<BulkUploadDetailsDTO> itr = listPath.iterator(); itr.hasNext();) {
				try {
					BulkUploadDetailsDTO dto1;
					dto1 = (BulkUploadDetailsDTO) itr.next();
					uploadPath = dto1.getBulkUploadPath();
					flag = dto1.getBulkUploadflag();
					logger.info("Getting upload Path::" + uploadPath);
					logger.info("Getting upload flag::" + flag);
		
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Error while fetching path and flag", e);
				}
			}
			
			if (flag.equalsIgnoreCase("Y")) {
				logger.info("Setting path as per DB");
				// String filePath = bundle.getString("csv.path") + "/" + fileName;
				path = uploadPath + new java.util.Date().getTime() + "_" + myfile.getFileName();
				// path= uploadPath +myfile.getFileName() ;
				logger.info("Path" + path);
			} 
			else if (flag.equalsIgnoreCase("N")) {
				logger.info("Setting path as per properties file");
				path = bundle.getString("csv.path") + "/"
						+ new java.util.Date().getTime() + "_"
						+ myfile.getFileName();
				// path= bundle.getString("csv.path")+ "/" +myfile.getFileName()
				// ;
				logger.info("Path" + path);
			} 
			else {
				logger.info("Setting path as per DB:::in else block");
				path = uploadPath + myfile.getFileName();
				logger.info("Path" + path);
			}
			
			formBean.setFilePath(path);
			
			//Creating new File
			newFile = new File(path);
			
			//Writing to file
			RandomAccessFile raf = new RandomAccessFile(newFile, "rw");
			raf.write(fileData);
			raf.close();
			result = true;
			
		}catch(Exception e){
			result = false;
			logger.error("Error in uploading file "+ e.getMessage());
			e.printStackTrace();
		}
		logger.info("End of uploadMstr");
		return result;
	}
	
	/**
	 * This function performs the create folder 
	 */
	 public void createNewFolder(String newPath , String folderPath){
		 try {
			 logger.info("newPath :: "+newPath+" folderPath:: "+folderPath);
			 String createNewPath ="1";
			 String [] path = newPath.split("/");
			 File file = new File(folderPath+'/'+newPath);
			 if(!file.exists()) {
				 for(int tempPath =0; tempPath < path.length; tempPath++) {
					 createNewPath = createNewPath+'/'+path[tempPath];
					 file = new File(folderPath+'/'+createNewPath);
					 if(!file.exists()) {
						 String newpa =folderPath+createNewPath;
						 file = new File(newpa);
						 file.mkdir();
						 logger.info("Folder Created Successfully");
					 }
					 
				 }
			 }
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		 
	 }
	 
}
