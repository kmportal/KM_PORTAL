package com.ibm.km.actions;

import java.io.PrintWriter;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;
import com.ibm.km.common.Constants;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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

import com.ibm.appsecure.exception.ValidationException;
import com.ibm.appsecure.service.GSDService;
import com.ibm.appsecure.util.AppUtils;
import com.ibm.appsecure.util.Encryption;
import com.ibm.appsecure.util.IEncryption;


import com.ibm.km.dto.KmActorMstr;
import com.ibm.km.dto.KmElementMstr;
import com.ibm.km.dto.KmUserDto;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.KmElementMstrFormBean;
import com.ibm.km.forms.KmScrollerMstrFormBean;
import com.ibm.km.forms.KmUserMstrFormBean;
import com.ibm.km.services.KmCategoryMstrService;
import com.ibm.km.services.KmElementMstrService;
import com.ibm.km.services.KmUserMstrService;
import com.ibm.km.services.LoginService;
import com.ibm.km.services.impl.KmCategoryMstrServiceImpl;
import com.ibm.km.services.impl.KmElementMstrServiceImpl;
import com.ibm.km.services.impl.KmUserMstrServiceImpl;
import com.ibm.km.services.impl.LoginServiceImpl;
import com.ibm.km.common.PropertyReader;
import com.ibm.km.actions.KmLoginAction;

