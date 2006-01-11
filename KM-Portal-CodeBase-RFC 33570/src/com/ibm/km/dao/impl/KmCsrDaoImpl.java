package com.ibm.km.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.struts.action.ActionMessage;

import com.ibm.km.dao.DBConnection;
import com.ibm.km.dao.KmCSRUserDao;
import com.ibm.km.dto.KmBriefingMstr;
import com.ibm.km.forms.KmCSRuserFormBean;
import com.ibm.km.exception.KmException;

public class KmCsrDaoImpl implements KmCSRUserDao{
	
	public void insert(KmCSRuserFormBean fb)
    throws KmException
{
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    
    con = null;
    ps = null;
    rs = null;
    int rowsUpdated = 0;
    try
    {
        StringBuffer query = new StringBuffer("INSERT INTO KM_CSRUSERDETAILS (COMMENTS, SENT_TO, SENT_BY, CREATED_DT) VALUES (?, ?, ? , current timestamp)");
        StringBuffer query1= new StringBuffer("Select EMAILID from KM_CONFIGURE_EMAILID");
        con = DBConnection.getDBConnection();
        ps = con.prepareStatement(query.append(" with ur").toString());
        ps.setString(1, fb.getComment());
        ps.setString(2, fb.getSelectedEmail());
        ps.setString(3, "KB_Administrator@in.ibm.com");
        rowsUpdated = ps.executeUpdate();
        
        
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
       
        throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
    }
  }
}
	public ArrayList<HashMap<String,String>> getEmailIdList() throws KmException
	{
		HashMap<String, String>  valueMap;
		ArrayList<HashMap<String,String>> valueList = new ArrayList<HashMap<String,String>>();
		Connection con;
	    PreparedStatement ps;
	    ResultSet rs;
	    
	    con = null;
	    ps = null;
	    rs = null;
	    int rowsUpdated = 0;
	    try
	    {
	    	KmCSRuserFormBean fb=null;
	    	fb = new KmCSRuserFormBean();
	        StringBuffer query1= new StringBuffer("Select EMAILID, DESCRIPTION from KM_CONFIGURE_EMAILID");
	        con = DBConnection.getDBConnection();
	        ps = con.prepareStatement(query1.append(" with ur").toString());
	        
	        rs = ps.executeQuery();
	        while(rs.next()){
	        	valueMap = new HashMap<String,String>();
	        	valueMap.put("emailid", rs.getString("EMAILID"));
	        	valueMap.put("desc", rs.getString("DESCRIPTION"));
	        	valueList.add(valueMap);
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
	       
	        throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
	    }
	  }
  
	    
		
		
		
		return valueList;
		
		
	}
	
	
	
	public boolean isValidOlmId(String userOLMId) throws KmException {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;	
		boolean isValidUser = false;
		//KmCSRuserFormBean fb= new KmCSRuserFormBean();
		StringBuffer SQL_GET_OLM_ID_STATUS = new StringBuffer("select USER_LOGIN_ID, STATUS from KM_USER_MSTR where USER_LOGIN_ID= ? and STATUS='A'");
		
		try {
						
			if(!"".equals(userOLMId))
			{
				con = DBConnection.getDBConnection();
				ps = con.prepareStatement(SQL_GET_OLM_ID_STATUS.append(" with ur").toString());
				ps.setString(1, userOLMId);
				rs = ps.executeQuery();
				System.out.println("OLM ID Is="+userOLMId);
			if (rs.next()) {
					
					System.out.println("Hello world2");
					String status = rs.getString("STATUS");
					String olmid = rs.getString("USER_LOGIN_ID");
			
			
					
					if((status.equalsIgnoreCase("A"))&&(olmid.equals(userOLMId)))
					{
						System.out.println("Hello world3");
						System.out.println("isValidUser:"+isValidUser);
						isValidUser = true;
						
						
					}
				}
			//	ActionMessage msg1=new ActionMessage("csruser.invalid.olmid");
			}		
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Hi sadiqua");
			throw new KmException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Hi riju");
			throw new KmException(e.getMessage());
		} finally {
			try {
				DBConnection.releaseResources(con, ps, rs);
			} catch (Exception e) {
				throw new KmException(e.getMessage());
			}
		}
		
		return isValidUser;
	}
	
    }
  
	
	


