package com.ibm.km.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ibm.km.dao.DBConnection;
import com.ibm.km.dao.KmRcContentUploadDao;
import com.ibm.km.dto.KmAlertMstr;
import com.ibm.km.dto.KmRcCategoryDTO;
import com.ibm.km.dto.KmRcDataDTO;
import com.ibm.km.dto.KmRcHeaderDTO;
import com.ibm.km.dto.KmRcTypeDTO;
import com.ibm.km.exception.KmException;

public class KmRcContentUploadDaoImpl implements KmRcContentUploadDao  {

	private static final Logger logger;

	static {
		  logger = Logger.getLogger(KmRcContentUploadDaoImpl.class);
		 }

	protected static final String GET_COMBO ="SELECT * FROM KM_RC_TYPE_MSTR WHERE ACTIVE='A'";
	protected static final String GET_HEADERS = "SELECT * FROM KM_RC_MSTR WHERE ACTIVE='A'";
	protected static final String INSERT_RC_DATA = "INSERT INTO KM_RC_DATA(TOPIC, RC_VALUE, RC_TYPE, RC_HEADER_ID, VALUE, PATH, START_DT, END_DT, DOCUMENT_ID, CIRCLE_ID, ISCONFIGURABLE, RC_CATEGORY_ID) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
	protected static final String GET_RC_DATA = "SELECT RD.*,RM.RC_HEADER AS HEADER FROM KM_RC_DATA RD ,  KM_RC_MSTR RM WHERE DOCUMENT_ID = ? AND RD.RC_HEADER_ID = RM.RC_ID";
	protected static final String GET_RC_TYPE_NAME = "SELECT * FROM KM_RC_TYPE_MSTR WHERE RC_TYPE_ID = ?";
	protected static final String DELETE_RC_DATA = "DELETE FROM KM_RC_DATA WHERE DOCUMENT_ID = ? ";
//	protected static final String GET_DOC_IDS = "SELECT RD.DOCUMENT_ID FROM KM_RC_DATA RD ,  KM_DOCUMENT_MSTR DM WHERE RD.RC_VALUE = ? AND RD.RC_TYPE IN ( ";
	protected static final String GET_DOC_IDS = "SELECT RD.DOCUMENT_ID FROM KM_RC_DATA RD  WHERE RD.RC_VALUE = ? AND RD.RC_TYPE IN (";
	protected static final String GET_RC_DATA_CSR = "SELECT RD.TOPIC,RM.RC_HEADER AS HEADER,RD.VALUE,RD.START_DT,RD.END_DT FROM KM_RC_DATA RD ,  KM_RC_MSTR RM WHERE DOCUMENT_ID = ? AND RC_CATEGORY_ID = ? AND RD.RC_HEADER_ID = RM.RC_ID";
	protected static final String GET_RC_HEADER_ID = "SELECT RC_ID FROM KM_RC_MSTR WHERE RC_HEADER = ?";
	protected static final String GET_RC_CATEGORY = "SELECT * from KM_RC_CATEGORY_TYPE";

	public ArrayList<KmRcTypeDTO> getCombo() throws KmException {
		  KmRcTypeDTO dto;
		  Connection con = null;
		  PreparedStatement ps = null;
		  ResultSet rs = null;

		  ArrayList<KmRcTypeDTO> comboList = new ArrayList<KmRcTypeDTO>();

		  try {
		   StringBuffer query =new StringBuffer(GET_COMBO);
		   con = DBConnection.getDBConnection();
		   ps = con.prepareStatement(query.append(" with ur").toString());

		   rs = ps.executeQuery();

		   while (rs.next()) {
		    dto = new KmRcTypeDTO();
		    dto.setRCTypeId(rs.getInt("RC_TYPE_ID"));
		    dto.setRCTypeValue(rs.getString("RC_TYPE_NAME"));
		    comboList.add(dto);
		   }
		    dto = new KmRcTypeDTO();
		    dto.setRCTypeId(-1);
		    dto.setRCTypeValue("No Combos");
		    comboList.add(dto);

		  } catch (SQLException e) {
		   logger.info(e);

		   logger.error(
		    "SQL Exception occured while getting combo items."
		     + "Exception Message: "
		     + e.getMessage());
		   throw new KmException("SQLException: " + e.getMessage(), e);
		  } catch (Exception e) {
		   logger.info(e);

		   logger.error(
		    "Exception occured while getting combo items."
		     + "Exception Message: "
		     + e.getMessage());
		   throw new KmException("Exception: " + e.getMessage(), e);
		  } finally {
		   try {

		    if (con != null) {
		     con.setAutoCommit(true);
		     con.close();
		    }
		    DBConnection.releaseResources(null, ps, rs);
		   } catch (Exception e) {

		   }
		  }
		  return comboList;
		 }//getCombo


