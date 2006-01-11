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
import org.apache.struts.upload.FormFile;

import com.ibm.km.common.Constants;
import com.ibm.km.common.PropertyReader;
import com.ibm.km.dao.AddFileDAO;
import com.ibm.km.dao.KmDocumentMstrDao;
import com.ibm.km.dao.impl.AddFileDaoImpl;
import com.ibm.km.dao.impl.KmDocumentMstrDaoImpl;
import com.ibm.km.dto.KmCsrLatestUpdatesDto;
import com.ibm.km.dto.KmDocumentMstr;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.dto.ProductDetails;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.KmProductUploadFormBean;
import com.ibm.km.forms.KmSchemesAndBenefitsDetailsFormBean;
import com.ibm.km.forms.KmSopUploadFormBean;
import com.ibm.km.search.IndexFiles;
import com.ibm.km.services.KmCsrLatestUpdatesService;
import com.ibm.km.services.KmDocumentService;
import com.ibm.km.services.KmElementMstrService;
import com.ibm.km.services.KmSopUploadService;
import com.ibm.km.services.impl.KmCsrLatestUpdatesServiceImpl;
import com.ibm.km.services.impl.KmDocumentServiceImpl;
import com.ibm.km.services.impl.KmElementMstrServiceImpl;
import com.ibm.km.services.impl.KmSopUploadServiceImpl;

/**
 * @author Kundan Kumar
 * @since 5th Nov, 2012
 */
public class KmSopUploadAction extends DispatchAction {
	private static final Logger logger;
	static {
		logger = Logger.getLogger(KmSopUploadAction.class);
	}

	public ActionForward init(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
				request.getSession().setAttribute("SAVE_SOP_DATA", "true");
				
				KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
				KmSopUploadFormBean formBean = (KmSopUploadFormBean)form;
				logger.info(userBean.getUserLoginId() + " entered init method for sop upload page.");
				formBean.reset(mapping,request);
							
				formBean.setCreatedBy(userBean.getElementId());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
				String date = sdf.format(new java.util.Date());
				date = date.substring(0,10);		
				if(formBean.getPublishDt().equals(""))
					formBean.setPublishDt(date);
				if(formBean.getEndDt().equals(""))
					formBean.setEndDt(date);
				
				
				logger.info(userBean.getUserLoginId() + " exited init method for sop upload page.");
				return mapping.findForward("initialize");
				
		}
	

