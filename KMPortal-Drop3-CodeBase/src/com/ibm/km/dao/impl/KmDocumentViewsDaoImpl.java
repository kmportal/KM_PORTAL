/*
 * Created on May 6, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ibm.km.common.PropertyReader;
import com.ibm.km.dao.DBConnection;
import com.ibm.km.dao.KmDocumentViewsDao;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;

/**
 * @author varunagg
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class KmDocumentViewsDaoImpl implements KmDocumentViewsDao {

	private static Logger logger = Logger.getLogger(KmDocumentViewsDaoImpl.class);
	// Code change due to the UAT observation
	protected static final String SQL_GET_VERSION_COUNT="select count(document_id)as VERSION_COUNT from Km_Document_mstr where element_id in(select element_id from KM_DOCUMENT_MSTR where document_id = ? ) and  approval_status = 'O'";
	
	protected static final String SQL_SHOW_DOCUMENT_VIEWS =" WITH nee(element_id,chain) AS  ( SELECT  ELEMENT_ID, cast(element_name as VARCHAR(" + PropertyReader.getAppValue("search.path.size") + "))  FROM KM_ELEMENT_MSTR WHERE element_id = ?  UNION ALL SELECT nplus1.element_id,nee.chain || '/' || nplus1.element_name  FROM KM_ELEMENT_MSTR as nplus1, nee WHERE nee.element_id=nplus1.PARENT_ID) SELECT a.document_id,b.DOCUMENT_DISPLAY_NAME,b.DOC_TYPE, b.document_path,nee.chain, count(1) AS COUNT FROM KM_DOCUMENT_VIEWS a inner join KM_DOCUMENT_MSTR b on a.DOCUMENT_ID=b.document_id inner join  nee on nee.element_id= b.element_id  WHERE cast(ACCESSED_ON as date) between cast(? as date) and cast(? as date)  and b.status='A' and b.approval_status='A' and (date(current timestamp) between date(publishing_start_dt) and date(publishing_end_dt)) ";
	 
	private Connection getConnection() throws KmException {

		logger.info("Entered getConnection for operation on table:KmDocumentViewsDaoImpl");

		try {
			return DBConnection.getDBConnection();
		} catch(DAOException e) {
			logger.info("Exception Occured while obtaining connection.");
			throw new KmException("Exception while trying to obtain a connection in KmFavoritesDaoImpl",e);
		}
   }		
	
   public List showDocumentViews(String elementId,String fromDate,String toDate) throws KmException {
		Connection con = null;
		PreparedStatement ps = null;
		
		ResultSet rs = null;
		
		List documentViewsList = new ArrayList();
		Map hm = null;
		StringBuffer query =new StringBuffer(SQL_SHOW_DOCUMENT_VIEWS);
		//String sql = SQL_SHOW_DOCUMENT_VIEWS;
		logger.info("Entered in showDocumentViews method");
		query.append(" GROUP BY b.DOCUMENT_DISPLAY_NAME,a.DOCUMENT_ID,b.DOC_TYPE,b.document_path,nee.chain order by count desc fetch first 50 rows only").toString();
		try {
			
			
			con = getConnection();
			ps = con.prepareStatement(query.append(" with ur").toString());
			ps.setInt(1, Integer.parseInt(elementId));
			ps.setString(2,fromDate);
			ps.setString(3,toDate);
			rs=ps.executeQuery();
			while (rs.next()) {
				hm=new HashMap();
				hm.put("DOCUMENT_ID", ""+rs.getInt("DOCUMENT_ID"));
				hm.put("DOCUMENT_NAME", rs.getString("DOCUMENT_DISPLAY_NAME"));
				logger.info(rs.getString("DOCUMENT_PATH"));
				String path=rs.getString("chain");
				logger.info(path);
				String documentStringPath="";
				if((path.indexOf("/")+1)<path.lastIndexOf("/"))	
					documentStringPath=path.substring((path.indexOf("/")+1),path.lastIndexOf("/"));
				
				hm.put("DOCUMENT_STRING_PATH",documentStringPath);
				hm.put("COUNT", rs.getString("COUNT"));
			/*	ps1=con.prepareStatement(SQL_GET_VERSION_COUNT);
				ps1.setInt(1, rs.getInt("DOCUMENT_ID"));
				rs1 = ps1.executeQuery();		 
				int versionCount=0;
				if(rs1.next()){
								versionCount=Integer.parseInt(rs1.getString("VERSION_COUNT"));
				} */
				hm.put("VERSION_COUNT","0");
				//System.out.println("DOctype="+rs.getInt("DOC_TYPE"));
				hm.put("DOC_TYPE",rs.getInt("DOC_TYPE"));
				documentViewsList.add(hm);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
			logger.error("SQLException occured while  showing DocumentViews" + "Exception Message: " + e.getMessage());
			throw new KmException("SQL Exception: " + e.getMessage(), e);
		} catch (Exception e) {
			
			logger.error(" Exception occured while showing DocumentViews." + "Exception Message: " + e.getMessage());
			throw new KmException(" Exception: " + e.getMessage(), e);
		} finally {
			try {
				if(rs!=null)
				rs.close();
				DBConnection.releaseResources(con,ps,rs);
			} catch (Exception e) {
				logger.error("DAO Exception occured while showing DocumentViews" + "Exception Message: " + e.getMessage());
				throw new KmException("DAO Exception: " + e.getMessage(), e);
			}
		}
	logger.info("Exit from showDocumentViews method");
		return documentViewsList;
	}
}