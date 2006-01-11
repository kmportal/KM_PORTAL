// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   KmBriefingMstrDaoImpl.java

package com.ibm.km.dao.impl;

import com.ibm.km.dao.DBConnection;
import com.ibm.km.dao.KmBriefingMstrDao;
import com.ibm.km.dto.CSRQuestionDto;
import com.ibm.km.dto.KmBriefingMstr;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.dto.LobDTO;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.KmBriefingMstrFormBean;
import java.sql.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;

public class KmBriefingMstrDaoImpl
    implements KmBriefingMstrDao
{

    public KmBriefingMstrDaoImpl()
    {
    }

    public void insert(KmBriefingMstr dto)
        throws KmException
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        logger.info("Entered insert for table KM_BRIEFING_MSTR");
        con = null;
        ps = null;
        rs = null;
        int rowsUpdated = 0;
        try
        {
            StringBuffer query = new StringBuffer("INSERT INTO KM_BRIEFING_MSTR (BRIEFING_ID, BRIEFING_HEADING, BRIEFING_DETAILS, CIRCLE_ID, CREATED_BY, CREATED_DT, DISPLAY_DT,FROMDATE,TODATE,CATEGORY_ID) VALUES ( NEXTVAL FOR KM_BRIEFING_ID, ?, ?, ?, ?,  current timestamp, timestamp_format(?, 'YYYY-MM-DD HH24:MI:SS'),timestamp_format(?, 'YYYY-MM-DD HH24:MI:SS'),timestamp_format(?, 'YYYY-MM-DD HH24:MI:SS'),?)");
            con = DBConnection.getDBConnection();
            ps = con.prepareStatement(query.append(" with ur").toString());
            ps.setString(1, dto.getBriefingHeading());
            ps.setString(2, dto.getBriefingDetails());
            ps.setInt(3, Integer.parseInt(dto.getCircleId()));
            ps.setInt(4, Integer.parseInt(dto.getCreatedBy()));
            ps.setString(5, (new StringBuilder(String.valueOf(dto.getDisplayDt()))).append("00:00:00").toString());
            ps.setString(6, (new StringBuilder(String.valueOf(dto.getFromDate()))).append("00:00:00").toString());
            logger.info("dto.getFromDate() : "+dto.getFromDate());
            ps.setString(7, (new StringBuilder(String.valueOf(dto.getToDate()))).append("00:00:00").toString());
            logger.info("dto.getToDate() : "+dto.getToDate());
            ps.setString(8, dto.getCategoryId());
            rowsUpdated = ps.executeUpdate();
            logger.info((new StringBuilder("Row insertion successful on table:KM_BRIEFING_MSTR. Inserted:")).append(rowsUpdated).append(" rows").toString());
        }
        catch(SQLException e)
        {
        	e.printStackTrace();
            logger.error((new StringBuilder("SQL Exception occured while inserting.Exception Message: ")).append(e.getMessage()).toString());
            throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            logger.error((new StringBuilder("Exception occured while inserting.Exception Message: ")).append(e.getMessage()).toString());
            throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }
      finally{
        try
        {
           
            DBConnection.releaseResources(con, ps, rs);
        }
        catch(Exception e)
        {
            logger.error((new StringBuilder("DAO Exception occured while inserting.Exception Message: ")).append(e.getMessage()).toString());
            throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }
      }
    }
    
    public ArrayList viewLoginBriefing(String circleId, String categoryId, String date)
    throws KmException
{
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    ArrayList briefingList;
    logger.debug("Entered view Briefings at login");
    con = null;
    ps = null;
    rs = null;
    briefingList = new ArrayList();
    try
    {
        StringBuffer query = new StringBuffer("SELECT * FROM KM_BRIEFING_MSTR WHERE");
       /* if(categoryId != null)
        {
            query.append(" CIRCLE_ID=? AND DISPLAY_DT >  ( current timestamp - 24 hours) and date(DISPLAY_DT) < (current date + 1 day) ");
            query.append(" AND ( CATEGORY_ID  = ? OR CATEGORY_ID IS NULL ) ");
        } else
        {
            query.append(" CIRCLE_ID=? AND DISPLAY_DT >  ( current timestamp - 24 hours) and date(DISPLAY_DT) < (current date + 1 day)  AND CATEGORY_ID IS NULL  ");
        }*/
        
        if(categoryId != null)
        {
            query.append(" CIRCLE_ID=? AND current date >=  date( FROMDATE ) and current date <= date(TODATE) ");
            query.append(" AND ( CATEGORY_ID  = ? OR CATEGORY_ID IS NULL ) ");
        } else
        {
            query.append(" CIRCLE_ID=? AND date(DISPLAY_DT) >  date( current timestamp - 1 day ) and date(DISPLAY_DT) < (current date + 1 day) ");
        }
        
        logger.info("Circle ID: "+ circleId + " Date:## " + date + " SQL Query:" + query.toString() );
        
        con = DBConnection.getDBConnection();
        ps = con.prepareStatement(query.append(" with ur").toString());

        ps.setInt(1, Integer.valueOf(circleId));
        //ps.setString(2, (new StringBuilder(String.valueOf(date))).append(" 00:00:00").toString());
        if(categoryId != null)
            ps.setString(2, categoryId);
        rs = ps.executeQuery();
        KmBriefingMstr briefing = null;
        for(; rs.next(); briefingList.add(briefing))
        {
            briefing = new KmBriefingMstr();
            briefing.setBriefingId(rs.getString("BRIEFING_ID"));
            briefing.setBriefingHeading(rs.getString("BRIEFING_HEADING"));
            briefing.setBriefingDetails(rs.getString("BRIEFING_DETAILS"));
            briefing.setCreatedDt(rs.getString("CREATED_DT").substring(0,19));
            //System.out.println(  briefing.getCreatedDt());
            
        }
        logger.debug("Briefing View successful on table:KM_BRIEFING_MSTR.");
    }
    catch(SQLException e)
    {
        e.printStackTrace();
        logger.error((new StringBuilder("SQL Exception occured while Viewing.Exception Message: ")).append(e.getMessage()).toString());
        throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
    }
    catch(Exception e)
    {
        logger.error((new StringBuilder("Exception occured while Viewing.Exception Message: ")).append(e.getMessage()).toString());
        throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
    }finally{
 
    try
    {
        
        DBConnection.releaseResources(con, ps, rs);
    }
    catch(Exception e)
    {
        logger.error((new StringBuilder("DAO Exception occured while Viewing.Exception Message: ")).append(e.getMessage()).toString());
        throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
    }
  
    }
    return briefingList;
}

    
    
    public ArrayList view(String circleId, String categoryId, String date)
        throws KmException
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        ArrayList briefingList;
        logger.info("Entered view Briefings for table KM_BRIEFING_MSTR");
        con = null;
        ps = null;
        rs = null;
        briefingList = new ArrayList();
        try
        {
            StringBuffer query = new StringBuffer("SELECT * FROM KM_BRIEFING_MSTR WHERE");
            if(categoryId != null)
            {
                query.append(" CIRCLE_ID=? AND month(DISPLAY_DT) = month(current timestamp) and year(DISPLAY_DT) = year(current timestamp) ");
                query.append(" AND ( CATEGORY_ID  = ? OR CATEGORY_ID IS NULL ) ");
            } else
            {
                query.append(" CIRCLE_ID=? AND month(DISPLAY_DT) = month(current timestamp) and year(DISPLAY_DT) = year(current timestamp) ");
            }
            query.append("order by DISPLAY_DT DESC with ur");
            
            logger.info("Circle ID: "+ circleId + " Date: " + date + " SQL Query:" + query.toString() );
            
            con = DBConnection.getDBConnection();
            ps = con.prepareStatement(query.toString());
            ps.setString(1, circleId);
            //ps.setString(2, (new StringBuilder(String.valueOf(date))).append(" 00:00:00").toString());
            if(categoryId != null)
                ps.setString(2, categoryId);
            rs = ps.executeQuery();
            KmBriefingMstr briefing = null;
            for(; rs.next(); briefingList.add(briefing))
            {
                briefing = new KmBriefingMstr();
                briefing.setBriefingId(rs.getString("BRIEFING_ID"));
                briefing.setBriefingHeading(rs.getString("BRIEFING_HEADING"));
                briefing.setBriefingDetails(rs.getString("BRIEFING_DETAILS"));
                //briefing.setCreatedDt(rs.getString("CREATED_DT").substring(0,19));
                briefing.setCreatedDt(rs.getString("DISPLAY_DT").substring(0,11));
                //System.out.println(  briefing.getCreatedDt());
            }
            logger.debug("Briefing View successful on table:KM_BRIEFING_MSTR.");
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            logger.error((new StringBuilder("SQL Exception occured while Viewing.Exception Message: ")).append(e.getMessage()).toString());
            throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
        }
        catch(Exception e)
        {
            logger.error((new StringBuilder("Exception occured while Viewing.Exception Message: ")).append(e.getMessage()).toString());
            throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }finally{
     
        try
        {
            
            DBConnection.releaseResources(con, ps, rs);
        }
        catch(Exception e)
        {
            logger.error((new StringBuilder("DAO Exception occured while Viewing.Exception Message: ")).append(e.getMessage()).toString());
            throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }
      
        }
        return briefingList;
    }

    public ArrayList editBriefings(String circleId, String fromDate, String endDate, int userId)
        throws KmException
    {
        Connection con;
        PreparedStatement ps=null,ps1=null;
        ResultSet rs=null,rs1=null;
        ArrayList briefingList;
        logger.info("Entered edit Briefings for table KM_BRIEFING_MSTR");
        con = null;
        
        String query = null;
        int km_actor_id = 0;
        query = "select KM_ACTOR_ID from KM_USER_MSTR where USER_ID = ? ";
        ////System.out.println("Inside KmBriefingMstrDaoImpl.editBriefings!!!!");
        try{
        	con = DBConnection.getDBConnection();
        	////System.out.println("Inside KmBriefingMstrDaoImpl.editBriefings!!!!created conn");
        	ps = con.prepareStatement(query);
        	ps.setInt(1,userId);
        	rs = ps.executeQuery();
        	////System.out.println("Inside KmBriefingMstrDaoImpl.editBriefings!!!!executed query");
        	while(rs.next()){
        		km_actor_id = rs.getInt("KM_ACTOR_ID");
        		////System.out.println("Inside KmBriefingMstrDaoImpl.editBriefings!!!!km_actor_id=="+km_actor_id+"circleId=="+circleId);
        	}
        
        }catch(SQLException sqle){
        	sqle.printStackTrace();
        }catch(DAOException daoe){
        	daoe.printStackTrace();
        }
        
        briefingList = new ArrayList();
        int i = 0;
        try
        {
           // con = DBConnection.getDBConnection();
        	System.out.println("kmactorId"+km_actor_id);
            if(km_actor_id == 1){
            	query = "SELECT e.ELEMENT_NAME,e.ELEMENT_LEVEL_ID,b.BRIEFING_ID,b.BRIEFING_HEADING, b.BRIEFING_DETAILS,b.CREATED_DT, b.DISPLAY_DT,b.CATEGORY_ID,ele.ELEMENT_NAME AS CATEGORY_NAME FROM KM_BRIEFING_MSTR b INNER JOIN KM_ELEMENT_MSTR e on e.ELEMENT_ID = b.CIRCLE_ID left join KM_ELEMENT_MSTR ele on ele.ELEMENT_ID = b.CATEGORY_ID WHERE DATE(b.CREATED_DT) >= DATE(CURRENT DATE - 3 DAYS) AND DATE(b.CREATED_DT) <= DATE(CURRENT DATE)  AND    b.CIRCLE_ID=? with ur";
            	ps1 = con.prepareStatement(query);
            	//ps.setString(1, (new StringBuilder(String.valueOf(endDate))).append(" 00:00:00").toString());
            	ps1.setString(1, circleId);
            }else{	
            	query = "SELECT e.ELEMENT_NAME,e.ELEMENT_LEVEL_ID,b.BRIEFING_ID,b.BRIEFING_HEADING, b.BRIEFING_DETAILS,b.CREATED_DT, b.DISPLAY_DT,b.CATEGORY_ID,ele.ELEMENT_NAME AS CATEGORY_NAME FROM KM_BRIEFING_MSTR b INNER JOIN KM_ELEMENT_MSTR e on e.ELEMENT_ID = b.CIRCLE_ID left join KM_ELEMENT_MSTR ele on ele.ELEMENT_ID = b.CATEGORY_ID WHERE DATE(b.DISPLAY_DT) >= DATE(timestamp_format(?, 'YYYY-MM-DD HH24:MI:SS')) AND DATE(b.DISPLAY_DT) <= DATE(timestamp_format(?, 'YYYY-MM-DD HH24:MI:SS'))  AND    b.CIRCLE_ID=? with ur";
            	ps1 = con.prepareStatement(query);
            	ps1.setString(1, (new StringBuilder(String.valueOf(fromDate))).append(" 00:00:00").toString());
            	ps1.setString(2, (new StringBuilder(String.valueOf(endDate))).append(" 00:00:00").toString());
            	ps1.setString(3, circleId);
            }
            rs1 = ps1.executeQuery();
            KmBriefingMstr briefing = null;
            for(; rs1.next(); briefingList.add(briefing))
            {
                briefing = new KmBriefingMstr();
                briefing.setBriefingId(rs1.getString("BRIEFING_ID"));
                briefing.setBriefingHeading(rs1.getString("BRIEFING_HEADING"));
                briefing.setCreatedDt(rs1.getString("CREATED_DT").substring(0, 11));
                briefing.setDisplayDt(rs1.getString("DISPLAY_DT").substring(0, 11));
                briefing.setDisplayDt(rs1.getString("DISPLAY_DT").substring(0, 11));
                if(rs1.getString("CATEGORY_NAME") == null)
                    briefing.setFavCategory("");
                else
                    briefing.setFavCategory(rs1.getString("CATEGORY_NAME"));
            }

            logger.info("Briefing edit on table:KM_BRIEFING_MSTR.");
        }
        catch(SQLException e)
        {
            logger.error((new StringBuilder("SQL Exception occured while Editing Briefings.Exception Message: ")).append(e.getMessage()).toString());
            throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
        }
        catch(Exception e)
        {
            logger.error((new StringBuilder("Exception occured while Editing Briefings.Exception Message: ")).append(e.getMessage()).toString());
            throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }
        finally{
       
        try
        {
           
            DBConnection.releaseResources(con, ps, rs);
            DBConnection.releaseResources(null, ps1, rs1);
        }
        catch(Exception e)
        {
            logger.error((new StringBuilder("DAO Exception occured while Viewing.Exception Message: ")).append(e.getMessage()).toString());
            throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }
 
        }
        return briefingList;
    }

    public KmBriefingMstr updateBriefings(int briefingId)
        throws KmException
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        KmBriefingMstr briefing;
        logger.info("Entered edit Briefings for table KM_BRIEFING_MSTR");
        con = null;
        ps = null;
        rs = null;
        ArrayList briefingList = new ArrayList();
        KmBriefingMstrFormBean formBean = new KmBriefingMstrFormBean();
        briefing = null;
        try
        {
            StringBuffer query = new StringBuffer("SELECT BRIEFING_ID,BRIEFING_HEADING, BRIEFING_DETAILS,CREATED_DT, DISPLAY_DT FROM KM_BRIEFING_MSTR WHERE  BRIEFING_ID=? ");
            con = DBConnection.getDBConnection();
            ps = con.prepareStatement(query.append(" with ur").toString());
            ps.setInt(1, briefingId);
            for(rs = ps.executeQuery(); rs.next(); briefing.setDisplayDt((new StringBuilder()).append(rs.getDate("DISPLAY_DT")).toString()))
            {
                briefing = new KmBriefingMstr();
                briefing.setBriefingId(rs.getString("BRIEFING_ID"));
                briefing.setBriefingHeading(rs.getString("BRIEFING_HEADING"));
                String briefDetails = rs.getString("BRIEFING_DETAILS");
                StringTokenizer stoken = null;
                stoken = new StringTokenizer(briefDetails, "|");
                int count = stoken.countTokens();
                if(count != 0)
                {
                    String arBriefDetails[] = new String[count];
                    for(int i = 0; i < count; i++)
                        arBriefDetails[i] = stoken.nextToken().trim();

                    briefing.setArrBriefDetails(arBriefDetails);
                }
            }

            logger.info("Briefing edit on table:KM_BRIEFING_MSTR.");
        }
        catch(SQLException e)
        {
            logger.error((new StringBuilder("SQL Exception occured while populating the briefings for edit.Exception Message: ")).append(e.getMessage()).toString());
            throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
        }
        catch(Exception e)
        {
            logger.error((new StringBuilder("Exception occured while Viewing populating the briefings for edit. Exception Message: ")).append(e.getMessage()).toString());
            throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }
        finally{
        try
        {
          
            DBConnection.releaseResources(con, ps, rs);
        }
        catch(Exception e)
        {
            logger.error((new StringBuilder("DAO Exception occured while populating the briefings for edit.Exception Message: ")).append(e.getMessage()).toString());
            throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }
        }
    
        
        return briefing;
    }

    public void updateinDbBriefings(String briefingId, String briefHeading, String arrBriefingDetails[], String displayDt)
        throws KmException
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        logger.info("Entered in update in DB Briefings for table KM_BRIEFING_MSTR");
        con = null;
        ps = null;
        rs = null;
        try
        {
            StringBuffer query = new StringBuffer("UPDATE  KM_BRIEFING_MSTR  SET BRIEFING_HEADING=?, BRIEFING_DETAILS=?, DISPLAY_DT= timestamp_format(?, 'YYYY-MM-DD HH24:MI:SS') WHERE  BRIEFING_ID=? ");
            con = DBConnection.getDBConnection();
            ps = con.prepareStatement(query.append(" with ur").toString());
            ps.setString(1, briefHeading);
            String briefingDetails = "";
            for(int i = 0; i < arrBriefingDetails.length; i++)
                briefingDetails = (new StringBuilder(String.valueOf(briefingDetails))).append("|").append(arrBriefingDetails[i]).toString();

            briefingDetails = briefingDetails.substring(1, briefingDetails.length());
            ps.setString(2, briefingDetails);
            ps.setString(3, (new StringBuilder(String.valueOf(displayDt))).append(" 00:00:00").toString());
            ps.setInt(4, Integer.parseInt(briefingId));
            int result = ps.executeUpdate();
            logger.info("Briefing update on table:KM_BRIEFING_MSTR.");
        }
        catch(SQLException e)
        {
            logger.error((new StringBuilder("SQL Exception occured while update.Exception Message: ")).append(e.getMessage()).toString());
            throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
        }
        catch(Exception e)
        {
            logger.error((new StringBuilder("Exception occured while Update.Exception Message: ")).append(e.getMessage()).toString());
            throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }
        finally{
        try
        {
           
            DBConnection.releaseResources(con, ps, rs);
        }
        catch(Exception e)
        {
            logger.error((new StringBuilder("DAO Exception occured while Update.Exception Message: ")).append(e.getMessage()).toString());
            throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }
     
       
        }
    }

    private static final Logger logger = Logger.getLogger(KmBriefingMstrDaoImpl.class);
    protected static final String SQL_INSERT = "INSERT INTO KM_BRIEFING_MSTR (BRIEFING_ID, BRIEFING_HEADING, BRIEFING_DETAILS, CIRCLE_ID, CREATED_BY, CREATED_DT, DISPLAY_DT,CATEGORY_ID) VALUES ( NEXTVAL FOR KM_BRIEFING_ID, ?, ?, ?, ?,  current timestamp, timestamp_format(?, 'YYYY-MM-DD HH24:MI:SS'),?)";
    protected static final String SQL_VIEW_BRIEFINGS = "SELECT * FROM KM_BRIEFING_MSTR WHERE CIRCLE_ID=? AND DATE(DISPLAY_DT)= DATE(timestamp_format(?, 'YYYY-MM-DD HH24:MI:SS')) ";
    protected static final String SQL_EDIT_BRIEFINGS = "SELECT e.ELEMENT_NAME,e.ELEMENT_LEVEL_ID,b.BRIEFING_ID,b.BRIEFING_HEADING, b.BRIEFING_DETAILS,b.CREATED_DT, b.DISPLAY_DT,b.CATEGORY_ID,ele.ELEMENT_NAME AS CATEGORY_NAME FROM KM_BRIEFING_MSTR b INNER JOIN KM_ELEMENT_MSTR e on e.ELEMENT_ID = b.CIRCLE_ID left join KM_ELEMENT_MSTR ele on ele.ELEMENT_ID = b.CATEGORY_ID WHERE DATE(b.DISPLAY_DT) >= DATE(timestamp_format(?, 'YYYY-MM-DD HH24:MI:SS')) AND DATE(b.DISPLAY_DT) <= DATE(timestamp_format(?, 'YYYY-MM-DD HH24:MI:SS'))  AND    b.CIRCLE_ID=? with ur";
    protected static final String SQL_SELECT_BRIEFINGS = "SELECT BRIEFING_ID,BRIEFING_HEADING, BRIEFING_DETAILS,CREATED_DT, DISPLAY_DT FROM KM_BRIEFING_MSTR WHERE  BRIEFING_ID=? ";
    protected static final String SQL_UPDATE_BRIEFINGS = "UPDATE  KM_BRIEFING_MSTR  SET BRIEFING_HEADING=?, BRIEFING_DETAILS=?, DISPLAY_DT= timestamp_format(?, 'YYYY-MM-DD HH24:MI:SS') WHERE  BRIEFING_ID=? ";

    

    
    // Added By Bhaskar
	public int insertQuestion(KmBriefingMstrFormBean formBean) {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        logger.info("Entered in insertquestions method  for table KM_QUESTION_BANK");
        con = null;
        ps = null;
        rs = null;
        int result=0;
        try
        {
        	StringBuffer query = new StringBuffer("INSERT INTO KM_QUESTION_BANK(QUESTION, QUESTION_TYPE, FIRST_ANSWER, SECOND_ANSWER, THIRD_ANSWER, FOURTH_ANSWER, CORRECT_ANSWER, NO_OF_CORRECT,ELEMENT_ID,CREATED_BY, CREATION_DATE)  VALUES(?, ?, ?, ?, ?, ?, ?, ?,?,?, CURRENT TIMESTAMP)");
            con = DBConnection.getDBConnection();
            ps = con.prepareStatement(query.append(" with ur").toString());
            ps.setString(1, formBean.getQuestion());
            
            if(formBean.getNoofCorrect() == 1)
            {
            ps.setString(2, "Single Choice");
            }
            if(formBean.getNoofCorrect() > 1)
            {
            ps.setString(2, "Multiple Choice");
            }
            ps.setString(3, formBean.getFirstAnswer());
            ps.setString(4, formBean.getSecondAnswer());
            ps.setString(5, formBean.getThirdAnswer());
            ps.setString(6, formBean.getFourthAnswer());
            ps.setString(7, formBean.getCorrectAnswer());
            ps.setInt(8, formBean.getNoofCorrect());
            ps.setInt(9,formBean.getElementId());
            ps.setString(10, formBean.getUser_login_id());
            result = ps.executeUpdate();
            
            logger.info("Briefing update on table:KM_BRIEFING_MSTR.");
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
           
            DBConnection.releaseResources(con, ps, rs);
        }
        catch(Exception e)
        {
            logger.error((new StringBuilder("DAO Exception occured while Update.Exception Message: ")).append(e.getMessage()).toString());
            //throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }
     
       
        }
		return result;
    }

	
