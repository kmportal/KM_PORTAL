/*
 * Created on Apr 28, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.services.impl;

import java.util.ArrayList;

import com.ibm.km.dao.KmFeedbackMstrDao;
import com.ibm.km.dao.impl.KmFeedbackMstrDaoImpl;
import com.ibm.km.dto.KmFeedbackMstr;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;
import com.ibm.km.services.KmFeedbackMstrService;

/**
 * @author Anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class KmFeedbackMstrServiceImpl implements KmFeedbackMstrService{

	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmFeedbackMstrService#createFeedbackService(com.ibm.km.dto.KmFeedbackMstr)
	 */
	public void createFeedbackService(KmFeedbackMstr dto)throws KmException {
		KmFeedbackMstrDao dao=new KmFeedbackMstrDaoImpl();
		dao.insert(dto);
		
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmFeedbackMstrService#viewFeedbacks(java.lang.String)
	 */
	public ArrayList viewFeedbacks(String[] elementIds,String elementId) throws KmException {
		ArrayList feedbackList= new ArrayList();
		KmFeedbackMstrDao dao=new KmFeedbackMstrDaoImpl();
		/*Defect MASDB00064285 fixed: Element path for the feedback was starting from India. */
		feedbackList=dao.viewFeedbacks(elementIds,elementId);
		return feedbackList;
		
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmFeedbackMstrService#readFeedbacksService(java.lang.String[])
	 */
	public void readFeedbacksService(String[] readFeedbacks,String[] feedbackResp, String[] feedBackId) throws KmException, DAOException {
		KmFeedbackMstrDao dao=new KmFeedbackMstrDaoImpl();
		dao.readFeedbacks(readFeedbacks,feedbackResp,feedBackId);
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmFeedbackMstrService#feedbackResponse(java.lang.String)
	 */
	public ArrayList feedbackResponse(int userId) throws DAOException {
		KmFeedbackMstrDao dao=new KmFeedbackMstrDaoImpl();
		ArrayList feedbackList=dao.feedbackResponse(userId);
		return feedbackList;
		
		
	}
	
	public ArrayList feedbackResponseAll(int userId) throws DAOException {
		KmFeedbackMstrDao dao=new KmFeedbackMstrDaoImpl();
		ArrayList feedbackList=dao.feedbackResponseAll(userId);
		return feedbackList;
		
		
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmFeedbackMstrService#feedbackReport(java.lang.String)
	 */
	public ArrayList feedbackReport(String[] elementIds,String elementId, String startDate, String endDate) throws KmException {
		ArrayList feedbackList= new ArrayList();
		KmFeedbackMstrDao dao=new KmFeedbackMstrDaoImpl();
		feedbackList=dao.feedbackReport(elementIds,elementId,startDate,endDate);
		return feedbackList;
		
	}
	public String getOLMIDFor(String userLoginId)throws KmException{
		KmFeedbackMstrDao dao=new KmFeedbackMstrDaoImpl();
		dao.getOLMIDFor(userLoginId);
		return userLoginId;
	}
}
