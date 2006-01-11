
package com.ibm.km.services.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.ibm.km.common.Constants;
import com.ibm.km.common.ContentValidator;
import com.ibm.km.common.Utility;
import com.ibm.km.dto.KmCsrLatestUpdatesDto;
import com.ibm.km.dto.KmDocumentMstr;
import com.ibm.km.dto.KmElementMstr;
import com.ibm.km.dto.ProductDetails;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.KmSopUploadFormBean;
import com.ibm.km.services.KmElementMstrService;
import com.ibm.km.services.KmSopUploadService;

public class KmSopUploadServiceImpl implements KmSopUploadService{
	
	private static final Logger logger;

	static {

		logger = Logger.getLogger(KmProductUploadServiceImpl.class);
	}
	
	public KmDocumentMstr saveProductDetails(KmSopUploadFormBean kmSopUploadFormBean)throws KmException{
		
		KmDocumentMstr documentDTO=new KmDocumentMstr();
		// DTO  for latest updates
		KmCsrLatestUpdatesDto kmCsrLatestUpdatesDto = new KmCsrLatestUpdatesDto();
		StringBuilder updateDesc = new StringBuilder();
		
		String documentPath =kmSopUploadFormBean.getElementFolderPath();
		logger.info("documentPath 55555555yyy"+documentPath);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH-mm-ss-S");
		String date = sdf.format(new java.util.Date());
		StringBuilder sb = new StringBuilder();
		StringBuilder plainTextStringBuffer = new StringBuilder();
		StringBuilder plainTextTempStringBuffer = new StringBuilder();
		String fileName = "";
		ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
		String folderPath = bundle.getString("folder.path");
		try{
			
			// Compiling XML content for all circle
			int headers = kmSopUploadFormBean.getHeader().length;

			sb.append("<SOP>\n");
			sb.append("<TOPIC>"+Utility.encodeContent(kmSopUploadFormBean.getTopic().trim())+"</TOPIC>\n");
			sb.append("<PUBLISHDATE>"+kmSopUploadFormBean.getPublishDt()+"</PUBLISHDATE>\n");
			sb.append("<ENDDATE>"+kmSopUploadFormBean.getEndDt()+"</ENDDATE>\n");
			
			
			kmCsrLatestUpdatesDto.setUpdateTitle(kmSopUploadFormBean.getTopic());
			
			kmCsrLatestUpdatesDto.setActivationDate(kmSopUploadFormBean.getPublishDt());
			kmCsrLatestUpdatesDto.setExpiryDate(kmSopUploadFormBean.getEndDt());			
			String documentPathArr[] = documentPath.split("/");
			//added by vishwas for lob wise
			System.out.println("for check lenth"+documentPathArr.length+"mmm"+documentPathArr[0]);
			if(documentPathArr.length==2)
			{
			kmCsrLatestUpdatesDto.setLob(documentPathArr[1]);		
			kmCsrLatestUpdatesDto.setLogiclob("lob");
			}
			else
			{
				kmCsrLatestUpdatesDto.setLob(documentPathArr[1]);
				kmCsrLatestUpdatesDto.setCircleId(documentPathArr[2]); 
				kmCsrLatestUpdatesDto.setCategory(documentPathArr[3]);
			}
			
			
			sb.append("<HEADERS>\n");
			StringBuilder sbContent= new StringBuilder();
			int rowCount=0;
			for (int i = 0; i < headers; i++) {
				if( !"".equals(kmSopUploadFormBean.getHeader()[i].toString().trim()) || !"".equals(kmSopUploadFormBean.getContent()[i].toString().trim()))
				{
				sb.append("\t<HEADER>"+Utility.encodeContent(kmSopUploadFormBean.getHeader()[i].toString().trim())+"</HEADER>\n");
				sbContent.append("\t<CONTENT>"+Utility.encodeContent(kmSopUploadFormBean.getContent()[i].toString().trim()).replaceAll("&nbsp;"," ")+"</CONTENT>\n");

				plainTextTempStringBuffer.append("\t<CONTENT>"+Utility.encodeContent(kmSopUploadFormBean.getPlainContent()[i].toString().trim()).replaceAll("&nbsp;"," ")+"</CONTENT>\n");
				
				updateDesc.append(kmSopUploadFormBean.getHeader()[i].toString().trim()+ " ");
				updateDesc.append(kmSopUploadFormBean.getPlainContent()[i].toString().trim()+ " ");
				rowCount++;
				}
			}
			while(rowCount < 5) {
				sb.append("\t<HEADER></HEADER>\n");
				sbContent.append("\t<CONTENT></CONTENT>\n");
				rowCount++;
			}
			
			sb.append("</HEADERS>\n");
			sb.append("<CONTENTS>\n");

			plainTextStringBuffer.append(sb);	

			plainTextStringBuffer.append(plainTextTempStringBuffer);	
			plainTextStringBuffer.append("</CONTENTS>\n");	
			
			sb.append(sbContent);
			sb.append("</CONTENTS>\n");
			
			kmCsrLatestUpdatesDto.setUpdateDesc(updateDesc.toString());
			
			StringBuffer sbPathDesc = new StringBuffer();
			// Adding SOP{ links if available 
			if(null != kmSopUploadFormBean.getProductHeaders())
			{
				int productHeaders = kmSopUploadFormBean.getProductHeaders().length;
				
				plainTextStringBuffer.append("<PRODUCT_HEADERS>\n");

				sb.append("<PRODUCT_HEADERS>\n");
				sbContent= new StringBuilder();
				for (int i = 0; i < productHeaders; i++) {
	
					plainTextStringBuffer.append("\t<PRODUCT_HEADER>"+kmSopUploadFormBean.getProductHeaders()[i].toString().trim()+"</PRODUCT_HEADER>\n");

					sb.append("\t<PRODUCT_HEADER>"+kmSopUploadFormBean.getProductHeaders()[i].toString().trim()+"</PRODUCT_HEADER>\n");
					sbContent.append("\t<PRODUCT_PATH>"+kmSopUploadFormBean.getProductPaths()[i].toString().trim()+"</PRODUCT_PATH>\n");
					sbPathDesc.append("\t<PATH_DESC>"+kmSopUploadFormBean.getProductPathsLabels()[i].toString().trim()+"</PATH_DESC>\n");
				}
				plainTextStringBuffer.append("</PRODUCT_HEADERS>\n");
				plainTextStringBuffer.append("<PRODUCT_PATHS>\n");
				plainTextStringBuffer.append(sbContent);
				plainTextStringBuffer.append(sbPathDesc);
				plainTextStringBuffer.append("</PRODUCT_PATHS>\n");

				sb.append("</PRODUCT_HEADERS>\n");
				sb.append("<PRODUCT_PATHS>\n");
				sb.append(sbContent);
				sb.append(sbPathDesc);
				sb.append("</PRODUCT_PATHS>\n");
			}
			plainTextStringBuffer.append("\t<IMAGES>\n");
			sb.append("\t<IMAGES>\n");
			logger.info("getDisplayedImagePath :: "+kmSopUploadFormBean.getDisplayedImagePath());
			// Writing existing images...
			if(null != kmSopUploadFormBean.getDisplayedImagePath()){
			 for (int i = 0; i < kmSopUploadFormBean.getDisplayedImagePath().length; i++) {	
				 plainTextStringBuffer.append("\t\t<IMAGE>\n"); 	
				 plainTextStringBuffer.append("\t\t\t<IMAGETITLE>"+Utility.encodeContent(kmSopUploadFormBean.getDisplayedImageTitle()[i].trim())+"</IMAGETITLE>\n");
				 plainTextStringBuffer.append("\t\t\t<IMAGEPATH>"+kmSopUploadFormBean.getDisplayedImagePath()[i].trim()+"</IMAGEPATH>\n");
				 plainTextStringBuffer.append("\t\t</IMAGE>\n"); 	
				
				    sb.append("\t\t<IMAGE>\n"); 	
				    sb.append("\t\t\t<IMAGETITLE>"+Utility.encodeContent(kmSopUploadFormBean.getDisplayedImageTitle()[i].trim())+"</IMAGETITLE>\n");
				    sb.append("\t\t\t<IMAGEPATH>"+kmSopUploadFormBean.getDisplayedImagePath()[i].trim()+"</IMAGEPATH>\n");
				    sb.append("\t\t</IMAGE>\n"); 	
			 }
			}
			// Writing new uploaded Images...
			logger.info("File Size :: "+kmSopUploadFormBean.getProductImageList().size());
			for (int i = 0; i < kmSopUploadFormBean.getProductImageList().size(); i++) {
				ProductDetails productDetailsDTO = kmSopUploadFormBean.getProductImageList().get(i);   
				if (null != productDetailsDTO.getImageFile()){	
					if( !"".equals(productDetailsDTO.getImageFile().getFileName())){
						logger.info("Writing new uploaded Images...");
						fileName = productDetailsDTO.getImageFile().getFileName();
						int fileSize = productDetailsDTO.getImageFile().getFileSize();	
						boolean imageTobeUplopaded = true;
						if (  !(fileName.toUpperCase().contains(".JPG") || fileName.toUpperCase().contains(".JPEG") || fileName.toUpperCase().contains(".GIF"))){
								
									imageTobeUplopaded = false;
						}		
						if ( fileSize > 2048576 ){
							imageTobeUplopaded = false;
						}
						if(imageTobeUplopaded){					
					    fileName = folderPath+documentPath+date+i+fileName;  
					    plainTextStringBuffer.append("\t\t<IMAGE>\n"); 	
					    plainTextStringBuffer.append("\t\t\t<IMAGETITLE>"+productDetailsDTO.getImageTitle().trim()+"</IMAGETITLE>\n");
					    plainTextStringBuffer.append("\t\t\t<IMAGEPATH>"+fileName+"</IMAGEPATH>\n");
					    plainTextStringBuffer.append("\t\t</IMAGE>\n"); 	
					    sb.append("\t\t<IMAGE>\n"); 	
					    sb.append("\t\t\t<IMAGETITLE>"+productDetailsDTO.getImageTitle().trim()+"</IMAGETITLE>\n");
					    sb.append("\t\t\t<IMAGEPATH>"+fileName+"</IMAGEPATH>\n");
					    sb.append("\t\t</IMAGE>\n"); 	
						// Write image to file 
						File file = new File(fileName);
						file.delete();
						byte[] fileData = productDetailsDTO.getImageFile().getFileData();
						RandomAccessFile raf = new RandomAccessFile(file, "rw");
						raf.write(fileData);
						raf.close();
						}
					}
				  }
			}
			plainTextStringBuffer.append("\t</IMAGES>\n");
			plainTextStringBuffer.append("</SOP>\n");
			sb.append("\t</IMAGES>\n");
			sb.append("</SOP>\n");
			logger.info("XML content : "+sb);
			
			logger.info("Writing File to : "+kmSopUploadFormBean.getXmlFileName());
		
				// write xml file with html content
				FileWriter fstream = new FileWriter(kmSopUploadFormBean.getXmlFileName());
				BufferedWriter out = new BufferedWriter(fstream);
	  		    out.write(sb.toString());
			    out.close();
			    
	            // write xml file with plain text
				fstream = new FileWriter(kmSopUploadFormBean.getXmlFileNameContentPlainText());
				out = new BufferedWriter(fstream);
	  		    out.write(plainTextStringBuffer.toString());
			    out.close();

			    boolean isValidFile = new ContentValidator().validate(kmSopUploadFormBean.getXmlFileName());
			    
			    if(!isValidFile)
			    {
			    	documentDTO.setValidFile(isValidFile);
			    	return documentDTO;
			    }
			    
			String xmlFileName = kmSopUploadFormBean.getXmlFileName().substring(kmSopUploadFormBean.getXmlFileName().lastIndexOf("/")+1);
			logger.info("XML file Name : "+xmlFileName);
			
		    documentDTO.setDocumentName(xmlFileName);
		    documentDTO.setDocName(xmlFileName);
		    documentDTO.setDocumentDisplayName(kmSopUploadFormBean.getTopic());
		    documentDTO.setDocumentDesc(kmSopUploadFormBean.getTopic());
		    documentDTO.setKeyword(kmSopUploadFormBean.getTopic());
			documentDTO.setPublishingStartDate(kmSopUploadFormBean.getPublishDt());
			documentDTO.setPublishingEndDate(kmSopUploadFormBean.getEndDt());
			
			documentDTO.setUserId(kmSopUploadFormBean.getCreatedBy());
			documentDTO.setApprovalStatus("A");
		    documentDTO.setUpdatedBy(kmSopUploadFormBean.getCreatedBy());
		    
		    logger.info(documentPath+"XML file Name document path : "+documentPath.substring(0,documentPath.lastIndexOf("/")));
		   
			if(documentPathArr.length!=2)
			{
				 documentPath = documentPath.substring(0,documentPath.lastIndexOf("/"));
			}
		   
		    
		    documentDTO.setDocumentPath(documentPath);
		    
		    KmElementMstr elementMstrDto= new KmElementMstr();
			elementMstrDto.setElementDesc(kmSopUploadFormBean.getTopic().trim());
			if(documentPathArr.length==2)
			{
				elementMstrDto.setPanStatus("Y");	
			elementMstrDto.setLobStatus("Y");
			}
			else
			{
				elementMstrDto.setPanStatus("N");
			}
			
			
			
			String parentId = documentPath.substring(documentPath.lastIndexOf("/")+1);
			
			elementMstrDto.setParentId(parentId);
			//elementMstrDto.setPanStatus("N");
			elementMstrDto.setElementLevel("0");
			elementMstrDto.setStatus("A");
			elementMstrDto.setElementName(kmSopUploadFormBean.getTopic().trim());
			documentDTO.setDocType(Constants.DOC_TYPE_SOP);
			elementMstrDto.setCreatedBy(kmSopUploadFormBean.getCreatedBy());
			elementMstrDto.setUpdatedBy(kmSopUploadFormBean.getCreatedBy());
			
			KmElementMstrService elementService=new KmElementMstrServiceImpl();
		    String elementId=elementService.createElement(elementMstrDto);
			documentDTO.setElementId(elementId);
			documentDTO.setKmCsrLatestUpdatesDto(kmCsrLatestUpdatesDto);
		}catch(Exception ee)
		{
			ee.printStackTrace();	
			throw new KmException(ee.getMessage());
		}
		return documentDTO;				
	}	


