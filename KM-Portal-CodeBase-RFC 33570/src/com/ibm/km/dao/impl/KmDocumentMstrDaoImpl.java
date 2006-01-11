package com.ibm.km.dao.impl;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;

import com.ibm.km.common.PropertyReader;
import com.ibm.km.common.Utility;
import com.ibm.km.dao.DBConnection;
import com.ibm.km.dao.KmDocumentMstrDao;
import com.ibm.km.dto.DocumentTreeElement;
import com.ibm.km.dto.KmDocumentMstr;
import com.ibm.km.dto.KmSearch;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmDocumentMstrDaoException;
import com.ibm.km.exception.KmException;
import com.ibm.km.search.DeleteFiles;
public class KmDocumentMstrDaoImpl  implements KmDocumentMstrDao {


 /** 
	* Logger for this class. Use logger.log(message) for logging. Refer to @link http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/overview.html for logging options and configuration.
 **/ 

	private static Logger logger = Logger.getLogger(KmDocumentMstrDaoImpl.class);

	protected static final String SQL_EDIT_DOCUMENT ="UPDATE KM_DOCUMENT_MSTR SET DOCUMENT_KEYWORD = ?, DOCUMENT_DESC = ?, DOCUMENT_DISPLAY_NAME = ? ,PUBLISHING_END_DT =timestamp_format(?, 'YYYY-MM-DD HH24:MI:SS') WHERE DOCUMENT_ID = ?";
	
	protected static final String SQL_GET_DOCUMENT_DETAILS="WITH nee(element_id,chain) AS  ( SELECT  ELEMENT_ID, cast(element_name as VARCHAR(" + PropertyReader.getAppValue("search.path.size") + ")) FROM KM_ELEMENT_MSTR  WHERE element_id = ? UNION ALL SELECT nplus1.element_id,nee.chain || '/' || nplus1.element_name  FROM KM_ELEMENT_MSTR as nplus1, nee  WHERE nee.element_id=nplus1.PARENT_ID) SELECT CHAIN ,doc.DOCUMENT_ID, doc.DOCUMENT_NAME,doc.DOC_NAME,doc.DOCUMENT_DISPLAY_NAME,doc.DOCUMENT_KEYWORD,doc.DOCUMENT_DESC, doc.PUBLISHING_END_DT  FROM NEE ,  KM_DOCUMENT_MSTR doc WHERE doc.ELEMENT_ID = NEE.ELEMENT_ID AND doc.DOCUMENT_ID=?";
	
	protected static final String SQL_GET_ELEMENT_ID="SELECT ELEMENT_ID FROM KM_DOCUMENT_MSTR WHERE DOCUMENT_ID =? ";
	
	protected static final String SQL_GET_DOCUMENT_IDS="SELECT DOCUMENT_ID FROM KM_DOCUMENT_MSTR WHERE ( ELEMENT_ID = ?  ";
	
	protected static final String SQL_GET_DOCUMENT_BY_ELEMENT_ID = "SELECT * FROM KM_DOCUMENT_MSTR WHERE ELEMENT_ID = ?  ";

	protected static final String SQL_GET_DOCUMENT_NAME="SELECT DOCUMENT_NAME, DOCUMENT_ID FROM KM_DOCUMENT_MSTR WHERE ( DOCUMENT_ID = ?  ";
	 
	protected static final String SQL_PARENT_ID_STRING ="WITH NEE(ELEMENT_ID,CHAIN, ELEMENT_LEVEL_ID) AS (SELECT  ELEMENT_ID, CAST(CAST(ELEMENT_ID AS CHARACTER(5))AS VARCHAR(60)) ,ELEMENT_LEVEL_ID FROM KM_ELEMENT_MSTR  WHERE ELEMENT_ID =? UNION ALL SELECT NPLUS1.ELEMENT_ID, NEE.CHAIN || '/' || CAST(NPLUS1.ELEMENT_ID AS CHARACTER(5)), NPLUS1.ELEMENT_LEVEL_ID FROM KM_ELEMENT_MSTR AS NPLUS1, NEE WHERE NEE.ELEMENT_ID=NPLUS1.PARENT_ID) SELECT CHAIN AS PARENT_STRING,doc.DOCUMENT_NAME FROM NEE ,  KM_DOCUMENT_MSTR doc WHERE doc.ELEMENT_ID = NEE.ELEMENT_ID AND NEE.ELEMENT_ID=?";
	
	protected static final String SQL_UPDATE_PATH="UPDATE KM_DOCUMENT_MSTR SET DOCUMENT_PATH = ? WHERE (DOCUMENT_ID = ? ";
	 
	protected static final String SQL_SELECT_FILES =  "WITH nee(element_id,chain) AS ( SELECT  ELEMENT_ID, cast(element_name as VARCHAR(" + PropertyReader.getAppValue("search.path.size") + ")) FROM KM_ELEMENT_MSTR WHERE element_id = ? UNION ALL SELECT nplus1.element_id,nee.chain || '/' || nplus1.element_name FROM KM_ELEMENT_MSTR as nplus1, nee WHERE nee.element_id=nplus1.PARENT_ID) SELECT doc.DOCUMENT_ID, chain,doc.DOCUMENT_NAME, doc.DOCUMENT_DISPLAY_NAME, doc.APPROVAL_STATUS, doc.UPLOADED_DT,doc.APPROVAL_REJECTION_DT, doc.DOCUMENT_PATH, ele.ELEMENT_ID FROM KM_DOCUMENT_MSTR doc ,  KM_ELEMENT_MSTR ele, nee WHERE doc.STATUS ='A' AND doc.APPROVAL_STATUS!='O' AND doc.ELEMENT_ID=ele.ELEMENT_ID AND doc.element_id=nee.element_id  and ele.PARENT_ID = ? ORDER BY DOC.UPDATED_DT DESC";
			
	protected static final String SQL_SELECT_DOCUMENT = "SELECT DOCUMENT_ID, DOCUMENT_GROUP_ID, DOCUMENT_NAME, DOCUMENT_DISPLAY_NAME, DOCUMENT_DESC, SUB_CATEGORY_ID, CATEGORY_ID, CIRCLE_ID, NUMBER_OF_HITS, STATUS, APPROVAL_STATUS, UPLOADED_BY, UPLOADED_DT, UPDATED_BY, UPDATED_DT, APPROVAL_REJECTION_DT, APPROVAL_REJECTION_BY, PUBLISHING_START_DT, PUBLISHING_END_DT FROM KM_DOCUMENT_MSTR  WHERE KM_DOCUMENT_MSTR.STATUS ='A' AND DOCUMENT_ID = ? ";
	
	protected static final String SQL_KEYWORD_SEARCH = "SELECT DOCUMENT_ID, DOCUMENT_NAME, DOCUMENT_DISPLAY_NAME, doc.CIRCLE_ID,doc.CATEGORY_ID,doc.SUB_CATEGORY_ID, APPROVAL_STATUS, UPLOADED_DT,APPROVAL_REJECTION_DT, CATEGORY_NAME,SUB_CATEGORY_NAME FROM KM_DOCUMENT_MSTR doc ,  KM_CATEGORY_MSTR cat ,  KM_SUB_CATEGORY_MSTR subcat WHERE doc.STATUS ='A' AND doc.CATEGORY_ID=cat.CATEGORY_ID AND doc.SUB_CATEGORY_ID=subcat.SUB_CATEGORY_ID AND UPPER(doc.DOCUMENT_KEYWORD) LIKE ";
	
	protected static final String SQL_CSR_KEYWORD_SEARCH = "SELECT DOCUMENT_ID, DOCUMENT_NAME, DOCUMENT_DISPLAY_NAME, doc.CIRCLE_ID,doc.CATEGORY_ID,doc.SUB_CATEGORY_ID, APPROVAL_STATUS, UPLOADED_DT,APPROVAL_REJECTION_DT, CATEGORY_NAME,SUB_CATEGORY_NAME FROM KM_DOCUMENT_MSTR doc ,  KM_CATEGORY_MSTR cat ,  KM_SUB_CATEGORY_MSTR subcat WHERE doc.STATUS ='A' AND doc.CATEGORY_ID=cat.CATEGORY_ID AND doc.SUB_CATEGORY_ID=subcat.SUB_CATEGORY_ID AND doc.APPROVAL_STATUS = 'A' AND UPPER(doc.DOCUMENT_KEYWORD) LIKE ";
	
	protected static final String SQL_DELETE_DOCUMENT = "UPDATE KM_DOCUMENT_MSTR SET STATUS= 'D', UPDATED_DT= current timestamp, UPDATED_BY = ? WHERE DOCUMENT_ID = ? ";
	
	protected static final String SQL_DELETE_DOCUMENTS ="UPDATE KM_DOCUMENT_MSTR SET STATUS= 'D', UPDATED_DT= current timestamp, UPDATED_BY = ? WHERE (DOCUMENT_ID = ? ";
    
	protected static final String SQL_INSERT_WITH_ID = "INSERT INTO KM_DOCUMENT_MSTR (DOCUMENT_ID, DOCUMENT_GROUP_ID, DOCUMENT_NAME, DOCUMENT_DISPLAY_NAME, DOCUMENT_DESC, SUB_CATEGORY_ID, CATEGORY_ID, CIRCLE_ID, NUMBER_OF_HITS, STATUS, APPROVAL_STATUS, UPLOADED_BY, UPLOADED_DT, UPDATED_BY, UPDATED_DT, APPROVAL_REJECTION_DT, APPROVAL_REJECTION_BY, PUBLISHING_START_DT, PUBLISHING_END_DT,APPROVAL_REJECTION_BY) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	protected static final String SQL_SELECT = "SELECT DOCUMENT_ID, DOCUMENT_GROUP_ID, DOCUMENT_NAME, DOCUMENT_DISPLAY_NAME, DOCUMENT_DESC, SUB_CATEGORY_ID, CATEGORY_ID, CIRCLE_ID, NUMBER_OF_HITS, STATUS, APPROVAL_STATUS, UPLOADED_BY, UPLOADED_DT, UPDATED_BY, UPDATED_DT, APPROVAL_REJECTION_DT, APPROVAL_REJECTION_BY, PUBLISHING_START_DT, PUBLISHING_END_DT FROM KM_DOCUMENT_MSTR ";
//added by vishwas for copy document
	protected static final String SQL_SELECT2 = "SELECT DOCUMENT_ID, DOCUMENT_GROUP_ID, DOCUMENT_NAME, DOCUMENT_DISPLAY_NAME, DOCUMENT_DESC, SUB_CATEGORY_ID, CATEGORY_ID, CIRCLE_ID,NUMBER_OF_HITS, STATUS, APPROVAL_STATUS, UPLOADED_BY, UPLOADED_DT, UPDATED_BY, UPDATED_DT, APPROVAL_REJECTION_DT, APPROVAL_REJECTION_BY,PUBLISHING_START_DT, PUBLISHING_END_DT,ELEMENT_ID,DOC_TYPE,DOCUMENT_KEYWORD,DOC_NAME FROM KM_DOCUMENT_MSTR";
	//end by vishwas for copy document

	protected static final String SQL_UPDATE = "UPDATE KM_DOCUMENT_MSTR SET DOCUMENT_ID = ?, DOCUMENT_GROUP_ID = ?, DOCUMENT_NAME = ?, DOCUMENT_DISPLAY_NAME = ?, DOCUMENT_DESC = ?, SUB_CATEGORY_ID = ?, CATEGORY_ID = ?, CIRCLE_ID = ?, NUMBER_OF_HITS = ?, STATUS = ?, APPROVAL_STATUS = ?, UPLOADED_BY = ?, UPLOADED_DT = ?, UPDATED_BY = ?, UPDATED_DT = ?, APPROVAL_REJECTION_DT = ?, APPROVAL_REJECTION_BY = ?, PUBLISHING_START_DT = ?, PUBLISHING_END_DT = ? WHERE DOCUMENT_ID = ?";

	protected static final String SQL_DELETE = "DELETE FROM KM_DOCUMENT_MSTR WHERE DOCUMENT_ID = ?";
    
	protected static final String SQL_CIRCLE_DOCUMENT_LIST = " SELECT A.DOCUMENT_ID, A.DOCUMENT_GROUP_ID, A.DOCUMENT_NAME, A.DOCUMENT_DISPLAY_NAME,A.DOCUMENT_DESC, C.SUB_CATEGORY_ID, C.SUB_CATEGORY_NAME, B.CATEGORY_ID, B.CATEGORY_NAME, D.CIRCLE_ID, D.CIRCLE_NAME, NUMBER_OF_HITS FROM KM_DOCUMENT_MSTR A, KM_CATEGORY_MSTR B, KM_SUB_CATEGORY_MSTR C, KM_CIRCLE_MSTR D WHERE A.CIRCLE_ID=D.CIRCLE_ID AND A.CATEGORY_ID=B.CATEGORY_ID AND A.SUB_CATEGORY_ID=C.SUB_CATEGORY_ID AND A.STATUS='A' AND A.APPROVAL_STATUS='A' AND ( A.CIRCLE_ID=?";
	
															//+	" AND DATE(CURRENT TIMESTAMP) >=DATE(A.PUBLISHING_START_DT) "
															//+	" AND DATE(CURRENT TIMESTAMP-5 DAY) <= DATE(A.PUBLISHING_END_DT)"
															//+	" ) ORDER BY B.CATEGORY_NAME, C.SUB_CATEGORY_NAME";
															
	protected static final String SQL_FAVORITE_INSERT = "INSERT INTO KM_FAVORITE_DOCUMENTS (USER_ID, DOCUMENT_ID, UPDATED_ON) VALUES (?, ?, CURRENT TIMESTAMP)";
														
	protected static final String SQL_FAVORITE_DELETE = "DELETE FROM KM_FAVORITE_DOCUMENTS WHERE USER_ID=? AND DOCUMENT_ID=?";
	
	protected static final String SQL_FAVORITE_CHECK = "SELECT COUNT(1) FROM KM_FAVORITE_DOCUMENTS WHERE USER_ID=? AND DOCUMENT_ID=?";
	
	protected static final String SQL_DOCUMENT_VIEW_INSERT = "INSERT INTO KM_DOCUMENT_VIEWS (DOCUMENT_ID, ACCESSED_ON, USER_ID) VALUES (?, CURRENT TIMESTAMP, ?)";
	
