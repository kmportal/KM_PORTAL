package com.ibm.km.services;

import java.util.ArrayList;

import com.ibm.km.dto.KmExcelMstrDTO;
import com.ibm.km.exception.KmException;
/**
* @author Anveeksha & Neeraj 
* created for Waiver Matrix Upload
* for viewing Company Wise bill Plan details
*/


public interface KmExcelService {
	/**
	 * @param filepath
	 * @return ArrayList of Bill Pans with no rate details
	 * @throws KmException
	 */
	public ArrayList createTempTable(String filepath) throws KmException;
	public int getErrorStatus() throws KmException;
	/**
	 * @param userID,filePath
	 * @throws KmException
	 */
	public void updateMasterTables(String userID,String filePath) throws KmException;
}
