package com.ibm.km.services;

import com.ibm.km.dto.KmDocumentMstr;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.KmSopBDUploadFormBean;

public interface KmSopBDUploadService {
	
	
	/**
	 * Method to save SOP details in XML. 
	 */
	public KmDocumentMstr saveProductDetails(KmSopBDUploadFormBean kmSopUploadFormBean)throws KmException;
	
	/**
	 * Method to read xml file from desk and display in jsp.
	 */
	public KmSopBDUploadFormBean viewProductDetails(KmSopBDUploadFormBean kmSopUploadFormBean)throws KmException;
}
