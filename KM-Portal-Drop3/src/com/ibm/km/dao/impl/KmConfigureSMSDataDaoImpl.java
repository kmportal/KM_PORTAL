package com.ibm.km.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import com.ibm.km.dao.DBConnection;
import com.ibm.km.dao.KmConfigureSMSDataDao;
import com.ibm.km.dto.KmConfigureDataForSMSDto;
import com.ibm.km.dto.SMSReportDTO;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;
import java.util.Date;

public class KmConfigureSMSDataDaoImpl implements KmConfigureSMSDataDao {
	Logger logger = Logger.getLogger(KmConfigureSMSDataDaoImpl.class);

	public ArrayList<KmConfigureDataForSMSDto> getColumnList(String tableName)
			throws KmException {

Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		KmConfigureDataForSMSDto dto = null;
		StringBuffer query = new StringBuffer(
				"select * from km_sms_configurable_columns where TABLENAME ='");
		query.append(tableName + "'");
		ArrayList<KmConfigureDataForSMSDto> columnNameList = new ArrayList<KmConfigureDataForSMSDto>();
		System.out.println("Query= " + query);
		try {
			con = DBConnection.getDBConnection();

			ps = con.prepareStatement(query.toString());
			rs = ps.executeQuery();

			while (rs.next()) {
				dto = new KmConfigureDataForSMSDto();
				dto.setColumnName(rs.getString("COLUMNNAME"));
				dto.setIsConfigurable(rs.getString("ISCONFIGURABLE"));
				dto.setColumnId(rs.getString("COLUMNID"));
				columnNameList.add(dto);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				DBConnection.releaseResources(con,ps,rs);
			} 
			catch (Exception e) 
			{
				logger.error("DAO Exception occured while getting column details" + "Exception Message: " + e.getMessage());
				throw new KmException("DAO Exception: " + e.getMessage(), e);
			}
			logger.info("Exit from getColumnList method in DAO");
		}

		return columnNameList;	}

	public Integer updateConfigurableColumnsForSMS(String idFlag,
			String tableName) throws KmException {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Integer status = 0;
		String[] idAndFlagArr;
		String[] flag;
		String columnId = "";
		String st = "";

		try {
			idAndFlagArr = idFlag.split("##");
			con = DBConnection.getDBConnection();
			for (int i = 0; i < idAndFlagArr.length; i++) {
				flag = idAndFlagArr[i].split("#");
				columnId = flag[0];
				st = flag[1];
				logger.info("Flag " + st.equals("N"));
				// if(st.equals("N")|| st.equals("n")){
				logger.info("St !!!!!!!!!" + st);
				ps = con
						.prepareStatement("UPDATE KM_SMS_CONFIGURABLE_COLUMNS SET ISCONFIGURABLE= '"
								+ st
								+ "' where COLUMNID= "
								+ columnId
								+ " and TABLENAME= '" + tableName + "'");
				status = ps.executeUpdate();
				/*
				 * }else if(flag[1].equals("Y")|| flag[1].equals("y")){
				 * logger.info("Yes !!!!!!!!!" +st); ps =con.prepareStatement(
				 * "UPDATE KM_SMS_CONFIGURABLE_COLUMNS SET ISCONFIGURABLE='Y' where COLUMNID=columnId and TABLENAME=tableName"
				 * ); status = ps.executeUpdate(); }
				 */
				logger.info("ID === " + columnId + "   :::: " + tableName
						+ " :::::::::: " + st);

			}
		}

		catch (SQLException e) {
			e.printStackTrace();
			logger.error("Error while uploading::", e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				DBConnection.releaseResources(con,ps,rs);
			} 
			catch (Exception e) 
			{
				logger.error("DAO Exception occured updating configurable column details" + "Exception Message: " + e.getMessage());
				throw new KmException("DAO Exception: " + e.getMessage(), e);
			}
			logger.info("Exit from update Column method in DAO");
		}
		return status;
	}

