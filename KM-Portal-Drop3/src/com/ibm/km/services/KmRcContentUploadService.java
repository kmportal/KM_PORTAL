package com.ibm.km.services;

import java.util.ArrayList;
import java.util.HashMap;

import com.ibm.km.dto.KmDocumentMstr;
import com.ibm.km.dto.KmRcDataDTO;
import com.ibm.km.dto.KmRcHeaderDTO;
import com.ibm.km.dto.KmRcTypeDTO;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.KmRCContentUploadFormBean;



public interface KmRcContentUploadService {
	
	/**
	 * Method to Get Combo List
	 * @param dto
	 * @return
	 * @throws KmException
	 */
	public ArrayList<KmRcTypeDTO> getCombo() throws KmException;
	
	public ArrayList<KmRcHeaderDTO> getHeaders() throws KmException;
	
	public  KmDocumentMstr  saveRechargeDetails(KmRCContentUploadFormBean formBean,KmUserMstr sessionUserBean) throws KmException;
	
	public int insertRcData(KmRcDataDTO dto) throws KmException;
	
	public KmRcDataDTO getRcData(String documentId) throws KmException;
	
	public String deleteRcData(String documentId) throws KmException;
	
	public ArrayList<HashMap<String,String>> getRcDataCsr(String rcValue,String rcType, ArrayList<String> docIdList) throws KmException;
	
	

}