	public ArrayList<KmRcHeaderDTO> getHeaders() throws KmException {

		KmRcHeaderDTO dto;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<KmRcHeaderDTO> headerList = new ArrayList<KmRcHeaderDTO>();

		  try {
		   StringBuffer query =new StringBuffer(GET_HEADERS);
		   con = DBConnection.getDBConnection();
		   ps = con.prepareStatement(query.append(" with ur").toString());

		   rs = ps.executeQuery();

		   while (rs.next()) {
			   dto = new KmRcHeaderDTO();
			   dto.setRCId(rs.getInt("RC_ID"));
			   dto.setRCHeader(rs.getString("RC_HEADER"));
			   headerList.add(dto);
		   }

		  } catch (SQLException e) {
		   logger.info(e);

		   logger.error(
		    "SQL Exception occured while getting Headers."
		     + "Exception Message: "
		     + e.getMessage());
		   throw new KmException("SQLException: " + e.getMessage(), e);
		  } catch (Exception e) {
		   logger.info(e);

		   logger.error(
		    "Exception occured while getting Headers."
		     + "Exception Message: "
		     + e.getMessage());
		   throw new KmException("Exception: " + e.getMessage(), e);
		  } finally {
		   try {

		    if (con != null) {
		     con.setAutoCommit(true);
		     con.close();
		    }
		    DBConnection.releaseResources(null, ps, rs);
		   } catch (Exception e) {

		   }
		  }
		  return headerList;
	}


	public int insertRcData(KmRcDataDTO dto) throws KmException {
		  logger.info("Entered Insert for Table KM_RC_DATA");
		  Connection con = null;
		  PreparedStatement ps = null;
		  ResultSet rs = null;
		  int rowsUpdated = 0;
		  try {
		   con = DBConnection.getDBConnection();

		   ps = con.prepareStatement(INSERT_RC_DATA);

		 ArrayList<String> headerList = dto.getHeaderList();
		 ArrayList<String> valueList  = dto.getValueList();
		 ArrayList<String> configList  = dto.getConfigList();

		 for(int i=0 ;i<dto.getTotalHeaders();i++)
		 {
		   ps.setString(1, dto.getRcTopic().trim());
		   ps.setString(2, dto.getRcValue().trim());
		   ps.setString(3, dto.getRcType());
		   ps.setInt(4,getRcHeaderId(headerList.get(i)));
		   ps.setString(5, valueList.get(i).trim());
		   ps.setString(6, dto.getContentPath());
		   ps.setDate(7,(Date) dto.getStartDt());
		   ps.setDate(8,(Date) dto.getEndDt());
		   ps.setInt(9,dto.getDocumentId());
		   ps.setInt(10,dto.getCircleId());
		   ps.setString(11, configList.get(i).trim());
		   ps.setString(12, dto.getRcCategoryId().trim());
		   //logger.info("Config for "+i+"   --   "+configList.get(i));
		   //ps.execute();
		   ps.addBatch();
		 }

		  ps.executeBatch();

		  } catch (SQLException e) {
			  e.printStackTrace();
		   logger.info(e);
		   e.printStackTrace();
		   logger.error(
		    "SQL Exception occured while inserting."
		     + "Exception Message: "
		     + e.getMessage());
		  logger.info("Batch Exception :: "+e.getNextException());
		   throw new KmException("SQLException: " + e.getMessage(), e);
		  } catch (Exception e) {
			  e.printStackTrace();
		   logger.info(e);
		   logger.error(
		    "Exception occured while inserting."
		     + "Exception Message: "
		     + e.getMessage());
		   throw new KmException("Exception: " + e.getMessage(), e);
		  } finally {
		   try {

		    if (con != null) {
		     con.setAutoCommit(true);
		     con.close();
		    }
		    DBConnection.releaseResources(null, ps, rs);
		   } catch (Exception e) {

		   }
		  }
		  return rowsUpdated;
		 }//insertRcData


