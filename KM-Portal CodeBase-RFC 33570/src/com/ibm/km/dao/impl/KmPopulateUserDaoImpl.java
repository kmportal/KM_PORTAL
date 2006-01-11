package com.ibm.km.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.ibm.km.dao.DBConnection;
import com.ibm.km.dao.KmPopulateUserDao;
import com.ibm.km.dto.KmLogin;
import com.ibm.km.exception.DAOException;

public class KmPopulateUserDaoImpl implements KmPopulateUserDao {

	private static final Logger logger;

	static {

		logger = Logger.getLogger(KmPopulateUserDaoImpl.class);
	}

	protected static final String SQL_SELECT =" SELECT KM_MODULE_MSTR.* ,  KM_USER_MSTR.* ,  KM_ACTORS.* ,  KM_ELEMENT_MSTR.* FROM KM_USER_MSTR inner join KM_ACTORS on  KM_USER_MSTR.KM_ACTOR_ID = KM_ACTORS.KM_ACTOR_ID inner join  KM_MODULE_ACTOR_MAPPING on KM_USER_MSTR.KM_ACTOR_ID = KM_MODULE_ACTOR_MAPPING.KM_ACTOR_ID  inner join KM_ELEMENT_MSTR on  KM_USER_MSTR.ELEMENT_ID = KM_ELEMENT_MSTR.ELEMENT_ID ,  KM_MODULE_MSTR  where USER_LOGIN_ID = ? AND USER_PASSWORD = ?	   and KM_MODULE_ACTOR_MAPPING.MODULE_ID = KM_MODULE_MSTR.MODULE_ID AND ( KM_MODULE_MSTR.STATUS in ('A','B') OR KM_MODULE_MSTR.STATUS='N') AND KM_USER_MSTR.STATUS = 'A' ORDER BY KM_MODULE_MSTR.MODULE_ORDER  ";
	protected static final String SQL_SELECT_COLOUMN ="SELECT MAX(SYSCAT.COLUMNS.COLNO)+1 COLUMN_COUNT,SYSCAT.COLUMNS.TABNAME FROM SYSCAT.COLUMNS WHERE SYSCAT.COLUMNS.TABNAME IN ('KM_MODULE_MSTR','KM_USER_MSTR','KM_ACTORS','KM_CIRCLE_MSTR') GROUP BY SYSCAT.COLUMNS.TABNAME";
	protected static final String SQL_SELECT_UD =" SELECT KM_MODULE_MSTR.* ,  KM_USER_MSTR.* ,  KM_ACTORS.* ,  KM_ELEMENT_MSTR.* FROM KM_USER_MSTR inner join KM_ACTORS on  KM_USER_MSTR.KM_ACTOR_ID = KM_ACTORS.KM_ACTOR_ID inner join  KM_MODULE_ACTOR_MAPPING on KM_USER_MSTR.KM_ACTOR_ID = KM_MODULE_ACTOR_MAPPING.KM_ACTOR_ID  inner join KM_ELEMENT_MSTR on  KM_USER_MSTR.ELEMENT_ID = KM_ELEMENT_MSTR.ELEMENT_ID ,  KM_MODULE_MSTR  where USER_LOGIN_ID = ? and KM_MODULE_ACTOR_MAPPING.MODULE_ID = KM_MODULE_MSTR.MODULE_ID AND ( KM_MODULE_MSTR.STATUS in ('A','B') OR KM_MODULE_MSTR.STATUS='N') AND KM_USER_MSTR.STATUS = 'A' ORDER BY KM_MODULE_MSTR.MODULE_ORDER  ";
	public static final int KM_COLOUMN_START = 1;

