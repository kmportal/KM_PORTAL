package com.ibm.km.dao.impl;

import java.sql.Connection;
import com.ibm.km.common.Constants;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.ibm.km.common.MyLabelValueBean;
import com.ibm.km.dao.*;
import com.ibm.km.dto.*;
import com.ibm.km.exception.*;

import java.util.*;
import java.sql.*;
import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;
public class KmCategoryMstrDaoImpl  implements KmCategoryMstrDao {


	/*
	* Logger for the class.
	*/
	private static final Logger logger;

	static {

		logger = Logger.getLogger(KmCategoryMstrDaoImpl.class);
	}

 //private static java.util.logging.Logger logger = java.util.logging.Logger.getLogger(KmCategoryMstrDaoImpl.class);


	protected static final String SQL_INSERT_WITH_ID = "INSERT INTO KM_CATEGORY_MSTR (CATEGORY_ID, CATEGORY_NAME, CATEGORY_DESC, STATUS, CIRCLE_ID, UPDATED_BY, CREATED_BY, CREATED_DT, UPDATE_DT) VALUES (?, ?, ?, ?, ?, ?, ?, current timestamp, ?)";

	protected static final String SQL_SELECT = "SELECT KM_CATEGORY_MSTR.CATEGORY_ID, KM_CATEGORY_MSTR.CATEGORY_NAME, KM_CATEGORY_MSTR.CATEGORY_DESC, KM_CATEGORY_MSTR.STATUS, KM_CATEGORY_MSTR.CIRCLE_ID, KM_CATEGORY_MSTR.UPDATED_BY, KM_CATEGORY_MSTR.CREATED_BY, KM_CATEGORY_MSTR.CREATED_DT, KM_CATEGORY_MSTR.UPDATE_DT FROM KM_CATEGORY_MSTR";

	//protected static final String SQL_SELECT_CATEGORY = "SELECT KM_CATEGORY_MSTR.CATEGORY_NAME, KM_CATEGORY_MSTR.CATEGORY_ID FROM KM_CATEGORY_MSTR WHERE STATUS = 'A' AND CIRCLE_ID = ? ORDER BY CATEGORY_NAME";
	
	protected static final String SQL_SELECT_CATEGORY = "SELECT KM_CATEGORY_MSTR.CATEGORY_NAME, KM_CATEGORY_MSTR.CATEGORY_ID FROM KM_CATEGORY_MSTR WHERE STATUS = 'A' AND ( CIRCLE_ID = ? ";
	
	protected static final String SQL_UPDATE = "UPDATE KM_CATEGORY_MSTR SET CATEGORY_ID = ?, CATEGORY_NAME = ?, CATEGORY_DESC = ?, STATUS = ?, CIRCLE_ID = ?, UPDATED_BY = ?, CREATED_BY = ?, CREATED_DT = ?, UPDATE_DT = ? WHERE CATEGORY_ID = ?";

	protected static final String SQL_DELETE = "DELETE FROM KM_CATEGORY_MSTR WHERE CATEGORY_ID = ?";

	protected static final String SQL_GET_CATEGORY_ID = "SELECT NEXTVAL FOR SAMEER.KM_CATEGORY_ID FROM SYSIBM.SYSDUMMY1";
	
	protected static final String SQL_GET_CATEGORY_MAP = "SELECT  A.CATEGORY_NAME, A.CATEGORY_ID, B.SUB_CATEGORY_NAME, B.SUB_CATEGORY_ID FROM KM_CATEGORY_MSTR A, KM_SUB_CATEGORY_MSTR B WHERE A.CATEGORY_ID=B.CATEGORY_ID AND A.CIRCLE_ID=? ORDER BY A.CATEGORY_ID, B.SUB_CATEGORY_ID";
	
	protected static final String SQL_COUNT_CATEGORY = "SELECT COUNT(CATEGORY_ID) NUM FROM KM_CATEGORY_MSTR WHERE CIRCLE_ID = ? ";
	//Changed
	protected static final String SQL_GET_ELEMENTS_MAP= " WITH nee(element_id,element_name,status,element_level_id,parent_id) AS ( SELECT  ELEMENT_ID, element_name ,status, element_level_id, parent_id  FROM KM_ELEMENT_MSTR WHERE element_id =? UNION ALL  SELECT nplus1.element_id, nplus1.element_name,nplus1.status ,nplus1.element_level_id, nplus1.parent_id FROM KM_ELEMENT_MSTR as nplus1, nee  WHERE nee.element_id=nplus1.PARENT_ID and nplus1.status='A')  SELECT A.parent_id as CATEGORY_ID, B.ELEMENT_NAME AS CATEGORY_NAME, A.element_id AS SUB_CATEGORY_ID ,A.element_name  AS SUB_CATEGORY_NAME, a.element_level_id ELEMENT_LEVEL_ID FROM nee A inner join km_element_mstr B on a.parent_id = b.element_id  where B.status='A'   and (a.element_level_id = 5 or a.element_level_id = 4)  order by a.element_level_id,a.parent_id,  a.element_name desc ";
	
	
	//Insert in to the table KM_CATEGORY_MSTR
	
