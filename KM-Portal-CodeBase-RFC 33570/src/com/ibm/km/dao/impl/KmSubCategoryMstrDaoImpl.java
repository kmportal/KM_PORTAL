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

import com.ibm.km.dao.*;
import com.ibm.km.dto.*;
import com.ibm.km.exception.*;

import java.util.*;
import java.sql.*;
import org.apache.log4j.Logger;
public class KmSubCategoryMstrDaoImpl implements KmSubCategoryMstrDao {
	/*
	* Logger for the class.
	*/
	private static final Logger logger;

	static {

		logger = Logger.getLogger(KmCategoryMstrDaoImpl.class);
	}

	protected static final String SQL_INSERT_WITH_ID ="INSERT INTO KM_SUB_CATEGORY_MSTR (SUB_CATEGORY_ID, SUB_CATEGORY_NAME, SUB_CATEGORY_DESC, CATEGORY_ID, STATUS, CIRCLE_ID, UPDATED_BY, CREATED_BY, CREATED_DT, UPDATE_DT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, current timestamp, current timestamp)";

	protected static final String SQL_SELECT_SUB_CATEGORY ="SELECT KM_SUB_CATEGORY_MSTR.SUB_CATEGORY_ID, KM_SUB_CATEGORY_MSTR.SUB_CATEGORY_NAME  FROM KM_SUB_CATEGORY_MSTR WHERE STATUS ='A' AND CATEGORY_ID = ? ORDER BY SUB_CATEGORY_NAME";

	protected static final String SQL_SELECT ="SELECT KM_SUB_CATEGORY_MSTR.SUB_CATEGORY_ID, KM_SUB_CATEGORY_MSTR.SUB_CATEGORY_NAME, KM_SUB_CATEGORY_MSTR.SUB_CATEGORY_DESC, KM_SUB_CATEGORY_MSTR.CATEGORY_ID, KM_SUB_CATEGORY_MSTR.STATUS, KM_SUB_CATEGORY_MSTR.CIRCLE_ID, KM_SUB_CATEGORY_MSTR.UPDATED_BY, KM_SUB_CATEGORY_MSTR.CREATED_BY, KM_SUB_CATEGORY_MSTR.CREATED_DT, KM_SUB_CATEGORY_MSTR.UPDATE_DT FROM KM_SUB_CATEGORY_MSTR";

	protected static final String SQL_UPDATE ="UPDATE KM_SUB_CATEGORY_MSTR SET SUB_CATEGORY_ID = ?, SUB_CATEGORY_NAME = ?, SUB_CATEGORY_DESC = ?, CATEGORY_ID = ?, STATUS = ?, CIRCLE_ID = ?, UPDATED_BY = ?, CREATED_BY = ?, CREATED_DT = ?, UPDATE_DT = ? WHERE SUB_CATEGORY_ID = ?";

	protected static final String SQL_DELETE ="DELETE FROM KM_SUB_CATEGORY_MSTR WHERE SUB_CATEGORY_ID = ?";

	protected static final String SQL_GET_SUB_CATEGORY_ID ="SELECT NEXTVAL FOR SAMEER.KM_SUB_CATEGORY_ID FROM SYSIBM.SYSDUMMY1";

	//Insert a new sub category into the table KM_SUB_CATEGORY_MSTR 

