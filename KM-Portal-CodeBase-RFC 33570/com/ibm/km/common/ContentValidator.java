package com.ibm.km.common;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;


public class ContentValidator {

	private static final Logger logger;

	static {
		logger = Logger.getLogger(ContentValidator.class);
	}

	public boolean validate(String fileName) {
		boolean validation = false;
		try{
			  String documentPath =fileName;
			  ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
			  String folderPath = bundle.getString("folder.path");
			  if(!documentPath.contains(folderPath))
			  {
				  documentPath = folderPath+documentPath; 
				
			  }
			  FileInputStream fstream = new FileInputStream(documentPath);
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  StringBuffer sb = new StringBuffer();
			  String strLine="";
				
			  while ((strLine = br.readLine()) != null)   {
				  sb.append(strLine);
			  }
			
			  in.close();
			  if(fstream!=null){
				  fstream.close();
			  }
			  if(br!=null){
				  br.close();
			  }
			  
			  
			    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				InputSource is = new InputSource();
				is.setCharacterStream(new StringReader(sb.toString()));
				Document doc = db.parse(is);                
				doc.getDocumentElement().normalize();
				validation = true;
				
		}catch(Exception ee)
		{
			validation = false;
			  logger.error("<<Error while File validation  >> " );
			ee.printStackTrace();		
		}
		  logger.info("<<File validation is : " + validation + " for the file: " + fileName + " >> " );

		return validation;				
	}
	
	
}
