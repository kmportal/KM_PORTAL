package com.ibm.km.dao;

import java.util.List;

import com.ibm.km.dto.KmSchemesAndBenefitsDto;

public interface KmSchemesAndBenefitsDao {
	
	List getDetails(KmSchemesAndBenefitsDto dto);
	
	boolean isPresent();
	
	boolean deleteDetails();
	
	boolean insertDetails(KmSchemesAndBenefitsDto dto);
	
}
