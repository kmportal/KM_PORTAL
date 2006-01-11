/*
 * Created on Apr 15, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.ibm.km.common.Utility;
import com.ibm.km.dao.DBConnection;
import com.ibm.km.dao.KmScrollerMstrDao;
import com.ibm.km.dto.KmAlertMstr;
import com.ibm.km.dto.KmElementMstr;
import com.ibm.km.dto.KmScrollerMstr;
import com.ibm.km.exception.KmException;

/**
 * @author Anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class KmScrollerMstrDaoImpl implements KmScrollerMstrDao {
	/*
	 * Logger for the class.
	 */
	private static final Logger logger;

	static {
		logger = Logger.getLogger(KmScrollerMstrDaoImpl.class);
		//timestamp_format(?+ 00:00:00, 'YYYY-MM-DD HH24:MI:SS')
	}

	protected static final String SQL_INSERT ="INSERT INTO KM_SCROLL_MSTR (MESSAGE, MESSAGE_ID, CIRCLE_ID,CREATED_DT, CREATED_BY, UPDATED_DT, UPDATED_BY, STATUS, START_DT, END_DT) VALUES (?, NEXTVAL FOR KM_SCROLL_ID_SEQ, ?,  current timestamp, ?, current timestamp,?, ?, timestamp_format(?, 'YYYY-MM-DD HH24:MI:SS') ,timestamp_format(?, 'YYYY-MM-DD HH24:MI:SS') )";

	protected static final String SQL_SCROLL_MESSEGE ="SELECT MESSAGE FROM KM_SCROLL_MSTR WHERE TIMESTAMP(CURRENT TIMESTAMP) BETWEEN TIMESTAMP(START_DT)  AND TIMESTAMP(END_DT) and CIRCLE_ID in ( ? ) ";
	
	protected static final String SQL_BULK_SCROLL_MESSEGE ="SELECT distinct(MESSAGE) FROM KM_SCROLL_MSTR WHERE DATE(CURRENT TIMESTAMP) BETWEEN DATE(START_DT)  AND DATE(END_DT) and CIRCLE_ID in (  ";

	protected static final String SQL_VIEW = "SELECT * FROM KM_SCROLL_MSTR WHERE CIRCLE_ID=? and TIMESTAMP(START_DT) <= (?) and TIMESTAMP(END_DT) >= (?) WITH UR ";

	protected static final String SQL_DELETE ="delete from KM_SCROLL_MSTR where MESSAGE_ID=?";
	
	protected static final String SQL_UPDATE ="update KM_SCROLL_MSTR set (MESSAGE,UPDATED_DT,UPDATED_BY,START_DT,END_DT)=(?,current timestamp,?,?,?) where MESSAGE_ID=? ";
	
	protected static final String SQL_SCROLLER_UPDATE="UPDATE KM_SCROLL_MSTR SET MESSAGE = ? WHERE MESSAGE_ID = ? ";
	/* (non-Javadoc)
	 * @see com.ibm.km.dao.KmScrollerMstrDao#insert(com.ibm.km.dto.KmScrollerMstr)
	*	inser a record for a scroller in the table SCROLLER_MSTR. Insert values like message, start date, end date etc
	*  */

	public int insert(KmScrollerMstr dto) throws KmException {
		logger.info("Entered insert for table KM_SCROLLER_MSTR");
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";
		int rowsUpdated = 0;
		try {
			con = DBConnection.getDBConnection();
//			logger.info("mesage id" + dto.getMessageId());
			if (dto.getMessageId() == null
				|| dto.getMessageId().equalsIgnoreCase("")) {
			StringBuffer query=new StringBuffer(SQL_INSERT);
				//sql = SQL_INSERT;
				ps = con.prepareStatement(query.append(" with ur").toString());
				/*Preparing the statement for insertion */
				ps.setString(1, dto.getMessage());
				ps.setString(2, dto.getElementId());
				
/*				if(dto.getCircleId().equals("-2") && !dto.getLobId().equals("-1") )
				{
	              ps.setInt(2, Integer.parseInt(dto.getLobId()));
				}  // Lob Scroller
				
				if(!dto.getCircleId().equals("-2") && !dto.getLobId().equals("-1") )
				{
	              ps.setInt(2, Integer.parseInt(dto.getCircleId()));
 				} // Circle Scroller
*/	              
				ps.setInt(3, Integer.parseInt(dto.getCreatedBy()));
				ps.setInt(4, Integer.parseInt(dto.getUpdatedBy()));
				ps.setString(5, dto.getStatus());
				ps.setString(6, dto.getStartDate() + " 00:00:00");
				ps.setString(7, dto.getEndDate() + " 23:59:59");

			} else if (dto.getMessageId() != null) { 
				sql = SQL_UPDATE;
				ps = con.prepareStatement(sql);
				ps.setString(1, dto.getMessage());
				ps.setInt(2, Integer.parseInt(dto.getUpdatedBy()));
				ps.setString(3, dto.getStartDate() + " 00:00:00");
				ps.setString(4, dto.getEndDate() + " 23:59:59");
				ps.setInt(5, Integer.parseInt(dto.getMessageId()));
			}
			rowsUpdated = ps.executeUpdate();
			logger.info(
				"Row insertion successful on table:KM_SCROLLER_MSTR. Inserted:"
					+ rowsUpdated
					+ " rows");
		} catch (SQLException e) {
			logger.error(
				"SQL Exception occured while inserting."
					+ "Exception Message: "
					+ e.getMessage());
			throw new KmException("SQLException: " + e.getMessage(), e);
		} catch (Exception e) {
			logger.error(
				"Exception occured while inserting."
					+ "Exception Message: "
					+ e.getMessage());
			e.printStackTrace();
			throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try {

				if (con != null) {
					con.setAutoCommit(true);
					con.close();
				}
				DBConnection.releaseResources(null, ps, rs);
			} catch (Exception e) {
				logger.error(
					"DAO Exception occured while inserting."
						+ "Exception Message: "
						+ e.getMessage());
				throw new KmException("DAO Exception: " + e.getMessage(), e);
			}
		}
		logger.info("Exit from  insert for table KM_SCROLLER_MSTR");
		return rowsUpdated;
	}

	public String getScrollerMessage(String elementId) throws KmException {
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String message = "";
		logger.info("Entered in getScrollerMessage");
		try {
		StringBuffer query=new StringBuffer(SQL_SCROLL_MESSEGE);
		query.append(" with ur ");
		
		if(!elementId.equals(""))
		{
			con = DBConnection.getDBConnection();
			ps = con.prepareStatement(query.toString());
			ps.setInt(1, Integer.parseInt(elementId));
			
			rs = ps.executeQuery();
			message = "";
			int i = 1;
			while (rs.next()) {
				String j = i + "";	
				message =
					message + "  " + j + "." + rs.getString("MESSAGE") + ".......... ";
				i++;
			}
		logger.info("message:"+message);
			
		  }	
		} catch (SQLException e) {

			logger.error(
				"SQL Exception occured while inserting."
					+ "Exception Message: "
					+ e.getMessage());
			throw new KmException("SQLException: " + e.getMessage(), e);
		} catch (Exception e) {

			logger.error(
				"Exception occured while inserting."
					+ "Exception Message: "
					+ e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try {
				DBConnection.releaseResources(con, ps, rs);
			} catch (Exception e) {
				logger.error(
					"DAO Exception occured while inserting."
						+ "Exception Message: "
						+ e.getMessage());
				throw new KmException("DAO Exception: " + e.getMessage(), e);
			}
		}
		logger.info("Exit from getScrollerMessage");
		return message;
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.dao.KmScrollerMstrDao#getScrollerList(com.ibm.km.dto.KmScrollerMstr)
	 */
	public String deleteScroller(KmScrollerMstr dto) throws KmException {
		logger.info("Entered delete Scroller for table KM_SCROLLER_MSTR");
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String message = "failure";
		try {
			con = DBConnection.getDBConnection();
			StringBuffer query=new StringBuffer(SQL_DELETE);
			//String sql = SQL_DELETE;

			ps = con.prepareStatement(query.append(" with ur").toString());
			ps.setInt(1, Integer.parseInt(dto.getMessageId()));
			ps.executeUpdate();
			message = "success";
			logger.info("Scroller Deleted");

		} catch (SQLException e) {
			message = "failure";

			logger.error(
				"SQL Exception occured while Scroller Delete."
					+ "Exception Message: "
					+ e.getMessage());
			throw new KmException("SQLException: " + e.getMessage(), e);
		} catch (Exception e) {
			message = "failure";

			logger.error(
				"Exception occured while getting Scroller Delete."
					+ "Exception Message: "
					+ e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try {

				if (con != null) {
					con.setAutoCommit(true);
					con.close();
				}
				DBConnection.releaseResources(null, ps, rs);
			} catch (Exception e) {
				logger.error(
					"DAO Exception occured while getting Scroller Delete."
						+ "Exception Message: "
						+ e.getMessage());
				throw new KmException("DAO Exception: " + e.getMessage(), e);
			}
		}
		logger.info("Exit from delete Scroller for table KM_SCROLLER_MSTR");
		return message;
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.dao.KmScrollerMstrDao#deleteScroller(com.ibm.km.dto.KmScrollerMstr)
	 */
	public ArrayList getScrollerList(KmScrollerMstr dto) throws KmException {
		logger.info("Entered view Scroller for table KM_SCROLLER_MSTR");
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		ArrayList scrollerList = new ArrayList();
		try {
			con = DBConnection.getDBConnection();
/*			if (dto.getActorId() != 3) {
				sql.append(
					" where CIRCLE_ID=? and date(START_DT) between ? and ?");
				logger.info(sql.toString());
//				logger.info("-----" + dto.getCircleId());
				logger.info("-----" + dto.getElementId());
				ps = con.prepareStatement(sql.toString()+" with ur ");
//				ps.setInt(1, Integer.parseInt(dto.getCircleId()));
				ps.setInt(1, Integer.parseInt(dto.getElementId()));
				ps.setString(2, dto.getStartDate());
				ps.setString(3, dto.getEndDate());

			} else if (dto.getActorId() == 3) {
				sql.append(
					" where CIRCLE_ID=? and CREATED_BY=? and date(START_DT) between ? and ?");
				logger.info(sql.toString());
				ps = con.prepareStatement(sql.toString()+" with ur ");
				ps.setInt(1, Integer.parseInt(dto.getElementId()));
				ps.setInt(2, Integer.parseInt(dto.getCreatedBy()));
				ps.setString(3, dto.getStartDate());
				ps.setString(4, dto.getEndDate());
			}
*/
			ps = con.prepareStatement(SQL_VIEW);
			
			java.sql.Date fromDt1 = Utility.getSqlDateFromString(dto.getStartDate().trim(),"yyyy-MM-dd") ;
			java.sql.Date toDt1 = Utility.getSqlDateFromString(dto.getEndDate().trim(),"yyyy-MM-dd") ;
			
			Calendar fromDt = new GregorianCalendar();
			fromDt.setTime(fromDt1);
			Calendar toDt = new GregorianCalendar();
			toDt.setTime(toDt1);
			
			toDt.set(Calendar.HOUR, 23);
			toDt.set(Calendar.MINUTE, 59);
			toDt.set(Calendar.SECOND, 59);
			ArrayList idIndex = new ArrayList();
			
			while(toDt.after(fromDt) ) {
			ps.setInt(1, Integer.parseInt(dto.getElementId()));
			Timestamp t1 = new Timestamp(fromDt.getTimeInMillis()) ;
			fromDt.add(Calendar.DATE, 1);
			Timestamp t2 = new Timestamp(fromDt.getTimeInMillis()-1001) ;
			ps.setTimestamp(2, t1);
			ps.setTimestamp(3, t2);
			rs = ps.executeQuery();
			while (rs.next()) {
				if(idIndex.contains(rs.getString("MESSAGE_ID")) )
						continue;
				idIndex.add(rs.getString("MESSAGE_ID"));
				KmScrollerMstr dto1 = new KmScrollerMstr();
				dto1.setMessage(rs.getString("MESSAGE"));
				dto1.setMessageId(rs.getString("MESSAGE_ID"));
				dto1.setStartDate(rs.getString("START_DT").substring(0, 10));
				dto1.setEndDate(rs.getString("END_DT").substring(0, 10));
				scrollerList.add(dto1);
			}
			}
			
			logger.info("Scroller List Populated : " + scrollerList.size());

		} catch (SQLException e) {

			logger.error(
				"SQL Exception occured while getting Scroller List."
					+ "Exception Message: "
					+ e);
			throw new KmException("SQLException: " + e.getMessage(), e);
		} catch (Exception e) {
			
			logger.error(
				"Exception occured while getting Scroller List."
					+ "Exception Message: "
					+ e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try {

				if (con != null) {
					con.setAutoCommit(true);
					con.close();
				}
				DBConnection.releaseResources(null, ps, rs);
			} catch (Exception e) {
				logger.error(
					"DAO Exception occured while getting Scroller List."
						+ "Exception Message: "
						+ e.getMessage());
				throw new KmException("DAO Exception: " + e.getMessage(), e);
			}
		}
		return scrollerList;
	}

	/*km phase2 edit alerts*/
	
	
	public int editScroller(KmAlertMstr dto) throws KmException {
	  logger.info("Entered into edit method for table KM_SCROLL_MSTR");
	  Connection con = null;
	  PreparedStatement ps = null;
	  ResultSet rs = null;
	  String sql = "";
	  int rowsUpdated = 0;
	   try {
	   con = DBConnection.getDBConnection();
	   StringBuffer query =new StringBuffer(SQL_SCROLLER_UPDATE);
	  
	   ps = con.prepareStatement(query.append(" with ur").toString());
	  
	   /*Preparing the statement for insertion */
	   ps.setString(1, dto.getMessage());
	   ps.setInt(2, Integer.parseInt(dto.getMessageId()));
	   
	   rowsUpdated = ps.executeUpdate();
	   logger.info(
		"Row updation successful on table:KM_Alert_MSTR. Inserted:"
		 + rowsUpdated
		 + " rows");
	  } catch (SQLException e) {
	   logger.info(e);
	   
	   logger.error(
		"SQL Exception occured while editing."
		 + "Exception Message: "
		 + e.getMessage());
	   throw new KmException("SQLException: " + e.getMessage(), e);
	  } catch (Exception e) {
	   logger.info(e);
	 
	   logger.error(
		"Exception occured while editing."
		 + "Exception Message: "
		 + e.getMessage());
	   throw new KmException("Exception: " + e.getMessage(), e);
	  } finally {
	   try {

		if (con != null) {
		 con.setAutoCommit(true);
		 con.close();
		}
		DBConnection.releaseResources(null, ps, rs);
	   } catch (Exception e) {
		
	   }
	  }
	  return rowsUpdated;
	 }

	/*
	 * To create PAN India Scroller
	 * (non-Javadoc)
	 * @see com.ibm.km.dao.KmScrollerMstrDao#createAllLobScroller(com.ibm.km.dto.KmScrollerMstr)
	 */
	public int createAllLobScroller(KmScrollerMstr dto) throws KmException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";
		int rowsUpdated = 0;
		ArrayList<KmElementMstr> elementList;
		try {
			KmElementMstrDaoImpl klm = new KmElementMstrDaoImpl();
			con = DBConnection.getDBConnection();
			StringBuffer query=new StringBuffer(SQL_INSERT);
			query.append(" with ur");
			ps = con.prepareStatement(query.toString());
			ps.setString(1, dto.getMessage());
			ps.setInt(2,1);   // Static for PAN India Scroller
			ps.setInt(3, Integer.parseInt(dto.getCreatedBy()));
			ps.setInt(4, Integer.parseInt(dto.getUpdatedBy()));
			ps.setString(5, dto.getStatus());
			ps.setString(6, dto.getStartDate() + " 00:00:00");
			ps.setString(7, dto.getEndDate() + " 23:59:59");
			rowsUpdated = ps.executeUpdate();
			ps = null;
			logger.info(
				"PAN India Scroller: Row insertion successful on table:KM_SCROLLER_MSTR. Inserted:"
					+ rowsUpdated
					+ " rows");
		} catch (SQLException e) {
			logger.error(
				"SQL Exception occured while inserting."
					+ "Exception Message: "
					+ e.getMessage());
			throw new KmException("SQLException: " + e.getMessage(), e);
		} catch (Exception e) {
			logger.error(
				"Exception occured while inserting."
					+ "Exception Message: "
					+ e.getMessage());
			e.printStackTrace();
			throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try {

				if (con != null) {
					con.setAutoCommit(true);
					con.close();
				}
				DBConnection.releaseResources(null, ps, rs);
			} catch (Exception e) {
				logger.error(
					"DAO Exception occured while inserting."
						+ "Exception Message: "
						+ e.getMessage());
				throw new KmException("DAO Exception: " + e.getMessage(), e);
			}
		}
		return rowsUpdated;
	}//createAllLobScroller

	
	public String getBulkScrollerMessage(List<Integer> elementIds) throws KmException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String message = "";
		
		if(elementIds.size() > 0)
		{		
			Iterator itr=null;
			logger.info("Entered in getBulkScrollerMessage");
			try {
				
			StringBuffer query=new StringBuffer(SQL_BULK_SCROLL_MESSEGE);
			
				
			
			for(itr=elementIds.iterator();itr.hasNext();)
			  {
				query.append(itr.next() + ",");
				
			  }
			int lastIndexOfComma = query.lastIndexOf(",");
			query = query.replace(lastIndexOfComma, lastIndexOfComma+1, "");		
			query.append(" ) with ur ");
			
				con = DBConnection.getDBConnection();
				ps = con.prepareStatement(query.toString());
				rs = ps.executeQuery();
				message = "";
				int i = 1;
				while (rs.next()) {
					String j = i + "";	
					message =
						message + "  " + j + "." + rs.getString("MESSAGE") + ".......... ";
					i++;
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
				logger.error(
					"SQL Exception occured while inserting."
						+ "Exception Message: "
						+ e.getMessage());
				throw new KmException("SQLException: " + e.getMessage(), e);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(
					"Exception occured while inserting."
						+ "Exception Message: "
						+ e.getMessage());
				throw new KmException("Exception: " + e.getMessage(), e);
			} finally {
				try {
	
					if (con != null) {
						con.setAutoCommit(true);
						con.close();
					}
					DBConnection.releaseResources(null, ps, rs);
				} catch (Exception e) {
					logger.error(
						"DAO Exception occured while inserting."
							+ "Exception Message: "
							+ e.getMessage());
					throw new KmException("DAO Exception: " + e.getMessage(), e);
				}
			}
		}
		logger.info("Exit from getScrollerMessage");
		return message;
	}

	
}//class