	protected static final String SQL_GET_CHILD_ELEMENTS =" Select X.ELEMENT_ID, X.ELEMENT_NAME ,X.PARENT_ID ,X.ELEMENT_LEVEL_ID,X.DOCUMENT_ID ,X.APPROVAL_STATUS,X.STATUS,X.DOC,X.PUBLISHING_START_DT,X.PUBLISHING_END_DT from (SELECT A.ELEMENT_ID , 	A.ELEMENT_NAME ,A.PARENT_ID ,A.ELEMENT_LEVEL_ID, B.DOCUMENT_ID ,B.APPROVAL_STATUS,A.STATUS,B.STATUS as Doc,B.PUBLISHING_START_DT,B.PUBLISHING_END_DT FROM KM_ELEMENT_MSTR A LEFT JOIN KM_DOCUMENT_MSTR B ON A.ELEMENT_ID=B.ELEMENT_ID where A.STATUS='A' and  A.PARENT_ID = ?	ORDER BY A.ELEMENT_LEVEL_ID DESC) 	X where	(X.APPROVAL_STATUS='A' or X.APPROVAL_STATUS is null) and (X.DOC='A' or X.DOC is null) and ((Date(current timestamp) between Date(X.PUBLISHING_START_DT) and Date(X.PUBLISHING_END_DT)) or X.PUBLISHING_START_DT is null  )";
	/*Changes made for phase 3*/	
//	protected static final String SQL_GET_TREE="WITH NEE(ELEMENT_ID,CHAIN, ELEMENT_LEVEL_ID,element_name,parent_id,status) AS (SELECT  ELEMENT_ID, CAST(ELEMENT_NAME AS VARCHAR(350))ELEMENT_NAME ,ELEMENT_LEVEL_ID,element_name,parent_id,status FROM KM_ELEMENT_MSTR  WHERE ELEMENT_ID =?  UNION ALL  SELECT NPLUS1.ELEMENT_ID, NEE.CHAIN || '>' || NPLUS1.ELEMENT_NAME, NPLUS1.ELEMENT_LEVEL_ID , NPLUS1.element_name,NPLUS1.parent_id,NPLUS1.status FROM KM_ELEMENT_MSTR AS NPLUS1, NEE  WHERE NEE.ELEMENT_ID=NPLUS1.PARENT_ID ) SELECT nee.element_id,nee.chain,nee.element_name,nee.parent_id,nee.element_level_id,b.document_id,b.status,nee.status as element_status ,b.approval_status,publishing_start_dt, b.publishing_end_dt FROM NEE LEFT JOIN KM_DOCUMENT_MSTR B ON nee.ELEMENT_ID=B.ELEMENT_ID where  (b.status='A' or b.status is null) and " + "(b.approval_status='A' or b.approval_status is null) and nee.status='A' and (date(current timestamp) between date(publishing_start_dt) and date(publishing_end_dt) or publishing_start_dt is null or publishing_end_dt is null) order by nee.chain ";
	protected static final String SQL_GET_TREE="WITH NEE(ELEMENT_ID,CHAIN, ELEMENT_LEVEL_ID,element_name,parent_id,status) AS (SELECT  ELEMENT_ID, cast(ELEMENT_NAME as VARCHAR(" + PropertyReader.getAppValue("search.path.size") + ")) ELEMENT_NAME ,ELEMENT_LEVEL_ID,element_name,parent_id,status FROM KM_ELEMENT_MSTR  WHERE ELEMENT_ID =?  UNION ALL  SELECT NPLUS1.ELEMENT_ID, NEE.CHAIN || '>' || NPLUS1.ELEMENT_NAME, NPLUS1.ELEMENT_LEVEL_ID , NPLUS1.element_name,NPLUS1.parent_id,NPLUS1.status FROM KM_ELEMENT_MSTR AS NPLUS1, NEE  WHERE NEE.ELEMENT_ID=NPLUS1.PARENT_ID ) SELECT nee.element_id,nee.chain,nee.element_name,nee.parent_id,nee.element_level_id,b.document_id,b.DOC_TYPE,b.status,nee.status as element_status ,b.approval_status,publishing_start_dt, b.publishing_end_dt FROM NEE LEFT JOIN KM_DOCUMENT_MSTR B ON nee.ELEMENT_ID=B.ELEMENT_ID where  (b.status='A' or b.status is null) and " + "(b.approval_status='A' or b.approval_status is null) and nee.status='A' and (date(current timestamp) between date(publishing_start_dt) and date(publishing_end_dt) or publishing_start_dt is null or publishing_end_dt is null) order by nee.chain ";
	/*Changes made for phase 3*/	
	protected static final String  SQL_GET_DOCUMENT_ID= "SELECT DOCUMENT_ID FROM KM_DOCUMENT_MSTR WHERE DOCUMENT_NAME=?";
	
	protected static final String SQL_GET_DOCUMENT_VERSIONS="select document_name,document_display_name,document_id ,APPROVAL_STATUS from Km_Document_mstr where element_id in(select element_id from KM_DOCUMENT_MSTR where document_id = ? ) and  approval_status = 'O'";
	
	protected static final String SQL_GET_CHILDREN_DOCUMENTS="SELECT doc.DOCUMENT_ID FROM KM_DOCUMENT_MSTR doc, KM_ELEMENT_MSTR ele WHERE ele.ELEMENT_ID=doc.ELEMENT_ID and ele.PARENT_ID = ? ";
	
	protected static final String SQL_GET_CIRCLE_APPROVER="SELECT CIRCLE_ADMIN_ID FROM KM_ESCALATION_MATRIX WHERE CIRCLE_ID = ? ";
	
	protected static final String SQL_GET_DOC_PATH="select DOCUMENT_NAME,DOCUMENT_PATH FROM KM_DOCUMENT_MSTR WHERE DOCUMENT_ID = ? ";
	
	protected static final String SQL_GET_DOCUMENT="select * FROM KM_DOCUMENT_MSTR WHERE DOCUMENT_ID = ?";
	
	protected static final String SQL_IS_FAV_DOCUMENT = "select * from KM_FAVORITE_DOCUMENTS where document_id =? and  user_id = ? with ur";
	
	protected static final String SQL_CHANGE_DOCUMENT_PATHS="UPDATE KM_DOCUMENT_MSTR SET DOCUMENT_PATH = ? WHERE ( ELEMENT_ID = ? ";
	
	protected static final String SQL_UPDATE_DOC_PATH="UPDATE KM_DOCUMENT_MSTR SET DOCUMENT_PATH = ? WHERE ELEMENT_ID = ? "; 
	
	protected static final String SQL_GET_ALL_DOCUMENT_PATHS = "WITH NEE(ELEMENT_ID,CHAIN, ELEMENT_LEVEL_ID) AS (SELECT  ELEMENT_ID, CAST(CAST(ELEMENT_ID AS CHARACTER(5))AS VARCHAR(60)) ,ELEMENT_LEVEL_ID FROM KM_ELEMENT_MSTR  WHERE ELEMENT_ID =1 UNION ALL SELECT NPLUS1.ELEMENT_ID, NEE.CHAIN || '/' || CAST(NPLUS1.ELEMENT_ID AS CHARACTER(5)), NPLUS1.ELEMENT_LEVEL_ID FROM KM_ELEMENT_MSTR AS NPLUS1, NEE WHERE NEE.ELEMENT_ID=NPLUS1.PARENT_ID) SELECT DISTINCT(CHAIN) AS PARENT_STRING,doc.DOCUMENT_ID FROM NEE ,  KM_DOCUMENT_MSTR doc WHERE doc.ELEMENT_ID = NEE.ELEMENT_ID AND ( NEE.ELEMENT_ID = ? ";
		
	protected static final String UPDATE_ALL_DOCUMENT_PATHS = "UPDATE KM_DOCUMENT_MSTR SET DOCUMENT_PATH  = ? WHERE  ELEMENT_ID = ? ";
	
	/* new km phase 2 queries */
	protected static final String SQL_SELECT_KEYSEARCH_FILES = "WITH nee(element_id,chain) AS  ( SELECT  ELEMENT_ID, cast(element_name as VARCHAR(" + PropertyReader.getAppValue("search.path.size") + ")) FROM KM_ELEMENT_MSTR  WHERE element_id = ?  UNION ALL  SELECT nplus1.element_id,nee.chain || '/' || nplus1.element_name FROM KM_ELEMENT_MSTR as nplus1, nee WHERE nee.element_id=nplus1.PARENT_ID) SELECT doc.DOCUMENT_ID, chain,doc.DOCUMENT_NAME, doc.PUBLISHING_START_DT,doc.PUBLISHING_END_DT,doc.DOCUMENT_DISPLAY_NAME, doc.APPROVAL_STATUS, doc.UPLOADED_DT,doc.APPROVAL_REJECTION_DT, doc.DOCUMENT_PATH, ele.ELEMENT_ID,doc.UPDATED_DT FROM KM_DOCUMENT_MSTR doc ,  KM_ELEMENT_MSTR ele, nee WHERE doc.STATUS ='A' AND doc.ELEMENT_ID=ele.ELEMENT_ID AND doc.element_id=nee.element_id ";
	//added by vishwas for copy document
	public static final String SQL_INSERT_FILE_INFO ="INSERT INTO KM_DOCUMENT_MSTR(DOCUMENT_ID, DOCUMENT_GROUP_ID, DOCUMENT_NAME,DOC_NAME,DOCUMENT_DISPLAY_NAME, DOCUMENT_DESC,DOCUMENT_KEYWORD,NUMBER_OF_HITS, STATUS, APPROVAL_STATUS, UPLOADED_BY, UPLOADED_DT, UPDATED_BY, UPDATED_DT, APPROVAL_REJECTION_BY, PUBLISHING_START_DT, PUBLISHING_END_DT, DOCUMENT_PATH, CATEGORY_ID, SUB_CATEGORY_ID, CIRCLE_ID,ELEMENT_ID, DOC_TYPE) VALUES(?, 0, ?, ?, ?, ?, ?, 0, 'A', ?, ?, current timestamp, ?, current timestamp, 0, timestamp_format(?, 'YYYY-MM-DD HH24:MI:SS'), timestamp_format(?, 'YYYY-MM-DD HH24:MI:SS') , ?, 1, 1, 1,?, ?)";
	//end by vishwas for copy document
	/* KM Phase II  query for reteiving archived files */
	
	protected static final String SQL_SELECTARCHIVED_FILES ="WITH nee(element_id,chain) AS ( SELECT  ELEMENT_ID, cast(element_name as VARCHAR(" + PropertyReader.getAppValue("search.path.size") + ")) FROM KM_ELEMENT_MSTR WHERE element_id = ? UNION ALL  SELECT nplus1.element_id,nee.chain || '/' || nplus1.element_name FROM KM_ELEMENT_MSTR as nplus1, nee  WHERE nee.element_id=nplus1.PARENT_ID) select ele.element_id,doc.document_id,doc.DOCUMENT_DISPLAY_NAME,nee.chain ,doc.publishing_end_dt from KM_DOCUMENT_MSTR doc , KM_ELEMENT_MSTR ele, nee where ele.element_id=doc.element_id and nee.element_id = ele.element_id    and ele.element_level_id=0 and date(PUBLISHING_END_DT)< date (current timestamp) order by publishing_end_dt desc";
	 
	protected static final String SQL_DOC_TYPE_FILTER = "SELECT DOCUMENT_ID FROM KM_DOCUMENT_MSTR WHERE DOC_TYPE = ? AND DOCUMENT_ID IN ( "; 
	/* Top 15 post paid plan */
	
	/*protected static final String SQL_TOP15_BILL_PLANS = "SELECT   distinct d.DOCUMENT_ID FROM KM_DOCUMENT_MSTR d, KM_DOCUMENT_VIEWS v WHERE DOC_TYPE=6 and STATUS='A' and  current date between date(PUBLISHING_START_DT) and date(PUBLISHING_END_DT)"
	+"and d.DOCUMENT_ID =v.DOCUMENT_ID and ACCESSED_ON_dt > current date - ? days fetch first 15 rows only" ;
	*/
	
	protected static final String SQL_TOP15_BILL_PLANS = "select DOCUMENT_ID from (SELECT   distinct d.DOCUMENT_ID,NUMBER_OF_HITS FROM KM_DOCUMENT_MSTR d, KM_DOCUMENT_VIEWS v WHERE DOC_TYPE=6 and STATUS='A' "+ 
	   " and  current date between date(PUBLISHING_START_DT) and date(PUBLISHING_END_DT) and d.DOCUMENT_ID =v.DOCUMENT_ID and ACCESSED_ON_dt > current date - ? days order by NUMBER_OF_HITS desc  fetch first 15 rows only) with ur ";
	
	/* Check number of SMS sent to the user*/
	
	protected static final String SQL_NUMBER_OF_SMS ="select COUNT(*) as SMSCOUNT from KM_SMS_REPORT_DATA where DATE(SMSCREATEDDATE) = DATE(CURRENT TIMESTAMP) AND MOBILENO = ?";
	
	protected static final String SQL_MAX_NUMBER_OF_SMS ="select * from KM_CONFIGURESMSCOUNT";
				
	public  int insert(KmDocumentMstr dto) throws KmDocumentMstrDaoException {

		logger.debug("Entered insert for table KM_DOCUMENT_MSTR");

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		int rowsUpdated = 0;
		try {
			StringBuffer query =new StringBuffer(SQL_INSERT_WITH_ID);
			//String sql = SQL_INSERT_WITH_ID;
			con = getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement(query.append(" with ur").toString());
			int paramCount = 1;
			ps.setString(paramCount++,  dto.getDocumentId());
			ps.setString(paramCount++,  dto.getDocumentGroupId());
			ps.setString(paramCount++,  dto.getDocumentName());
			ps.setString(paramCount++,  dto.getDocumentDisplayName());
			ps.setString(paramCount++,  dto.getDocumentDesc());
			ps.setString(paramCount++,  dto.getSubCategoryId());
			ps.setString(paramCount++,  dto.getCategoryId());
			ps.setString(paramCount++,  dto.getCircleId());
			ps.setString(paramCount++,  dto.getNumberOfHits());
			ps.setString(paramCount++,  dto.getStatus());
			ps.setString(paramCount++,  dto.getApprovalStatus());
			ps.setString(paramCount++,  dto.getUploadedBy());
			ps.setTimestamp(paramCount++,  dto.getUploadedDt());
			ps.setString(paramCount++,  dto.getUpdatedBy());
			ps.setTimestamp(paramCount++,  dto.getUpdatedDt());
			ps.setTimestamp(paramCount++,  dto.getApprovalRejectionDt());
			ps.setString(paramCount++,  dto.getApprovalRejectionBy());
			ps.setTimestamp(paramCount++,  dto.getPublishingStartDt());
			ps.setTimestamp(paramCount++,  dto.getPublishingEndDt());
			ps.setString(paramCount++, dto.getApprovalRejectionBy());
			rowsUpdated=ps.executeUpdate();
			con.commit();

		logger.info("Row insertion successful on table: Inserted:" + rowsUpdated  + " rows. user:" + dto.getUploadedBy() );

		} catch (SQLException e) {
			
		logger.error("SQL Exception occured while inserting." + "Exception Message: " + e.getMessage());
			throw new KmDocumentMstrDaoException("SQLException: " + e.getMessage(), e);
		} catch (Exception e) {
			
		logger.error("Exception occured while inserting." + "Exception Message: " + e.getMessage());
			throw new KmDocumentMstrDaoException("Exception: " + e.getMessage(), e);
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

		return rowsUpdated;
	}

	public  KmDocumentMstr findByPrimaryKey(String documentId) throws KmDocumentMstrDaoException {

		logger.debug("Entered findByPrimaryKey for table:KM_DOCUMENT_MSTR");

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			StringBuffer query =new StringBuffer(SQL_SELECT);
			//String sql = SQL_SELECT + " WHERE DOCUMENT_ID = ? ";
			query.append(" WHERE DOCUMENT_ID = ? ").toString();
			con = getConnection();
			ps = con.prepareStatement(query.append(" with ur").toString());
			ps.setString(1, documentId);
			rs = ps.executeQuery();
			return fetchSingleResult(rs);
		} catch (SQLException e) {
			
			logger.error("SQL Exception occured while find." + "Exception Message: " + e.getMessage());
			throw new KmDocumentMstrDaoException("SQLException: " + e.getMessage(), e);
		} catch (Exception e) {
			
			logger.error("Exception occured while find." + "Exception Message: " + e.getMessage());
			throw new KmDocumentMstrDaoException("Exception: " + e.getMessage(), e);
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
			
			}
		}

	}
