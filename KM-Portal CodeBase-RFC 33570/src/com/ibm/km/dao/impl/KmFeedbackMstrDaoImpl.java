/*
 * Created on Apr 28, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.dao.impl;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.ibm.km.common.PropertyReader;
import com.ibm.km.dao.DBConnection;
import com.ibm.km.dao.KmFeedbackMstrDao;
import com.ibm.km.dto.KmFeedbackMstr;

import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmDocumentMstrDaoException;
import com.ibm.km.exception.KmException;
import java.util.*;
import java.sql.*;

/**
 * @author Anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class KmFeedbackMstrDaoImpl implements KmFeedbackMstrDao{
	/*
		 * Logger for the class.
		 */
	private static final Logger logger;

	static {

		logger = Logger.getLogger(KmFeedbackMstrDaoImpl.class);
	}
	
	protected static final String SQL_INSERT_WITH_ID ="INSERT INTO KM_FEEDBACK_MSTR (FEEDBACK_ID, COMMENT, CREATED_DT, CREATED_BY, CIRCLE_ID, CATEGORY_ID, SUB_CATEGORY_ID, ELEMENT_ID, READ_STATUS) VALUES ( NEXTVAL FOR KM_FEEDBACK_ID,?, current timestamp, ?, ?, 0, 0,?,'N')";
	
	public static final String SQL_UPDATE_STATUS = "UPDATE KM_FEEDBACK_MSTR SET READ_STATUS =?,FEEDBACK_RESP=?, UPDATED_DT=current timestamp WHERE FEEDBACK_ID = ?";
	
	protected static final String SQL_SELECT_FEEDBACKS ="WITH nee(element_id,chain,element_level_id,element_name) AS  ( SELECT  ELEMENT_ID,  cast(element_name as VARCHAR(" + PropertyReader.getAppValue("search.path.size") + ")) ,element_level_id,element_name  FROM KM_ELEMENT_MSTR  WHERE element_id = ?  UNION ALL   SELECT nplus1.element_id,nee.chain || '/' || nplus1.element_name,nplus1.element_level_id,nplus1.element_name  FROM KM_ELEMENT_MSTR as nplus1, nee  WHERE nee.element_id=nplus1.PARENT_ID) SELECT  fed.FEEDBACK_ID, chain,fed.COMMENT, fed.ELEMENT_ID, nee.ELEMENT_NAME, fed.CREATED_BY, fed.CREATED_DT, usr.USER_LOGIN_ID FROM KM_FEEDBACK_MSTR fed inner join NEE  on fed.ELEMENT_ID=nee.ELEMENT_ID inner join KM_USER_MSTR usr on usr.USER_ID=fed.CREATED_BY WHERE  fed.READ_STATUS='N'  ORDER BY fed.CREATED_DT  ";
	
   
   protected static final String SQL_FEEDBACK_RESP="SELECT COMMENT , CREATED_DT, UPDATED_DT,READ_STATUS,FEEDBACK_RESP FROM KM_FEEDBACK_MSTR WHERE CREATED_BY=?   ORDER BY UPDATED_DT DESC ";
   
   protected static final String SQL_FEEDBACK_REPORT ="WITH nee(element_id,chain,element_level_id,element_name) AS  ( SELECT  ELEMENT_ID,  cast(element_name as VARCHAR(" + PropertyReader.getAppValue("search.path.size") + ")) ,element_level_id,element_name  FROM KM_ELEMENT_MSTR  WHERE element_id = ?  UNION ALL   SELECT nplus1.element_id,nee.chain || '/' || nplus1.element_name,nplus1.element_level_id,nplus1.element_name FROM KM_ELEMENT_MSTR as nplus1, nee  WHERE nee.element_id=nplus1.PARENT_ID) SELECT fed.CREATED_DT,fed.UPDATED_DT,usr.USER_LOGIN_ID, fed.COMMENT, fed.READ_STATUS FROM KM_FEEDBACK_MSTR fed inner join NEE  on fed.ELEMENT_ID=nee.ELEMENT_ID inner join KM_USER_MSTR usr on usr.USER_ID=fed.CREATED_BY WHERE fed.READ_STATUS !='N'   and date(fed.CREATED_DT) between DATE(timestamp_format(?, 'YYYY-MM-DD HH24:MI:SS')) and DATE(timestamp_format(?, 'YYYY-MM-DD HH24:MI:SS'))  ORDER BY fed.CREATED_DT";
	/* (non-Javadoc)
	 * @see com.ibm.km.dao.KmFeedbackMstrDao#insert(com.ibm.km.dto.KmFeedbackMstr)
	 */
	public void insert(KmFeedbackMstr dto) throws KmException {

		logger.info("Entered insert for table KM_FEEDBACK_MSTR");

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		int rowsUpdated = 0;
		try {
			StringBuffer query=new StringBuffer(SQL_INSERT_WITH_ID);
			//String sql = SQL_INSERT_WITH_ID;
			con = DBConnection.getDBConnection();
			logger.info("Created by:" + dto.getCreatedBy() + " CircleID:" + dto.getCircleId() + " ElementID:" + dto.getElementId());
			ps = con.prepareStatement(query.append(" with ur").toString());
			int paramCount = 1;
			
			/*Preparing the statement for insertion */
			
			ps.setString(paramCount++, dto.getComment());
			ps.setInt(paramCount++, Integer.parseInt(dto.getCreatedBy()));
			ps.setInt(paramCount++, Integer.parseInt(dto.getCircleId()));
			ps.setInt(paramCount++, Integer.parseInt(dto.getElementId()));
			rowsUpdated = ps.executeUpdate();
			

			logger.info(
				"Row insertion successful on table:KM_FEEDBACK_MSTR. Inserted:"
					+ rowsUpdated
					+ " rows");

		} catch (SQLException e) {
			
			logger.error(
				"SQL Exception occured while inserting." + "Exception Message: " + e.getMessage());
			throw new KmException("SQLException: " + e.getMessage(), e);
		} catch (Exception e) {
			logger.error(
				"Exception occured while inserting." + "Exception Message: " + e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try {
				
				if (con != null) {
					con.setAutoCommit(true);
					con.close();
				}
				DBConnection.releaseResources(null,ps,rs);
			} catch (Exception e) {
				logger.error(
				"Exception occured while inserting." + "Exception Message: " + e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
			}
		}

		
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.dao.KmFeedbackMstrDao#viewFeedbacks(java.lang.String)
	 */
	public ArrayList viewFeedbacks(String[] elementIds,String elementId) throws KmException {
		logger.info("Entered fetch from table KM_FEEDBACK_MSTR");

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList feedbackList=new ArrayList();
		KmFeedbackMstr dto=null;
		
		try {
			StringBuffer query=new StringBuffer(SQL_SELECT_FEEDBACKS);
			//Code change after UAT Observation
			//String sql = SQL_SELECT_FEEDBACKS;
		/*	for(int i=1;i<elementIds.length;i++){
						query.append(" OR fed.ELEMENT_ID=? ").toString();		
			}
			query.append(" ) ORDER BY fed.CREATED_DT    ").toString();*/
			con = DBConnection.getDBConnection();
			//logger.info(sql);
			////System.out.println(sql);
			ps = con.prepareStatement(query.append(" with ur").toString());
			
			/*Defect MASDB00064285 fixed: Element path for the feedback was starting from India. */
			ps.setInt(1, Integer.parseInt(elementId));
		/*	for(int i=0;i<elementIds.length;i++){
				ps.setInt(i+2, Integer.parseInt(elementIds[i]));	
			} */
			
			rs=ps.executeQuery();
			while(rs.next()){
				dto=new KmFeedbackMstr();
				String path=rs.getString("chain");
				/*Defect MASDB00064285 fixed: Element path for the feedback was starting from India. */
				String feedbackStringPath="";
				if((path.indexOf("/"))> -1){	
				feedbackStringPath=path.substring(path.indexOf("/")+1);
				dto.setFeedbackStringPath(feedbackStringPath);
				}else{
				dto.setFeedbackStringPath("");
				}
				dto.setComment(rs.getString("COMMENT"));
				dto.setElementName(rs.getString("ELEMENT_NAME"));
				dto.setCreatedBy(rs.getString("USER_LOGIN_ID"));
				dto.setCreatedDate(rs.getDate("CREATED_DT"));
				dto.setFeedbackId(rs.getString("FEEDBACK_ID"));
				feedbackList.add(dto);
			}

			logger.info("Feedbacklist"+feedbackList);
			logger.info(
				"Feedbacks are fetched from the table:KM_FEEDBACK_MSTR. ");
					

		} catch (SQLException e) {
		
			logger.error(
				"SQL Exception viewing feedback." + "Exception Message: " + e.getMessage());
			throw new KmException("SQLException: " + e.getMessage(), e);
		} catch (Exception e) {
			
			logger.error(
				"Exception occured while viewing feedback." + "Exception Message: " + e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try {
				
				if (con != null) {
					con.setAutoCommit(true);
					con.close();
				}
				DBConnection.releaseResources(null,ps,rs);
			} catch (Exception e) {
		logger.error("DAO Exception occured while inserting." + "Exception Message: " + e.getMessage());
			throw new KmException("DAO Exception: " + e.getMessage(), e);
			}
		}
		return feedbackList;
		
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.dao.KmFeedbackMstrDao#readFeedbacks(java.lang.String[])
	 */
	public void readFeedbacks(String[] readFeedbacks,String[] feedbackResp, String[] feedBackId) throws DAOException {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		KmFeedbackMstr dto;
		ArrayList fileList = new ArrayList();
		logger.info("Entered read feedback for table KM_FEEDBACK_MSTR");
			
		try
		{
			StringBuffer query=new StringBuffer(SQL_UPDATE_STATUS);
			con = DBConnection.getDBConnection();
			//Query SQL_UPDATE_STATUS changed for Feedback Combo box -Atul
			pstmt = con.prepareStatement(query.append(" with ur").toString());
			int i = 0;
				
			while( i < readFeedbacks.length )
			{ 
				if(!readFeedbacks[i].equals("N")){
					pstmt.setString(1,readFeedbacks[i]);
					//For inserting feedback response - Added by Atul
				
					pstmt.setString(2,feedbackResp[i]);
					//Ended by Atul
				
					pstmt.setInt(3,Integer.parseInt(feedBackId[i]));
				
					pstmt.addBatch();
				}
					i++;
			}
			pstmt.executeBatch();
			con.commit();
			logger.info("exit from  read feedback for table KM_FEEDBACK_MSTR");
		}
		catch(KmException e)
		{
			logger.error("KM Exception occured while reading feedback." + "Exception Message: " + e.getMessage());
		throw new KmException("KM Exception: " + e.getMessage(),e);
		}
		catch(SQLException e)
		{
			logger.error("SQL Exception occured while reading feedback." + "Exception Message: " + e.getMessage());
		throw new KmException("SQL Exception: " + e.getMessage(), e);
		}
		finally
		{
			try
			{
				DBConnection.releaseResources(con,pstmt,null);
			}
			catch(Exception e)
			{
				logger.error("DAO Exception occured while reading feedback." + "Exception Message: " + e.getMessage());
			throw new KmException("DAO Exception: " + e.getMessage(), e);
			}
		}	
	}

	
	public ArrayList feedbackResponseAll(int userId) throws DAOException {
		ArrayList feedbackRespList=null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs= null;
		KmFeedbackMstr feedbackDto=null;
		//for CircleId
		//String sql = "SELECT COMMENT , CREATED_DT, UPDATED_DT,READ_STATUS,FEEDBACK_RESP FROM KM_FEEDBACK_MSTR WHERE ELEMENT_ID in (select element_id from KM_ELEMENT_MSTR where PARENT_ID=?) AND UPDATED_DT IS NOT NULL ORDER BY UPDATED_DT DESC ";
		//or
		// for LOBId
		//String sql = "SELECT COMMENT , CREATED_DT, UPDATED_DT,READ_STATUS,FEEDBACK_RESP FROM KM_FEEDBACK_MSTR WHERE ELEMENT_ID in (select element_id from KM_ELEMENT_MSTR where PARENT_ID in (select element_id from KM_ELEMENT_MSTR where PARENT_ID=?)) ORDER BY UPDATED_DT DESC ";
		String sql = "SELECT COMMENT , CREATED_DT, UPDATED_DT,READ_STATUS,FEEDBACK_RESP FROM KM_FEEDBACK_MSTR WHERE CIRCLE_ID=?";
		try
		{
			con = DBConnection.getDBConnection();
			StringBuffer query=new StringBuffer(sql);
			pstmt = con.prepareStatement(query.append(" fetch first 100 ROWS only with ur").toString());
			pstmt.setInt(1, userId);
			rs=pstmt.executeQuery();
			feedbackRespList=new ArrayList();
			while(rs.next()) {
				feedbackDto= new KmFeedbackMstr();
				feedbackDto.setComment(rs.getString("COMMENT"));
				feedbackDto.setFeedbackResponse(rs.getString("FEEDBACK_RESP"));
				feedbackDto.setCreatedDate(rs.getDate("CREATED_DT"));
				feedbackDto.setUpdatedDate(rs.getDate("UPDATED_DT"));
				if(rs.getString("READ_STATUS").equals("I")) {
					feedbackDto.setReadStatus("Incorporated");
				}
				else if(rs.getString("READ_STATUS").equals("R")) {
					feedbackDto.setReadStatus("Rejected");	
				}
				else {
					feedbackDto.setReadStatus("Not Read");
				}
				feedbackRespList.add(feedbackDto);
			}

		}catch(SQLException sqle){
			sqle.printStackTrace();
		}
		
		return feedbackRespList;
	}
	/* (non-Javadoc)
	 * @see com.ibm.km.dao.KmFeedbackMstrDao#feedbackResponse(java.lang.String)
	 * Method for showing feedback response to CSR user - Added by Atul
	 */
	public ArrayList feedbackResponse(int userId) throws DAOException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs= null;
		KmFeedbackMstr feedbackDto=null;
		ArrayList feedbackRespList=null;
		logger.info("Entered read feedback for table KM_FEEDBACK_MSTR in feedbackresponse(String userId)");
			
		try
		{
			con = DBConnection.getDBConnection();
			StringBuffer query=new StringBuffer(SQL_FEEDBACK_RESP);
			pstmt = con.prepareStatement(query.append(" fetch first 100 ROWS only with ur").toString());
			pstmt.setInt(1, userId);
			rs=pstmt.executeQuery();
			feedbackRespList=new ArrayList();
			
			while(rs.next()) {
				feedbackDto= new KmFeedbackMstr();
				feedbackDto.setComment(rs.getString("COMMENT"));
				feedbackDto.setFeedbackResponse(rs.getString("FEEDBACK_RESP"));
				feedbackDto.setCreatedDate(rs.getDate("CREATED_DT"));
				feedbackDto.setUpdatedDate(rs.getDate("UPDATED_DT"));
				if(rs.getString("READ_STATUS").equals("I")) {
					feedbackDto.setReadStatus("Incorporated");
				}
				else if(rs.getString("READ_STATUS").equals("R")) {
					feedbackDto.setReadStatus("Rejected");	
				}
				else {
					feedbackDto.setReadStatus("Not Read");
				}
				feedbackRespList.add(feedbackDto);
			}
			
			
			logger.info("exit from  read feedback for table KM_FEEDBACK_MSTR");
		}
		catch(KmException e)
		{
			logger.error("KM Exception occured while reading feedback response." + "Exception Message: " + e.getMessage());
		throw new KmException("KM Exception: " + e.getMessage(),e);
		}
		catch(SQLException e)
		{
			logger.error("SQL Exception occured while reading feedback response." + "Exception Message: " + e.getMessage());
		throw new KmException("SQL Exception: " + e.getMessage(), e);
		}
		finally
		{
			try
			{
				DBConnection.releaseResources(con,pstmt,rs);
			}
			catch(Exception e)
			{
				logger.error("DAO Exception occured while reading feedback response." + "Exception Message: " + e.getMessage());
			throw new KmException("DAO Exception: " + e.getMessage(), e);
			}
		}	
	  return feedbackRespList;
	}
	/* (non-Javadoc)
	 * @see com.ibm.km.dao.KmFeedbackMstrDao#feedbackReport(java.lang.String)
	 */
	public ArrayList feedbackReport(String[] elementIds,String elementId,String startDate, String endDate) throws KmException {
		logger.info("Entered fetch from table KM_FEEDBACK_MSTR");

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList feedbackList=new ArrayList();
		KmFeedbackMstr dto=null;
		
		try {
			StringBuffer query=new StringBuffer(SQL_FEEDBACK_REPORT);
			con = DBConnection.getDBConnection();
			ps = con.prepareStatement(query.append(" with ur").toString());
			
			ps.setInt(1, Integer.parseInt(elementId));
			ps.setString(2, startDate+ " 00:00:00");
	        ps.setString(3,endDate+ " 00:00:00");
			rs=ps.executeQuery();
			while(rs.next()){
				dto=new KmFeedbackMstr();
				dto.setCreatedDate(rs.getDate("CREATED_DT"));
				dto.setUpdatedDate(rs.getDate("UPDATED_DT"));
				dto.setCreatedBy(rs.getString("USER_LOGIN_ID"));
				dto.setComment(rs.getString("COMMENT"));
				if(rs.getString("READ_STATUS").equals("I")) {
					dto.setReadStatus("Incorporated");
				}
				else if(rs.getString("READ_STATUS").equals("R")) {
					dto.setReadStatus("Rejected");	
				}
				feedbackList.add(dto);
			}

			logger.info("Feedbacklist"+feedbackList);
			logger.info(
				"Feedbacks are fetched from the table:KM_FEEDBACK_MSTR. ");
					

		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(
				"SQL Exception feedbackReport." + "Exception Message: " + e.getMessage());
			throw new KmException("SQLException: " + e.getMessage(), e);
		} catch (Exception e) {
			
			logger.error(
				"Exception occured while feedbackReport." + "Exception Message: " + e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try {
				
				if (con != null) {
					con.setAutoCommit(true);
					con.close();
				}
				DBConnection.releaseResources(null,ps,rs);
			} catch (Exception e) {
		logger.error("DAO Exception occured while feedbackReport" + "Exception Message: " + e.getMessage());
			throw new KmException("DAO Exception: " + e.getMessage(), e);
			}
		}
		return feedbackList;
		
	}
}