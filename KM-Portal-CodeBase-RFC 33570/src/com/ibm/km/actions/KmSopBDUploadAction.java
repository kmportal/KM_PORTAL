package com.ibm.km.actions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;

import com.ibm.km.common.Constants;
import com.ibm.km.common.PropertyReader;
import com.ibm.km.dao.AddFileDAO;
import com.ibm.km.dao.KmDocumentMstrDao;
import com.ibm.km.dao.impl.AddFileDaoImpl;
import com.ibm.km.dao.impl.KmDocumentMstrDaoImpl;
import com.ibm.km.dto.KmCsrLatestUpdatesDto;
import com.ibm.km.dto.KmDocumentMstr;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.KmSopBDUploadFormBean;
import com.ibm.km.forms.KmSopUploadFormBean;
import com.ibm.km.search.IndexFiles;
import com.ibm.km.services.KmCsrLatestUpdatesService;
import com.ibm.km.services.KmDocumentService;
import com.ibm.km.services.KmElementMstrService;
import com.ibm.km.services.KmSopBDUploadService;
import com.ibm.km.services.KmSopUploadService;
import com.ibm.km.services.impl.KmCsrLatestUpdatesServiceImpl;
import com.ibm.km.services.impl.KmDocumentServiceImpl;
import com.ibm.km.services.impl.KmElementMstrServiceImpl;
import com.ibm.km.services.impl.KmSopBDUploadServiceImpl;
import com.ibm.km.services.impl.KmSopUploadServiceImpl;

/**
 * @author Kundan Kumar
 * @since 5th Nov, 2012
 */
public class KmSopBDUploadAction extends DispatchAction {
	private static final Logger logger;
	static {
		logger = Logger.getLogger(KmSopBDUploadAction.class);
	}

	public ActionForward init(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
				request.getSession().setAttribute("SAVE_SOP_BD_DATA", "true");
				KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
				KmSopBDUploadFormBean formBean = (KmSopBDUploadFormBean)form;
				logger.info(userBean.getUserLoginId() + " entered init method for sop BD upload page.");
				formBean.reset(mapping,request);
				formBean.setCreatedBy(userBean.getElementId());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
				String date = sdf.format(new java.util.Date());
				date = date.substring(0,10);		
				if(formBean.getPublishDt().equals(""))
					formBean.setPublishDt(date);
				if(formBean.getEndDt().equals(""))
					formBean.setEndDt(date);
				
				logger.info(userBean.getUserLoginId() + " exited init method for sop bd upload page.");
				return mapping.findForward("initialize");
				
		}
	

