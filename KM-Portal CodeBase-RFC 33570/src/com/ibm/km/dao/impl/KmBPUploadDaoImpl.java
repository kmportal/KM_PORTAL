package com.ibm.km.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.ibm.km.dao.DBConnection;
import com.ibm.km.dao.KmBPUploadDao;
import com.ibm.km.dto.KmBPUploadDto;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.KmBPUploadFormBean;
import com.ibm.ws.scripting.ArrayListAttrHelper;

public class KmBPUploadDaoImpl implements KmBPUploadDao{
	

	private static Logger logger = Logger.getLogger(KmBPUploadDaoImpl.class);

	
	public ArrayList<KmBPUploadDto> getHeaders() throws DAOException{
		ArrayList<KmBPUploadDto> headers = new ArrayList<KmBPUploadDto>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query = "SELECT HEADER_ID,HEADER_NAME,CATEGORY_ID FROM KM_BP_HEADER_MSTR WHERE ACTIVE='A'";
		
		try{
		conn = DBConnection.getDBConnection();
		pstmt = conn.prepareStatement(query);
		rs = pstmt.executeQuery();
		while(rs.next()){
			KmBPUploadDto dto = new KmBPUploadDto();
			dto.setHeaderId(rs.getInt("HEADER_ID")+"");
			dto.setHeaderName(rs.getString("HEADER_NAME"));
			dto.setCategoryId(rs.getInt("CATEGORY_ID")+"");
			headers.add(dto);
		}
		}catch(SQLException sqle){
			sqle.printStackTrace();
		}
		finally
		{
			try
			{
				DBConnection.releaseResources(conn,pstmt,rs);
			}
			catch(DAOException e)
			{
				logger.error("DAOException occured in getHeaders" + "Exception Message: " + e.getMessage());
				throw new KmException(e.getMessage(),e);
				
			}
		}	
		
		return headers;
	}
	
	public int insertBPData(KmBPUploadDto dto) throws DAOException{
		int rowsUpdated = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query="insert into KM_BP_DATA(TOPIC,HEADER,CONTENT,PATH,FROM_DT,TO_DT,DOCUMENT_ID,CIRCLE_ID) values(?,?,?,?,?,?,?,?)";
		try {
			   conn = DBConnection.getDBConnection();
			   pstmt = conn.prepareStatement(query);
			   ArrayList<String> headerList = dto.getHeaderList(); 
			   ArrayList<String> contentList  = dto.getContentList();
			   //System.out.println("headerList in daoimpl="+headerList.get(0));
			   //System.out.println("contentList in daoimpl="+contentList.get(0));
			   //System.out.println(dto.getFromDate());
			//   System.out.println("TO DATE IN DAO: -->>" + dto.getToDate());
			   for(int i = 0; i<dto.getTotalHeaders();i++){
				   ////System.out.println("Inside for loop");
				   int paramCount =1;
				   pstmt.setString(paramCount++, dto.getTopic());
				   pstmt.setString(paramCount++, headerList.get(i));
				   pstmt.setString(paramCount++, contentList.get(i));
				   pstmt.setString(paramCount++, dto.getContentPath());
				   pstmt.setDate(paramCount++,dto.getFromDate());
				   pstmt.setDate(paramCount++,dto.getToDate());
				   pstmt.setInt(paramCount++,Integer.parseInt(dto.getDocumentId()));
				   pstmt.setInt(paramCount++,Integer.parseInt(dto.getCircleId()));
				   pstmt.addBatch();
				 }  
				
			   pstmt.executeBatch();

			   }catch(SQLException sqle){
				   sqle.printStackTrace();
				   sqle.getNextException().printStackTrace();
			   }
			   finally
				{
					try
					{
						DBConnection.releaseResources(conn,pstmt,rs);
					}
					catch(DAOException e)
					{
						logger.error("DAOException occured in insertBPData" + "Exception Message: " + e.getMessage());
						throw new KmException(e.getMessage(),e);
						
					}
				}	
		
		
		return rowsUpdated;
	}
	public ArrayList<KmBPUploadDto> viewBPDetails(KmBPUploadDto dto) throws DAOException{
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query="select * from KM_BP_DATA kbd, KM_BP_HEADER_MSTR kbhm where kbd.header=kbhm.header_Id and kbd.document_id=? with ur";
		ArrayList<KmBPUploadDto> dataList = new ArrayList<KmBPUploadDto>();
		
		conn = DBConnection.getDBConnection();
		try{
		pstmt = conn.prepareStatement(query);
		pstmt.setInt(1,Integer.parseInt(dto.getDocumentId()));
		rs = pstmt.executeQuery();
		while(rs.next()){
			KmBPUploadDto subDto = new KmBPUploadDto();
			subDto.setTopic(rs.getString("TOPIC"));
			subDto.setHeaderId(rs.getString("HEADER_ID"));
			subDto.setHeaderName(rs.getString("HEADER_NAME"));
			subDto.setContent(rs.getString("CONTENT"));
			subDto.setCategoryId(rs.getInt("CATEGORY_ID")+"");
			subDto.setPath(rs.getString("PATH"));
			subDto.setFromDt(rs.getString("FROM_DT"));
			subDto.setToDt(rs.getString("TO_DT"));
			subDto.setDocumentId(rs.getInt("DOCUMENT_ID")+"");
			subDto.setCircle(rs.getInt("CIRCLE_ID")+"");
			//subDto.setHeaderId(rs.getInt("HEADER_ID")+"");
			//subDto.setCategoryId(rs.getInt("CATEGORY_ID")+"");
			dataList.add(subDto);
		}
		
		}catch(SQLException sqle){
			sqle.printStackTrace();
		}
		finally
		{
			try
			{
				DBConnection.releaseResources(conn,pstmt,rs);
			}
			catch(DAOException e)
			{
				logger.error("DAOException occured in viewBPDetails" + "Exception Message: " + e.getMessage());
				throw new KmException(e.getMessage(),e);
			}
		}	
		return dataList;
	}
	
