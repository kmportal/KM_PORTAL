/*
 * @autor Karan Deep Singh
 * 19 Oct 2012*/

package com.ibm.km.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.log4j.Logger;

import com.ibm.km.common.KeyChar;
import com.ibm.km.common.PropertyReader;
import com.ibm.km.dao.DBConnection;
import com.ibm.km.dao.KeywordMgmtDao;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;

public class KeywordMgmtDaoImpl implements KeywordMgmtDao  {
	
	private static Logger logger = Logger.getLogger(KeywordMgmtDaoImpl.class);

	protected static final String SELECT_KEYWORD_LIKE ="SELECT KEYWORD FROM KM_KEYWORD_VIEW  " +
			" WHERE KEYWORD LIKE ? AND (DAYS(CURRENT TIMESTAMP) - DAYS(LAST_ACCESSED_DATE)) < 90 " +
			" ORDER BY COUNT DESC FETCH FIRST "; 
	
	protected static final String SELECT_WORD ="SELECT * FROM KM_KEYWORD_VIEW ORDER BY COUNT DESC WITH UR"; 
	
	protected static final String UPDATE_COUNT = "UPDATE KM_KEYWORD_VIEW SET COUNT=COUNT+1, " +
			" LAST_ACCESSED_DATE = CURRENT TIMESTAMP WHERE KEYWORD = ?  WITH UR";
	
	protected static final String INSERT_KEYWORD = "INSERT INTO KM_KEYWORD_VIEW(KEYWORD, COUNT, " +
			" LAST_ACCESSED_DATE)  VALUES(?, 1, CURRENT TIMESTAMP) WITH UR"; 
	
	protected static final String SELECT_KEYWORD = "SELECT KEYWORD FROM KM_KEYWORD_VIEW WHERE " +
			" KEYWORD = ? WITH UR";

	
	public boolean checkKeyword(String keyword) throws KmException {
		Connection con=null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean result = false;
		
		try {
				con = DBConnection.getDBConnection();
				ps = con.prepareStatement(SELECT_KEYWORD);
				ps.setString(1,keyword);
				rs = ps.executeQuery();
				
				if(rs.next())
					result = true;
				return result;
		} 
		catch (Exception e) {
			e.printStackTrace();
			throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try{
				DBConnection.releaseResources(con,ps,rs);
			}catch(DAOException e){
				logger.error(e);
				throw new KmException(e.getMessage(),e);
			}				
		}	
	
	}//getKeyword

	
	public ArrayList<String> getMatchingKeywords(String keyword)
			throws KmException {
		Connection con=null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		ArrayList<String> keywordList = new ArrayList<String>();
		try {
			keyword = keyword.toLowerCase();
			
				if(keyword.length() > Integer.parseInt(PropertyReader.getAppValue("initial.characters.allowed")))
					keyword = "%"+keyword+"%";
				else
					keyword = keyword+"%";
				
				String query = SELECT_KEYWORD_LIKE + PropertyReader.getAppValue("total.search.results") + " ROWS ONLY WITH UR";
				
				con = DBConnection.getDBConnection();
				ps = con.prepareStatement(query);
				ps.setString(1,keyword);
				rs = ps.executeQuery();
				
				while(rs.next())
				{
					keywordList.add(rs.getString("KEYWORD"));
				}
				return keywordList;
		} 
		catch (Exception e) {
			e.printStackTrace();
			throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try{
				DBConnection.releaseResources(con,ps,rs);
			}catch(DAOException e){
				logger.error(e);
				throw new KmException(e.getMessage(),e);
			}				
		}
	}//getMatchingKeywords


	public int insertKeyword(String keyword) throws KmException {
		logger.info("Inserting for table KM_KEYWORD_VIEW:" + keyword);

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int rowsUpdated = 0;
		
		try {
			keyword = keyword.toLowerCase();
			
			if(!checkKeyword(keyword))
			{
				con = DBConnection.getDBConnection();
				con.setAutoCommit(false);
				ps = con.prepareStatement(INSERT_KEYWORD);
				ps.setString(1,keyword);
				rowsUpdated=ps.executeUpdate();
				con.commit();
				KeyChar kc = KeyChar.getRoot();
				if(kc != null) {
					kc.addNode(keyword);
				}
				//System.out.println("\nInserting Keyword..........");
			}
			else
			{
				 //System.out.println("\nUpdating Count..........");
				 updateCount(keyword);
			}	 

		} catch (SQLException e) {
			e.printStackTrace();	
			throw new KmException("Exception: " + e.getMessage(), e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try{
				DBConnection.releaseResources(con,ps,rs);
			}catch(DAOException e){
				logger.error(e);
				throw new KmException(e.getMessage(),e);
			}				
		}

		return rowsUpdated;

	}//insertKeyword

	
	public int updateCount(String keyword) throws KmException {
		Connection con=null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		int rowsUpdated = 0;
		try {
			con=DBConnection.getDBConnection();
			ps=con.prepareStatement(UPDATE_COUNT);
			ps.setString(1,keyword);
			rowsUpdated = ps.executeUpdate();
	         
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KmException("SQLException: " + e.getMessage(), e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try{
				DBConnection.releaseResources(con,ps,rs);
			}catch(DAOException e){
				logger.error(e);
				throw new KmException(e.getMessage(),e);
			}
		}
		return rowsUpdated;
	}//	updateCount

	//@Override
	public ArrayList<String> getKeywords() throws KmException{

		Connection con=null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		ArrayList<String> keywordList = new ArrayList<String>();
		try {
				String query = SELECT_WORD;
				con = DBConnection.getDBConnection();
				ps = con.prepareStatement(query);
				rs = ps.executeQuery();
				while(rs.next())
				{
					keywordList.add(rs.getString("KEYWORD"));
				}
				return keywordList;
		} 
		catch (Exception e) {
			e.printStackTrace();
			throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try{
				DBConnection.releaseResources(con,ps,rs);
			}catch(DAOException e){
				logger.error(e);
				throw new KmException(e.getMessage(),e);
			}				
		}
		}

	
	
}
