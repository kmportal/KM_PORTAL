package com.ibm.km.forms;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.*;


 /** 
	* @author Naman 
	* Created On Mon Jan 14 13:12:43 IST 2008 
	* Form Bean class for table KM_RULE_MSTR 
 
 **/ 
public class KmRuleMstrFormBean extends ActionForm {


    private String ruleId;

    private String ruleName;

    private String ruleDesc;

    private String circleId;

    private java.sql.Timestamp createdDt;

    private String createdBy;

    private java.sql.Timestamp updatedDt;

    private String updatedBy;

    private String status;

    /** Creates a dto for the KM_RULE_MSTR table */
    public KmRuleMstrFormBean() {
    }


    public ActionErrors validate(
        ActionMapping mapping,
        HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        //TODO Replace with Actual code.This method is called if validate for this action mapping is set to true. and iff errors is not null and not emoty it forwards to the page defined in input attritbute of the ActionMapping
        return errors;
    }
 /** 
	* Get ruleId associated with this object.
	* @return ruleId
 **/ 

    public String getRuleId() {
        return ruleId;
    }

 /** 
	* Set ruleId associated with this object.
	* @param ruleId The ruleId value to set
 **/ 

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

 /** 
	* Get ruleName associated with this object.
	* @return ruleName
 **/ 

    public String getRuleName() {
        return ruleName;
    }

 /** 
	* Set ruleName associated with this object.
	* @param ruleName The ruleName value to set
 **/ 

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

 /** 
	* Get ruleDesc associated with this object.
	* @return ruleDesc
 **/ 

    public String getRuleDesc() {
        return ruleDesc;
    }

 /** 
	* Set ruleDesc associated with this object.
	* @param ruleDesc The ruleDesc value to set
 **/ 

    public void setRuleDesc(String ruleDesc) {
        this.ruleDesc = ruleDesc;
    }

 /** 
	* Get circleId associated with this object.
	* @return circleId
 **/ 

    public String getCircleId() {
        return circleId;
    }

 /** 
	* Set circleId associated with this object.
	* @param circleId The circleId value to set
 **/ 

    public void setCircleId(String circleId) {
        this.circleId = circleId;
    }

 /** 
	* Get createdDt associated with this object.
	* @return createdDt
 **/ 

    public java.sql.Timestamp getCreatedDt() {
        return createdDt;
    }

 /** 
	* Set createdDt associated with this object.
	* @param createdDt The createdDt value to set
 **/ 

    public void setCreatedDt(java.sql.Timestamp createdDt) {
        this.createdDt = createdDt;
    }

 /** 
	* Get createdBy associated with this object.
	* @return createdBy
 **/ 

    public String getCreatedBy() {
        return createdBy;
    }

 /** 
	* Set createdBy associated with this object.
	* @param createdBy The createdBy value to set
 **/ 

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

 /** 
	* Get updatedDt associated with this object.
	* @return updatedDt
 **/ 

    public java.sql.Timestamp getUpdatedDt() {
        return updatedDt;
    }

 /** 
	* Set updatedDt associated with this object.
	* @param updatedDt The updatedDt value to set
 **/ 

    public void setUpdatedDt(java.sql.Timestamp updatedDt) {
        this.updatedDt = updatedDt;
    }

 /** 
	* Get updatedBy associated with this object.
	* @return updatedBy
 **/ 

    public String getUpdatedBy() {
        return updatedBy;
    }

 /** 
	* Set updatedBy associated with this object.
	* @param updatedBy The updatedBy value to set
 **/ 

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

 /** 
	* Get status associated with this object.
	* @return status
 **/ 

    public String getStatus() {
        return status;
    }

 /** 
	* Set status associated with this object.
	* @param status The status value to set
 **/ 

    public void setStatus(String status) {
        this.status = status;
    }

}
