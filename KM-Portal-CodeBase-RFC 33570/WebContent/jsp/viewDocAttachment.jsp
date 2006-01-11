<%@ page language="java" contentType="text/xls; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="java.io.OutputStream"%>
<%@page import="java.io.File"%>
<%@page import="java.io.FileInputStream"%>
<html>
<head>
		<%
				try{
				String filePath = request.getParameter("filepath");
				System.out.println("attachment path from lookup " + filePath);
				
				File f = new File(filePath);
				String fileName = f.getName();
				
				System.out.println("-----1----");
				String fileType = fileName.substring(fileName.indexOf(".")+1,fileName.length());
				if (fileType.trim().equalsIgnoreCase("txt")){
				response.setContentType( "text/plain" );
				}else if (fileType.trim().equalsIgnoreCase("doc")) {
				response.setContentType( "application/msword" );
				}else if (fileType.trim().equalsIgnoreCase("xls")) {
				response.setContentType( "application/vnd.ms-excel" );
				}
				else if (fileType.trim().equalsIgnoreCase("pdf")) {
				response.setContentType( "application/pdf" );
				}else {
				response.setContentType( "application/octet-stream" );
				}
				response.setContentLength((int)f.length());
				String name=fileName.substring(1,fileName.length());
				if(name.contains("_")){
				String fname =name.substring(0,name.lastIndexOf("_"));
				String ext = name.substring(name.lastIndexOf("."),name.length());
				name=fname+ext;
				
				}
				
				System.out.println("-----2----");
				response.setHeader("Pragma", "public");
				response.setHeader("Content-Disposition","attachment; filename="+name);
				response.setHeader("Cache-Control", "max-age=0"); 
				byte[] buf = new byte[8192];
				FileInputStream inStream = new FileInputStream(f);
				pageContext.getOut().clear();				
				OutputStream outStream=response.getOutputStream();
				int sizeRead = 0;
				while ((sizeRead = inStream.read(buf, 0, buf.length)) > 0) {
				
				outStream.write(buf, 0, sizeRead);
				}
				outStream.close();
				inStream.close();
				return;
				}catch(Exception e){}
		%>
</html>