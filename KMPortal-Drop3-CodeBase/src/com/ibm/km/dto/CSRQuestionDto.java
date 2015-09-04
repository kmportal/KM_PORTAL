/*
 * Created on Feb 15, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.dto;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * @author Pramod
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CSRQuestionDto {
	
private String question="";
private Integer questionId;
	
	private String firstAnswer="";


	private String secondAnswer="";
	public Integer getQuestionId() {
		return questionId;
	}
	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}
	private String thirdAnswer="";
	private String fourthAnswer="";
	public Integer getSkipSize() {
		return skipSize;
	}
	public void setSkipSize(Integer skipSize) {
		this.skipSize = skipSize;
	}
	private String correctAnswer="";
	private String questionType="";
	private Integer noofCorrect=0;
	private Integer skipSize;
	public String getQuestion() {
		return question;
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
}
