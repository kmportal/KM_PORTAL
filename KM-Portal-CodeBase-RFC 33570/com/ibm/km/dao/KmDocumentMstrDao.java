/* 
 * KmDocumentMstrDao.java
 * Created: January 14, 2008
 * 
 * 
 */ 

package com.ibm.km.dao;

import java.util.ArrayList;
import java.util.HashMap;

import com.ibm.km.dto.*;
import com.ibm.km.exception.*;


/** 
  * DAO for the <b>KM_DOCUMENT_MSTR</b> database table.
  * Provides methods to perform insert, update, delete and select queries.
  * <br><pre>
  * Table: KM_DOCUMENT_MSTR
  * ----------------------------------------------
  *     column: DOCUMENT_ID        NUMBER null
  *     column: DOCUMENT_GROUP_ID        NUMBER null
  *     column: DOCUMENT_NAME        VARCHAR2 null
  *     column: DOCUMENT_DISPLAY_NAME        VARCHAR2 null
  *     column: DOCUMENT_DESC        VARCHAR2 null
  *     column: SUB_CATEGORY_ID        NUMBER null
  *     column: CATEGORY_ID        NUMBER null
  *     column: CIRCLE_ID        NUMBER null
  *     column: NUMBER_OF_HITS        NUMBER null
  *     column: STATUS        CHAR null
  *     column: APPROVAL_STATUS        CHAR null
  *     column: UPLOADED_BY        NUMBER null
  *     column: UPLOADED_DT        DATE null
  *     column: UPDATED_BY        NUMBER null
  *     column: UPDATED_DT        DATE null
  *     column: APPROVAL_REJECTION_DT        DATE null
  *     column: APPROVAL_REJECTION_BY        NUMBER null
  *     column: PUBLISHING_START_DT        DATE null
  *     column: PUBLISHING_END_DT        DATE null
  * 
  * Primary Key(s):  DOCUMENT_ID
  * </pre>
  * 
  */ 
public interface KmDocumentMstrDao {

 /** 

	* Inserts a row in the KM_DOCUMENT_MSTR table.
	* @param dto  DTO Object holding the data to be inserted.
	* @return  The no. of rows inserted. 
	* @throws KmDocumentMstrDaoException In case of an error
 **/ 

    public  int insert(KmDocumentMstr dto) throws KmDocumentMstrDaoException;

 /** 

	* Updates a row in the KM_DOCUMENT_MSTR table.
	* The Primary Key Object determines wich row gets updated.
	* @param dto  DTO Object holding the data to be updated. The primary key vales determine which row will be updated
	* @return  The no. of rows updated. 
	* @throws KmDocumentMstrDaoException In case of an error
 **/ 

    public  int update(KmDocumentMstr dto) throws KmDocumentMstrDaoException;

 /** 

	* Deletes a row in the database based on the primary key supplied.
	* @param documentId  The documentId value for which the row needs to be deleted
	* @return  The no. of rows deleted.
	* @throws KmDocumentMstrDaoException In case of an error
 **/ 

    public  int delete(String documentId) throws KmDocumentMstrDaoException;
    
    
    /*
     * This method return the favourite status of a document for given CSR user Id.
     */
    public String isFavouriteDocument(String documentId, String userId) throws KmException ;

 /** 

	* Returns a single row of the database based on the primary key information supplied. If there is no match, <code>null</code> is returned.
	* @param documentId  The documentId value for which the row needs to be retrieved.
	* @return DTO Object representing a single row of the table based on the primary key information supplied.If there is no match, <code>null</code> is returned.
 **/ 

    public  KmDocumentMstr findByPrimaryKey(String documentId) throws KmDocumentMstrDaoException;


	/**
	 * @param fileId
	 */
	public void deleteDocument(String documentId, String updatedBy) throws KmException;


	/**
	 * @param circleId
	 * @param categoryId
	 * @param subCategoryId
	 * @param userId
	 * @return
	 */
	public ArrayList viewFiles(String circleId, String categoryId, String subCategoryId, String userId)throws KmException;

	/**
	 * @param keyword
	 * @param circleId
	 * @param uploadedBy
	 * @return file list
	 */
	public ArrayList keywordFileSearch(String keyword, String circleId, String uploadedBy)throws KmException;
	
	/**
	 * @param circleId
	 * @return file list
	 */
	public ArrayList retrieveCircleWideDocumentList(int[] circleId)throws KmDocumentMstrDaoException;
	
	/**
	 * 
	 * @param userId
	 * @param docId
	 * @return
	 * @throws KmException
	 */
	public int addToFavorites (int userId, int docId) throws KmException;
	
