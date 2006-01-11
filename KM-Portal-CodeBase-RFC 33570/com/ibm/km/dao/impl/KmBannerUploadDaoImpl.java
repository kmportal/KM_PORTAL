package com.ibm.km.dao.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

import com.ibm.km.actions.KmAddNewFileAction;
import com.ibm.km.dao.DBConnection;
import com.ibm.km.dao.KmBannerUploadDao;
import com.ibm.km.exception.ActivateUserAccountException;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;

public class KmBannerUploadDaoImpl implements KmBannerUploadDao{
	

	private static Logger logger = Logger.getLogger(KmBannerUploadDaoImpl.class);

	
	public int uploadBanner(byte[] data,String fileName, String bannerPage) throws KmException,DAOException{
		
		//System.out.println("Inside dao impl uploadBanner() method!!!!");
		int result = 0;
		PreparedStatement pstmt = null;
		
		String insert_query = "insert into KM_IMAGE_MSTR values(?,?,?,?,?)";
		String update_query = "update KM_IMAGE_MSTR set FILE_NAME=?,IMAGE=?,TIME_STAMP=? where BANNER_PAGE=?";
		
		Connection conn = DBConnection.getDBConnection();
		boolean exists= checkForBannerImageInDB(bannerPage);

		Date d = new Date();
		
		try{
			if(exists){
				pstmt = conn.prepareStatement(update_query);
				pstmt.setString(1,fileName);
				pstmt.setObject(2,data);
				pstmt.setTimestamp(3,new Timestamp(d.getTime()));
				pstmt.setString(4,bannerPage);
			}else{
				
				pstmt = conn.prepareStatement(insert_query);
				pstmt.setString(1,fileName);
				pstmt.setObject(2, data);
				pstmt.setString(3,"");
				pstmt.setTimestamp(4, new Timestamp(d.getTime()));
				pstmt.setString(5,bannerPage);
			}
			result = pstmt.executeUpdate();
		}catch(SQLException sqle){
			sqle.printStackTrace();
			throw new KmException(
					"Exception while trying to upload banner ", sqle);
		
		}
		finally
		{
			try
			{
				DBConnection.releaseResources(conn,pstmt,null);
			}
			catch(DAOException e)
			{
				logger.error("DAOException occured while uploadBanner" + "Exception Message: " + e.getMessage());
				throw new KmException(e.getMessage(),e);
			}
		}	
		return result;
	}
	
	
	public InputStream retrieveBanner(String bannerPage) throws KmException,DAOException{
		
		//System.out.println("Inside dao impl retrieveBanner() method!!!!");
		
		String query = "select * from KM_IMAGE_MSTR where BANNER_PAGE=? order by Time_Stamp desc FETCH FIRST 1 ROWS ONLY with ur";
		Connection conn = DBConnection.getDBConnection();
		PreparedStatement pstmt = null;
		//Blob b = new Blob(data);
		Date d = new Date();
		ResultSet rs = null;
		InputStream inputStream = null;
		try{
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1,bannerPage);
			//pstmt.setTimestamp(4, new Timestamp(d.getTime()));
			rs = pstmt.executeQuery();
			
			while (rs.next()) { 
				// materialization of the Blob 
				String fileName = rs.getString("FILE_NAME");
				Blob blob = rs.getBlob("IMAGE"); 
				inputStream = blob.getBinaryStream(); 
			}
			
			
		
		}catch(SQLException sqle){
			sqle.printStackTrace();
			throw new KmException(
					"Exception while trying to retrieve banner ", sqle);
		}
		finally
		{
			try
			{
				DBConnection.releaseResources(conn,pstmt,rs);
			}
			catch(DAOException e)
			{
				logger.error("DAOException occured while Approving File" + "Exception Message: " + e.getMessage());
				throw new KmException(e.getMessage(),e);
			}
		}	
		return inputStream;
	}

	private boolean checkForBannerImageInDB(String bannerPage) throws DAOException{
		boolean bannerExists = false;
		Connection conn = DBConnection.getDBConnection();
		//System.out.println("Inside checkForBannerImageInDB method!!!!");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String select_query = "select * from KM_IMAGE_MSTR where BANNER_PAGE=? with ur";
		try{
		pstmt = conn.prepareStatement(select_query);
		pstmt.setString(1,bannerPage);
		rs = pstmt.executeQuery();
		
		if(rs.next()){
			bannerExists = true;
		}
		}catch(SQLException sqle){
			sqle.printStackTrace();
			throw new KmException(
					"Exception while trying to check banner status ", sqle);
		}
		finally
		{
			try
			{
				DBConnection.releaseResources(conn,pstmt,rs);
			}
			catch(DAOException e)
			{
				logger.error("DAOException occured while checkForBannerImageInDB" + "Exception Message: " + e.getMessage());
				throw new KmException(e.getMessage(),e);
			}
		}	
				
		return bannerExists;
		
	}

}
