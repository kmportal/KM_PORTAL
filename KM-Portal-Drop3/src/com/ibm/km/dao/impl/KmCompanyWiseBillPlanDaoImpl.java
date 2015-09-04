package com.ibm.km.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.ibm.km.actions.KmElementMstrAction;
import com.ibm.km.dto.KmCompanyWiseBillPlanDTO;
import com.ibm.km.dao.DBConnection;
import com.ibm.km.dao.KmCompanyWiseBillPlanDao;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;
/**
 * @author Anveeksha & Neeraj
 * for waiver matrix uplaod 
 * save the data of uploaded excel to database
 */
public class KmCompanyWiseBillPlanDaoImpl implements KmCompanyWiseBillPlanDao {

	/*
	 * query variables
	 */ 
	public static final String sql_Get_Plan_List = "SELECT BILL_PLAN_NAME as PLANNAME,REMARKS FROM KM_COMPANY_WISE_BILLPLAN INNER JOIN  KM_BILLPLAN_MSTR ON KM_COMPANY_WISE_BILLPLAN.BillPlanID= KM_BILLPLAN_MSTR.BillPlan_ID WHERE COMPANYID= ";
	public static final String sql_Get_Company_Details = "SELECT * FROM KM_COMPANY_MSTR WHERE COMPANY_ID = ?";
	public static final String sql_Get_plan_Rate_Details="SELECT BILL_PLAN_DESC, COMPONENT_ID, COMPONENT_DESC, LOCAL_A2A, LOCAL_A2A_CUG, LOCAL_A2M, LOCAL_A2FL, STD_A2A_1, STD_A2A_2, STD_A2A_3, STD_A2M_1, STD_A2M_2, STD_A2M_3, STD_A2FL_1, STD_A2FL_2, STD_A2FL_3, ISD_ROW1, ISD_GULF, ISD_ROW2 FROM KM_BILLPLAN_RATES WHERE BILL_PLAN_DESC = ? with ur ";	
	public static final String sql_Get_Company_List = "SELECT  COMPANY_ID,case when (PARENT_NAME= '')  then COMPANY_NAME else CASE when COMPANY_NAME='' then PARENT_NAME  else PARENT_NAME concat '-----'	 concat COMPANY_NAME end END  as companylist FROM KM_COMPANY_MSTR where ((COMPANY_NAME<>'' or PARENT_NAME<>'') and (UCASE(COMPANY_NAME) LIKE ? OR UCASE(PARENT_NAME) LIKE ?))  order by companylist";

	
	/*
	 * Logger for the class.
	 */
	private static final Logger logger;
	static {
		logger = Logger.getLogger(KmCompanyWiseBillPlanDaoImpl.class);
	}

