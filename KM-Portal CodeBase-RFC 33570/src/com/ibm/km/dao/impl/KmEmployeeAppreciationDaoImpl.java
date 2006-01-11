package com.ibm.km.dao.impl;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.rowset.serial.SerialBlob;

import org.apache.log4j.Logger;

import com.ibm.km.common.PropertyReader;
import com.ibm.km.common.Utility;
import com.ibm.km.dao.DBConnection;
import com.ibm.km.dao.KmEmployeeAppreciationDao;
import com.ibm.km.dto.EmployeeAppreciationDTO;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.KmEmployeeAppreciationFormBean;

public class KmEmployeeAppreciationDaoImpl implements KmEmployeeAppreciationDao
{
	private static final Logger logger;
	static {
		logger = Logger.getLogger(KmEmployeeAppreciationDaoImpl.class);
	}
	
    public KmEmployeeAppreciationDaoImpl()
    {
    }

    public int insertAppreciation(KmEmployeeAppreciationFormBean kmEmployeeAppreciationForm) throws KmException
    {
        Connection con=null;
        PreparedStatement pstmt=null;
        ResultSet rs=null;
        int[] insertedstatus;
        int  insertedRecordNos=0;
        try
        { 
            StringBuffer query = new StringBuffer("INSERT INTO KM_EMP_APPRECIATION (EMP_NAME,HEADER_DESC,CONTENT,EMP_IMAGE,IMAGE_NAME,STATUS,CREATED_BY) VALUES(?,?,?,?,?,'A',?)");
            con = DBConnection.getDBConnection();            
            pstmt = con.prepareStatement(query.toString());
            EmployeeAppreciationDTO employeeAppreciationDTO=new EmployeeAppreciationDTO();
           //System.out.println("kmEmployeeAppreciationForm.getEmployeeAppreciationList().size() "+kmEmployeeAppreciationForm.getEmployeeAppreciationList().size());;
            
          	for( int i=0; i<kmEmployeeAppreciationForm.getEmployeeAppreciationList().size(); i++ )
			{
          		employeeAppreciationDTO = kmEmployeeAppreciationForm.getEmployeeAppreciationList().get(i);
          		
          		if(employeeAppreciationDTO.getEmployeeName().trim().length() > 0 )
          		{
          			Blob empImage = null;
	          		pstmt.setString(1, employeeAppreciationDTO.getEmployeeName());
					pstmt.setString(2, Utility.encodeContent(employeeAppreciationDTO.getAppreciationHeader()));
					pstmt.setString(3, Utility.encodeContent(employeeAppreciationDTO.getAppreciationContent()));
					
					if(null != employeeAppreciationDTO.getEmployeeImage())
					{
						String empImageName = employeeAppreciationDTO.getEmployeeImage().getFileName();
						int empImageSize = employeeAppreciationDTO.getEmployeeImage().getFileSize();
						
						//System.out.println(empImageName + " : " +empImageSize);
						
						boolean imageTobeUplopaded = true;
						if (  !empImageName.toUpperCase().contains(".JPG"))
							{
								if ( !empImageName.toUpperCase().contains(".GIF") )
								{
									imageTobeUplopaded = false;
								}
						}						
						if ( empImageSize > 102400 )
						{
							imageTobeUplopaded = false;
						}
						if(imageTobeUplopaded)
						{					
						empImage = new SerialBlob(employeeAppreciationDTO.getEmployeeImage().getFileData());
						}
					}
					pstmt.setBlob(4, empImage);
					pstmt.setString(5, employeeAppreciationDTO.getEmployeeImage().getFileName());
					pstmt.setInt(6, Integer.parseInt(kmEmployeeAppreciationForm.getCreatedBy()));
				    pstmt.addBatch();
				   
          		}
			}
          	insertedstatus = pstmt.executeBatch();
		    
		    for( int i=0; i<insertedstatus.length; i++ )
			{
		    	if(insertedstatus[i]>=0)
		    	{
		    		++insertedRecordNos;
		    	}
			}
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            logger.info("Root cause :"+e.getNextException());
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
	            DBConnection.releaseResources(con, pstmt, rs);
	        }
	        catch(DAOException e)
	        {
	        	 e.printStackTrace();
	             logger.info(e);
	             throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
	        }
        }
        return insertedRecordNos;      
    }

    public ArrayList<EmployeeAppreciationDTO> getEmployeeAppreciationList()
        throws KmException
    {
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        ArrayList<EmployeeAppreciationDTO> employeeAppreciationList = new ArrayList();
        con = null;
        rs = null;
        ps = null;
        try
        {
        	String appreciationMaxDisplayDays = PropertyReader.getAppValue("appreciation.max.display.days");
            StringBuffer query = new StringBuffer("SELECT APPRECIATION_ID, EMP_NAME, HEADER_DESC,CONTENT,EMP_IMAGE, IMAGE_NAME FROM KM_EMP_APPRECIATION WHERE CREATED_DT > (CURRENT DATE - "+appreciationMaxDisplayDays+" days) and STATUS = 'A'  ORDER BY CREATED_DT DESC WITH UR");
            con = DBConnection.getDBConnection();
            ps = con.prepareStatement(query.toString());
            EmployeeAppreciationDTO empAppdto = new EmployeeAppreciationDTO();
            for(rs = ps.executeQuery(); rs.next(); )
            {
            	empAppdto = new EmployeeAppreciationDTO();
            	empAppdto.setEmployeeName(rs.getString("EMP_NAME"));
            	empAppdto.setAppreciationHeader(rs.getString("HEADER_DESC"));
            	empAppdto.setAppreciationContent(rs.getString("CONTENT"));
            	empAppdto.setEmpImageData(rs.getBlob("EMP_IMAGE"));
            	empAppdto.setEmpImageName(rs.getString("IMAGE_NAME"));
            	empAppdto.setAppreciationId(rs.getInt("APPRECIATION_ID"));
            	employeeAppreciationList.add(empAppdto);
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
        return employeeAppreciationList;
    }

	//@Override
	public EmployeeAppreciationDTO getEmployeeAppreciationDetail(int appId)
			throws KmException {
        Connection con;
        EmployeeAppreciationDTO empAppdto = new EmployeeAppreciationDTO();
        ResultSet rs;
        PreparedStatement ps;
        con = null;
        rs = null;
        ps = null;
     	InputStream in=null;
        try
        {
            StringBuffer query = new StringBuffer("SELECT APPRECIATION_ID, EMP_NAME, HEADER_DESC,CONTENT,EMP_IMAGE, IMAGE_NAME FROM KM_EMP_APPRECIATION WHERE APPRECIATION_ID = " + appId + "  WITH UR");
            con = DBConnection.getDBConnection();
            ps = con.prepareStatement(query.toString());
            for(rs = ps.executeQuery(); rs.next(); )
            {
            	empAppdto = new EmployeeAppreciationDTO();
            	empAppdto.setEmployeeName(rs.getString("EMP_NAME"));
            	empAppdto.setAppreciationHeader(rs.getString("HEADER_DESC"));
            	empAppdto.setAppreciationContent(rs.getString("CONTENT"));
            	empAppdto.setEmpImageData(rs.getBlob("EMP_IMAGE"));
            	empAppdto.setEmpImageName(rs.getString("IMAGE_NAME"));
            	empAppdto.setAppreciationId(rs.getInt("APPRECIATION_ID"));
            	if(rs.getBlob("EMP_IMAGE") != null) {
            	in = rs.getBlob("EMP_IMAGE").getBinaryStream();
            	empAppdto.setEmpImageStream(in); 
            	
            	}
            }
        }
        catch(SQLException e)
        {
           	logger.error("Error is:", e);
           // logger.info(e);
            throw new KmException((new StringBuilder("SQLException: ")).append(e.getMessage()).toString(), e);
        }
        catch(Exception e)
        {
           	logger.error("Error is:", e);
            //logger.info(e);
            throw new KmException((new StringBuilder("Exception: ")).append(e.getMessage()).toString(), e);
        }
        finally{
        try
        {
        	in.close();
            DBConnection.releaseResources(con, ps, rs);
        }
        catch(DAOException e)
        {
           	logger.error("Error is:", e);
            throw new KmException(e.getMessage(), e);
        } catch (IOException e) {
        	logger.error("Error is:", e);
        	throw new KmException(e.getMessage(), e);
		
		}
        }
        return empAppdto;
	}

}
