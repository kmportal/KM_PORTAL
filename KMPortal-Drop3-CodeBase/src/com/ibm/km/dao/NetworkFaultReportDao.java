package com.ibm.km.dao;
/**
 * @author Ajil Chandran
 * Created On 02/12/2010
 */

import com.ibm.km.dto.NetworkFaultReportDto;
import com.ibm.km.exception.DAOException;

import java.util.List;

public interface NetworkFaultReportDao {

	
	public List fetchNetworkFaultReport(String[] circles, NetworkFaultReportDto dto)throws DAOException;
	public List fetchNetworkImpactReport(String[] circles, NetworkFaultReportDto dto)throws DAOException;
	


}
