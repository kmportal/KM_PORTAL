/*

 * Created on Feb 6, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.services;

import java.util.ArrayList;
import java.util.HashMap;

import com.ibm.km.dto.KmDocumentMstr;
import com.ibm.km.dto.KmSearch;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;

/**
 * @author namangup
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface KmDocumentService {
	public ArrayList retrieveCSRDocumentList(int[] circleID) throws KmException;

	/**
	 * Method to increase Document Hit Count
	 * @param documentID
	 * @param userId
	 * @return
	 * @throws KmException
	 */
	public KmDocumentMstr increaseDocHitCount(String documentID, String userId)throws KmException ;

	/**
	 * Method to get Document
	 * @param documentID
	 * @return
	 * @throws KmException
	 */
	public KmDocumentMstr getDocument(String documentID)throws KmException ;
	
	/**
	 * Method to delete File Service
	 * @param documentId
	 * @param updatedBy
	 * @throws KmException
	 */
	public void deleteFileService(String documentId, String updatedBy)throws KmException;

	public String isFavouriteDocument(String documentId, String userId) throws KmException;
		
    /**
     * Method  for Keyword file Search
     * @param keyword
     * @param circleId
     * @param uploadedBy
     * @return
     * @throws KmException
     */
	public ArrayList keywordFileSearch(String keyword, String circleId, String uploadedBy)throws KmException;

	/**
	 * Method to add to Favourite
	 * @param userId
	 * @param docId
	 * @return
	 * @throws KmException
	 */
	public int addToFavorites (String userId, String docId) throws KmException;
	
	/**
	 * method to delete Favourite
	 * @param userId
	 * @param docId
	 * @return
	 * @throws KmException
	 */
	public int deleteFavorites (String userId, String docId) throws KmException;
	
	/**
	 * method to check for Favourite
	 * @param userId
	 * @param docId
	 * @return
	 * @throws KmException
	 */
	public boolean checkForFavorite (String userId, String docId) throws KmException;
	
	/**
	 * Method to retrieve Document Name
	 * @param documentId
	 * @return
	 * @throws KmException
	 */
	public String[] getDocumentName(String[] documentId)throws KmException;

	/**
	 * Method to view File 
	 * @param root
	 * @param parentId
	 * @return
	 * @throws KmException
	 */
	public ArrayList viewFileService(String root,String parentId)throws KmException;

	
	/**
	 * Method to change Path
	 * @param movedDocumentList
	 * @param oldPath
	 * @param newPath
	 * @return
	 * @throws KmException
	 */
	public boolean changePath(String[] movedDocumentList, String oldPath, String newPath) throws KmException;

	/**
	 * method to get All Documents
	 * @param parentId
	 * @return
	 * @throws KmException
	 */
	//added by vishwas
	public ArrayList changePath1(String[] movedDocumentList, String oldPath, String newPath) throws KmException;
	public ArrayList copyPath1(String[] movedDocumentList, String oldPath, String newPath,String elementid) throws KmException;
	public String getElementId() throws KmException;
	//end by vishwas
	public ArrayList getAllDocuments(String parentId)throws KmException;

	/**
	 * Method to get Document ID's
	 * @param movedDocumentList
	 * @return
	 * @throws KmException
	 */
	public String[] getDocumentIds(String[] movedDocumentList) throws KmException;

	/**
	 * Method to get Document Path
	 * @param documentId
	 * @return
	 * @throws KmException
	 */
	public String getDocumentPath(String documentId)throws KmException;

	/**
	 * Method to get Element Id
	 * @param string
	 * @return
	 * @throws KmException
	 */
	public String getElementId(String string) throws KmException;

    /**
     * Method to get Document Dto
     * @param documentId
     * @return
     * @throws KmException
     */
	public KmDocumentMstr getDocumentDto(String documentId) throws KmException;

	/**
	 * Method to  edit Documennt
	 * @param dto
	 * @throws KmException
	 */
	public void editDocument(KmDocumentMstr dto)throws KmException;
	
	/**
	 * Method to retrieve Document Id
	 * @param documentName
	 * @return
	 * @throws KmException
	 */
	public String getDocumentId(String documentName)throws KmException;
	
	/**
	 * Method to retrieve Document Version
	 * @param documentId
	 * @return
	 * @throws KmException
	 */
	public ArrayList getDocumentVersions(String documentId) throws KmException;

	/**
	 * Method to retrieve Documents
	 * @param parentId
	 * @return
	 * @throws KmException
	 */
	public String[] getDocuments(String parentId)throws KmException;

	/**
	 * Method to delete Documents
	 * @param documents
	 * @param updatedBy
	 * @throws KmException
	 */
	public void deleteDocuments(String[] documents,String updatedBy)throws KmException;

	/**
	 * Method to get circle Approver
	 * @param circleId
	 * @return
	 * @throws KmException
	 */
	public String getCircleApprover(String circleId)throws KmException;

	/**
	 * Method to get Document path
	 * @param string
	 * @return
	 * @throws KmException
	 */
	public String getDocPath(String string)throws KmException;

	/**
	 * Method to changeDocument path 
	 * @param documentElementIds
	 * @return
	 * @throws KmException
	 * @throws Exception
	 */
	public String[] changeDocumentPathsInDb(String[] documentElementIds)throws KmException, Exception;

	/**
	 * Method to changeDocument path 
	 * @param elementId
	 * @return
	 * @throws KmException
	 */
	public boolean changeDocumentPathsInDb(String elementId)throws KmException;

	/**
	 * Method to update Document path
	 * @param allDocuments
	 * @param alldocumentPaths
	 * @return
	 * @throws KmException
	 */
	public boolean updateDocumentPaths(String[] allDocuments, String[] alldocumentPaths)throws KmException;

	/**
	 * method to get Document Details
	 * @param string
	 * @return
	 * @throws KmException
	 */
	public KmDocumentMstr getDocumentDetails(String string) throws KmException;
	
	/*  KM PHASE II  a new service for archived search   */
	/**
	 * Method to retrieve archived documents
	 * @param dto
	 * @return
	 * @throws KmException
	 */
	public  ArrayList archivedSearch(KmDocumentMstr dto)  throws KmException;
	
	/*  KM PHASE II  a new service for csrkeyword search   */
	/**
	 * Method for csr Keyword search
	 * @param dto
	 * @return
	 * @throws KmException
	 */  
	public  ArrayList csrKeywordSearch(	KmSearch dto)  throws KmException;

	 /*  KM PHASE II  a new service for retrieving single document   */	
	/**
	 * Method to retrieve single document
	 * @param subCategoryId
	 * @return
	 * @throws KmException
	 * @throws DAOException
	 */
	public KmDocumentMstr getSingleDoc(String subCategoryId)throws KmException,DAOException;

	
	public KmDocumentMstr getDocumentByElementId(String elementId) throws KmException;
	
	public ArrayList<String> docFilterAsPerDocType(ArrayList<String> docIdList,int docType,String isTop15) throws KmException;
	
	public ArrayList<KmDocumentMstr> getTopDocuments(String elementId, String docType) throws KmException;
	

}
