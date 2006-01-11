package com.ibm.km.actions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
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

import com.ibm.km.dto.KmElementMstr;
import com.ibm.km.dto.KmFeedbackMstr;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.forms.KmFeedbackMstrFormBean;
import com.ibm.km.forms.KmLoginFormBean;
import com.ibm.km.services.KmCircleMstrService;
import com.ibm.km.services.KmElementMstrService;
import com.ibm.km.services.KmFeedbackMstrService;
import com.ibm.km.services.impl.KmCircleMstrServiceImpl;
import com.ibm.km.services.impl.KmElementMstrServiceImpl;
import com.ibm.km.services.impl.KmFeedbackMstrServiceImpl;

/**
 * @version 	1.0
 * @author		Anil
 */
public class KmFeedbackMstrAction extends DispatchAction {

	/**
	 * Logger for the class.
	**/
	private static final Logger logger;

	static {

		logger = Logger.getLogger(KmFeedbackMstrAction.class);
	}
	/**
	 * Initializes Create Feedback page
	 **/

	public ActionForward init(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
			KmFeedbackMstrFormBean formBean =
			(KmFeedbackMstrFormBean) form;
			
			KmUserMstr userBean = (KmUserMstr) request.getSession().getAttribute("USER_INFO");
			ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
			try{
			
			logger.info(userBean.getUserLoginId()+ " Entered in to the init method of KmFeedbackMstrAction");
			
			KmElementMstrService kmElementService = new KmElementMstrServiceImpl();
			
			List firstDropDown;
			if (userBean.getKmActorId().equals(bundle.getString("LOBAdmin"))||userBean.getKmActorId().equals(bundle.getString("Superadmin"))) {
				firstDropDown = kmElementService.getAllChildren("1");
			}else{
				firstDropDown = kmElementService.getChildren("1");
			}					
			if (firstDropDown!=null && firstDropDown.size()!=0){
				formBean.setInitialLevel("2");
			}
			formBean.setParentId(userBean.getElementId());
			request.setAttribute("elementLevelNames",kmElementService.getAllElementLevelNames());
			request.setAttribute("allParentIdString",kmElementService.getAllParentIdString("1","1"));
			request.setAttribute("firstList",firstDropDown);
			//Populate feedbackList for Csr
			KmFeedbackMstrService kmfeedbackMstr = new KmFeedbackMstrServiceImpl();
			ArrayList feedbackList= kmfeedbackMstr.feedbackResponse(Integer.parseInt(userBean.getUserId()));			//	KmElementMstrService kmElementService = new KmElementMstrServiceImpl();
		    formBean.setFeedbackListMy(feedbackList);
		    //System.out.println("\n\nSize "+feedbackList.size());
		    //Find LOB
		    KmElementMstrService elementMstrService = new KmElementMstrServiceImpl();
			String LOBId = elementMstrService.getParentId(userBean.getElementId());
			//Populate feedbackList for All CSR
			kmfeedbackMstr = new KmFeedbackMstrServiceImpl();
			feedbackList= kmfeedbackMstr.feedbackResponseAll(Integer.parseInt(userBean.getElementId()));			//	KmElementMstrService kmElementService = new KmElementMstrServiceImpl();
		    formBean.setFeedbackListAll(feedbackList);

			}
			catch(Exception e){
				logger.error("Exception occured while initializing feedback page");
				
			}
			return mapping.findForward("createFeedBack");
		}

