/*
 * Created on Nov 28, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.dao.impl;

import java.util.ArrayList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.log4j.Logger;

import com.ibm.km.common.PropertyReader;
import com.ibm.km.dao.DBConnection;
import com.ibm.km.dao.KmHitCountDAO;
import com.ibm.km.dto.KmHitCountReportDto;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;
import com.ibm.km.exception.KmUserMstrDaoException;

/**
 * @author Atul
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class KmHitCountDaoImpl implements KmHitCountDAO {

	/* (non-Javadoc)
	 * @see com.ibm.km.dao.KmHitCountDAO#hitCountReport(java.lang.String)
	 */
	private static final Logger logger;

		static {

			logger = Logger.getLogger(KmHitCountDaoImpl.class);
		}
	protected static final String SQL_HIT_COUNT_REPORT = "WITH nee(element_id,chain) AS ( SELECT  ELEMENT_ID,  cast(element_name as VARCHAR(" + PropertyReader.getAppValue("search.path.size") + "))  FROM KM_ELEMENT_MSTR  WHERE element_id = ? 	UNION ALL  SELECT nplus1.element_id,nee.chain || '/' || nplus1.element_name	FROM KM_ELEMENT_MSTR as nplus1, nee WHERE nee.element_id=nplus1.PARENT_ID)SELECT a.document_id,b.DOCUMENT_DISPLAY_NAME, nee.chain, count(1) AS COUNT FROM KM_DOCUMENT_VIEWS a ,  KM_DOCUMENT_MSTR b, nee, KM_ELEMENT_MSTR c WHERE a.DOCUMENT_ID=b.document_id and nee.element_id= b.element_id	and b.element_id = c.element_id and b.status='A' and b.approval_status='A'	and  date(a.ACCESSED_ON) = ? GROUP BY b.DOCUMENT_DISPLAY_NAME,a.DOCUMENT_ID,nee.chain order by count desc "; 
		
	public ArrayList hitCountReport(String elementId, String date) throws KmException {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		KmHitCountReportDto dto = null;
		ArrayList aList = null;
			
		//System.out.println("elementId::::::::::::::::::::::"+elementId);
		//System.out.println("date::::::::::::::::::::::"+date);
		
		
		logger.info("Entered in hitCountReport method");
		try
		{
			StringBuffer query=new StringBuffer(SQL_HIT_COUNT_REPORT);
			con =  DBConnection.getDBConnection();
			pstmt = con.prepareStatement(query.append(" with ur ").toString());
			pstmt.setString(1,elementId);
			pstmt.setString(2, date);
			rs = pstmt.executeQuery();
			
			aList = new ArrayList();	
			while(rs.next()){
				dto=new KmHitCountReportDto();
				dto.setDocumentName(rs.getString("DOCUMENT_DISPLAY_NAME"));
				dto.setDocumentPath(rs.getString("CHAIN"));
				dto.setNoHits(rs.getString("COUNT"));
				aList.add(dto);
			}
			logger.info("Exit from hitCountReport method");
		}
			
		catch(KmUserMstrDaoException e)
		{
			logger.error("KmUserMstrDaoException occured in hitCountReport." + "Exception Message: " + e.getMessage());
			throw new KmException(e.getMessage(),e);
		}
		catch(SQLException e)
		{
			logger.error("SQLException occured in hitCountReport." + "Exception Message: " + e.getMessage());
			throw new KmException(e.getMessage(),e);
		}
		catch(DAOException e)
		{
			logger.error("DAOException occured in hitCountReport." + "Exception Message: " + e.getMessage());
			throw new KmException(e.getMessage(),e);
		}
		
		finally
		{
			try
			{
				DBConnection.releaseResources(con,pstmt,rs);
			}
			catch(DAOException e)
			{
				logger.error("DAOException occured in hitCountReport." + "Exception Message: " + e.getMessage());
				throw new KmException(e.getMessage(),e);
			}
		}
		return aList;
		
	}

}
