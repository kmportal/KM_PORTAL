/*
 * Created on Feb 12, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.common;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;

/**
 * A simple example of an in-memory search using Lucene.
 */


public class DocumentSearch {

	/**
	 * Logger for the class.
	**/
	private static final Logger logger;

	static {

		logger = Logger.getLogger(DocumentSearch.class);
	}

	/**
	 * Make a Document object with an un-indexed title field and an
	 * indexed content field.
	 */
	private static Document createDocument(String title, String content) {
		Document doc = new Document();

		// Add the title as an unindexed field...
//		doc.add(Field.UnIndexed("title", title));
		doc.add(new Field("title", title, Field.Store.YES,
				Field.Index.TOKENIZED));

		
		doc.add(new Field("content", content, Field.Store.YES,
				Field.Index.TOKENIZED));

		return doc;
	}

	/**
	 * Searches for the given string in the "content" field
	 */
	private static void search(Searcher searcher, String queryString)
		throws ParseException, IOException {

		// Build a Query object
//		Query query = QueryParser.parse(queryString, "content", new StandardAnalyzer());

		QueryParser parser = new QueryParser("content", new StandardAnalyzer());
		Query query = parser.parse(queryString);
		// Search for the query
		Hits hits = searcher.search(query);

		// Examine the Hits object to see if there were any matches
		int hitCount = hits.length();
		if (hitCount == 0) {
			logger.info(
				"No matches were found for \"" + queryString + "\"");
		}
		else {
			logger.info("Hits for \"" +
				queryString + "\" were found in quotes by:");

			// Iterate over the Documents in the Hits object
			for (int i = 0; i < hitCount; i++) {
				Document doc = hits.doc(i);

				// Print the value that we stored in the "title" field. Note
				// that this Field was not indexed, but (unlike the
				// "contents" field) was stored verbatim and can be
				// retrieved.
				//logger.info("  " + (i + 1) + ". " + doc.get("title"));
			}
		}
		
	}
}