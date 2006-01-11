/*
 * Created on Feb 14, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.services.impl;

import java.util.ArrayList;


import com.ibm.km.dao.RejectedFileDao;
import com.ibm.km.dao.impl.RejectedFileDaoImpl;
import com.ibm.km.exception.KmException;
import com.ibm.km.services.ApproveFileService;
import com.ibm.km.services.RejectedFileService;

/**
 * @author Vipin
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class RejectedFileServiceImpl implements RejectedFileService{
	
	
	/* (non-Javadoc)
	 * @see com.ibm.km.services.ApproveFileService#getFileList(java.lang.String, java.lang.String)
	 */
	public ArrayList getFileList(String userId, String fromDate, String toDate, String root, String actorId)
		throws KmException {
			
			RejectedFileDao dao = new RejectedFileDaoImpl();
			return dao.getFileList(userId,fromDate,toDate, root,actorId);
	}

	

}
