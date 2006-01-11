/*
 * Created on Dec 5, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.services.impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.ibm.km.dao.KmUserReportDao;
import com.ibm.km.dao.impl.KmUserReportDaoImpl;
import com.ibm.km.exception.KmException;
import com.ibm.km.services.KmUserReportService;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class KmUserReportServiceImpl implements KmUserReportService{
	
	private static final Logger logger;

	static {

		logger = Logger.getLogger(KmCategoryMstrServiceImpl.class);
	}
	
	public ArrayList getUserLoginReport( String elementId, String elementLevel)throws KmException
		{
			ArrayList userList=new ArrayList();
			KmUserReportDao dao=new KmUserReportDaoImpl();
				
			userList=dao.getUserLoginList(elementId,elementLevel);    		
			
			return userList;		
		}
	/* (non-Javadoc)
			 * @see com.ibm.km.services.KmFileReportService#getcircleWisereport(java.lang.String)
			 */
   public ArrayList getcircleWisereport(String elementId, String kmActorId,String date) throws KmException {
					KmUserReportDao dao=new KmUserReportDaoImpl();
					return dao.getcircleWisereport(elementId,kmActorId,date);
		
				}	
				
/* km phase II agent wise ID report service impl*/

public ArrayList getAgentIdWiseReport( String elementId, String eleLevelId, String partner)throws KmException
	{
		ArrayList agentList=new ArrayList();
		KmUserReportDao dao=new KmUserReportDaoImpl();
		
	
			
			agentList=dao.getAgentList(elementId,eleLevelId,partner);    		
			
		
		return agentList;		
	}
public ArrayList getLockedUserReport(String elementId) throws KmException {
	
	KmUserReportDao dao=new KmUserReportDaoImpl();
	return dao.getLockedUserList(elementId);    		
		
	
	
}

				

}