	public  int insert(KmCategoryMstr dto) throws KmException {

		logger.info("Entered insert for table KM_CATEGORY_MSTR");

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs=null;

		int rowsUpdated = 0;
		try {
            
			StringBuffer query1=new StringBuffer(SQL_GET_CATEGORY_ID);
			StringBuffer query2=new StringBuffer(SQL_GET_CATEGORY_ID);
			//String sql1=SQL_GET_CATEGORY_ID;
			//String sql = SQL_GET_CATEGORY_ID;
			con=DBConnection.getDBConnection();
			con.setAutoCommit(false);
			ps=con.prepareStatement(query1.append("with ur ").toString());
			
			rs=ps.executeQuery();
			rs.next();
			int categoryId=Integer.parseInt(rs.getString(1));
			
			
			ps=con.prepareStatement(query2.append("with ur ").toString());
			
			/*Preparing statement for insertion */
			ps.setInt(1, categoryId);
			ps.setString(2,  dto.getCategoryName().trim());
			ps.setString(3,  dto.getCategoryDesc());
			ps.setString(4,  dto.getStatus());
			ps.setInt(5,  dto.getCircleId());
			ps.setInt(6,  dto.getUpdatedBy());
			ps.setInt(7,  dto.getCreatedBy());
			ps.setDate(8,  dto.getUpdateDt()); 
			
			/*Executing the querry */
			
			rowsUpdated=ps.executeUpdate();  
			
			
			con.commit();
			return categoryId;
	//	logger.info("Row insertion successful on table:KM_CATEGORY_MSTR. Inserted:" + rowsUpdated  + " rows");

		} catch (SQLException e) {
				e.printStackTrace();
				logger.info(e);
				logger.info("SQL Exception occured");
				return 0;
	//	logger.severe("SQL Exception occured while inserting." + "Exception Message: " + e.getMessage());
	
	  //      throw new KmCategoryMstrDaoException("SQLException: " + e.getMessage(), e);
			
		} catch (Exception e) {
			e.printStackTrace();
	//	logger.severe("Exception occured while inserting." + "Exception Message: " + e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
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
			}
		}
		
		
	}


 

	/* (non-Javadoc)
	 * @see com.ibm.km.dao.KmCategoryMstrDao#checkOnCategoryName(java.lang.String)
	 */
	public boolean checkOnCategoryName(String categoryName, String circleId) throws KmException {
		logger.info("Entered checkCategory for table:KM_CATEGORY_MSTR");
		logger.info("Circle Name ="+categoryName);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isCategoryExists = false;
	
		try {
			StringBuffer query=new StringBuffer(SQL_SELECT);
			query.append(" WHERE UPPER( KM_CATEGORY_MSTR.CATEGORY_NAME) = ? AND KM_CATEGORY_MSTR.CIRCLE_ID = ? ").toString();
			con = DBConnection.getDBConnection();
			ps = con.prepareStatement(query.append("With ur ").toString());
			ps.setString(1, categoryName.toUpperCase());
			ps.setInt(2, Integer.parseInt(circleId));
			rs = ps.executeQuery();
				
			/*Checking whether the category name already exists in the table */
				
			while(rs.next()){
				isCategoryExists = true;
			}
			return isCategoryExists;
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(
				"SQL Exception occured while checkCategory." + "Exception Message: " + e.getMessage());
			throw new KmException("SQLException: " + e.getMessage(), e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception occured while checking Category on name." + "Exception Message: " + e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/* (non-Javadoc)
	 * @see com.ibm.km.dao.KmFileDao#getCategories(java.lang.String)
	 * @To get all categories under the given circle
	 **/
	 // To get all categories under the given circle from table KM_DOCUMENT_MSTR.
	public ArrayList getCategories(String[] circleId) throws KmException {
		Connection con=null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		KmCategoryMstr dto;
		String sql = SQL_SELECT_CATEGORY;
		for(int i=1;i<circleId.length;i++){
			sql+=" OR CIRCLE_ID=? ";		
		}
		sql+=") ORDER BY CATEGORY_NAME";
		
		try {
			ArrayList categoryList=new ArrayList();
			con=DBConnection.getDBConnection();
			ps=con.prepareStatement(sql);
			for(int i=0;i<circleId.length;i++){
				ps.setInt(i+1, Integer.parseInt(circleId[i]));	
		}		
		//logger.info("Success1");
		rs = ps.executeQuery();
		while(rs.next()){
			
			dto=new KmCategoryMstr();
			dto.setCategoryName(rs.getString("CATEGORY_NAME"));
			dto.setCategoryId(rs.getInt("CATEGORY_ID"));
			categoryList.add(dto); 	
 
		}
			
		//logger.info("Success1");	
		return categoryList;
         
	} catch (SQLException e) {
		e.printStackTrace();
		logger.info(e);
//	logger.severe("SQL Exception occured while find." + "Exception Message: " + e.getMessage());
		throw new KmException("SQLException: " + e.getMessage(), e);
	} catch (Exception e) {
		e.printStackTrace();
		logger.info(e);
//	logger.severe("Exception occured while find." + "Exception Message: " + e.getMessage());
		e.printStackTrace();
		throw new KmException("Exception: " + e.getMessage(), e);
	} finally {

			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (con != null) {
					con.close();
				}
		   } catch (Exception e) {
				e.printStackTrace();
			}
		}
	}







	/* (non-Javadoc)
	 * @see com.ibm.km.dao.KmCategoryMstrDao#getCategoryMap(java.lang.String[])
	 */
	public HashMap getCategoryMap(String circleId) throws KmException {
		Connection con=null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		HashMap categoryMap=new HashMap();
		ArrayList subCategoryList= new ArrayList();
		String sql = SQL_GET_CATEGORY_MAP;
		logger.info("Entered in getCategoryMap");
		
		try {
			ArrayList categoryList=new ArrayList();
			con=DBConnection.getDBConnection();
			ps=con.prepareStatement(sql);
			ps.setInt(1, Integer.parseInt(circleId));	
			rs = ps.executeQuery();
			String oldCategoryName=null;
			if(rs.next()){
				oldCategoryName=rs.getString("CATEGORY_NAME");
				subCategoryList.add(new LabelValueBean(rs.getString("SUB_CATEGORY_NAME"),rs.getString("SUB_CATEGORY_ID")));
				while(rs.next()){
					if(!oldCategoryName.equals(rs.getString("CATEGORY_NAME"))){
						categoryMap.put(oldCategoryName,subCategoryList);
						oldCategoryName=rs.getString("CATEGORY_NAME");
						subCategoryList = new ArrayList();
					}
					subCategoryList.add(new LabelValueBean(rs.getString("SUB_CATEGORY_NAME"),rs.getString("SUB_CATEGORY_ID")));
				}
				categoryMap.put(oldCategoryName,subCategoryList);
			}
			
		} catch (SQLException e) {
		
			logger.error("SQLException occured while getting CategoryMap ." + "Exception Message: " + e.getMessage());
			throw new KmException("SQLException: " + e.getMessage(), e);
		} catch (Exception e) {
		
			logger.error("Exception occured while getting CategoryMap ." + "Exception Message: " + e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try {
				DBConnection.releaseResources(con,ps,rs);
		   } catch (Exception e) {
			logger.error("DAO Exception occured while getting CategoryMap ." + "Exception Message: " + e.getMessage());
						throw new KmException("Exception: " + e.getMessage(), e);
			}
		}
		logger.info("Exit from getCategoryMap");
		return categoryMap;

	}
	/* (non-Javadoc)
	 * @see com.ibm.km.dao.KmCategoryMstrDao#getNoOfCategories(java.lang.String)
	 */
	public int getNoOfCategories(String circleId) throws KmException {
		Connection con=null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		HashMap categoryMap=new HashMap();
		ArrayList subCategoryList= new ArrayList();
		String sql = SQL_COUNT_CATEGORY;
		int noOfCategories=0;
		try {
			
			con=DBConnection.getDBConnection();
			ps=con.prepareStatement(sql);
			ps.setInt(1, Integer.parseInt(circleId));	
			rs = ps.executeQuery();
			while(rs.next()){
				noOfCategories=rs.getInt("NUM");
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
	//	logger.severe("SQL Exception occured while find." + "Exception Message: " + e.getMessage());
			throw new KmException("SQLException: " + e.getMessage(), e);
		} catch (Exception e) {
			e.printStackTrace();
	//	logger.severe("Exception occured while find." + "Exception Message: " + e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (con != null) {
					con.close();
				}
		   } catch (Exception e) {
			e.printStackTrace();
			}
		}
		return noOfCategories;
	}

		
	public LinkedHashMap getCategoryMapElements(String elementId, String favCategoryId) throws KmException {
			Connection con=null;
			ResultSet rs = null;
			ResultSet rs1 = null;
			PreparedStatement ps = null;
			LinkedHashMap categoryMap=new LinkedHashMap();
			LinkedHashMap newCategoryMap=new LinkedHashMap();
			ArrayList subCategoryList= new ArrayList();
			String sqlQuery =SQL_GET_ELEMENTS_MAP;
		
			try {
				ArrayList categoryList=new ArrayList();
				con=DBConnection.getDBConnection();
				ps=con.prepareStatement(sqlQuery+" with ur ");
				ps.setInt(1, Integer.parseInt(elementId));	
				rs = ps.executeQuery();
				rs1=rs;
				
				String oldCategoryName=null;
				String oldCategoryId=null;
				String elementLevelId=null;
				int i=0;
					while(rs.next()){
						//logger.info("Category Name: "+oldCategoryName);
						elementLevelId=rs.getString("ELEMENT_LEVEL_ID");
						if(elementLevelId.equals(Constants.CIRCLE_CSR)){
							
							
							categoryMap.put(new MyLabelValueBean(rs.getString("SUB_CATEGORY_NAME"),rs.getString("SUB_CATEGORY_ID")),new ArrayList());
						}else if(elementLevelId.equals(Constants.LOB_ADMIN)){
							if(i==0){
								oldCategoryName=rs.getString("CATEGORY_NAME");
								oldCategoryId=rs.getString("CATEGORY_ID");
								
								subCategoryList.add(new LabelValueBean(rs.getString("SUB_CATEGORY_NAME"),rs.getString("SUB_CATEGORY_ID")));
								i++;
							}else if(i>0){
								if(!oldCategoryName.equals(rs.getString("CATEGORY_NAME"))){
									
									categoryMap.put(new MyLabelValueBean(oldCategoryName,oldCategoryId),subCategoryList);
									oldCategoryName=rs.getString("CATEGORY_NAME");
									oldCategoryId=rs.getString("CATEGORY_ID");
									subCategoryList = new ArrayList();
									
								}
								subCategoryList.add(new LabelValueBean(rs.getString("SUB_CATEGORY_NAME"),rs.getString("SUB_CATEGORY_ID")));							
							}
						}
					}
					if(i>0){
						categoryMap.put(new MyLabelValueBean(oldCategoryName,oldCategoryId),subCategoryList);
					}
		} catch (SQLException e) {
				e.printStackTrace();
		//	logger.severe("SQL Exception occured while find." + "Exception Message: " + e.getMessage());
				throw new KmException("SQLException: " + e.getMessage(), e);
			} catch (Exception e) {
				e.printStackTrace();
		    //	logger.severe("Exception occured while find." + "Exception Message: " + e.getMessage());
				throw new KmException("Exception: " + e.getMessage(), e);
			} finally {
				try {
					if (rs != null)
						rs.close();
					if (ps != null)
						ps.close();
					if (con != null) {
						con.close();
					}
			   } catch (Exception e) {
				e.printStackTrace();
				}
			}
			try{
			if(favCategoryId!=null){
				
				Iterator myVeryOwnIterator = categoryMap.keySet().iterator();
				LabelValueBean bean=null;
				LabelValueBean favCategoryBean=null;
				ArrayList favCategoryList=new ArrayList();
				while(myVeryOwnIterator.hasNext()) {
			    
			   
					bean=(LabelValueBean) myVeryOwnIterator.next();
					if(bean.getValue().equals(favCategoryId)){
						newCategoryMap.put(bean, categoryMap.get(bean));
			    }
			    
			}
			myVeryOwnIterator=categoryMap.keySet().iterator();
			while(myVeryOwnIterator.hasNext()) {
				  bean=(LabelValueBean) myVeryOwnIterator.next();
				if(!bean.getValue().equals(favCategoryId)){
					
					favCategoryBean=bean;
					favCategoryList=(ArrayList)categoryMap.get(bean);
					newCategoryMap.put(bean, favCategoryList);
				}
			}
				return newCategoryMap;
			}	
			else{
				return categoryMap;
			}
			}
			catch(Exception e){
				return categoryMap;
			}
		}

	private HashMap getMap(HashMap categoryMap) {
		Iterator myVeryOwnIterator = categoryMap.keySet().iterator();
		while(myVeryOwnIterator.hasNext()) {
		    //System.out.println(myVeryOwnIterator.next());		   
		    LabelValueBean bean=(LabelValueBean) myVeryOwnIterator.next();		    
		}
			
		return categoryMap;
	}
}