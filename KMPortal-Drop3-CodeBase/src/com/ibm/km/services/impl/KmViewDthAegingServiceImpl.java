package com.ibm.km.services.impl;

import java.util.ArrayList;

import com.ibm.km.dao.KmViewDthAegingDao;
import com.ibm.km.dao.impl.KmViewDthAegingDaoImpl;
import com.ibm.km.dto.KmViewDthAegingDto;
import com.ibm.km.dto.OfferDetailsDTO;
import com.ibm.km.services.KmViewDthAegingService;

public class KmViewDthAegingServiceImpl implements KmViewDthAegingService{

	public ArrayList<KmViewDthAegingDto> getBucketId(String days) throws Exception{
		ArrayList<KmViewDthAegingDto> bucketDetails = null;
		
		KmViewDthAegingDao dao = new KmViewDthAegingDaoImpl();
		bucketDetails = dao.getBucketId(days);
		return bucketDetails;
		
	}
	public ArrayList<OfferDetailsDTO> getOfferDetails(String bucketId) throws Exception{
		ArrayList<OfferDetailsDTO> offerList = new ArrayList<OfferDetailsDTO>();
		
		KmViewDthAegingDao dao = new KmViewDthAegingDaoImpl();
		offerList = dao.getOfferDetails(bucketId);
		
		return offerList;
	}
	
}
