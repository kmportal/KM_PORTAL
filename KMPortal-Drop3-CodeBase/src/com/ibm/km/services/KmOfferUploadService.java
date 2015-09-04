package com.ibm.km.services;

import java.util.ArrayList;
import java.util.List;

import com.ibm.km.dto.OfferDetailsDTO;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.KmOfferUploadFormBean;

public interface KmOfferUploadService {
	
	public ArrayList<OfferDetailsDTO> getBucketList()throws KmException;
	
	public int insertOffer(KmOfferUploadFormBean kmOfferUploadForm)throws KmException;
	
	public int updateOffer(KmOfferUploadFormBean kmOfferUploadForm)throws KmException;
	
	public List<OfferDetailsDTO> getOfferList(int offerId)throws KmException;
}
