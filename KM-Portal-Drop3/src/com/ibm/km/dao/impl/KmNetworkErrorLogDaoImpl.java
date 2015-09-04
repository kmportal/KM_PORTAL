package com.ibm.km.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.ibm.km.dao.DBConnection;
import com.ibm.km.dao.KmNetworkErrorLogDao;
import com.ibm.km.dto.NetworkErrorLogDto;
import com.ibm.km.exception.KmException;
public class KmNetworkErrorLogDaoImpl  implements KmNetworkErrorLogDao {


	/*
	* Logger for the class.
	*/
	private static final Logger logger;

	static {

		logger = Logger.getLogger(KmCategoryMstrDaoImpl.class);
	}

 //private static java.util.logging.Logger logger = java.util.logging.Logger.getLogger(KmCategoryMstrDaoImpl.class);


	protected static final String SQL_INSERT_WITH_ID = "INSERT INTO KM_NETWORK_FAULT_LOG(ID, FAULT_DESC, AFFECTED_AREA, TAT_HOURS, TAT_MINUTES, STATUS, REPORTED_DATE, CIRCLE_ID) "+ 
    												   " VALUES(next value for KM_NETWORK_FAULT_ID_SEQ, ?,?,?,?,?, current timestamp,?) with ur";

	protected static final String SQL_SELECT_CSD_USER = "select CSD_MOBILE_NO from KM_CSD_USERS where CIRCLE_ID = ? and STATUS = 'A' with UR";
	
	protected static final String SQL_SELECT_LOG_ID = "select max(ID) from KM_NETWORK_FAULT_LOG  with UR";
	
	protected static final String SQL_SELECT_TSG_CIRCLE_ID = "SELECT CM.KM_CIRCLE_KEY,CM.KM_CIRCLE_DESC FROM KM_TSG_CIRCLE_CONFIG CC INNER JOIN KM_TSG_CIRCLE_MAP CM ON CC.KM_CIRCLE_ID=CM.KM_CIRCLE_ID WHERE CC.KM_ELEMENT_ID = ? WITH UR"; 
	//Insert in to the table KM_CATEGORY_MSTR
	
	public  int insert(NetworkErrorLogDto networkErrorLogDto) throws KmException {

		logger.info("Entered insert for table KM_NETWORK_FAULT_LOG");

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs=null;

		int logId = 0;
		int rowsUpdated=0;
		
		try {
            
			StringBuffer query1=new StringBuffer(SQL_INSERT_WITH_ID);
			con=DBConnection.getDBConnection();
			con.setAutoCommit(false);
			ps=con.prepareStatement(query1.toString());
			ps.setString(1, networkErrorLogDto.getProblemDesc());
			ps.setString(2, networkErrorLogDto.getAreaAffected());
			ps.setInt(3, Integer.parseInt(networkErrorLogDto.getResoTATHH()));
			ps.setInt(4, Integer.parseInt(networkErrorLogDto.getResoTATMM()));
			ps.setString(5, networkErrorLogDto.getStatus());
			ps.setInt(6, networkErrorLogDto.getCircleID());
			
			
			
			/*Executing the querry */
			
			ps.executeUpdate();  
			
			/*Selecting Log Id */
			
			ps=con.prepareStatement(SQL_SELECT_LOG_ID);
			rs=ps.executeQuery();
			while(rs.next())
			{
				logId = rs.getInt(1) ;
			}
			
			con.commit();
		} catch (SQLException e) {
				e.printStackTrace();
				logger.info(e);
				logger.info("SQL Exception occured");
				return 0;
		} catch (Exception e) {
			e.printStackTrace();
	//	logger.severe("Exception occured while inserting." + "Exception Message: " + e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try {
		        if (rs != null)
					rs.close();  
				if (ps != null)
					ps.close();
				if (con != null) {
					con.setAutoCommit(true);
					con.close();
				}
			} catch (Exception e) {
			}
			return logId;
		}
	}
		//Select CSD_USER from table KM_CSD_USERS
	
	public  String selectCSDUsers(int circleId) throws KmException {

		logger.info("Entered select from table KM_CSD_USERS");

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs=null;
		String csdUsers = ""; 
		try {
            
			StringBuffer query1=new StringBuffer(SQL_SELECT_CSD_USER);
			con=DBConnection.getDBConnection();
			
			ps=con.prepareStatement(query1.toString());
			ps.setInt(1, circleId);
			
			
			/*Executing the querry */
			
			rs=ps.executeQuery();  
			
			while(rs.next())
			{
				csdUsers = csdUsers+","+rs.getString(1);
			}
			
			if(!"".equals(csdUsers))
			{
				csdUsers = csdUsers.substring(1);
			}
			//System.out.println("csdUsers "+csdUsers);
			return csdUsers;

		} catch (SQLException e) {
				e.printStackTrace();
				logger.info(e);
				logger.info("SQL Exception occured");
				return csdUsers;
		} catch (Exception e) {
			e.printStackTrace();
			throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try {
		        if (rs != null)
					rs.close();  
				if (ps != null)
					ps.close();
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}
		
		
	}

	//new change
	public  String getCircle(int circleId) throws KmException {
		
		logger.info("Entered to get the circle id of TSG User");

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs=null;
		String circleCode = ""; 
		try {
            
			StringBuffer query1=new StringBuffer(SQL_SELECT_TSG_CIRCLE_ID);
			con=DBConnection.getDBConnection();
			
			ps=con.prepareStatement(query1.toString());
			ps.setInt(1, circleId);
			
			
			/*Executing the querry */
			
			rs=ps.executeQuery();  
			
			while(rs.next())
			{
				circleCode = rs.getString(1);
			}
			
			
			return circleCode;

		} catch (SQLException e) {
				logger.info(e);
				logger.info("SQL Exception occured");
				throw new KmException("Exception: " + e.getMessage(), e);
		} catch (Exception e) {
			
			throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try {
		        if (rs != null)
					rs.close();  
				if (ps != null)
					ps.close();
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}
		
			
	}
 }