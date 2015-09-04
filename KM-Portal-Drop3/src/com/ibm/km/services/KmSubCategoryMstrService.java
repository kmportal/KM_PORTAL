/*
 * Created on Jan 30, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

package com.ibm.km.services;

import java.util.ArrayList;
import java.util.List;


import com.ibm.km.dto.KmSubCategoryMstr;
import com.ibm.km.exception.*;


/**
 * @author Anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */


public interface KmSubCategoryMstrService {

	/**
	 * Method to create Sub Category
	 * @param circleMstrDto
	 * @throws KmException
	 */
	public void createSubCategoryService(KmSubCategoryMstr circleMstrDto) throws KmException;

	/**
	 * Method to check on sub category Name 
	 * @param subCategoryName
	 * @param categoryId
	 * @return
	 * @throws KmException
	 */
	public boolean checkOnSubCategoryNameService(String subCategoryName, String categoryId)throws KmException;

	/**
	 * Method to retrieve categories based on Id
	 * @param categoryId
	 * @return
	 * @throws KmException
	 */
	public ArrayList retrieveSubCategoryService(String categoryId) throws KmException;

}

