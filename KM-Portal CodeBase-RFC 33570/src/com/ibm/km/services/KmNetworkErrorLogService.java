/*
 * Created on Nov 24, 2010
 *
 */
package com.ibm.km.services;

import com.ibm.km.dto.NetworkErrorLogDto;
import com.ibm.km.exception.KmException;

/**
 * @author Kundan Kumar
 *
 */

public interface KmNetworkErrorLogService  {
	

	/**
	 * Method to insert error log
	 * @param NetworkErrorLogDto
	 * @throws KmException
	 */
	public int insertNetworkErrorLogService(NetworkErrorLogDto userMstrDto) throws KmException;
	public  String selectCSDUsers(int circleId) throws KmException ;
	//new change
	public  String getCircle(int circleId) throws KmException ;

}