	public List populateValues(KmLogin dto) throws DAOException {

		logger.info("populateValues Start ..");

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rst = null;
		ResultSetMetaData rsmd = null;

		try {
			StringBuffer query=new StringBuffer(SQL_SELECT);
			con = DBConnection.getDBConnection();
			ps = con.prepareStatement(query.append(" with ur ").toString());
			ps.setString(1, dto.getUserId());
			ps.setString(2, dto.getPassword());
			rst = ps.executeQuery();
			rsmd = rst.getMetaData();
			logger.info("populateValues End ..");
			return fetchUserDetails(rsmd, rst);

		} catch (SQLException e) {

			logger.error(
				"SQL Exception occured while fetching user details."
					+ "Exception Message: "
					+ e.getMessage());
			e.printStackTrace();
			throw new DAOException("SQLException: " + e.getMessage(), e);
		} catch (Exception e) {

			logger.error(
				"Exception occured while populating Values."
					+ "Exception Message: "
					+ e.getMessage());
			throw new DAOException(" Exception: " + e.getMessage(), e);
		} finally {
			try {
				DBConnection.releaseResources(con, ps, rst);
			} catch (Exception e) {
				logger.error(
					"DAOException occured while populating Values."
						+ "Exception Message: "
						+ e.getMessage());
				throw new DAOException("DAO Exception: " + e.getMessage(), e);
			}
		}

	}

	
	//Added by vishwas for UD intregation
	
	
	//end by vishwas
	
	
	protected List fetchUserDetails(ResultSetMetaData rsmd, ResultSet rst)
		throws DAOException {
		List results = new ArrayList();
		List KM_MODULE_MSTR = null;
		List KM_USER_MSTR = null;
		List KM_ACTORS = null;
		List KM_CIRCLE_MSTR = null;
		List resultList = null;
		logger.info("fetchUserDetails start..");
		//Method getColumnCount() called
		HashMap hMap = getColumnCount();

		Iterator iterator = (hMap.keySet()).iterator();
		int KM_MODULE_MSTR_COLOUMN = 0;
		int KM_USER_MSTR_COLOUMN = 0;
		int KM_ACTORS_COLOUMN = 0;
		int KM_CIRCLE_MSTR_COLOUMN = 0;
		try {

			while (iterator.hasNext()) {
				String colCount = iterator.next().toString();
				if (("KM_MODULE_MSTR").equals(colCount)) {

					KM_MODULE_MSTR_COLOUMN =
						((Integer) hMap.get(colCount)).intValue();

				}
				if (("KM_USER_MSTR").equals(colCount)) {

					KM_USER_MSTR_COLOUMN =
						((Integer) hMap.get(colCount)).intValue();

				}
				if (("KM_ACTOR").equals(colCount)) {

					KM_ACTORS_COLOUMN =
						((Integer) hMap.get(colCount)).intValue();
				}
				if (("KM_CIRCLE_MSTR").equals(colCount)) {

					KM_CIRCLE_MSTR_COLOUMN =
						((Integer) hMap.get(colCount)).intValue();
				}
			}
			//logger.info(KM_MODULE_MSTR_COLOUMN+" "+KM_USER_MSTR_COLOUMN+" "+KM_ACTORS_COLOUMN+" "+KM_CIRCLE_MSTR_COLOUMN);
			String[] colNames = new String[rsmd.getColumnCount()];
			for (int cols = 1; cols <= rsmd.getColumnCount(); cols++) {
				colNames[cols - 1] = rsmd.getColumnName(cols);
			}
			KM_MODULE_MSTR = new ArrayList();
			KM_USER_MSTR = new ArrayList();
			KM_ACTORS = new ArrayList();
			KM_CIRCLE_MSTR = new ArrayList();
			while (rst.next()) {
				HashMap moduleMap = new HashMap();
				HashMap userMap = new HashMap();
				HashMap actorMap = new HashMap();
				HashMap circleMap = new HashMap();

				for (int val = 1; val <= rsmd.getColumnCount(); val++) {
					//KM_MODULE_MSTR
					if (val >= KM_COLOUMN_START
						&& val < KM_MODULE_MSTR_COLOUMN) {
						moduleMap.put(colNames[val - 1], rst.getObject(val));
					}
					//KM_USER_MSTR
					if (val >= KM_MODULE_MSTR_COLOUMN
						&& val < KM_USER_MSTR_COLOUMN) {
						userMap.put(colNames[val - 1], rst.getObject(val));
					}
					//KM_ACTOR_MSTR
					if (val >= KM_USER_MSTR_COLOUMN
						&& val < KM_ACTORS_COLOUMN) {
						actorMap.put(colNames[val - 1], rst.getObject(val));
					}
					//KM_CIRCLE_MSTR
					if (val >= KM_ACTORS_COLOUMN
						&& val < KM_CIRCLE_MSTR_COLOUMN) {
						//logger.info("Column name: "+ colNames[val - 1]);
						circleMap.put(colNames[val - 1], rst.getObject(val));
					}
				}
				KM_MODULE_MSTR.add(moduleMap);
				KM_USER_MSTR.add(userMap);
				KM_ACTORS.add(actorMap);
				KM_CIRCLE_MSTR.add(circleMap);

				moduleMap = null;
				userMap = null;
				actorMap = null;
				circleMap = null;
			}
			resultList = new ArrayList();
			resultList.add(KM_MODULE_MSTR);
			resultList.add(KM_USER_MSTR);
			resultList.add(KM_ACTORS);
			resultList.add(KM_CIRCLE_MSTR);
//			logger.info("fetchUserDetails End.." + resultList);

			return resultList;
		} catch (Exception e) {

			logger.error(
				"Exception occured while fetching user details."
					+ "Exception Message: "
					+ e.getMessage());
			e.printStackTrace();
			throw new DAOException("Exception: " + e.getMessage(), e);
		}
	}

