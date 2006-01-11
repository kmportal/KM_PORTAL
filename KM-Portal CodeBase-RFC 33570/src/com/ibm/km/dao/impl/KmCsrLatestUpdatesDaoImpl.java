package com.ibm.km.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.ibm.km.common.Utility;
import com.ibm.km.dao.DBConnection;
import com.ibm.km.dao.KmCsrLatestUpdatesDao;
import com.ibm.km.dto.KmCsrLatestUpdatesDto;
import com.ibm.km.dto.KmUserMstr;

import com.ibm.km.exception.DAOException;


public class KmCsrLatestUpdatesDaoImpl implements KmCsrLatestUpdatesDao{
	 
	private static Logger logger = Logger.getLogger(KmCsrLatestUpdatesDaoImpl.class);
	
	protected static final String SQL_INSERT_LATEST_UPDATES = "INSERT INTO KM_LATEST_UPDATES( DOCUMENT_ID, DOC_TITLE, DOC_DETAIL, DOC_ACTIVATION_DT, DOC_EXPIRY_DT, CATEGORY, LOB_ID, CIRCLE_ID, DOC_TYPE) VALUES(?,?,?,?,?,?,?,?,?)  with ur";
//	protected static final String SQL_SELECT_LATEST_UPDATES = "select DOCUMENT_ID,DOC_TITLE,DOC_DETAIL,DOC_TYPE from KM_LATEST_UPDATES where DOC_ACTIVATION_DT >=(current timestamp - # ) and DOC_EXPIRY_DT>=current timestamp and LOB_ID = ? order by DOC_ACTIVATION_DT desc, REC_ID desc with ur";
	
	protected static final String SQL_SELECT_LATEST_UPDATES = "select a.DOCUMENT_ID,a.DOC_TITLE,a.DOC_DETAIL,a.DOC_TYPE from KM_LATEST_UPDATES a, KM_DOCUMENT_MSTR b where b.STATUS='A' and b.PUBLISHING_START_DT >=(current timestamp - # )  and b.PUBLISHING_START_DT < (current timestamp) and b.PUBLISHING_END_DT>=current timestamp and a.LOB_ID = ? and a.CIRCLE_ID = ? and a.DOCUMENT_ID=b.DOCUMENT_ID order by a.DOC_ACTIVATION_DT desc, a.REC_ID desc with ur";
	
	protected static final String SQL_DELETE_UPDATES = "update KM_LATEST_UPDATES set DOC_EXPIRY_DT = current timestamp where document_id=? with ur";
	
