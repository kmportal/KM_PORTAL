
package com.ibm.km.services.impl;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.text.SimpleDateFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.ibm.km.forms.KmProductUploadFormBean;

public class KmProductUploadServiceImplTest {
	
	private static final Logger logger;

	static {

		logger = Logger.getLogger(KmProductUploadServiceImpl.class);
	}
	public static void main(String[] args) {
		

		String fileName = "/kb_files1/KM/1/12520/12100/12101/20121031_15-53-20.xml";	
		
		KmProductUploadFormBean kmProductUploadFormBean = new KmProductUploadFormBean();
		try{
			  FileInputStream fstream = new FileInputStream(fileName);
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  StringBuffer sb = new StringBuffer();
			  String strLine="";
			  
			  while ((strLine = br.readLine()) != null)   {
				  sb.append(strLine);
			  }
			  in.close();
			  //System.out.println (sb);
			  
			    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				InputSource is = new InputSource();
				
				is.setCharacterStream(new StringReader(sb.toString()));
				Document doc = db.parse(is);                
				doc.getDocumentElement().normalize();
				
				NodeList nodeLst = doc.getElementsByTagName("TOPIC");
				kmProductUploadFormBean.setTopic(nodeLst.item(0).getTextContent());
				//System.out.println("Topik : "+nodeLst.item(0).getTextContent());
				
				nodeLst = doc.getElementsByTagName("HEADER");
				NodeList nodeLst2 = doc.getElementsByTagName("CONTENT");
								
				int headerLength = nodeLst.getLength();
				
				String[] header = new String[headerLength];
				String[] content = new String[headerLength];
				
				for (int s = 0; s < headerLength; s++) {
					header[s] = nodeLst.item(s).getTextContent();
					content[s] = nodeLst2.item(s).getTextContent();				
				}
				
				kmProductUploadFormBean.setHeader(header);
				kmProductUploadFormBean.setContent(content);
				
			 					
				nodeLst = doc.getElementsByTagName("IMAGETITLE");
				nodeLst2 = doc.getElementsByTagName("IMAGENAME");
				
				
				headerLength = nodeLst.getLength();
				
				String[] imageTitle = new String[headerLength];
				String[] imageName = new String[headerLength];
				
				for (int s = 0; s < headerLength; s++) {
					imageTitle[s] = nodeLst.item(s).getTextContent();
					imageName[s] = nodeLst2.item(s).getTextContent();				
				}
				
				kmProductUploadFormBean.setImageTitle(imageTitle);
				kmProductUploadFormBean.setImageName(imageName);
				
				
				
				

						
		    
		}catch(Exception ee)
		{
			ee.printStackTrace();			
		}
	}	
}
