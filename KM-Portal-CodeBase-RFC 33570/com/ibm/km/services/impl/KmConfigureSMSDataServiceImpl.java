package com.ibm.km.services.impl;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.ibm.km.dao.KmConfigureSMSDataDao;
import com.ibm.km.dao.KmElementMstrDao;
import com.ibm.km.dao.KmUserMstrDao;
import com.ibm.km.dao.impl.KmConfigureSMSDataDaoImpl;
import com.ibm.km.dao.impl.KmElementMstrDaoImpl;
import com.ibm.km.dao.impl.KmUserMstrDaoImpl;
import com.ibm.km.dto.KmConfigureDataForSMSDto;
import com.ibm.km.dto.QuizReportDto;
import com.ibm.km.dto.SMSReportDTO;
import com.ibm.km.exception.KmException;
import com.ibm.km.services.KmConfigureSMSDataService;

public class KmConfigureSMSDataServiceImpl implements KmConfigureSMSDataService{
	public static Logger logger = Logger.getRootLogger();
	
	public ArrayList<KmConfigureDataForSMSDto> columnListForTable(String tableName) throws KmException{
		KmConfigureSMSDataDao configureSMSDataDao = new KmConfigureSMSDataDaoImpl();
		ArrayList<KmConfigureDataForSMSDto> columnListForTable=new ArrayList<KmConfigureDataForSMSDto>();
		columnListForTable=configureSMSDataDao.getColumnList(tableName);
		return columnListForTable;
	
	}
	public Integer updateConfigurableColumnsForSMS(String idFlag,String tableName) throws KmException{
		KmConfigureSMSDataDao configureSMSDataDao = new KmConfigureSMSDataDaoImpl();
		Integer status = configureSMSDataDao.updateConfigurableColumnsForSMS(idFlag,tableName);
		return status;
	}
	public ArrayList<KmConfigureDataForSMSDto> getDocTypeList() throws KmException{
		KmConfigureSMSDataDao configureSMSDataDao = new KmConfigureSMSDataDaoImpl();
		ArrayList<KmConfigureDataForSMSDto> getDocTypeList=new ArrayList<KmConfigureDataForSMSDto>();
		getDocTypeList=configureSMSDataDao.getDocTypeList();
		return getDocTypeList;
	
	}
	public ArrayList<SMSReportDTO> getSmsReport(String fromDate, String toDate) throws KmException {
		KmConfigureSMSDataDao configureSMSDataDao = new KmConfigureSMSDataDaoImpl();
		return configureSMSDataDao.getSmsReport(fromDate, toDate);
	}
}
