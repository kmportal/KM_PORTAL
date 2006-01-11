package com.ibm.km.dao.impl;

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.ibm.km.common.Utility;
import com.ibm.km.dao.DBConnection;
import com.ibm.km.dao.KmDynamicIndexPageDao;
import com.ibm.km.dto.KmDynamicIndexedPageDto;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;

public class KmDynamicIndexPageDaoImpl implements KmDynamicIndexPageDao{


	 private static Logger logger = Logger.getLogger(KmDynamicIndexPageDaoImpl.class);

	public ArrayList<KmDynamicIndexedPageDto> loadCategories(int categoryId,String firstiteration) throws Exception{
		ArrayList<KmDynamicIndexedPageDto> categories = new ArrayList<KmDynamicIndexedPageDto>();
		String sql = "";
		Statement pstmt=null;
		ResultSet rs = null;
		Connection conn = null;
		
		try {
		
		conn = DBConnection.getDBConnection();
		 sql = "Select Element_Id,Element_Name,Element_level_id from KM_ELEMENT_MSTR where element_id="+categoryId+" and element_level_id>3 and STATUS='A' with ur" ;
//		sql = "Select ELEMENT_ID from KM_ELEMENT_MSTR where PARENT_ID=(select PARENT_ID from KM_ELEMENT_MSTR where element_id=" + categoryId + ") and PAN_STATUS='Y'";
//		pstmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
		pstmt = conn.createStatement();
		
		rs = pstmt.executeQuery(sql);
		int panIndiaElementId = -1;
		int elementId = -1;
		logger.info("IN categoryId==" + categoryId);
		logger.info("IN firstiteration==" + firstiteration);
		
		while(rs.next() && firstiteration.equals("false") ){
			KmDynamicIndexedPageDto dto = new KmDynamicIndexedPageDto();
			panIndiaElementId = elementId = rs.getInt("Element_id");
			//int elementLevelId = rs.getInt("ELEMENT_LEVEL_ID");
			dto.setCategoryId(categoryId+"");
			dto.setCategoryName(rs.getString("Element_Name"));
			dto.setElementLevelId(rs.getInt("ELEMENT_LEVEL_ID")+"");
			int elementLevelId = rs.getInt("ELEMENT_LEVEL_ID");
			if(elementLevelId == 0){
				continue;				
				}
			dto.setCategories(loadSubCategories(elementId));
			categories.add(dto);
		}
//		logger.info("panIndiaElementId==" + panIndiaElementId);
		
		sql = "Select Element_Id,Element_Name,Element_level_id from KM_ELEMENT_MSTR where parent_id="+categoryId + " and STATUS='A' with ur" ;
		if (!firstiteration.equalsIgnoreCase("false")) {
			rs = null;
			rs = pstmt.executeQuery(sql);
			while (rs.next()) {
	//			logger.info("inside rs!!!!!");

				KmDynamicIndexedPageDto dto = new KmDynamicIndexedPageDto();

				elementId = rs.getInt("Element_id");
				//int elementLevelId = rs.getInt("ELEMENT_LEVEL_ID");
				logger.info("elementId==" + elementId);
				dto.setCategoryId(elementId + "");
				dto.setCategoryName(rs.getString("Element_Name"));
				dto.setElementLevelId(rs.getInt("ELEMENT_LEVEL_ID") + "");
				int elementLevelId = rs.getInt("ELEMENT_LEVEL_ID");
				if(elementLevelId == 0){
					String[] docDetails = getDocumentId(elementId);
					if (null == docDetails)
					{
						continue;
					}
					dto.setDocumentId(docDetails[0]);
					dto.setDocType(Integer.parseInt(docDetails[1]));
					dto.setDocumentViewUrl(Utility.getDocumentViewURL(docDetails[0], Integer.parseInt(docDetails[1])));
				}
				dto.setCategories(loadSubCategories(elementId));
				categories.add(dto);
			}
		}
		}
		catch (SQLException e) {
			e.printStackTrace();
			logger.error("SQLException occured while  loadCategories" + "Exception Message: " + e.getMessage());
			throw new KmException("SQL Exception: " + e.getMessage(), e);
		} catch (Exception e) {
			
			logger.error(" Exception occured while loadCategories." + "Exception Message: " + e.getMessage());
			throw new KmException(" Exception: " + e.getMessage(), e);
		} finally {
			try {
				/*if(rs!=null)
				rs.close();*/
				DBConnection.releaseResources(conn,pstmt,rs);
			} catch (Exception e) {
				logger.error("DAO Exception occured while loadCategories" + "Exception Message: " + e.getMessage());
				throw new KmException("DAO Exception: " + e.getMessage(), e);
			}
		}
		return categories;
		
	}
	
