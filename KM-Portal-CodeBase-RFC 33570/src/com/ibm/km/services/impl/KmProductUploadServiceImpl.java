
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.sql.rowset.serial.SerialBlob;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.ibm.km.common.Constants;
import com.ibm.km.common.ContentValidator;
import com.ibm.km.common.Utility;
import com.ibm.km.dao.KmDocumentMstrDao;
import com.ibm.km.dao.impl.KmDocumentMstrDaoImpl;
import com.ibm.km.dto.KmCsrLatestUpdatesDto;
import com.ibm.km.dto.KmDocumentMstr;
import com.ibm.km.dto.KmElementMstr;
import com.ibm.km.dto.ProductDetails;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.KmProductUploadFormBean;
import com.ibm.km.services.KmCsrLatestUpdatesService;
import com.ibm.km.services.KmElementMstrService;
import com.ibm.km.services.KmProductUploadService;

public class KmProductUploadServiceImpl implements KmProductUploadService{
	
	private static final Logger logger;

	static {

		logger = Logger.getLogger(KmProductUploadServiceImpl.class);
	}
	
	public KmDocumentMstr saveProductDetails(KmProductUploadFormBean kmProductUploadFormBean)throws KmException{
		String message="";
		
		// DTO for document update
		KmDocumentMstr documentDTO=new KmDocumentMstr();
		
		// DTO  for latest updates
		KmCsrLatestUpdatesDto kmCsrLatestUpdatesDto = new KmCsrLatestUpdatesDto();
		StringBuilder updateDesc = new StringBuilder();
		String documentPath =kmProductUploadFormBean.getElementFolderPath();
		 
		ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
		String folderPath = bundle.getString("folder.path");
		logger.info("documentPath "+documentPath);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH-mm-ss-S");
		String date = sdf.format(new java.util.Date());
		StringBuilder sb = new StringBuilder();
		StringBuilder plainTextStringBuffer = new StringBuilder();
		
		int imageSizeMax = Integer.parseInt(bundle.getString("product.upload.image.size"));
		int headers = kmProductUploadFormBean.getHeader().length;		
		String fileName = "";
	
		if(!"".equals(message.trim()))
		{
		message = message+" image size should be below "+imageSizeMax+" byte.";
		kmProductUploadFormBean.setMessage(message);
		}
		else
		{
		try{
			
			plainTextStringBuffer.append("<PRODUCT>\n");
			plainTextStringBuffer.append("\t<TOPIC>"+Utility.encodeContent(kmProductUploadFormBean.getTopic().trim())+"</TOPIC>\n");
			plainTextStringBuffer.append("\t<PUBLISHDATE>"+kmProductUploadFormBean.getPublishDt()+"</PUBLISHDATE>\n");
			plainTextStringBuffer.append("\t<ENDDATE>"+kmProductUploadFormBean.getEndDt()+"</ENDDATE>\n");
			
			sb.append("<PRODUCT>\n");
			sb.append("\t<TOPIC>"+Utility.encodeContent(kmProductUploadFormBean.getTopic().trim())+"</TOPIC>\n");
			sb.append("\t<PUBLISHDATE>"+kmProductUploadFormBean.getPublishDt()+"</PUBLISHDATE>\n");
			sb.append("\t<ENDDATE>"+kmProductUploadFormBean.getEndDt()+"</ENDDATE>\n");
			
			kmCsrLatestUpdatesDto.setUpdateTitle(kmProductUploadFormBean.getTopic());
			kmCsrLatestUpdatesDto.setActivationDate(kmProductUploadFormBean.getPublishDt());
			kmCsrLatestUpdatesDto.setExpiryDate(kmProductUploadFormBean.getEndDt());			
			String documentPathArr[] = documentPath.split("/");
			kmCsrLatestUpdatesDto.setLob(documentPathArr[1]);		
			kmCsrLatestUpdatesDto.setCircleId(documentPathArr[2]); 
			kmCsrLatestUpdatesDto.setCategory(documentPathArr[3]); 
			
			plainTextStringBuffer.append("\t<DATA>\n");

			sb.append("\t<DATA>\n");
			
			int rowCount=0;
			for (int i = 0; i < headers; i++) {            
				if( !"".equals(kmProductUploadFormBean.getHeader()[i].toString().trim()) ||  !"".equals(kmProductUploadFormBean.getContent()[i].toString().trim()))
				{
					
					plainTextStringBuffer.append("\t\t<PRODUCTDATA>\n");
					plainTextStringBuffer.append("\t\t\t<HEADER>"+Utility.encodeContent(kmProductUploadFormBean.getHeader()[i].toString().trim())+"</HEADER>\n");
					plainTextStringBuffer.append("\t\t\t<CONTENT>"+Utility.encodeContent(kmProductUploadFormBean.getPlainContent()[i].toString().trim()).replaceAll("&nbsp;"," ")+"</CONTENT>\n");
					plainTextStringBuffer.append("\t\t</PRODUCTDATA>\n");
					
					sb.append("\t\t<PRODUCTDATA>\n");
					sb.append("\t\t\t<HEADER>"+Utility.encodeContent(kmProductUploadFormBean.getHeader()[i].toString().trim())+"</HEADER>\n");
					sb.append("\t\t\t<CONTENT>"+Utility.encodeContent(kmProductUploadFormBean.getContent()[i].toString().trim()).replaceAll("&nbsp;"," ")+"</CONTENT>\n");
					sb.append("\t\t</PRODUCTDATA>\n");
					
					updateDesc.append(kmProductUploadFormBean.getHeader()[i].toString().trim()+ ": ");
					updateDesc.append(kmProductUploadFormBean.getPlainContent()[i].toString().trim()+ " ");
					rowCount++;
				}
			}
			while(rowCount < 5) {

				plainTextStringBuffer.append("\t\t<PRODUCTDATA>\n");
				plainTextStringBuffer.append("\t\t\t<HEADER></HEADER>\n");
				plainTextStringBuffer.append("\t\t\t<CONTENT></CONTENT>\n");
				plainTextStringBuffer.append("\t\t</PRODUCTDATA>\n");
				
				sb.append("\t\t<PRODUCTDATA>\n");
				sb.append("\t\t\t<HEADER></HEADER>\n");
				sb.append("\t\t\t<CONTENT></CONTENT>\n");
				sb.append("\t\t</PRODUCTDATA>\n");
				rowCount++;
			}
			plainTextStringBuffer.append("\t</DATA>\n");

			sb.append("\t</DATA>\n");
			
			
			kmCsrLatestUpdatesDto.setUpdateDesc(updateDesc.toString());
			
			plainTextStringBuffer.append("\t<IMAGES>\n");
			
			sb.append("\t<IMAGES>\n");
			
			// Writing existing images...
			if(null != kmProductUploadFormBean.getDisplayedImagePath())
			{
			 for (int i = 0; i < kmProductUploadFormBean.getDisplayedImagePath().length; i++) {	
				 plainTextStringBuffer.append("\t\t<IMAGE>\n"); 	
				 plainTextStringBuffer.append("\t\t\t<IMAGETITLE>"+Utility.encodeContent(kmProductUploadFormBean.getDisplayedImageTitle()[i].trim())+"</IMAGETITLE>\n");
				 plainTextStringBuffer.append("\t\t\t<IMAGEPATH>"+kmProductUploadFormBean.getDisplayedImagePath()[i].trim()+"</IMAGEPATH>\n");
				 plainTextStringBuffer.append("\t\t</IMAGE>\n"); 	
				
				    sb.append("\t\t<IMAGE>\n"); 	
				    sb.append("\t\t\t<IMAGETITLE>"+Utility.encodeContent(kmProductUploadFormBean.getDisplayedImageTitle()[i].trim())+"</IMAGETITLE>\n");
				    sb.append("\t\t\t<IMAGEPATH>"+kmProductUploadFormBean.getDisplayedImagePath()[i].trim()+"</IMAGEPATH>\n");
				    sb.append("\t\t</IMAGE>\n"); 	
			 }
			}
			
			// Writing new uploaded Images...
			for (int i = 0; i < kmProductUploadFormBean.getProductImageList().size(); i++) {
				 
				ProductDetails productDetailsDTO = kmProductUploadFormBean.getProductImageList().get(i);   
				
				if (null != productDetailsDTO.getImageFile()) 
				  {	
					if( !"".equals(productDetailsDTO.getImageFile().getFileName()))
					{
						////System.out.println("Writing new uploaded Images...");
						fileName = productDetailsDTO.getImageFile().getFileName();
						int fileSize = productDetailsDTO.getImageFile().getFileSize();	
						
						boolean imageTobeUplopaded = true;
						if (  !fileName.toUpperCase().contains(".JPG") && !fileName.toUpperCase().contains(".JPEG"))
							{
								if ( !fileName.toUpperCase().contains(".GIF") )
								{
									logger.info("not an image file");
									imageTobeUplopaded = false;
								}
						}		
						logger.info(fileSize)	;			
						if ( fileSize > 2048576 )
						{
							imageTobeUplopaded = false;
						}
						if(imageTobeUplopaded)
						{					
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
						logger.info("Image FILE PATH IS : "+fileName);
						byte[] fileData = productDetailsDTO.getImageFile().getFileData();
						logger.info("image file data length : "+fileData.length);
						
						RandomAccessFile raf = new RandomAccessFile(file, "rw");
						raf.write(fileData);
						raf.close();
						}
					}
				  }
			}
			
			plainTextStringBuffer.append("\t</IMAGES>\n");
			plainTextStringBuffer.append("</PRODUCT>\n");
			
			sb.append("\t</IMAGES>\n");
			sb.append("</PRODUCT>\n");
			
			////System.out.println("XML content : "+sb);
			
			// Write image to file 
			String xmlFileName = kmProductUploadFormBean.getXmlFileName();
				
			logger.info("XML FILE PATH IS : "+xmlFileName);
           
			// write xml file containing html content
			FileWriter fstream = new FileWriter(xmlFileName);
		    BufferedWriter out = new BufferedWriter(fstream);
  		    out.write(sb.toString());
		    out.close();
		    
		    // write xml file containing plain text content - used for searching and indexing
		    String plainXMLFileName=kmProductUploadFormBean.getXmlFileNameContentPlainText();
			fstream = new FileWriter(plainXMLFileName);
		    out = new BufferedWriter(fstream);
  		    out.write(plainTextStringBuffer.toString());
		    out.close();

		    boolean isValidFile = new ContentValidator().validate(plainXMLFileName);
		    
		    if(!isValidFile)
		    {
		    	documentDTO.setValidFile(isValidFile);
		    	return documentDTO;
		    }
		
		    xmlFileName = kmProductUploadFormBean.getXmlFileName().substring(kmProductUploadFormBean.getXmlFileName().lastIndexOf("/")+1);
			logger.info("XML file Name : "+xmlFileName);
			
		    documentDTO.setDocumentName(xmlFileName);
		    documentDTO.setDocName(xmlFileName);
		    documentDTO.setDocumentDisplayName(kmProductUploadFormBean.getTopic());
		    documentDTO.setDocumentDesc(kmProductUploadFormBean.getTopic());
		    documentDTO.setKeyword(kmProductUploadFormBean.getTopic());
		    documentDTO.setPublishingStartDate(kmProductUploadFormBean.getPublishDt());
		    documentDTO.setPublishingEndDate(kmProductUploadFormBean.getEndDt());
			documentDTO.setUserId(kmProductUploadFormBean.getCreatedBy());
			documentDTO.setApprovalStatus("A");
		    documentDTO.setUpdatedBy(kmProductUploadFormBean.getCreatedBy());
		    
		    documentPath = documentPath.substring(0,documentPath.lastIndexOf("/"));
		    
		    documentDTO.setDocumentPath(documentPath);
		    documentDTO.setDocType(Constants.DOC_TYPE_PRODUCT);
		    
		    KmElementMstr elementMstrDto= new KmElementMstr();
			elementMstrDto.setElementDesc(kmProductUploadFormBean.getTopic());
			
			
			
			String parentId = documentPath.substring(documentPath.lastIndexOf("/")+1);
			////System.out.println("parent Id : "+documentPath.substring(documentPath.lastIndexOf("/")+1));
			elementMstrDto.setParentId(parentId);
			elementMstrDto.setPanStatus("N");
			elementMstrDto.setElementLevel("0");
			elementMstrDto.setStatus("A");
			
			//KM Phase 2
			elementMstrDto.setElementName(kmProductUploadFormBean.getTopic());
			//elementMstrDTO.setElementName(file.getFileName());
			elementMstrDto.setCreatedBy(kmProductUploadFormBean.getCreatedBy());
			elementMstrDto.setUpdatedBy(kmProductUploadFormBean.getCreatedBy());
			
			KmElementMstrService elementService=new KmElementMstrServiceImpl();
		    String elementId=elementService.createElement(elementMstrDto);
			documentDTO.setElementId(elementId);
			
			documentDTO.setKmCsrLatestUpdatesDto(kmCsrLatestUpdatesDto);
		}catch(Exception ee)
		{
			ee.printStackTrace();
			throw new KmException(ee.getMessage());
		}
	  }
		return documentDTO;				
	}	


