package com.ibm.km.dto;

public class QuizReportDto {
	
	
	private String correct_answers;
	private String wrong_answers;
	private String result;
	private String ud_id;
	private String user_id;
	private String circle_name;
	private String skipQuesions;
	private String lob;
	
	
	public String getLob() {
		return lob;
	}
	public void setLob(String lob) {
		this.lob = lob;
	}
	public String getSkipQuesions() {
		return skipQuesions;
	}
	public void setSkipQuesions(String skipQuesions) {
		this.skipQuesions = skipQuesions;
	}
	public String getCorrect_answers() {
		return correct_answers;
	}
	public void setCorrect_answers(String correct_answers) {
		this.correct_answers = correct_answers;
	}
	public String getWrong_answers() {
		return wrong_answers;
	}
	public void setWrong_answers(String wrong_answers) {
		this.wrong_answers = wrong_answers;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getUd_id() {
		return ud_id;
	}
	public void setUd_id(String ud_id) {
		this.ud_id = ud_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getCircle_name() {
		return circle_name;
	}
	public void setCircle_name(String circle_name) {
		this.circle_name = circle_name;
	}
	
	
	
}