	/**
	 * @param str
	 * @return ArrayList for list of companies 
	 * @throws KmException
	 */
	public ArrayList getCompanyList(String str) throws KmException {

		Connection con = null;
		ResultSet rsGetCompanyList =  null;
		PreparedStatement psGetCompanyList = null;
		ArrayList companylist = new ArrayList();
		KmCompanyWiseBillPlanDTO dto=null;
		
		try{
		
			con = DBConnection.getDBConnection();
			con.setAutoCommit(false);	
			
			String valStr = "%"+str.toUpperCase()+"%";
			psGetCompanyList = con.prepareStatement(sql_Get_Company_List);
			psGetCompanyList.setString(1,valStr);
			psGetCompanyList.setString(2,valStr);
			rsGetCompanyList = psGetCompanyList.executeQuery();
			
			while(rsGetCompanyList.next()) 
			{
				dto = new KmCompanyWiseBillPlanDTO();
				dto.setCompanyName(rsGetCompanyList.getString("companylist"));
				dto.setCompanyId(rsGetCompanyList.getString("COMPANY_ID"));
				companylist.add(dto);
			}
			con.commit();
			return companylist;
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			logger.error("SQLException occured while  showing DocumentViews" + "Exception Message: " + e.getMessage());
			throw new KmException("SQL Exception: " + e.getMessage(), e);
		} 
		catch (DAOException e) 
		{
			e.printStackTrace();
			logger.error(" Exception occured while showing DocumentViews." + "Exception Message: " + e.getMessage());
			throw new KmException(" DAOException: " + e.getMessage(), e);
		} 
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(" Exception occured while showing DocumentViews." + "Exception Message: " + e.getMessage());
			throw new KmException(" DAOException: " + e.getMessage(), e);
		}
		finally 
		{
			try 
			{
				DBConnection.releaseResources(con,psGetCompanyList,rsGetCompanyList);
			} 
			catch (Exception e) 
			{
				logger.error("DAO Exception occured while getting company list" + "Exception Message: " + e.getMessage());
				throw new KmException("DAO Exception: " + e.getMessage(), e);
			}
			logger.info("Exit from Create Temp Table method in DAO");
		}
		
	}

	/**
	 * @param id
	 * @return ArrayList for Bill Plans of selected company
	 * @throws KmException
	 */
	public ArrayList getBillPlanList(int id) throws KmException {
		
		Connection con = null;
		ResultSet rs = null;
		ArrayList billPlanList = new ArrayList();
		StringBuffer query=new StringBuffer(sql_Get_Plan_List);
		KmCompanyWiseBillPlanDTO dto=null;
		query.append(id);
		
		try{
			
			con = DBConnection.getDBConnection();
			con.setAutoCommit(false);
			rs = con.prepareStatement(query.toString()).executeQuery();
			
			while (rs.next()) 
			{
				dto = new KmCompanyWiseBillPlanDTO();
				
				if(!(rs.getString("PLANNAME").equals("")))
				{
					dto.setPlanName(rs.getString("PLANNAME"));
					if(rs.getString("REMARKS").equals(""))
					{
						dto.setPlanName("-");
					}
					else
					{
						dto.setRemarks(rs.getString("REMARKS"));
					}
				}
				
				
				billPlanList.add(dto);
			}
			con.commit();
			return billPlanList;
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			logger.error("SQLException occured while  showing DocumentViews" + "Exception Message: " + e.getMessage());
			throw new KmException("SQL Exception: " + e.getMessage(), e);
		} 
		catch (DAOException e) 
		{
			e.printStackTrace();
			logger.error(" Exception occured while showing DocumentViews." + "Exception Message: " + e.getMessage());
			throw new KmException(" DAOException: " + e.getMessage(), e);
		} 
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(" Exception occured while showing DocumentViews." + "Exception Message: " + e.getMessage());
			throw new KmException(" DAOException: " + e.getMessage(), e);
		}
		finally 
		{
			try 
			{
				DBConnection.releaseResources(con,null,rs);
			} 
			catch (Exception e) 
			{
				logger.error("DAO Exception occured while getting bill plan" + "Exception Message: " + e.getMessage());
				throw new KmException("DAO Exception: " + e.getMessage(), e);
			}
			logger.info("Exit from Create Temp Table method in DAO");
		}
	}

	/**
	 * @param id
	 * @return Arraylist for details of company selected
	 * @throws KmException
	 */
	public ArrayList getCompanyDetails(int id) throws KmException {
		PreparedStatement psGetCompanyDetails = null;
		Connection con=null;
		KmCompanyWiseBillPlanDTO companyDetailDTO=null;
		ResultSet rs = null;
		try{
		
			con =  DBConnection.getDBConnection();
			con.setAutoCommit(false);
			
			psGetCompanyDetails=con.prepareStatement(sql_Get_Company_Details);
			psGetCompanyDetails.setInt(1,id);
			rs = psGetCompanyDetails.executeQuery();
			
			ArrayList companyDetails = new ArrayList();
			
			while(rs.next())
			{
				for(int i=2;i<=rs.getMetaData().getColumnCount();i++)
				{
					companyDetailDTO = new KmCompanyWiseBillPlanDTO();
					if(rs.getString(i).equals(""))
					{
						companyDetailDTO.setCompanyDetails("-");
					}
					else
					{
						companyDetailDTO.setCompanyDetails(rs.getString(i));
					}
					
					companyDetails.add(companyDetailDTO);
				}
			}	
			con.commit();
			return companyDetails;
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			logger.error("SQLException occured while  showing DocumentViews" + "Exception Message: " + e.getMessage());
			throw new KmException("SQL Exception: " + e.getMessage(), e);
		} 
		catch (DAOException e) 
		{
			e.printStackTrace();
			logger.error(" Exception occured while showing DocumentViews." + "Exception Message: " + e.getMessage());
			throw new KmException(" DAOException: " + e.getMessage(), e);
		} 
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(" Exception occured while showing DocumentViews." + "Exception Message: " + e.getMessage());
			throw new KmException(" DAOException: " + e.getMessage(), e);
		}
		finally 
		{
			try 
			{
				DBConnection.releaseResources(con,psGetCompanyDetails,rs);
			} 
			catch (Exception e) 
			{
				logger.error("DAO Exception occured while getting company details" + "Exception Message: " + e.getMessage());
				throw new KmException("DAO Exception: " + e.getMessage(), e);
			}
			logger.info("Exit from Create Temp Table method in DAO");
		}
	}
	
	/**
	 * @param plan_name
	 * @return ArrayList for Rate Details fo selected bill plan
	 * @throws KmException
	 */
	public ArrayList getBillPlanRateDetail(String planName ) throws KmException {
		
		ArrayList planDetails=new ArrayList();
		KmCompanyWiseBillPlanDTO planDetailDto=null;
		Connection con = null;
		PreparedStatement psGetPlanDetails = null;
		ResultSet rs = null;
		try{
	
			con =  DBConnection.getDBConnection();
			psGetPlanDetails=con.prepareStatement(sql_Get_plan_Rate_Details);
			psGetPlanDetails.setString(1,planName);
			rs=psGetPlanDetails.executeQuery();

			while(rs.next())
			{
				planDetailDto=new KmCompanyWiseBillPlanDTO();
				planDetailDto.setBillPlanDesc(rs.getString(1));
				planDetailDto.setComponentId(rs.getString(2));
				planDetailDto.setComponentDesc(rs.getString(3));
				planDetailDto.setLocalA2A(rs.getString(4));
				planDetailDto.setLocalA2ACUG(rs.getString(5));
				planDetailDto.setLocalA2M(rs.getString(6));
				planDetailDto.setLocalA2FL(rs.getString(7));
				planDetailDto.setStdA2A1(rs.getString(8));
				planDetailDto.setStdA2A2(rs.getString(9));
				planDetailDto.setStdA2A3(rs.getString(10));
				planDetailDto.setStdA2M1(rs.getString(11));
				planDetailDto.setStdA2M2(rs.getString(12));
				planDetailDto.setStdA2M3(rs.getString(13));
				planDetailDto.setStdA2FL1(rs.getString(14));
				planDetailDto.setStdA2FL2(rs.getString(15));
				planDetailDto.setStdA2FL3(rs.getString(16));
				planDetailDto.setIsdROW1(rs.getString(17));
				planDetailDto.setIsdGulf(rs.getString(18));
				planDetailDto.setIsdROW2(rs.getString(19));
					
				planDetails.add(planDetailDto);
			}
			return planDetails;
	}
		catch (SQLException e) 
		{
			e.printStackTrace();
			logger.error("SQLException occured while  showing DocumentViews" + "Exception Message: " + e.getMessage());
			throw new KmException("SQL Exception: " + e.getMessage(), e);
		} 
		catch (DAOException e) 
		{
			e.printStackTrace();
			logger.error(" Exception occured while showing DocumentViews." + "Exception Message: " + e.getMessage());
			throw new KmException(" DAOException: " + e.getMessage(), e);
		} 
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(" Exception occured while showing DocumentViews." + "Exception Message: " + e.getMessage());
			throw new KmException(" DAOException: " + e.getMessage(), e);
		}
		finally 
		{
			try 
			{
				DBConnection.releaseResources(con,psGetPlanDetails,rs);
			} 
			catch (Exception e) 
			{
				logger.error("DAO Exception occured while getting plan rates" + "Exception Message: " + e.getMessage());
				throw new KmException("DAO Exception: " + e.getMessage(), e);
			}
			logger.info("Exit from Create Temp Table method in DAO");
		}
	}
}
