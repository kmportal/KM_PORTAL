package com.ibm.km.networkfault;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.store.AlreadyClosedException;

import com.ibm.km.common.Utility;

/** Simple command-line based search demo. */
public class SearchFault {

	private static final Logger logger;

	public static void main(String[] args) {
		try {
			Hits hits = new SearchFault().search("delhi");

			for (int index = 0; index < hits.length(); index++) {
				Document document = hits.doc(index);
				//System.out.println("");

				//System.out.println((index + 1) + "Problem Desc : "
					//	+ document.get("documentId"));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	static {

		logger = Logger.getLogger(SearchFault.class);
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

	public Hits search(String location) throws Exception {

		String index_test = Utility.getNetworkFaultIndexPath(); // gets the path for index 
		String field = "locations";
		String queries = null;
		boolean raw = false;
		String normsField = null;

		Hits hits = null;
		try {

			IndexReader reader = IndexReader.open(index_test);

			if (normsField != null)
				reader = new OneNormsReader(reader, normsField);

			Searcher searcher = new IndexSearcher(reader);
			Analyzer analyzer = new StandardAnalyzer();

			BufferedReader in = null;
			if (queries != null) {
				in = new BufferedReader(new FileReader(queries));
			} else {
				in = new BufferedReader(new InputStreamReader(System.in,
						"UTF-8"));
			}
			QueryParser parser = new QueryParser(field, analyzer);
			if (location.length() == 0)
				return null;

			Query query = parser.parse(location);

			logger.info("Searching for...... " + query.toString(field));
			SortField[] sortFields = new SortField[2];
			sortFields[0] =new SortField("circleName");
			sortFields[1] =new SortField("loggingTime",true);
			hits = searcher.search(query,new Sort(sortFields));

			logger.info(hits.length() + " total matching documents");

			for (int i = 0; i < hits.length(); i++) {

				Document doc = hits.doc(i);

				if (queries != null) // non-interactive
					break;
			}

		} catch (AlreadyClosedException e) {
			logger.info("Exception occured during content search");
		} catch (Exception e) {
			logger.info("Exception occured during content search");
		} finally {
			return hits;
		}
	}

	
}
