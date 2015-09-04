package com.ibm.km.actions;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.json.JSONObject;

import com.ibm.km.common.PropertyReader;
import com.ibm.km.dto.KmAlertMstr;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.forms.KmAlertMstrFormBean;
import com.ibm.km.services.KmAlertMstrService;
import com.ibm.km.services.KmElementMstrService;
import com.ibm.km.services.impl.KmAlertMstrServiceImpl;
import com.ibm.km.services.impl.KmElementMstrServiceImpl;
import com.ibm.km.common.Constants;


/**
 * @version 	1.0
 * @author
 */
public class KmAlertMstrAction extends DispatchAction {

	/**
	 * Logger for the class.
	**/
	private static final Logger logger;

	static {

		logger = Logger.getLogger(KmAlertMstrAction.class);
	}
	/**
	 * Initializes Create Alert page
	 **/

	public ActionForward init(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

			KmUserMstr sessionUserBean = (KmUserMstr) request.getSession().getAttribute("USER_INFO");
			logger.info(sessionUserBean.getUserLoginId()+ " Entered in to the init method of KmAlertMstrAction");
			KmAlertMstrFormBean formBean=(KmAlertMstrFormBean) form;	
			ArrayList circleList=new ArrayList();
			KmElementMstrService elementService= new KmElementMstrServiceImpl();
			
			/* Added by Anil for giving create alert access to all admins */
			if(sessionUserBean.getKmActorId().equals(Constants.SUPER_ADMIN)){
				
				circleList=elementService.getAllChildrenNoPan("1","3");
				formBean.setCircleId("");
			}
			else if(sessionUserBean.getKmActorId().equals(Constants.LOB_ADMIN)){
				
				circleList=elementService.getChildren(sessionUserBean.getElementId());
				formBean.setCircleId("");
			}
			else{
				circleList =null;
			}
			
			if(request.getParameter("message")==null){
				formBean.setMessage("");
				
			}
			else{
				formBean.setMessage(request.getParameter("message").toString());
				formBean.setMessageId(request.getParameter("messageId").toString());
				
			}
			formBean.setKmActorId(sessionUserBean.getKmActorId());
			
			if(sessionUserBean.getKmActorId().equals(Constants.CIRCLE_ADMIN))
			{
				KmAlertMstrService service=new KmAlertMstrServiceImpl();
				formBean.setMessage(service.getAlertMessage(sessionUserBean.getElementId()));
				
			}
			request.setAttribute("CIRCLE_LIST",circleList);
			return mapping.findForward("addAlert");
		}
	
	public ActionForward loadPreviousMessage(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response){
			
			KmAlertMstrService service=new KmAlertMstrServiceImpl();
			String currentCircleId = request.getParameter("currentCircleId");
		    //System.out.println("\n\ncurrentCircleId "+currentCircleId);		    
		    String message = "";
			try {
				message = service.getAlertMessage(currentCircleId);
				JSONObject json=new JSONObject();
				json.put("message", message);
				response.setHeader("Cache-Control", "no-cache");
				response.setHeader("Content-Type", "application/x-json");
				response.getWriter().print(json);		
			} catch (IOException e) {
				logger.error("IOException in Loading Previous Message: "+e.getMessage());
				
			} catch (Exception e) {
				logger.error("Exception in Loading Previous Message: "+e.getMessage());
				
			}
			
			return null;
		}
	
	
	
	
	/*
	 * Creates a new Alert
	 */
	
	
	public ActionForward insert(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
				
				ActionErrors errors = new ActionErrors();
				ActionMessages messages= new ActionMessages();
				ActionForward forward = new ActionForward(); // return value
				KmAlertMstrFormBean formBean=(KmAlertMstrFormBean) form;		
				KmAlertMstr dto=new KmAlertMstr();
				KmAlertMstrService service=new KmAlertMstrServiceImpl();
				HttpSession session = request.getSession();
				KmUserMstr sessionUserBean = (KmUserMstr) session.getAttribute("USER_INFO");
				try{
					logger.info(sessionUserBean.getUserLoginId()+ " Entered in to the insert method of KmAlertMstrAction");
					
					
					//Populating the DTO
					dto.setMessage(formBean.getMessage());
					if(sessionUserBean.getKmActorId().equals(Constants.SUPER_ADMIN)||sessionUserBean.getKmActorId().equals(Constants.LOB_ADMIN)){
						dto.setCircleId(formBean.getCircleId());
					}else{
						dto.setCircleId(sessionUserBean.getElementId());
					}
					logger.info("creatd by -----"+sessionUserBean.getUserId());
					dto.setCreatedBy(sessionUserBean.getUserId());
					dto.setUpdatedBy(sessionUserBean.getUserId());
					dto.setStatus("A");
					dto.setMessageId(formBean.getMessageId());
					
					//calling service to create Alert
					int rowsUpdated=service.createAlert(dto);
					if (rowsUpdated > 0){
						logger.info("A New Alert is Created");
						messages.add("msg1",new ActionMessage("alert.created"));
						formBean.setMessage("");
						formBean.setStartDate("");
						formBean.setEndDate("");
					}
					saveMessages(request, messages);	
					return init(mapping,formBean,request,response);
	
				}catch(Exception e){
					errors.add("",new ActionError("alert.error"));
					logger.error("Error occured while creating the Alert");
					saveErrors(request, errors);
					logger.error("Exception occured while creating Alert :"+e);
					formBean.setCircleId("");
					formBean.setMessage("");
					return init(mapping,formBean,request,response);
				}
	}
	