	public ArrayList<KmCsrLatestUpdatesDto> initGetUpdates(String lobId, String circleId) throws DAOException{
		ArrayList<KmCsrLatestUpdatesDto> list = new ArrayList<KmCsrLatestUpdatesDto>();
		ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
		String timeDuration = bundle.getString("latestUpdates.timeDuration");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn= DBConnection.getDBConnection();
		try{
		/* Added by Parnika for LOBwise Latest Updates */
			
			pstmt = conn.prepareStatement(SQL_SELECT_LATEST_UPDATES.replace("#", timeDuration));
			pstmt.setInt(1, Integer.parseInt(lobId));				
			pstmt.setInt(2, Integer.parseInt(circleId));				
			/* End of changes by Parnika */
		rs = pstmt.executeQuery();
		while(rs.next()){
			KmCsrLatestUpdatesDto dto = new KmCsrLatestUpdatesDto();
			dto.setDocType(rs.getInt("DOC_TYPE"));
			dto.setDocumentId(rs.getInt("DOCUMENT_ID")+"");
			dto.setUpdateTitle(rs.getString("DOC_TITLE"));
			if(rs.getString("DOC_DETAIL").length() >= 40){
				dto.setUpdateBrief(rs.getString("DOC_DETAIL").substring(0,39)+"...");
			}else{
				dto.setUpdateBrief(rs.getString("DOC_DETAIL"));
			}
			dto.setUpdateDesc(rs.getString("DOC_DETAIL"));
			dto.setDocumentViewUrl(Utility.getDocumentViewURL(dto.getDocumentId(), dto.getDocType()));
			list.add(dto);
		}
		
		}catch(SQLException sqle){
			sqle.printStackTrace();
			throw new DAOException(sqle.getMessage());
		}
		finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null) {
					conn.close();
				}
				if (rs != null) {
					rs.close();
				}
				
			} catch (Exception e) {
				logger.error("SQL Exception occured while closing connection for Latest update. initGetUpdates:" + "Exception Message: " + e.getMessage());
			}
		}
		return list;
	}

	/**
	 * Method to insert Latest Updates into db.
	 */
	public  int insertLatestUpdates(KmCsrLatestUpdatesDto dto) throws DAOException {

		logger.info("Entered insert for table KM_LATEST_UPDATES");

		Connection con = null;
		PreparedStatement ps = null;
		int rowsUpdated = 0;
		int columnSize = 200;
		try {
			con = DBConnection.getDBConnection();
			ps = con.prepareStatement(SQL_INSERT_LATEST_UPDATES);
			
			
			String updateTitle = dto.getUpdateTitle();
			//updateTitle = Utility.encodeContent(updateTitle);
			int updateTitleLength = updateTitle.length();	
			
			if (updateTitleLength > columnSize)
			{
				updateTitle = updateTitle.substring(0, columnSize-1);
			}

			
			String updateDesc = dto.getUpdateDesc();
			//updateDesc = Utility.encodeContent(updateDesc);
			int updateDescLength = updateDesc.length();			
			if (updateDescLength > columnSize)
			{
				updateDesc = updateDesc.substring(0, columnSize-1);
			}

			int paramCount = 1;
			ps.setString(paramCount++,  dto.getDocumentId());
//System.out.println("updateTitle:"+updateTitle);
			ps.setString(paramCount++,  updateTitle);
//			System.out.println("updateDesc:"+updateDesc);
			ps.setString(paramCount++,  updateDesc);
			ps.setString(paramCount++,  dto.getActivationDate()+ " 00:00:00");
			ps.setString(paramCount++,  dto.getExpiryDate()+ " 23:59:59");
			ps.setString(paramCount++,  dto.getCategory());
			ps.setInt(paramCount++,  Integer.parseInt(dto.getLob()));
			if(dto.getLogiclob()=="lob")
			{
			ps.setInt(paramCount++, 1);
			}
			else
			{
				ps.setInt(paramCount++,  Integer.parseInt(dto.getCircleId()));	
			}
			ps.setInt(paramCount++, dto.getDocType());
			rowsUpdated=ps.executeUpdate();

		logger.info("Latest update inserted.");

		} catch (SQLException e) {
			
		logger.error("SQL Exception occured while inserting Latest update." + "Exception Message: " + e.getMessage());
		e.printStackTrace();
		throw new DAOException(e.getMessage());
		} catch (Exception e) {
			logger.error("SQL Exception occured while inserting Latest update." + "Exception Message: " + e.getMessage());
			e.printStackTrace();
			throw new DAOException(e.getMessage());
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				logger.error("SQL Exception occured while closing connection for Latest update." + "Exception Message: " + e.getMessage());
			}
		}

		return rowsUpdated;
	}





	public void deleteDocumentEntry(int[] documentId) throws DAOException {
		logger.info("Entered deleteDocumentEntry in Latest Updates ..");

		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DBConnection.getDBConnection();
			ps = con.prepareStatement(SQL_DELETE_UPDATES);

			for(int i = 0; i < documentId.length; i++) {
				ps.setInt(1, documentId[i]);
				ps.executeUpdate();
			}
		logger.info("deleteDocumentEntry in Latest updates done.");

		} catch (SQLException e) {
			
		logger.error("SQL Exception occured while deleteDocumentEntry in Latest update." + "Exception Message: " + e.getMessage());
			throw new DAOException(e.getMessage());
		} catch (Exception e) {
			logger.error("SQL Exception occured while deleteDocumentEntry." + "Exception Message: " + e.getMessage());
			throw new DAOException(e.getMessage());
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (con != null) {
					con.setAutoCommit(true);
					con.close();
				}
			} catch (Exception e) {
				logger.error("SQL Exception occured while closing connection for deleteDocumentEntry update." + "Exception Message: " + e.getMessage());
			}
		}
		return;
	}





}
