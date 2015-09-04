package com.ibm.km.forms;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.*;


 /** 
	* @author Naman 
	* Created On Mon Jan 14 13:09:29 IST 2008 
	* Form Bean class for table KM_ACTORS 
 
 **/ 
public class KmActorsFormBean extends ActionForm {


    private String kmActorId;

    private String kmActorName;

    private java.sql.Timestamp createdDt;

    private String createdBy;

    private java.sql.Timestamp updatedDt;

    private String updatedBy;

    /** Creates a dto for the KM_ACTORS table */
    public KmActorsFormBean() {
    }


    public ActionErrors validate(
        ActionMapping mapping,
        HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        //TODO Replace with Actual code.This method is called if validate for this action mapping is set to true. and iff errors is not null and not emoty it forwards to the page defined in input attritbute of the ActionMapping
        return errors;
    }
 /** 
	* Get kmActorId associated with this object.
	* @return kmActorId
 **/ 

    public String getKmActorId() {
        return kmActorId;
    }

 /** 
	* Set kmActorId associated with this object.
	* @param kmActorId The kmActorId value to set
 **/ 

    public void setKmActorId(String kmActorId) {
        this.kmActorId = kmActorId;
    }

 /** 
	* Get kmActorName associated with this object.
	* @return kmActorName
 **/ 

    public String getKmActorName() {
        return kmActorName;
    }

 /** 
	* Set kmActorName associated with this object.
	* @param kmActorName The kmActorName value to set
 **/ 

    public void setKmActorName(String kmActorName) {
        this.kmActorName = kmActorName;
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

}
