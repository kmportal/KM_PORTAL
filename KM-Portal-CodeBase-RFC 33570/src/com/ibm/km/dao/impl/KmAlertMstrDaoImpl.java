/*
 * Created on Apr 15, 2008
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
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.ibm.km.common.PropertyReader;
import com.ibm.km.dao.DBConnection;
import com.ibm.km.dao.KmAlertMstrDao;
import com.ibm.km.dto.KmAlertMstr;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;

/**
 * @author aditya
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class KmAlertMstrDaoImpl implements KmAlertMstrDao {
 /*
  * Logger for the class.
  */
 private static final Logger logger;

 static {
  logger = Logger.getLogger(KmAlertMstrDaoImpl.class);
  //timestamp_format(?+ 00:00:00, 'YYYY-MM-DD HH24:MI:SS')
 }

 protected static final String SQL_INSERT ="INSERT INTO KM_ALERT_MSTR (MESSAGE, MESSAGE_ID, CIRCLE_ID,CREATED_DT, CREATED_BY, UPDATED_DT, UPDATED_BY, STATUS, ALERT_SOURCE) VALUES (?, ? , ?,  current timestamp, ?, current timestamp,?, ? ,?)";
 
 protected static final String SQL_ALERT_MESSEGE ="SELECT MESSAGE,CREATED_DT AS CREATED_DT FROM KM_ALERT_MSTR  where CREATED_DT =(SELECT max(CREATED_DT) FROM KM_ALERT_MSTR where CIRCLE_ID = ? and STATUS = 'A') ";
 
 protected StringBuffer SQL_VIEW = new StringBuffer("Select * from KM_ALERT_MSTR ");

 protected static final String SQL_DELETE ="delete from KM_ALERT_MSTR where MESSAGE_ID=?";
 
 protected static final String SQL_UPDATE = "update KM_ALERT_MSTR set (MESSAGE,UPDATED_DT,UPDATED_BY)=(?,current timestamp,?) where CREATED_BY=? ";
 
 protected StringBuffer UPDATE_ALERT_FLAG = new StringBuffer("update KM_USER_MSTR set USER_ALERTS =? ");
 
