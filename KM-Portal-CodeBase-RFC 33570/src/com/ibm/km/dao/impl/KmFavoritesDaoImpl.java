/*
 * Created on May 2, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ibm.km.common.PropertyReader;
import com.ibm.km.dao.DBConnection;
import com.ibm.km.dao.KmFavoritesDao;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;

/**
 * @author varunagg
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class KmFavoritesDaoImpl implements KmFavoritesDao {
	
	private static Logger logger = Logger.getLogger(KmFavoritesDaoImpl.class);
	
	protected static final String SQL_GET_VERSION_COUNT="select count(document_id)as VERSION_COUNT from Km_Document_mstr where element_id in(select element_id from KM_DOCUMENT_MSTR where document_id = ? ) and  approval_status = 'O'";
	
	protected static final String SQL_GET_FAVORITES = " WITH nee(element_id,chain) AS ( SELECT  ELEMENT_ID, cast(element_name as VARCHAR(" + PropertyReader.getAppValue("search.path.size") + ")) FROM KM_ELEMENT_MSTR WHERE element_id = ? UNION ALL SELECT nplus1.element_id,nee.chain || '/' || nplus1.element_name 	FROM KM_ELEMENT_MSTR as nplus1, nee WHERE nee.element_id=nplus1.PARENT_ID) SELECT A.DOCUMENT_ID, B.DOCUMENT_DISPLAY_NAME,B.DOC_TYPE, B.DOCUMENT_PATH,nee.chain, A.UPDATED_ON, B.UPLOADED_DT,B.PUBLISHING_END_DT  FROM KM_FAVORITE_DOCUMENTS A INNER JOIN KM_DOCUMENT_MSTR B ON A.DOCUMENT_ID=B.DOCUMENT_ID INNER JOIN nee ON  nee.ELEMENT_ID =B.ELEMENT_ID   WHERE A.USER_ID=? AND   B.STATUS = 'A' and B.APPROVAL_STATUS = 'A' and (date(current timestamp) between date(publishing_start_dt) and date(publishing_end_dt))  ORDER BY UPDATED_ON DESC ";
	
	private Connection getConnection() throws KmException {

		logger.info("Entered getConnection for operation on table:KM_DOCUMENT_MSTR");

		try {
		return DBConnection.getDBConnection();
		}catch(DAOException e) {

		logger.info("Exception Occured while obtaining connection.");

		throw new KmException("Exception while trying to obtain a connection in KmFavoritesDaoImpl",e);
	}

   }		
	
	public List getFavorites(int userId,String circleId) throws KmException {
		Connection con = null;
		PreparedStatement ps = null;
		
		ResultSet rs = null;
		
		List favoritesList = new ArrayList();
		List favListId = new ArrayList();
		Map hm = null;
		logger.info("Entered in getFavorites method");
		try {
			//String sql = SQL_GET_FAVORITES;
			StringBuffer query =new StringBuffer(SQL_GET_FAVORITES);
			con = getConnection();
			ps = con.prepareStatement(query.append(" with ur ").toString());
			ps.setString(1, circleId);
			ps.setInt(2,userId);
			rs=ps.executeQuery();
			while (rs.next()) {
				hm=new HashMap();
				hm.put("DOCUMENT_ID", ""+rs.getInt("DOCUMENT_ID"));
				favListId.add(rs.getInt("DOCUMENT_ID"));
				hm.put("DOCUMENT_NAME", rs.getString("DOCUMENT_DISPLAY_NAME"));
				
//				Remove seconds and milliseconds from ever page wherever displayed : defect no. MASDB00060756
				
				String date =rs.getString("UPDATED_ON").substring(0,16); 
				logger.info("---------"+date.substring(0,16));
				hm.put("UPDATED_ON", date);
				hm.put("DOCUMENT_PATH", rs.getString("DOCUMENT_PATH"));
				
                //Added by Atul 		
                String published_dt="";
                published_dt=rs.getString("PUBLISHING_END_DT");
                if(!(published_dt.equalsIgnoreCase(""))) {
					published_dt=published_dt.substring(0,10);
                }
				hm.put("PUBLISHING_END_DT", published_dt);
				//Ended
				String path=rs.getString("chain");
				logger.info(path);
				String documentStringPath="";
				if((path.indexOf("/")+1)<path.lastIndexOf("/")){	
				    documentStringPath=path.substring((path.indexOf("/")+1),path.lastIndexOf("/"));
				}
				hm.put("DOCUMENT_STRING_PATH",documentStringPath);
			/*	ps1=con.prepareStatement(SQL_GET_VERSION_COUNT+" with ur ");
				ps1.setInt(1, rs.getInt("DOCUMENT_ID"));
				rs1 = ps1.executeQuery();		 
				int versionCount=0;
				if(rs1.next()){
								versionCount=Integer.parseInt(rs1.getString("VERSION_COUNT"));
				}  */
				hm.put("VERSION_COUNT","0");
				hm.put("DOC_TYPE",rs.getInt("DOC_TYPE"));
				favoritesList.add(hm);
			}
			//System.out.println("favListId >>>>>>>>>>>>" + favListId.size());
		}
		catch (SQLException e) {
			logger.error("SQL Exception occured while getting Favorites." + "Exception Message: " + e.getMessage());
			throw new KmException("SQL Exception: " + e.getMessage(), e);
		} catch (Exception e) {
			logger.error("Exception occured while getting Favorites." + "Exception Message: " + e.getMessage());
			throw new KmException(" Exception: " + e.getMessage(), e);
		} finally {
			try {
				if(rs!=null)
				rs.close();
				DBConnection.releaseResources(con,ps,rs);
			} catch (Exception e) {
				logger.error("DAOException occured while getting Favorites." + "Exception Message: " + e.getMessage());
				throw new KmException("DAO Exception: " + e.getMessage(), e);
			}
		}
		
		logger.info("Exit from  getFavorites method");
		return favoritesList;
	}
}
