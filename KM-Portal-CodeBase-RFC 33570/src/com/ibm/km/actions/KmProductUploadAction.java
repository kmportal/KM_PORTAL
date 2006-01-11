package com.ibm.km.actions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.ibm.km.dao.KmElementMstrDao;
import com.ibm.km.dao.impl.AddFileDaoImpl;
import com.ibm.km.dao.impl.KmDocumentMstrDaoImpl;
import com.ibm.km.dto.EmployeeAppreciationDTO;
import com.ibm.km.dto.KmCsrLatestUpdatesDto;
import com.ibm.km.dto.KmDocumentMstr;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.dto.ProductDetails;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.KmProductUploadFormBean;
import com.ibm.km.forms.KmSopUploadFormBean;
import com.ibm.km.search.IndexFiles;
import com.ibm.km.services.KmCsrLatestUpdatesService;
import com.ibm.km.services.KmDocumentService;
import com.ibm.km.services.KmElementMstrService;
import com.ibm.km.services.KmProductUploadService;
import com.ibm.km.services.KmSopUploadService;
import com.ibm.km.services.impl.KmCsrLatestUpdatesServiceImpl;
import com.ibm.km.services.impl.KmDocumentServiceImpl;
import com.ibm.km.services.impl.KmElementMstrServiceImpl;
import com.ibm.km.services.impl.KmProductUploadServiceImpl;
import com.ibm.km.services.impl.KmSopUploadServiceImpl;

/**
 * @author Kundan Kumar
 * @since 9th Oct, 2012
 */
public class KmProductUploadAction extends DispatchAction {
	private static final Logger logger;
	static {
		logger = Logger.getLogger(KmProductUploadAction.class);
	}

	public ActionForward init(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
				request.getSession().setAttribute("SAVE_PRODUCT_DATA", "true");
				KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
				KmProductUploadFormBean formBean = (KmProductUploadFormBean)form;
				logger.info(userBean.getUserLoginId() + " entered init method for product upload page.");
				formBean.reset(mapping,request);
				try {				 
					formBean.setCreatedBy(userBean.getElementId());
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
					String date = sdf.format(new java.util.Date());
					date = date.substring(0,10);		
					if(formBean.getPublishDt().equals(""))
						formBean.setPublishDt(date);
					if(formBean.getEndDt().equals(""))
						formBean.setEndDt(date);
				} catch (Exception e) {
					logger.error("Exception in Loading page for product upload : "+e.getMessage());
				}
				logger.info(userBean.getUserLoginId() + " exited init method for product upload page.");
				return mapping.findForward("initialize");
		}
	
