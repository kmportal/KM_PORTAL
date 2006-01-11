package com.ibm.km.services;

import com.ibm.km.dto.KmDocumentMstr;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.KmProductUploadFormBean;;

public interface KmProductUploadService {
	
	
	/**
	 * Method to save product details in XML & images on the disk. 
	 */
	public KmDocumentMstr saveProductDetails(KmProductUploadFormBean kmProductUploadFormBean)throws KmException;
	
	/**
	 * Method to read xml file from desk and display in jsp.
	 */
	public KmProductUploadFormBean viewProductDetails(KmProductUploadFormBean kmProductUploadFormBean)throws KmException;
}