	public ActionForward showAlertMessages(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
			
			KmUserMstr sessionUserBean = (KmUserMstr) request.getSession().getAttribute("USER_INFO");
						
			logger.info(sessionUserBean.getUserLoginId()+ " Entered in to the view method of KmAlertMstrAction");
			StringBuffer alertMessages = new StringBuffer();
			KmAlertMstr dto=new KmAlertMstr();
			List alertList = new ArrayList();
			dto.setActorId(Integer.parseInt(sessionUserBean.getKmActorId()));
			dto.setCircleId(sessionUserBean.getElementId());
			dto.setUserId(Integer.parseInt(sessionUserBean.getUserId()));
			KmAlertMstrService service=new KmAlertMstrServiceImpl();
			alertList=service.getAlertList(dto);
			logger.info("Retrieving alerts from the DB");
			if(alertList!=null ){
				////System.out.println("Alert size "+ alertList.size());
				if(alertList.size()>0){
					for (int i = 0; i < alertList.size(); i++) {
						dto = (KmAlertMstr) alertList.get(i);

						if (dto != null) {
							alertMessages.append(dto.getCreatedDt().substring(11,16)+" Hrs   :    "+dto.getMessage());
					
							alertMessages.append("\n");
							alertMessages.append("\n");
						}
					}
				}
				else
				{
					alertMessages.append("none");
					
				}
		
			}else{
				alertMessages.append("none");
			}
		
				//xmlString.append("<?xml version='1.0' encoding='ISO-8859-1'?>").append("<ALERTS>").append("<ALERT1>").append(sessionExpiryMessage).append("</ALERT1>").append("<ALERT2>").append(alertMessages).append("</ALERT2>").append("</ALERTS>");
				logger.info(alertMessages);
				
				response.setContentType("text");
				response.setHeader("Cache-Control", "No-Cache");
				PrintWriter out = response.getWriter();
				out.write(alertMessages.toString());
				out.flush();
			
			return null;
		}
	public ActionForward sessionExpiryAlert(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
			String sessionExpiryMessage="none";
				try{
					
				if(PropertyReader.getAppValue("SESSION.EXPIRY.ALERT.FLAG").equals("N")){
					return null;
				}
				HttpSession session= request.getSession();
				KmUserMstr sessionUserBean = (KmUserMstr) session.getAttribute("USER_INFO");
							
			//	logger.info(sessionUserBean.getUserLoginId()+ " Entered in to the sessionExpiryAlert method of KmAlertMstrAction");
				
				
			//	//System.out.println("SessionTime "+sessionUserBean.getSessionExpiry());
			/*	if(new GregorianCalendar().after(sessionUserBean.getSessionExpiry()) ){
					
					
					int timeToExpire=(session.getMaxInactiveInterval())/60-Integer.parseInt(PropertyReader.getAppValue("session.expry.alert.time"));
					sessionExpiryMessage="Your session will be expired within "+timeToExpire+" minutes. You want to open a new session";
				}*/
			//	//System.out.println("Session Expiry Message : "+sessionExpiryMessage);
				
				}
				catch (Exception e) {
					sessionExpiryMessage="Your session Expired";
				}finally{
				response.setContentType("text");
				response.setHeader("Cache-Control", "No-Cache");
				PrintWriter out = response.getWriter();
				out.write(sessionExpiryMessage);
				out.flush();
				}return null;
			}
	/*public ActionForward sessionExpiryAlert(
	ActionMapping mapping,
	ActionForm form,
	HttpServletRequest request,
	HttpServletResponse response)
	throws Exception {
		
		HttpSession session = request.getSession();
		
		long maxT = (session.getMaxInactiveInterval()*1000);
		long currT = System.currentTimeMillis();
		long startT = (session.getCreationTime());
		long backupT = 4*60000;//time to pop up prompt
		long timeLeft = maxT -(currT-startT);
		long token = -1; 
		String sessionExpiryMessage="none";
		//System.out.println("Timeleft :"+timeLeft + "   Inactive Interval "+maxT+"    craetion time : "+startT+"    backupT : "+backupT );
		if(timeLeft<=0){
			sessionExpiryMessage = "Your session is expired";
			token = 0;
		}//session expired
		else if(timeLeft>0 && timeLeft<backupT){
			token = timeLeft/60000;
			sessionExpiryMessage=token+" minutes are left to expire the session";
		}//backup
		else if(timeLeft>backupT){
			token = -1;
		}//safe
		
	
		
		
		logger.info("Entered in to the sessionExpiryAlert method of KmAlertMstrAction");
		
		
		
		//System.out.println("Session Expiry Message : "+sessionExpiryMessage);
		response.setContentType("text");
		response.setHeader("Cache-Control", "No-Cache");
		PrintWriter out = response.getWriter();
		out.write(sessionExpiryMessage);
		out.flush();
		
		return null;
	}*/
	
	
	public ActionForward sessionUpdate(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
				
				KmUserMstr userBean= (KmUserMstr)request.getSession().getAttribute("USER_INFO");
				logger.info(userBean.getUserLoginId()+"  Entered in to the sessionRefresh method of KmAlertMstrAction");
				Calendar calendar =  new GregorianCalendar();
				calendar.add(Calendar.MINUTE,Integer.parseInt(PropertyReader.getAppValue("session.expry.alert.time")));
			//	userBean.setSessionExpiry(calendar);
				response.setContentType("text");
				response.setHeader("Cache-Control", "No-Cache");
				PrintWriter out = response.getWriter();
		//		log.info("session updated");
				out.write("Session Updated");
				out.flush();
				
				return null;
			}	
	public ActionForward viewAlert(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
				
				ActionErrors errors = new ActionErrors();
				ActionMessages messages= new ActionMessages();
				ActionForward forward = new ActionForward(); // return value
				KmAlertMstrFormBean formBean=(KmAlertMstrFormBean) form;		
				KmAlertMstr dto=new KmAlertMstr();
				KmAlertMstrService service=new KmAlertMstrServiceImpl();
				HttpSession session = request.getSession();
				KmUserMstr sessionUserBean = (KmUserMstr) session.getAttribute("USER_INFO");
				try{
					logger.info(sessionUserBean.getUserLoginId()+ " Entered in to the viewAlert of KmAlertMstrAction");
					
					
					//Populating the DTO
					dto.setCircleId(sessionUserBean.getCircleId());
					dto.setActorId(Integer.parseInt(sessionUserBean.getKmActorId()));
					dto.setCreatedBy(sessionUserBean.getUserId());
					
					
					//calling service to create Alert
					//int rowsUpdated=service.createAlert(dto);
					ArrayList alertList=service.getAlertList(dto);
					formBean.setAlertList(alertList);
					if (alertList.size() <= 0 || alertList.equals(null)){
						logger.info("Listing Alerts");
						messages.add("msg1",new ActionMessage("alert.errorList"));
						formBean.setMessage("");
						formBean.setStartDate("");
						formBean.setEndDate("");
					}
					saveMessages(request, messages);	
					return mapping.findForward("viewAlert");
	
				}catch(Exception e){
					errors.add("",new ActionError("alert.errorView"));
					logger.error("Error Occured While Fetching Alert List");
					saveErrors(request, errors);
					logger.error("Exception Occured While Fetching Alert List :"+e);
					return mapping.findForward("viewAlert");
				}
	}
	
