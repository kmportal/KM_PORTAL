/**
 * 
 */
package com.ibm.km.services.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.ibm.km.dao.KmPlanHitsDao;
import com.ibm.km.dao.impl.KmPlanHitsDaoImpl;
import com.ibm.km.dto.KmBillPlanDto;
import com.ibm.km.exception.KmException;

/**
 * @author Nehil Parashar
 *
 */
public class KmPlanHitsServiceImpl implements com.ibm.km.services.KmPlanHitsService
{
	private static Logger logger = Logger.getLogger(KmPlanHitsServiceImpl.class);
	
	@Override
	public List<KmBillPlanDto> getBulkPlanData(String lobId) throws KmException
	{
		logger.info("inside getBulkPlanData");
		
 		KmPlanHitsDao dao = new KmPlanHitsDaoImpl();
		return dao.getPlanHitsDetails(lobId);
	}

	@Override
	public List<KmBillPlanDto> getSinglePlanData(String documentId) throws KmException
	{
		logger.info("inside getSinglePlanData");
		
 		KmPlanHitsDao dao = new KmPlanHitsDaoImpl();
		return dao.getSinglePlanData(documentId);
	}
}
