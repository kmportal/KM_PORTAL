/*
 * Created on Apr 29, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.services.impl;

import java.util.ArrayList;

import com.ibm.km.dao.KmBriefingMstrDao;
import com.ibm.km.dao.impl.KmBriefingMstrDaoImpl;
import com.ibm.km.dto.CSRQuestionDto;
import com.ibm.km.dto.KmBriefingMstr;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.dto.LobDTO;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.KmBriefingMstrFormBean;
import com.ibm.km.services.KmBriefingMstrService;

/**
 * @author Anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class KmBriefingMstrServiceImpl implements KmBriefingMstrService{

	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmBriefingMstrService#createBriefing(com.ibm.km.dto.KmBriefingMstr)
	 */
	public void createBriefing(KmBriefingMstr dto) throws KmException {
		KmBriefingMstrDao dao=new KmBriefingMstrDaoImpl();
		dao.insert(dto);
		
	}
	
	public ArrayList viewBriefing(String circleId, String  categoryId, String date) throws KmException {
		KmBriefingMstrDao dao=new KmBriefingMstrDaoImpl();
		return dao.view(circleId,categoryId, date);
		
	}
	
	public ArrayList viewLoginBriefing(String circleId, String  categoryId, String date) throws KmException {
		KmBriefingMstrDao dao=new KmBriefingMstrDaoImpl();
		return dao.viewLoginBriefing(circleId,categoryId, date);
		
	}
	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmBriefingMstrService#editBriefings(int)
	 */
	/*public ArrayList editBriefings(String []  elementIds, String fromDate, String endDate, int userId) throws KmException {
		KmBriefingMstrDao dao=new KmBriefingMstrDaoImpl();
		ArrayList alBriefings= dao.editBriefings(elementIds,fromDate,endDate,userId);
		return alBriefings;
	}
*/
	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmBriefingMstrService#updateBriefings(int)
	 */
	public KmBriefingMstr updateBriefings(int briefingId) throws KmException {
		KmBriefingMstrDao dao=new KmBriefingMstrDaoImpl();
		KmBriefingMstr briefingDto=(KmBriefingMstr) dao.updateBriefings(briefingId);
		return briefingDto;
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmBriefingMstrService#updateinDbBriefings(java.util.ArrayList)
	 */
	public void updateinDbBriefings(String briefingId,String briefHeading,
	String[] arrBriefingDetails,String displayDt) throws KmException {
		KmBriefingMstrDao dao=new KmBriefingMstrDaoImpl();
		dao.updateinDbBriefings(briefingId, briefHeading,
		arrBriefingDetails, displayDt);
		
	}

	public ArrayList editBriefings(String circleId, String fromDate, String endDate, int userId) throws KmException {
		KmBriefingMstrDao dao=new KmBriefingMstrDaoImpl();
		ArrayList alBriefings= dao.editBriefings(circleId,fromDate,endDate,userId);
		return alBriefings;
	}
	
	public int  insertQuestion(KmBriefingMstrFormBean formBean)
			throws KmException {
		KmBriefingMstrDao dao=new KmBriefingMstrDaoImpl();
		return dao.insertQuestion(formBean);
		
	}

	
	public ArrayList<CSRQuestionDto> getQuestions(KmUserMstr sessionUserBean) throws KmException {
		KmBriefingMstrDao dao=new KmBriefingMstrDaoImpl();
		return dao.getQuestions(sessionUserBean);
	}

	@Override
	public int insertQuizResult(KmBriefingMstrFormBean formBean)
			throws KmException {
		KmBriefingMstrDao dao=new KmBriefingMstrDaoImpl();
		return dao.insertQuizResult(formBean);
	}

	@Override
	public int insertSkipQuizResult(KmBriefingMstrFormBean formBean)
			throws KmException {
		KmBriefingMstrDao dao=new KmBriefingMstrDaoImpl();
			return dao.insertSkipQuizResult(formBean);
			}

	@Override
	public int getQuizResult(KmUserMstr sessionUserBean) throws KmException {
		KmBriefingMstrDao dao=new KmBriefingMstrDaoImpl();
		return dao.getQuizResult(sessionUserBean);
		}

	@Override
	public int getQuestionsSize(KmBriefingMstrFormBean formBean) throws KmException {KmBriefingMstrDao dao=new KmBriefingMstrDaoImpl();
	return dao.getQuestionsSize(formBean);
	}

	@Override
	public  int  getskipQuesize(KmUserMstr sessionUserBean) throws KmException {
		KmBriefingMstrDao dao=new KmBriefingMstrDaoImpl();
	return dao.getskipQuesize(sessionUserBean);
	}

	@Override
	public ArrayList<CSRQuestionDto> getSkipQuestions(KmUserMstr sessionUserBean) throws KmException {
		KmBriefingMstrDao dao=new KmBriefingMstrDaoImpl();
		return dao.getSkipQuestions(sessionUserBean);
	}

	@Override
	public int insertSkipQuestions(KmBriefingMstrFormBean formBean)
			throws KmException {
		KmBriefingMstrDao dao=new KmBriefingMstrDaoImpl();
		return dao.insertSkipQuestions(formBean);
	}

	@Override
	public ArrayList<LobDTO> getLobList() throws KmException {
		
		KmBriefingMstrDao dao=new KmBriefingMstrDaoImpl();
		return dao.getLobList();
		}

	@Override
	public int getQuestionResultSize(KmUserMstr sessionUserBean)
			throws KmException {
		
		KmBriefingMstrDao dao=new KmBriefingMstrDaoImpl();
		return dao.getQuestionResultSize(sessionUserBean);}

	@Override
	public String getNewQuestions(KmBriefingMstrFormBean formBean)
			throws KmException {
		KmBriefingMstrDao dao=new KmBriefingMstrDaoImpl();
		return dao.getNewQuestions(formBean);
	}
}