	public ArrayList<KmConfigureDataForSMSDto> getDocTypeList()
			throws KmException {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		KmConfigureDataForSMSDto dto = null;
		StringBuffer query = new StringBuffer(
				"select * from   KM2.KM_DOCUMENT_TYPE_DETAILS");
		ArrayList<KmConfigureDataForSMSDto> getDocTypeList = new ArrayList<KmConfigureDataForSMSDto>();
		System.out.println("Query= " + query);
		try {
			con = DBConnection.getDBConnection();

			ps = con.prepareStatement(query.toString());
			rs = ps.executeQuery();

			while (rs.next()) {
				dto = new KmConfigureDataForSMSDto();
				dto.setDocId(rs.getInt("DOCID"));
				dto.setDocTypeName(rs.getString("DOCTYPE"));
				dto.setStatus(rs.getString("STATUS"));
				getDocTypeList.add(dto);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		finally
		{
			try 
			{
				DBConnection.releaseResources(con,ps,rs);
			} 
			catch (Exception e) 
			{
				logger.error("DAO Exception occured while getting document Type  details" + "Exception Message: " + e.getMessage());
				throw new KmException("DAO Exception: " + e.getMessage(), e);
			}
			logger.info("Exit from get Document type method in DAO");
		}
	
		return getDocTypeList;
	}

	public ArrayList<SMSReportDTO> getSmsReport(String fromDate, String toDate) throws KmException {
		Connection con;
		ResultSet rs;
		PreparedStatement ps;
		SMSReportDTO dto = null;
		ArrayList<SMSReportDTO> smsReportList = new ArrayList<SMSReportDTO>();
		con = null;
		rs = null;
		ps = null;
		try {
			
			String Query = "select SMSSENDER, MOBILENO, SMSCONTENT, SMSCATEGORY, SMSCREATEDDATE,CIRCLE_NAME, PATNERNAME,LOCATION, SMSSTATUS, UDID,OLM_ID,count(mobileno) as Total " +
					"from KM_SMS_REPORT_DATA  srd,km_circle_mstr cm where date(SMSCREATEDDATE) between ? and ?  and srd.CIRCLEID=cm.circle_id group by SMSCREATEDDATE,SMSSENDER,MOBILENO, SMSCONTENT, SMSCATEGORY, CIRCLE_NAME, PATNERNAME,LOCATION, SMSSTATUS,UDID,OLM_ID" ;
			
			logger.info("Query to retrieve SMS Report :: "+Query);
			con = DBConnection.getDBConnection();
			ps = con.prepareStatement(Query);
			ps.setString(1,fromDate);
			ps.setString(2,toDate);
			rs = ps.executeQuery();
			while (rs.next()) {
				dto = new SMSReportDTO();
				dto.setSmsSender(rs.getString("SMSSENDER"));
				dto.setMobileNo(rs.getString("MOBILENO"));
				dto.setSmsContent(rs.getString("SMSCONTENT"));
				dto.setSmsCategory(rs.getString("SMSCATEGORY"));
				dto.setSmsCreatedDate(rs.getString("SMSCREATEDDATE"));
				dto.setCircleName(rs.getString("CIRCLE_NAME"));
				dto.setPatnerName(rs.getString("PATNERNAME"));
				dto.setLocation(rs.getString("LOCATION"));
				dto.setUdId(rs.getString("UDID"));
				dto.setOlmId(rs.getString("OLM_ID"));
				dto.setTotalSMSCount(rs.getInt("Total"));
				smsReportList.add(dto);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			logger.info(e);
			throw new KmException((new StringBuilder("SQLException: ")).append(
					e.getMessage()).toString(), e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e);
			throw new KmException((new StringBuilder("Exception: ")).append(
					e.getMessage()).toString(), e);
		}

		finally {
			try {
				DBConnection.releaseResources(con, ps, rs);
			} catch (DAOException e) {
				e.printStackTrace();
				throw new KmException(e.getMessage(), e);
			}

		}

		return smsReportList;
	}
}
