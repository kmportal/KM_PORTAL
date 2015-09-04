
package com.ibm.km.services.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.StringReader;
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
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.KmSopBDUploadFormBean;
import com.ibm.km.services.KmElementMstrService;
import com.ibm.km.services.KmSopBDUploadService;

public class KmSopBDUploadServiceImpl implements KmSopBDUploadService{
	
	private static final Logger logger;

	static {

		logger = Logger.getLogger(KmSopBDUploadServiceImpl.class);
	}
	
	public KmDocumentMstr saveProductDetails(KmSopBDUploadFormBean kmSopBDUploadFormBean)throws KmException{
		
		KmDocumentMstr documentDTO=new KmDocumentMstr();
		// DTO  for latest updates
		KmCsrLatestUpdatesDto kmCsrLatestUpdatesDto = new KmCsrLatestUpdatesDto();
		StringBuilder updateDesc = new StringBuilder();
		
		String documentPath =kmSopBDUploadFormBean.getElementFolderPath();
		logger.info("documentPath "+documentPath);
		StringBuilder plainTextStringBuffer = new StringBuilder();

		StringBuilder sb = new StringBuilder();
		try{
			
			// Compiling XML content for all circle
			int headers = kmSopBDUploadFormBean.getHeader().length;

			plainTextStringBuffer.append("<SOPBD>\n");
			plainTextStringBuffer.append("<TOPIC>"+Utility.encodeContent(kmSopBDUploadFormBean.getTopic().trim())+"</TOPIC>\n");
			plainTextStringBuffer.append("<PUBLISHDATE>"+kmSopBDUploadFormBean.getPublishDt()+"</PUBLISHDATE>\n");
			plainTextStringBuffer.append("<ENDDATE>"+kmSopBDUploadFormBean.getEndDt()+"</ENDDATE>\n");

			sb.append("<SOPBD>\n");
			sb.append("<TOPIC>"+Utility.encodeContent(kmSopBDUploadFormBean.getTopic().trim())+"</TOPIC>\n");
			sb.append("<PUBLISHDATE>"+kmSopBDUploadFormBean.getPublishDt()+"</PUBLISHDATE>\n");
			sb.append("<ENDDATE>"+kmSopBDUploadFormBean.getEndDt()+"</ENDDATE>\n");
			
			kmCsrLatestUpdatesDto.setUpdateTitle(kmSopBDUploadFormBean.getTopic());
			kmCsrLatestUpdatesDto.setActivationDate(kmSopBDUploadFormBean.getPublishDt());
			kmCsrLatestUpdatesDto.setExpiryDate(kmSopBDUploadFormBean.getEndDt());			
			String documentPathArr[] = documentPath.split("/");
			kmCsrLatestUpdatesDto.setLob(documentPathArr[1]);		
			kmCsrLatestUpdatesDto.setCircleId(documentPathArr[2]); 
			kmCsrLatestUpdatesDto.setCategory(documentPathArr[3]);
			
			plainTextStringBuffer.append("<DATA>\n");

			sb.append("<DATA>\n");
			int rowCount=0;
			for (int i = 0; i < headers; i++)
			{
				if( !"".equals(kmSopBDUploadFormBean.getHeader()[i].toString().trim()) ||  !"".equals(kmSopBDUploadFormBean.getContent()[i].toString().trim()))
				{
					plainTextStringBuffer.append("\t<SOPDATA>\n");
					plainTextStringBuffer.append("\t\t<HEADER>"+Utility.encodeContent(kmSopBDUploadFormBean.getHeader()[i].toString().trim())+"</HEADER>\n");
					plainTextStringBuffer.append("\t\t<CONTENT>"+Utility.encodeContent(kmSopBDUploadFormBean.getPlainContent()[i].toString().trim().replaceAll("&nbsp;"," "))+"</CONTENT>\n");
					plainTextStringBuffer.append("\t</SOPDATA>\n");

					sb.append("\t<SOPDATA>\n");
					sb.append("\t\t<HEADER>"+Utility.encodeContent(kmSopBDUploadFormBean.getHeader()[i].toString().trim())+"</HEADER>\n");
					sb.append("\t\t<CONTENT>"+Utility.encodeContent(kmSopBDUploadFormBean.getContent()[i].toString().trim().replaceAll("&nbsp;"," "))+"</CONTENT>\n");
					sb.append("\t</SOPDATA>\n");
					
					updateDesc.append(kmSopBDUploadFormBean.getHeader()[i].toString().trim()+ ": ");
					updateDesc.append(kmSopBDUploadFormBean.getPlainContent()[i].toString().trim()+ " ");
					rowCount++;
				}
			}
			while(rowCount <  5) {
				sb.append("\t<SOPDATA>\n");
				sb.append("\t\t<HEADER></HEADER>\n");
				sb.append("\t\t<CONTENT></CONTENT>\n");
				sb.append("\t</SOPDATA>\n");
				rowCount++;
			}
			
			plainTextStringBuffer.append("</DATA>\n");

			sb.append("</DATA>\n");
						
			kmCsrLatestUpdatesDto.setUpdateDesc(updateDesc.toString());
			
			rowCount=0;
			plainTextStringBuffer.append("<VAS>\n");

			sb.append("<VAS>\n");
			// Adding VAS details if available 
			if(null != kmSopBDUploadFormBean.getHeaderVas())
			{
				for (int i = 0; i < kmSopBDUploadFormBean.getHeaderVas().length; i++) 
				{
					if( !"".equals(kmSopBDUploadFormBean.getHeaderVas()[i].toString().trim()) ||  !"".equals(kmSopBDUploadFormBean.getContentVas()[i].toString().trim()))
					{
						plainTextStringBuffer.append("\t<VASDATA>\n");
						plainTextStringBuffer.append("\t\t<VASHEADER>"+Utility.encodeContent(kmSopBDUploadFormBean.getHeaderVas()[i].toString().trim())+"</VASHEADER>\n");
						plainTextStringBuffer.append("\t\t<VASCONTENT>"+Utility.encodeContent(kmSopBDUploadFormBean.getPlainContentVas()[i].toString().trim()).replaceAll("&nbsp;"," ")+"</VASCONTENT>\n");
						plainTextStringBuffer.append("\t</VASDATA>\n");

						sb.append("\t<VASDATA>\n");
						sb.append("\t\t<VASHEADER>"+Utility.encodeContent(kmSopBDUploadFormBean.getHeaderVas()[i].toString().trim())+"</VASHEADER>\n");
						sb.append("\t\t<VASCONTENT>"+Utility.encodeContent(kmSopBDUploadFormBean.getContentVas()[i].toString().trim()).replaceAll("&nbsp;"," ")+"</VASCONTENT>\n");
						sb.append("\t</VASDATA>\n");
						rowCount++;
					}
				}
			}
			while(rowCount < 5) {
				sb.append("\t<VASDATA>\n");
				sb.append("\t\t<VASHEADER></VASHEADER>\n");
				sb.append("\t\t<VASCONTENT></VASCONTENT>\n");
				sb.append("\t</VASDATA>\n");
				rowCount++;
			}
			rowCount=0;

			plainTextStringBuffer.append("</VAS>\n");
			plainTextStringBuffer.append("<VOICE>\n");

			sb.append("</VAS>\n");
			sb.append("<VOICE>\n");
			// Adding VOICE & SMS (VOICE) details if available 
			if(null != kmSopBDUploadFormBean.getHeaderVoice())
			{
				for (int i = 0; i < kmSopBDUploadFormBean.getHeaderVoice().length; i++) 
				{
					if( !"".equals(kmSopBDUploadFormBean.getHeaderVoice()[i].toString().trim()) ||  !"".equals(kmSopBDUploadFormBean.getContentVoice()[i].toString().trim()))
					{
						plainTextStringBuffer.append("\t<VOICEDATA>\n");
						plainTextStringBuffer.append("\t\t<VOICEHEADER>"+Utility.encodeContent(kmSopBDUploadFormBean.getHeaderVoice()[i].toString().trim())+"</VOICEHEADER>\n");
						plainTextStringBuffer.append("\t\t<VOICECONTENT>"+Utility.encodeContent(kmSopBDUploadFormBean.getPlainContentVoice()[i].toString().trim()).replaceAll("&nbsp;"," ")+"</VOICECONTENT>\n");
						plainTextStringBuffer.append("\t</VOICEDATA>\n");

						sb.append("\t<VOICEDATA>\n");
						sb.append("\t\t<VOICEHEADER>"+Utility.encodeContent(kmSopBDUploadFormBean.getHeaderVoice()[i].toString().trim())+"</VOICEHEADER>\n");
						sb.append("\t\t<VOICECONTENT>"+Utility.encodeContent(kmSopBDUploadFormBean.getContentVoice()[i].toString().trim()).replaceAll("&nbsp;"," ")+"</VOICECONTENT>\n");
						sb.append("\t</VOICEDATA>\n");
						rowCount++;
					}
				} 
			}
			while(rowCount < 5) {
				sb.append("\t<VOICEDATA>\n");
				sb.append("\t\t<VOICEHEADER></VOICEHEADER>\n");
				sb.append("\t\t<VOICECONTENT></VOICECONTENT>\n");
				sb.append("\t</VOICEDATA>\n");
				rowCount++;
			}
			plainTextStringBuffer.append("</VOICE>\n");

			sb.append("</VOICE>\n");
			
			rowCount=0;
			
			// Adding  MO & NOP (MO) details if available 
			if(null != kmSopBDUploadFormBean.getHeaderMo())
			{
				plainTextStringBuffer.append("<MO>\n");

				sb.append("<MO>\n");
				for (int i = 0; i < kmSopBDUploadFormBean.getHeaderMo().length; i++) 
				{
					if( !"".equals(kmSopBDUploadFormBean.getHeaderMo()[i].toString().trim()) ||  !"".equals(kmSopBDUploadFormBean.getContentMo()[i].toString().trim()))
					{
						plainTextStringBuffer.append("\t<MODATA>\n");
						plainTextStringBuffer.append("\t\t<MOHEADER>"+Utility.encodeContent(kmSopBDUploadFormBean.getHeaderMo()[i].toString().trim())+"</MOHEADER>\n");
						plainTextStringBuffer.append("\t\t<MOCONTENT>"+Utility.encodeContent(kmSopBDUploadFormBean.getPlainContentMo()[i].toString().trim()).replaceAll("&nbsp;"," ")+"</MOCONTENT>\n");
						plainTextStringBuffer.append("\t</MODATA>\n");

						sb.append("\t<MODATA>\n");
						sb.append("\t\t<MOHEADER>"+Utility.encodeContent(kmSopBDUploadFormBean.getHeaderMo()[i].toString().trim())+"</MOHEADER>\n");
						sb.append("\t\t<MOCONTENT>"+Utility.encodeContent(kmSopBDUploadFormBean.getContentMo()[i].toString().trim()).replaceAll("&nbsp;"," ")+"</MOCONTENT>\n");
						sb.append("\t</MODATA>\n");
						rowCount++;
					}
				}
			}
			while(rowCount < 5) {
				sb.append("\t<MODATA>\n");
				sb.append("\t\t<MOHEADER></MOHEADER>\n");
				sb.append("\t\t<MOCONTENT></MOCONTENT>\n");
				sb.append("\t</MODATA>\n");
				rowCount++;
			}
			plainTextStringBuffer.append("</MO>\n");

			sb.append("</MO>\n");
			
			
			// Adding case not know (CNN) details if available 
			rowCount=0;
			if(null != kmSopBDUploadFormBean.getHeaderCNN())
			{
				plainTextStringBuffer.append("<CNN>\n");

				sb.append("<CNN>\n");
				for (int i = 0; i < kmSopBDUploadFormBean.getHeaderCNN().length; i++) 
				{
					if( !"".equals(kmSopBDUploadFormBean.getHeaderCNN()[i].toString().trim()) ||  !"".equals(kmSopBDUploadFormBean.getContentCNN()[i].toString().trim()))
					{
						plainTextStringBuffer.append("\t<CNNDATA>\n");
						plainTextStringBuffer.append("\t\t<CNNHEADER>"+Utility.encodeContent(kmSopBDUploadFormBean.getHeaderCNN()[i].toString().trim())+"</CNNHEADER>\n");
						plainTextStringBuffer.append("\t\t<CNNCONTENT>"+Utility.encodeContent(kmSopBDUploadFormBean.getPlainContentCNN()[i].toString().trim()).replaceAll("&nbsp;"," ")+"</CNNCONTENT>\n");
						plainTextStringBuffer.append("\t</CNNDATA>\n");

						sb.append("\t<CNNDATA>\n");
						sb.append("\t\t<CNNHEADER>"+Utility.encodeContent(kmSopBDUploadFormBean.getHeaderCNN()[i].toString().trim())+"</CNNHEADER>\n");
						sb.append("\t\t<CNNCONTENT>"+Utility.encodeContent(kmSopBDUploadFormBean.getContentCNN()[i].toString().trim()).replaceAll("&nbsp;"," ")+"</CNNCONTENT>\n");
						sb.append("\t</CNNDATA>\n");
						rowCount++;
					}
				}
			}
			while(rowCount < 5) {
				sb.append("\t<CNNDATA>\n");
				sb.append("\t\t<CNNHEADER></CNNHEADER>\n");
				sb.append("\t\t<CNNCONTENT></CNNCONTENT>\n");
				sb.append("\t</CNNDATA>\n");
				rowCount++;
			}
			plainTextStringBuffer.append("</CNN>\n");

		    sb.append("</CNN>\n");
		    
			// Adding airtel live / wap (ALIVE) details if available 
		    rowCount=0;
			if(null != kmSopBDUploadFormBean.getHeaderALive())
			{
				plainTextStringBuffer.append("<ALIVE>\n");

				sb.append("<ALIVE>\n");
				for (int i = 0; i < kmSopBDUploadFormBean.getHeaderALive().length; i++) 
				{
					if( !"".equals(kmSopBDUploadFormBean.getHeaderALive()[i].toString().trim()) ||  !"".equals(kmSopBDUploadFormBean.getContentALive()[i].toString().trim()))
					{
						plainTextStringBuffer.append("\t<ALIVEDATA>\n");
						plainTextStringBuffer.append("\t\t<ALIVEHEADER>"+Utility.encodeContent(kmSopBDUploadFormBean.getHeaderALive()[i].toString().trim())+"</ALIVEHEADER>\n");
						plainTextStringBuffer.append("\t\t<ALIVECONTENT>"+Utility.encodeContent(kmSopBDUploadFormBean.getPlainContentALive()[i].toString().trim()).replaceAll("&nbsp;"," ")+"</ALIVECONTENT>\n");
						plainTextStringBuffer.append("\t</ALIVEDATA>\n");

						sb.append("\t<ALIVEDATA>\n");
						sb.append("\t\t<ALIVEHEADER>"+Utility.encodeContent(kmSopBDUploadFormBean.getHeaderALive()[i].toString().trim())+"</ALIVEHEADER>\n");
						sb.append("\t\t<ALIVECONTENT>"+Utility.encodeContent(kmSopBDUploadFormBean.getContentALive()[i].toString().trim()).replaceAll("&nbsp;"," ")+"</ALIVECONTENT>\n");
						sb.append("\t</ALIVEDATA>\n");
						rowCount++;
					}
				}
			}
			while(rowCount < 5) {
				sb.append("\t<ALIVEDATA>\n");
				sb.append("\t\t<ALIVEHEADER></ALIVEHEADER>\n");
				sb.append("\t\t<ALIVECONTENT></ALIVECONTENT>\n");
				sb.append("\t</ALIVEDATA>\n");
				rowCount++;
			}	
			plainTextStringBuffer.append("</ALIVE>\n");
			plainTextStringBuffer.append("</SOPBD>\n");
			
			sb.append("</ALIVE>\n");
			sb.append("</SOPBD>\n");

			logger.info("XML content : "+sb);
			
			logger.info("Writing File to : "+kmSopBDUploadFormBean.getXmlFileName());

			// write xml file with html content
				FileWriter fstream = new FileWriter(kmSopBDUploadFormBean.getXmlFileName());
				BufferedWriter out = new BufferedWriter(fstream);
	  		    out.write(sb.toString());
			    out.close();
			    
				// write xml file containing plain text content
				fstream = new FileWriter(kmSopBDUploadFormBean.getXmlFileNameContentPlainText());
				out = new BufferedWriter(fstream);
	  		    out.write(plainTextStringBuffer.toString());
			    out.close();

			    boolean isValidFile = new ContentValidator().validate(kmSopBDUploadFormBean.getXmlFileName());
			    
			    if(!isValidFile)
			    {
			    	documentDTO.setValidFile(isValidFile);
			    	return documentDTO;
			    }
			    
			String xmlFileName = kmSopBDUploadFormBean.getXmlFileName().substring(kmSopBDUploadFormBean.getXmlFileName().lastIndexOf("/")+1);
			logger.info("XML file Name : "+xmlFileName);
			
		    documentDTO.setDocumentName(xmlFileName);
		    documentDTO.setDocName(xmlFileName);
		    documentDTO.setDocumentDisplayName(kmSopBDUploadFormBean.getTopic());
		    documentDTO.setDocumentDesc(kmSopBDUploadFormBean.getTopic());
		    documentDTO.setKeyword(kmSopBDUploadFormBean.getTopic());
			documentDTO.setPublishingStartDate(kmSopBDUploadFormBean.getPublishDt());
			documentDTO.setPublishingEndDate(kmSopBDUploadFormBean.getEndDt());
			
			documentDTO.setUserId(kmSopBDUploadFormBean.getCreatedBy());
			documentDTO.setApprovalStatus("A");
		    documentDTO.setUpdatedBy(kmSopBDUploadFormBean.getCreatedBy());
		    
		    documentPath = documentPath.substring(0,documentPath.lastIndexOf("/"));
		    
		    
		    documentDTO.setDocumentPath(documentPath);
		    
		    KmElementMstr elementMstrDto= new KmElementMstr();
			elementMstrDto.setElementDesc(kmSopBDUploadFormBean.getTopic().trim());
			
			String parentId = documentPath.substring(documentPath.lastIndexOf("/")+1);
			
			elementMstrDto.setParentId(parentId);
			elementMstrDto.setPanStatus("N");
			elementMstrDto.setElementLevel("0");
			elementMstrDto.setStatus("A");
			elementMstrDto.setElementName(kmSopBDUploadFormBean.getTopic().trim());
			documentDTO.setDocType(Constants.DOC_TYPE_SOP_BD);
			elementMstrDto.setCreatedBy(kmSopBDUploadFormBean.getCreatedBy());
			elementMstrDto.setUpdatedBy(kmSopBDUploadFormBean.getCreatedBy());
			
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


	public KmSopBDUploadFormBean viewProductDetails(KmSopBDUploadFormBean kmSopBDUploadFormBean)throws KmException{

		try{
			  String documentPath =kmSopBDUploadFormBean.getXmlFileName();
			  ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
			  String folderPath = bundle.getString("folder.path");
			  if(!documentPath.contains(folderPath))
			  {
				  documentPath = folderPath+documentPath; 
				
			  }
			  kmSopBDUploadFormBean.setXmlFileName(documentPath);
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
				
				kmSopBDUploadFormBean.setTopic(Utility.decodeContent(doc.getElementsByTagName("TOPIC").item(0).getTextContent()));
				kmSopBDUploadFormBean.setPublishDt(doc.getElementsByTagName("PUBLISHDATE").item(0).getTextContent());
				kmSopBDUploadFormBean.setEndDt(doc.getElementsByTagName("ENDDATE").item(0).getTextContent());
				
				nodeLst =  doc.getElementsByTagName("HEADER");
				nodeLst2 = doc.getElementsByTagName("CONTENT");
								
				int headerLength = nodeLst.getLength();
				String header[] = new String[headerLength];
				String content[] = new String[headerLength];

				for (int s = 0; s < headerLength; s++) {
					header[s] = Utility.decodeContent(nodeLst.item(s).getTextContent());
					content[s] = Utility.decodeContent(nodeLst2.item(s).getTextContent());
				}
				
				kmSopBDUploadFormBean.setContent(content);
				kmSopBDUploadFormBean.setHeader(header);
				
				// Getting VAS details if available 
				nodeLst = null;
				nodeLst2 = null;
				nodeLst =  doc.getElementsByTagName("VASHEADER");
				nodeLst2 = doc.getElementsByTagName("VASCONTENT");
						
				logger.info("VAS nodeLst : "+nodeLst);
				logger.info("nodeLst length : "+nodeLst.getLength());
				
				int headerVasLength = nodeLst.getLength();
				
				String headerVas[] = new String[headerVasLength];
				String contentVas[] = new String[headerVasLength];
				
				for (int s = 0; s < headerVasLength; s++) {
					headerVas[s] = Utility.decodeContent(nodeLst.item(s).getTextContent());
					contentVas[s] = Utility.decodeContent(nodeLst2.item(s).getTextContent());
				}
				kmSopBDUploadFormBean.setHeaderVas(headerVas);
				kmSopBDUploadFormBean.setContentVas(contentVas);
				
				// Getting voice & SMS (Voice) details if available 
				nodeLst = null;
				nodeLst2 = null;
				nodeLst =  doc.getElementsByTagName("VOICEHEADER");
				nodeLst2 = doc.getElementsByTagName("VOICECONTENT");
						
				logger.info("voice nodeLst : "+nodeLst);
				logger.info("nodeLst length : "+nodeLst.getLength());
				
				int headerVoiceLength = nodeLst.getLength();
				
				String headerVoice[] = new String[headerVoiceLength];
				String contentVoice[] = new String[headerVoiceLength];
				
				for (int s = 0; s < headerVoiceLength; s++) {
					headerVoice[s] = Utility.decodeContent(nodeLst.item(s).getTextContent());
					contentVoice[s] = Utility.decodeContent(nodeLst2.item(s).getTextContent());
				}
				kmSopBDUploadFormBean.setHeaderVoice(headerVoice);
				kmSopBDUploadFormBean.setContentVoice(contentVoice);
				
				// Getting MO & NOP (Mo) details if available      
				nodeLst = null;
				nodeLst2 = null;
				nodeLst =  doc.getElementsByTagName("MOHEADER");
				nodeLst2 = doc.getElementsByTagName("MOCONTENT");
						
				logger.info("MO nodeLst : "+nodeLst);
				logger.info("nodeLst length : "+nodeLst.getLength());
				
				int headerMoLength = nodeLst.getLength();
				
				String headerMo[] = new String[headerMoLength];
				String contentMo[] = new String[headerMoLength];
				
				for (int s = 0; s < headerMoLength; s++) {
					headerMo[s] = Utility.decodeContent(nodeLst.item(s).getTextContent());
					contentMo[s] = Utility.decodeContent(nodeLst2.item(s).getTextContent());
				}
				kmSopBDUploadFormBean.setHeaderMo(headerMo);
				kmSopBDUploadFormBean.setContentMo(contentMo);
				
				// Getting case not know (CNN) details if available 
				nodeLst = null;
				nodeLst2 = null;
				nodeLst =  doc.getElementsByTagName("CNNHEADER");
				nodeLst2 = doc.getElementsByTagName("CNNCONTENT");
						
				logger.info("CNN nodeLst : "+nodeLst);
				logger.info("nodeLst length : "+nodeLst.getLength());
				
				int headerCNNLength = nodeLst.getLength();
				
				String headerCNN[] = new String[headerCNNLength];
				String contentCNN[] = new String[headerCNNLength];
				
				for (int s = 0; s < headerCNNLength; s++) {
					headerCNN[s] = Utility.decodeContent(nodeLst.item(s).getTextContent());
					contentCNN[s] = Utility.decodeContent(nodeLst2.item(s).getTextContent());
				}
				kmSopBDUploadFormBean.setHeaderCNN(headerCNN);
				kmSopBDUploadFormBean.setContentCNN(contentCNN);
				
				// Getting airtel live / wap (ALive) details if available 
				nodeLst = null;
				nodeLst2 = null;
				nodeLst =  doc.getElementsByTagName("ALIVEHEADER");
				nodeLst2 = doc.getElementsByTagName("ALIVECONTENT");
						
				logger.info("ALive nodeLst : "+nodeLst);
				logger.info("nodeLst length : "+nodeLst.getLength());
				
				int headerALiveLength = nodeLst.getLength();
				
				String headerALive[] = new String[headerALiveLength];
				String contentALive[] = new String[headerALiveLength];
				
				for (int s = 0; s < headerALiveLength; s++) {
					headerALive[s] = Utility.decodeContent(nodeLst.item(s).getTextContent());
					contentALive[s] = Utility.decodeContent(nodeLst2.item(s).getTextContent());
				}
				kmSopBDUploadFormBean.setHeaderALive(headerALive);
				kmSopBDUploadFormBean.setContentALive(contentALive);
				
		}catch(Exception ee)
		{
			ee.printStackTrace();		
			throw new KmException(ee.getMessage());
		}
	   return kmSopBDUploadFormBean;				
	}	
}
