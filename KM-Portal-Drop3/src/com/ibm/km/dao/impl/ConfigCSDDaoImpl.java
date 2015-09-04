
package com.ibm.km.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import com.ibm.km.dao.ConfigCSDDao;
import com.ibm.km.dao.DBConnection;
import com.ibm.km.dto.ConfigCSDDto;
import com.ibm.km.exception.DAOException;


/**
 * @author Ajil Chandran
 * Created On 25/11/2010
 */

public class ConfigCSDDaoImpl implements ConfigCSDDao {
	/*
	 * Logger for the class.
	 */
	private static Logger logger =	null;
	static {

		logger = Logger.getLogger(ConfigCSDDaoImpl.class.getName());
	}

protected static final String SQL_INSERT_CSD_USER= " INSERT INTO KM_CSD_USERS(CSD_MOBILE_NO, CIRCLE_ID, STATUS, UPDATED_DATE) VALUES( ? , ? , 'A' , current date)";
protected static final String SQL_GET_CSD_USERS= " select CSD_MOBILE_NO from KM_CSD_USERS where CIRCLE_ID= ? and STATUS= ?  WITH UR";
protected static final String SQL_REMOVE_CSD_USER="UPDATE KM_CSD_USERS SET STATUS= ?,UPDATED_DATE= current date WHERE CIRCLE_ID= ? AND CSD_MOBILE_NO in ";	
//protected static final String SQL_GET_CIRCLE_NAME="SELECT em.ELEMENT_NAME FROM KM_CSD_USERS cu, KM_ELEMENT_MSTR em WHERE em.ELEMENT_ID=cu.CIRCLE_ID AND cu.STATUS= 'A' AND cu.CSD_MOBILE_NO= ?";
protected static final String SQL_GET_CIRCLE_COUNT="SELECT COUNT(*)FROM KM_CSD_USERS WHERE STATUS= 'A' AND CSD_MOBILE_NO= ?  WITH UR";

