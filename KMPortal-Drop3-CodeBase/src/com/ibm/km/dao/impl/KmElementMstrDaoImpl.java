
package com.ibm.km.dao.impl;

import com.ibm.km.common.MyLabelValueBean;
import com.ibm.km.common.PropertyReader;
import com.ibm.km.dao.DBConnection;
import com.ibm.km.dao.KmElementMstrDao;
import com.ibm.km.dto.KmElementMstr;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.dto.QuizReportDto;
import com.ibm.km.exception.*;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.sql.*;
import java.util.*;

import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;

public class KmElementMstrDaoImpl   implements KmElementMstrDao
{

    public KmElementMstrDaoImpl()
    {
		
    }


 public HashMap<String, String> getAllCircleDesc()    throws KmException
 {
    Connection con;
    ResultSet rs;
    PreparedStatement ps;
    HashMap<String, String> circleMap = new HashMap<String, String>();
    con = null;
    rs = null;
    ps = null;
    try
    {
        con = DBConnection.getDBConnection();
        ps = con.prepareStatement(SQL_GET_CIRCLE_DESC);
        rs = ps.executeQuery();
        while(rs.next())
        {
        	circleMap.put(rs.getString("ELEMENT_ID"),rs.getString("ELEMENT_NAME"));
        }

    }
    catch(SQLException e)
    {
        e.printStackTrace();
        logger.info(e);
        throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
    }
    catch(Exception e)
    {
        e.printStackTrace();
        logger.info(e);
        throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
    }

  finally{
    try
    {
        DBConnection.releaseResources(con, ps, rs);
    }
    catch(DAOException e)
    {
        e.printStackTrace();
        throw new KmException(e.getMessage(), e);
    }

   }
    return circleMap;
}
//added by vishwas for lob wise upload
 public HashMap<String, String> getAllLobDesc()    throws KmException
 {
    Connection con;
    ResultSet rs;
    PreparedStatement ps;
    HashMap<String, String> circleMap = new HashMap<String, String>();
    con = null;
    rs = null;
    ps = null;
    try
    {
        con = DBConnection.getDBConnection();
        ps = con.prepareStatement(SQL_GET_LOB_DESC);
        rs = ps.executeQuery();
        while(rs.next())
        {
        	circleMap.put(rs.getString("ELEMENT_ID"),rs.getString("ELEMENT_NAME"));
        }

    }
    catch(SQLException e)
    {
        e.printStackTrace();
        logger.info(e);
        throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
    }
    catch(Exception e)
    {
        e.printStackTrace();
        logger.info(e);
        throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
    }

  finally{
    try
    {
        DBConnection.releaseResources(con, ps, rs);
    }
    catch(DAOException e)
    {
        e.printStackTrace();
        throw new KmException(e.getMessage(), e);
    }

   }
    return circleMap;
}
 public ArrayList<KmElementMstr> getAllCircles() throws KmException
 {
	  Connection con;
	    ResultSet rs;
	    PreparedStatement ps;
	    KmElementMstr dto =  null;
	    ArrayList listOfCircles = new ArrayList();
	    con = null;
	    rs = null;
	    ps = null;
	    try
	    {
	        con = DBConnection.getDBConnection();
	        ps = con.prepareStatement(SQL_GET_ALL_CIRCLE);
	        	for(rs = ps.executeQuery(); rs.next(); listOfCircles.add(dto))
	            {
	                dto = new KmElementMstr();
	                dto.setElementName(rs.getString("ELEMENT_NAME"));
	                dto.setElementId(rs.getString("ELEMENT_ID"));
	                dto.setElementLevel(rs.getString("ELEMENT_LEVEL_ID"));
	            }
	    }
	    catch(SQLException e)
	    {
	        e.printStackTrace();
	        logger.info(e);
	        throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
	    }
	    catch(Exception e)
	    {
	        e.printStackTrace();
	        logger.info(e);
	        throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
	    }

	  finally{
	    try
	    {
	        DBConnection.releaseResources(con, ps, rs);
	    }
	    catch(DAOException e)
	    {
	        e.printStackTrace();
	        throw new KmException(e.getMessage(), e);
	    }

	   }
	  logger.info("size of list of circles  :"+listOfCircles.size());
	    return listOfCircles;
 } 
 /*public ArrayList<KmElementMstr> findElementsInCircle(int elementId) throws KmException
 {
	  Connection con;
	    ResultSet rs;
	    PreparedStatement ps;
	    KmElementMstr dto =  null;
	    ArrayList listOfCircles = new ArrayList();
	    con = null;
	    rs = null;
	    ps = null;
	    try
	    {
	        con = DBConnection.getDBConnection();
	        logger.info("Query :"+SQL_GET_ALL_ELEMENTS_UNDER_CIRCLE);
	        logger.info("Parameter :"+elementId);
	        ps = con.prepareStatement(SQL_GET_ALL_ELEMENTS_UNDER_CIRCLE);
	        ps.setInt(1, elementId);
	        	for(rs = ps.executeQuery(); rs.next(); listOfCircles.add(dto))
	            {
	                dto = new KmElementMstr();
	                dto.setElementName(rs.getString("ELEMENT_NAME"));
	                dto.setElementId(rs.getString("ELEMENT_ID"));
	                dto.setElementLevel(rs.getString("ELEMENT_LEVEL_ID"));
	                dto.setSequenceNo(rs.getInt("SEQUENCENO"));
	            }
	    }
	    catch(SQLException e)
	    {
	        e.printStackTrace();
	        logger.info(e);
	        throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
	    }
	    catch(Exception e)
	    {
	        e.printStackTrace();
	        logger.info(e);
	        throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
	    }

	  finally{
	    try
	    {
	        DBConnection.releaseResources(con, ps, rs);
	    }
	    catch(DAOException e)
	    {
	        e.printStackTrace();
	        throw new KmException(e.getMessage(), e);
	    }

	   }
	  logger.info("size of list of circles  :"+listOfCircles.size());
	    return listOfCircles;
 } */
 
    public ArrayList<Integer> getAllElementsAsPerLevel(int levelId)
    throws KmException
{
    Connection con;
    ResultSet rs;
    PreparedStatement ps;
    ArrayList<Integer> elementList;
    con = null;
    rs = null;
    ps = null;
    elementList = new ArrayList<Integer>();
    try
    {
        con = DBConnection.getDBConnection();
        ps = con.prepareStatement(SQL_GET_ELEMENTS_AS_PER_LEVEL);
        ps.setInt(1,levelId);
        rs = ps.executeQuery();
        while(rs.next())
        {
        	elementList.add(rs.getInt("ELEMENT_ID"));
        }

    }
    catch(SQLException e)
    {
        e.printStackTrace();
        logger.info(e);
        throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
    }
    catch(Exception e)
    {
        e.printStackTrace();
        logger.info(e);
        throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
    }

  finally{
    try
    {
        DBConnection.releaseResources(con, ps, rs);
    }
    catch(DAOException e)
    {
        e.printStackTrace();
        throw new KmException(e.getMessage(), e);
    }

   }
    return elementList;
 }//getAllElementsAsPerLevel

    public KmElementMstr getElementDetails(int elementId)
    throws KmException
{
    Connection con;
    KmElementMstr dto = new KmElementMstr();
    ResultSet rs;
    PreparedStatement ps;
    con = null;
    rs = null;
    ps = null;
    try
    {
        con = DBConnection.getDBConnection();
        ps = con.prepareStatement(SQL_GET_ELEMENT);
        ps.setInt(1,elementId);
        rs = ps.executeQuery();

        if(rs.next())
        {
        	dto.setElementId(rs.getInt("ELEMENT_ID")+"");
        	dto.setElementName(rs.getString("ELEMENT_NAME"));
        	dto.setElementDesc(rs.getString("ELEMENT_DESC"));
        	dto.setParentId(rs.getInt("PARENT_ID")+"");
        	dto.setElementLevel(rs.getInt("ELEMENT_LEVEL_ID")+"");
        	dto.setPanStatus(rs.getString("PAN_STATUS"));
        	dto.setStatus(rs.getString("STATUS"));
        }

    }
    catch(SQLException e)
    {
        e.printStackTrace();
        logger.info(e);
        throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
    }
    catch(Exception e)
    {
        e.printStackTrace();
        logger.info(e);
        throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
    }

  finally{
    try
    {
        DBConnection.releaseResources(con, ps, rs);
    }
    catch(DAOException e)
    {
        e.printStackTrace();
        throw new KmException(e.getMessage(), e);
    }

   }
    return dto;
 }//getElementDetails





    public ArrayList getChildren(String elementId)
        throws KmException
    {
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        ArrayList elementList;
        con = null;
        rs = null;
        ps = null;
        elementList = new ArrayList();
        try
        {
            StringBuffer query = new StringBuffer("SELECT * FROM KM_ELEMENT_MSTR WHERE  STATUS = 'A' AND PARENT_ID=? AND ELEMENT_LEVEL_ID != 0 AND PAN_STATUS != 'Y'");
            con = DBConnection.getDBConnection();
            logger.info("Element Id "+elementId);
            ps = con.prepareStatement(query.append("  ORDER BY lower(ELEMENT_NAME) with ur ").toString());
            KmElementMstr dto = new KmElementMstr();
            ps.setInt(1, Integer.parseInt(elementId));
            for(rs = ps.executeQuery(); rs.next(); elementList.add(dto))
            {
                dto = new KmElementMstr();
                dto.setElementName(rs.getString("ELEMENT_NAME"));
                dto.setElementId(rs.getString("ELEMENT_ID"));
                dto.setElementLevel(rs.getString("ELEMENT_LEVEL_ID"));
            }

            logger.info((new StringBuilder("List is returned :")).append(elementList.size()).toString());
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            logger.info(e);
            throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            logger.info(e);
            throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }

      finally{
        try
        {
            DBConnection.releaseResources(con, ps, rs);
        }
        catch(DAOException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }

      }
        return elementList;
    }
    
    public ArrayList getCreateUserChildren(String elementId)
    throws KmException
{
    Connection con;
    ResultSet rs;
    PreparedStatement ps;
    ArrayList elementList;
    con = null;
    rs = null;
    ps = null;
    elementList = new ArrayList();
    try
    {
        StringBuffer query = new StringBuffer("SELECT * FROM KM_ELEMENT_MSTR WHERE  STATUS = 'A' AND PARENT_ID=? AND ELEMENT_LEVEL_ID != 0 AND PAN_STATUS = 'Y'");
        con = DBConnection.getDBConnection();
        logger.info(elementId);
        ps = con.prepareStatement(query.append("  ORDER BY lower(ELEMENT_NAME) with ur ").toString());
        KmElementMstr dto = new KmElementMstr();
        ps.setInt(1, Integer.parseInt(elementId));
        for(rs = ps.executeQuery(); rs.next(); elementList.add(dto))
        {
            dto = new KmElementMstr();
            dto.setElementName(rs.getString("ELEMENT_NAME"));
            dto.setElementId(rs.getString("ELEMENT_ID"));
            dto.setElementLevel(rs.getString("ELEMENT_LEVEL_ID"));
        }

        logger.info((new StringBuilder("List is returned :")).append(elementList.size()).toString());
    }
    catch(SQLException e)
    {
        e.printStackTrace();
        logger.info(e);
        throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
    }
    catch(Exception e)
    {
        e.printStackTrace();
        logger.info(e);
        throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
    }

  finally{
    try
    {
        DBConnection.releaseResources(con, ps, rs);
    }
    catch(DAOException e)
    {
        e.printStackTrace();
        throw new KmException(e.getMessage(), e);
    }

  }
    return elementList;
}
    



    public ArrayList getChildren(String parentId, String elementLevelId)
        throws KmException
    {
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        ArrayList elementList;
        con = null;
        rs = null;
        ps = null;
        elementList = new ArrayList();
        try
        {
            StringBuffer query = new StringBuffer("SELECT * FROM KM_ELEMENT_MSTR WHERE  STATUS = 'A' AND PARENT_ID=? AND ELEMENT_LEVEL_ID != 0 AND PAN_STATUS != 'Y'");
            query.append(" AND ELEMENT_LEVEL_ID=?").toString();
            con = DBConnection.getDBConnection();
            logger.info(parentId);
            ps = con.prepareStatement(query.append(" with ur ").toString());
            KmElementMstr dto = new KmElementMstr();
            ps.setInt(1, Integer.parseInt(parentId));
            ps.setInt(2, Integer.parseInt(elementLevelId));
            for(rs = ps.executeQuery(); rs.next(); elementList.add(dto))
            {
                dto = new KmElementMstr();
                dto.setElementName(rs.getString("ELEMENT_NAME"));
                dto.setElementId(rs.getString("ELEMENT_ID"));
                dto.setElementLevel(rs.getString("ELEMENT_LEVEL_ID"));
            }

            logger.info((new StringBuilder("List is returned :")).append(elementList.size()).toString());
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            logger.info(e);
            throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            logger.info(e);
            throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }
        finally{

        try
        {
            DBConnection.releaseResources(con, ps, rs);
        }
        catch(DAOException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }
        }
        return elementList;
    }

    public ArrayList getChildrenWithPath(String parentId, String root)
        throws KmException
    {
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        ArrayList elementList;
        con = null;
        rs = null;
        ps = null;
        elementList = new ArrayList();
        try
        {
            StringBuffer query = new StringBuffer("WITH NEE(ELEMENT_ID,  CHAIN) AS (SELECT  ELEMENT_ID, CAST(ELEMENT_NAME AS VARCHAR(2400)) FROM KM_ELEMENT_MSTR WHERE ELEMENT_ID = ? UNION ALL SELECT NPLUS1.ELEMENT_ID, NEE.CHAIN || '/' || NPLUS1.ELEMENT_NAME FROM KM_ELEMENT_MSTR AS NPLUS1, NEE WHERE NEE.ELEMENT_ID=NPLUS1.PARENT_ID) SELECT NEE.ELEMENT_ID, ELE.ELEMENT_NAME, CHAIN AS ELEMENT_PATH FROM KM_ELEMENT_MSTR ELE inner join NEE on ELE.ELEMENT_ID=NEE.ELEMENT_ID WHERE   ELE.PARENT_ID = ? AND ELE.ELEMENT_LEVEL_ID!=0 AND ELE.STATUS='A' AND ELE.PAN_STATUS = 'N' ");
            con = DBConnection.getDBConnection();
            logger.info(parentId);
            ps = con.prepareStatement(query.append(" with ur ").toString());
            KmElementMstr dto = new KmElementMstr();
            ps.setInt(1, Integer.parseInt(root));
            ps.setInt(2, Integer.parseInt(parentId));
            for(rs = ps.executeQuery(); rs.next(); logger.info((new StringBuilder("Element: ")).append(elementList).toString()))
            {
                dto = new KmElementMstr();
                dto.setElementName(rs.getString("ELEMENT_NAME"));
                dto.setElementId(rs.getString("ELEMENT_ID"));
                String path = rs.getString("ELEMENT_PATH");
                String documentStringPath = "";
                if(path.indexOf("/") + 1 < path.lastIndexOf("/"))
                    documentStringPath = path.substring(path.indexOf("/") + 1, path.lastIndexOf("/"));
                dto.setPath(documentStringPath);
                elementList.add(dto);
            }

            logger.info((new StringBuilder("List is returned :")).append(elementList.size()).toString());
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            logger.info(e);
            throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            logger.info(e);
            throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }
        finally{
        try
        {
            DBConnection.releaseResources(con, ps, rs);
        }
        catch(DAOException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }
        }
        return elementList;
    }

    public ArrayList getAllChildrenWithPath(String parentId, String root)
        throws KmException
    {
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        ArrayList elementList;
        con = null;
        rs = null;
        ps = null;
        elementList = new ArrayList();
        try
        {
            StringBuffer query = new StringBuffer("WITH NEE(ELEMENT_ID,  CHAIN) AS (SELECT  ELEMENT_ID, CAST(ELEMENT_NAME AS VARCHAR(2400)) FROM KM_ELEMENT_MSTR WHERE ELEMENT_ID = ? UNION ALL SELECT NPLUS1.ELEMENT_ID, NEE.CHAIN || '/' || NPLUS1.ELEMENT_NAME FROM KM_ELEMENT_MSTR AS NPLUS1, NEE WHERE NEE.ELEMENT_ID=NPLUS1.PARENT_ID) SELECT NEE.ELEMENT_ID, ELE.ELEMENT_NAME, ELE.ELEMENT_DESC,CHAIN AS ELEMENT_PATH FROM KM_ELEMENT_MSTR ELE inner join NEE on ELE.ELEMENT_ID=NEE.ELEMENT_ID WHERE ELE.PARENT_ID = ? AND ELE.ELEMENT_LEVEL_ID!=0 AND ELE.STATUS='A' ");
            KmElementMstr dto = new KmElementMstr();
            con = DBConnection.getDBConnection();
            ps = con.prepareStatement(query.append(" with ur").toString());
            ps.setInt(1, Integer.parseInt(root));
            ps.setInt(2, Integer.parseInt(parentId));
            for(rs = ps.executeQuery(); rs.next(); elementList.add(dto))
            {
                dto = new KmElementMstr();
                dto.setElementName(rs.getString("ELEMENT_NAME"));
                dto.setElementId(rs.getString("ELEMENT_ID"));
                dto.setElementDesc(rs.getString("ELEMENT_DESC"));
                String elementStringPath = "";
                String elementPath = rs.getString("ELEMENT_PATH");
                if(elementPath.indexOf("/") + 1 < elementPath.lastIndexOf("/"))
                    elementStringPath = elementPath.substring(elementPath.indexOf("/") + 1, elementPath.lastIndexOf("/"));
                dto.setPath(elementStringPath);
            }

            logger.info("Element List is Returned");
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }
        finally{
        try
        {
            DBConnection.releaseResources(con, ps, rs);
        }
        catch(DAOException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }
        }
        return elementList;
    }