	public KmRcDataDTO getRcData(String documentId) throws KmException {

		  KmRcDataDTO dto = new KmRcDataDTO();
		  ArrayList<String> headerList = new ArrayList<String>();
		  ArrayList<String> valueList  = new ArrayList<String>();
		  ArrayList<String> configList  = new ArrayList<String>();
		  Connection con = null;
		  PreparedStatement ps = null;
		  ResultSet rs = null;

		  try {
		   StringBuffer query =new StringBuffer(GET_RC_DATA);
		   con = DBConnection.getDBConnection();
		   ps = con.prepareStatement(query.append(" with ur").toString());
		   ps.setInt(1,Integer.parseInt(documentId));

		   //System.out.println("documentId "+documentId);

		   rs = ps.executeQuery();
		   ResultSetMetaData rsmd=rs.getMetaData();
		   logger.info("Column--"+rsmd.getColumnName(11));
		   
		   while (rs.next()) {
			   headerList.add(rs.getString("HEADER"));
			   valueList.add(rs.getString("VALUE"));
			   dto.setRcTopic(rs.getString("TOPIC"));
			   dto.setRcValue(rs.getString("RC_VALUE"));
			   dto.setStartDt(rs.getDate("START_DT"));
			   dto.setEndDt(rs.getDate("END_DT"));
			   dto.setContentPath(rs.getString("PATH"));
			   dto.setRcType(rs.getString("RC_TYPE"));
			   dto.setRcTypeId(rs.getString("RC_TYPE"));
			   dto.setCircleId(rs.getInt("CIRCLE_ID"));
			   dto.setRcCategoryId(rs.getString("RC_CATEGORY_ID"));
			   configList.add(rs.getString("ISCONFIGURABLE"));
		   }

		   dto.setHeaderList(headerList);
		   dto.setValueList(valueList);
		   dto.setConfigList(configList);

		   if(null != dto.getRcType()){
			   if(!"".equals(dto.getRcType())){
		   logger.info("The RC type is      -------------------       "+dto.getRcType());
		    dto.setRcType(getRcType(Integer.parseInt(dto.getRcType())));
		   }
		   }		   
		  } catch (SQLException e) {
		   logger.info(e);
		   e.printStackTrace();
		   logger.error(
		    "SQL Exception occured while getting RC Data."
		     + "Exception Message: "
		     + e.getMessage());
		   throw new KmException("SQLException: " + e.getMessage(), e);
		  } catch (Exception e) {
		   logger.info(e);
		   e.printStackTrace();
		   logger.error(
		    "Exception occured while getting RC Data."
		     + "Exception Message: "
		     + e.getMessage());
		   throw new KmException("Exception: " + e.getMessage(), e);
		  } finally {
		   try {

		    if (con != null) {
		     con.setAutoCommit(true);
		     con.close();
		    }
		    DBConnection.releaseResources(null, ps, rs);
		   } catch (Exception e) {

		   }
		  }
		  return dto;
	}//getRcData

