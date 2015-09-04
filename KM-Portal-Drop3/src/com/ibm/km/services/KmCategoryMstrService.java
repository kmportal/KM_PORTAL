/*
 * Created on Jan 26, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ibm.km.dto.KmCategoryMstr;
import com.ibm.km.exception.*;


/**
 * @author Anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */


public interface KmCategoryMstrService {
	
	/**
	 * Method to create Category Service
	 * @param circleMstrDto
	 * @throws KmException
	 */
	public void createCategoryService(KmCategoryMstr circleMstrDto) throws KmException;


	/**
	 * Method to check Category name service 
	 * @param categoryName
	 * @param circleId
	 * @return true or false
	 */
	public boolean checkOnCategoryNameService(String categoryName,String circleId)throws KmException;

	/**
	 * Method to retrieve Category Service
	 * @param circleId
	 * @return
	 * @throws KmException
	 */
	public ArrayList retrieveCategoryService(String[] circleId)throws KmException;
	
	/**
	 * Method to retrieve Category Map
	 * @param circleId
	 * @param favCategoryId
	 * @return
	 * @throws KmException
	 */
	public HashMap retrieveCategoryMap(String circleId,String favCategoryId)throws KmException;

    /**
     * Method to retrieve No of Categories
     * @param string
     * @return
     * @throws KmException
     */
	public int getNoOfCategories(String string) throws KmException;

}