	public KmSopUploadFormBean viewProductDetails(KmSopUploadFormBean kmSopUploadFormBean)throws KmException{

		try{
			  String documentPath =kmSopUploadFormBean.getXmlFileName();
			  ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
			  String folderPath = bundle.getString("folder.path");
			  if(!documentPath.contains(folderPath))
			  {
				  documentPath = folderPath+documentPath; 
				
			  }
			  kmSopUploadFormBean.setXmlFileName(documentPath);
			  logger.info("File to be viewed Name  : " +documentPath);
			  FileInputStream fstream = new FileInputStream(documentPath);
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  StringBuffer sb = new StringBuffer();
			  String strLine="";
				
			  while ((strLine = br.readLine()) != null)   {
				  sb.append(strLine);
			  }
			  in.close();
			  
			    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				InputSource is = new InputSource();
				
				is.setCharacterStream(new StringReader(sb.toString()));
				Document doc = db.parse(is);                
				doc.getDocumentElement().normalize();
				NodeList nodeLst = null;
				NodeList nodeLst2 = null;
				NodeList nodeLst3 = null;
				
				kmSopUploadFormBean.setTopic(Utility.decodeContent(doc.getElementsByTagName("TOPIC").item(0).getTextContent()));
				kmSopUploadFormBean.setPublishDt(doc.getElementsByTagName("PUBLISHDATE").item(0).getTextContent());
				kmSopUploadFormBean.setEndDt(doc.getElementsByTagName("ENDDATE").item(0).getTextContent());
				
				nodeLst =  doc.getElementsByTagName("HEADER");
				nodeLst2 = doc.getElementsByTagName("CONTENT");
								
				int headerLength = nodeLst.getLength();
				String header[] = new String[headerLength];
				String content[] = new String[headerLength];

				for (int s = 0; s < headerLength; s++) {
					header[s] = Utility.decodeContent(nodeLst.item(s).getTextContent());
					content[s] = Utility.decodeContent(nodeLst2.item(s).getTextContent());
				}
				
				kmSopUploadFormBean.setContent(content);
				kmSopUploadFormBean.setHeader(header);
				
				nodeLst = null;
				nodeLst2 = null;
				nodeLst3 = null;
				nodeLst =  doc.getElementsByTagName("PRODUCT_HEADER");
				nodeLst2 = doc.getElementsByTagName("PRODUCT_PATH");
				nodeLst3 = doc.getElementsByTagName("PATH_DESC");
								
				int productHeaderLength = nodeLst.getLength();
				
				String pathHeader[] = new String[productHeaderLength];
				String pathLink[] = new String[productHeaderLength];
				String pathDesc[] = new String[productHeaderLength];
				
				for (int s = 0; s < productHeaderLength; s++) {
					pathHeader[s] = Utility.decodeContent(nodeLst.item(s).getTextContent());
					pathLink[s] = nodeLst2.item(s).getTextContent();
					pathDesc[s] = nodeLst3.item(s).getTextContent();
				}
				kmSopUploadFormBean.setProductHeaders(pathHeader);
				kmSopUploadFormBean.setProductPaths(pathLink);
				kmSopUploadFormBean.setProductPathsLabels(pathDesc);
				
				nodeLst = null;
				nodeLst2 = null;
				
				nodeLst = doc.getElementsByTagName("IMAGETITLE");
				nodeLst2 = doc.getElementsByTagName("IMAGEPATH");
				
				if (null != nodeLst)
				{
					headerLength = nodeLst.getLength();
					String imageTitle[] = new String[headerLength];
					String imagePath[] = new String[headerLength];
					
					for (int s = 0; s < headerLength; s++) {
						imageTitle[s] = Utility.decodeContent(nodeLst.item(s).getTextContent());
						imagePath[s] =  nodeLst2.item(s).getTextContent();	
					}
					kmSopUploadFormBean.setImageTitle(imageTitle);
					kmSopUploadFormBean.setImageName(imagePath);				
				}
				
		    
		}catch(Exception ee)
		{
			ee.printStackTrace();		
			throw new KmException(ee.getMessage());
		}
	   return kmSopUploadFormBean;				
	}	
}
