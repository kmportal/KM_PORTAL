package com.ibm.km.common;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.tiles.TilesRequestProcessor;

import com.ibm.km.actions.KmCSRAction;
import com.ibm.km.dto.KmAlertMstr;
import com.ibm.km.dto.KmLinkMstrDto;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.forms.KmCSRHomeBean;
import com.ibm.km.services.KmAlertMstrService;
import com.ibm.km.services.KmElementMstrService;
import com.ibm.km.services.impl.KmAlertMstrServiceImpl;
import com.ibm.km.services.impl.KmElementMstrServiceImpl;

public class KMRequestProcessor extends TilesRequestProcessor { 

	private static final Logger logger;

	static {

		logger = Logger.getLogger(KMRequestProcessor.class);
	}
	
	protected boolean processPreprocess(HttpServletRequest request, HttpServletResponse response) 
	{
		String requestedURI = request.getRequestURI();
		String requestMapping=requestedURI.substring(requestedURI.lastIndexOf("/") + 1,requestedURI.lastIndexOf(".do"));
		boolean flag = false;
		//boolean flag = true;
		try {
			
				
			if("login".equalsIgnoreCase(requestMapping)){
				String userId=(String) request.getParameter("userid");
				String password=(String) request.getParameter("password");
				//if(userId!=null&&request.getMethod().equals("POST")){
					request.setAttribute("userid",userId);
					request.setAttribute("password",password);
				//}
			} 
			//added By Beeru Singh
			if("UDlogin".equalsIgnoreCase(requestMapping)){
				logger.info("hello ud intregation======request====================");
				
				String userId=(String) request.getParameter("userid");
				String password=(String) request.getParameter("password");
				//if(userId!=null&&request.getMethod().equals("POST")){
					request.setAttribute("userid",userId);
					request.setAttribute("password",password);
				//}
			} 
			if (!("login".equalsIgnoreCase(requestMapping) ||("UDlogin".equalsIgnoreCase(requestMapping)))
				&& !("logout".equalsIgnoreCase(requestMapping))
				&& !("initCSRLogin".equalsIgnoreCase(requestMapping))
			 
				&& !("csrPageLogin".equalsIgnoreCase(requestMapping))
			&& !("timeout".equalsIgnoreCase(requestMapping))) 
			{
				if ("initforgotPassword".equalsIgnoreCase(requestMapping)
					|| "forgotPassword".equalsIgnoreCase(requestMapping)||"initCSRLogin".equalsIgnoreCase(requestMapping)) {
					flag = true;
					return flag;
				} 
				if("logout".equalsIgnoreCase(requestMapping))
					return true;
				HttpSession session = request.getSession();
				KmUserMstr sessionUserBean = (KmUserMstr) session.getAttribute("USER_INFO");
				
				if (sessionUserBean== null) {
					logger.info("Invalid Session. Please Login again.");
					session.invalidate();
					doForward("/timeout.do", request, response);
					flag = false;
				}
				else
				{
				//  Prevent unauthorized module access - redirect to login	

					String methodName =(String) request.getParameter("methodName");
					
						
					if (null == methodName)
					{
						if(null != request.getContentType()) 
						{
							String tempContentType = new String(request.getContentType());
							if(tempContentType.contains("multipart/form-data"))
							{
								KmUserMstr userBean = (KmUserMstr) (request
										.getSession())
										.getAttribute("USER_INFO");
								if (userBean != null) {
									String actor = userBean.getKmActorId();
									if (!(actor.equals("1") || actor.equals("5") || actor.equals("2")|| actor.equals("3"))) {
										logger.info("\nUnauthorized URL Access, please Login again. MethodName : "+ methodName);
										session.invalidate();
										doForward("/login.do", request,	response);
										return false;
									}
								}								
							}
						}
					}
					if(null != methodName)
					{
					    boolean isAuthorised = false;							
					    KmLinkMstrDto kmLinkMstrDto = null;
					    String temp = requestMapping + ".do?methodName=" + methodName;
					    
					    ArrayList<KmLinkMstrDto> userRoleList =  (ArrayList<KmLinkMstrDto>) session.getAttribute("USER_ROLE_LIST");
						for (int i = 0; i < userRoleList.size(); i++) {
							kmLinkMstrDto = userRoleList.get(i);
						//	logger.info("RoleList::: [ " + kmLinkMstrDto.getLinkPath()  + "     >>>> "+ session.getId() );
							if(kmLinkMstrDto.getLinkPath().startsWith(temp)) {
								isAuthorised = true;
								break;
							}
						}
						
						KmUserMstr tempUser = (KmUserMstr) session.getAttribute("USER_INFO");

						if(!isAuthorised)
						{
							logger.info("\nUnauthorized URL [ "+temp+" ] Access, please Login again.");
							session.invalidate();
							doForward("/login.do", request, response);
							return false;
						}
					}	
					// unauthorized access control block ends here 
								
					KmCSRHomeBean csrHomeBean=(KmCSRHomeBean)session.getAttribute("CSR_HOME_BEAN");
					KmElementMstrService elementService = new KmElementMstrServiceImpl();
					if((sessionUserBean.getKmActorId().equals(Constants.CIRCLE_CSR)||sessionUserBean.getKmActorId().equals(Constants.CATEGORY_CSR))){
						
						if(csrHomeBean==null){
							String circleElementId=sessionUserBean.getElementId();
							String lobId=elementService.getParentId(circleElementId);
							if(sessionUserBean.getElementId()!=null&&elementService.getElementLevelId(sessionUserBean.getElementId()).equals("3")){
								circleElementId=sessionUserBean.getElementId();
							}						
							KmCSRAction csrAction = new KmCSRAction();
							csrHomeBean=csrAction.initializeCSRHomeBean(lobId,circleElementId,sessionUserBean.getFavCategoryId(),sessionUserBean.isRestricted());
							session.setAttribute("CURRENT_LOB_ID",lobId);
							session.setAttribute("CSR_HOME_BEAN",csrHomeBean);
						}
						try{
							
							long timegap=(System.currentTimeMillis()-sessionUserBean.getAlertUpdateTime())/1000;
							int alertCheckGap=60*Integer.parseInt(PropertyReader.getAppValue("ALERT_CHECK_INTERVAL"));
							csrHomeBean.setAlertMessage("");
							if(timegap>=alertCheckGap && !requestMapping.equals("kmAlertMstr")){
								KmAlertMstrService alertService= new KmAlertMstrServiceImpl();
								KmAlertMstr alertDto=new KmAlertMstr();
								alertDto.setCircleId(sessionUserBean.getElementId());
								alertDto.setUserId(Integer.parseInt(sessionUserBean.getUserId()));
								//csrHomeBean.setAlertMessage(alertService.getAlertMessage(alertDto));
								sessionUserBean.setAlertUpdateTime(System.currentTimeMillis());
							}
						}catch (Exception e) {
							logger.error("Exception occured while fetching alert during page refresh"+e);
						}
					}
					if (null == session.getAttribute("USER_CHANGEPWD")) {
						flag = true;
					} else {
						if (sessionUserBean.getUserPassword().equals(session.getAttribute("USER_CHANGEPWD"))) {
							flag = true;
						} else {
							doForward("/Logout.do", request, response);
							flag = false;
						}
					}
				}
			} else {
				flag = true;							
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}
}