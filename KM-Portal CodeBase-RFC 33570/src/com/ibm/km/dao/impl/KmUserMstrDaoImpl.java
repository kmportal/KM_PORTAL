package com.ibm.km.dao.impl;

import java.sql.CallableStatement;

import com.ibm.jtc.orb.map.Value;
import com.ibm.km.common.Constants;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.ibm.km.common.PropertyReader;
import com.ibm.km.dao.DBConnection;
import com.ibm.km.dao.KmUserMstrDao;
import com.ibm.km.dto.KmActorMstr;
import com.ibm.km.dto.KmUserDto;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;
import com.ibm.xtq.ast.nodes.ValueOf;
public class KmUserMstrDaoImpl implements KmUserMstrDao {



	private static Logger logger =
		Logger.getLogger(KmUserMstrDaoImpl.class);
						/*commented by ajil to solve issue in bulk deletion
	public static final String SQL_DELETE_USER ="UPDATE KM_USER_MSTR SET STATUS = 'D' UPDATED_DT = current timestamp  " +
												"WHERE UPPER(USER_LOGIN_ID) = ? AND STATUS = 'A' AND and ";
	*/
	public static final String SQL_DELETE_USER ="UPDATE KM_USER_MSTR SET STATUS = 'D',UPDATED_DT=current timestamp " +
												"WHERE UPPER(USER_LOGIN_ID) = ? AND STATUS = 'A' ";

	public static final String SQL_GET_CIRCLE_USERS ="select USER_ID from KM_USER_MSTR where element_id= ? and KM_ACTOR_ID = 3 ";

	
	protected static final String SQL_INSERT_WITH_ID ="INSERT INTO KM_USER_MSTR (USER_ID, USER_LOGIN_ID, USER_FNAME, USER_MNAME, USER_LNAME, USER_MOBILE_NUMBER, USER_EMAILID, USER_PASSWORD, USER_PSSWRD_EXPRY_DT, CREATED_DT, CREATED_BY,  STATUS, CIRCLE_ID, GROUP_ID, KM_ACTOR_ID, KM_OWNER_ID, LOGIN_ATTEMPTED, USER_LOGIN_STATUS, ELEMENT_ID, FAV_CATEGORY_ID,OLM_ID,PARTNER,PBX_ID,BUSINESS_SEGEMENT,ACTIVITY,ROLE) VALUES (NEXTVAL FOR KM_USER_ID_SEQ, ?, ?, ?, ?, ?, ?, ?, current timestamp + 45 DAYS, current timestamp , ?, ?, ?, ?, ?, ?, 0, 'N',?,?,?,?,?,?,?,?)";
	protected static final String SQL_SELECT ="SELECT KM_USER_MSTR.USER_ID, KM_USER_MSTR.USER_LOGIN_ID, KM_USER_MSTR.USER_FNAME, KM_USER_MSTR.USER_MNAME, KM_USER_MSTR.USER_LNAME, KM_USER_MSTR.USER_MOBILE_NUMBER, KM_USER_MSTR.USER_EMAILID, KM_USER_MSTR.USER_PASSWORD, KM_USER_MSTR.USER_PSSWRD_EXPRY_DT, KM_USER_MSTR.CREATED_DT, KM_USER_MSTR.CREATED_BY, KM_USER_MSTR.UPDATED_DT, KM_USER_MSTR.UPDATED_BY, KM_USER_MSTR.STATUS, KM_USER_MSTR.CIRCLE_ID, KM_USER_MSTR.GROUP_ID, KM_USER_MSTR.KM_ACTOR_ID, KM_USER_MSTR.KM_OWNER_ID, KM_USER_MSTR.LOGIN_ATTEMPTED, KM_USER_MSTR.LAST_LOGIN_TIME, KM_USER_MSTR.USER_LOGIN_STATUS, KM_USER_MSTR.FAV_CATEGORY_ID, KM_USER_MSTR.ELEMENT_ID ,  KM_USER_MSTR.FAV_CATEGORY_ID FROM KM_USER_MSTR ";

		protected static final String SQL_SELECT_USERS = "SELECT  KM_USER_MSTR.USER_ID, KM_USER_MSTR.USER_LOGIN_ID, KM_USER_MSTR.USER_FNAME, KM_USER_MSTR.USER_MNAME, KM_USER_MSTR.USER_LNAME, KM_USER_MSTR.USER_MOBILE_NUMBER, KM_USER_MSTR.USER_EMAILID, KM_USER_MSTR.USER_PASSWORD, KM_USER_MSTR.USER_PSSWRD_EXPRY_DT, KM_USER_MSTR.CREATED_DT, KM_USER_MSTR.CREATED_BY, KM_USER_MSTR.UPDATED_DT, KM_USER_MSTR.UPDATED_BY, KM_USER_MSTR.STATUS, KM_USER_MSTR.CIRCLE_ID, KM_USER_MSTR.GROUP_ID, KM_USER_MSTR.KM_ACTOR_ID, KM_USER_MSTR.KM_OWNER_ID, KM_USER_MSTR.LOGIN_ATTEMPTED, KM_USER_MSTR.LAST_LOGIN_TIME, KM_USER_MSTR.USER_LOGIN_STATUS, KM_USER_MSTR.FAV_CATEGORY_ID, KM_USER_MSTR.ELEMENT_ID FROM KM_USER_MSTR WHERE   ";

	protected static final String SQL_SELECT_USERS2 =    "SELECT KM_USER_MSTR.USER_ID, KM_USER_MSTR.USER_LOGIN_ID, KM_USER_MSTR.USER_FNAME, KM_USER_MSTR.USER_MNAME, KM_USER_MSTR.USER_LNAME, KM_USER_MSTR.USER_MOBILE_NUMBER, KM_USER_MSTR.USER_EMAILID, KM_USER_MSTR.USER_PASSWORD, KM_USER_MSTR.USER_PSSWRD_EXPRY_DT, KM_USER_MSTR.CREATED_DT, KM_USER_MSTR.CREATED_BY , KM_USER_MSTR.UPDATED_DT, KM_USER_MSTR.UPDATED_BY, KM_USER_MSTR.STATUS, KM_USER_MSTR.CIRCLE_ID, KM_USER_MSTR.GROUP_ID, KM_USER_MSTR.KM_ACTOR_ID, KM_USER_MSTR.KM_OWNER_ID, KM_USER_MSTR.LOGIN_ATTEMPTED, KM_USER_MSTR.LAST_LOGIN_TIME, KM_USER_MSTR.USER_LOGIN_STATUS, KM_USER_MSTR.FAV_CATEGORY_ID, KM_ELEMENT_MSTR.ELEMENT_NAME FROM KM_USER_MSTR, KM_ELEMENT_MSTR,KM_ACTORS WHERE  ";

	protected static final String SQL_SELECT_USERID ="SELECT USER_LOGIN_ID,ELEMENT_ID FROM KM_USER_MSTR WHERE KM_USER_MSTR.USER_LOGIN_ID = ? ";

	//protected static final String SQL_UPDATE = "UPDATE KM_USER_MSTR SET USER_ID = ?, USER_LOGIN_ID = ?, USER_FNAME = ?, USER_MNAME = ?, USER_LNAME = ?, USER_MOBILE_NUMBER = ?, USER_EMAILID = ?, USER_PASSWORD = ?, USER_PSSWRD_EXPRY_DT = ?, CREATED_DT = ?, CREATED_BY = ?, UPDATED_DT = ?, UPDATED_BY = ?, STATUS = ?, CIRCLE_ID = ?, GROUP_ID = ?, KM_ACTOR_ID = ?, KM_OWNER_ID = ?, LOGIN_ATTEMPTED = ?, LAST_LOGIN_TIME = ? WHERE USER_ID = ?";

	//protected static final String SQL_DELETE = "DELETE FROM KM_USER_MSTR WHERE USER_ID = ?";

	protected static final String SQL_UPDATE_PWD ="UPDATE KM_USER_MSTR SET USER_PASSWORD=?,UPDATED_DT=current timestamp,UPDATED_BY=?, USER_PSSWRD_EXPRY_DT =(current timestamp+45 DAYS), LAST_LOGIN_TIME=(current timestamp) WHERE USER_ID = ?";

	protected static final String SQL_SELECT_CIRCLE_APPROVER ="SELECT KM_USER_MSTR.USER_ID, KM_USER_MSTR.USER_LOGIN_ID, KM_USER_MSTR.USER_FNAME, KM_USER_MSTR.USER_MNAME, KM_USER_MSTR.USER_LNAME, KM_USER_MSTR.USER_MOBILE_NUMBER, KM_USER_MSTR.USER_EMAILID, KM_USER_MSTR.USER_PASSWORD, KM_USER_MSTR.USER_PSSWRD_EXPRY_DT, KM_USER_MSTR.CREATED_DT, KM_USER_MSTR.CREATED_BY, KM_USER_MSTR.UPDATED_DT, KM_USER_MSTR.UPDATED_BY, KM_USER_MSTR.STATUS, KM_USER_MSTR.CIRCLE_ID, KM_USER_MSTR.GROUP_ID, KM_USER_MSTR.KM_ACTOR_ID, KM_USER_MSTR.KM_OWNER_ID, KM_USER_MSTR.LOGIN_ATTEMPTED, KM_USER_MSTR.LAST_LOGIN_TIME, KM_CIRCLE_MSTR.CIRCLE_NAME FROM KM_USER_MSTR, KM_CIRCLE_MSTR WHERE KM_USER_MSTR.CIRCLE_ID = KM_CIRCLE_MSTR.CIRCLE_ID AND KM_USER_MSTR.KM_ACTOR_ID = 2";

	protected static final String SQL_SELECT_CIRCLE_USER ="SELECT KM_USER_MSTR.USER_ID, KM_USER_MSTR.USER_LOGIN_ID, KM_USER_MSTR.USER_FNAME, KM_USER_MSTR.USER_MNAME, KM_USER_MSTR.USER_LNAME, KM_USER_MSTR.USER_MOBILE_NUMBER, KM_USER_MSTR.USER_EMAILID, KM_USER_MSTR.USER_PASSWORD, KM_USER_MSTR.USER_PSSWRD_EXPRY_DT, KM_USER_MSTR.CREATED_DT, KM_USER_MSTR.CREATED_BY, KM_USER_MSTR.UPDATED_DT, KM_USER_MSTR.UPDATED_BY, KM_USER_MSTR.STATUS, KM_USER_MSTR.CIRCLE_ID, KM_USER_MSTR.GROUP_ID, KM_USER_MSTR.KM_ACTOR_ID, KM_USER_MSTR.KM_OWNER_ID, KM_USER_MSTR.LOGIN_ATTEMPTED, KM_USER_MSTR.LAST_LOGIN_TIME, KM_CIRCLE_MSTR.CIRCLE_NAME FROM KM_USER_MSTR, KM_CIRCLE_MSTR WHERE KM_USER_MSTR.CIRCLE_ID = KM_CIRCLE_MSTR.CIRCLE_ID";

	protected static final String SQL_UPDATE_USER ="UPDATE KM_USER_MSTR SET  USER_FNAME = ?, USER_MNAME = ?, USER_LNAME = ?, USER_MOBILE_NUMBER = ?, USER_EMAILID = ?, STATUS = ?,  UPDATED_BY = ?, UPDATED_DT = current timestamp, USER_LOGIN_STATUS =? ";
		
