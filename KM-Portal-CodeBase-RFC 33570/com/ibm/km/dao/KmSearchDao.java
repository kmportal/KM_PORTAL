/*
 * Created on Apr 30, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.dao;

import java.util.ArrayList;
import java.util.Map;

import  com.ibm.km.dto.KmSearchDetailsDTO;
import com.ibm.km.dto.KmSearch;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.KmDocumentMstrFormBean;

/**
 * @author Anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface KmSearchDao {

	/**
	 * @param dto
	 * @return
	 */
	ArrayList search(KmSearch dto) throws KmException;

	/**
	 * @param Id
	 * @param condition
	 * @param circleId
	 * @return
	 */
	ArrayList getChange(String Id, String condition, String circleId) throws KmException;

	/**
	 * @param dto
	 * @param documentIdString
	 * @return
	 */
	ArrayList contentSearch(KmSearch dto, String[] documentIds) throws KmException;
	
	/* km phase2 csr keyword search */
	
	/**
	* @param dto
	* @return
	*/
	ArrayList csrSearch(KmSearch dto) throws KmException;
	ArrayList <KmSearchDetailsDTO> searchDetails(String keyword,String mainOptionValue,String selectedSubOptionValue,int actorId, String loginId)  throws KmException;
	public ArrayList <KmSearchDetailsDTO> editDetails(String keyword,String mainOption,String subOption,  int serialNo)  throws KmException;
	public String updateDetails(String keyword,String mainOption,KmDocumentMstrFormBean formBean,  int serialNo)  throws KmException;
	public String deleteDetails(String mainOption, int serialNo)  throws KmException;
	
	ArrayList <KmSearchDetailsDTO> getConfigurableColumnList(String tableName)  throws KmException;

	public Map<String, Object> sendSms(String mainOption, int serialNo)throws KmException;

	public int insertSMSDetails(String olmId,String userLoginId, String mobileNo, String smsTemplate,
			String mainOption, String circleId, String partner, String location, String status,String udId)throws KmException;

	public KmSearchDetailsDTO getUserDetailForSMS(String userLoginId)	throws KmException;
}