	public ActionForward insertUpdate(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
			throws Exception { 
	
		if ( request.getSession().getAttribute("SAVE_PRODUCT_DATA") == null ) {
			  return init(mapping, form, request, response);
			}
				request.getSession().setAttribute("SAVE_PRODUCT_DATA", null);
				
		KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();
		logger.info(userBean.getUserLoginId() + " inside insertUpdate of KmProductUploadAction...");
		KmProductUploadFormBean kmProductUploadFormBean = (KmProductUploadFormBean)form;
		kmProductUploadFormBean.setCreatedBy(userBean.getUserId());		
		String fileName="";
		String plainFileName="";
		String documentId="";
		KmProductUploadService kmProductUploadService = new KmProductUploadServiceImpl();		
		Map<String, String> statusMap = new HashMap<String, String>();
	    
		try {
			   KmDocumentMstr documentDto ;
			   ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
			   String folderPath = bundle.getString("folder.path");
			   String circles[] = kmProductUploadFormBean.getCreateMultiple();
			    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH-mm-ss-S");
				String date = null;
				
			    KmElementMstrService  kmElementMstrService  = new KmElementMstrServiceImpl();
			    
			    if(null == circles){
			    	circles = new String[1];
			    	circles[0] = userBean.getElementId();
			    }
			    
			    logger.info("kmProductUploadFormBean.getElementFolderPath() : " +kmProductUploadFormBean.getElementFolderPath());
			    
			    Map<String, String> elementPathMap = kmElementMstrService.getElementPathList(kmProductUploadFormBean.getElementFolderPath(), circles);
			    
			    for(int yy=0; yy< circles.length; yy++ )
				{
					////System.out.println(circles[yy]+" path : "+ elementPathMap.get(circles[yy]));
					
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
						kmProductUploadFormBean.setXmlFileName(fileName);

						plainFileName = folderPath+elementPathMap.get(circles[yy])+ date+ "_plain" + ".xml";
						kmProductUploadFormBean.setXmlFileNameContentPlainText(plainFileName);
						
						////System.out.println(" XmlFileName : "+kmProductUploadFormBean.getXmlFileName());
						
						// updating element path for each circle.
						kmProductUploadFormBean.setElementFolderPath(elementPathMap.get(circles[yy]));
						
						documentDto = kmProductUploadService.saveProductDetails(kmProductUploadFormBean);
					
						if(!documentDto.isValidFile())
						{
							errors.add("error",new ActionError("not.valid.content"));
							saveErrors(request, errors);
							return mapping.findForward("initialize"); 
						}
						
						if ("".equals(kmProductUploadFormBean.getMessage()))
						{
					    	AddFileDAO addFileDAO = new AddFileDaoImpl();  
					        documentId = addFileDAO.saveFileInfoInDB(documentDto);
					    				    
					        insertLatestUpdateRecord(documentDto, documentId);
							
					    	//Code to update the Index for the above document in Pending state
							IndexFiles indexObject = new IndexFiles();
							
							if(PropertyReader.getAppValue("SINGLE.INDEX").equals("Y")){	
								indexObject.initIndex(new File(fileName), documentId,circles[yy]);
								indexObject.initIndex(new File(plainFileName), documentId,circles[yy]);
							}	
							else{
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
			    kmProductUploadFormBean.setXmlFileName(fileName);
			    kmProductUploadFormBean.setDocId(documentId);
			    
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
			    	statusMessage = "<font color=green>Product Uploaded Successfully for : </font>"+ uploadedCircles.replaceFirst(", ","");
			    if(uploadedC)
			    	statusMessage = statusMessage + "<br><font color=red>Category doesn't exist for :  </font>"+ missingCategory.replaceFirst(", ","");
			    if(uploadedF)
			    	statusMessage = statusMessage + "<br><font color=red>Directory doesn't exist for :  </font>"+ missingDirectory.replaceFirst(", ","");
			    
			    
			    
				messages.add("msg1",new ActionMessage("product.uploaded"));
				request.setAttribute("statusMessage", statusMessage);
				saveMessages(request, messages);
		} catch (Exception e) {
		  e.printStackTrace();
			logger.error("Exception occured while uploading product Details :" + e);	
			errors.add("error",new ActionError("product.not.uploaded"));
			saveErrors(request, errors);
			return mapping.findForward("initialize");
		}		
		return viewProductDetails(mapping,form,request,response);
	}

	private void insertLatestUpdateRecord(KmDocumentMstr documentDto, String documentId ) throws KmException {
		KmCsrLatestUpdatesDto kmCsrLatestUpdatesDto = documentDto.getKmCsrLatestUpdatesDto();
		kmCsrLatestUpdatesDto.setDocumentId(documentId);
		kmCsrLatestUpdatesDto.setDocType(Constants.DOC_TYPE_PRODUCT);

		// save latest updates
		KmCsrLatestUpdatesService kmCsrLatestUpdatesService = new KmCsrLatestUpdatesServiceImpl();
		int recInsertCount = kmCsrLatestUpdatesService.insertLatestUpdates(kmCsrLatestUpdatesDto);
		if(recInsertCount>0)
		{
			logger.info("Letest Updates uploaded for Product Upload : "+kmCsrLatestUpdatesDto.getUpdateTitle());
		}
	}
		
	public ActionForward viewProductDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	throws Exception { 
		KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
		ActionErrors errors = new ActionErrors();
		KmDocumentService service = new KmDocumentServiceImpl();
		KmProductUploadFormBean kmProductUploadFormBean = (KmProductUploadFormBean)form;
		ActionForward forward  = mapping.findForward("viewProductDetails"); 
		String docId = "";
		if(request.getParameter("docID")!=null)
		{
			docId = request.getParameter("docID").toString();
		}
		else		
		if(kmProductUploadFormBean.getDocId()!=null)
		{
		   	docId = kmProductUploadFormBean.getDocId().toString();
		}

		//System.out.println(" >>>>>>>>>>> DOCID:  "+docId);
		try { 
			 kmProductUploadFormBean.reset(mapping, request);
			// get fileName from DB			
			kmProductUploadFormBean.setXmlFileName(service.getDocPath(docId) );	
			KmProductUploadService kmProductUploadService = new KmProductUploadServiceImpl();		
			kmProductUploadFormBean = kmProductUploadService.viewProductDetails(kmProductUploadFormBean);
			
			 if(userBean.isCsr())
				{
				 
				 forward  = mapping.findForward("viewProductDetailsCsr"); 
				 service.increaseDocHitCount(docId,userBean.getUserId());
				}
			 
			}catch (Exception e) {
		    e.printStackTrace();
			logger.error("1 Exception occured while geting product details :" + e);	
			errors.add("error",new ActionError("product.not.found"));
			saveErrors(request, errors);
			}
			kmProductUploadFormBean.setKmActorId(userBean.getKmActorId());
			kmProductUploadFormBean.setDocId(docId);
			request.setAttribute("kmProductUploadFormBean",kmProductUploadFormBean);
	      
			return forward;
	}
	
	public ActionForward viewEditProductDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	throws Exception { 
		request.getSession().setAttribute("UPDATE_PRODUCT_DATA", "true");
		KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
		ActionErrors errors = new ActionErrors();
		KmDocumentService service = new KmDocumentServiceImpl();
		KmProductUploadFormBean kmProductUploadFormBean = (KmProductUploadFormBean)form;
		String docId = "";
		if(request.getParameter("docID")!=null)
		{
			docId = request.getParameter("docID").toString();
		}
		else		
		if(kmProductUploadFormBean.getDocId()!=null)
		{
		   	docId = kmProductUploadFormBean.getDocId().toString();
		}
		//System.out.println(" >>>>>>>>>>> 2 DOCID:  "+docId);

		try {
			kmProductUploadFormBean.reset(mapping, request);
			// get fileName from DB
			kmProductUploadFormBean.setXmlFileName(service.getDocPath(docId) );	
			////System.out.println("view edit prod Details XmlFileName : "+kmProductUploadFormBean.getXmlFileName().trim());
			
			
			KmProductUploadService kmProductUploadService = new KmProductUploadServiceImpl();		
			kmProductUploadFormBean = kmProductUploadService.viewProductDetails(kmProductUploadFormBean);
			
			}catch (Exception e) {
		    e.printStackTrace();
			logger.error("2 Exception occured while geting product details :" + e);	
			errors.add("error",new ActionError("product.not.found"));
			saveErrors(request, errors);
			}
			
			kmProductUploadFormBean.setKmActorId(userBean.getKmActorId());
			kmProductUploadFormBean.setDocId(docId);
			request.setAttribute("kmProductUploadFormBean",kmProductUploadFormBean);			
	        return mapping.findForward("editProductDetails");	       
	}
	
	public ActionForward updateProduct(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
		throws Exception { 
		if ( request.getSession().getAttribute("UPDATE_PRODUCT_DATA") == null ) {
			  return init(mapping, form, request, response);
		}
		request.getSession().setAttribute("UPDATE_PRODUCT_DATA", null);		
		KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();
		logger.info(userBean.getUserLoginId() + " inside updateProduct of KmProductUploadAction...");
		
		KmProductUploadFormBean kmProductUploadFormBean = (KmProductUploadFormBean)form;
		kmProductUploadFormBean.setCreatedBy(userBean.getUserId());	
		String documentId="";
		String xmlFileName="";
		String plainFileName="";
		try {
		    KmDocumentMstr documentDto ;
		    ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
			String folderPath = bundle.getString("folder.path");
			KmProductUploadService kmProductUploadService = new KmProductUploadServiceImpl();	
			KmDocumentService service = new KmDocumentServiceImpl();
			
			documentId = kmProductUploadFormBean.getDocId();
			xmlFileName = service.getDocPath(documentId);
			
			////System.out.println(" old documentId "+documentId);
		    ////System.out.println(" old XmlFileName : "+xmlFileName);
		    
		    String elementFolderPath = xmlFileName.substring(0,xmlFileName.lastIndexOf("/"));
		    
		    String[] elements = xmlFileName.split("/");
			String circleId = elements[2];
		    
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH-mm-ss-S");
			String date = sdf.format(new java.util.Date());	
			
			xmlFileName = folderPath+elementFolderPath+"/"+ date+ ".xml";		
			plainFileName=folderPath+elementFolderPath+"/"+ date+ "_plain" + ".xml";;

			elementFolderPath = elementFolderPath +"/"+ date+ ".xml";
			
		    ////System.out.println(" old new elementFolderPath : "+elementFolderPath);
		    ////System.out.println(" old new Circle element id : "+elements[2]);
		    
			////System.out.println(" New XmlFileName : "+xmlFileName);
			
			kmProductUploadFormBean.setXmlFileName(xmlFileName);
			kmProductUploadFormBean.setElementFolderPath(elementFolderPath);
			
			kmProductUploadFormBean.setXmlFileNameContentPlainText(plainFileName);

			
					// saving document...
					
					documentDto = kmProductUploadService.saveProductDetails(kmProductUploadFormBean);
				
					if(!documentDto.isValidFile())
					{
						errors.add("error",new ActionError("not.valid.content"));
						saveErrors(request, errors);
						return mapping.findForward("initialize"); 
					}
					
					if ("".equals(kmProductUploadFormBean.getMessage()))
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
				      kmDocumentMstrDao.deleteDocument(kmProductUploadFormBean.getDocId(), userBean.getUserId());
					}
			 kmProductUploadFormBean.setXmlFileName(xmlFileName);
			 kmProductUploadFormBean.setDocId(documentId);		 
			 messages.add("msg1",new ActionMessage("product.updated"));
			 saveMessages(request, messages);
			
		} catch (Exception e) {
		  e.printStackTrace();
			logger.error("Exception occured while uploading product Details :" + e);	
			errors.add("error",new ActionError("product.not.updated"));
			saveErrors(request, errors);
			return mapping.findForward("initialize");
		}		
		return viewProductDetails(mapping,form,request,response);
	}
}


