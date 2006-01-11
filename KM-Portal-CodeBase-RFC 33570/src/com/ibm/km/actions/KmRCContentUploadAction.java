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
import com.ibm.km.dto.KmCsrLatestUpdatesDto;
import com.ibm.km.dto.KmDocumentMstr;
import com.ibm.km.dto.KmElementMstr;
import com.ibm.km.dto.KmRcCategoryDTO;
import com.ibm.km.dto.KmRcDataDTO;
import com.ibm.km.dto.KmSearch;
import com.ibm.km.dto.KmSearchDetailsDTO;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.KmDocumentMstrFormBean;
import com.ibm.km.forms.KmRCContentUploadFormBean;
import com.ibm.km.search.IndexFiles;
import com.ibm.km.services.KmCsrLatestUpdatesService;
import com.ibm.km.services.KmDocumentService;
import com.ibm.km.services.KmElementMstrService;
import com.ibm.km.services.KmRcContentUploadService;
import com.ibm.km.services.KmSearchService;
import com.ibm.km.services.impl.KmCsrLatestUpdatesServiceImpl;
import com.ibm.km.services.impl.KmDocumentServiceImpl;
import com.ibm.km.services.impl.KmElementMstrServiceImpl;
import com.ibm.km.services.impl.KmRcContentUploadServiceImpl;
import com.ibm.km.services.impl.KmSearchServiceImpl;
import com.ibm.km.sms.SendSMSXML;

public class KmRCContentUploadAction extends DispatchAction{
	
	/*
	  * Logger for the class.
	  */
	 private static final Logger logger;

	 static {
	  logger = Logger.getLogger(KmRCContentUploadAction.class);
	 }
	
	public ActionForward init(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		try {
		//System.out.println("Inside init method!!!!");
		KmRCContentUploadFormBean formBean = (KmRCContentUploadFormBean) form;
		formBean.setTopic("");
		formBean.setRechargeValue(null);
		formBean.setStartDt("");
		formBean.setEndDt("");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String date = sdf.format(new java.util.Date());
		date = date.substring(0,10);		
		if(formBean.getStartDt().equals(""))
			formBean.setStartDt(date);
		if(formBean.getEndDt().equals(""))
			formBean.setEndDt(date);
		
		
		KmRcContentUploadService rechargeService = new KmRcContentUploadServiceImpl();
		ArrayList<KmRcCategoryDTO> rcCategoryList = rechargeService.getRcCategory();
		formBean.setComboList(rechargeService.getCombo());
		formBean.setFieldList(rechargeService.getHeaders());
		formBean.setRcCategoryList(rcCategoryList);
		request.setAttribute("comboList",formBean.getComboList());
		request.setAttribute("fieldList",formBean.getFieldList());
		request.setAttribute("rcCategoryList", formBean.getRcCategoryList());
		
		} catch (KmException e) {
			e.printStackTrace();
			//System.out.println("KmException in Loading page for RC Content Upload: "+e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println("Exception in Loading page for RC Content Upload: "+e.getMessage());
			
		}
		return mapping.findForward("success");

	}//init
	
