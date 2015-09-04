/*
 * Created on Feb 13, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.services.impl;

import java.util.ArrayList;

import com.ibm.km.dao.SubmitFileDao;
import com.ibm.km.dao.impl.SubmitFileDaoImpl;
import com.ibm.km.exception.KmException;
import com.ibm.km.services.SubmitFileService;

/**
 * @author Vipin
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SubmitFileServiceImpl implements SubmitFileService{
	
	/* (non-Javadoc)
	 * @see com.ibm.km.services.SubmitFileService#getFileList(java.lang.String, java.lang.String)
	 */
	public ArrayList getFileList(String userId, String fromDate,String toDate)throws KmException
	{
		SubmitFileDao dao = new SubmitFileDaoImpl();
		return dao.getFileList(userId,fromDate,toDate);
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.km.services.SubmitFileService#submitFile(java.lang.String[])
	 */
	public void submitFile(String[] fileIds) throws KmException {
		
		SubmitFileDao dao = new SubmitFileDaoImpl();
		dao.submitFile(fileIds);
	}

}
