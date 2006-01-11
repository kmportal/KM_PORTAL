/*
 * Created on Feb 13, 2008
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
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ibm.km.common.PropertyReader;
import com.ibm.km.dao.ApproveFileDao;
import com.ibm.km.dao.DBConnection;
import com.ibm.km.dto.ApproveFileDto;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;
import com.ibm.km.exception.KmUserMstrDaoException;

/**
 * @author Pramod
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ApproveFileDaoImpl implements ApproveFileDao{
	
	
	Logger logger = Logger.getLogger(SubmitFileDaoImpl.class);
	 

	//Bug resolved MASDB00064826
	//Changed
	public static final String SQL_GET_FILE_LIST = "WITH nee(element_id,chain) AS ( SELECT  ELEMENT_ID,  cast(element_name as VARCHAR(" + PropertyReader.getAppValue("search.path.size") + ")) FROM KM_ELEMENT_MSTR WHERE element_id = ?  UNION ALL SELECT nplus1.element_id,nee.chain || '/' || nplus1.element_name FROM KM_ELEMENT_MSTR as nplus1, nee WHERE nee.element_id=nplus1.PARENT_ID)	SELECT doc.DOCUMENT_ID,chain,doc.DOCUMENT_NAME, doc.DOCUMENT_DISPLAY_NAME, usr.USER_LOGIN_ID, doc.UPLOADED_DT 	FROM KM_DOCUMENT_MSTR doc inner join KM_USER_MSTR usr on usr.USER_ID = doc.UPLOADED_BY inner join KM_ELEMENT_MSTR ele  on doc.element_id=ele.element_id inner join nee on nee.element_id = ele.element_id where   doc.STATUS = 'A' and doc.APPROVAL_STATUS = 'P' and APPROVAL_REJECTION_BY = ? ORDER BY DOC.UPDATED_DT ";
		
	public static final String SQL_SET_APPROVED_STATUS = "UPDATE KM_DOCUMENT_MSTR SET APPROVAL_STATUS = 'A',COMMENT = ?,APPROVAL_REJECTION_DT = current timestamp, APPROVAL_REJECTION_BY = ? WHERE  DOCUMENT_ID = ?";
													
	public static final String SQL_SET_MANUAL_APPROVED_STATUS = "UPDATE KM_DOCUMENT_MSTR SET escalation_time = timestamp_format(?, 'YYYY-MM-DD HH24:MI:SS'),ESCALATION_COUNT=?,APPROVAL_REJECTION_BY=? WHERE DOCUMENT_ID = ?";
														
	public static final String SQL_GET_CHECKED_APPROVED_FILES = " select  doc.DOCUMENT_PATH,doc.DOCUMENT_DISPLAY_NAME ,doc.DOCUMENT_ID,doc.ELEMENT_ID ,doc.UPLOADED_DT,doc.ESCALATION_TIME,doc.ESCALATION_COUNT,doc.APPROVAL_REJECTION_BY,doc.UPLOADED_BY,usr.user_fname,usr.user_lname,own.user_fname AS OWNER_FNAME,own.user_lname as OWNER_LNAME from KM_DOCUMENT_MSTR doc  inner join Km_user_mstr  usr on  doc.APPROVAL_REJECTION_BY=usr.user_id inner join Km_user_mstr own on own.USER_ID = doc.UPLOADED_BY WHERE doc.approval_status='P' and doc.status = 'A' and  doc.escalation_time<=current timestamp and doc.escalation_count!=4 WITH UR ";
				
	public static final String SQL_GET_UPLOADED_TIME = "select UPLOADED_DT,ESCALATION_TIME,ESCALATION_COUNT from KM_DOCUMENT_MSTR  WHERE approval_status='P' and DOCUMENT_ID=?";
																	
	public static final String SQL_SET_REJECT_STATUS = "UPDATE KM_DOCUMENT_MSTR SET APPROVAL_STATUS = 'R',COMMENT = ?,APPROVAL_REJECTION_DT = current timestamp, UPDATED_BY = ? WHERE DOCUMENT_ID = ?";
			
//KM Phase 2
	public static final String SQL_DEACTIVATE_OLDFILE = "update km_document_mstr set approval_status = 'O' where document_id =(select max(a.document_id) from km_document_mstr a ,  km_document_mstr b where b.doc_name = a.doc_name and a.document_path=b.document_path and a.approval_status = 'A' and b.approval_status = 'P' and b.document_id=?)";
	
	
	/* (non-Javadoc)
	 * @see com.ibm.km.dao.SubmitFileDao#getFileList(java.lang.String, java.lang.String)
	 */
	public ArrayList getFileList(String userId)
		throws KmException {
			
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			ApproveFileDto dto;
	        StringBuffer query=new StringBuffer(SQL_GET_FILE_LIST);
			//String sql=SQL_GET_FILE_LIST;
			ArrayList fileList = new ArrayList();
			logger.info("Entered in getFileList method");
			try
			{
				con = getConnection();
				pstmt = con.prepareStatement(query.append(" with ur ").toString());
				pstmt.setString(1, "1");
				pstmt.setString(2,userId);
				rs = pstmt.executeQuery();
				while(rs.next())
				{
					dto = new ApproveFileDto();
					String path=rs.getString("chain");
					String documentStringPath="";

					if((path.indexOf("/")+1)<path.lastIndexOf("/")){	
								   documentStringPath=path.substring((path.indexOf("/")+1),path.lastIndexOf("/"));
							  }

					dto.setDocStringPath(documentStringPath);
					dto.setDocName(rs.getString("DOCUMENT_DISPLAY_NAME"));
					dto.setDocumentId(rs.getString("DOCUMENT_ID"));
					dto.setUserName(rs.getString("USER_LOGIN_ID"));
					dto.setUploadedDt(rs.getDate("UPLOADED_DT"));
					fileList.add(dto);
				}
				logger.info("Exit from getFileList method");
				return fileList;
			}
			
			catch(KmUserMstrDaoException e)
			{
				logger.error("KmUserMstrDaoException occured while getting File List." + "Exception Message: " + e.getMessage());
				throw new KmException(e.getMessage(),e);
			}
			catch(SQLException e)
			{
				logger.error("SQLException occured while getting File List." + "Exception Message: " + e.getMessage());
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
					logger.error("DAOException occured while getting File List." + "Exception Message: " + e.getMessage());
					throw new KmException(e.getMessage(),e);
				}
			}	
		
	}
	
		/* (non-Javadoc)
		 * @see com.ibm.km.dao.SubmitFileDao#submitFile(java.lang.String[])
		 */
		public void approveFile(String[] fileIds,String[] commentList,String userId) throws KmException {
			
			Connection con = null;
			PreparedStatement pstmt = null;
			logger.info("Entered in approveFile method");
			try
			{
				StringBuffer query=new StringBuffer(SQL_SET_APPROVED_STATUS);
				con = getConnection();
				pstmt = con.prepareStatement(query.append(" with ur").toString());
				
				int i = 0;
				
				while( i < fileIds.length)
				{
					pstmt.setString(1,commentList[i]);
					pstmt.setInt(2,Integer.parseInt(userId));
					pstmt.setInt(3,Integer.parseInt(fileIds[i]));
					pstmt.addBatch();
					i++;
				}
				pstmt.executeBatch();
				con.commit();
				logger.info("Exit from approveFile method");
			}
			catch(KmUserMstrDaoException km)
			{
				logger.error("KmUserMstrDaoException occured while Approving File" + "Exception Message: " + km.getMessage());
				throw new KmException(km.getMessage(),km);
			}
			catch(SQLException e)
			{
				logger.error("SQLException occured while Approving File" + "Exception Message: " + e.getMessage());
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
					logger.error("DAOException occured while Approving File" + "Exception Message: " + e.getMessage());
					throw new KmException(e.getMessage(),e);
				}
			}	

		}
		
			/* (non-Javadoc)
			 * @see com.ibm.km.dao.SubmitFileDao#submitFile(java.lang.String[])
			 */
			public void updateEscalationTime(String documentIds,String escalationTime,int escalationCount,String approverId) throws KmException {
			
				Connection con = null;
				PreparedStatement pstmt = null;
				logger.info("Entered in updateEscalationTime method");
				try
				{
					StringBuffer query=new StringBuffer(SQL_SET_MANUAL_APPROVED_STATUS);
					con = getConnection();
					pstmt = con.prepareStatement(query.append(" with ur").toString());
				
					pstmt.setString(1,escalationTime);
					pstmt.setInt(2,escalationCount);
					pstmt.setString(3,approverId);
					pstmt.setInt(4,Integer.parseInt(documentIds));
					pstmt.executeUpdate();
					logger.info("Exit from updateEscalationTime method");
					
				}
				catch(KmUserMstrDaoException km)
				{
					logger.error("KmUserMstrDaoException occured while updating Escalation Time" + "Exception Message: " + km.getMessage());
					throw new KmException(km.getMessage(),km);
				}
				catch(SQLException e)
				{
					logger.error("SQLException occured while updating Escalation Time" + "Exception Message: " + e.getMessage());
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
						logger.error("DAOException occured while updating Escalation Time" + "Exception Message: " + e.getMessage());
						throw new KmException(e.getMessage(),e);
					}
				}	

			}
		
			/* (non-Javadoc)
			 * @see com.ibm.km.dao.SubmitFileDao#submitFile(java.lang.String[])
			 */
			public List checkApprovedFiles() throws KmException {
			
				Connection con = null;
				PreparedStatement ps = null;
				ResultSet rs=null;
				List data=new ArrayList();
				ApproveFileDto dto=null;
				logger.info("Entered in checkApprovedFiles method");
				try
				{
					
					con = getConnection();
					ps = con.prepareStatement(SQL_GET_CHECKED_APPROVED_FILES);
					rs = ps.executeQuery();
					while( rs.next())
					{
						dto= new ApproveFileDto();
						dto.setDocCompletePath(rs.getString("DOCUMENT_PATH"));
						dto.setElementId(rs.getString("ELEMENT_ID"));
						dto.setDocName(rs.getString("DOCUMENT_DISPLAY_NAME"));
						dto.setDocumentId(rs.getString("DOCUMENT_ID"));
						dto.setUploadedDate(rs.getTimestamp("UPLOADED_DT"));
						dto.setUploadedBy(rs.getString("UPLOADED_BY"));
						dto.setEscalationTime(rs.getTimestamp("ESCALATION_TIME"));
						dto.setEscalationCount(rs.getInt("ESCALATION_COUNT"));
						dto.setApprover(new StringBuffer(rs.getString("user_fname")).append(" ").append(rs.getString("user_lname")).toString());
						dto.setUploadedUserName(new StringBuffer(rs.getString("owner_fname")).append(" ").append(rs.getString("owner_lname")).toString());
						data.add(dto);
					}
					
					con.commit();
					logger.info("Exit from checkApprovedFiles method");
					return data;
					
				}
				catch(KmUserMstrDaoException km)
				{
					logger.error("KmUserMstrDaoException occured while checking approved files Escalation Time" + "Exception Message: " + km.getMessage());
					throw new KmException(km.getMessage(),km);
				}
				catch(SQLException e)
				{
					logger.error("SQLException occured while checking approved files Escalation Time" + "Exception Message: " + e.getMessage());
					throw new KmException(e.getMessage(),e);
				}
				finally
				{
					try
					{
						DBConnection.releaseResources(con,ps,rs);
					}
					catch(DAOException e)
					{
						logger.error("DAOException occured while checking approved files Escalation Time" + "Exception Message: " + e.getMessage());
						throw new KmException(e.getMessage(),e);
					}
				}	

			}
			
			/* (non-Javadoc)
			 * @see com.ibm.km.dao.SubmitFileDao#submitFile(java.lang.String[])
			 */
			public ApproveFileDto getUploadedTime(String documentId) throws KmException {
		
				Connection con = null;
				PreparedStatement ps = null;
				ResultSet rs=null;
				ApproveFileDto dto=null;
				Timestamp ts = null;
				logger.info("Entered in getUploadedTime method");
				try
				{
					con = getConnection();
					StringBuffer query=new StringBuffer(SQL_GET_UPLOADED_TIME);
					ps = con.prepareStatement(query.append(" with ur").toString());
					ps.setInt(1,Integer.parseInt(documentId));
					rs = ps.executeQuery();
					
					if( rs.next())
					{	dto = new ApproveFileDto();
						dto.setUploadedDate(rs.getTimestamp("UPLOADED_DT"));
						dto.setEscalationTime(rs.getTimestamp("ESCALATION_TIME"));
						dto.setEscalationCount(rs.getInt("ESCALATION_COUNT"));
					}
				
					con.commit();
					logger.info("Exit from getUploadedTime method");
					return dto;
				
				}
				catch(KmUserMstrDaoException km)
				{
					logger.error("KmUserMstrDaoException occured while getting uploaded Time" + "Exception Message: " + km.getMessage());
					throw new KmException(km.getMessage(),km);
				}
				catch(SQLException e)
				{
					logger.error("SQLException occured while getting uploaded Time" + "Exception Message: " + e.getMessage());
					throw new KmException(e.getMessage(),e);
				}
				finally
				{
					try
					{
						DBConnection.releaseResources(con,ps,rs);
					}
					catch(DAOException e)
					{
						logger.error("DAOException occured while getting uploaded Time" + "Exception Message: " + e.getMessage());
						throw new KmException(e.getMessage(),e);
					}
				}	

			}	
		
		/* (non-Javadoc)
		 * @see com.ibm.km.dao.SubmitFileDao#submitFile(java.lang.String[])
		 */
		public void deactivateOldFile(String[] fileIds) throws KmException {
			
			Connection con = null;
			PreparedStatement ps = null;
			logger.info("Entered in deactivateOldFile method");
			try
			{
				StringBuffer query=new StringBuffer(SQL_DEACTIVATE_OLDFILE);
				con = getConnection();
				ps = con.prepareStatement(query.append(" with ur").toString());
			
				int i = 0;
				while( i < fileIds.length)
				{
					ps.setInt(1,Integer.parseInt(fileIds[i]));
					ps.addBatch();
					i++;
				}
				ps.executeBatch();
				con.commit();
				logger.info("Exit from deactivateOldFile method");
			}
			catch(KmUserMstrDaoException km)
			{
				logger.error("KmUserMstrDaoException occured while DeActivating old File" + "Exception Message: " + km.getMessage());
				throw new KmException(km.getMessage(),km);
			}
			catch(SQLException e)
			{
				logger.error("SQLException occured while DeActivating old File" + "Exception Message: " + e.getMessage());
				throw new KmException(e.getMessage(),e);
			}
			finally
			{
				try
				{
					DBConnection.releaseResources(con,ps,null);
				}
				catch(DAOException e)
				{
					logger.error("DAOException occured while DeActivating old File" + "Exception Message: " + e.getMessage());
					throw new KmException(e.getMessage(),e);
				}
			}	

		}
	public void rejectFile(String[] fileIds,String[] commentList,String userId) throws KmException {
			
				Connection con = null;
				PreparedStatement pstmt = null;
				logger.info("Entered in  rejectFile method");
				try
				{
					StringBuffer query=new StringBuffer(SQL_SET_REJECT_STATUS);
					con = getConnection();
					pstmt = con.prepareStatement(query.append(" with ur").toString());
					int i = 0;
				
					while( i < fileIds.length)
					{
						pstmt.setString(1,commentList[i]);
						pstmt.setInt(2,Integer.parseInt(userId));
						pstmt.setInt(3,Integer.parseInt(fileIds[i]));
						pstmt.addBatch();
						i++;
					}
					pstmt.executeBatch();
					con.commit();
					logger.info("Exit from rejectFile method");
				}
				catch(KmUserMstrDaoException km)
				{
					logger.error("KmUserMstrDaoException occured while Rejecting File" + "Exception Message: " + km.getMessage());
					throw new KmException(km.getMessage(),km);
				}
				catch(SQLException e)
				{
					logger.error("SQLException occured while Rejecting File" + "Exception Message: " + e.getMessage());
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
						logger.error("DAOException occured while Rejecting File" + "Exception Message: " + e.getMessage());
						throw new KmException(e.getMessage(),e);
					}
				}	

			}
	