	public ActionForward delete(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
				
				ActionErrors errors = new ActionErrors();
				ActionMessages messages= new ActionMessages();
				ActionForward forward = new ActionForward(); // return value
				KmAlertMstrFormBean formBean=(KmAlertMstrFormBean) form;		
				KmAlertMstr dto=new KmAlertMstr();
				KmAlertMstrService service=new KmAlertMstrServiceImpl();
				HttpSession session = request.getSession();
				KmUserMstr sessionUserBean = (KmUserMstr) session.getAttribute("USER_INFO");
				try{
					logger.info(sessionUserBean.getUserLoginId()+ " Entered in to the Delete of KmAlertMstrAction");
					
					
					//Populating the DTO
					if(request.getParameter("messageId").equals(null)){
						addmessage(messages,formBean,request);
						
					}
					else{
						logger.info("request.getParameter"+request.getParameter("messageId"));
						dto.setMessageId(request.getParameter("messageId"));
						messages.add("msg1",new ActionMessage("alert.deleted"));
						saveMessages(request, messages);
						String message=service.deleteAlert(dto);
						if (message.equals("failure")){
							addmessage(messages,formBean,request);
						}
						else{
							messages.add("msg1",new ActionMessage("alert.deleted"));
							formBean.setStartDate("");
							formBean.setEndDate("");
							saveMessages(request, messages);
						}
						
					}
				
					return mapping.findForward("deleteAlert");
	
				}catch(Exception e){
					errors.add("",new ActionError("Alert.notdeleted"));
					logger.error("Error occured while deleting the Alert");
					saveErrors(request, errors);
					logger.error("Exception occured while deleting Alert :"+e);
					return mapping.findForward("deleteAlert");
				}
	}
	