	public KmProductUploadFormBean viewProductDetails(KmProductUploadFormBean kmProductUploadFormBean)throws KmException{

		try{
			  String documentPath =kmProductUploadFormBean.getXmlFileName();
			  ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
			  String folderPath = bundle.getString("folder.path");
			  if(!documentPath.contains(folderPath))
			  {
				  documentPath = folderPath+documentPath; 				
			  }
			  kmProductUploadFormBean.setXmlFileName(documentPath);
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
				
				nodeLst = doc.getElementsByTagName("TOPIC");
				kmProductUploadFormBean.setTopic(Utility.decodeContent(nodeLst.item(0).getTextContent()));
				////System.out.println("Topik : "+nodeLst.item(0).getTextContent());
				
				nodeLst = doc.getElementsByTagName("PUBLISHDATE");
				kmProductUploadFormBean.setPublishDt(nodeLst.item(0).getTextContent());
				
				nodeLst = doc.getElementsByTagName("ENDDATE");
				kmProductUploadFormBean.setEndDt(nodeLst.item(0).getTextContent());
				
				
				nodeLst = doc.getElementsByTagName("HEADER");
				nodeLst2 = doc.getElementsByTagName("CONTENT");
				
				int headerLength = nodeLst.getLength();		
				String header[] = new String[headerLength];
				String content[] = new String[headerLength];

				for (int s = 0; s < headerLength; s++) {
					header[s] = Utility.decodeContent(nodeLst.item(s).getTextContent());
					content[s] = Utility.decodeContent(nodeLst2.item(s).getTextContent());
				}
				
				kmProductUploadFormBean.setContent(content);
				kmProductUploadFormBean.setHeader(header);
				
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
					kmProductUploadFormBean.setImageTitle(imageTitle);
					kmProductUploadFormBean.setImageName(imagePath);				
				}
		}catch(Exception ee)
		{
			ee.printStackTrace();	
			throw new KmException(ee.getMessage());
		}
	   return kmProductUploadFormBean;				
	}	
}
