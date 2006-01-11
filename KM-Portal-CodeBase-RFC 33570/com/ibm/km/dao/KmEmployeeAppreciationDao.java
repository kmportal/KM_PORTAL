package com.ibm.km.dao;

import java.util.ArrayList;

import com.ibm.km.dto.EmployeeAppreciationDTO;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.KmEmployeeAppreciationFormBean;

public interface KmEmployeeAppreciationDao {

	public int insertAppreciation(KmEmployeeAppreciationFormBean kmEmployeeAppreciationForm) throws KmException;

	public ArrayList<EmployeeAppreciationDTO> getEmployeeAppreciationList() throws KmException;

	public EmployeeAppreciationDTO getEmployeeAppreciationDetail(int empId)throws KmException;

}