//added by vishwas for copy document
	public  KmDocumentMstr findByPrimaryKeyforcopy(String documentId) throws KmDocumentMstrDaoException {

		logger.debug("Entered findByPrimaryKey for table:KM_DOCUMENT_MSTR");

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		KmDocumentMstr kdm=null;
		
		try {
			StringBuffer query =new StringBuffer(SQL_SELECT2);
			//String sql = SQL_SELECT + " WHERE DOCUMENT_ID = ? ";
			query.append(" WHERE DOCUMENT_ID = ? ").toString();
			con = getConnection();
			ps = con.prepareStatement(query.append(" with ur").toString());
			ps.setString(1, documentId);
			rs = ps.executeQuery();
			while(rs.next())
			{
				 kdm=new KmDocumentMstr();
				kdm.setDocumentId(rs.getString(1));
				kdm.setDocumentGroupId(rs.getString(2));
				kdm.setDocName(rs.getString(3));
				kdm.setDocumentDisplayName(rs.getString(4));
				kdm.setDocumentDesc(rs.getString(5));
				kdm.setSubCategoryId(rs.getString(6));
				kdm.setCategoryId(rs.getString(7));
				kdm.setCircleId(rs.getString(8));
				kdm.setNumberOfHits(rs.getString(9));
				kdm.setStatus(rs.getString(10));
				kdm.setApprovalStatus(rs.getString(11));
				kdm.setUploadedBy(rs.getString(12));
				kdm.setUploadedDate(rs.getString(13));
				kdm.setUpdatedBy(rs.getString(14));
				kdm.setUpdatedDt(rs.getTimestamp(15));
				kdm.setApprovalRejectionDate(rs.getString(16));
				kdm.setApprovalRejectionBy(rs.getString(17));
				kdm.setPublishingStartDate(rs.getString(18));
				kdm.setPublishingEndDt(rs.getTimestamp(19));
				kdm.setElementId(rs.getString(20));
				kdm.setDocType(rs.getInt(21));
				kdm.setKeyword(rs.getString(22));
				kdm.setDocumentName(rs.getString(23));
				
			}
			return kdm;
		} catch (SQLException e) {
			
			logger.error("SQL Exception occured while find." + "Exception Message: " + e.getMessage());
			throw new KmDocumentMstrDaoException("SQLException: " + e.getMessage(), e);
		} catch (Exception e) {
			
			logger.error("Exception occured while find." + "Exception Message: " + e.getMessage());
			throw new KmDocumentMstrDaoException("Exception: " + e.getMessage(), e);
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
			
			}
		}

	}

	//end by vishwas for copy document
	
	public  int update(KmDocumentMstr dto) throws KmDocumentMstrDaoException {

		logger.info("Entered update for table KM_DOCUMENT_MSTR");

		Connection con = null;
		PreparedStatement ps = null;
		int numRows = -1;

		try {
			//String sql = SQL_UPDATE;
			StringBuffer query =new StringBuffer(SQL_UPDATE);
			con = getConnection();
			ps = con.prepareStatement(query.append(" with ur").toString());
			if (dto.getDocumentId() ==null)
				ps.setNull(1, Types.DECIMAL);
			else
				ps.setString(1,  dto.getDocumentId());
			if (dto.getDocumentGroupId() ==null)
				ps.setNull(2, Types.DECIMAL);
			else
				ps.setString(2,  dto.getDocumentGroupId());
			if (dto.getDocumentName() ==null)
				ps.setNull(3, Types.VARCHAR);
			else
				ps.setString(3,  dto.getDocumentName());
			if (dto.getDocumentDisplayName() ==null)
				ps.setNull(4, Types.VARCHAR);
			else
				ps.setString(4,  dto.getDocumentDisplayName());
			if (dto.getDocumentDesc() ==null)
				ps.setNull(5, Types.VARCHAR);
			else
				ps.setString(5,  dto.getDocumentDesc());
			if (dto.getSubCategoryId() ==null)
				ps.setNull(6, Types.DECIMAL);
			else
				ps.setString(6,  dto.getSubCategoryId());
			if (dto.getCategoryId() ==null)
				ps.setNull(7, Types.DECIMAL);
			else
				ps.setString(7,  dto.getCategoryId());
			if (dto.getCircleId() ==null)
				ps.setNull(8, Types.DECIMAL);
			else
				ps.setString(8,  dto.getCircleId());
			if (dto.getNumberOfHits() ==null)
				ps.setNull(9, Types.DECIMAL);
			else
				ps.setString(9,  dto.getNumberOfHits());
			if (dto.getStatus() ==null)
				ps.setNull(10, Types.CHAR);
			else
				ps.setString(10,  dto.getStatus());
			if (dto.getApprovalStatus() ==null)
				ps.setNull(11, Types.CHAR);
			else
				ps.setString(11,  dto.getApprovalStatus());
			if (dto.getUploadedBy() ==null)
				ps.setNull(12, Types.DECIMAL);
			else
				ps.setString(12,  dto.getUploadedBy());
			if (dto.getUploadedDt() ==null)
				ps.setNull(13, Types.TIMESTAMP);
			else
				ps.setTimestamp(13,  dto.getUploadedDt());
			if (dto.getUpdatedBy() ==null)
				ps.setNull(14, Types.DECIMAL);
			else
				ps.setString(14,  dto.getUpdatedBy());
			if (dto.getUpdatedDt() ==null)
				ps.setNull(15, Types.TIMESTAMP);
			else
				ps.setTimestamp(15,  dto.getUpdatedDt());
			if (dto.getApprovalRejectionDt() ==null)
				ps.setNull(16, Types.TIMESTAMP);
			else
				ps.setTimestamp(16,  dto.getApprovalRejectionDt());
			if (dto.getApprovalRejectionBy() ==null)
				ps.setNull(17, Types.DECIMAL);
			else
				ps.setString(17,  dto.getApprovalRejectionBy());
			if (dto.getPublishingStartDt() ==null)
				ps.setNull(18, Types.TIMESTAMP);
			else
				ps.setTimestamp(18,  dto.getPublishingStartDt());
			if (dto.getPublishingEndDt() ==null)
				ps.setNull(19, Types.TIMESTAMP);
			else
				ps.setTimestamp(19,  dto.getPublishingEndDt());
			ps.setString(20,  dto.getDocumentId());
			numRows = ps.executeUpdate();

		logger.info("Update successful on table:KM_DOCUMENT_MSTR. Updated:" + numRows  + " rows");

		} catch (SQLException e) {
			
			logger.error("SQL Exception occured while update." + "Exception Message: " + e.getMessage());
			  throw new KmDocumentMstrDaoException("SQLException: " + e.getMessage(), e);
		} catch (Exception e) {
			
			logger.error("Exception occured while update." + "Exception Message: " + e.getMessage());
			throw new KmDocumentMstrDaoException("Exception: " + e.getMessage(), e);
		} finally {
			try {
					if (ps != null)
						ps.close();
					if (con != null) {
						con.close();
					}
				} catch (Exception e) {
				
			}
		}
		return numRows;
	}

	public  int delete(String documentId) throws KmDocumentMstrDaoException {

		logger.info("Entered delete for table KM_DOCUMENT_MSTR");

		Connection con = null;
		PreparedStatement ps = null;
		int numRows = -1;

		try {
			//String sql = SQL_DELETE;
			StringBuffer query =new StringBuffer(SQL_DELETE);
			con = getConnection();
			ps = con.prepareStatement(query.append(" with ur").toString());
			ps.setString(1, documentId);
			numRows = ps.executeUpdate();

		logger.info("Delete successful on table:KM_DOCUMENT_MSTR. Deleted:" + numRows  + " rows");

		} catch (SQLException e) {
			

		logger.error("SQL Exception occured while delete." + "Exception Message: " + e.getMessage());
			throw new KmDocumentMstrDaoException("SQLException: " + e.getMessage(), e);
		} catch (Exception e) {
			

		logger.error("Exception occured while delete." + "Exception Message: " + e.getMessage());
			throw new KmDocumentMstrDaoException("Exception: " + e.getMessage(), e);
		} finally {
			try {
					if (ps != null)
						ps.close();
					if (con != null) {
						con.close();
					}
				} catch (Exception e) {
					
				}
		}
		return numRows;
	}

	protected  KmDocumentMstr[] fetchMultipleResults(ResultSet rs) throws SQLException {
		ArrayList results = new ArrayList();
		while (rs.next()) {
			KmDocumentMstr dto = new KmDocumentMstr();
			populateDto(dto, rs);
			results.add(dto);
		}
		KmDocumentMstr retValue[] = new KmDocumentMstr[results.size()];
		results.toArray(retValue);
		return retValue;
	}


	protected  KmDocumentMstr fetchSingleResult(ResultSet rs) throws SQLException {
		if (rs.next()) {
			KmDocumentMstr dto = new KmDocumentMstr();
			populateDto(dto, rs);
			return dto;
		} else 
			return null;
	}

	protected static void populateDto(KmDocumentMstr dto, ResultSet rs) throws SQLException {
		String docPath = rs.getString("CIRCLE_ID") + "/" + rs.getString("CATEGORY_ID") + "/" + rs.getString("SUB_CATEGORY_ID") + "/" + rs.getString("DOCUMENT_NAME");
		
		dto.setDocumentPath(docPath);
        
		dto.setDocumentId(rs.getString("DOCUMENT_ID"));

		dto.setDocumentGroupId(rs.getString("DOCUMENT_GROUP_ID"));

		dto.setDocumentName(rs.getString("DOCUMENT_NAME"));

		dto.setDocumentDisplayName(rs.getString("DOCUMENT_DISPLAY_NAME"));

		dto.setDocumentDesc(rs.getString("DOCUMENT_DESC"));

		dto.setSubCategoryId(rs.getString("SUB_CATEGORY_ID"));

		dto.setCategoryId(rs.getString("CATEGORY_ID"));

		dto.setCircleId(rs.getString("CIRCLE_ID"));

		dto.setNumberOfHits(rs.getString("NUMBER_OF_HITS"));

		dto.setStatus(rs.getString("STATUS"));

		dto.setApprovalStatus(rs.getString("APPROVAL_STATUS"));

		dto.setUploadedBy(rs.getString("UPLOADED_BY"));

		dto.setUploadedDt(rs.getTimestamp("UPLOADED_DT"));

		dto.setUpdatedBy(rs.getString("UPDATED_BY"));

		dto.setUpdatedDt(rs.getTimestamp("UPDATED_DT"));

		dto.setApprovalRejectionDt(rs.getTimestamp("APPROVAL_REJECTION_DT"));

		dto.setApprovalRejectionBy(rs.getString("APPROVAL_REJECTION_BY"));

		dto.setPublishingStartDt(rs.getTimestamp("PUBLISHING_START_DT"));

		dto.setPublishingEndDt(rs.getTimestamp("PUBLISHING_END_DT"));

	}
    
	protected static void populateCSRDocumentDto(KmDocumentMstr dto, ResultSet rs) throws SQLException {
		String docPath = rs.getString("CIRCLE_ID") + "/" + rs.getString("CATEGORY_ID") + "/" + rs.getString("SUB_CATEGORY_ID") + "/" + rs.getString("DOCUMENT_NAME");
		
		dto.setDocumentPath(docPath);
		
		dto.setDocumentId(rs.getString("DOCUMENT_ID"));

		dto.setDocumentGroupId(rs.getString("DOCUMENT_GROUP_ID"));

		dto.setDocumentName(rs.getString("DOCUMENT_NAME"));

		dto.setDocumentDisplayName(rs.getString("DOCUMENT_DISPLAY_NAME"));

		dto.setDocumentDesc(rs.getString("DOCUMENT_DESC"));

		dto.setSubCategoryId(rs.getString("SUB_CATEGORY_ID"));
		
		dto.setSubCategoryName(rs.getString("SUB_CATEGORY_NAME"));

		dto.setCategoryId(rs.getString("CATEGORY_ID"));
		
		dto.setCategoryName(rs.getString("CATEGORY_NAME"));

		dto.setCircleId(rs.getString("CIRCLE_ID"));
		
		dto.setCircleName(rs.getString("CIRCLE_NAME"));

		dto.setNumberOfHits(rs.getString("NUMBER_OF_HITS"));
	}

	private Connection getConnection() throws KmDocumentMstrDaoException {

		//logger.info("Entered getConnection for operation on table:KM_DOCUMENT_MSTR");

		try {
			return DBConnection.getDBConnection();
		}catch(DAOException e) {
		
			logger.info("Exception Occured while obtaining connection.");

			throw new KmDocumentMstrDaoException("Exception while trying to obtain a connection",e);
	}

   }
   
   public ArrayList retrieveCircleWideDocumentList(int[] circleId) throws KmDocumentMstrDaoException{
		logger.info("Retrieving Circle Wide Document List ordered by Category ID and Sub-Category ID");
	
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList documentList=new ArrayList();
		KmDocumentMstr dto=null;
		StringBuffer query =new StringBuffer(SQL_CIRCLE_DOCUMENT_LIST);
		//String sql = SQL_CIRCLE_DOCUMENT_LIST;
		for(int i=1;i<circleId.length;i++){
			query.append(" OR A.CIRCLE_ID=? ").toString();		
		}
		query.append(" ) ORDER BY A.CIRCLE_ID, B.CATEGORY_NAME, C.SUB_CATEGORY_NAME").toString();
		try {
			con = getConnection();
			ps = con.prepareStatement(query.append(" with ur ").toString());
			for(int i=0;i<circleId.length;i++){
				ps.setInt(i+1, circleId[i]);	
			}		
			rs = ps.executeQuery();
			while(rs.next()){
				dto = new KmDocumentMstr();
				populateCSRDocumentDto(dto, rs);
				documentList.add(dto);			
			}
			return documentList;
		} catch (SQLException e) {
		
			logger.error("SQL Exception occured while Retrieving." + "Exception Message: " + e.getMessage());
			throw new KmDocumentMstrDaoException("SQLException: " + e.getMessage(), e);
		} catch (Exception e) {
		
			logger.error("Exception occured while Retrieving." + "Exception Message: " + e.getMessage());
			throw new KmDocumentMstrDaoException("Exception: " + e.getMessage(), e);
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
				
			}
		}  	
   }
   /* (non-Javadoc)
	* @see com.ibm.km.dao.KmFileDao#deleteDocument(java.lang.String)
	* @delete the file details from the database
	**/
   // Update the document status to D in KM_DOCUMENT_MSTR.
   public void deleteDocument(String fileId, String updatedBy) throws KmException {
	   Connection con=null;
	   Statement stmt = null;
	   ResultSet rs = null;
	   PreparedStatement ps = null;
	   try {
			StringBuffer query =new StringBuffer(SQL_DELETE_DOCUMENT);
		   //String sql = SQL_DELETE_DOCUMENT;
		   con=DBConnection.getDBConnection();
		   ps=con.prepareStatement(query.append(" with ur").toString());
			logger.info("File ID : "+fileId);
		   ps.setInt(2, Integer.parseInt(fileId));
		   ps.setInt(1, Integer.parseInt(updatedBy));
		   
		   //Executing the querry to delete the file
		   boolean deleted = ps.execute();
			
					
	   } catch (SQLException e) {
			
			logger.info(e);
		   //	logger.severe("SQL Exception occured while find." + "Exception Message: " + e.getMessage());
		   throw new KmException("SQLException: " + e.getMessage(), e);
	   } catch (Exception e) {
			
			logger.info(e);
		   //	logger.severe("Exception occured while find." + "Exception Message: " + e.getMessage());
		   throw new KmException("Exception: " + e.getMessage(), e);
	   } finally {
		   try {
			   if (rs != null)
				   rs.close();
			   if (stmt != null)
				   stmt.close();
			   if (con != null) {
				   con.close();
			   }
		   } catch (Exception e) {
				
		   }
	   }
		
   }




   /* (non-Javadoc)
	* @see com.ibm.km.dao.KmFileDao#viewFiles(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	*/
   public ArrayList viewFiles(String root,String parentId) throws KmException {
	   Connection con=null;
	   Statement stmt = null;
	   ResultSet rs = null;
	   ArrayList fileList = new ArrayList();
	   PreparedStatement ps = null;
	   KmDocumentMstr dto;
		
	   try {
		//Code change after UAT observation
	   //String sql=SQL_SELECT_FILES;
	   StringBuffer query =new StringBuffer(SQL_SELECT_FILES);
	   logger.info("PARENT ID "+ parentId);
	   con=DBConnection.getDBConnection();
	   ps=con.prepareStatement(query.append(" with ur ").toString());
	   ps.setInt(1, Integer.parseInt(root));
	   ps.setInt(2, Integer.parseInt(parentId));
	   
	   rs = ps.executeQuery();
			
	   while(rs.next()){
		   dto=new KmDocumentMstr();
			//logger.info(rs.getString("chain"));
		   dto.setDocumentPath(rs.getString("DOCUMENT_PATH"));
		   String path=rs.getString("chain");
			//logger.info(path);
		   String documentStringPath="";

		   if((path.indexOf("/")+1)<path.lastIndexOf("/")){	
				documentStringPath=path.substring((path.indexOf("/")+1),path.lastIndexOf("/"));
		   }

		   dto.setDocumentStringPath(documentStringPath);
		   dto.setDocumentId(rs.getString("DOCUMENT_ID"));
		   dto.setElementId(rs.getString("ELEMENT_ID"));
		   dto.setDocumentName(rs.getString("DOCUMENT_NAME"));
		   dto.setDocumentDisplayName(rs.getString("DOCUMENT_DISPLAY_NAME"));
			logger.info("APPROVAL_STATUS   "+rs.getString("APPROVAL_STATUS"));
		   if(("A").equals(rs.getString("APPROVAL_STATUS")))
		   {
			   dto.setApprovalStatus("Approved");
				
		   }
		   else if(("S").equals(rs.getString("APPROVAL_STATUS")))
		   {
			   dto.setApprovalStatus("Submitted");
				
		   }
		   else if(("R").equals(rs.getString("APPROVAL_STATUS")))
		   {
			   dto.setApprovalStatus("Rejected");
				
		   }
		   else if(("P").equals(rs.getString("APPROVAL_STATUS")))
		   {
			   dto.setApprovalStatus("Pending");
				
		   }
		   else if(("O").equals(rs.getString("APPROVAL_STATUS")))
		   {
			   dto.setApprovalStatus("Versioned");
				
		   }
				
		if(rs.getString("UPLOADED_DT")!=null){
			dto.setUploadedDate(rs.getString("UPLOADED_DT").substring(0,16));		
		}
			
		if(rs.getString("APPROVAL_REJECTION_DT")!=null){
			dto.setApprovalRejectionDate(rs.getString("APPROVAL_REJECTION_DT").substring(0,16));
		}

		 // dto.setElementName(rs.getString("ELEMENT_NAME"));
		   
		   fileList.add(dto);	
	   }
	   return fileList;
         
   } catch (SQLException e) {
		logger.info(e);	
	  
// logger.severe("SQL Exception occured while find." + "Exception Message: " + e.getMessage());
	   throw new KmException("SQLException: " + e.getMessage(), e);
   } catch (Exception e) {
// logger.severe("Exception occured while find." + "Exception Message: " + e.getMessage());
		logger.info("Exception : "+e);
	   
	   throw new KmException("Exception: " + e.getMessage(), e);
   } finally {
	   try {
		   if (rs != null)
			   rs.close();
		   if (stmt != null)
			   stmt.close();
		   if (con != null) {
			   con.close();
		   }
	  } catch (Exception e) {
		
	   }
   }
   }



   /* (non-Javadoc)
	* @see com.ibm.km.dao.KmFileDao#keywordFileSearch(java.lang.String, java.lang.String, java.lang.String)
	*/
   public ArrayList keywordFileSearch(String keyword, String circleId, String uploadedBy) throws KmException{
	   Connection con=null;
	   Statement stmt = null;
	   ResultSet rs = null;
	   ArrayList fileList = new ArrayList();
	   PreparedStatement ps = null;
	   KmDocumentMstr dto;
		
	   try {
	   //String sql="";
	   StringBuffer query =new StringBuffer(SQL_KEYWORD_SEARCH);
	   String Keyword=keyword.toUpperCase();
		
	   if(!uploadedBy.equals("") && circleId.equals(""))	
		   query.append("'%").append(Keyword).append("%' AND doc.UPLOADED_BY = ?").toString();
	   if(uploadedBy.equals("")&&!circleId.equals("")&&!keyword.equals(""))
		   query.append("'%").append(Keyword).append("%' AND doc.CIRCLE_ID = ?").toString();
	   if(circleId.equals("")&&uploadedBy.equals(""))
		   query.append("'%").append(Keyword).append("%'").toString();
	   if(!uploadedBy.equals("")&&!circleId.equals("")&&!keyword.equals("")){
		   uploadedBy="";
		   query.append("'%").append(Keyword).append("%' AND doc.CIRCLE_ID = ?").toString();		
	   }
	   query.append(" ORDER BY doc.DOCUMENT_ID").toString();
	   
	   con=DBConnection.getDBConnection();
	   ps=con.prepareStatement(query.append(" with ur ").toString());
	   
	   //Preparing sql statement
	   if(!uploadedBy.equals("") && circleId.equals("")){
	   		
			ps.setInt(1, Integer.parseInt(uploadedBy));
	   }
	   if(uploadedBy.equals("")&&!circleId.equals("")&&!keyword.equals("")){
			
			ps.setInt(1, Integer.parseInt(circleId));	   	
	   }
	/*   if(circleId.equals("")&&uploadedBy.equals("")){
			ps.setString(1, keyword);
	   }  */
	   if(!uploadedBy.equals("")&&!circleId.equals("")&&!keyword.equals("")){
			
			ps.setInt(1, Integer.parseInt(circleId));
	   }	   
	   
	   rs = ps.executeQuery();
	   while(rs.next()){
		   dto=new KmDocumentMstr();
		   dto.setDocumentPath(rs.getString("DOCUMENT_PATH"));
		   dto.setDocumentStringPath(rs.getString("chain"));
		   dto.setDocumentId(rs.getString("DOCUMENT_ID"));
		   dto.setDocumentName(rs.getString("DOCUMENT_NAME"));
		   dto.setDocumentDisplayName(rs.getString("DOCUMENT_DISPLAY_NAME"));
			
		   if(("A").equals(rs.getString("APPROVAL_STATUS")))
		   {
			   dto.setApprovalStatus("Approved");
				
		   }
		   else if(("S").equals(rs.getString("APPROVAL_STATUS")))
		   {
			   dto.setApprovalStatus("Submitted");
				
		   }
		   else if(("R").equals(rs.getString("APPROVAL_STATUS")))
		   {
			   dto.setApprovalStatus("Rejected");
				
		   }
		   else if(("P").equals(rs.getString("APPROVAL_STATUS")))
		   {
			   dto.setApprovalStatus("Pending");
				
		   }
		   else if(("O").equals(rs.getString("APPROVAL_STATUS")))
		   {
			   dto.setApprovalStatus("Versioned");
				
		   }
				
		if(rs.getString("UPLOADED_DT")!=null){
			dto.setUploadedDate(rs.getString("UPLOADED_DT").substring(0,16));		
		}
			
		if(rs.getString("APPROVAL_REJECTION_DT")!=null){
			dto.setApprovalRejectionDate(rs.getString("APPROVAL_REJECTION_DT").substring(0,16));
		}

		   dto.setCategoryName(rs.getString("CATEGORY_NAME"));
		   dto.setSubCategoryName(rs.getString("SUB_CATEGORY_NAME"));
			
		   fileList.add(dto);	
 
	   }
			
			
	   return fileList;
         
   } catch (SQLException e) {
		logger.info("Exception : "+e);
	 
// logger.severe("SQL Exception occured while find." + "Exception Message: " + e.getMessage());
	   throw new KmException("SQLException: " + e.getMessage(), e);
   } catch (Exception e) {

// logger.severe("Exception occured while find." + "Exception Message: " + e.getMessage());
		logger.info("Exception : "+e);
	 
	   throw new KmException("Exception: " + e.getMessage(), e);
   } finally {
	   try {
		   if (rs != null)
			   rs.close();
		   if (stmt != null)
			   stmt.close();
		   if (con != null) {
			   con.close();
		   }
	  } catch (Exception e) {
	
	   }
   }
   }

	public int addToFavorites (int userId, int docId) throws KmException {
		Connection conn=null;
		PreparedStatement pst=null;
		int status=0;
		try {
			conn=DBConnection.getDBConnection();
			StringBuffer query =new StringBuffer(SQL_FAVORITE_INSERT);
			//String sql = SQL_FAVORITE_INSERT;
			pst = conn.prepareStatement(query.append(" with ur").toString());
			pst.setInt(1,userId);
			pst.setInt(2,docId);
			status = pst.executeUpdate();
		} catch (SQLException e) {
		
			throw new KmException("SQLException: " + e.getMessage(), e);
		} catch (Exception e) {
		
			//	logger.severe("Exception occured while find." + "Exception Message: " + e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try {
				if (pst != null)
					pst.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				
				//	logger.severe("Exception occured while find." + "Exception Message: " + e.getMessage());
				throw new KmException("Exception: " + e.getMessage(), e);
			}
		}
		return status;
	}
	
	public int deleteFavorites (int userId, int docId) throws KmException {
		Connection conn=null;
		PreparedStatement pst=null;
		int status;
		try {
			conn=DBConnection.getDBConnection();
			StringBuffer query =new StringBuffer(SQL_FAVORITE_DELETE);
			//String sql = SQL_FAVORITE_DELETE;
			pst = conn.prepareStatement(query.append(" with ur").toString());
			pst.setInt(1,userId);
			pst.setInt(2,docId);
			status = pst.executeUpdate();
		} catch (SQLException e) {
			
			throw new KmException("SQLException: " + e.getMessage(), e);
		} catch (Exception e) {
			
			//	logger.severe("Exception occured while find." + "Exception Message: " + e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try {
				if (pst != null)
					pst.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			
				//	logger.severe("Exception occured while find." + "Exception Message: " + e.getMessage());
				throw new KmException("Exception: " + e.getMessage(), e);
			}
		}
		return status;
	}

	public boolean checkForFavorite (int userId, int docId) throws KmException {
		Connection conn=null;
		PreparedStatement pst=null;
		boolean status=false;
		ResultSet rs=null;
		try {
			conn=DBConnection.getDBConnection();
			StringBuffer query =new StringBuffer(SQL_FAVORITE_CHECK);
			//String sql = SQL_FAVORITE_CHECK;
			pst = conn.prepareStatement(query.append(" with ur ").toString());
			pst.setInt(1,userId);
			pst.setInt(2,docId);
			rs = pst.executeQuery();
			if (rs.next()) {
				if (rs.getInt(1)>0)
					status=true;
			}
		} catch (SQLException e) {
			
			throw new KmException("SQLException: " + e.getMessage(), e);
		} catch (Exception e) {
			
			//	logger.severe("Exception occured while find." + "Exception Message: " + e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try {
				if (pst != null)
					pst.close();
				if (conn != null)
					conn.close();
				if (rs!=null)
					rs.close();
			} catch (Exception e) {
				
				//	logger.severe("Exception occured while find." + "Exception Message: " + e.getMessage());
				throw new KmException("Exception: " + e.getMessage(), e);
			}
		}
		return status;
	}
	
	public int insertDocView (int docId, int userId) throws KmException {
		Connection conn=null;
		PreparedStatement pst=null;
		int status=0;
		try {
			conn=DBConnection.getDBConnection();
			
			StringBuffer query =new StringBuffer(SQL_DOCUMENT_VIEW_INSERT);
			//String sql = SQL_DOCUMENT_VIEW_INSERT;
			pst = conn.prepareStatement(query.append(" with ur").toString());
			pst.setInt(1,docId);
			pst.setInt(2, userId);
			status = pst.executeUpdate();
		} catch (SQLException e) {
			logger.debug("Error:" + e);
			throw new KmException("SQLException in insertDocView: " + e.getMessage(), e);
		} catch (Exception e) {
		
			//	logger.severe("Exception occured while find." + "Exception Message: " + e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try {
				if (pst != null)
					pst.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				
				//	logger.severe("Exception occured while find." + "Exception Message: " + e.getMessage());
				throw new KmException("Exception: " + e.getMessage(), e);
			}
		}
		return status;
	} 

   
   
   public ArrayList retrieveCircleWideDocumentListElements(int[] circleId) throws KmDocumentMstrDaoException {
		   logger.info("Retrieving Circle Wide Document List ordered by Category ID and Sub-Category ID");

		   ArrayList lst = null;
		   try{
					lst =  getChildrenList(circleId[0]);
		   }catch(Exception e){
			   logger.error(e);
		   }
		   return lst;
	  }
   
	public ArrayList getChildrenList(int id)
	{
		 ArrayList list = new ArrayList();
		 //String query = SQL_GET_TREE;
		StringBuffer query =new StringBuffer(SQL_GET_TREE);
		 Connection con = null;
		 ResultSet rs=null;
		 PreparedStatement stmt =null;
		 try{
			
			 con = getConnection();
			 stmt = con.prepareStatement(query.append(" with ur ").toString());
			 stmt.setInt(1,id);
			 rs = stmt.executeQuery();
			
			while(rs.next()){
				 int elementId=rs.getInt("ELEMENT_ID");
				 String elementName=rs.getString("ELEMENT_NAME");
				 int parentId=rs.getInt("PARENT_ID");
				 int elementLevelId=rs.getInt("ELEMENT_LEVEL_ID");
				 int documentId=rs.getInt("DOCUMENT_ID");
				 int docType = rs.getInt("DOC_TYPE");
				 DocumentTreeElement bean = new DocumentTreeElement();
				 bean.setElementId(new Integer(elementId));
				 bean.setElementName(elementName);
				 bean.setParentId(new Integer(parentId));
				 bean.setElementLevelId(new Integer(elementLevelId));
				 bean.setDocumentId(new Integer(documentId));
				 bean.setDocType(new Integer(docType));
				 bean.setDocumentViewUrl(Utility.getDocumentViewURL(""+documentId, docType));
//				 logger.info("DocumentID: "+bean.getDocumentId());
				 list.add(bean);
				
				
			 }
		 }catch(Exception e){
			 logger.error(e);
		 }finally{
				 try
				 {
					 DBConnection.releaseResources(con,stmt,rs);
				 }catch(Exception e){
					logger.error(e);
				 }
		 }
		 return list;
	}


	public String[] getDocumentName(String[] documentId) throws KmException {

		Connection con=null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		//String sql = SQL_GET_DOCUMENT_NAME;
		StringBuffer query =new StringBuffer(SQL_GET_DOCUMENT_NAME);
		String[] elementName=new String[documentId.length];
		for(int i=1;i<documentId.length;i++){
			query.append(" OR DOCUMENT_ID=? ").toString();		
		}
		query.append(" )").toString();
		try {
				con = DBConnection.getDBConnection();
				ps = con.prepareStatement(query.append(" with ur ").toString());
				//logger.info("documentID : "+documentId);

			

				for(int i=0;i<documentId.length;i++){
						ps.setInt(i+1, Integer.parseInt(documentId[i]));	
				}

				rs = ps.executeQuery();
				int i=0;
				while(rs.next()){
					elementName[i]=rs.getString("DOCUMENT_NAME");
					i++;
				}
				
				return elementName;
		} 
		catch (Exception e) {
			logger.info(e);
			
	//	logger.severe("Exception occured while find." + "Exception Message: " + e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (con != null) {
					con.close();
				}
		   } catch (Exception e) {
			   logger.error(e);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.dao.KmDocumentMstrDao#viewFiles(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public ArrayList viewFiles(String circleId, String categoryId, String subCategoryId, String userId) throws KmException {
		// TODO Auto-generated method stub
		return null;
	}




	/* (non-Javadoc)
	 * @see com.ibm.km.dao.KmElementMstrDao#changePath(java.lang.String[], java.lang.String, java.lang.String)
	 */
	public void changePath(String[] movedDocumentList, String oldPath, String newPath) throws KmException {
		Connection con=null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		//String sql = SQL_UPDATE_PATH;
		StringBuffer query =new StringBuffer(SQL_UPDATE_PATH);
		//System.out.println("pathhhhhhhhhhhh1 movedDocumentList.length"+movedDocumentList.length);
		for(int i=1;i<movedDocumentList.length;i++){
				query.append(" OR DOCUMENT_ID=? ").toString();		
			}
			query.append(" ) ").toString();
			try {
				con = DBConnection.getDBConnection();
				ps = con.prepareStatement(query.append(" with ur").toString());
				//System.out.println("pathhhhhhhhhhhh1"+query);
				ps.setString(1, newPath);
				//System.out.println("pathhhhhhhhhhhh3"+newPath);
				for(int i=0;i<movedDocumentList.length;i++){
					ps.setInt(i+2, Integer.parseInt(movedDocumentList[i]));	
					//System.out.println("pathhhhhhhhhhhh4"+Integer.parseInt(movedDocumentList[i]));
				}		
				int n = ps.executeUpdate();
				logger.info("Path changed successfully");
			
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

	/* (non-Javadoc)
	 * @see com.ibm.km.dao.KmDocumentMstrDao#getDocumentIds(java.lang.String[])
	 */
	//added by vishwas for path correction
	public void changePathcorrect(String moveDoc, String oldPath, String newPath) throws KmException {
		Connection con=null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		//String sql = SQL_UPDATE_PATH;
		StringBuffer query =new StringBuffer(SQL_UPDATE_PATH);
		//System.out.println("pathhhhhhhhhhhh1 movedDocumentList.length"+movedDocumentList.length);
		//for(int i=1;i<movedDocumentList.length;i++)
		{
				//query.append(" OR DOCUMENT_ID=? ").toString();		
			}
			query.append(" ) ").toString();
			try {
				con = DBConnection.getDBConnection();
				ps = con.prepareStatement(query.append(" with ur").toString());
				//System.out.println("pathhhhhhhhhhhh1"+query);
				ps.setString(1, newPath);
				//System.out.println("pathhhhhhhhhhhh3"+newPath);
				//for(int i=0;i<movedDocumentList.length;i++)
				{
					ps.setInt(2, Integer.parseInt(moveDoc));	
					//System.out.println("pathhhhhhhhhhhh4"+Integer.parseInt(moveDoc)+":"+query+":"+newPath);
				}		
				int n = ps.executeUpdate();
				logger.info("Path changed successfully");
			
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
	//added by vishwas for copy document
	
	public String insertCopyPath(String moveDoc, String oldPath, String newPath,String elementid1) throws KmException {
		Connection con=null;
		Connection con2=null;
		Connection con3=null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		PreparedStatement ps = null;
		String documentId=null;
		String elementId=null;
		//String sql = SQL_UPDATE_PATH;
		//System.out.println("query=========inside insert copy path method============"+moveDoc);	
		//String sql1=SQL_GET_DOCUMENT_ID;
		KmDocumentMstr kdm=new KmDocumentMstr();
		try
		{
		kdm=findByPrimaryKeyforcopy(moveDoc);
	/**	System.out.println("2====================="+kdm.getDocumentName());
		System.out.println("3====================="+kdm.getDocName().trim());
		System.out.println("4====================="+(kdm.getDocumentDisplayName().trim()));
		System.out.println("5====================="+kdm.getDocumentDesc());
		System.out.println("6====================="+kdm.getKeyword());
		System.out.println("7====================="+kdm.getApprovalStatus());
		System.out.println("8====================="+kdm.getUploadedBy());
		System.out.println("9====================="+kdm.getUpdatedBy());
		System.out.println("10====================="+kdm.getPublishingStartDate()+ " 00:00:00");
		System.out.println("11====================="+kdm.getPublishingEndDate()+" 23:59:59");
		System.out.println("12====================="+newPath);
		System.out.println("13====================="+Integer.parseInt(kdm.getElementId()));
		System.out.println("14====================="+kdm.getDocType());
		**/
		StringBuffer query=new StringBuffer("SELECT NEXTVAL FOR KM_DOCUMENT_ID_SEQ FROM SYSIBM.SYSDUMMY1");
		StringBuffer query4=new StringBuffer("SELECT NEXTVAL FOR KM_ELEMENT_ID_SEQ FROM SYSIBM.SYSDUMMY1");
		System.out.println("query====================="+query);
		con=getConnection();
		con2=getConnection();
		con3=getConnection();
		pstmt=con.prepareStatement(query.append(" with ur").toString());
		pstmt3=con2.prepareStatement(query4.append(" with ur").toString());
		//System.out.println("query1====================="+query);
		//System.out.println("query4====================="+query4);
		
		rs=pstmt.executeQuery();
		rs2=pstmt3.executeQuery();
		while(rs.next())
		{
			//System.out.println("query=2===================="+query);
			documentId=rs.getString(1);
		}
		while(rs2.next())
		{
			//System.out.println("query=2===================="+query4);
			elementId=rs2.getString(1);
		}
		//System.out.println("1=============elementId========"+elementId);
		
		
		
		
		
		String query3="insert into KM_DOCUMENT_MSTR(DOCUMENT_ID,DOCUMENT_GROUP_ID,DOCUMENT_NAME,DOCUMENT_DISPLAY_NAME,CATEGORY_ID,SUB_CATEGORY_ID, CIRCLE_ID,NUMBER_OF_HITS,Status,UPLOADED_DT,DOCUMENT_PATH,ELEMENT_ID,APPROVAL_STATUS,PUBLISHING_START_DT,PUBLISHING_END_DT,DOCUMENT_KEYWORD,DOC_NAME,DOC_TYPE,DOCUMENT_DESC)"+ 
		"VALUES ("+documentId+",0,'"+kdm.getDocName().trim()+"','"+kdm.getDocumentDisplayName().trim()+"',1,1,1,"+kdm.getNumberOfHits()+",'A',current timestamp,'"+newPath+"',"+elementid1+",'"+kdm.getApprovalStatus()+"','"+kdm.getPublishingStartDate()+"','"+kdm.getPublishingEndDt()+"','"+kdm.getKeyword()+"','"+kdm.getDocumentName()+"','"+kdm.getDocType()+"','"+kdm.getDocumentDesc()+"')";

		
		System.out.println(query3+"queryyyyyyyyyy");
		pstmt2 = con3.prepareStatement(query3);
		/**pstmt.setInt(1,Integer.valueOf(documentId));
		pstmt.setString(2,kdm.getDocName().trim()); //DOCUMENT_NAME,DOC_NAME,DOCUMENT_DISPLAY_NAME
		pstmt.setString(3,kdm.getDocName().trim());
		pstmt.setString(4,(kdm.getDocumentDisplayName().trim()));
		pstmt.setString(5,kdm.getDocumentDesc());
		pstmt.setString(6,kdm.getKeyword());
		pstmt.setString(7,kdm.getApprovalStatus());
		pstmt.setInt(8, Integer.valueOf(kdm.getUploadedBy()));
		pstmt.setInt(9,Integer.valueOf(kdm.getUpdatedBy()));
		
		// added publish date and publish end date of a new	
		////System.out.println("DT:" + dto.getPublishingStartDate());
		pstmt.setString(10,kdm.getPublishingStartDate());
		pstmt.setString(11,kdm.getPublishingEndDate());
		////System.out.println("DT end:" + dto.getPublishingEndDate());
		
		pstmt.setString(12,newPath);
		pstmt.setInt(13,Integer.parseInt(kdm.getElementId()));
		pstmt.setInt(14, kdm.getDocType());
		
		**/
		
		
		int i=pstmt2.executeUpdate();
	//	System.out.println("insertion successfull"+i);
		
		//System.out.println("pathhhhhhhhhhhh1 movedDocumentList.length"+movedDocumentList.length);
		//for(int i=1;i<movedDocumentList.length;i++)
		return documentId;
		}
	catch (Exception e) {
			logger.error(e);
	//	logger.severe("Exception occured while find." + "Exception Message: " + e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try{
				DBConnection.releaseResources(con,ps,rs);
				DBConnection.releaseResources(con2,pstmt,rs2);
				DBConnection.releaseResources(con3,pstmt2,null);
				DBConnection.releaseResources(con2,pstmt3,null);
				
			}catch(DAOException e){
				logger.error(e);
				throw new KmException(e.getMessage(),e);
			}				
		}	
			
}
	public String[] getDocumentIds(String[] movedDocumentList) throws KmException {
		Connection con=null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		//String sql = SQL_GET_DOCUMENT_IDS;
		StringBuffer query =new StringBuffer(SQL_GET_DOCUMENT_IDS);
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
					docList.add(rs.getString("DOCUMENT_ID"));
				//	documentId[i]=rs.getString("DOCUMENT_ID");
					
				}
				String documentId [] = (String[]) docList.toArray(new String [docList.size()]);
				//String[] documentId=new String[movedDocumentList.length];
			//	System.out.println("documentId"+documentId);
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
	
	
	/* (non-Javadoc)
	 * @see com.ibm.km.dao.KmDocumentMstrDao#getDocumentPath(java.lang.String)
	 */
public String getDocumentPath(String elementId) throws KmException {
		Connection con=null;
		
	ResultSet rs = null;
	PreparedStatement ps = null;
	String parentString=null;
	String trimmedParentString="";
	String documentPath="";
	String documentName="";	
	try {
		StringBuffer query =new StringBuffer(SQL_PARENT_ID_STRING);	
		//String sql = SQL_PARENT_ID_STRING;
		con=DBConnection.getDBConnection();
		ps=con.prepareStatement(query.append(" with ur ").toString());
		ps.setInt(1, 1);
		ps.setInt(2, Integer.parseInt(elementId));
			
		rs = ps.executeQuery();
		if(rs.next())
			parentString= rs.getString("PARENT_STRING");	
			documentName=rs.getString("DOCUMENT_NAME");
			StringTokenizer tokenized = new StringTokenizer(parentString);
			while(tokenized.hasMoreTokens()){
				trimmedParentString=trimmedParentString+tokenized.nextToken();
		}
		logger.info("trimmedParentString:"+trimmedParentString);
		
	} catch (SQLException e) {
		
		logger.info(e);
		//	logger.severe("SQL Exception occured while find." + "Exception Message: " + e.getMessage());
		throw new KmException("SQLException: " + e.getMessage(), e);
	} catch (Exception e) {
		
		logger.info(e);
		//	logger.severe("Exception occured while find." + "Exception Message: " + e.getMessage());
		throw new KmException("Exception: " + e.getMessage(), e);
	} finally {
		try{
			DBConnection.releaseResources(con,ps,rs);
		}catch(DAOException e){
			
			throw new KmException(e.getMessage(),e);
		}				
	}	
	return trimmedParentString;
	}
		
		/* (non-Javadoc)
		 * @see com.ibm.km.dao.KmDocumentMstrDao#getDocumentPath(java.lang.String)
		 */
		public KmDocumentMstr getDocumentByElementId(String elementId) throws KmException {
			
			Connection con=null;
			KmDocumentMstr dto = new KmDocumentMstr();
			ResultSet rs = null;
			PreparedStatement ps = null;
			try {
			StringBuffer query =new StringBuffer(SQL_GET_DOCUMENT_BY_ELEMENT_ID);	
			//String sql = SQL_PARENT_ID_STRING;
			con=DBConnection.getDBConnection();
			ps=con.prepareStatement(query.append(" with ur ").toString());
			ps.setInt(1, Integer.parseInt(elementId));
				
			rs = ps.executeQuery();
			if(rs.next()) {
				dto.setDocumentId(rs.getString("DOCUMENT_ID"));
				dto.setDocumentGroupId(rs.getString("DOCUMENT_GROUP_ID"));
				dto.setDocumentName(rs.getString("DOCUMENT_NAME"));
				dto.setDocumentDisplayName(rs.getString("DOCUMENT_DISPLAY_NAME"));
				dto.setDocumentDesc(rs.getString("DOCUMENT_DESC"));
				dto.setSubCategoryId(rs.getString("SUB_CATEGORY_ID"));
				dto.setCategoryId(rs.getString("CATEGORY_ID"));
				dto.setCircleId(rs.getString("CIRCLE_ID"));
				dto.setNumberOfHits(rs.getString("NUMBER_OF_HITS"));
				dto.setStatus(rs.getString("STATUS"));
				dto.setApprovalStatus(rs.getString("APPROVAL_STATUS"));
				dto.setUploadedBy(rs.getString("UPLOADED_BY"));
				dto.setUploadedDt(rs.getTimestamp("UPLOADED_DT"));
				dto.setUpdatedBy(rs.getString("UPDATED_BY"));
				dto.setUpdatedDt(rs.getTimestamp("UPDATED_DT"));
				dto.setApprovalRejectionDt(rs.getTimestamp("APPROVAL_REJECTION_DT"));
				dto.setApprovalRejectionBy(rs.getString("APPROVAL_REJECTION_BY"));
				dto.setPublishingStartDt(rs.getTimestamp("PUBLISHING_START_DT"));
				dto.setPublishingEndDt(rs.getTimestamp("PUBLISHING_END_DT"));
				if(rs.getString("DOC_TYPE") != null)
				dto.setDocType(rs.getInt("DOC_TYPE"));
					
				//System.out.println("\nPublish Start Date"+rs.getTimestamp("PUBLISHING_START_DT"));
			}
			
			} catch (SQLException e) {
			
			logger.info(e);
			//	logger.severe("SQL Exception occured while find." + "Exception Message: " + e.getMessage());
			throw new KmException("SQLException: " + e.getMessage(), e);
			} catch (Exception e) {
			
			logger.info(e);
			//	logger.severe("Exception occured while find." + "Exception Message: " + e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
			} finally {
			try{
				DBConnection.releaseResources(con,ps,rs);
			}catch(DAOException e){
				
				throw new KmException(e.getMessage(),e);
			}				
			}	
			return dto;
		}


/* (non-Javadoc)
 * @see com.ibm.km.dao.KmDocumentMstrDao#getElementId(java.lang.String)
 */
		public String getElementId() throws KmException
		{
			Connection con=null;
			Statement stmt = null;
			ResultSet rs = null;
			PreparedStatement ps = null;
			String elementId="";
			try{
			
				StringBuffer query =new StringBuffer("SELECT NEXTVAL FOR KM_ELEMENT_ID_SEQ FROM SYSIBM.SYSDUMMY1");	
				//String sql = SQL_GET_ELEMENT_ID;
				con=DBConnection.getDBConnection();
				ps=con.prepareStatement(query.append(" with ur ").toString());
				//ps.setInt(1, Integer.parseInt(documentId));
				//Executing the querry to delete the file
				rs=ps.executeQuery();
				while(rs.next()){
					elementId=rs.getString(1);
				}

				
				return elementId;
			}
			catch (SQLException e) {
				logger.info(e);
				
				//	logger.severe("SQL Exception occured while find." + "Exception Message: " + e.getMessage());
				throw new KmException("SQLException: " + e.getMessage(), e);
			} catch (Exception e) {
				logger.info(e);
				
				//	logger.severe("Exception occured while find." + "Exception Message: " + e.getMessage());
				throw new KmException("Exception: " + e.getMessage(), e);
			} finally {
				try {
					if (rs != null)
						rs.close();
					if (stmt != null)
						stmt.close();
					if (con != null) {
						con.close();
					}
				} catch (Exception e) {
					logger.error(e);
				}
			}

			
		}
		
		
		
public String getElementId(String documentId) throws KmException {
	Connection con=null;
	Statement stmt = null;
	ResultSet rs = null;
	PreparedStatement ps = null;
	String elementId="";
	try {
		StringBuffer query =new StringBuffer(SQL_GET_ELEMENT_ID);	
		//String sql = SQL_GET_ELEMENT_ID;
		con=DBConnection.getDBConnection();
		ps=con.prepareStatement(query.append(" with ur ").toString());
		ps.setInt(1, Integer.parseInt(documentId));
		//Executing the querry to delete the file
		rs=ps.executeQuery();
		while(rs.next()){
			elementId=rs.getString("ELEMENT_ID");
		}
		return elementId;			
	} catch (SQLException e) {
		logger.info(e);
		
		//	logger.severe("SQL Exception occured while find." + "Exception Message: " + e.getMessage());
		throw new KmException("SQLException: " + e.getMessage(), e);
	} catch (Exception e) {
		logger.info(e);
		
		//	logger.severe("Exception occured while find." + "Exception Message: " + e.getMessage());
		throw new KmException("Exception: " + e.getMessage(), e);
	} finally {
		try {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (con != null) {
				con.close();
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}
			// TODO Auto-generated method stub
	
}

/* (non-Javadoc)
 * @see com.ibm.km.dao.KmDocumentMstrDao#getDocumentDto(java.lang.String)
 */
public KmDocumentMstr getDocumentDto(String documentId) throws KmException {
	Connection con=null;
	Statement stmt = null;
	ResultSet rs = null;
	ArrayList fileList = new ArrayList();
	PreparedStatement ps = null;
	KmDocumentMstr dto=  null;
		
	try {
		
	//String sql=SQL_GET_DOCUMENT_DETAILS;
	StringBuffer query =new StringBuffer(SQL_GET_DOCUMENT_DETAILS);	
	con=DBConnection.getDBConnection();
	ps=con.prepareStatement(query.append(" with ur ").toString());
	ps.setInt(1, Integer.parseInt("1"));
	ps.setInt(2, Integer.parseInt(documentId));
	   
	rs = ps.executeQuery();
			
	while(rs.next()){
		dto=new KmDocumentMstr();
		logger.info(rs.getString("chain"));
		String path=rs.getString("chain");
		logger.info(path);
		String documentStringPath="";

		if((path.indexOf("/")+1)<path.lastIndexOf("/")){	
			 documentStringPath=path.substring((path.indexOf("/")+1),path.lastIndexOf("/"));
		}

		dto.setDocumentStringPath(documentStringPath);
		dto.setDocumentId(rs.getString("DOCUMENT_ID"));
		dto.setDocumentDisplayName(rs.getString("DOCUMENT_DISPLAY_NAME"));
		dto.setKeyword(rs.getString("DOCUMENT_KEYWORD"));
		dto.setDocumentName(rs.getString("DOCUMENT_NAME"));
		dto.setDocumentDesc(rs.getString("DOCUMENT_DESC"));
		dto.setDocName(rs.getString("DOC_NAME"));
		//Added by Atul
		dto.setPublishingEndDate(rs.getDate("PUBLISHING_END_DT")+"");
        // Ended by Atul 
	  // dto.setElementName(rs.getString("ELEMENT_NAME"));
		   
		
	}
	
         
	} catch (SQLException e) {
		logger.info(e);	
		
	//logger.severe("SQL Exception occured while find." + "Exception Message: " + e.getMessage());
		throw new KmException("SQLException: " + e.getMessage(), e);
	} catch (Exception e) {
	
	//logger.severe("Exception occured while find." + "Exception Message: " + e.getMessage());
		logger.info("Exception : "+e);
		
		throw new KmException("Exception: " + e.getMessage(), e);
	} finally {
		try {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (con != null) {
				con.close();
			}
	   }catch (Exception e) {
		   logger.error(e);
		}
	}
	return dto;
}

/* (non-Javadoc)
 * @see com.ibm.km.dao.KmDocumentMstrDao#editDocument(com.ibm.km.dto.KmDocumentMstr)
 */
public void editDocument(KmDocumentMstr dto) throws KmException {
	Connection con=null;
	PreparedStatement stmt = null;
	ResultSet rs = null;
	PreparedStatement ps = null;
	try {
		
		//String sql=SQL_EDIT_DOCUMENT;
		StringBuffer query =new StringBuffer(SQL_EDIT_DOCUMENT);	
	
		con=DBConnection.getDBConnection();
		ps=con.prepareStatement(query.append(" with ur").toString());
		ps.setString(1, dto.getKeyword());
		ps.setString(2, dto.getDocumentDesc());
		ps.setString(3, dto.getDocumentDisplayName());
		ps.setString(4,dto.getPublishEndDt()+ " 00:00:00");
		ps.setInt(5, Integer.parseInt(dto.getDocumentId()));
		//Added by Atul
		
		
		//Ended by Atul
         ps.executeUpdate();
		logger.info("Document Details Updated Successfully");	
         
	} catch (SQLException e) {
		logger.info(e);	
		
	//logger.severe("SQL Exception occured while find." + "Exception Message: " + e.getMessage());
		throw new KmException("SQLException: " + e.getMessage(), e);
	} catch (Exception e) {
	
	//logger.severe("Exception occured while find." + "Exception Message: " + e.getMessage());
		logger.info("Exception : "+e);
	
		throw new KmException("Exception: " + e.getMessage(), e);
	} finally {
		try {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (con != null) {
				con.close();
			}
	   } catch (Exception e) {
		   logger.error(e);
		}
	}
}

/* (non-Javadoc)
 * @see com.ibm.km.dao.KmDocumentMstrDao#getDocumentId(java.lang.String)
 */
public String getDocumentId(String documentName) throws KmException {
		Connection con=null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String documentId = null;
		try {
			
			StringBuffer query =new StringBuffer(SQL_GET_DOCUMENT_ID);
			//String sql=SQL_GET_DOCUMENT_ID;
			con=DBConnection.getDBConnection();
			ps=con.prepareStatement(query.append(" with ur ").toString());
			ps.setString(1, documentName);
			rs=ps.executeQuery();
			while(rs.next()){
				documentId=rs.getString("DOCUMENT_ID");
			}
			return documentId;				

		} catch (SQLException e) {
			logger.info(e);	
			
		//logger.severe("SQL Exception occured while find." + "Exception Message: " + e.getMessage());
			throw new KmException("SQLException: " + e.getMessage(), e);
		} catch (Exception e) {
		
		//logger.severe("Exception occured while find." + "Exception Message: " + e.getMessage());
			logger.info("Exception : "+e);
			
			throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (con != null) {
					con.close();
				}
		   } catch (Exception e) {
			   logger.error(e);
			}
		}
		
	}

/* (non-Javadoc)
 * @see com.ibm.km.dao.KmDocumentMstrDao#getDocumentVersions(java.lang.String)
 */
public ArrayList getDocumentVersions(String documentId) throws KmException {
	Connection con=null;
	PreparedStatement stmt = null;
	ResultSet rs = null;
	PreparedStatement ps = null;
	ArrayList documentList=new ArrayList();
	KmDocumentMstr dto=null;
	try {
			
		logger.info("Document Id: "+documentId);
		//String sql=SQL_GET_DOCUMENT_VERSIONS;
		StringBuffer query =new StringBuffer(SQL_GET_DOCUMENT_VERSIONS);
		con=DBConnection.getDBConnection();
		ps=con.prepareStatement(query.append(" with ur ").toString());
		ps.setString(1, documentId);
		rs=ps.executeQuery();
		while(rs.next()){
			dto=new KmDocumentMstr();
			
			dto.setDocumentName(rs.getString("DOCUMENT_NAME"));
			dto.setDocumentId(rs.getString("DOCUMENT_ID"));
			documentList.add(dto);
		}
		return documentList;				

	} catch (SQLException e) {
		logger.info(e);	
		
	//logger.severe("SQL Exception occured while find." + "Exception Message: " + e.getMessage());
		throw new KmException("SQLException: " + e.getMessage(), e);
	} catch (Exception e) {
		
	//logger.severe("Exception occured while find." + "Exception Message: " + e.getMessage());
		logger.info("Exception : "+e);
		
		throw new KmException("Exception: " + e.getMessage(), e);
	} finally {
		try {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (con != null) {
				con.close();
			}
	   } catch (Exception e) {
		   logger.error(e);
		}
	}	
}

/* (non-Javadoc)
 * @see com.ibm.km.dao.KmDocumentMstrDao#getDocuments(java.lang.String)
 */
public String[] getDocuments(String parentId) throws KmException {
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
		
	ArrayList list=new ArrayList();	
	try
	{
		StringBuffer query =new StringBuffer(SQL_GET_CHILDREN_DOCUMENTS);
		con = DBConnection.getDBConnection();
		pstmt = con.prepareStatement(query.append(" with ur ").toString());
		pstmt.setString(1,parentId);
			
		rs = pstmt.executeQuery();
		int i=0;	
		while(rs.next())
		{
				
			list.add(rs.getString("DOCUMENT_ID"));
		}
		String[]documents=(String[])list.toArray(new String[list.size()]);
		return documents;	
	}
		
	catch(SQLException e)
	{
		logger.error(e);
		throw new KmException(e.getMessage(),e);
	}
	catch(DAOException e){
		logger.error(e);
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
			logger.error(e);
			throw new KmException(e.getMessage(),e);
		}
	}	
}

/* (non-Javadoc)
 * @see com.ibm.km.dao.KmDocumentMstrDao#deleteDocuments(java.lang.String[])
 */
public void deleteDocuments(String[] documents,String updatedBy) throws KmException {
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	DeleteFiles delete= new DeleteFiles();
	//String sql=SQL_DELETE_DOCUMENTS;
	StringBuffer query =new StringBuffer(SQL_DELETE_DOCUMENTS);
	for(int i=1;i<documents.length;i++){
		
		query.append(" OR DOCUMENT_ID = ? ").toString();		
	}
	query.append(" ) ").toString();	
	
	try
	{
		logger.info(query);
		con = DBConnection.getDBConnection();
		pstmt = con.prepareStatement(query.append(" with ur").toString());
		pstmt.setInt(1,Integer.parseInt(updatedBy));
		for(int i=0;i<documents.length;i++){
			pstmt.setInt(i+2, Integer.parseInt(documents[i]));	
		}		
		int documentsUpdated = pstmt.executeUpdate();
		logger.info("No.Of Documents Updated :"+documentsUpdated);
		for(int i=0;i<documents.length;i++){
			delete.deleteFiles(documents[i]);
		}
	}
		
	catch(SQLException e)
	{
		logger.error(e);
		throw new KmException(e.getMessage(),e);
	}
	catch(DAOException e){
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
			logger.error(e);
			throw new KmException(e.getMessage(),e);
		}
	}	
	
}

/* (non-Javadoc)
 * @see com.ibm.km.dao.KmDocumentMstrDao#getCircleApprover(java.lang.String)
 */
public String getCircleApprover(String circleId) throws KmException {
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
		
	String circleApprover=null;
	try
	{
		StringBuffer query =new StringBuffer(SQL_GET_CIRCLE_APPROVER);
		con = DBConnection.getDBConnection();
		pstmt = con.prepareStatement(query.append(" with ur ").toString());
		pstmt.setString(1,circleId);
			
		rs = pstmt.executeQuery();
		int i=0;	
		while(rs.next())
		{
				
			circleApprover=rs.getString("CIRCLE_ADMIN_ID");
		}
		
		return circleApprover;	
	}
		
	catch(SQLException e)
	{
		logger.error(e);
		throw new KmException(e.getMessage(),e);
	}
	catch(DAOException e){
		logger.error(e);
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
			logger.error(e);
			throw new KmException(e.getMessage(),e);
		}
	}	
	
}

/* (non-Javadoc)
 * @see com.ibm.km.dao.KmDocumentMstrDao#getDocPath(java.lang.String)
 */
public String getDocPath(String documentId) throws KmException {
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
		
	String docName="";
	String docPath="";
	try
	{
		logger.info("asa::doc id:::"+documentId);
		StringBuffer query =new StringBuffer(SQL_GET_DOC_PATH);
		con = DBConnection.getDBConnection();
		pstmt = con.prepareStatement(query.append(" with ur ").toString());
		pstmt.setInt(1,Integer.parseInt(documentId));
		logger.info("asa:::SQL_GET_DOC_PATH:" + SQL_GET_DOC_PATH+" with ur" + " Doc id:" + documentId);
		rs = pstmt.executeQuery();
		int i=0;	
		if(rs.next())
		{
				
			docPath=rs.getString("DOCUMENT_PATH");
			docName=rs.getString("DOCUMENT_NAME");
		}
		
		return docPath+"/"+docName;	
	}
		
	catch(SQLException e)
	{
		logger.error("Exception in KmDocumentMstrDaoImpl >> getDocPath >> for document ID:" + documentId);
		e.printStackTrace();
		throw new KmException(e.getMessage(),e);
	}
	catch(DAOException e){
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

/* (non-Javadoc)
 * @see com.ibm.km.dao.KmDocumentMstrDao#changeDocumentPathsInDb(java.lang.String[])
 */
public String[] changeDocumentPathsInDb(String[] documentElementIds) throws KmException {
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	StringBuffer query =new StringBuffer(SQL_GET_ALL_DOCUMENT_PATHS);
	//String sql=SQL_GET_ALL_DOCUMENT_PATHS;
	//String[]documentPaths;
	//String[] documentIds;
	ArrayList documentIdList=new ArrayList();
	ArrayList documentPathList=new ArrayList();
	String parentString="";
	String documentId="";
	boolean success=true;
	for(int i=1;i<documentElementIds.length;i++){
		query.append(" OR NEE.ELEMENT_ID = ? ").toString();		
	}
	query.append(" ) ").toString();	
	
	try
	{
		//logger.info(sql);
		con = DBConnection.getDBConnection();
		pstmt = con.prepareStatement(query.append(" with ur ").toString());
		for(int i=0;i<documentElementIds.length;i++){
			pstmt.setString(i+1, documentElementIds[i]);	
		}		
		rs = pstmt.executeQuery();
		String trimmedParentString="";
		int i=0;
		while(rs.next()){
		
			parentString= rs.getString("PARENT_STRING");	
			documentId=rs.getString("DOCUMENT_ID");
		//	documentName=rs.getString("DOCUMENT_NAME");
			StringTokenizer tokenized = new StringTokenizer(parentString);
			trimmedParentString="";
			while(tokenized.hasMoreTokens()){
				trimmedParentString=trimmedParentString+tokenized.nextToken();
			}
			trimmedParentString=trimmedParentString.substring(0,trimmedParentString.lastIndexOf("/"));
		//	documents[i]=trimmedParentString;
		//	//System.out.println("trimmedParentString : "+ trimmedParentString);
			documentIdList.add(documentId);
			documentPathList.add(trimmedParentString);
			
			i++;
		
		}
		
		String[]documentIds=(String[])documentIdList.toArray(new String[documentIdList.size()]);
		String[]documentPaths=(String[])documentPathList.toArray(new String[documentPathList.size()]);
		updateDocumentPaths(documentIds,documentPaths);
	}
		
	catch(SQLException e)
	{
		logger.error(e);
		success=false;
		throw new KmException(e.getMessage(),e);
	}
	catch(DAOException e){
		logger.error(e);
		throw new KmException(e.getMessage(),e);
	}
	catch(Exception e){
		logger.error(e);
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
			logger.error(e);
			throw new KmException(e.getMessage(),e);
		}
	}	
	return documentElementIds;
	
}

/* (non-Javadoc)
 * @see com.ibm.km.dao.KmDocumentMstrDao#changeDocumentPathsInDb(java.lang.String)
 */
public boolean changeDocumentPathsInDb(String elementId) throws KmException {
	//getDocumentPath
	Connection con=null;
	PreparedStatement stmt = null;
	ResultSet rs = null;
	PreparedStatement ps = null;
	//String sql = SQL_UPDATE_DOC_PATH;
	StringBuffer query =new StringBuffer(SQL_UPDATE_DOC_PATH);
	boolean success=false;
	try {
					
		String documentPath=getDocumentPath(elementId);
		documentPath=documentPath.substring(0,documentPath.lastIndexOf("/"));
		//logger.info(" ELEMENT ID   >>>>>:::::"+elementId+"       documentPath >>>>>>:::::::"+documentPath);
		con = DBConnection.getDBConnection();
		ps = con.prepareStatement(query.append(" with ur").toString());
		ps.setString(1, documentPath);
		ps.setString(2, elementId);
		int n = ps.executeUpdate();
		logger.info("Path changed successfully");
		success=true;	
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
	}return success;	
}

/* (non-Javadoc)
 * @see com.ibm.km.dao.KmDocumentMstrDao#updateDocumentPaths(java.lang.String[], java.lang.String[])
 */
public boolean updateDocumentPaths(String[] allDocuments, String[] alldocumentPaths) throws KmException {
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	//String sql=UPDATE_ALL_DOCUMENT_PATHS;
	StringBuffer query =new StringBuffer(UPDATE_ALL_DOCUMENT_PATHS);
	boolean success=false;
	logger.info("Entered in approveFile method");
		try
		{
			con = getConnection();
			pstmt = con.prepareStatement(query.append(" with ur").toString());
				
			int i = 0;
				
			while( i < allDocuments.length)
			{
				
				pstmt.setString(1,alldocumentPaths[i]);
				pstmt.setInt(2,Integer.parseInt(allDocuments[i]));
				
				pstmt.addBatch();
				i++;
			}
			try {
				int arr1[]= pstmt.executeBatch();
				for(i=0;i<arr1.length;i++) {
					//System.out.println("exception"+arr1[i]);
					
				}
			}
			catch(BatchUpdateException buex)
			{    
		    	
				System.err.println("Contents of BatchUpdateException:");
  System.err.println(" Update counts: ");
  int [] updateCounts = buex.getUpdateCounts();              
  for (int j = 0; j < updateCounts.length; j++) {
	System.err.println("  Statement " + j + ":" + updateCounts[j]);
  }
  System.err.println(" Message: " + buex.getMessage());     
  System.err.println(" SQLSTATE: " + buex.getSQLState());
  System.err.println(" Error code: " + buex.getErrorCode());
  SQLException ex = buex.getNextException();                
  while (ex != null) {                                     
	System.err.println("SQL exception:");
	System.err.println(" Message: " + ex.getMessage());
	System.err.println(" SQLSTATE: " + ex.getSQLState());
	System.err.println(" Error code: " + ex.getErrorCode());
	ex = ex.getNextException();

			}
			}
			//con.commit();
		success=true;
	}
		
	catch(SQLException e)
	{
		logger.error(e);
		throw new KmException(e.getMessage(),e);
	}
	catch(DAOException e){
		logger.error(e);
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
			logger.error(e);
			throw new KmException(e.getMessage(),e);
		}
	}
	return success;	
}

/* (non-Javadoc)
 * @see com.ibm.km.dao.KmDocumentMstrDao#getDocumentDetails(java.lang.String)
 */
public KmDocumentMstr getDocumentDetails(String documentId) throws KmException {
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	KmDocumentMstr document=null;	
	String docName="";
	String docPath="";
	try
	{
		StringBuffer query =new StringBuffer(SQL_GET_DOCUMENT);
		con = DBConnection.getDBConnection();
		pstmt = con.prepareStatement(query.append(" with ur ").toString());
		pstmt.setString(1,documentId);
			
		rs = pstmt.executeQuery();
		int i=0;	
		if(rs.next())
		{
			document=new KmDocumentMstr();	
			
			document.setDocumentPath(rs.getString("DOCUMENT_PATH")+"/"+rs.getString("DOCUMENT_NAME"));
			document.setDocName(rs.getString("DOC_NAME"));
			document.setDocumentDisplayName(rs.getString("DOCUMENT_DISPLAY_NAME"));
			document.setDocumentName(rs.getString("DOCUMENT_NAME"));
			
		}
		
		
	}
		
	catch(SQLException e)
	{
		logger.error(e);
		throw new KmException(e.getMessage(),e);
	}
	catch(DAOException e){
		logger.error(e);
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
			logger.error(e);
			throw new KmException(e.getMessage(),e);
		}
	}	
	return document;
}	
/*  KM PHASE II Dao Implemetation for archived search   */
		
		
public String isFavouriteDocument(String documentId, String userId) throws KmException {
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String isFavDoc="no";
	try
	{  		
		StringBuffer query =new StringBuffer(SQL_IS_FAV_DOCUMENT);
		con = DBConnection.getDBConnection();
		pstmt = con.prepareStatement(query.toString());
		pstmt.setString(1,documentId);
		pstmt.setString(2,userId);
		rs = pstmt.executeQuery();
		if(rs.next())
		{			
			isFavDoc = "on";			
		}		
		
	}
		
	catch(SQLException e)
	{
		logger.error(e);
		throw new KmException(e.getMessage(),e);
	}
	catch(DAOException e){
		logger.error(e);
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
			logger.error(e);
			throw new KmException(e.getMessage(),e);
		}
	}	
	return isFavDoc;
}	

