/* 
 * KmNetworkErrorLogDao.java
 * Created: Nov 24, 2010
 * 
 * 
 */ 

package com.ibm.km.dao;

import com.ibm.km.dto.NetworkErrorLogDto;
import com.ibm.km.exception.KmException;

public interface KmNetworkErrorLogDao {

 /** 

	* Inserts a row in the KM_NETWORK_FAULT_LOG table.
	* @param dto  DTO Object holding the data to be inserted.
	* @return  The no. of rows inserted. 
	* @throws KmException In case of an error
 **/ 

	public  int insert(NetworkErrorLogDto dto) throws KmException;
	public  String selectCSDUsers(int circleId) throws KmException ;
	//new change
	public  String getCircle(int circleId) throws KmException ;
 
}