    public ArrayList getAllChildren(String parentId)
        throws KmException
    {
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        con = null;
        rs = null;
        ps = null;
        ArrayList arraylist;
        try
        {
            ArrayList elementList = new ArrayList();
            StringBuffer query = new StringBuffer("SELECT * FROM KM_ELEMENT_MSTR WHERE  STATUS = 'A' AND PARENT_ID=? AND ELEMENT_LEVEL_ID != 0 ");
            KmElementMstr dto = new KmElementMstr();
            con = DBConnection.getDBConnection();
            ps = con.prepareStatement(query.append("order by lower(element_name)  with ur").toString());
           logger.info(Integer.parseInt(parentId)+"testing "+query);
            ps.setInt(1, Integer.parseInt(parentId));
            for(rs = ps.executeQuery(); rs.next(); elementList.add(dto))
            {
                dto = new KmElementMstr();
                dto.setElementName(rs.getString("ELEMENT_NAME"));
                dto.setElementId(rs.getString("ELEMENT_ID"));
                dto.setElementLevel(rs.getString("ELEMENT_LEVEL_ID"));
            }

            logger.info("Element List is Returned");
            arraylist = elementList;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }finally{
        try
        {
            DBConnection.releaseResources(con, ps, rs);
        }
        catch(DAOException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }
        }
        return arraylist;

    }

  //added by vishwas for UD Intregation to get Lob child
    public ArrayList getAllChildrenUD(String parentId)
    throws KmException
{
    Connection con;
    ResultSet rs;
    PreparedStatement ps;
    con = null;
    rs = null;
    ps = null;
    ArrayList arraylist;
    try
    {
    	//StringBuffer query =new StringBuffer();
    	ArrayList elementList = new ArrayList();
      logger.info("parent ID  for UD"+parentId);
    	String query="";
    	if(parentId=="B0071323_POST")
        {
    		logger.info("user is post paid UD user");
        		 query = new String("SELECT * FROM KM_ELEMENT_MSTR WHERE  STATUS = 'A' and ELEMENT_LEVEL_ID != 0  and  ELEMENT_ID in('185794') order by lower(element_name)  with ur");
        }
        else
        {
        	 query = new String("SELECT * FROM KM_ELEMENT_MSTR WHERE  STATUS = 'A' and ELEMENT_LEVEL_ID != 0  and  ELEMENT_ID in('185795') order by lower(element_name)  with ur");
        }
        
    	System.out.println("query :: "+query);
    	KmElementMstr dto = new KmElementMstr();
        con = DBConnection.getDBConnection();
        ps = con.prepareStatement(query);
       // ps.setInt(1, Integer.parseInt(parentId));
        for(rs = ps.executeQuery(); rs.next(); elementList.add(dto))
        {
            dto = new KmElementMstr();
            dto.setElementName(rs.getString("ELEMENT_NAME"));
            dto.setElementId(rs.getString("ELEMENT_ID"));
            dto.setElementLevel(rs.getString("ELEMENT_LEVEL_ID"));
        }

        logger.info("Element List is Returned");
        arraylist = elementList;
    }
    catch(SQLException e)
    {
        e.printStackTrace();
        throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
    }
    catch(Exception e)
    {
        e.printStackTrace();
        throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
    }finally{
    try
    {
        DBConnection.releaseResources(con, ps, rs);
    }
    catch(DAOException e)
    {
        e.printStackTrace();
        throw new KmException(e.getMessage(), e);
    }
    }
    return arraylist;

}
    
    public ArrayList getAllChildrencircleUD(String parentId)
    throws KmException
{
    Connection con;
    ResultSet rs;
    PreparedStatement ps;
    con = null;
    rs = null;
    ps = null;
    ArrayList arraylist;
    try
    {
        ArrayList elementList = new ArrayList();
        StringBuffer query=new StringBuffer();
       
        System.out.println("parentId for normal=========================="+parentId);
        if(parentId.equals("B0071323_POST"))
        {
        	System.out.println("parentId for postpaid"+parentId);
         query = new StringBuffer("SELECT * FROM KM_ELEMENT_MSTR WHERE  STATUS = 'A' AND PARENT_ID in('185794') and ELEMENT_LEVEL_ID = 3 ");	
        }
        if(parentId.equals("B0071323_PRE"))
        {
        	System.out.println("parentId for prepaid"+parentId);
        	 query = new StringBuffer("SELECT * FROM KM_ELEMENT_MSTR WHERE  STATUS = 'A' AND PARENT_ID in('185795') and ELEMENT_LEVEL_ID = 3 ");
        }
        KmElementMstr dto = new KmElementMstr();
        con = DBConnection.getDBConnection();
        ps = con.prepareStatement(query.append("order by lower(element_name)  with ur").toString());
       rs=ps.executeQuery();
        //  ps.setInt(1, Integer.parseInt(parentId));
        //for(rs = ps.executeQuery(); rs.next(); elementList.add(dto))
     System.out.println(query+"result sertt"+rs);
        while(rs.next())
    	   
        {
            dto = new KmElementMstr();
            dto.setElementName(rs.getString("ELEMENT_NAME"));
            dto.setElementId(rs.getString("ELEMENT_ID"));
            dto.setElementLevel(rs.getString("ELEMENT_LEVEL_ID"));
            elementList.add(dto);
        }
     //   elementList.add(dto);
        logger.info("Element List is Returned");
        arraylist = elementList;
    }
    catch(SQLException e)
    {
        e.printStackTrace();
        throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
    }
    catch(Exception e)
    {
        e.printStackTrace();
        throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
    }finally{
    try
    {
        DBConnection.releaseResources(con, ps, rs);
    }
    catch(DAOException e)
    {
        e.printStackTrace();
        throw new KmException(e.getMessage(), e);
    }
    }
    return arraylist;

}
    //end by vishwas
    
    public ArrayList getAllPANChildren(String parentId)   throws KmException
    {
    Connection con;
    ResultSet rs;
    PreparedStatement ps;
    con = null;
    rs = null;
    ps = null;
    ArrayList arraylist;
    try
    {
        ArrayList elementList = new ArrayList();
        StringBuffer query = new StringBuffer("SELECT * FROM KM_ELEMENT_MSTR WHERE  STATUS = 'A' AND PARENT_ID=? AND ELEMENT_LEVEL_ID != 0 and PAN_STATUS = 'Y'");
        KmElementMstr dto = new KmElementMstr();
        con = DBConnection.getDBConnection();
        ps = con.prepareStatement(query.append("order by lower(element_name)  with ur").toString());
        ps.setInt(1, Integer.parseInt(parentId));
        for(rs = ps.executeQuery(); rs.next(); elementList.add(dto))
        {
            dto = new KmElementMstr();
            dto.setElementName(rs.getString("ELEMENT_NAME"));
            dto.setElementId(rs.getString("ELEMENT_ID"));
            dto.setElementLevel(rs.getString("ELEMENT_LEVEL_ID"));
        }

        logger.info("Element List is Returned");
        arraylist = elementList;
    }
    catch(SQLException e)
    {
        e.printStackTrace();
        throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
    }
    catch(Exception e)
    {
        e.printStackTrace();
        throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
    }finally{
    try
    {
        DBConnection.releaseResources(con, ps, rs);
    }
    catch(DAOException e)
    {
        e.printStackTrace();
        throw new KmException(e.getMessage(), e);
    }
    }
    return arraylist;

}

    public ArrayList getAllChildrenElements(String parentId)
    throws KmException
{
    Connection con;
    ResultSet rs;
    PreparedStatement ps;
    con = null;
    rs = null;
    ps = null;
    ArrayList arraylist;
    try
    {
        ArrayList elementList = new ArrayList();
        StringBuffer query = new StringBuffer("SELECT * FROM KM_ELEMENT_MSTR WHERE  STATUS = 'A' AND PARENT_ID=? ");
        KmElementMstr dto = new KmElementMstr();
        con = DBConnection.getDBConnection();
        ps = con.prepareStatement(query.append("order by lower(element_name)  with ur").toString());
        ps.setInt(1, Integer.parseInt(parentId));
        for(rs = ps.executeQuery(); rs.next(); elementList.add(dto))
        {
            dto = new KmElementMstr();
            dto.setElementName(rs.getString("ELEMENT_NAME"));
            dto.setElementId(rs.getString("ELEMENT_ID"));
            dto.setElementLevel(rs.getString("ELEMENT_LEVEL_ID"));
            dto.setElementDesc(rs.getString("ELEMENT_DESC"));
            dto.setParentId(rs.getString("PARENT_ID"));
            dto.setPanStatus(rs.getString("PAN_STATUS"));
            dto.setStatus(rs.getString("STATUS"));
        }

        logger.info("Element List is Returned");
        arraylist = elementList;
    }
    catch(SQLException e)
    {
        e.printStackTrace();
        throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
    }
    catch(Exception e)
    {
        e.printStackTrace();
        throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
    }finally{
    try
    {
        DBConnection.releaseResources(con, ps, rs);
    }
    catch(DAOException e)
    {
        e.printStackTrace();
        throw new KmException(e.getMessage(), e);
    }
    }
    return arraylist;

}

    public ArrayList getAllChildren(String parentId, String levelId)
        throws KmException
    {
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        con = null;
        rs = null;
        ps = null;
        ArrayList arraylist;
        try
        {
            ArrayList elementList = new ArrayList();
            StringBuffer query = new StringBuffer("select ele1.element_id,ele1.element_name,ele1.element_level_id,ele2.element_id,ele2.element_name as parent_name from km_element_mstr ele1 inner join km_element_mstr ele2  on  ele1.parent_id = ele2.element_id  where ele1.parent_id=ele2.element_id and ele1.element_level_id=? ");
            KmElementMstr dto = new KmElementMstr();
            con = DBConnection.getDBConnection();
            ps = con.prepareStatement(query.append(" order by lower(ele2.element_name), lower(ele1.element_name) with ur").toString());
            ps.setInt(1, Integer.parseInt(levelId));
            for(rs = ps.executeQuery(); rs.next(); elementList.add(dto))
            {
                dto = new KmElementMstr();
                dto.setElementName((new StringBuilder(String.valueOf(rs.getString("PARENT_NAME")))).append(":").append(rs.getString("ELEMENT_NAME")).toString());
                dto.setElementId(rs.getString("ELEMENT_ID"));
                dto.setElementLevel(rs.getString("ELEMENT_LEVEL_ID"));
                dto.setParentName(rs.getString("PARENT_NAME"));
            }

            logger.info("Element List is Returned");
            arraylist = elementList;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }finally{
        try
        {
            DBConnection.releaseResources(con, ps, rs);
        }
        catch(DAOException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }
        }
        return arraylist;

    }

    public String getElementLevelName(String elementLevelId)
        throws KmException
    {
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        con = null;
        rs = null;
        ps = null;
        String s;
        try
        {
            logger.info((new StringBuilder("elementId ")).append(elementLevelId).toString());
            String elementLevelName = "";
            StringBuffer query = new StringBuffer("SELECT ELEMENT_LEVEL_NAME FROM KM_ELEMENT_LEVEL_MSTR WHERE ELEMENT_LEVEL_ID = ?");
            con = DBConnection.getDBConnection();
            ps = con.prepareStatement(query.append(" with ur").toString());
            ps.setInt(1, Integer.parseInt(elementLevelId));
            for(rs = ps.executeQuery(); rs.next();)
                elementLevelName = rs.getString("ELEMENT_LEVEL_NAME");

            logger.info((new StringBuilder("elementLevelNAme ")).append(elementLevelName).toString());
            s = elementLevelName;
        }
        catch(SQLException e)
        {
	        e.printStackTrace();
            logger.info(e);
            throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
        }
        catch(Exception e)
        {
	        e.printStackTrace();
            logger.info(e);
            throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }
        finally
        {
        try
        {
            DBConnection.releaseResources(con, ps, rs);
        }
        catch(DAOException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }
        }
        return s;

    }

    public ArrayList<String> getServiceIds(String elementId)
        throws KmException
    {
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        con = null;
        rs = null;
        ps = null;
        ArrayList<String> elementLevelList;
        try
        {
        	elementLevelList = new ArrayList();
            StringBuffer query = new StringBuffer("SELECT b.ELEMENT_ID,b.ELEMENT_NAME FROM  KM_ELEMENT_MSTR a,KM_ELEMENT_MSTR b  WHERE  a.ELEMENT_NAME=b.ELEMENT_NAME AND a.ELEMENT_ID=?");
            con = DBConnection.getDBConnection();
            ps = con.prepareStatement(query.append(" with ur ").toString());
            ps.setInt(1, Integer.parseInt(elementId));
            for(rs = ps.executeQuery(); rs.next();)
            	elementLevelList.add(rs.getString("ELEMENT_ID"));

            
        }
        catch(SQLException e)
        {
	        e.printStackTrace();
            logger.info(e);
            throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
        }
        catch(Exception e)
        {
	        e.printStackTrace();
            logger.info(e);
            throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }
       finally{
        try
        {
            DBConnection.releaseResources(con, ps, rs);
        }
        catch(DAOException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }
       }
        return elementLevelList;

    }
    
    
    public String getServicePath(String elementId)
    throws KmException
{
    Connection con;
    ResultSet rs;
    PreparedStatement ps;
    con = null;
    rs = null;
    ps = null;
    String elementPath;
    try
    {
    	String elementPath1 = null;
        StringBuffer query = new StringBuffer("SELECT KM2.TEST_HIERARCHY1(?) FROM SYSIBM.SYSDUMMY1");
        con = DBConnection.getDBConnection();
        ps = con.prepareStatement(query.append(" with ur ").toString());
        ps.setInt(1, Integer.parseInt(elementId));
        for(rs = ps.executeQuery(); rs.next();)
        	elementPath1=rs.getString(1);

        elementPath=elementPath1;
        
    }
    catch(SQLException e)
    {
        e.printStackTrace();
        logger.info(e);
        throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
    }
    catch(Exception e)
    {
        e.printStackTrace();
        logger.info(e);
        throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
    }
   finally{
    try
    {
        DBConnection.releaseResources(con, ps, rs);
    }
    catch(DAOException e)
    {
        e.printStackTrace();
        throw new KmException(e.getMessage(), e);
    }
   }
    return elementPath;

}
    
    
    public String getServiceIdPath(String elementId)
    throws KmException
{
    Connection con;
    ResultSet rs;
    PreparedStatement ps;
    con = null;
    rs = null;
    ps = null;
    String elementPath;
    try
    {
    	String elementPath1 = null;
        StringBuffer query = new StringBuffer("SELECT KM2.TEST_HIERARCHY(?) FROM SYSIBM.SYSDUMMY1");
        con = DBConnection.getDBConnection();
        ps = con.prepareStatement(query.append(" with ur ").toString());
        ps.setInt(1, Integer.parseInt(elementId));
        for(rs = ps.executeQuery(); rs.next();)
        	elementPath1=rs.getString(1);

        elementPath=elementPath1;
        
    }
    catch(SQLException e)
    {
        e.printStackTrace();
        logger.info(e);
        throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
    }
    catch(Exception e)
    {
        e.printStackTrace();
        logger.info(e);
        throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
    }
   finally{
    try
    {
        DBConnection.releaseResources(con, ps, rs);
    }
    catch(DAOException e)
    {
        e.printStackTrace();
        throw new KmException(e.getMessage(), e);
    }
   }
    return elementPath;

}
    
    
    
    
    public String getElementLevelId(String elementId)
    throws KmException
{
    Connection con;
    ResultSet rs;
    PreparedStatement ps;
    con = null;
    rs = null;
    ps = null;
    String s;
    try
    {
        String elementLevelId = "";
        StringBuffer query = new StringBuffer("SELECT ELEMENT_LEVEL_ID FROM  KM_ELEMENT_MSTR  WHERE  ELEMENT_ID = ?");
        
        logger.info(" getElementLevel Id ELEMENT_ID :"+elementId);
        con = DBConnection.getDBConnection();
        ps = con.prepareStatement(query.append(" with ur ").toString());
        ps.setInt(1, Integer.parseInt(elementId));
        for(rs = ps.executeQuery(); rs.next();)
            elementLevelId = rs.getString("ELEMENT_LEVEL_ID");

        s = elementLevelId;
    }
    catch(SQLException e)
    {
        e.printStackTrace();
        logger.info(e);
        throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
    }
    catch(Exception e)
    {
        e.printStackTrace();
        logger.info(e);
        throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
    }
   finally{
    try
    {
        DBConnection.releaseResources(con, ps, rs);
    }
    catch(DAOException e)
    {
        e.printStackTrace();
        throw new KmException(e.getMessage(), e);
    }
   }
    return s;

}
    

