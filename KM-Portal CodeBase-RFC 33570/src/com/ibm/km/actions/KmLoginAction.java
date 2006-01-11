
/*
 * Created on Jan 18, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.actions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.ibm.appsecure.exception.EncryptionException;
import com.ibm.appsecure.exception.ValidationException;
import com.ibm.appsecure.service.GSDService;
import com.ibm.appsecure.util.Encryption;
import com.ibm.appsecure.util.IEncryption;
import com.ibm.km.common.PropertyReader;
import com.ibm.km.dao.impl.KmElementMstrDaoImpl;
import com.ibm.km.dto.KmAlertMstr;
import com.ibm.km.dto.KmElementMstr;
import com.ibm.km.dto.KmLinkMstrDto;
import com.ibm.km.dto.KmLogin;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.dto.LinkMstrDto;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.KmCSRHomeBean;
import com.ibm.km.forms.KmDocumentHitsCountFormBean;
import com.ibm.km.forms.KmLoginFormBean;
import com.ibm.km.forms.KmRCContentUploadFormBean;
import com.ibm.km.forms.KmSopUploadFormBean;
import com.ibm.km.services.KeywordMgmtService;
import com.ibm.km.services.KmAlertMstrService;
import com.ibm.km.services.KmDocumentHitsCountService;
import com.ibm.km.services.KmElementMstrService;
import com.ibm.km.services.KmLinkMstrService;
import com.ibm.km.services.KmScrollerMstrService;
import com.ibm.km.services.KmUserMstrService;
import com.ibm.km.services.LoginService;
import com.ibm.km.services.impl.KeywordMgmtServiceImpl;
import com.ibm.km.services.impl.KmAlertMstrServiceImpl;
import com.ibm.km.services.impl.KmDocumentHitsCountServiceImpl;
import com.ibm.km.services.impl.KmElementMstrServiceImpl;
import com.ibm.km.services.impl.KmLinkMstrServiceImpl;
import com.ibm.km.services.impl.KmScrollerMstrServiceImpl;
import com.ibm.km.services.impl.KmUserMstrServiceImpl;
import com.ibm.km.services.impl.LoginServiceImpl;
import com.ibm.km.common.Constants;


/**
 * @author namangup
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class KmLoginAction extends Action {

	private static Logger logger = Logger.getLogger(KmLoginAction.class.getName());
	/* Local Variables */
	private static String AUTHENTICATION_SUCCESS = "loginSuccess";

	private static String FORGOTPASSWORD = "forgotPassword";

	private static String CSRLOGIN_SUCCESS = "csrLoginSuccess";

	private static String CSR_AUTHENTICATION_FAILURE = "csrLoginFailure";

	private static String FORGOTPASSWORD_SUCCESS = "forgotPwdSuccess";

	private static String HOME_SUCCESS = "home";
	
	private static int PASSWORD_LENGTH=8;

	

	
	public ActionForward execute(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {

		logger.info("KmLoginAction Start >>>");

		java.util.Date dt1 = new java.util.Date();
		
		ActionErrors errors = new ActionErrors();
		ActionMessages messages=new ActionMessages();
		ActionForward forward = new ActionForward();
		HttpSession session = request.getSession();
		KmLoginFormBean loginformBean=null;
		loginformBean = (KmLoginFormBean) form;	
		KmLogin login = new KmLogin();
		ArrayList userRoleList = new ArrayList();
		KmLinkMstrService linkMstrService = new KmLinkMstrServiceImpl();
		
		//logger.info(loginformBean.getUserName() + " user in login attempt from ip:" + getAddressInfo(request) );
		try {
		
			if ("/initForgotPassword".equalsIgnoreCase(mapping.getPath())) {
				
				String kmForgot=request.getParameter("KmForgot");
				
				if(kmForgot!=null &&  kmForgot.equals("true")) {
					forward = mapping.findForward("kmForgotPassword");
				}
				else {
					forward = mapping.findForward(FORGOTPASSWORD);
				}
				 
			}
			
			if ("/forgotPassword".equalsIgnoreCase(mapping.getPath())) {
							logger.info(loginformBean.getUserName() + " is using forgot password module From the Machine with IP : "+request.getRemoteAddr());
							
							String csrStatus="";
							csrStatus=request.getParameter("CSR");
							//String kmPass=""; 
							String kmPass =loginformBean.getKmforgot();
							//kmPass=(String)request.getAttribute("PASSWORDKM");
							logger.info("kmpass"+kmPass);
							LoginService loginService= new LoginServiceImpl();
							//Getting the user details and email id .
							ArrayList alist=loginService.getEmailId(loginformBean.getUserName());
							//	ArrayList alist=kmLoginDaoImpl.getEmailId(loginformBean.getUserName());
							String actorId="";
							String emailId="";
							String userLoginId="";
							String status="";
							
							if(alist!=null) {
								userLoginId=(String)alist.get(0);
								emailId=(String)alist.get(1);
								actorId=(String)alist.get(2);
								status=(String) alist.get(3);
								if(Integer.parseInt((String) alist.get(4))<6){
									errors.add("loginId", new ActionError("password.reset.retry"));
									saveErrors(request, errors);	
									if(csrStatus.equals("TRUE")) {
										forward = mapping.findForward("forgotPassword");
										return (forward);
									}
									else{
										forward = mapping.findForward("kmForgotPassword");
										return (forward);
									}
								}
								if(!status.equalsIgnoreCase("A")){
									errors.add("loginId", new ActionError("login.user.deactivated"));
									saveErrors(request, errors);	
									if(csrStatus.equals("TRUE")) {
										forward = mapping.findForward("forgotPassword");
										return (forward);
									}
									else{
										forward = mapping.findForward("kmForgotPassword");
										return (forward);
									}
								}
								
							}
							else {
								errors.add("loginId", new ActionError("login.invalid.login"));
								saveErrors(request, errors);	
								if(csrStatus.equals("TRUE")) {
									forward = mapping.findForward("forgotPassword");
									return (forward);
								}
								else{
									forward = mapping.findForward("kmForgotPassword");
									return (forward);
								}
							}
				
			
							if(userLoginId.equals("")) {
								if(csrStatus.equals("TRUE")) {
									forward = mapping.findForward("forgotPassword");
									return (forward);
								}
								else {
									if(userLoginId.equals("")) {
										errors.add("loginId", new ActionError("errors.login.invalid_id"));
										saveErrors(request, errors);
									}
									forward = mapping.findForward("kmForgotPassword");
									return (forward);
								}
					
							}
				
						//	String UDAdmin=getResources(request).getMessage("UD.Admin")
						//	.toString();
									logger.info("Forgot Password: Sending Email for:" + loginformBean.getUserName() + " userID:" + login.getUserId() );
									String strSubject = "Your KM Password";
									String strMessage = null;
									String txtMessage = null;
									StringBuffer sbMessage = new StringBuffer();

				
							IEncryption t = new Encryption();
				
							if(!userLoginId.equalsIgnoreCase("")) {
								if(!emailId.equalsIgnoreCase("")) {
									sbMessage.append("Dear " + loginformBean.getUserName()
																+ ", \n\n");
														sbMessage.append("Your KM password is : ");
						//strMessage = loginformBean.getUserName().substring(0, 1) Math.abs(new Random().nextInt()) loginformBean.getUserName().substring(2, 3);
														
						// Security closer for new generated password as GSD complaint ...
					    strMessage = generatePassword(loginformBean.getUserName());														
					    // ---------------------------------------------------------------
														
									String encPassword = t.generateDigest(strMessage);
									sbMessage.append(strMessage +"\n");
								//	sbMessage.append(strMessage + "\nUD ADMIN : Please Change the Password in the for the iPoral User "+loginformBean.getUserName());
								//	sbMessage.append("\n");
								//	sbMessage.append("\n");
														sbMessage.append("\nRegards ");
														sbMessage.append("\nKM Administartor ");
														sbMessage.append("\n\n/** This is an Auto generated email.Please do not reply to this.**/");
														txtMessage = sbMessage.toString();
									try {
											Properties prop = System.getProperties();
											ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
											String strHost = bundle.getString("login.smtp");
											String strFromEmail = bundle.getString("sender.email");

											prop.put("mail.smtp.host", strHost);
											Session ses = Session.getDefaultInstance(prop, null);
											MimeMessage msg = new MimeMessage(ses);
											msg.setFrom(new InternetAddress(strFromEmail));
											msg.addRecipient(Message.RecipientType.TO,
													new InternetAddress(emailId));
										//	msg.addRecipient(Message.RecipientType.CC,new InternetAddress(UDAdmin));
											msg.setSubject(strSubject);
											msg.setText(txtMessage);
											msg.setSentDate(new Date());
											/*BufferedWriter br = new BufferedWriter(new FileWriter(new File("/mail.txt")));
											br.write(txtMessage);
											br.close();*/
											Transport.send(msg);
											logger.info("Email Send to:"+ emailId);
											// Bug:MASDB00064305 is resolved :    Proper message is not shown when the password is sent
											// Changes are included in login.jsp and csrLogin.jsp
											messages.add("msg1",new ActionMessage("password.sent",emailId));
											saveMessages(request,messages);
										//	kmLoginDaoImpl.updatePasswordExpiryDate(userLoginId);
											//Changed by Anil against code review defect : MASDB00098318 
											loginService.updatePassword(loginformBean.getUserName(),encPassword);
										//	kmLoginDaoImpl.updatePassword(loginformBean.getUserName(),encPassword);
											loginformBean.setMessage("Password is sent to your mailid : "+emailId);
											
												return  mapping.findForward(FORGOTPASSWORD_SUCCESS);
											
										} catch (javax.mail.internet.AddressException ae) {
											errors.add("errors.forgotPassword", new ActionError("error.forgotPassword"));
											saveErrors(request, errors);
											forward = mapping.findForward(FORGOTPASSWORD_SUCCESS);
											logger.error("AddressException occurs in execute of Login Action: "+ ae.getMessage());
										} catch (javax.mail.MessagingException me) {
											errors.add("errors.forgotPassword", new ActionError("error.server.forgotPassword"));
											forward = mapping.findForward(FORGOTPASSWORD_SUCCESS);
											logger.error("MessagingException occurs in execute of Login Action: "+ me.getMessage());
											saveErrors(request, errors);
										}
										catch(Exception e){
											logger.error("Exception occurs in execute of Login Action: "+ e.getMessage());
											forward = mapping.findForward(FORGOTPASSWORD_SUCCESS);
											
										}
														//logger.info("strMessage"+strMessage);

								}
								else {
									errors.add("loginId", new ActionError("login.noEmailId"));
									saveErrors(request, errors);
									if(csrStatus!=null && csrStatus.equals("TRUE")){
										forward=mapping.findForward("forgotPassword");
									}else{
										forward = mapping.findForward("kmForgotPassword");
									}
								}
							}
							else {
								errors.add("loginId", new ActionError("login.invalid.login"));
								saveErrors(request, errors);
								if(csrStatus!=null && csrStatus.equals("TRUE")){
										forward=mapping.findForward("csrLoginFailure");
								}else{
										forward = mapping.findForward("loginFailure");
								}
							}
				
						}
			if ("/home".equalsIgnoreCase(mapping.getPath())) 
			{
				setScroller(session);
				forward = mapping.findForward(HOME_SUCCESS);
			
			}
			
			if ("/login".equalsIgnoreCase(mapping.getPath())) {
			
				String userId=(String)request.getAttribute("userid");
				String password=(String)request.getAttribute("password");	
				logger.info("pass:"+password);
				if(userId!=null){
					loginformBean.setUserId(userId);
					loginformBean.setPassword(password);		
				}
				String csrStatus=request.getParameter("CSR");
				IEncryption enc_dec = new Encryption();
				//System.out.println("******************************************************");
				////System.out.println("mine pwd" +enc_dec.decrypt("240-216-96-138-223-30-157-171"));
				/* 
				* GSD implementation to validate user name and password
				*/
				//Encryption Code for Password 
				IEncryption encrypt = new Encryption();
				login.setUserId(loginformBean.getUserId());
				login.setPassword(encrypt.generateDigest(loginformBean.getPassword()));
				
				////System.out.println(login.getPassword());
				KmUserMstrService userService = new KmUserMstrServiceImpl();
				if(!userService.checkDuplicateUserLogin(loginformBean.getUserId().toUpperCase())){
					errors.add("errors.login.invalid_id", new ActionError("errors.login.invalid_id"));
					saveErrors(request, errors);			
					logger.error("Invalid Login Id");
				return mapping.findForward("loginFailure");
								}
				GSDService gSDService = new GSDService();
				KmUserMstr userInfo =
					(KmUserMstr) gSDService.validateCredentials(
						login.getUserId(),
 						loginformBean.getPassword(),
						"com.ibm.km.dto.KmUserMstr");
					
				LoginService loginService = new LoginServiceImpl();
				
              // LDAP user validation inmplemented by Kundan Kumar for security finding closer 
				if("Y".equalsIgnoreCase(PropertyReader.getAppValue("doLdapValidation")))
				{ 
					logger.info("Checking LDAP as ValidateLDAP is "+PropertyReader.getAppValue("doLdapValidation"));
					try
					{
						if(!loginService.isValidUser(loginformBean.getUserId()))
						{
			                errors.add("errors.login.user_invalid", new ActionError("login.ldapValidationError"));
			 				saveErrors(request, errors);			
			 				logger.info("User LDAP validation failed for user : "+loginformBean.getUserId());
			 				forward=mapping.findForward(CSR_AUTHENTICATION_FAILURE);
			 				return (forward);
						}
					}catch(Exception ee)
					{
						 errors.add("errors.login.user_invalid", new ActionError("login.ldapConnectionFail"));
			 				saveErrors(request, errors);			
			 				logger.info("Connection couldn't established for the user : "+loginformBean.getUserId());
			 				forward=mapping.findForward(CSR_AUTHENTICATION_FAILURE);
			 				return (forward);		 			
					}
				}
				//---------------------------- LDAP validation finish
				
				KmUserMstr userBean = loginService.populateUserDetails(login);
		
				List favList = new ArrayList();
				
				//To get favrorites document list of a user
				favList = loginService.getFavorites(Integer.parseInt(userBean.getUserId()));


				ArrayList linksList = new ArrayList();
				ArrayList<KmLinkMstrDto> toplinksList = new ArrayList<KmLinkMstrDto>();
				
				toplinksList = linkMstrService.viewLinks(Constants.TOP_LINKS); 

				userRoleList = linkMstrService.getUserRoleList(userBean.getKmActorId());
				// Changes for displaying top most SOP and Product documents
				ArrayList topBarLinks = new ArrayList();
				ArrayList bottomBarLinks = new ArrayList();
				if(userBean.getKmActorId().equals(Constants.CIRCLE_CSR) || userBean.getKmActorId().equals(Constants.CATEGORY_CSR)){
					KmElementMstrService elementMstrService = new KmElementMstrServiceImpl();
					String LOBId = elementMstrService.getParentId(userBean.getElementId());
					linksList = linkMstrService.viewLinks(LOBId); 
					KmDocumentHitsCountFormBean bean = new KmDocumentHitsCountFormBean();
					bean.setLobId(Integer.parseInt(LOBId));
					KmDocumentHitsCountService documentHitsService = new KmDocumentHitsCountServiceImpl();
					topBarLinks = documentHitsService.getTopBarLinks(bean);
					bottomBarLinks = documentHitsService.getBottomBarLinks(bean);
				
					}
				
				// Multiple login disable; inmplemented by Kundan Kumar 
				/*logger.info("Checking Multiple login...");
				if("Y".equals(userBean.getUserLoginStatus()))
				{
	                errors.add("errors.login.user_invalid", new ActionError("login.singleSignError"));
	 				saveErrors(request, errors);			
	 				logger.info("Multiple login found for user : "+loginformBean.getUserId());
	 			 // userBean.setUserPassword("");
	 				forward=mapping.findForward(CSR_AUTHENTICATION_FAILURE);
	 				return (forward);
				}*/
				//---------------------------- Multiple login disable finish
				
				//checking for the restricted circle 
				
				    KmElementMstrService elementService= new KmElementMstrServiceImpl();
				    userBean.setRestricted(elementService.getCircleStatus(userBean.getElementId()));
				 
					//Setting user information in session
				    session.invalidate();
					session = request.getSession(true);
					userBean = populatePathInfo(userBean, session);
					session.setAttribute("USER_INFO", userBean);
					session.setAttribute("LINKS_LIST",linksList);
					session.setAttribute("TOP_LINKS_LIST",toplinksList);
					session.setAttribute("USER_ROLE_LIST",userRoleList);
/*					session.setAttribute("TOPBAR_LINKS",topBarLinks);
					session.setAttribute("BOTTOMBAR_LINKS",bottomBarLinks);
*/
					session.setAttribute("favList", favList);
					
					//session.setAttribute("LATEST_UPDATES",updatesList);
					//Warning the User for pasword expiry
					request.setAttribute("warn", loginService.getWarning(userBean.getUserPsswrdExpryDt()));
					////System.out.println("Warning "+request.getAttribute("warn"));
					logger.info("1 Actor ID: "+userBean.getKmActorId());
					if (Constants.CIRCLE_CSR.equals(userBean.getKmActorId())||Constants.CATEGORY_CSR.equals(userBean.getKmActorId())) {
						if(userBean.getLastLoginTime()==null) {
							request.setAttribute("csrFirstLogin", "true");
						//	forward = mapping.findForward("csrChangePassword");
						session.setAttribute("CSRFIRSTLOGIN","true");
						
						setScroller(session);
						
						return  mapping.findForward("firstLoginChangePassword");
						}
						else {
						//  forward = mapping.findForward(CSRLOGIN_SUCCESS);
						forward = mapping.findForward("csrOnloadPage");
					/*	if(userBean.getUserLoginStatus().equals("Y")){
												request.setAttribute("LOGIN_STATUS","Y");
						} */
						userBean.setUserLoginStatus("Y");
						//Phase3	//
						/*Calendar calendar =  new GregorianCalendar();
						calendar.add(Calendar.MINUTE,Integer.parseInt(PropertyReader.getAppValue("session.expry.alert.time")));
				        userBean.setSessionExpiry(calendar);*/
						
						userBean.setAlertUpdateTime(System.currentTimeMillis());
						userBean.setSessionID(session.getId());
						userService.updateUserStatus(userBean);	
						logger.info(loginformBean.getUserId() + " Logged in to the KM From the Machine with IP : "+request.getRemoteAddr());
						}
					} else { 
						if(userBean.getLastLoginTime()==null) {
							  request.setAttribute("FirstLogin", "true");
								setScroller(session);
							  forward = mapping.findForward("firstLoginChangePassword");
					   }
						
					   else {
						   
						   int docCount = loginService.getExpiredDocumentCount(userBean);
						   request.setAttribute("docCount", docCount);
						   setScroller(session);
					   forward = mapping.findForward(AUTHENTICATION_SUCCESS);
					  //Added by Anil as part of UD integration 
					   userBean.setUserLoginStatus("Y");
					   userBean.setSessionID(session.getId());
					   userService.updateUserStatus(userBean);	
					   logger.info(loginformBean.getUserId() + " Logged in to the KM From the Machine with IP : "+request.getRemoteAddr());									
					   }
					}
				
									
	
			}
			
			//added by vishwas for UD intregation on 17/09/14
			if ("/UDlogin".equalsIgnoreCase(mapping.getPath())) {
				String msisdn = "";
				String udid ="";
				String encdata ="";
		        String password="welcome2ibm";
		        String userId=request.getParameter("userid");//getin from url
		        encdata=request.getParameter("encdata");//geting from url
		        udid=request.getParameter("udid");//getting from url
		      // msisdn=request.getParameter("msisdn");
		       //System.out.println("udid"+udid);
		        //System.out.println(request.getParameter("userid")+"*************uesrId");
		        String udUserOLMIDPost = PropertyReader.getAppValue("ud.user.olmid.post");//for testing purpose
            	String udUserOLMIDPre = PropertyReader.getAppValue("ud.user.olmid.pre");//for testing prupose
            	//String userId="B0071323_POST";//for testing purpose
            	//encdata="yUfzwrJpgEsJBoEWq3Y1HTCLtwwZihiF3fbOf7SyKDBOnukx04Iabfhps3is3chf";//for testing purpose
            	//udid="UD_CSR";
		        
				//encdata=request.getParameter("encdata");//geting from url
				// System.out.println(request.getParameter("encdata")+"*************encdata");
				KmUserMstrService userService = new KmUserMstrServiceImpl();
//				String userId1=userId;
				ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
				String udkey=bundle.getString("UDKey");
				//String data = "h5Q2t1+JhmoozPwsCUloaF+kcs6VfeXuqk/TXIpf0h9BrqBRIZke5+tJKfoPVswF";
				 String encyData;
				encyData = encdata; //'B0015265_TEST1' = "R0PB3As+v7FRZXIMMtUcwRyoiwO5sPV71uQ5hYfLvC7Qijj9SnvMvvpn8ssUd2qXK2sqnuWdKisJuSWbA60mhw==";
				
				logger.info("encdata:===================="+encdata + " udkey:"+udkey+" userid:"+userId+":udid"+udid);

            	if(userId == null || userId.trim().equalsIgnoreCase("")) {
    				errors.add("errors.login.invalid_id", new ActionError("errors.login.user_id"));
        			logger.info("userid is null, UD authentication failed.");
        			saveErrors(request, errors);
        			forward=mapping.findForward("loginFailure");
        			return forward;
            	
            	}

            	if(encdata == null || encdata.trim().equalsIgnoreCase("")) {
    				errors.add("errors.login.invalid_id", new ActionError("errors.login.encdata"));
        			logger.info("encdata is null, UD authentication failed for User ID: " + userId);
        			saveErrors(request, errors);
        			forward=mapping.findForward("loginFailure");
        			return forward;
                }
				//This method takes two parameter first encrypted data and second key.
				JAVASHA.UserInfo us = JAVASHA.SHA.Decrypt(encyData,udkey);
				logger.debug("US Info authentication: " + us);
				boolean udAuth = false;
				logger.info("UD authentication is: " + udAuth + " UserId:" + us.UserId + " CurrentTime:" + us.CurrentTime + " MacAddress:"+  us.MacAddress);
				if(us != null) {
					if(udid.equalsIgnoreCase(us.UserId))
	                {
						udAuth = true;
	                }
				}
            	
            	if(!udAuth) {
				errors.add("errors.login.invalid_id", new ActionError("errors.login.udauth"));
    			logger.info("UD authentication failed for User ID: " + userId);
    			saveErrors(request, errors);
    			forward=mapping.findForward("loginFailure");
    			return forward;
            	}
            	KmCSRHomeBean kcr=new KmCSRHomeBean();
            
            	//kcr.setUDUserPost(udUserOLMIDPost);
            	//kcr.setUDUserPre(udUserOLMIDPre);
            	//kcr.setUD("ud");
				//userService = new UserMstrServiceImpl();
                LoginService loginService = new LoginServiceImpl();

               KmUserMstr userBean = loginService.populateUserDetailsforUD(userId);
               
               //userBean.setFirstName(userId);
               
                userBean.setUdId(udid);
                userBean.setMsisdn(us.MacAddress);


				ArrayList linksList = new ArrayList();
				ArrayList<LinkMstrDto> toplinksList = new ArrayList<LinkMstrDto>();

				toplinksList = linkMstrService.viewLinks(Constants.TOP_LINKS);

				System.out.println("\n\nList Top Link    "+toplinksList);
				userRoleList = linkMstrService.getUserRoleList(userBean.getKmActorId());

				ArrayList topBarLinks = new ArrayList();
				ArrayList bottomBarLinks = new ArrayList();

				// Refreshing Cache for all the Master Table data List

				if(userBean.getKmActorId().equals(Constants.SUPER_ADMIN))
				{
					//new MasterServiceImpl().refreshCache();//check it
				}


				//checking for the restricted circle

				    KmElementMstrService elementService= new KmElementMstrServiceImpl();
				    //userBean.setRestricted(elementService.getCircleStatus(userBean.getElementId()));

					//Setting user information in session
				    session.invalidate();
					session = request.getSession(true);
					//userBean = populatePathInfo(userBean, session);
					
					session.setAttribute("briefing","1");
				
					
					
					
					
					//System.out.println("before set the value in session");
					
					if(udUserOLMIDPost.equals(userId))
					{
						System.out.println("in post postsession"+userId);
					session.setAttribute("udUserOLMIDPost1", udUserOLMIDPost);
					}
					else if(udUserOLMIDPre.equals(userId))
					{
						
						session.setAttribute("udUserOLMIDPre", udUserOLMIDPre);//here i have to make changes for prepaid
					}
						session.setAttribute("ud", "UDlogin");
	            	//System.out.println("ater value set in seesion");
	            	
						List favList = new ArrayList();
						
						//To get favrorites document list of a user
										
						int udUserId = loginService.getUserIdForUD(userId);
						favList = loginService.getFavorites(udUserId);
						logger.info("Retrieved favourite list for UD User");
						
						for(int i = 0; i<favList.size();i++){
							logger.info("Favourite List retrieved at time of Login of UD  :: "+favList.get(i));
						}	
						
					session.setAttribute("USER_INFO", userBean);
					session.setAttribute("LINKS_LIST",linksList);
					session.setAttribute("TOP_LINKS_LIST",toplinksList);
					session.setAttribute("USER_ROLE_LIST",userRoleList);
					session.setAttribute("TOPBAR_LINKS",topBarLinks);
					session.setAttribute("BOTTOMBAR_LINKS",bottomBarLinks);
					session.setAttribute("favList", favList);
					   forward = mapping.findForward(CSRLOGIN_SUCCESS);
					   logger.info( forward+"UD11111: Logged in to the LMS From the Machine with IP : "+request.getRemoteAddr());
					   //Added by Anil as part of UD integration
					   userBean.setUserLoginStatus("Y");
					   userBean.setSessionID(session.getId());
					   if(request.getParameter("ipaddress") != null && !request.getParameter("ipaddress").equals("") ) {
						   userBean.setIpaddress(request.getParameter("ipaddress"));
					   }
					   else {
						   userBean.setIpaddress(request.getRemoteAddr() );
					   }

					   userService.updateUserStatus(userBean);
					   logger.info( "UD: Logged in to the LMS From the Machine with IP : "+request.getRemoteAddr());
					   } // UD Login - End
			//End by vishwas for UD Intregation
			
			if ("/csrlogin".equalsIgnoreCase(mapping.getPath())) {
				//System.out.println("in cdr log in--------------------");
				String csrStatus=request.getParameter("CSR");
				session.setAttribute("briefing","1");
				LoginService loginService=new LoginServiceImpl();
				KmUserMstr userBean = loginService.populateUserDetails(login);
				 String firstLogin= (String)session.getAttribute("CSRFIRSTLOGIN");
				 if(firstLogin!=null && firstLogin.equals("true")) {
					forward = mapping.findForward("csrChangePassword");
				 } else {
					forward = mapping.findForward(CSRLOGIN_SUCCESS);
					
				 }
			  }	
			if("/csrPageLogin".equalsIgnoreCase(mapping.getPath())) 	{
				
			
				String csrStatus=request.getParameter("CSR");
				session.setAttribute("briefing","1");
				if("TRUE".equals(csrStatus)){
					forward = mapping.findForward(CSR_AUTHENTICATION_FAILURE);
				}

				/* 
						 * GSD implementation to validate user name and password
						 */
				//Encryption Code for Password 
				IEncryption encrypt = new Encryption();
				login.setUserId(loginformBean.getUserId());
				login.setPassword(encrypt.generateDigest(loginformBean.getPassword()));
				GSDService gSDService = new GSDService();
				KmUserMstr userInfo =
					(KmUserMstr) gSDService.validateCredentials(
						login.getUserId(), 
						loginformBean.getPassword(),
						"com.ibm.km.dto.KmUserMstr");
				//populating userBean		
				LoginService loginService = new LoginServiceImpl();
				KmUserMstr userBean = loginService.populateUserDetails(login);
		
					//Setting user information in session
					session = request.getSession(true);
					session.setAttribute("USER_INFO", userBean);
					userRoleList = linkMstrService.getUserRoleList(userBean.getKmActorId());
					session.setAttribute("USER_ROLE_LIST",userRoleList);
					
					//Warning the User for pasword expiry
					request.setAttribute("warn", loginService.getWarning(userBean.getUserPsswrdExpryDt()));

					logger.info("2 Actor ID: "+userBean.getKmActorId());
					if (Constants.CIRCLE_CSR.equals(userBean.getKmActorId())||Constants.CATEGORY_CSR.equals(userBean.getKmActorId())) {
						if(userBean.getLastLoginTime()==null) {
							request.setAttribute("CSRFIRSTLOGIN", "true");
							forward = mapping.findForward("csrChangePassword");
						}
						else {
						  forward = mapping.findForward(CSRLOGIN_SUCCESS);
						}
					} 
				
							
									  
			}
		}

	catch (EncryptionException e) {
			errors.add("errors.login.user_invalid", new ActionError(e.getMessage()));
			e.printStackTrace();
			logger.error("EncryptionException in Login by User ID: " + loginformBean.getUserId());
			saveErrors(request, errors);
			forward=mapping.findForward(CSR_AUTHENTICATION_FAILURE);
	 }
	 catch (ValidationException ve) {
			errors.add("errors.login.user_invalid", new ActionError(ve.getMessageId()));
			ve.printStackTrace();
			logger.error("ValidationException in Login by User ID: " + loginformBean.getUserId());
			saveErrors(request, errors);
			forward=mapping.findForward(CSR_AUTHENTICATION_FAILURE);
		}
	catch (KmException ex) {
				errors.add("errors.login.user_invalid", new ActionError("errors.login.invalid_id"));
				saveErrors(request, errors);		
				ex.printStackTrace();
				logger.error("Single Sign-In Exception in Login by User ID: " + loginformBean.getUserId() );
				forward=mapping.findForward(CSR_AUTHENTICATION_FAILURE);
			}
	catch (DAOException ex) {
				errors.add("errors.login.user_invalid", new ActionError("login.connectionError"));
				saveErrors(request, errors);			
				logger.error("Network Exception in Login by User ID: " + ex.getMessage());
				forward=mapping.findForward(CSR_AUTHENTICATION_FAILURE);
	}		
		catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Exception in Login by User ID: " + ex.getMessage());
			forward=mapping.findForward(CSR_AUTHENTICATION_FAILURE);
		}

		java.util.Date dt2 = new java.util.Date();
		logger.info("Login processing Time:" + (dt2.getTime() - dt1.getTime()) );
	
		return (forward);
	}

	private void setScroller(HttpSession session) throws KmException {
		String topScrollerMsg = getTopScroller(session);
		String bottomScrollerMsg = getBottomScroller(session);
		if(topScrollerMsg== null || topScrollerMsg.equals(""))
			topScrollerMsg = "Welcome To iPortal.";
		if(bottomScrollerMsg== null || bottomScrollerMsg.equals(""))
			bottomScrollerMsg = "Welcome To iPortal.";
		session.setAttribute("tickler1", topScrollerMsg);
		session.setAttribute("tickler2", bottomScrollerMsg);
	}

	private KmUserMstr populatePathInfo(KmUserMstr userBean, HttpSession session) {

		//System.out.println("In Populate  : ");

		logger.info("populatePathInfo Start >>>");
		ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
		try {
			
			KmElementMstrService kmElementService = new KmElementMstrServiceImpl();
			List firstDropDown = null;
			if (userBean.getKmActorId().equals(bundle.getString("LOBAdmin"))||userBean.getKmActorId().equals(bundle.getString("Superadmin"))) {
				firstDropDown = kmElementService.getAllChildren(userBean.getElementId());
			}else{
				firstDropDown = kmElementService.getChildren(userBean.getElementId());
			
			}				
			if (firstDropDown!=null && firstDropDown.size()!=0){
				userBean.setInitialParentId(((KmElementMstr)firstDropDown.get(0)).getElementLevel());
				session.setAttribute("firstDropDown", firstDropDown);
				//System.out.println("INN HEREEEEEEEEEEE : " );
			}
			else{
				//System.out.println("INN ELSEEEEE : " );

				int initialLevel=Integer.parseInt(kmElementService.getElementLevelId(userBean.getElementId()));
				initialLevel++;
				userBean.setInitialParentId(initialLevel+"");
			}
			
			String elementFolderPath = kmElementService.getAllParentIdString("1",userBean.getElementId());
			userBean.setInitialElementPath(elementFolderPath);
			
			//System.out.println("In Populate  : " +  userBean.getInitialElementPath() + " InitParent: " + userBean.getInitialParentId());
			//System.out.println("List firstDropDown : " + firstDropDown);

			
		} catch (KmException e) {
			e.printStackTrace();
			logger.error("KmException occured while processing populate path info: " + e);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception occured while processing populate path info: " + e);
		}

		//System.out.println(userBean.getUserLoginId() + " exited init");
		logger.info("populatePathInfo End >>>");

		return userBean;
	}

	/**
	 * @param session
	 * @return
	 * @throws KmException
	 */
	private String getTopScroller(HttpSession session) throws KmException {
		
		KmScrollerMstrService scrollerService = new KmScrollerMstrServiceImpl();
		KmElementMstrService elementService = new KmElementMstrServiceImpl();
		KmUserMstr kmUser = null;
		kmUser = (KmUserMstr) session.getAttribute("USER_INFO");
		String elementId ="";
		String message = "";
		
		if(kmUser.getKmActorId().equals(Constants.SUPER_ADMIN))
		{
			List<Integer> elementIds = new ArrayList();
			KmElementMstr klmDto;
			ArrayList<KmElementMstr> elementList;
			elementList = elementService.getAllChildren(Constants.SUPER_ADMIN);
			Iterator itr;
			for(itr=elementList.iterator();itr.hasNext();)
			  {
				klmDto = (KmElementMstr) itr.next();
				elementIds.add(Integer.parseInt(klmDto.getElementId()));
			  }
			elementIds.add(1);  //PAN India Scroller
			message = scrollerService.getBulkScrollerMessage(elementIds); // + scrollerService.getBulkScrollerMessage(elementService.getAllElementsAsPerLevel(2));
			
//			message[1] = scrollerService.getBulkScrollerMessage(elementService.getAllElementsAsPerLevel(2));
			
		} // Lob and Circle Scroller for Super Admin
		if(kmUser.getKmActorId().equals(Constants.CIRCLE_ADMIN) || kmUser.getKmActorId().equals(Constants.CIRCLE_USER) ||  kmUser.getKmActorId().equals(Constants.REPORT_ADMIN) || kmUser.getKmActorId().equals(Constants.TSG_USER) )
		{
		   elementId = elementService.getParentId(kmUser.getElementId()); 
		   message = scrollerService.getScrollerMessage(Constants.PAN_INDIA_SCROLLER);	   
		   message =
			   message +  scrollerService.getScrollerMessage(elementId); // add LOB messages
		   
	//	   message[1] = scrollerService.getScrollerMessage(kmUser.getElementId());
		} // Lob and Circle Sroller for Circle Admin,Circle User,TSG User,Report Admin
		if(kmUser.getKmActorId().equals(Constants.LOB_ADMIN))
		{
			elementId = kmUser.getElementId();
			message = scrollerService.getScrollerMessage(Constants.PAN_INDIA_SCROLLER);
			message =
				   message + scrollerService.getScrollerMessage(elementId);
			
/*			List<Integer> elementIds = new ArrayList();
			KmElementMstr klmDto;
			ArrayList<KmElementMstr> elementList;
			elementList = elementService.getAllChildren(elementId);
			Iterator itr;
			for(itr=elementList.iterator();itr.hasNext();)
			  {
				klmDto = (KmElementMstr) itr.next();
				elementIds.add(Integer.parseInt(klmDto.getElementId()));
			  }
			
			message[1] = scrollerService.getBulkScrollerMessage(elementIds);
*/			
		} // Lob and Circle Scroller for Lob Admin
		return message;
	}
	
	/**
	 * @param session
	 * @return
	 * @throws KmException
	 */
	private String getBottomScroller(HttpSession session) throws KmException {
		
		KmScrollerMstrService scrollerService = new KmScrollerMstrServiceImpl();
		KmElementMstrService elementService = new KmElementMstrServiceImpl();
		KmUserMstr kmUser = null;
		kmUser = (KmUserMstr) session.getAttribute("USER_INFO");
		String elementId ="";
		String message = "";
		
		if(kmUser.getKmActorId().equals(Constants.SUPER_ADMIN))
		{
			message = scrollerService.getBulkScrollerMessage(elementService.getAllElementsAsPerLevel(3));
		} // Lob and Circle Scroller for Super Admin
		if(kmUser.getKmActorId().equals(Constants.CIRCLE_ADMIN) || kmUser.getKmActorId().equals(Constants.CIRCLE_USER) ||  kmUser.getKmActorId().equals(Constants.REPORT_ADMIN) || kmUser.getKmActorId().equals(Constants.TSG_USER) )
		{
		   message = scrollerService.getScrollerMessage(kmUser.getElementId());
		} // Lob and Circle Sroller for Circle Admin,Circle User,TSG User,Report Admin
		if(kmUser.getKmActorId().equals(Constants.LOB_ADMIN))
		{
			elementId = kmUser.getElementId();
			List<Integer> elementIds = new ArrayList();
			KmElementMstr klmDto;
			ArrayList<KmElementMstr> elementList;
			elementList = elementService.getAllChildren(elementId);
			Iterator itr;
			for(itr=elementList.iterator();itr.hasNext();)
			  {
				klmDto = (KmElementMstr) itr.next();
				elementIds.add(Integer.parseInt(klmDto.getElementId()));
			  }
			
			message = scrollerService.getBulkScrollerMessage(elementIds);
			
		} // Lob and Circle Scroller for Lob Admin
		return message;
	}

	private static String generatePassword(String userLoginId)
	 {		 
		 String specialChars= PropertyReader.getAppValue("specialChars").trim();
			String upperChars = PropertyReader.getAppValue("upperChars").trim();
			String lowerChars = PropertyReader.getAppValue("lowerChars").trim();
			Random randomGenerator =  new Random();
			int specialCharsLength = randomGenerator.nextInt(specialChars.length());
			int upperCharsLength = randomGenerator.nextInt(upperChars.length());
			int lowerCharsLength = randomGenerator.nextInt(lowerChars.length());
			logger.info(specialCharsLength);
			if (specialCharsLength>0)
			{
				specialChars =specialChars.substring(specialCharsLength-1, specialCharsLength);
			}
			else
				specialChars =specialChars.substring(specialCharsLength,1);	
			if(upperCharsLength>0)
			{
				   upperChars =upperChars.substring(upperCharsLength-1, upperCharsLength);
			}
			else
				   upperChars =upperChars.substring(upperCharsLength,1);
			if(lowerCharsLength>0)
			{
				 lowerChars =lowerChars.substring(lowerCharsLength-1,lowerCharsLength);
			}
			else			
				lowerChars =lowerChars.substring(lowerCharsLength,1);
			String	strMessage = userLoginId.substring(0, 1)	+ Math.abs(new Random().nextInt(1000))+ upperChars + specialChars+ userLoginId.substring(2, 3)+lowerChars;
			if(strMessage.length() < PASSWORD_LENGTH)
				strMessage=passwordPadding(strMessage);
	//		logger.info("\n\nPassword : "+strMessage+"\n\n");

			return strMessage;
	 }	

	private String getAddressInfo(HttpServletRequest request) {
		String ip = "";
		ip = "<<X-Forwarded-For:" + checkNull(request, "Forwarded-For") + ">>";  
            ip += "<<Proxy-Client-IP:"+ checkNull(request, "Proxy-Client-IP") + ">>";
            ip += "<<WL-Proxy-Client-IP:"+ checkNull(request, "WL-Proxy-Client-IP") + ">>";  
            ip += "<<HTTP_CLIENT_IP:"+ checkNull(request, "HTTP_CLIENT_IP") + ">>";  
            ip += "<<HTTP_X_FORWARDED_FOR:"+ checkNull(request, "HTTP_X_FORWARDED_FOR") + ">>"; 
            ip += "<<rlnclientipaddr:"+ checkNull(request, "rlnclientipaddr") + ">>";
            ip += "<<REMOTE_HOST:" + request.getRemoteHost() + ">>";  
            ip += "<<REMOTE_ADDR:" + request.getRemoteAddr() + ">>";  
            ip += "<<REMOTE_USER:" + request.getRemoteUser() + ">>";
        return ip;
	}

	private String checkNull(HttpServletRequest request, String header) {
		String temp = "";
		if(request.getHeader(header) != null)
			temp = request.getHeader(header);
		else
			temp = request.getHeader("X-"+ header);
		return temp;
	}
	
	// Added to pad password with extra chars if it is less then 8
	private static String passwordPadding(String pwd)
	{
		String specialChars= PropertyReader.getAppValue("specialChars").trim();
		String upperChars = PropertyReader.getAppValue("upperChars").trim();
		String lowerChars = PropertyReader.getAppValue("lowerChars").trim();
		String retPwd=pwd;
		Random randomGenerator =  new Random();
		
		StringBuffer randomStringBuf=new StringBuffer();
		randomStringBuf.append(specialChars).append(upperChars).append(lowerChars);
		
		logger.info("Padding password");
		String randomString=randomStringBuf.toString();
		randomStringBuf=null;
		String stuffChar="";
		for(int i=0;i<(PASSWORD_LENGTH-pwd.length());i++)
		{
			int pos=randomGenerator.nextInt(randomString.length());
			if(pos >0)
			 stuffChar=randomString.substring(pos-1, pos);
			else
				stuffChar=randomString.substring(pos,1);
			retPwd+=stuffChar;
			
		}
		
	 
	 return retPwd;
	 
	}
	
	
}