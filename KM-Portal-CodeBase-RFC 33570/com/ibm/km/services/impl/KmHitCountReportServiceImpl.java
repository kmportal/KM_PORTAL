/*
 * Created on Nov 28, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.services.impl;

import java.util.ArrayList;

import com.ibm.km.dao.KmHitCountDAO;
import com.ibm.km.dao.impl.KmHitCountDaoImpl;
import com.ibm.km.exception.KmException;
import com.ibm.km.services.KmHitCountReportService;

/**
 * @author Atul
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class KmHitCountReportServiceImpl implements KmHitCountReportService {

	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmHitCountReportService#hitCountReport(java.lang.String)
	 */
	public ArrayList hitCountReport(String elementId,String date) throws KmException {
		KmHitCountDAO hitCountDao = new KmHitCountDaoImpl();
		ArrayList alHitReport = hitCountDao.hitCountReport(elementId,date);
		return alHitReport;
	}

}
