/*
 * Created on Feb 11, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.log4j.Logger;

import com.ibm.km.dao.AddFileDAO;
import com.ibm.km.dao.DBConnection;
import com.ibm.km.dto.AddFileDTO;
import com.ibm.km.dto.KmDocumentMstr;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;
import com.ibm.km.exception.KmUserMstrDaoException;

/**
 * @author Vipin
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AddFileDaoImpl implements AddFileDAO {

	
	// Changes after UAT
	
	public static final String SQL_INSERT_FILE_INFO ="INSERT INTO KM_DOCUMENT_MSTR(DOCUMENT_ID, DOCUMENT_GROUP_ID, DOCUMENT_NAME,DOC_NAME,DOCUMENT_DISPLAY_NAME, DOCUMENT_DESC,DOCUMENT_KEYWORD,NUMBER_OF_HITS, STATUS, APPROVAL_STATUS, UPLOADED_BY, UPLOADED_DT, UPDATED_BY, UPDATED_DT, APPROVAL_REJECTION_BY, PUBLISHING_START_DT, PUBLISHING_END_DT, DOCUMENT_PATH, CATEGORY_ID, SUB_CATEGORY_ID, CIRCLE_ID,ELEMENT_ID, DOC_TYPE) VALUES(?, 0, ?, ?, ?, ?, ?, 0, 'A', ?, ?, current timestamp, ?, current timestamp, 0, timestamp_format(?, 'YYYY-MM-DD HH24:MI:SS'), timestamp_format(?, 'YYYY-MM-DD HH24:MI:SS') , ?, 1, 1, 1,?, ?)";
	
	public static final String SQL_INSERT_ELEMENT_DOCUMENT= "INSERT INTO KM_ELEMENT_MSTR(ELEMENT_ID, ELEMENT_NAME, ELEMENT_DESC, PARENT_ID, ELEMENT_LEVEL_ID, PAN_STATUS,STATUS, CREATED_BY, CREATED_DT) VALUES(?, ?, ?, ?, 0, 'N', 'A', ?, CURRENT TIMESTAMP)";
								
	//Changed	
	public static final String SQL_CHECK_DUPLCATE_FILE = "select a.APPROVAL_STATUS,a.ELEMENT_ID, a.DOCUMENT_NAME from KM_DOCUMENT_MSTR a inner join KM_ELEMENT_MSTR b on  a.element_id=b.ELEMENT_ID where b.ELEMENT_LEVEL_ID=0 and b.PARENT_ID=? and a.doc_name=? and a.status='A' and b.status='A' AND (DATE(CURRENT TIMESTAMP) BETWEEN DATE(a.PUBLISHING_START_DT) AND DATE(a.PUBLISHING_END_DT))";
								
	//KM Phase2
	public static final String SQL_UPDATE_FILE = "UPDATE KM_DOCUMENT_MSTR SET DOCUMENT_NAME = ?,UPDATED_BY = ?,DOCUMENT_KEYWORD = ?, DOCUMENT_DESC= ? ,PUBLISHING_START_DT = timestamp_format(? ,'YYYY-MM-DD HH24:MI:SS'), PUBLISHING_END_DT = timestamp_format(?,'YYYY-MM-DD HH24:MI:SS'),UPDATED_DT = current timestamp WHERE DOCUMENT_NAME = ? ";
													
	protected static final String SQL_GET_DOCUMENT_ID = "SELECT NEXTVAL FOR KM_DOCUMENT_ID_SEQ FROM SYSIBM.SYSDUMMY1";
	
	protected static final String SQL_GET_ELEMENT_ID = "SELECT NEXTVAL FOR KM_ELEMENT_ID_SEQ FROM SYSIBM.SYSDUMMY1";
	
	Logger logger = Logger.getLogger(AddFileDaoImpl.class);
	

	/* (non-Javadoc)
	 * @see com.ibm.km.dao.AddFileDAO#saveFileInfoInDB(com.ibm.km.dto.AddFileDTO)
	 */
	public String saveFileInfoInDB(KmDocumentMstr dto) throws KmException {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs=null;
		logger.info("Entered in saveFileInfoInDB method");
		try
		{
			StringBuffer query=new StringBuffer(SQL_GET_DOCUMENT_ID);
			//String sql1=SQL_GET_DOCUMENT_ID;
			con=getConnection();
			pstmt=con.prepareStatement(query.append(" with ur").toString());
			rs=pstmt.executeQuery();
			rs.next();
			String documentId=rs.getString(1);
			pstmt = con.prepareStatement(SQL_INSERT_FILE_INFO);
			pstmt.setString(1,documentId);
			pstmt.setString(2,dto.getDocumentName()); //DOCUMENT_NAME,DOC_NAME,DOCUMENT_DISPLAY_NAME
			System.out.println("DT: getDocumentName" + dto.getDocumentName());
			pstmt.setString(3,dto.getDocName().trim());
			System.out.println("DT: getDocName" + dto.getDocName());
			pstmt.setString(4,(dto.getDocumentDisplayName().trim()));
			System.out.println("DT: getDocumentDisplayName" + dto.getDocumentDisplayName());
			pstmt.setString(5,dto.getDocumentDesc());
			System.out.println("DT: getDocumentDesc" + dto.getDocumentDesc());
			pstmt.setString(6,dto.getKeyword());
			System.out.println("DT: getKeyword" + dto.getKeyword());
			pstmt.setString(7,dto.getApprovalStatus());
			System.out.println("DT: getApprovalStatus" + dto.getApprovalStatus());
			pstmt.setString(8,dto.getUploadedBy());
			System.out.println("DT: getUploadedBy" + dto.getUploadedBy());
			pstmt.setString(9,dto.getUpdatedBy());
			System.out.println("DT: getUpdatedBy" + dto.getUpdatedBy());
			// added publish date and publish end date of a new	
			System.out.println("DT:" + dto.getPublishingStartDate());
			pstmt.setString(10,dto.getPublishingStartDate()+ " 00:00:00");
			pstmt.setString(11,dto.getPublishingEndDate()+" 23:59:59");
			System.out.println("DT end:" + dto.getPublishingEndDate());
			
			pstmt.setString(12,dto.getDocumentPath());
			pstmt.setInt(13,Integer.parseInt(dto.getElementId()));
			pstmt.setInt(14, dto.getDocType());
			logger.info("Before executing  saveFileInfoInDB method. File1" +pstmt.toString());
			pstmt.executeUpdate();
			logger.info("Exit from saveFileInfoInDB method. File1" + Integer.parseInt(dto.getElementId()) + " documentId by:" + documentId);
			logger.info("Exit from saveFileInfoInDB method. File" + dto.getDocumentName() + " uploaded by:" + dto.getUploadedBy());
			return documentId;
		}
		catch(KmUserMstrDaoException e){
			e.printStackTrace();
			logger.error("KmUserMstrDaoException occured while saving FileInfoInDB." + "Exception Message: " + e.getMessage());
			throw new KmException(e.getMessage(),e);
		}
		catch(SQLException e){
			e.printStackTrace();
			logger.error("SQLException occured while saving FileInfoInDB." + "Exception Message: " + e.getMessage());
			throw new KmException(e.getMessage(),e);
		}
		catch(Exception e){
			e.printStackTrace();
			logger.error("Exception occured while saving FileInfoInDB." + "Exception Message: " + e.getMessage());
			throw new KmException(e.getMessage(),e);
		}
		finally
		{
			try
			{
				DBConnection.releaseResources(con,pstmt,rs);
			}
			catch(DAOException e)
			{
				logger.error("DAOException occured while saving FileInfoInDB." + "Exception Message: " + e.getMessage());
				throw new KmException(e.getMessage(),e);
			}
		}
		
	}
	
	public int saveDocumentAsElement(AddFileDTO dto) throws KmException {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs=null;
		
		logger.info("Entered in saveDocumentAsElement method");
		try
		{
			con = getConnection();
			StringBuffer query=new StringBuffer(SQL_GET_ELEMENT_ID);
			pstmt=con.prepareStatement(query.append(" with ur").toString());
			rs=pstmt.executeQuery();
			rs.next();
			int documentId=Integer.parseInt(rs.getString(1));
			pstmt = con.prepareStatement(SQL_INSERT_ELEMENT_DOCUMENT);
			pstmt.setString(1,documentId+"");
			pstmt.setString(2,(dto.getDocDisplayName().trim()));
			pstmt.setString(3,dto.getDocName());
			pstmt.setString(4,dto.getParentId());
			pstmt.setString(5,dto.getUserId());
			pstmt.executeUpdate();
			logger.info("Exit in saveDocumentAsElement method");
			return documentId;
		}
		catch(KmUserMstrDaoException e)
		{
			logger.error("KmUserMstrDaoException occured while saving DocumentAsElement." + "Exception Message: " + e.getMessage());
			throw new KmException(e.getMessage(),e);
		}
		catch(SQLException e)
		{
			logger.error("SQLException occured while saving DocumentAsElement." + "Exception Message: " + e.getMessage());
			throw new KmException(e.getMessage(),e);
		}
		finally
		{
			try
			{
				DBConnection.releaseResources(con,pstmt,rs);
			}
			catch(DAOException e)
			{
				logger.error("DAOException occured while saving DocumentAsElement." + "Exception Message: " + e.getMessage());
				throw new KmException(e.getMessage(),e);
			}
		}
		
	}	
	
	/* (non-Javadoc)
		 * @see com.ibm.km.dao.AddFileDAO#checkFile(java.lang.String)
		 */
	public KmDocumentMstr checkFile(String parentId, String fileName) throws KmException 
		{
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String status = null;
			KmDocumentMstr dto = null;
			
			logger.info("Entered in checkFile method");
			try
			{
				con = getConnection();
				StringBuffer query=new StringBuffer(SQL_CHECK_DUPLCATE_FILE);
				pstmt = con.prepareStatement(query.append(" with ur ").toString());
				pstmt.setInt(1,Integer.valueOf(parentId));
				pstmt.setString(2,fileName);
				//System.out.println("testingggg");
				rs = pstmt.executeQuery();
				//System.out.println("testingggg");
				while(rs.next()){
					dto=new KmDocumentMstr();
					dto.setStatus(rs.getString("APPROVAL_STATUS"));
					dto.setElementId(rs.getString("ELEMENT_ID"));
					dto.setDocumentName(rs.getString("DOCUMENT_NAME"));
				}
				logger.info("Exit from checkFile method");
			}
			
			catch(KmUserMstrDaoException e)
			{
				logger.error("KmUserMstrDaoException occured in checkFile." + "Exception Message: " + e.getMessage());
				throw new KmException(e.getMessage(),e);
			}
			catch(SQLException e)
			{
				logger.error("SQLException occured in checkFile." + "Exception Message: " + e.getMessage());
				throw new KmException(e.getMessage(),e);
			}
			finally
			{
				try
				{
					DBConnection.releaseResources(con,pstmt,rs);
				}
				catch(DAOException e)
				{
					logger.error("DAOException occured in checkFile." + "Exception Message: " + e.getMessage());
					throw new KmException(e.getMessage(),e);
				}
			}
			return dto;
		}

	/* (non-Javadoc)
		 * @see com.ibm.km.dao.AddFileDAO#updateFileInfoInDB(java.lang.String)
		 */
	public void updateDocumentName(KmDocumentMstr dto) throws KmException
	 {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		String docName = "";
		logger.info("Entered in updateDocumentName method");
		try
		{
			con = getConnection();
			StringBuffer query=new StringBuffer(SQL_UPDATE_FILE);
			pstmt = con.prepareStatement(query.append(" with ur").toString());
			pstmt.setString(1,dto.getFileName());
			pstmt.setInt(2,Integer.parseInt(dto.getUserId()));
			pstmt.setString(3, dto.getKeyword());
			pstmt.setString(4, dto.getDocumentDesc());
			pstmt.setString(5, dto.getPublishDt()+" 00:00:00");
			pstmt.setString(6, dto.getPublishEndDt()+" 00:00:00");
			pstmt.setString(7,dto.getOldFileName());
			pstmt.executeUpdate();
			
			logger.info("Exit from updateDocumentName method");
		}
		catch(KmUserMstrDaoException e)
		{
			logger.error("KmUserMstrDaoException occured while updating DocumentName." + "Exception Message: " + e.getMessage());
			throw new KmException(e.getMessage(),e);
		}
		catch(SQLException e)
		{
			logger.error("SQLException occured while updating DocumentName." + "Exception Message: " + e.getMessage());
			throw new KmException(e.getMessage(),e);
		}
		finally
		{
			try
			{
				DBConnection.releaseResources(con,pstmt,null);
			}
			catch(DAOException e)
			{
				logger.error("DAOException occured while updating DocumentName." + "Exception Message: " + e.getMessage());
				throw new KmException(e.getMessage(),e);
			}
		}	
	 }

	private Connection getConnection() throws KmUserMstrDaoException {

			logger.info("Entered getConnection for operation on table:KM_USER_MSTR");

			try {
			return DBConnection.getDBConnection();
			}catch(DAOException e) {

			logger.info("Exception Occured while obtaining connection.");

			throw new KmUserMstrDaoException("Exception while trying to obtain a connection",e);
		}

	   }
}