	public int getRcHeaderId(String rcHeader) throws KmException {
		  Connection con = null;
		  PreparedStatement ps = null;
		  ResultSet rs = null;

		  int rcHeaderId = 0;

		  try {
		   StringBuffer query =new StringBuffer(GET_RC_HEADER_ID);
		   con = DBConnection.getDBConnection();
		   ps = con.prepareStatement(query.append(" with ur").toString());

		   ps.setString(1,rcHeader.trim());
		   rs = ps.executeQuery();

		   while (rs.next()) {
			   rcHeaderId = rs.getInt("RC_ID");
		   }

		  } catch (SQLException e) {
			 e.printStackTrace();
		   logger.info(e);

		   logger.error(
		    "SQL Exception occured while getting RC Type."
		     + "Exception Message: "
		     + e.getMessage());
		   throw new KmException("SQLException: " + e.getMessage(), e);
		  } catch (Exception e) {
			e.printStackTrace();
		   logger.info(e);

		   logger.error(
		    "Exception occured while getting RC Type."
		     + "Exception Message: "
		     + e.getMessage());
		   throw new KmException("Exception: " + e.getMessage(), e);
		  } finally {
		   try {

		    if (con != null) {
		     con.setAutoCommit(true);
		     con.close();
		    }
		    DBConnection.releaseResources(null, ps, rs);
		   } catch (Exception e) {

		   }
		  }
		  return rcHeaderId;
		 }//getRcType

	public String getRcType(int rcTypeId) throws KmException {
		  Connection con = null;
		  PreparedStatement ps = null;
		  ResultSet rs = null;

		  String rcTypeName="";

		  try {
		   StringBuffer query =new StringBuffer(GET_RC_TYPE_NAME);
		   con = DBConnection.getDBConnection();
		   ps = con.prepareStatement(query.append(" with ur").toString());

		   ps.setInt(1,rcTypeId);
		   rs = ps.executeQuery();

		   while (rs.next()) {
			   rcTypeName = rs.getString("RC_TYPE_NAME");
		   }

		  } catch (SQLException e) {
			 e.printStackTrace();
		   logger.info(e);

		   logger.error(
		    "SQL Exception occured while getting RC Type."
		     + "Exception Message: "
		     + e.getMessage());
		   throw new KmException("SQLException: " + e.getMessage(), e);
		  } catch (Exception e) {
			e.printStackTrace();
		   logger.info(e);

		   logger.error(
		    "Exception occured while getting RC Type."
		     + "Exception Message: "
		     + e.getMessage());
		   throw new KmException("Exception: " + e.getMessage(), e);
		  } finally {
		   try {

		    if (con != null) {
		     con.setAutoCommit(true);
		     con.close();
		    }
		    DBConnection.releaseResources(null, ps, rs);
		   } catch (Exception e) {

		   }
		  }
		  return rcTypeName;
		 }//getRcType

	public String deleteRcData(String documentId) throws KmException {
		logger.info("Entered delete RC Data for table KM_RC_DATA and documentId:" + documentId);
		  Connection con = null;
		  PreparedStatement ps = null;
		  ResultSet rs = null;

		  String message = "failure";
		  try {
		   con = DBConnection.getDBConnection();
		   StringBuffer query =new StringBuffer(DELETE_RC_DATA);
		   ps = con.prepareStatement(query.append(" with ur").toString());
		   ps.setInt(1, Integer.parseInt(documentId));
		   ps.executeUpdate();
		   message = "success";
		   logger.info("RC Data Deleted");

		  } catch (SQLException e) {
			  e.printStackTrace();
		   message = "failure";
		   logger.info(e);
		   logger.error(
		    "SQL Exception occured while RC Data Delete."
		     + "Exception Message: "
		     + e.getMessage());
		   throw new KmException("SQLException: " + e.getMessage(), e);
		  } catch (Exception e) {
		   message = "failure";
		   logger.info(e);
		   logger.error(
		    "Exception occured while getting RC Data Delete."
		     + "Exception Message: "
		     + e.getMessage());
		   throw new KmException("Exception: " + e.getMessage(), e);
		  } finally {
		   try {

		    if (con != null) {
		     con.setAutoCommit(true);
		     con.close();
		    }
		    DBConnection.releaseResources(null, ps, rs);
		   } catch (Exception e) {

		   }
		  }
		  return message;


	}//deleteRcData