  public ActionForward insertRCData(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
	//System.out.println("Inside insertRCData method!!!!");
	KmRCContentUploadFormBean formBean = (KmRCContentUploadFormBean) form;
	HttpSession session = request.getSession();
	KmUserMstr sessionUserBean = (KmUserMstr) session.getAttribute("USER_INFO");
	ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");

	KmCsrLatestUpdatesDto kmCsrLatestUpdatesDto = new KmCsrLatestUpdatesDto();
	
	ArrayList<String> headerList = new ArrayList<String>();
	ArrayList<String> valueList = new ArrayList<String>();
	ArrayList<String> configList = new ArrayList<String>();
	
	String header,content,isConfigurable,fileData="RC "+formBean.getRechargeValue()+" Topic "+formBean.getTopic()+" ";

	String fileData2 = "<RECHARGECONTENT> \n";
	fileData2 += " <TOPIC>" + Utility.encodeContent(formBean.getTopic()) + "</TOPIC> \n";
	fileData2 += " <RECHARGEVALUE>" + Utility.encodeContent(formBean.getRechargeValue()) + "</RECHARGEVALUE> \n";
	
	if(formBean.getCheckedCombos() != null) {
		for(int i = 0; i < formBean.getCheckedCombos().length; i++) {
			fileData2 += " <COMBO>" + Utility.encodeContent(formBean.getCheckedCombos()[i]) + "</COMBO> \n";
		}
	}
	fileData2 += " <DATA> \n";

	
	String initialDirectoryPath = bundle.getString("folder.path");
	
	try {
	
	for(int i=0;i<formBean.getFieldList().size();i++)
	{
		header =  request.getParameter("report["+i+"].header");
		content = request.getParameter("report["+i+"].content");
		isConfigurable = request.getParameter("report["+i+"].isConfigurable");
		if(isConfigurable!=null){
			isConfigurable = "Y";
			} else {
				isConfigurable = "N";
			}
		//logger.info("Retrieved value from CHECKBOXES    ----     "+isConfigurable);
		fileData = fileData + header + " " + content + " " + isConfigurable + " "; 
		//logger.info("Retrieved content values    ----     "+content);
		//logger.info("Retrieved headers values    ----     "+header);
		fileData2 += "<RECHARGEDATA> <HEADER>" + Utility.encodeContent(header) + " </HEADER> <CONTENT> " + Utility.encodeContent(content) + "</CONTENT> </RECHARGEDATA> \n";
		headerList.add(header);
		valueList.add(content);
		configList.add(isConfigurable);
		
	}
	
	fileData2 += " </DATA> \n";
	fileData2 += " </RECHARGECONTENT> \n";
	
//	formBean.setFileData(fileData);
	formBean.setFileData(fileData2);

	kmCsrLatestUpdatesDto.setUpdateTitle("RC "+formBean.getRechargeValue()+" Topic "+formBean.getTopic()+" ");
	kmCsrLatestUpdatesDto.setActivationDate(formBean.getStartDt());
	kmCsrLatestUpdatesDto.setExpiryDate(formBean.getEndDt());			
	String documentPathArr[] = formBean.getElementFolderPath().split("/");
	kmCsrLatestUpdatesDto.setLob(documentPathArr[1]);		
	kmCsrLatestUpdatesDto.setCircleId(documentPathArr[2]); 
	kmCsrLatestUpdatesDto.setCategory(documentPathArr[3]);
	
	int updateDescLength = fileData.length();
	
	if (updateDescLength > 200)
	{
		updateDescLength = 200;
	}
	if (updateDescLength > 0)
	{
	kmCsrLatestUpdatesDto.setUpdateDesc(fileData.substring(0, updateDescLength).toString());
	}
	
    
    KmDocumentMstr documentDto ;

    String circles[] = formBean.getCreateMultiple();
    
    if(null == circles){
    	//System.out.println("circle element Id : "+sessionUserBean.getElementId());
    	circles = new String[1];
    	circles[0] = sessionUserBean.getElementId();
    	
    }
    
    //System.out.println("\nformBean.getElementFolderPath() "+formBean.getElementFolderPath());
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH-mm-ss-S");
	String date = sdf.format(new java.util.Date());
	KmRcContentUploadService rechargeService = new KmRcContentUploadServiceImpl();
    KmElementMstrService  kmElementMstrService  = new KmElementMstrServiceImpl();
    Map<String, String> elementPathMap = kmElementMstrService.getElementPathList(formBean.getElementFolderPath(), circles);
    
	for(int yy=0; yy< circles.length; yy++ )
	{
		formBean.setFilePath(initialDirectoryPath+elementPathMap.get(circles[yy])+"RC_"+ date+ ".xml" );
		//System.out.println("elementPathMap.get(circles[yy]) : "+elementPathMap.get(circles[yy])) ;
		//System.out.println("formBean.getFilePath() : "+ formBean.getFilePath() );
		
		
		if( null != elementPathMap.get(circles[yy]) )
		{
			//System.out.println("inside save... ");
			formBean.setElementFolderPath(elementPathMap.get(circles[yy]));
			
			documentDto = rechargeService.saveRechargeDetails(formBean ,sessionUserBean );
			documentDto.setDocType(Constants.DOC_TYPE_RC);
			
			
		    	AddFileDAO addFileDAO = new AddFileDaoImpl();  
		        String documentId = addFileDAO.saveFileInfoInDB(documentDto);
		    				    	
		        kmCsrLatestUpdatesDto.setDocumentId(documentId);
		        kmCsrLatestUpdatesDto.setDocType(Constants.DOC_TYPE_RC);

		        // save latest updates
				KmCsrLatestUpdatesService kmCsrLatestUpdatesService = new KmCsrLatestUpdatesServiceImpl();
				int recInsertCount = kmCsrLatestUpdatesService.insertLatestUpdates(kmCsrLatestUpdatesDto);
				if(recInsertCount>0)
				{
					logger.info("Letest Updates uploaded for Product Upload : "+kmCsrLatestUpdatesDto.getUpdateTitle());
				}
				
		    	//Code to update the Index for the above document in Pending state
				IndexFiles indexObject = new IndexFiles();				
				
				if(PropertyReader.getAppValue("SINGLE.INDEX").equals("Y")){	
					
					indexObject.initIndex(new File(formBean.getFilePath() ), documentId,circles[yy]);
				}	
				else{
					
					indexObject.initIndexNew(new File(formBean.getFilePath() ), documentId,circles[yy]);
				}
				
				/*Inserting RC Data in DB */
				
				KmRcDataDTO dto = new KmRcDataDTO();
				dto.setRcTopic(formBean.getTopic());
				dto.setRcValue(formBean.getRechargeValue());
				dto.setRcType(formBean.getSelectedCombo());
				dto.setContentPath(formBean.getElementFolderPath());
				dto.setDocumentId(Integer.parseInt(documentId));
				dto.setStartDt(Utility.getSqlDateFromString(formBean.getStartDt(),"yyyy-MM-dd"));
				dto.setEndDt(Utility.getSqlDateFromString(formBean.getEndDt(),"yyyy-MM-dd"));
				dto.setHeaderList(headerList);
				dto.setValueList(valueList);
				dto.setConfigList(configList);
				dto.setTotalHeaders(formBean.getFieldList().size());
				dto.setCircleId(Integer.parseInt(circles[yy]));
				dto.setRcCategoryId(formBean.getSelectedRcCategory());
				
				rechargeService.insertRcData(dto);
				formBean.setDocumentId(documentId);
			}
		}
		ActionMessages messages = new ActionMessages();
	    messages.add("msg1",new ActionMessage("recharge.uploaded"));
	    saveMessages(request, messages);
		} catch (KmException e) {
			e.printStackTrace();
			//System.out.println("KmException in Inserting RC Data: "+e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();

			//System.out.println("Exception in Inserting RC Data: "+e.getMessage());
			
		}
	 
	return viewRCData(mapping,form,request,response);
  }//insertRCData


