/*
 * Created on Oct 29, 2007
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.services;

import java.util.ArrayList;
import java.util.List;

import com.ibm.km.dto.KmCircleMstr;
import com.ibm.km.exception.KmException;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Anil
 * @version 2.0
 * 
 */
public interface KmCircleMstrService {

    /**
     * Method to retrieve Circle Details
     * @return
     * @throws KmException
     */
	public List getCircleDetails() throws KmException;

	/**
	 * Method to create Circle Service
	 * @param circleMstrDto
	 * @throws KmException
	 */
	public void createCircleService(KmCircleMstr circleMstrDto) throws KmException;

    /**
     * Method to Check circle
     * @param string
     * @return
     * @throws KmException
     */
	public boolean checkCircleOnCircleName(String string)throws KmException;

	/**
	 * Methods to retreive Circles
	 * @return
	 * @throws KmException
	 */
	public ArrayList retrieveCircles() throws KmException;

	/**
	 * Method to retrieve all circles
	 * @return
	 * @throws KmException
	 */
	public ArrayList retrieveAllCircles() throws KmException;

}