	protected static final String SQL_UPDATE_USER_BULK ="UPDATE KM_USER_MSTR SET  USER_FNAME = ?, USER_MNAME = ?, USER_LNAME = ?, USER_MOBILE_NUMBER = ?, USER_EMAILID = ?,   STATUS = 'A', UPDATED_BY = ?, UPDATED_DT = current timestamp ";

	protected static final String SQL_SELECT_CIRCLE ="SELECT KM_CIRCLE_MSTR.CIRCLE_NAME, KM_CIRCLE_MSTR.CIRCLE_ID FROM KM_CIRCLE_MSTR WHERE STATUS = 'A' ORDER BY CIRCLE_NAME";

	protected static final String SQL_GET_FAV_CATEGORY ="SELECT FAV_CATEGORY_ID FROM KM_USER_MSTR WHERE USER_ID = ? ";

	protected static final String SQL_COUNT_ELEMENT_USERS ="select count(element_id) as user_count from KM_USER_MSTR where element_id = ? ";

	protected static final String SQL_CHECK_FOR_FAVOURITE ="select count(element_id)as user_count from KM_USER_MSTR where fav_category_id=?";

	protected static final String SQL_GET_MANAGERS =" select * from Km_user_mstr usr where status='A' and  user_id = ";

	//added by vishwas
	protected static final String SQL_SELECT_ACTOR = "SELECT KM_ACTOR_ID, KM_ACTOR_NAME  FROM KM_ACTORS WITH UR";
	//end by vishwas
	
	protected static final String SQL_GET_APPROVER ="select user_fname,user_lname from KM_USER_MSTR usr, Km_escalation_matrix esc where usr.user_id=esc.CIRCLE_ADMIN_ID and esc.CIRCLE_ID=?";
	/*	
	 * commented by ajil
	 * 
		protected static final String SQL_INSERT_USER_MSTR_1="insert into KM_USER_MSTR values(NEXTVAL FOR KM_USER_ID_SEQ,?,?,?,?,?,?,?,current timestamp + 45 days, current timestamp,?,null,null,'A',1,1,4,2,0,null,'N',null,?,null,null,?,null)";
		
		protected static final String SQL_INSERT_USER_MSTR_2="insert into KM_USER_MSTR values(NEXTVAL FOR KM_USER_ID_SEQ,?,?,?,?,?,?,?,current timestamp + 45 days, current timestamp,?,null,null,'A',1,1,6,2,0,null,'N',?,?,null,null,?,null )";
	*/	
	
	/* commented by kundan for LDAP implementation
	protected static final String SQL_INSERT_USER_MSTR_1="INSERT INTO KM_USER_MSTR(USER_ID, USER_LOGIN_ID, USER_FNAME, USER_MNAME, USER_LNAME, USER_MOBILE_NUMBER, USER_EMAILID, USER_PASSWORD, USER_PSSWRD_EXPRY_DT, CREATED_DT, CREATED_BY, UPDATED_DT,UPDATED_BY, STATUS, CIRCLE_ID, GROUP_ID, KM_ACTOR_ID, KM_OWNER_ID, LOGIN_ATTEMPTED, LAST_LOGIN_TIME, USER_LOGIN_STATUS, FAV_CATEGORY_ID, ELEMENT_ID, USER_ALERTS, ALERT_ID, PARTNER_NAME, PASSWORD_RESET_TIME) VALUES " +
			" (NEXTVAL FOR KM_USER_ID_SEQ,?,?,?,?,?,?,?,current timestamp + 45 days, current timestamp,?,null,null,'A',1,1,4,2,0,null,'N',null,?,null,null,?,null)";
	
	protected static final String SQL_INSERT_USER_MSTR_2="INSERT INTO KM_USER_MSTR(USER_ID, USER_LOGIN_ID, USER_FNAME, USER_MNAME, USER_LNAME, USER_MOBILE_NUMBER, USER_EMAILID, USER_PASSWORD, USER_PSSWRD_EXPRY_DT, CREATED_DT, CREATED_BY, UPDATED_DT,UPDATED_BY, STATUS, CIRCLE_ID, GROUP_ID, KM_ACTOR_ID, KM_OWNER_ID, LOGIN_ATTEMPTED, LAST_LOGIN_TIME, USER_LOGIN_STATUS, FAV_CATEGORY_ID, ELEMENT_ID, USER_ALERTS, ALERT_ID, PARTNER_NAME, PASSWORD_RESET_TIME) VALUES " +
			" (NEXTVAL FOR KM_USER_ID_SEQ,?,?,?,?,?,?,?,current timestamp + 45 days, current timestamp,?,null,null,'A',1,1,6,2,0,null,'N',?,?,null,null,?,null )";
   */
	
	// LDAP implementation changes - addition of column OLD_ID in the query
	protected static final String SQL_INSERT_USER_MSTR_1="INSERT INTO KM_USER_MSTR(USER_ID, USER_LOGIN_ID, USER_FNAME, USER_MNAME, USER_LNAME, USER_MOBILE_NUMBER, USER_EMAILID, USER_PASSWORD, USER_PSSWRD_EXPRY_DT, CREATED_DT, CREATED_BY, UPDATED_DT,UPDATED_BY, STATUS, CIRCLE_ID, GROUP_ID, KM_ACTOR_ID, KM_OWNER_ID, LOGIN_ATTEMPTED, LAST_LOGIN_TIME, USER_LOGIN_STATUS, FAV_CATEGORY_ID, ELEMENT_ID, USER_ALERTS, ALERT_ID, PARTNER_NAME, PASSWORD_RESET_TIME,OLM_ID) VALUES " +
	" (NEXTVAL FOR KM_USER_ID_SEQ,?,?,?,?,?,?,?,current timestamp + 45 days, current timestamp,?,null,null,'A',1,1,4,2,0,null,'N',null,?,null,null,?,null,?)";

    protected static final String SQL_INSERT_USER_MSTR_2="INSERT INTO KM_USER_MSTR(USER_ID, USER_LOGIN_ID, USER_FNAME, USER_MNAME, USER_LNAME, USER_MOBILE_NUMBER, USER_EMAILID, USER_PASSWORD, USER_PSSWRD_EXPRY_DT, CREATED_DT, CREATED_BY, UPDATED_DT,UPDATED_BY, STATUS, CIRCLE_ID, GROUP_ID, KM_ACTOR_ID, KM_OWNER_ID, LOGIN_ATTEMPTED, LAST_LOGIN_TIME, USER_LOGIN_STATUS, FAV_CATEGORY_ID, ELEMENT_ID, USER_ALERTS, ALERT_ID, PARTNER_NAME, PASSWORD_RESET_TIME,OLM_ID) VALUES " +
	" (NEXTVAL FOR KM_USER_ID_SEQ,?,?,?,?,?,?,?,current timestamp + 45 days, current timestamp,?,null,null,'A',1,1,6,2,0,null,'N',?,?,null,null,?,null , ?)";
    //  LDAP implementation change finish
    
    protected static final String SQL_UPDATE_USER_BULK1 = "UPDATE KM_USER_MSTR SET USER_FNAME=?, USER_MNAME=?, USER_LNAME=?, USER_MOBILE_NUMBER=?,USER_EMAILID=?,UPDATED_DT=current timestamp,UPDATED_BY=?, PARTNER_NAME=?,  FAV_CATEGORY_ID=?,ELEMENT_ID=?,PBX_ID=?,BUSINESS_SEGEMENT=?,ROLE=?,ACTIVITY=?,LOCATION=? WHERE USER_LOGIN_ID = ?";
	/*kmphase2 partner search */		
	protected static final String SQL_GET_PARTNER="select PARTNER_NAME from KM_PARTNER_MSTR WHERE STATUS='A' order by PARTNER_NAME";
	
	protected static final String LAST_THREE_PASSWORD_CHECK="Select count(b.USER_PASSWORD) as matches from KM_USER_MSTR a, KM_PASSWORD_HISTORY b where a.USER_LOGIN_ID = ?  and b.USER_PASSWORD = ? and b.USER_LOGIN_ID = a.USER_LOGIN_ID  WITH UR";

	//Insert into the table KM_USER_MSTR

