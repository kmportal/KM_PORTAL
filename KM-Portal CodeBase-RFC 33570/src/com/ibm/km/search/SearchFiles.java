package com.ibm.km.search;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.FilterIndexReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.store.AlreadyClosedException;

import com.ibm.km.common.PropertyReader;
import com.ibm.km.dao.KmSearchDao;
import com.ibm.km.dao.impl.KmSearchDaoImpl;
import com.ibm.km.dto.KmDocumentMstr;

/** Simple command-line based search demo. */
public class SearchFiles {

	private static final Logger logger;

	static {

		logger = Logger.getLogger(SearchFiles.class);
	}
	
  private static class OneNormsReader extends FilterIndexReader {
	private String field;

	public OneNormsReader(IndexReader in, String field) {
	  super(in);
	  this.field = field;
	}

	public byte[] norms(String field) throws IOException {
	  return in.norms(this.field);
	}
  }

  
  public Hits search(String key) throws Exception {

//System.out.println("key***************"+key);
	//ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
	String index_test = PropertyReader.getAppValue("Index.path"); // gets the path for index 
	String field = "contents";
	String queries = null;
	boolean raw = false;
	String normsField = null;
	String keyword=key;
	Hits hits= null;
	try{
	IndexReader reader = IndexReader.open(index_test);

	if (normsField != null)
	  reader = new OneNormsReader(reader, normsField);

	Searcher searcher = new IndexSearcher(reader);
	Analyzer analyzer = new StandardAnalyzer();

	BufferedReader in = null;
	if (queries != null) {
	  in = new BufferedReader(new FileReader(queries));
	} else {
	  in = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
	}
	  QueryParser parser = new QueryParser(field, analyzer);
	  //logger.info("Parser: "+parser.toString());
   // while (true) {
	  //if (queries == null)                        // prompt the user
	  //  logger.info("Enter query: ");

	  //String line = in.readLine();
	  String line=keyword;
//		if (line == null || line.length() == -1)
//		  break;
//
//		line = line.trim();
		if (line.length() == 0)
		  return null;
	  Query query = parser.parse(line);
	  logger.info("Searching for...... " + query.toString(field));

	  hits = searcher.search(query);
      
//	  if (repeat > 0) {                           // repeat & time as benchmark
//		Date start = new Date();
//		for (int i = 0; i < repeat; i++) {
//		  hits = searcher.search(query);
//		}
//		Date end = new Date();
//		logger.info("Time: "+(end.getTime()-start.getTime())+"ms");
//	  }

	  logger.info(hits.length() + " total matching documents");

//	  final int HITS_PER_PAGE = 10;
//	  for (int start = 0; start < hits.length(); start += HITS_PER_PAGE) {
//		int end = Math.min(hits.length(), start + HITS_PER_PAGE);
//		logger.info("end-----"+end);
		for (int i = 0; i < hits.length(); i++) {  

		  if (raw) {                              // output raw format
			logger.info("doc==="+hits.id(i)+" score===="+hits.score(i));
			//continue;
		  }

		  Document doc = hits.doc(i);
	/*Added by Anil                      	  
		  int maxFiles=4000;
		  
		  if (doc != null) {

			  if (hits.length() <= maxFiles) {
				  maxFiles = hits.length();
			  }
			  //	String[] documentId=new String[documentHits.length()];
			  String[] documentId = new String[hits.length()];

			  for (int start = 0; start < hits.length(); start++) {
				  
				  documentId[start] = hits.doc(start).get("documentId");
						
						
			  }
			  if (maxFiles != 0) {
				 
				  documentList = dao.contentSearch(dto, documentId);
			  }
		  }
		  
		Added by Anil */  
		  String path = doc.get("path");
		
		  if (path != null) {
		//	logger.info((i+1) + ". " + path);
			String title = doc.get("title");
			if (title != null) {
		//	  logger.info("   Title: " + title);
			}
			String documentId = doc.get("documentId");
			String circleId=doc.get("circleId");
			
			if (documentId != null) {
	//	//System.out.println("   Document Id: " + documentId);
			}			
		  } else {
		//	logger.info((i+1) + ". " + "No path for this document");
		  }
//		}

		if (queries != null)                      // non-interactive
		  break;
//        
//		if (hits.length() > end) {
//		  logger.info("more (y/n) ? ");
//		  line = in.readLine();
//		  if (line.length() == 0 || line.charAt(0) == 'n')
//			break;
//		}
	  }
	  
	//}
		if(in!=null)
		{
			//logger.info("In search new in");
			in.close();
		}
	if(reader!=null)
	{
		//logger.info("In search new reader");
	//reader.close();
	}
	logger.info("In search ");
	}
	catch(AlreadyClosedException e){
		e.printStackTrace();
		   logger.info("Exception occured during content search");
	}
	catch(Exception e){
		logger.info("Exception occured during content search1");
		e.printStackTrace();
	}	
	finally{
	return hits;
	} 
  } 
  