public ArrayList archivedSearch(KmDocumentMstr dto) throws KmException {
		Connection con=null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		ArrayList fileList = new ArrayList();
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		KmDocumentMstr dto1;
		
		try {
		//String sql=SQL_SELECTARCHIVED_FILES;
		StringBuffer query =new StringBuffer(SQL_SELECTARCHIVED_FILES);
		logger.info("ROOT : "+dto.getRoot());
		String parentId=dto.getElementId();
		String root=dto.getRoot();
		String keyword=dto.getKeyword();
		String searchType=dto.getSearchType();
		logger.info("elementId : "+parentId+"    keyword : "+keyword+ "  Search Type :"+searchType);

		//logger.info(query.append .toString());
		con=DBConnection.getDBConnection();
		ps=con.prepareStatement(query.append(" with ur").toString());
		ps.setInt(1, Integer.parseInt(parentId));
		rs = ps.executeQuery();
		while(rs.next()){
			dto1=new KmDocumentMstr();
			logger.info(rs.getString("chain"));
			String path=rs.getString("chain");
			logger.info(path);
			String documentStringPath="";

			if((path.indexOf("/")+1)<path.lastIndexOf("/")){	
				 documentStringPath=path.substring((path.indexOf("/")+1),path.lastIndexOf("/"));
			}

			dto1.setDocumentStringPath(documentStringPath);
			dto1.setDocumentId(rs.getString("DOCUMENT_ID"));
			dto1.setElementId(rs.getString("ELEMENT_ID"));
			dto1.setDocumentDisplayName(rs.getString("DOCUMENT_DISPLAY_NAME"));
			String published_end_dt="";
			published_end_dt=rs.getString("PUBLISHING_END_DT");
			if(!(published_end_dt.equalsIgnoreCase(""))) {
				published_end_dt=published_end_dt.substring(0,10);
			}
				dto1.setPublishingEndDate(published_end_dt);
		   fileList.add(dto1);	
		}
		return fileList;
         
	} catch (SQLException e) {
		
		logger.error(e);
//	logger.severe("SQL Exception occured while find." + "Exception Message: " + e.getMessage());
		throw new KmException("SQLException: " + e.getMessage(), e);
	} catch (Exception e) {

//	logger.severe("Exception occured while find." + "Exception Message: " + e.getMessage());
		logger.error(e);
		throw new KmException("Exception: " + e.getMessage(), e);
	} finally {
		try {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
			if (rs1 != null)
				rs1.close();
			if (ps1 != null)
				ps1.close();
			if (con != null) {
				con.close();
			}
	   } catch (Exception e) {
		   logger.error(e);
		}
	}
	}

