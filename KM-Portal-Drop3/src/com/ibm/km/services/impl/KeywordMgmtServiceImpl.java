package com.ibm.km.services.impl;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.ibm.km.common.KeyChar;
import com.ibm.km.common.PropertyReader;
import com.ibm.km.common.Utility;
import com.ibm.km.dao.impl.KeywordMgmtDaoImpl;
import com.ibm.km.dao.impl.KmDocumentMstrDaoImpl;
import com.ibm.km.exception.KmDocumentMstrDaoException;
import com.ibm.km.exception.KmException;
import com.ibm.km.services.KeywordMgmtService;


public class KeywordMgmtServiceImpl implements KeywordMgmtService {
	
	private static Logger logger = Logger.getLogger(KeywordMgmtServiceImpl.class);

	
	public boolean checkKeyword(String keyword) throws KmException {
		KeywordMgmtDaoImpl keyMgmt=new KeywordMgmtDaoImpl();
		try{
			return keyMgmt.checkKeyword(keyword);
		}catch (KmException e) {
			logger.error("Exception Occurred : " + e.getMessage());
				throw new KmException("KmException: " + e.getMessage(), e);
			}
	}

	
	public ArrayList<String> getMatchingKeywords(String keyword)
			throws KmException {
		try{
			String s = PropertyReader.getAppValue("keyword.search.method");
			if(s != null && s.equals("MEMORY")) {
				return Utility.getMatch(keyword);	
			} else {
				KeywordMgmtDaoImpl keyMgmt=new KeywordMgmtDaoImpl();
				return keyMgmt.getMatchingKeywords(keyword);
			}
		}catch (KmException e) {
			logger.error("Exception Occurred : " + e.getMessage());
				throw new KmException("KmException: " + e.getMessage(), e);
			}
	}

	
	public void insert() throws KmException {
		String s = PropertyReader.getAppValue("keyword.search.method");
		if(s != null && s.equals("MEMORY")) {
		KeywordMgmtDaoImpl keyMgmt=new KeywordMgmtDaoImpl();
		try{
			logger.info("Creating Keyword Index .........");
			KeyChar kc = KeyChar.getRoot();
			if(kc == null) {
				logger.info("Creating new Index .........");
			ArrayList<String> value = keyMgmt.getKeywords();
			Utility.insertWords(value);
			}
		}catch (KmException e) {
			logger.error("Exception Occurred : " + e.getMessage());
				throw new KmException("KmException: " + e.getMessage(), e);
			}
		}
	}

	
	public int insertKeyword(String keyword) throws KmException {
		KeywordMgmtDaoImpl keyMgmt=new KeywordMgmtDaoImpl();
		logger.info("Inserting Keyword.." + keyword);
		try{
			return keyMgmt.insertKeyword(keyword);
		}catch (KmException e) {
			logger.error("Exception Occurred : " + e.getMessage());
				throw new KmException("KmException: " + e.getMessage(), e);
			}
	}

	
	public int updateCount(String keyword) throws KmException {
		KeywordMgmtDaoImpl keyMgmt=new KeywordMgmtDaoImpl();
		logger.info("Updating Count..");
		try{
			return keyMgmt.updateCount(keyword);
		}catch (KmException e) {
			logger.error("Exception Occurred : " + e.getMessage());
				throw new KmException("KmException: " + e.getMessage(), e);
			}
	}

}
