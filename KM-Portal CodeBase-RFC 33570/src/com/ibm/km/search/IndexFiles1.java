package com.ibm.km.search;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;

import com.ibm.km.common.PropertyReader;

/** Index all text files under a directory. */
public class IndexFiles1 {
	
	private static final Logger logger;

	static {

		logger = Logger.getLogger(IndexFiles.class);
	}	
	
  
  public IndexFiles1() {}

  static final File INDEX_DIR = new File("Index123456789");
  //ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
  File INDEX_TEST = new File(PropertyReader.getAppValue("Index.path"));
  Directory directory = null;
  

  public void initIndex(File f,  String documentId,String circleId){
	Date start = new Date();
	try {
		//directory=FSDirectory.getDirectory(bundle.getString("Index.path"), false);
	//	if (IndexReader.isLocked(directory)) 
		//	IndexReader.unlock(directory);
		
		IndexWriter writer = new IndexWriter(INDEX_TEST, new StandardAnalyzer(), false);
		 logger.info("Indexing to directory '" +INDEX_TEST+ "'...");
		  indexDocs(writer, f, documentId,circleId);
		  logger.info("Optimizing........");
		  writer.optimize();
		  writer.close();
		  Date end = new Date();
		  logger.info(end.getTime() - start.getTime() + " total milliseconds");
	}
	catch(LockObtainFailedException e){
		e.printStackTrace();
		//System.out.println("LockObtainFailed for DOCUMENT_ID = "+documentId);
		
	}
	catch(Exception ex){
		//System.out.println("Exception occured while writing into index : DOCUMENT_ID = "+documentId);
		ex.printStackTrace();
			
		
	}
	 
	
	
  }
  
  public void initIndexNew(File f,  String documentId,String circleId){
		Date start = new Date();
		String indexPath=null;
		IndexWriter writer=null;
		try {
			//directory=FSDirectory.getDirectory(bundle.getString("Index.path"), false);
		//	if (IndexReader.isLocked(directory)) 
			//	IndexReader.unlock(directory);
			indexPath=new StringBuffer(PropertyReader.getAppValue("Index.path")).append("/").append(circleId).toString();
			writer= new IndexWriter(new File(indexPath), new StandardAnalyzer(), false);
			 logger.info("Indexing to directory '" +INDEX_TEST+ "'...");
			  indexDocs(writer, f, documentId,circleId);
			  logger.info("Optimizing........");
			  writer.optimize();
			  writer.close();
			  Date end = new Date();
			  logger.info(end.getTime() - start.getTime() + " total milliseconds");
		}
		catch(LockObtainFailedException e){
			e.printStackTrace();
			//System.out.println("LockObtainFailed for DOCUMENT_ID = "+documentId);
			
		}
		catch (FileNotFoundException e) {
			logger.warn("Index not created for the circle. Circle Id : " + circleId);
			logger.info("Creating index for the circle. Circle Id : " + circleId);
			new File(indexPath).mkdir();
			
				try {
					writer= new IndexWriter(new File(indexPath), new StandardAnalyzer(), true);
				} catch (CorruptIndexException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (LockObtainFailedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
		}
		catch(Exception ex){
			//System.out.println("Exception occured while writing into index : DOCUMENT_ID = "+documentId);
			ex.printStackTrace();
				
			
		}
		 
		
		
	  }
  

  static void indexDocs(IndexWriter writer, File file, String documentId,String circleId)
	throws IOException {
	// do not try to index files that cannot be read
	if (file.canRead()) {
	  if (file.isDirectory()) { //Can be used to index a whole directory. Will have to modify the KmDocumentMstr Argument to Use it.
		String[] files = file.list();
		// an IO error could occur
		if (files != null) {
		  for (int i = 0; i < files.length; i++) {
			logger.info("file.isDirectory------"+file.getAbsolutePath());
			indexDocs(writer, new File(file, files[i]), documentId,circleId);
		  }
		}
	  } else {
        
		try {
		  Document doc = null;
		  if (file.getName().indexOf(".pdf") >= 0){
			logger.info("adding PDF:----------- " + file);
			//Runtime.getRuntime().exec("java ExtractText " + file+" [C:/Index/test.txt]");
			doc = LucenePDFDocument.getDocument(file, documentId,circleId);
		  }
		  else if(file.getName().indexOf(".html") >= 0 || file.getName().indexOf(".htm")>=0){
			  logger.info("adding HTML:----------- " + file);
				//Runtime.getRuntime().exec("java ExtractText " + file+" [C:/Index/test.txt]");
				doc = HTMLDocument.Document(file,documentId,circleId);
			   
		  }
		  else{
			logger.info("adding file:........ " + file);
			doc = FileDocument.Document(file, documentId,circleId);          
		  }
		//  logger.info("Document Details: "+doc.toString());
		  writer.addDocument(doc);
		}
		// at least on windows, some temporary files raise this exception with an "access denied" message
		// checking if the file can be read doesn't help
		catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace() ;
		}catch (Exception e) {
			e.printStackTrace();
		}
	  }
	}
  }
  
}