	public ActionForward insertSOP(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
			throws Exception { 
		if ( request.getSession().getAttribute("SAVE_SOP_DATA") == null ) {
		  return init(mapping, form, request, response);
		}
			request.getSession().setAttribute("SAVE_SOP_DATA", null);
		
		KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();
		logger.info(userBean.getUserLoginId() + " inside insertSOP.");
		KmSopUploadFormBean kmSopUploadFormBean = (KmSopUploadFormBean)form;
		kmSopUploadFormBean.setCreatedBy(userBean.getUserId());		
		String fileName="";
		String plainFileName="";
	    KmSopUploadService SopUploadService = new KmSopUploadServiceImpl();		
	    Map<String, String> statusMap = new HashMap<String, String>();	 
	    /*logger.info("kmSopUploadFormBean.getImageName()[0]   ----    "+kmSopUploadFormBean.getImageName().length);
	    logger.info("kmSopUploadFormBean.getProductDetailsList().get(0).getImageFile().getFileName()   ----    "+kmSopUploadFormBean.getProductDetailsList().get(0).getImageFile().getFileName());*/
	    logger.info("kmSopUploadFormBean.getProductImageList().get(0).getImageFile().getFileName()   ----    "+kmSopUploadFormBean.getProductImageList().get(0).getImageFile().getFileName());
	   /* logger.info("kmSopUploadFormBean.getProductImages()[0].getFileName()   ----    "+kmSopUploadFormBean.getProductImages()[0].getFileName());
	    logger.info("kmSopUploadFormBean.getSopImage().length   ----    "+kmSopUploadFormBean.getSopImage().length);*/
		try {
			    KmDocumentMstr documentDto ;
			    ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
				String folderPath = bundle.getString("folder.path");
				String circles[] = kmSopUploadFormBean.getCreateMultiple();				
			    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH-mm-ss-S");
				String date = null;
				String documentId ="";
			    KmElementMstrService  kmElementMstrService  = new KmElementMstrServiceImpl();
			    if(!validateFileUpload(kmSopUploadFormBean)){
					errors.add("error",new ActionError("not.valid.content"));
					saveErrors(request, errors);
					return mapping.findForward("initialize"); 
				}			   
			    String[] elementFolderPath = kmSopUploadFormBean.getElementFolderPath().split("/");
				boolean lob=kmSopUploadFormBean.getDateCheck();
				logger.info(elementFolderPath[1]+"kmSopUploadFormBean.getElementFolderPath() : " +kmSopUploadFormBean.getElementFolderPath()+" circle ID : " +lob+"folderPath"+folderPath );
				if(lob==false)
			    {
			    circles[0] = elementFolderPath[2];
				
				logger.info("kmSopUploadFormBean.getElementFolderPath() : " +kmSopUploadFormBean.getElementFolderPath()+" circle ID : " +circles[0] );
		    
			    Map<String, String> elementPathMap = kmElementMstrService.getElementPathList(kmSopUploadFormBean.getElementFolderPath(), circles);
			    
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
						kmSopUploadFormBean.setXmlFileName(fileName);
						
						plainFileName = folderPath+elementPathMap.get(circles[yy])+ date+ "_plain" + ".xml";
						kmSopUploadFormBean.setXmlFileNameContentPlainText(plainFileName);

						System.out.println(" XmlFileName : "+kmSopUploadFormBean.getXmlFileName());
						
						// updating element path for each circle.
						kmSopUploadFormBean.setElementFolderPath(elementPathMap.get(circles[yy]));
						
						documentDto = SopUploadService.saveProductDetails(kmSopUploadFormBean);
						
						if(!documentDto.isValidFile())
						{
							errors.add("error",new ActionError("not.valid.content"));
							saveErrors(request, errors);
							return mapping.findForward("initialize"); 
						}
					
						if ("".equals(kmSopUploadFormBean.getMessage()))
						{
					    	AddFileDAO addFileDAO = new AddFileDaoImpl();  
					        documentId = addFileDAO.saveFileInfoInDB(documentDto);
					    				
					        insertLatestUpdateRecord(documentDto, documentId);
							
					    	//Code to update the Index for the above document in Pending state
							IndexFiles indexObject = new IndexFiles();
							
							
							if(PropertyReader.getAppValue("SINGLE.INDEX").equals("Y")){	
								
								//System.out.println("Calling index creation method : initIndex for "+kmSopUploadFormBean.getXmlFileName());
								indexObject.initIndex(new File(fileName), documentId,circles[yy]);
								indexObject.initIndex(new File(plainFileName), documentId,circles[yy]);
							}	
							else{
								//System.out.println("Calling index creation method : initIndexNew for "+ kmSopUploadFormBean.getXmlFileName());
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
				 kmSopUploadFormBean.setXmlFileName(fileName);
				 //System.out.println("documentId >> "+documentId);
				 kmSopUploadFormBean.setDocId(documentId);
				 
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
				    	statusMessage = "<font color=green>SOP Uploaded Successfully for : </font>"+ uploadedCircles.replaceFirst(", ","");
				    if(uploadedC)
				    	statusMessage = statusMessage + "<br><font color=red>Category doesn't exist for :  </font>"+ missingCategory.replaceFirst(", ","");
				    if(uploadedF)
				    	statusMessage = statusMessage + "<br><font color=red>Directory doesn't exist for :  </font>"+ missingDirectory.replaceFirst(", ","");
				    
				    
					request.setAttribute("statusMessage", statusMessage);
					
				 messages.add("msg1",new ActionMessage("sop.created"));
				 saveMessages(request, messages);
			    }
				//added by vishwas for LOB wise upload
				else{
					 circles[0] = elementFolderPath[1];
					System.out.println(circles[0]+"kmSopUploadFormBean.getElementFolderPath()kkkkkkkkkkkkk "+kmSopUploadFormBean.getElementFolderPath());
					logger.info("Lob wise upload condtion else part========================else========="+kmSopUploadFormBean.getElementFolderPath());
					//kmSopUploadFormBean.setElementFolderPath("1");
					// Map<String, String> elementPathMap = kmElementMstrService.getElementPathList(kmSopUploadFormBean.getElementFolderPath(), circles);
				    
					//for(int yy=0; yy< circles.length; yy++ )
					{
						//System.out.println(circles[yy]+" path : "+ elementPathMap.get(circles[yy]));
						//if(null != elementPathMap.get(circles[yy]))
						{
							////System.out.println("checking folderPath+elementPathMap.get(circles[yy]) : "+folderPath+elementPathMap.get(circles[yy]));	
							if( ! (new File(folderPath+kmSopUploadFormBean.getElementFolderPath()).exists()))
							{
								statusMap.put(kmSopUploadFormBean.getElementFolderPath(), "Directory doesn't exist");
								logger.info("Path doesn't exist for : "+folderPath+kmSopUploadFormBean.getElementFolderPath());
							//	continue;
							}
							date = sdf.format(new java.util.Date());
							logger.info("Lob wise upload condtion else part=======3=================else"+kmSopUploadFormBean.getElementFolderPath());
							fileName = folderPath+kmSopUploadFormBean.getElementFolderPath()+"/"+ date+ ".xml";
							kmSopUploadFormBean.setXmlFileName(fileName);
							
							plainFileName = folderPath+kmSopUploadFormBean.getElementFolderPath()+"/"+date+ "_plain" + ".xml";
							kmSopUploadFormBean.setXmlFileNameContentPlainText(plainFileName);

							System.out.println(plainFileName+" XmlFileName : "+kmSopUploadFormBean.getXmlFileName());
							
							// updating element path for each circle.
							kmSopUploadFormBean.setElementFolderPath(kmSopUploadFormBean.getElementFolderPath());
							
							documentDto = SopUploadService.saveProductDetails(kmSopUploadFormBean);
							
							System.out.println(plainFileName+" XmlFileName : "+kmSopUploadFormBean.getXmlFileName());
							if(!documentDto.isValidFile())
							{
								errors.add("error",new ActionError("not.valid.content"));
								saveErrors(request, errors);
								return mapping.findForward("initialize"); 
							}
							logger .info("test msgggggggggggggggggggggg1 kmSopUploadFormBean.getMessage()"+kmSopUploadFormBean.getMessage());
							if ("".equals(kmSopUploadFormBean.getMessage()))
							{
						    	AddFileDAO addFileDAO = new AddFileDaoImpl();  
						        documentId = addFileDAO.saveFileInfoInDB(documentDto);
						    				
						        insertLatestUpdateRecord(documentDto, documentId);
								
						    	//Code to update the Index for the above document in Pending state
								IndexFiles indexObject = new IndexFiles();
								
								
								if(PropertyReader.getAppValue("SINGLE.INDEX").equals("Y")){	
									
									//System.out.println("Calling index creation method : initIndex for "+kmSopUploadFormBean.getXmlFileName());
									indexObject.initIndex(new File(fileName), documentId,kmSopUploadFormBean.getElementFolderPath());
									indexObject.initIndex(new File(plainFileName), documentId,kmSopUploadFormBean.getElementFolderPath());
								}	
								else{
									//System.out.println("Calling index creation method : initIndexNew for "+ kmSopUploadFormBean.getXmlFileName());
									indexObject.initIndexNew(new File(fileName), documentId,kmSopUploadFormBean.getElementFolderPath());
									indexObject.initIndexNew(new File(plainFileName), documentId,kmSopUploadFormBean.getElementFolderPath());
								}
								logger .info("test msgggggggggggggggggggggg5 kmSopUploadFormBean.getMessage()"+kmSopUploadFormBean.getMessage());
								statusMap.put(kmSopUploadFormBean.getElementFolderPath(), "Uploaded Successfully");
							}
						}
						//else
						{
						///	logger.info("Category doesn't exist for : "+kmSopUploadFormBean.getElementFolderPath());
							//statusMap.put(kmSopUploadFormBean.getElementFolderPath(), "Category doesn't exist");
						}
					}
					 kmSopUploadFormBean.setXmlFileName(fileName);
					 //System.out.println("documentId >> "+documentId);
					 kmSopUploadFormBean.setDocId(documentId);
					 
					    String uploadedCircles = "";
					    String missingCategory = "";
					    String missingDirectory = "";
					    boolean uploadedS  = false;
					    boolean uploadedC  = false;
					    boolean uploadedF = false;
					    String  statusMessage = "";
					    
					    
					    HashMap<String, String> circleDescMap = kmElementMstrService.getAllLobDesc();
					    
					   // for (int i = 0; i < circles.length; i++)
					    {
							
					    logger .info("test msgggggggggggggggggggggg1"+statusMap.get(kmSopUploadFormBean.getElementFolderPath()));	
					    if("Uploaded Successfully".equals(statusMap.get(kmSopUploadFormBean.getElementFolderPath())))
							{  
								logger .info("test msgggggggggggggggggggggg");
								uploadedS = true;
								uploadedCircles = uploadedCircles + ", "+circleDescMap.get(circles[0]);;
							}
					    logger .info("test msgggggggggggggggggggggg uploadedCircles"+uploadedCircles);	
					    /**
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
					    **/
					   // if(uploadedS)
					    	statusMessage = "<font color=green>SOP Uploaded Successfully for : </font>"+ uploadedCircles.replaceFirst(", ","");
					    /**
					    if(uploadedC)
					    	statusMessage = statusMessage + "<br><font color=red>Category doesn't exist for :  </font>"+ missingCategory.replaceFirst(", ","");
					    if(uploadedF)
					    	statusMessage = statusMessage + "<br><font color=red>Directory doesn't exist for :  </font>"+ missingDirectory.replaceFirst(", ","");
					     **/
					   
						request.setAttribute("statusMessage", statusMessage);
						
					 messages.add("msg1",new ActionMessage("sop.created"));
					 saveMessages(request, messages);
					
				}
				}
				
			    } catch (Exception e) {
		  e.printStackTrace();
			logger.error("Exception occured while uploading SOP Details :" + e);	
			errors.add("error",new ActionError("sop.not.created"));
			saveErrors(request, errors);
			kmSopUploadFormBean.setHeader(null);
			return mapping.findForward("initialize");
		}		
		return viewSopDetails(mapping,form,request,response);
	}


	private void insertLatestUpdateRecord(KmDocumentMstr documentDto,
			String documentId) throws KmException {
		KmCsrLatestUpdatesDto kmCsrLatestUpdatesDto = documentDto.getKmCsrLatestUpdatesDto();
		kmCsrLatestUpdatesDto.setDocumentId(documentId);
		kmCsrLatestUpdatesDto.setDocType(Constants.DOC_TYPE_SOP);

		// save latest updates
		KmCsrLatestUpdatesService kmCsrLatestUpdatesService = new KmCsrLatestUpdatesServiceImpl();
		int recInsertCount = kmCsrLatestUpdatesService.insertLatestUpdates(kmCsrLatestUpdatesDto);
		if(recInsertCount>0)
		{
			logger.info("Letest Updates uploaded for Product Upload : "+kmCsrLatestUpdatesDto.getUpdateTitle());
		}
	}
	
	public ActionForward viewSopDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	throws Exception { 
		KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
		ActionErrors errors = new ActionErrors();
		KmSopUploadFormBean kmSopUploadFormBean = (KmSopUploadFormBean)form;
		System.out.println(kmSopUploadFormBean.getXmlFileName()+"viewSopDetails XmlFileName :jjjjjjj "+kmSopUploadFormBean.getXmlFileName().trim());
		KmDocumentService service = new KmDocumentServiceImpl();
		String docId = "";
		try {
			if(request.getParameter("docID")!=null)
			{
				docId = request.getParameter("docID").toString();
			}
			else		
			if(kmSopUploadFormBean.getDocId()!=null)
			{
			   	docId = kmSopUploadFormBean.getDocId().toString();
			}
			kmSopUploadFormBean.reset(mapping, request);
			System.out.println("docId : "+docId+", User Id : "+userBean.getUserId());
				// get fileName from DB
				kmSopUploadFormBean.setXmlFileName(service.getDocPath(docId) );	
				System.out.println("viewSopDetails XmlFileName : "+kmSopUploadFormBean.getXmlFileName().trim());
				KmSopUploadService kmSopUploadService = new KmSopUploadServiceImpl();		
				
				kmSopUploadFormBean = kmSopUploadService.viewProductDetails(kmSopUploadFormBean);
		    	
				if(userBean.isCsr())
				{
					kmSopUploadFormBean.setIsFavourite(service.isFavouriteDocument(docId, userBean.getUserId()));
					service.increaseDocHitCount(docId,userBean.getUserId());
				}
			}catch (Exception e) {
		    e.printStackTrace();
			logger.error("Exception occured while geting SOP details :" + e);	
			errors.add("error",new ActionError("sop.not.found"));
			saveErrors(request, errors);
			}
			kmSopUploadFormBean.setDocId(docId);
			
			kmSopUploadFormBean.setKmActorId(userBean.getKmActorId());
			request.setAttribute("kmSopUploadFormBean",kmSopUploadFormBean);
			if(userBean.isCsr())
			{ 
						return mapping.findForward("viewSopDetailsCsr");
			}
			return mapping.findForward("viewSopDetails");
	}
	
	public ActionForward viewEditSopDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	throws Exception { 
		request.getSession().setAttribute("UPDATE_SOP_DATA", "true");
		KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
		ActionErrors errors = new ActionErrors();
		KmDocumentService service = new KmDocumentServiceImpl();
		KmSopUploadFormBean kmSopUploadFormBean = (KmSopUploadFormBean)form;
		String docId = "";
		
		if(request.getParameter("docID")!=null)
		{
			docId = request.getParameter("docID").toString();
		}
		else		
		if(kmSopUploadFormBean.getDocId()!=null)
		{
		   	docId = kmSopUploadFormBean.getDocId().toString();
		}
		
		kmSopUploadFormBean.setXmlFileName(service.getDocPath(docId) );	
		//System.out.println("XmlFileName() : "+kmSopUploadFormBean.getXmlFileName().trim());
		
		KmSopUploadService kmSopUploadService = new KmSopUploadServiceImpl();		
		try {
			kmSopUploadFormBean = kmSopUploadService.viewProductDetails(kmSopUploadFormBean);
			kmSopUploadFormBean.setKmActorId(userBean.getKmActorId());
			kmSopUploadFormBean.setDocId(docId);
			request.setAttribute("kmSopUploadFormBean",kmSopUploadFormBean);
		    	
			}catch (Exception e) {
		    e.printStackTrace();
		    
			logger.error("Exception occured while uploading product :" + e);	
			errors.add("error",new ActionError("sop.not.found"));
			saveErrors(request, errors);
			}
			
	        return mapping.findForward("viewEditSopDetails");
	}

public ActionForward updateSOP(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	throws Exception { 
	if ( request.getSession().getAttribute("UPDATE_SOP_DATA") == null ) {
		  return init(mapping, form, request, response);
		}
	request.getSession().setAttribute("UPDATE_SOP_DATA", null);
	
	KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
	ActionErrors errors = new ActionErrors();
	ActionMessages messages = new ActionMessages();
	
	KmSopUploadFormBean kmSopUploadFormBean = (KmSopUploadFormBean)form;
	kmSopUploadFormBean.setCreatedBy(userBean.getUserId());		
	String documentId="";
	String xmlFileName="";
	String plainFileName="";
	try {
	    KmDocumentMstr documentDto ;
	    ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
		String folderPath = bundle.getString("folder.path");
		
		KmSopUploadService sopUploadService = new KmSopUploadServiceImpl();	
		KmDocumentService service = new KmDocumentServiceImpl();
		
		documentId = kmSopUploadFormBean.getDocId();
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
		
		kmSopUploadFormBean.setXmlFileName(xmlFileName);
		kmSopUploadFormBean.setElementFolderPath(elementFolderPath);
		
		kmSopUploadFormBean.setXmlFileNameContentPlainText(plainFileName);

		// saving document...
				
				documentDto = sopUploadService.saveProductDetails(kmSopUploadFormBean);
				
				if(!documentDto.isValidFile())
				{
					errors.add("error",new ActionError("not.valid.content"));
					saveErrors(request, errors);
					return mapping.findForward("initialize"); 
				}
			
				if ("".equals(kmSopUploadFormBean.getMessage()))
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
			      kmDocumentMstrDao.deleteDocument(kmSopUploadFormBean.getDocId(), userBean.getUserId());
				}
		 kmSopUploadFormBean.setXmlFileName(xmlFileName);
		 kmSopUploadFormBean.setDocId(documentId);		 
		 messages.add("msg1",new ActionMessage("sop.updated"));
		 saveMessages(request, messages);
	} catch (Exception e) {
	  e.printStackTrace();
		logger.error("Exception occured while uploading SOP Details :" + e);	
		errors.add("error",new ActionError("sop.not.updated"));
		saveErrors(request, errors);
		return mapping.findForward("initialize");
	}
	return viewSopDetails(mapping,form,request,response);
  }

	public ActionForward viewDocumentLinks(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	throws Exception { 
		logger.info("inside viewDocumentLinks..");	
	        return mapping.findForward("viewDocumentLinks");

  }
	
	
	public boolean validateFileUpload(KmSopUploadFormBean formBean){
	{
		logger.info("Start of validateFileUpload");
		boolean isValid = true;
		try{
			List<ProductDetails> products = formBean.getProductImageList();
			//Checking if file present
			//logger.info("hamara logger --"+products.size());
			for(int i=0; i<products.size();i++){
			//logger.info("The product type is   ---    "+products.get(i).getImageFile().getContentType());
			String fileType = products.get(i).getImageFile().getContentType();
			logger.info("The file type is   ------------   "+fileType);
			if(!fileType.equalsIgnoreCase("application/octet-stream")){
			if(!(fileType.equalsIgnoreCase("image/jpeg") || fileType.equalsIgnoreCase("image/gif") || fileType.equalsIgnoreCase("image/jpeg"))){
				logger.error("File not an image file. Response send back to user.");
				isValid = false;
				break;
			}}
			String file = products.get(i).getImageFile().getFileName().toString();
			logger.info("The file name retrieved is    ------     "+file);
			if(file != null && !file.equals("")){	
				//Checking if xls file
				String ext= (file).substring(file.lastIndexOf(".")+1,file.length());
				logger.info("Extension is "+ext);
				if(ext!=null)
				{
					if(!(ext.equalsIgnoreCase("jpg")||ext.equalsIgnoreCase("jpeg")||ext.equalsIgnoreCase("gif")))
					{
					logger.error("File not an image file. Response send back to user.");
					isValid = false;
					break;
					}
				}
				}
			}
		}
		catch(Exception e){
			logger.error("Error in upload file validation "+ e.getMessage());
			e.printStackTrace();
		}
		logger.info("End of validateFileUpload");
		return isValid;
	}
	}

	
	
}