    public String getAllParentIdString(String rootId, String elementId)
        throws KmException
    {
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        String parentString;
        String trimmedParentString;
        con = null;
        rs = null;
        ps = null;
        parentString = null;
        trimmedParentString = "";
        try
        {
            //StringBuffer query = new StringBuffer("WITH NEE(ELEMENT_ID,CHAIN, ELEMENT_LEVEL_ID) AS (SELECT  ELEMENT_ID, CAST(CAST(ELEMENT_ID AS CHARACTER(5))AS VARCHAR(60)) ,ELEMENT_LEVEL_ID FROM KM_ELEMENT_MSTR  WHERE ELEMENT_ID =? UNION ALL SELECT NPLUS1.ELEMENT_ID, NEE.CHAIN || '/' || CAST(NPLUS1.ELEMENT_ID AS CHARACTER(5)), NPLUS1.ELEMENT_LEVEL_ID FROM KM_ELEMENT_MSTR AS NPLUS1, NEE WHERE NEE.ELEMENT_ID=NPLUS1.PARENT_ID) SELECT CHAIN AS PARENT_STRING FROM NEE WHERE NEE.ELEMENT_ID=?");
        	StringBuffer query = new StringBuffer("Select PATH_INFO from KM2.KM_ELEMENT_MSTR where ELEMENT_ID =?");
        	logger.info("Query for path info = "+query);
            con = DBConnection.getDBConnection();
            ps = con.prepareStatement(query.append(" with ur").toString());
          //  ps.setInt(1, Integer.parseInt(rootId));
            ps.setInt(1, Integer.parseInt(elementId));
            rs = ps.executeQuery();
            if(rs.next())
                parentString = rs.getString("PATH_INFO");
            
            logger.info("Parent String in getAllParentId method : "+parentString);
          /*  for(StringTokenizer tokenized = new StringTokenizer(parentString); tokenized.hasMoreTokens();)
                trimmedParentString = (new StringBuilder(String.valueOf(trimmedParentString))).append(tokenized.nextToken()).toString();*/

        }
        catch(SQLException e)
        {
            logger.info(e);
            e.printStackTrace();
            throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
        }
        catch(Exception e)
        {
            logger.info(e);
            e.printStackTrace();
            throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }
       finally{
        try
        {
            DBConnection.releaseResources(con, ps, rs);
        }
        catch(DAOException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }
       }
        return parentString;
    }

    public String getAllParentNameString(String rootId, String elementId)
        throws KmException
    {
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        String parentString;
        con = null;
        rs = null;
        ps = null;
        parentString = null;
        String trimmedParentString = "";
        try
        {
            StringBuffer query = new StringBuffer("WITH NEE(ELEMENT_ID,CHAIN, ELEMENT_LEVEL_ID) AS (SELECT  ELEMENT_ID, CAST(ELEMENT_NAME AS VARCHAR(350)) ,ELEMENT_LEVEL_ID FROM KM_ELEMENT_MSTR  WHERE ELEMENT_ID =? UNION ALL SELECT NPLUS1.ELEMENT_ID, NEE.CHAIN || '>' || NPLUS1.ELEMENT_NAME, NPLUS1.ELEMENT_LEVEL_ID FROM KM_ELEMENT_MSTR AS NPLUS1, NEE WHERE NEE.ELEMENT_ID=NPLUS1.PARENT_ID) SELECT CHAIN AS PARENT_STRING FROM NEE WHERE NEE.ELEMENT_ID=?");
            con = DBConnection.getDBConnection();
            ps = con.prepareStatement(query.append(" with ur").toString());
            ps.setInt(1, Integer.parseInt(rootId));
            ps.setInt(2, Integer.parseInt(elementId));
            rs = ps.executeQuery();
            if(rs.next())
                parentString = rs.getString("PARENT_STRING");
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            logger.info(e);
            throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            logger.info(e);
            throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }
        finally{
        try
        {
            DBConnection.releaseResources(con, ps, rs);
        }
        catch(DAOException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }
        }
        return parentString;
    }

    public List getElementLevelNames()
        throws KmException
    {
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        List list;
        int i;
        con = null;
        rs = null;
        ps = null;
        list = new ArrayList();
        i = 0;
        List list1;
        try
        {
            StringBuffer query = new StringBuffer("SELECT ELEMENT_LEVEL_NAME,ELEMENT_LEVEL_ID FROM KM_ELEMENT_LEVEL_MSTR ORDER BY ELEMENT_LEVEL_ID");
            con = DBConnection.getDBConnection();
            ps = con.prepareStatement(query.append(" with ur ").toString());
            for(rs = ps.executeQuery(); rs.next();)
            {
                list.add(i, rs.getString("ELEMENT_LEVEL_NAME"));
                i++;
            }

            logger.info((new StringBuilder("Element Level Names: ")).append(list).toString());
            list1 = list;
        }
        catch(SQLException e)
        {
	        e.printStackTrace();
            logger.info(e);
            throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
        }
        catch(Exception e)
        {
	        e.printStackTrace();
            logger.info(e);
            throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }
        finally{
        try
        {
            DBConnection.releaseResources(con, ps, rs);
        }
        catch(DAOException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }
        }
        return list1;

    }

    public String getParentId(String elementId)
        throws KmException
    {
    	logger.info("Retrieving Parent ID for circle "+elementId);
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        String parentId;
        con = null;
        rs = null;
        ps = null;
        parentId = null;
        try
        {
            StringBuffer query = new StringBuffer("SELECT PARENT_ID FROM  KM_ELEMENT_MSTR  WHERE  ELEMENT_ID = ?");
            con = DBConnection.getDBConnection();
            ps = con.prepareStatement(query.append(" with ur ").toString());
            ps.setInt(1, Integer.parseInt(elementId));
            rs = ps.executeQuery();
            if(rs.next())
                parentId = rs.getString("PARENT_ID");
            logger.info("Parent Id for Circle "+elementId+" is : "+parentId);
        }
        catch(SQLException e)
        {
	        e.printStackTrace();
            logger.info(e);
            throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
        }
        catch(Exception e)
        {
	        e.printStackTrace();
            logger.info(e);
            throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }
        finally{
        try
        {
            DBConnection.releaseResources(con, ps, rs);
        }
        catch(DAOException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }
        }
        return parentId;
    }

    public KmElementMstr getPanChild(String parentId)
        throws KmException
    {
    	logger.info("Retrieving Pan Child for "+parentId);
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        KmElementMstr dto;
        con = null;
        rs = null;
        ps = null;
        dto = null;
       
        try
        {
            StringBuffer query = new StringBuffer("SELECT * FROM KM_ELEMENT_MSTR WHERE  STATUS = 'A'  AND PAN_STATUS='Y' AND PARENT_ID= ?  ");
            con = DBConnection.getDBConnection();
            logger.info(parentId);
            ps = con.prepareStatement(query.append(" with ur ").toString());
            ps.setInt(1, Integer.parseInt(parentId));
            rs = ps.executeQuery();
           
            
            for(rs = ps.executeQuery(); rs.next(); dto.setElementId(rs.getString("ELEMENT_ID")))
            {
                dto = new KmElementMstr();
                dto.setElementName(rs.getString("ELEMENT_NAME"));
                logger.info("Pan Children :: "+dto.getElementName());
            }
           // logger.info(parentId+"============================="+dto.getElementId());
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            logger.info(e);
            throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            logger.info(e);
            throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }
       finally{
        try
        {
            DBConnection.releaseResources(con, ps, rs);
        }
        catch(DAOException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }
       }
        return dto;
    }
//added by vishwas for method correction
    public ArrayList getPanChild1(String parentId)
    throws KmException
{
    Connection con;
    ResultSet rs;
    PreparedStatement ps;
    KmElementMstr dto;
    con = null;
    rs = null;
    ps = null;
   // dto = null;
   ArrayList<KmElementMstr> al=new ArrayList<KmElementMstr>();
  
   try
    {
        StringBuffer query = new StringBuffer("SELECT ELEMENT_ID FROM KM_ELEMENT_MSTR WHERE  STATUS = 'A'  AND PAN_STATUS='Y' AND PARENT_ID= ?  ");
        con = DBConnection.getDBConnection();
        logger.info(parentId);
        ps = con.prepareStatement(query.append(" with ur ").toString());
        ps.setInt(1, Integer.parseInt(parentId));
        rs = ps.executeQuery();
        while(rs.next())
        {
        	 dto=new KmElementMstr();
        	dto.setElementId(rs.getString("ELEMENT_ID"));
            al.add(dto);
        }
        
       
        //for(rs = ps.executeQuery(); rs.next(); dto.setElementId(rs.getString("ELEMENT_ID")))
        {
           // dto = new KmElementMstr();
            //dto.setElementName(rs.getString("ELEMENT_NAME"));
        }
       // logger.info(parentId+"============================="+dto.getElementId());
    }
    catch(SQLException e)
    {
        e.printStackTrace();
        logger.info(e);
        throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
    }
    catch(Exception e)
    {
        e.printStackTrace();
        logger.info(e);
        throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
    }
   finally{
    try
    {
        DBConnection.releaseResources(con, ps, rs);
    }
    catch(DAOException e)
    {
        e.printStackTrace();
        throw new KmException(e.getMessage(), e);
    }
   }
    return al;
}


    //end by vishwas
    
    public String insertElement(KmElementMstr elementMstrDTO)
        throws KmException
    {
        Connection con;
        PreparedStatement pstmt;
        ResultSet rs;
        con = null;
        pstmt = null;
        rs = null;
        String s="";
        try
        {
            StringBuffer query = new StringBuffer("SELECT NEXTVAL FOR KM_ELEMENT_ID_SEQ FROM SYSIBM.SYSDUMMY1");
            StringBuffer query1 = new StringBuffer("INSERT INTO KM_ELEMENT_MSTR(ELEMENT_ID, ELEMENT_NAME,ELEMENT_DESC, PARENT_ID, ELEMENT_LEVEL_ID, PAN_STATUS,STATUS, CREATED_BY, CREATED_DT, UPDATED_BY, UPDATED_DT,LOBSTATUS,SEQUENCENO)VALUES(?, ?, ?, ?, ?, ?, ?, ?, CURRENT TIMESTAMP,?, CURRENT TIMESTAMP,?,1)");
            con = DBConnection.getDBConnection();
            pstmt = con.prepareStatement(query.append(" with ur").toString());
            rs = pstmt.executeQuery();
            rs.next();
            int elementId = Integer.parseInt(rs.getString(1));
            pstmt = con.prepareStatement(query1.toString());
            pstmt.setInt(1, elementId);
            pstmt.setString(2, elementMstrDTO.getElementName());
            pstmt.setString(3, elementMstrDTO.getElementDesc());

            pstmt.setInt(4, Integer.parseInt(elementMstrDTO.getParentId()));
            pstmt.setInt(5, Integer.parseInt(elementMstrDTO.getElementLevel()));
            pstmt.setString(6, elementMstrDTO.getPanStatus());
            pstmt.setString(7, elementMstrDTO.getStatus());
            pstmt.setInt(8, Integer.parseInt(elementMstrDTO.getCreatedBy()));
            pstmt.setInt(9, Integer.parseInt(elementMstrDTO.getUpdatedBy()));
            //if(elementMstrDTO.getLobStatus()!=null)
            {
            	 pstmt.setString(10, (elementMstrDTO.getLobStatus()));
            }
            pstmt.executeUpdate();
            s = (new StringBuilder(String.valueOf(elementId))).toString();
        }
        catch(KmUserMstrDaoException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }
        catch(DAOException e1)
        {
            e1.printStackTrace();

        }
        finally{
        try
        {
            DBConnection.releaseResources(con, pstmt, rs);
        }
        catch(DAOException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }
        }
        return s;


    }

    //added by vishwas for copy document and inserted in kmelement mstr table
    public void insertcopyElement(KmElementMstr elementMstrDTO,String elementid)
    throws KmException
{
    Connection con;
    PreparedStatement pstmt;
    ResultSet rs;
    con = null;
    pstmt = null;
    rs = null;
    String s="";
    try
    {
        StringBuffer query = new StringBuffer("SELECT NEXTVAL FOR KM_ELEMENT_ID_SEQ FROM SYSIBM.SYSDUMMY1");
        StringBuffer query1 = new StringBuffer("INSERT INTO KM_ELEMENT_MSTR(ELEMENT_ID, ELEMENT_NAME,ELEMENT_DESC, PARENT_ID, ELEMENT_LEVEL_ID, PAN_STATUS,STATUS, CREATED_DT, UPDATED_DT,SEQUENCENO)VALUES(?, ?, ?, ?, ?, ?, ?, CURRENT TIMESTAMP, CURRENT TIMESTAMP,1)");
        con = DBConnection.getDBConnection();
        pstmt = con.prepareStatement(query.append(" with ur").toString());
        
        logger.info("Copy in :: "+query1);
        rs = pstmt.executeQuery();
        rs.next();
        int elementId = Integer.parseInt(rs.getString(1));
        pstmt = con.prepareStatement(query1.toString());
        pstmt.setInt(1, Integer.valueOf(elementid));
        pstmt.setString(2, elementMstrDTO.getElementName());
        pstmt.setString(3, elementMstrDTO.getElementDesc());

        pstmt.setInt(4, Integer.parseInt(elementMstrDTO.getParentId()));
        pstmt.setInt(5, Integer.parseInt(elementMstrDTO.getElementLevel()));
        pstmt.setString(6, elementMstrDTO.getPanStatus());
        pstmt.setString(7, elementMstrDTO.getStatus());
       // pstmt.setInt(8, Integer.parseInt(elementMstrDTO.getCreatedBy()));
        //pstmt.setInt(9, Integer.parseInt(elementMstrDTO.getUpdatedBy()));
        //if(elementMstrDTO.getLobStatus()!=null)
        {
        	// pstmt.setString(10, (elementMstrDTO.getLobStatus()));
        }
        pstmt.executeUpdate();
       // s = (new StringBuilder(String.valueOf(elementId))).toString();
    }
    catch(KmUserMstrDaoException e)
    {
        e.printStackTrace();
        throw new KmException(e.getMessage(), e);
    }
    catch(SQLException e)
    {
        e.printStackTrace();
        throw new KmException(e.getMessage(), e);
    }
    catch(DAOException e1)
    {
        e1.printStackTrace();

    }
    finally{
    try
    {
        DBConnection.releaseResources(con, pstmt, rs);
    }
    catch(DAOException e)
    {
        e.printStackTrace();
        throw new KmException(e.getMessage(), e);
    }
    }
   // return s;


}

    //end by vishwas for copy document
    
