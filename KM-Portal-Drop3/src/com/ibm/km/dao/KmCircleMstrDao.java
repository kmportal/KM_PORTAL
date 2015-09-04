/* 
 * KmCircleMstrDao.java
 * Created: January 14, 2008
 * 
 * 
 */ 

package com.ibm.km.dao;

import java.sql.ResultSet;
import java.util.ArrayList;

import com.ibm.km.dto.*;
import com.ibm.km.exception.*;


/** 
  * DAO for the <b>KM_CIRCLE_MSTR</b> database table.
  * Provides methods to perform insert, update, delete and select queries.
  * <br><pre>
  * Table: KM_CIRCLE_MSTR
  * ----------------------------------------------
  *     column: CIRCLE_NAME        VARCHAR2 null
  *     column: CIRCLE_ID        NUMBER null
  *     column: CREATED_DT        DATE null
  *     column: CREATED_BY        NUMBER null
  *     column: UPDATED_DT        DATE null
  *     column: UPDATED_BY        NUMBER null
  *     column: STATUS        CHAR null
  * 
  * Primary Key(s):  CIRCLE_ID
  * </pre>
  * 
  */ 
public interface KmCircleMstrDao {

	/** 
	
		* Inserts a row in the KM_CIRCLE_MSTR table.
		* @param dto  DTO Object holding the data to be inserted.
		* @return  The no. of rows inserted. 
		* @throws KmCircleMstrDaoException In case of an error
	**/

	public int insert(KmCircleMstr dto) throws DAOException;



	
	

	/** 
	
		* Returns a multiple rows of the database. If there is no match, <code>null</code> is returned.
		* @return DTO Object representing a multiple rows of the table.If there is no match, <code>null</code> is returned.
	 **/
	
	/** 
		* Returns the details of all the circles in the database
	 **/
	
	public KmCircleMstr[] getCircleData() throws DAOException;

	/**
	 * @param circleName
	 * @return
	 */
	public boolean checkCircleName(String circleName)throws KmException;

	/**
	 * @return circleList
	 */
	public ArrayList getAllCircles() throws KmException;

	/**
	 * @return circleList
	 */
	public ArrayList getCircles() throws KmException;

}