				private void addmessage(ActionMessages messages,KmAlertMstrFormBean formBean,HttpServletRequest request){
				messages.add("msg1",new ActionMessage("alert.notdeleted"));
				formBean.setStartDate("");
				formBean.setEndDate("");
				saveMessages(request, messages);
				}


	/* vie edit alert page km phase 2 */
	
		   public ActionForward initViewEdit(
				   ActionMapping mapping,
				   ActionForm form,
				   HttpServletRequest request,
				   HttpServletResponse response)
				   throws Exception {

					   KmUserMstr sessionUserBean = (KmUserMstr) request.getSession().getAttribute("USER_INFO");
					   KmAlertMstrFormBean formBean=(KmAlertMstrFormBean) form;
					   KmElementMstrService service=new KmElementMstrServiceImpl(); 
					   ActionForward forward = new ActionForward();	
					   ArrayList circleList=null;
					   String elementId=sessionUserBean.getElementId();
				
					   formBean.setSelectedCircleId(sessionUserBean.getCircleId());
					  
					   try{
					   if(sessionUserBean.getKmActorId().equals(Constants.SUPER_ADMIN))
					   {
					   formBean.setKmActorId("1");
					   logger.info(elementId + " Entered in to the initViewEdit method of KmAlertMstrAction");
					   circleList=service.getAllChildrenNoPan("1","3");
					   formBean.setCircleList(circleList);
				
				
					   }else
					   if(sessionUserBean.getKmActorId().equals(Constants.LOB_ADMIN))
					 {
					  formBean.setKmActorId(Constants.LOB_ADMIN);
					  logger.info(elementId + " Entered in to the initViewEdit method of KmAlertMstrAction");
					  circleList=service.getChildren(elementId);
					 formBean.setCircleList(circleList);
					 }else
					   if(sessionUserBean.getKmActorId().equals(Constants.CIRCLE_ADMIN))
					   {
					  formBean.setKmActorId(Constants.CIRCLE_ADMIN);
					  logger.info(elementId + " Entered in to the initViewEdit method of KmAlertMstrAction");
					  viewAlerts(mapping,formBean,request,response);
					   }
								
					   }catch(Exception e){
						   e.printStackTrace();
						   logger.error("Exception occured while listing circles :"+e);	            	
					   }
				
						return mapping.findForward("viewEditAlert");
				
				   }
			
			
		 /* view edit alert  action in  km phase 2 */
				
