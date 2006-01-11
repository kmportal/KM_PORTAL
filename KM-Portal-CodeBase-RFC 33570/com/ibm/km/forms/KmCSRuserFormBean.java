package com.ibm.km.forms;

import java.util.List;

import org.apache.struts.action.ActionForm;



/**
 * @author IBM_ADMIN
 *
 */
public class KmCSRuserFormBean extends ActionForm {
	
	

	private String comment;
	private String email;
	private String olm_id;
	private String methodName;
    public String flag;
    private List emailIdList;
    private List descList;
    private String selectedEmail;
    private String csrf;
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getComment() {
		return comment;
	}


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOlm_id() {
		return olm_id;
	}

	public void setOlm_id(String olm_id) {
		this.olm_id = olm_id;
	}

	
	
	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public List getEmailIdList() {
		return emailIdList;
	}
	public void setEmailIdList(List emailIdList) {
		this.emailIdList = emailIdList;
	}
	public void setSelectedEmail(String selectedEmail) {
		this.selectedEmail = selectedEmail;
	}
	public String getSelectedEmail() {
		return selectedEmail;
	}
	public void setDescList(List descList) {
		this.descList = descList;
	}
	public List getDescList() {
		return descList;
	}
	public void setCsrf(String csrf) {
		this.csrf = csrf;
	}
	public String getCsrf() {
		return csrf;
	}
	
}
