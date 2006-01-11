package com.ibm.km.dao;

import java.util.ArrayList;

import com.ibm.km.exception.DAOException;
import com.ibm.km.forms.KmDocumentHitsCountFormBean;

public interface KmDocumentHitsCountDao {
	
	public ArrayList getTopBarLinks(KmDocumentHitsCountFormBean bean) throws DAOException;

	public ArrayList getBottomBarLinks(KmDocumentHitsCountFormBean bean) throws DAOException;


}
