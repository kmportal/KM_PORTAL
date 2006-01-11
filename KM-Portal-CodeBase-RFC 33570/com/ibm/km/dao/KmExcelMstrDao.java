package com.ibm.km.dao;

import java.util.ArrayList;

import com.ibm.km.dto.KmExcelMstrDTO;
import com.ibm.km.dto.KmDocumentMstr;
import com.ibm.km.exception.KmException;
/**
 * @author Anveeksha & Neeraj
 * for waiver matrix uplaod 
 * save the data of uploaded excel to database
 */
public interface KmExcelMstrDao {
	/**
	 * @param dto
	 * @return ArrayList of Bill Plans without Rate Details
	 * @throws KmException
	 */
	public ArrayList createTempTable(KmExcelMstrDTO dto) throws KmException ;
	public int getErrorStatus() throws KmException;
	/**
	 * @param userID,filePath
	 * @throws KmException
	 */
	public void updateMasterTables(String userID,String filePath) throws KmException;
}