		   public ActionForward viewAlerts(
				   ActionMapping mapping,
				   ActionForm form,
				   HttpServletRequest request,
				   HttpServletResponse response)
				   throws Exception {

					   KmUserMstr sessionUserBean = (KmUserMstr) request.getSession().getAttribute("USER_INFO");
					   KmAlertMstrFormBean formBean=(KmAlertMstrFormBean) form;
					   KmElementMstrService service=new KmElementMstrServiceImpl(); 
					   KmAlertMstrService alertService=new KmAlertMstrServiceImpl();
					   ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");	
					   ArrayList latestAlertList=null;
					   ArrayList circleList=null;
					   String alertCount="";
					   String elementId=sessionUserBean.getElementId();
					   String circleId="";
					   String path="";
					   String time=bundle.getString("alert.time");
					  
					   try{
					
				
					   formBean.setCircleId(formBean.getSelectedCircleId());			
					   if(sessionUserBean.getKmActorId().equals(Constants.SUPER_ADMIN))
					   {
						circleId=formBean.getCircleId();
						path=service.getAllParentNameString(sessionUserBean.getElementId(),formBean.getCircleId());
					 
					   }else
					   if(sessionUserBean.getKmActorId().equals(Constants.LOB_ADMIN))
						{
						 circleId=formBean.getCircleId();
						 path=service.getAllParentNameString(sessionUserBean.getElementId(),formBean.getCircleId());
						}else
					   if(sessionUserBean.getKmActorId().equals(Constants.CIRCLE_ADMIN))
					   {	
					   circleId=sessionUserBean.getElementId();
					   path=service.getElementName(elementId);
					   }
				
					   //path=path1+"/"+path2;
					  // formBean.setDocumentPath(path);
					  // circleList=service.getAllChildren(elementId,"3");
					   //formBean.setCircleList(circleList);
				
					   logger.info(circleId + " Entered in to the viewalert method of KmAlertMstrAction");
					   //alertList=service.getAlertHistoryReport(circleId);
					   latestAlertList=alertService.getAlerts(circleId,time);
					   if(latestAlertList!=null){
						alertCount=latestAlertList.size()+"";
						formBean.setAlertCount(alertCount);
					   }
					   request.setAttribute("ALERT_LIST",latestAlertList);
					   //formBean.setSelectedCircleId("");
					   }catch(Exception e){
						   e.printStackTrace();
						   logger.error("Exception occured while listing report :"+e);	            	
					   }
					   return mapping.findForward("viewEditAlert");
				
				   }

//	  edit alerts

	  public ActionForward editAlerts(
			  ActionMapping mapping,
			  ActionForm form,
			  HttpServletRequest request,
			  HttpServletResponse response)
			  throws Exception {

				KmUserMstr sessionUserBean = (KmUserMstr) request.getSession().getAttribute("USER_INFO");
				logger.info(sessionUserBean.getUserLoginId()+ " Entered in to the edit method of KmAlertMstrAction");
				KmAlertMstrFormBean formBean=(KmAlertMstrFormBean) form;	
				KmAlertMstrService service=new KmAlertMstrServiceImpl();
				KmAlertMstr dto=new KmAlertMstr();
				int rowsUpdated=-1;
				try{
				
					formBean.setMessage(request.getParameter("message").toString());
					formBean.setMessageId(request.getParameter("messageId").toString());
					 
					dto.setMessage(formBean.getMessage());
					dto.setMessageId(formBean.getMessageId());
					dto.setCreatedDt(formBean.getCreatedDt());
					dto.setUpdatedDt(formBean.getUpdatedDt());
	
					//calling service to edit Alert
					rowsUpdated=service.editAlert(dto);
					
					if (rowsUpdated != -1){
						logger.info("Alert is updated");
					}
					else{
						logger.error("Exception occured while editing alert message");
					}
					
				}
				catch(Exception e){
					logger.error("Exception occured while editing alert message");
					
				}
				
				response.setContentType("text/html");
				response.setHeader("Cache-Control", "No-Cache");
				PrintWriter out = response.getWriter();
		
				out.write(rowsUpdated+"");
				out.flush();
			return null;   
			 
		 	 
			  }
			

