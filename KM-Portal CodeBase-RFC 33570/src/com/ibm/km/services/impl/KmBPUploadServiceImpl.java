package com.ibm.km.services.impl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.ibm.km.dao.KmBPUploadDao;
import com.ibm.km.dao.impl.KmBPUploadDaoImpl;
import com.ibm.km.dto.KmBPUploadDto;
import com.ibm.km.dto.KmCsrLatestUpdatesDto;
import com.ibm.km.dto.KmDocumentMstr;
import com.ibm.km.dto.KmElementMstr;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.exception.DAOException;
import com.ibm.km.forms.KmBPUploadFormBean;
import com.ibm.km.services.KmBPUploadService;
import com.ibm.km.services.KmElementMstrService;

public class KmBPUploadServiceImpl implements KmBPUploadService{
	
	public ArrayList<KmBPUploadDto> getHeaders() throws DAOException{
		ArrayList<KmBPUploadDto> headers = new ArrayList<KmBPUploadDto>();
		
		KmBPUploadDao dao = new KmBPUploadDaoImpl();
		headers = dao.getHeaders();
		return headers;		
	}
	public int insertBPData(KmBPUploadDto dto) throws DAOException{
		int rowsAdded = 0;
		KmBPUploadDao dao = new KmBPUploadDaoImpl();
		rowsAdded = dao.insertBPData(dto);
		
		return rowsAdded;
	}
	public KmDocumentMstr saveBillPlanDetails(KmBPUploadFormBean formBean,KmUserMstr sessionUserBean) throws DAOException{
		KmDocumentMstr dto = new KmDocumentMstr();
		// DTO  for latest updates
		KmCsrLatestUpdatesDto kmCsrLatestUpdatesDto = new KmCsrLatestUpdatesDto();
		StringBuilder updateDesc = new StringBuilder();
		
		String documentPath =formBean.getElementFolderPath();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH-mm-ss-S");
		String date = sdf.format(new java.util.Date());
		
		try{
			
			//System.out.println("txt content : "+formBean.getFileData());
			
			//System.out.println("Writing File to : "+formBean.getFilePath());
		
	        
				FileWriter fstream = new FileWriter(formBean.getFilePath());
				BufferedWriter out = new BufferedWriter(fstream);
	  		    out.write(formBean.getFileData());
			    out.close();
			
			    dto.setDocumentName(formBean.getFilePath().substring(formBean.getFilePath().lastIndexOf("/")+1));
			    dto.setDocName(formBean.getFilePath().substring(formBean.getFilePath().lastIndexOf("/")+1));
			    dto.setDocumentDisplayName(""+formBean.getTopic());
			    dto.setDocumentDesc(formBean.getTopic());
			    dto.setKeyword(formBean.getTopic());
			    
			    ///
				DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
				DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
				
				Date d1 = inputFormat.parse(formBean.getFromDate());
				String d1Txt = outputFormat.format(d1);
			    dto.setPublishingStartDate(d1Txt);
			    
			    Date d2 = inputFormat.parse(formBean.getToDate());
				String d2Txt = outputFormat.format(d2);
			    dto.setPublishingEndDate(d2Txt);
			    ///

				dto.setUserId(sessionUserBean.getCreatedBy());
				dto.setApprovalStatus("A");
				dto.setUpdatedBy(sessionUserBean.getCreatedBy());
				dto.setDocumentPath(documentPath);

				kmCsrLatestUpdatesDto.setUpdateTitle(""+formBean.getTopic());
				String documentPathArr[] = documentPath.split("/");
				kmCsrLatestUpdatesDto.setLob(documentPathArr[1]);		
				kmCsrLatestUpdatesDto.setCircleId(documentPathArr[2]); 
				kmCsrLatestUpdatesDto.setCategory(documentPathArr[3]);
				
				updateDesc.append(formBean.getFileData());
				
				int updateDescLength = updateDesc.length();
				
				if (updateDescLength > 200)
				{
					updateDescLength = 200;
				}
				if (updateDescLength > 0)
				{
				kmCsrLatestUpdatesDto.setUpdateDesc(updateDesc.substring(0, updateDescLength).toString());
				}
				
			kmCsrLatestUpdatesDto.setActivationDate(d1Txt);
			kmCsrLatestUpdatesDto.setExpiryDate(d2Txt);	
			
			KmElementMstr elementMstrDto= new KmElementMstr();
			elementMstrDto.setElementDesc(formBean.getTopic());
			
			String temp = documentPath.substring(0, documentPath.lastIndexOf("/"));
			temp = temp.substring(temp.lastIndexOf("/") + 1); 

			elementMstrDto.setParentId(temp);
			elementMstrDto.setPanStatus("N");
			elementMstrDto.setElementLevel("0");
			elementMstrDto.setStatus("A");
			elementMstrDto.setElementName(""+formBean.getTopic());
			elementMstrDto.setCreatedBy(sessionUserBean.getCreatedBy());
			elementMstrDto.setUpdatedBy(sessionUserBean.getCreatedBy());
			
			KmElementMstrService elementService=new KmElementMstrServiceImpl();
		    String elementId=elementService.createElement(elementMstrDto);
		    dto.setElementId(elementId);
		    
		    dto.setKmCsrLatestUpdatesDto(kmCsrLatestUpdatesDto);
				    
		}catch(Exception e)
		{
			e.printStackTrace();			
		}
		
		
		return dto;
	}
	public ArrayList<KmBPUploadDto> viewBPDetails(KmBPUploadDto dto) throws DAOException{
		ArrayList<KmBPUploadDto> dataList = new ArrayList<KmBPUploadDto>();
		
		KmBPUploadDao dao = new KmBPUploadDaoImpl();
		dataList = dao.viewBPDetails(dto);
		
		return dataList;
	}
	
	public int deleteBPDetails(KmBPUploadDto dto) throws DAOException{
		int rowsDeleted =0;
		
		KmBPUploadDao dao = new KmBPUploadDaoImpl();
		dao.deleteBPDetails(dto);
		
		return rowsDeleted;
	}
	
	public ArrayList<String> getDocumentId(String searchKey) throws DAOException{
		ArrayList<String> documentIds = new ArrayList<String>();
		
		KmBPUploadDao dao = new KmBPUploadDaoImpl();
		documentIds = dao.getDocumentId(searchKey);
		
		return documentIds;
	}
	public ArrayList<HashMap<String, String>> getCsrBPDetails(ArrayList<String> documentIds) throws DAOException
	{
		ArrayList<HashMap<String, String>> billPlansList = new ArrayList<HashMap<String,String>>();
		
		KmBPUploadDao dao = new KmBPUploadDaoImpl();
		billPlansList = dao.getCsrBPDetails(documentIds);
		
		return billPlansList;
	}
	public String[] getCircleList(String documentId) throws DAOException{
		String[] circles = null;
	
		KmBPUploadDao dao = new KmBPUploadDaoImpl();
		circles = dao.getCircleList(documentId);
	
	return circles;
	}
}
