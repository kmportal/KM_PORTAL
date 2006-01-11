package com.ibm.km.dao;

import java.util.ArrayList;
import com.ibm.km.exception.KmException;
/**
 * @author Anveeksha & Neeraj
 * for waiver matrix uplaod 
 * save the data of uploaded excel to database
 */
public interface KmCompanyWiseBillPlanDao {
	
	/**
	 * @param str
	 * @return ArrayList for list of companies 
	 * @throws KmException
	 */
	public ArrayList getCompanyList(String str) throws KmException;
	
	/**
	 * @param id
	 * @return ArrayList for Bill Plans of selected company
	 * @throws KmException
	 */
	public ArrayList getBillPlanList(int id) throws KmException;
	
	/**
	 * @param id
	 * @return Arraylist for details of company selected
	 * @throws KmException
	 */
	public ArrayList getCompanyDetails(int id) throws KmException;
	
	/**
	 * @param plan_name
	 * @return ArrayList for Rate Details fo selected bill plan
	 * @throws KmException
	 */
	public ArrayList getBillPlanRateDetail(String planName ) throws KmException;

}
