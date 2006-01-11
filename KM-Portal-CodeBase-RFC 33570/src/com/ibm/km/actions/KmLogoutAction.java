package com.ibm.km.actions;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ibm.km.common.UserDetails;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.services.KmUserMstrService;
import com.ibm.km.services.impl.KmUserMstrServiceImpl;

/**
 * @version 	1.0
 * @author
 */
public class KmLogoutAction extends Action {
	/*
			 * Logger for the class.
			 */
	private static final Logger logger;
	static {
		logger = Logger.getLogger(KmLogoutAction.class);
	}
	/* Local Variables */
	private static int logoutCounter = 0;
	private static String LOGOUT_SUCCESS = "logoutSuccess";
	private static String CSR_LOGOUT_SUCCESS = "csrLogoutSuccess";
	private static String LOGOUT_FAILURE = "logoutFailure";

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		ActionErrors errors = new ActionErrors();
		ActionForward forward = new ActionForward(); 
		HttpSession session = request.getSession();
		logger.info("Logout Request");
		try {
			
			logger.info("mapping.getPath()-----"+mapping.getPath());
			if("/timeout".equalsIgnoreCase(mapping.getPath()))
			{
				forward = mapping.findForward("timeout");
			}
			
			if("/Logout".equalsIgnoreCase(mapping.getPath())) {		
				logger.info("User Logging out " );

				KmUserMstr userBean= UserDetails.getUserDetails(request);
//				if(userBean.getKmActorId().equals(Constants.CIRCLE_CSR)||userBean.getKmActorId().equals(Constants.CATEGORY_CSR)){
//					forward = mapping.findForward(CSR_LOGOUT_SUCCESS);	
//				}else{
//					forward = mapping.findForward(LOGOUT_SUCCESS);
//				}
				logger.info("User Logging out: Got details " );
			//	userBean.setUserLoginStatus("N");
			//	KmUserMstrService userService= new KmUserMstrServiceImpl();
			//	userService.updateUserStatus(userBean);
				logger.info("User Logging out user details updated" );
				forward = mapping.findForward(LOGOUT_SUCCESS);
			}	
			
			if("/CSRLogout".equalsIgnoreCase(mapping.getPath())) {
				KmUserMstr userBean= UserDetails.getUserDetails(request);
				logger.info("2User Logging out: Got details " );
			//	userBean.setUserLoginStatus("N");
				KmUserMstrService userService= new KmUserMstrServiceImpl();
			//	userService.updateUserStatus(userBean);
				logger.info("2User Logging out user details updated" );
				forward = mapping.findForward("csrLogout");
			}
									
			session.invalidate();
			logger.info("User Logged out successfully. " );
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception From KmLogoutAction" + e);
		}
		return (forward);
	}
}