	/**
	 * 
	 * @param userId
	 * @param docId
	 * @return
	 * @throws KmException
	 */
	public int deleteFavorites (int userId, int docId) throws KmException;
	
	/**
	 * 
	 * @param userId
	 * @param docId
	 * @return
	 * @throws KmException
	 */
	public boolean checkForFavorite (int userId, int docId) throws KmException;
	
	/**
	 * 
	 * @param docId
	 * @param userIdS
	 * @return
	 * @throws KmException
	 */
	public int insertDocView (int docId, int userIdS) throws KmException;
	
	/**
	 * 
	 * @param circleId
	 * @return
	 * @throws KmDocumentMstrDaoException
	 */
	public ArrayList retrieveCircleWideDocumentListElements(int[] circleId)throws KmDocumentMstrDaoException;

	/**
	 * 
	 * @param documentId
	 * @return
	 * @throws KmException
	 */
	public String[] getDocumentName(String[] documentId) throws KmException;

	/**
	 * 
	 * @param root
	 * @param parentId
	 * @return
	 * @throws KmException
	 */
	public ArrayList viewFiles(String root,String parentId)throws KmException;
	/**
		 * @param movedDocumentList
		 * @param oldPath
		 * @param newPath
	*/
	public void changePath(String[] movedDocumentList, String oldPath, String newPath) throws KmException;
	//added by vishwas
	public void changePathcorrect(String movedDocument, String oldPath, String newPath) throws KmException;
	public String insertCopyPath(String movedDocument, String oldPath, String newPath,String elementid1) throws KmException;
//end by vishwas
	/**
	 * @param elementId List
	 * @return documentId List
	 */
	public String[] getDocumentIds(String[] movedDocumentList)throws KmException;

	/**
	 * @param documentId
	 * @return
	 */
	public String getDocumentPath(String documentId)throws KmException;

	/**
	 * @param documentId
	 * @return
	 */
	public String getElementId(String documentId) throws KmException;
	
	public String getElementId() throws KmException;

	/**
	 * @param documentId
	 * @return
	 */
	public KmDocumentMstr getDocumentDto(String documentId)  throws KmException;

	/**
	 * @param dto
	 */
	public void editDocument(KmDocumentMstr dto) throws KmException;

	/**
	 * @param documentName
	 */
	public String getDocumentId(String documentName) throws KmException;

	/**
	 * @param documentId
	 * @return
	 */
	public ArrayList getDocumentVersions(String documentId)throws KmException;

	/**
	 * @param parentId
	 */
	public String[] getDocuments(String parentId)throws KmException;

	/**
	 * @param documents
	 */
	public void deleteDocuments(String[] documents, String updatedBy)throws KmException;

	/**
	 * @param circleId
	 */
	public String getCircleApprover(String circleId)throws KmException;

	/**
	 * @param documentId
	 * @return
	 */
	public String getDocPath(String documentId)throws KmException;

	/**
	 * @param movedElementList
	 * @return
	 */
	public String[] changeDocumentPathsInDb(String[] documentElementIds)throws KmException;

	/**
	 * @param elementId
	 * @return
	 */
	public boolean changeDocumentPathsInDb(String elementId)throws KmException;

	/**
	 * @param allDocuments
	 * @param alldocumentPaths
	 * @return
	 */
	public boolean updateDocumentPaths(String[] allDocuments, String[] alldocumentPaths)throws KmException;

	/**
	 * @param documentId
	 * @return
	 */
	public KmDocumentMstr getDocumentDetails(String documentId)throws KmException;
	
	
	
	/*  KM PHASE II dao for archived search   */ 
		 /**
		 * @param dto
		 * @return
		 */
		  ArrayList archivedSearch(KmDocumentMstr dto) throws KmException;

	/*  KM PHASE II dao for csrkeyword search   */ 
			 /**
			 * @param dto
			 * @return
			 */
		 ArrayList csrKeywordSearch(KmSearch dto) throws KmException;
		 /**
		  * 
		  * @param subCategoryId
		  * @return
		  * @throws KmException
		  * @throws DAOException
		  */
		public KmDocumentMstr getSingleDoc(String subCategoryId)throws KmException,DAOException;


		public KmDocumentMstr getDocumentByElementId(String elementId) throws KmException;
		
		public ArrayList<String> docFilterAsPerDocType(ArrayList<String> docIdList,int docType,String isTop15) throws KmException;

		public ArrayList<KmDocumentMstr> getTopDocuments(String elementId, String docType) throws KmException ;

		public int numberOfSentSms(String mobileNo) throws KmException;

		public int getMaxNoOfSMS();
		
}
