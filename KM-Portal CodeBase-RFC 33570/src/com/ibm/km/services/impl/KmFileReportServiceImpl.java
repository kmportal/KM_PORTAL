package com.ibm.km.services.impl;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.ibm.km.dao.KmFileReportDao;
import com.ibm.km.dao.impl.KmFileReportDaoImpl;
import com.ibm.km.dto.FileReportDto;
import com.ibm.km.dto.KmElementMstr;
import com.ibm.km.exception.KmException;
import com.ibm.km.services.KmFileReportService;


/**
 * @author Anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class KmFileReportServiceImpl implements KmFileReportService{
	
	private static final Logger logger;

	static {

		logger = Logger.getLogger(KmCategoryMstrServiceImpl.class);
	}
		

	
	public ArrayList getApproverService(String circleId, String categoryId, String subCategoryId,String fromDate,String toDate)throws KmException 
	{
		ArrayList approverList=new ArrayList();
		KmFileReportDao dao=new KmFileReportDaoImpl();
		
			approverList=dao.getApproverList(circleId,categoryId,subCategoryId,fromDate,toDate);
			return approverList;
	}
	public ArrayList getHitService(String circleId, String categoryId, String subCategoryId,String fromDate,String toDate)throws KmException 
	{
		ArrayList hitList=new ArrayList();
		KmFileReportDao dao=new KmFileReportDaoImpl();
		
			hitList=dao.getHitList(circleId,categoryId,subCategoryId,fromDate,toDate);
			return hitList;
	}
	
	public ArrayList getAddedFileList(ArrayList circleList,String selectedDate)throws KmException 
	{
			ArrayList addFileList=new ArrayList();
			
			KmFileReportDao dao=new KmFileReportDaoImpl();
			
		       
			Iterator iterator = circleList.iterator();
			int count=0;
			KmElementMstr elementDto= null;
			for(Iterator i=circleList.iterator();i.hasNext();){
			
				elementDto=(KmElementMstr)i.next();
				count=Integer.parseInt(dao.getAddedFileCount(elementDto.getElementId(),selectedDate));
				elementDto.setDocumentCount(count+"");
				logger.info("Circle : "+elementDto.getElementName()+"   Count : "+count);
				addFileList.add(elementDto);
			}
			
			
				return addFileList;
	}
	public int getDeletedFileList(String elementId,String selectedDate)throws KmException 
			{
				//ArrayList hitList=new ArrayList();
				int count=0;
				KmFileReportDao dao=new KmFileReportDaoImpl();
				
					count=dao.getDeletedFileList(elementId,selectedDate);
				//	hitList=dao.getHitList(circleId,categoryId,subCategoryId,fromDate,toDate);
					return count;
			}
	
	public ArrayList getFileCircleList(String elementId,String selectedDate)throws KmException 
				{
					ArrayList circleList=new ArrayList();
					int count=0;
					KmFileReportDao dao=new KmFileReportDaoImpl();
					circleList=dao.getFileCircleList(elementId,selectedDate);
					return circleList;
				}
	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmFileReportService#getFileApprovedList(java.lang.String, java.lang.String)
	 */
	public ArrayList getFileApprovedList(String elementId, String selectedDate) throws KmException {
		ArrayList circleList=new ArrayList();
		int count=0;
		KmFileReportDao dao=new KmFileReportDaoImpl();
		circleList=dao.getFileApprovedList(elementId,selectedDate);
		return circleList;
	}
	public int getTotalFileCount(String elementId,String selectedDate)throws KmException 
				{
					//ArrayList hitList=new ArrayList();
					int count=0;
					KmFileReportDao dao=new KmFileReportDaoImpl();
						count=dao.getTotalFileCount(elementId);
					//	hitList=dao.getHitList(circleId,categoryId,subCategoryId,fromDate,toDate);
						return count;
				}
	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmFileReportService#getTotalFileCount(java.lang.String)
	 */
	public int getTotalFileCount(String elementId) throws KmException {
		// TODO Auto-generated method stub
		return 0;
	}
	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmFileReportService#getAddedFileCount(java.lang.String, java.lang.String)
	 */
	public int getAddedFileCount(String elementId, String selectedDate) throws KmException {
		int count=0;
		KmFileReportDao dao=new KmFileReportDaoImpl();
	//	count=dao.getAddedFileCount(elementId,selectedDate);
	    return count;
	}
	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmFileReportService#getDeletedFileList(java.util.ArrayList, java.lang.String)
	 */
	public ArrayList getDeletedFileList(ArrayList circleList, String selectedDate)throws KmException {
		ArrayList deletedFileList=new ArrayList();
			
			KmFileReportDao dao=new KmFileReportDaoImpl();
			
		       
			Iterator iterator = circleList.iterator();
			int count=0;
			KmElementMstr elementDto= null;
			for(Iterator i=circleList.iterator();i.hasNext();){
			
				elementDto=(KmElementMstr)i.next();
				count=Integer.parseInt(dao.getDeletedFileCount(elementDto.getElementId(),selectedDate));
				elementDto.setDocumentCount(count+"");
				logger.info("Circle : "+elementDto.getElementName()+"   Count : "+count);
				deletedFileList.add(elementDto);
			}
			
			
				return deletedFileList;
	}
	/* (non-Javadoc)
	 * @see com.ibm.km.services.KmFileReportService#getApprovedRejectedFileCount(java.lang.String)
	 */
	public FileReportDto getApprovedRejectedFileCount(String elementId) throws KmException {
		KmFileReportDao dao=new KmFileReportDaoImpl();
		return dao.getFileCount(elementId);
		
	}
}
