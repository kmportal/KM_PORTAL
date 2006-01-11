package com.ibm.km.networkfault;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;

import com.ibm.km.common.Utility;
import com.ibm.km.dto.NetworkErrorLogDto;

/** Index all locations for a logged network fault. */
public class IndexLocation {
	
	private static final Logger logger;

	static {

		logger = Logger.getLogger(IndexLocation.class);
	}	
  
  public IndexLocation() {}

  //ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
  File networdFaultIndexDir = new File(Utility.getNetworkFaultIndexPath());
  Directory directory = null;
  
  public void initNetworkFaultIndex(NetworkErrorLogDto networkErrorLogDto,boolean createNew)throws LockObtainFailedException{
		Date start = new Date();
		IndexWriter writer=null;
		try {
			
			writer= new IndexWriter(networdFaultIndexDir, new StandardAnalyzer(), createNew);
			 logger.info("Indexing to directory '" +networdFaultIndexDir+ "'...");
			  indexLocations(writer,networkErrorLogDto);
			  logger.info("Optimizing........");
			  writer.optimize();
			  writer.close();
			  Date end = new Date();
			  logger.info(end.getTime() - start.getTime() + " total milliseconds");
		}
		catch(LockObtainFailedException e){
			logger.error(e.getMessage());
			throw new LockObtainFailedException(e.getMessage());
		}
		catch (FileNotFoundException e) {
			logger.error("Indexing to directory '" +networdFaultIndexDir+ "'  failded");
			// By default folder not available for current day, creating a new one.
			initNetworkFaultIndex(networkErrorLogDto,true);
			}
		catch(Exception ex){
			logger.error(ex.getMessage());
		}
	  }
  
  static void indexLocations(IndexWriter writer, NetworkErrorLogDto networkErrorLogDto)
	throws IOException {
		try {
		  Document doc = null;
		    
			doc = FileLocation.document(networkErrorLogDto);          
  		    writer.addDocument(doc);
		}
		catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace() ;
		}catch (Exception e) {
			e.printStackTrace();
		}
  }
}
