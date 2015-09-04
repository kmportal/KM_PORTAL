/*
 * Created on Apr 30, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ibm.km.dto.KmSearchDetailsDTO;
import com.ibm.km.dto.KmSearch;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.KmDocumentMstrFormBean;

/**
 * @author Anil
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface KmSearchService {

	/**
	 * Method to Search
	 * 
	 * @param dto
	 * @return
	 * @throws KmException
	 */
	ArrayList search(KmSearch dto) throws KmException;

	/**
	 * Method for Content Search
	 * 
	 * @param dto
	 * @return
	 * @throws KmException
	 */
	ArrayList contentSearch(KmSearch dto) throws KmException;

	/**
	 * Location search method to find network faults
	 * 
	 * @param dto
	 * @return
	 * @throws KmException
	 */
	List locationSearch(KmSearch dto) throws KmException;

	/**
	 * Method to get Change
	 * 
	 * @param Id
	 * @param cond
	 * @param circleId
	 * @return
	 * @throws KmException
	 */
	ArrayList getChange(String Id, String cond, String circleId)
			throws KmException;

	/* KM phase2 csr keywors serach */
	/**
	 * Method for csr Search
	 * 
	 * @param dto
	 * @return
	 * @throws KmException
	 */
	ArrayList csrSearch(KmSearch dto) throws KmException;

	ArrayList contentSearchCSR(KmSearch dto) throws KmException;

	ArrayList contentSearchAdmin(KmSearch searchDto) throws KmException;

	ArrayList<KmSearchDetailsDTO> searchDetails(String keyword,
			String mainOptionValue, String selectedSubOptionValue, int actorId,
			String loginId) throws KmException;

	ArrayList<KmSearchDetailsDTO> editDetails(String keyword,
			String mainOption, String subOption, int serialNo)
			throws KmException;

	String updateDetails(String keyword, String mainOption,
			KmDocumentMstrFormBean formBean, int serialNo) throws KmException;

	String deleteDetails(String keyword, int serialNo) throws KmException;

	Map<String, Object> sendSms(String mainOption, int serialNo)
			throws KmException;

	public int insertSMSDetails(String olmId,String userLoginId, String mobileNo,
			String smsTemplate, String mainOption, String circleId,
			String partner, String location, String status,String udId)throws KmException;

	public KmSearchDetailsDTO getUserDetailForSMS(String userLoginId) throws KmException;
}