	public int insert(KmUserMstr dto) throws KmException {

		logger.info("Entered insert for table KM_USER_MSTR");

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql;
		int rowsUpdated = 0;
		
		try {
			con = getConnection();
			
			int userCount=0;
			int actorId=Integer.parseInt(dto.getKmActorId());
			if(actorId==7){
				ps = con.prepareStatement("SELECT COUNT(*) AS COUNT FROM KM_USER_MSTR WHERE ELEMENT_ID = ? and KM_ACTOR_ID = ? WITH UR");
				ps.setInt(1, Integer.parseInt(dto.getElementId()));
				ps.setInt(2, actorId);
				rs=ps.executeQuery();
				if(rs.next())
					userCount= Integer.parseInt(rs.getString("COUNT"));
				if(userCount>=Integer.parseInt(PropertyReader.getAppValue("max.report.admin.limit")))
					return -1;
					
			}
			
			//sql = SQL_INSERT_WITH_ID;
			
			
			
			
			
			ps = con.prepareStatement(SQL_INSERT_WITH_ID);
			int paramCount = 0;

			//	Preparing statement
			
			
			ps.setString(1, dto.getUserLoginId().trim());
			ps.setString(2, dto.getUserFname());
			ps.setString(3, dto.getUserMname());
			ps.setString(4, dto.getUserLname());
			ps.setString(5, dto.getUserMobileNumber());
			ps.setString(6, dto.getUserEmailid());
			ps.setString(7, dto.getUserPassword().trim());
			ps.setInt(8, Integer.parseInt(dto.getCreatedBy()));
			ps.setString(9, dto.getStatus());
			ps.setInt(10, 1);
			ps.setInt(11, 1);
			ps.setInt(12, actorId);
			ps.setInt(13, 2);
			ps.setInt(14, Integer.parseInt(dto.getElementId()));
			if (dto.getFavCategoryId() != null
				&& !dto.getFavCategoryId().equals(""))
				ps.setInt(15, Integer.parseInt(dto.getFavCategoryId()));
			else
				ps.setInt(15, 8);
			

			// added for OLM ID addition in 
			ps.setString(16,dto.getUserLoginId().trim().substring(0,8));
			//	Executing querry

			ps.setString(17, dto.getPartner());
			ps.setString(18, dto.getPbxId());
			ps.setString(19, dto.getBusinessSegment());
			ps.setString(20, dto.getActivity());
			ps.setString(21, dto.getRole());

			rowsUpdated = ps.executeUpdate();
			logger.info(
				"Row insertion successful on table:KM_USER_MSTR. Inserted:"
					+ rowsUpdated
					+ " rows");

		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(
				"SQL Exception occured while inserting."
					+ "Exception Message: "
					+ e.getMessage());
			throw new KmException("SQLException: " + e.getMessage(), e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(
				"Exception occured while inserting."
					+ "Exception Message: "
					+ e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try {

				if (con != null) {
					
					con.close();
				}
				DBConnection.releaseResources(null, ps, rs);
			} catch (Exception e) {
				logger.error(
					"DAO Exception occured while inserting."
						+ "Exception Message: "
						+ e.getMessage());
				throw new KmException("DAO Exception: " + e.getMessage(), e);
			}
		}

		return rowsUpdated;
	}

	//Update the user details in the table KM_USER_MSTR
	public int update(KmUserMstr dto) throws KmException {

		logger.info("Entered update for table KM_USER_MSTR");

		Connection con = null;
		PreparedStatement ps = null;
		int numRows = -1;
		//String sql;
		try {
			StringBuffer query=new StringBuffer(SQL_UPDATE_USER);
			if (dto.getCategoryId().equals("")) {
				dto.setCategoryId("0");
				logger.info("Null Category ID");
			}
			if (dto.getCategoryId().equals("0"))
				query.append(" WHERE USER_ID = ? ").toString();
			else
				query.append(", FAV_CATEGORY_ID=? WHERE USER_ID = ? ").toString();
			logger.info("SQL is " + query);
			//con=DatabaseConnection.getDb2DbConnection();
			con = DBConnection.getDBConnection();
			ps = con.prepareStatement(query.append(" with ur").toString());

			logger.info("Before preparing statement");
			ps.setString(1, dto.getUserFname()); //Error
			ps.setString(2, dto.getUserMname());
			ps.setString(3, dto.getUserLname());
			ps.setString(4, dto.getUserMobileNumber());
			ps.setString(5, dto.getUserEmailid());
			ps.setString(6, dto.getStatus());
			ps.setString(7, dto.getUpdatedBy());
			ps.setString(8, dto.getUserLoginStatus());
			if (dto.getCategoryId().equals("0")) {

				ps.setInt(9, Integer.parseInt(dto.getUserId()));
			} else {

				ps.setString(9, dto.getCategoryId());
				ps.setInt(10, Integer.parseInt(dto.getUserId()));
			}
			numRows = ps.executeUpdate();
			logger.info(
				"Update successful on table:KM_USER_MSTR. Updated:"
					+ numRows
					+ " rows");

		} catch (SQLException e) {

			logger.error(e);
			logger.error(
				"SQL Exception occured while update."
					+ "Exception Message: "
					+ e.getMessage());

			throw new KmException("SQLException: " + e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e);
			logger.error(
				"Exception occured while update."
					+ "Exception Message: "
					+ e.getMessage());

			throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try {
				DBConnection.releaseResources(con, ps, null);
			} catch (Exception e) {
				logger.error(
					"DAO Exception occured while update."
						+ "Exception Message: "
						+ e.getMessage());

				throw new KmException("DAO Exception: " + e.getMessage(), e);
			}
		}
		return numRows;

	}
	public int updatePassword(KmUserMstr dto) throws KmException {

		logger.info("Entered update password for table USER_MSTR");
		
		Connection con = null;
		PreparedStatement ps = null;
		CallableStatement cs = null;
		int numRows = -1;
		int updateCnt = 1;
		int passwordcount=Integer.parseInt(PropertyReader.getGsdValue("PasswordDisableLimit"));

		try {
			//con=DatabaseConnection.getDb2DbConnection();
			con = DBConnection.getDBConnection();
			StringBuffer query=new StringBuffer(SQL_UPDATE_PWD);
			ps = con.prepareStatement(query.toString());
			if (dto.getNewPassword() == null)
				ps.setNull(updateCnt++, Types.VARCHAR);
			else
				ps.setString(updateCnt++, dto.getNewPassword());
			if (dto.getUserId() == null)
				ps.setNull(updateCnt++, Types.VARCHAR);
			else
				ps.setString(updateCnt++, dto.getUserId());

			ps.setString(updateCnt++, dto.getUserId());

			numRows = ps.executeUpdate();

			if (1 == numRows) {

				logger.debug("proceure calling");
				cs = con.prepareCall("{call KM2.KM_CHANGEPASSWORD(?,?,?,?)}");
				cs.setString(1, dto.getUserLoginId());
				cs.setString(2, dto.getNewPassword());
				cs.setString(3, dto.getOldPassword());
				cs.setInt(4, passwordcount);
				cs.execute();

			}

			logger.info(
				"Update successful for password on table:KM_USER_MSTR. Updated:"
					+ numRows
					+ " rows");

		} catch (SQLException e) {

			logger.error(
				"SQL Exception occured while updatePassword."
					+ "Exception Message: "
					+ e);
			throw new KmException("SQLException: " + e.getMessage(), e);
		} catch (Exception e) {

			logger.error(
				"Exception occured while updatePassword."
					+ "Exception Message: "
					+ e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (cs != null)
					cs.close();
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				logger.error(
					"DAO Exception occured while updatePassword."
						+ "Exception Message: "
						+ e.getMessage());
				throw new KmException("DAO Exception: " + e.getMessage(), e);
			}
		}
		return numRows;
	}

	protected KmUserMstr fetchSingleResult(ResultSet rs) throws SQLException {
		if (rs.next()) {
			KmUserMstr dto = new KmUserMstr();
			populateDto(dto, rs);
			return dto;
		} else
			return null;
	}

	protected static void populateDto(KmUserMstr dto, ResultSet rs)
		throws SQLException {
		dto.setUserId(rs.getString("USER_ID"));

		dto.setUserLoginId(rs.getString("USER_LOGIN_ID"));

		dto.setUserFname(rs.getString("USER_FNAME"));

		dto.setUserMname(rs.getString("USER_MNAME"));

		dto.setUserLname(rs.getString("USER_LNAME"));

		dto.setUserMobileNumber(rs.getString("USER_MOBILE_NUMBER"));

		dto.setUserEmailid(rs.getString("USER_EMAILID"));

		dto.setUserPassword(rs.getString("USER_PASSWORD"));

		dto.setUserPsswrdExpryDt(rs.getTimestamp("USER_PSSWRD_EXPRY_DT"));

		dto.setCreatedDt(rs.getTimestamp("CREATED_DT"));

		dto.setCreatedBy(rs.getString("CREATED_BY"));

		dto.setUpdatedDt(rs.getTimestamp("UPDATED_DT"));

		dto.setUpdatedBy(rs.getString("UPDATED_BY"));

		dto.setStatus(rs.getString("STATUS"));

		dto.setCircleId(rs.getString("CIRCLE_ID"));

		dto.setGroupId(rs.getString("GROUP_ID"));

		dto.setKmActorId(rs.getString("KM_ACTOR_ID"));

		dto.setKmOwnerId(rs.getString("KM_OWNER_ID"));

		dto.setLoginAttempted(rs.getString("LOGIN_ATTEMPTED"));

		dto.setLastLoginTime(rs.getString("LAST_LOGIN_TIME"));

		dto.setUserLoginStatus(rs.getString("USER_LOGIN_STATUS"));

		dto.setCategoryId(rs.getString("FAV_CATEGORY_ID"));

	}

	//Check for the presense of user login id in the database to prevent duplication while inserting

	public boolean CheckUserId(String userLoginId) throws DAOException,KmException {

		logger.info("Entered CheckUserId for table:KM_USER_MSTR");

		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		boolean isExist = false;
		try {
			StringBuffer query= new StringBuffer("SELECT USER_ID FROM KM_USER_MSTR WHERE USER_LOGIN_ID = ? AND STATUS = 'A' ");
			//String sql =
				query.append("  ").toString();
				//con=DatabaseConnection.getDb2DbConnection();
				con = DBConnection.getDBConnection();
			pst = con.prepareStatement(query.append(" with ur ").toString());
			pst.setString(1, userLoginId);
			rs = pst.executeQuery();
			if (rs.next()) {
				isExist = true;
			}
		}
		catch( Exception ex)
		
		{
		ex.printStackTrace();
	}
	
	/*	catch (SQLException e) {
			e.printStackTrace();
			e.printStackTrace();
			logger.error(
				"SQL Exception occured while CheckUserId."
					+ "Exception Message: "
					+ e.getMessage());
			throw new KmException("SQLException: " + e.getMessage(), e);
		} catch (DAOException e) {

		logger.error(
			"SQL Exception occured while CheckUserId."
				+ "Exception Message: "
				+ e.getMessage());
		throw new DAOException("SQLException: " + e.getMessage(), e);
		}
		 catch (Exception e) {

			logger.error(
				"Exception occured while CheckUserId."
					+ "Exception Message: "
					+ e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
		} */finally {
			try {
				DBConnection.releaseResources(con, pst, rs);
			} catch (Exception e) {
				logger.error(
					"DAO Exception occured while CheckUserId."
						+ "Exception Message: "
						+ e.getMessage());
				throw new KmException("DAO Exception: " + e.getMessage(), e);
			}
		}
		return isExist;
	}

	private Connection getConnection() throws KmException {

		logger.info(
			"Entered getConnection for operation on table:KM_USER_MSTR");

		try {
			return DBConnection.getDBConnection();
		} catch (DAOException e) {

			logger.info("Exception Occured while obtaining connection.");

			throw new KmException(
				"Exception while trying to obtain a connection",
				e);
		}

	}

	/* (non-Javadoc)
	 * @see com.ibm.km.dao.KmUserMstrDao#viewCircleApprovers()
	 */
	public ArrayList viewCircleApprovers() throws KmException {
		Connection con = null;
		ResultSet rs = null;
		KmUserDto dto;
		PreparedStatement ps = null;
		try {

			ArrayList userList = new ArrayList();
			StringBuffer query=new StringBuffer(SQL_SELECT_CIRCLE_APPROVER);
			//String sql =
			query.append(" ORDER BY STATUS, USER_LOGIN_ID").toString();
			//con=DatabaseConnection.getDb2DbConnection();
			con = DBConnection.getDBConnection();
			//ps.setInt(1, Integer.parseInt("2"));
			ps = con.prepareStatement(query.append(" with ur ").toString());
			rs = ps.executeQuery();

			logger.info("success");
			while (rs.next()) {
				dto = new KmUserDto();
				dto.setUserId(rs.getString("USER_ID"));
				dto.setUserLoginId(rs.getString("USER_LOGIN_ID"));
				dto.setUserFname(rs.getString("USER_FNAME"));
				dto.setUserMname(rs.getString("USER_MNAME"));
				dto.setUserLname(rs.getString("USER_LNAME"));
				dto.setUserMobileNumber(rs.getString("USER_MOBILE_NUMBER"));
				dto.setUserEmailid(rs.getString("USER_EMAILID"));
				dto.setStatus(rs.getString("STATUS"));
				dto.setKmActorId(rs.getString("KM_ACTOR_ID"));
				dto.setUserType("CircleApprover");
				dto.setCircleName(rs.getString("CIRCLE_NAME"));

				userList.add(dto);
			}

			return userList;

		} catch (SQLException e) {

			logger.error(
				"SQL Exception occured while Viewing Circle Approvers."
					+ "SQL Exception Message: "
					+ e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
		} catch (Exception e) {
			logger.error(
				" Exception occured while Viewing Circle Approvers."
					+ " Exception Message: "
					+ e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try {
				DBConnection.releaseResources(con, ps, rs);
			} catch (Exception e) {
				logger.error(
					"DAO Exception occured while Viewing Circle Approvers."
						+ "DAO Exception Message: "
						+ e.getMessage());
				throw new KmException("Exception: " + e.getMessage(), e);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.dao.KmUserMstrDao#viewCircleUsers(java.lang.String)
	 */
	public ArrayList viewCircleUsers(String circleId) throws KmException {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		KmUserDto dto;
		try {
			StringBuffer query=new StringBuffer(SQL_SELECT_CIRCLE_USER);
			logger.info("Inside DAO");
			logger.info("Circle Id :" + circleId);
			ArrayList userList = new ArrayList();
			//String sql =
			query.append(" AND KM_USER_MSTR.CIRCLE_ID = ? AND KM_USER_MSTR.KM_ACTOR_ID != 2 AND KM_USER_MSTR.USER_ID != 1 ORDER BY STATUS, USER_LOGIN_ID ").toString();
			//con=DatabaseConnection.getDb2DbConnection();
			con = DBConnection.getDBConnection();
			ps = con.prepareStatement(query.toString());
			ps.setInt(1, Integer.parseInt(circleId));
			rs = ps.executeQuery();

			while (rs.next()) {
				dto = new KmUserDto();
				dto.setUserId(rs.getString("USER_ID"));
				dto.setUserLoginId(rs.getString("USER_LOGIN_ID"));
				dto.setUserFname(rs.getString("USER_FNAME"));
				dto.setUserMname(rs.getString("USER_MNAME"));
				dto.setUserLname(rs.getString("USER_LNAME"));
				dto.setUserMobileNumber(rs.getString("USER_MOBILE_NUMBER"));
				dto.setUserEmailid(rs.getString("USER_EMAILID"));
				dto.setStatus(rs.getString("STATUS"));
				dto.setKmActorId(rs.getString("KM_ACTOR_ID"));
				dto.setUserType("CircleApprover");
				dto.setCircleName(rs.getString("CIRCLE_NAME"));
				String userType = rs.getString("KM_ACTOR_ID");

				if (userType.equals(Constants.CIRCLE_USER)) {
					dto.setUserType("Circle User");

				} else if (userType.equals(Constants.CIRCLE_CSR)) {
					dto.setUserType("CSR");

				}
				userList.add(dto);

			}
			logger.info("circle user success");
			return userList;
		} catch (SQLException e) {

			logger.error(
				"SQL Exception occured while Viewing Circle Users."
					+ "SQL Exception Message: "
					+ e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
		} catch (Exception e) {

			logger.error(
				" Exception occured while Viewing Circle Users."
					+ " Exception Message: "
					+ e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);

		} finally {
			try {
				DBConnection.releaseResources(con, ps, rs);
			} catch (Exception e) {
				logger.error(
					"DAO Exception occured while Viewing Circle Users."
						+ "DAO Exception Message: "
						+ e.getMessage());
				throw new KmException("Exception: " + e.getMessage(), e);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.dao.KmUserMstrDao#selectUser(java.lang.String)
	 * To get the details of the user by passing the userId
	 **/
	public KmUserDto selectUser(String userId) throws KmException {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		KmUserDto dto = null;
		try {
			logger.info("Inside DAO");
			logger.info("User Id :" + userId);
			StringBuffer query=new StringBuffer(SQL_SELECT);
			//String sql = 
			query.append(" WHERE KM_USER_MSTR.USER_ID = ? ").toString();
			//con=DatabaseConnection.getDb2DbConnection();
			con = DBConnection.getDBConnection();
			ps = con.prepareStatement(query.append("  with ur ").toString());
			ps.setInt(1, Integer.parseInt(userId));
			rs = ps.executeQuery();

			while (rs.next()) {
				dto = new KmUserDto();
				dto.setUserId(rs.getString("USER_ID"));
				dto.setUserLoginId(rs.getString("USER_LOGIN_ID"));
				dto.setUserFname(rs.getString("USER_FNAME"));
				dto.setUserMname(rs.getString("USER_MNAME"));
				dto.setUserLname(rs.getString("USER_LNAME"));
				dto.setUserMobileNumber(rs.getString("USER_MOBILE_NUMBER"));
				dto.setUserEmailid(rs.getString("USER_EMAILID"));
				dto.setStatus(rs.getString("STATUS"));
				dto.setCircleId(rs.getString("CIRCLE_ID"));
				dto.setKmActorId(rs.getString("KM_ACTOR_ID"));
				dto.setElementId(rs.getString("ELEMENT_ID"));
				dto.setCategoryId(rs.getString("FAV_CATEGORY_ID"));

			}
			logger.info("user data retrieved successfully");
			return dto;
		} catch (SQLException e) {
			logger.error(
				"SQL Exception occured while selecting Users."
					+ "SQL Exception Message: "
					+ e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
		} catch (Exception e) {
			logger.error(
				" Exception occured while selecting Users."
					+ " Exception Message: "
					+ e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);

		} finally {
			try {
				DBConnection.releaseResources(con, ps, rs);
			} catch (Exception e) {
				logger.error(
					"DAO Exception occured while selecting Users."
						+ "DAO Exception Message: "
						+ e.getMessage());
				throw new KmException("Exception: " + e.getMessage(), e);
			}
		}
	}
	/* (non-Javadoc)
	 * @see com.ibm.km.dao.KmUserMstrDao#viewCsrs(java.lang.String)
	 */
	public ArrayList viewCsrs(String circleId) throws KmException {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		KmUserDto dto;
		try {
			StringBuffer query=new StringBuffer(SQL_SELECT_CIRCLE_USER);
			logger.info("Inside DAO");
			logger.info("Circle Id :" + circleId);
			ArrayList userList = new ArrayList();
			String sql =
				
			query.append(" AND KM_USER_MSTR.CIRCLE_ID = ? AND KM_USER_MSTR.KM_ACTOR_ID = 4  ORDER BY STATUS, USER_LOGIN_ID ").toString();
			//con=DatabaseConnection.getDb2DbConnection();
			con = DBConnection.getDBConnection();
			ps = con.prepareStatement(query.append(" with ur ").toString());
			ps.setInt(1, Integer.parseInt(circleId));
			rs = ps.executeQuery();

			while (rs.next()) {
				dto = new KmUserDto();
				dto.setUserId(rs.getString("USER_ID"));
				dto.setUserLoginId(rs.getString("USER_LOGIN_ID"));
				dto.setUserFname(rs.getString("USER_FNAME"));
				dto.setUserMname(rs.getString("USER_MNAME"));
				dto.setUserLname(rs.getString("USER_LNAME"));
				dto.setUserMobileNumber(rs.getString("USER_MOBILE_NUMBER"));
				dto.setUserEmailid(rs.getString("USER_EMAILID"));
				dto.setStatus(rs.getString("STATUS"));
				dto.setKmActorId(rs.getString("KM_ACTOR_ID"));
				dto.setUserType("CircleApprover");
				dto.setCircleName(rs.getString("CIRCLE_NAME"));
				String userType = rs.getString("KM_ACTOR_ID");

				if (userType.equals(Constants.CIRCLE_USER)) {
					dto.setUserType("Circle User");

				} else if (userType.equals(Constants.CIRCLE_CSR)) {
					dto.setUserType("CSR");

				}
				userList.add(dto);

			}
			logger.info("circle user success");
			return userList;
		} catch (SQLException e) {
			logger.error(
				"SQL Exception occured while Viewing CSRS."
					+ "SQL Exception Message: "
					+ e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
		} catch (Exception e) {
			logger.error(
				" Exception occured while Viewing CSRS."
					+ " Exception Message: "
					+ e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);

		} finally {
			try {
				DBConnection.releaseResources(con, ps, rs);
			} catch (Exception e) {
				logger.error(
					"DAO Exception occured while Viewing CSRS."
						+ "DAO Exception Message: "
						+ e.getMessage());
				throw new KmException("Exception: " + e.getMessage(), e);
			}
		}
	}
	/* (non-Javadoc)
	 * @see com.ibm.km.dao.KmUserMstrDao#viewUsers(java.lang.String, java.lang.String)
	 */
	public ArrayList viewUsers(String loginActorId, String userId)
		throws KmException {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		KmUserDto dto;
		//String sql = "";
		try {
			StringBuffer query=new StringBuffer(SQL_SELECT_USERS);
			logger.info("Inside DAO"+loginActorId);

			ArrayList userList = new ArrayList();
			
			
			if (loginActorId.equals(Constants.SUPER_ADMIN))
				//sql =
			
				query.append(" KM_USER_MSTR.KM_ACTOR_ID IN (2,3,4,5,6,7,8)  AND KM_ELEMENT_MSTR.ELEMENT_ID = KM_USER_MSTR.ELEMENT_ID ORDER BY STATUS, USER_LOGIN_ID ").toString();
			else if (loginActorId.equals(Constants.CIRCLE_ADMIN))
//				sql =
				query.append("   KM_USER_MSTR.CREATED_BY = ? AND KM_ELEMENT_MSTR.ELEMENT_ID = KM_USER_MSTR.ELEMENT_ID ORDER BY STATUS, USER_LOGIN_ID ").toString();
			else if (loginActorId.equals(Constants.CIRCLE_USER))
		//		sql =
				query.append("   KM_USER_MSTR.CREATED_BY = ? AND KM_ELEMENT_MSTR.ELEMENT_ID = KM_USER_MSTR.ELEMENT_ID ORDER BY STATUS, USER_LOGIN_ID ").toString();
			else if (loginActorId.equals(Constants.LOB_ADMIN))
	//			sql =
				query.append("   KM_USER_MSTR.CREATED_BY=? AND KM_ELEMENT_MSTR.ELEMENT_ID = KM_USER_MSTR.ELEMENT_ID ORDER BY STATUS, USER_LOGIN_ID ").toString();
			
			//con=DatabaseConnection.getDb2DbConnection();
			con = DBConnection.getDBConnection();
			ps = con.prepareStatement(query.append(" with ur ").toString());
			if (loginActorId.equals(Constants.CIRCLE_ADMIN)
				|| loginActorId.equals(Constants.CIRCLE_USER)
				|| loginActorId.equals(Constants.LOB_ADMIN))
				ps.setInt(1, Integer.parseInt(userId));
			rs = ps.executeQuery();
			logger.info("SQL Stmt :" + query);
			while (rs.next()) {
				dto = new KmUserDto();
				dto.setUserId(rs.getString("USER_ID"));
				dto.setUserLoginId(rs.getString("USER_LOGIN_ID"));
				dto.setUserFname(rs.getString("USER_FNAME"));
				dto.setUserMname(rs.getString("USER_MNAME"));
				dto.setUserLname(rs.getString("USER_LNAME"));
				dto.setUserMobileNumber(rs.getString("USER_MOBILE_NUMBER"));
				dto.setUserEmailid(rs.getString("USER_EMAILID"));
				dto.setStatus(rs.getString("STATUS"));
				dto.setKmActorId(rs.getString("KM_ACTOR_ID"));
				dto.setElementName(rs.getString("ELEMENT_NAME"));
				String userType = rs.getString("KM_ACTOR_ID");

				if (userType.equals(Constants.CIRCLE_ADMIN)) {
					dto.setUserType("Circle Admin");

				} else if (userType.equals(Constants.CIRCLE_USER)) {
					dto.setUserType("Circle User");

				} else if (userType.equals(Constants.CIRCLE_CSR)) {
					dto.setUserType("CSR");

				} else if (userType.equals(Constants.LOB_ADMIN)) {
					dto.setUserType("LOB Admin");

				} else if (userType.equals(Constants.CATEGORY_CSR)) {
					dto.setUserType("Category CSR");

				
				} else if (userType.equals(Constants.REPORT_ADMIN)) {
				dto.setUserType("Report Admin");

				}
				else if (userType.equals(Constants.TSG_USER)) {
					dto.setUserType("TSG User");

					}
				userList.add(dto);

			}
			logger.info("circle user success");
			return userList;
		} catch (SQLException e) {
			logger.error(
				"SQL Exception occured while Viewing Users."
					+ "SQL Exception Message: "
					+ e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
		} catch (Exception e) {
			logger.error(
				" Exception occured while Viewing Users."
					+ " Exception Message: "
					+ e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try {
				DBConnection.releaseResources(con, ps, rs);
			} catch (Exception e) {
				logger.error(
					"DAO Exception occured while Viewing Users."
						+ "DAO Exception Message: "
						+ e.getMessage());
				throw new KmException("Exception: " + e.getMessage(), e);
			}
		}
	}
	//Added by vishwas on 17/09/14
	public ArrayList viewUsers(String loginActorId,String kmactorid, String userId)
	throws KmException {
	Connection con = null;
	ResultSet rs = null;
	PreparedStatement ps = null;
	KmUserDto dto;
	//String sql = "";
	try {
		StringBuffer query=new StringBuffer(SQL_SELECT_USERS2);
		logger.info(kmactorid+"Inside DAO"+loginActorId+"userId"+Integer.parseInt(userId));

		ArrayList userList = new ArrayList();
		
		
		//1 for super admin
		if (Integer.valueOf(loginActorId).equals(1))
		
			//sql =
			query.append(" KM_ACTORS.KM_ACTOR_NAME ='"+ kmactorid.trim()+"' AND KM_ELEMENT_MSTR.ELEMENT_ID = KM_USER_MSTR.ELEMENT_ID  AND KM_USER_MSTR.KM_ACTOR_ID = KM_ACTORS.KM_ACTOR_ID ORDER BY STATUS, USER_LOGIN_ID ").toString();
			//query.append(" KM_USER_MSTR.KM_ACTOR_ID IN (2,3,4,5,6,7,8)  AND KM_ELEMENT_MSTR.ELEMENT_ID = KM_USER_MSTR.ELEMENT_ID ORDER BY STATUS, USER_LOGIN_ID ").toString();
		
		
		else if (Integer.valueOf(loginActorId).equals(2))
//			sql =
			query.append("  KM_ACTORS.KM_ACTOR_NAME ='"+ kmactorid.trim()+"' and KM_USER_MSTR.CREATED_BY = ? AND KM_ACTORS.KM_ACTOR_ID=KM_USER_MSTR.KM_ACTOR_ID AND KM_ELEMENT_MSTR.ELEMENT_ID = KM_USER_MSTR.ELEMENT_ID ORDER BY STATUS, USER_LOGIN_ID ").toString();
		else if (Integer.valueOf(loginActorId).equals(3))
	//		sql =
			query.append("  KM_ACTORS.KM_ACTOR_NAME ='"+ kmactorid.trim()+"' and KM_USER_MSTR.CREATED_BY = ? AND KM_ACTORS.KM_ACTOR_ID=KM_USER_MSTR.KM_ACTOR_ID AND KM_ELEMENT_MSTR.ELEMENT_ID = KM_USER_MSTR.ELEMENT_ID ORDER BY STATUS, USER_LOGIN_ID ").toString();
		else if (Integer.valueOf(loginActorId).equals(5))
//			sql =
			query.append(" KM_ACTORS.KM_ACTOR_NAME ='"+ kmactorid.trim()+"' and KM_USER_MSTR.CREATED_BY=? AND KM_ACTORS.KM_ACTOR_ID=KM_USER_MSTR.KM_ACTOR_ID AND KM_ELEMENT_MSTR.ELEMENT_ID = KM_USER_MSTR.ELEMENT_ID ORDER BY STATUS, USER_LOGIN_ID ").toString();
		
		//con=DatabaseConnection.getDb2DbConnection();
		con = DBConnection.getDBConnection();
		ps = con.prepareStatement(query.append(" with ur ").toString());
		if (loginActorId.equals(Constants.CIRCLE_ADMIN)|| loginActorId.equals(Constants.CIRCLE_USER)|| loginActorId.equals(Constants.LOB_ADMIN))
			ps.setInt(1, Integer.parseInt(userId));
		logger.info("SQL Stmt888888888888 :" + query);
		rs = ps.executeQuery();
		//logger.info(rs.next()+"SQL Stmt :" + query);
		while (rs.next()) {
			dto = new KmUserDto();
			logger.info("Inside :" + userList.size());
			dto.setUserId(rs.getString("USER_ID"));
			logger.info("rs.getString(USER_ID) " + rs.getString("USER_ID"));
			dto.setUserLoginId(rs.getString("USER_LOGIN_ID"));
			dto.setUserFname(rs.getString("USER_FNAME"));
			logger.info("rs.getString(USER_FNAME) " + rs.getString("USER_FNAME"));
			dto.setUserMname(rs.getString("USER_MNAME"));
			dto.setUserLname(rs.getString("USER_LNAME"));
			dto.setUserMobileNumber(rs.getString("USER_MOBILE_NUMBER"));
			dto.setUserEmailid(rs.getString("USER_EMAILID"));
			dto.setStatus(rs.getString("STATUS"));
			dto.setKmActorId(rs.getString("KM_ACTOR_ID"));
			logger.info("rs.getString(KM_ACTOR_ID) " + rs.getString("KM_ACTOR_ID"));
			dto.setElementName(rs.getString("ELEMENT_NAME"));
			String userType = rs.getString("KM_ACTOR_ID");

			if (userType.equals(Constants.CIRCLE_ADMIN)) {
				dto.setUserType("Circle Admin");

			} else if (userType.equals(Constants.CIRCLE_USER)) {
				dto.setUserType("Circle User");

			} else if (userType.equals(Constants.CIRCLE_CSR)) {
				dto.setUserType("CSR");

			} else if (userType.equals(Constants.LOB_ADMIN)) {
				dto.setUserType("LOB Admin");

			} else if (userType.equals(Constants.CATEGORY_CSR)) {
				dto.setUserType("Category CSR");

			
			} else if (userType.equals(Constants.REPORT_ADMIN)) {
			dto.setUserType("Report Admin");

			}
			else if (userType.equals(Constants.TSG_USER)) {
				dto.setUserType("TSG User");

				}
			else if (userType.equals(Constants.SUPER_ADMIN)) {
				dto.setUserType("Super Admin");

				}
			userList.add(dto);

		}
		logger.info("circle user success"+userList.size());
		return userList;
	} catch (SQLException e) {
		logger.error(
			"SQL Exception occured while Viewing Users."
				+ "SQL Exception Message: "
				+ e.getMessage());
		throw new KmException("Exception: " + e.getMessage(), e);
	} catch (Exception e) {
		logger.error(
			" Exception occured while Viewing Users."
				+ " Exception Message: "
				+ e.getMessage());
		throw new KmException("Exception: " + e.getMessage(), e);
	} finally {
		try {
			DBConnection.releaseResources(con, ps, rs);
		} catch (Exception e) {
			logger.error(
				"DAO Exception occured while Viewing Users."
					+ "DAO Exception Message: "
					+ e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
		}
	}
}

	
	//end by vishwas 17/09/14
	
	//added by vishwas
	public ArrayList<KmActorMstr> getActorList() throws KmException{
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<KmActorMstr> masterList = new ArrayList<KmActorMstr>();
		KmActorMstr dto = null;
		try {
			con = DBConnection.getDBConnection();
			ps = con.prepareStatement(SQL_SELECT_ACTOR);
			//ps.setInt(1, Integer.parseInt(actorID));
			rs = ps.executeQuery();
			while(rs.next()) {
				dto = new KmActorMstr();
				dto.setKmActorId(rs.getString("KM_ACTOR_ID"));
				dto.setKmActorName(rs.getString("KM_ACTOR_NAME"));
				masterList.add(dto);
			}
		} catch (Exception e) {
			throw new KmException("Exception occured while getting Master Types list :  "+ e.getMessage(),e);
		} finally {
			try {
				DBConnection.releaseResources(con, ps, rs);
			} catch (Exception e) {				
				throw new KmException(e.getMessage(), e);
			}
		}
		return masterList;
	}

	
	//end by vishwas
	
	/* (non-Javadoc)
	 * @see com.ibm.km.dao.KmUserMstrDao#getElementsUsers(java.lang.String)
	 */
	public String[] getElementsUsers(String elementId) throws KmException {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String userId = "";
		ArrayList userList = new ArrayList();
		try {
			StringBuffer query=new StringBuffer(SQL_GET_CIRCLE_USERS);
			//String sql = SQL_GET_CIRCLE_USERS;
			//con=DatabaseConnection.getDb2DbConnection();
			con = DBConnection.getDBConnection();
			ps = con.prepareStatement(query.append(" with ur ").toString());
			ps.setInt(1, Integer.parseInt(elementId));
			rs = ps.executeQuery();
			logger.info("USER DAO" + elementId);
			int i = 0;
			while (rs.next()) {
				userId = rs.getString("USER_ID");
				userList.add(userId);
			}
			String[] userIds =
				(String[]) userList.toArray(new String[userList.size()]);
			return userIds;
		} catch (SQLException e) {
			logger.error(
				"SQL Exception occured while getting Element Users."
					+ "SQL Exception Message: "
					+ e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
		} catch (Exception e) {
			logger.error(
				" Exception occured while getting Element Users."
					+ " Exception Message: "
					+ e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try {
				DBConnection.releaseResources(con, ps, rs);
			} catch (Exception e) {
				logger.error(
					"DAO Exception occured while getting Element Users."
						+ "DAO Exception Message: "
						+ e.getMessage());
				throw new KmException("Exception: " + e.getMessage(), e);
			}
		}
	}
	/* (non-Javadoc)
	 * @see com.ibm.km.dao.KmUserMstrDao#getFavCategory(java.lang.String)
	 */
	public String getFavCategory(String userId) throws KmException {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String favCategory = "";
		ArrayList userList = new ArrayList();
		try {
			StringBuffer query=new StringBuffer(SQL_GET_FAV_CATEGORY);
			//String sql = SQL_GET_FAV_CATEGORY;
			//con=DatabaseConnection.getDb2DbConnection();
			con = DBConnection.getDBConnection();
			ps = con.prepareStatement(query.append(" with ur ").toString());
			ps.setInt(1, Integer.parseInt(userId));
			rs = ps.executeQuery();
			if (rs.next()) {
				favCategory = rs.getString("FAV_CATEGORY_ID");
			}
			return favCategory;
		} catch (SQLException e) {
			logger.error(
				"SQL Exception occured while getting Fav Category."
					+ "SQL Exception Message: "
					+ e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
		} catch (Exception e) {
			logger.error(
				" Exception occured while getting Fav Category."
					+ " Exception Message: "
					+ e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try {
				DBConnection.releaseResources(con, ps, rs);
			} catch (Exception e) {
				logger.error(
					"SQL Exception occured while getting Fav Category."
						+ "SQL Exception Message: "
						+ e.getMessage());
				throw new KmException("Exception: " + e.getMessage(), e);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.dao.KmUserMstrDao#noOfElementUsers(java.lang.String)
	 */
	public int noOfElementUsers(String elementId) throws KmException {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		int countUsers = 0;
		try {
            StringBuffer query=new StringBuffer(SQL_COUNT_ELEMENT_USERS);
			//String sql = SQL_COUNT_ELEMENT_USERS;
            //con=DatabaseConnection.getDb2DbConnection();
            con = DBConnection.getDBConnection();
			ps = con.prepareStatement(query.append(" with ur ").toString());
			ps.setInt(1, Integer.parseInt(elementId));
			rs = ps.executeQuery();
			if (rs.next()) {
				countUsers = rs.getInt("user_count");
			}
			return countUsers;
		} catch (SQLException e) {
			logger.error(
				"SQL Exception occured while getting noOfElementUsers."
					+ "SQL Exception Message: "
					+ e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
		} catch (Exception e) {
			logger.error(
				" Exception occured while getting noOfElementUsers."
					+ " Exception Message: "
					+ e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);

		} finally {
			try {
				DBConnection.releaseResources(con, ps, rs);
			} catch (Exception e) {
				logger.error(
					"DAO Exception occured while getting noOfElementUsers."
						+ "DAO Exception Message: "
						+ e.getMessage());
				throw new KmException("Exception: " + e.getMessage(), e);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.dao.KmUserMstrDao#checkForFavourite(java.lang.String)
	 */
	public boolean checkForFavourite(String elementId) throws KmException {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean isFavourite = false;
		int count = 0;
		try {
            StringBuffer query=new StringBuffer(SQL_CHECK_FOR_FAVOURITE);
			//String sql = SQL_CHECK_FOR_FAVOURITE;
            //con=DatabaseConnection.getDb2DbConnection();
            con = DBConnection.getDBConnection();
			ps = con.prepareStatement(query.append(" with ur ").toString());
			ps.setInt(1, Integer.parseInt(elementId));
			rs = ps.executeQuery();
			if (rs.next()) {
				count = rs.getInt("user_count");
			}
			if (count > 0) {
				isFavourite = true;
			}
			return isFavourite;
		} catch (SQLException e) {
			logger.error(
				"SQL Exception occured while checking for Favourite."
					+ "SQL Exception Message: "
					+ e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
		} catch (Exception e) {
			logger.error(
				" Exception occured while checking for Favourite."
					+ " Exception Message: "
					+ e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);

		} finally {
			try {
				DBConnection.releaseResources(con, ps, rs);
			} catch (Exception e) {
				logger.error(
					"DAO Exception occured while checking for Favourite."
						+ "DAO Exception Message: "
						+ e.getMessage());
				throw new KmException("Exception: " + e.getMessage(), e);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.dao.KmUserMstrDao#getManagerDetals(java.lang.String)
	 */
	public KmUserMstr getManagers(String circleId, int escCount)
		throws KmException {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		KmUserMstr manager = null;
		try {

			StringBuffer sql = new StringBuffer(SQL_GET_MANAGERS);
			if (escCount == 1) {
				sql.append(
					" ( select manager1_user_id from KM_ESCALATION_MATRIX where circle_id =? ) ");
			} else {
				sql.append(
					" ( select manager2_user_id from KM_ESCALATION_MATRIX where circle_id =? ) ");
			}

			//con=DatabaseConnection.getDb2DbConnection();
			con = DBConnection.getDBConnection();
			ps = con.prepareStatement(sql.toString() + " with ur ");
			ps.setInt(1, Integer.parseInt(circleId));
			rs = ps.executeQuery();
			if (rs.next()) {
				manager = new KmUserMstr();
				manager.setUserId(rs.getString("USER_ID"));
				manager.setUserFname(rs.getString("USER_FNAME"));
				manager.setUserLoginId(rs.getString("USER_LOGIN_ID"));
				manager.setUserFname(rs.getString("USER_FNAME"));
				manager.setUserLname(rs.getString("USER_LNAME"));
				manager.setUserEmailid(rs.getString("USER_EMAILID"));
			}
			return manager;
		} catch (SQLException e) {
			logger.error(
				"SQL Exception occured while getting managers."
					+ "SQl Exception Message: "
					+ e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
		} catch (Exception e) {
			logger.error(
				" Exception occured while getting managers."
					+ " Exception Message: "
					+ e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);

		} finally {
			try {
				DBConnection.releaseResources(con, ps, rs);
			} catch (Exception e) {
				logger.error(
					"DAO Exception occured while getting managers."
						+ "DAO Exception Message: "
						+ e.getMessage());
				throw new KmException("Exception: " + e.getMessage(), e);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.dao.KmUserMstrDao#getApprover(java.lang.String)
	 */
	public String getApprover(String circleId) throws KmException {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String favCategory = "";
		ArrayList userList = new ArrayList();
		try {
			//String sql = SQL_GET_APPROVER;
			StringBuffer query=new StringBuffer(SQL_GET_APPROVER);
			//con=DatabaseConnection.getDb2DbConnection();
			con = DBConnection.getDBConnection();
			ps = con.prepareStatement(query.append(" with ur ").toString());
			ps.setInt(1, Integer.parseInt(circleId));
			rs = ps.executeQuery();
			String approver = "";
			if (rs.next()) {
				approver =
					rs.getString("user_fname")
						+ " "
						+ rs.getString("user_lname");
			}
			return approver;
		} catch (SQLException e) {
			logger.error(
				"SQL Exception occured while getting Fav Category."
					+ "SQL Exception Message: "
					+ e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
		} catch (Exception e) {
			logger.error(
				" Exception occured while getting Fav Category."
					+ " Exception Message: "
					+ e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try {
				DBConnection.releaseResources(con, ps, rs);
			} catch (Exception e) {
				logger.error(
					"SQL Exception occured while getting Fav Category."
						+ "SQL Exception Message: "
						+ e.getMessage());
				throw new KmException("Exception: " + e.getMessage(), e);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.dao.KmUserMstrDao#deleteUser(java.lang.String)
	 */
	public int deleteUser(String userLoginId) throws Exception {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		int count = 0;
		try {
			
			//String sql = SQL_DELETE_USER;
			StringBuffer query=new StringBuffer(SQL_DELETE_USER);
			//con=DatabaseConnection.getDb2DbConnection();
			con = DBConnection.getDBConnection();
			ps = con.prepareStatement(query.append(" with ur ").toString());
			ps.setString(1, userLoginId.toUpperCase().trim());
			count=ps.executeUpdate();
			logger.info(SQL_DELETE_USER+"delete user"+userLoginId.toUpperCase().trim()+"count"+count);
		} catch (SQLException e) {
			logger.error(e);
			logger.error(
				"SQL Exception occured while deleting user."
					+ "SQL Exception Message: "
					+ e.getMessage());
			//throw new KmException("Exception: " + e.getMessage(), e);
		} catch (Exception e) {
			logger.error(
				" Exception occured while deleting user."
					+ " Exception Message: "
					+ e.getMessage());
			//throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try {
				
				DBConnection.releaseResources(con, ps, rs);
			} catch (Exception e) {
				logger.error(
					"SQL Exception occured while deleting user."
						+ "SQL Exception Message: "
						+ e.getMessage());
				//throw new KmException("Exception: " + e.getMessage(), e);
			}
		}
		return count;

	}

	/* (non-Javadoc)
	 * @see com.ibm.km.dao.KmUserMstrDao#insertUserData(com.ibm.km.dto.KmUserMstr)
	 */
	public int insertUserData(KmUserMstr user) throws DAOException,Exception {
		Connection con = null;
		PreparedStatement ps = null;
		Connection con1 = null;
		PreparedStatement ps1 = null;
		ResultSet rs1=null;
		
		String favCategoryId=null;
		int count=0;
		StringBuffer query=null;
		try {
			try{
				//con1=DatabaseConnection.getDb2DbConnection();
				con1 = DBConnection.getDBConnection();
				ps = con1.prepareStatement("select element_id  from km_element_mstr where parent_id = ? and upper(ELEMENT_NAME) = ? with ur");
				ps.setString(1, user.getElementId());
				ps.setString(2, user.getFavCategoryName().trim().toUpperCase() );
				rs1=ps.executeQuery();
				if(rs1.next()){
					favCategoryId=rs1.getString("ELEMENT_ID");
				}
				
			}
			catch(Exception e){
				
				logger.info("Exception occured while fetching fav category Id for Bul category CSR creation");
			}
			finally{
				DBConnection.releaseResources(con1, ps1, rs1);
			}
			
			
			
						
			if (favCategoryId==null) {
				query=new StringBuffer(SQL_INSERT_USER_MSTR_1);
				/*SQL_INSERT_USER_MSTR_1 =	"insert into KM_USER_MSTR values(NEXTVAL FOR KM_USER_ID_SEQ,?, "
						+ " ?,?,?,?,?,?, "
						+ " current timestamp + 45 days, current timestamp, "
						+ " ?,null,null,'A',1,1,4,2,0,null,'N', "
						+ " null, "
						+ " ?,null,null,?)";
*/
			} else {
				query=new StringBuffer(SQL_INSERT_USER_MSTR_2);
				/*SQL_INSERT_USER_MSTR_2 =
					"insert into KM_USER_MSTR values(NEXTVAL FOR KM_USER_ID_SEQ,?, "
						+ " ?,?,?,?,?,?, "
						+ " current timestamp + 45 days, current timestamp, "
						+ " ?,null,null,'A',1,1,6,2,0,null,'N', "
						+ " (select element_id  from km_element_mstr where parent_id = ? and upper(ELEMENT_NAME) = ?), "
						+ " ?,null,null,? )";
			*/}

			//con=DatabaseConnection.getDb2DbConnection();
			con = getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement(query.append(" with ur").toString());
			int paramCount = 0;

			//	Preparing statement

			ps.setString(1, user.getUserLoginId());
			ps.setString(2, user.getUserFname());
			ps.setString(3, user.getUserMname());
			ps.setString(4, user.getUserLname());
			ps.setString(5, user.getUserMobileNumber());
			ps.setString(6, user.getUserEmailid().trim());
			ps.setString(7, user.getUserPassword().trim());
			ps.setInt(8, Integer.parseInt(user.getCreatedBy()));
			if (favCategoryId==null) {
				//ps.setString(9, user.getElementId());
				ps.setInt(9,Integer.parseInt( user.getElementId()));
				ps.setString(10,user.getPartnerName());
				// addition of OLM_Id value in ps for LDAP impl
				ps.setString(11, user.getUserLoginId().substring(0, 8));
			} else {

				ps.setString(9, favCategoryId);
			//	ps.setString(10, user.getElementId());
				ps.setInt(10, Integer.parseInt( user.getElementId()));
				ps.setString(11,user.getPartnerName());
				// addition of OLM_Id value in ps for LDAP impl				
				ps.setString(12, user.getUserLoginId().substring(0, 8));
			}
			//	Executing querry
			

			ps.executeUpdate();
			
			con.commit();
			count++;

		} catch (SQLException e) {
			logger.error(e);
			logger.error(
				" Exception occured while creating user."
					+ " Exception Message: "
					+ e.getMessage());
			//throw new KmException("Exception: " + e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e);
			logger.error(
				" Exception occured while craeting user."
					+ " Exception Message: "
					+ e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			DBConnection.releaseResources(con, ps, null);	
		}
		return count;
	}


	public void updateUserStatus(KmUserMstr userMstrDto) throws KmException {

		logger.info("Entered Login update for table KM_LOGIN_DATA");

		Connection con = null;
		PreparedStatement ps = null;
		String query=null;
		try {
			con = getConnection();
			if(userMstrDto.getUserLoginStatus().equals("Y")){
				query="INSERT INTO KM_LOGIN_DATA(USER_LOGIN_ID, LOGIN_TIME, ELEMENT_ID, FAV_CATEGORY_ID, USER_FNAME, USER_LNAME, SESSION_ID) VALUES(?, current timestamp, ?,?,?,?,?) ";
				
				/* Inserting into KM_LOGIN_DATA table */				
				ps = con.prepareStatement(query);
				ps.setString(1, userMstrDto.getUserLoginId());
				ps.setString(2, userMstrDto.getElementId());
				if(userMstrDto.getFavCategoryId().equals(""))
					ps.setNull(3, Types.INTEGER);
				else
					ps.setString(3, userMstrDto.getFavCategoryId());
				ps.setString(4, userMstrDto.getUserFname());
				ps.setString(5, userMstrDto.getUserLname());
				ps.setString(6, userMstrDto.getSessionID());
				//System.out.println("\n\n Session ID : "+userMstrDto.getSessionID());
				
				ps.executeUpdate();
				
			}
	/*		else if (userMstrDto.getUserLoginStatus().equals("N")){
				//System.out.println("Logout Process");
				query="update KM_LOGIN_DATA set LOGOUT_TIME = current timestamp where USER_LOGIN_ID = ?  and LOGOUT_TIME IS NULL and SESSION_ID = ? " ;
				
				// Updating Logout time
				
				ps = con.prepareStatement(query);
				ps.setString(1, userMstrDto.getUserLoginId());
//				ps.setString(2, userMstrDto.getUserLoginId());
				ps.setString(2, userMstrDto.getSessionID());
				ps.executeUpdate();
			}*/
			
			logger.info("User Login Status Updated.");
			
			

		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(
				"SQL Exception occured while user status update."
					+ "Exception Message: "
					+ e.getMessage());

			throw new KmException("SQLException: " + e.getMessage(), e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(
				"Exception occured while user status update."
					+ "Exception Message: "
					+ e.getMessage());

			throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try {
				DBConnection.releaseResources(con, ps, null);
			} catch (Exception e) {
				logger.error(
					"DAO Exception occured while user status update."
						+ "Exception Message: "
						+ e.getMessage());

				throw new KmException("DAO Exception: " + e.getMessage(), e);
			}
		}
	}
	
	// Not getting used
	public void updateSessionExpiry(KmUserMstr userMstrDto) throws KmException {

		logger.info("Entered update for table KM_USER_MSTR");

		Connection con = null;
		PreparedStatement ps = null;
		//String sql="";
		String query=null;
		try {
			
				query=("UPDATE KM_USER_MSTR SET SESSION_EXPIRY_TIME = current timestamp + 5 minute WHERE USER_ID = ? with ur");
			
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, userMstrDto.getUserId());
			ps.executeUpdate();
			logger.info(
				"User Status Updated");
			
			/* Modified by Anil for phase 2*/
			
			/* Inserting into KM_LOGIN_DATA table */
			
		/*	ps = con.prepareStatement("INSERT INTO KM_LOGIN_DATA(USER_LOGIN_ID, LOGIN_TIME, ELEMENT_ID, FAV_CATEGORY_ID, USER_FNAME, USER_LNAME, SESSION_ID) VALUES(?, current timestamp, ?,?,?,?,?) ");
			ps.setString(1, userMstrDto.getUserLoginId());
			ps.setString(2, userMstrDto.getElementId());
			ps.setString(3, userMstrDto.getFavCategoryId());
			ps.setString(4, userMstrDto.getUserFname());
			ps.setString(5, userMstrDto.getUserLname());
			ps.setString(6, userMstrDto.getSessionID());
			//System.out.println("\n\n Session ID : "+userMstrDto.getSessionID());
			
			ps.executeUpdate();
			*/

		} catch (SQLException e) {

			logger.error(
				"SQL Exception occured while user status update."
					+ "Exception Message: "
					+ e.getMessage());

			throw new KmException("SQLException: " + e.getMessage(), e);
		} catch (Exception e) {

			logger.error(
				"Exception occured while user status update."
					+ "Exception Message: "
					+ e.getMessage());

			throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try {
				DBConnection.releaseResources(con, ps, null);
			} catch (Exception e) {
				logger.error(
					"DAO Exception occured while user status update."
						+ "Exception Message: "
						+ e.getMessage());

				throw new KmException("DAO Exception: " + e.getMessage(), e);
			}
		}
	}
	/* (non-Javadoc)
	 * @see com.ibm.km.dao.KmUserMstrDao#getUserStatus(java.lang.String)
	 */
	public String getUserStatus(String userLoginId) throws KmException {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String status="";
		try {
			String sql ="SELECT STATUS,KM_ACTOR_ID,USER_LOGIN_STATUS FROM KM_USER_MSTR WHERE USER_LOGIN_ID = ? with ur";
			//con=DatabaseConnection.getDb2DbConnection();
			con = DBConnection.getDBConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, userLoginId);
			rs=ps.executeQuery();
			if(rs.next()){
				if(!rs.getString("STATUS").equalsIgnoreCase("A"))
				{	
					status="D";				
				}
				else if(rs.getString("KM_ACTOR_ID").equals(Constants.CIRCLE_CSR)||rs.getString("KM_ACTOR_ID").equals(Constants.CIRCLE_CSR)){
					status="C";
				}
				else{
					status="A";
				}
			}
			else{
				status="D";
			}

		} catch (SQLException e) {
			
			logger.error(
				"SQL Exception occured getting user details."
					+ "SQL Exception Message: "
					+ e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
		} catch (Exception e) {
			logger.error(
				" Exception occured while deleting user."
					+ " Exception Message: "
					+ e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try {
				DBConnection.releaseResources(con, ps, rs);
			} catch (Exception e) {
				logger.error(
					"SQL Exception occured while deleting user."
						+ "SQL Exception Message: "
						+ e.getMessage());
				throw new KmException("Exception: " + e.getMessage(), e);
			}
		}
		return status;

	}
	
	
	public int bulkUpdate(KmUserMstr dto) throws Exception {

		logger.info("Entered update for table KM_USER_MSTR");	
		Connection con = null;
		PreparedStatement ps = null;
		Connection con1 = null;
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		int numRows = 0;
		int i=1;
		String favCategoryId = null;

		try {			
			StringBuffer query = new StringBuffer(SQL_UPDATE_USER_BULK1);
			con = DBConnection.getDBConnection();
			//con =DatabaseConnection.getDb2DbConnection();
			ps=con.prepareStatement(query.append(" with ur").toString());
			/**
			 * System.out.println("dto.getUserFname()::::::::::::"+dto.getUserFname());
			System.out.println("getUserMname()::::::::::::"+dto.getUserMname());
			System.out.println("getUserLname()::::::::::::"+dto.getUserLname());
			System.out.println("getUserMobileNumber()::::::::::::"+dto.getUserMobileNumber());
			System.out.println("getUserEmailid()::::::::::::"+dto.getUserEmailid());
			System.out.println("getUpdatedBy()::::::::::::"+dto.getUpdatedBy());
			System.out.println("getPartnerName()::::::::::::"+dto.getPartnerName());
			System.out.println("getFavCatId()::::::::::::"+dto.getFavCatId());
			System.out.println("getElementId()::::::::::::"+dto.getElementId());
			System.out.println("getPbxId()::::::::::::"+dto.getPbxId());
			System.out.println("getBusinessSegment()::::::::::::"+dto.getBusinessSegment());
			System.out.println("getRole()::::::::::::"+dto.getRole());
			System.out.println("getActivity()::::::::::::"+dto.getActivity());
			System.out.println("getLocation()::::::::::::"+dto.getLocation());
			System.out.println("getUserLoginId()::::::::::::"+dto.getUserLoginId());
			**/
			ps.setString(1, dto.getUserFname());
			ps.setString(2, dto.getUserMname());
			ps.setString(3, dto.getUserLname());
			ps.setString(4, dto.getUserMobileNumber());
			ps.setString(5, dto.getUserEmailid());
			ps.setString(6, dto.getUpdatedBy());
			ps.setString(7, dto.getPartnerName());
			ps.setInt(8, dto.getFavCatId());
			ps.setString(9, dto.getElementId());
			ps.setString(10, dto.getPbxId());
			ps.setString(11, dto.getBusinessSegment());
			ps.setString(12, dto.getRole());
			ps.setString(13, dto.getActivity());
			ps.setString(14, dto.getLocation());
			ps.setString(15, dto.getUserLoginId());			
			numRows = ps.executeUpdate();
			logger.info("Update successful on table:KM_USER_MSTR. Updated:"
					+ numRows + " rows");

		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("SQL Exception occured while update."
					+ "Exception Message: " + e.getMessage());

			throw new Exception("SQLException: " + e.getMessage(), e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception occured while update."
					+ "Exception Message: " + e.getMessage());

			throw new Exception("Exception: " + e.getMessage(), e);
		} finally {
			try {
				DBConnection.releaseResources(con, ps, null);
			} catch (Exception e) {
				logger.error("DAO Exception occured while update."
						+ "Exception Message: " + e.getMessage());

				throw new Exception("DAO Exception: " + e.getMessage(), e);
			}
		}
		return numRows;

	}


	
	
	
	
	
	
	//Update the user details in the table KM_USER_MSTR
	/**
	public int bulkUpdate(KmUserMstr dto) throws KmException {

		logger.info("Entered update for table KM_USER_MSTR");

		Connection con = null;
		PreparedStatement ps = null;
		Connection con1 = null;
		PreparedStatement ps1 = null;
		ResultSet rs1=null;
		int numRows = 0;
		String favCategoryId=null;
		
		try {
			
			
			try{
				con1=DatabaseConnection.getDb2DbConnection();
				//con1 = DBConnection.getDBConnection();
				String query="select element_id  from km_element_mstr where parent_id = ? and LCASE(ELEMENT_NAME) = ? with ur";
				logger.info(dto.getElementId()+"Entered update for table KM_USER_MSTR"+query+":"+dto.getFavCategoryName().toUpperCase()+":"+dto.getFavCatId());
				ps = con1.prepareStatement(query);
				ps.setString(1, dto.getElementId());
				ps.setString(2, dto.getFavCategoryName().toUpperCase() );
				rs1=ps.executeQuery();
				if(rs1.next()){
					favCategoryId=rs1.getString("ELEMENT_ID");
				}
				
			}
			catch(Exception e){
				dto.setFavCategoryName("");
				logger.info("Exception occured while fetching fav category Id for Bul category CSR creation");
			}
			finally{
				DBConnection.releaseResources(con1, ps1, rs1);
			}
			
			
			
			
			StringBuffer query=new StringBuffer(SQL_UPDATE_USER_BULK);
			
			if (favCategoryId==null)
				query.append(" , PARTNER_NAME = ? , USER_PASSWORD = ?, KM_ACTOR_ID = 4 WHERE USER_LOGIN_ID = ? ").toString();
			else
				query.append(", PARTNER_NAME = ?, FAV_CATEGORY_ID = ?,    USER_PASSWORD = ?, KM_ACTOR_ID = 6 WHERE USER_LOGIN_ID = ? ").toString();
			con=DatabaseConnection.getDb2DbConnection();
			//con = DBConnection.getDBConnection();
			ps = con.prepareStatement(query.append(" with ur").toString());

		
			ps.setString(1, dto.getUserFname()); //Error
			
		
			if(dto.getUserMname()!=null)
				ps.setString(2, dto.getUserMname());
			else
				ps.setString(2,null);
		
			ps.setString(3, dto.getUserLname());
		
			ps.setString(4, dto.getUserMobileNumber());
		
			ps.setString(5, dto.getUserEmailid());
		
			
			ps.setInt(6, Integer.parseInt(dto.getCreatedBy()));
		
			if(dto.getPartnerName()!=null){
				ps.setString(7, dto.getPartnerName());
			}
			else{
				ps.setString(7, null);
			}
			
			if (favCategoryId==null) {
				
				ps.setString(8, dto.getUserPassword());
				ps.setString(9, dto.getUserLoginId());
			} else {

				ps.setString(8, favCategoryId);
				ps.setString(9, dto.getUserPassword());
				ps.setString(10, dto.getUserLoginId());
			}
			numRows = ps.executeUpdate();
			logger.info(
				"Update successful on table:KM_USER_MSTR. Updated:"
					+ numRows
					+ " rows");

		} catch (SQLException e) {
			
			logger.error(
				"SQL Exception occured while update."
					+ "Exception Message: "
					+ e.getMessage());

			throw new KmException("SQLException: " + e.getMessage(), e);
		} catch (Exception e) {
		
			logger.error(
				"Exception occured while update."
					+ "Exception Message: "
					+ e.getMessage());

			throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try {
				DBConnection.releaseResources(con, ps, null);
			} catch (Exception e) {
				logger.error(
					"DAO Exception occured while update."
						+ "Exception Message: "
						+ e.getMessage());

				throw new KmException("DAO Exception: " + e.getMessage(), e);
			}
		}
		return numRows;

	}
	**/
	/* KM Phase2 partner serach */

	
	public ArrayList getPartner() throws KmException{
	Connection con=null;
	ResultSet rs = null;
	PreparedStatement ps = null;
	ArrayList partnerList=new ArrayList();
	try {
			
		//String sql = SQL_GET_PARTNER;
		StringBuffer query=new StringBuffer(SQL_GET_PARTNER);
		con=DBConnection.getDBConnection();
		//logger.info(elementId);
		ps=con.prepareStatement(query.append(" with ur ").toString());
		KmUserMstr user= null;
		//ps.setInt(1, Integer.parseInt(elementId));
		rs = ps.executeQuery();
		while(rs.next()){
		user=new KmUserMstr();
		user.setPartnerName(rs.getString("PARTNER_NAME"));
		partnerList.add(user); 	
	   }
		logger.info("List is returned :"+partnerList.size());	
			
	         
	} catch (SQLException e) {
		
		logger.error(e);
		//	logger.severe("SQL Exception occured while find." + "Exception Message: " + e.getMessage());
		throw new KmException("SQLException: " + e.getMessage(), e);
	} catch (Exception e) {
	
		logger.error(e);
//		logger.severe("Exception occured while find." + "Exception Message: " + e.getMessage());
	throw new KmException("Exception: " + e.getMessage(), e);
	} finally {
		try{
		DBConnection.releaseResources(con,ps,rs);
		}catch(DAOException e){
			logger.error(e);
		throw new KmException(e.getMessage(),e);
	   }				
	}return partnerList;
		}

	public KmUserMstr CheckUserLoginId(String user_login_id) throws Exception, DAOException {
		logger.info("Entered CheckUserId for table:KM_USER_MSTR");

		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		KmUserMstr user = null;
		try {
			//String query1=new String(SQL_SELECT_USERID);
			StringBuffer query= new StringBuffer(SQL_SELECT_USERID);
			//String sql =
				//query.append(" WHERE KM_USER_MSTR.USER_LOGIN_ID = ?  ").toString();
			
				logger.info("check user data query"+query+":"+user_login_id);
				//con=DatabaseConnection.getDb2DbConnection();
				con = DBConnection.getDBConnection();
			pst = con.prepareStatement(query.append(" with ur ").toString());
			logger.info(con+"check user data query1"+query+":"+user_login_id);
			pst.setString(1, user_login_id);
			rs = pst.executeQuery();
			if (rs.next()) {
				user= new KmUserMstr();
				user.setUserLoginId(rs.getString("USER_LOGIN_ID"));
				user.setElementId(rs.getString("ELEMENT_ID"));
			}
		} catch (SQLException e) {
			logger.error(e);
			logger.error(
				"SQL Exception occured while CheckUserId."
					+ "Exception Message: "
					+ e.getMessage());
			//throw new KmException("SQLException: " + e.getMessage(), e);
		} catch (DAOException e) {

		logger.error(
			"SQL Exception occured while CheckUserId."
				+ "Exception Message: "
				+ e.getMessage());
		throw new DAOException("SQLException: " + e.getMessage(), e);
		}
		 catch (Exception e) {

			logger.error(
				"Exception occured while CheckUserId."
					+ "Exception Message: "
					+ e.getMessage());
			//throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try {
				DBConnection.releaseResources(con, pst, rs);
			} catch (Exception e) {
				logger.error(
					"DAO Exception occured while CheckUserId."
						+ "Exception Message: "
						+ e.getMessage());
			//	throw new KmException("DAO Exception: " + e.getMessage(), e);
			}
		}
		return user;
	}

	public KmUserMstr userIdFromMobile(String mobileNumber) throws DAOException {
		logger.info("Entered userIdFromMobile for table:KM_USER_MSTR");

		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		KmUserMstr userDto=null;
		try {
			String query= "SELECT USER_ID,USER_LOGIN_ID,ELEMENT_ID,USER_FNAME,USER_LNAME FROM KM_USER_MSTR WHERE KM_ACTOR_ID IN (2,1) AND USER_MOBILE_NUMBER = ? AND STATUS = 'A'   WITH UR";
			//con=DatabaseConnection.getDb2DbConnection();
			con = DBConnection.getDBConnection();
			pst = con.prepareStatement(query);
			pst.setString(1, mobileNumber);
			rs = pst.executeQuery();
			if (rs.next()) {
				userDto = new KmUserMstr();
				userDto.setUserId(rs.getString("USER_ID"));
				userDto.setElementId(rs.getString("ELEMENT_ID"));
				userDto.setUserFname(rs.getString("USER_FNAME"));
				userDto.setUserLname(rs.getString("USER_LNAME"));
				userDto.setUserLoginId(rs.getString("USER_LOGIN_ID"));
				
			}
		} catch (SQLException e) {
			logger.error(e);
			logger.error(
				"SQL Exception occured while userIdFromMobile."
					+ "Exception Message: "
					+ e.getMessage());
			throw new KmException("SQLException: " + e.getMessage(), e);
		} catch (DAOException e) {

		logger.error(
			"SQL Exception occured while userIdFromMobile."
				+ "Exception Message: "
				+ e.getMessage());
		throw new DAOException("SQLException: " + e.getMessage(), e);
		}
		 catch (Exception e) {

			logger.error(
				"Exception occured while userIdFromMobile."
					+ "Exception Message: "
					+ e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try {
				DBConnection.releaseResources(con, pst, rs);
			} catch (Exception e) {
				logger.error(
					"DAO Exception occured while userIdFromMobile."
						+ "Exception Message: "
						+ e.getMessage());
				throw new KmException("DAO Exception: " + e.getMessage(), e);
			}
		}
		return userDto;
	}

	public KmUserMstr userIdFromMobile(String mobileNumber,String actorIDs) throws DAOException {
		logger.info("Entered userIdFromMobile for table:KM_USER_MSTR");

		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		KmUserMstr userDto=null;
		try {
			String query= "SELECT USER_ID,USER_LOGIN_ID,ELEMENT_ID,USER_FNAME,USER_LNAME, UM.CIRCLE_ID CIRCLE_ID, CIRCLE_NAME " +
			"FROM KM_USER_MSTR UM, KM_CIRCLE_MSTR CM  WHERE UM.CIRCLE_ID=CM.CIRCLE_ID  and KM_ACTOR_ID IN ("+actorIDs+") AND "+
			"USER_MOBILE_NUMBER = ? AND UM.STATUS = 'A'   WITH UR";
			//con=DatabaseConnection.getDb2DbConnection();
			con = DBConnection.getDBConnection();
			pst = con.prepareStatement(query);
			pst.setString(1, mobileNumber);
			rs = pst.executeQuery();
			if (rs.next()) {
				userDto = new KmUserMstr();
				userDto.setUserId(rs.getString("USER_ID"));
				userDto.setElementId(rs.getString("ELEMENT_ID"));
				userDto.setUserFname(rs.getString("USER_FNAME"));
				userDto.setUserLname(rs.getString("USER_LNAME"));
				userDto.setUserLoginId(rs.getString("USER_LOGIN_ID"));
				userDto.setCircleId(rs.getString("CIRCLE_ID"));
				userDto.setCircleName(rs.getString("CIRCLE_NAME"));
			}
		} catch (SQLException e) {
			logger.error(e);
			logger.error(
				"SQL Exception occured while userIdFromMobile."
					+ "Exception Message: "
					+ e.getMessage());
			throw new KmException("SQLException: " + e.getMessage(), e);
		} catch (DAOException e) {

		logger.error(
			"SQL Exception occured while userIdFromMobile."
				+ "Exception Message: "
				+ e.getMessage());
		throw new DAOException("SQLException: " + e.getMessage(), e);
		}
		 catch (Exception e) {

			logger.error(
				"Exception occured while userIdFromMobile."
					+ "Exception Message: "
					+ e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try {
				DBConnection.releaseResources(con, pst, rs);
			} catch (Exception e) {
				logger.error(
					"DAO Exception occured while userIdFromMobile."
						+ "Exception Message: "
						+ e.getMessage());
				throw new KmException("DAO Exception: " + e.getMessage(), e);
			}
		}
		return userDto;
	}
	
	public boolean validateLastThreePasswords(String userLoginId, String password) throws KmException, DAOException {
		Connection con=null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		int count=0;
		try {
			String query=new String(LAST_THREE_PASSWORD_CHECK);
			con=DBConnection.getDBConnection();
			ps=con.prepareStatement(query);
			ps.setString(1,userLoginId);
			ps.setString(2,password);
			rs = ps.executeQuery();	
			if(rs.next())	
				count=Integer.parseInt(rs.getString("matches"));
			if(count == 0)
				return false;
			else
				return true;
			
		} catch (SQLException e) {
			
			logger.error(e);
			throw new KmException("SQLException: " + e.getMessage(), e);
		} catch (Exception e) {
		
			logger.error(e);
		throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try{
			DBConnection.releaseResources(con,ps,rs);
			}catch(DAOException e){
				logger.error(e);
			throw new KmException(e.getMessage(),e);
		   }				
		}
		
	}//validateLastThreePasswords
		
	
}