	public int deleteBPDetails(KmBPUploadDto dto) throws DAOException{
		int rowsDeleted = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query="DELETE FROM KM_BP_DATA where DOCUMENT_ID=?";

		conn = DBConnection.getDBConnection();
		try{
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1,Integer.parseInt(dto.getDocumentId()));
			rowsDeleted = pstmt.executeUpdate();
		}catch(SQLException sqle){
			sqle.printStackTrace();
		}
		finally
		{
			try
			{
				DBConnection.releaseResources(conn,pstmt,rs);
			}
			catch(DAOException e)
			{
				logger.error("DAOException occured in deleteBPDetails" + "Exception Message: " + e.getMessage());
				throw new KmException(e.getMessage(),e);
			}
		}	
		
		return rowsDeleted;
	}
	public ArrayList<String> getDocumentId(String searchKey) throws DAOException{
		ArrayList<String> documentIds = new ArrayList<String>();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		//String query="Select distinct document_id from KM_BP_DATA where upper(?) in(upper(CONTENT),upper(TOPIC))";
		String query="Select distinct document_id from KM_BP_DATA where upper(CONTENT) like upper( ? ) or upper(TOPIC) like upper(?)";
		conn = DBConnection.getDBConnection();
		try{
			searchKey = "%"+searchKey+"%";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1,searchKey);
			pstmt.setString(2,searchKey);
			rs = pstmt.executeQuery();
			
		while(rs.next()){
			documentIds.add(rs.getInt("DOCUMENT_ID")+"");
		}
		}catch(SQLException sqle){
			sqle.printStackTrace();
		}
		finally
		{
			try
			{
				DBConnection.releaseResources(conn,pstmt,rs);
			}
			catch(DAOException e)
			{
				logger.error("DAOException occured in getDocumentId" + "Exception Message: " + e.getMessage());
				throw new KmException(e.getMessage(),e);
			}
		}	

		
		return documentIds;
	}
	public ArrayList<HashMap<String, String>> getCsrBPDetails(ArrayList<String> documentIds) throws DAOException{
		ArrayList<HashMap<String, String>> billPlansList = new ArrayList<HashMap<String, String>>();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query="Select TOPIC,HEADER,CONTENT,FROM_DT,document_id from KM_BP_DATA where document_Id=?";

		conn = DBConnection.getDBConnection();
		try{
			
			for(int i = 0;i<documentIds.size();i++){
				pstmt = conn.prepareStatement(query);
				pstmt.setInt(1,Integer.parseInt(documentIds.get(i)));
				rs = null;
				rs = pstmt.executeQuery();
				HashMap<String,String> billPlanData = new HashMap<String, String>();
				if(rs.next())
				{	
					billPlanData.put("TOPIC",rs.getString("TOPIC"));
					billPlanData.put("STARTDATE", rs.getString("FROM_DT"));
					billPlanData.put(rs.getString("HEADER"),rs.getString("CONTENT"));
					while(rs.next()){
					billPlanData.put("TOPIC",rs.getString("TOPIC"));
					billPlanData.put("STARTDATE", rs.getString("FROM_DT"));
					billPlanData.put(rs.getString("HEADER"),rs.getString("CONTENT"));
					}
					billPlansList.add(billPlanData);
				}
			}
		
		}catch(SQLException sqle){
			sqle.printStackTrace();
		}
		finally
		{
			try
			{
				DBConnection.releaseResources(conn,pstmt,rs);
			}
			catch(DAOException e)
			{
				logger.error("DAOException occured in getCsrBPDetails" + "Exception Message: " + e.getMessage());
				throw new KmException(e.getMessage(),e);
			}
		}	

		
		
		return billPlansList;
	}
	public String[] getCircleList(String documentId) throws DAOException{
		//String[] circles = null;
		ArrayList<String> circleList = new ArrayList<String>();
		//circles = (String[])circleList.toArray();
		String query = "select distinct circle_id from KM_BP_DATA where DOCUMENT_ID=?";
		Connection conn = DBConnection.getDBConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try{
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1,Integer.parseInt(documentId));
			rs = pstmt.executeQuery();
		
			while(rs.next()){
				circleList.add(rs.getString("circle_id"));
			}
		
			//circles = (String[])circleList.toArray();
		}catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		finally
		{
			try
			{
				DBConnection.releaseResources(conn,pstmt,rs);
			}
			catch(DAOException e)
			{
				logger.error("DAOException occured in getCircleList" + "Exception Message: " + e.getMessage());
				throw new KmException(e.getMessage(),e);
			}
		}	
		//System.out.println("circleList.size()="+circleList.size());
		String[] circles = new String[circleList.size()];
		circles = (String[])circleList.toArray(circles);
		return circles;
		
	}

	
}