	public ArrayList<Integer> getDocIds(String rcValue,String rcType, ArrayList<String> docIdList) throws KmException {
		  Connection con = null;
		  PreparedStatement ps = null;
		  ResultSet rs = null;

		  ArrayList<Integer> docList = new ArrayList<Integer>();
			
			String docIds = "";
			if(docIdList != null && docIdList.size() != 0)
			{
			for(int i=0 ; i<docIdList.size() ;i++)
			{
				docIds = docIds + docIdList.get(i) + ",";
			}	
			docIds = docIds.substring(0,(docIds.length() -1));
			}
//			System.out.println("docIds" + docIds);
					
		  try {
		   StringBuffer query =new StringBuffer(GET_DOC_IDS);
		   con = DBConnection.getDBConnection();
		   query.append(rcType);
		   query.append(" ) ");
		   if(!docIds.equals("")) {
			   query.append(" AND RD.DOCUMENT_ID IN (" + docIds + " )" );
		   }
		   String sql = query.append("  GROUP BY RD.DOCUMENT_ID with ur").toString();
		   ps = con.prepareStatement(sql);
		   ps.setString(1,rcValue.trim());
		   rs = ps.executeQuery();

		   while (rs.next()) {
		    docList.add(rs.getInt("DOCUMENT_ID"));
		   }

		  } catch (SQLException e) {
		   e.printStackTrace();
		   logger.error(
		    "SQL Exception occured while getting documents."
		     + "Exception Message: "
		     + e.getMessage());
		   throw new KmException("SQLException: " + e.getMessage(), e);
		  } catch (Exception e) {
		   e.printStackTrace();
		   logger.error(
		    "Exception occured while getting documents."
		     + "Exception Message: "
		     + e.getMessage());
		   throw new KmException("Exception: " + e.getMessage(), e);
		  } finally {
		   try {

		    if (con != null) {
		     con.setAutoCommit(true);
		     con.close();
		    }
		    DBConnection.releaseResources(null, ps, rs);
		   } catch (Exception e) {

		   }
		  }
		  return docList;
		 }//getDocIds



