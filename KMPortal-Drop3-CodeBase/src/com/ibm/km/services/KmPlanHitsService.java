/**
 * 
 */
package com.ibm.km.services;

import java.util.List;

import com.ibm.km.dto.KmBillPlanDto;
import com.ibm.km.exception.KmException;

/**
 * @author Nehil Parashar
 *
 */
public interface KmPlanHitsService 
{
	public List<KmBillPlanDto> getBulkPlanData(String lobId) throws KmException;
	public List<KmBillPlanDto> getSinglePlanData(String documentId) throws KmException;
}