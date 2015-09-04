package com.ibm.km.dao;

import java.util.ArrayList;

import com.ibm.km.exception.KmException;

public interface KeywordMgmtDao {
	
	public ArrayList<String> getMatchingKeywords(String keyword) throws KmException;
	
	public int updateCount(String keyword) throws KmException;
	
	public int insertKeyword(String keyword) throws KmException;
	
	public boolean checkKeyword(String keyword) throws KmException;

	public ArrayList<String> getKeywords() throws KmException;

}
