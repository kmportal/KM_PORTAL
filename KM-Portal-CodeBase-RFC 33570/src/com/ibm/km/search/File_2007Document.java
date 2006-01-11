package com.ibm.km.search;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.log4j.Logger;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.WriteOutContentHandler;

import com.ibm.km.common.PropertyReader;




/** A utility for making Lucene Documents from a File. */

public class File_2007Document {
	
	private static final Logger logger;

	static {

		logger = Logger.getLogger(File_2007Document.class);
	}
	
	 private static final char FILE_SEPARATOR = System.getProperty("file.separator").charAt(0);
    
  public static Document Document(File f, String kmDocumentId, String circleId)
       throws java.io.FileNotFoundException {
	 
	 //////////////////////////////	  
    // make a new, empty document
    Document doc = new Document();
    
    //String output=TikaExtraction(f.getAbsolutePath());
    TikaExtraction(f.getAbsolutePath());

    // Add the path of the file as a field named "path".  Use a field that is 
    // indexed (i.e. searchable), but don't tokenize the field into words.
    doc.add(new Field("path", f.getPath(), Field.Store.YES, Field.Index.UN_TOKENIZED));
    
	// Add the document id of the file as a field named "documentId".  Use a field that is 
	// indexed (i.e. searchable), but don't tokenize the field into words.
	doc.add(new Field("documentId", kmDocumentId, Field.Store.YES, Field.Index.UN_TOKENIZED));
	
//	 Add the circleId of the file as a field named "circleId".  Use a field that is 
	// indexed (i.e. searchable), but don't tokenize the field into words.
	doc.add(new Field("circleId", circleId, Field.Store.YES, Field.Index.UN_TOKENIZED));
    
	

    // Add the last modified date of the file a field named "modified".  Use 
    // a field that is indexed (i.e. searchable), but don't tokenize the field
    // into words.
    doc.add(new Field("modified",
        DateTools.timeToString(f.lastModified(), DateTools.Resolution.MINUTE),
        Field.Store.YES, Field.Index.UN_TOKENIZED));

  
    
    String fname=f.getAbsolutePath();
    
    doc.add(new Field("contents", new FileReader(PropertyReader.getAppValue("Interim2007.filepath")+fname.substring(fname.lastIndexOf(FILE_SEPARATOR)+1,fname.indexOf(".")))));
   
    return doc;
  }

   
  
  private File_2007Document() {}
  
   
  public static void TikaExtraction(String sInputFile) {
	  
	  FileInputStream is = null;
	 // ContentHandler contenthandler=null;
	  WriteOutContentHandler contenthandler=null;
	  String ss =sInputFile;
	// logger.info("file name.............."+ss.indexOf(".")+"...total lenght()...."+ss.length());
	  ////System.out.println(ss.substring(ss.lastIndexOf("\\")+1,ss.indexOf(".")));
	// logger.info("file name.............."+ss.substring(ss.lastIndexOf(FILE_SEPARATOR)+1,ss.indexOf(".")));
	  File outFile = new File(PropertyReader.getAppValue("Interim2007.filepath")+ss.substring(ss.lastIndexOf(FILE_SEPARATOR)+1,ss.indexOf(".")));
	//  logger.info("file name.............."+outFile.getAbsolutePath());
	 // logger.info("File Name : "+PropertyReader.getAppValue("Interim2007.filepath")+ss.substring(ss.lastIndexOf(FILE_SEPARATOR)+1,ss.indexOf(".")));
	  Writer output = null;
	    try {
			output = new BufferedWriter(new FileWriter(outFile));
		} catch (IOException e) {
			e.printStackTrace();
			
		}
	  
	    try {
	    	
	     	File f= new File(sInputFile);
	      is = new FileInputStream(f);

	     // contenthandler = new BodyContentHandler();
	     contenthandler = new WriteOutContentHandler(output);
	      Metadata metadata = new Metadata();
	      metadata.set(Metadata.RESOURCE_NAME_KEY, f.getName());
	      Parser parser = new AutoDetectParser();
	      // OOXMLParser parser = new OOXMLParser();
	      parser.parse(is, contenthandler, metadata);
	   //  logger.info("Mime: " + metadata.get(Metadata.CONTENT_TYPE));
	   //  logger.info("Title: " + metadata.get(Metadata.TITLE));
	   //  logger.info("Author: " + metadata.get(Metadata.AUTHOR));
	   
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }
	    finally {
	        if (is != null)
				try {
					is.close();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    }
	 
	 }
  
 
}
    