	/**
	 * This method return the column count of the tables
	 * @return HashMap
	 */
	private HashMap getColumnCount() throws DAOException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection con = null;
		HashMap columnMap = null;
		try {
			StringBuffer query=new StringBuffer(SQL_SELECT_COLOUMN);
			con = DBConnection.getDBConnection();
			stmt = con.prepareStatement(query.append(" with ur ").toString());
			rs = stmt.executeQuery();
			int KM_MODULE_MSTR_COLOUMN_COUNT = 0;
			int KM_USER_MSTR_COLOUMN_COUNT = 0;
			int KM_ACTORS_COLOUMN_COUNT = 0;
			int KM_CIRCLE_MSTR_COLOUMN_COUNT = 0;
			while (rs.next()) {
				//Contains total number of column in KM_MODULE_MSTR
				if (("KM_MODULE_MSTR").equals(rs.getString("TABNAME"))) {
					KM_MODULE_MSTR_COLOUMN_COUNT = rs.getInt("COLUMN_COUNT");
				}
				//Contains total number of column in KM_USER_MSTR
				if (("KM_USER_MSTR").equals(rs.getString("TABNAME"))) {
					KM_USER_MSTR_COLOUMN_COUNT = rs.getInt("COLUMN_COUNT");
				}
				//Contains total number of column in KM_ACTOR_MSTR
				if (("KM_ACTORS").equals(rs.getString("TABNAME"))) {
					KM_ACTORS_COLOUMN_COUNT = rs.getInt("COLUMN_COUNT");
				}
				//Contains total number of column in KM_CIRCLE_MSTR
				if (("KM_CIRCLE_MSTR").equals(rs.getString("TABNAME"))) {
					KM_CIRCLE_MSTR_COLOUMN_COUNT = rs.getInt("COLUMN_COUNT");
				}

			}
			//logger.info(KM_MODULE_MSTR_COLOUMN_COUNT+" "+KM_USER_MSTR_COLOUMN_COUNT+" "+KM_ACTORS_COLOUMN_COUNT+" "+KM_CIRCLE_MSTR_COLOUMN_COUNT);
			int KM_MODULE_MSTR_COLUMN_ENDS =
				KM_COLOUMN_START + KM_MODULE_MSTR_COLOUMN_COUNT;
			int KM_USER_MSTR_COLUMN_ENDS =
				KM_MODULE_MSTR_COLUMN_ENDS + KM_USER_MSTR_COLOUMN_COUNT;
			int KM_ACTORS_COLUMN_END =
				KM_USER_MSTR_COLUMN_ENDS + KM_ACTORS_COLOUMN_COUNT;
			int KM_CIRCLE_MSTR_COLOUMN_END =
				KM_ACTORS_COLUMN_END + KM_CIRCLE_MSTR_COLOUMN_COUNT;

			columnMap = new HashMap();
			columnMap.put(
				"KM_MODULE_MSTR",
				new Integer(KM_MODULE_MSTR_COLUMN_ENDS));
			columnMap.put(
				"KM_USER_MSTR",
				new Integer(KM_USER_MSTR_COLUMN_ENDS));
			columnMap.put("KM_ACTOR", new Integer(KM_ACTORS_COLUMN_END));
			columnMap.put(
				"KM_CIRCLE_MSTR",
				new Integer(KM_CIRCLE_MSTR_COLOUMN_END));

		} catch (SQLException sqle) {
		
			logger.error(
				"SQLException occured while getting coloumn count.SQLException Message: "
					+ sqle.getMessage());
			throw new DAOException("SQLException: " + sqle.getMessage(), sqle);
		} catch (Exception e) {
			logger.error(
				"Exception occured while getting coloumn count.Exception Message: "
					+ e.getMessage());
			throw new DAOException("Exception: " + e.getMessage(), e);
		} finally {
			try {
				/*
				 * Close the resultset, statement, connection.
				 */
				DBConnection.releaseResources(con, stmt, rs);
			} catch (DAOException sqle) {
				logger.error(
					"Exception occured while getting Column Count."
						+ "Exception Message: "
						+ sqle.getMessage());
				throw new DAOException("Exception: " + sqle.getMessage(), sqle);
			} 

		}
		return columnMap;
	}


	//added by vishwas for UD intregation
	public List populateValuesforUD(String loginID) throws DAOException {
		

			logger.info("Populating user details");

			Connection con = null;
			PreparedStatement ps = null;
			ResultSet rst = null;
			ResultSetMetaData rsmd = null;

			try {
				StringBuffer query=new StringBuffer(SQL_SELECT_UD);
				con = DBConnection.getDBConnection();
				ps = con.prepareStatement(query.append(" with ur ").toString());
				ps.setString(1, loginID);
				rst = ps.executeQuery();
				rsmd = rst.getMetaData();
				logger.info("Exit from Populating user details from UD");
				return fetchUserDetails(rsmd, rst);

			} catch (SQLException e) {
				e.printStackTrace();

				logger.error(
					"SQL Exception occured while fetching user details in UD."
						+ "Exception Message: "
						+ e.getMessage());
				throw new DAOException("SQLException: " + e.getMessage(), e);
			} catch (Exception e) {
				e.printStackTrace();

				logger.error(
					"Exception occured while populating Values in UD."
						+ "Exception Message: "
						+ e.getMessage());
				throw new DAOException(" Exception: " + e.getMessage(), e);
			} finally {
				try {
					DBConnection.releaseResources(con, ps, rst);
				} catch (Exception e) {
					logger.error(
						"DAOException occured while populating Values."
							+ "Exception Message: "
							+ e.getMessage());
					throw new DAOException("DAO Exception: " + e.getMessage(), e);
				}
			}


		}
	
	//end by vishwas for UD intragation
}
