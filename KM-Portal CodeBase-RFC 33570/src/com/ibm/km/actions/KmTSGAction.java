package com.ibm.km.actions;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.ibm.km.dao.ConfigCSDDao;
import com.ibm.km.dao.impl.ConfigCSDDaoImpl;
import com.ibm.km.dto.ConfigCSDDto;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.ConfigCSDForm;
import com.ibm.km.forms.KmUserMstrFormBean;
import com.ibm.km.services.ConfigCSDService;
import com.ibm.km.services.impl.ConfigCSDServiceImpl;

/**
 * @author Ajil Chandran
 * Created On  11 Nov 2010
 */
public class KmTSGAction extends DispatchAction {
	/**
	 * Logger for the class.
	**/
	private static final Logger logger;

	static {

		logger = Logger.getLogger(KmTSGAction.class.getName());
	}
	
	private static String INIT_CSD_CONFIGURE = "initCsdConfigure";
	/**
	Initailizes the Configure CSD User page. All existing csd users for that circle is fetcdhed from DB
	 **/
	public ActionForward init(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		ConfigCSDForm configCSDForm= (ConfigCSDForm)form;
		configCSDForm.reset(mapping, request);
		ConfigCSDDto configCSDDto = new ConfigCSDDto();
		configCSDDto.setMobileNumber("");
				
		ActionForward forward = new ActionForward(); // return value
		HttpSession session = request.getSession();
		//KmUserMstrFormBean kmUserMstrFormBean = (KmUserMstrFormBean) form;
		KmUserMstr sessionUserBean =(KmUserMstr) session.getAttribute("USER_INFO");
		////System.out.println(sessionUserBean.getElementId());
		int circleId=Integer.parseInt(sessionUserBean.getElementId().trim());
		ConfigCSDService configCSDService= new ConfigCSDServiceImpl();
		
		try {logger.info(
				sessionUserBean.getUserLoginId()
				+ " Entered into the init method of KmTSGAction");
			forward=mapping.findForward(INIT_CSD_CONFIGURE);
			List csdNumbersList	=configCSDService.fetchCsdUsers(circleId);
			session.setAttribute("CSD_USER_LIST",csdNumbersList );
			////System.out.println(csdList[0]);
			
			
			
		} 
		catch (KmException e) {
			
			logger.error(
			"Exception occured while fetching the CSD User List");
				
		}
		catch (Exception e) {
			logger.error(
			"Exception occured while initializing the init method in KmTSGAction");
		}
		return forward;
		
	
		
	}
	
	/**	
	*Creates new CSD users.  
	**/
	public ActionForward addCsdUser (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		ConfigCSDDto configCSDDto= new ConfigCSDDto();
		ConfigCSDForm configCSDForm= (ConfigCSDForm)form;
		ConfigCSDService dao= new ConfigCSDServiceImpl();
		HttpSession session = request.getSession();
		KmUserMstr sessionUserBean =(KmUserMstr) session.getAttribute("USER_INFO");
		configCSDForm.setCircleId(sessionUserBean.getElementId());

		
		String responseMessage = "Error Occured while adding CSD User";
		try {logger.info(
				sessionUserBean.getUserLoginId()
				+ " Entered into the addCsdUser method of KmTSGAction");
			BeanUtils.copyProperties(configCSDDto, configCSDForm);
		
			
			dao.insertCsd(configCSDDto);
			responseMessage = "User added successfully";
			configCSDForm.reset(mapping, request);
			
			
		
		} 
catch (KmException e) {
			
			logger.error(
			"Exception occured while adding  CSD User ");
		
		
		}
		catch (Exception e) {
			logger.error(
			"Exception occured while executing addCsdUser method in KmTSGAction");
		
		}finally{
			PrintWriter out = response.getWriter();
			response.setContentType("text/plain");
			response.setHeader("Cache-Control", "No-Cache");
			out.write(responseMessage);
			out.flush();
			
		}
		return null;
		

	}
	
	/**	
	*Remove CSD users by updating the status to 'D'.  
	**/
	public ActionForward removeCsdUser(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
						
		ConfigCSDService dao= new ConfigCSDServiceImpl();
		HttpSession session = request.getSession();
		KmUserMstr sessionUserBean =(KmUserMstr) session.getAttribute("USER_INFO");
		String circleId=(sessionUserBean.getElementId());
		String status="D";
		
		
		String responseMessage = "Error Occured while Removing CSD User";
		
	try {logger.info(
			sessionUserBean.getUserLoginId()
			+ " Entered into the removeCsdUser method of KmTSGAction");
		
	
	String[] csdList= request.getParameterValues("mobileNumbers");
	 dao.removeCsd(csdList, circleId,status);
	 responseMessage = "CSD User(s) Removed";
		
	} catch (KmException e) {
		
		logger.error(
		"Exception occured while Removing  CSD User ");
	
	
	}
	catch (Exception e) {
		logger.error(
		"Exception occured while executing removeCsdUser method in KmTSGAction");
	
	}	
	finally{
		PrintWriter out = response.getWriter();
		response.setContentType("text/plain");
		response.setHeader("Cache-Control", "No-Cache");
		out.write(responseMessage);
		out.flush();
	}
	return null;
		
}
/*	public ActionForward updateCsdUser(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//System.out.println("inside action");
		
		ActionForward forward=null;
		ConfigCSDForm configCSDForm = (ConfigCSDForm)form;
		ConfigCSDDto configCSDDto= new ConfigCSDDto();
		HttpSession session = request.getSession();
		KmUserMstr sessionUserBean =(KmUserMstr) session.getAttribute("USER_INFO");
		configCSDForm.setCircleId(sessionUserBean.getElementId());
		
		try {
			// get circle id from session 
		//	configCSDDto.setCircleID("101");
			////System.out.println(configCSDForm.getMobileNumbers().length);
			BeanUtils.copyProperties(configCSDDto, configCSDForm);
			forward=mapping.findForward(INIT_CSD_CONFIGURE);
			
			
		} 
		
		
		catch (Exception e) {
			// TODO: handle exception
		}
		
		
			
		return forward ;
	}
	*/

}
