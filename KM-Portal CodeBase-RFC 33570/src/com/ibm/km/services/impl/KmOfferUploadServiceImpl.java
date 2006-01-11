
package com.ibm.km.services.impl;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.ibm.km.dao.KmOfferUploadDao;
import com.ibm.km.dao.impl.KmOfferUploadDaoImpl;
import com.ibm.km.dto.OfferDetailsDTO;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.KmOfferUploadFormBean;
import com.ibm.km.services.KmOfferUploadService;

public class KmOfferUploadServiceImpl implements KmOfferUploadService{
	
	private static final Logger logger;

	static {

		logger = Logger.getLogger(KmOfferUploadServiceImpl.class);
	}

	public ArrayList<OfferDetailsDTO> getBucketList()throws KmException
	{
		KmOfferUploadDao dao = new KmOfferUploadDaoImpl();
		ArrayList<OfferDetailsDTO> bucketList = dao.getBucketList();
		return bucketList;
	}
	public int insertOffer(KmOfferUploadFormBean kmOfferUploadFormBean)throws KmException{
		int offerId=0;
		KmOfferUploadDao dao=new KmOfferUploadDaoImpl();	
		try
		{
			offerId = dao.insertOffer(kmOfferUploadFormBean);
			
			if ( offerId > 0)
			{
				logger.info("Offer id "+offerId+" uploaded.");
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage());
			throw new KmException(e.getMessage(), e);
		}
		
		return offerId;				
	}
	
	public int updateOffer(KmOfferUploadFormBean kmOfferUploadFormBean)throws KmException{
		int offerId=0;
		KmOfferUploadDao dao=new KmOfferUploadDaoImpl();	
		try
		{
			offerId = dao.updateOffer(kmOfferUploadFormBean);
			
			if ( offerId > 0)
			{
				logger.info("Offer id "+offerId+" updated.");
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage());
			throw new KmException(e.getMessage(), e);
		}
		
		return offerId;				
	}
	
	public ArrayList<OfferDetailsDTO> getOfferList(int offerId) throws KmException {
		KmOfferUploadDao dao = new KmOfferUploadDaoImpl();
		ArrayList<OfferDetailsDTO> offerList = dao.getOfferList(offerId);
	        try
	        {
	        }
	        catch (Exception e) {
				e.printStackTrace();
			}
		return offerList;
	}
}
