package com.ibm.km.services.impl;

import java.util.ArrayList;

import com.ibm.km.dao.KmLinkMstrDao;
import com.ibm.km.dao.impl.KmLinkMstrDaoImpl;
import com.ibm.km.dto.KmLinkMstrDto;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;
import com.ibm.km.services.KmLinkMstrService;

public class KmLinkMstrServiceImpl implements KmLinkMstrService{

	public ArrayList editLink(KmLinkMstrDto kmLinkMstrDto) throws KmException,DAOException{
		ArrayList list = null;
		KmLinkMstrDao dao = new KmLinkMstrDaoImpl();
		list = dao.editLink(kmLinkMstrDto);
		return list;
	}
	
	public void createLink(){
		
	}
	
	public void deleteLink(){
		
	}
	
	public ArrayList viewLinks() throws KmException,DAOException{
		
		ArrayList list = null;
		KmLinkMstrDaoImpl dao = new KmLinkMstrDaoImpl();
		list = dao.viewLinks();
		
		return list;
	}
	
	public ArrayList getUserRoleList(String actorId) throws KmException,DAOException{
		ArrayList list = null;
		KmLinkMstrDao dao = new KmLinkMstrDaoImpl();
		list = dao.getUserRoleList(actorId);
		
		return list;
	}
	
	public ArrayList viewLinks(String elementId) throws KmException,DAOException{
		ArrayList list = null;
		KmLinkMstrDao dao = new KmLinkMstrDaoImpl();
		list = dao.viewLinks(elementId);
		
		return list;
	}
	
	public int getMaxRowCountForElement(int elementId) throws DAOException{
		int max_count = 0;
		
		KmLinkMstrDao dao = new KmLinkMstrDaoImpl();
		max_count = dao.getMaxRowCountForElement(elementId);
		
		return max_count;

	}
	
	public int getLinkIdForElement(int elementId) throws DAOException{
		int linkId = 0;
		
		KmLinkMstrDao dao = new KmLinkMstrDaoImpl();
		linkId = dao.getLinkIdForElement(elementId);
		
		return linkId;

	}
}