// protected String sqlupdateAlertId = "select ALERT_ID,USER_ALERTS,TIMESTAMPDIFF(4, CAST(SESSION_EXPIRY_TIME-CURRENT TIMESTAMP AS CHAR(22))) AS MINUTES from KM_USER_MSTR where USER_ID=? ";
  
 protected String sqlupdateAlertId = "select ALERT_ID,USER_ALERTS from KM_USER_MSTR where USER_ID=? ";
  /*km phase2 edit alert & alerthistory report*/  
 protected static final String SQL_GET_ALERT="select date( KM_ALERT_MSTR.CREATED_DT) as CREATED_DATE ,  KM_ALERT_MSTR.CIRCLE_ID,Time( KM_ALERT_MSTR.CREATED_DT)as CREATION_TIME  ,  KM_ALERT_MSTR.MESSAGE ,  KM_USER_MSTR.USER_LOGIN_ID as ADMIN_ID ,  KM_ALERT_MSTR.ALERT_SOURCE from KM_ALERT_MSTR  inner join KM_USER_MSTR on KM_ALERT_MSTR.CREATED_BY= KM_USER_MSTR.USER_ID where KM_ALERT_MSTR.CIRCLE_ID = ? ";
 
 protected static final StringBuffer SQL_GET_LATEST_ALERTS=new StringBuffer("select date( KM_ALERT_MSTR.CREATED_DT) as CREATED_DATE ,  KM_ALERT_MSTR.MESSAGE_ID,Time( KM_ALERT_MSTR.CREATED_DT)as CREATION_TIME  ,  KM_ALERT_MSTR.MESSAGE ,  KM_ELEMENT_MSTR.ELEMENT_NAME as CIRCLE_ID ,  KM_ALERT_MSTR.ALERT_SOURCE from KM_ALERT_MSTR  inner join KM_ELEMENT_MSTR on  KM_ALERT_MSTR.CIRCLE_ID= KM_ELEMENT_MSTR.ELEMENT_ID ");
 
 protected static final String ALERT_UPDATE ="update KM_ALERT_MSTR set (MESSAGE,UPDATED_DT)=(?,current timestamp) where MESSAGE_ID=?";
  
 // protected String sqlupdateAlertId="select * from KM_USER_MSTR ";
 /* (non-Javadoc)
  * @see com.ibm.km.dao.KmAlertMstrDao#insert(com.ibm.km.dto.KmAlertMstr)
 * inser a record for a Alert in the table Alert_MSTR. Insert values like message, start date, end date etc
 *  */

 public int insertNew(KmAlertMstr dto) throws KmException {
  logger.info("Entered insert for table KM_Alert_MSTR");
  Connection con = null;
  PreparedStatement ps = null;
  ResultSet rs = null;
 // String sql = "";
  StringBuffer sqlView = SQL_VIEW;

  int rowsUpdated = 0;
  int alertFlags = 0;
  try {
   con = DBConnection.getDBConnection();
  
   //   sqlView.append(" where created_by=? ");
   //   logger.info("SQL_VIEW====="+sqlView.toString());
   //   ps = con.prepareStatement(sqlView.toString());
   //   ps.setInt(1,Integer.parseInt(dto.getCreatedBy()));
   //   rs=ps.executeQuery();
   //   boolean flag=rs.next();
   //   logger.info("flag is "+flag+"---"+dto.getMessageId());

   //if(dto.getMessageId()==null && flag==false){
   //   if(flag==false){
 //  StringBuffer query =new StringBuffer(SQL_INSERT);
   //sql = SQL_INSERT;
   ps = con.prepareStatement(SQL_INSERT);
   logger.info("flag==false");
   /*Preparing the statement for insertion */
   ps.setString(1, dto.getMessage());
   ps.setInt(2, Integer.parseInt(dto.getCircleId()));
   ps.setInt(3, Integer.parseInt(dto.getCreatedBy()));
   ps.setInt(4, Integer.parseInt(dto.getUpdatedBy()));
   ps.setString(5, dto.getStatus());
   ps.setString(6, dto.getAlertSource());

   //   }
   //   //else if(dto.getMessageId()!=null && flag==true) {
   //   else{
   //    sql = SQL_UPDATE; 
   //    ps = con.prepareStatement(sql);
   //    ps.setString(1, dto.getMessage());
   //    ps.setInt(2, Integer.parseInt(dto.getUpdatedBy()));
   //    ps.setInt(3,Integer.parseInt(dto.getCreatedBy()));
   //    
   //   }
   rowsUpdated = ps.executeUpdate();
   logger.info(
    "Row insertion successful on table:KM_Alert_MSTR. Inserted:"
     + rowsUpdated
     + " rows");

   // update USER master for alerts
   ps = con.prepareStatement(UPDATE_ALERT_FLAG.toString());
   ps.setString(1, "N");
   alertFlags = ps.executeUpdate();
   logger.info(
    "Alert Flag in table:KM_USER_MSTR. Updated:"
     + alertFlags
     + " rows");

  } catch (SQLException e) {
	  e.printStackTrace();
   logger.info(e);
  
   logger.error(
    "SQL Exception occured while inserting."
     + "Exception Message: "
     + e.getMessage());
   throw new KmException("SQLException: " + e.getMessage(), e);
  } catch (Exception e) {
	  e.printStackTrace();
   logger.info(e);
  
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
   
   }
  }
  return rowsUpdated;
 }
 
 
 
 public int insert(KmAlertMstr dto) throws KmException {
	  logger.info("Entered insert for table KM_Alert_MSTR");
	  Connection con = null;
	  PreparedStatement ps = null;
	  ResultSet rs = null;
	  int rowsUpdated = 0;
	  int alertFlags = 0;
	  try {
	   con = DBConnection.getDBConnection();
	   int messageId;
	   ps=con.prepareStatement("SELECT NEXTVAL FOR KM_ALERT_ID_SEQ AS MESSAGE_ID  FROM SYSIBM.SYSDUMMY1 ");
	   rs=ps.executeQuery();
	   if(rs.next()){
		   messageId=rs.getInt("MESSAGE_ID");
	   }else{
		   throw new SQLException("Alert Sequence not obtained");
	   }
	   
	   ps = con.prepareStatement(SQL_INSERT);
	  
	   /*Preparing the statement for insertion */
	   ps.setString(1, dto.getMessage());
	   ps.setInt(2, messageId);
	   ps.setInt(3, Integer.parseInt(dto.getCircleId()));
	   ps.setInt(4, Integer.parseInt(dto.getCreatedBy()));
	   ps.setInt(5, Integer.parseInt(dto.getUpdatedBy()));
	   ps.setString(6, dto.getStatus());
	   ps.setString(7, dto.getAlertSource());
	   rowsUpdated = ps.executeUpdate();
	   logger.info(
	    "Row insertion successful on table:KM_Alert_MSTR. Inserted:"
	     + rowsUpdated
	     + " rows");
	   
	   //***** Code added (by Kundan) to resolve alert issue - (max 10 alerts should be there in column)*****//
		   String alertIds = "";
		   String newAlertIds="";
		   int maxNoOfAlerts=Integer.parseInt(PropertyReader.getAppValue("MAX.NO.OF.ALERTS"));
		   int maxMinutesAlertsExpires=Integer.parseInt(PropertyReader.getAppValue("MINUTES.EXPIRES.ALERTS"));
		   // getting prvious AlertIds
		   ps=con.prepareStatement("select MESSAGE_ID from KM_ALERT_MSTR where CIRCLE_ID = ?   and  minute (current timestamp  - CREATED_DT) < "+maxMinutesAlertsExpires+"   order by created_dt desc fetch first "+ maxNoOfAlerts +" rows only");
		   ps.setString(1, dto.getCircleId());
		   rs=ps.executeQuery();
		   		   		   	   		  
		   // Adding previous alert ids.
		   while(rs.next()){
			   alertIds=alertIds+","+rs.getString("MESSAGE_ID");
		   }	
		   alertIds = alertIds.replaceFirst(",","");
		   
		   String alertIdArr[] = alertIds.split(",");
		   alertIds="";
		   for(int i=alertIdArr.length-1; i>=0;i--)
		   {
			   alertIds = alertIds+","+ alertIdArr[i]	;
		   }
		   alertIds = alertIds.replaceFirst(",","");
		   
		   if(alertIds.lastIndexOf(",") != (alertIds.length()-1))
		   {
			   alertIds = alertIds + ",";
		   }
		   alertIds =  alertIds.substring(0, alertIds.length());
		   //********** End of Code to resolve alert issue - (max 10 alerts should be there in column)*****//
		   
	   ps = con.prepareStatement("UPDATE KM_USER_MSTR set USER_ALERTS =? , ALERT_ID = '"+ alertIds+"' where ELEMENT_ID = ? and KM_ACTOR_ID IN (?,?) and status = 'A'  with ur  ");	   
	   ps.setString(1, "N");
	  // ps.setString(2, messageId+",");
	   ps.setString(2, dto.getCircleId());
	   ps.setString(3, PropertyReader.getAppValue("CircleCSR"));
	   ps.setString(4, PropertyReader.getAppValue("CategoryCSR"));
	   alertFlags = ps.executeUpdate();
	   
	   
	   logger.info(
	    "Alert Flag in table:KM_USER_MSTR. Updated:"
	     + alertFlags
	     + " rows");

	  } catch (SQLException e) {e.printStackTrace();
	   logger.info(e);
	  
	   logger.error(
	    "SQL Exception occured while inserting."
	     + "Exception Message: "
	     + e.getMessage());
	   throw new KmException("SQLException: " + e.getMessage(), e);
	  } catch (Exception e) {
		  e.printStackTrace();
	   logger.info(e);
	  
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
	   
	   }
	  }
	  return rowsUpdated;
	 }

 public String getAlertMessage(String circleId) throws KmException {
  KmAlertMstr dto;
  Connection con = null;
  PreparedStatement ps = null;
  ResultSet rs = null;

  ArrayList scrollList = new ArrayList();

  String message = "";
  try {
   StringBuffer query =new StringBuffer(SQL_ALERT_MESSEGE);
   con = DBConnection.getDBConnection();
   ps = con.prepareStatement(query.append(" with ur").toString());
   ps.setInt(1, Integer.parseInt(circleId));
   rs = ps.executeQuery();
   message = "";
   while (rs.next()) { 
    dto = new KmAlertMstr();
    message =
     message + rs.getString("MESSAGE") + "  "+rs.getString("CREATED_DT").substring(0,19);
   }
   //logger.info(message);
  } catch (SQLException e) {
   logger.info(e);
  
   logger.error(
    "SQL Exception occured while inserting."
     + "Exception Message: "
     + e.getMessage());
   throw new KmException("SQLException: " + e.getMessage(), e);
  } catch (Exception e) {
   logger.info(e);
   
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
  
   }
  }
  return message;
 }

 /* (non-Javadoc)
  * @see com.ibm.km.dao.KmAlertMstrDao#getAlertList(com.ibm.km.dto.KmAlertMstr)
  */
 public String deleteAlert(KmAlertMstr dto) throws KmException {
  logger.info("Entered delete Alert for table KM_Alert_MSTR");
  Connection con = null;
  PreparedStatement ps = null;
  ResultSet rs = null;

  String message = "failure";
  try {
   con = DBConnection.getDBConnection();
   //String sql = SQL_DELETE;
   StringBuffer query =new StringBuffer(SQL_DELETE);
   ps = con.prepareStatement(query.append(" with ur").toString());
   ps.setInt(1, Integer.parseInt(dto.getMessageId()));
   ps.executeUpdate();
   message = "success";
   logger.info("Alert Deleted");

  } catch (SQLException e) {
   message = "failure";
   logger.info(e);
   
   logger.error(
    "SQL Exception occured while Alert Delete."
     + "Exception Message: "
     + e.getMessage());
   throw new KmException("SQLException: " + e.getMessage(), e);
  } catch (Exception e) {
   message = "failure";
   logger.info(e);
  
   logger.error(
    "Exception occured while getting Alert Delete."
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
  return message;
 }

 /* (non-Javadoc)
  * @see com.ibm.km.dao.KmAlertMstrDao#deleteAlert(com.ibm.km.dto.KmAlertMstr)
  */
 public ArrayList getAlertListNew(KmAlertMstr dto) throws KmException {

  Connection con = null;
  PreparedStatement ps = null;
  ResultSet rs = null;
  int alertFlags = 0;
  String finalList = "";
  ArrayList alertList = new ArrayList();
  ArrayList newAlertIds = new ArrayList();
  
  String interval="";
  try {
   con = DBConnection.getDBConnection();
  
   ps = con.prepareStatement(sqlupdateAlertId+" with ur");
   ps.setInt(1, dto.getUserId());
   rs = ps.executeQuery();
   String flag = "Y";
   String alertfrmUser = "";
   while (rs.next()) {
    flag = rs.getString("USER_ALERTS");
    alertfrmUser = rs.getString("ALERT_ID");
	//alerts.add(rs.getString("MINUTES"));
   }
  
   if (flag == null || flag.equalsIgnoreCase("N")) {
    String lastAlertId = "";
    StringBuffer sql = SQL_VIEW;
    //KM Phase2Changed by Anil 
    interval=PropertyReader.getAppValue("alert.interval");
    sql.append(
     " where CIRCLE_ID in (0,?) and (current timestamp - CREATED_DT )< "+interval+" ");
    logger.info(sql.toString());
    logger.info("-----" + dto.getCircleId());
    ps = con.prepareStatement(sql.toString()+"  with ur  ");
    ps.setInt(1, Integer.parseInt(dto.getCircleId()));
    rs = ps.executeQuery();
    KmAlertMstr newdto = new KmAlertMstr();
    while (rs.next()) {
     newdto = new KmAlertMstr();
     newdto.setMessage(rs.getString("MESSAGE"));
     newdto.setCreatedDt(rs.getString("CREATED_DT"));
     alertList.add(newdto);
    //KM Phase 2 : Changed by Anil for showing creation time along with alerts
    
    newAlertIds.add(rs.getString("MESSAGE_ID"));
   // newAlertIds.add(newdto);
    }
    logger.info("Alert List Populated : " + alertList.size());
    logger.info("MESSAGE_ID---" + newAlertIds.toString());
  //  alerts.add(alertList);
    //Adding the list of alerts to User Mstr
    logger.info("alertfrmUser------------" + alertfrmUser);
    finalList = alertInsert(alertfrmUser, newAlertIds);
    logger.info("finalList<<<<<-------->>>>>>" + finalList);
    // UPDATE FALG FOR USER

    String updateAlertId =
     "update KM_USER_MSTR set USER_ALERTS =?,ALERT_ID=? where USER_ID=? ";
    logger.info("userIIIIIIDDDDDDDDD" + dto.getUserId());
    ps = con.prepareStatement(updateAlertId+" with ur");
    ps.setString(1, "Y");
    ps.setString(2, finalList);
    ps.setInt(3, dto.getUserId());
    alertFlags = ps.executeUpdate();
    logger.info(
     "Alert Flag in table:KM_USER_MSTR. Updated:"
      + alertFlags
      + " rows");
   }
  } catch (SQLException e) {
   logger.info(e);
  
   logger.error(
    "SQL Exception occured while getting Alert List."
     + "Exception Message: "
     + e.getMessage());
   throw new KmException("SQLException: " + e.getMessage(), e);
  } catch (Exception e) {
   logger.info(e);
  
   logger.error(
    "Exception occured while getting Alert List."
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
  return (ArrayList) alertList;
 }
 /* (non-Javadoc)
  * @see com.ibm.km.dao.KmAlertMstrDao#deleteAlert(com.ibm.km.dto.KmAlertMstr)
  */
 public ArrayList getAlertList(KmAlertMstr dto) throws KmException {

  Connection con = null;
  PreparedStatement ps = null;
  ResultSet rs = null;
  int alertFlags = 0;
  String finalList = "";
  ArrayList alertList = new ArrayList();
  StringBuffer alerts= null;
  String interval="";
  try {
   con = DBConnection.getDBConnection();
  
   ps = con.prepareStatement("select ALERT_ID from KM_USER_MSTR where USER_ID=? AND USER_ALERTS = 'N' with ur");
   ps.setInt(1, dto.getUserId());
   rs = ps.executeQuery();
   
   String alertfrmUser = "";
   while (rs.next()) {
   
    alertfrmUser = rs.getString("ALERT_ID");
	
   }
   if(alertfrmUser == null || alertfrmUser.trim().equals("")){
	   return null;
   }
   finalList = cropAlertNewNew(alertfrmUser);
   if(finalList==null){
	   return null;
   }
    StringBuffer sql = SQL_VIEW;
    //KM Phase2Changed by Anil 
    logger.info("alertfrmUser------------" + alertfrmUser);
    interval=PropertyReader.getAppValue("alert.interval");
    sql.append(
     " where message_id in ("+finalList.substring(0, finalList.lastIndexOf(","))+") AND CIRCLE_ID = ? and (current timestamp - CREATED_DT )< "+interval+" ");
    logger.info(sql.toString());
    logger.info("-----" + dto.getCircleId());
    ps = con.prepareStatement(sql.toString()+"   with ur  ");
    ps.setInt(1, Integer.parseInt(dto.getCircleId()));
    rs = ps.executeQuery();
    KmAlertMstr newdto = new KmAlertMstr();
    alerts =new StringBuffer("");
    while (rs.next()) {
     newdto = new KmAlertMstr();
     newdto.setMessage(rs.getString("MESSAGE"));
     newdto.setCreatedDt(rs.getString("CREATED_DT"));
     alertList.add(newdto);
     alerts.append(rs.getString("MESSAGE_ID")).append(",");
    }
     
 

    String updateAlertId =
     "update KM_USER_MSTR set USER_ALERTS =?,ALERT_ID=? where USER_ID=? ";
   
    ps = con.prepareStatement(updateAlertId+" with ur");
    ps.setString(1, "Y");
 logger.info("updating "+alerts);
    ps.setString(2, alerts.toString());
    ps.setInt(3, dto.getUserId());
    alertFlags = ps.executeUpdate();
    logger.info(
     "Alert Flag in table:KM_USER_MSTR. Updated:"
      + alertFlags
      + " rows");
   
  } catch (SQLException e) {
   logger.info(e);
  e.printStackTrace();
   logger.error(
    "SQL Exception occured while getting Alert List."
     + "Exception Message: "
     + e.getMessage());
   throw new KmException("SQLException: " + e.getMessage(), e);
  } catch (Exception e) {
   logger.info(e);
  
   logger.error(
    "Exception occured while getting Alert List."
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
  return (ArrayList) alertList;
 }
 private String cropAlert(String alertfrmUser) {
	 int maxNoOfAlerts=Integer.parseInt(PropertyReader.getAppValue("MAX.NO.OF.ALERTS"));
	 logger.info(
			   "In function alertInsert"
			    + alertfrmUser);
	 		String finalAlertList=null;
	 		try{
			  // Iterating over the list of new alert ids
			 
			    //Tokenize the old alerts and keep in arraylist
			    StringTokenizer st = new StringTokenizer(alertfrmUser, ",",false);
			    int i=0;
			    ArrayList alertIds= new ArrayList();
			    while (st.hasMoreTokens()) {
			    	////System.out.println();
			    	alertIds.add(st.nextToken());
			    	i++;
			    }
			  int size=alertIds.size();

			    // if the alerts are more than maxNoOfAlerts then FIFO
			    if (size > maxNoOfAlerts ) {
			    	StringBuffer alerts=new StringBuffer("");
			    	for(int k =0; k< size;k++){
			    		if(k>=size%maxNoOfAlerts)
			    			alerts.append((String)alertIds.get(k)).append(",");
			    	}
			    	finalAlertList=alerts.toString();
			    	
			    	/*//System.out.println("1st element removed ? "+alertIds.remove(0));
			    	StringBuffer alerts=new StringBuffer("");
			    	for(int j=0;j<10;j++){
			    		
			    		alerts.append((String)alertIds.get(j)).append(",");
			    	}
			    	finalAlertList=alerts.toString();*/
			    }
			    else{
			    	finalAlertList=alertfrmUser;
			    }
			    logger.info("alertfrmUser" + alertfrmUser);
			   
			   // checking the alertfrmUser null condition
			   
	 		}
	 		catch (Exception e) {
	 			e.printStackTrace();
				
			}
 logger.info(
		   "In function finalAlertList :"
		    + finalAlertList);
			  return finalAlertList;
}

private String cropAlertNew(String alertfrmUser) {
	 int maxNoOfAlerts=Integer.parseInt(PropertyReader.getAppValue("MAX.NO.OF.ALERTS"));
	 logger.info(
			   "In function alertInsert"
			    + alertfrmUser);
	 		String finalAlertList=null;
	 		try{
			  // Iterating over the list of new alert ids
	 			int  j =0;
	 			int lastindex=0;;
			    //Tokenize the old alerts and keep in arraylist
	 			for(int i=0;i<alertfrmUser.length();i++){
	 				if(alertfrmUser.charAt(i) ==',')
	 				{
	 					j++;
	 					if(j==maxNoOfAlerts){
	 						lastindex=i;
	 					}
	 					else if(j>maxNoOfAlerts){
	 						break;
	 					}
	 					
	 				}	
	 			}
	 			if(j>maxNoOfAlerts){
	 				finalAlertList = alertfrmUser.substring(0,lastindex+1);
	 			}
	 			else{
	 				finalAlertList = alertfrmUser;
	 			}
			   
	 		}
	 		catch (Exception e) {
	 			e.printStackTrace();
				
			}
 logger.info(
		   "In function finalAlertList :"
		    + finalAlertList);
			  return finalAlertList;
}
private String cropAlertNewNew(String alertfrmUser) {
	 int maxNoOfAlerts=Integer.parseInt(PropertyReader.getAppValue("MAX.NO.OF.ALERTS"));
	 logger.info(
			   "In function alertInsert"
			    + alertfrmUser);
	 		StringBuffer finalAlertList=null;
	 		try{
	 			 StringTokenizer st = new StringTokenizer(alertfrmUser, ",",false);
				    int i=0;
				    ArrayList stack= new ArrayList();
				    while (st.hasMoreTokens()) {
				    	stack.add(st.nextToken());
				    }
				    
				    int stackSize=stack.size();
				    if(stackSize>3){
				    	
				    	int index= stack.size() - maxNoOfAlerts;
				    	//Popping from stack
				    	String alertId;
				    	finalAlertList = new StringBuffer("");
				    	for(Iterator itr = stack.listIterator(index);itr.hasNext();){
				    		alertId = (String)itr.next();
				    		finalAlertList.append(alertId).append(",");
				    	}
				    	alertfrmUser =finalAlertList.toString() ; 
				    }
	 		}
	 		catch (Exception e) {
	 			e.printStackTrace();
				
			}
logger.info(
		   "In function finalAlertList :"
		    + alertfrmUser);
			  return alertfrmUser;
}

private String alertInsert(String alertfrmUser, List newAlertIds) {
  logger.info(
   "In function alertInsert"
    + alertfrmUser
    + "newAlertIds---"
    + newAlertIds);

  // Iterating over the list of new alert ids
  Iterator itr = newAlertIds.iterator();
  while (itr.hasNext()) {
   List alertIds = new ArrayList();
  
   // New alert ids
   String next = (String) itr.next();
  
   // if already exsiting alerts in database are not null then following 
   if (alertfrmUser != null) {

    //Tokenize the old alerts and keep in arraylist
    StringTokenizer st = new StringTokenizer(alertfrmUser, ",");
    while (st.hasMoreTokens()) {
     
     
     alertIds.add((String) st.nextToken());
    }

    //If new alerts are same as old remove them from new list
    if (alertIds.contains(next)) {
     newAlertIds.remove(next);
     break;
    }

    // if the alerts are more than 10 then FIFO
    if (alertIds.size() >= 10) {
     String finalAlertList;
     finalAlertList =
      alertfrmUser.substring(
       alertfrmUser.indexOf(",") + 1,
       alertfrmUser.length());
     alertfrmUser = finalAlertList + next + ",";
    }

    // if the alerts are less than 10 then normal
    else {
     alertfrmUser = alertfrmUser + next + ",";
    }
    logger.info("alertfrmUser" + alertfrmUser);
   }
   // checking the alertfrmUser null condition
   else {
    alertfrmUser = next + ",";
    logger.info("alertfrmUser----" + alertfrmUser);
   }
  }

  return alertfrmUser;
 }
 public ArrayList getAlertHistoryReport(String circleId,String timeInterval) throws KmException{
		 Connection con=null;
		
		 ResultSet rs = null;
		 PreparedStatement ps = null;
			
		 ArrayList alertList=new ArrayList();
		 try {
			StringBuffer query =new StringBuffer(SQL_GET_ALERT);
			query.append(" AND(current timestamp - KM_ALERT_MSTR.CREATED_DT )< "+timeInterval+"  order by KM_ALERT_MSTR.CREATED_DT DESC ");
			  
			// String sql = SQL_GET_ALERT;
			 con=DBConnection.getDBConnection();
			 logger.info(circleId);
			 ps=con.prepareStatement(query.append(" with ur ").toString());
			 KmAlertMstr dto=new  KmAlertMstr();
			 ps.setInt(1, Integer.parseInt(circleId));
			 rs = ps.executeQuery();
			 while(rs.next()){
				 dto=new KmAlertMstr();
				 dto.setCreatedDt(rs.getString("CREATED_DATE"));
				 dto.setCircleId(rs.getString("CIRCLE_ID"));
				 dto.setCreatedTime(rs.getString("CREATION_TIME"));
					
				 dto.setMessage(rs.getString("MESSAGE"));
				 dto.setCreatedBy(rs.getString("ADMIN_ID"));
				 dto.setAlertSource(rs.getString("ALERT_SOURCE"));
				 alertList.add(dto); 	
			 }
				
			 logger.info("List is returned :"+alertList.size());	
			
	         
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
		 return alertList;
	 }
	
 public ArrayList getAlerts(String circleId,String time) throws KmException{
			 Connection con=null;
		
			 ResultSet rs = null;
			 PreparedStatement ps = null;
				
			
			 ArrayList alertList=new ArrayList();
			//String time=bundle.getString("alert.time");
			 try {
					
				 StringBuffer sql = new StringBuffer(SQL_GET_LATEST_ALERTS.toString());
				
				 sql.append(
				 " where (date( KM_ALERT_MSTR.CREATED_DT))=(current date)  and KM_ALERT_MSTR.CIRCLE_ID=? and (current timestamp - KM_ALERT_MSTR.CREATED_DT )< ").append(time);
				 logger.info(sql.toString());
				 //String sql = SQL_GET_LATEST_ALERTS;
				 con=DBConnection.getDBConnection();
				 logger.info(circleId);
				 ps=con.prepareStatement(sql+" with ur ");
				
				 KmAlertMstr dto=new  KmAlertMstr();
				 ps.setInt(1, Integer.parseInt(circleId));
				 rs = ps.executeQuery();
				 while(rs.next()){
					 dto=new KmAlertMstr();
					 dto.setCreatedDt(rs.getString("CREATED_DATE"));
					 dto.setCircleId(rs.getString("CIRCLE_ID"));
					 dto.setCreatedTime(rs.getString("CREATION_TIME"));
					
					 dto.setMessage(rs.getString("MESSAGE"));
					 dto.setMessageId(rs.getString("MESSAGE_ID"));
					 dto.setAlertSource(rs.getString("ALERT_SOURCE"));
					 alertList.add(dto); 	
				 }
				
				 logger.info("List is returned :"+alertList.size());	
			
	         
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
			 return alertList;
		 }
	/*km phase2 edit alerts*/
	
	
	public int editAlert(KmAlertMstr dto) throws KmException {
	  logger.info("Entered into edit method for table KM_Alert_MSTR");
	  Connection con = null;
	  PreparedStatement ps = null;
	  ResultSet rs = null;
	  String sql = "";
	  int rowsUpdated = 0;
	  int alertFlags = 0;
	   try {
	   con = DBConnection.getDBConnection();
	   StringBuffer query =new StringBuffer(ALERT_UPDATE);
	   //sql = ALERT_UPDATE;
	   ps = con.prepareStatement(query.append(" with ur").toString());
	  
	   /*Preparing the statement for insertion */
	   ps.setString(1, dto.getMessage());
	   ps.setInt(2, Integer.parseInt(dto.getMessageId()));
	   
	   rowsUpdated = ps.executeUpdate();
	   logger.info(
		"Row updation successful on table:KM_Alert_MSTR. Inserted:"
		 + rowsUpdated
		 + " rows");
	   
	   
	   // update USER master for alerts
	   ps = con.prepareStatement(UPDATE_ALERT_FLAG.toString());
	   ps.setString(1, "N");
	   alertFlags = ps.executeUpdate();
	   logger.info(
	    "Alert Flag in table:KM_USER_MSTR. Updated:"
	     + alertFlags
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
 
}
