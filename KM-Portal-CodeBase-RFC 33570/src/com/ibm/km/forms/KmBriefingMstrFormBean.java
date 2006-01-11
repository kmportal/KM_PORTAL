package com.ibm.km.forms;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.ibm.km.dto.CSRQuestionDto;
import com.ibm.km.dto.KmUserDto;


/**
 * Form bean for a Struts application.

 * @version 	1.0
 * @author	Anil
 */
public class KmBriefingMstrFormBean extends ActionForm {

	private String briefingId="";
	private String briefingHeading="";
	private String[] briefingDetails=new String[10]; 
	private String circleId="";
	private String createdDt=null;
	private String createdBy="";
	private String displayDt="";
	private String methodName="";
	private String count="0";
		private ArrayList categoryList=null;
	private String categoryId="";
	private String fromDate=null;
	private String toDate=null;
	private ArrayList briefingList=null;
	private String kmActorId="";
	private String initStatus="";
	private String question="";
	
	private String firstAnswer="";
	private String secondAnswer="";
	private String thirdAnswer="";
	private String fourthAnswer="";
	private String correctAnswer="";
	private String questionType="";
	private Integer noofCorrect=0;
	private Integer questionId;
	private String questionName;
	private String answer=null;
	private String user_login_id="";
	private String udId="";
	private String circle_Id="";
	private int elementId=0;
	private String elementName="";
	private String csrf;
	
	

	public int getElementId() {
		return elementId;
	}

	public void setElementId(int elementId) {
		this.elementId = elementId;
	}

	public String getElementName() {
		return elementName;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
	}

	private ArrayList<CSRQuestionDto> questions = null;
	private ArrayList<CSRQuestionDto> skipQuestions = null;
	private String newAnswer="";
	
	
	public String getNewAnswer() {
		return newAnswer;
	}

	public void setNewAnswer(String newAnswer) {
		this.newAnswer = newAnswer;
	}

	public String getAnswer() {
		return answer;
	}
	
	

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public ArrayList<CSRQuestionDto> getSkipQuestions() {
		return skipQuestions;
	}

	public void setSkipQuestions(ArrayList<CSRQuestionDto> skipQuestions) {
		this.skipQuestions = skipQuestions;
	}