/*public void approveFile(ArrayList fileList) throws KmException {
			
			Connection con = null;
			PreparedStatement pstmt = null;
			ApproveFileDto dto ;
			
			try
			{
				con = getConnection();
				pstmt = con.prepareStatement(SQL_UPDATE_STATUS);
				int i = 0;
				
//				
//				
//				while( i < fileIds.length)
//				{
//					pstmt.setString(1,commentList[i]);
//					pstmt.setInt(2,Integer.parseInt(fileIds[i]));
//					pstmt.addBatch();
//					i++;
//				}
			
				while( i < fileList.size())
				{
					HashMap map = (HashMap)fileList.get(i); 
					
					pstmt.setString(1,map.get("comment").toString());
					pstmt.setInt(2,Integer.parseInt(map.get("id").toString()));
					pstmt.addBatch();
					i++;
				}


				pstmt.executeBatch();
				con.commit();
			}
			catch(KmUserMstrDaoException km)
			{
				km.printStackTrace();
				throw new KmException(km.getMessage(),km);
			}
			catch(SQLException e)
			{
				e.getNextException().printStackTrace();
				//e.printStackTrace();
				throw new KmException(e.getMessage(),e);
			}
			finally
			{
				try
				{
					con.close();
					pstmt.close();

				}
				catch(SQLException e)
				{
					e.printStackTrace();
					throw new KmException(e.getMessage(),e);
				}
			}	

		}
	
*/	
	
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