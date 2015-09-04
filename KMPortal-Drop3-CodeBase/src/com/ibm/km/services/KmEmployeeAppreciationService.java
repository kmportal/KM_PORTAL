package com.ibm.km.services;

import java.io.InputStream;
import java.util.List;

import com.ibm.km.dto.EmployeeAppreciationDTO;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.KmEmployeeAppreciationFormBean;

public interface KmEmployeeAppreciationService {
	
	/**
	 * Method to insert appreciation
	 */
	
	public String insertAppreciation(KmEmployeeAppreciationFormBean kmEmployeeAppreciationForm)throws KmException;
	
	/**
	 * Method to get list of employee appreciations
	 */
	public List<EmployeeAppreciationDTO> getEmployeeAppreciationList()throws KmException;

	public InputStream getEmployeeImage(int appId) throws KmException;

}