  public Hits searchNew(String key,String circle) throws Exception {


		String indexPath = null;
		String field = "contents";
		String queries = null;
		boolean raw = false;
		String normsField = null;
		String keyword=key;
		Hits hits= null;
		try{
		indexPath=	new StringBuffer(PropertyReader.getAppValue("Index.path")).append("/").append(circle).toString(); // gets the path for index 
		IndexReader reader = IndexReader.open(indexPath);

		if (normsField != null)
		  reader = new OneNormsReader(reader, normsField);

		Searcher searcher = new IndexSearcher(reader);
		Analyzer analyzer = new StandardAnalyzer();

		BufferedReader in = null;
		if (queries != null) {
		  in = new BufferedReader(new FileReader(queries));
		} else {
		  in = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
		}
		  QueryParser parser = new QueryParser(field, analyzer);
		  //logger.info("Parser: "+parser.toString());
	   // while (true) {
		  //if (queries == null)                        // prompt the user
		  //  logger.info("Enter query: ");

		  //String line = in.readLine();
		  String line=keyword;
//			if (line == null || line.length() == -1)
//			  break;
	//
//			line = line.trim();
			if (line.length() == 0)
			  return null;
	      
		  Query query = parser.parse(line);
		  logger.info("Searching for...... " + query.toString(field));

		  hits = searcher.search(query);
	      
//		  if (repeat > 0) {                           // repeat & time as benchmark
//			Date start = new Date();
//			for (int i = 0; i < repeat; i++) {
//			  hits = searcher.search(query);
//			}
//			Date end = new Date();
//			logger.info("Time: "+(end.getTime()-start.getTime())+"ms");
//		  }

		  logger.info(hits.length() + " total matching documents");

//		  final int HITS_PER_PAGE = 10;
//		  for (int start = 0; start < hits.length(); start += HITS_PER_PAGE) {
//			int end = Math.min(hits.length(), start + HITS_PER_PAGE);
//			logger.info("end-----"+end);
			for (int i = 0; i < hits.length(); i++) {  

			  if (raw) {                              // output raw format
				logger.info("doc==="+hits.id(i)+" score===="+hits.score(i));
				//continue;
			  }

			  Document doc = hits.doc(i);
		/*Added by Anil                      	  
			  int maxFiles=4000;
			  
			  if (doc != null) {

				  if (hits.length() <= maxFiles) {
					  maxFiles = hits.length();
				  }
				  //	String[] documentId=new String[documentHits.length()];
				  String[] documentId = new String[hits.length()];

				  for (int start = 0; start < hits.length(); start++) {
					  
					  documentId[start] = hits.doc(start).get("documentId");
							
							
				  }
				  if (maxFiles != 0) {
					 
					  documentList = dao.contentSearch(dto, documentId);
				  }
			  }
			  
			Added by Anil */  
			  String path = doc.get("path");
			
			  if (path != null) {
			//	logger.info((i+1) + ". " + path);
				String title = doc.get("title");
				if (title != null) {
			//	  logger.info("   Title: " + title);
				}
				String documentId = doc.get("documentId");
				String circleId=doc.get("circleId");
				
				if (documentId != null) {
		//	//System.out.println("   Document Id: " + documentId);
				}			
			  } else {
			//	logger.info((i+1) + ". " + "No path for this document");
			  }
//			}

			if (queries != null)                      // non-interactive
			  break;
//	        
//			if (hits.length() > end) {
//			  logger.info("more (y/n) ? ");
//			  line = in.readLine();
//			  if (line.length() == 0 || line.charAt(0) == 'n')
//				break;
//			}
		  }
		  
		//}
			if (in !=null)
			{
			 in.close();
			}
		//	reader.close();
		    logger.info("In search new");
		}
		catch(AlreadyClosedException e){
			e.printStackTrace();
			   logger.info("Exception occured during content search");
		}
		catch(Exception e){
			e.printStackTrace();
			logger.info("Exception occured during content search");
		}	
		finally{
		return hits;
		} 
	  }  
}