	public ActionForward insertSOPBD(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
			throws Exception { 
	/*	if ( !isTokenValid(request) ) {
			return init(mapping, form, request, response);
	    }*/
		
		if ( request.getSession().getAttribute("SAVE_SOP_BD_DATA") == null ) {
			  return init(mapping, form, request, response);
			}
				request.getSession().setAttribute("SAVE_SOP_BD_DATA", null);
				
		KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
		ActionErrors errors = new ActionErrors();
		ActionForward forward = new ActionForward(); // return value
		ActionMessages messages = new ActionMessages();
		logger.info(userBean.getUserLoginId() + " inside insertSOPBD.");
		KmSopBDUploadFormBean kmSopBDUploadFormBean = (KmSopBDUploadFormBean)form;
		kmSopBDUploadFormBean.setCreatedBy(userBean.getUserId());		
		String fileName="";
		String plainFileName="";
	    KmSopBDUploadService sopBDUploadService = new KmSopBDUploadServiceImpl();	
	    Map<String, String> statusMap = new HashMap<String, String>();
	    
		try {
			    KmDocumentMstr documentDto ;
			    ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
				String folderPath = bundle.getString("folder.path");
				String circles[] = kmSopBDUploadFormBean.getCreateMultiple();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH-mm-ss-S");
				String date = null;
				String documentId="";
			    KmElementMstrService  kmElementMstrService  = new KmElementMstrServiceImpl();			   
			   
			    logger.info("kmSopBDUploadFormBean.getElementFolderPath() : " +kmSopBDUploadFormBean.getElementFolderPath());
			    
			    String[] elementFolderPath = kmSopBDUploadFormBean.getElementFolderPath().split("/");
				circles[0] = elementFolderPath[2];
				
			    Map<String, String> elementPathMap = kmElementMstrService.getElementPathList(kmSopBDUploadFormBean.getElementFolderPath(), circles);
			    
				for(int yy=0; yy< circles.length; yy++ )
				{
					//System.out.println(circles[yy]+" path : "+ elementPathMap.get(circles[yy]));
					if(null != elementPathMap.get(circles[yy]))
					{
						////System.out.println("checking folderPath+elementPathMap.get(circles[yy]) : "+folderPath+elementPathMap.get(circles[yy]));	
						if( ! (new File(folderPath+elementPathMap.get(circles[yy])).exists()))
						{
							statusMap.put(circles[yy], "Directory doesn't exist");
							logger.info("Path doesn't exist for : "+folderPath+elementPathMap.get(circles[yy]));
							continue;
						}
						date = sdf.format(new java.util.Date());
						fileName = folderPath+elementPathMap.get(circles[yy])+ date+ ".xml";
						plainFileName = folderPath+elementPathMap.get(circles[yy])+ date+ "_plain" + ".xml";
						kmSopBDUploadFormBean.setXmlFileNameContentPlainText(plainFileName);
						kmSopBDUploadFormBean.setXmlFileName(fileName);

						//System.out.println(" XmlFileName : "+kmSopBDUploadFormBean.getXmlFileName());
						
						// updating element path for each circle.
						kmSopBDUploadFormBean.setElementFolderPath(elementPathMap.get(circles[yy]));
						
						documentDto = sopBDUploadService.saveProductDetails(kmSopBDUploadFormBean);
						
						if(!documentDto.isValidFile())
						{
							errors.add("error",new ActionError("not.valid.content"));
							saveErrors(request, errors);
							return mapping.findForward("initialize"); 
						}
					
						if ("".equals(kmSopBDUploadFormBean.getMessage()))
						{
					    	AddFileDAO addFileDAO = new AddFileDaoImpl();  
					        documentId = addFileDAO.saveFileInfoInDB(documentDto);
					    				
					        insertLatestUpdateRecord(documentDto, documentId);
							
					    	//Code to update the Index for the above document in Pending state
							IndexFiles indexObject = new IndexFiles();
							
							//System.out.println("documentPath..."+fileName);
							
							if(PropertyReader.getAppValue("SINGLE.INDEX").equals("Y")){	
								
								//System.out.println("Calling index creation method : initIndex for "+kmSopBDUploadFormBean.getXmlFileName());
								indexObject.initIndex(new File(fileName), documentId,circles[yy]);
								indexObject.initIndex(new File(plainFileName), documentId,circles[yy]);
							}	
							else{
								//System.out.println("Calling index creation method : initIndexNew for "+ kmSopBDUploadFormBean.getXmlFileName());
								indexObject.initIndexNew(new File(fileName), documentId,circles[yy]);
								indexObject.initIndexNew(new File(plainFileName), documentId,circles[yy]);
							}
							statusMap.put(circles[yy], "Uploaded Successfully");
							}
						}
						else
						{
							logger.info("Category doesn't exist for : "+circles[yy]);
							statusMap.put(circles[yy], "Category doesn't exist");
						}
					}
			 kmSopBDUploadFormBean.setXmlFileName(fileName);
			 kmSopBDUploadFormBean.setDocId(documentId);
			    String uploadedCircles = "";
			    String missingCategory = "";
			    String missingDirectory = "";
			    boolean uploadedS  = false;
			    boolean uploadedC  = false;
			    boolean uploadedF = false;
			    String  statusMessage = "";
			    
			    
			    HashMap<String, String> circleDescMap = kmElementMstrService.getAllCircleDesc();
			    
			    for (int i = 0; i < circles.length; i++) {
					if("Uploaded Successfully".equals(statusMap.get(circles[i])))
					{  
						uploadedS = true;
						uploadedCircles = uploadedCircles + ", "+circleDescMap.get(circles[i]);
					}
					if("Category doesn't exist".equals(statusMap.get(circles[i])))
					{  
						uploadedC = true;
						missingCategory = missingCategory + ", "+circleDescMap.get(circles[i]);
					}
					if("Directory doesn't exist".equals(statusMap.get(circles[i])))
					{  
						uploadedF = true;
						missingDirectory = missingDirectory + ", "+circleDescMap.get(circles[i]);
					}
					
				} 
			    
			    if(uploadedS)
			    	statusMessage = "<font color=green>SOP BD Uploaded Successfully for : </font>"+ uploadedCircles.replaceFirst(", ","");
			    if(uploadedC)
			    	statusMessage = statusMessage + "<br><font color=red>Category doesn't exist for :  </font>"+ missingCategory.replaceFirst(", ","");
			    if(uploadedF)
			    	statusMessage = statusMessage + "<br><font color=red>Directory doesn't exist for :  </font>"+ missingDirectory.replaceFirst(", ","");
			    
			request.setAttribute("statusMessage", statusMessage);
			 messages.add("msg1",new ActionMessage("sop.bd.created"));
			 saveMessages(request, messages);
		} catch (Exception e) {
		  e.printStackTrace();
			logger.error("Exception occured while uploading SOP BD Details :" + e);	
			errors.add("error",new ActionError("sop.bd.not.created"));
			saveErrors(request, errors);
			return mapping.findForward("initialize");
		}		
		return viewSopBDDetails(mapping,form,request,response);
	}


	private void insertLatestUpdateRecord(KmDocumentMstr documentDto,
			String documentId) throws KmException {
		KmCsrLatestUpdatesDto kmCsrLatestUpdatesDto = documentDto.getKmCsrLatestUpdatesDto();
		kmCsrLatestUpdatesDto.setDocumentId(documentId);
		kmCsrLatestUpdatesDto.setDocType(Constants.DOC_TYPE_SOP_BD);

		// save latest updates
		KmCsrLatestUpdatesService kmCsrLatestUpdatesService = new KmCsrLatestUpdatesServiceImpl();
		int recInsertCount = kmCsrLatestUpdatesService.insertLatestUpdates(kmCsrLatestUpdatesDto);
		if(recInsertCount>0)
		{
			logger.info("Letest Updates uploaded for Product Upload : "+kmCsrLatestUpdatesDto.getUpdateTitle());
		}
	}
	
	public ActionForward viewSopBDDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	throws Exception { 
		KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
		ActionErrors errors = new ActionErrors();
		String docId = "";
		KmSopBDUploadFormBean kmSopBDUploadFormBean = (KmSopBDUploadFormBean)form;
		KmDocumentService service = new KmDocumentServiceImpl();
		if(request.getParameter("docID")!=null)
		{
			docId = request.getParameter("docID").toString();
		}
		else		
		if(kmSopBDUploadFormBean.getDocId()!=null)
		{
		 	docId = kmSopBDUploadFormBean.getDocId().toString();
		}
		
		try
		{
			kmSopBDUploadFormBean.reset(mapping, request);
			// get fileName from DB
			kmSopBDUploadFormBean.setXmlFileName(service.getDocPath(docId) );	
			//System.out.println("viewSop BDDetails XmlFileName : "+kmSopBDUploadFormBean.getXmlFileName().trim());
			
			KmSopBDUploadService kmSopBDUploadService = new KmSopBDUploadServiceImpl();			
			kmSopBDUploadFormBean = kmSopBDUploadService.viewProductDetails(kmSopBDUploadFormBean);
			
			if(userBean.isCsr())
			{
				service.increaseDocHitCount(docId,userBean.getUserId());
			}
			
		}catch (Exception e) {
		    e.printStackTrace();
			logger.error("Exception occured while geting SOP BD details :" + e);	
			errors.add("error",new ActionError("sop.bd.not.found"));
			saveErrors(request, errors);
		}
			kmSopBDUploadFormBean.setKmActorId(userBean.getKmActorId());
			kmSopBDUploadFormBean.setDocId(docId);
			request.setAttribute("kmSopBDUploadFormBean",kmSopBDUploadFormBean);
			if(userBean.isCsr())
			{
				return mapping.findForward("viewSopBDDetailsCsr");	
			}
			return mapping.findForward("viewSopBDDetails");
	}
	
	public ActionForward viewEditSopBDDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	throws Exception { 
		KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
		ActionErrors errors = new ActionErrors();
		request.getSession().setAttribute("UPDATE_SOP_BD_DATA", "true");
		KmDocumentService service = new KmDocumentServiceImpl();
		KmSopBDUploadFormBean kmSopBDUploadFormBean = (KmSopBDUploadFormBean)form;
		String docId = "";
		
		if(request.getParameter("docID")!=null)
		{
			docId = request.getParameter("docID").toString();
		}
		else		
		if(kmSopBDUploadFormBean.getDocId()!=null)
		{
		   	docId = kmSopBDUploadFormBean.getDocId().toString();
		}
		try {
			kmSopBDUploadFormBean.setXmlFileName(service.getDocPath(docId) );	
			//System.out.println("view edit XmlFileName : "+kmSopBDUploadFormBean.getXmlFileName().trim());
			
			KmSopBDUploadService kmSopBDUploadService = new KmSopBDUploadServiceImpl();		
			kmSopBDUploadFormBean = kmSopBDUploadService.viewProductDetails(kmSopBDUploadFormBean);
		    	
		}catch (Exception e) {
	    e.printStackTrace();
		logger.error("Exception occured while uploading product :" + e);	
		errors.add("error",new ActionError("sop.bd.not.found"));
		saveErrors(request, errors);
		}
		
		kmSopBDUploadFormBean.setKmActorId(userBean.getKmActorId());
		kmSopBDUploadFormBean.setDocId(docId);
		request.setAttribute("kmSopBDUploadFormBean",kmSopBDUploadFormBean);
	    return mapping.findForward("viewEditSopBDDetails");
	}

public ActionForward updateSOPBD(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	throws Exception { 
	if ( request.getSession().getAttribute("UPDATE_SOP_BD_DATA") == null ) {
		  return init(mapping, form, request, response);
		}
	request.getSession().setAttribute("UPDATE_SOP_BD_DATA", null);
	
	KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
	ActionErrors errors = new ActionErrors();
	ActionMessages messages = new ActionMessages();
	
	KmSopBDUploadFormBean kmSopBDUploadFormBean = (KmSopBDUploadFormBean)form;
	kmSopBDUploadFormBean.setCreatedBy(userBean.getUserId());
	KmSopBDUploadService sopBDUploadService = new KmSopBDUploadServiceImpl();
	
	String documentId="";
	String xmlFileName="";
	String plainFileName="";
	try {
	    KmDocumentMstr documentDto ;
	    ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
		String folderPath = bundle.getString("folder.path");
		
		KmDocumentService service = new KmDocumentServiceImpl();
		
		documentId = kmSopBDUploadFormBean.getDocId();
		xmlFileName = service.getDocPath(documentId);
		
		//System.out.println(" old documentId "+documentId);
	    //System.out.println(" old XmlFileName : "+xmlFileName);
	    
	    String elementFolderPath = xmlFileName.substring(0,xmlFileName.lastIndexOf("/"));
	    
	    String[] elements = xmlFileName.split("/");
		String circleId = elements[2];
	    
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH-mm-ss-S");
		String date = sdf.format(new java.util.Date());	
		
		xmlFileName = folderPath+elementFolderPath+"/"+ date+ ".xml";		
		plainFileName=folderPath+elementFolderPath+"/"+ date+ "_plain" + ".xml";;

		elementFolderPath = elementFolderPath +"/"+ date+ ".xml";
		
	    //System.out.println(" old new elementFolderPath : "+elementFolderPath);
	    //System.out.println(" old new Circle element id : "+elements[2]);
	    
		//System.out.println(" New XmlFileName : "+xmlFileName);
		
		kmSopBDUploadFormBean.setXmlFileName(xmlFileName);
		kmSopBDUploadFormBean.setElementFolderPath(elementFolderPath);
		
		kmSopBDUploadFormBean.setXmlFileNameContentPlainText(plainFileName);
				// saving document...
				
				documentDto = sopBDUploadService.saveProductDetails(kmSopBDUploadFormBean);
				
				if(!documentDto.isValidFile())
				{
					errors.add("error",new ActionError("not.valid.content"));
					saveErrors(request, errors);
					return mapping.findForward("initialize"); 
				}
			
				if ("".equals(kmSopBDUploadFormBean.getMessage()))
				{
			    	AddFileDAO addFileDAO = new AddFileDaoImpl();  
			        documentId = addFileDAO.saveFileInfoInDB(documentDto);
			    				    	
			        insertLatestUpdateRecord(documentDto, documentId);
			        
			    	//Code to update the Index for the above document in Pending state
					IndexFiles indexObject = new IndexFiles();
					
					if(PropertyReader.getAppValue("SINGLE.INDEX").equals("Y"))
					{	
						indexObject.initIndex(new File(xmlFileName), documentId,circleId);
						indexObject.initIndex(new File(plainFileName), documentId,circleId);
					}	
					else{
						indexObject.initIndexNew(new File(xmlFileName), documentId,circleId);
						indexObject.initIndexNew(new File(plainFileName), documentId,circleId);
					}
				  // disabling previous document
				  KmDocumentMstrDao kmDocumentMstrDao  = new KmDocumentMstrDaoImpl(); 
			      kmDocumentMstrDao.deleteDocument(kmSopBDUploadFormBean.getDocId(), userBean.getUserId());
				}

		kmSopBDUploadFormBean.setXmlFileName(xmlFileName);
		kmSopBDUploadFormBean.setDocId(documentId);
		messages.add("msg1",new ActionMessage("sop.bd.updated"));
		saveMessages(request, messages);
		
	} catch (Exception e) {
	e.printStackTrace();
	logger.error("Exception occured while uploading SOP BD Details :" + e);	
	errors.add("error",new ActionError("sop.bd.not.updated"));
	saveErrors(request, errors);
	return mapping.findForward("initialize");
	}
	return viewSopBDDetails(mapping,form,request,response);
}

}
