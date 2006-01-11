/*
 * Created on Mar 19, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.ibm.km.dto.BulkUploadDetailsDTO;
import com.ibm.km.exception.KmException;

/**
 * @author Pramod
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class KmDisplayFile {
	
	private static final Logger logger;

	static {

		logger = Logger.getLogger(KmDisplayFile.class);
	}
	
	public String displayFile(String displayFilePath,String tempFilePath)throws KmException
	{
		
		File file = new File(displayFilePath);
		logger.info("File Path :" + displayFilePath);
		logger.info("File name : " + file.getName());
		logger.info("Input File Size " + file.length());
		logger.info("temp File Path :" + tempFilePath);
		InputStream in=null;
		if(file.length()==0)
		{
			return null;
		}
		
		
		File f=new File(tempFilePath+file.getName());
		if(f.exists())
		{
			f.delete();
			/*tempFilePath =  "/Docs/" + file.getName();
			return tempFilePath;*/
		}
		try
		{
		logger.info("inside try");
		in = new FileInputStream(file);
		byte[] fileData = new byte[in.available()];
		in.read(fileData);
					
		
		RandomAccessFile rafwriter = new RandomAccessFile(f,"rw");
		rafwriter.write(fileData);
		rafwriter.close(); 
		
		if(f.length()==0)
		{
			return null;
		}
		
		logger.info("File Length is " + f.length());
		
		//----Added by Saanya to remove docs folder----//
		//tempFilePath =  "/Docs/" + displayFilePath;
		//tempFilePath =  "/Docs/" + f.getName();
		
		//tempFilePath =  "C:\\kb_files1\\Docs\\" + f.getName();;
		
		try{
		ArrayList<BulkUploadDetailsDTO> listPath = new ArrayList<BulkUploadDetailsDTO>();
		DocumentPathUtil documentPathUtil = new DocumentPathUtil();
		String uploadPath = "";
		String flag = "";
		
		listPath = documentPathUtil.getPath();
		for (Iterator<BulkUploadDetailsDTO> itr = listPath.iterator(); itr.hasNext();) {
			try {
				BulkUploadDetailsDTO dto1;
				dto1 = (BulkUploadDetailsDTO) itr.next();
				uploadPath = dto1.getBulkUploadPath();
				flag = dto1.getBulkUploadflag();
				logger.info("Getting Docs folder Path::" + uploadPath);
				logger.info("Getting Docs folder flag::" + flag);
	
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Error while fetching path and flag", e);
			}
		}
		
		if(flag != null && !flag.equals("") && flag.equals("1")){
			tempFilePath = uploadPath;
		}else if(flag != null && !flag.equals("") && flag.equals("0")){
			tempFilePath = "/Docs/";
		}
		
		//tempFilePath =   tempFilePath + displayFilePath;
		tempFilePath =   tempFilePath + f.getName();
		}catch (Exception e){
			
		}
		//----End of Added by Saanya to remove docs folder----//
		
		logger.info("Final oputput path is  " + tempFilePath);
		}
		catch(IOException io)
		{
				io.printStackTrace();
				throw new KmException("IOException occurs at displayFile-ApproveFileServiceImpl",io);	
		}
		finally{
			if(in!=null){
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	//	formBean.setFilePath("/test/Application Information Document -  NMS_V1.1.doc");
	//	logger.info("File Path is " + path);
	//	formBean.setShowFile("true");
			
		return tempFilePath;	 
	}

}
