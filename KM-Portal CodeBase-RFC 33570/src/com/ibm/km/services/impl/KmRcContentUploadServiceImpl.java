package com.ibm.km.services.impl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.ibm.km.dao.KmRcContentUploadDao;
import com.ibm.km.dao.impl.KmRcContentUploadDaoImpl;
import com.ibm.km.dto.KmDocumentMstr;
import com.ibm.km.dto.KmElementMstr;
import com.ibm.km.dto.KmRcDataDTO;
import com.ibm.km.dto.KmRcHeaderDTO;
import com.ibm.km.dto.KmRcTypeDTO;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.KmRCContentUploadFormBean;
import com.ibm.km.forms.KmSopUploadFormBean;
import com.ibm.km.services.KmElementMstrService;
import com.ibm.km.services.KmRcContentUploadService;

public class KmRcContentUploadServiceImpl implements KmRcContentUploadService {
	
	public ArrayList<KmRcTypeDTO> getCombo() throws KmException {
		
		KmRcContentUploadDao recharge = new KmRcContentUploadDaoImpl();
	
		return recharge.getCombo();
	}


	public ArrayList<KmRcHeaderDTO> getHeaders() throws KmException {
		
		KmRcContentUploadDao recharge = new KmRcContentUploadDaoImpl();
		
		return recharge.getHeaders();
	}


public KmDocumentMstr saveRechargeDetails(KmRCContentUploadFormBean formBean,
		KmUserMstr sessionUserBean) throws KmException {

	
	KmDocumentMstr documentDTO=new KmDocumentMstr();	
	String documentPath =formBean.getElementFolderPath();
	
	try{
		
		//System.out.println("txt content : "+formBean.getFileData());
		
		//System.out.println("Writing File to : "+formBean.getFilePath());
	
        
			FileWriter fstream = new FileWriter(formBean.getFilePath());
			BufferedWriter out = new BufferedWriter(fstream);
  		    out.write(formBean.getFileData());
		    out.close();
		
	    documentDTO.setDocumentName(formBean.getFilePath().substring(formBean.getFilePath().lastIndexOf("/")+1));
	    documentDTO.setDocName(formBean.getFilePath().substring(formBean.getFilePath().lastIndexOf("/")+1));
//	    documentDTO.setDocumentDisplayName("RC_"+formBean.getRechargeValue());
//	    documentDTO.setDocumentDesc(formBean.getRechargeValue());
//	    documentDTO.setKeyword(formBean.getRechargeValue());
	    documentDTO.setDocumentDisplayName(""+formBean.getTopic());
	    documentDTO.setDocumentDesc(formBean.getTopic());
	    documentDTO.setKeyword(formBean.getTopic());
	    
		DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		Date date = inputFormat.parse(formBean.getStartDt());
		String outputText = outputFormat.format(date);
	    documentDTO.setPublishingStartDate(outputText);
	    
	    date = inputFormat.parse(formBean.getEndDt());
		outputText = outputFormat.format(date);
	    documentDTO.setPublishingEndDate(outputText);
		
	    documentDTO.setUserId(sessionUserBean.getCreatedBy());
		documentDTO.setApprovalStatus("A");
	    documentDTO.setUpdatedBy(sessionUserBean.getCreatedBy());
	    documentDTO.setDocumentPath(documentPath);
	    
	    KmElementMstr elementMstrDto= new KmElementMstr();
//		elementMstrDto.setElementDesc(formBean.getRechargeValue());
		elementMstrDto.setElementDesc(formBean.getTopic());
		
		String temp = documentPath.substring(0, documentPath.lastIndexOf("/"));
		temp = temp.substring(temp.lastIndexOf("/") + 1); 

		elementMstrDto.setParentId(temp);
		elementMstrDto.setPanStatus("N");
		elementMstrDto.setElementLevel("0");
		elementMstrDto.setStatus("A");
//		elementMstrDto.setElementName("RC_"+formBean.getRechargeValue());
		elementMstrDto.setElementName(""+formBean.getTopic());
		elementMstrDto.setCreatedBy(sessionUserBean.getCreatedBy());
		elementMstrDto.setUpdatedBy(sessionUserBean.getCreatedBy());
		
		KmElementMstrService elementService=new KmElementMstrServiceImpl();
	    String elementId=elementService.createElement(elementMstrDto);
		documentDTO.setElementId(elementId);
			    
	}catch(Exception ee)
	{
		ee.printStackTrace();			
	}
	return documentDTO;				

}



   public int insertRcData(KmRcDataDTO dto) throws KmException {
	   KmRcContentUploadDao recharge = new KmRcContentUploadDaoImpl();
	   return recharge.insertRcData(dto);
   }


 public KmRcDataDTO getRcData(String documentId) throws KmException {
	 
	 KmRcContentUploadDao recharge = new KmRcContentUploadDaoImpl();
	 return recharge.getRcData(documentId);
 	}


 public String deleteRcData(String documentId) throws KmException{
	 KmRcContentUploadDao recharge = new KmRcContentUploadDaoImpl();
	 return recharge.deleteRcData(documentId);
	
}



public ArrayList<HashMap<String, String>> getRcDataCsr(String rcValue,
		String rcType, ArrayList<String> docIdList) throws KmException {
	 KmRcContentUploadDao recharge = new KmRcContentUploadDaoImpl();
	 return recharge.getRcDataCsr(rcValue, rcType, docIdList);
}	

	
	
}