	public ArrayList<HashMap<String,String>> getRcDataCsr(String rcValue,String selectedRcCategory,String rcType, ArrayList<String> docIdList) throws KmException {

		  HashMap<String, String>  valueMap;
		  ArrayList<HashMap<String,String>> valueList = new ArrayList<HashMap<String,String>>();
		  Connection con = null;
		  PreparedStatement ps = null;
		  ResultSet rs = null;
		  long date = System.currentTimeMillis();
		  java.sql.Date currentDate = new java.sql.Date(date);
		  logger.info("Current time retrived from s ytem   -----------             " +currentDate);

		  try {

		   ArrayList<Integer> docList = getDocIds(rcValue,rcType, docIdList);

		   StringBuffer query =new StringBuffer(GET_RC_DATA_CSR);
		   con = DBConnection.getDBConnection();
		   ps = con.prepareStatement(query.append(" with ur").toString());

		   for(int i=0;i<docList.size();i++)
		   {
			   valueMap = new HashMap<String,String>();

			   ps.setInt(1,docList.get(i));
			   ps.setString(2, selectedRcCategory);
			   logger.info("Final query for searchiong     ---      "+ps);
			   rs = ps.executeQuery();
			   ResultSetMetaData rsmd=rs.getMetaData();
			   logger.info("Doc ID's retrieved ...........--------::::::::::::::::::::::::::::::::::::"+docList.get(i));
			   logger.info("Category id retrieved ...........--------::::::::::::::::::::::::::::::::::::"+selectedRcCategory);
			   logger.info("Got the RS from the database DAO...........--------::::::::::::::::::::::::::::::::::::");
			if(rs.next())
			{
				logger.info("Inside rs.next in  first place    ----    rs.next was possible      -::::::::::::::::::::");
				logger.info("What we get as SD      --------------      "+rs.getDate("START_DT").toString()+"        End date   ------     "+rs.getDate("END_DT").toString());
				valueMap.put("docId", docList.get(i).toString());
				valueMap.put(rs.getString("HEADER"),rs.getString("VALUE"));
				valueMap.put("-1",rs.getString("TOPIC"));
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				java.util.Date SD = sdf.parse(rs.getDate("START_DT").toString());
				java.util.Date ED = sdf.parse(rs.getDate("END_DT").toString());
				java.util.Date CD = sdf.parse(currentDate.toString());
	        	
				int difSD = SD.compareTo(CD);
				int difED = ED.compareTo(CD);
				logger.info("Dif from SD  ----   "+difSD+"    DIF from Ed   -------    "+difED);
				//logger.info("First test   --    "+(!rs.getDate("START_DT").after(currentDate))+"    Second test     ----    "+(!currentDate.after(rs.getDate("END_DT"))));
				if(difSD<=0 && difED>=0){
					valueMap.put("expired", "N");
				}
				else{
					valueMap.put("expired", "Y");
				}
				
				valueMap.put("startDate", rs.getDate("START_DT").toString());
				valueMap.put("endDate", rs.getDate("END_DT").toString());
				while (rs.next())
			     {
				   valueMap.put(rs.getString("HEADER"),rs.getString("VALUE"));
				   logger.info("In the while loop of DAO...........--------::::::::::::::::::::::::::::::::::::");
			     }
				logger.info("Value map generated no   :::::::: -----------    "+valueMap.size()+"        Value map is null  --  "+valueMap.isEmpty());
				valueList.add(valueMap);
			}
			//logger.info("We came till here in DAO...........--------::::::::::::::::::::::::::::::::::::");
		   }

		  } catch (SQLException e) {
		   logger.info(e);
		   e.printStackTrace();
		   logger.error(
		    "SQL Exception occured while getting RC Data for CSR."
		     + "Exception Message: "
		     + e.getMessage());
		   throw new KmException("SQLException: " + e.getMessage(), e);
		  } catch (Exception e) {
		   logger.info(e);
		   e.printStackTrace();
		   logger.error(
		    "Exception occured while getting RC Data for CSR."
		     + "Exception Message: "
		     + e.getMessage());
		   throw new KmException("Exception: " + e.getMessage(), e);
		  } finally {
		   try {

		    if (con != null) {
		     con.setAutoCommit(true);
		     con.close();
		    }
		    DBConnection.releaseResources(null, ps, rs);
		   } catch (Exception e) {

		   }
		  }
		  return valueList;
	}//getRcData


	public ArrayList<KmRcCategoryDTO> getRcCategory() throws KmException {
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<KmRcCategoryDTO> rcCategoryList = new ArrayList<KmRcCategoryDTO>();
		try {
			StringBuffer query =new StringBuffer(GET_RC_CATEGORY);
			con = DBConnection.getDBConnection();
			ps = con.prepareStatement(query.append(" with ur").toString());
			rs = ps.executeQuery();

			   while (rs.next())
			     {
				   KmRcCategoryDTO rcCatDTO = new KmRcCategoryDTO();
				   rcCatDTO.setRcTypeId(rs.getString("TYPE_ID"));
				   rcCatDTO.setRcType(rs.getString("TYPE"));
				   rcCatDTO.setRcSubType(rs.getString("SUB_TYPE"));
				   rcCategoryList.add(rcCatDTO);
			     }
			   
			  } catch (SQLException e) {
			   logger.info(e);
			   e.printStackTrace();
			   logger.error(
			    "SQL Exception occured while getting RC Category."
			     + "Exception Message: "
			     + e.getMessage());
			   throw new KmException("SQLException: " + e.getMessage(), e);
			  } catch (Exception e) {
			   logger.info(e);
			   e.printStackTrace();
			   logger.error(
			    "Exception occured while getting RC Category."
			     + "Exception Message: "
			     + e.getMessage());
			   throw new KmException("Exception: " + e.getMessage(), e);
			  } finally {
			   try {

			    if (con != null) {
			     con.setAutoCommit(true);
			     con.close();
			    }
			    DBConnection.releaseResources(null, ps, rs);
			   } catch (Exception e) {

			   }
			  }
		return rcCategoryList;
	}


}
