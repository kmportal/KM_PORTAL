package com.ibm.km.services.impl;
/**
 * @author Ajil Chandran
 * Created On  02 Dec 2010
 */
import java.util.ArrayList;
import java.util.List;


import com.ibm.km.dao.NetworkFaultReportDao;
import com.ibm.km.dao.impl.NetworkFaultReportDaoImpl;
import com.ibm.km.dto.NetworkFaultReportDto;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;
import com.ibm.km.services.NetworkFaultReportService;

public class NetworkFaultReportServiceImpl implements NetworkFaultReportService {


	public List fetchNetworkFaultReportAll(String[] circles, NetworkFaultReportDto dto) throws KmException {
		List report=new ArrayList();
		NetworkFaultReportDao dao= new NetworkFaultReportDaoImpl();
		try {
			report=dao.fetchNetworkFaultReport(circles,dto);
		} catch (DAOException e) {
			throw new KmException("Exception: " + e.getMessage(), e);
		}catch (Exception e) {
			throw new KmException("Exception: " + e.getMessage(), e);
		}
		
		return report;
	}

	public List fetchImpactReportAll(String[] circles, NetworkFaultReportDto dto) throws KmException {
		List report=new ArrayList();
		NetworkFaultReportDao dao= new NetworkFaultReportDaoImpl();
		try {
			report=dao.fetchNetworkImpactReport(circles, dto);
		} catch (DAOException e) {
			throw new KmException("Exception: " + e.getMessage(), e);
		}catch (Exception e) {
			throw new KmException("Exception: " + e.getMessage(), e);
		}
		
		return report;
	}
	

}
