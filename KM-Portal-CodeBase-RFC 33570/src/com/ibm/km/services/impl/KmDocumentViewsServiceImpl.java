/*
 * Created on May 6, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.services.impl;

import java.util.List;

import com.ibm.km.dao.KmDocumentViewsDao;
import com.ibm.km.dao.impl.KmDocumentViewsDaoImpl;
import com.ibm.km.exception.KmException;
import com.ibm.km.services.KmDocumentViewsService;

/**
 * @author varunagg
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class KmDocumentViewsServiceImpl implements KmDocumentViewsService {
	public List showDocumentViews(String elementId,String fromDate,String toDate) throws KmException {
		KmDocumentViewsDao dao = new KmDocumentViewsDaoImpl();
		List documentViewsList = dao.showDocumentViews(elementId,fromDate,toDate);
		return documentViewsList;		
	}
}