/**
 * KmUserMstrAction
 * @author Anil
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class KmUserMstrAction extends DispatchAction {

	/**
	 * Logger for the class.
	**/
	private static final Logger logger;

	static {

		logger = Logger.getLogger(KmUserMstrAction.class);
	}

	/* Local Variables */
	private static String INITCREATEUSER_SUCCESS = "initCreateUser";
	
	private static int PASSWORD_LENGTH=8;

	private static String CREATEUSER_SUCCESS = "CreateUserSuccess";

	private static String CREATEUSER_FAILURE = "CreateUserFailure";

	private static String USERCREATED_SUCCESS = "UserCreatedSuccess";

	private static String INIT_EDIT_USER = "initEditUser";

	private static String EDIT_USER_SUCCESS = "EditUserSuccess";
	private static String INITFILTER ="initfilter";
	public KmUserMstrAction() {
	}
	static int loginCounter = 0;

	/**
	 *Initailizes the Create User page. For SuperAdmin uploads all the circles and for CircleApprover shows the different user types
	**/
	public ActionForward init(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		ActionForward forward = new ActionForward(); // return value
		HttpSession session = request.getSession();
		ActionErrors errors = new ActionErrors();
		KmUserMstrFormBean kmUserMstrFormBean = (KmUserMstrFormBean) form;
		KmUserMstr sessionUserBean =
			(KmUserMstr) session.getAttribute("USER_INFO");
		KmUserMstrService createUserService = new KmUserMstrServiceImpl();
		KmElementMstrService elementService = new KmElementMstrServiceImpl();
		KmElementMstrFormBean elementBean = new KmElementMstrFormBean();
		
		saveToken(request);
		
		ArrayList elementList = new ArrayList();

		try {
			
			// Added by Parnika
			
			initializeParameter(request, sessionUserBean, kmUserMstrFormBean);
			
			// End of changes 
			logger.info(
				sessionUserBean.getUserLoginId()
					+ " Entered into the init method of KmUserMstrAction");
			String loginUserActorId = sessionUserBean.getKmActorId();
			kmUserMstrFormBean.setKmLoginActorId(loginUserActorId);
			session.setAttribute("LOGIN_USER_ACTOR_ID", loginUserActorId);
			
			//Calling service which returns the element level id

			kmUserMstrFormBean.setUserLevelId(
				Integer.parseInt(
					elementService.getElementLevelId(
						sessionUserBean.getElementId())));
			kmUserMstrFormBean.setUserLevelName(
				kmUserMstrFormBean.getUserLevelId() + "");

			//Calling service which returns the element level name
			//String elementLevelName =elementService.getElementLevelName(kmUserMstrFormBean.getUserLevelId()+"");
			String comboType =                   
				elementService.getElementLevelName(
					kmUserMstrFormBean.getUserLevelId() + 1 + "");
			kmUserMstrFormBean.setSelectedCombo(comboType);

		
			//Getting the elementList
//			 PAN INDIA USER ID CHANGES
			if(loginUserActorId.equals("5"))
			{
				logger.info("In lob admin condition "+loginUserActorId);
			kmUserMstrFormBean.setElementList(
				elementService.getCreateUserChildren(sessionUserBean.getElementId()));
			}
			else
			{
				logger.info("In else lob admin condition "+loginUserActorId);
			kmUserMstrFormBean.setElementList(
					elementService.getChildren(sessionUserBean.getElementId()));
			}
			

			/*kmUserMstrFormBean.setElementList(
					elementService.getAllChildren(sessionUserBean.getElementId()));*/
			kmUserMstrFormBean.setComboCaption(
				kmUserMstrFormBean.getUserLevelId() + 1,
				comboType);
			kmUserMstrFormBean.setSelectedCombo(comboType);
			
			//Calling service which returns the element level id

			kmUserMstrFormBean.setCategoryList(
				elementService.getChildren(sessionUserBean.getElementId()));
			forward = mapping.findForward(INITCREATEUSER_SUCCESS);
			return forward;

		} catch (Exception e) {

			logger.error(
				"Exception occured while initializing the create user page");
			forward = mapping.findForward(INITCREATEUSER_SUCCESS);
			return forward;
		}

	}
	
		private void initializeParameter(HttpServletRequest request,
			KmUserMstr userBean,
			KmUserMstrFormBean formBean) throws KmException {
		ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");

		KmElementMstrService kmElementService = new KmElementMstrServiceImpl();
		List firstDropDown;
		try{
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
		}catch(Exception e){
			logger.info("Exception occured while initializing the create scroller page ");
			
			
		}
	}

	/**	
	*Creates new users. For SuperAdmin user CircleApprovers can be created. For CircleApprovers Circle users or CSRs 
	**/
	public ActionForward insert(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		ActionForward forward = new ActionForward(); // return value
		HttpSession session = request.getSession();
		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();
		KmUserMstrFormBean kmUserMstrFormBean = (KmUserMstrFormBean) form;
		KmUserMstr sessionUserBean =
			(KmUserMstr) session.getAttribute("USER_INFO");
		KmUserMstrService createUserService = new KmUserMstrServiceImpl();
		KmCategoryMstrService categoryService = new KmCategoryMstrServiceImpl();
		KmElementMstrService elementService = new KmElementMstrServiceImpl();
		
		String loginUserActorId = sessionUserBean.getKmActorId();
		kmUserMstrFormBean.setKmLoginActorId(loginUserActorId);
		session.setAttribute("LOGIN_USER_ACTOR_ID", loginUserActorId);
		
		
		try {

			logger.info(
				sessionUserBean.getUserLoginId()
					+ " Entered into the insert method of KmUserMstrAction");

			logger.info("Fav_cat_id : " + kmUserMstrFormBean.getCategoryId());
			//added by ashutosh for password generation and mail
	
			//String pwd = generatePwd(kmUserMstrFormBean.getUserFname());
			
			/*Modified by Karan 3 Jan 2013*/
			
			String pwd = generatePassword(kmUserMstrFormBean.getUserLoginId());
			
			IEncryption encpwd = new Encryption();
			String encpass = encpwd.generateDigest(pwd);
			kmUserMstrFormBean.setUserPassword(pwd);
			
			kmUserMstrFormBean.setCreatedBy(sessionUserBean.getUserId());

			//Setting the actor id for the user in the bean

			kmUserMstrFormBean.setKmLoginActorId(
				sessionUserBean.getKmActorId());
			//Setting Actor Id for the user
			kmUserMstrFormBean.setCreatedBy(sessionUserBean.getUserId());
			
			/*
			if (sessionUserBean.getKmActorId().equals(Constants.SUPER_ADMIN))
				kmUserMstrFormBean.setKmActorId(kmUserMstrFormBean.getRoleId());
			if (sessionUserBean.getKmActorId().equals(Constants.LOB_ADMIN))
				kmUserMstrFormBean.setKmActorId(kmUserMstrFormBean.getKmActorId());

			*/
			
			// setting Actor Id for all types of users
			
			kmUserMstrFormBean.setKmActorId(kmUserMstrFormBean.getRoleId());
			
			// Setting 
			
			if (sessionUserBean.getKmActorId().equals(Constants.SUPER_ADMIN)){
				
				if(!kmUserMstrFormBean.getKmActorId().equals(Constants.LOB_ADMIN)){
					kmUserMstrFormBean.setElementId(kmUserMstrFormBean.getCircleElementId());
				}
				
			}
						

			//Setting the element Id for the user

			if (sessionUserBean.getKmActorId().equals(Constants.CIRCLE_ADMIN)
				|| sessionUserBean.getKmActorId().equals(Constants.CIRCLE_USER))
				kmUserMstrFormBean.setElementId(sessionUserBean.getElementId());

			logger.info("loginactorId = " + kmUserMstrFormBean.getKmActorId());
			GSDService gSDService = new GSDService();
			IEncryption encrypt = new Encryption();

			logger.debug("Getting digest for user password");
			gSDService.validateCredentials(
				kmUserMstrFormBean.getUserLoginId(),
				kmUserMstrFormBean.getUserPassword());
/*			logger.info(
				"Start:" + kmUserMstrFormBean.getUserPassword() + ":End");
*/			//				Encrypting user password
		
			// 
			
			logger.info(" Validating Password.....");
			

			// Security finding trans token impl...		
				 	if(!isTokenValid(request))
				     {
						kmUserMstrFormBean.setElementList(elementService.getChildren(sessionUserBean.getElementId()));
						forward = mapping.findForward(CREATEUSER_FAILURE);				
				    	errors.add("errors.incorrectPassword",new ActionError("msg.security.id021"));
						saveErrors(request, errors);
						return forward;
				     }
			
			//System.out.println(kmUserMstrFormBean.getUserPassword());
			
			
//code commented by ashutosh
			
//			if(!validateUpperCaseChar_(kmUserMstrFormBean.getUserPassword()))
//		     {
//				kmUserMstrFormBean.setElementList(elementService.getChildren(sessionUserBean.getElementId()));
//				kmUserMstrFormBean.setUserPassword("");
//				kmUserMstrFormBean.setUserConfirmPassword("");
//				forward = mapping.findForward(CREATEUSER_FAILURE);
//				
//		    	errors.add("errors.incorrectPassword",new ActionError("msg.security.id017"));
//				saveErrors(request, errors);
//				return forward;
//		     }
//			
//			
//			if(!validateSpecialChar_(kmUserMstrFormBean.getUserPassword()))
//		     {
//				kmUserMstrFormBean.setElementList(elementService.getChildren(sessionUserBean.getElementId()));
//				kmUserMstrFormBean.setUserPassword("");
//				kmUserMstrFormBean.setUserConfirmPassword(""); 
//				forward = mapping.findForward(CREATEUSER_FAILURE);					
//		    	errors.add("errors.incorrectPassword",new ActionError("msg.security.id015"));
//				saveErrors(request, errors);
//				return forward;
//		     }
//			
			
			

			
			
		//edited by ashutosh
			//kmUserMstrFormBean.setUserPassword(
				//encrypt.generateDigest(kmUserMstrFormBean.getUserPassword()));

			KmUserMstr userMstrDto = new KmUserMstr();

			// Populating the dto				
			userMstrDto.setUserLoginId(
				kmUserMstrFormBean.getUserLoginId().toUpperCase());
			userMstrDto.setUserFname(kmUserMstrFormBean.getUserFname());
			userMstrDto.setUserMname(kmUserMstrFormBean.getUserMname());
			userMstrDto.setUserLname(kmUserMstrFormBean.getUserLname());
			userMstrDto.setUserMobileNumber(
				kmUserMstrFormBean.getUserMobileNumber());
			userMstrDto.setUserEmailid(kmUserMstrFormBean.getUserEmailid());
			
			//edited by ashutosh
			userMstrDto.setUserPassword(encpass);
			userMstrDto.setCreatedBy(kmUserMstrFormBean.getCreatedBy());
			userMstrDto.setCategoryId(kmUserMstrFormBean.getCategoryId());
			userMstrDto.setStatus("A");
			userMstrDto.setKmOwnerId("1");
			userMstrDto.setGroupId("1");
			userMstrDto.setCircleId("1");
			userMstrDto.setElementId(kmUserMstrFormBean.getElementId());
			userMstrDto.setKmActorId(kmUserMstrFormBean.getKmActorId());
			userMstrDto.setFavCategoryId(kmUserMstrFormBean.getFavCategoryId());
			LoginService loginService = new LoginServiceImpl();
			String userOLMID = userMstrDto.getUserLoginId().substring(0, 8);
			logger.info("userOLMID : "+userOLMID);
			
			//changes by Parnika
			
			userMstrDto.setPartner(kmUserMstrFormBean.getPartner());
			userMstrDto.setPbxId(kmUserMstrFormBean.getPbxId());
			userMstrDto.setBusinessSegment(kmUserMstrFormBean.getBusinessSegment());
			userMstrDto.setActivity(kmUserMstrFormBean.getActivity());
			userMstrDto.setRole(kmUserMstrFormBean.getRole());
			
			// end of changes by Parnika
			
			//Calling the service that check for duplicate user login id
			if (createUserService
				.checkDuplicateUserLogin(userMstrDto.getUserLoginId())) {

				messages.add("msg1", new ActionMessage("createUser.duplicate"));
				kmUserMstrFormBean.setUserStatus(
					"User Login Id Already exists");
				kmUserMstrFormBean.setUserLoginId("");
				kmUserMstrFormBean.setUserPassword("");
				kmUserMstrFormBean.setUserConfirmPassword("");
				kmUserMstrFormBean.setCategoryList(
					elementService.getChildren(sessionUserBean.getElementId()));
				//Retrieving the element list
				if (sessionUserBean.getKmActorId().equals(Constants.SUPER_ADMIN)
					|| sessionUserBean.getKmActorId().equals(Constants.LOB_ADMIN)) {

					kmUserMstrFormBean.setElementList(
						elementService.getChildren(
							sessionUserBean.getElementId()));
				}
				forward = mapping.findForward(CREATEUSER_FAILURE);
			}
			else  	// Calling the service that check for Login id as valid OLM ID
				if("Y".equalsIgnoreCase(PropertyReader.getAppValue("doLdapValidation")) && !loginService.isValidOlmId(userOLMID))
					 {
							messages.add("msg1", new ActionMessage("createUser.invalidUserId"));
							kmUserMstrFormBean.setUserStatus("User Login Id should have active OLM Id");
							//kmUserMstrFormBean.setUserLoginId("");
							//kmUserMstrFormBean.setUserPassword("");
							//kmUserMstrFormBean.setUserConfirmPassword("");
							kmUserMstrFormBean.setCategoryList(
								elementService.getChildren(sessionUserBean.getElementId()));
							//Retrieving the element list
							if (sessionUserBean.getKmActorId().equals(Constants.SUPER_ADMIN)
								|| sessionUserBean.getKmActorId().equals(Constants.LOB_ADMIN)) {

								kmUserMstrFormBean.setElementList(
									elementService.getChildren(
										sessionUserBean.getElementId()));
							}
							forward = mapping.findForward(CREATEUSER_FAILURE);
					}    //---------------------------- LDAP validation finishelse {
			   else
			   {
				/*Calling create user service  */
				int userCount =createUserService.createUserService(userMstrDto);
				if(userCount==-1){
					forward = mapping.findForward(CREATEUSER_FAILURE);
					errors.add(
							"errors.max_user_limit",
							new ActionError("max.report.admin.message",PropertyReader.getAppValue("max.report.admin.limit")));
						//validationExp.printStackTrace();
						saveErrors(request, errors);
						kmUserMstrFormBean.setUserLoginId("");
						kmUserMstrFormBean.setUserFname("");
						kmUserMstrFormBean.setUserMname("");
						kmUserMstrFormBean.setUserLname("");
						kmUserMstrFormBean.setUserEmailid("");
						kmUserMstrFormBean.setUserMobileNumber("");
						kmUserMstrFormBean.setUserPassword("");
						kmUserMstrFormBean.setUserConfirmPassword("");
						kmUserMstrFormBean.setCircleId("-1");
						kmUserMstrFormBean.setCategoryId("0");
						kmUserMstrFormBean.setElementId("");
						kmUserMstrFormBean.setFavCategoryId("");
						
						// Added by parnika
						kmUserMstrFormBean.setPartner("");
						kmUserMstrFormBean.setPbxId("");
						kmUserMstrFormBean.setBusinessSegment("");
						kmUserMstrFormBean.setRole("");
						kmUserMstrFormBean.setActivity("");
						// End of changes by parnika

						kmUserMstrFormBean.setCategoryList(
							elementService.getChildren(sessionUserBean.getElementId()));
						return (forward);
				}
				
				if (kmUserMstrFormBean.getKmActorId().equals(Constants.CIRCLE_ADMIN)) {
								
					
					messages.add(
							"msg6",
							new ActionMessage("createUser.circleapprover"));
				} 
				else if (kmUserMstrFormBean.getKmActorId().equals(Constants.CIRCLE_USER)) {
					
					
					messages.add(
						"msg3",
						new ActionMessage("createUser.circleuser"));
					kmUserMstrFormBean.setUserStatus(
						"A New Circle User Is Created");
				} else if (kmUserMstrFormBean.getKmActorId().equals(Constants.CIRCLE_CSR)) {
				
					messages.add(
						"msg1",
						new ActionMessage("createUser.csr"));
					kmUserMstrFormBean.setUserStatus(
						"A New CSR User Is Created");
				}else if (kmUserMstrFormBean.getKmActorId().equals(Constants.LOB_ADMIN)) {
			
					messages.add(
							"msg2",
							new ActionMessage("createUser.lobadmin"));
					}
				else if (kmUserMstrFormBean.getKmActorId().equals(Constants.CATEGORY_CSR)) {
			
					messages.add(
						"msg7",
						new ActionMessage("createUser.categorycsr"));
					kmUserMstrFormBean.setUserStatus(
						"A New Category CSR Is Created");
				}
				else if (kmUserMstrFormBean.getKmActorId().equals(Constants.REPORT_ADMIN)) {
					
		
					messages.add(
						"msg8",
						new ActionMessage("createUser.reportadmin"));
					kmUserMstrFormBean.setUserStatus(
						"A New Category CSR Is Created");
				}
				else if (kmUserMstrFormBean.getKmActorId().equals(Constants.TSG_USER)) {
				
					messages.add(
						"msg9",
						new ActionMessage("createUser.TSGUser"));
					kmUserMstrFormBean.setUserStatus(
						"A New TSG User Is Created");
				}
				
				// Sending mail to user for password
				try{
					
				String eMail = kmUserMstrFormBean.getUserEmailid();
				String msg = sendMail(eMail, kmUserMstrFormBean,
						"Sending Mail", "User Creation");
				}
				catch(Exception e){
					messages.add("msg10",new ActionMessage("createUser.mailError"));
					
				}
				

				saveMessages(request, messages);
				kmUserMstrFormBean.setUserLoginId("");
				kmUserMstrFormBean.setUserFname("");
				kmUserMstrFormBean.setUserMname("");
				kmUserMstrFormBean.setUserLname("");
				kmUserMstrFormBean.setUserEmailid("");
				kmUserMstrFormBean.setUserMobileNumber("");
				kmUserMstrFormBean.setUserPassword("");
				kmUserMstrFormBean.setUserConfirmPassword("");
				kmUserMstrFormBean.setCircleId("-1");
				kmUserMstrFormBean.setCategoryId("0");
				kmUserMstrFormBean.setElementId("");
				kmUserMstrFormBean.setFavCategoryId("");

				kmUserMstrFormBean.setCategoryList(
					elementService.getChildren(sessionUserBean.getElementId()));
				kmUserMstrFormBean.setElementList(
					elementService.getChildren(sessionUserBean.getElementId()));
				


				logger.info(
					"getUser() method : successful : redirected to target : UserEditJsp ");
				forward = mapping.findForward(USERCREATED_SUCCESS);
			}

			saveMessages(request, messages);
		} catch (ValidationException validationExp) {
			kmUserMstrFormBean.setElementList(
				elementService.getChildren(sessionUserBean.getElementId()));
			kmUserMstrFormBean.setUserPassword("");
			kmUserMstrFormBean.setUserConfirmPassword("");
			logger.error(
				"createUser method : caught ValidationException for GSD : "
					+ validationExp.getMessageId());
			errors.add(
				"errors.userName",
				new ActionError(validationExp.getMessageId()));
			//validationExp.printStackTrace();
			saveErrors(request, errors);
			//	Calling circle service to get circle list
			if (sessionUserBean.getKmActorId().equals(Constants.SUPER_ADMIN)) {
				kmUserMstrFormBean.setElementList(
					elementService.getChildren(sessionUserBean.getElementId()));
				//kmUserMstrFormBean.setCircleId("");
			}
			kmUserMstrFormBean.setCategoryList(
				elementService.getChildren(sessionUserBean.getElementId()));
			return mapping.findForward(CREATEUSER_FAILURE);
		} 
		
		catch (Exception e) {
			e.printStackTrace();

			kmUserMstrFormBean.setElementList(
				elementService.getChildren(sessionUserBean.getElementId()));
			kmUserMstrFormBean.setUserLoginId("");
			kmUserMstrFormBean.setUserFname("");
			kmUserMstrFormBean.setUserMname("");
			kmUserMstrFormBean.setUserLname("");
			kmUserMstrFormBean.setUserEmailid("");
			kmUserMstrFormBean.setUserMobileNumber("");
			kmUserMstrFormBean.setUserPassword("");
			kmUserMstrFormBean.setUserConfirmPassword("");
			kmUserMstrFormBean.setCircleId("-1");
			kmUserMstrFormBean.setCategoryId("0");
			kmUserMstrFormBean.setElementId("");
			kmUserMstrFormBean.setFavCategoryId("");
			if (e instanceof IllegalAccessException) {
				logger.error(
					"createUser() method : caught IllegalAccessException Exception while using BeanUtils.copyProperties()");
			}
			if (e instanceof InvocationTargetException) {
				logger.error(
					"createUser() method : caught InvocationTargetException Exception while using BeanUtils.copyProperties()");
			}
			if (e instanceof KmException) {

				logger.error(
					"createUser() method : caught KmException Exception while using user creation");
			}
			logger.error("Exception From LoginAction" + e);

			forward = mapping.findForward(CREATEUSER_FAILURE);

		}

		return (forward);

	}
	/**
	*Lists the users depending upon the type of the logged in user
	**/
	public ActionForward viewUserList(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		logger.info(" Entered into the viewUserList method of KmUserMstrAction");

		//	KmUserMstrDao userDAO = new KmUserMstrDaoImpl();
		KmUserMstrFormBean kmUserMstrFormBean = (KmUserMstrFormBean) form;
		ArrayList userList = new ArrayList();
		ArrayList<KmActorMstr> actorList = new ArrayList<KmActorMstr>();
		HttpSession session = request.getSession();
		KmUserMstr sessionUserBean =
			(KmUserMstr) session.getAttribute("USER_INFO");
		logger.info(
			sessionUserBean.getUserLoginId()
				+ " Entered into the viewUserList method of KmUserMstrAction"+kmUserMstrFormBean.getKmActorId());
		KmUserMstrService viewUserService = new KmUserMstrServiceImpl();
		String loginActorId = sessionUserBean.getKmActorId();
		session.setAttribute("loginUserId", loginActorId);
		kmUserMstrFormBean.setUpdatedBy(loginActorId);

		//Retreiving different user types based on the login user type
		try {

		/**	kmUserMstrFormBean.setUserList(
				(ArrayList) viewUserService.viewUsers(
					loginActorId,
					sessionUserBean.getUserId().toString()));**/
//Added by viswas for new bfr
			actorList = viewUserService.getActorList();
			System.out.println("helloooooooooooooooooooooooooooooooo"+actorList);
			kmUserMstrFormBean.setActorList(actorList);
			System.out.println("helloooooooooooooooooooooooooooooooo"+kmUserMstrFormBean.getKmActorId());
			session.setAttribute("kmactorid", kmUserMstrFormBean.getKmActorId());
			kmUserMstrFormBean.setUserList((ArrayList)viewUserService.viewUsers(loginActorId,kmUserMstrFormBean.getKmActorId(), sessionUserBean.getUserId().toString()));
			//end by viswas for new bfr
			
			//Setting the label for identifying the elementType while listing the users
			if (loginActorId.equals(Constants.SUPER_ADMIN)) {
				kmUserMstrFormBean.setElementType("LOB");
			} else
				kmUserMstrFormBean.setElementType("Circle");

			/*	if(loginActorId.equals("1"))
					kmUserMstrFormBean.setUserList((ArrayList) viewUserService.viewUsers(loginActorId,sessionUserBean.getCircleId().toString()));
				else if(loginActorId.equals("2"))
					kmUserMstrFormBean.setUserList((ArrayList) viewUserService.viewUsers(loginActorId,sessionUserBean.getCircleId().toString()));
				else if(loginActorId.equals("3"))
					kmUserMstrFormBean.setUserList((ArrayList) viewUserService.viewUsers(loginActorId,sessionUserBean.getCircleId().toString())); */
			//		//	logger.info(UserDetails.getUserLoginId(request)+" : Exiting viewList method");
		} catch (Exception e) {
			logger.error("Exception occurs in viewUserList: " + e.getMessage());
		}
		return mapping.findForward("viewUserList");
	}
	public ActionForward excelImport(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {

			//	KmUserMstrDao userDAO = new KmUserMstrDaoImpl();
			KmUserMstrFormBean kmUserMstrFormBean = (KmUserMstrFormBean) form;
			HttpSession session = request.getSession();
			KmUserMstr sessionUserBean =
				(KmUserMstr) session.getAttribute("USER_INFO");
			logger.info(
				sessionUserBean.getUserLoginId()
					+ " Entered into the excelImport method of KmUserMstrAction");
			KmUserMstrService viewUserService = new KmUserMstrServiceImpl();
			String loginActorId = sessionUserBean.getKmActorId();
			session.setAttribute("loginUserId", loginActorId);
			kmUserMstrFormBean.setUpdatedBy(loginActorId);

			//Retreiving different user types based on the login user type
			try {

				kmUserMstrFormBean.setUserList(
					(ArrayList) viewUserService.viewUsers(
						loginActorId,kmUserMstrFormBean.getKmActorId(),
						sessionUserBean.getUserId().toString()));

				//Setting the label for identifying the elementType while listing the users
				if (loginActorId.equals(Constants.SUPER_ADMIN)) {
					kmUserMstrFormBean.setElementType("LOB");
				} else
					kmUserMstrFormBean.setElementType("Circle");

				/*	if(loginActorId.equals("1"))
						kmUserMstrFormBean.setUserList((ArrayList) viewUserService.viewUsers(loginActorId,sessionUserBean.getCircleId().toString()));
					else if(loginActorId.equals("2"))
						kmUserMstrFormBean.setUserList((ArrayList) viewUserService.viewUsers(loginActorId,sessionUserBean.getCircleId().toString()));
					else if(loginActorId.equals("3"))
						kmUserMstrFormBean.setUserList((ArrayList) viewUserService.viewUsers(loginActorId,sessionUserBean.getCircleId().toString())); */
				//		//	logger.info(UserDetails.getUserLoginId(request)+" : Exiting viewList method");
			} catch (Exception e) {
				logger.error("Exception occurs in viewUserList: " + e.getMessage());
			}
			return mapping.findForward("viewUserListExcel");
		}
	
	/**
	*Edit the details of the selected user
	**/
	public ActionForward editUser(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		ActionErrors errors = new ActionErrors();
		ActionForward forward = new ActionForward();
		ActionMessages messages = new ActionMessages();
		KmUserMstrFormBean kmUserMstrFormBean = (KmUserMstrFormBean) form;
		KmElementMstrService elementService = new KmElementMstrServiceImpl();
		HttpSession session = request.getSession();
		KmUserMstr sessionUserBean =
			(KmUserMstr) session.getAttribute("USER_INFO");
		logger.info(
			sessionUserBean.getUserLoginId() + " Entered editUser method.");
		ArrayList<KmActorMstr> actorList = new ArrayList<KmActorMstr>();
		String loginUserActorId = sessionUserBean.getKmActorId();
		kmUserMstrFormBean.setKmLoginActorId(loginUserActorId);
		session.setAttribute("LOGIN_USER_ACTOR_ID", loginUserActorId);
		
		KmUserMstr userMstrDto = new KmUserMstr();
		KmUserMstrService viewUserService = new KmUserMstrServiceImpl();
		try {

			/*request.getSession().setAttribute("org.apache.struts.action.TOKEN", "84ad3c9ac4053dd6bb93df702086a456");
			
			//System.out.println(">>>>");
			//System.out.println(request.getSession().getAttribute("org.apache.struts.action.TOKEN"));
		
			//System.out.println("<<<<");*/
			
//			 Security finding trans token impl...		
		 	if(!isTokenValid(request))
		     {
				//kmUserMstrFormBean.setElementList(elementService.getChildren(sessionUserBean.getElementId()));
				forward = mapping.findForward(INIT_EDIT_USER);		
		    	errors.add("errors.incorrectPassword",new ActionError("msg.security.id021"));
				saveErrors(request, errors);
				return forward;
		     }

		 	actorList = viewUserService.getActorList();
		 	kmUserMstrFormBean.setActorList(actorList);
		 	String kmactorid=(String)session.getAttribute("kmactorid");
		 	
		 	System.out.println("testingggggggggggg"+(kmactorid));
		 	String loginUserId = sessionUserBean.getKmActorId();
			/* Set form bean data (edited information) to DTO */
			userMstrDto.setUserId(kmUserMstrFormBean.getUserId());
			userMstrDto.setUserLoginId(kmUserMstrFormBean.getUserLoginId());
			userMstrDto.setUserFname(kmUserMstrFormBean.getUserFname());
			userMstrDto.setUserMname(kmUserMstrFormBean.getUserMname());
			userMstrDto.setUserLname(kmUserMstrFormBean.getUserLname());
			userMstrDto.setUserMobileNumber(
				kmUserMstrFormBean.getUserMobileNumber());
			userMstrDto.setUserEmailid(kmUserMstrFormBean.getUserEmailid());
			userMstrDto.setStatus(kmUserMstrFormBean.getStatus());
			userMstrDto.setUpdatedBy(sessionUserBean.getUserId());
			userMstrDto.setCategoryId(kmUserMstrFormBean.getCategoryId());
			KmUserMstrService editUserService = new KmUserMstrServiceImpl();
			if (sessionUserBean.getKmActorId().equals(Constants.SUPER_ADMIN)) {
				kmUserMstrFormBean.setElementType("LOB");
			} else {

				kmUserMstrFormBean.setElementType("Circle");
			}
			logger.info("editUser() : Calling updateUser of User Service.");
			
			// Security finding trans token impl...		
		 	if(!isTokenValid(request))
		     {
		 		kmUserMstrFormBean.setElementList(elementService.getChildren(sessionUserBean.getElementId()));
				forward = mapping.findForward(CREATEUSER_FAILURE);
				errors.add("errors.incorrectPassword",new ActionError("msg.security.id021"));	
				saveErrors(request, errors);
				return forward;
		     }
		 	
		 	
			/*
			 * Call the updateUser() method of Service Layer to update user
			 * details.
			 */
			editUserService.editUserService(userMstrDto);
			messages.add("msg", new ActionMessage("updateUser.success"));
			saveMessages(request, messages);
			
			kmUserMstrFormBean.setUserStatus(
				"User Details Successfully Updated");
			String loginActorId = sessionUserBean.getKmActorId();
			kmUserMstrFormBean.setActorId((kmactorid));
			System.out.println(kmactorid+"testingggggggggggg kmUserMstrFormBean.getKmActorId()"+kmUserMstrFormBean.getKmActorId());
			kmUserMstrFormBean.setUserList(
				(ArrayList) viewUserService.viewUsers(
					loginActorId,kmactorid,
					sessionUserBean.getUserId().toString()));
			/*	if(loginActorId.equals("1"))
					kmUserMstrFormBean.setUserList((ArrayList) viewUserService.viewUsers(loginActorId,sessionUserBean.getCircleId().toString()));
				else if(loginActorId.equals("2"))
					kmUserMstrFormBean.setUserList((ArrayList) viewUserService.viewUsers(loginActorId,sessionUserBean.getCircleId().toString()));
				else if(loginActorId.equals("3"))
					kmUserMstrFormBean.setUserList((ArrayList) viewUserService.viewUsers(loginActorId,sessionUserBean.getCircleId().toString())); */
			logger.info("editUser() : User details successfully updated.");
		} catch (Exception e) {
			logger.error("Exception occurs in editUser: " + e.getMessage());
			if (e instanceof IllegalAccessException) {
				logger.error(
					"editUser() method : caught IllegalAccessException Exception while using BeanUtils.copyProperties()");
			}
			if (e instanceof InvocationTargetException) {
				logger.error(
					"editUser() method : caught InvocationTargetException Exception while using BeanUtils.copyProperties()");
			}
			if (e instanceof KmException) {
				logger.error(
					"editUser() method : caught KmException Exception while using user creation");
			}
			logger.error("Exception From LoginAction" + e);
			forward = mapping.findForward(CREATEUSER_FAILURE);

		}
		logger.info(
			"editUser() method : successfully updated user details : redirected to target : showList ");
		return mapping.findForward(EDIT_USER_SUCCESS);
	}
	/**
	*Initializes the User Edit page by listing all the users depending upon the login user type
	**/
	public ActionForward initEdit(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		ActionForward forward = new ActionForward();
		KmUserMstrFormBean kmUserMstrFormBean = (KmUserMstrFormBean) form;
		KmUserMstr userMstrDto = new KmUserMstr();
		KmUserMstrService editUserService = new KmUserMstrServiceImpl();
		HttpSession session = request.getSession();
		KmUserMstr sessionUserBean =
			(KmUserMstr) session.getAttribute("USER_INFO");
		KmCategoryMstrService categoryService = new KmCategoryMstrServiceImpl();
		KmElementMstrService elementService = new KmElementMstrServiceImpl();
		saveToken(request);
		try {
			logger.info(
				sessionUserBean.getUserLoginId()
					+ " Entered initEditUser method.");
			String userId = kmUserMstrFormBean.getSelectedUserId();
			logger.info("Selected user Id= " + userId);
			//	String userLoginId=request.getParameter("USER_LOGIN_ID");

			kmUserMstrFormBean.setUserId(userId);
			//Calling service to get the details of user by passing userId
			kmUserMstrFormBean.setUserDetails(
				editUserService.getUserService(userId));
			KmUserDto userDto = new KmUserDto();
			userDto = kmUserMstrFormBean.getUserDetails();
			kmUserMstrFormBean.setUserLoginId(userDto.getUserLoginId());
			kmUserMstrFormBean.setUserFname(userDto.getUserFname());
			kmUserMstrFormBean.setUserMname(userDto.getUserMname());
			kmUserMstrFormBean.setUserLname(userDto.getUserLname());
			kmUserMstrFormBean.setUserMobileNumber(
				userDto.getUserMobileNumber());
			kmUserMstrFormBean.setUserEmailid(userDto.getUserEmailid());
			kmUserMstrFormBean.setStatus(userDto.getStatus());
			kmUserMstrFormBean.setKmActorId(userDto.getKmActorId());
			kmUserMstrFormBean.setElementId(userDto.getElementId());
			kmUserMstrFormBean.setCircleId(userDto.getCircleId());
			logger.info(userDto.getCategoryId());
			kmUserMstrFormBean.setCategoryId(userDto.getCategoryId());

			//	kmUserMstrFormBean.setFavCategoryId(userDto.getFavCategoryId());
			String circleId = kmUserMstrFormBean.getCircleId();
			logger.info(
				"CiateeId = == = = " + kmUserMstrFormBean.getCategoryId());
			String[] circleIdArray = { circleId };
			if (kmUserMstrFormBean.getKmActorId().equals(Constants.CIRCLE_CSR)
				|| kmUserMstrFormBean.getKmActorId().equals(Constants.CATEGORY_CSR)) {
				//kmUserMstrFormBean.setCategoryId(0);
				kmUserMstrFormBean.setElementList(
					elementService.getChildren(
						kmUserMstrFormBean.getElementId()));
			}
			return mapping.findForward(INIT_EDIT_USER);
		} catch (Exception e) {

			logger.error("Exception occured while initializing editUser page ");
		}
		return null;
	}
	/*
	 * 
	 * fdsfdsfsfds
	 *
	 * To change the template for this generated type comment go to
	 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
	 */

	public ActionForward checkingTheUser(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		ActionErrors errors = new ActionErrors();
		KmUserMstrService service = new KmUserMstrServiceImpl();
		//KmUserMstr sessionUserBean = (KmUserMstr) request.getSession().getAttribute("USER_INFO");
		//logger.info(sessionUserBean.getUserLoginId()+ " Entered in to the view method of KmAlertMstrAction");

		String userId = request.getParameter("userid");
		boolean userExists = service.checkDuplicateUserLogin(userId);
		String flag;
		if (userExists) {
			flag = "true";
			errors.add(
				"errors.login.user_invalid",
				new ActionError("loginUser.exist"));

		} else {
			flag = "false";
			errors.add(
				"errors.login.user_invalid",
				new ActionError("loginUser.notExist"));
		}
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "No-Cache");
		PrintWriter out = response.getWriter();
		out.write(flag);
		out.flush();

		return null;

	}

	public boolean validateUpperCaseChar_(String pwd)
	  {
	  String expr = null;

	  expr = "[A-Z]";
	  if (!AppUtils.findRegularExp(expr, pwd)) {
		  //System.out.println("Not Found......");
	    return false;
	    }
	  else
	  {
		//System.out.println("Found......");  
	    return true;
	  }
	 }
	  
	  public boolean validateSpecialChar_(String pwd)
	  {
	    String expr = null;

	    expr= "[!@#$%&*]";
	    if (!AppUtils.findRegularExp(expr, pwd)) {
	    	//System.out.println(" Not Found......");  
		    return false;
		    }
		  else
		  {
			  //System.out.println("Found......");  
		    return true;
		  }
	  }
	  //mothed added by ashutosh for password generation
	  public String generatePwd(String Username) throws Exception {
			
			String pwd = Username.substring(0, 1)
					+ Math.abs(new Random().nextInt()) + Username.substring(2, 3);
			return pwd;
		}
	  
	  
	// Added by ashutosh for sending email

		public String sendMail(String userEmail,
				KmUserMstrFormBean kmUserMstrFormBean, String master,
				String activity) throws Exception {

			logger.info("Sending Email for user:"+ kmUserMstrFormBean.getUserLoginId() + " on email id:" + userEmail);
			String message = null;
			StringBuffer sbMessage = new StringBuffer();
			LoginService loginService = new LoginServiceImpl();

			String txtMessage = null;

			String strSubject = "iPORTAL Account successfully Created";
			sbMessage.append("Dear Sir" + ", \n\n");

			
			sbMessage.append("iPORTAL User Account Successfully created : \n\n");

/*			sbMessage.append("User ID :"
					+ kmUserMstrFormBean.getUserLoginId()
					+ "\n");
*/
			sbMessage.append("Password :"
					+ kmUserMstrFormBean.getUserPassword()
					+ "\n");

			sbMessage.append("\n\nRegards ");
			sbMessage.append("\niPORTAL System Administrator ");
			sbMessage
					.append("\n\n/** This is an Auto generated email.Please do not reply to this.**/");
			txtMessage = sbMessage.toString();
			try {
				
				ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");

				String strHost = bundle.getString("login.smtp");
				String strFromEmail = bundle.getString("sender.email");
				//

				Properties prop = System.getProperties();
				logger.info("strHost:" + strHost );

				prop.put("mail.smtp.host", strHost);
				Session ses = Session.getDefaultInstance(prop, null);
				MimeMessage msg = new MimeMessage(ses);
				msg.setFrom(new InternetAddress(strFromEmail));
				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
						userEmail));
				msg.setSubject(strSubject);
				msg.setText(txtMessage);
				Transport.send(msg);
				logger.info("Mail Send to:" + userEmail);
			} catch (Exception e) {
				
				//HAVE TO ADD DELETE METHOD FOR ROLLBACK THIS PROCESS
				//loginService.deleteUser(kmUserMstrFormBean.getUserLoginId());
				logger.error("Exception occured in sendMail():" + e.getMessage());
				e.printStackTrace();
				throw new Exception(e.getMessage());

			}
			return message;
		}
		
		private static String generatePassword(String userLoginId)
		 {		 
			  logger.info("Generating Password");
			 String specialChars= PropertyReader.getAppValue("specialChars").trim();
				String upperChars = PropertyReader.getAppValue("upperChars").trim();
				String lowerChars = PropertyReader.getAppValue("lowerChars").trim();
				Random randomGenerator =  new Random();
				int specialCharsLength = randomGenerator.nextInt(specialChars.length());
				int upperCharsLength = randomGenerator.nextInt(upperChars.length());
				int lowerCharsLength = randomGenerator.nextInt(lowerChars.length());
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
				String	strMessage = userLoginId.substring(0, 1)+ upperChars + Math.abs(new Random().nextInt(1000))+ specialChars+ userLoginId.substring(2, 3)+lowerChars;
				//logger.info("\n\nPassword : "+userLoginId.substring(0, 1)+"\n\n");
				//logger.info("\n\nPassword : "+specialChars+"\n\n");
				//logger.info("\n\nPassword : "+userLoginId.substring(2, 3)+"\n\n");
				//logger.info("\n\nPassword : "+lowerChars+"\n\n");
				
				if(strMessage.length() < PASSWORD_LENGTH)
					strMessage=passwordPadding(strMessage);
				return strMessage;
		 }	
		
	// Method added to pad password if less than 8 chars
	private static String passwordPadding(String pwd)
	{
		String specialChars= PropertyReader.getAppValue("specialChars").trim();
		String upperChars = PropertyReader.getAppValue("upperChars").trim();
		String lowerChars = PropertyReader.getAppValue("lowerChars").trim();
		String retPwd=pwd;
		Random randomGenerator =  new Random();
		
		StringBuffer randomStringBuf=new StringBuffer();
		randomStringBuf.append(specialChars).append(upperChars).append(lowerChars);
		
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
	
	
}//action