  public ActionForward viewRCData(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
	  
	  ActionMessages messages = new ActionMessages();
	  KmRCContentUploadFormBean formBean = (KmRCContentUploadFormBean) form;
	  String documentId;
		  if(request.getParameter("docID")!=null)
			{
			  documentId = request.getParameter("docID").toString();
			}
		  else
		  {
			  documentId = formBean.getDocumentId();
		  }
		  
		  
	  KmRcContentUploadService rechargeService = new KmRcContentUploadServiceImpl();
	  KmRcDataDTO dto;
	  String fwdJsp = "";
	  String[] createMultiple = new String[1];
	  HttpSession session = request.getSession();
	  KmUserMstr sessionUserBean = (KmUserMstr) session.getAttribute("USER_INFO");
		
	  try {
		  
		  if(documentId != "")
		  { 
		  dto = rechargeService.getRcData(documentId);
		  
		  String[] headers = dto.getHeaderList().toArray(new String[dto.getHeaderList().size()]);
		  String [] contents = dto.getValueList().toArray(new String[dto.getValueList().size()]);
		  String [] configList = dto.getConfigList().toArray(new String[dto.getConfigList().size()]);
		  createMultiple[0]= dto.getCircleId()+"";
		  String mobileNo = sessionUserBean.getMsisdn();
		  logger.info("Mobile No : "+mobileNo);
		  if((mobileNo != null) && !(mobileNo.equals(""))){
			  logger.info("From UD.....");
			  formBean.setMobileNo(mobileNo);
		  }
		  formBean.setHeaders(headers);
		  formBean.setContents(contents);
		  formBean.setConfigList(configList);
		  formBean.setTopic(dto.getRcTopic());
		  formBean.setRechargeValue(dto.getRcValue());
		  formBean.setStartDt(dto.getStartDt().toString());
		  formBean.setEndDt(dto.getEndDt().toString());
		  formBean.setElementFolderPath(dto.getContentPath());
		  formBean.setSelectedCombo(dto.getRcType());
		  formBean.setCreateMultiple(createMultiple);
		  
		  
		  formBean.setDocId(documentId);
		  request.setAttribute("docID", documentId);
		  formBean.setKmActorId(sessionUserBean.getKmActorId());
		  
		 
		  request.setAttribute("kmRCContentUploadFormBean", formBean);
		  
		  if(sessionUserBean.isCsr())
			{
			  KmDocumentService service = new KmDocumentServiceImpl();
			  service.increaseDocHitCount(documentId,sessionUserBean.getUserId());
			  fwdJsp = "displayRcDataCsr";
			}
		  else
			 fwdJsp = "displayRcData";
			
		  
		  }
		  
		  else
		  {
			  messages.add("msg1",new ActionMessage("recharge.not.uploaded"));
			  fwdJsp = "pathNotExist";
		  }

	  }
	  catch (KmException e) {
		  	e.printStackTrace();
			//System.out.println("KmException in getting RC Data: "+e.getMessage());
			
		}
	  catch (Exception e) {
		  	e.printStackTrace();
			//System.out.println("Exception in getting RC Data: "+e.getMessage());
			
		}
	  
	  
		//System.out.println("Forward To: "+fwdJsp);
		saveMessages(request, messages);
		
		
	  return mapping.findForward(fwdJsp);
	  
  }
  
