/*
 * Created on Jan 18, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.forms;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
/**
 * @author namangup
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class KmLoginFormBean extends ActionForm{
	private String password = null;
	private String userId = null;
	private String userName = null;
	private String message = null;
	private String kmforgot=null;
	private String circleScroller = null;
	


	public String getCircleScroller() {
		return circleScroller;
	}

	public void setCircleScroller(String circleScroller) {
		this.circleScroller = circleScroller;
	}

	/**
	 * 
	 */
	public KmLoginFormBean() {
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

		ActionErrors errors = new ActionErrors();
		return errors;

	}
	
	/**
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param string
	 */
	public void setPassword(String string) {
		password = string;
	}

	/**
	 * @param string
	 */
	public void setUserId(String string) {
		userId = string.toUpperCase();
	}

	/**
	 * @return
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param string
	 */
	public void setUserName(String string) {
		userName = string.toUpperCase();
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
	public String getKmforgot() {
		return kmforgot;
	}

	/**
	 * @param string
	 */
	public void setKmforgot(String string) {
		kmforgot = string;
	}

}
