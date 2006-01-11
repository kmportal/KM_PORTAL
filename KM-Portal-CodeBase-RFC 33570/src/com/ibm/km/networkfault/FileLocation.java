package com.ibm.km.networkfault;

import java.io.Reader;
import java.io.StringReader;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import com.ibm.km.common.Utility;
import com.ibm.km.dto.NetworkErrorLogDto;

/** A utility for making Lucene Documents from user input. */

public class FileLocation {

	public static Document document(NetworkErrorLogDto networkErrorLogDto) throws java.io.FileNotFoundException {
	 
	// make a new, empty document
	Document doc = new Document();
	//System.out.println("INdexing for circle "+networkErrorLogDto.getCircleName());
	doc.add(new Field("problemId", networkErrorLogDto.getProblemId(), Field.Store.YES, Field.Index.UN_TOKENIZED));
	doc.add(new Field("circleId", networkErrorLogDto.getCircleID()+"", Field.Store.YES, Field.Index.UN_TOKENIZED));
	doc.add(new Field("circleName", networkErrorLogDto.getCircleName(), Field.Store.YES, Field.Index.UN_TOKENIZED));
	doc.add(new Field("areaAffected", networkErrorLogDto.getAreaAffected(), Field.Store.YES, Field.Index.UN_TOKENIZED));
	doc.add(new Field("problemDesc", networkErrorLogDto.getProblemDesc(), Field.Store.YES, Field.Index.UN_TOKENIZED));
	doc.add(new Field("loggingTime", Utility.getCurrentTime_(), Field.Store.YES, Field.Index.UN_TOKENIZED));
	doc.add(new Field("tat", networkErrorLogDto.getResoTATHH()+":"+networkErrorLogDto.getResoTATMM(), Field.Store.YES, Field.Index.UN_TOKENIZED));
	doc.add(new Field("status", networkErrorLogDto.getStatus(), Field.Store.YES, Field.Index.UN_TOKENIZED));
	
	String contents = networkErrorLogDto.getAreaAffected();
	Reader reader = new StringReader(contents);
	
	doc.add(new Field("locations", reader));

  // return the document
  return doc;
}

  private FileLocation() {}
}
    