  public ActionForward editRcData(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {

	  ActionMessages messages = new ActionMessages();
	  KmRCContentUploadFormBean formBean = (KmRCContentUploadFormBean) form;
	  String documentId ="";
	  if(request.getParameter("docID")!=null)
		{
		  documentId = request.getParameter("docID").toString();
		}
	  else
	  {
		  documentId = formBean.getDocumentId();
	  }
	  
	  KmRcContentUploadService rechargeService = new KmRcContentUploadServiceImpl();
	  KmRcDataDTO dto;
	  String fwdJsp = "";
	  String[] createMultiple = new String[1];
	  
	  try {
		  
		  if(documentId != "")
		  { 
		  dto = rechargeService.getRcData(documentId);
		  ArrayList<KmRcCategoryDTO> rcCategoryList = rechargeService.getRcCategory();
		  
		  String[] headers = dto.getHeaderList().toArray(new String[dto.getHeaderList().size()]);
		  String [] contents = dto.getValueList().toArray(new String[dto.getValueList().size()]);
		  String [] configList = dto.getConfigList().toArray(new String[dto.getConfigList().size()]);
		  createMultiple[0]= dto.getCircleId()+"";
		  
		  //System.out.println("\n createMultiple[0]"+createMultiple[0]);

			formBean.setComboList(rechargeService.getCombo());
			formBean.setFieldList(rechargeService.getHeaders());
			request.setAttribute("comboList",formBean.getComboList());
			request.setAttribute("fieldList",formBean.getFieldList());
			formBean.setRcCategoryList(rcCategoryList);
			
		  formBean.setHeaders(headers);
		  formBean.setContents(contents);
		  formBean.setConfigList(configList);
		  formBean.setTopic(dto.getRcTopic());
		  formBean.setRechargeValue(dto.getRcValue());
		  formBean.setStartDt(dto.getStartDt().toString());
		  formBean.setEndDt(dto.getEndDt().toString());
		  formBean.setElementFolderPath(dto.getContentPath());
		  formBean.setSelectedCombo(dto.getRcTypeId());
		  formBean.setSelectedRcCategory(dto.getRcCategoryId());
		  formBean.setCreateMultiple(createMultiple);
		  messages.add("msg1",new ActionMessage("recharge.uploaded"));
		  request.setAttribute("kmRCContentUploadFormBean", formBean);
		  request.setAttribute("rcCategoryList", formBean.getRcCategoryList());
		  fwdJsp = "editRcData";
		  }
		  
		  else
		  {
			  messages.add("msg1",new ActionMessage("recharge.not.uploaded"));
			  fwdJsp = "pathNotExist";
		  }

	  }
	  catch (KmException e) {
		  	e.printStackTrace();
			//System.out.println("KmException in getting RC Data: "+e.getMessage());
			
		}
	  catch (Exception e) {
		  	e.printStackTrace();
			//System.out.println("Exception in getting RC Data: "+e.getMessage());
			
		}
	  
	  
	  saveMessages(request, messages);
	  
	  return mapping.findForward(fwdJsp);
	  
} 
  
  public ActionForward updateRCData(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {

	  KmRCContentUploadFormBean formBean = (KmRCContentUploadFormBean) form;
	  String [] configList = formBean.getConfigList();
	  for(int i=0;i<formBean.getFieldList().size();i++){
		  logger.info("isConfig from updateRC formBean   - -   " + configList[i]);
		  logger.info("isConfig from report request   - -   " + request.getParameter("report["+i+"].isConfigurable"));
		  logger.info("content from report request   - -   " + request.getParameter("report["+i+"].content"));
		  
	  }
	  String documentId = formBean.getDocId();
	  formBean.setDocumentId(documentId);
	  
	  logger.info("DocID from form bean   -----   " + documentId);
	  logger.info("DociD from request inside update method    ------     "+ request.getParameter("docID"));
	  KmRcContentUploadService rechargeService = new KmRcContentUploadServiceImpl();
	  KmDocumentMstrDao kmDocumentMstrDao  = new KmDocumentMstrDaoImpl();
	  KmUserMstr userBean = (KmUserMstr) request.getSession().getAttribute("USER_INFO");
	  
	  try {
		  
		  //System.out.println("\n\ndocumentId in updateRCData "+documentId);
			
		  rechargeService.deleteRcData(documentId);
		   
	      kmDocumentMstrDao.deleteDocument(documentId, userBean.getUserId());

	  }
	  catch (KmException e) {
		  	e.printStackTrace();
			//System.out.println("KmException in updating RC Data: "+e.getMessage());
			
		}
	  catch (Exception e) {
		  	e.printStackTrace();
			//System.out.println("Exception in updating RC Data: "+e.getMessage());
			
		}
	  		
	  return insertRCData(mapping,formBean,request,response);
	  
  }//updateRCData
  
  public ActionForward initView(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		try {
		logger.info("Inside initView method!!!!");
		ArrayList<HashMap<String,String>> valueList = new ArrayList<HashMap<String,String>>();
		KmRCContentUploadFormBean formBean = (KmRCContentUploadFormBean) form;
		KmRcContentUploadService rechargeService = new KmRcContentUploadServiceImpl();
		formBean.setComboList(rechargeService.getCombo());
		formBean.setFieldList(rechargeService.getHeaders());
		request.setAttribute("comboList",formBean.getComboList());
		request.setAttribute("fieldList",formBean.getFieldList());
		formBean.setValueList(valueList);
		request.setAttribute("valueList",valueList);
		ArrayList<KmRcCategoryDTO> rcCategoryList = rechargeService.getRcCategory();
		formBean.setRcCategoryList(rcCategoryList);
		request.setAttribute("rcCategoryList", formBean.getRcCategoryList());


		//formBean.setFirstView("false");
		//String[] checkedCombos = {"on","on","on","on","on"};
		//formBean.setCheckedCombos(checkedCombos);
		} catch (KmException e) {
			//System.out.println("KmException in Loading page for RC Content View Csr: "+e.getMessage());
			
		} catch (Exception e) {
			//System.out.println("Exception in Loading page for RC Content View Csr: "+e.getMessage());
			
		}
		return mapping.findForward("viewRCCsr");

	}//init
  
  
  public ActionForward viewRcCsr(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		try {
		logger.info("Inside viewRcCsr method!!!!");
		KmRCContentUploadFormBean formBean = (KmRCContentUploadFormBean) form;
		KmRcContentUploadService rechargeService = new KmRcContentUploadServiceImpl();
		formBean.setComboList(rechargeService.getCombo());
		formBean.setFieldList(rechargeService.getHeaders());
		request.setAttribute("comboList",formBean.getComboList());
		request.setAttribute("fieldList",formBean.getFieldList());
		
		String[] checkedCombos = formBean.getCheckedCombos();
		String combos = "";
		if(checkedCombos != null)
		{
		for(int i=0 ; i<checkedCombos.length ;i++)
		{
		   combos = combos + checkedCombos[i] + ",";
		}	
		combos = combos.substring(0,(combos.length() -1));
		}

		// change from here
		HttpSession session = request.getSession();
		ArrayList<String> filteredDocIdList = null;
		KmUserMstr sessionUserBean = (KmUserMstr) session.getAttribute("USER_INFO");
		KmDocumentService docService = new KmDocumentServiceImpl();
		KmSearch searchDto=new KmSearch();
		ArrayList<String> documentList=null;			
			String circleId=(String) session.getAttribute("CURRENT_CIRCLE_ID");
			if(circleId!=null){
				searchDto.setElementId(circleId);
			}else{
				searchDto.setElementId(sessionUserBean.getElementId());
			}
			searchDto.setActorId(sessionUserBean.getKmActorId());
/*			if(sessionUserBean.getKmActorId().equals(Constants.CIRCLE_CSR)||sessionUserBean.getKmActorId().equals(Constants.CATEGORY_CSR)){
				searchDto.setSearchType("CSR_KEYWORD");
			}
			else{
				searchDto.setSearchType("ADMIN_KEYWORD");
			}
*/
			KmSearchService searchService=new KmSearchServiceImpl();
			searchDto.setKeyword(formBean.getRechargeValue());
			searchDto.setRcCategoryId(formBean.getSelectedRcCategory());
			searchDto.setDateCheck("OFF");
//			System.out.println("formBean.getRechargeValue()" + formBean.getRechargeValue());
			documentList=searchService.contentSearch(searchDto);
//			System.out.println("documentList" + documentList);
		filteredDocIdList = docService.docFilterAsPerDocType(documentList,Constants.DOC_TYPE_RC,formBean.getIsTop15());
		// till here
	//	System.out.println("filteredDocIdList" + filteredDocIdList);
		ArrayList<HashMap<String,String>> valueList = new ArrayList<HashMap<String,String>>();
		if(filteredDocIdList.size() == 0) 
		{
			formBean.setFirstView("No Record Found");
		} else {
				valueList = rechargeService.getRcDataCsr(formBean.getRechargeValue(),formBean.getSelectedRcCategory(),combos, filteredDocIdList);
				if(valueList.size() == 0)
				{
					formBean.setFirstView("No Record Found");
				}
				else
				{
					formBean.setFirstView("");
				}
		}
		
		request.setAttribute("valueList",valueList);
		request.setAttribute("rcContentSearchResult", formBean);
		ArrayList<KmRcCategoryDTO> rcCategoryList = rechargeService.getRcCategory();
		formBean.setRcCategoryList(rcCategoryList);
		request.setAttribute("rcCategoryList", formBean.getRcCategoryList());
		for(int i=0;i<valueList.size();i++){
			logger.info("Value  --    "+ valueList.get(i));
		}

		formBean.setValueList(valueList);
		//String[] checkedCombos1 = {"on","on","on","on","on"};
		//formBean.setCheckedCombos(checkedCombos1);
		formBean.setCheckedCombos(checkedCombos);
		//formBean.setCheckedCombos(null);
		formBean.setRechargeValue("");
		
		} catch (KmException e) {
			e.printStackTrace();
			//System.out.println("KmException in viewRcCsr method: "+e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println("Exception in viewRcCsr method: "+e.getMessage());
			
		}
		return mapping.findForward("viewRCCsr");

	}
  
  
  //Method sendRCSMS added by ABU for sending configurable RC data as SMS
  
 
  public ActionForward sendRCSMS(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {

	  HttpSession session = request.getSession();
	  String smsStatus = "";
	  KmRCContentUploadFormBean formBean = (KmRCContentUploadFormBean) form;
	  KmRcContentUploadService rechargeService = new KmRcContentUploadServiceImpl();
	  String mobileNo = formBean.getMobileNo();
	  String mainOption = "RC";
	  KmDocumentService docService = new KmDocumentServiceImpl();
		
	  KmUserMstr sessionUserBean = (KmUserMstr) session.getAttribute("USER_INFO");
	  String fwdStatus = "";
	  String status ="";
	  int result = 0;
	  KmSearchDetailsDTO kmSearchDTO =  new KmSearchDetailsDTO();
	  KmSearchService searchService = new KmSearchServiceImpl();

	  String documentId = formBean.getDocId();
	  String smsTemplate = "Information requested - ";
	  
	  KmRcDataDTO dto;
	  
	  if(documentId != "")
	  		  { 
	  		  dto = rechargeService.getRcData(documentId); 
	  		  
	  		  String[] headers = dto.getHeaderList().toArray(new String[dto.getHeaderList().size()]);
	  		  String [] contents = dto.getValueList().toArray(new String[dto.getValueList().size()]);
	  		  String [] configList = dto.getConfigList().toArray(new String[dto.getConfigList().size()]);
	  		  
	  		  for(int i=0;i<dto.getConfigList().size();i++){
	  			  
	  			  if(configList[i].equals("Y")){
	  				smsTemplate = smsTemplate + headers[i] + ": " + contents[i] + ". ";
	  			  }
	  		  }
	  		  logger.info("Message prepared    ---     "+smsTemplate);
	  		}
	  if(smsTemplate.equals("Information requested - ")){
		  formBean.setSmsStatus("SMS could not be sent : No configured data found for this RC.");
	  }
	  
	  else{
	  try{
	  	SendSMSXML sendSMSXml = new SendSMSXML();
		logger.info("########## Mobile Number From UD login ::::::::: "+request.getParameter("mobileNo"));
		if(!request.getParameter("mobileNo").equals("") || request.getParameter("mobileNo") != null){
			//if(!formBean.getMobileNo().equals("")||formBean.getMobileNo()!= null){
				logger.info("if blockl ");
			//mobileNo = formBean.getMobileNo();
				mobileNo = request.getParameter("mobileNo");
			}
			
		else {
			logger.info("else block ");
			//mobileNo = request.getParameter("mobileNo");
			mobileNo = formBean.getMobileNo();
		}
		try{
		logger.info("Mobile Number !! : " + mobileNo);
		int numberOfSentSms = docService.getNumberOfSms(mobileNo);
		int maxSMS = docService.getMaxNoOfSMS();
		if(numberOfSentSms<maxSMS){
		status = sendSMSXml.sendSms(mobileNo, smsTemplate);
			logger.info("Status :   " + status);
			if (status.equals(Constants.SEND_SMS_STATUS_SUCCESS)) {
				logger.info("Hurray !!! msg gone @@@@@@@@@");
				formBean.setSmsStatus("SMS Successfully sent to the user...!!!");
				kmSearchDTO = searchService.getUserDetailForSMS(sessionUserBean.getUserLoginId());
				logger.info("Sender :: "+sessionUserBean.getUserLoginId()+" ## Mobile Number :"+mobileNo+" ## smsTemplate : "+smsTemplate
							+" ## mainOption "+mainOption+" ## Circle ID = "+sessionUserBean.getCircleId()+" ## Partner : "+kmSearchDTO.getPartner()+
							" ## Location :: "+kmSearchDTO.getLocation());
					String udId = sessionUserBean.getUdId();
					//String udId = "A1247263";
					logger.info("UD Id is ::: "+udId);
					if(!udId.equals("") &&  udId != null){
						logger.info("Sending UDID .... ");
						result = searchService.insertSMSDetails(kmSearchDTO.getOlmId(),sessionUserBean.getUserLoginId(), mobileNo, smsTemplate, mainOption, sessionUserBean.getCircleId(),kmSearchDTO.getPartner(),kmSearchDTO.getLocation(),"Success",udId);
					}else{
						logger.info("Sending user login id .... ");
						result = searchService.insertSMSDetails(kmSearchDTO.getOlmId(),sessionUserBean.getUserLoginId(), mobileNo, smsTemplate, mainOption, sessionUserBean.getCircleId(),kmSearchDTO.getPartner(),kmSearchDTO.getLocation(),"Success","");
					}
					logger.info("SMS Data Inserted in database" +result);
					//formBean.setSmsStatus("SMS '" + smsTemplate	+ "' sent to " + mobileNo);
				}

			else if (status.equals(Constants.SEND_SMS_STATUS_FAIL)) {
				logger.info(":((((((( msg didnt go !!!!!!!");
				formBean.setSmsStatus("SMS could not be sent...");
				//smsStatus = "failed";
			}
			else if (status.equals(Constants.SEND_SMS_STATUS_NOT_CONNECTED)) {
				logger.info(":((((((( msg didnt go due to network error !!!!!!!");
				formBean.setSmsStatus("SMS could not be sent... "+ Constants.SEND_SMS_STATUS_NOT_CONNECTED);
				logger.info("Gadha ::: "+formBean.getSmsStatus());
			}

		}
		else {
			logger.info("From rcContentUpload   ---   Number of SMS sent is more that 5...");
			formBean.setSmsStatus("Couldn't Send the SMS: Maximum number of SMS is already sent for the day to the user...");
		}
			}
			catch (Exception ex) {
			logger.info("Exception  :" + ex.getMessage());
			formBean.setSmsStatus("SMS could not be sent due to some network issue...");
			logger.info("Goru ::: "+formBean.getSmsStatus());
			ex.printStackTrace();
		}
	  }
	  
	  catch (Exception ex) {
			logger.info("Exception  :" + ex.getMessage());
			ex.printStackTrace();
				formBean.setSmsStatus("Could not send sms due to invalid mobile number");
			 
		}
	  }

		request.setAttribute("kmRCContentUploadFormBean", formBean);
		
		//System.out.println("smsStat in formBean in rcAction class    ----  " + formBean.getSmsStatus()); 
		//System.out.println("smsStatus from rcAction when seting in session -------   "+formB.getSmsStatus());
		
		fwdStatus = "smsStatus";
		return mapping.findForward(fwdStatus);	
	  }
  
  
  //End of method sendRCSMS
  
  
  
}

