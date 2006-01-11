package com.ibm.km.dao;

import java.sql.Connection;
import java.util.ArrayList;

import com.ibm.km.dto.KmLinkMstrDto;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;

public interface KmLinkMstrDao {
	
	public ArrayList editLink(KmLinkMstrDto kmLinkMstrDto) throws KmException,DAOException;
	
	public ArrayList viewLinks() throws KmException,DAOException;
	
	public ArrayList viewLinks(String elementId) throws KmException,DAOException;
	
	public ArrayList getUserRoleList(String actorId) throws KmException,DAOException;
	
	public int getMaxRowCountForElement(int elementId) throws DAOException;
	
	public int getLinkIdForElement(int elementId) throws DAOException;
}