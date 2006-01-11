package com.ibm.km.handler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;

import com.ibm.km.common.PropertyReader;
import com.ibm.km.engine.KMHandler;
import com.ibm.km.engine.exception.LmsException;
import com.ibm.km.search.File_2007Document;

public class ProcessIndexHandler extends KMHandler {
	
	private File f;
	private String documentId;
	private String circleId;

	private static Logger logger=Logger.getLogger(ProcessIndexHandler.class);

	public ProcessIndexHandler(File f,  String documentId,String circleId)
	{
		this.f = f;
		this.documentId = documentId;
		this.circleId = circleId;
	}
	
	
	public Boolean call() throws Exception {
		process();
		return new Boolean(true);
	}
	
	protected void process() throws LmsException
	{
		try
		{
			logger.info("Process: ProcessIndexHandler Started ...");

			//	 logger.info("Indexing to directory '" +INDEX_TEST+ "'...");
				  indexDocs(f, documentId,circleId);
				//  logger.info("Optimizing........");
			
				  logger.info("ProcessIndexHandler****************** Done...");
		}
		catch(Exception ex)
		{
			logger.info("ProcessIndexHandler-->> Exception: "+ex);
			ex.printStackTrace();
		}
	}
	
	  private static  void  indexDocs(File file, String documentId,String circleId)
		throws IOException {
		// do not try to index files that cannot be read
		logger.info("file name & path.... "+file.getAbsolutePath()+"....."+file.getName());
		logger.info(" File Path !!"+file.getPath());
		  logger.info("Indexing Started .... ");
		if (file.canRead()) {
			logger.info("Reading File");
		  if (file.isDirectory()) { //Can be used to index a whole directory. Will have to modify the KmDocumentMstr Argument to Use it.
			String[] files = file.list();
			// an IO error could occur
			if (files != null) {
			  for (int i = 0; i < files.length; i++) {
				logger.info("ProcessIndexHandler->>file.isDirectory------"+file.getAbsolutePath());
				indexDocs(new File(file, files[i]), documentId,circleId);
			  }
			}
		  } else {
	        
			try {

			logger.info("ProcessIndexHandler--->> Indexing for: " + file);
			 performIndexing(file,documentId,circleId);
			 logger.info("ProcessIndexHandler--->> Indexing done for: " + file);
			}
			// at least on windows, some temporary files raise this exception with an "access denied" message
			// checking if the file can be read doesn't help
			catch (FileNotFoundException fnfe) {
				logger.info("ProcessIndexHandler--->> FileNotFoundException:" + fnfe);
				fnfe.printStackTrace() ;
			}catch (Exception e) {
				logger.info("ProcessIndexHandler--->> Exception2:" + e);
				e.printStackTrace();
			}
		  }
		}else{
			logger.info("<<--ProcessIndexHandler--->> Unable to read file");
			//System.out.println("Unable to read file ");
		}
	  }
	  
	  
	  private synchronized static void performIndexing(File file, String documentId,String circleId) throws FileNotFoundException{
			logger.info("Performing synchronized Indexing--->> " + file);

		  	Document doc = null;
			File INDEX_TEST = new File(PropertyReader.getAppValue("Index.path"));
			IndexWriter writer = null;
			try {
				writer = new IndexWriter(INDEX_TEST, new StandardAnalyzer(),false);
				logger.info(" in Process handler !!!!!!!!!!!! "+file+" $$ "+documentId+" $$$ "+circleId);
				doc = File_2007Document.Document(file,documentId,circleId);
				writer.addDocument(doc);
			} catch (CorruptIndexException e) {
				logger.info("Exception: In performIndexing CorruptIndexException>>> " + e);
				e.printStackTrace();
			} catch (LockObtainFailedException e) {
				logger.info("Exception: In performIndexing LockObtainFailedException>>> " + e);
				e.printStackTrace();
			} catch (IOException e) {
				logger.info("Exception: In performIndexing IOException>>> " + e);
				e.printStackTrace();
			}
			if(writer != null) {
				try {
					writer.optimize();
					  writer.close();
					  writer = null;
						logger.info("Synchronized Indexing Done --->> " + file);
				} catch (CorruptIndexException e) {
					logger.info("Exception: In optimize CorruptIndexException>>> " + e);
					e.printStackTrace();
				} catch (IOException e) {
					logger.info("Exception: In optimize IOException>>> " + e);
					e.printStackTrace();
				}
			}
			INDEX_TEST = null;
	  }
	
}
