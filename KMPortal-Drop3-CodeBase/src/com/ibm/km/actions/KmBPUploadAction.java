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

import com.ibm.km.common.Constants;
import com.ibm.km.common.PropertyReader;
import com.ibm.km.common.Utility;
import com.ibm.km.dao.AddFileDAO;
import com.ibm.km.dao.KmDocumentMstrDao;
import com.ibm.km.dao.impl.AddFileDaoImpl;
import com.ibm.km.dao.impl.KmDocumentMstrDaoImpl;
import com.ibm.km.dto.KmBPUploadDto;
import com.ibm.km.dto.KmBillPlanDto;
import com.ibm.km.dto.KmCsrLatestUpdatesDto;
import com.ibm.km.dto.KmDocumentMstr;
import com.ibm.km.dto.KmSearch;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.forms.BillPlanHitsFormBean;
import com.ibm.km.forms.KmBPUploadFormBean;
import com.ibm.km.search.IndexFiles;
import com.ibm.km.services.KmBPUploadService;
import com.ibm.km.services.KmCsrLatestUpdatesService;
import com.ibm.km.services.KmDocumentService;
import com.ibm.km.services.KmElementMstrService;
import com.ibm.km.services.KmPlanHitsService;
import com.ibm.km.services.KmSearchService;
import com.ibm.km.services.impl.KmBPUploadServiceImpl;
import com.ibm.km.services.impl.KmCsrLatestUpdatesServiceImpl;
import com.ibm.km.services.impl.KmDocumentServiceImpl;
import com.ibm.km.services.impl.KmElementMstrServiceImpl;
import com.ibm.km.services.impl.KmPlanHitsServiceImpl;
import com.ibm.km.services.impl.KmSearchServiceImpl;

public class KmBPUploadAction extends DispatchAction{
	
	private static final Logger logger = Logger.getLogger(KmBPUploadAction.class);
	 
