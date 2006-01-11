package com.ibm.km.services;

import java.util.ArrayList;

import com.ibm.km.dto.KmConfigureDataForSMSDto;
import com.ibm.km.dto.QuizReportDto;
import com.ibm.km.dto.SMSReportDTO;
import com.ibm.km.exception.KmException;

public interface KmConfigureSMSDataService {
	
	public ArrayList<KmConfigureDataForSMSDto> columnListForTable(String tableName) throws KmException;
	
	public Integer updateConfigurableColumnsForSMS(String idFlag,String tableName) throws KmException;

	public ArrayList<KmConfigureDataForSMSDto> getDocTypeList()throws KmException;
	
	public ArrayList<SMSReportDTO> getSmsReport(String fromDate, String toDate)throws KmException;

}
