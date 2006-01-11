package com.ibm.km.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import com.ibm.km.common.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.json.JSONObject;

import com.ibm.km.common.PropertyReader;
import com.ibm.km.dao.impl.KmDocumentMstrDaoImpl;
import com.ibm.km.dto.FileReportDto;
import com.ibm.km.dto.KmDocumentMstr;
import com.ibm.km.dto.KmElementMstr;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.forms.KmFileReportFormBean;
import com.ibm.km.search.IndexFiles;
import com.ibm.km.services.KmCategoryMstrService;
import com.ibm.km.services.KmCircleMstrService;
import com.ibm.km.services.KmElementMstrService;
import com.ibm.km.services.KmFileReportService;
import com.ibm.km.services.KmSubCategoryMstrService;
import com.ibm.km.services.impl.KmCategoryMstrServiceImpl;
import com.ibm.km.services.impl.KmCircleMstrServiceImpl;
import com.ibm.km.services.impl.KmElementMstrServiceImpl;
import com.ibm.km.services.impl.KmFileReportServiceImpl;
import com.ibm.km.services.impl.KmSubCategoryMstrServiceImpl;


/**
 * @version 	1.0
 * @author		Atishay
 */


public class KmFileReportAction extends DispatchAction {
	/**
	* Logger for the class.
	**/
	private static final Logger logger;

	static {

		logger = Logger.getLogger(KmFileReportAction.class);
	}
	/**
	 * Initializes the search file page by loading all the circles for super admin. 
	 * For Circle approvers and circle users it loads all categories of the curresponding circle
	**/
	public ActionForward init(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		
		ActionErrors errors = new ActionErrors();
		ActionForward forward = new ActionForward(); // return value
		KmFileReportFormBean KmFileReportFormBean = (KmFileReportFormBean) form;
		/*	 Kmphase II	add the default date to jsp page :*/
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String date = sdf.format(new java.util.Date());
		date = date.substring(0,10);
		KmFileReportFormBean.setFromDate(date);
		try {
			KmFileReportFormBean.setFilesAddedList(null);
			KmFileReportFormBean.setFilesDeletedList(null);
			KmFileReportFormBean.setApproverList(null);
			KmFileReportFormBean.setReportType("");
			KmFileReportFormBean.setInitStatus("yes");
			forward=mapping.findForward("viewReport");	
		} catch (Exception e) {
			logger.error("Exception occured while initializing the search file page :"+e);
			
		}
		// Finish with
		return (forward);

	}
	public ActionForward loadElementDropDown(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response){
			String parentId = request.getParameter("ParentId");
			KmElementMstrService kmElementService = new KmElementMstrServiceImpl();

			try {
				JSONObject json = kmElementService.getElementsAsJson(parentId);
				response.setHeader("Cache-Control", "no-cache");
				response.setHeader("Content-Type", "application/x-json");
				response.getWriter().print(json);		
			} catch (IOException e) {
				logger.error("IOException in Loading Element Dropdown: "+e.getMessage());
				
			} catch (Exception e) {
				logger.error("Exception in Loading Element Dropdown: "+e.getMessage());
				
			}
		
			return null;
		}
	
	/**
	* Loads all categories under the selected circle for super admin user
	**/
	public ActionForward loadCategory(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		KmFileReportFormBean KmFileReportFormBean =
			(KmFileReportFormBean) form;
			KmCategoryMstrService kmCategoryService=new KmCategoryMstrServiceImpl();
		try{
			String circleId=KmFileReportFormBean.getCircleId();
			String[] circleIdArray = {circleId};
		//Loading the categories
			KmFileReportFormBean.setCategoryList(kmCategoryService.retrieveCategoryService(circleIdArray));
			//Retrieving circle wise files for Super Admin 
				
		}catch(Exception e){
			logger.error("Exception occured while loading categories and listing circle wise files:"+e);
		}return mapping.findForward("viewReport");
	}
	/**
	* Loading sub-categories for selected category
	**/
	public ActionForward loadSubCategory(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		KmFileReportFormBean KmFileReportFormBean =
			(KmFileReportFormBean) form;
		KmSubCategoryMstrService subCategoryService=new KmSubCategoryMstrServiceImpl();
		try{
			String categoryId=KmFileReportFormBean.getCategoryId();
			
			
			//Loading sub categories
			KmFileReportFormBean.setSubCategoryList(subCategoryService.retrieveSubCategoryService(categoryId));
				
		
		}catch(Exception e){
			logger.error("Exception occured while loading sub-categories :"+e);
		}return mapping.findForward("viewReport");
	}
	