	public Integer getQuestionId() {
		return questionId;
	}


	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}


	public String getQuestionName() {
		return questionName;
	}


	public void setQuestionName(String questionName) {
		this.questionName = questionName;
	}


	public String getUser_login_id() {
		return user_login_id;
	}

	public void setUser_login_id(String user_login_id) {
		this.user_login_id = user_login_id;
	}

	public String getUdId() {
		return udId;
	}

	public void setUdId(String udId) {
		this.udId = udId;
	}

	public String getCircle_Id() {
		return circle_Id;
	}

	public void setCircle_Id(String circle_Id) {
		this.circle_Id = circle_Id;
	}

	public String getQuestion() {
		return question;
	}

	
	
	
	public ArrayList<CSRQuestionDto> getQuestions() {
		return questions;
	}

	public void setQuestions(ArrayList<CSRQuestionDto> questions) {
		this.questions = questions;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getFirstAnswer() {
		return firstAnswer;
	}

	public void setFirstAnswer(String firstAnswer) {
		this.firstAnswer = firstAnswer;
	}

	public String getSecondAnswer() {
		return secondAnswer;
	}

	public void setSecondAnswer(String secondAnswer) {
		this.secondAnswer = secondAnswer;
	}

	public String getThirdAnswer() {
		return thirdAnswer;
	}

	public void setThirdAnswer(String thirdAnswer) {
		this.thirdAnswer = thirdAnswer;
	}

	public String getFourthAnswer() {
		return fourthAnswer;
	}

	public void setFourthAnswer(String fourthAnswer) {
		this.fourthAnswer = fourthAnswer;
	}

	public String getCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}


	

	/**
	 * @return
	 */
	public String getBriefingDetails(int index) {
		return briefingDetails[index];
	}

	/**
	 * @return
	 */
	public String getBriefingHeading() {
		return briefingHeading;
	}

	/**
	 * @return
	 */
	public String getBriefingId() {
		return briefingId;
	}

	/**
	 * @return
	 */
	public String getCircleId() {
		return circleId;
	}

	/**
	 * @return
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @return
	 */
	public String getCreatedDt() {
		return createdDt;
	}

	/**
	 * @return
	 */
	public String getDisplayDt() {
		return displayDt;
	}

	/**
	 * @param string
	 */
	public void setBriefingDetails(int index, String value) {
		briefingDetails[index] = value;
	}

	/**
	 * @param string
	 */
	public void setBriefingHeading(String string) {
		briefingHeading = string;
	}

	/**
	 * @param string
	 */
	public void setBriefingId(String string) {
		briefingId = string;
	}

	/**
	 * @param string
	 */
	public void setCircleId(String string) {
		circleId = string;
	}

	/**
	 * @param string
	 */
	public void setCreatedBy(String string) {
		createdBy = string;
	}

	/**
	 * @param string
	 */
	public void setCreatedDt(String string) {
		createdDt = string;
	}

	/**
	 * @param string
	 */
	public void setDisplayDt(String string) {
		displayDt = string;
	}

	/**
	 * @return
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * @param string
	 */
	public void setMethodName(String string) {
		methodName = string;
	}

	/**
	 * @return
	 */
	public String getCount() {
		return count;
	}

	/**
	 * @param i
	 */
	public void setCount(String i) {
		count = i;
	}

	/**
	 * @return
	 */
	public String[] getBriefingDetails() {
		return briefingDetails;
	}

	/**
	 * @return
	 */
	public ArrayList getCategoryList() {
		return categoryList;
	}

	/**
	 * @param strings
	 */
	public void setBriefingDetails(String[] strings) {
		briefingDetails = strings;
	}

	/**
	 * @param list
	 */
	public void setCategoryList(ArrayList list) {
		categoryList = list;
	}

	/**
	 * @return
	 */
	public String getCategoryId() {
		return categoryId;
	}

	/**
	 * @param string
	 */
	public void setCategoryId(String string) {
		categoryId = string;
	}

	/**
	 * @return
	 */
	public String getFromDate() {
		return fromDate;
	}

	/**
	 * @return
	 */
	public String getToDate() {
		return toDate;
	}

	/**
	 * @param string
	 */
	public void setFromDate(String string) {
		fromDate = string;
	}

	/**
	 * @param string
	 */
	public void setToDate(String string) {
		toDate = string;
	}

	/**
	 * @return
	 */
	public ArrayList getBriefingList() {
		return briefingList;
	}

	/**
	 * @param string
	 */
	public void setBriefingList(ArrayList al) {
		briefingList = al;
	}

	/**
	 * @return
	 */
	public String getKmActorId() {
		return kmActorId;
	}

	/**
	 * @param string
	 */
	public void setKmActorId(String string) {
		kmActorId = string;
	}

	/**
	 * @return
	 */
	public String getInitStatus() {
		return initStatus;
	}

	/**
	 * @param string
	 */
	public void setInitStatus(String string) {
		initStatus = string;
	}

	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	public Integer getNoofCorrect() {
		return noofCorrect;
	}

	public void setNoofCorrect(Integer noofCorrect) {
		this.noofCorrect = noofCorrect;
	}

	public void reset() {
		question="";
		firstAnswer="";
		secondAnswer="";
		thirdAnswer="";
		fourthAnswer="";
		correctAnswer="";
		questionType="";
		noofCorrect=0;
		elementId=0;
		elementName="";
		
		
	}

	public void setCsrf(String csrf) {
		this.csrf = csrf;
	}

	public String getCsrf() {
		return csrf;
	}

}
