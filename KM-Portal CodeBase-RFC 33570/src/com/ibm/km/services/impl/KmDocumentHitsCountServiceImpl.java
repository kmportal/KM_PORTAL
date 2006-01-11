package com.ibm.km.services.impl;

import java.util.ArrayList;

import com.ibm.km.dao.KmDocumentHitsCountDao;
import com.ibm.km.dao.impl.KmDocumentHitsCountDaoImpl;
import com.ibm.km.exception.DAOException;
import com.ibm.km.forms.KmDocumentHitsCountFormBean;
import com.ibm.km.services.KmDocumentHitsCountService;

public class KmDocumentHitsCountServiceImpl implements KmDocumentHitsCountService{
	
	public ArrayList getTopBarLinks(KmDocumentHitsCountFormBean formBean) throws DAOException{
		
		KmDocumentHitsCountDao dao = new KmDocumentHitsCountDaoImpl();
		ArrayList links = dao.getTopBarLinks(formBean);
		
		return links;
	}

	public ArrayList getBottomBarLinks(KmDocumentHitsCountFormBean formBean) throws DAOException{
		
		KmDocumentHitsCountDao dao = new KmDocumentHitsCountDaoImpl();
		ArrayList links = dao.getBottomBarLinks(formBean);
		
		return links;
	}


}
