package com.ibm.km.search;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import java.io.File;
import java.util.Date;
import java.util.Arrays;

/** Indexer for HTML files. */
public class IndexHTML {
	
	private static final Logger logger;

	static {

		logger = Logger.getLogger(IndexHTML.class);
	}
	
	
  private IndexHTML() {}

  private static boolean deleting = false;	  // true during deletion pass
  private static IndexReader reader;		  // existing index
  private static IndexWriter writer;		  // new index being built
  private static TermEnum uidIter;		  // document id iterator

  private static void indexDocs(File file, String index, boolean create)
       throws Exception {
    if (!create) {				  // incrementally update

      reader = IndexReader.open(index);		  // open existing index
      uidIter = reader.terms(new Term("uid", "")); // init uid iterator

      indexDocs(file);

      if (deleting) {				  // delete rest of stale docs
        while (uidIter.term() != null && uidIter.term().field() == "uid") {
          logger.info("deleting " +
              HTMLDocument.uid2url(uidIter.term().text()));
          reader.deleteDocuments(uidIter.term());
          uidIter.next();
        }
        deleting = false;
      }

      uidIter.close();				  // close uid iterator
      reader.close();				  // close existing index

    } else					  // don't have exisiting
      indexDocs(file);
  }

  private static void indexDocs(File file) throws Exception {
    if (file.isDirectory()) {			  // if a directory
      String[] files = file.list();		  // list its files
      Arrays.sort(files);			  // sort the files
      for (int i = 0; i < files.length; i++)	  // recursively index them
        indexDocs(new File(file, files[i]));

    } else if (file.getPath().endsWith(".html") || // index .html files
      file.getPath().endsWith(".htm") || // index .htm files
      file.getPath().endsWith(".txt")) { // index .txt files

      if (uidIter != null) {
        String uid = HTMLDocument.uid(file);	  // construct uid for doc

        while (uidIter.term() != null && uidIter.term().field() == "uid" &&
            uidIter.term().text().compareTo(uid) < 0) {
          if (deleting) {			  // delete stale docs
            logger.info("deleting " +
                HTMLDocument.uid2url(uidIter.term().text()));
            reader.deleteDocuments(uidIter.term());
          }
          uidIter.next();
        }
        if (uidIter.term() != null && uidIter.term().field() == "uid" &&
            uidIter.term().text().compareTo(uid) == 0) {
          uidIter.next();			  // keep matching docs
        } else if (!deleting) {			  // add new docs
          Document doc = HTMLDocument.Document(file);
          logger.info("adding " + doc.get("path"));
          writer.addDocument(doc);
        }
      } else {					  // creating a new index
        Document doc = HTMLDocument.Document(file);
        logger.info("adding " + doc.get("path"));
        writer.addDocument(doc);		  // add docs unconditionally
      }
    }
  }
}