//Added BY BHaskar
protected static final String KM_QUESTION_BANK_LIST="select * from KM_QUESTION_BANK where date(CREATION_DATE)=current date and element_id=? with ur";
		
	public ArrayList<CSRQuestionDto> getQuestions(KmUserMstr sessionUserBean) throws KmException {
		Connection conn=null;
		PreparedStatement pst=null;
		ResultSet rs = null;
		CSRQuestionDto dto=null;
		ArrayList<CSRQuestionDto> questionsList=new ArrayList<CSRQuestionDto>();
		
		try {
			conn=DBConnection.getDBConnection();
			
			pst = conn.prepareStatement(KM_QUESTION_BANK_LIST);
			pst.setInt(1, Integer.parseInt(sessionUserBean.getElementId()));
			rs = pst.executeQuery();
			while(rs.next())
			{
			
				dto =new CSRQuestionDto();
				dto.setQuestionId(rs.getInt("ID"));
				dto.setQuestion(rs.getString("QUESTION"));
				dto.setFirstAnswer(rs.getString("FIRST_ANSWER"));
				dto.setSecondAnswer(rs.getString("SECOND_ANSWER"));
				dto.setThirdAnswer(rs.getString("THIRD_ANSWER"));
				dto.setFourthAnswer(rs.getString("FOURTH_ANSWER"));
				questionsList.add(dto);
				
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
		return  questionsList ;
		 
	}

	@Override
	public int insertQuizResult(KmBriefingMstrFormBean formBean)
			throws KmException {
        Connection con;
        PreparedStatement ps,ps1;
        ResultSet rs,rs1;
        logger.info("Entered in insertquestions method  for table KM_QUIZ_RESULT");
        con = null;
        ps = null;
        ps1=null;
        rs = null;
        rs1=null;
        int result=0;
        String answer=null;
        try
        {

        	StringBuffer query1=new StringBuffer("SELECT CORRECT_ANSWER FROM KM_QUESTION_BANK where ID=? ");
            StringBuffer query = new StringBuffer("INSERT INTO KM_QUIZ_RESULT(QUESTION_ID,QUESTION_NAME,FIRST_ANSWER,SECOND_ANSWER,THIRD_ANSWER,FOURTH_ANSWER, ANSWER, DATE_OF_QUIZ, USER_ID, STATUS, UD_ID, CIRCLE_ID,ELEMENT_ID)  VALUES(?, ?,?,?,?,?,?, CURRENT TIMESTAMP, ?, ?, ?, ?,? )");
            con = DBConnection.getDBConnection();
            ps = con.prepareStatement(query.append(" with ur").toString());   
            ps1=con.prepareStatement(query1.append(" with ur").toString());
            ps1.setInt(1,formBean.getQuestionId());
            rs1=ps1.executeQuery();
            while (rs1.next())
            {
            	answer=rs1.getString("CORRECT_ANSWER");
            }
           
            ps.setInt(1, formBean.getQuestionId());
            ps.setString(2, formBean.getQuestion());
            ps.setString(3, formBean.getFirstAnswer());
            ps.setString(4, formBean.getSecondAnswer());
            ps.setString(5, formBean.getThirdAnswer());
            ps.setString(6, formBean.getFourthAnswer());
            
            ps.setString(7, formBean.getAnswer());
            ps.setString(8, formBean.getUser_login_id());
            if(answer.equalsIgnoreCase(formBean.getAnswer()))
            {
            	ps.setString(9, "C");	
            }
            else
            {
            	ps.setString(9, "W");
            }
          if(formBean.getUdId().length()>0){
        	  ps.setString(10, formBean.getUdId()); 
          }
          else
          {
        	  ps.setString(10, "");  
          }
            
            ps.setString(11, formBean.getCircle_Id());
            ps.setInt(12, formBean.getElementId());

            result = ps.executeUpdate();
            
            
            logger.info("Briefing update on KM_QUIZ_RESULT.");
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
           
            DBConnection.releaseResources(con, ps, rs);
            DBConnection.releaseResources(null, ps1, rs1);
        }
        catch(Exception e)
        {
            logger.error((new StringBuilder("DAO Exception occured while Update.Exception Message: ")).append(e.getMessage()).toString());
            //throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }
     
       
        }
		return result;
    }

	@Override
	public int insertSkipQuizResult(KmBriefingMstrFormBean formBean)
			throws KmException {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        logger.info("Entered in insertquestions method  for table KM_QUIZ_RESULT");
        con = null;
        ps = null;
       
        rs = null;
       
        int result=0;
        String answer=null;
        try
        {
        	    StringBuffer query = new StringBuffer("INSERT INTO KM_QUIZ_RESULT(QUESTION_ID,QUESTION_NAME,FIRST_ANSWER,SECOND_ANSWER,THIRD_ANSWER,FOURTH_ANSWER, ANSWER, DATE_OF_QUIZ, USER_ID, STATUS, UD_ID, CIRCLE_ID,ELEMENT_ID)  VALUES(?, ?,?,?,?,?,?, CURRENT TIMESTAMP, ?, ?, ?, ?,? )");
            con = DBConnection.getDBConnection();
            ps = con.prepareStatement(query.append(" with ur").toString());   
            ps.setInt(1, formBean.getQuestionId());
            ps.setString(2, formBean.getQuestion());
            ps.setString(3, formBean.getFirstAnswer());
            ps.setString(4, formBean.getSecondAnswer());
            ps.setString(5, formBean.getThirdAnswer());
            ps.setString(6, formBean.getFourthAnswer());
            
            ps.setString(7, "");
            ps.setString(8, formBean.getUser_login_id());
            ps.setString(9, "S");
            if(formBean.getUdId().length()>0)
            {
            	ps.setString(10, formBean.getUdId());
            }
            else
            {
            	ps.setString(10, "");
            }
            
            ps.setString(11, formBean.getCircle_Id());
            ps.setInt(12, formBean.getElementId());

            result = ps.executeUpdate();
              logger.info("Briefing update on KM_QUIZ_RESULT.");
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
           
            DBConnection.releaseResources(con, ps, rs);
           
        }
        catch(Exception e)
        {
            logger.error((new StringBuilder("DAO Exception occured while Update.Exception Message: ")).append(e.getMessage()).toString());
            //throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }
     
       
        }
		return result;
    }

	@Override
	public int getQuizResult(KmUserMstr sessionUserBean) throws KmException {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        logger.info("Entered in get questions method  for table KM_QUIZ_RESULT");
        con = null;
        ps = null;
      
        rs = null;
     
        int result=0;
        
       String user_id=sessionUserBean.getUserLoginId();
        try
        {
        	    StringBuffer query = new StringBuffer("SELECT COUNT(*) AS SIZE FROM KM_QUIZ_RESULT WHERE USER_ID='"+user_id+"' AND DATE(DATE_OF_QUIZ)=CURRENT DATE WITH UR");
            con = DBConnection.getDBConnection();
            ps = con.prepareStatement(query.toString());   

           rs= ps.executeQuery();
           while (rs.next())
           {
        	   result=rs.getInt("SIZE");
        	   
           }
            
            logger.info("Briefing update on KM_QUIZ_RESULT.");
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
           
            DBConnection.releaseResources(con, ps, rs);
           
        }
        catch(Exception e)
        {
            logger.error((new StringBuilder("DAO Exception occured while Update.Exception Message: ")).append(e.getMessage()).toString());
            //throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }
     
       
        }
		return result;
    }

	@Override
	public int getQuestionsSize(KmBriefingMstrFormBean formBean) throws KmException {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        logger.info("Entered in get questions method  for table KM_QUIZ_RESULT");
        con = null;
        ps = null;
       
        rs = null;
      
        int questionsSize=0;
        
       
        try
        {

    	    StringBuffer query = new StringBuffer("SELECT COUNT(*) AS SIZE FROM KM_QUESTION_BANK WHERE  DATE(CREATION_DATE)=CURRENT DATE AND ELEMENT_ID=? WITH UR");
        con = DBConnection.getDBConnection();
        ps = con.prepareStatement(query.toString());   
       ps.setInt(1, formBean.getElementId());
       rs= ps.executeQuery();
       while (rs.next())
       {
    	   questionsSize=rs.getInt("SIZE");
    	   
       }
        
        logger.info("Briefing update on KM_QUIZ_RESULT.");
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
           
            DBConnection.releaseResources(con, ps, rs);
        }
        catch(Exception e)
        {
            logger.error((new StringBuilder("DAO Exception occured while Update.Exception Message: ")).append(e.getMessage()).toString());
            //throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }
     
       
        }
		return questionsSize;
    }

	@Override
	public ArrayList<CSRQuestionDto> getSkipQuestions(KmUserMstr sessionUserBean) throws KmException {
		Connection conn=null;
		PreparedStatement pst1=null;
		ResultSet rs1=null;
		CSRQuestionDto dto=null;
		ArrayList<CSRQuestionDto> questionsList=new ArrayList<CSRQuestionDto>();
		int questionId=0;
		String userId=sessionUserBean.getUserLoginId();
		String elementId=sessionUserBean.getElementId();
		try {
			
			String query="SELECT QUESTION_ID,QUESTION_NAME,FIRST_ANSWER,SECOND_ANSWER,THIRD_ANSWER,FOURTH_ANSWER from KM_QUIZ_RESULT WHERE STATUS='S' AND USER_ID='"+userId+"' AND ELEMENT_ID='"+elementId+"' AND DATE(DATE_OF_QUIZ)=CURRENT DATE  WITH UR";
			//String query1="SELECT * from KM_QUESTION_BANK WHERE ID=? AND DATE(CREATION_DATE)=CURRENT DATE WITH UR";
			
			conn=DBConnection.getDBConnection();
			
			pst1 = conn.prepareStatement(query);
			
			rs1 = pst1.executeQuery();
			while(rs1.next())
			{
				dto =new CSRQuestionDto();
				dto.setQuestionId(rs1.getInt("QUESTION_ID"));
				dto.setQuestion(rs1.getString("QUESTION_NAME"));
				dto.setFirstAnswer(rs1.getString("FIRST_ANSWER"));
				dto.setSecondAnswer(rs1.getString("SECOND_ANSWER"));
				dto.setThirdAnswer(rs1.getString("THIRD_ANSWER"));
				dto.setFourthAnswer(rs1.getString("FOURTH_ANSWER"));
				questionsList.add(dto);
				
			}
			
		} catch (SQLException e) {
			throw new KmException("SQLException: " + e.getMessage(), e);
		} catch (Exception e) {
				logger.error("Exception occured while getTopDocuments. Exception Message: " + e.getMessage());
			throw new KmException("Exception: " + e.getMessage(), e);
		} finally {
			try
			{
				
				DBConnection.releaseResources(null, pst1, rs1);
			}
			catch(DAOException e)
			{
				logger.error(e);
				throw new KmException(e.getMessage(),e);
			}
		}
		return  questionsList ;
		 
	}

	@Override
	public int insertSkipQuestions(KmBriefingMstrFormBean formBean)
			throws KmException {
        Connection con;
        PreparedStatement ps,ps1;
        ResultSet rs,rs1;
        logger.info("Entered in insertquestions method  for table KM_QUIZ_RESULT");
        con = null;
        ps = null;
        ps1=null;
        rs = null;
        rs1=null;
        int result=0;
        String answer=null;
        try
        {
        	
        	StringBuffer query1=new StringBuffer("SELECT CORRECT_ANSWER FROM KM_QUESTION_BANK where ID=? ");
            StringBuffer query = new StringBuffer("  update KM_QUIZ_RESULT set STATUS=?, ANSWER=? where QUESTION_ID=? and  USER_ID=? and CIRCLE_ID=? and DATE(DATE_OF_QUIZ)=current date ");
            con = DBConnection.getDBConnection();
            ps = con.prepareStatement(query.append(" with ur").toString());   
            ps1=con.prepareStatement(query1.append(" with ur").toString());
            ps1.setInt(1,formBean.getQuestionId());
            rs1=ps1.executeQuery();
            while (rs1.next())
            {
            	answer=rs1.getString("CORRECT_ANSWER");
            	
            }
            
            if(answer.equalsIgnoreCase(formBean.getAnswer()))
            {
            	ps.setString(1, "C");	
            }
            else
            {
            	ps.setString(1, "W");
            }
            ps.setString(2, formBean.getAnswer());
            ps.setInt(3,formBean.getQuestionId());
            ps.setString(4, formBean.getUser_login_id());
            ps.setString(5, formBean.getCircle_Id());
            
            result = ps.executeUpdate();
             logger.info("Briefing update on KM_QUIZ_RESULT.");
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
           
            DBConnection.releaseResources(con, ps, rs);
            DBConnection.releaseResources(null, ps1, rs1);
        }
        catch(Exception e)
        {
            logger.error((new StringBuilder("DAO Exception occured while Update.Exception Message: ")).append(e.getMessage()).toString());
            //throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }
     
       
        }
		return result;
    }

	
	@Override
	public int getskipQuesize(KmUserMstr sessionUserBean) throws KmException {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        logger.info("Entered in get questions method  for table KM_QUIZ_RESULT");
        con = null;
        ps = null;
       
        rs = null;
      
        int skipResult=0;
        CSRQuestionDto dto=null;
        ArrayList<CSRQuestionDto> questionsList=new ArrayList<CSRQuestionDto>();
       String user_id=sessionUserBean.getUserLoginId();
       String elementId=sessionUserBean.getElementId();
        try
        {
        	    StringBuffer query = new StringBuffer("SELECT COUNT(*) AS SIZE FROM KM_QUIZ_RESULT WHERE USER_ID='"+user_id+"' AND ELEMENT_ID='"+elementId+"' AND DATE(DATE_OF_QUIZ)=CURRENT DATE AND STATUS='S' WITH UR");
            con = DBConnection.getDBConnection();
            ps = con.prepareStatement(query.toString());   

           rs= ps.executeQuery();
           while (rs.next())
           {
        	   skipResult=rs.getInt("SIZE"); 
           }
            
            logger.info("Briefing update on KM_QUIZ_RESULT.");
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
           
            DBConnection.releaseResources(con, ps, rs);
           
        }
        catch(Exception e)
        {
            logger.error((new StringBuilder("DAO Exception occured while Update.Exception Message: ")).append(e.getMessage()).toString());
            //throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }
     
        }
        return skipResult;
        
        }

	@Override
	public ArrayList<LobDTO> getLobList() throws KmException {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        con = null;
        ps = null;
        rs = null;
     
        LobDTO dto=null;
		ArrayList<LobDTO> lobList=new ArrayList<LobDTO>();
       
        try
        {
        	
        	StringBuffer query=new StringBuffer("select ELEMENT_ID,ELEMENT_NAME from KM_ELEMENT_MSTR where PARENT_ID=1 and STATUS='A' and ELEMENT_LEVEL_ID=2  ");
           
            con = DBConnection.getDBConnection();
            ps = con.prepareStatement(query.append(" with ur").toString());   
             rs=ps.executeQuery();
             
            while (rs.next())
            {
            	dto=new LobDTO();
            	dto.setElementId(rs.getInt("ELEMENT_ID"));
            	dto.setElementName(rs.getString("ELEMENT_NAME"));
            	lobList.add(dto);
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
           
            DBConnection.releaseResources(con, ps, rs);
        }
        catch(Exception e)
        {
            logger.error((new StringBuilder("DAO Exception occured while Update.Exception Message: ")).append(e.getMessage()).toString());
            //throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }
     
       
        }
		return lobList;
    }

	@Override
	public int getQuestionResultSize(KmUserMstr sessionUserBean) throws KmException {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        logger.info("Entered in get questions method  for table KM_QUIZ_RESULT");
        con = null;
        ps = null;
       
        rs = null;
   
        int questionResult=0;
        String elementId=sessionUserBean.getElementId();
        String userId=sessionUserBean.getUserLoginId();
        
       
        try
        {

    	StringBuffer query = new StringBuffer("select count(*) as SIZE from KM_QUIZ_RESULT where date(DATE_OF_QUIZ)=current date and STATUS not in ('C','W') and ELEMENT_ID=? and USER_ID=? with ur ");
        con = DBConnection.getDBConnection();
        ps = con.prepareStatement(query.toString());  
        ps.setInt(1,Integer.parseInt(sessionUserBean.getElementId()));
        ps.setString(2,sessionUserBean.getUserLoginId());
       
       rs= ps.executeQuery();
       while (rs.next())
       {
    	   questionResult=rs.getInt("SIZE");
    	   
       }
        
        logger.info("Briefing update on KM_QUIZ_RESULT.");
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
           
            DBConnection.releaseResources(con, ps, rs);
           
        }
        catch(Exception e)
        {
            logger.error((new StringBuilder("DAO Exception occured while Update.Exception Message: ")).append(e.getMessage()).toString());
            //throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }
     
       
        }
		return questionResult;
    }


	public String  getNewQuestions(KmBriefingMstrFormBean formBean) throws KmException {
		Connection conn=null;
		PreparedStatement pst=null;
		ResultSet rs = null;
		CSRQuestionDto dto=null;
		ArrayList<CSRQuestionDto> questionsList=new ArrayList<CSRQuestionDto>();
		String answer="";
		
		String query="SELECT ANSWER FROM KM_QUIZ_RESULT WHERE QUESTION_ID=? AND DATE(DATE_OF_QUIZ)=CURRENT DATE AND ELEMENT_ID=? AND USER_ID=?  WITH UR";
		try {
			conn=DBConnection.getDBConnection();
			
			pst = conn.prepareStatement(query);
			pst.setInt(1,formBean.getQuestionId());
			pst.setInt(2,formBean.getElementId());
			pst.setString(3,formBean.getUser_login_id());
			rs = pst.executeQuery();
			while(rs.next())
			{
				answer=rs.getString("ANSWER");
				//System.out.println("answer"+answer);
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
		return  answer ;
		 
	}

	
}