	public ActionForward initReport(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {

				KmUserMstr sessionUserBean = (KmUserMstr) request.getSession().getAttribute("USER_INFO");
				KmAlertMstrFormBean formBean=(KmAlertMstrFormBean) form;
				KmElementMstrService service=new KmElementMstrServiceImpl(); 
				ActionForward forward = new ActionForward();	
				ArrayList circleList=null;
				String elementId=sessionUserBean.getElementId();
				
				formBean.setSelectedCircleId(sessionUserBean.getCircleId());
		        
				try{
				if(sessionUserBean.getKmActorId().equals(Constants.LOB_ADMIN))
				{
				formBean.setKmActorId(Constants.LOB_ADMIN);
				logger.info(elementId + " Entered in to the initReport method of KmAlertMstrAction");
				circleList=service.getChildren(elementId);
				formBean.setCircleList(circleList);
				
				
				}else
				if(sessionUserBean.getKmActorId().equals(Constants.CIRCLE_ADMIN)|| sessionUserBean.getKmActorId().equals(Constants.REPORT_ADMIN))
				{
					formBean.setKmActorId(Constants.CIRCLE_ADMIN);
					logger.info(elementId + " Entered in to the initReport method of KmAlertMstrAction");
					alertReport(mapping,formBean,request,response);
				}
								
				}catch(Exception e){
					e.printStackTrace();
					logger.error("Exception occured while listing report :"+e);	            	
				}
				
				 return mapping.findForward("alertReport");
				
			}
			
			
	/* alert report action in  km phase 2 */
				
	public ActionForward alertReport(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {

				KmUserMstr sessionUserBean = (KmUserMstr) request.getSession().getAttribute("USER_INFO");
				KmAlertMstrFormBean formBean=(KmAlertMstrFormBean) form;
				KmElementMstrService service=new KmElementMstrServiceImpl(); 	
				KmAlertMstrService alertservice=new KmAlertMstrServiceImpl();
				ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");	
				ArrayList alertList=null;
				ArrayList circleList=null;
				String elementId=sessionUserBean.getElementId();
				String timeInterval=bundle.getString("alert.report.timeinterval");
				String circleId="";
				String path="";
				try{
					
				formBean.setCircleId(formBean.getSelectedCircleId());			
				if(sessionUserBean.getKmActorId().equals(Constants.LOB_ADMIN))
				{
				 circleId=formBean.getCircleId();
				 path=service.getAllParentNameString(sessionUserBean.getElementId(),formBean.getCircleId());
					 
				}else
				if(sessionUserBean.getKmActorId().equals(Constants.CIRCLE_ADMIN) || sessionUserBean.getKmActorId().equals(Constants.REPORT_ADMIN))
				{	
				circleId=sessionUserBean.getElementId();
				path=service.getElementName(elementId);
				}
				
				//path=path1+"/"+path2;
				formBean.setDocumentPath(path);
				circleList=service.getChildren(elementId);
				formBean.setCircleList(circleList);
				
				logger.info(circleId + " Entered in to the alertReport method of KmAlertMstrAction");
				alertList=alertservice.getAlertHistoryReport(circleId,timeInterval);
				request.setAttribute("ALERT_HISTORY",alertList);
				formBean.setSelectedCircleId("");
				}catch(Exception e){
					e.printStackTrace();
					logger.error("Exception occured while listing report :"+e);	            	
				}
				return mapping.findForward("alertReport");
				
			}
	
	/* alert report excel import action km phase 2 */			
				
	public ActionForward importExcelReport(
				ActionMapping mapping,
				ActionForm form,
				HttpServletRequest request,
				HttpServletResponse response)
				throws Exception {

					KmUserMstr sessionUserBean = (KmUserMstr) request.getSession().getAttribute("USER_INFO");
					KmAlertMstrFormBean formBean=(KmAlertMstrFormBean) form;
					KmElementMstrService service=new KmElementMstrServiceImpl(); 
					KmAlertMstrService aservice=new KmAlertMstrServiceImpl();	
					ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");	
					String timeInterval=bundle.getString("alert.report.timeinterval");
					ArrayList alertList=null;
					ArrayList circleList=null;
					String elementId=sessionUserBean.getElementId();
					String circleId="";
					String path="";
					try{
					if(sessionUserBean.getKmActorId().equals(Constants.LOB_ADMIN))
					{
						 circleId=formBean.getCircleId();
						 path=service.getAllParentNameString(sessionUserBean.getElementId(),formBean.getCircleId());
					}else
						if(sessionUserBean.getKmActorId().equals(Constants.CIRCLE_ADMIN)|| sessionUserBean.getKmActorId().equals(Constants.REPORT_ADMIN))
							{
								circleId=sessionUserBean.getElementId();
								path=service.getElementName(elementId);
							}
					formBean.setDocumentPath(path);
					circleList=service.getAllChildrenNoPan(elementId,"3");
					formBean.setCircleList(circleList);
					logger.info(circleId + " Entered in to the importExcelReport method of KmAlertMstrAction");
					alertList=aservice.getAlertHistoryReport(circleId,timeInterval);
					request.setAttribute("ALERT_HISTORY",alertList);
				
					}catch(Exception e){
						e.printStackTrace();
						logger.error("Exception occured while listing report :"+e);	            	
					}
					return mapping.findForward("excelAlertReport");
				
				}		



			



}
