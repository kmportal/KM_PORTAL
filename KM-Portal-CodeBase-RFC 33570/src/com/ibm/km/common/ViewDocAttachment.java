package com.ibm.km.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


public class ViewDocAttachment extends Action {
	
	private static Logger logger = Logger.getLogger(ViewDocAttachment.class.getName());

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		try{
			String filePath = request.getParameter("filepath");
			logger.info("Attachment path from lookup : " + filePath);
			
			File f = new File(filePath);
			String fileName = f.getName();
			
			String fileType = fileName.substring(fileName.indexOf(".")+1,fileName.length());
			if (fileType.trim().equalsIgnoreCase("txt")){
			response.setContentType( "text/plain" );
			}else if (fileType.trim().equalsIgnoreCase("doc") || fileType.trim().equalsIgnoreCase("docx")) {
			response.setContentType( "application/msword" );
			}else if (fileType.trim().equalsIgnoreCase("xls") || fileType.trim().equalsIgnoreCase("xlsx")) {
			response.setContentType( "application/vnd.ms-excel" );
			}else if (fileType.trim().equalsIgnoreCase("pdf")) {
			response.setContentType( "application/pdf" );
			}else if (fileType.trim().equalsIgnoreCase("html")) {
			response.setContentType( "text/html" );
			}
			else {
			response.setContentType( "application/octet-stream" );
			}
			response.setContentLength((int)f.length());
			
			String name = fileName;
			if(name.contains(" ")){
				name = name.replaceAll(" ", "_");
			}
			
			/*if(name.contains("_")){
				String fname =name.substring(0,name.lastIndexOf("_"));
				String ext = name.substring(name.lastIndexOf("."),name.length());
				name = fname + ext;
			}*/
			
			response.setHeader("Pragma", "public");
			response.setHeader("Content-Disposition","inline; filename=" + name);
			response.setHeader("Cache-Control", "max-age=0"); 
			byte[] buf = new byte[8192];
			FileInputStream inStream = new FileInputStream(f);
			OutputStream outStream=response.getOutputStream();
			int sizeRead = 0;
			while ((sizeRead = inStream.read(buf, 0, buf.length)) > 0) {
			
			outStream.write(buf, 0, sizeRead);
			}
			outStream.close();
			inStream.close();
			logger.info("Attachment outputed ");
			
			}catch(Exception e){
				e.printStackTrace();
			}
			ActionForward forward = mapping.findForward("SUCCESS");
			return forward;
	}
}