	public int insert(KmSubCategoryMstr dto) throws KmException {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		int rowsUpdated = 0;
		logger.info("Entered insert for table:KM_SUB_CATEGORY_MSTR");
		try {
			StringBuffer query1=new StringBuffer(SQL_INSERT_WITH_ID);
			StringBuffer query2=new StringBuffer(SQL_GET_SUB_CATEGORY_ID);
			
			//String sql = SQL_INSERT_WITH_ID;
			//String sql1 = SQL_GET_SUB_CATEGORY_ID;
			con = DBConnection.getDBConnection();

			ps = con.prepareStatement(query2.toString());
			rs = ps.executeQuery();
			rs.next();
			int subCategoryId = Integer.parseInt(rs.getString(1));
			ps = con.prepareStatement(query1.toString());

			//	Preparing statement

			ps.setInt(1, subCategoryId);
			ps.setString(2, dto.getSubCategoryName().trim());
			ps.setString(3, dto.getSubCategoryDesc());
			ps.setInt(4, dto.getCategoryId());
			ps.setString(5, dto.getStatus());
			ps.setInt(6, dto.getCircleId());
			ps.setInt(7, dto.getUpdatedBy());
			ps.setInt(8, dto.getCreatedBy());

			//	Executing querry	

			rowsUpdated = ps.executeUpdate();
			logger.info("Exit from insert for table:KM_SUB_CATEGORY_MSTR");
			return subCategoryId;

		} catch (SQLException e) {

			logger.error(
				"SQL Exception occured while insetion."
					+ "Exception Message: "
					+ e.getMessage());
			throw new KmException("SQL Exception: " + e.getMessage(), e);

		} catch (Exception e) {

			logger.error(
				" Exception occured while insetion."
					+ "Exception Message: "
					+ e.getMessage());
			throw new KmException(" Exception: " + e.getMessage(), e);
		} finally {
			try {
				DBConnection.releaseResources(con, ps, rs);
			} catch (Exception e) {
				logger.error(
					"DAO Exception occured while insetion."
						+ "Exception Message: "
						+ e.getMessage());
				throw new KmException("DAO Exception: " + e.getMessage(), e);
			}
		}
	}
	/* (non-Javadoc)
	 * @see com.ibm.km.dao.KmSubCategoryMstrDao#getSubCategories(java.lang.String)
	 * @To get all sub categories under the given category
	 **/
	//To get all sub-categories under the selected category from the table KM_SUB_CATEGORY_MSTR
	public ArrayList getSubCategories(String categoryId) throws KmException {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		KmSubCategoryMstr dto;
		logger.info("Entered getSubCategories for table:KM_SUB_CATEGORY_MSTR");
		try {
			ArrayList subCategoryList = new ArrayList();
			StringBuffer query=new StringBuffer(SQL_SELECT_SUB_CATEGORY);
			//String sql = SQL_SELECT_SUB_CATEGORY;
			con = DBConnection.getDBConnection();
			ps = con.prepareStatement(query.toString());
			ps.setInt(1, Integer.parseInt(categoryId));
			rs = ps.executeQuery();
			while (rs.next()) {

				dto = new KmSubCategoryMstr();
				dto.setSubCategoryName(rs.getString("SUB_CATEGORY_NAME"));
				dto.setSubCategoryId(rs.getInt("SUB_CATEGORY_ID"));
				subCategoryList.add(dto);
			}

			logger.info(
				"Exit from getSubCategories for table:KM_SUB_CATEGORY_MSTR");
			return subCategoryList;

		} catch (SQLException e) {
			logger.error(
				"SQL Exception occured while getting SubCategories."
					+ "Exception Message: "
					+ e.getMessage());
			throw new KmException("SQL Exception: " + e.getMessage(), e);
		} catch (Exception e) {
			logger.error(
				" Exception occured while getting SubCategories."
					+ "Exception Message: "
					+ e.getMessage());
			throw new KmException(" Exception: " + e.getMessage(), e);
		} finally {
			try {
				DBConnection.releaseResources(con, ps, rs);
			} catch (Exception e) {
				logger.error(
					"DAO Exception occured while getting SubCategories."
						+ "Exception Message: "
						+ e.getMessage());
				throw new KmException("DAO Exception: " + e.getMessage(), e);
			}
		}
	}
	/* (non-Javadoc)
	 * @see com.ibm.km.dao.KmSubCategoryMstrDao#checkOnSubCategoryName(java.lang.String, java.lang.String)
	 */
	public boolean checkOnSubCategoryName(
		String subCategoryName,
		String categoryId)
		throws KmException {
		logger.info("Entered checkSubCategory for table:KM_SUB_CATEGORY_MSTR");

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isCategoryExists = false;
		try {
			StringBuffer query=new StringBuffer(SQL_SELECT);
			//String sql =
			query.append(" WHERE UPPER( KM_SUB_CATEGORY_MSTR.SUB_CATEGORY_NAME) = ? AND KM_SUB_CATEGORY_MSTR.CATEGORY_ID = ? ").toString();
			con = DBConnection.getDBConnection();
			ps = con.prepareStatement(query.toString());
			ps.setString(1, subCategoryName.toUpperCase());
			ps.setInt(2, Integer.parseInt(categoryId));
			rs = ps.executeQuery();
			/*Checking whether the sub-category name already exists in the table */
			while (rs.next()) {
				isCategoryExists = true;
			}
			logger.info(
				"Exit from checkSubCategory for table:KM_SUB_CATEGORY_MSTR");
			return isCategoryExists;
		} catch (SQLException e) {
			logger.error(
				"SQL Exception occured while checking SubCategory."
					+ "Exception Message: "
					+ e.getMessage());
			throw new KmException("SQLException: " + e.getMessage(), e);
		} catch (Exception e) {
			logger.error(
				"Exception occured while cchecking SubCategory."
					+ "Exception Message: "
					+ e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try {
				DBConnection.releaseResources(con, ps, rs);
			} catch (Exception e) {
				logger.error(
					"DAO Exception occured while checking SubCategory."
						+ "Exception Message: "
						+ e.getMessage());
				throw new KmException("DAO Exception: " + e.getMessage(), e);
			}
		}
	}
}