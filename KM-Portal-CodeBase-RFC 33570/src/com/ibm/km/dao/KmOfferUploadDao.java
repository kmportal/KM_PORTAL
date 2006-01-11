package com.ibm.km.dao;

import java.util.ArrayList;

import com.ibm.km.dto.OfferDetailsDTO;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.KmOfferUploadFormBean;

public interface KmOfferUploadDao {

	public ArrayList<OfferDetailsDTO> getBucketList()throws KmException;
	
	public int insertOffer(KmOfferUploadFormBean kmOfferUploadFormBean) throws KmException;
	
	public int updateOffer(KmOfferUploadFormBean kmOfferUploadFormBean) throws KmException;

	public ArrayList<OfferDetailsDTO> getOfferList(int offerId) throws KmException;
}
