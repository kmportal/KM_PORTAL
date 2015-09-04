/*
 * Created on Feb 13, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.ibm.km.common.Constants;
import com.ibm.km.common.PropertyReader;

import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.ibm.km.dao.DBConnection;
import com.ibm.km.dao.RejectedFileDao;
import com.ibm.km.dto.RejectedFileDto;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;
import com.ibm.km.exception.KmUserMstrDaoException;

/**
 * @author Vipin
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class RejectedFileDaoImpl implements RejectedFileDao{
	
	public static Logger logger = Logger.getLogger(RejectedFileDaoImpl.class);
	
	public static final String SQL_GET_FILE_LIST =  "WITH NEE(ELEMENT_ID,CHAIN) AS ( SELECT  ELEMENT_ID,  cast(ELEMENT_NAME as VARCHAR(" + PropertyReader.getAppValue("search.path.size") + "))  FROM KM_ELEMENT_MSTR  WHERE ELEMENT_ID = ?  UNION ALL  SELECT NPLUS1.ELEMENT_ID,NEE.CHAIN || '/' || NPLUS1.ELEMENT_NAME FROM KM_ELEMENT_MSTR AS NPLUS1, NEE WHERE NEE.ELEMENT_ID=NPLUS1.PARENT_ID) SELECT doc.DOCUMENT_ID,CHAIN,doc.DOCUMENT_NAME, doc.DOCUMENT_DISPLAY_NAME,doc.COMMENT  FROM KM_DOCUMENT_MSTR doc, KM_ELEMENT_MSTR ele, NEE  WHERE doc.ELEMENT_ID=ele.ELEMENT_ID AND doc.ELEMENT_ID=NEE.ELEMENT_ID   and doc.STATUS='A' and doc.APPROVAL_STATUS = 'R' and doc.UPLOADED_BY = ?   and cast(doc.UPLOADED_DT as date) between cast(? as date) and cast(? as date) ORDER BY doc.UPDATED_DT";
	
	//Bug MASDB00064530 reolved 
	public static final String SQL_GET_ADMIN_FILE_LIST ="WITH NEE(ELEMENT_ID,CHAIN) AS ( SELECT  ELEMENT_ID,  cast(ELEMENT_NAME as VARCHAR(" + PropertyReader.getAppValue("search.path.size") + "))  FROM KM_ELEMENT_MSTR  WHERE ELEMENT_ID = ?  UNION ALL  SELECT NPLUS1.ELEMENT_ID,NEE.CHAIN || '/' || NPLUS1.ELEMENT_NAME FROM KM_ELEMENT_MSTR AS NPLUS1, NEE WHERE NEE.ELEMENT_ID=NPLUS1.PARENT_ID) SELECT doc.DOCUMENT_ID,CHAIN,doc.DOCUMENT_NAME, doc.DOCUMENT_DISPLAY_NAME,doc.COMMENT FROM KM_DOCUMENT_MSTR doc, KM_ELEMENT_MSTR ele, NEE WHERE doc.ELEMENT_ID=ele.ELEMENT_ID AND doc.ELEMENT_ID=NEE.ELEMENT_ID  and doc.STATUS='A' and doc.APPROVAL_STATUS = 'R' and doc.UPDATED_BY = ?  and cast(doc.UPLOADED_DT as date) between cast(? as date) and cast(? as date) ORDER BY doc.DOCUMENT_ID";

	/* (non-Javadoc)
	 * @see com.ibm.km.dao.SubmitFileDao#getFileList(java.lang.String, java.lang.String)
	 */
	public ArrayList getFileList(String userId, String fromDate,String toDate, String root, String actorId)
		throws KmException {
			
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			RejectedFileDto dto;
			ArrayList fileList = new ArrayList();
			
			try
			{
				con = getConnection();
				StringBuffer query=new StringBuffer(SQL_GET_FILE_LIST);
				StringBuffer admin_query=new StringBuffer(SQL_GET_ADMIN_FILE_LIST);
				if(actorId.equals(Constants.CIRCLE_USER)){
					pstmt = con.prepareStatement(query.append(" with ur ").toString());
				}
				else if(actorId.equals(Constants.CIRCLE_ADMIN)){
					pstmt = con.prepareStatement(admin_query.append(" with ur ").toString());
				}
				pstmt.setInt(1, Integer.parseInt(root));
				pstmt.setInt(2,Integer.parseInt(userId));
				pstmt.setString(3,fromDate);
				pstmt.setString(4,toDate);
				
				rs = pstmt.executeQuery();
				
				while(rs.next())
				{
					dto = new RejectedFileDto();
					String path=rs.getString("chain");
					logger.info(path);
					String documentStringPath="";
					//Code change after UAT observation
					if((path.indexOf("/")+1)<path.lastIndexOf("/")){	
						 documentStringPath=path.substring((path.indexOf("/")+1),path.lastIndexOf("/"));
					}

					dto.setDocCompletePath(documentStringPath);
					dto.setDocName(rs.getString("DOCUMENT_DISPLAY_NAME"));
					dto.setDocumentId(rs.getString("DOCUMENT_ID"));
					dto.setComment(rs.getString("COMMENT"));
					fileList.add(dto);
				}
				return fileList;
				
			}
			catch(KmUserMstrDaoException km)
			{
				km.printStackTrace();
				throw new KmException(km.getMessage(),km);
			}
			catch(SQLException e)
			{
				e.printStackTrace();
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
					e.printStackTrace();
					throw new KmException(e.getMessage(),e);
				}
			}	
		
	}
	
	
	private Connection getConnection() throws KmUserMstrDaoException {

				//logger.info("Entered getConnection for operation on table:KM_USER_MSTR");

				try {
				return DBConnection.getDBConnection();
				}catch(DAOException e) {

				//logger.info("Exception Occured while obtaining connection.");

				throw new KmUserMstrDaoException("Exception while trying to obtain a connection",e);
			}

		   }
	
	
}