	/*
	 * Generate a new feedback by the CSR to be sent to Circle User
	 */
	
	
	public ActionForward insert(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
				
				ActionErrors errors = new ActionErrors();
						ActionMessages messages=new ActionMessages();
						ActionForward forward = new ActionForward(); // return value
						KmFeedbackMstrFormBean formBean =
							(KmFeedbackMstrFormBean) form;
						KmLoginFormBean kmLoginFormBean=new KmLoginFormBean();
						KmFeedbackMstr categoryDTO=new KmFeedbackMstr();
						ArrayList circleList = new ArrayList();
						
						KmFeedbackMstrService feedbackService=new KmFeedbackMstrServiceImpl();
						KmCircleMstrService circleService=new KmCircleMstrServiceImpl();
						HttpSession session = request.getSession();
						KmUserMstr sessionUserBean = (KmUserMstr) session.getAttribute("USER_INFO");
						KmFeedbackMstr dto=new KmFeedbackMstr();
						formBean.reset(mapping,request);
						try{
									logger.info(sessionUserBean.getUserLoginId()+ " Entered in to the insert method of KmFeedbackMstrAction");
									formBean.setCreatedBy(sessionUserBean.getUserId());
									
									//Populating DTO
								
										//Setting the element on which the feedback is to be sent.
									
									dto.setElementId(formBean.getParentId());
									dto.setComment(formBean.getComment());
									dto.setCreatedBy(formBean.getCreatedBy());
									//Added for All feedback functionality
									dto.setCircleId(sessionUserBean.getElementId());
									//System.out.println("dto.getElementId()=="+sessionUserBean.getElementId());
									//////////////////////////////////////
									
					
									// Calling create feedback servicex
									feedbackService.createFeedbackService(dto);
									messages.add("msg1",new ActionMessage("feedback.success"));
									saveMessages(request,messages);
									formBean.setComment("");				
									return init(mapping, formBean, request, response);
	
						}catch(Exception e){
							
							logger.error("Exception occured while creating category :"+e);
							errors.add("errors", new ActionError("feedback.failure"));
							saveErrors(request,errors);
							return init(mapping, formBean, request, response);
						}
						
			}
	public ActionForward initReadFeedback(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
				
				ActionErrors errors = new ActionErrors();
						ActionMessages messages=new ActionMessages();
						ActionForward forward = new ActionForward(); // return value
						KmFeedbackMstrFormBean formBean =
							(KmFeedbackMstrFormBean) form;
						KmLoginFormBean kmLoginFormBean=new KmLoginFormBean();
						KmFeedbackMstr categoryDTO=new KmFeedbackMstr();
						ArrayList circleList = new ArrayList();
						
						KmFeedbackMstrService feedbackService=new KmFeedbackMstrServiceImpl();
						KmCircleMstrService circleService=new KmCircleMstrServiceImpl();
						HttpSession session = request.getSession();
						KmUserMstr userBean = (KmUserMstr) session.getAttribute("USER_INFO");
						KmFeedbackMstr dto=new KmFeedbackMstr();
						ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
						try{
							logger.info(userBean.getUserLoginId()+ " Entered in to the initReadFeedback method of KmFeedbackMstrAction");
							KmElementMstrService kmElementService = new KmElementMstrServiceImpl();
							List firstDropDown;
							if (userBean.getKmActorId().equals(bundle.getString("LOBAdmin"))||userBean.getKmActorId().equals(bundle.getString("Superadmin"))) {
								firstDropDown = kmElementService.getAllChildren(userBean.getElementId());
							}else{
								firstDropDown = kmElementService.getChildren(userBean.getElementId());
							}					
							if (firstDropDown!=null && firstDropDown.size()!=0){
								formBean.setInitialLevel(((KmElementMstr)firstDropDown.get(0)).getElementLevel());
							}
							else{
						
								int initialLevel=Integer.parseInt(kmElementService.getElementLevelId(userBean.getElementId()));
								initialLevel++;
								formBean.setInitialLevel(initialLevel+"");
							}
							
							formBean.setParentId(userBean.getElementId());
							request.setAttribute("elementLevelNames",kmElementService.getAllElementLevelNames());
							request.setAttribute("allParentIdString",kmElementService.getAllParentIdString("1",userBean.getElementId()));
							request.setAttribute("firstList",firstDropDown);
							if(request.getAttribute("FEEDBACK_LIST")==null)
								formBean.setInitStatus("true");	
							else
								formBean.setInitStatus("false");
	                        logger.info("Init ststus");
						}catch(Exception e){
							
							logger.error("Exception occured while listing feedbacks :"+e);
							errors.add("errors", new ActionError("feedback.failure"));
							saveErrors(request,errors);
							
						}
				return mapping.findForward("initFeedBack");
			}
	public ActionForward viewFeedback(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
				
				ActionErrors errors = new ActionErrors();
						ActionMessages messages=new ActionMessages();
						ActionForward forward = new ActionForward(); // return value
						KmFeedbackMstrFormBean formBean =(KmFeedbackMstrFormBean) form;
						KmLoginFormBean kmLoginFormBean=new KmLoginFormBean();
						KmFeedbackMstr categoryDTO=new KmFeedbackMstr();
						ArrayList circleList = new ArrayList();
						String path="";
						KmFeedbackMstrService feedbackService=new KmFeedbackMstrServiceImpl();
						KmCircleMstrService circleService=new KmCircleMstrServiceImpl();
						HttpSession session = request.getSession();
						KmUserMstr userBean = (KmUserMstr) session.getAttribute("USER_INFO");
						KmFeedbackMstr dto=new KmFeedbackMstr();
						ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
						KmElementMstrService elementService=new KmElementMstrServiceImpl();
						try{
							
							
												     
							logger.info(userBean.getUserLoginId()+ " Entered in to the viewFeedback method of KmFeedbackMstrAction");
							String elementId=formBean.getParentId();
							formBean.setElementId(elementId);
							String[] elementIds=elementService.getChildrenIds(elementId);
							logger.info("Element Id of Feedback : "+formBean.getParentId());
							
						//Change made against Defect Id MASDB00105338 	
							 path=elementService.getAllParentNameString(userBean.getElementId(),elementId);
							 formBean.setElementFolderPath(path);
							 
							/*Defect MASDB00064285 fixed: Element path for the feedback was starting from India. */
							
							ArrayList feedbackList=feedbackService.viewFeedbacks(elementIds,formBean.getParentId());
							formBean.setFeedbackList(feedbackList);
							request.setAttribute("FEEDBACK_LIST",feedbackList);
							logger.info("Feedbacklist :"+formBean.getFeedbackList());
							formBean.setInitialSelectBox("");
							// Added by Atul- for setting the default value equal to N in Select Box
							String feedArray[]= new String[0];
							String feedResp[]= new String[0];
							if(feedbackList.size()!=0) {
								feedArray=new String[feedbackList.size()];
								feedResp = new String[feedbackList.size()];
							}
							for(int i=0;i<feedArray.length;i++) {
							    feedArray[i]= "N";
							 }
							for(int i=0;i<feedResp.length;i++) {
								feedResp[i]= "";
							 }
							formBean.setFeedbackResp(feedResp);
							formBean.setFeedbackArray(feedArray);
							
							//Ended by Atul
	
						}catch(Exception e){
							
							logger.error("Exception occured while listing feedbacks :"+e);
							errors.add("errors", new ActionError("feedback.failure"));
							saveErrors(request,errors);
							
						}
					return initReadFeedback(mapping, formBean, request, response);	
						
			}
	public ActionForward readFeedback(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
				
				ActionMessages messages=new ActionMessages();
						KmFeedbackMstrFormBean formBean =
							(KmFeedbackMstrFormBean) form;
						ArrayList circleList = new ArrayList();
						
						KmFeedbackMstrService feedbackService=new KmFeedbackMstrServiceImpl();
						KmCircleMstrService circleService=new KmCircleMstrServiceImpl();
						HttpSession session = request.getSession();
						KmUserMstr sessionUserBean = (KmUserMstr) session.getAttribute("USER_INFO");
						KmFeedbackMstr dto=new KmFeedbackMstr();
						KmElementMstrService elementService=new KmElementMstrServiceImpl();
						try{
							logger.info(sessionUserBean.getUserLoginId()+ " Entered in to the readFeedback method of KmFeedbackMstrAction");
							logger.info("ENTERED READ");		
							String elementId=formBean.getParentId();
							formBean.setElementId(elementId);
							String[] elementIds=elementService.getChildrenIds(elementId);
							formBean.setInitStatus("true");
							formBean.setParentId(formBean.getElementId());
							String[] readFeedbacks=formBean.getReadFeedbackList();
							/*for(int i=0;i<readFeedbacks.length;i++){
										logger.info(readFeedbacks[i]);
							} */
							//Added by Atul for Feedback Combo box
							ArrayList feedBackList=formBean.getFeedbackList();
							String[] feedStatus= formBean.getFeedbackArray();
							String[] feedResp=formBean.getFeedbackResp();
							KmFeedbackMstr dtoKm= new KmFeedbackMstr();
						
							String feedArr[]= new String[0];
							if(feedBackList.size()!=0) {
								feedArr = new String[feedBackList.size()];
							}
							 
							for(int i=0;i<feedBackList.size();i++) {
							   dtoKm= (KmFeedbackMstr)feedBackList.get(i);
							   feedArr[i]= dtoKm.getFeedbackId();
					
							}
							
							
							// Ended by Atul for feedback combo box
							
							feedbackService.readFeedbacksService(feedStatus,feedResp,feedArr);
							formBean.setFeedbackList(feedbackService.viewFeedbacks(elementIds,sessionUserBean.getElementId()));		
							formBean.setInitialSelectBox("-1");
							messages.add("msg1",new ActionMessage("feedback.read"));
							saveMessages(request,messages);	
					
							
	
						}catch(Exception e){
						//	e.printStackTrace();
							//logger.error("Exception occured while reading feedbacks :"+e);
							//errors.add("errors", new ActionError("feedback.failure"));
							//saveErrors(request,errors);
							
							messages.add("msg1",new ActionMessage("feedback.read"));
							saveMessages(request,messages);	
							
						}
				return initReadFeedback(mapping, formBean, request, response);
						
			}
			