	public ActionForward initBPUpload(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception{
		logger.info("Inside initBPUpload");
		KmBPUploadFormBean formBean = (KmBPUploadFormBean)form;
		ArrayList<KmBPUploadDto> headers = null;
		
		KmBPUploadService service = new KmBPUploadServiceImpl();
		headers = service.getHeaders();
		formBean.setHeaders(headers);
		formBean.setTopic("");
		formBean.setToDate("");
		formBean.setFromDate("");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String date = sdf.format(new java.util.Date());
		date = date.substring(0,10);		
		if(formBean.getToDate().equals(""))
			formBean.setToDate(date);
		if(formBean.getFromDate().equals(""))
			formBean.setFromDate(date);
		logger.info("Returning from initBPUpload");		
		return mapping.findForward("success");
	}
	public ActionForward insert(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception{
		logger.info("Inside insert");
		ActionMessages messages = new ActionMessages();
		KmBPUploadFormBean formBean = (KmBPUploadFormBean)form;
		KmBPUploadService service = new KmBPUploadServiceImpl();
		ArrayList<String> headerList = new ArrayList<String>();
		ArrayList<String> contentList = new ArrayList<String>();
		HttpSession session = request.getSession();
		KmUserMstr sessionUserBean = (KmUserMstr) session.getAttribute("USER_INFO");
		String documentId="";
		try{
			String fileData = formBean.getTopic()+"\n";
			String fileData2 = "<BILLPLAN> \n";
			fileData2 += " <TOPIC>" + Utility.encodeContent(formBean.getTopic()) + "</TOPIC> \n";
			fileData2 += " <DATA> \n";
			int headerSize = formBean.getHeaders().size();
			for(int i=0; i<headerSize;i++){
				if(request.getParameter("subheaders["+i+"].headerId")!= null){
					String headerName = request.getParameter("subheaders["+i+"].headerName");
					String header = request.getParameter("subheaders["+i+"].headerId");
					String content = request.getParameter("subheaders["+i+"].content");
					//System.out.println("parameter=="+request.getParameter("subheaders["+i+"].content"));
					//System.out.println("parameter=="+request.getParameter("subheaders["+i+"].headerId"));
					headerList.add(header);
					contentList.add(content);
					fileData += headerName+" "+content+"\n"; 
					fileData2 += "<BILLPLANDATA> <HEADER>" + Utility.encodeContent(headerName) + " </HEADER> <CONTENT> " + Utility.encodeContent(content) + "</CONTENT> </BILLPLANDATA> \n";
				}
			}
			fileData2 += " </DATA> \n";
			fileData2 += " </BILLPLAN> \n";
			//System.out.println("headerList="+headerList.get(0));
			//System.out.println("contentList="+contentList.get(0));
			KmDocumentMstr documentDto ;
			ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
	
			String initialDirectoryPath = bundle.getString("folder.path");
		    String circles[] = formBean.getCreateMultiple();
		    
		    if(null == circles){
		    	//System.out.println("circle element Id : "+sessionUserBean.getElementId());
		    	circles = new String[1];
		    	circles[0] = sessionUserBean.getElementId();
		    	
		    }
		    String documentPath = ""; 
		    if(request.getAttribute("KMBPUPLOADDTO") != null){
		    	KmBPUploadDto dto = (KmBPUploadDto)request.getAttribute("KMBPUPLOADDTO");
		    
		    	KmDocumentMstrDao dao = new KmDocumentMstrDaoImpl();
		    	documentPath = dao.getDocPath(dto.getDocumentId());
		    	documentPath=documentPath.substring(0,documentPath.lastIndexOf("/"));
		    	formBean.setElementFolderPath(documentPath);
		    	//circles = service.getCircleList(dto.getDocumentId());
		    	circles = dto.getCircles();
		    	//System.out.println("circles="+circles[0]);
		    }	
		    //System.out.println(formBean.getElementFolderPath());
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH-mm-ss-S");
			String date = sdf.format(new java.util.Date());
			
		    KmElementMstrService  kmElementMstrService  = new KmElementMstrServiceImpl();
		    Map<String, String> elementPathMap = kmElementMstrService.getElementPathList(formBean.getElementFolderPath(), circles);
		    String fileName="";
		    //String fileData = "Inside Bill Plan Upload";
		    //formBean.setFileData(fileData);
		    formBean.setFileData(fileData2);
		    //System.out.println("circles.length=="+circles.length+"circles[yy] : "+circles[0]);
			for(int yy=0; yy< circles.length; yy++ )
			{
				//System.out.println("inside circles loop");
				//System.out.println(initialDirectoryPath+elementPathMap.get(circles[yy]));
				formBean.setFilePath(initialDirectoryPath+elementPathMap.get(circles[yy])+"BP_"+ date+ ".xml" );
				//System.out.println("elementPathMap.get(circles[yy]) : "+elementPathMap.get(circles[yy])) ;
				//System.out.println("formBean.getFilePath() : "+ formBean.getFilePath() );
	
		    if( null != elementPathMap.get(circles[yy]) )
			{
	
				formBean.setElementFolderPath(elementPathMap.get(circles[yy]));
				
				documentDto = service.saveBillPlanDetails(formBean ,sessionUserBean );
				documentDto.setDocType(Constants.DOC_TYPE_BP);
				
			    	AddFileDAO addFileDAO = new AddFileDaoImpl();  
			        documentId = addFileDAO.saveFileInfoInDB(documentDto);
			    	//System.out.println("documentId after savefileinfoInDB="+documentId);			   
			        KmCsrLatestUpdatesDto kmCsrLatestUpdatesDto = documentDto.getKmCsrLatestUpdatesDto();
			        kmCsrLatestUpdatesDto.setDocumentId(documentId);
			        kmCsrLatestUpdatesDto.setDocType(Constants.DOC_TYPE_BP);
	
			        // save latest updates
					KmCsrLatestUpdatesService kmCsrLatestUpdatesService = new KmCsrLatestUpdatesServiceImpl();
					int recInsertCount = kmCsrLatestUpdatesService.insertLatestUpdates(kmCsrLatestUpdatesDto);
					if(recInsertCount>0)
					{
						logger.info("Letest Updates uploaded for bill plan upload : "+kmCsrLatestUpdatesDto.getUpdateTitle());
					}
					
			    	//Code to update the Index for the above document in Pending state
					IndexFiles indexObject = new IndexFiles();				
					
					if(PropertyReader.getAppValue("SINGLE.INDEX").equals("Y")){	
						
						indexObject.initIndex(new File(formBean.getFilePath() ), documentId,circles[yy]);
					}	
					else{
						
						indexObject.initIndexNew(new File(formBean.getFilePath() ), documentId,circles[yy]);
					}
					
					/*Inserting BP Data in DB */
					
					KmBPUploadDto dto = new KmBPUploadDto();
					dto.setTopic(formBean.getTopic());
					//dto.setRcType(formBean.getSelectedCombo());
					dto.setContentPath(formBean.getElementFolderPath());
					dto.setDocumentId(documentId);
					////System.out.println("formBean.getToDate()"+formBean.getToDate());
					////System.out.println("formBean.getFromDate()"+formBean.getFromDate());
					dto.setToDate(Utility.getSqlDateFromString(formBean.getToDate(),"yyyy-M-d"));
					dto.setFromDate(Utility.getSqlDateFromString(formBean.getFromDate(),"yyyy-M-d"));
					dto.setHeaderList(headerList);
					dto.setContentList(contentList);
					dto.setTotalHeaders(headerList.size());
					dto.setCircleId(circles[yy]);
					  // System.out.println("TO DATE IN ACTION: -->>" + dto.getToDate());

					request.setAttribute("KMBPUPLOADDTO",dto);
					formBean.setDocumentId(documentId);
					service.insertBPData(dto);
			
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			messages.add("msg",new ActionMessage("bp.not.uploaded"));
			saveMessages(request, messages);
			mapping.findForward("pathNotExist");
		}
		logger.info("Returing from insert");

		return viewBPDetails(mapping, formBean, request, response);
	}

	public ActionForward viewBPDetails(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception{
		logger.info("Inside viewBPDetails");

		ActionMessages messages = new ActionMessages();
		ActionErrors errors = new ActionErrors();
		KmBPUploadFormBean formBean = (KmBPUploadFormBean)form;
		ArrayList<KmBPUploadDto> dataList = new ArrayList<KmBPUploadDto>();
		HttpSession session = request.getSession();
		KmUserMstr sessionUserBean = (KmUserMstr) session.getAttribute("USER_INFO");
		KmBPUploadService service = new KmBPUploadServiceImpl();
		ArrayList<KmBPUploadDto> headers = null;
		String docId = "";
		try{		
			KmBPUploadDto dto = (KmBPUploadDto)request.getAttribute("KMBPUPLOADDTO");
			if(dto == null) 
				dto = new KmBPUploadDto();
			//System.out.println("formBean.getDocumentId()=="+formBean.getDocumentId());
			
			
			if(request.getParameter("docID")!=null)
			{
				docId = request.getParameter("docID").toString();
			}
			else		
			if(formBean.getDocumentId()!=null)
			{
			   	docId = formBean.getDocumentId();
			}
			
			dto.setDocumentId(docId);
			//System.out.println("dto.getDocumentId()=="+dto.getDocumentId());
			if(docId != "")
			  { 
				dataList = service.viewBPDetails(dto);
				headers = service.getHeaders();
				formBean.setHeaders(headers);
				formBean.setDataList(dataList);
				formBean.setTopic(dataList.get(0).getTopic());
				formBean.setFromDate(dataList.get(0).getFromDt());
				formBean.setToDate(dataList.get(0).getToDt());
				formBean.setDocumentId(dto.getDocumentId());
				formBean.setDocumentId(docId);
				request.setAttribute("KMBPUPLOADDTO",dto);
				request.setAttribute("formBean",formBean);
			  }else{
				  messages.add("msg",new ActionMessage("bp.not.uploaded"));
				  saveMessages(request, messages);
				  return mapping.findForward("pathNotExist");
			  }
			
			if(sessionUserBean.isCsr())
			{
				KmDocumentService docService = new KmDocumentServiceImpl();
				docService.increaseDocHitCount(docId,sessionUserBean.getUserId());
			}
		}catch(Exception e){
			e.printStackTrace();
			errors.add("error",new ActionError("bp.not.found"));
			saveErrors(request, errors);
		}
		if(sessionUserBean.isCsr())
		{
			return mapping.findForward("viewBPDetailsCsr");
		}
		logger.info("Returning from viewBPDetails");
		return mapping.findForward("viewBPDetails");
	}
	public ActionForward editBPDetails(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception{
		logger.info("Inside editBPDetails");

		request.getSession().setAttribute("EDIT_BP_DETAILS", "true");
		KmBPUploadFormBean formBean = (KmBPUploadFormBean)form;
		 String documentId ="";
		  if(request.getParameter("docID")!=null)
			{
			  documentId = request.getParameter("docID").toString();
			}
		  else
		  {
			  documentId = formBean.getDocumentId();
		  }
		KmBPUploadService service = new KmBPUploadServiceImpl();
		
		ArrayList<KmBPUploadDto> headers = null;
		ArrayList<KmBPUploadDto> dataList = new ArrayList<KmBPUploadDto>();
		//KmBPUploadDto dto = (KmBPUploadDto)request.getAttribute("KMBPUPLOADDTO");
		KmBPUploadDto dto = new KmBPUploadDto();
		dto.setDocumentId(documentId);
		//System.out.println("getDocumentId()=="+dto.getDocumentId());
		dataList = service.viewBPDetails(dto);
		headers = service.getHeaders();
		formBean.setHeaders(headers);
		formBean.setDataList(dataList);
		formBean.setTopic(dataList.get(0).getTopic());
		formBean.setFromDate(dataList.get(0).getFromDt());
		formBean.setToDate(dataList.get(0).getToDt());
		////System.out.println("request parameter documentId"+request.getParameter("documentId"));
		////System.out.println(formBean.getDataList());
		
		request.setAttribute("KMBPUPLOADFORMBEAN", formBean);
		
		logger.info("Returing from editBPDetails");
		//return insert(mapping, formBean, request, response);
		return mapping.findForward("editBPDetails");
	}
	
	public ActionForward updateBPDetails(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception{
		
			logger.info("Inside updateBPDetails");
	KmBPUploadFormBean formBean = (KmBPUploadFormBean)form;
		HttpSession session = request.getSession();
		KmUserMstr sessionUserBean = (KmUserMstr) session.getAttribute("USER_INFO");
		String circles[] = null;
		
		KmBPUploadDto dto = new KmBPUploadDto();
		dto.setDocumentId(formBean.getDocumentId());
		
		KmBPUploadService service = new KmBPUploadServiceImpl();
		circles = service.getCircleList(dto.getDocumentId());
		dto.setCircles(circles);
		
		KmDocumentMstrDao dao = new KmDocumentMstrDaoImpl();
		dao.deleteDocument(dto.getDocumentId(),sessionUserBean.getUserId());
		
		service.deleteBPDetails(dto);
		logger.info("returing from updateBPDetails");

		request.setAttribute("KMBPUPLOADDTO", dto);
		return insert(mapping, formBean, request, response);
	}


	public ActionForward initCsrViewBPDetails(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception{
		logger.info("Inside initCsrViewBPDetails");

		KmBPUploadFormBean formBean = (KmBPUploadFormBean)form;
		ArrayList<HashMap<String, String>> billPlansList = new ArrayList<HashMap<String,String>>();
		ArrayList<KmBPUploadDto> headers = new ArrayList<KmBPUploadDto>();
		
		formBean.setSearchKey("");
		
		formBean.setBillPlansList(billPlansList);
		formBean.setHeaders(headers);
		
		return mapping.findForward("viewCsrBPDetails");
	}
	
	public ActionForward csrViewBPDetails(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		logger.info("inside csrViewBPDetails");
		
		KmBPUploadFormBean formBean = (KmBPUploadFormBean)form;
		HttpSession session = request.getSession();
		KmUserMstr sessionUserBean = (KmUserMstr) session.getAttribute("USER_INFO");
		//ArrayList<String> documentIds = new ArrayList<String>();
		ArrayList<HashMap<String, String>> billPlansList = new ArrayList<HashMap<String,String>>();
		ArrayList<KmBPUploadDto> headers = null;
		KmBPUploadService service = new KmBPUploadServiceImpl();
		KmDocumentService docService = new KmDocumentServiceImpl();
		ArrayList<String> filteredDocIdList;
		
		try
		{
		KmSearch searchDto=new KmSearch();
		ArrayList<String> documentList=null;			
			String circleId=(String) session.getAttribute("CURRENT_CIRCLE_ID");
			if(circleId!=null){
				searchDto.setElementId(circleId);
			}else{
				searchDto.setElementId(sessionUserBean.getElementId());
			}
			searchDto.setKeyword(formBean.getSearchKey());
			if(sessionUserBean.getKmActorId().equals(Constants.CIRCLE_CSR)||sessionUserBean.getKmActorId().equals(Constants.CATEGORY_CSR)){
				searchDto.setSearchType("CSR_KEYWORD");
			}
			else
			{
				searchDto.setSearchType("ADMIN_KEYWORD");
			}
			searchDto.setActorId(sessionUserBean.getKmActorId());
			KmSearchService searchService=new KmSearchServiceImpl();
//			documentList=searchService.search(searchDto);
			documentList=searchService.contentSearch(searchDto);
		filteredDocIdList = docService.docFilterAsPerDocType(documentList,Constants.DOC_TYPE_BP,formBean.getIsTop15() );
		logger.info("document List size: " +documentList.size()+" IDs : "+ filteredDocIdList);
		billPlansList = service.getCsrBPDetails(filteredDocIdList);
		headers = service.getHeaders();
		formBean.setBillPlansList(billPlansList);
		formBean.setIsTop15("N");
		formBean.setHeaders(headers);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.info("Error in csrViewBPDetails method");
		}
		logger.info("returing from csrViewBPDetails");
		
		return mapping.findForward("viewCsrBPDetails");
	}

	public ActionForward getBillPlanHits(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		logger.info("Inside getBillPlanHits");
		String lobId=request.getParameter("lobId");
		
		KmPlanHitsService service = new KmPlanHitsServiceImpl();
		List<KmBillPlanDto> billPlanhitsList = service.getBulkPlanData(lobId);
		
		request.setAttribute("BILL_PLAN_HITS", billPlanhitsList);
		
		return mapping.findForward("successBulkPlanHit");
	}
	
	
	public ActionForward retrieveData(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		logger.info("Inside retrieveData");
		
		KmBPUploadFormBean formBean = (KmBPUploadFormBean)form;
		String documentId = formBean.getDocumentId(); 
		
		KmPlanHitsService service = new KmPlanHitsServiceImpl();
		List<KmBillPlanDto> billPlanList = service.getSinglePlanData(documentId);
		
		request.setAttribute("BILL_PLAN", billPlanList);
		
		return mapping.findForward("successSinglePlan");
	}
}
	