	public ActionForward viewReport(
				ActionMapping mapping,
				ActionForm form,
				HttpServletRequest request,
				HttpServletResponse response)
				throws Exception {
				ActionMessages messages=new ActionMessages();
				KmFileReportFormBean kmFileReportFormBean = (KmFileReportFormBean) form;
				HttpSession session = request.getSession();
				KmUserMstr sessionUserBean = (KmUserMstr) session.getAttribute("USER_INFO");	
				KmFileReportService kmFileReportService= new KmFileReportServiceImpl();
				KmElementMstrService elementSerice=new KmElementMstrServiceImpl();
				ArrayList circleList=null;
				try {
				    logger.info("Inside viewReport method");
					//Code change after UAT observation
					String fromDate=kmFileReportFormBean.getSelectDate();
					String toDate=kmFileReportFormBean.getFromDate();
					String elementId=sessionUserBean.getElementId();		
					String parentId=kmFileReportFormBean.getParentId();		
					String initialSelectBox=kmFileReportFormBean.getInitialSelectBox();
					logger.info("select box:"+initialSelectBox+"parentId:"+parentId);
					
					circleList=elementSerice.getAllChildrenRec(elementId,"3");
					//String[] circleIds=(String[]) circleList.toArray(new String[circleList.size()]);
				
					//Added by Atul
					if(kmFileReportFormBean.getReportType().equals("filesAdded"))
						{
							
						
							kmFileReportFormBean.setFilesAddedList(kmFileReportService.getAddedFileList(circleList,toDate));
							KmElementMstr eleDto=null;
							int totalCount=0;
							for(Iterator i=kmFileReportFormBean.getFilesAddedList().iterator();i.hasNext();){
								eleDto=(KmElementMstr)i.next();
							    totalCount=totalCount+Integer.parseInt(eleDto.getDocumentCount());
							}
							kmFileReportFormBean.setAddedFileCount(totalCount);
							kmFileReportFormBean.setReportType("filesAdded");
					}
						
					if(kmFileReportFormBean.getReportType().equals("filesDeleted"))
						{
							kmFileReportFormBean.setFilesDeletedList(kmFileReportService.getDeletedFileList(circleList,toDate));
							KmElementMstr eleDto=null;
							int totalCount=0;
							for(Iterator i=kmFileReportFormBean.getFilesDeletedList().iterator();i.hasNext();){
								eleDto=(KmElementMstr)i.next();
								totalCount=totalCount+Integer.parseInt(eleDto.getDocumentCount());
							}
							kmFileReportFormBean.setDeletedFileCount(kmFileReportService.getDeletedFileList(elementId,toDate));
							kmFileReportFormBean.setReportType("filesDeleted");
						}
											
					
					if(kmFileReportFormBean.getReportType().equals("filesAddedCircle"))
						{
							logger.info("ELEMENT_ID : "+elementId);
							kmFileReportFormBean.setFilesAddedList(kmFileReportService.getFileCircleList(elementId,toDate));
							kmFileReportFormBean.setReportType("filesAddedCircle");
						}
			/*		if(kmFileReportFormBean.getReportType().equals("filesApprovedList"))
						{
							kmFileReportFormBean.setFilesAddedList(kmFileReportService.getFileApprovedList(parentId,toDate));
							kmFileReportFormBean.setReportType("filesApprovedList");
						}	
					if(kmFileReportFormBean.getReportType().equals("getTotalFileCount"))
						{
							kmFileReportFormBean.setAddedFileCount(kmFileReportService.getTotalFileCount(elementId));
							kmFileReportFormBean.setReportType("getTotalFileCount");
						}		*/						
					//Ended by Atul
				//	if(kmFileReportFormBean.getApproverList().isEmpty()){messages.add("msg",new ActionMessage("viewAllUser.NotFound"));logger.info("ashgdash");}
				//	logger.info(kmFileReportFormBean.getApproverList());			
					logger.info("Listing Report");	
					
					
					
					
				} catch (Exception e) {
					
					logger.error("Exception occured while listing report :"+e);
				
				}
				

				saveMessages(request,messages);
				kmFileReportFormBean.setSelectedParentId(kmFileReportFormBean.getParentId());
				kmFileReportFormBean.setInitialSelectBox("-1");
				return mapping.findForward("viewReport");
                
			}
	public ActionForward initApprovedFiles(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		ActionErrors errors = new ActionErrors();
		ActionForward forward = new ActionForward(); // return value
		KmFileReportFormBean KmFileReportFormBean = (KmFileReportFormBean) form;
		HttpSession session = request.getSession();
		KmUserMstr sessionUserBean = (KmUserMstr) session.getAttribute("USER_INFO");
		KmCircleMstrService circleService=new KmCircleMstrServiceImpl();
		KmFileReportService kmFileReportService= new KmFileReportServiceImpl();
		ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
		try {
			
				logger.info(sessionUserBean.getUserLoginId()+" Entered into the initApprovedFiles method of KmDocumentMstrAction");
				KmFileReportFormBean.setInitialSelectBox("-1");
				KmElementMstrService kmElementService = new KmElementMstrServiceImpl();
				List firstDropDown;
				if (sessionUserBean.getKmActorId().equals(bundle.getString("LOBAdmin"))||sessionUserBean.getKmActorId().equals(bundle.getString("Superadmin"))) {
					firstDropDown = kmElementService.getAllChildren(sessionUserBean.getElementId());
				}else{
					firstDropDown = kmElementService.getChildren(sessionUserBean.getElementId());
				}					
				if (firstDropDown!=null && firstDropDown.size()!=0){
					KmFileReportFormBean.setInitialLevel(((KmElementMstr)firstDropDown.get(0)).getElementLevel());
				}
				else{
						
					int initialLevel=Integer.parseInt(kmElementService.getElementLevelId(sessionUserBean.getElementId()));
					initialLevel++;
					KmFileReportFormBean.setInitialLevel(initialLevel+"");
								}
				KmFileReportFormBean.setParentId(sessionUserBean.getElementId());
				request.setAttribute("elementLevelNames",kmElementService.getAllElementLevelNames());
				request.setAttribute("allParentIdString",kmElementService.getAllParentIdString("1",sessionUserBean.getElementId()));
				request.setAttribute("firstList",firstDropDown);
				
				forward=mapping.findForward("approvedFilesReport");	
		} catch (Exception e) {
			logger.error("Exception occured while initializing the approvedFiles Report file page :"+e);
			
		}
		// Finish with
		return (forward);

	}
	public ActionForward approvedRejectedFiles(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		ActionErrors errors = new ActionErrors();
		ActionForward forward = new ActionForward(); // return value
		KmFileReportFormBean formBean = (KmFileReportFormBean) form;
		HttpSession session = request.getSession();
		KmUserMstr sessionUserBean = (KmUserMstr) session.getAttribute("USER_INFO");
		KmCircleMstrService circleService=new KmCircleMstrServiceImpl();
		KmFileReportService kmFileReportService= new KmFileReportServiceImpl();
		ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
		KmElementMstrService eleService=new KmElementMstrServiceImpl();
		FileReportDto dto=null;
		try {
			
			logger.info(sessionUserBean.getUserLoginId()+" Entered into the approvedRejectedFiles method of KmDocumentMstrAction");
			String elementId=formBean.getParentId();
			formBean.setParentId1(elementId);
			logger.info(elementId+"       ELEMENT  :  ID");
			dto=kmFileReportService.getApprovedRejectedFileCount(elementId);
			String path="";
			path=eleService.getAllParentNameString(sessionUserBean.getElementId(),elementId);
			if(path!=null){
				path=path.substring((path.indexOf(">")+1),path.length());
			}
			dto.setElementPath(path);
			
			request.setAttribute("FILE_COUNT_DTO",dto);	
		
		} catch (Exception e) {
			logger.error("Exception occured while generating the Approved/Rejected File Report page :"+e);
			
		}
		// Finish with
		return initApprovedFiles(mapping,formBean,request,response);

	}

/* kmphase II all reports format exported into excel */

public ActionForward FilesAddedExcelReport(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		ActionErrors errors = new ActionErrors();
		ActionForward forward = new ActionForward(); // return value
		KmFileReportFormBean kmFileReportFormBean = (KmFileReportFormBean) form;
		try {
		logger.info( "Entered into the FilesAddedExcel report of KmReportMstrAction");
		
		} catch (Exception e) {
		
		logger.error("Exception occured while listing report :"+e);
		}
		return mapping.findForward("fileAddedExcelReport" );
	}
		
