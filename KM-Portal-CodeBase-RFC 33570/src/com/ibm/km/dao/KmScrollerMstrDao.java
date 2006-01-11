/*
 * Created on Apr 14, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.dao;

import java.util.ArrayList;
import java.util.List;

import com.ibm.km.dto.KmAlertMstr;
import com.ibm.km.dto.KmScrollerMstr;
import com.ibm.km.exception.KmException;

/**
 * @author Anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface KmScrollerMstrDao {

	/**
	 * @param dto
	 * insert a record for a scroller in the database table KM_SCROLL_MSTR
	 */
	public int insert(KmScrollerMstr dto) throws KmException;
	
	/**
	 * @param circleId
	 * @return
	 * @throws KmException
	 */
	public String getScrollerMessage(String circleId)throws KmException;
	/**
	 * @param dto
	 * @return
	 */
	public ArrayList getScrollerList(KmScrollerMstr dto)throws KmException;
	/**
	 * @param dto
	 * @return
	 */
	public String deleteScroller(KmScrollerMstr dto)throws KmException;
	
	/**
	 * @param dto
	 * @return
	 * @throws KmException
	 */
	public int editScroller(KmAlertMstr dto)throws KmException;
	
	public int createAllLobScroller(KmScrollerMstr dto) throws KmException;
	
	public String getBulkScrollerMessage(List<Integer> elementIds)throws KmException;
	
	

}
