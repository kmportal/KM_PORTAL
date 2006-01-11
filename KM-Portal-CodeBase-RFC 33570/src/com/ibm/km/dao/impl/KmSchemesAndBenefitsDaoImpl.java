package com.ibm.km.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.openjpa.jdbc.kernel.exps.ToUpperCase;

import com.ibm.km.dao.DBConnection;
import com.ibm.km.dao.KmSchemesAndBenefitsDao;
import com.ibm.km.dto.KmSchemesAndBenefitsDto;
import com.ibm.km.exception.DAOException;


public class KmSchemesAndBenefitsDaoImpl implements KmSchemesAndBenefitsDao {
	
	/** logger - used to display message*/
	private static final Logger logger = Logger.getLogger(KmSchemesAndBenefitsDaoImpl.class);
	
/*----------------------Retrieve Data------------------------------*/
	public List getDetails(KmSchemesAndBenefitsDto dto) {
	
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ArrayList<KmSchemesAndBenefitsDto> recordList = new ArrayList<KmSchemesAndBenefitsDto>();
		
		try {
			String sqlQuery = "SELECT SRNO, SCHEME_TYPE, DESCRIPTION, DISPLAY_FLAG, INSERTED_BY, INSERTED_DATE " +
					"FROM KM_SCHEMESANDBENEFITS ORDER BY SRNO WITH UR";
			
			conn = DBConnection.getDBConnection();
			preparedStatement = conn.prepareStatement(sqlQuery);
			resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				KmSchemesAndBenefitsDto iPortalDTOvar = new KmSchemesAndBenefitsDto();
				iPortalDTOvar.setId(resultSet.getString(1));
				iPortalDTOvar.setType(resultSet.getString(2));
				iPortalDTOvar.setDescription(resultSet.getString(3));
				iPortalDTOvar.setDisplayFlag(resultSet.getString(4));
				iPortalDTOvar.setInsertedBy(resultSet.getString(5));
				iPortalDTOvar.setInsertedDate(resultSet.getString(6));
					
				recordList.add(iPortalDTOvar);
			}
			logger.info("Records selected from Schemes and Benefits");

		} catch (SQLException sql) {
			sql.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				DBConnection.releaseResources(conn, preparedStatement, resultSet);
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return recordList;
	}
	
/*----------------------Check if Data Present------------------------------*/
	public boolean isPresent() {
	
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		boolean result = false;
		
		try {
			String sqlQuery = "SELECT SCHEME_TYPE FROM KM_SCHEMESANDBENEFITS WITH UR";
			
			conn = DBConnection.getDBConnection();
			preparedStatement = conn.prepareStatement(sqlQuery);
			resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				result = true;
			}
			
		} catch (SQLException sql) {
			sql.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				DBConnection.releaseResources(conn, preparedStatement, resultSet);
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

/*----------------------For deleting all data--------------------*/
	public boolean deleteDetails(){
		
		Connection conn = null;
		boolean result = false;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			String strsql = "DELETE FROM KM_SCHEMESANDBENEFITS";
			
			conn = DBConnection.getDBConnection();
			if (conn != null) {
				preparedStatement = conn.prepareStatement(strsql);
				int count = preparedStatement.executeUpdate();
				if (count > 0) {
					result = true;
					logger.info("Number of Records deleted from Schemes and Benefits "+ count);
				} else {
					logger.info("Error ! No Record deleted");
				}
			}
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				DBConnection.releaseResources(conn, preparedStatement, resultSet);
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	
/*----------------------For inserting details from excel--------------------*/
	public boolean insertDetails(KmSchemesAndBenefitsDto dto){
		
		Connection conn = null;
		boolean result = false;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			/*String strsql = "INSERT INTO KM_SCHEMESANDBENEFITS(SRNO, SCHEME_TYPE, DESCRIPTION, DISPLAY_FLAG, INSERTED_BY, INSERTED_DATE) " +
					"VALUES(?, ?, ?, ?, ?, SYSDATE)";*/
			String strsql = "INSERT INTO KM_SCHEMESANDBENEFITS(SRNO, SCHEME_TYPE, DESCRIPTION, DISPLAY_FLAG, INSERTED_BY, INSERTED_DATE) " +
			"VALUES(?, ?, ?, ?, ?, SYSDATE)";
			
			conn = DBConnection.getDBConnection();
			if (conn != null) {
				
				preparedStatement = conn.prepareStatement(strsql);
		
				preparedStatement.setInt(1, dto.getSrNo());
				preparedStatement.setString(2, dto.getType().trim());
				preparedStatement.setString(3, dto.getDescription().trim());
				preparedStatement.setString(4, dto.getInsertedBy().trim());
				preparedStatement.setString(5, "Yes");
				
				/*preparedStatement.setString(4, dto.getDisplayFlag().trim());
				preparedStatement.setString(5, dto.getInsertedBy().trim());*/
				
				int count = preparedStatement.executeUpdate();
				
				if (count > 0) {
					result = true;
					logger.info("Record inserted in Schemes and Benefits");
				} else {
					logger.info("Error ! No Record inserted");
				}
			}
			conn.close();
			} 
		catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				DBConnection.releaseResources(conn, preparedStatement, resultSet);
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
}
