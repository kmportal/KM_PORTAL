/*
 * Created on Apr 7, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.forms;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author namangup
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class KmCSRHomeBean {
	private String circleId;
	private String circleName;
	private String categoryId;
	private String categoryName;
	private String panCircleId;
	private ArrayList circlelist;
	private HashMap panIndiaCategoryMap;
	private HashMap circleCategoryMap;
	private ArrayList documentList;
	private ArrayList categoryList;
	private ArrayList subCategoryList=null;
	private ArrayList subSubCategoryList=null;
	private String message = "";
	private String circleScroller = "";
	private ArrayList lobList=null;
	private String lobId="";
	private String alertMessage="";
	private String uD="";
	private String uDUserPost="";
	public String getUDUserPost() {
		return uDUserPost;
	}

	public void setUDUserPost(String userPost) {
		uDUserPost = userPost;
	}

	public String getUDUserPre() {
		return uDUserPre;
	}

	public void setUDUserPre(String userPre) {
		uDUserPre = userPre;
	}

	private String uDUserPre="";
	
	public String getUD() {
		return uD;
	}

	public void setUD(String ud) {
		uD = ud;
	}

	private String topScroller = "";
	private String bottomScroller = "";
	
	// added

	public String getAlertMessage() {
		return alertMessage;
	}

	public void setAlertMessage(String alertMessage) {
		this.alertMessage = alertMessage;
	}

	/**
	 * @return
	 */
	public String getCircleId() {
		return circleId;
	}

	/**
	 * @param string
	 */
	public void setCircleId(String string) {
		circleId = string;
	}

	/**
	 * @return
	 */
	public ArrayList getCirclelist() {
		return circlelist;
	}

	/**
	 * @return
	 */
	public void setCirclelist(ArrayList list) {
		circlelist = list;
	}


	/**
	 * @return
	 */
	public ArrayList getDocumentList() {
		return documentList;
	}

	/**
	 * @param list
	 */
	public void setDocumentList(ArrayList list) {
		documentList = list;
	}

	/**
	 * @return
	 */
	public HashMap getPanIndiaCategoryMap() {
		return panIndiaCategoryMap;
	}

	/**
	 * @param map
	 */
	public void setPanIndiaCategoryMap(HashMap map) {
		panIndiaCategoryMap = map;
	}

	/**
	 * @return
	 */
	public HashMap getCircleCategoryMap() {
		return circleCategoryMap;
	}

	/**
	 * @param map
	 */
	public void setCircleCategoryMap(HashMap map) {
		circleCategoryMap = map;
	}

	/**
	 * @return
	 */
	public ArrayList getCategoryList() {
		return categoryList;
	}

	/**
	 * @return
	 */
	public ArrayList getSubCategoryList() {
		return subCategoryList;
	}

	/**
	 * @param list
	 */
	public void setCategoryList(ArrayList list) {
		categoryList = list;
	}

	/**
	 * @param list
	 */
	public void setSubCategoryList(ArrayList list) {
		subCategoryList = list;
	}

	/**
	 * @return
	 */
	public String getCircleName() {
		return circleName;
	}

	/**
	 * @param string
	 */
	public void setCircleName(String string) {
		circleName = string;
	}

	/**
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param string
	 */
	public void setMessage(String string) {
		message = string;
	}

	/**
	 * @return
	 */
	public String getPanCircleId() {
		return panCircleId;
	}

	/**
	 * @param string
	 */
	public void setPanCircleId(String string) {
		panCircleId = string;
	}

	/**
	 * @return
	 */
	public ArrayList getSubSubCategoryList() {
		return subSubCategoryList;
	}

	/**
	 * @param list
	 */
	public void setSubSubCategoryList(ArrayList list) {
		subSubCategoryList = list;
	}

	/**
	 * @return
	 */
	public ArrayList getLobList() {
		return lobList;
	}

	/**
	 * @param list
	 */
	public void setLobList(ArrayList list) {
		lobList = list;
	}

	/**
	 * @return
	 */
	public String getLobId() {
		return lobId;
	}

	/**
	 * @param string
	 */
	public void setLobId(String string) {
		lobId = string;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	public String getCircleScroller() {
		return circleScroller;
	}

	public void setCircleScroller(String circleScroller) {
		this.circleScroller = circleScroller;
	}

	public void setTopScroller(String topScroller) {
		this.topScroller = topScroller;
	}

	public String getTopScroller() {
		return topScroller;
	}

	public void setBottomScroller(String bottomScroller) {
		this.bottomScroller = bottomScroller;
	}

	public String getBottomScroller() {
		return bottomScroller;
	}


	
}
