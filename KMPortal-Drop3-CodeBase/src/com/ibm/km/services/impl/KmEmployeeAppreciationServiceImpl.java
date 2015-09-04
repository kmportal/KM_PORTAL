
package com.ibm.km.services.impl;

import java.io.InputStream;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.ibm.km.dao.KmEmployeeAppreciationDao;
import com.ibm.km.dao.impl.KmEmployeeAppreciationDaoImpl;
import com.ibm.km.dto.EmployeeAppreciationDTO;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.KmEmployeeAppreciationFormBean;
import com.ibm.km.services.KmEmployeeAppreciationService;

public class KmEmployeeAppreciationServiceImpl implements KmEmployeeAppreciationService{
	
	private static final Logger logger;

	static {

		logger = Logger.getLogger(KmElementMstrServiceImpl.class);
	}

	public String insertAppreciation(KmEmployeeAppreciationFormBean kmEmployeeAppreciationForm)throws KmException{
		String message="";
		KmEmployeeAppreciationDao dao=new KmEmployeeAppreciationDaoImpl();		
		int recordsInserted = dao.insertAppreciation(kmEmployeeAppreciationForm);
		
		if ( recordsInserted > 0)
		{
			message="Total ["+recordsInserted+"] Employee Appreciation Inserted.";
		}
		return message;				
	}
	
	public ArrayList<EmployeeAppreciationDTO> getEmployeeAppreciationList() throws KmException {
		KmEmployeeAppreciationDao dao = new KmEmployeeAppreciationDaoImpl();
		ArrayList<EmployeeAppreciationDTO> employeeAppreciationList = dao.getEmployeeAppreciationList();
		
		try
        {
        	for(int ii=0; ii<employeeAppreciationList.size(); ii++)
        	{
        		String empImageName = "ImageProviderServlet?requestType=appreciationPage&id="+employeeAppreciationList.get(ii).getAppreciationId();		        	
	        	employeeAppreciationList.get(ii).setEmpImageName(empImageName);
        	}
        }
        catch (Exception e) {
			e.printStackTrace();
		}
        
		return employeeAppreciationList;
	}

	public InputStream getEmployeeImage(int appId) throws KmException {
		KmEmployeeAppreciationDao dao = new KmEmployeeAppreciationDaoImpl();
		EmployeeAppreciationDTO dto = dao.getEmployeeAppreciationDetail(appId);
		InputStream is = null;
        if(dto != null)
		is = dto.getEmpImageStream();  
        return is;
	}

}