		/* Method added for showing feedback response to the CSR user - Added by Atul 
		 * */
	public ActionForward initResp(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
				KmFeedbackMstrFormBean formBean =
				(KmFeedbackMstrFormBean) form;
			
				KmUserMstr userBean = (KmUserMstr) request.getSession().getAttribute("USER_INFO");
				ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
				try{
			
				logger.info(userBean.getUserLoginId()+ " Entered in to the initResp method of KmFeedbackMstrAction");
				KmFeedbackMstrService kmfeedbackMstr = new KmFeedbackMstrServiceImpl();
				ArrayList feedbackList= kmfeedbackMstr.feedbackResponse(Integer.parseInt(userBean.getUserId()));			//	KmElementMstrService kmElementService = new KmElementMstrServiceImpl();
			    formBean.setFeedbackList(feedbackList);
				}
				catch(Exception e){
					
					logger.error("Exception occured while initializing feedback page");
				
				}
				return mapping.findForward("initResp");
			}
	
	
	public ActionForward initRespAll(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
				KmFeedbackMstrFormBean formBean =
				(KmFeedbackMstrFormBean) form;
			
				KmUserMstr userBean = (KmUserMstr) request.getSession().getAttribute("USER_INFO");
				ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
				try{
					
				logger.info(userBean.getUserLoginId()+ " Entered in to the initResp method of KmFeedbackMstrAction");
				//get LOBId//
				KmElementMstrService elementMstrService = new KmElementMstrServiceImpl();
				String LOBId = elementMstrService.getParentId(userBean.getElementId());
				//////////
				//System.out.println("elementId=="+LOBId);
				//System.out.println("elementId=="+userBean.getElementId());
				//System.out.println("circleId=="+userBean.getCircleId());
				//System.out.println("categoryId=="+userBean.getCategoryId());
				KmFeedbackMstrService kmfeedbackMstr = new KmFeedbackMstrServiceImpl();
				ArrayList feedbackList= kmfeedbackMstr.feedbackResponseAll(Integer.parseInt(userBean.getElementId()));			//	KmElementMstrService kmElementService = new KmElementMstrServiceImpl();
			    formBean.setFeedbackList(feedbackList);
				}
				catch(Exception e){
					
					logger.error("Exception occured while initializing feedback page");
				
				}
				return mapping.findForward("initRespAll");
			}

	
	/* Added by harpreet kmphase II view feedback report exported into excel */

