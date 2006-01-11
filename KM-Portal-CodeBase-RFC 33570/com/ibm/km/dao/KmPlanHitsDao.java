/**
 * 
 */
package com.ibm.km.dao;

import java.util.List;

import com.ibm.km.dto.KmBillPlanDto;
import com.ibm.km.exception.KmException;

/**
 * @author Nehil Parashar
 *
 */
public interface KmPlanHitsDao 
{
	public List<KmBillPlanDto> getPlanHitsDetails(String lobId) throws KmException;
	public List<KmBillPlanDto> getSinglePlanData(String documentId)throws KmException;
}
