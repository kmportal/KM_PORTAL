/*
 * Created on Apr 29, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.services;

import java.util.ArrayList;

import com.ibm.km.dto.CSRQuestionDto;
import com.ibm.km.dto.KmBriefingMstr;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.dto.LobDTO;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.KmBriefingMstrFormBean;

/**
 * @author Anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface KmBriefingMstrService {

	/**
	 *Method  to create Briefings 
	 * @param dto
	 * @throws KmException
	 */
	void createBriefing(KmBriefingMstr dto) throws KmException;
	
	/**
	 * Method to view Briefings
	 * @param circleId
	 * @param date
	 * @return
	 * @throws KmException
	 */
	ArrayList viewBriefing(String circleId, String cagtegoryId, String date) throws KmException;
	ArrayList viewLoginBriefing(String circleId, String cagtegoryId, String date) throws KmException;
	
	/**
	 * Method to edit briefings
	 * @param elementIds
	 * @param fromDate
	 * @param endDate
	 * @param userId
	 * @return
	 * @throws KmException
	 */
    //public ArrayList editBriefings(String[] elementIds , String fromDate, String endDate, int userId)  throws KmException;
    
    /**
     * Method to UpdateBriefings 
     * @param briefingId
     * @return
     * @throws KmException
     */
    KmBriefingMstr updateBriefings(int briefingId) throws KmException;
    
	/**
	 * Method to Update Briefing in Db
	 * @param briefingId
	 * @param briefHeading
	 * @param arrBriefingDetails
	 * @param displayDt
	 * @throws KmException
	 */
	void updateinDbBriefings(String briefingId,String briefHeading,
	String[] arrBriefingDetails,String displayDt) throws KmException;
	
	/**
	 * Method to edit briefings
	 * @param circleId
	 * @param fromDate
	 * @param endDate
	 * @param userId
	 * @return
	 * @throws KmException
	 */
    public ArrayList editBriefings(String circleId , String fromDate, String endDate, int userId)  throws KmException;

    public int insertQuestion(KmBriefingMstrFormBean formBean) throws KmException;

	public ArrayList<CSRQuestionDto> getQuestions(KmUserMstr sessionUserBean) throws KmException;

	public int insertQuizResult(KmBriefingMstrFormBean formBean)throws KmException;

	public int insertSkipQuizResult(KmBriefingMstrFormBean formBean)throws KmException;

	public int getQuizResult(KmUserMstr sessionUserBean)throws KmException;

	public int getQuestionsSize(KmBriefingMstrFormBean formBean) throws KmException;

	public int getskipQuesize(KmUserMstr sessionUserBean)throws KmException;

	public ArrayList<CSRQuestionDto> getSkipQuestions(KmUserMstr sessionUserBean)throws KmException;

	int insertSkipQuestions(KmBriefingMstrFormBean formBean)throws KmException;

	public ArrayList<LobDTO> getLobList() throws KmException;

	public int getQuestionResultSize(KmUserMstr sessionUserBean)throws KmException;

	public String getNewQuestions(KmBriefingMstrFormBean formBean)throws KmException;

	
}
