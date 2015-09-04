/**
 * 
 */
package com.ibm.km.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ibm.km.dao.DBConnection;
import com.ibm.km.dao.KmPlanHitsDao;
import com.ibm.km.dto.KmBillPlanDto;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;
import com.ibm.km.services.KmElementMstrService;
import com.ibm.km.services.impl.KmElementMstrServiceImpl;

/**
 * @author Nehil Parashar
 *
 */
public class KmPlanHitsDaoImpl implements KmPlanHitsDao 
{
	private static Logger logger = Logger.getLogger(KmPlanHitsDaoImpl.class);
	
	private final String BILL_PLAN_HITS = "Select Distinct bp.DOCUMENT_ID, bp.Topic, dm.NUMBER_OF_HITS from KM_BP_DATA bp, KM_DOCUMENT_MSTR dm "
										  + " Where bp.DOCUMENT_ID = dm.DOCUMENT_ID and dm.DOCUMENT_PATH like ? order by dm.NUMBER_OF_HITS desc FETCH FIRST 15 ROWS ONLY with ur";
	
	private final String SINGLE_PLAN_DATA = "Select distinct TOPIC, bph.HEADER_NAME, CONTENT, PATH, from_dt, TO_DT, ELEMENT_NAME " + 
											" from KM_BP_DATA bp, KM_CIRCLE_MSTR cm , KM_BP_HEADER_MSTR bph, KM_ELEMENT_MSTR em" +
											" Where DOCUMENT_ID=? and bp.CIRCLE_ID=em.ELEMENT_ID and bp.HEADER=bph.HEADER_ID with ur";

	
	@Override
	public List<KmBillPlanDto> getPlanHitsDetails(String lobId) throws KmException
	{
	    Connection con = null;
	    ResultSet rs = null;
	    PreparedStatement ps = null;
	    
	    List<KmBillPlanDto> billPlanhitsList = new ArrayList<KmBillPlanDto>();
	    
	    try
	    {
	        con = DBConnection.getDBConnection();
	        ps = con.prepareStatement(BILL_PLAN_HITS);
	        
	        ps.setString(1,"%" + lobId + "%");
	        
	        logger.info(BILL_PLAN_HITS);
	        
	        rs = ps.executeQuery();
	        
	        while(rs.next())
	        {
	        	KmBillPlanDto aDto = new KmBillPlanDto();
	        	
	        	aDto.setDocumentId(rs.getInt(1));
	        	aDto.setBillPlan(rs.getString(2));
	        	
	        	billPlanhitsList.add(aDto);
	        }
	    }
	    catch(SQLException e)
	    {
	        throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
	    }
	    catch(Exception e)
	    {
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
	  
	  return billPlanhitsList;
	}

	@Override
	public List<KmBillPlanDto> getSinglePlanData(String documentId) throws KmException
	{
	    Connection con = null,newCon=null;
	    ResultSet rs = null;
	    PreparedStatement ps1 = null,ps2=null;
	    
	    List<KmBillPlanDto> billPlanList = new ArrayList<KmBillPlanDto>();
	    
	    try
	    {
	        con = DBConnection.getDBConnection();
	        ps1 = con.prepareStatement(SINGLE_PLAN_DATA);
	        ps1.setString(1, documentId);
	        
	        logger.info(SINGLE_PLAN_DATA);
	        rs = ps1.executeQuery();
	        
	        while(rs.next())
	        {
	        	KmBillPlanDto aDto = new KmBillPlanDto();
	        	    
	        	aDto.setTopic(rs.getString(1));
	        	aDto.setHeader(rs.getString(2));
	        	aDto.setContent(rs.getString(3));
	        	//
	        	String elements[]=rs.getString(4).split("/");
	        	String lastElement=elements[elements.length-1];
	        	KmElementMstrService elementService=new KmElementMstrServiceImpl();
	        	String elementPath1 = elementService.getServicePath(lastElement);
                logger.info(elementPath1);  
                //
	        	aDto.setPath(elementPath1);
	        	aDto.setFromDate(rs.getString(5));
	        	aDto.setToDate(rs.getString(6));
	        	aDto.setCircleName(rs.getString(7));
	        	
	        	billPlanList.add(aDto);
	        }
	    }
	    catch(SQLException e)
	    {
	        throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
	    }
	    catch(Exception e)
	    {
	    	throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
	    }
	    finally
	    {
	    	try
			{
	    		DBConnection.releaseResources(con, ps1, rs);
			}
			catch(DAOException e)
			{
				e.printStackTrace();
				throw new KmException(e.getMessage(), e);
			}
	    }
	    return billPlanList;
	}
}