	public int insertCsd(ConfigCSDDto configCSDDto) throws DAOException {
		logger.info("Entered insert for table KM_CSD_USERS");
		////System.out.println("inside dao impl insert function");
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		int rowsUpdated = 0;
		//"INSERT INTO KM_CIRCLE_MSTR (CIRCLE_NAME, CIRCLE_ID, CREATED_DT, CREATED_BY, STATUS, SINGLE_SIGN_IN) VALUES (?, NEXTVAL FOR KM_CIRCLE_ID, current timestamp, ?, 'A', 'N')";
		int errCode =0;
		String sqlState = null;
		//String[] csdList=null; 
		String circleId=null;
		try {
			String sql= SQL_INSERT_CSD_USER;
			con = DBConnection.getDBConnection();
			
			ps = con.prepareStatement(sql);
			// ps.setInt(paramCount++, Integer.parseInt(dto.getCreatedBy()));
			
			ps.setString(1,configCSDDto.getMobileNumber());
			ps.setInt(2, Integer.parseInt(configCSDDto.getCircleId().trim()));
			try{
			rowsUpdated=ps.executeUpdate();
			logger.info(
					"Row insertion successful on table:KM_CSD_USERS. Inserted :"
						+ rowsUpdated
						+ " rows");
			}catch (SQLException e) {
				
				
			////System.out.println("sql excep catched");
				errCode=e.getErrorCode();
				sqlState=e.getSQLState();
												
			//	e.getSQLState();
			}
			
			if(errCode == -803 && sqlState.equals("23505")){
			/*	user alredy exist on the table so we dont need to insert
			 * just change the status to 'A'
			 *  
			 */
				logger.info("user alredy exist on table KM_CSD_USERS : calling updateCsd() to change the status to 'A'");
				String[] csdList={configCSDDto.getMobileNumber()};
				circleId=configCSDDto.getCircleId();
			
				rowsUpdated=this.updateCsd(csdList, circleId,"A");
			}
			
			////System.out.println("success");
			
		} 		
		 
		catch (SQLException e) {

			logger.error(
				"SQL Exception occured while inserting." + "Exception Message: " + e.getMessage());
			throw new DAOException("SQLException: " + e.getMessage(), e);
		} catch (DAOException e) {
			//e.printStackTrace();
			logger.error(
				"Exception occured while inserting." + "Exception Message: " + e.getMessage());
			throw new DAOException("Exception: " + e.getMessage(), e);
		}
		
		catch (Exception e) {
			
			logger.error(
				"Exception occured while inserting." + "Exception Message: " + e.getMessage());
			throw new DAOException("Exception: " + e.getMessage(), e);
		}
		finally {
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
				logger.error(
						"DAO Exception occured while inserting."
							+ "Exception Message: "
							+ e.getMessage());
					throw new DAOException("DAO Exception: " + e.getMessage(), e);
			}
			
				}
		return rowsUpdated;
	}

	public List fetchCsdUsers(int circleId) throws DAOException {
		logger.info("Entered fetchCsdUsers for table KM_CSD_USERS");
		Connection con=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		List csdNumberList =new ArrayList();
		ConfigCSDDto configCSDDto=null;
	
		try {
			
			String sql=SQL_GET_CSD_USERS;
			con = DBConnection.getDBConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1,circleId);
			ps.setString(2, "A");
			
			rs=ps.executeQuery();
			logger.info(
					"Fetching  successful on table:KM_CSD_USERS");
			 while(rs.next()){
				 configCSDDto= new ConfigCSDDto();

				configCSDDto.setCsdUserNumber(rs.getString("CSD_MOBILE_NO"));
				csdNumberList.add(configCSDDto);
				 
			 }
			
			
		} catch (SQLException e) {

			logger.error(
				"SQL Exception occured while fetching." + "Exception Message: " + e.getMessage());
			throw new DAOException("SQLException: " + e.getMessage(), e);
		} catch (DAOException e) {
			//e.printStackTrace();
			logger.error(
				"Exception occured while fetching." + "Exception Message: " + e.getMessage());
			throw new DAOException("Exception: " + e.getMessage(), e);
		}
		finally {
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
				logger.error(
					"DAO Exception occured while fetching."
					+ "Exception Message: "
					+ e.getMessage());
			throw new DAOException("DAO Exception: " + e.getMessage(), e);
			}
		}
		
		
		return csdNumberList;
	}

	public int updateCsd(String[] csdList, String circleId, String status) throws DAOException {
			
		
		logger.info("Entered updateCsd for table KM_CSD_USERS");
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int rowsUpdated=0;
		StringBuilder  mobNoumbers=new StringBuilder("( ");
try {	
		/*for (int i=0; i<csdList.length;i++)
			{  if(i==0){
				mobNoumbers ="("+ csdList[0];
					}
			else{
					mobNoumbers = mobNoumbers+","+csdList[i];  
				}
			}*/
	
	for(String csd :csdList){

				mobNoumbers.append(csd).append(",");  
			
	}
	
		String sql= SQL_REMOVE_CSD_USER+mobNoumbers.substring(0, mobNoumbers.lastIndexOf(","))+")";
		////System.out.println(sql);
		con = DBConnection.getDBConnection();
		ps = con.prepareStatement(sql);
		ps.setString(1, status);
		ps.setInt(2, Integer.parseInt(circleId.trim()));
		rowsUpdated =ps.executeUpdate();
		logger.info(
		"Updation successful on table:KM_CSD_USERS");
		
		/*	for (int i=0; i<csdList.length;i++)
		{	////System.out.println(csdList[i]);
		 ps.setInt(3, Integer.parseInt(csdList[i].trim()));
		 ps.executeUpdate();
		 //System.out.println("removed");
		} */
	
} 

catch (SQLException e) {

	logger.error(
		"SQL Exception occured while deleting csd user" + "Exception Message: " + e.getMessage());
	throw new DAOException("SQLException: " + e.getMessage(), e);
} catch (DAOException e) {
	logger.error(
			"Exception occured while deleting csd user." + "Exception Message: " + e.getMessage());
		throw new DAOException("Exception: " + e.getMessage(), e);
	
}
 catch (Exception e) {
	//e.printStackTrace();
	logger.error(
		"Exception occured while deleting csd user." + "Exception Message: " + e.getMessage());
	throw new DAOException("Exception: " + e.getMessage(), e);
}
finally {
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
		logger.error(
			"DAO Exception occured while deleting csd user."
			+ "Exception Message: "
			+ e.getMessage());
	throw new DAOException("DAO Exception: " + e.getMessage(), e);
	}
}
		
		
		return rowsUpdated;
	}
/*
	public int getCircleCount(ConfigCSDDto configCSDDto) throws KmException {
		Connection con= null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		int circleCount=0;
		//System.out.println("inside get C name");
		try {
			String sql= SQL_GET_CIRCLE_COUNT;
			////System.out.println(sql);
			con = DBConnection.getDBConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1,Integer.parseInt(configCSDDto.getMobileNumber().trim()) );
			rs=ps.executeQuery();
			while(rs.next()){
				circleCount=rs.getInt(1);
				//System.out.println("circle name : "+rs.getInt(1));
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return circleCount;
	} */

}
