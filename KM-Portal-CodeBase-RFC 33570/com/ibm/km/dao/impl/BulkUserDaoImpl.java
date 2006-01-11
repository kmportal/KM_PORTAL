/*
 * Created on Nov 26, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.log4j.Logger;


import com.ibm.km.dao.BulkUserDao;
import com.ibm.km.dao.DBConnection;
import com.ibm.km.dto.KmFileDto;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;

/**
 * @author Anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class BulkUserDaoImpl implements BulkUserDao {

	/* (non-Javadoc)
	 * @see com.ibm.km.dao.BulkUserDao#bulkUserDeleteFile()
	 */

	Logger logger = Logger.getLogger(SubmitFileDaoImpl.class);
	public int bulkUserDeleteFile(KmFileDto dto) throws KmException {
		Connection con = null;
		PreparedStatement ps1 = null;
		ResultSet rs = null;
		String loginIds = "";
		String sql ="INSERT INTO KM_BULK_DELETE_FILES VALUES (?,?,current timestamp,?,?,?,?,?) with ur";
		try {
			//con=DatabaseConnection.getDb2DbConnection();
			con = DBConnection.getDBConnection();

			ps1 =
				con.prepareStatement(
					"select NEXTVAL FOR KM_FILE_ID_SEQ from sysibm.sysdummy1 with ur");
			rs = ps1.executeQuery();
			rs.next();
			int fileId = Integer.parseInt(rs.getString(1));
			ps1 = con.prepareStatement(sql);
			ps1.setInt(1, fileId);
			ps1.setString(2, dto.getFileName());
			if (dto.getLoginIds().length() > 0)
				loginIds =
					dto.getLoginIds().toString().substring(
						0,
						(dto.getLoginIds().toString().length() - 1));
			ps1.setString(3, loginIds);
			ps1.setString(4, dto.getStatus().trim());
			ps1.setString(5, dto.getTotalUsers());
			ps1.setString(6, dto.getUsersDeleted());
			ps1.setString(7, dto.getErrorMessage());
			ps1.executeUpdate();
			return fileId;

		} /*catch (SQLException e) {
								e.printStackTrace();
								throw new KmException("SQLException occured while stroing CSV file details in DB : " + e.getMessage(), e);
							}*/
		catch (Exception e) {

			
			//	logger.severe("Exception occured while find." + "Exception Message: " + e.getMessage());
			throw new KmException(
				"Exception occured while stroing CSV file details in DB :  "
					+ e.getMessage(),
				e);
		} finally {
			try {
				DBConnection.releaseResources(con, ps1, rs);
			} catch (DAOException e) {
				logger.error(
					"DAOException occured while saving FileInfoInDB."
						+ "Exception Message: "
						+ e.getMessage());
				throw new KmException(e.getMessage(), e);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.dao.BulkUserDao#getBulkUploadDetails(java.lang.String)
	 */
	public ArrayList getBulkDeleteDetails(String fileId) throws KmException {
		Connection con = null;
		PreparedStatement ps1 = null;
		ResultSet rs = null;
		ArrayList list = new ArrayList();
		try {
			//con=DatabaseConnection.getDb2DbConnection();
			con = DBConnection.getDBConnection();
			ps1 =
				con.prepareStatement(
					"select * from KM_BULK_DELETE_FILES where file_id = ?  with ur");
			ps1.setString(1, fileId);
			rs = ps1.executeQuery();
			while (rs.next()) {
				KmFileDto dto = new KmFileDto();
				dto.setFileName(rs.getString("FILE_NAME"));
				dto.setLoginIds(rs.getString("ERROR_LOGIN_IDS"));
				if (rs.getString("STATUS").trim().equals("S")) {
					dto.setStatus("Success");
				} else if (rs.getString("STATUS").equals("F")) {
					dto.setStatus("Fail");
				} else if (rs.getString("STATUS").equals("P")) {
					dto.setStatus("Partial");
				} else {
					dto.setStatus("Unknown");
				}
				
				dto.setErrorMessage(rs.getString("ERROR_MESSAGE"));
				dto.setUsersDeleted(rs.getString("USERS_DELETED"));
				dto.setTotalUsers(rs.getString("TOTAL_RECORDS"));
				list.add(dto);

			}
			return list;

		} catch (Exception e) {

			
			//	logger.severe("Exception occured while find." + "Exception Message: " + e.getMessage());
			throw new KmException(
				"Exception occured while stroing CSV file details in DB :  "
					+ e.getMessage(),
				e);
		} finally {
			try {
				DBConnection.releaseResources(con, ps1, rs);
			} catch (DAOException e) {
				logger.error(
					"DAOException occured while saving FileInfoInDB."
						+ "Exception Message: "
						+ e.getMessage());
				throw new KmException(e.getMessage(), e);
			}
		}

	}

	

	public int bulkUserUploadFile(KmFileDto dto) throws KmException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String loginIds = "";
		String sql =
			"INSERT INTO KM_BULK_UPLOAD_FILES (FILE_ID,FILE_PATH,CIRCLE_ID,UPLOAD_DT,UPLOADED_BY,STATUS,FILE_TYPE) VALUES (?,?,?,current timestamp,?,?,?) ";
		try {
			//con=DatabaseConnection.getDb2DbConnection();
			con = DBConnection.getDBConnection();

			ps =
				con.prepareStatement(
					"select NEXTVAL FOR KM_FILE_ID_SEQ from sysibm.sysdummy1 with ur");
			rs = ps.executeQuery();
			rs.next();
			int fileId = Integer.parseInt(rs.getString(1));
			
			if(rs != null) {
				rs.close();
				rs = null;
			}
			if(ps != null) {
				ps.close();
				ps = null;
			}
			
		logger.info("SQL:" + sql);
			ps = con.prepareStatement(sql);
			ps.setInt(1, fileId);
			ps.setString(2,dto.getFilePath());
			ps.setString(3, dto.getElementId());
			ps.setString(4, dto.getUploadedBy());
			ps.setString(5, dto.getStatus().trim());
			ps.setString(6, dto.getFileType());
			logger.info("1:" + fileId + ":2:" + dto.getFilePath() + ":3:" + dto.getElementId() + ":4:"+ dto.getUploadedBy() + ":5:"+dto.getStatus().trim() + ":6:"+ dto.getFileType() );
			
			ps.executeUpdate();
			return fileId;

		} catch (Exception e) {
			e.printStackTrace();

			throw new KmException(
				"Exception occured while stroing CSV file details in DB :  "
					+ e.getMessage(),
				e);
		} finally {
			try {
				DBConnection.releaseResources(con, ps, rs);
			} catch (DAOException e) {
				logger.error(
					"DAOException occured while saving FileInfoInDB."
						+ "Exception Message: "
						+ e.getMessage());
				throw new KmException(e.getMessage(), e);
			}
		}
	}
	/**
	public KmFileDto getBulkUploadDetails() throws KmException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con1 = null;
		PreparedStatement ps1 = null;
		ArrayList list = new ArrayList();
		KmFileDto dto = null;
		try {
			con=DatabaseConnection.getDb2DbConnection();
			//con = DBConnection.getDBConnection();
			ps =
				con.prepareStatement(
					"select * from KM_BULK_UPLOAD_FILES where status='U' order by file_id  with ur");
			rs = ps.executeQuery();
			if (rs.next()) {
				dto = new KmFileDto();
				dto.setFileId(rs.getString("FILE_ID"));
				dto.setElementId(rs.getString("CIRCLE_ID"));
				dto.setUploadedBy(rs.getString("UPLOADED_BY"));
				dto.setFilePath(rs.getString("FILE_PATH"));
				dto.setFileType(rs.getString("FILE_TYPE"));
				try{
					con=DatabaseConnection.getDb2DbConnection();
					//con = DBConnection.getDBConnection();
					ps =
						con.prepareStatement(
										"UPDATE KM_BULK_UPLOAD_FILES SET STATUS='S' WHERE FILE_ID = ?  with ur");
					ps.setString(1,dto.getFileId());
					ps.executeUpdate();
				
			
				}
				catch(Exception e){
					logger.info("Exception occured while updating the file status to processing");
				}
				finally{
					try {
									DBConnection.releaseResources(con1, ps1, null);
								} catch (DAOException e) {
									logger.error(
										"DAOException occured while saving FileInfoInDB."
											+ "Exception Message: "
											+ e.getMessage());
									throw new KmException(e.getMessage(), e);
								}
				}
			}
			return dto;

		} catch (Exception e) {

			
			//	logger.severe("Exception occured while find." + "Exception Message: " + e.getMessage());
			throw new KmException(
				"Exception occured while stroing CSV file details in DB :  "
					+ e.getMessage(),
				e);
		} finally {
			try {
				DBConnection.releaseResources(con, ps, rs);
			} catch (DAOException e) {
				logger.error(
					"DAOException occured while saving FileInfoInDB."
						+ "Exception Message: "
						+ e.getMessage());
				throw new KmException(e.getMessage(), e);
			}
		}

	}
**/
	
	public ArrayList<KmFileDto> getBulkUploadDetails() throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		//Connection con1 = null;
		PreparedStatement ps1 = null;
		ArrayList list = new ArrayList();
		KmFileDto dto = null;
		ArrayList<KmFileDto> al=new ArrayList<KmFileDto>();
		try {
			
			//System.out.println("selecting files from DB ");
			con = DBConnection.getDBConnection();
			//con = DatabaseConnection.getDb2DbConnection();
			
			
			ps =con.prepareStatement("select * from KM_BULK_UPLOAD_FILES where status='U' order by file_id  with ur");
			rs = ps.executeQuery();
			while (rs.next()) {
				
				
				//System.out.println("i am in bulk user dao impl if");
				dto = new KmFileDto();
				dto.setFileId(rs.getString("FILE_ID"));
				dto.setElementId(rs.getString("CIRCLE_ID"));
				dto.setUploadedBy(rs.getString("UPLOADED_BY"));
				dto.setFilePath(rs.getString("FILE_PATH"));
				dto.setFileType(rs.getString("FILE_TYPE"));
				al.add(dto);
				
			}
			try{
				for(KmFileDto dto1:al)
				{
					
					
					//con = DatabaseConnection.getDb2DbConnection();
					ps1 = con.prepareStatement("UPDATE KM_BULK_UPLOAD_FILES SET STATUS='I' WHERE FILE_ID = ?  with ur");
					ps1.setString(1,dto1.getFileId());
					ps1.executeUpdate();
					ps1.close();
				}
			
				}
				catch(Exception e){
					logger.info("Exception occured while updating the file status to processing");
				}
				finally{
					try {
						DBConnection.releaseResources(con, ps1, null);
								} catch (Exception e) {
									logger.error(
										"DAOException occured while saving FileInfoInDB."
											+ "Exception Message: "
											+ e.getMessage());
									throw new Exception(e.getMessage(), e);
								}
				}
			//System.out.println("returning to service layer");
			return al;

		} catch (Exception e) {

			e.printStackTrace();
				logger.error("Exception occured while find." + "Exception Message: " + e.getMessage());
			throw new Exception("Exception occured while stroing CSV file details in DB :  "+ e.getMessage(),e);
		} finally {
			try {
				DBConnection.releaseResources(con, ps, rs);
			} catch (Exception e) {
				logger.error("DAOException occured while saving FileInfoInDB."+ "Exception Message: "+ e.getMessage());
				throw new Exception(e.getMessage(), e);
			}
		}

	}
	/* (non-Javadoc)
	 * @see com.ibm.km.dao.BulkUserDao#getBulkDeleteDetails(int)
	 */
	public void getBulkDeleteDetails(int fileId) throws KmException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.dao.BulkUserDao#updateFileStatus(int)
	 */
	public void updateFileStatus(KmFileDto dto) throws KmException {
	Connection con = null;
	PreparedStatement ps = null;
	Logger logger1 = Logger.getLogger("DEFAULT_LOGGER");
	try {
		//con=DatabaseConnection.getDb2DbConnection();
		con = DBConnection.getDBConnection();
		
		ps = con.prepareStatement("UPDATE KM_BULK_UPLOAD_FILES SET STATUS= ? , USERS_CREATED = ? , USERS_UPDATED = ? , USERS_DELETED = ? , ERROR_MESSAGE  = ?, TOTAL_RECORDS = ? WHERE FILE_ID = ?  with ur");
				
		ps.setString(1, dto.getStatus());
		ps.setString(2,  dto.getUsersCreated());
		//ps.setString(3, dto.getUsersUpdated());
		ps.setInt(3, Integer.parseInt(dto.getUsersUpdated()));
		//ps.setString(4, dto.getUsersDeleted());
		ps.setInt(4, Integer.parseInt(dto.getUsersDeleted()));
		//ps.setString(5, dto.getFileType());
		if(dto.getErrorMessage()==null);
			dto.setErrorMessage("");
		ps.setString(5, dto.getErrorMessage());
		if(dto.getTotalUsers()==null){
			dto.setTotalUsers("0");
		}
		ps.setString(6, dto.getTotalUsers());
		ps.setInt(7, Integer.valueOf(dto.getFileId()));
		ps.executeUpdate();
		
		logger1.info("***************999999999999991*************"+dto.getStatus());
		logger1.info("***************999999999999992*************"+dto.getUsersCreated());
		logger1.info("***************999999999999993*************"+dto.getUsersUpdated());
		logger1.info("***************999999999999994*************"+dto.getUsersDeleted());
		logger1.info("***************999999999999995*************"+dto.getTotalUsers());
		logger1.info("***************999999999999996*************"+dto.getFileId());
		

	} catch (Exception e) {

		e.printStackTrace();
		//	logger.severe("Exception occured while find." + "Exception Message: " + e.getMessage());
		throw new KmException(
			"Exception occured while updating file status  "
				+ e.getMessage(),
			e);
	} finally {
		try {
			DBConnection.releaseResources(con, ps, null);
		} catch (DAOException e) {
			logger.error(
				"Exception occured while updating file status "
					+ e.getMessage());
			throw new KmException(e.getMessage(), e);
		}
	}
		
	}

	public ArrayList getBulkUserFiles(String circleId,String date) throws KmException {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		ArrayList list = new ArrayList();
		KmFileDto dto = null;
		try {
			//con=DatabaseConnection.getDb2DbConnection();
				con = DBConnection.getDBConnection();
			// Added by Parnika for UAT changes
			String query="";
			query="SELECT b.ELEMENT_ID as ELEMENT_ID,b.ELEMENT_LEVEL_ID as ELEMENT_LEVEL_ID FROM KM_USER_MSTR a, KM_ELEMENT_MSTR b WHERE a.ELEMENT_ID= b.ELEMENT_ID and a.ELEMENT_ID = ?";
			ps1=con.prepareStatement(query);
			logger.info(circleId+"query for view bulk upload"+query);
			
			ps1.setInt(1, Integer.valueOf(circleId));
			//System.out.println(circleId+"testingggggggg"+query);
			rs1 = ps1.executeQuery();
			//System.out.println("testingggggggghelooooooooooooo");
			if(rs1.next()){
				if(rs1.getString("ELEMENT_LEVEL_ID").equals("1")){
					ps =con.prepareStatement("select f.FILE_ID,f.CIRCLE_ID,f.FILE_PATH,f.FILE_TYPE,f.STATUS,f.TOTAL_RECORDS,f.USERS_CREATED, f.USERS_UPDATED, f.USERS_DELETED,u.USER_LOGIN_ID from KM_BULK_UPLOAD_FILES f INNER JOIN KM_USER_MSTR u ON f.UPLOADED_BY = u.USER_ID WHERE DATE(UPLOAD_DT) between ? and ? with ur ");
					ps.setDate(1, Date.valueOf(date));
					ps.setDate(2, Date.valueOf(date));
					//ps.setString(1, date);
					//ps.setString(2, date);
				}else if(rs1.getString("ELEMENT_LEVEL_ID").equals("2")){
					ps=con.prepareStatement("select f.FILE_ID,f.CIRCLE_ID,f.FILE_PATH,f.FILE_TYPE,f.STATUS,f.TOTAL_RECORDS,f.USERS_CREATED, f.USERS_UPDATED, f.USERS_DELETED,u.USER_LOGIN_ID  from KM_BULK_UPLOAD_FILES f INNER JOIN KM_USER_MSTR u ON f.UPLOADED_BY = u.USER_ID   WHERE f.CIRCLE_ID in (select ELEMENT_ID from KM_ELEMENT_MSTR where PARENT_ID=?) AND DATE(UPLOAD_DT) between ? and ? with ur ");
					ps.setInt(1, Integer.valueOf(circleId));
					ps.setDate(2, Date.valueOf(date));
					ps.setDate(3, Date.valueOf(date));
					//ps.setString(1, circleId);
					//ps.setString(2, date);
					//ps.setString(3, date);	
				}
				else if(rs1.getString("ELEMENT_LEVEL_ID").equals("3")){
					ps =con.prepareStatement("select f.FILE_ID,f.CIRCLE_ID,f.FILE_PATH,f.FILE_TYPE,f.STATUS,f.TOTAL_RECORDS,f.USERS_CREATED, f.USERS_UPDATED, f.USERS_DELETED,u.USER_LOGIN_ID  from KM_BULK_UPLOAD_FILES f INNER JOIN KM_USER_MSTR u ON f.UPLOADED_BY = u.USER_ID   WHERE f.CIRCLE_ID = ? AND DATE(UPLOAD_DT) between ? and ? with ur ");
					ps.setInt(1, Integer.valueOf(circleId));
					ps.setDate(2, Date.valueOf(date));
					ps.setDate(3, Date.valueOf(date));
					//ps.setString(1, circleId);
					//ps.setString(2, date);
					//ps.setString(3, date);					
				}
			}

			rs = ps.executeQuery();
			String filePath="";
			while (rs.next()) {
				dto = new KmFileDto();
				dto.setFileId(rs.getString("FILE_ID"));
				dto.setElementId(rs.getString("CIRCLE_ID"));
				dto.setUploadedBy(rs.getString("USER_LOGIN_ID"));
				filePath=rs.getString("FILE_PATH");
				dto.setFilePath(filePath);
				
				dto.setFileName(filePath.substring((filePath.lastIndexOf("/")+1),filePath.length()));
				if(rs.getString("FILE_TYPE").equals("C"))
					dto.setFileType("Creation");
				//added by vishwas
				else if(rs.getString("FILE_TYPE").equals("U"))
				     dto.setFileType("Updation");
				//end by vishwas
				else
					dto.setFileType("Deletion");
				if(rs.getString("STATUS").equals("P"))
					dto.setStatus("Processed");
				else if(rs.getString("STATUS").equals("S"))
				dto.setStatus("Success");

				else
					dto.setStatus("Not Processed");
				if(rs.getString("TOTAL_RECORDS")==null)
					dto.setTotalUsers("0");
				else	
					dto.setTotalUsers(rs.getString("TOTAL_RECORDS"));
				if(rs.getString("USERS_CREATED")==null)
					dto.setUsersCreated("0");
				else
					dto.setUsersCreated(rs.getString("USERS_CREATED"));
				if(rs.getString("USERS_UPDATED")==null)
					dto.setUsersUpdated("0");
				else
					dto.setUsersUpdated(rs.getString("USERS_UPDATED"));
				
				if(rs.getString("USERS_DELETED")==null)
					dto.setUsersDeleted("0");
				else
					dto.setUsersDeleted(rs.getString("USERS_DELETED"));
			
			   list.add(dto);
			}
		}
		catch(Exception e){
		
					logger.info("Exception occured while updating the file status to processing");
		}
		finally{
			try {
				DBConnection.releaseResources(con, ps, rs);
		} catch (DAOException e) {
			logger.error(
					"DAOException occured while saving FileInfoInDB."
											+ "Exception Message: "
											+ e.getMessage());
									throw new KmException(e.getMessage(), e);
								}
				}
			
		
		return list;		
	}

}
