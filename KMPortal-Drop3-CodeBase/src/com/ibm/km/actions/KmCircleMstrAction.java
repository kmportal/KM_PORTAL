package com.ibm.km.actions;

import java.util.ArrayList;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
//dsfds


import com.ibm.km.dto.KmCircleMstr;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.forms.KmCircleMstrFormBean;
import com.ibm.km.forms.KmLoginFormBean;
import com.ibm.km.services.KmCircleMstrService;
import com.ibm.km.services.KmElementMstrService;
import com.ibm.km.services.impl.KmCircleMstrServiceImpl;
import com.ibm.km.services.impl.KmElementMstrServiceImpl;

import org.apache.log4j.Logger;
import javax.servlet.http.*;
import org.apache.commons.beanutils.BeanUtils;
/**
 * @version 	1.0
 * @author		Anil
 */
public class KmCircleMstrAction extends DispatchAction {

	/**
	 * Logger for the class.
	**/
	private static final Logger logger;

	static {

		logger = Logger.getLogger(KmCircleMstrAction.class);
	}

	

	
	/**
	* Initializes the create circle page and lists all circles
	**/
	public ActionForward init(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
				ActionErrors errors = new ActionErrors();
				ActionForward forward = new ActionForward();
				HttpSession session = request.getSession();
				KmUserMstr sessionUserBean = (KmUserMstr) session.getAttribute("USER_INFO");
				KmCircleMstrFormBean circleMstrBean = (KmCircleMstrFormBean) form;
				KmCircleMstrService circleService = new KmCircleMstrServiceImpl();
				logger.info(sessionUserBean.getUserLoginId()+ " Entered into the init method of KmCircleMstrAction");
				KmElementMstrService ser=new KmElementMstrServiceImpl();
				String cirId=ser.getCircleId("100");
				logger.info("CircleId : "+ser.getCircleId("100"));
			//	circleMstrBean.setEdit("OFF");
				circleMstrBean.setCircleId("");
				circleMstrBean.setCircleName("");
				circleMstrBean.setCircleReportList((ArrayList) circleService.getCircleDetails());
				return mapping.findForward("viewCircles");
	}
	/**
	*Creates a new circle and list all circles including the newly created circle
	**/
	public ActionForward insert(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
			
			
			ActionErrors errors = new ActionErrors();
			ActionMessages messages=new ActionMessages();
			ActionForward forward = new ActionForward();
			HttpSession session = request.getSession();
			KmUserMstr sessionUserBean = (KmUserMstr) session.getAttribute("USER_INFO");
			KmCircleMstrFormBean circleMstrBean = (KmCircleMstrFormBean) form;
			KmCircleMstrService circleService = new KmCircleMstrServiceImpl();
			boolean isCircleExists = false;
			KmCircleMstr circleMstrDto = new KmCircleMstr();
		try{		
			
			logger.info(sessionUserBean.getUserLoginId()+ " Entered into the insert method of KmCircleMstrAction");
			//Populating DTO
				
			circleMstrBean.setCreatedBy(sessionUserBean.getUserId());
			circleMstrDto.setCreatedBy(circleMstrBean.getCreatedBy());
			//circleMstrDto.setActorId(sessionUserBean.getKmActorId());
			circleMstrDto.setCircleName(circleMstrBean.getCircleName());
		
			/*Check circle service for Existing circleName */
			logger.info(circleMstrBean.getCircleName());
			boolean isCircleNameExists=circleService.checkCircleOnCircleName(circleMstrBean.getCircleName());
			 if(isCircleNameExists){
				circleMstrBean.setCircleReportList(
					(ArrayList) circleService.getCircleDetails());
				messages.add("msg1",new ActionMessage("createCircle.duplicate"));
				circleMstrBean.setHidCircleId(
					"Circle Name " + circleMstrBean.getCircleName() + " Already Exists");
				logger.info("Circle Name already exists");	
				circleMstrBean.setCircleId("");
				circleMstrBean.setCircleName("");
			}
			else {
					
				/*Create circle service method called*/
					
				circleService.createCircleService(circleMstrDto);
				messages.add("msg2",new ActionMessage("createCircle.success"));
				circleMstrBean.setHidCircleId("A New Circle Is Created.");
				logger.info("A New Circle is Created");
				
				circleMstrBean.setCircleReportList(
				(ArrayList) circleService.getCircleDetails());
				circleMstrBean.setEdit("OFF");
					
				circleMstrBean.setCircleId("");
				circleMstrBean.setCircleName("");
				
			}
			saveMessages(request,messages);
		}catch (Exception e) {
			
			logger.error("Exception from CircleMasterAction.." + e);
			circleMstrBean.setCircleReportList(
							(ArrayList) circleService.getCircleDetails());
	
			
	}
	return mapping.findForward("viewCircles");
		
	}
}	
