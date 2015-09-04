/*
 * Created on Apr 30, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.ibm.km.dto.KmSearchDetailsDTO;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import com.ibm.km.common.Constants;
import com.ibm.km.common.PropertyReader;
import com.ibm.km.dao.DBConnection;
import com.ibm.km.dao.KmSearchDao;
import com.ibm.km.dto.KmConfigureDataForSMSDto;
import com.ibm.km.dto.KmDocumentMstr;
import com.ibm.km.dto.KmSearch;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.KmDocumentMstrFormBean;
import com.ibm.km.forms.KmSearchFormBean;

/**
 * @author Anil
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class KmSearchDaoImpl implements KmSearchDao {

	// cast(doc.UPLOADED_DT as date) between cast(? as date) and cast(? as date)
	// ORDER BY doc.DOCUMENT_ID
	private static Logger logger = Logger.getLogger(KmSearchDaoImpl.class);
	protected static final String SQL_GET_VERSION_COUNT = "select count(document_id)as VERSION_COUNT from Km_Document_mstr where element_id in(select element_id from KM_DOCUMENT_MSTR where document_id = ? ) and  approval_status = 'O'";
	protected static final String SQL_SELECT_FILES = "WITH nee(element_id,chain) AS ( SELECT  ELEMENT_ID, cast(element_name as varchar("
			+ PropertyReader.getAppValue("search.path.size")
			+ "))  FROM KM_ELEMENT_MSTR WHERE element_id = ? UNION ALL SELECT nplus1.element_id,nee.chain || '/' || nplus1.element_name FROM KM_ELEMENT_MSTR as nplus1, nee WHERE nee.element_id=nplus1.PARENT_ID) SELECT doc.DOCUMENT_ID, chain,doc.DOCUMENT_NAME, doc.PUBLISHING_START_DT,doc.PUBLISHING_END_DT,doc.DOCUMENT_DISPLAY_NAME,doc.DOC_TYPE, doc.APPROVAL_STATUS, doc.UPLOADED_DT,doc.APPROVAL_REJECTION_DT, doc.DOCUMENT_PATH, ele.ELEMENT_ID,doc.UPDATED_DT FROM KM_DOCUMENT_MSTR doc inner join KM_ELEMENT_MSTR ele on doc.ELEMENT_ID=ele.ELEMENT_ID inner join  nee on doc.element_id=nee.element_id  WHERE doc.STATUS ='A'";
	protected static final String SQL_GET_SEARCHED_DOCUMENTS = "WITH nee(element_id,chain, element_level_id) AS  ( SELECT  ELEMENT_ID, cast(element_name as varchar("
			+ PropertyReader.getAppValue("search.path.size")
			+ ")) , element_level_id  FROM KM_ELEMENT_MSTR  WHERE element_id =  ? UNION ALL  SELECT nplus1.element_id,nee.chain || '/' || nplus1.element_name, nplus1.element_level_id  FROM KM_ELEMENT_MSTR as nplus1, nee  WHERE nee.element_id=nplus1.PARENT_ID)  SELECT doc.DOCUMENT_ID, chain,doc.DOCUMENT_NAME, doc.DOCUMENT_DISPLAY_NAME, doc.APPROVAL_STATUS, doc.UPLOADED_DT,doc.APPROVAL_REJECTION_DT, doc.DOCUMENT_PATH, doc.doc_type, nee.ELEMENT_ID,  doc.PUBLISHING_START_DT, doc.PUBLISHING_END_DT FROM KM_DOCUMENT_MSTR doc, nee WHERE nee.element_level_id=0 AND doc.element_id=nee.element_id and doc.document_id in ( ";
	protected static final String GET_PARENT_LOB = "SELECT ELEMENT_NAME FROM KM_ELEMENT_MSTR "
			+ "WHERE ELEMENT_ID = (SELECT PARENT_ID FROM KM_ELEMENT_MSTR WHERE ELEMENT_ID = ? ) WITH UR";
	protected static final String GET_ARC_DETAILS = "SELECT CIRCLE_NAME, CITY_NAME, ARC_STORE, PINCODE, ADDRESS, TIMING, AVAILABILITY_OF_CCDC_MACHINE, AVAILABILITY_OF_DOCTOR_SIM, ARCOR, ZONE, SRNO FROM KM2.KM_ARC_DETAILS WHERE LOWER(TRIM(STATUS))='a' AND ";
	protected static final String GET_DIST_DETAILS = "SELECT PINCODE, STATE_NAME, CITY_NAME, PIN_CATEGORY, AREA, HUB, CIRCLE, SFANDSSDNAME, SFANDSSDMAILID, SFANDSSDCONTACTNO, TSM_NAME, TSM_MAIL_ID, TSM_CONTACT_NO,"
			+ " SR_MANAGER, SR_MANAGER_MAIL_ID, SR_MANAGER_CONTACT_NO, ASM_NAME, ASM_MAIL_ID, ASM_CONTACT_NO, SRNO  FROM KM2.KM_DIST_DETAILS WHERE  LOWER(TRIM(STATUS))='a' AND ";
	protected static final String GET_COORDS_DETAILS = "SELECT CIRCLE_NAME, COMPANY_NAME, COMPANY_SPOC_NAME, COMPANY_SPOC_EMAILID, COMPANY_SPOC_CONTACTNO, RM_NAME, RM_EMAIL_ID, RM_CONTACTNO, REQUESTOR, REQUESTOR_LOCATION, REQUESTOR_CONT_NO, REQUESTED_DATE, SRNO "
			+ "FROM KM2.KM_COORDINATOR_DETAILS WHERE  LOWER(TRIM(STATUS))='a' AND ";
	protected static final String GET_PACK_DETAILS = "SELECT BPD.TOPIC,BPD.HEADER,BPD.CONTENT,BPD.FROM_DT,BPD.TO_DT,CM.CIRCLE_NAME  FROM km2.KM_BP_DATA BPD,km2.KM_CIRCLE_MSTR CM WHERE BPD.CIRCLE_ID = CM.CIRCLE_ID AND (LOWER(TRIM(BPD.TOPIC)) ";
	protected static final String GET_PACK_DETAILS_FOR_CSR = "SELECT BPD.TOPIC,BPD.HEADER,BPD.CONTENT,BPD.FROM_DT,BPD.TO_DT,CM.CIRCLE_NAME FROM km2.KM_BP_DATA BPD,km2.KM_USER_MSTR UM,km2.KM_CIRCLE_MSTR CM"
			+ " WHERE UM.USER_LOGIN_ID = ? and (BPD.CIRCLE_ID = UM.CIRCLE_ID OR BPD.CIRCLE_ID = 1) and CM.CIRCLE_ID = BPD.CIRCLE_ID and UM.CIRCLE_ID = CM.CIRCLE_ID  AND (LOWER(TRIM(BPD.TOPIC)) ";
	protected static final String UPDATE_ARC_DETAILS = "UPDATE KM2.KM_ARC_DETAILS SET CIRCLE_NAME=?, CITY_NAME=?, ARC_STORE=?, PINCODE=?, ADDRESS=?, TIMING=?, AVAILABILITY_OF_CCDC_MACHINE=?, AVAILABILITY_OF_DOCTOR_SIM=?, UPDATED_DT= current timestamp, UPDATED_BY=?, ARCOR=?, ZONE=? WHERE SRNO=?";
	protected static final String UPDATE_DIST_DETAILS = "UPDATE KM2.KM_DIST_DETAILS SET PINCODE=?, STATE_NAME=?, CITY_NAME=?, PIN_CATEGORY=?, AREA=?, HUB=?, CIRCLE=?, SFANDSSDNAME=?, SFANDSSDMAILID=?, SFANDSSDCONTACTNO=?, TSM_NAME=?, TSM_MAIL_ID=?, TSM_CONTACT_NO=?, SR_MANAGER=?, SR_MANAGER_MAIL_ID=?, SR_MANAGER_CONTACT_NO=?, ASM_NAME=?, ASM_MAIL_ID=?, ASM_CONTACT_NO=?, UPDATED_DT= current timestamp, UPDATED_BY=? WHERE SRNO = ?";
	protected static final String UPDATE_COORDS_DETAILS = "UPDATE KM2.KM_COORDINATOR_DETAILS SET CIRCLE_NAME=?, COMPANY_NAME=?, COMPANY_SPOC_NAME=?, COMPANY_SPOC_EMAILID=?, COMPANY_SPOC_CONTACTNO=?, RM_NAME=?, RM_EMAIL_ID=?, RM_CONTACTNO=?, REQUESTOR=?, REQUESTOR_LOCATION=?, REQUESTOR_CONT_NO=?, UPDATED_DT= current timestamp, UPDATED_BY=? WHERE  SRNO=?";
	protected static final String DELETE_ARC_DETAILS = "UPDATE km2.KM_ARC_DETAILS SET STATUS='D' WHERE SRNO = ?";
	protected static final String DELETE_DIST_DETAILS = "UPDATE km2.KM_DIST_DETAILS SET STATUS='D' WHERE SRNO = ?";
	protected static final String DELETE_COORDS_DETAILS = "UPDATE km2.KM_COORDINATOR_DETAILS SET STATUS='D' WHERE SRNO = ?";

	protected static final String GET_VAS_DETAILS = "SELECT VASNAME,ACTIVATIONCODE,DEACTIVATIONCODE,CHARGES,BENEFITS,CONSTRUCT,SRNO  FROM KM2.KM_VAS_DETAIL WHERE LOWER(TRIM(STATUS))='a' AND ";
	protected static final String UPDATE_VAS_DETAILS = "UPDATE KM2.KM_VAS_DETAIL SET VASNAME=?, ACTIVATIONCODE=?, DEACTIVATIONCODE=?, CHARGES=?, BENEFITS=?, CONSTRUCT=? WHERE SRNO = ?";
	protected static final String DELETE_VAS_DETAILS = "UPDATE km2.KM_VAS_DETAIL SET STATUS='D' WHERE SRNO = ?";

	protected static final String GET_USER_DETAILS = "SELECT PARTNER_NAME,LOCATION FROM KM2.KM_USER_MSTR WHERE USER_LOGIN_ID = ? ";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.km.dao.KmSearchDao#search(com.ibm.km.dto.KmSearch)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.km.dao.KmSearchDao#getChange(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	public ArrayList getChange(String Id, String condition, String circleId)
			throws KmException {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrgetChangedValues = new ArrayList();
		try {
			con = DBConnection.getDBConnection();

			if (condition.equalsIgnoreCase("category")) {
				String sql = "SELECT CATEGORY_ID, CATEGORY_NAME FROM KM_CATEGORY_MSTR WHERE CIRCLE_ID = ?  with ur";
				ps = con.prepareStatement(sql);
				ps.setInt(1, Integer.parseInt(Id));
			}
			if (condition.equalsIgnoreCase("subcategory")) {

				String sql = "SELECT SUB_CATEGORY_ID, SUB_CATEGORY_NAME FROM KM_SUB_CATEGORY_MSTR WHERE CATEGORY_ID = ?  with ur ";
				ps = con.prepareStatement(sql);
				ps.setInt(1, Integer.parseInt(Id));
				logger.info(sql + "=" + Id);
			}

			rs = ps.executeQuery();

			while (condition.equals("category") && rs.next()) {
				KmSearchFormBean formbean = new KmSearchFormBean();
				formbean.setCategoryId(rs.getString("CATEGORY_ID"));
				formbean.setCategoryName(rs.getString("CATEGORY_NAME"));
				arrgetChangedValues.add(formbean);
			}
			while (condition.equals("subcategory") && rs.next()) {
				KmSearchFormBean formbean = new KmSearchFormBean();
				formbean.setCategoryId(rs.getString("SUB_CATEGORY_ID"));
				formbean.setCategoryName(rs.getString("SUB_CATEGORY_NAME"));
				arrgetChangedValues.add(formbean);
				// logger.info("Inside SubCategory Filling");
			}
		} catch (SQLException e) {
			logger.info(e);

			logger.error("SQL Exception occured while inserting."
					+ "Exception Message: " + e.getMessage());
			throw new KmException("SQLException: " + e.getMessage(), e);
		} catch (Exception e) {
			logger.info(e);

			logger.error("Exception occured while inserting."
					+ "Exception Message: " + e.getMessage());
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

			}
		}
		logger.info("Length : " + arrgetChangedValues.size());
		return arrgetChangedValues;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.km.dao.KmSearchDao#search(com.ibm.km.dto.KmSearch)
	 */
	public ArrayList search(KmSearch dto) throws KmException {
		Connection con = null;
		Connection con1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		ArrayList fileList = new ArrayList();
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		KmDocumentMstr dto1;
		String lob = ""; // added by Karan

		try {
			StringBuffer query = new StringBuffer(SQL_SELECT_FILES);
			// String sql=SQL_SELECT_FILES;
			// logger.info("ROOT : "+dto.getRoot());
			String parentId = dto.getElementId();
			String root = dto.getRoot();
			String keyword = dto.getKeyword();
			logger.info("\n\nkeyword:" + keyword + "\n\n\n");
			String searchType = dto.getSearchType();
			logger.info("elementId : " + parentId + "    keyword : " + keyword
					+ "  Search Type :" + searchType);

			if (!keyword.equals("") && !keyword.equals("showAllDocuments")) {
				keyword = keyword.toUpperCase();

				if (searchType.equals("ADMIN_KEYWORD")) {
					query
							.append(
									" AND UPPER(doc.DOCUMENT_KEYWORD) LIKE ? and (date(current timestamp) between date(publishing_start_dt) and date(publishing_end_dt))	 ORDER BY doc.NUMBER_OF_HITS DESC")
							.toString();
				} else {
					query
							.append(
									" AND doc.APPROVAL_STATUS = 'A' AND UPPER(doc.DOCUMENT_KEYWORD) LIKE ? and (date(current timestamp) between date(publishing_start_dt) and date(publishing_end_dt))	 ORDER BY doc.NUMBER_OF_HITS DESC ")
							.toString();
				}
			} else if (keyword.equals("")
					&& !keyword.equals("showAllDocuments")) {
				query
						.append(
								" AND  doc.APPROVAL_STATUS = 'A' and (date(current timestamp) between date(publishing_start_dt) and date(publishing_end_dt)) ORDER BY doc.UPLOADED_DT DESC")
						.toString();
			} else {

				query
						.append(
								"  and (date(current timestamp) between date(publishing_start_dt) and date(publishing_end_dt)) ORDER BY doc.UPLOADED_DT DESC  ")
						.toString();
			}
			logger.info("xxxx");
			logger.info(query);
			con = DBConnection.getDBConnection();
			ps = con.prepareStatement(query.append(" with ur").toString());
			ps.setInt(1, Integer.parseInt(parentId));

			if (!keyword.equals("") && !keyword.equals("showAllDocuments")) {
				ps.setString(2, "%" + keyword + "%");
			}

			rs = ps.executeQuery();
			lob = getParent(dto.getElementId());
			while (rs.next()) {
				dto1 = new KmDocumentMstr();
				dto1.setDocumentPath(rs.getString("DOCUMENT_PATH"));
				String path = rs.getString("chain");
				if (!searchType.equals("ADMIN_KEYWORD")) {
					path = lob + "/" + path;
				}
				logger.info("\n\n\n\npath" + path + "\n\n");

				StringBuffer documentStringPath = new StringBuffer("");
				if (dto.getDocumentList() != null) {
					documentStringPath.append("Pan India/");
					if (path != null) {
						if ((path.indexOf("/") + 1) < path.lastIndexOf("/")) {
							documentStringPath = documentStringPath.append(path
									.substring((path.indexOf("/") + 1), path
											.lastIndexOf("/")));
						}
					}
					dto1.setDocumentStringPath(documentStringPath.toString());
				} else if (path != null) {
					if ((path.indexOf("/") + 1) < path.lastIndexOf("/")) {
						path = path.substring(0, path.lastIndexOf("/"));
					}
					dto1.setDocumentStringPath(path);
				}

				dto1.setDocumentId(rs.getString("DOCUMENT_ID"));
				dto1.setElementId(rs.getString("ELEMENT_ID"));
				dto1.setDocumentName(rs.getString("DOCUMENT_NAME"));
				dto1.setDocumentDisplayName(rs
						.getString("DOCUMENT_DISPLAY_NAME"));
				dto1.setDocType(rs.getInt("DOC_TYPE"));

				// DocumentViewer

				switch (dto1.getDocType()) {
				case 0:
					dto1
							.setDocumentViewer("documentAction.do?methodName=displayDocument");
					break;
				case Constants.DOC_TYPE_FILE:
					dto1
							.setDocumentViewer("documentAction.do?methodName=displayDocument");
					break;
				case Constants.DOC_TYPE_PRODUCT:
					dto1
							.setDocumentViewer("productUpload.do?methodName=viewProductDetails");
					break;
				case Constants.DOC_TYPE_SOP:
					dto1
							.setDocumentViewer("sopUpload.do?methodName=viewSopDetails");
					break;
				case Constants.DOC_TYPE_SOP_BD:
					dto1
							.setDocumentViewer("sopBDUpload.do?methodName=viewSopBDDetails");
					break;
				case Constants.DOC_TYPE_RC:
					dto1
							.setDocumentViewer("rcContentUpload.do?methodName=viewRCData");
					break;
				case Constants.DOC_TYPE_BP:
					dto1
							.setDocumentViewer("bpUpload.do?methodName=viewBPDetails");
					break;
				case Constants.DOC_TYPE_DTH:
					dto1
							.setDocumentViewer("offerUpload.do?methodName=viewDTHOffer");
					break;
				default:
					dto1
							.setDocumentViewer("documentAction.do?methodName=displayDocument");
				}

				//logger.info("APPROVAL_STATUS   "+rs.getString("APPROVAL_STATUS"
				// ));
				if (("A").equals(rs.getString("APPROVAL_STATUS"))) {
					dto1.setApprovalStatus("Approved");

				} else if (("S").equals(rs.getString("APPROVAL_STATUS"))) {
					dto1.setApprovalStatus("Submitted");

				} else if (("R").equals(rs.getString("APPROVAL_STATUS"))) {
					dto1.setApprovalStatus("Rejected");

				} else if (("P").equals(rs.getString("APPROVAL_STATUS"))) {
					dto1.setApprovalStatus("Pending");

				} else if (("O").equals(rs.getString("APPROVAL_STATUS"))) {
					dto1.setApprovalStatus("Versioned");

				}

				// Remove seconds and milliseconds from ever page wherever
				// displayed : defect no. MASDB00060756

				if (rs.getString("UPLOADED_DT") != null) {
					dto1.setUploadedDate(rs.getString("UPLOADED_DT").substring(
							0, 16));
				}

				if (rs.getString("APPROVAL_REJECTION_DT") != null) {
					dto1.setApprovalRejectionDate(rs.getString(
							"APPROVAL_REJECTION_DT").substring(0, 16));
				}
				String published_dt = "";
				published_dt = rs.getString("PUBLISHING_START_DT");
				if (published_dt != null) {
					// Bug MASDB00064380 resolved
					if (!(published_dt.equalsIgnoreCase(""))) {
						published_dt = published_dt.substring(0, 10);
					}
					dto1.setPublishDt(published_dt);
					String published_end_dt = "";
					published_end_dt = rs.getString("PUBLISHING_END_DT");
					if (!(published_end_dt.equalsIgnoreCase(""))) {
						published_end_dt = published_end_dt.substring(0, 10);
					}
					dto1.setPublishEndDt(published_end_dt);
				} else {
					dto1.setPublishDt("");
					dto1.setPublishEndDt("");
				}
				if (dto.getActorId().equals(Constants.CIRCLE_CSR)
						|| dto.getActorId().equals(Constants.CATEGORY_CSR)) {

					/* Getting the version count */
					try {
						con1 = DBConnection.getDBConnection();
						ps1 = con1.prepareStatement(SQL_GET_VERSION_COUNT
								+ " with ur ");
						ps1.setInt(1, Integer.parseInt(dto1.getDocumentId()));
						rs1 = ps1.executeQuery();
						int versionCount = 0;
						if (rs1.next()) {
							versionCount = Integer.parseInt(rs1
									.getString("VERSION_COUNT"));
						}
						dto1.setVersionCount(versionCount);
					} catch (Exception e) {
						logger
								.info("Exception occured while getting version count");
					} finally {
						try {
							if (rs1 != null)
								rs1.close();
							if (ps1 != null)
								ps1.close();
							if (con1 != null) {
								con1.close();
							}
						} catch (Exception e) {
							logger
									.info("Exception occured while getting version count");
						}
					}
				}
				fileList.add(dto1);
			}
			return fileList;

		} catch (SQLException e) {
			e.printStackTrace();
			logger.info(e);

			// logger.severe("SQL Exception occured while find." +
			// "Exception Message: " + e.getMessage());
			throw new KmException("SQLException: " + e.getMessage(), e);
		} catch (Exception e) {

			// logger.severe("Exception occured while find." +
			// "Exception Message: " + e.getMessage());
			logger.info("Exception : " + e);

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

			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.km.dao.KmSearchDao#contentSearch(com.ibm.km.dto.KmSearch,
	 * java.lang.String)
	 */
	public ArrayList contentSearch(KmSearch searchDto, String[] documentIds)
			throws KmException {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		ArrayList fileList = searchDto.getDocumentList();
		KmDocumentMstr dto = null;
		String lob = ""; // added by Karan
		String parentId = searchDto.getElementId();
		logger.info("Element Id ############# " + parentId);
		try {
			if (fileList == null) {
				fileList = new ArrayList();
			}
			StringBuffer sql = new StringBuffer(SQL_GET_SEARCHED_DOCUMENTS);

			// TODO ---
			String docIds = "";
			for (int i = 0; i < documentIds.length; i++) {
				if (i == 0)
					docIds += documentIds[i];
				else
					docIds += (" , " + documentIds[i]);
				// sql.append(" OR doc.document_id = ?  ");
			}
			sql.append(docIds);

			// modified for iPortal_Enhancements changes for Maximum view doc
			// display; CSR20111025-00-06938:BFR4
			sql
					.append(") AND doc.STATUS ='A' AND (DATE(CURRENT TIMESTAMP) BETWEEN DATE(PUBLISHING_START_DT) AND DATE(PUBLISHING_END_DT)) ");

			if (searchDto.getSearchType().equals("CSR_CONTENT")) {
				sql.append(" AND doc.APPROVAL_STATUS='A' ");
			}

			if ("on".equals(searchDto.getDateCheck())) {
				sql
						.append(" AND UPLOADED_DT >= timestamp_format(?, 'YYYY-MM-DD HH24:MI:SS') AND UPLOADED_DT <= timestamp_format(?, 'YYYY-MM-DD HH24:MI:SS')  ");
			}
			sql.append(" order by doc.NUMBER_OF_HITS desc ");

			logger.info(sql.append("  fetch first ").append(
					searchDto.getMaxFiles()).append(" rows only with ur "));

			// System.out.println("sql <>  "+sql.toString());
			con = DBConnection.getDBConnection();
			ps = con.prepareStatement(sql.toString());
			ps.setInt(1, Integer.parseInt(searchDto.getElementId()));
			int count = 2;

			/*
			 * for(int i=0;i<documentIds.length;i++){ ps.setInt(i+2,
			 * Integer.parseInt(documentIds[i])); count++; }
			 */
			lob = getParent(searchDto.getElementId());
			if ("on".equals(searchDto.getDateCheck())) {
				ps.setString(count, searchDto.getFromDate() + " 00:00:00");
				ps.setString(++count, searchDto.getToDate() + " 23:59:59");
			}

			rs = ps.executeQuery();
			while (rs.next()) {
				dto = new KmDocumentMstr();
				// logger.info(rs.getString("chain"));
				dto.setDocumentPath(rs.getString("DOCUMENT_PATH"));
				String path = rs.getString("chain");
				if (searchDto.getSearchType().equals("CSR_CONTENT")) {
					path = lob + "/" + path;
				}
				logger.info("\n\n\n\npath ::::: " + path + "\n\n");
				// logger.info(path);
				StringBuffer documentStringPath = new StringBuffer("");
				if (searchDto.getDocumentList() != null) {
					documentStringPath.append("Pan India/");
					if (path != null) {
						if ((path.indexOf("/") + 1) < path.lastIndexOf("/")) {
							documentStringPath = documentStringPath.append(path
									.substring((path.indexOf("/") + 1), path
											.lastIndexOf("/")));
						}
					}
					dto.setDocumentStringPath(documentStringPath.toString());
				} else if (path != null) {
					if ((path.indexOf("/") + 1) < path.lastIndexOf("/")) {
						path = path.substring(0, path.lastIndexOf("/"));
					}
					dto.setDocumentStringPath(path);
				}

				dto.setDocumentId(rs.getString("DOCUMENT_ID"));
				dto.setElementId(rs.getString("ELEMENT_ID"));
				dto.setDocumentName(rs.getString("DOCUMENT_NAME"));
				dto.setDocType(rs.getInt("DOC_TYPE"));

				// DocumentViewer

				switch (dto.getDocType()) {
				case 0:
					dto
							.setDocumentViewer("documentAction.do?methodName=displayDocument");
					break;
				case Constants.DOC_TYPE_FILE:
					dto
							.setDocumentViewer("documentAction.do?methodName=displayDocument");
					break;
				case Constants.DOC_TYPE_PRODUCT:
					dto
							.setDocumentViewer("productUpload.do?methodName=viewProductDetails");
					break;
				case Constants.DOC_TYPE_SOP:
					dto
							.setDocumentViewer("sopUpload.do?methodName=viewSopDetails");
					break;
				case Constants.DOC_TYPE_SOP_BD:
					dto
							.setDocumentViewer("sopBDUpload.do?methodName=viewSopBDDetails");
					break;
				case Constants.DOC_TYPE_RC:
					dto
							.setDocumentViewer("rcContentUpload.do?methodName=viewRCData");
					break;
				case Constants.DOC_TYPE_BP:
					dto
							.setDocumentViewer("bpUpload.do?methodName=viewBPDetails");
					break;
				case Constants.DOC_TYPE_DTH:
					dto
							.setDocumentViewer("offerUpload.do?methodName=viewDTHOffer");
					break;
				default:
					dto
							.setDocumentViewer("documentAction.do?methodName=displayDocument");
				}

				dto.setDocumentDisplayName(rs
						.getString("DOCUMENT_DISPLAY_NAME"));
				//logger.info("APPROVAL_STATUS   "+rs.getString("APPROVAL_STATUS"
				// ));
				if (("A").equals(rs.getString("APPROVAL_STATUS"))) {
					dto.setApprovalStatus("Approved");

				} else if (("S").equals(rs.getString("APPROVAL_STATUS"))) {
					dto.setApprovalStatus("Submitted");

				} else if (("R").equals(rs.getString("APPROVAL_STATUS"))) {
					dto.setApprovalStatus("Rejected");

				} else if (("P").equals(rs.getString("APPROVAL_STATUS"))) {
					dto.setApprovalStatus("Pending");

				} else if (("O").equals(rs.getString("APPROVAL_STATUS"))) {
					dto.setApprovalStatus("Versioned");

				}

				if (rs.getString("UPLOADED_DT") != null) {
					dto.setUploadedDate(rs.getString("UPLOADED_DT").substring(
							0, 16));
				}

				if (rs.getString("APPROVAL_REJECTION_DT") != null) {
					dto.setApprovalRejectionDate(rs.getString(
							"APPROVAL_REJECTION_DT").substring(0, 16));
				}
				String published_dt = "";
				published_dt = rs.getString("PUBLISHING_START_DT");
				if (published_dt != null) {

					if (!(published_dt.equalsIgnoreCase(""))) {
						published_dt = published_dt.substring(0, 16);
					}
					dto.setPublishDt(published_dt);
					String published_end_dt = "";
					published_end_dt = rs.getString("PUBLISHING_END_DT");
					if (!(published_end_dt.equalsIgnoreCase(""))) {
						published_end_dt = published_end_dt.substring(0, 10);
					}
					dto.setPublishEndDt(published_end_dt);
				} else {
					dto.setPublishDt("");
					dto.setPublishEndDt("");
				}

				// dto.setElementName(rs.getString("ELEMENT_NAME"));

				/* SOP Documents Refining Karan 5 Dec 2012 */

				// searchModeChecked = "1" for SOP documents
				if (searchDto.getSearchModeChecked() != null
						&& searchDto.getSearchModeChecked().equals("1")) {
					if (dto.getDocType() == Constants.DOC_TYPE_SOP) // Refining
					// only SOP
					// documents
					// if SOP
					// radio
					// button
					// checked
					// at front
					// end
					{
						fileList.add(dto);
					}
				} else {
					fileList.add(dto);

				}

			}
			return fileList;
		} catch (SQLException e) {
			e.printStackTrace();

			logger.info(e);
			// logger.severe("SQL Exception occured while find." +
			// "Exception Message: " + e.getMessage());
			throw new KmException("SQLException: " + e.getMessage(), e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			// logger.severe("Exception occured while find." +
			// "Exception Message: " + e.getMessage());
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

	/* km phase 2 csr keyword search */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.km.dao.KmSearchDao#csrSearch(com.ibm.km.dto.KmSearch)
	 */
	public ArrayList csrSearch(KmSearch dto) throws KmException {
		Connection con = null;
		Connection con1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		ArrayList fileList = new ArrayList();
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		KmDocumentMstr dto1;

		try {
			StringBuffer query = new StringBuffer(SQL_SELECT_FILES);
			String parentId = dto.getParentId();
			String keyword = dto.getKeyword();
			if (!keyword.equals("")) {
				keyword = keyword.toUpperCase();
				query
						.append(
								" AND doc.APPROVAL_STATUS = 'A' AND UPPER(doc.DOCUMENT_KEYWORD) LIKE ? and (date(current timestamp) between date(publishing_start_dt) and date(publishing_end_dt))	 ORDER BY doc.NUMBER_OF_HITS DESC ")
						.toString();
			} else {
				query
						.append(
								" AND doc.APPROVAL_STATUS = 'A' AND  (date(current timestamp) between date(publishing_start_dt) and date(publishing_end_dt))	 ORDER BY doc.NUMBER_OF_HITS DESC ")
						.toString();
			}
			logger.info(query);
			con = DBConnection.getDBConnection();
			ps = con.prepareStatement(query.append(" with ur").toString());
			ps.setInt(1, Integer.parseInt(parentId));
			if (!keyword.equals("")) {
				ps.setString(2, "%" + keyword + "%");
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				dto1 = new KmDocumentMstr();
				dto1.setDocumentPath(rs.getString("DOCUMENT_PATH"));
				String path = rs.getString("chain");
				// logger.info(path);
				String documentStringPath = "";

				if ((path.indexOf("/") + 1) < path.lastIndexOf("/")) {
					documentStringPath = path.substring(
							(path.indexOf("/") + 1), path.lastIndexOf("/"));
				}

				dto1.setDocumentStringPath(documentStringPath);
				dto1.setDocumentId(rs.getString("DOCUMENT_ID"));
				dto1.setElementId(rs.getString("ELEMENT_ID"));
				dto1.setDocumentName(rs.getString("DOCUMENT_NAME"));
				dto1.setDocumentDisplayName(rs
						.getString("DOCUMENT_DISPLAY_NAME"));
				String publishing_end_dt = "";
				publishing_end_dt = rs.getString("PUBLISHING_END_DT");
				if (!(publishing_end_dt.equalsIgnoreCase(""))) {
					publishing_end_dt = publishing_end_dt.substring(0, 10);
					dto1.setPublishEndDt(publishing_end_dt);
				} else {
					dto1.setPublishEndDt("");
				}

				try {
					con1 = DBConnection.getDBConnection();
					StringBuffer query1 = new StringBuffer(
							SQL_GET_VERSION_COUNT);
					ps1 = con1.prepareStatement(query1.append(" with ur ")
							.toString());
					ps1.setInt(1, Integer.parseInt(dto1.getDocumentId()));
					rs1 = ps1.executeQuery();
					int versionCount = 0;
					if (rs1.next()) {
						versionCount = Integer.parseInt(rs1
								.getString("VERSION_COUNT"));
					}
					dto1.setVersionCount(versionCount);
				} catch (Exception e) {
					logger
							.info("Exception occured while getting version count");
				} finally {
					try {
						if (rs1 != null)
							rs1.close();
						if (ps1 != null)
							ps1.close();
						if (con1 != null) {
							con1.close();
						}
					} catch (Exception e) {
						logger
								.info("Exception occured while getting version count");
					}
				}
				fileList.add(dto1);
			}
			return fileList;

		} catch (SQLException e) {
			logger.error(e);
			// logger.severe("SQL Exception occured while find." +
			// "Exception Message: " + e.getMessage());
			throw new KmException("SQLException: " + e.getMessage(), e);
		} catch (Exception e) {

			// logger.severe("Exception occured while find." +
			// "Exception Message: " + e.getMessage());
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

	public String getParent(String circleID) throws KmException {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String ELEMENT_NAME = "";
		try {
			con = DBConnection.getDBConnection();
			ps = con.prepareStatement(GET_PARENT_LOB);
			ps.setInt(1, Integer.parseInt(circleID));
			rs = ps.executeQuery();
			if (rs.next()) {
				ELEMENT_NAME = rs.getString("ELEMENT_NAME");
			}
			return ELEMENT_NAME;

		} catch (SQLException e) {
			logger.error("Exception in get Parent:" + e);
			throw new KmException("SQLException: " + e.getMessage(), e);
		} catch (Exception e) {
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
	}// getParent

	public ArrayList<KmSearchDetailsDTO> searchDetails(String keyword,
			String mainOptionValue, String selectedSubOptionValue, int actorId,
			String loginId) throws KmException {
		logger.info("search keyword :" + keyword + "  mainOptionValue :"
				+ mainOptionValue + " selectedSubOptionValue :"
				+ selectedSubOptionValue + " actor ID  :" + actorId
				+ "login id :" + loginId);
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		KmSearchDetailsDTO searchDetailsDTO = null;
		ArrayList<KmSearchDetailsDTO> searchDetailsList = new ArrayList<KmSearchDetailsDTO>();
		String query = "";
		keyword = keyword.toLowerCase();
		try {
			if (mainOptionValue.equalsIgnoreCase("arc")) {
				query = GET_ARC_DETAILS;
				if (selectedSubOptionValue.equalsIgnoreCase("pincode")) {
					query = query + " LOWER(TRIM(PINCODE)) LIKE '%"
							+ keyword.toLowerCase() + "%' WITH UR";
				} else if (selectedSubOptionValue.equalsIgnoreCase("area")) {
					query = query + " (LOWER(TRIM(ARC_STORE)) LIKE '%"
							+ keyword.toLowerCase()
							+ "%' OR LOWER(TRIM(ADDRESS)) LIKE '%"
							+ keyword.toLowerCase() + "%') WITH UR";
				}
			} else if (mainOptionValue.equalsIgnoreCase("dist")) {
				query = GET_DIST_DETAILS;
				if (selectedSubOptionValue.equalsIgnoreCase("pincode")) {
					query = query + " LOWER(TRIM(PINCODE)) LIKE '%"
							+ keyword.toLowerCase() + "%' WITH UR";
				} else if (selectedSubOptionValue.equalsIgnoreCase("area")) {
					query = query + " LOWER(TRIM(AREA)) LIKE '%"
							+ keyword.toLowerCase() + "%' WITH UR";
				}
			} else if (mainOptionValue.equalsIgnoreCase("coords")) {
				query = GET_COORDS_DETAILS;
				if (selectedSubOptionValue.equalsIgnoreCase("spocname")) {
					query = query + " LOWER(TRIM(COMPANY_SPOC_NAME)) LIKE '%"
							+ keyword.toLowerCase() + "%' WITH UR";
				}
			} else if (mainOptionValue.equalsIgnoreCase("pack")) {
				if (actorId == 4)
					query = GET_PACK_DETAILS_FOR_CSR + " like '%"
							+ keyword.toLowerCase()
							+ "%' or LOWER(TRIM(HEADER)) like '%"
							+ keyword.toLowerCase()
							+ "%' or LOWER(TRIM(CONTENT)) like '%"
							+ keyword.toLowerCase() + "%') WITH UR";
				else if (actorId == 1)
					query = GET_PACK_DETAILS + " like '%"
							+ keyword.toLowerCase()
							+ "%' or LOWER(TRIM(HEADER)) like '%"
							+ keyword.toLowerCase()
							+ "%' or LOWER(TRIM(CONTENT)) like '%"
							+ keyword.toLowerCase() + "%') WITH UR";
				// GET_PACK_DETAILS_FOR_CSR
			} else if (mainOptionValue.equalsIgnoreCase("vas")) {
				query = GET_VAS_DETAILS;
				logger.debug("GET_VAS_DETAILS :: " + GET_VAS_DETAILS);
				if (selectedSubOptionValue.equalsIgnoreCase("acode")) {
					logger.debug("supoption is acode");
					query = query + " LOWER(TRIM(ACTIVATIONCODE)) LIKE '%"
							+ keyword.toLowerCase() + "%' WITH UR";
				} else if (selectedSubOptionValue.equalsIgnoreCase("dcode")) {
					query = query + " LOWER(TRIM(DEACTIVATIONCODE)) LIKE '%"
							+ keyword.toLowerCase() + "%' WITH UR";
				}
			}
			logger.info("Final Query :" + query);
			con = DBConnection.getDBConnection();
			ps = con.prepareStatement(query);
			if (mainOptionValue.equalsIgnoreCase("pack")) {
				if (actorId == 4) {
					ps.setString(1, loginId);
				}
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				searchDetailsDTO = new KmSearchDetailsDTO();
				if (mainOptionValue.equalsIgnoreCase("arc")) {
					searchDetailsDTO.setCircle(rs.getString("CIRCLE_NAME"));
					searchDetailsDTO.setZone(rs.getString("ZONE"));
					searchDetailsDTO.setArcOr(rs.getString("ARCOR"));
					searchDetailsDTO.setCity(rs.getString("CITY_NAME"));
					searchDetailsDTO.setPincode(rs.getString("PINCODE"));
					searchDetailsDTO.setArc(rs.getString("ARC_STORE"));
					searchDetailsDTO.setAddress(rs.getString("ADDRESS"));
					searchDetailsDTO.setShowRoomTiming(rs.getString("TIMING"));
					searchDetailsDTO.setAvailabilityOfCcDcMachine(rs
							.getString("AVAILABILITY_OF_CCDC_MACHINE"));
					searchDetailsDTO.setAvailabilityOfDoctorSim(rs
							.getString("AVAILABILITY_OF_DOCTOR_SIM"));
					searchDetailsDTO.setSrNo(rs.getInt("SRNO"));
				} else if (mainOptionValue.equalsIgnoreCase("dist")) {
					searchDetailsDTO.setPincode(rs.getString("PINCODE"));
					searchDetailsDTO.setStateName(rs.getString("STATE_NAME"));
					searchDetailsDTO.setCity(rs.getString("CITY_NAME"));
					searchDetailsDTO.setPinCatagory(rs
							.getString("PIN_CATEGORY"));
					searchDetailsDTO.setArea(rs.getString("AREA"));
					searchDetailsDTO.setHub(rs.getString("HUB"));
					searchDetailsDTO.setCircle(rs.getString("CIRCLE"));
					searchDetailsDTO.setSfAndSSDName(rs
							.getString("SFANDSSDNAME"));
					searchDetailsDTO.setSfAndSSDContNo(rs
							.getString("SFANDSSDCONTACTNO"));
					searchDetailsDTO.setSfAndSSDEmail(rs
							.getString("SFANDSSDMAILID"));
					searchDetailsDTO.setTsmName(rs.getString("TSM_NAME"));
					searchDetailsDTO.setTsMContNo(rs
							.getString("TSM_CONTACT_NO"));
					searchDetailsDTO.setTsmMailId(rs.getString("TSM_MAIL_ID"));
					searchDetailsDTO.setSrMngr(rs.getString("SR_MANAGER"));
					searchDetailsDTO.setSrMngrContNo(rs
							.getString("SR_MANAGER_CONTACT_NO"));
					searchDetailsDTO.setSrMngrMailId(rs
							.getString("SR_MANAGER_MAIL_ID"));
					searchDetailsDTO.setAsmName(rs.getString("ASM_NAME"));
					searchDetailsDTO.setAsmContNo(rs
							.getString("ASM_CONTACT_NO"));
					searchDetailsDTO.setAsmMailId(rs.getString("ASM_MAIL_ID"));
					searchDetailsDTO.setSrNo(rs.getInt("SRNO"));
				} else if (mainOptionValue.equalsIgnoreCase("coords")) {
					searchDetailsDTO.setCircle(rs.getString("CIRCLE_NAME"));
					searchDetailsDTO.setCompName(rs.getString("COMPANY_NAME"));
					searchDetailsDTO.setCompSpocName(rs
							.getString("COMPANY_SPOC_NAME"));
					searchDetailsDTO.setCompSpocMail(rs
							.getString("COMPANY_SPOC_EMAILID"));
					searchDetailsDTO.setCompSpocCont(rs
							.getString("COMPANY_SPOC_CONTACTNO"));
					searchDetailsDTO.setRmMgr(rs.getString("RM_NAME"));
					searchDetailsDTO.setRmMailId(rs.getString("RM_EMAIL_ID"));
					searchDetailsDTO.setRmCont(rs.getString("RM_CONTACTNO"));
					searchDetailsDTO.setRequestor(rs.getString("REQUESTOR"));
					searchDetailsDTO.setRequestorLocation(rs
							.getString("REQUESTOR_LOCATION"));
					searchDetailsDTO.setRequestorCont(rs
							.getString("REQUESTOR_CONT_NO"));
					searchDetailsDTO.setRequestedOn(rs
							.getString("REQUESTED_DATE"));
					searchDetailsDTO.setSrNo(rs.getInt("SRNO"));
				} else if (mainOptionValue.equalsIgnoreCase("pack")) {
					searchDetailsDTO.setTopic(rs.getString("TOPIC"));
					searchDetailsDTO.setHeader(rs.getString("HEADER"));
					searchDetailsDTO.setContents(rs.getString("CONTENT"));
					searchDetailsDTO.setFromDt(rs.getString("FROM_DT"));
					searchDetailsDTO.setToDt(rs.getString("TO_DT"));
					searchDetailsDTO.setCircle(rs.getString("CIRCLE_NAME"));
				} else if (mainOptionValue.equalsIgnoreCase("vas")) {
					searchDetailsDTO.setVasName(rs.getString("VASNAME"));
					searchDetailsDTO.setActivationCode(rs
							.getString("ACTIVATIONCODE"));
					searchDetailsDTO.setDeactivationCode(rs
							.getString("DEACTIVATIONCODE"));
					searchDetailsDTO.setCharges(rs.getInt("CHARGES"));
					searchDetailsDTO.setBenefits(rs.getString("BENEFITS"));
					searchDetailsDTO.setConstruct(rs.getString("CONSTRUCT"));
					searchDetailsDTO.setSrNo(rs.getInt("SRNO"));
				}
				searchDetailsList.add(searchDetailsDTO);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new KmException("Error Occoured while getting Data !!!");
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
				throw new KmException("Error Occoured while getting Data !!!");
			}
		}

		return searchDetailsList;
	}

	public ArrayList<KmSearchDetailsDTO> editDetails(String keyword,
			String mainOption, String subOption, int serialNo)
			throws KmException {
		logger.info("search keyword :" + keyword + "  mainOption :"
				+ mainOption + " subOption :" + subOption + " Serial No :"
				+ serialNo);
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		KmSearchDetailsDTO searchDetailsDTO = null;
		ArrayList<KmSearchDetailsDTO> searchDetailsList = new ArrayList<KmSearchDetailsDTO>();
		String query = "";
		try {
			if (mainOption.equalsIgnoreCase("arc")) {
				query = GET_ARC_DETAILS + " SRNO = " + serialNo;
			} else if (mainOption.equalsIgnoreCase("dist")) {
				query = GET_DIST_DETAILS + " SRNO = " + serialNo;
			} else if (mainOption.equalsIgnoreCase("coords")) {
				query = GET_COORDS_DETAILS + " SRNO = " + serialNo;
			} else if (mainOption.equalsIgnoreCase("vas")) {
				query = GET_VAS_DETAILS + " SRNO = " + serialNo;
			}

			logger.info("Final Query :" + query);
			con = DBConnection.getDBConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				searchDetailsDTO = new KmSearchDetailsDTO();
				if (mainOption.equalsIgnoreCase("arc")) {
					searchDetailsDTO.setCircle(rs.getString("CIRCLE_NAME"));
					searchDetailsDTO.setZone(rs.getString("ZONE"));
					searchDetailsDTO.setArcOr(rs.getString("ARCOR"));
					searchDetailsDTO.setCity(rs.getString("CITY_NAME"));
					searchDetailsDTO.setPincode(rs.getString("PINCODE"));
					searchDetailsDTO.setArc(rs.getString("ARC_STORE"));
					searchDetailsDTO.setAddress(rs.getString("ADDRESS"));
					searchDetailsDTO.setShowRoomTiming(rs.getString("TIMING"));
					searchDetailsDTO.setAvailabilityOfCcDcMachine(rs
							.getString("AVAILABILITY_OF_CCDC_MACHINE"));
					searchDetailsDTO.setAvailabilityOfDoctorSim(rs
							.getString("AVAILABILITY_OF_DOCTOR_SIM"));
					searchDetailsDTO.setSrNo(rs.getInt("SRNO"));
				} else if (mainOption.equalsIgnoreCase("dist")) {
					searchDetailsDTO.setPincode(rs.getString("PINCODE"));
					searchDetailsDTO.setStateName(rs.getString("STATE_NAME"));
					searchDetailsDTO.setCity(rs.getString("CITY_NAME"));
					searchDetailsDTO.setPinCatagory(rs
							.getString("PIN_CATEGORY"));
					searchDetailsDTO.setArea(rs.getString("AREA"));
					searchDetailsDTO.setHub(rs.getString("HUB"));
					searchDetailsDTO.setCircle(rs.getString("CIRCLE"));
					searchDetailsDTO.setSfAndSSDName(rs
							.getString("SFANDSSDNAME"));
					searchDetailsDTO.setSfAndSSDContNo(rs
							.getString("SFANDSSDCONTACTNO"));
					searchDetailsDTO.setSfAndSSDEmail(rs
							.getString("SFANDSSDMAILID"));
					searchDetailsDTO.setTsmName(rs.getString("TSM_NAME"));
					searchDetailsDTO.setTsMContNo(rs
							.getString("TSM_CONTACT_NO"));
					searchDetailsDTO.setTsmMailId(rs.getString("TSM_MAIL_ID"));
					searchDetailsDTO.setSrMngr(rs.getString("SR_MANAGER"));
					searchDetailsDTO.setSrMngrContNo(rs
							.getString("SR_MANAGER_CONTACT_NO"));
					searchDetailsDTO.setSrMngrMailId(rs
							.getString("SR_MANAGER_MAIL_ID"));
					searchDetailsDTO.setAsmName(rs.getString("ASM_NAME"));
					searchDetailsDTO.setAsmContNo(rs
							.getString("ASM_CONTACT_NO"));
					searchDetailsDTO.setAsmMailId(rs.getString("ASM_MAIL_ID"));
					searchDetailsDTO.setSrNo(rs.getInt("SRNO"));
				} else if (mainOption.equalsIgnoreCase("coords")) {
					searchDetailsDTO.setCircle(rs.getString("CIRCLE_NAME"));
					searchDetailsDTO.setCompName(rs.getString("COMPANY_NAME"));
					searchDetailsDTO.setCompSpocName(rs
							.getString("COMPANY_SPOC_NAME"));
					searchDetailsDTO.setCompSpocMail(rs
							.getString("COMPANY_SPOC_EMAILID"));
					searchDetailsDTO.setCompSpocCont(rs
							.getString("COMPANY_SPOC_CONTACTNO"));
					searchDetailsDTO.setRmMgr(rs.getString("RM_NAME"));
					searchDetailsDTO.setRmMailId(rs.getString("RM_EMAIL_ID"));
					searchDetailsDTO.setRmCont(rs.getString("RM_CONTACTNO"));
					searchDetailsDTO.setRequestor(rs.getString("REQUESTOR"));
					searchDetailsDTO.setRequestorLocation(rs
							.getString("REQUESTOR_LOCATION"));
					searchDetailsDTO.setRequestorCont(rs
							.getString("REQUESTOR_CONT_NO"));
					searchDetailsDTO.setRequestedOn(rs
							.getString("REQUESTED_DATE"));
					searchDetailsDTO.setSrNo(rs.getInt("SRNO"));

				} else if (mainOption.equalsIgnoreCase("vas")) {
					searchDetailsDTO.setVasName(rs.getString("VASNAME"));
					searchDetailsDTO.setActivationCode(rs
							.getString("ACTIVATIONCODE"));
					searchDetailsDTO.setDeactivationCode(rs
							.getString("DEACTIVATIONCODE"));
					searchDetailsDTO.setCharges(rs.getInt("CHARGES"));
					searchDetailsDTO.setBenefits(rs.getString("BENEFITS"));
					searchDetailsDTO.setConstruct(rs.getString("CONSTRUCT"));
					searchDetailsDTO.setSrNo(rs.getInt("SRNO"));
				}
				searchDetailsList.add(searchDetailsDTO);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new KmException("Error Occoured while getting Data !!!");
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
				throw new KmException("Error Occoured while getting Data !!!");
			}
		}
		return searchDetailsList;
	}

	public String updateDetails(String keyword, String mainOption,
			KmDocumentMstrFormBean formBean, int serialNo) throws KmException {
		logger.info("search keyword :" + keyword + "  mainOption :"
				+ mainOption + " Serial No :" + serialNo);
		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<KmSearchDetailsDTO> searchDetailsList = new ArrayList<KmSearchDetailsDTO>();
		String result;
		String query = "";
		try {
			if (mainOption.equalsIgnoreCase("arc")) {
				query = UPDATE_ARC_DETAILS;
			} else if (mainOption.equalsIgnoreCase("dist")) {
				query = UPDATE_DIST_DETAILS;
			} else if (mainOption.equalsIgnoreCase("coords")) {
				query = UPDATE_COORDS_DETAILS;
			} else if (mainOption.equalsIgnoreCase("vas")) {
				query = UPDATE_VAS_DETAILS;
			}
			logger.info("Final Query :" + query);
			con = DBConnection.getDBConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement(query);
			if (mainOption.equalsIgnoreCase("arc")) {
				ps.setString(1, formBean.getCircle());
				ps.setString(2, formBean.getCity());
				ps.setString(3, formBean.getArc());
				ps.setString(4, formBean.getPinCode());
				ps.setString(5, formBean.getAddress());
				ps.setString(6, formBean.getShowRoomTiming());
				ps.setString(7, formBean.getAvailabilityOfCcDcMachine());
				ps.setString(8, formBean.getAvailabilityOfDoctorSim());
				ps.setString(9, formBean.getUserLoginId());
				ps.setString(10, formBean.getArcOr());
				ps.setString(11, formBean.getZone());
				ps.setInt(12, serialNo);
			} else if (mainOption.equalsIgnoreCase("dist")) {
				ps.setString(1, formBean.getPinCode());
				ps.setString(2, formBean.getStateName());
				ps.setString(3, formBean.getCity());
				ps.setString(4, formBean.getPinCatagory());
				ps.setString(5, formBean.getArea());
				ps.setString(6, formBean.getHub());
				ps.setString(7, formBean.getCircle());
				ps.setString(8, formBean.getSfAndSSDName());
				ps.setString(9, formBean.getSfAndSSDEmail());
				ps.setString(10, formBean.getSfAndSSDContNo());
				ps.setString(11, formBean.getTsmName());
				ps.setString(12, formBean.getTsmMailId());
				ps.setString(13, formBean.getTsMContNo());
				ps.setString(14, formBean.getSrMngr());
				ps.setString(15, formBean.getSrMngrMailId());
				ps.setString(16, formBean.getSrMngrContNo());
				ps.setString(17, formBean.getAsmName());
				ps.setString(18, formBean.getAsmMailId());
				ps.setString(19, formBean.getAsmContNo());
				ps.setString(20, formBean.getUserLoginId());
				ps.setInt(21, serialNo);
			} else if (mainOption.equalsIgnoreCase("coords")) {
				ps.setString(1, formBean.getCircle());
				ps.setString(2, formBean.getCompName());
				ps.setString(3, formBean.getCompSpocName());
				ps.setString(4, formBean.getCompSpocMail());
				ps.setString(5, formBean.getCompSpocCont());
				ps.setString(6, formBean.getRmMgr());
				ps.setString(7, formBean.getRmMailId());
				ps.setString(8, formBean.getRmCont());
				ps.setString(9, formBean.getRequestor());
				ps.setString(10, formBean.getRequestorLocation());
				ps.setString(11, formBean.getRequestorCont());
				ps.setString(12, formBean.getUserLoginId());
				ps.setInt(13, serialNo);
			} else if (mainOption.equalsIgnoreCase("vas")) {
				ps.setString(1, formBean.getVasName());
				ps.setString(2, formBean.getActivationCode());
				ps.setString(3, formBean.getDeactivationCode());
				ps.setInt(4, formBean.getCharges());
				ps.setString(5, formBean.getBenefits());
				ps.setString(6, formBean.getConstruct());
				ps.setInt(7, serialNo);
			}

			int updateRecords = ps.executeUpdate();

			logger.info("updatedRecords :" + updateRecords);
			if (updateRecords > 0) {
				result = "success";
				con.commit();
			} else {
				result = "fail";
				con.rollback();
			}
		} catch (Exception ex) {
			result = "fail";
			try {
				con.rollback();
			} catch (Exception e) {
				ex.printStackTrace();
				throw new KmException("Error Occoured while updating Data !!!");
			}
			ex.printStackTrace();
			throw new KmException("Error Occoured while updating Data !!!");
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				logger.error(e);
				throw new KmException("Error Occoured while updating Data !!!");
			}
		}
		return result;
	}

	public String deleteDetails(String mainOption, int serialNo)
			throws KmException {
		logger.info("delete  mainOption :" + mainOption + " Serial No :"
				+ serialNo);
		Connection con = null;
		PreparedStatement ps = null;
		String result;
		String query = "";
		try {
			if (mainOption.equalsIgnoreCase("arc")) {
				query = DELETE_ARC_DETAILS;
			} else if (mainOption.equalsIgnoreCase("dist")) {
				query = DELETE_DIST_DETAILS;
			} else if (mainOption.equalsIgnoreCase("coords")) {
				query = DELETE_COORDS_DETAILS;
			} else if (mainOption.equalsIgnoreCase("vas")) {
				query = DELETE_VAS_DETAILS;
			}
			logger.info("Final Query :" + query);
			con = DBConnection.getDBConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement(query);
			ps.setInt(1, serialNo);
			int updateRecords = ps.executeUpdate();
			logger.info("deletedRecords :" + updateRecords);
			if (updateRecords > 0) {
				result = "deletionSuccess";
				con.commit();
			} else {
				result = "deletionFail";
				con.rollback();
			}
		} catch (Exception ex) {
			result = "deletionFail";
			try {
				con.rollback();
			} catch (Exception e) {
				ex.printStackTrace();
				throw new KmException("Error Occoured while deleting Data !!!");
			}
			ex.printStackTrace();
			throw new KmException("Error Occoured while deleting Data !!!");
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				logger.error(e);
				throw new KmException("Error Occoured while deleting Data !!!");
			}
		}
		return result;
	}

	public ArrayList<KmSearchDetailsDTO> getConfigurableColumnList(
			String tableName) throws KmException {
		logger.info("Configurable columns from table :" + tableName);
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		KmSearchDetailsDTO dto = null;
		ArrayList<KmSearchDetailsDTO> getConfigurableColumnList = new ArrayList<KmSearchDetailsDTO>();
		String query = "";
		try {
			query = "select COLUMNNAME from KM_SMS_CONFIGURABLE_COLUMNS where ISCONFIGURABLE ='Y' and TABLENAME = '"
					+ tableName + "'";
			logger.info("Final Query :" + query);
			con = DBConnection.getDBConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				dto = new KmSearchDetailsDTO();
				// dto.setColumnId(rs.getString("COLUMNID"));
				dto.setColumnName(rs.getString("COLUMNNAME"));
				getConfigurableColumnList.add(dto);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new KmException("Error Occoured while getting Data !!!");
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
				throw new KmException("Error Occoured while getting Data !!!");
			}
		}
		/*
		 * for (int x = 0; x < getConfigurableColumnList.size(); x++) {
		 * logger.debug("$$$$$$$$$$   " +
		 * getConfigurableColumnList.get(x).getColumnName()); }
		 */
		return getConfigurableColumnList;
	}

	public Map<String, Object> sendSms(String mainOption, int serialNo)
			throws KmException {

		logger.info("Send SMS to  mainOption :" + mainOption + " Serial No :"
				+ serialNo);
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String result = "";
		String query = "";
		String tableName = "";
		KmSearchDetailsDTO dto;
		KmDocumentMstrFormBean formBean = new KmDocumentMstrFormBean();
		ArrayList<KmSearchDetailsDTO> lst = new ArrayList<KmSearchDetailsDTO>();
		Map<String, Object> columns = new LinkedHashMap<String, Object>();
		try {
			if (mainOption.equalsIgnoreCase("dist"))
				tableName = "KM_DIST_DETAILS";
			else if (mainOption.equalsIgnoreCase("arc"))
				tableName = "KM_ARC_DETAILS";
			else if (mainOption.equalsIgnoreCase("coords"))
				tableName = "KM_COORDINATOR_DETAILS";
			else if (mainOption.equalsIgnoreCase("vas"))
				tableName = "KM_VAS_DETAIL";

			StringBuffer sb = new StringBuffer();
			StringBuffer sb1 = new StringBuffer();

			ArrayList<KmSearchDetailsDTO> configurableColumnList = null;
			configurableColumnList = getConfigurableColumnList(tableName);
			if (configurableColumnList != null) {
				for (int x = 0; x < configurableColumnList.size(); x++) {
					logger.debug("Column Name = "
							+ configurableColumnList.get(x).getColumnName());
					sb.append(configurableColumnList.get(x).getColumnName()
							+ ",");
				}
				logger.debug("sb value " + sb);
				if (sb.length() > 0) {
					sb1.append(sb.substring(0, sb.length() - 1));
					sb.delete(0, sb.length());
				}
				logger.debug("########  " + sb1);
				String baseQuery = "select " + sb1 + " from " + tableName
						+ " where SRNO = " + serialNo;
				logger.info(baseQuery);
				con = DBConnection.getDBConnection();
				con.setAutoCommit(false);

				ps = con.prepareStatement(baseQuery);
				rs = ps.executeQuery();
				int i = 1;

				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();

				while (rs.next()) {

					for (int x = 1; x <= columnCount; x++) {
						dto = new KmSearchDetailsDTO();

						logger.info("ColumnName ::: "
								+ metaData.getColumnLabel(x) + " Data ::"
								+ rs.getObject(x));
						columns
								.put(metaData.getColumnLabel(x), rs
										.getObject(x));
						// lst.add((KmSearch) columns);
					}
				}

			} else {
				formBean.setSmsStatus(Constants.CONFIGUEABLE_DATA_NOT_FOUND);
			}
		} catch (Exception ex) {

			ex.printStackTrace();
			formBean.setSmsStatus(Constants.CONFIGUEABLE_DATA_NOT_FOUND);
			throw new KmException("No Configurable Columns Found !!!");

		} finally {
			try {
				if (ps != null)
					ps.close();
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				logger.error(e);
				throw new KmException(
						"Error Occoured while Retrieving Data !!!");
			}
		}

		return columns;
	}

	public int insertSMSDetails(String olmId,String userLoginId, String mobileNo,
			String smsTemplate, String mainOption, String circleId,
			String partner, String location,String status,String udId) throws KmException {

		logger.info("Inserting SMS details");
		Connection con;
		PreparedStatement ps;
		ResultSet rs;

		con = null;
		ps = null;
		rs = null;
		int result = 0;
		String smsContentCategory = "";
		
		if (mainOption.equalsIgnoreCase("dist"))
			smsContentCategory = "DISTRIBUTOR";
		else if (mainOption.equalsIgnoreCase("arc"))
			smsContentCategory = "ARC";
		else if (mainOption.equalsIgnoreCase("coords"))
			smsContentCategory = "COORDINATOR";
		else if (mainOption.equalsIgnoreCase("vas"))
			smsContentCategory = "OTHERS";
		
		logger.info("Sender :: "+userLoginId+" ## Mobile Number :"+mobileNo+" ## smsTemplate : "+smsTemplate
				+" ## smsContentCategory "+smsContentCategory+" ## Circle ID = "+circleId+" ## Partner : "+partner+
				" ## Location :: "+location+" ## Status : "+status+" ## OLMId = "+olmId);
		try {
			StringBuffer query = new StringBuffer(
					"INSERT INTO KM_SMS_REPORT_DATA(SMSSENDER,MOBILENO,SMSCONTENT,SMSCATEGORY,SMSCREATEDDATE,CIRCLEID,PATNERNAME,LOCATION,SMSSTATUS,UDID,OLM_ID) VALUES(?,?,?,?,CURRENT TIMESTAMP, ?, ?, ?," +
					"?,?,?)");
			
			logger.info("SMS Query in InsertSMS :: "+query);
			 con = DBConnection.getDBConnection();
			 ps = con.prepareStatement(query.append(" with ur").toString());   
			 
			 ps.setString(1, userLoginId);
			 ps.setString(2, mobileNo);
			 ps.setString(3, smsTemplate);
			 ps.setString(4, smsContentCategory);
			 ps.setInt(5, Integer.parseInt(circleId));
			 ps.setString(6, partner);
			 ps.setString(7, location);
			 ps.setString(8, status);
			 ps.setString(9, udId);
			 ps.setString(10, olmId);
			 result = ps.executeUpdate();
			 con.commit();
			logger.info("SMS Data inserted in database");
			 
			 

		} catch (Exception e) {
			logger.error((new StringBuilder(
					"SQL Exception occured while inserting sms data in database Exception Message: "))
					.append(e.getMessage()).toString());
			e.printStackTrace();
			
		} finally {
			try {
				DBConnection.releaseResources(con, ps, rs);
			} catch (Exception e) {
				logger
						.error((new StringBuilder(
								"DAO Exception occured while Update.Exception Message: "))
								.append(e.getMessage()).toString());
			}
		}
		return result;
	}

	@SuppressWarnings("finally")
	@Override
	public KmSearchDetailsDTO getUserDetailForSMS(String userLoginId)
			throws KmException {
		// TODO Auto-generated method stub
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		KmSearchDetailsDTO searchDetailsDTO = null;
		StringBuffer query = new StringBuffer(
				"SELECT OLM_ID,PARTNER_NAME,LOCATION FROM KM2.KM_USER_MSTR WHERE USER_LOGIN_ID ='");
		query.append(userLoginId + "'");
		String partnerName ="";
		String location = "";
		String olmId = "";
		
		try {
			con = DBConnection.getDBConnection();

			ps = con.prepareStatement(query.toString());
			rs = ps.executeQuery();

			while (rs.next()) {
				searchDetailsDTO = new KmSearchDetailsDTO();
				partnerName = rs.getString("PARTNER_NAME");
				location = rs.getString("LOCATION");
				olmId = rs.getString("OLM_ID");
				logger.info("partnerName  @@ "+ partnerName+" !!!! location  #### "+ location +" ^^^^ "+olmId);
				if(partnerName == null || partnerName.equals("")){
					logger.info(" partnerName is  null");
					searchDetailsDTO.setPartner("Not Available");
				}else{
					logger.info(" partnerName is not null");
					searchDetailsDTO.setPartner(partnerName);
				}
				if(location ==  null || location.equals("")){
					logger.info("location is  null");
					searchDetailsDTO.setLocation("Not Available");
				}else{
					logger.info("location is not null");
					searchDetailsDTO.setLocation(location);
				}
				if(olmId ==  null || olmId.equals("") ){
					logger.info("olmid is  null");
					searchDetailsDTO.setOlmId("Not Available");
				}else{
					logger.info("olmid is not null");
					searchDetailsDTO.setOlmId(olmId);
				}
				logger.info("partnerName  !!!!  "+ searchDetailsDTO.getPartner() +" %% location   *****  "+ searchDetailsDTO.getLocation()+" ##### OLM_Id = "+searchDetailsDTO.getOlmId());	
				
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				DBConnection.releaseResources(con, ps, rs);
			} catch (DAOException e) {
				e.printStackTrace();
				throw new KmException(e.getMessage(), e);
			}

			return searchDetailsDTO;
		}
	}
}