		public ActionForward ViewFeedbackExcelReport(
				ActionMapping mapping,
				ActionForm form,
				HttpServletRequest request,
				HttpServletResponse response)
				throws Exception {
				ActionErrors errors = new ActionErrors();
				ActionForward forward = new ActionForward(); // return value
				KmFeedbackMstrFormBean kmFeedbackMstrFormBean = (KmFeedbackMstrFormBean) form;
				try {
				logger.info( "Entered into the ViewFeedbackExcelReport  of KmFeedbackMstrAction");
		
				} catch (Exception e) {
			
				logger.error("Exception occured while listing report :"+e);
				}
				return mapping.findForward("viewFeedbackExcelReport" );
			}
// feedback detail report added by harpreet
		
		public ActionForward initFeedbackReport(
				ActionMapping mapping,
				ActionForm form,
				HttpServletRequest request,
				HttpServletResponse response)
				throws Exception {
					
					ActionErrors errors = new ActionErrors();
							//ActionMessages messages=new ActionMessages();
							//ActionForward forward = new ActionForward(); // return value
							KmFeedbackMstrFormBean formBean =(KmFeedbackMstrFormBean) form;
							//KmLoginFormBean kmLoginFormBean=new KmLoginFormBean();
							//KmFeedbackMstr categoryDTO=new KmFeedbackMstr();
							//ArrayList circleList = new ArrayList();
							
						//	KmFeedbackMstrService feedbackService=new KmFeedbackMstrServiceImpl();
						//	KmCircleMstrService circleService=new KmCircleMstrServiceImpl();
							HttpSession session = request.getSession();
							KmUserMstr userBean = (KmUserMstr) session.getAttribute("USER_INFO");
							//KmFeedbackMstr dto=new KmFeedbackMstr();
							ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
							try{
								logger.info(userBean.getUserLoginId()+ " Entered in to the initFeedbackReport method of KmFeedbackMstrAction");
								KmElementMstrService kmElementService = new KmElementMstrServiceImpl();
								List firstDropDown;
								if (userBean.getKmActorId().equals(bundle.getString("LOBAdmin"))||userBean.getKmActorId().equals(bundle.getString("Superadmin"))) {
									firstDropDown = kmElementService.getAllChildren(userBean.getElementId());
								}else{
									firstDropDown = kmElementService.getChildren(userBean.getElementId());
								}					
								if (firstDropDown!=null && firstDropDown.size()!=0){
									formBean.setInitialLevel(((KmElementMstr)firstDropDown.get(0)).getElementLevel());
								}
								else{
							
									int initialLevel=Integer.parseInt(kmElementService.getElementLevelId(userBean.getElementId()));
									initialLevel++;
									formBean.setInitialLevel(initialLevel+"");
								}
								
								formBean.setParentId(userBean.getElementId());
								if(request.getParameter("methodName").equals("initFeedbackReport")){
									String today;
									GregorianCalendar gc = new GregorianCalendar();
									
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
									Date yest = gc.getTime();
									today  = sdf.format(yest);
									today = today.substring(0,10);
									formBean.setStartDate(today);
									formBean.setEndDate(today);
								}
								request.setAttribute("elementLevelNames",kmElementService.getAllElementLevelNames());
								request.setAttribute("allParentIdString",kmElementService.getAllParentIdString("1",userBean.getElementId()));
								request.setAttribute("firstList",firstDropDown);
								if(request.getAttribute("FEEDBACK_LIST")==null)
									formBean.setInitStatus("true");	
								else
									formBean.setInitStatus("false");
								
								
		                        logger.info("Init status");
							}catch(Exception e){
								
								logger.error("Exception occured while listing feedbacks :"+e);
								errors.add("errors", new ActionError("feedback.failure"));
								saveErrors(request,errors);
								
							}
					return mapping.findForward("feedBackReport");
				}
		public ActionForward feedbackReport(
				ActionMapping mapping,
				ActionForm form,
				HttpServletRequest request,
				HttpServletResponse response)
				throws Exception {
					
					ActionErrors errors = new ActionErrors();
						//	ActionMessages messages=new ActionMessages();
							//ActionForward forward = new ActionForward(); // return value
							KmFeedbackMstrFormBean formBean =(KmFeedbackMstrFormBean) form;
							//KmLoginFormBean kmLoginFormBean=new KmLoginFormBean();
							//KmFeedbackMstr categoryDTO=new KmFeedbackMstr();
							//ArrayList circleList = new ArrayList();
							String path="";
							KmFeedbackMstrService feedbackService=new KmFeedbackMstrServiceImpl();
							//KmCircleMstrService circleService=new KmCircleMstrServiceImpl();
							HttpSession session = request.getSession();
							KmUserMstr userBean = (KmUserMstr) session.getAttribute("USER_INFO");
							//KmFeedbackMstr dto=new KmFeedbackMstr();
							//ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
							KmElementMstrService elementService=new KmElementMstrServiceImpl();
							try{
								
								
													     
								logger.info(userBean.getUserLoginId()+ " Entered in to the feedbackReport method of KmFeedbackMstrAction");
								String elementId=formBean.getParentId();
								formBean.setElementId(elementId);
								String[] elementIds=elementService.getChildrenIds(elementId);
								logger.info("Element Id of Feedback : "+formBean.getParentId());
								
								 path=elementService.getAllParentNameString(userBean.getElementId(),elementId);
								 formBean.setElementFolderPath(path);
								 
								ArrayList feedbackList=feedbackService.feedbackReport(elementIds,formBean.getParentId(),formBean.getStartDate(),formBean.getEndDate());
								formBean.setFeedbackList(feedbackList);
								request.setAttribute("FEEDBACK_LIST",feedbackList);
								logger.info("Feedbacklist :"+formBean.getFeedbackList());
								formBean.setInitialSelectBox("");
								String feedArray[]= new String[0];
								if(feedbackList.size()!=0) {
									feedArray=new String[feedbackList.size()];
								}
								for(int i=0;i<feedArray.length;i++) {
								    feedArray[i]= "N";
								 }
								formBean.setFeedbackArray(feedArray);
								
								
							}catch(Exception e){
								
								logger.error("Exception occured while listing feedbacks :"+e);
								errors.add("errors", new ActionError("feedback.failure"));
								saveErrors(request,errors);
								
							}
						return initFeedbackReport(mapping, formBean, request, response);	
							
				}
		
		public ActionForward feedbackExcelReport(
				ActionMapping mapping,
				ActionForm form,
				HttpServletRequest request,
				HttpServletResponse response)
				throws Exception {
			//	ActionErrors errors = new ActionErrors();
				//ActionForward forward = new ActionForward(); // return value
				//KmFeedbackMstrFormBean kmFeedbackMstrFormBean = (KmFeedbackMstrFormBean) form;
				try {
				logger.info( "Entered into the feedbackExcelReport  of KmFeedbackMstrAction");
		
				} catch (Exception e) {
				
				logger.error("Exception occured while listing report :"+e);
				}
				return mapping.findForward("feedbackExcelReport" );
			}
		
}
