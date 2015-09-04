package com.ibm.km.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ibm.km.dao.DBConnection;
import com.ibm.km.dao.NetworkFaultReportDao;
import com.ibm.km.dto.NetworkFaultReportDto;
import com.ibm.km.exception.DAOException;

/**
 * @author Ajil Chandran Created On 02/12/2010
 */

public class NetworkFaultReportDaoImpl implements NetworkFaultReportDao {
	/*
	 * Logger for the class.
	 */
	private static Logger logger = null;
	static {

		logger = Logger.getLogger(NetworkFaultReportDaoImpl.class.getName());
	}

	protected static final String SQL_GET_FAULT_REPORT = "SELECT nfl.ID ,nfl.CIRCLE_ID,em.ELEMENT_NAME ,nfl.FAULT_DESC,nfl.AFFECTED_AREA,date( nfl.REPORTED_DATE) as R_DATE,time( nfl.REPORTED_DATE)R_TIME, nfl.TAT_HOURS, nfl.TAT_MINUTES "
			+ " FROM KM_NETWORK_FAULT_LOG nfl ,  KM_ELEMENT_MSTR em WHERE nfl.CIRCLE_ID= em.ELEMENT_ID";

	protected static final String SQL_GET_IMPACT_REPORT = "SELECT em.ELEMENT_NAME ,sum( nfl.TAT_HOURS) as HH, sum(nfl.TAT_MINUTES)as MM "
			+ " FROM KM_NETWORK_FAULT_LOG nfl ,  KM_ELEMENT_MSTR em WHERE nfl.CIRCLE_ID= em.ELEMENT_ID ";

	protected static final String MTD_REPORT = " AND month(REPORTED_DATE)=month(current date) AND year(REPORTED_DATE)=year(current date) ";

	public List fetchNetworkFaultReport(String[] circles,
			NetworkFaultReportDto data) throws DAOException {
		logger
				.info("Entered fetchNetworkFaultReport for table KM_ELEMENT_MSTR");

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = null;
		NetworkFaultReportDto dto = null;
		List faultRecord = new ArrayList();
		// int count=1;
		try {
			con = DBConnection.getDBConnection();
			sql = SQL_GET_FAULT_REPORT;
			/**
			 * report type for MTD 0, Daily 1
			 */
			if (data.getReportType().equals("1")) {

				sql = sql + " AND date( nfl.REPORTED_DATE) = '"
						+ data.getDate() + "' ";

			} else if (data.getReportType().equals("0")) {

				sql = sql + MTD_REPORT;
			}
			/**
			 * Element Level 1= all cirlces , 2 and 3= selected circles/ cirlces
			 * inside a lob
			 */
			if (data.getElementLevel().equals("1")) {
				ps = con.prepareStatement(sql + "order by  em.ELEMENT_NAME, nfl.REPORTED_DATE WITH UR");
			} else if (data.getElementLevel().equals("3")
					|| data.getElementLevel().equals("2")) {

				StringBuilder circle = new StringBuilder("( ");
				for (String csd : circles) {

					circle.append(csd).append(",");

				}
				sql = sql + " AND nfl.CIRCLE_ID in "
						+ circle.substring(0, circle.lastIndexOf(",")) + ")";
				;
				ps = con.prepareStatement(sql + "order by em.ELEMENT_NAME,  nfl.REPORTED_DATE WITH UR");
			}

			rs = ps.executeQuery();
			logger.info("Fetching  successful on table:KM_ELEMENT_MSTR");
			while (rs.next()) {
				dto = new NetworkFaultReportDto();
				dto.setFaultId(rs.getInt("ID"));
				dto.setCircleName(rs.getString("ELEMENT_NAME"));
				dto.setFaultDescription(rs.getString("FAULT_DESC"));
				dto.setAffectedArea(rs.getString("AFFECTED_AREA"));
				dto.setReportedDate(rs.getDate("R_DATE").toString());
				dto.setReportedTime(rs.getTime("R_TIME").toString());
				dto.setTatHours(rs.getInt("TAT_HOURS"));
				dto.setTatMinutes(rs.getInt("TAT_MINUTES"));
				dto.setCircleId(Integer.parseInt(rs.getString("CIRCLE_ID")
						.trim()));
				faultRecord.add(dto);
			}

		} catch (SQLException e) {

			logger.error("SQL Exception occured while fetching."
					+ "Exception Message: " + e.getMessage());
			throw new DAOException("SQLException: " + e.getMessage(), e);
		} catch (DAOException e) {
			// e.printStackTrace();
			logger.error("Exception occured while fetching."
					+ "Exception Message: " + e.getMessage());
			throw new DAOException("Exception: " + e.getMessage(), e);
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
				logger.error("DAO Exception occured while fetching."
						+ "Exception Message: " + e.getMessage());
				throw new DAOException("DAO Exception: " + e.getMessage(), e);
			}
		}
		return faultRecord;
	}

	public List fetchNetworkImpactReport(String[] circles,
			NetworkFaultReportDto data) throws DAOException {
		logger
				.info("Entered fetchNetworkImpactReport for table KM_ELEMENT_MSTR");
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = null;
		NetworkFaultReportDto dto = null;
		List impactRecord = new ArrayList();
		// int count=1;
		try {
			con = DBConnection.getDBConnection();
			sql = SQL_GET_IMPACT_REPORT;
			if (data.getReportType().equals("1")) {

				sql = sql + " AND date( nfl.REPORTED_DATE) = '"
						+ data.getDate() + "' ";

			} else if (data.getReportType().equals("0")) {
				sql = sql + MTD_REPORT;
			}
			if (data.getElementLevel().equals("1")) {
				ps = con.prepareStatement(sql);
			} else if (data.getElementLevel().equals("3")
					|| data.getElementLevel().equals("2")) {

				StringBuilder circle = new StringBuilder("( ");
				for (String csd : circles) {
					circle.append(csd).append(",");
				}
				sql = sql + " AND nfl.CIRCLE_ID in "
						+ circle.substring(0, circle.lastIndexOf(",")) + ") ";
				;
				ps = con.prepareStatement(sql);
			}
			ps = con.prepareStatement(sql + " GROUP BY em.ELEMENT_NAME  order by em.ELEMENT_NAME WITH UR");

			rs = ps.executeQuery();
			logger.info("Fetching  successful on table:KM_ELEMENT_MSTR");
			while (rs.next()) {
				dto = new NetworkFaultReportDto();

				dto.setCircleName(rs.getString("ELEMENT_NAME"));
				int minutes = rs.getInt("MM");
				int hours = rs.getInt("HH");
				if (minutes >= 60) {
					hours = hours + minutes / 60;
					minutes = minutes % 60;
				}
				dto.setTatHours(hours);
				dto.setTatMinutes(minutes);

				impactRecord.add(dto);
			}

		} catch (SQLException e) {
			logger.error("SQL Exception occured while fetching."
					+ "Exception Message: " + e.getMessage());
			throw new DAOException("SQLException: " + e.getMessage(), e);
		} catch (DAOException e) {
			logger.error("Exception occured while fetching."
					+ "Exception Message: " + e.getMessage());
			throw new DAOException("Exception: " + e.getMessage(), e);
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
				logger.error("DAO Exception occured while fetching."
						+ "Exception Message: " + e.getMessage());
				throw new DAOException("DAO Exception: " + e.getMessage(), e);
			}
		}
		return impactRecord;
	}

}
