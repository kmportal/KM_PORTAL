/*
 * Created on Nov 28, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.dao;

import java.util.ArrayList;

import com.ibm.km.exception.KmException;

/**
 * @author Atul
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface KmHitCountDAO {
  
	/**
	 * @param elementId
	 * @param date 
	 * @return
	 * @throws KmException
	 */
	public ArrayList hitCountReport(String elementId, String date) throws KmException;
}
