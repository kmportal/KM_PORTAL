/*
 * Created on May 2, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.dao;

import java.util.List;

import com.ibm.km.exception.KmException;

/**
 * @author varunagg
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface KmFavoritesDao {
	
	/**
	 * 
	 * @param userId
	 * @param circleId
	 * @return
	 * @throws KmException
	 */
	public List getFavorites(int userId,String circleId) throws KmException;
}