/* KM Phase II   csr Keyword search for actor 4 & 6*/ 
	/* (non-Javadoc)
		 * @see com.ibm.km.dao.KmDocumentMstrDao#csrkeywordsearch(com.ibm.km.dto.KmDocumentMstr)
		 */
		public ArrayList csrKeywordSearch(KmSearch dto) throws KmException {
			Connection con=null;
			ResultSet rs = null;
			ArrayList fileList = new ArrayList();
			PreparedStatement ps = null;
			KmDocumentMstr dto1;
		
			try {
			//String sql=SQL_SELECT_KEYSEARCH_FILES;
			StringBuffer query =new StringBuffer(SQL_SELECT_KEYSEARCH_FILES);
//			logger.info("ROOT : "+dto.getRoot());
			String parentId=dto.getElementId();
			String root=dto.getRoot();
			String keyword=dto.getKeyword();
			
			String searchType=dto.getSearchType();
//			logger.info("elementId : "+parentId+"    keyword : "+keyword+ "  Search Type :"+searchType);

			query.append("AND doc.APPROVAL_STATUS = 'A' AND doc.DOCUMENT_KEYWORD LIKE '%").append(keyword).append("%' and (date(current timestamp) between date(publishing_start_dt) and date(publishing_end_dt))	 ORDER BY doc.UPDATED_DT DESC").toString();
			
			logger.info(query.append(" with ur ").toString());
			con=DBConnection.getDBConnection();
			ps=con.prepareStatement(query.append(" with ur ").toString());
			ps.setInt(1, Integer.parseInt(parentId));
			rs = ps.executeQuery();
			
			
			while(rs.next()){
				dto1=new KmDocumentMstr();
		//		logger.info(rs.getString("chain"));
				dto1.setDocumentPath(rs.getString("DOCUMENT_PATH"));
				String path=rs.getString("chain");
			//	logger.info(path);
				String documentStringPath="";

				if((path.indexOf("/")+1)<path.lastIndexOf("/")){	
					 documentStringPath=path.substring((path.indexOf("/")+1),path.lastIndexOf("/"));
				}

				dto1.setDocumentStringPath(documentStringPath);
				dto1.setDocumentId(rs.getString("DOCUMENT_ID"));
				dto1.setElementId(rs.getString("ELEMENT_ID"));
				
				dto1.setDocumentDisplayName(rs.getString("DOCUMENT_DISPLAY_NAME"));
				String published_dt="";
				published_dt=rs.getString("PUBLISHING_START_DT");
				String published_end_dt="";
				published_end_dt=rs.getString("PUBLISHING_END_DT");
					if(!(published_end_dt.equalsIgnoreCase(""))) {
						published_end_dt=published_end_dt.substring(0,10);
					}
					dto1.setPublishingEndDate(published_end_dt);
				  fileList.add(dto1);	
			}
					return fileList;
				
		}
		 catch (SQLException e) {
			 logger.error(e);
//		logger.severe("SQL Exception occured while find." + "Exception Message: " + e.getMessage());
			throw new KmException("SQLException: " + e.getMessage(), e);
		} catch (Exception e) {

//		logger.severe("Exception occured while find." + "Exception Message: " + e.getMessage());
			logger.error(e);
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
			   logger.error(e);
			}
		}
		}

		public KmDocumentMstr getSingleDoc(String subCategoryId) throws KmException, DAOException {
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String documentId=null;	
			KmDocumentMstr document=null;
			try
			{
				//StringBuffer query =new StringBuffer("SELECT DOC.DOCUMENT_ID,DOC.DOCUMENT_PATH,DOC.DOCUMENT_NAME FROM KM_ELEMENT_MSTR ELE INNER JOIN KM_DOCUMENT_MSTR DOC ON ELE.ELEMENT_ID = DOC.ELEMENT_ID WHERE  ELE.PARENT_ID = ?	 AND DOC.STATUS = 'A' AND (DATE(CURRENT TIMESTAMP) BETWEEN DATE(DOC.PUBLISHING_START_DT) AND DATE(DOC.PUBLISHING_END_DT)) AND DOC.APPROVAL_STATUS = 'A' WITH UR ");
				String sql="SELECT DOC.DOCUMENT_ID,DOC.DOCUMENT_PATH,DOC.DOCUMENT_NAME FROM KM_ELEMENT_MSTR ELE LEFT JOIN KM_DOCUMENT_MSTR DOC ON ELE.ELEMENT_ID = DOC.ELEMENT_ID WHERE  ELE.PARENT_ID = ?	 AND ((DOC.STATUS = 'A' AND (DATE(CURRENT TIMESTAMP) BETWEEN DATE(DOC.PUBLISHING_START_DT) AND DATE(DOC.PUBLISHING_END_DT)) AND DOC.APPROVAL_STATUS = 'A') OR (doc.STATUS IS NULL AND ELE.STATUS = 'A'))  WITH UR ";
				con = DBConnection.getDBConnection();
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1,subCategoryId);
					
				rs = pstmt.executeQuery();
				int i=0;
				
				while(rs.next())
				{
					i++;
					documentId=rs.getString("DOCUMENT_ID");
					if(i>1 || documentId == null ){
						document=null;
						break;
					}
					document=new KmDocumentMstr();
					document.setDocumentId(documentId);	
					document.setDocumentPath(rs.getString("DOCUMENT_PATH"));
					document.setDocumentName(rs.getString("DOCUMENT_NAME"));
					
				}
				
				
			}
				
			catch(SQLException e)
			{
				logger.error(e);
				throw new KmException(e.getMessage(),e);
			}
			catch(DAOException e){
				logger.error(e);
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
					logger.error(e);
					throw new KmException(e.getMessage(),e);
				}
			}	
			return document;
		}

		public int getDocumentType(String documentId) throws KmException, DAOException {
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;	
			int documentType=0;
			try
			{
				String sql="select DOC_TYPE from KM_DOCUMENT_MSTR where DOCUMENT_ID = ? with ur";
				con = DBConnection.getDBConnection();
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1,documentId);
				rs = pstmt.executeQuery();
				
				while(rs.next())
				{
					documentType =rs.getInt("DOC_TYPE");
				}
			}
			catch(SQLException e)
			{
				logger.error(e);
				throw new KmException(e.getMessage(),e);
			}
			catch(DAOException e){
				logger.error(e);
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
					logger.error(e);
					throw new KmException(e.getMessage(),e);
				}
			}	
			return documentType;
		}
		
		public ArrayList<String> docFilterAsPerDocType(ArrayList docIdList,int docType,String isTop15) throws KmException 
		{
			Connection con=null;
			Statement stmt = null;
			ResultSet rs = null;
			PreparedStatement ps = null;
			String commaDelimitted = ""; 
			ArrayList filteredDocIdList = new ArrayList();
			try {
				
				logger.info("::::value of isTOP15:::::"+isTop15);
				if (isTop15 != null && isTop15.equals("Y"))
				{
					logger.info("****inside docFilterAsPerDocType *******");
					con=DBConnection.getDBConnection();
					ps = con.prepareStatement(SQL_TOP15_BILL_PLANS);
					ps.setInt(1,Integer.parseInt(PropertyReader.getAppValue("billplan.search.top15.history.days")));
					rs=ps.executeQuery();
					while(rs.next())
					{
						filteredDocIdList.add(rs.getInt(1)+"");
					}
					return filteredDocIdList;

				}
				
				logger.info("docFilterAsPerDocType: docIdList:" + docIdList);
				boolean first = true;
				if(docIdList == null || docIdList.size() == 0)
					return filteredDocIdList;

				for(int i = 0; i < docIdList.size(); i++) {
					if(docIdList.get(i) instanceof KmDocumentMstr) {
						KmDocumentMstr dto = (KmDocumentMstr) docIdList.get(i);
						if (!first)
							commaDelimitted += " , ";
						commaDelimitted += dto.getDocumentId();
						first = false;
					}
				}
				
/*				if(docIdList != null)
				for (Iterator<String> it = docIdList.iterator() ; it.hasNext() ; ){  
					commaDelimitted += (it.next()).toString() ;  
				  if (it.hasNext()) {  
					  commaDelimitted += ", ";   
				  }  
				}  
				*/
				
					if(commaDelimitted.equals(""))
					{ 
						return filteredDocIdList;
					}
					
					
					StringBuffer query =new StringBuffer(SQL_DOC_TYPE_FILTER);
					query.append(commaDelimitted);
					con=DBConnection.getDBConnection();
					//System.out.println("billplan.search.max.limit "+Integer.parseInt(PropertyReader.getAppValue("billplan.search.max.limit")));
					if(docType == 5){
						logger.info("GHADHA CODE !!!!!");
						ps = con.prepareStatement(query.append(" ) AND STATUS='A' WITH UR").toString());
						ps.setInt(1,docType);
						rs=ps.executeQuery();
					}
					else{
					ps = con.prepareStatement(query.append(" ) and  current date between date(PUBLISHING_START_DT) and date(PUBLISHING_END_DT) AND STATUS='A' fetch first "+Integer.parseInt(PropertyReader.getAppValue("billplan.search.max.limit"))+" rows only WITH UR").toString());
					ps.setInt(1,docType);
					rs=ps.executeQuery();
					}
					while(rs.next())
					{
						filteredDocIdList.add(rs.getInt(1)+"");
					}
				
				
				return filteredDocIdList;
				
			} catch (SQLException e) {
				e.printStackTrace();
				logger.info(e);
				throw new KmException("SQLException: " + e.getMessage(), e);
			} catch (Exception e) {
				e.printStackTrace();
				logger.info(e);
				throw new KmException("Exception: " + e.getMessage(), e);
			} finally {
				try {
					if (rs != null)
						rs.close();
					if (stmt != null)
						stmt.close();
					if (ps != null)
						ps.close();
					if (con != null) {
						con.close();
					}
				} catch (Exception e) {
					logger.error(e);
				}
			}
			
		}	
					
		
		public ArrayList<KmDocumentMstr> getTopDocuments(String elementId, String docType) throws KmException {
			Connection conn=null;
			PreparedStatement pst=null;
			ResultSet rs = null;
			String SQL_TOP_DOCUMENTS = " WITH NEE(ELEMENT_ID,CHAIN, ELEMENT_LEVEL_ID,element_name,parent_id,status) AS (SELECT  ELEMENT_ID, cast(element_name as VARCHAR(" + PropertyReader.getAppValue("search.path.size") + "))ELEMENT_NAME ,ELEMENT_LEVEL_ID,element_name,parent_id,status FROM KM_ELEMENT_MSTR  WHERE ELEMENT_ID in ("+ elementId +") UNION ALL SELECT NPLUS1.ELEMENT_ID, NEE.CHAIN || '>' || NPLUS1.ELEMENT_NAME, NPLUS1.ELEMENT_LEVEL_ID , NPLUS1.element_name,NPLUS1.parent_id, NPLUS1.status FROM KM_ELEMENT_MSTR AS NPLUS1, NEE  WHERE NEE.ELEMENT_ID=NPLUS1.PARENT_ID ) SELECT nee.element_id,nee.chain, nee.element_name,nee.parent_id,nee.element_level_id,b.document_id,b.DOC_TYPE,b.status,nee.status as element_status , b.approval_status,publishing_start_dt, b.publishing_end_dt, b.NUMBER_OF_HITS FROM NEE LEFT JOIN KM_DOCUMENT_MSTR B ON nee.ELEMENT_ID=B.ELEMENT_ID where (b.status='A' or b.status is null) and (b.approval_status='A' or b.approval_status is null) and nee.status='A' and b.doc_type in (" +docType + ") and (date(current timestamp) between date(publishing_start_dt) and date(publishing_end_dt) or publishing_start_dt is null or publishing_end_dt is null) order by b.NUMBER_OF_HITS desc FETCH FIRST 5 ROWS only with ur ";
			
			ArrayList<KmDocumentMstr> retArr = new ArrayList<KmDocumentMstr>();
			logger.info("Values for elementId:" +elementId + "  and DocType:" + docType);
			if(elementId == null || docType == null || elementId.equals("") || docType.equals("") ) {
				logger.info("Invalid Values for elementId:" +elementId + "  and DocType:" + docType);
				return retArr;
			}
			try {
				conn=DBConnection.getDBConnection();
				logger.info("getTopDocuments >> query:" +SQL_TOP_DOCUMENTS );
				pst = conn.prepareStatement(SQL_TOP_DOCUMENTS);
				rs = pst.executeQuery();
				while(rs.next())
				{
					KmDocumentMstr dto = new KmDocumentMstr();
					dto.setElementId(rs.getString("ELEMENT_ID") );
					dto.setDocumentId(rs.getString("DOCUMENT_ID"));
					dto.setDocType(rs.getInt("DOC_TYPE"));
					dto.setParentId(rs.getString("PARENT_ID"));
					dto.setElementName(rs.getString("ELEMENT_NAME"));
					dto.setNumberOfHits(rs.getString("NUMBER_OF_HITS"));
					dto.setDocumentPath(rs.getString("CHAIN"));
					retArr.add(dto);
				}
				
			} catch (SQLException e) {
				throw new KmException("SQLException: " + e.getMessage(), e);
			} catch (Exception e) {
					logger.error("Exception occured while getTopDocuments. Exception Message: " + e.getMessage());
				throw new KmException("Exception: " + e.getMessage(), e);
			} finally {
				try
				{
					DBConnection.releaseResources(conn,pst,rs);
				}
				catch(DAOException e)
				{
					logger.error(e);
					throw new KmException(e.getMessage(),e);
				}
			}
			return retArr;
		}
		
		
		
		@SuppressWarnings("finally")
		public int numberOfSentSms(String mobileNo) throws KmException {
			
			Connection conn=null;
			PreparedStatement pst=null;
			ResultSet rs = null;
			int numberOfSms = 0;
				try {
				conn=DBConnection.getDBConnection();
				logger.info("Query to get number of sms sent  :" +SQL_NUMBER_OF_SMS);
				StringBuffer query =new StringBuffer(SQL_NUMBER_OF_SMS);
				pst = conn.prepareStatement(query.append(" with ur").toString());
				pst.setString(1, mobileNo);
				rs = pst.executeQuery();
				while(rs.next()){
					numberOfSms = rs.getInt("SMSCOUNT");
				}			
			}
			catch(SQLException e)
	        {
	            logger.error((new StringBuilder("SQL Exception occured while update.Exception Message: ")).append(e.getMessage()).toString());
	           // throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
	        }
	        catch(Exception e)
	        {
	            logger.error((new StringBuilder("Exception occured while Update.Exception Message: ")).append(e.getMessage()).toString());
	           // throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
	        }
	        finally{
	        try
	        {
	           
	            DBConnection.releaseResources(conn, pst, rs);
	           
	        }
	        catch(Exception e)
	        {
	            logger.error((new StringBuilder("DAO Exception occured while Update.Exception Message: ")).append(e.getMessage()).toString());
	            //throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
	        }
			return numberOfSms;
		}		
	
	}
		@SuppressWarnings("finally")
		@Override
		public int getMaxNoOfSMS() {
		
			Connection conn=null;
			PreparedStatement pst=null;
			ResultSet rs = null;
			int maxNoOfSms = 0;
				try {
				conn=DBConnection.getDBConnection();
				logger.info("Query to get maximum number of sms :" +SQL_MAX_NUMBER_OF_SMS);
				StringBuffer query =new StringBuffer(SQL_MAX_NUMBER_OF_SMS);
				pst = conn.prepareStatement(query.append(" with ur").toString());
				rs = pst.executeQuery();
				while(rs.next()){
					maxNoOfSms = rs.getInt("SMSCOUNT");
				}			
			}
			catch(SQLException e)
	        {
	            logger.error((new StringBuilder("SQL Exception occured while update.Exception Message: ")).append(e.getMessage()).toString());
	           // throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
	        }
	        catch(Exception e)
	        {
	            logger.error((new StringBuilder("Exception occured while Update.Exception Message: ")).append(e.getMessage()).toString());
	           // throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
	        }
	        finally{
	        try
	        {
	           
	            DBConnection.releaseResources(conn, pst, rs);
	           
	        }
	        catch(Exception e)
	        {
	            logger.error((new StringBuilder("DAO Exception occured while Update.Exception Message: ")).append(e.getMessage()).toString());
	            //throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
	        }
			return maxNoOfSms;
	
			
		}
}
		
}
