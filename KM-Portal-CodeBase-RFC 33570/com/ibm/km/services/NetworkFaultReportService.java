package com.ibm.km.services;
/**
 * @author Ajil Chandran
 * Created On  02 Dec 2010
 */
import java.util.List;

import com.ibm.km.dto.NetworkFaultReportDto;
import com.ibm.km.exception.KmException;

public interface NetworkFaultReportService  {

	public List fetchNetworkFaultReportAll(String[] circles,NetworkFaultReportDto dto)throws KmException;
	public List fetchImpactReportAll(String[] circles,NetworkFaultReportDto dto)throws KmException;
}
