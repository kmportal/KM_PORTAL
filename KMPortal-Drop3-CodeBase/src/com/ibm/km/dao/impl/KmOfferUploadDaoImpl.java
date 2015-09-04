package com.ibm.km.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.ibm.km.dao.DBConnection;
import com.ibm.km.dao.KmOfferUploadDao;
import com.ibm.km.dto.KmElementMstr;
import com.ibm.km.dto.OfferDetailsDTO;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;
import com.ibm.km.exception.KmUserMstrDaoException;
import com.ibm.km.forms.KmOfferUploadFormBean;

public class KmOfferUploadDaoImpl implements KmOfferUploadDao
{
	private static final Logger logger;
	static {
		logger = Logger.getLogger(KmOfferUploadDaoImpl.class);
	}
	
    public KmOfferUploadDaoImpl()
    {
    }

    public ArrayList<OfferDetailsDTO> getBucketList()throws KmException
    {
    Connection con;
    ResultSet rs;
    PreparedStatement ps;
    ArrayList<OfferDetailsDTO>  bucketList = new ArrayList<OfferDetailsDTO> ();
    con = null;
    rs = null;
    ps = null;
    try
    {
        StringBuffer query = new StringBuffer("select BUCKET_ID, FROM_DAY ||'-'||TO_DAY  AS BUCKET_DESC from KM_BUCKET_MSTR WHERE STATUS = 'A' WITH UR");
        con = DBConnection.getDBConnection();
        ps = con.prepareStatement(query.toString());
        OfferDetailsDTO  bucketDto =null;
        for(rs = ps.executeQuery(); rs.next(); bucketList.add(bucketDto))
        {
        	bucketDto = new OfferDetailsDTO();
        	bucketDto.setBucketId(rs.getInt("BUCKET_ID"));
        	bucketDto.setBucketDesc(rs.getString("BUCKET_DESC"));
        	
        	if(bucketDto.getBucketDesc().contains("-1"))
			{
        		bucketDto.setBucketDesc(" > "+ (Integer.parseInt(bucketDto.getBucketDesc().substring(0,2))-1));
			}
        	
        }

        logger.info((new StringBuilder("List is returned :")).append(bucketList.size()).toString());
    }
    catch(SQLException e)
    {
        e.printStackTrace();
        logger.info(e);
        throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
    }
    catch(Exception e)
    {
        e.printStackTrace();
        logger.info(e);
        throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
    }
   
  finally{
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
    return bucketList;
}
    
    public int insertOffer(KmOfferUploadFormBean kmOfferUploadForm) throws KmException
    {
        Connection con=null;
        PreparedStatement pstmt=null;
        ResultSet rs=null;
        int  offerId = 0;
        try
        {   
            StringBuffer query = new StringBuffer("INSERT INTO KM_OFFER_UPLOAD (BUCKET_ID, OFFER_TITLE, OFFER_DESC, START_DATE, END_DATE ,STATUS,CREATED_BY) VALUES(?,?,?,?,?,'A',?)");
            con = DBConnection.getDBConnection();            
            pstmt = con.prepareStatement(query.toString());
            pstmt.setInt(1, Integer.parseInt(kmOfferUploadForm.getBucketId()));
            pstmt.setString(2, kmOfferUploadForm.getOfferTitle());
            pstmt.setString(3, kmOfferUploadForm.getOfferDesc());
            pstmt.setString(4, kmOfferUploadForm.getStartDate());
            pstmt.setString(5, kmOfferUploadForm.getEndDate());
            pstmt.setString(6, kmOfferUploadForm.getCreatedBy());
            pstmt.executeUpdate();
                           
            pstmt = con.prepareStatement("SELECT MAX(OFFER_ID) as OFFER_ID FROM KM_OFFER_UPLOAD WITH UR ");
            rs = pstmt.executeQuery();
            while(rs.next())
            {
            	offerId = rs.getInt("OFFER_ID");
            }
        }
        catch(KmUserMstrDaoException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }
        catch(Exception e1)
        {
            e1.printStackTrace();
            throw new KmException(e1.getMessage(), e1);
        }
        finally{
        try
        {
            DBConnection.releaseResources(con, pstmt, rs);
        }
        catch(DAOException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }
        }
        return offerId;      
    }

    public int updateOffer(KmOfferUploadFormBean kmOfferUploadForm) throws KmException
    {
        Connection con=null;
        PreparedStatement pstmt=null;
        ResultSet rs=null;
        int  offerId = 0;
        try
        {   
            StringBuffer query = new StringBuffer("update KM_OFFER_UPLOAD set BUCKET_ID = ?, OFFER_TITLE = ?, OFFER_DESC = ?, START_DATE = ? , END_DATE = ?  where OFFER_ID = ? AND STATUS = 'A' WITH UR");
            con = DBConnection.getDBConnection();            
            pstmt = con.prepareStatement(query.toString());
            pstmt.setInt(1, Integer.parseInt(kmOfferUploadForm.getBucketId()));
            pstmt.setString(2, kmOfferUploadForm.getOfferTitle());
            pstmt.setString(3, kmOfferUploadForm.getOfferDesc());
            pstmt.setString(4, kmOfferUploadForm.getStartDate());
            pstmt.setString(5, kmOfferUploadForm.getEndDate());
            pstmt.setInt(6, kmOfferUploadForm.getOfferId());
            
            pstmt.executeUpdate();
            offerId = kmOfferUploadForm.getOfferId();
            logger.info(kmOfferUploadForm.getOfferTitle()+" : Offer id "+kmOfferUploadForm.getOfferId()+" updated.");  
            
        }
        catch(KmUserMstrDaoException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }
        catch(Exception e1)
        {
            e1.printStackTrace();
            throw new KmException(e1.getMessage(), e1);
        }
        finally{
        try
        {
            DBConnection.releaseResources(con, pstmt, rs);
        }
        catch(DAOException e)
        {
            e.printStackTrace();
            throw new KmException(e.getMessage(), e);
        }
        }
        return offerId;      
    }
    
    public ArrayList<OfferDetailsDTO> getOfferList(int offerId) throws KmException
    {
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        ArrayList<OfferDetailsDTO> offerList = new ArrayList<OfferDetailsDTO>();
        con = null;
        rs = null;
        ps = null;
        try
        {               	    
            StringBuffer query = new StringBuffer("SELECT OU.OFFER_ID AS OFFER_ID, BM.FROM_DAY ||'-'||BM.TO_DAY  AS BUCKET_DESC , OU.BUCKET_ID as BUCKET_ID, OU.OFFER_TITLE as OFFER_TITLE, OU.OFFER_DESC as OFFER_DESC, OU.START_DATE as START_DATE, OU.END_DATE as END_DATE FROM KM_OFFER_UPLOAD OU, KM_BUCKET_MSTR BM WHERE BM.BUCKET_ID = OU.BUCKET_ID AND OU.STATUS = 'A' WITH UR");
            if (offerId > 0)
            {
            	query = new StringBuffer("SELECT OU.OFFER_ID AS OFFER_ID, BM.FROM_DAY ||'-'||BM.TO_DAY  AS BUCKET_DESC , OU.BUCKET_ID as BUCKET_ID, OU.OFFER_TITLE as OFFER_TITLE, OU.OFFER_DESC as OFFER_DESC, OU.START_DATE as START_DATE, OU.END_DATE as END_DATE FROM KM_OFFER_UPLOAD OU, KM_BUCKET_MSTR BM WHERE  OFFER_ID = "+offerId+" AND BM.BUCKET_ID = OU.BUCKET_ID AND OU.STATUS = 'A' WITH UR");
            }
            
            con = DBConnection.getDBConnection();
            ps = con.prepareStatement(query.toString());
            OfferDetailsDTO offerDto = new OfferDetailsDTO();
            for(rs = ps.executeQuery(); rs.next(); )
            {
            	offerDto = new OfferDetailsDTO();
            	offerDto.setOfferId(rs.getInt("OFFER_ID"));
            	offerDto.setBucketId(rs.getInt("BUCKET_ID"));
            	offerDto.setBucketDesc(rs.getString("BUCKET_DESC"));
            	if(offerDto.getBucketDesc().contains("-1"))
    			{
            		offerDto.setBucketDesc(" > "+ (Integer.parseInt(offerDto.getBucketDesc().substring(0,2))-1));
    			}
            	offerDto.setOfferTitle(rs.getString("OFFER_TITLE"));
            	offerDto.setOfferDesc(rs.getString("OFFER_DESC"));
            	offerDto.setStartDate(rs.getDate("START_DATE"));
            	offerDto.setEndDate(rs.getDate("END_DATE"));
            	offerList.add(offerDto);
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            logger.info(e);
            throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            logger.info(e);
            throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }
        finally{
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
        return offerList;
    }

}