	private ArrayList<KmDynamicIndexedPageDto> loadSubCategories(int categoryId) throws Exception{
		ArrayList<KmDynamicIndexedPageDto> subCategories = new ArrayList<KmDynamicIndexedPageDto>();
		
		String sql = "Select Element_Id,Element_Name,Element_Level_Id from KM_ELEMENT_MSTR where parent_id="+categoryId + " and STATUS='A' with ur" ;
		
		Statement pstmt1=null;
		ResultSet rs1 = null;
		Connection conn = null;
		
		try
		{
			conn = DBConnection.getDBConnection();
			
//		pstmt1 = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
		pstmt1 = conn.createStatement();
		//pstmt1.setInt(1,categoryId);
		rs1 = pstmt1.executeQuery(sql);
		String elementLinkText="";
		while(rs1.next()){
			////System.out.println("inside rs1!!!!!");
			KmDynamicIndexedPageDto dto = new KmDynamicIndexedPageDto();
			dto.setCategoryId(rs1.getInt("Element_id")+"");
			elementLinkText=rs1.getString("Element_Name");			
			dto.setCategoryName(elementLinkText);
			if (elementLinkText.length()>25) elementLinkText = elementLinkText.substring(0,25); 
			dto.setCategoryDisplayName(elementLinkText);
			
			dto.setElementLevelId(rs1.getInt("ELEMENT_LEVEL_ID")+"");
			int elementLevelId = rs1.getInt("ELEMENT_LEVEL_ID");
			int elementId = rs1.getInt("Element_id");
			if(elementLevelId == 0){
				String[] docDetails = getDocumentId(elementId);
				if (null == docDetails)
				{
					continue;
				}
				dto.setDocumentId(docDetails[0]);
				dto.setDocType(Integer.parseInt(docDetails[1]));
				dto.setDocumentViewUrl(Utility.getDocumentViewURL(docDetails[0], Integer.parseInt(docDetails[1])));
			}
			subCategories.add(dto);
		}
		
		}
	catch (SQLException e) {
		e.printStackTrace();
		logger.error("SQLException occured while  loadCategories" + "Exception Message: " + e.getMessage());
		throw new KmException("SQL Exception: " + e.getMessage(), e);
	} catch (Exception e) {
		
		logger.error(" Exception occured while loadCategories." + "Exception Message: " + e.getMessage());
		throw new KmException(" Exception: " + e.getMessage(), e);
	} finally {
		try {

			DBConnection.releaseResources(conn,pstmt1,rs1);

		} catch (Exception e) {
			logger.error("DAO Exception occured while loadCategories" + "Exception Message: " + e.getMessage());
			throw new KmException("DAO Exception: " + e.getMessage(), e);
		}
	}
		
		return subCategories;
	}
	

	private String[] getDocumentId(int elementId) throws DAOException{
		String documentId = "";
		String docType = "";
		String[] documentDetails =null;
		////System.out.println(" &&&&& "+elementId);
		String sql = "Select document_id,DOC_TYPE from KM_DOCUMENT_MSTR where ELEMENT_ID="+elementId +" and date(PUBLISHING_END_DT) >= current date AND STATUS='A'  with UR";
		Statement pstmt = null;
		ResultSet rs = null;
		
		Connection conn = null;
		
		try{
		conn = DBConnection.getDBConnection();
			//		pstmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
		pstmt = conn.createStatement();
			
		rs = pstmt.executeQuery(sql);
		while(rs.next()){
			documentDetails = new String[2];
			documentDetails[0] = rs.getString("DOCUMENT_ID");
			String temp = rs.getString("DOC_TYPE");
			if(temp == null)
				temp = "1";
			documentDetails[1] = temp;
//			logger.info(documentDetails[0]+","+documentDetails[1]);
		}
		}catch(SQLException sqle){
			sqle.printStackTrace();
		}
		finally {
			try {
				DBConnection.releaseResources(conn,pstmt,rs); 
			} catch (Exception e) {
				logger.error("DAO Exception occured while getDocumentId" + "Exception Message: " + e.getMessage());
			}
		}
		return documentDetails;
	}

}