	public ActionForward FilesDeletedExcelReport(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
			ActionErrors errors = new ActionErrors();
			ActionForward forward = new ActionForward(); // return value
			KmFileReportFormBean kmFileReportFormBean = (KmFileReportFormBean) form;
			try {
			logger.info( "Entered into the FilesDeletedExcel report of KmReportMstrAction");
			} catch (Exception e) {
			
			logger.error("Exception occured while listing report :"+e);
			}
			return mapping.findForward("fileDeletedExcelReport" );
		}	
		
	public ActionForward FileAddedExcelListReport(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
			ActionErrors errors = new ActionErrors();
			ActionForward forward = new ActionForward(); // return value
			KmFileReportFormBean kmFileReportFormBean = (KmFileReportFormBean) form;
			try {
			logger.info( "Entered into the FileAddedExcelList report of KmReportMstrAction");
			} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception occured while listing report :"+e);
			}
			return mapping.findForward("fileAddedExcelListReport" );
		}	
		
		public ActionForward approvedRejectedExcelReport(
					ActionMapping mapping,
					ActionForm form,
					HttpServletRequest request,
					HttpServletResponse response)
					throws Exception {

					ActionErrors errors = new ActionErrors();
					ActionForward forward = new ActionForward(); // return value
					KmFileReportFormBean formBean = (KmFileReportFormBean) form;
					HttpSession session = request.getSession();
					KmUserMstr sessionUserBean = (KmUserMstr) session.getAttribute("USER_INFO");
					KmCircleMstrService circleService=new KmCircleMstrServiceImpl();
					KmFileReportService kmFileReportService= new KmFileReportServiceImpl();
					ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
					KmElementMstrService eleService=new KmElementMstrServiceImpl();
					FileReportDto dto=null;
					try {
			
						logger.info(sessionUserBean.getUserLoginId()+" Entered into the approvedRejectedExcelReport method of KmDocumentMstrAction");
						String elementId=formBean.getParentId1();
						dto=kmFileReportService.getApprovedRejectedFileCount(elementId);
						String path="";
						path=eleService.getAllParentNameString(sessionUserBean.getElementId(),elementId);
						if(path!=null){
							path=path.substring((path.indexOf(">")+1),path.length());
						}
						dto.setElementPath(path);
						request.setAttribute("FILE_COUNT_DTO",dto);	
		
					} catch (Exception e) {
						logger.error("Exception occured while generating the Approved/Rejected File Report page :"+e);
			
					}
					// Finish with
					return mapping.findForward("approvedFilesExcelReport" );

				}		
		
		public ActionForward initCSRWiseReport(
				ActionMapping mapping,
				ActionForm form,
				HttpServletRequest request,
				HttpServletResponse response)
				throws Exception {

				ActionErrors errors = new ActionErrors();
				
				KmFileReportFormBean formBean = (KmFileReportFormBean) form;
				HttpSession session = request.getSession();
				KmUserMstr sessionUserBean = (KmUserMstr) session.getAttribute("USER_INFO");
				String yesterDay="";
				try {
		
					logger.info(sessionUserBean.getUserLoginId()+" Entered into the initCSRWiseReport");
					if(sessionUserBean.getKmActorId().equals(Constants.CIRCLE_ADMIN)|| sessionUserBean.getKmActorId().equals(Constants.REPORT_ADMIN)){
						GregorianCalendar gc = new GregorianCalendar();
						gc.add(gc.DATE, -1);
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
						Date yest = gc.getTime();
						yesterDay  = sdf.format(yest);
						yesterDay = yesterDay.substring(0,10);
						formBean.setSelectDate(yesterDay);
						return mapping.findForward("downloadReport");
					}
					
	
				} catch (Exception e) {
					logger.error("Exception occured while initializing the CSR Wise Document report ");
		
				}
				// Finish with
				return null;

			}	
		
		public ActionForward downloadReport(
				ActionMapping mapping,
				ActionForm form,
				HttpServletRequest request,
				HttpServletResponse response)
				throws Exception {

				KmFileReportFormBean formBean = (KmFileReportFormBean) form;
				ActionErrors errors = new ActionErrors();
				KmUserMstr sessionUserBean = (KmUserMstr) request.getSession().getAttribute("USER_INFO");
			
				try {
					logger.info("Selected date "+formBean.getSelectDate());
					String date = formBean.getSelectDate();
					String[] dateProps = date.split("-");
					date="";
					for(int i = 0;i<dateProps.length;i++)
						date = date+Integer.parseInt(dateProps[i])+"-";
					
					date = date.substring(0,date.lastIndexOf("-"));
					logger.info("date "+date);
					StringBuffer filePath=new StringBuffer(PropertyReader.getAppValue("CSR.Document.Report.Path"));
					filePath.append(sessionUserBean.getElementId()).append("/").append(date);
					File file=new File(filePath.toString());
					//System.out.println(filePath);
					if(file.length()==0){
						errors.add("file.not.found", new ActionError("report.not.found"));
						saveErrors(request, errors);
						return mapping.findForward("downloadReport");
					}
					response.setContentType ("application/vnd.ms-excel");
					response.setHeader("Cache-Control", "no-cache");
					response.setContentLength((int)file.length());
					response.setHeader("Content-Disposition","attachment; filename=CSRWiseDocumentCountReport.csv");
					response.setHeader("Cache-Control", "no-cache");
					OutputStream outStream =response.getOutputStream();
					FileInputStream inStream = new FileInputStream(file);
					try {	 
						 
						 byte[] buf = new byte[8192];
						
						 int sizeRead = 0;
						 
						 while ((sizeRead = inStream.read(buf, 0, buf.length)) > 0) {
							
							 outStream.write(buf, 0, sizeRead);
						 }
						 inStream.close();
						 outStream.close();
					}
					 catch(IllegalStateException
								ignoredException) {
						if(outStream!=null){
							outStream.close();			
						}
						if(inStream!=null){
							inStream.close();			
						}
						logger.error(ignoredException);
					}	 
					
	
				} catch (Exception e) {
					errors.add("file.not.found", new ActionError("report.not.found"));
					saveErrors(request, errors);
					initCSRWiseReport(mapping,formBean,request,response);
		
				}
				// Finish with
				return null;

			}
		
		
		
}
	
