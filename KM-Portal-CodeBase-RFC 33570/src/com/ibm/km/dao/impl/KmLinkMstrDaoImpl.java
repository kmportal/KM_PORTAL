package com.ibm.km.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.ibm.km.dao.DBConnection;
import com.ibm.km.dao.KmLinkMstrDao;
import com.ibm.km.dto.KmLinkMstrDto;
import com.ibm.km.dto.List;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.KmLinkMstrFormBean;

public class KmLinkMstrDaoImpl implements KmLinkMstrDao{
	

	private static Logger logger = Logger.getLogger(KmLinkMstrDaoImpl.class);

	public ArrayList editLink(KmLinkMstrDto kmLinkMstrDto) throws KmException,DAOException{
		Connection conn = DBConnection.getDBConnection();
		ArrayList list = null;
		PreparedStatement pstmt = null;
		int rowCount = 0;
		try{
			

				pstmt = conn.prepareStatement("update KM_LINK_MSTR set Link_Title=?, Link_Path=? where Link_Id=? and Element_Id=?");
				//System.out.println("link Title====="+kmLinkMstrDto.getLinkTitle());
				pstmt.setString(1, kmLinkMstrDto.getLinkTitle());
				pstmt.setString(2, kmLinkMstrDto.getLinkPath());
				pstmt.setInt(3, kmLinkMstrDto.getLinkId());
				pstmt.setInt(4,kmLinkMstrDto.getElementId());
				rowCount = pstmt.executeUpdate();
				//System.out.println("rowcount in dao=="+rowCount);
				if(rowCount == 0 ){
					kmLinkMstrDto.setLinkId(getMaxLinkIdFromDB(conn)+1);
					rowCount = insertLink(kmLinkMstrDto);
				}
				
			list = viewLinks(kmLinkMstrDto.getElementId()+"");
		
		}
		catch(SQLException sqle){
			
			sqle.printStackTrace();
			
		}
		finally
		{
			try
			{
				DBConnection.releaseResources(conn,pstmt,null);
			}
			catch(DAOException e)
			{
				logger.error("DAOException occured in editLink" + "Exception Message: " + e.getMessage());
				throw new KmException(e.getMessage(),e);
			}
		}	
		return list;
	}
	public int insertLink(KmLinkMstrDto kmLinkMstrDto) throws KmException,DAOException{
		
		Connection conn = DBConnection.getDBConnection();
		PreparedStatement pstmt = null;
		int rowCount = 0;
		try{
			pstmt = conn.prepareStatement("insert into KM_LINK_MSTR values(?,?,?,?,?)");
			pstmt.setInt(1, kmLinkMstrDto.getLinkId());
			pstmt.setString(2,kmLinkMstrDto.getLinkTitle());
			pstmt.setString(3, kmLinkMstrDto.getLinkPath());
			if(kmLinkMstrDto.getElementId() == 1)
				pstmt.setInt(4,1);   // Element_Level_Id = 1 in case of Top Link Configuration By Super Admin
			else
		    pstmt.setInt(4, Integer.parseInt(kmLinkMstrDto.getKmActorId()));
			pstmt.setInt(5,kmLinkMstrDto.getElementId());
			rowCount = pstmt.executeUpdate();
		}catch(SQLException sqle){
			sqle.printStackTrace();
		}
		finally
		{
			try
			{
				DBConnection.releaseResources(conn,pstmt,null);
			}
			catch(DAOException e)
			{
				logger.error("DAOException occured in insertLink" + "Exception Message: " + e.getMessage());
				throw new KmException(e.getMessage(),e);
			}
		}	
		
		return rowCount;

	}
	
	
	public ArrayList viewLinks() throws KmException,DAOException{
		ArrayList list= new ArrayList();
		Connection conn = DBConnection.getDBConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			pstmt = conn.prepareStatement("select * from KM_Link_Mstr");
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				KmLinkMstrDto list1 = new KmLinkMstrDto();
//				KmLinkMstrFormBean formBean = new KmLinkMstrFormBean();
				list1.setLinkId(rs.getInt("Link_Id"));
				list1.setLinkTitle(rs.getString("Link_Title"));
				list1.setLinkPath(rs.getString("Link_Path"));
				list.add(list1);
				
				//System.out.println("Link Path "+list1.getLinkPath());
			}
		}
		catch(SQLException sqle){
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
				logger.error("DAOException occured in viewLinks" + "Exception Message: " + e.getMessage());
				throw new KmException(e.getMessage(),e);
			}
		}	
		return list;
	}
	public ArrayList viewLinks(String elementId) throws KmException,DAOException{
		ArrayList list= new ArrayList();
		Connection conn = DBConnection.getDBConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			pstmt = conn.prepareStatement("select * from KM_Link_Mstr where ELEMENT_ID=?");
			pstmt.setInt(1,Integer.parseInt(elementId));
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				KmLinkMstrDto list1 = new KmLinkMstrDto();
//				KmLinkMstrFormBean formBean = new KmLinkMstrFormBean();
				list1.setLinkId(rs.getInt("Link_Id"));
				list1.setLinkTitle(rs.getString("Link_Title"));
				list1.setLinkPath(rs.getString("Link_Path"));
				if(list1.getLinkPath() != null && !(list1.getLinkPath().startsWith("http://") || list1.getLinkPath().startsWith("HTTP://") ) 
						&& !(list1.getLinkPath().startsWith("https://")) && !rs.getString("Link_Path").equalsIgnoreCase(""))
					list1.setLinkPath("http://" + rs.getString("Link_Path"));
				list.add(list1);
				
			}
			
		}
		catch(SQLException sqle){
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
				logger.error("DAOException occured in viewLinks" + "Exception Message: " + e.getMessage());
				throw new KmException(e.getMessage(),e);
			}
		}	
		return list;
	}

	public ArrayList getUserRoleList(String actorId) throws KmException,DAOException{
		ArrayList list= new ArrayList();
		Connection conn = DBConnection.getDBConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			//System.out.println("actorId : "+actorId);
			pstmt = conn.prepareStatement("select KMM.MODULE_URL  as MODULE_URL from KM_MODULE_MSTR KMM, KM_MODULE_ACTOR_MAPPING KMA where KMM.MODULE_ID = KMA.MODULE_ID and KMA.KM_ACTOR_ID = ? AND KMM.STATUS in ('A','B','H') with ur");
			pstmt.setInt(1,Integer.parseInt(actorId));
			rs = pstmt.executeQuery();
			KmLinkMstrDto linkPathDto =null;
			while(rs.next()){
				linkPathDto = new KmLinkMstrDto();
				linkPathDto.setLinkPath(rs.getString("MODULE_URL"));
				list.add(linkPathDto);
			}
			
		}
		catch(SQLException sqle){
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
				logger.error("DAOException occured in getUserRoleList" + "Exception Message: " + e.getMessage());
				throw new KmException(e.getMessage(),e);
			}
		}	
		return list;
	}

	
	private int getMaxLinkIdFromDB(Connection conn) throws DAOException{
		int max_count = 0;
		
		//Connection conn = DBConnection.getDBConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			pstmt = conn.prepareStatement("select MAX(Link_Id) as COUNT from KM_Link_Mstr");
			rs = pstmt.executeQuery();
			while(rs.next()){
				max_count = rs.getInt("COUNT");
			}
		}
		catch(SQLException sqle){
			sqle.printStackTrace();
		}
		
		return max_count;
	}
	public int getMaxRowCountForElement(int elementId) throws DAOException{
		int max_count = 0;
		Connection conn = DBConnection.getDBConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			pstmt = conn.prepareStatement("select count(Link_Id) as COUNT from KM_Link_Mstr where element_Id=?");
			pstmt.setInt(1,elementId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				max_count = rs.getInt("COUNT");
			}
			conn.close();
		}
		catch(SQLException sqle){
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
				logger.error("DAOException occured in getMaxRowCountForElement" + "Exception Message: " + e.getMessage());
				throw new KmException(e.getMessage(),e);
			}
		}	

		
		return max_count;
	}
	
	public int getLinkIdForElement(int elementId) throws DAOException{
		int linkId = 0;
		
		Connection conn = DBConnection.getDBConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			pstmt = conn.prepareStatement("select Link_Id from KM_Link_Mstr where element_Id=? order by Link_Id asc FETCH FIRST 1 ROWS ONLY");
			pstmt.setInt(1,elementId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				linkId = rs.getInt("Link_Id");
			}
			conn.close();
		}
		catch(SQLException sqle){
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
				logger.error("DAOException occured in getLinkIdForElement" + "Exception Message: " + e.getMessage());
				throw new KmException(e.getMessage(),e);
			}
		}	
		
		return linkId;
	}
}