    public ArrayList getAllDocuments(String parentId)
        throws KmException
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        ArrayList documentList;
        con = null;
        ps = null;
        rs = null;
        documentList = new ArrayList();
        KmElementMstr elementDto = new KmElementMstr();
        String element_Id = null;
        try
        {
            StringBuffer query = new StringBuffer("SELECT B.ELEMENT_ID, B.ELEMENT_NAME,C.DOCUMENT_NAME,C.DOCUMENT_ID  FROM  KM_ELEMENT_MSTR B inner join KM_DOCUMENT_MSTR C on B.ELEMENT_ID = C.ELEMENT_ID WHERE B.PARENT_ID=?    AND B.ELEMENT_LEVEL_ID = 0 AND C.STATUS = 'A'");
            con = DBConnection.getDBConnection();
            ps = con.prepareStatement(query.append(" with ur").toString());
            ps.setInt(1, Integer.parseInt(parentId));
        //    KmElementMstr elementDto;
            for(rs = ps.executeQuery(); rs.next(); documentList.add(elementDto))
            {
                elementDto = new KmElementMstr();
                rs.getString("ELEMENT_ID");
                elementDto.setElementId(rs.getString("ELEMENT_ID"));
                elementDto.setElementName(rs.getString("ELEMENT_NAME"));
                elementDto.setDocumentId(rs.getString("DOCUMENT_ID"));
            }

        }
        catch(SQLException e)
        {
            e.printStackTrace();
            throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            logger.error((new StringBuilder("Exception occured while find.Exception Message: ")).append(e.getMessage()).toString());
            throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }
        finally{
        try
        {
            DBConnection.releaseResources(con, ps, rs);
        }
        catch(DAOException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }
        }
        return documentList;
    }

    public ArrayList getDocList(String parentId)    throws KmException
{
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    ArrayList documentList;
    con = null;
    ps = null;
    rs = null;
    documentList = new ArrayList();
    KmElementMstr elementDto = new KmElementMstr();
    String element_Id = null;
    try
    {
        StringBuffer query = new StringBuffer("SELECT B.ELEMENT_ID,  C.DOC_NAME,C.DOCUMENT_DISPLAY_NAME,C.DOCUMENT_ID  FROM  KM_ELEMENT_MSTR B inner join KM_DOCUMENT_MSTR C on B.ELEMENT_ID = C.ELEMENT_ID WHERE B.PARENT_ID=? AND B.ELEMENT_LEVEL_ID = 0 AND C.STATUS = 'A'");
        con = DBConnection.getDBConnection();
        ps = con.prepareStatement(query.append(" with ur").toString());
        ps.setInt(1, Integer.parseInt(parentId));
    //    KmElementMstr elementDto;
        for(rs = ps.executeQuery(); rs.next(); documentList.add(elementDto))
        {
            elementDto = new KmElementMstr();
           // rs.getString("ELEMENT_ID");
            elementDto.setElementId(rs.getString("ELEMENT_ID"));
            elementDto.setElementName(rs.getString("DOC_NAME"));
            elementDto.setDocumentName(rs.getString("DOCUMENT_DISPLAY_NAME"));
            elementDto.setDocumentId(rs.getString("DOCUMENT_ID"));
        }

    }
    catch(SQLException e)
    {
        e.printStackTrace();
        throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
    }
    catch(Exception e)
    {
        e.printStackTrace();
        logger.error((new StringBuilder("Exception occured while find.Exception Message: ")).append(e.getMessage()).toString());
        throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
    }
    finally{
    try
    {
        DBConnection.releaseResources(con, ps, rs);
    }
    catch(DAOException e)
    {
        e.printStackTrace();
        throw new KmException(e.getMessage(), e);
    }
    }
    return documentList;
}
//added by vishwas for copy document
    
    public boolean insertCopyElements(String movedDocumentList[], String parentId,String elementid)
    throws KmException
{
    Connection con;
    PreparedStatement pstmt;
    PreparedStatement pstmt2;
    ResultSet rs;
    ResultSet rs2=null;
    boolean success;
    con = null;
    pstmt = null;
    pstmt2 = null;
    rs = null;
    success = false;
    KmElementMstr dto = new KmElementMstr();
    try
    {
       // StringBuffer query = new StringBuffer("UPDATE KM_ELEMENT_MSTR SET PARENT_ID = ? WHERE ELEMENT_ID = ?");
        StringBuffer query = new StringBuffer("select * from  KM_ELEMENT_MSTR WHERE ELEMENT_ID = ?");
        con = DBConnection.getDBConnection();
        pstmt = con.prepareStatement(query.append(" with ur").toString());
        logger.info("element update"+movedDocumentList.length);
       
        for(int i = 0; i < movedDocumentList.length; i++)
        {
        	System.out.println();
        	 logger.info(Integer.parseInt(movedDocumentList[i])+"element parentId document in copy"+parentId);
        	//pstmt.setInt(1, Integer.parseInt(parentId));
            pstmt.setInt(1, Integer.parseInt(movedDocumentList[i]));
            rs = pstmt.executeQuery();
            while(rs.next())
            {
            dto = new KmElementMstr();
           
         
            
            dto.setElementName(rs.getString("ELEMENT_NAME"));
           // dto.setElementId(String.valueOf(elementId));
            dto.setElementLevel(rs.getString("ELEMENT_LEVEL_ID"));
            dto.setElementDesc(rs.getString("ELEMENT_DESC"));
            dto.setParentId(parentId);
            dto.setPanStatus(rs.getString("PAN_STATUS"));
            dto.setStatus(rs.getString("STATUS"));
            insertcopyElement(dto,elementid);
            elementid=elementid+i;
            logger.info("element parentId in copy"+parentId);
            logger.info("element movedDocumentList[i]"+movedDocumentList[i]);
            } //pstmt.addBatch();
        }

       // pstmt.executeBatch();
        con.commit();
        success = true;
    }
    catch(SQLException e)
    {
        e.printStackTrace();

    }
    catch(DAOException e)
    {
        e.printStackTrace();

    }
    catch(Exception e)
    {
        e.printStackTrace();
       // throw new KmException(e.getMessage(), e);
    }


  finally{
    try
    {
        DBConnection.releaseResources(con, pstmt, rs);
    }
    catch(DAOException e)
    {
        e.printStackTrace();
        throw new KmException(e.getMessage(), e);
    }
  }
    return success;
}


    
//end by vishwas for copy document    

    public boolean moveElements(String movedDocumentList[], String parentId)
        throws KmException
    {
        Connection con;
        PreparedStatement pstmt;
        ResultSet rs;
        boolean success;
        con = null;
        pstmt = null;
        rs = null;
        success = false;
        try
        {
            StringBuffer query = new StringBuffer("UPDATE KM_ELEMENT_MSTR SET PARENT_ID = ? WHERE ELEMENT_ID = ?");
            con = DBConnection.getDBConnection();
            pstmt = con.prepareStatement(query.append(" with ur").toString());
            logger.info("element update"+movedDocumentList.length);
            for(int i = 0; i < movedDocumentList.length; i++)
            {
                pstmt.setInt(1, Integer.parseInt(parentId));
                pstmt.setInt(2, Integer.parseInt(movedDocumentList[i]));
              
                logger.info("element parentId"+parentId);
                logger.info("element movedDocumentList[i]"+movedDocumentList[i]);
                pstmt.addBatch();
            }

            pstmt.executeBatch();
            con.commit();
            success = true;
        }
        catch(SQLException e)
        {
            e.printStackTrace();

        }
        catch(DAOException e)
        {
            e.printStackTrace();

        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }


      finally{
        try
        {
            DBConnection.releaseResources(con, pstmt, rs);
        }
        catch(DAOException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }
      }
        return success;
    }

    public String[] checkExistingElement(String elementName, String parentId)
        throws KmException
    {
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        con = null;
        rs = null;
        ps = null;
        String[] str = new String[2];
        str[0]="false";
        try
        {
            StringBuffer query = new StringBuffer("SELECT * FROM KM_ELEMENT_MSTR WHERE UPPER(ELEMENT_NAME) = UPPER(?) AND PARENT_ID = ? AND ELEMENT_LEVEL_ID!=0 AND STATUS='A'");
            con = DBConnection.getDBConnection();
            ps = con.prepareStatement(query.append(" with ur ").toString());
            ps.setString(1, elementName);
            ps.setInt(2, Integer.parseInt(parentId));
            rs = ps.executeQuery();
            if(rs.next())
            {
            	int elemId = rs.getInt("ELEMENT_ID");
            	str[0] = "true";
            	str[1] = elemId + "";
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }
        finally{
        try
        {
            DBConnection.releaseResources(con, ps, rs);
        }
        catch(DAOException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }
        }
        return str;
    }

    public String getElementName(String elementId)
        throws KmException
    {
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        String elementName;
        con = null;
        rs = null;
        ps = null;
        elementName = "";
        String s;
        try
        {
            StringBuffer query = new StringBuffer("SELECT ELEMENT_NAME FROM KM_ELEMENT_MSTR WHERE ELEMENT_ID = ?  ");
            con = DBConnection.getDBConnection();
            ps = con.prepareStatement(query.append(" with ur ").toString());
            ps.setInt(1, Integer.parseInt(elementId));
            for(rs = ps.executeQuery(); rs.next();)
                elementName = rs.getString("ELEMENT_NAME");

            s = elementName;
        }
        catch(Exception e)
        {
	        e.printStackTrace();
                throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }finally{
        try
        {
            DBConnection.releaseResources(con, ps, rs);
        }
        catch(DAOException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }}
        return s;

    }

    public ArrayList getAllLevelChildren(String parentId)
        throws KmException
    {
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        ArrayList elementList;
        con = null;
        rs = null;
        ps = null;
        elementList = new ArrayList();
        try
        {
            StringBuffer query = new StringBuffer("WITH NEE(ELEMENT_ID) AS (SELECT  ELEMENT_ID FROM KM_ELEMENT_MSTR WHERE ELEMENT_ID = ? UNION ALL SELECT NPLUS1.ELEMENT_ID FROM KM_ELEMENT_MSTR AS NPLUS1, NEE WHERE NEE.ELEMENT_ID=NPLUS1.PARENT_ID) SELECT NEE.ELEMENT_ID, ELE.ELEMENT_NAME, ELE.ELEMENT_LEVEL_ID FROM KM_ELEMENT_MSTR ELE inner join NEE on ELE.ELEMENT_ID=NEE.ELEMENT_ID WHERE  ELE.ELEMENT_LEVEL_ID!=0 AND ELE.STATUS='A' ");
            con = DBConnection.getDBConnection();
            logger.info(parentId);
            ps = con.prepareStatement(query.append(" with ur").toString());
            KmElementMstr dto = new KmElementMstr();
            ps.setInt(1, Integer.parseInt(parentId));
            for(rs = ps.executeQuery(); rs.next(); logger.info((new StringBuilder("Element: ")).append(elementList).toString()))
            {
                dto = new KmElementMstr();
                dto.setElementName(rs.getString("ELEMENT_NAME"));
                dto.setElementId(rs.getString("ELEMENT_ID"));
                dto.setElementLevel(rs.getString("ELEMENT_LEVEL_ID"));
                elementList.add(dto);
            }

            logger.info((new StringBuilder("List is returned :")).append(elementList.size()).toString());
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            logger.info(e);
            throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            logger.info(e);
            throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }
        finally{
        try
        {
            DBConnection.releaseResources(con, ps, rs);
        }
        catch(DAOException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }
        }
        return elementList;
    }

    public void updateLevel(int elementId, int newLevel) throws KmException {
        Connection con;
        PreparedStatement pstmt;
        ResultSet rs;
        con = null;
        pstmt = null;
        rs = null;
        try
        {
            con = DBConnection.getDBConnection();
            StringBuffer query = new StringBuffer("UPDATE KM_ELEMENT_MSTR ELE SET ELE.ELEMENT_LEVEL_ID=? WHERE ELE.ELEMENT_ID=? ");
            pstmt = con.prepareStatement(query.append(" with ur").toString());
            pstmt.setInt(1, newLevel);
            pstmt.setInt(2, elementId);
            pstmt.executeUpdate();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }
        catch(DAOException e1)
        {
            e1.printStackTrace();

        }
        catch(Exception e)
        {
            e.printStackTrace();

        }

      finally{

        try
        {
            DBConnection.releaseResources(con, pstmt, rs);
        }
        catch(DAOException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }
      }

    }

    public void updateElementLevel(ArrayList childrenList, int levelDiff)
        throws KmException
    {
        Connection con;
        PreparedStatement pstmt;
        ResultSet rs;
        con = null;
        pstmt = null;
        rs = null;
        try
        {
            con = DBConnection.getDBConnection();
            StringBuffer query = new StringBuffer("UPDATE KM_ELEMENT_MSTR ELE SET ELE.ELEMENT_LEVEL_ID=ELE.ELEMENT_LEVEL_ID+? WHERE status ='A' and ( ELE.ELEMENT_ID=? ");
            Iterator i = childrenList.iterator();
            i.next();
            for(; i.hasNext(); i.next())
                query.append(" OR ELE.ELEMENT_ID=? ").toString();

            query.append(" )").toString();
            pstmt = con.prepareStatement(query.append(" with ur").toString());
            pstmt.setInt(1, levelDiff);
            int count = 1;
            for(Iterator iterator = childrenList.iterator(); iterator.hasNext(); pstmt.setInt(++count, Integer.parseInt(((KmElementMstr)iterator.next()).getElementId())));
            pstmt.executeUpdate();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }
        catch(DAOException e1)
        {
            e1.printStackTrace();

        }
        catch(Exception e)
        {
            e.printStackTrace();

        }

      finally{

        try
        {
            DBConnection.releaseResources(con, pstmt, rs);
        }
        catch(DAOException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }
      }
    }

    public String[] getChildrenIds(String elementId)
        throws KmException
    {
        Connection con;
        PreparedStatement pstmt;
        ResultSet rs;
        ArrayList list;
        con = null;
        pstmt = null;
        rs = null;
        list = new ArrayList();
        String as[];
        try
        {
            con = DBConnection.getDBConnection();
            StringBuffer query = new StringBuffer("WITH NEE(ELEMENT_ID) AS (SELECT  ELEMENT_ID FROM KM_ELEMENT_MSTR WHERE ELEMENT_ID = ? UNION ALL SELECT NPLUS1.ELEMENT_ID FROM KM_ELEMENT_MSTR AS NPLUS1, NEE WHERE NEE.ELEMENT_ID=NPLUS1.PARENT_ID) SELECT ele.ELEMENT_ID FROM NEE, KM_ELEMENT_MSTR ele where NEE.ELEMENT_ID=ele.ELEMENT_ID AND ele.STATUS='A' ");
            pstmt = con.prepareStatement(query.append("  with ur ").toString());
            pstmt.setString(1, elementId);
            rs = pstmt.executeQuery();
            int i = 0;
            for(; rs.next(); list.add(rs.getString("ELEMENT_ID")));
            String categoryID[] = (String[])list.toArray(new String[list.size()]);
            as = categoryID;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }
        catch(DAOException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }finally{
        try
        {
            DBConnection.releaseResources(con, pstmt, rs);
        }
        catch(DAOException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }
        }
        return as;

    }


    public ArrayList getChildrenIds(String parentId, String elementLevelId)
        throws KmException
    {
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        ArrayList elementIdList =new ArrayList();
        con = null;
        rs = null;
        ps = null;

        try
        {
            StringBuffer query = new StringBuffer("SELECT ELEMENT_ID FROM KM_ELEMENT_MSTR WHERE  STATUS = 'A' AND PARENT_ID=? AND ELEMENT_LEVEL_ID != 0 AND PAN_STATUS != 'Y'");
            query.append(" AND ELEMENT_LEVEL_ID=?").toString();
            con = DBConnection.getDBConnection();
            logger.info(parentId);
            ps = con.prepareStatement(query.append(" with ur ").toString());
            KmElementMstr dto = new KmElementMstr();
            ps.setInt(1, Integer.parseInt(parentId));
            ps.setInt(2, Integer.parseInt(elementLevelId));
            for(rs = ps.executeQuery(); rs.next(); elementIdList.add(rs.getString("ELEMENT_ID")))


            logger.info((new StringBuilder("List is returned :")).append(elementIdList.size()).toString());
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            logger.info(e);
            throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            logger.info(e);
            throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }
        finally{

        try
        {
            DBConnection.releaseResources(con, ps, rs);
        }
        catch(DAOException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }
        }
        return elementIdList;
    }

    public void editElement(KmElementMstr dto)
        throws KmException
    {
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        con = null;
        rs = null;
        ps = null;
        try
        {
            StringBuffer query = new StringBuffer("UPDATE KM_ELEMENT_MSTR SET ELEMENT_NAME = ? , ELEMENT_DESC = ? WHERE ELEMENT_ID = ?");
            con = DBConnection.getDBConnection();
            ps = con.prepareStatement(query.append(" with ur").toString());
            ps.setString(1, dto.getElementName());
            ps.setString(2, dto.getElementDesc());
            ps.setInt(3, Integer.parseInt(dto.getElementId()));
            int rows = ps.executeUpdate();
            logger.info("Element Details Updated Successfully");
        }
        catch(SQLException e)
        {
            logger.info(e);
            e.printStackTrace();
            throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
        }
        catch(Exception e)
        {
            logger.info((new StringBuilder("Exception : ")).append(e).toString());
            e.printStackTrace();
            throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }
       finally{
        try
        {
            DBConnection.releaseResources(con, ps, rs);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
       }

    }

    public KmElementMstr getElemetDto(String elementId)
        throws KmException
    {
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        KmElementMstr dto;
        con = null;
        rs = null;
        ArrayList fileList = new ArrayList();
        ps = null;
        dto = null;
        try
        {
            StringBuffer query = new StringBuffer("WITH nee(element_id,chain) AS ( SELECT  ELEMENT_ID,  cast(element_name as VARCHAR(" + PropertyReader.getAppValue("search.path.size") + "))  FROM KM_ELEMENT_MSTR WHERE element_id = ?  UNION ALL \tSELECT nplus1.element_id,nee.chain || '/' || nplus1.element_name  FROM KM_ELEMENT_MSTR as nplus1, nee  WHERE nee.element_id=nplus1.PARENT_ID) SELECT CHAIN ,ele.ELEMENT_ID, ele.ELEMENT_NAME,ele.ELEMENT_NAME,ele.ELEMENT_DESC FROM NEE inner join KM_ELEMENT_MSTR ele on ele.ELEMENT_ID = NEE.ELEMENT_ID WHERE   ele.ELEMENT_ID = ?");
            con = DBConnection.getDBConnection();
            ps = con.prepareStatement(query.append(" with ur").toString());
            ps.setInt(1, Integer.parseInt("1"));
            ps.setInt(2, Integer.parseInt(elementId));
            for(rs = ps.executeQuery(); rs.next(); dto.setElementDesc(rs.getString("ELEMENT_DESC")))
            {
                dto = new KmElementMstr();
                logger.info(rs.getString("chain"));
                String path = rs.getString("chain");
                logger.info(path);
                String documentStringPath = "";
                if(path.indexOf("/") + 1 < path.lastIndexOf("/"))
                    documentStringPath = path.substring(path.indexOf("/") + 1, path.lastIndexOf("/"));
                dto.setElementStringPath(documentStringPath);
                dto.setElementId(rs.getString("ELEMENT_ID"));
                dto.setElementName(rs.getString("ELEMENT_NAME"));
            }

        }
        catch(SQLException e)
        {
            logger.info(e);
            e.printStackTrace();
            throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
        }
        catch(Exception e)
        {
            logger.info((new StringBuilder("Exception : ")).append(e).toString());
            e.printStackTrace();
            throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }
      finally{
        try
        {
            DBConnection.releaseResources(con, ps, rs);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }}
        return dto;
    }

    public String[] getElements(String elementId)
        throws KmException
    {
        Connection con;
        PreparedStatement pstmt;
        ResultSet rs;
        ArrayList list;
        con = null;
        pstmt = null;
        rs = null;
        list = new ArrayList();
        String as[];
        try
        {
            StringBuffer query = new StringBuffer("WITH NEE(ELEMENT_ID) AS (SELECT  ELEMENT_ID FROM KM_ELEMENT_MSTR WHERE ELEMENT_ID = ? UNION ALL SELECT NPLUS1.ELEMENT_ID FROM KM_ELEMENT_MSTR AS NPLUS1, NEE WHERE NEE.ELEMENT_ID=NPLUS1.PARENT_ID) SELECT nee.ELEMENT_ID FROM NEE, KM_ELEMENT_MSTR ele where NEE.ELEMENT_ID=ele.ELEMENT_ID AND ele.ELEMENT_LEVEL_ID!=0 and ele.STATUS='A'");
            con = DBConnection.getDBConnection();
            pstmt = con.prepareStatement(query.append("  with ur ").toString());
            pstmt.setString(1, elementId);
            rs = pstmt.executeQuery();
            int i = 0;
            for(; rs.next(); list.add(rs.getString("ELEMENT_ID")));
            String elements[] = (String[])list.toArray(new String[list.size()]);
            as = elements;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }
        catch(DAOException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }finally{
        try
        {
            DBConnection.releaseResources(con, pstmt, rs);
        }
        catch(DAOException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }
        }
        return as;


    }

    public String[] getElements(String elementIds[])
        throws KmException
    {
        Connection con;
        PreparedStatement pstmt;
        ResultSet rs;
        ArrayList list;
        con = null;
        pstmt = null;
        rs = null;
        list = new ArrayList();
        String as[];
        try
        {
            String query = "WITH NEE(ELEMENT_ID) AS (SELECT  ELEMENT_ID FROM KM_ELEMENT_MSTR WHERE ELEMENT_ID = ? UNION ALL SELECT NPLUS1.ELEMENT_ID FROM KM_ELEMENT_MSTR AS NPLUS1, NEE WHERE NEE.ELEMENT_ID=NPLUS1.PARENT_ID)  SELECT nee.ELEMENT_ID FROM NEE, KM_ELEMENT_MSTR ele where NEE.ELEMENT_ID=ele.ELEMENT_ID AND ele.ELEMENT_LEVEL_ID=0 and ele.STATUS='A' with ur ";
            con = DBConnection.getDBConnection();
            for(int i = 0; i < elementIds.length; i++)
            {
                pstmt = con.prepareStatement(query);
                pstmt.setString(1, elementIds[i]);
                for(rs = pstmt.executeQuery(); rs.next(); list.add(rs.getString("ELEMENT_ID")));
            }

            String elements[] = (String[])list.toArray(new String[list.size()]);
            logger.info((new StringBuilder("No. of documents : ")).append(elements.length).toString());
            as = elements;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }
        catch(DAOException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }finally{
        try
        {
            DBConnection.releaseResources(con, pstmt, rs);
        }
        catch(DAOException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }
        }
        return as;



    }

//added by vishwas
	
	public String[] DisplayName(String[] movedDocumentList) throws KmException {
		Connection con=null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		//String sql = SQL_GET_DOCUMENT_IDS;
		StringBuffer query =new StringBuffer(SQL_GET_DOCUMENT_IDS_DISPLAY);
		ArrayList docList = new ArrayList();
		for(int i=1;i<movedDocumentList.length;i++){
				query.append(" OR ELEMENT_ID=? ").toString();		
			}
			query.append(" ) ").toString();
		try {
				con = DBConnection.getDBConnection();
				ps = con.prepareStatement(query.append(" with ur ").toString());
				for(int i=0;i<movedDocumentList.length;i++){
					ps.setInt(i+1, Integer.parseInt(movedDocumentList[i]));	
				}		
				rs = ps.executeQuery();
				
				while(rs.next()){
					docList.add(rs.getString("DOCUMENT_DISPLAY_NAME"));
				//	documentId[i]=rs.getString("DOCUMENT_ID");
					
				}
				String documentId [] = (String[]) docList.toArray(new String [docList.size()]);
				//String[] documentId=new String[movedDocumentList.length];
				return documentId;
			
		} 
		catch (Exception e) {
			logger.error(e);
	//	logger.severe("Exception occured while find." + "Exception Message: " + e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try{
				DBConnection.releaseResources(con,ps,rs);
			}catch(DAOException e){
				logger.error(e);
				throw new KmException(e.getMessage(),e);
			}				
		}	
			
	}
	
	
	//end by vishwas

    
    public void deleteElements(String elements[], String updatedBy)
        throws KmException
    {
        Connection con;
        PreparedStatement pstmt;
        ResultSet rs;
        StringBuffer query;
        con = null;
        pstmt = null;
        rs = null;
        query = new StringBuffer("UPDATE KM_ELEMENT_MSTR SET STATUS= 'D', UPDATED_DT= current timestamp, UPDATED_BY = ? WHERE STATUS='A' and (ELEMENT_ID = ? ");
        for(int i = 1; i < elements.length; i++)
            query.append(" OR ELEMENT_ID = ? ").toString();

        query.append(" ) ").toString();
        try
        {
            con = DBConnection.getDBConnection();
            pstmt = con.prepareStatement(query.append(" with ur").toString());
            pstmt.setString(1, updatedBy);
            for(int i = 0; i < elements.length; i++)
                pstmt.setInt(i + 2, Integer.parseInt(elements[i]));

            int documentsUpdated = pstmt.executeUpdate();
            logger.info((new StringBuilder("No.Of Elements Updated :")).append(documentsUpdated).toString());
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }
        catch(DAOException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }
      finally{
        try
        {
            DBConnection.releaseResources(con, pstmt, rs);
        }
        catch(DAOException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }

      }
    }

    public String getElementId(String documentId)
        throws KmException
    {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "UPDATE KM_ELEMENT_MSTR SET STATUS= 'D', UPDATED_DT= current timestamp, UPDATED_BY = ? WHERE STATUS='A' and (ELEMENT_ID = ? ";
        return null;
    }

    public String getCircleId(String elementId)
        throws KmException
    {
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        String parentString;
        con = null;
        rs = null;
        ps = null;
        parentString = null;
        String trimmedParentString = "";
        String s;
        try
        {
            StringBuffer query = new StringBuffer("WITH NEE(ELEMENT_ID,CHAIN, ELEMENT_LEVEL_ID) AS (SELECT  ELEMENT_ID, CAST(CAST(ELEMENT_ID AS CHARACTER(5))AS VARCHAR(60)) ,ELEMENT_LEVEL_ID FROM KM_ELEMENT_MSTR  WHERE ELEMENT_ID =1  UNION ALL  SELECT NPLUS1.ELEMENT_ID, NEE.CHAIN || '/' || CAST(NPLUS1.ELEMENT_ID AS CHARACTER(5)), NPLUS1.ELEMENT_LEVEL_ID FROM KM_ELEMENT_MSTR AS NPLUS1, NEE WHERE NEE.ELEMENT_ID=NPLUS1.PARENT_ID) SELECT CHAIN AS PARENT_STRING FROM NEE WHERE NEE.ELEMENT_ID=? ");
            con = DBConnection.getDBConnection();
            ps = con.prepareStatement(query.append(" with ur").toString());
            ps.setInt(1, Integer.parseInt(elementId));
            rs = ps.executeQuery();
            if(rs.next())
                parentString = rs.getString("PARENT_STRING");
            s = parentString.substring(12, 17).trim();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            logger.info(e);
            throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }finally{
        try
        {
            DBConnection.releaseResources(con, ps, rs);
        }
        catch(DAOException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }}
        return s;


    }

    public String getPanCircleId()
        throws KmException
    {
        Connection con;
        PreparedStatement pstmt;
        ResultSet rs;
        String elementId;
        con = null;
        pstmt = null;
        rs = null;
        elementId = null;
        String s;
        try
        {
            StringBuffer query = new StringBuffer("SELECT * FROM KM_ELEMENT_MSTR WHERE PAN_STATUS='Y'");
            con = DBConnection.getDBConnection();
            pstmt = con.prepareStatement(query.append(" with ur ").toString());
            rs = pstmt.executeQuery();
            if(rs.next())
                elementId = rs.getString("ELEMENT_ID");
            s = elementId;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }
        catch(DAOException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }finally{
        try
        {
            DBConnection.releaseResources(con, pstmt, rs);
        }
        catch(DAOException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }}
        return s;


    }

    public String[] getDocs(String movedElementList[])
        throws KmException
    {
        Connection con;
        PreparedStatement pstmt;
        ResultSet rs;
        StringBuffer query;
        ArrayList list;
        con = null;
        pstmt = null;
        rs = null;
        query = new StringBuffer("SELECT doc.DOCUMENT_ID AS DOC_ID FROM KM_ELEMENT_MSTR ele ,  KM_DOCUMENT_MSTR doc WHERE ele.ELEMENT_LEVEL_ID = 0 AND ele.ELEMENT_ID=doc.ELEMENT_ID AND( ele.ELEMENT_ID = ? ");
        for(int i = 1; i < movedElementList.length; i++)
            query.append(" OR ele.ELEMENT_ID = ? ").toString();

        query.append(" ) ").toString();
        list = new ArrayList();
        String as[];
        try
        {
            con = DBConnection.getDBConnection();
            pstmt = con.prepareStatement(query.append(" with ur ").toString());
            int i;
            for(i = 0; i < movedElementList.length; i++)
                pstmt.setInt(i + 1, Integer.parseInt(movedElementList[i]));

            rs = pstmt.executeQuery();
            i = 0;
            for(; rs.next(); list.add(rs.getString("DOC_ID")));
            String elements[] = (String[])list.toArray(new String[list.size()]);
            as = elements;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }
        catch(DAOException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }finally{
        try
        {
            DBConnection.releaseResources(con, pstmt, rs);
        }
        catch(DAOException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }}
        return as;



    }

    public ArrayList getAllChildrenRec(String parentId, String elementLevel)
        throws KmException
    {
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        con = null;
        rs = null;
        ps = null;
        ArrayList arraylist;
        try
        {
            ArrayList elementList = new ArrayList();
            StringBuffer query = new StringBuffer(" WITH NEE(ELEMENT_ID) AS  (SELECT  ELEMENT_ID   FROM KM_ELEMENT_MSTR  WHERE ELEMENT_ID = ?  UNION ALL  SELECT NPLUS1.ELEMENT_ID   FROM KM_ELEMENT_MSTR AS NPLUS1, NEE  WHERE NEE.ELEMENT_ID=NPLUS1.PARENT_ID)  SELECT NEE.ELEMENT_ID, ELE.ELEMENT_NAME, ELE.ELEMENT_LEVEL_ID,ele.parent_id ,ele1.element_name AS PARENT_NAME  FROM KM_ELEMENT_MSTR ELE inner join NEE  on ELE.ELEMENT_ID=NEE.ELEMENT_ID inner join KM_ELEMENT_MSTR ele1 on ele1.element_id=ele.parent_id  WHERE   ELE.ELEMENT_LEVEL_ID=? AND ELE.STATUS='A' ");
            KmElementMstr dto = new KmElementMstr();
            con = DBConnection.getDBConnection();
            ps = con.prepareStatement(query.append(" with ur").toString());
            ps.setString(1, parentId);
            ps.setInt(2, Integer.parseInt(elementLevel));
            for(rs = ps.executeQuery(); rs.next(); elementList.add(dto))
            {
                dto = new KmElementMstr();
                dto.setElementName((new StringBuilder(String.valueOf(rs.getString("PARENT_NAME")))).append(":").append(rs.getString("ELEMENT_NAME")).toString());
                dto.setElementId(rs.getString("ELEMENT_ID"));
                dto.setElementLevel(rs.getString("ELEMENT_LEVEL_ID"));
                dto.setParentName(rs.getString("PARENT_NAME"));
            }

            logger.info("Element List is Returned");
            arraylist = elementList;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }finally{
        try
        {
            DBConnection.releaseResources(con, ps, rs);
        }
        catch(DAOException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }}
        return arraylist;



    }

    public LinkedHashMap getCategoryMapElements(String elementId, String favCategoryId)
    throws KmException
{
    	logger.info("getting CATEGORY Mab");
Connection con;
ResultSet rs;
PreparedStatement ps;
LinkedHashMap categoryMap;
LinkedHashMap categoryMap1;
LinkedHashMap newCategoryMap;
ArrayList subCategoryList;
int parentLevel;
int childLevel;
con = null;
rs = null;
ps = null;
categoryMap = new LinkedHashMap();
categoryMap1 = new LinkedHashMap();
newCategoryMap = new LinkedHashMap();
subCategoryList = new ArrayList();
parentLevel = 4;
childLevel = 5;
try
{
	 logger.info(" Inside getCategoryMapElements" + elementId);
	   
    con = DBConnection.getDBConnection();
    // old query 
   // ps = con.prepareStatement("WITH nee(element_id,element_name,status,element_level_id,parent_id) AS ( SELECT  ELEMENT_ID, element_name ,status, element_level_id, parent_id  FROM KM_ELEMENT_MSTR WHERE element_id =? UNION ALL  SELECT nplus1.element_id, nplus1.element_name,nplus1.status ,nplus1.element_level_id, nplus1.parent_id FROM KM_ELEMENT_MSTR as nplus1, nee  WHERE nee.element_id=nplus1.PARENT_ID and nplus1.status='A')  SELECT A.parent_id as CATEGORY_ID, B.ELEMENT_NAME AS CATEGORY_NAME, A.element_id AS SUB_CATEGORY_ID ,A.element_name  AS SUB_CATEGORY_NAME, a.element_level_id ELEMENT_LEVEL_ID,b.element_level_id  AS  PARENT_LEVEL_ID FROM  nee A inner join km_element_mstr B on a.parent_id = b.element_id left join KM_DOCUMENT_MSTR doc on doc.element_id = a.element_id  where    a.element_level_id in(?,?,?) and B.status='A'   and ((doc.approval_status = 'A'  and  current date between date(doc.PUBLISHING_START_DT) and date(doc.PUBLISHING_END_DT) and doc.status = 'A')or doc.approval_status is null)  order by parent_level_id,a.parent_id, lower(a.element_name) asc WITH UR");
   //new query added sequence no
     ps = con.prepareStatement("WITH nee(element_id,element_name,status,element_level_id,parent_id,sequenceno) AS ( SELECT  ELEMENT_ID, element_name ,status, element_level_id, parent_id,SEQUENCENO FROM KM_ELEMENT_MSTR WHERE element_id =? UNION ALL  SELECT nplus1.element_id, nplus1.element_name,nplus1.status ,nplus1.element_level_id, nplus1.parent_id,nplus1.SEQUENCENO FROM KM_ELEMENT_MSTR as nplus1, nee  WHERE nee.element_id=nplus1.PARENT_ID and nplus1.status='A')  SELECT A.parent_id as CATEGORY_ID,B.ELEMENT_NAME AS CATEGORY_NAME, A.element_id AS SUB_CATEGORY_ID ,A.element_name  AS SUB_CATEGORY_NAME, a.element_level_id ELEMENT_LEVEL_ID,b.element_level_id  AS  PARENT_LEVEL_ID,a.SEQUENCENO as seqno FROM  nee A inner join km_element_mstr B on a.parent_id = b.element_id left join KM_DOCUMENT_MSTR doc on doc.element_id = a.element_id  where    a.element_level_id in(?,?,?) and B.status='A'   and ((doc.approval_status = 'A'  and  current date between date(doc.PUBLISHING_START_DT) and  date(doc.PUBLISHING_END_DT) and doc.status = 'A')or doc.approval_status is null)  order by a.sequenceno,parent_level_id,a.parent_id,lower(a.element_name) asc WITH UR");
    ps.setInt(1, Integer.parseInt(elementId));
    ps.setInt(2, childLevel);
    ps.setInt(3, parentLevel);
    ps.setInt(4, 0);
    //logger.info(parentLevel+"wueryyyyyyyyyyy"+Integer.parseInt(elementId)+":"+childLevel);
    rs = ps.executeQuery();
    String oldCategoryName = null;
    String oldCategoryId = null;
    int elementLevelId = 0;
    int parentLevelId = 0;
    int i = 0;
    while(rs.next())
    {
        elementLevelId = rs.getInt("ELEMENT_LEVEL_ID");
        parentLevelId = rs.getInt("PARENT_LEVEL_ID");
        if(elementLevelId == parentLevel)
        {
        	categoryMap.put(new MyLabelValueBean(rs.getString("SUB_CATEGORY_NAME"), rs.getString("SUB_CATEGORY_ID")), new ArrayList());
        	categoryMap1.put(new MyLabelValueBean(rs.getString("SUB_CATEGORY_NAME"), rs.getString("SUB_CATEGORY_ID")), new ArrayList());
        }
        else
        if((elementLevelId == childLevel || elementLevelId == 0) && parentLevelId < 5)
            if(i == 0)
            {
            	//logger.info("CATEGORY_NAME"+rs.getString("CATEGORY_NAME"));
            	oldCategoryName = rs.getString("CATEGORY_NAME");
                oldCategoryId = rs.getString("CATEGORY_ID");
                subCategoryList.add(new LabelValueBean(rs.getString("SUB_CATEGORY_NAME"), rs.getString("SUB_CATEGORY_ID")));
                i++;
            } else
            if(i > 0)
            {
                if(!oldCategoryName.equals(rs.getString("CATEGORY_NAME")))
                {
                       categoryMap.put(new MyLabelValueBean(oldCategoryName, oldCategoryId), subCategoryList);
                    if(parentLevelId < childLevel)
                    {
                        oldCategoryName = rs.getString("CATEGORY_NAME");
                        oldCategoryId = rs.getString("CATEGORY_ID");
                        subCategoryList = new ArrayList();
                    }
                }
                subCategoryList.add(new LabelValueBean(rs.getString("SUB_CATEGORY_NAME"), rs.getString("SUB_CATEGORY_ID")));
            }
        if(i > 0)
            categoryMap.put(new MyLabelValueBean(oldCategoryName, oldCategoryId), subCategoryList);
    }
}
catch(SQLException e)
{
    e.printStackTrace();
    throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
}
catch(Exception e)
{
    e.printStackTrace();
    throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
}
 finally {
			try {
				DBConnection.releaseResources(con, ps, rs);
			} catch (Exception e) {
				e.printStackTrace();
			  throw new KmException(e.getMessage(), e);
			}
}
//System.out.println("elementId" +elementId+"****************childLevel***************"+childLevel+"***************parentLevel********"+parentLevel);
try
{
    if(favCategoryId != null)
    {
        Iterator myVeryOwnIterator = categoryMap.keySet().iterator();
        Iterator myVeryOwnIteratorNew = categoryMap1.keySet().iterator();
        logger.info("category map size"+categoryMap.size());
        logger.info("category map size"+categoryMap1.size());
        LabelValueBean bean = null;
        LabelValueBean favCategoryBean = null;
        ArrayList favCategoryList = new ArrayList();
        
        while(myVeryOwnIteratorNew.hasNext())
        {
            bean = (LabelValueBean)myVeryOwnIteratorNew.next();
            if(bean.getValue().equals(favCategoryId))
                newCategoryMap.put(bean, categoryMap.get(bean));
        }
        
        while(myVeryOwnIterator.hasNext())
        {
			bean = (LabelValueBean) myVeryOwnIterator.next();
			if (bean.getValue().equals(favCategoryId)) {
				if (!newCategoryMap.containsKey(bean)) {
					newCategoryMap.put(bean, categoryMap.get(bean));

				}
			}
		}
        
        for(myVeryOwnIteratorNew = categoryMap1.keySet().iterator(); myVeryOwnIteratorNew.hasNext();)
        {
			bean = (LabelValueBean) myVeryOwnIteratorNew.next();
			if (!bean.getValue().equals(favCategoryId)) {
				favCategoryBean = bean;
				favCategoryList = (ArrayList) (ArrayList) categoryMap
						.get(bean);
				newCategoryMap.put(bean, favCategoryList);
			}
		}
        
        for(myVeryOwnIterator = categoryMap.keySet().iterator(); myVeryOwnIterator.hasNext();)
        {
			bean = (LabelValueBean) myVeryOwnIterator.next();
			if (!bean.getValue().equals(favCategoryId)) {
				if (!newCategoryMap.containsKey(bean)) {
					favCategoryBean = bean;
					favCategoryList = (ArrayList) (ArrayList) categoryMap
							.get(bean);
					newCategoryMap.put(bean, favCategoryList);
				}
			}
		}

        return newCategoryMap;
    }
}
catch(Exception e)
{
    e.printStackTrace();
    return categoryMap;
}
finally {
	  try
	    {
		    categoryMap1=null;
	       // DBConnection.releaseResources(con, ps, rs);
	    }
	    catch(Exception e)
	    {
	        e.printStackTrace();
	        throw new KmException(e.getMessage(), e);
	    }
	
}

return categoryMap;
}	

    public ArrayList getAllChildrenNoPan(String parentId, String levelId)
    throws KmException
{
    Connection con;
    ResultSet rs;
    ResultSet rs1;
    PreparedStatement ps;
    PreparedStatement ps1;
    con = null;
    rs = null;
    rs1 = null;
    ps = null;
    ps1 = null;
    ArrayList arraylist;
    ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
   String logic="";
    try
    {
        ArrayList elementList = new ArrayList();
        StringBuffer query = new StringBuffer("select ele1.element_id,ele1.element_name,ele1.element_level_id,ele2.element_id,ele2.element_name as parent_name from km_element_mstr ele1 inner join km_element_mstr ele2  on  ele1.parent_id = ele2.element_id  where ele1.parent_id=ele2.element_id and ele1.element_level_id=? ");
      //getting the value for logic to generate alternate query for brefing and sop
        String query1=new String("select BILL_PLAN_NAME from KM_TEMP_BILLPLAN_MSTR where BILLPLAN_ID=01 with ur");
       
       KmElementMstr dto = new KmElementMstr();
        con = DBConnection.getDBConnection();
        //added by vishwas for alternate query
    ps1=con.prepareStatement(query1);
    rs1=ps1.executeQuery();
    while(rs1.next())
    {
    	logic=rs1.getString(1);
    }
     
        if(logic.equals("Y"))
        {
        	logger.info("logic value23========"+logic);
        	
     	   ps = con.prepareStatement(query.append(" and ele1.pan_status ='Y' order by lower(ele2.element_name), lower(ele1.element_name) with ur").toString());   
        }
         //end by vishwas for alternate query
        else
        {
        	logger.info("logic value2========"+logic);
        	ps = con.prepareStatement(query.append(" and ele1.pan_status !='Y' order by lower(ele2.element_name), lower(ele1.element_name) with ur").toString());
        }
       
       
        ps.setInt(1, Integer.parseInt(levelId));
        for(rs = ps.executeQuery(); rs.next(); elementList.add(dto))
        {
            dto = new KmElementMstr();
            dto.setElementName((new StringBuilder(String.valueOf(rs.getString("PARENT_NAME")))).append(":").append(rs.getString("ELEMENT_NAME")).toString());
            dto.setElementId(rs.getString("ELEMENT_ID"));
            dto.setElementLevel(rs.getString("ELEMENT_LEVEL_ID"));
            dto.setParentName(rs.getString("PARENT_NAME"));
        }

        logger.info("Element List is Returned");
        arraylist = elementList;
    }
    catch(SQLException e)
    {
        e.printStackTrace();
        throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
    }
    catch(Exception e)
    {
        e.printStackTrace();
        throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
    }finally{
    try
    {
        DBConnection.releaseResources(con, ps, rs);
        DBConnection.releaseResources(con, ps1, rs1);
    }
    catch(DAOException e)
    {
        e.printStackTrace();
        throw new KmException(e.getMessage(), e);
    }
    }
 return arraylist;

}
    public boolean getCircleStatus(String circleId)
        throws KmException
    {
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        boolean isRestricted;
        con = null;
        rs = null;
        ps = null;
        isRestricted = false;
        try
        {
            ArrayList elementList = new ArrayList();
            String query = "SELECT RESTRICTED FROM KM_ELEMENT_MSTR WHERE ELEMENT_ID = ? AND RESTRICTED = 'Y' WITH UR ";
            con = DBConnection.getDBConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, Integer.parseInt(circleId));
            for(rs = ps.executeQuery(); rs.next();)
                isRestricted = true;

            logger.info("Element List is Returned");
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }finally{

        try
        {
            DBConnection.releaseResources(con, ps, rs);
        }
        catch(DAOException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }
        }
        return isRestricted;
    }

    public HashMap getSubCategoryMapElements(String elementId, int parentLevel, int childLevel)
        throws KmException
    {
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        LinkedHashMap categoryMap;
        ArrayList subCategoryList;
        con = null;
        rs = null;
        ps = null;
        categoryMap = new LinkedHashMap();
        subCategoryList = new ArrayList();
        try
        {
            con = DBConnection.getDBConnection();
            ps = con.prepareStatement("WITH nee(element_id,element_name,status,element_level_id,parent_id) AS ( SELECT  ELEMENT_ID, element_name ,status, element_level_id, parent_id  FROM KM_ELEMENT_MSTR WHERE element_id =? UNION ALL  SELECT nplus1.element_id, nplus1.element_name,nplus1.status ,nplus1.element_level_id, nplus1.parent_id FROM KM_ELEMENT_MSTR as nplus1, nee  WHERE nee.element_id=nplus1.PARENT_ID and nplus1.status='A')  SELECT A.parent_id as CATEGORY_ID, B.ELEMENT_NAME AS CATEGORY_NAME, A.element_id AS SUB_CATEGORY_ID ,A.element_name  AS SUB_CATEGORY_NAME, a.element_level_id ELEMENT_LEVEL_ID,b.element_level_id  AS  PARENT_LEVEL_ID FROM  nee A inner join km_element_mstr B on a.parent_id = b.element_id left join KM_DOCUMENT_MSTR doc on doc.element_id = a.element_id  where    a.element_level_id in(?,?,?) and B.status='A'   and ((doc.approval_status = 'A'  and  current date between date(doc.PUBLISHING_START_DT) and date(doc.PUBLISHING_END_DT) and doc.status = 'A')or doc.approval_status is null)  order by parent_level_id,a.parent_id, lower(a.element_name) desc WITH UR");
            ps.setInt(1, Integer.parseInt(elementId));
            ps.setInt(2, childLevel);
            ps.setInt(3, parentLevel);
            ps.setInt(4, 0);
            rs = ps.executeQuery();
            String oldCategoryName = null;
            String oldCategoryId = null;
            int elementLevelId = 0;
            int parentLevelId = 0;
            int i = 0;
            while(rs.next())
            {
                elementLevelId = rs.getInt("ELEMENT_LEVEL_ID");
                parentLevelId = rs.getInt("PARENT_LEVEL_ID");
                if(elementLevelId == parentLevel)
                    categoryMap.put(new MyLabelValueBean(rs.getString("SUB_CATEGORY_NAME"), rs.getString("SUB_CATEGORY_ID")), new ArrayList());
                else
                if((elementLevelId == childLevel || elementLevelId == 0) && parentLevelId < childLevel)
                    if(i == 0)
                    {
                        oldCategoryName = rs.getString("CATEGORY_NAME");
                        oldCategoryId = rs.getString("CATEGORY_ID");
                        subCategoryList.add(new LabelValueBean(rs.getString("SUB_CATEGORY_NAME"), rs.getString("SUB_CATEGORY_ID")));
                        i++;
                    } else
                    if(i > 0)
                    {
                        if(!oldCategoryName.equals(rs.getString("CATEGORY_NAME")))
                        {
                            categoryMap.put(new MyLabelValueBean(oldCategoryName, oldCategoryId), subCategoryList);
                            if(parentLevelId < childLevel)
                            {
                                oldCategoryName = rs.getString("CATEGORY_NAME");
                                oldCategoryId = rs.getString("CATEGORY_ID");
                                subCategoryList = new ArrayList();
                            }
                        }
                        subCategoryList.add(new LabelValueBean(rs.getString("SUB_CATEGORY_NAME"), rs.getString("SUB_CATEGORY_ID")));
                    }
            }
            if(i > 0)
                categoryMap.put(new MyLabelValueBean(oldCategoryName, oldCategoryId), subCategoryList);
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }
        finally{
			try {
				DBConnection.releaseResources(con, ps, rs);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
        return categoryMap;
    }

    public KmElementMstr getCompleteElementDetails(String elementId)
        throws DAOException
    {
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        KmElementMstr element;
        con = null;
        rs = null;
        ps = null;
        element = null;
        try
        {
            ArrayList elementList = new ArrayList();
            con = DBConnection.getDBConnection();
            ps = con.prepareStatement(" select ele.ELEMENT_ID,ele.ELEMENT_NAME,ele.ELEMENT_LEVEL_ID,doc.DOCUMENT_ID,doc.DOCUMENT_NAME,doc.DOCUMENT_PATH from KM_ELEMENT_MSTR ele left join KM_DOCUMENT_MSTR doc on ele.ELEMENT_ID = doc.ELEMENT_ID where ele.ELEMENT_ID = ? and ( doc.approval_status = 'A' or doc.approval_status is null)  with ur ");
            ps.setInt(1, Integer.parseInt(elementId));
            rs = ps.executeQuery();
            if(rs.next())
            {
                element = new KmElementMstr();
                element.setElementId(elementId);
                element.setElementLevel(rs.getString("ELEMENT_LEVEL_ID"));
                element.setElementName(rs.getString("ELEMENT_NAME"));
                element.setDocumentId(rs.getString("DOCUMENT_ID"));
                element.setDocumentName(rs.getString("DOCUMENT_NAME"));
                element.setDocumentPath(rs.getString("DOCUMENT_PATH"));
            }
            logger.info("Complete details of document is returned ");
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }
      finally{

        try
        {
            DBConnection.releaseResources(con, ps, rs);
        }
        catch(DAOException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }}
        return element;
    }

  /*  public volatile HashMap getCategoryMapElements(String s, String s1)
        throws KmException
    {
        return getCategoryMapElements(s, s1);
    }*/
    protected static final String SQL_USER_LEVEL_ID = "SELECT ELEMENT_LEVEL_ID FROM KM_ELEMENT_MSTR ELE, KM_USER_MSTR USR WHERE USR.USER_ID = ? AND ELE.ELEMENT_ID = USR.ELEMENT_ID";
    protected static final String SQL_UPDATE_PARENT = "UPDATE KM_ELEMENT_MSTR SET PARENT_ID = ? WHERE ELEMENT_ID = ?";
    protected static final String SQL_EDIT_ELEMENT = "UPDATE KM_ELEMENT_MSTR SET ELEMENT_NAME = ? , ELEMENT_DESC = ? WHERE ELEMENT_ID = ?";
    protected static final String SQL_GET_ELEMENT_NAME = "SELECT ELEMENT_NAME FROM KM_ELEMENT_MSTR WHERE ELEMENT_ID = ?  ";
    protected static final String SQL_GET_ELEMENT_DETAILS = "WITH nee(element_id,chain) AS ( SELECT  ELEMENT_ID,  cast(element_name as VARCHAR(" + PropertyReader.getAppValue("search.path.size") + ")) FROM KM_ELEMENT_MSTR WHERE element_id = ?  UNION ALL \tSELECT nplus1.element_id,nee.chain || '/' || nplus1.element_name  FROM KM_ELEMENT_MSTR as nplus1, nee  WHERE nee.element_id=nplus1.PARENT_ID) SELECT CHAIN ,ele.ELEMENT_ID, ele.ELEMENT_NAME,ele.ELEMENT_NAME,ele.ELEMENT_DESC FROM NEE inner join KM_ELEMENT_MSTR ele on ele.ELEMENT_ID = NEE.ELEMENT_ID WHERE   ele.ELEMENT_ID = ?";
    protected static final String SQL_GET_DOCUMENTS = "SELECT B.ELEMENT_ID, B.ELEMENT_NAME,C.DOCUMENT_NAME,C.DOCUMENT_ID  FROM  KM_ELEMENT_MSTR B inner join KM_DOCUMENT_MSTR C on B.ELEMENT_ID = C.ELEMENT_ID WHERE B.PARENT_ID=?    AND B.ELEMENT_LEVEL_ID = 0 AND C.STATUS = 'A'";
    protected static final String SQL_SELECT_ELEMENT = "ELEMENT_ID, ELEMENT_NAME FROM KM_ELEMENT_MSTR WHERE ELEMENT_ID = ?";
    protected static final String SQL_GET_CHILDREN = "SELECT * FROM KM_ELEMENT_MSTR WHERE  STATUS = 'A' AND PARENT_ID=? AND ELEMENT_LEVEL_ID != 0 AND PAN_STATUS != 'Y'";
    protected static final String SQL_GET_ALL_CHILDREN = "SELECT * FROM KM_ELEMENT_MSTR WHERE  STATUS = 'A' AND PARENT_ID=? AND ELEMENT_LEVEL_ID != 0 ";
    protected static final String SQL_GET_LEVEL_CHILDREN = "select ele1.element_id,ele1.element_name,ele1.element_level_id,ele2.element_id,ele2.element_name as parent_name from km_element_mstr ele1 inner join km_element_mstr ele2  on  ele1.parent_id = ele2.element_id  where ele1.parent_id=ele2.element_id and ele1.element_level_id=? ";
    protected static final String SQL_GET_LEVEL_CHILDREN_REC = " WITH NEE(ELEMENT_ID) AS  (SELECT  ELEMENT_ID   FROM KM_ELEMENT_MSTR  WHERE ELEMENT_ID = ?  UNION ALL  SELECT NPLUS1.ELEMENT_ID   FROM KM_ELEMENT_MSTR AS NPLUS1, NEE  WHERE NEE.ELEMENT_ID=NPLUS1.PARENT_ID)  SELECT NEE.ELEMENT_ID, ELE.ELEMENT_NAME, ELE.ELEMENT_LEVEL_ID,ele.parent_id ,ele1.element_name AS PARENT_NAME  FROM KM_ELEMENT_MSTR ELE inner join NEE  on ELE.ELEMENT_ID=NEE.ELEMENT_ID inner join KM_ELEMENT_MSTR ele1 on ele1.element_id=ele.parent_id  WHERE   ELE.ELEMENT_LEVEL_ID=? AND ELE.STATUS='A' ";
    protected static final String SQL_GET_CHILDREN_PATH = "WITH NEE(ELEMENT_ID,  CHAIN) AS (SELECT  ELEMENT_ID, CAST(ELEMENT_NAME AS VARCHAR(2400)) FROM KM_ELEMENT_MSTR WHERE ELEMENT_ID = ? UNION ALL SELECT NPLUS1.ELEMENT_ID, NEE.CHAIN || '/' || NPLUS1.ELEMENT_NAME FROM KM_ELEMENT_MSTR AS NPLUS1, NEE WHERE NEE.ELEMENT_ID=NPLUS1.PARENT_ID) SELECT NEE.ELEMENT_ID, ELE.ELEMENT_NAME, CHAIN AS ELEMENT_PATH FROM KM_ELEMENT_MSTR ELE inner join NEE on ELE.ELEMENT_ID=NEE.ELEMENT_ID WHERE   ELE.PARENT_ID = ? AND ELE.ELEMENT_LEVEL_ID!=0 AND ELE.STATUS='A' AND ELE.PAN_STATUS = 'N' ";
    protected static final String SQL_GET_ALL_CHILDREN_PATH = "WITH NEE(ELEMENT_ID,  CHAIN) AS (SELECT  ELEMENT_ID, CAST(ELEMENT_NAME AS VARCHAR(2400)) FROM KM_ELEMENT_MSTR WHERE ELEMENT_ID = ? UNION ALL SELECT NPLUS1.ELEMENT_ID, NEE.CHAIN || '/' || NPLUS1.ELEMENT_NAME FROM KM_ELEMENT_MSTR AS NPLUS1, NEE WHERE NEE.ELEMENT_ID=NPLUS1.PARENT_ID) SELECT NEE.ELEMENT_ID, ELE.ELEMENT_NAME, ELE.ELEMENT_DESC,CHAIN AS ELEMENT_PATH FROM KM_ELEMENT_MSTR ELE inner join NEE on ELE.ELEMENT_ID=NEE.ELEMENT_ID WHERE ELE.PARENT_ID = ? AND ELE.ELEMENT_LEVEL_ID!=0 AND ELE.STATUS='A' ";
    protected static final String SQL_GET_PAN_CHILD = "SELECT * FROM KM_ELEMENT_MSTR WHERE  STATUS = 'A'  AND PAN_STATUS='Y' AND PARENT_ID= ?  ";
    protected static final String SQL_GET_ALL_LEVEL_CHILDREN = "WITH NEE(ELEMENT_ID) AS (SELECT  ELEMENT_ID FROM KM_ELEMENT_MSTR WHERE ELEMENT_ID = ? UNION ALL SELECT NPLUS1.ELEMENT_ID FROM KM_ELEMENT_MSTR AS NPLUS1, NEE WHERE NEE.ELEMENT_ID=NPLUS1.PARENT_ID) SELECT NEE.ELEMENT_ID, ELE.ELEMENT_NAME, ELE.ELEMENT_LEVEL_ID FROM KM_ELEMENT_MSTR ELE inner join NEE on ELE.ELEMENT_ID=NEE.ELEMENT_ID WHERE  ELE.ELEMENT_LEVEL_ID!=0 AND ELE.STATUS='A' ";
    protected static final String SQL_ELEMENT_LEVEL_NAME = "SELECT ELEMENT_LEVEL_NAME FROM KM_ELEMENT_LEVEL_MSTR WHERE ELEMENT_LEVEL_ID = ?";
    protected static final String SQL_ELEMENT_LEVEL_ID = "SELECT ELEMENT_LEVEL_ID FROM  KM_ELEMENT_MSTR  WHERE  ELEMENT_ID = ?";
    protected static final String SQL_PARENT_ID_STRING = "WITH NEE(ELEMENT_ID,CHAIN, ELEMENT_LEVEL_ID) AS (SELECT  ELEMENT_ID, CAST(CAST(ELEMENT_ID AS CHARACTER(5))AS VARCHAR(60)) ,ELEMENT_LEVEL_ID FROM KM_ELEMENT_MSTR  WHERE ELEMENT_ID =? UNION ALL SELECT NPLUS1.ELEMENT_ID, NEE.CHAIN || '/' || CAST(NPLUS1.ELEMENT_ID AS CHARACTER(5)), NPLUS1.ELEMENT_LEVEL_ID FROM KM_ELEMENT_MSTR AS NPLUS1, NEE WHERE NEE.ELEMENT_ID=NPLUS1.PARENT_ID) SELECT CHAIN AS PARENT_STRING FROM NEE WHERE NEE.ELEMENT_ID=?";
    protected static final String SQL_PARENT_NAME_STRING = "WITH NEE(ELEMENT_ID,CHAIN, ELEMENT_LEVEL_ID) AS (SELECT  ELEMENT_ID, CAST(ELEMENT_NAME AS VARCHAR(350)) ,ELEMENT_LEVEL_ID FROM KM_ELEMENT_MSTR  WHERE ELEMENT_ID =? UNION ALL SELECT NPLUS1.ELEMENT_ID, NEE.CHAIN || '>' || NPLUS1.ELEMENT_NAME, NPLUS1.ELEMENT_LEVEL_ID FROM KM_ELEMENT_MSTR AS NPLUS1, NEE WHERE NEE.ELEMENT_ID=NPLUS1.PARENT_ID) SELECT CHAIN AS PARENT_STRING FROM NEE WHERE NEE.ELEMENT_ID=?";
    protected static final String SQL_GET_ALL_LEVEL_NAMES = "SELECT ELEMENT_LEVEL_NAME,ELEMENT_LEVEL_ID FROM KM_ELEMENT_LEVEL_MSTR ORDER BY ELEMENT_LEVEL_ID";
    protected static final String SQL_PARENT_ID = "SELECT PARENT_ID FROM  KM_ELEMENT_MSTR  WHERE  ELEMENT_ID = ?";
    protected static final String SQL_GET_ELEMENT_ID = "SELECT NEXTVAL FOR KM_ELEMENT_ID_SEQ FROM SYSIBM.SYSDUMMY1";
    public static final String SQL_INSERT_ELEMENT = "INSERT INTO KM_ELEMENT_MSTR(ELEMENT_ID, ELEMENT_NAME,ELEMENT_DESC, PARENT_ID, ELEMENT_LEVEL_ID, PAN_STATUS,STATUS, CREATED_BY, CREATED_DT, UPDATED_BY, UPDATED_DT)VALUES(?, ?, ?, ?, ?, ?, ?, ?, CURRENT TIMESTAMP,?, CURRENT TIMESTAMP)";
    protected static final String SQL_COUNT_ELEMENT = "SELECT * FROM KM_ELEMENT_MSTR WHERE ELEMENT_NAME=? AND PARENT_ID = ? AND ELEMENT_LEVEL_ID!=0 AND STATUS='A'";
    protected static final String SQL_UPDATE_ELEMENT_LEVEL = "UPDATE KM_ELEMENT_MSTR ELE SET ELE.ELEMENT_LEVEL_ID=ELE.ELEMENT_LEVEL_ID+? WHERE status ='A' and ( ELE.ELEMENT_ID=? ";
    protected static final String SQL_GET_CHILDREN_IDS = "WITH NEE(ELEMENT_ID) AS (SELECT  ELEMENT_ID FROM KM_ELEMENT_MSTR WHERE ELEMENT_ID = ? UNION ALL SELECT NPLUS1.ELEMENT_ID FROM KM_ELEMENT_MSTR AS NPLUS1, NEE WHERE NEE.ELEMENT_ID=NPLUS1.PARENT_ID) SELECT ele.ELEMENT_ID FROM NEE, KM_ELEMENT_MSTR ele where NEE.ELEMENT_ID=ele.ELEMENT_ID AND ele.STATUS='A' ";
    protected static final String SQL_GET_ELEMENTS = "WITH NEE(ELEMENT_ID) AS (SELECT  ELEMENT_ID FROM KM_ELEMENT_MSTR WHERE ELEMENT_ID = ? UNION ALL SELECT NPLUS1.ELEMENT_ID FROM KM_ELEMENT_MSTR AS NPLUS1, NEE WHERE NEE.ELEMENT_ID=NPLUS1.PARENT_ID) SELECT nee.ELEMENT_ID FROM NEE, KM_ELEMENT_MSTR ele where NEE.ELEMENT_ID=ele.ELEMENT_ID AND ele.ELEMENT_LEVEL_ID!=0 and ele.STATUS='A'";
    protected static final String SQL_GET_DOC_ELEMENTS = "WITH NEE(ELEMENT_ID) AS (SELECT  ELEMENT_ID FROM KM_ELEMENT_MSTR WHERE ELEMENT_ID = ? UNION ALL SELECT NPLUS1.ELEMENT_ID FROM KM_ELEMENT_MSTR AS NPLUS1, NEE WHERE NEE.ELEMENT_ID=NPLUS1.PARENT_ID)  SELECT nee.ELEMENT_ID FROM NEE, KM_ELEMENT_MSTR ele where NEE.ELEMENT_ID=ele.ELEMENT_ID AND ele.ELEMENT_LEVEL_ID=0 and ele.STATUS='A'";
    protected static final String SQL_DELETE_ELEMENTS = "UPDATE KM_ELEMENT_MSTR SET STATUS= 'D', UPDATED_DT= current timestamp, UPDATED_BY = ? WHERE STATUS='A' and (ELEMENT_ID = ? ";
    protected static final String SQL_GET_PAN_CIRCLE = "SELECT * FROM KM_ELEMENT_MSTR WHERE PAN_STATUS='Y'";
    protected static final String SQL_GET_PATH_FOR_CIRCLE_ID = "WITH NEE(ELEMENT_ID,CHAIN, ELEMENT_LEVEL_ID) AS (SELECT  ELEMENT_ID, CAST(CAST(ELEMENT_ID AS CHARACTER(5))AS VARCHAR(60)) ,ELEMENT_LEVEL_ID FROM KM_ELEMENT_MSTR  WHERE ELEMENT_ID =1  UNION ALL  SELECT NPLUS1.ELEMENT_ID, NEE.CHAIN || '/' || CAST(NPLUS1.ELEMENT_ID AS CHARACTER(5)), NPLUS1.ELEMENT_LEVEL_ID FROM KM_ELEMENT_MSTR AS NPLUS1, NEE WHERE NEE.ELEMENT_ID=NPLUS1.PARENT_ID) SELECT CHAIN AS PARENT_STRING FROM NEE WHERE NEE.ELEMENT_ID=? ";
    protected static final String SQL_GET_DOCUMENT_ELEMENTS = "SELECT doc.DOCUMENT_ID AS DOC_ID FROM KM_ELEMENT_MSTR ele ,  KM_DOCUMENT_MSTR doc WHERE ele.ELEMENT_LEVEL_ID = 0 AND ele.ELEMENT_ID=doc.ELEMENT_ID AND( ele.ELEMENT_ID = ? ";
    protected static final String SQL_GET_ELEMENTS_MAP = "WITH nee(element_id,element_name,status,element_level_id,parent_id) AS ( SELECT  ELEMENT_ID, element_name ,status, element_level_id, parent_id  FROM KM_ELEMENT_MSTR WHERE element_id =? UNION ALL  SELECT nplus1.element_id, nplus1.element_name,nplus1.status ,nplus1.element_level_id, nplus1.parent_id FROM KM_ELEMENT_MSTR as nplus1, nee  WHERE nee.element_id=nplus1.PARENT_ID and nplus1.status='A')  SELECT A.parent_id as CATEGORY_ID, B.ELEMENT_NAME AS CATEGORY_NAME, A.element_id AS SUB_CATEGORY_ID ,A.element_name  AS SUB_CATEGORY_NAME, a.element_level_id ELEMENT_LEVEL_ID,b.element_level_id  AS  PARENT_LEVEL_ID FROM  nee A inner join km_element_mstr B on a.parent_id = b.element_id left join KM_DOCUMENT_MSTR doc on doc.element_id = a.element_id  where    a.element_level_id in(?,?,?) and B.status='A'   and ((doc.approval_status = 'A'  and  current date between date(doc.PUBLISHING_START_DT) and date(doc.PUBLISHING_END_DT) and doc.status = 'A')or doc.approval_status is null)  order by parent_level_id,a.parent_id, lower(a.element_name) desc WITH UR";
    protected static final String SQL_GET_COMPLETE_ELEMENT = " select ele.ELEMENT_ID,ele.ELEMENT_NAME,ele.ELEMENT_LEVEL_ID,doc.DOCUMENT_ID,doc.DOCUMENT_NAME,doc.DOCUMENT_PATH from KM_ELEMENT_MSTR ele left join KM_DOCUMENT_MSTR doc on ele.ELEMENT_ID = doc.ELEMENT_ID where ele.ELEMENT_ID = ? and ( doc.approval_status = 'A' or doc.approval_status is null)  with ur ";
    protected static final String SQL_GET_ELEMENTS_AS_PER_LEVEL = "SELECT ELEMENT_ID FROM KM_ELEMENT_MSTR WHERE ELEMENT_LEVEL_ID = ? AND STATUS = 'A' WITH UR ";
    protected static final String SQL_GET_ELEMENT = "SELECT * FROM KM_ELEMENT_MSTR WHERE ELEMENT_ID = ? WITH UR ";
    protected static final String SQL_GET_CIRCLE_DESC = "select ELEMENT_ID , ELEMENT_NAME from KM_ELEMENT_MSTR where ELEMENT_LEVEL_ID = 3 and STATUS = 'A' with UR";
   
    //added by vishwas to get all Lob description
    protected static final String SQL_GET_LOB_DESC ="select ELEMENT_ID , ELEMENT_NAME from KM_ELEMENT_MSTR where ELEMENT_LEVEL_ID = 2 and STATUS = 'A' with UR";
    protected static final String SQL_GET_ALL_CIRCLE ="SELECT * FROM km2.KM_ELEMENT_MSTR WHERE  STATUS = 'A' AND  ELEMENT_LEVEL_ID != 0 and ELEMENT_LEVEL_ID =3 WITH UR";
    protected static final String SQL_GET_ALL_ELEMENTS_UNDER_CIRCLE ="SELECT * FROM  km2.KM_ELEMENT_MSTR WHERE  STATUS = 'A' AND  ELEMENT_LEVEL_ID != 0 and PARENT_ID = ?";
    protected static final String SQL_GET_DOCUMENT_IDS_DISPLAY="SELECT DOCUMENT_DISPLAY_NAME FROM KM_DOCUMENT_MSTR WHERE ( ELEMENT_ID = ?  ";
    private static final Logger logger = Logger.getLogger(KmElementMstrDaoImpl.class);
////Added by Harpreet For Copy of Element

    public int insertElement(String elementId,String parentId)throws KmException{

    	Connection con=null;
        ResultSet rs=null;
        PreparedStatement ps=null;
        try
        {
            con = DBConnection.getDBConnection();
            ps = con.prepareStatement(" select ele.ELEMENT_ID,ele.ELEMENT_NAME,ele.ELEMENT_LEVEL_ID,doc.DOCUMENT_ID,doc.DOCUMENT_NAME,doc.DOCUMENT_PATH from KM_ELEMENT_MSTR ele left join KM_DOCUMENT_MSTR doc on ele.ELEMENT_ID = doc.ELEMENT_ID where ele.ELEMENT_ID = ? and ( doc.approval_status = 'A' or doc.approval_status is null)  with ur ");
            ps.setInt(1, Integer.parseInt(elementId));
            ps.setInt(2, Integer.parseInt(parentId));
            ps.executeUpdate();
               logger.info("Complete details of document is returned ");
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }
      finally{

        try
        {
            DBConnection.releaseResources(con, ps, rs);
        }
        catch(DAOException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }}


    return 1;
    }

	public int insertdocument(String elementId)throws KmException{

		return 1;
	}

	public boolean phyCopy(String docPath)throws KmException{

	return true;
	}

	// Adding by RAM
	public ArrayList<KmElementMstr> findElementsInCircle(int elementId, String favCategoryId) throws KmException
{
	logger.info("POPOPOPOP  in KmElementMstrDaoImpl :"+elementId+"  favCategoryId :"+favCategoryId);
    Connection con;
    ResultSet rs;
    PreparedStatement ps;
    LinkedHashMap categoryMap;
    LinkedHashMap newCategoryMap;
    ArrayList elementList;
    int parentLevel;
    int childLevel;
    con = null;
    rs = null;
    ps = null;
    elementList = new ArrayList();
    parentLevel = 4;
    try
    {
        con = DBConnection.getDBConnection();
        ps = con.prepareStatement("WITH nee(element_id,element_name,status,element_level_id,parent_id,sequenceno) AS ( SELECT  ELEMENT_ID, element_name ,status, element_level_id, parent_id,SEQUENCENO FROM KM_ELEMENT_MSTR WHERE element_id =? UNION ALL  SELECT nplus1.element_id, nplus1.element_name,nplus1.status ,nplus1.element_level_id, nplus1.parent_id,nplus1.SEQUENCENO FROM KM_ELEMENT_MSTR as nplus1, nee  WHERE nee.element_id=nplus1.PARENT_ID and nplus1.status='A') SELECT A.parent_id as CATEGORY_ID, B.ELEMENT_NAME AS CATEGORY_NAME, A.element_id AS SUB_CATEGORY_ID ,A.element_name  AS SUB_CATEGORY_NAME, a.element_level_id ELEMENT_LEVEL_ID,b.element_level_id  AS  PARENT_LEVEL_ID,a.SEQUENCENO as seqno FROM  nee A inner join km_element_mstr B on a.parent_id = b.element_id left join KM_DOCUMENT_MSTR doc on doc.element_id = a.element_id where a.element_level_id in(4) and B.status='A'   and ((doc.approval_status = 'A'  and  current date between date(doc.PUBLISHING_START_DT) and date(doc.PUBLISHING_END_DT) and doc.status = 'A')or doc.approval_status is null)  order by parent_level_id,a.parent_id, lower(a.element_name) asc WITH UR ");
        ps.setInt(1, elementId);
        logger.info(parentLevel+"wueryyyyyyyyyyy"+elementId+":");
        rs = ps.executeQuery();
        String oldCategoryName = null;
        String oldCategoryId = null;
        int elementLevelId = 0;
        int parentLevelId = 0;
        int i = 0;
        int sequenceNo = 0;
        int elementIdd = 0;
        String elementName = "";
        KmElementMstr dto = null;
        while(rs.next())
        {
        	dto=new KmElementMstr();
            sequenceNo = rs.getInt("SEQNO");
            elementIdd = rs.getInt("SUB_CATEGORY_ID");
            elementName = rs.getString("SUB_CATEGORY_NAME");
            dto.setElementId(String.valueOf(elementIdd));
            dto.setElementName(elementName);
            dto.setSequenceNo(sequenceNo);
            elementList.add(dto);
        }
    }
    catch(SQLException e)
    {
        e.printStackTrace();
        throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
    }
    catch(Exception e)
    {
        e.printStackTrace();
        throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
    }
    finally{
    try
    {
        DBConnection.releaseResources(con, ps, rs);
    }
    catch(Exception e)
    {
        e.printStackTrace();
    }
    }
    return elementList;
}
	// End of adding by RAM

	 
	


	public ArrayList<KmElementMstr> findElementsInCircle(int elementId) throws KmException
	 {
		  Connection con;
		    ResultSet rs;
		    PreparedStatement ps;
		    KmElementMstr dto =  null;
		    ArrayList listOfCircles = new ArrayList();
		    con = null;
		    rs = null;
		    ps = null;
		    try
		    {
		        con = DBConnection.getDBConnection();
		        logger.info("Query :"+SQL_GET_ALL_ELEMENTS_UNDER_CIRCLE);
		        logger.info("Parameter :"+elementId);
		        ps = con.prepareStatement(SQL_GET_ALL_ELEMENTS_UNDER_CIRCLE);
		        ps.setInt(1, elementId);
		        	for(rs = ps.executeQuery(); rs.next(); listOfCircles.add(dto))
		            {
		                dto = new KmElementMstr();
		                dto.setElementName(rs.getString("ELEMENT_NAME"));
		                dto.setElementId(rs.getString("ELEMENT_ID"));
		                dto.setElementLevel(rs.getString("ELEMENT_LEVEL_ID"));
		                dto.setSequenceNo(rs.getInt("SEQUENCENO"));
		            }
		    }
		    catch(SQLException e)
		    {
		        e.printStackTrace();
		        logger.info(e);
		        throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
		    }
		    catch(Exception e)
		    {
		        e.printStackTrace();
		        logger.info(e);
		        throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
		    }

		  finally{
		    try
		    {
		        DBConnection.releaseResources(con, ps, rs);
		    }
		    catch(DAOException e)
		    {
		        e.printStackTrace();
		        throw new KmException(e.getMessage(), e);
		    }

		   }
		  logger.info("size of list of circles  :"+listOfCircles.size());
		    return listOfCircles;
	 } 
	 

	public String updateElementSequence(String idAndValues) throws KmException
	{
		logger.info("POPOPOPOP  in KmElementMstrDaoImpl : updateElementSequence:"+idAndValues);
	    Connection con;
	    PreparedStatement ps;
	    int parentLevel;
	    int childLevel;
	    con = null;
	    ps = null;
	    parentLevel = 4;
	    String []idAndValuesArr;
	    int elementId;
	    int elementSequence;
	    String[] idSequence;
	    int counter=0;
	    String msg=null;
	    try
	    {
	    	idAndValuesArr = idAndValues.split("##");
	        con = DBConnection.getDBConnection();
	        con.setAutoCommit(false);
	        for(int i=0; i< idAndValuesArr.length; i++)
	        {
	        idSequence = idAndValuesArr[i].split("#");
	        ps = con.prepareStatement("UPDATE KM_ELEMENT_MSTR SET SEQUENCENO = ?  WHERE  ELEMENT_ID = ?");
	        logger.info("in dao sequence:"+Integer.valueOf(idSequence[1])+"   ||elementid  :"+ Integer.valueOf(idSequence[0]));
	        ps.setInt(1, Integer.valueOf(idSequence[1]));
	        ps.setInt(2, Integer.valueOf(idSequence[0]));
	        ps.executeUpdate();
	        counter++;
	        msg="success";
	        }
	        con.commit();
	        logger.info("Records updated  :"+counter);
	    }
	    catch(SQLException e)
	    {
	    	msg="error";
	    	try
	    	{
	    	con.rollback();
	    	}
	    	catch(Exception ex)
	    	{
	    		ex.printStackTrace();
	    	}
	        e.printStackTrace();
	        throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
	    }
	    catch(Exception e)
	    {
	    	msg="error";
	    	try
	    	{
	    	con.rollback();
	    	}
	    	catch(Exception ex)
	    	{
	    		ex.printStackTrace();
	    	}
	        e.printStackTrace();
	        throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
	    }
	    finally{
	    try
	    {
	        DBConnection.releaseResources(con, ps, null);
	    }
	    catch(Exception e)
	    {
	        e.printStackTrace();
	    }
	    }
	    return msg;
	}
	// End of adding by RAM

//Added By Bhaskar
	public ArrayList<QuizReportDto> getReport(KmUserMstr sessionUserBean)
			throws KmException {
		  Connection con;
		    ResultSet rs;
		    PreparedStatement ps;
		    QuizReportDto dto =  null;
		    ArrayList<QuizReportDto> reportList=new ArrayList<QuizReportDto>();
		    con = null;
		    rs = null;
		    ps = null;
		    String user_login_id=sessionUserBean.getUserLoginId();
		    String circle_id=sessionUserBean.getCircleId();
		    String element_id=sessionUserBean.getElementId();
		    try
		    {
		    		
		    	//After UAT Changes
		    	String Query=" select A.counta as WRONGANSWERS,B.countb as correctanswers,C.user_Id as userId,D.ud_id as udId,E.circle_name as CircleName,F.countc as skipanswers,G.element_name as LOB  from (select  count(*) counta  from KM_QUIZ_RESULT where STATUS='W' and  date(date_of_quiz)=current date and user_Id='"+user_login_id+"')as A,"+ 
		    	"(select  count(*) countb  from KM_QUIZ_RESULT where STATUS='C'  and date(date_of_quiz)=current date and user_Id='"+user_login_id+"')as B,(select user_Id from  KM_QUIZ_RESULT where user_id='"+user_login_id+"' and date(date_of_quiz)=current date  fetch first row only) as C,"+
		    	"(select ud_Id from  KM_QUIZ_RESULT where date(date_of_quiz)=current date fetch first row only) as D,(select circle_name from KM_QUIZ_RESULT km,km_circle_mstr cm where cm.circle_id=km.circle_id and km.circle_id='"+circle_id+"'  and date(date_of_quiz)=current date fetch first row only)as E,"+
		    	"(select  count(*) countc  from KM_QUIZ_RESULT where STATUS='S' and date(date_of_quiz)=current date and user_Id='"+user_login_id+"')as F,(select element_name from KM_QUIZ_RESULT km,km_element_mstr em where em.element_id=km.element_id and em.element_id='"+element_id+"'  and date(date_of_quiz)=current date fetch first row only)as G with ur";
		    	
		    	
		    	con = DBConnection.getDBConnection();
		        
		        ps = con.prepareStatement(Query);
		        
		        rs=ps.executeQuery();
		        while(rs.next()){
		        	
		        	int wrongAnswers=Integer.parseInt(rs.getString("WRONGANSWERS"));
		        	int correctAnswers=Integer.parseInt(rs.getString("correctanswers"));
		        	
		                dto = new QuizReportDto();
		                dto.setWrong_answers(rs.getString("WRONGANSWERS"));
		                dto.setCorrect_answers(rs.getString("correctanswers"));
		                if(correctAnswers > wrongAnswers)
		                {
		                	dto.setResult("PASS");
		                }
		                else{
		                	dto.setResult("FAIL");
		                }
		                dto.setUser_id(rs.getString("userId"));
		                dto.setUd_id(rs.getString("udId"));
		                dto.setCircle_name(rs.getString("CircleName"));
		                dto.setSkipQuesions(rs.getString("skipanswers"));
		                dto.setLob(rs.getString("LOB"));
		                reportList.add(dto);
		        }
		          
		    }
		    catch(SQLException e)
		    {
		        e.printStackTrace();
		        logger.info(e);
		        throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
		    }
		    catch(Exception e)
		    {
		        e.printStackTrace();
		        logger.info(e);
		        throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
		    }

		  finally{
		    try
		    {
		        DBConnection.releaseResources(con, ps, rs);
		    }
		    catch(DAOException e)
		    {
		        e.printStackTrace();
		        throw new KmException(e.getMessage(), e);
		    }

		   }
		  
		    return reportList;
	 }


	@Override
	public ArrayList<QuizReportDto> getQuizReport() throws KmException {
		  Connection con;
		    ResultSet rs;
		    PreparedStatement ps;
		    QuizReportDto dto =  null;
		    ArrayList<QuizReportDto> reportList=new ArrayList<QuizReportDto>();
		    con = null;
		    rs = null;
		    ps = null;
		   
		    try
		    {
		    	//After UAT Changes
		    	//String Query=" select user_id,ud_id,circle_id, (select  count(*) from KM_QUIZ_RESULT where STATUS='C')  AS Correct_Answers ,(select  count(*) from KM_QUIZ_RESULT where STATUS='W')AS  Wrong_Answers,(select  count(*) from KM_QUIZ_RESULT where STATUS='S') AS  Skip_Questions from KM_QUIZ_RESULT where date(DATE_OF_QUIZ)=current date  group by user_id,ud_id,circle_id";
		    	
		    	//String Query="SELECT qm.USER_ID,qm.UD_ID,sum(case when qm.STATUS='C' then 1 else 0 END ) CORRECT_ANSWERS,sum(case when qm.STATUS='W' then 1 else 0 END ) WRONG_ANSWERS,"+
				    	//"sum(case when qm.STATUS='S' then 1 else 0 END ) SKIP_QUESTIONS,max(CIRCLE_NAME) CIRCLE_NAME FROM KM_QUIZ_RESULT qm,km_circle_mstr cm WHERE date(DATE_OF_QUIZ)=current date  and qm.circle_id=cm.circle_id GROUP BY qm.USER_ID,qm.UD_ID";

		    	String Query="SELECT qm.USER_ID,qm.UD_ID,sum(case when qm.STATUS='C' then 1 else 0 END ) CORRECT_ANSWERS,sum(case when qm.STATUS='W' then 1 else 0 END ) WRONG_ANSWERS,"+
		    	"sum(case when qm.STATUS='S' then 1 else 0 END ) SKIP_QUESTIONS,max(CIRCLE_NAME) CIRCLE_NAME,max(ELEMENT_NAME)ELEMENT_NAME FROM KM_QUIZ_RESULT qm,km_circle_mstr cm,KM_ELEMENT_MSTR em WHERE date(DATE_OF_QUIZ)=current date  and qm.circle_id=cm.circle_id and qm.ELEMENT_ID=em.ELEMENT_ID GROUP BY qm.USER_ID,qm.UD_ID with ur";

				    	
		    	con = DBConnection.getDBConnection();
		        
		        ps = con.prepareStatement(Query);
		        
		        rs=ps.executeQuery();
		        while(rs.next()){
		        	
		        	int wrongAnswers=Integer.parseInt(rs.getString("WRONG_ANSWERS"));
		        	int correctAnswers=Integer.parseInt(rs.getString("CORRECT_ANSWERS"));
		        	
		                dto = new QuizReportDto();
		                dto.setWrong_answers(rs.getString("WRONG_ANSWERS"));
		                dto.setCorrect_answers(rs.getString("CORRECT_ANSWERS"));
		                if(correctAnswers > wrongAnswers)
		                {
		                	dto.setResult("PASS");
		                }
		                else{
		                	dto.setResult("FAIL");
		                }
		                dto.setUser_id(rs.getString("USER_ID"));
		                dto.setUd_id(rs.getString("UD_ID"));
		                dto.setCircle_name(rs.getString("CIRCLE_NAME"));
		                dto.setSkipQuesions(rs.getString("SKIP_QUESTIONS"));
		                dto.setLob(rs.getString("ELEMENT_NAME"));
		                reportList.add(dto);
		        }
		          
		    }
		    catch(SQLException e)
		    {
		        e.printStackTrace();
		        logger.info(e);
		        throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
		    }
		    catch(Exception e)
		    {
		        e.printStackTrace();
		        logger.info(e);
		        throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
		    }

		  finally{
		    try
		    {
		        DBConnection.releaseResources(con, ps, rs);
		    }
		    catch(DAOException e)
		    {
		        e.printStackTrace();
		        throw new KmException(e.getMessage(), e);
		    }

		   }
		  
		    return reportList;
	 }
	
}