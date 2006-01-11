package com.ibm.km.dao;

import com.ibm.km.exception.KmException;
import com.ibm.km.forms.KmCSRuserFormBean;

public interface KmCSRUserDao {
	
	
	public void insert(KmCSRuserFormBean fb)
    throws KmException;

}
