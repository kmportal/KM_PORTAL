/*
 * Created on Feb 6, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.ibm.km.common.FileCopy;
import com.ibm.km.common.PropertyReader;
import com.ibm.km.common.Utility;
import com.ibm.km.dao.KmCsrLatestUpdatesDao;
import com.ibm.km.dao.KmDocumentMstrDao;
import com.ibm.km.dao.impl.KmCsrLatestUpdatesDaoImpl;
import com.ibm.km.dao.impl.KmDocumentMstrDaoImpl;
import com.ibm.km.dto.KmDocumentMstr;
import com.ibm.km.dto.KmSearch;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmDocumentMstrDaoException;
import com.ibm.km.exception.KmException;
import com.ibm.km.search.IndexFiles;
import com.ibm.km.services.KmDocumentService;
import com.ibm.km.services.KmElementMstrService;

/**
 * @author namangup
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class KmDocumentServiceImpl implements KmDocumentService {
	private static final Logger logger;

	static {

		logger = Logger.getLogger(LoginServiceImpl.class);
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmDocumentService#retrieveCSRDocumentList(int)
	 */
	public ArrayList  retrieveCSRDocumentList(int[] circleID) throws KmException {
		KmDocumentMstrDao retrieveListObj=new KmDocumentMstrDaoImpl();
		logger.info("Retrieving Circle Wide Document List from Document Service");
		try{
			return retrieveListObj.retrieveCircleWideDocumentListElements(circleID);
		}catch (KmDocumentMstrDaoException e) {
			logger.error("SQL Exception occured while Retrieving Circle Wide Document List from Document Service." + "Exception Message: " + e.getMessage());
				throw new KmException("SQLException: " + e.getMessage(), e);
			}
		
		
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmDocumentService#getDocument(java.lang.String)
	 */
	public KmDocumentMstr getDocument(String documentID) throws KmException {
		KmDocumentMstrDaoImpl retrieveDocObj=new KmDocumentMstrDaoImpl();
		logger.info("Retrieving Circle Wide Document List from Document Service");
		try{
			return retrieveDocObj.findByPrimaryKey(documentID);
		}catch (KmDocumentMstrDaoException e) {
			logger.error("SQL Exception occured while Retrieving Circle Wide Document List from Document Service." + "Exception Message: " + e.getMessage());
				throw new KmException("SQLException: " + e.getMessage(), e);
			}
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmDocumentService#increaseDocHitCount(java.lang.String)
	 */
	public KmDocumentMstr increaseDocHitCount(String documentID, String userId) throws KmException {
		
		KmDocumentMstrDaoImpl retrieveDocObj=new KmDocumentMstrDaoImpl();
		logger.info("Retrieving Circle Wide Document List from Document Service");
		try{
			KmDocumentMstr docBean=retrieveDocObj.findByPrimaryKey(documentID);
			
			docBean.setNumberOfHits((Integer.parseInt(docBean.getNumberOfHits())+1)+"");
			retrieveDocObj.update(docBean);
			retrieveDocObj.insertDocView(Integer.parseInt(documentID), Integer.parseInt(userId));
			return docBean;
		}catch (KmDocumentMstrDaoException e) {
			logger.error("SQL Exception occured while Retrieving Circle Wide Document List from Document Service." + "Exception Message: " + e);
				throw new KmException("SQLException: " + e.getMessage(), e);
			}
	}
	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmSearchFileService#viewFileService(java.lang.String)
	 */
	public ArrayList viewFileService(String root,String parentId) throws KmException{
		ArrayList fileList=null;
		KmDocumentMstrDao dao=new KmDocumentMstrDaoImpl();
			
			fileList=dao.viewFiles(root,parentId);
			return fileList;
	}






	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmSearchFileService#deleteFileService(java.lang.String)
	 */
	public void deleteFileService(String documentId, String updatedBy) throws KmException {
		KmDocumentMstrDao dao=new KmDocumentMstrDaoImpl();
		KmElementMstrService elementService=new KmElementMstrServiceImpl();
		String elementId=elementService.getElementId(documentId);
		String[] element={elementId}; 
	//	elementService.updateElementStatus(documentId);
		dao.deleteDocument(documentId,updatedBy);

		
		try {
		KmCsrLatestUpdatesDao kmCsrLatestUpdatesDao = new KmCsrLatestUpdatesDaoImpl();
		int[] j =  new int[] {Integer.parseInt(documentId)};
		kmCsrLatestUpdatesDao.deleteDocumentEntry(j);
		} catch (Exception e) {
			logger.info("Exception:" + e);
			e.printStackTrace();
		}
		
	}

		public String isFavouriteDocument(String documentId, String userId) throws KmException {
			KmDocumentMstrDao dao=new KmDocumentMstrDaoImpl();
			return dao.isFavouriteDocument(documentId,userId);
		}
		

	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmFileService#keywordFileSearch(java.lang.String, java.lang.String, java.lang.String)
	 */
	public ArrayList keywordFileSearch(String keyword, String circleId, String uploadedBy) throws KmException{
		ArrayList fileList=null;
		KmDocumentMstrDao dao=new KmDocumentMstrDaoImpl();
		
			fileList=dao.keywordFileSearch(keyword, circleId, uploadedBy);
			return fileList;
	}

	public int addToFavorites(String userId, String docId) throws KmException {
		KmDocumentMstrDao dao=new KmDocumentMstrDaoImpl();
		int status = dao.addToFavorites(Integer.parseInt(userId),Integer.parseInt(docId));
		return status;
	}
	
	public int deleteFavorites(String userId, String docId) throws KmException {
		KmDocumentMstrDao dao=new KmDocumentMstrDaoImpl();
		int status = dao.deleteFavorites(Integer.parseInt(userId),Integer.parseInt(docId));
		return status;
	}
	
	public boolean checkForFavorite (String userId, String docId) throws KmException {
		KmDocumentMstrDao dao=new KmDocumentMstrDaoImpl();
		boolean status = dao.checkForFavorite(Integer.parseInt(userId),Integer.parseInt(docId));
		return status;
	}
	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmElementMstrService#getDocumentName(java.lang.String)
	 */

	public String[] getDocumentName(String[] documentId) throws KmException {

		KmDocumentMstrDao dao=new KmDocumentMstrDaoImpl();
		return dao.getDocumentName(documentId);
	}
	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmElementMstrService#changePath(java.lang.String[], java.lang.String, java.lang.String)
	 */
	public boolean changePath(String[] movedDocumentList, String oldPath, String newPath) throws KmException {
		KmDocumentService documentService=new KmDocumentServiceImpl();
		KmElementMstrService elementService= new KmElementMstrServiceImpl();
		FileCopy copy=new FileCopy();
		FileInputStream out;
		boolean success=false;
		List<String> al =new ArrayList<String>();
		try{
			String[] moveDocumentNames=new String[movedDocumentList.length];
			String[] moveDocumentIds=new String[movedDocumentList.length];
			//Fetching documentID list
			moveDocumentIds=documentService.getDocumentIds(movedDocumentList);
			
			String oldFilePath;
			String newFilePath;
			ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
			//Physical movement of the document
			String path=bundle.getString("folder.path");
			moveDocumentNames=documentService.getDocumentName(moveDocumentIds);
			logger.info("elementidlist  	:"+movedDocumentList.length);
			logger.info("documentidlist 	:"+moveDocumentIds.length);
			logger.info("elementnamelist	:"+moveDocumentNames.length);
			
			for(int i=0;i<moveDocumentIds.length;i++)
			{
			//	moveDocumentNames=documentService.getDocumentName(movedDocumentList[i]);	
				
				oldFilePath=path+oldPath+"/"+moveDocumentNames[i];
				newFilePath=path+newPath+"/"+moveDocumentNames[i];
				logger.info("Old: "+oldFilePath+" New: "+newFilePath);
				File oldFile=new File(oldFilePath);
				File newFile=new File(newFilePath);
				try{
					copy.copy(oldFilePath,newFilePath);
				}
				catch(FileNotFoundException fe){
					logger.error("File not found during document move.. Old path :"+oldPath+"      New Path :" +newPath);
					logger.info("File not found during document move document move "+Utility.getStackTrace(fe));
				}
			//Deleting the document form the old location	
				success=oldFile.delete();
			//	success=oldFile.renameTo(newFile);
				//System.out.println("success-------------------"+success);
				al.add(String.valueOf(success));
				if(success==false){
					System.err.println("Unable to Move Document: "+oldFilePath+" to: "+newFilePath);
				}
			
		if(success==true)
		{
			KmDocumentMstrDao dao=new KmDocumentMstrDaoImpl();
			dao.changePath(moveDocumentIds, oldPath, newPath);
		}
			}

			//KmDocumentMstrDao dao=new KmDocumentMstrDaoImpl();
			//dao.changePath(moveDocumentIds, oldPath, newPath);
			
		}catch(Exception e){
			logger.error("File not found during document move.. Old path " +Utility.getStackTrace(e));
			e.printStackTrace();
			throw new KmException("Exception: " + e.getMessage());	
		}
		return success;
	}
	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmElementMstrService#getAllDocuments(java.lang.String)
	 */
	//added by vishwas
	public ArrayList changePath1(String[] movedDocumentList, String oldPath, String newPath) throws KmException {
		KmDocumentService documentService=new KmDocumentServiceImpl();
		KmElementMstrService elementService= new KmElementMstrServiceImpl();
		FileCopy copy=new FileCopy();
		FileInputStream out;
		boolean success=false;
		String logic="";
		ArrayList<String> al =new ArrayList<String>();
		ArrayList<String> all =new ArrayList<String>();
		try{
			String[] moveDocumentNames=new String[movedDocumentList.length];
			String[] moveDocumentIds=new String[movedDocumentList.length];
			//Fetching documentID list
			moveDocumentIds=documentService.getDocumentIds(movedDocumentList);
			//System.out.println("moveDocumentIds"+moveDocumentIds[0]+":"+moveDocumentIds[1]);
			//System.out.println("moveDocumentIds"+moveDocumentIds);
			String oldFilePath;
			String newFilePath;
			ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
			//Physical movement of the document
			String path=bundle.getString("folder.path");
			moveDocumentNames=documentService.getDocumentName(moveDocumentIds);
			//System.out.println("moveDocumentNames"+moveDocumentNames);
			//System.out.println("moveDocumentNames"+moveDocumentNames.toString());
			logger.info("elementidlist  moveDocumentNames	:"+movedDocumentList.length+":"+moveDocumentNames);
			logger.info("documentidlist 	:"+moveDocumentIds.length+":"+moveDocumentIds);
			logger.info("elementnamelist	:"+moveDocumentNames.length);
			
			for(int i=0;i<moveDocumentIds.length;i++)
			{
			//	moveDocumentNames=documentService.getDocumentName(movedDocumentList[i]);	
				//System.out.println("hello movedocument"+moveDocumentNames[i]);
				oldFilePath=path+oldPath+"/"+moveDocumentNames[i];
				newFilePath=path+newPath+"/"+moveDocumentNames[i];
				logger.info("Old: "+oldFilePath+" New: "+newFilePath);
				File oldFile=new File(oldFilePath);
				File newFile=new File(newFilePath);
				try{
					logic=copy.copy1(oldFilePath,newFilePath);
					logger.info("logic: "+logic+" New: "+newFilePath);
				}
				catch(FileNotFoundException fe){
					logger.error("File not found during document move.. Old path :"+oldPath+"      New Path :" +newPath);
					logger.info("File not found during document move document move "+Utility.getStackTrace(fe));
				}
			//Deleting the document form the old location	
			if(logic!="blank")
			{
				success=oldFile.delete();
			//	success=oldFile.renameTo(newFile);
			//	System.out.println("success-------------------"+success);
				al.add(String.valueOf(success));
				String elementid=documentService.getElementId(moveDocumentIds[i]);
				all.add(elementid);
				if(success==false){
					System.err.println("Unable to Move Document: "+oldFilePath+" to: "+newFilePath);
				}
			
		if(success==true)
		{
			KmDocumentMstrDao dao=new KmDocumentMstrDaoImpl();
			//dao.changePath(moveDocumentIds, oldPath, newPath);
			dao.changePathcorrect(moveDocumentIds[i], oldPath, newPath);
		}
			}
			else
			{
				String s="Nofile";
				al.add(s);
					System.err.println("Unable to Move Document: "+oldFilePath+" to: "+newFilePath+":"+"file does not exist in the given path in impl"+oldFilePath);
					logger.info("file does not exist in the given path in impl"+oldFilePath);
					
			}

			//KmDocumentMstrDao dao=new KmDocumentMstrDaoImpl();
			//dao.changePath(moveDocumentIds, oldPath, newPath);
			}
		}catch(Exception e){
			logger.error("File not found during document move.. Old path " +Utility.getStackTrace(e));
			e.printStackTrace();
			throw new KmException("Exception: " + e.getMessage());	
		}
		return all;
	}

	//end by vishwas
	
	//added by vishwas for copy document
	public ArrayList copyPath1(String[] movedDocumentList, String oldPath, String newPath,String elementid1 ) {
		KmDocumentService documentService=new KmDocumentServiceImpl();
		KmElementMstrService elementService= new KmElementMstrServiceImpl();
		FileCopy copy=new FileCopy();
		FileInputStream out;
		boolean success=false;
		String logic="";
		ArrayList<String> al =new ArrayList<String>();
		ArrayList<String> all =new ArrayList<String>();
		ArrayList<Object> all2 =new ArrayList<Object>();
		
		int elementid2=0;
		try{
			String[] moveDocumentNames=new String[movedDocumentList.length];
			String[] moveDocumentIds=new String[movedDocumentList.length];
			//Fetching documentID list
			moveDocumentIds=documentService.getDocumentIds(movedDocumentList);
			//System.out.println("moveDocumentIds"+moveDocumentIds[0]+":"+moveDocumentIds[1]);
			//System.out.println("moveDocumentIds"+moveDocumentIds);
			String oldFilePath;
			String newFilePath;
			ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
			//Physical movement of the document
			String path=bundle.getString("folder.path");
			moveDocumentNames=documentService.getDocumentName(moveDocumentIds);
			//System.out.println("moveDocumentNames"+moveDocumentNames);
			//System.out.println("moveDocumentNames"+moveDocumentNames.toString());
			logger.info("elementidlist  moveDocumentNames	:"+movedDocumentList.length+":"+moveDocumentNames);
			logger.info("documentidlist 	:"+moveDocumentIds.length+":"+moveDocumentIds);
			logger.info("elementnamelist	:"+moveDocumentNames.length);
			
			for(int i=0;i<moveDocumentIds.length;i++)
			{
			//	moveDocumentNames=documentService.getDocumentName(movedDocumentList[i]);	
				//System.out.println("hello movedocument"+moveDocumentNames[i]);
				oldFilePath=path+oldPath+"/"+moveDocumentNames[i];
				newFilePath=path+newPath+"/"+moveDocumentNames[i];
				String newFilePath1=path+newPath+"/"+"_plain"+moveDocumentNames[i];
				logger.info("Old: "+oldFilePath+" New: "+newFilePath);
				File oldFile=new File(oldFilePath);
				File newFile=new File(newFilePath);
				boolean logic2=newFile.exists();
				if(logic2!=true)
				{
				try{
					logic=copy.copy1(oldFilePath,newFilePath);
					logger.info("logic: "+logic+" New: "+newFilePath);
				}
				catch(FileNotFoundException fe){
					logger.error("File not found during document move.. Old path :"+oldPath+"      New Path :" +newPath);
					logger.info("File not found during document move document move "+Utility.getStackTrace(fe));
				}
			//Deleting the document form the old location	
			
				if(logic!="blank")
			{
				success=oldFile.exists();
			//	success=oldFile.renameTo(newFile);
				System.out.println("success-------------------"+success);
				//al.add(String.valueOf(success));
				String elementid=documentService.getElementId(moveDocumentIds[i]);
				
				all.add(elementid);
				if(success==false){
					System.err.println("Unable to Move Document: "+oldFilePath+" to: "+newFilePath);
				}
			
		if(success==true)
		{
			KmDocumentMstrDao dao=new KmDocumentMstrDaoImpl();
			//dao.changePath(moveDocumentIds, oldPath, newPath);
			 String[] elements = newFilePath.split("/");
				String circleId = elements[4];
				logger.info("circleId :"+circleId);
				IndexFiles indexObject = new IndexFiles();
				logger.info("moveDocumentIds[i] :"+moveDocumentIds[i]);
			
			String docId=dao.insertCopyPath(moveDocumentIds[i], oldPath, newPath,elementid1);
			logger.info("docId :"+docId);
			if(PropertyReader.getAppValue("SINGLE.INDEX").equals("Y"))
			{	
				logger.info("xmlFileName :"+newFilePath);
				indexObject.initIndex(new File(newFilePath), docId,circleId);
				indexObject.initIndex(new File(newFilePath1), docId,circleId);
			}	
			else{
				logger.info("xmlFileName else:"+newFilePath);
				indexObject.initIndexNew(new File(newFilePath),docId,circleId);
				indexObject.initIndexNew(new File(newFilePath1),docId,circleId);
			}
			
			elementid1=elementid1+i;
		}
			}
			
			else
			{
				String s="Nofile";
				al.add(s);
					System.err.println("Unable to Copy Document: "+oldFilePath+" to: "+newFilePath+":"+"file does not exist in the given path in impl"+oldFilePath);
					logger.info("file does not exist in the given path in impl"+oldFilePath);
					
			}
				}
				else
				{
					System.err.println(newFilePath+":"+"file allready exist in the given path in impl");
					logger.info("file allready exist in the given path in impl"+newFilePath);
					
				}

			all2.add(all);
			System.out.println(all.size()+"hello logic for copyyyyyyyyyy"+logic2);
			all2.add(logic2);
				//KmDocumentMstrDao dao=new KmDocumentMstrDaoImpl();
			//dao.changePath(moveDocumentIds, oldPath, newPath);
			}
		}catch(Exception e){
			logger.error("File not found during document move.. Old path " +Utility.getStackTrace(e));
			e.printStackTrace();
			try {
				throw new KmException("Exception: " + e.getMessage());
			} catch (KmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	
		}
		return all2;
	}
	
	public String getElementId() throws KmException {
		KmDocumentMstrDao dao=new KmDocumentMstrDaoImpl();
		return dao.getElementId();
	}
	//end by vishwas for copy document
	
	
	public ArrayList getAllDocuments(String parentId) throws KmException {
		KmDocumentMstrDao dao=new KmDocumentMstrDaoImpl();
		ArrayList documentList=new ArrayList();
		//documentList=dao.l(parentId,"0");
		return  documentList;
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmDocumentService#getDocumentIds(java.lang.String[])
	 */
	public String[] getDocumentIds(String[] movedDocumentList)throws KmException {
		KmDocumentMstrDao dao=new KmDocumentMstrDaoImpl();
		return dao.getDocumentIds(movedDocumentList);
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmDocumentService#getDocumentPath(java.lang.String)
	 */
	public String getDocumentPath(String documentId) throws KmException {
		String documentPath="";
		KmDocumentMstrDao dao=new KmDocumentMstrDaoImpl();
		documentPath=dao.getDocumentPath(documentId);
		
		documentPath=documentPath.substring(0,documentPath.lastIndexOf("/"));
		//logger.info("DOCUMENT          PATH :"+documentPath);
		return documentPath;
	
		
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmDocumentService#getElementId(java.lang.String)
	 */
	public String getElementId(String documentId) throws KmException {
		KmDocumentMstrDao dao=new KmDocumentMstrDaoImpl();
		return dao.getElementId(documentId)	;	

	}

	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmDocumentService#getDocumentDto(java.lang.String)
	 */
	public KmDocumentMstr getDocumentDto(String documentId) throws KmException {
		KmDocumentMstrDao dao=new KmDocumentMstrDaoImpl();
		return dao.getDocumentDto(documentId);
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmDocumentService#editDocument(com.ibm.km.dto.KmDocumentMstr)
	 */
	public void editDocument(KmDocumentMstr dto) throws KmException {
		KmDocumentMstrDao dao=new KmDocumentMstrDaoImpl();
		dao.editDocument(dto);
	}
	
	public String getDocumentId(String documentName) throws KmException {
		KmDocumentMstrDao dao=new KmDocumentMstrDaoImpl();
		return dao.getDocumentId(documentName);
	}
	public ArrayList getDocumentVersions(String documentId) throws KmException {
			KmDocumentMstrDao dao=new KmDocumentMstrDaoImpl();
			return dao.getDocumentVersions(documentId);
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmDocumentService#getDocuments(java.lang.String)
	 */
	public String[] getDocuments(String parentId) throws KmException {
		KmDocumentMstrDao dao=new KmDocumentMstrDaoImpl();
		return dao.getDocuments(parentId);
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmDocumentService#deleteDocuments(java.lang.String[])
	 */
	public void deleteDocuments(String[] documents,String updatedBy) throws KmException {
		KmDocumentMstrDao dao=new KmDocumentMstrDaoImpl();
		dao.deleteDocuments(documents,updatedBy);
		
		try {
			KmCsrLatestUpdatesDao kmCsrLatestUpdatesDao = new KmCsrLatestUpdatesDaoImpl();
			int[] j =  new int[documents.length];
			for(int i = 0; i < documents.length; i++) {
				j[i] = Integer.parseInt(documents[i]);
			}
			kmCsrLatestUpdatesDao.deleteDocumentEntry(j);
			} catch (Exception e) {
				logger.info("Exception:" + e);
				e.printStackTrace();
			}

		
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmDocumentService#getCircleApprover(java.lang.String)
	 */
	public String getCircleApprover(String circleId)throws KmException {
		KmDocumentMstrDao dao=new KmDocumentMstrDaoImpl();
		return dao.getCircleApprover(circleId);
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmDocumentService#getDocPath(java.lang.String)
	 */
	public String getDocPath(String documentId) throws KmException {
		KmDocumentMstrDao dao=new KmDocumentMstrDaoImpl();
		return dao.getDocPath(documentId);
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmDocumentService#changeDocumentPathInDb(java.lang.String[])
	 */
	public String[] changeDocumentPathsInDb(String[] documentElementIds) throws KmException , Exception{
		try{
		
		KmDocumentMstrDao dao=new KmDocumentMstrDaoImpl();
		
		return dao.changeDocumentPathsInDb(documentElementIds);
		}
		catch(Exception e){
			e.printStackTrace();	
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmDocumentService#changeDocumentPathsInDb(java.lang.String)
	 */
	public boolean changeDocumentPathsInDb(String elementId) throws KmException {
		KmDocumentMstrDao dao=new KmDocumentMstrDaoImpl();
		return dao.changeDocumentPathsInDb(elementId);
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmDocumentService#updateDocumentPaths(java.lang.String[], java.lang.String[])
	 */
	public boolean updateDocumentPaths(String[] allDocuments, String[] alldocumentPaths) throws KmException {
		KmDocumentMstrDao dao=new KmDocumentMstrDaoImpl();
		return dao.updateDocumentPaths(allDocuments,alldocumentPaths);
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmDocumentService#getDocumentDetails(java.lang.String)
	 */
	public KmDocumentMstr getDocumentDetails(String documentId) throws KmException {
		KmDocumentMstrDao dao=new KmDocumentMstrDaoImpl();
		return dao.getDocumentDetails(documentId);
	}
	/*  KM PHASE II service Implemetation for archived search   */
		   /* (non-Javadoc)
				* @see com.ibm.km.services.KmDocumentService#archivedSearch(com.ibm.km.dto.KmDocumentMstr)
				*/
			   public ArrayList archivedSearch(KmDocumentMstr dto) throws KmException {
				   KmDocumentMstrDao dao=new KmDocumentMstrDaoImpl();
					   return dao.archivedSearch(dto);	
			   }
			   
	/* (non-Javadoc)
			 * @see com.ibm.km.services.KmDocumentService#csrkeywordsearch(com.ibm.km.dto.KmDocumentMstr)
			 */
			public ArrayList csrKeywordSearch(KmSearch dto) throws KmException {
					KmDocumentMstrDao dao=new KmDocumentMstrDaoImpl();
						
					return dao.csrKeywordSearch(dto);	
			}

			public KmDocumentMstr getSingleDoc(String subCategoryId) throws KmException,DAOException {
				KmDocumentMstrDao dao= new KmDocumentMstrDaoImpl();
				return dao.getSingleDoc(subCategoryId);
			}

		
			public KmDocumentMstr getDocumentByElementId(String elementId) throws KmException {
				KmDocumentMstrDao dao= new KmDocumentMstrDaoImpl();
				return dao.getDocumentByElementId(elementId);
			}	
			
			public ArrayList<String> docFilterAsPerDocType(ArrayList<String> docIdList,int docType,String isTop15) throws KmException {
				KmDocumentMstrDao dao= new KmDocumentMstrDaoImpl();
				return dao.docFilterAsPerDocType(docIdList,docType, isTop15);
			}	
			
			public ArrayList<KmDocumentMstr> getTopDocuments(String elementId, String docType) throws KmException {
				KmDocumentMstrDao dao= new KmDocumentMstrDaoImpl();
				return dao.getTopDocuments(elementId,docType);
			}	
		

		
		
	
}
