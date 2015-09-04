package com.ibm.km.actions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;

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

import com.ibm.km.common.Constants;
import com.ibm.km.dto.CSRQuestionDto;
import com.ibm.km.dto.KmBriefingMstr;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.forms.KmBriefingMstrFormBean;
import com.ibm.km.forms.KmLoginFormBean;
import com.ibm.km.services.KmBriefingMstrService;
import com.ibm.km.services.KmCircleMstrService;
import com.ibm.km.services.KmElementMstrService;
import com.ibm.km.services.impl.KmBriefingMstrServiceImpl;
import com.ibm.km.services.impl.KmCircleMstrServiceImpl;
import com.ibm.km.services.impl.KmElementMstrServiceImpl;

/**
 * @version 	1.0
 * @author		Anil
 */
public class KmBriefingMstrAction extends DispatchAction {

	/**
	 * Logger for the class.
	**/
	private static final Logger logger;
	private static String CSR_VIEW_BRIEFING_PAGE = "csrBriefingPage";
	private static String INIT_BRIEFING_PAGE = "initBriefing";
	private static String CREATE_BRIEFING = "createBriefing";
	

	static {

		logger = Logger.getLogger(KmBriefingMstrAction.class);
	}
	/**
	 * Initializes Create Briefing page
	 **/

	public ActionForward init(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		form.reset(mapping, request);
		KmBriefingMstrFormBean kmBriefingMstrFormBean =
			(KmBriefingMstrFormBean) form;
		KmUserMstr sessionUserBean =
			(KmUserMstr) request.getSession().getAttribute("USER_INFO");
		KmElementMstrService elementService = new KmElementMstrServiceImpl();
		ArrayList circleList= new ArrayList();
		
		try {
			logger.info(
				sessionUserBean.getUserLoginId()
					+ " Entered in to the init method of KmBriefingMstrAction");
			
			/* Added by Anil for giving create alert access to all admins */
			if(sessionUserBean.getKmActorId().equals(Constants.SUPER_ADMIN)){
				circleList=elementService.getAllChildrenNoPan("1","3");
				
			}
			else if(sessionUserBean.getKmActorId().equals(Constants.LOB_ADMIN)){
				circleList=elementService.getChildren(sessionUserBean.getElementId());
				
			}
			else{
				circleList =null;
				kmBriefingMstrFormBean.setCategoryList(elementService.getChildren(sessionUserBean.getElementId()));
			}
			request.setAttribute("CIRCLE_LIST",circleList);
			kmBriefingMstrFormBean.setKmActorId(sessionUserBean.getKmActorId());
			kmBriefingMstrFormBean.setCircleId("");
			kmBriefingMstrFormBean.setCategoryId("");
			kmBriefingMstrFormBean.setBriefingHeading("");
			kmBriefingMstrFormBean.setBriefingDetails(0, "");
			kmBriefingMstrFormBean.setDisplayDt("");
			kmBriefingMstrFormBean.setCount("0");
			kmBriefingMstrFormBean.setCircleId(kmBriefingMstrFormBean.getCircleId());
			/* added by harpreet KM Phase II add the default date to scroller dates */
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
			String date = sdf.format(new java.util.Date());
			date = date.substring(0,10);
			kmBriefingMstrFormBean.setDisplayDt(date);
			
			if((kmBriefingMstrFormBean.getCircleId().equals(""))&&(!sessionUserBean.getKmActorId().equals(Constants.CIRCLE_ADMIN)))
				kmBriefingMstrFormBean.setCategoryList(null);
					
					
		} catch (Exception e) {

			logger.error("Exception occured in init method " + e.getMessage());

		}
		return mapping.findForward(INIT_BRIEFING_PAGE);
	}
	
	public ActionForward getCategories(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
			KmBriefingMstrFormBean kmBriefingMstrFormBean =
							(KmBriefingMstrFormBean) form;
			ActionErrors errors =new ActionErrors();
			KmElementMstrService elementService = new KmElementMstrServiceImpl();
			KmUserMstr sessionUserBean =
			(KmUserMstr) request.getSession().getAttribute("USER_INFO");
			
			ArrayList circleList= new ArrayList();
			try{
				if(kmBriefingMstrFormBean.getCircleId().equals("")){
					kmBriefingMstrFormBean.setCategoryList(null);
				}else{
				
				kmBriefingMstrFormBean.setCategoryList(
				elementService.getChildren(kmBriefingMstrFormBean.getCircleId()));
				}
				if(sessionUserBean.getKmActorId().equals(Constants.SUPER_ADMIN)){
					circleList=elementService.getAllChildrenNoPan("1","3");
					
				}
				else if(sessionUserBean.getKmActorId().equals(Constants.LOB_ADMIN)){
					circleList=elementService.getChildren(sessionUserBean.getElementId());
					
				}
				else{
					circleList =null;
					kmBriefingMstrFormBean.setCategoryList(elementService.getChildren(sessionUserBean.getElementId()));
				}
				request.setAttribute("CIRCLE_LIST",circleList);
			}
			catch (Exception e) {

			logger.error("Exception occured while fetching categories :" + e);
			errors.add("errors", new ActionError("briefing.failure"));
			saveErrors(request, errors);
			
		}
			return mapping.findForward(INIT_BRIEFING_PAGE);
	}
	
	/*
	 * Creates a briefing by the Circle Users to be displayed for CSRs
	 */

	public ActionForward insert(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();
		ActionForward forward = new ActionForward(); // return value
		KmBriefingMstrFormBean formBean = (KmBriefingMstrFormBean) form;
		KmLoginFormBean kmLoginFormBean = new KmLoginFormBean();
		KmBriefingMstr dto = new KmBriefingMstr();
		ArrayList circleList = new ArrayList();
		KmElementMstrService elementService = new KmElementMstrServiceImpl();
		KmBriefingMstrService service = new KmBriefingMstrServiceImpl();
		KmCircleMstrService circleService = new KmCircleMstrServiceImpl();
		HttpSession session = request.getSession();
		KmUserMstr sessionUserBean =
			(KmUserMstr) session.getAttribute("USER_INFO");

		try {
			logger.info(
				sessionUserBean.getUserLoginId()
					+ " Entered in to the insert method of KmBriefingMstrAction");
			formBean.setCreatedBy(sessionUserBean.getUserId());
			
			//changed by Anil for giving alert creattin privilliage to all admins
			
			dto = beanToDto(formBean);
			if(sessionUserBean.getKmActorId().equals(Constants.CIRCLE_ADMIN)){
				dto.setCircleId(sessionUserBean.getElementId());
			}
			else{
				dto.setCircleId(formBean.getCircleId());
			}
			
		//	//System.out.println(dto.getCircleId());
			int count = Integer.parseInt(formBean.getCount());
			
			
			if(formBean.getCategoryId().equals("0"))
			   dto.setCategoryId(null);
			service.createBriefing(dto);
			messages.add("msg1", new ActionMessage("briefing.success"));
			saveMessages(request, messages);
			for (int i = 0; i <= count; i++) {

				formBean.setBriefingDetails(i, "");
			}
			if(sessionUserBean.getKmActorId().equals(Constants.SUPER_ADMIN)){
				circleList=elementService.getAllChildrenNoPan("1","3");
				formBean.setCategoryList(null);
			}
			else if(sessionUserBean.getKmActorId().equals(Constants.LOB_ADMIN)){
				circleList=elementService.getChildren(sessionUserBean.getElementId());
				formBean.setCategoryList(null);
			}
			else{
				circleList =null;
				formBean.setCategoryList(elementService.getChildren(sessionUserBean.getElementId()));
			}
			request.setAttribute("CIRCLE_LIST",circleList);
			formBean.setBriefingHeading("");
			formBean.setDisplayDt("");
			formBean.setCount("0");
			formBean.setCategoryId("");
			formBean.setCircleId("");
			//request.setAttribute("count",kmBriefingMstrFormBean.getCount());
			return mapping.findForward(CREATE_BRIEFING);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception occured while creating briefing :" + e);
			errors.add("errors", new ActionError("briefing.failure"));
			saveErrors(request, errors);
			formBean.setCircleId("");
			formBean.setCategoryId("");
			formBean.setBriefingHeading("");
			formBean.setCount("0");
			
			return init(mapping,formBean,request,response);
		}

	}
	
	public ActionForward initCSRBriefing(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		KmBriefingMstrFormBean formBean = (KmBriefingMstrFormBean) form;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String date = sdf.format(new java.util.Date());
		date = date.substring(0,10);
		formBean.setDisplayDt(date);
		
		return  mapping.findForward("viewCsrBriefingPage");
	}

	public ActionForward viewCSRBriefing(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		ActionForward forward = new ActionForward();

		HttpSession session = request.getSession();
		session = request.getSession(true);
		String ud=(String)session.getAttribute("ud");
		KmBriefingMstrFormBean formBean = (KmBriefingMstrFormBean) form;
		KmUserMstr sessionUserBean =
			(KmUserMstr) session.getAttribute("USER_INFO");
		/*String briefingSource=(String)request.getParameter("270587");
		if(briefingSource==null){
			briefingSource="";
		}
		//System.out.println("Briefing source =" +briefingSource); */
		String date = request.getParameter("briefingDate");
		sessionUserBean.setAlertUpdateTime(0);
		if (date == null) {
			date = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "";
		}
		formBean.setDisplayDt(date);
		request.setAttribute("briefingDate", date);
		System.out.println("hello testing ud"+ud);
		request.setAttribute("ud", ud);//for ud intregation added by vishwas
		ArrayList briefingBeanList = new ArrayList();
		KmBriefingMstrService briefingService = new KmBriefingMstrServiceImpl();
		ArrayList briefingDtoList = null;
		KmBriefingMstr brief = null;
		
		try{
			String favCategoryId = null;
		if (sessionUserBean.getKmActorId().equals(Constants.CATEGORY_CSR)) {
			favCategoryId = sessionUserBean.getFavCategoryId();
		}

		logger.debug("Users favCategoryId" + favCategoryId);
		logger.debug("Users getElementId" + sessionUserBean.getElementId());

		String briefing = (String) session.getAttribute("briefing");
		if (briefing == null) {
			briefing = "1"; 
			logger.debug("11111111111: ");
		}
		if (briefing.equals("2")) { // 2 is when view briefing from Side menu
			briefingDtoList =
				briefingService.viewBriefing(sessionUserBean.getElementId(),favCategoryId, date);	
		}
		else
		{    // To view Briefing for the first time when login
		briefingDtoList =
			briefingService.viewLoginBriefing(sessionUserBean.getElementId(),favCategoryId, date);	
		}
		for (Iterator iterator = briefingDtoList.iterator();
			iterator.hasNext();) 
		{
			briefingBeanList.add(
				DtoToBean((KmBriefingMstr) iterator.next()));
		}
		
		//System.out.println("\n\n\n "+briefingService.getQuestions().size());
		
		
		request.setAttribute("BRIEFING_LIST", briefingBeanList);
		//request.setAttribute("QUIZ_LIST", briefingService.getQuestions());
		
		if (briefing.equals("2")) {
			logger.debug("222222: ");
			forward = mapping.findForward("viewCsrBriefingPage");
		} else {
			logger.debug("333333333333333: ");

			forward = mapping.findForward(CSR_VIEW_BRIEFING_PAGE);
		}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return forward;
	}

	private KmBriefingMstr beanToDto(KmBriefingMstrFormBean formBean) {

		KmBriefingMstr dto = new KmBriefingMstr();
		// Getting the briefing details
		int count = Integer.parseInt(formBean.getCount());
		String detailedBriefing = formBean.getBriefingDetails(0);
		for (int i = 1; i <= count; i++) {
			detailedBriefing =
				detailedBriefing + "|" + formBean.getBriefingDetails(i);
		}
		//logger.info(detailedBriefing);
		dto.setBriefingHeading(formBean.getBriefingHeading());
		dto.setBriefingDetails(detailedBriefing);
		dto.setDisplayDt(formBean.getDisplayDt());
		dto.setCreatedBy(formBean.getCreatedBy());
		if (formBean.getCategoryId().equals("")) {
			dto.setCircleId(formBean.getCircleId());
		} else {
			dto.setCategoryId(formBean.getCategoryId());
		}
		return dto;
	}

	public KmBriefingMstrFormBean DtoToBean(KmBriefingMstr dto) {
		KmBriefingMstrFormBean formBean = new KmBriefingMstrFormBean();
		formBean.setBriefingHeading(dto.getBriefingHeading());
		formBean.setDisplayDt(dto.getDisplayDt());
		formBean.setCreatedDt(dto.getCreatedDt());
		formBean.setCreatedBy(dto.getCreatedBy());
		formBean.setCircleId(dto.getCircleId());
		StringTokenizer tokenized =
			new StringTokenizer(dto.getBriefingDetails(), "|", false);
		int i = 0;
		while (tokenized.hasMoreTokens()) {
			formBean.setBriefingDetails(i, (String) tokenized.nextToken());
			i++;
		}
		formBean.setCount(i - 1 + "");
		return formBean;
	}

	public ActionForward editBriefing(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		ActionForward forward = new ActionForward();
		////System.out.println("Inside KmBriefingMstrAction.editBriefing!!!! ");
		HttpSession session = request.getSession();
		session = request.getSession(true);
		KmUserMstr sessionUserBean =
			(KmUserMstr) session.getAttribute("USER_INFO");
		
		String date = request.getParameter("briefingDate");
		
		int i=0;
		KmElementMstrService elementService = new KmElementMstrServiceImpl();
		KmBriefingMstrService briefingService = new KmBriefingMstrServiceImpl();
		KmBriefingMstrFormBean formBean = (KmBriefingMstrFormBean) form;
		formBean.setInitStatus("false");
		ArrayList elementList = null;
		ArrayList circleList=null;
		try{
			
			logger.info(sessionUserBean.getUserLoginId()+" Entered into editBriefing method of KmBiefingMstrAction" );
		String[] elementIds=null;
		String circleId="";
		if(sessionUserBean.getKmActorId().equals(Constants.SUPER_ADMIN)){
			circleList=elementService.getAllChildrenNoPan("1","3");
			request.setAttribute("CIRCLE_LIST",circleList);
		}
		if(sessionUserBean.getKmActorId().equals(Constants.LOB_ADMIN)){
			////System.out.println("Inside sessionBean.actor_id if!!!! ");
			circleId=formBean.getCircleId();
			circleList=elementService.getChildren(sessionUserBean.getElementId());
			request.setAttribute("CIRCLE_LIST",circleList);
		}else{
			circleId=sessionUserBean.getElementId();
		}
		
		/*elementList=elementService.getAllChildren(circleId);
		if(elementList!=null){
			elementIds=new String [elementList.size()+1]; 
			for (Iterator iter=elementList.iterator(); iter.hasNext();) {
				KmElementMstr element=(KmElementMstr)iter.next();
				elementIds[i]=element.getElementId();
				i++;
			}
			elementIds[i]=circleId;
		}
		else{
			elementIds=new String [1];
			elementIds[0]=circleId;
		}*/
		
		if(sessionUserBean.getKmActorId().equals(Constants.SUPER_ADMIN)){
			circleId=formBean.getCircleId();//circleId = "12141";
		}
		ArrayList aList =briefingService.editBriefings(circleId,formBean.getFromDate(),formBean.getToDate(),Integer.parseInt(sessionUserBean.getUserId()));
		////System.out.println("Inside edited briefings inside KmBriefingMstraction!!!!!after editing briefings");
		formBean.setBriefingList(aList);
		mapping.findForward("editBriefing");
		}
		catch(Exception e){
			logger.error("Exception occured while fetching the briefings");
		}
		return mapping.findForward("editBriefing");
	}
	/*
	 * Method added by Atul for view/edit briefing
	 */
	public ActionForward initEditBriefing(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
			////System.out.println("Inside initEditBriefing!!!!!");
			KmBriefingMstrFormBean formBean = (KmBriefingMstrFormBean) form;
			KmUserMstr sessionUserBean =
				(KmUserMstr) request.getSession().getAttribute("USER_INFO");
			KmElementMstrService elementService = new KmElementMstrServiceImpl();
			ArrayList circleList= new ArrayList();
			try{
				if(sessionUserBean.getKmActorId().equals(Constants.SUPER_ADMIN)){
					circleList=elementService.getAllChildrenNoPan("1","3");
					
				}
				if(sessionUserBean.getKmActorId().equals(Constants.LOB_ADMIN)){
					circleList=elementService.getChildren(sessionUserBean.getElementId());
				}
			formBean.setKmActorId(sessionUserBean.getKmActorId());
			request.setAttribute("CIRCLE_LIST",circleList);
			/* added by harpreet KM Phase II add the default date to scroller dates */
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
			String date = sdf.format(new java.util.Date());
			date = date.substring(0,10);
			formBean.setFromDate(date);
			formBean.setToDate(date);
			formBean.setInitStatus("true");
			 
			
			}catch (Exception e) {

			logger.error("Exception occured in init method " + e.getMessage());

		}
		return mapping.findForward("initEdit");
			
	}
			
	/*
	 * Method will populate the values editing the briefing - Added by Atul
	 */
	public ActionForward updateBriefing(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
			
			////System.out.println("Inside update briefing!!!!!");
			HttpSession session = request.getSession();
			session = request.getSession(true);
			KmUserMstr sessionUserBean =
				(KmUserMstr) session.getAttribute("USER_INFO");
		try{	
			KmBriefingMstrService briefingService = new KmBriefingMstrServiceImpl();
			KmBriefingMstrFormBean formBean = (KmBriefingMstrFormBean) form;
			String briefId = request.getParameter("briefId");
			KmElementMstrService elementService = new KmElementMstrServiceImpl();
			
			KmBriefingMstr briefingDto   =briefingService.updateBriefings(Integer.parseInt(briefId));
			formBean.setBriefingHeading(briefingDto.getBriefingHeading());
			formBean.setBriefingDetails(briefingDto.getArrBriefDetails());
			formBean.setBriefingId(briefingDto.getBriefingId());
			formBean.setDisplayDt(briefingDto.getDisplayDt());
			if(sessionUserBean.getKmActorId().equals(Constants.CIRCLE_ADMIN)){
				formBean.setCircleId(sessionUserBean.getElementId());
			}
	}catch(Exception e){
				logger.error("Exception occured while updating the briefing");
			}
			return mapping.findForward("updateBriefing");
		}	
	/*
	 * Method will update  the edited briefing - Added by Atul
	 */	
	public ActionForward submitUpdatedBriefing(
				ActionMapping mapping,
				ActionForm form,
				HttpServletRequest request,
				HttpServletResponse response)
				throws Exception {
				HttpSession session = request.getSession();
				session = request.getSession(true);
				KmUserMstr sessionUserBean =
					(KmUserMstr) session.getAttribute("USER_INFO");
								KmBriefingMstrService briefingService = new KmBriefingMstrServiceImpl();
				KmBriefingMstrFormBean formBean = (KmBriefingMstrFormBean) form;
				
				
				KmElementMstrService elementService = new KmElementMstrServiceImpl();
				
				int i=0;
				formBean.setInitStatus("false");
				try{
					logger.info(sessionUserBean.getUserLoginId()+" Entered into submitUpdatedBriefing method of KmBiefingMstrAction" );
					 String briefingDetails="";
		             for(int j=0;j<formBean.getBriefingDetails().length;j++) {
		             briefingDetails=briefingDetails + "|"+ formBean.getBriefingDetails()[j] ;
		             }
		              
		             briefingDetails=briefingDetails.substring(1,briefingDetails.length());
		               					
					char[] briefinDetailsArr= new char[briefingDetails.length()];
					int count=0;
					briefinDetailsArr=briefingDetails.toCharArray();
					 for(int k=0;k<briefingDetails.length();k++){
						if(briefinDetailsArr[k]=='|'){
							count++;
						}
					}
					 
					 if(count==briefingDetails.length()){
						request.setAttribute("UPDATE_ERROR","Y" );
						return mapping.findForward("updateBriefing");
						
					}
					
					briefingService.updateinDbBriefings(formBean.getBriefingId(),formBean.getBriefingHeading(),
							formBean.getBriefingDetails(),formBean.getDisplayDt());
					
					//Added for super_admin user//
					ArrayList circleList= new ArrayList();
					if(sessionUserBean.getKmActorId().equals(Constants.SUPER_ADMIN)){
						circleList=elementService.getAllChildrenNoPan("1","3");
						request.setAttribute("CIRCLE_LIST",circleList);
					}
					//Added for super_admin user//
					
//					Bug resolved : MASDB00103921
					if(sessionUserBean.getKmActorId().equals(Constants.LOB_ADMIN)){
						request.setAttribute("CIRCLE_LIST",elementService.getChildren(sessionUserBean.getElementId()));
					}
					/*String[] elementIds=null;
					ArrayList elementList = elementService.getAllChildren(formBean.getCircleId());
					if(elementList!=null){
					elementIds=new String [elementList.size()+1]; 
					for (Iterator iter=elementList.iterator(); iter.hasNext();) {
						KmElementMstr element=(KmElementMstr)iter.next();
						elementIds[i]=element.getElementId();
						
						i++;
					}
					elementIds[i]=formBean.getCircleId();
				}
				else{
					elementIds=new String [1];
					elementIds[0]=formBean.getCircleId();
				}
				*/
				ArrayList aList=null;

				
				aList =briefingService.editBriefings(formBean.getCircleId(),formBean.getFromDate(),formBean.getToDate(),Integer.parseInt(sessionUserBean.getUserId()));
				formBean.setBriefingList(aList);
				
				}
				catch(Exception e){
					e.printStackTrace();
					logger.error("Exception occured while updating the briefing");
				
				}
				return mapping.findForward("editBriefing");
			}	
	
	
	public ActionForward addQuestion(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
			
			HttpSession session = request.getSession();
			session = request.getSession(true);
			KmUserMstr sessionUserBean =
				(KmUserMstr) session.getAttribute("USER_INFO");
			ActionErrors errors = new ActionErrors();
			ActionMessages messages = new ActionMessages();
			ActionForward forward = new ActionForward();
			KmBriefingMstrFormBean formBean = (KmBriefingMstrFormBean) form;
			int questionsSize=0;
			KmBriefingMstrService briefingService = new KmBriefingMstrServiceImpl();
			
		try{
			
			formBean.reset();	
			//questionsSize=briefingService.getQuestionsSize(formBean);
			request.setAttribute("lobList", briefingService.getLobList());
			//System.out.println("questions size"+questionsSize);
						
			forward=mapping.findForward("addQuestionOne");
			
	}catch(Exception e){
				logger.error("Exception occured while updating the briefiysong");
			}
			return forward;
		}	



public ActionForward createQuestion(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		HttpSession session = request.getSession();
		session = request.getSession(true);
		KmUserMstr sessionUserBean =
			(KmUserMstr) session.getAttribute("USER_INFO");
		int result;
		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();
		ActionForward forward = new ActionForward(); // return value
		String canswer[];
		int questionsSize=0;
		KmBriefingMstrService briefingService = new KmBriefingMstrServiceImpl();
		KmBriefingMstrFormBean formBean = (KmBriefingMstrFormBean) form;
		String answer;
		String firstans;
		String secondans;
		String thirdans;
		String fourAns;
			try{	
		
				answer=formBean.getCorrectAnswer().trim();
				firstans=formBean.getFirstAnswer().trim();
				secondans=formBean.getSecondAnswer().trim();
				thirdans=formBean.getThirdAnswer().trim();
				fourAns=formBean.getFourthAnswer().trim();
				questionsSize=briefingService.getQuestionsSize(formBean);
				canswer=formBean.getCorrectAnswer().split(",");
				//For questions size
				
				if(questionsSize == 5)
				{
					
					messages.add("msg1", new ActionMessage("briefQuest2.success"));
					saveMessages(request, messages);
					forward=mapping.findForward("addQuestionOne");
				}
					
				else if(answer.equalsIgnoreCase(firstans) || answer.equalsIgnoreCase(secondans) || answer.equalsIgnoreCase(thirdans) || answer.equalsIgnoreCase(fourAns))
				{
					formBean.setNoofCorrect(canswer.length);
					formBean.setUser_login_id(sessionUserBean.getUserLoginId());
					result=briefingService.insertQuestion(formBean);
					
					messages.add("msg1", new ActionMessage("briefQuest1.success"));
					saveMessages(request, messages);
					forward=mapping.findForward("success");
					formBean.reset();		
				}
				
				else 
				{
					messages.add("msg1", new ActionMessage("briefQuest1.failure"));
					saveMessages(request, messages);
					//formBean.reset();
					request.setAttribute("lobList", briefingService.getLobList());
					forward=mapping.findForward("addQuestionOne");
					
				}
				
		
		}catch(Exception e){
			logger.error("Exception occured while updating the briefiysong");
		}
		return forward;
	}



public ActionForward CSRQuiz(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		HttpSession session = request.getSession();
		session = request.getSession(true);
		KmUserMstr sessionUserBean =
			(KmUserMstr) session.getAttribute("USER_INFO");
		KmBriefingMstrFormBean formBean = (KmBriefingMstrFormBean) form;
		 
		 KmBriefingMstrService briefingService = new KmBriefingMstrServiceImpl();
		 CSRQuestionDto arr;
		 ActionErrors errors = new ActionErrors();
			ActionMessages messages = new ActionMessages();
			ActionForward forward = new ActionForward(); // return value
			int result=0;
			int skipResult=0;
			int questionResult=0;
		ArrayList<CSRQuestionDto> questionDto=new ArrayList<CSRQuestionDto>();
		ArrayList<CSRQuestionDto> questionDto1=new ArrayList<CSRQuestionDto>();	
		ArrayList<CSRQuestionDto> questionDto2=new ArrayList<CSRQuestionDto>();
	
	try{
		
		//For Quiz Result size
		result=briefingService.getQuizResult(sessionUserBean);
		//For quiz questions
		formBean.setQuestions(briefingService.getQuestions(sessionUserBean));
		questionDto=formBean.getQuestions();
		
		//For Skip Questions Size
		skipResult=briefingService.getskipQuesize(sessionUserBean);
		
		//questionResult=briefingService.getQuestionResultSize(sessionUserBean);
		//System.out.println("questionResult"+questionResult);
		//formBean.setNewQuestions(briefingService.getNewQuestions(sessionUserBean));
		
		//questionDto2=formBean.getNewQuestions();
		
		//System.out.println("question size"+questionDto2.size());
		
		if(skipResult >0)
		{
			
			formBean.setSkipQuestions(briefingService.getSkipQuestions(sessionUserBean));
		
			questionDto1=formBean.getSkipQuestions();
			if(questionDto1.size()>0)
			{
				arr=questionDto1.get(0);
				formBean.setQuestionId(arr.getQuestionId());
				formBean.setQuestion(arr.getQuestion());
				formBean.setFirstAnswer(arr.getFirstAnswer());
				formBean.setSecondAnswer(arr.getSecondAnswer());
				formBean.setThirdAnswer(arr.getThirdAnswer());
				formBean.setFourthAnswer(arr.getFourthAnswer());
				forward=mapping.findForward("csrQuizOnePage");
			}
			else
			{
				messages.add("msg1", new ActionMessage("questions.failure"));
				saveMessages(request, messages);
				forward=mapping.findForward("questionPage");
			}
			
		}
		
		else if(questionDto2.size() > 0 )
		{
			if(questionDto2.size()>0)
			{
				arr=questionDto2.get(0);
				formBean.setQuestionId(arr.getQuestionId());
				formBean.setQuestion(arr.getQuestion());
				formBean.setFirstAnswer(arr.getFirstAnswer());
				formBean.setSecondAnswer(arr.getSecondAnswer());
				formBean.setThirdAnswer(arr.getThirdAnswer());
				formBean.setFourthAnswer(arr.getFourthAnswer());
				forward=mapping.findForward("csrQuizOnePage");
			}
			else
			{
				messages.add("msg1", new ActionMessage("questions.failure"));
				saveMessages(request, messages);
				forward=mapping.findForward("questionPage");
			}
				
		}
		
		else if(questionDto.size() == 0)
		{
			messages.add("msg1", new ActionMessage("questions.failure"));
			saveMessages(request, messages);
			forward=mapping.findForward("questionPage");	
			
		}
		
		else  if(result == questionDto.size())
		{
			messages.add("msg1", new ActionMessage("quiz.submit"));
			saveMessages(request, messages);
			forward=mapping.findForward("questionPage");
		}
		else if(questionDto.size()>0){
		
			if(questionDto.size()>0){
		arr=questionDto.get(0);
		formBean.setQuestionId(arr.getQuestionId());
		formBean.setQuestion(arr.getQuestion());
		formBean.setFirstAnswer(arr.getFirstAnswer());
		formBean.setSecondAnswer(arr.getSecondAnswer());
		formBean.setThirdAnswer(arr.getThirdAnswer());
		formBean.setFourthAnswer(arr.getFourthAnswer());
		forward=mapping.findForward("csrQuizOnePage");
	}
	else 
	{
		
		messages.add("msg1", new ActionMessage("questions.failure"));
		saveMessages(request, messages);
		forward=mapping.findForward("questionPage");
	}
		}
		
}catch(Exception e){
			logger.error("Exception occured while updating the briefiysong");
		}
		
		return forward;
	}	

public ActionForward firstQuestion(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		HttpSession session = request.getSession();
		session = request.getSession(true);
		KmUserMstr sessionUserBean =
			(KmUserMstr) session.getAttribute("USER_INFO");
		KmBriefingMstrFormBean formBean = (KmBriefingMstrFormBean) form;
		 
		 KmBriefingMstrService briefingService = new KmBriefingMstrServiceImpl();
		 int result=0;
		 CSRQuestionDto arr;
			ActionMessages messages = new ActionMessages();
			ActionForward forward = new ActionForward(); // return value
			ArrayList<CSRQuestionDto> questionDto=new ArrayList<CSRQuestionDto>();
			ArrayList<CSRQuestionDto> questionDto1=new ArrayList<CSRQuestionDto>();
			ArrayList<CSRQuestionDto> questionDto2=new ArrayList<CSRQuestionDto>();
			int skipResult=0;	
			
	try{	
		//for Questions
		formBean.setQuestions(briefingService.getQuestions(sessionUserBean));
		questionDto=formBean.getQuestions();
		skipResult=briefingService.getskipQuesize(sessionUserBean);
		formBean.setSkipQuestions(briefingService.getSkipQuestions(sessionUserBean));
		questionDto1=formBean.getSkipQuestions();

		
		if(skipResult >0)
		{	
			formBean.setUser_login_id(sessionUserBean.getUserLoginId());
			formBean.setUdId(sessionUserBean.getUdId());
			formBean.setCircle_Id(sessionUserBean.getCircleId());
			briefingService.insertSkipQuestions(formBean);
		
			if(questionDto1.size()>1)
			{
				arr=questionDto1.get(1);
				formBean.setQuestionId(arr.getQuestionId());
				formBean.setQuestion(arr.getQuestion());
				formBean.setFirstAnswer(arr.getFirstAnswer());
				formBean.setSecondAnswer(arr.getSecondAnswer());
				formBean.setThirdAnswer(arr.getThirdAnswer());
				formBean.setFourthAnswer(arr.getFourthAnswer());
				forward=mapping.findForward("csrQuizTwoPage");
			}
			else 
			{	
			
				forward=mapping.findForward("csrBriefPage");
			}
		
		}
		
		
		
		else if(questionDto.size()>0)
		{
			formBean.setUser_login_id(sessionUserBean.getUserLoginId());
			formBean.setUdId(sessionUserBean.getUdId());
			formBean.setCircle_Id(sessionUserBean.getCircleId());
			formBean.setElementId(Integer.parseInt(sessionUserBean.getElementId()));	
			formBean.setNewAnswer(briefingService.getNewQuestions(formBean));
			
			if (formBean.getNewAnswer()== "" || formBean.getNewAnswer()==null) {
				result=briefingService.insertQuizResult(formBean);
			
				if (questionDto.size()>1)
				{
					arr=questionDto.get(1);
			formBean.setQuestionId(arr.getQuestionId());
			formBean.setQuestion(arr.getQuestion());
			formBean.setFirstAnswer(arr.getFirstAnswer());
			formBean.setSecondAnswer(arr.getSecondAnswer());
			formBean.setThirdAnswer(arr.getThirdAnswer());
			formBean.setFourthAnswer(arr.getFourthAnswer());
			forward=mapping.findForward("csrQuizTwoPage");
			}
				else 
				{	
				
					forward=mapping.findForward("csrBriefPage");
				}
				}
			
			else if(formBean.getNewAnswer()!="")
			{
				System.out.println("new block");
				messages.add("msg1", new ActionMessage("quiz.submit1"));
				saveMessages(request, messages);
				arr=questionDto.get(1);
				formBean.setQuestionId(arr.getQuestionId());
				formBean.setQuestion(arr.getQuestion());
				formBean.setFirstAnswer(arr.getFirstAnswer());
				formBean.setSecondAnswer(arr.getSecondAnswer());
				formBean.setThirdAnswer(arr.getThirdAnswer());
				formBean.setFourthAnswer(arr.getFourthAnswer());
				forward=mapping.findForward("csrQuizNewPage");
			}
		else  if(result == questionDto.size())
		{
			messages.add("msg1", new ActionMessage("quiz.submit"));
			saveMessages(request, messages);
			forward=mapping.findForward("questionPage");
		}
		}
		else 
		{			
			forward=mapping.findForward("csrBriefPage");
		}
	}
		
catch(Exception e){
	e.printStackTrace();

			logger.error("Exception occured while updating the briefiysong");
		}
		
	return forward;
	}	



public ActionForward firstSkip(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		HttpSession session = request.getSession();
		session = request.getSession(true);
		KmUserMstr sessionUserBean =
			(KmUserMstr) session.getAttribute("USER_INFO");
		KmBriefingMstrFormBean formBean = (KmBriefingMstrFormBean) form;
		 
		 KmBriefingMstrService briefingService = new KmBriefingMstrServiceImpl();
		 int result=0;
		 CSRQuestionDto arr;
		 ActionErrors errors = new ActionErrors();
			ActionMessages messages = new ActionMessages();
			ActionForward forward = new ActionForward(); // return value

	try{	
		
		formBean.setUser_login_id(sessionUserBean.getUserLoginId());
		formBean.setUdId(sessionUserBean.getUdId());
		formBean.setCircle_Id(sessionUserBean.getCircleId());
		formBean.setElementId(Integer.parseInt(sessionUserBean.getElementId()));
		
		result=briefingService.insertSkipQuizResult(formBean);
		formBean.setQuestions(briefingService.getQuestions(sessionUserBean));
		
		ArrayList<CSRQuestionDto> questionDto=new ArrayList<CSRQuestionDto>();
		questionDto=formBean.getQuestions();
		System.out.println("questionDto"+questionDto.size());
		if(questionDto.size()>1){
			System.out.println("if block");
			arr=questionDto.get(1);
			formBean.setQuestionId(arr.getQuestionId());
			formBean.setQuestion(arr.getQuestion());
			formBean.setFirstAnswer(arr.getFirstAnswer());
			formBean.setSecondAnswer(arr.getSecondAnswer());
			formBean.setThirdAnswer(arr.getThirdAnswer());
			formBean.setFourthAnswer(arr.getFourthAnswer());
			forward=mapping.findForward("csrQuizTwoPage");
			}
			else 
				
			{
				System.out.println("else block");
				forward=mapping.findForward("csrBriefPage");
			}
		
		
		
}catch(Exception e){
			logger.error("Exception occured while updating the briefiysong");
		}
		
	return forward;
	}	

public ActionForward secondQuestion(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		HttpSession session = request.getSession();
		session = request.getSession(true);
		KmUserMstr sessionUserBean =
			(KmUserMstr) session.getAttribute("USER_INFO");
		KmBriefingMstrFormBean formBean = (KmBriefingMstrFormBean) form;
		 
		 KmBriefingMstrService briefingService = new KmBriefingMstrServiceImpl();
		 int result=0;
		 CSRQuestionDto arr;
			ActionMessages messages = new ActionMessages();
			ActionForward forward = new ActionForward(); // return value
			ArrayList<CSRQuestionDto> questionDto=new ArrayList<CSRQuestionDto>();
			ArrayList<CSRQuestionDto> questionDto1=new ArrayList<CSRQuestionDto>();
		int skipResult=0;
			
	try{	
		//for Questions
		formBean.setQuestions(briefingService.getQuestions(sessionUserBean));
		questionDto=formBean.getQuestions();
		skipResult=briefingService.getskipQuesize(sessionUserBean);
		formBean.setSkipQuestions(briefingService.getSkipQuestions(sessionUserBean));
		questionDto1=formBean.getSkipQuestions();
		
		if(skipResult >0)
		{

			formBean.setUser_login_id(sessionUserBean.getUserLoginId());
			formBean.setUdId(sessionUserBean.getUdId());
			formBean.setCircle_Id(sessionUserBean.getCircleId());
			briefingService.insertSkipQuestions(formBean);
			
			if(questionDto1.size()>1)
			{
				arr=questionDto1.get(1);
				formBean.setQuestionId(arr.getQuestionId());
				formBean.setQuestion(arr.getQuestion());
				formBean.setFirstAnswer(arr.getFirstAnswer());
				formBean.setSecondAnswer(arr.getSecondAnswer());
				formBean.setThirdAnswer(arr.getThirdAnswer());
				formBean.setFourthAnswer(arr.getFourthAnswer());
				forward=mapping.findForward("csrQuizThreePage");
			}
			else
			{
				forward=mapping.findForward("csrBriefPage");
			}
			
		}
		
		else if(questionDto.size()>0)
		{
			formBean.setUser_login_id(sessionUserBean.getUserLoginId());
			formBean.setUdId(sessionUserBean.getUdId());
			formBean.setCircle_Id(sessionUserBean.getCircleId());
			formBean.setElementId(Integer.parseInt(sessionUserBean.getElementId()));	
			formBean.setNewAnswer(briefingService.getNewQuestions(formBean));
			
			if (formBean.getNewAnswer()== "" || formBean.getNewAnswer()==null) {
				result=briefingService.insertQuizResult(formBean);
			
				if (questionDto.size()>2)
				{
					arr=questionDto.get(2);
			formBean.setQuestionId(arr.getQuestionId());
			formBean.setQuestion(arr.getQuestion());
			formBean.setFirstAnswer(arr.getFirstAnswer());
			formBean.setSecondAnswer(arr.getSecondAnswer());
			formBean.setThirdAnswer(arr.getThirdAnswer());
			formBean.setFourthAnswer(arr.getFourthAnswer());
			forward=mapping.findForward("csrQuizThreePage");
				}
				else 
				{	
				forward=mapping.findForward("csrBriefPage");
				}			
			}
			
			else if(formBean.getNewAnswer()!="")
			{
				
				messages.add("msg1", new ActionMessage("quiz.submit1"));
				saveMessages(request, messages);
				arr=questionDto.get(2);
				formBean.setQuestionId(arr.getQuestionId());
				formBean.setQuestion(arr.getQuestion());
				formBean.setFirstAnswer(arr.getFirstAnswer());
				formBean.setSecondAnswer(arr.getSecondAnswer());
				formBean.setThirdAnswer(arr.getThirdAnswer());
				formBean.setFourthAnswer(arr.getFourthAnswer());
				forward=mapping.findForward("csrQuizNewTwoPage");
			}
			
		else  if(result == questionDto.size())
		{
			messages.add("msg1", new ActionMessage("quiz.submit"));
			saveMessages(request, messages);
			forward=mapping.findForward("questionPage");
		}
		
		else  if(result ==questionDto.size())
		{
			messages.add("msg1", new ActionMessage("quiz.submit"));
			saveMessages(request, messages);
			forward=mapping.findForward("questionPage");
		}
		
		}
		else 
		{	
		
			forward=mapping.findForward("csrBriefPage");
		}
		
}catch(Exception e){
			logger.error("Exception occured while updating the briefiysong");
		}
		
	return forward;
	}	

public ActionForward secondSkip(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		HttpSession session = request.getSession();
		session = request.getSession(true);
		KmUserMstr sessionUserBean =
			(KmUserMstr) session.getAttribute("USER_INFO");
		KmBriefingMstrFormBean formBean = (KmBriefingMstrFormBean) form;
		 
		 KmBriefingMstrService briefingService = new KmBriefingMstrServiceImpl();
		 int result=0;
		 CSRQuestionDto arr;
		 ActionErrors errors = new ActionErrors();
			ActionMessages messages = new ActionMessages();
			ActionForward forward = new ActionForward(); // return value

	try{	
		System.out.println("second skip block");
		formBean.setUser_login_id(sessionUserBean.getUserLoginId());
		formBean.setUdId(sessionUserBean.getUdId());
		formBean.setCircle_Id(sessionUserBean.getCircleId());
		formBean.setElementId(Integer.parseInt(sessionUserBean.getElementId()));
		result=briefingService.insertSkipQuizResult(formBean);
		formBean.setQuestions(briefingService.getQuestions(sessionUserBean));
		
		ArrayList<CSRQuestionDto> questionDto=new ArrayList<CSRQuestionDto>();
		questionDto=formBean.getQuestions();
		System.out.println("size for questions"+questionDto.size());
		if(questionDto.size()>2){
			
			arr=questionDto.get(2);
			formBean.setQuestionId(arr.getQuestionId());
			formBean.setQuestion(arr.getQuestion());
			formBean.setFirstAnswer(arr.getFirstAnswer());
			formBean.setSecondAnswer(arr.getSecondAnswer());
			formBean.setThirdAnswer(arr.getThirdAnswer());
			formBean.setFourthAnswer(arr.getFourthAnswer());
			forward=mapping.findForward("csrQuizThreePage");
			}
			else
			{
				System.out.println("else block");
					forward=mapping.findForward("csrBriefPage");
			}
		
		
}catch(Exception e){
			logger.error("Exception occured while updating the briefiysong");
		}
		
	return forward;
	}	


public ActionForward thirdQuestion(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		HttpSession session = request.getSession();
		session = request.getSession(true);
		KmUserMstr sessionUserBean =
			(KmUserMstr) session.getAttribute("USER_INFO");
		KmBriefingMstrFormBean formBean = (KmBriefingMstrFormBean) form;
		 
		 KmBriefingMstrService briefingService = new KmBriefingMstrServiceImpl();
		 int result=0;
		 CSRQuestionDto arr;
	
			ActionMessages messages = new ActionMessages();
			ActionForward forward = new ActionForward(); // return value
			ArrayList<CSRQuestionDto> questionDto=new ArrayList<CSRQuestionDto>();
			ArrayList<CSRQuestionDto> questionDto1=new ArrayList<CSRQuestionDto>();
			
			int skipResult=0;
			
	try{	
		//for Questions
		formBean.setQuestions(briefingService.getQuestions(sessionUserBean));
		questionDto=formBean.getQuestions();
		skipResult=briefingService.getskipQuesize(sessionUserBean);
		if(skipResult >0)
		{
			formBean.setUser_login_id(sessionUserBean.getUserLoginId());
			formBean.setUdId(sessionUserBean.getUdId());
			formBean.setCircle_Id(sessionUserBean.getCircleId());
			briefingService.insertSkipQuestions(formBean);
			formBean.setSkipQuestions(briefingService.getSkipQuestions(sessionUserBean));
				
				questionDto1=formBean.getSkipQuestions();
				if(questionDto1.size()>2)
			{
					
				arr=questionDto1.get(3);
				formBean.setQuestionId(arr.getQuestionId());
				formBean.setQuestion(arr.getQuestion());
				formBean.setFirstAnswer(arr.getFirstAnswer());
				formBean.setSecondAnswer(arr.getSecondAnswer());
				formBean.setThirdAnswer(arr.getThirdAnswer());
				formBean.setFourthAnswer(arr.getFourthAnswer());
				forward=mapping.findForward("csrQuizFourPage");
			}
			else
			{
				forward=mapping.findForward("csrBriefPage");
			}
			
		}
			else if(questionDto.size()>0)
		{
				formBean.setUser_login_id(sessionUserBean.getUserLoginId());
				formBean.setUdId(sessionUserBean.getUdId());
				formBean.setCircle_Id(sessionUserBean.getCircleId());
				formBean.setElementId(Integer.parseInt(sessionUserBean.getElementId()));	
				formBean.setNewAnswer(briefingService.getNewQuestions(formBean));
				
				if (formBean.getNewAnswer()== "" || formBean.getNewAnswer()==null) {
					result=briefingService.insertQuizResult(formBean);
				
					if (questionDto.size()>3)
					{
						arr=questionDto.get(3);
				formBean.setQuestionId(arr.getQuestionId());
				formBean.setQuestion(arr.getQuestion());
				formBean.setFirstAnswer(arr.getFirstAnswer());
				formBean.setSecondAnswer(arr.getSecondAnswer());
				formBean.setThirdAnswer(arr.getThirdAnswer());
				formBean.setFourthAnswer(arr.getFourthAnswer());
				forward=mapping.findForward("csrQuizFourPage");
				}
					else 
					{	
					
						forward=mapping.findForward("csrBriefPage");
					}
					
					
				}
				
				else if(formBean.getNewAnswer()!="")
				{
					
					messages.add("msg1", new ActionMessage("quiz.submit1"));
					saveMessages(request, messages);
					arr=questionDto.get(3);
					formBean.setQuestionId(arr.getQuestionId());
					formBean.setQuestion(arr.getQuestion());
					formBean.setFirstAnswer(arr.getFirstAnswer());
					formBean.setSecondAnswer(arr.getSecondAnswer());
					formBean.setThirdAnswer(arr.getThirdAnswer());
					formBean.setFourthAnswer(arr.getFourthAnswer());
					forward=mapping.findForward("csrQuizNewThreePage");
				}
				
			else  if(result == questionDto.size())
			{
				messages.add("msg1", new ActionMessage("quiz.submit"));
				saveMessages(request, messages);
				forward=mapping.findForward("questionPage");
			}
			
			
			}
		else  if(result == questionDto.size())
		{
			messages.add("msg1", new ActionMessage("quiz.submit"));
			saveMessages(request, messages);
			forward=mapping.findForward("questionPage");
		}
		else 
		{	
		
			forward=mapping.findForward("csrBriefPage");
		}
		
		
}catch(Exception e){
			logger.error("Exception occured while updating the briefiysong");
		}
		
	return forward;
	}	
public ActionForward thirdSkip(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		HttpSession session = request.getSession();
		session = request.getSession(true);
		KmUserMstr sessionUserBean =
			(KmUserMstr) session.getAttribute("USER_INFO");
		KmBriefingMstrFormBean formBean = (KmBriefingMstrFormBean) form;
		 
		 KmBriefingMstrService briefingService = new KmBriefingMstrServiceImpl();
		 int result=0;
		 CSRQuestionDto arr;
		 ActionErrors errors = new ActionErrors();
			ActionMessages messages = new ActionMessages();
			ActionForward forward = new ActionForward(); // return value

	try{	
		
		formBean.setUser_login_id(sessionUserBean.getUserLoginId());
		formBean.setUdId(sessionUserBean.getUdId());
		formBean.setCircle_Id(sessionUserBean.getCircleId());
		formBean.setElementId(Integer.parseInt(sessionUserBean.getElementId()));
		
		result=briefingService.insertSkipQuizResult(formBean);
			formBean.setQuestions(briefingService.getQuestions(sessionUserBean));
		
		ArrayList<CSRQuestionDto> questionDto=new ArrayList<CSRQuestionDto>();
		questionDto=formBean.getQuestions();
		if(questionDto.size()>3){
			arr=questionDto.get(3);
			formBean.setQuestionId(arr.getQuestionId());
			
			formBean.setQuestion(arr.getQuestion());
			formBean.setFirstAnswer(arr.getFirstAnswer());
			formBean.setSecondAnswer(arr.getSecondAnswer());
			formBean.setThirdAnswer(arr.getThirdAnswer());
			formBean.setFourthAnswer(arr.getFourthAnswer());
			forward=mapping.findForward("csrQuizFourPage");
			}
			else
			{
					forward=mapping.findForward("csrBriefPage");
			}
			
		
		
		
}catch(Exception e){
			logger.error("Exception occured while updating the briefiysong");
		}
		
	return forward;
	}	

public ActionForward fourthQuestion(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		HttpSession session = request.getSession();
		session = request.getSession(true);
		KmUserMstr sessionUserBean =
			(KmUserMstr) session.getAttribute("USER_INFO");
		KmBriefingMstrFormBean formBean = (KmBriefingMstrFormBean) form;
		 
		 KmBriefingMstrService briefingService = new KmBriefingMstrServiceImpl();
		 int result=0;
		 CSRQuestionDto arr;
		 ActionErrors errors = new ActionErrors();
			ActionMessages messages = new ActionMessages();
			ActionForward forward = new ActionForward(); // return value
			ArrayList<CSRQuestionDto> questionDto=new ArrayList<CSRQuestionDto>();
			ArrayList<CSRQuestionDto> questionDto1=new ArrayList<CSRQuestionDto>();
			ArrayList<CSRQuestionDto> questionDto2=new ArrayList<CSRQuestionDto>();
			int skipResult=0;
			int questionResult=0;
		
	try{	
		//for Questions
		formBean.setQuestions(briefingService.getQuestions(sessionUserBean));
		questionDto=formBean.getQuestions();
		//questionDto1=briefingService.getskipQuestion(sessionUserBean);
		skipResult=briefingService.getskipQuesize(sessionUserBean);
		/*if(skipResult >0)
		{

			formBean.setUser_login_id(sessionUserBean.getUserLoginId());
			formBean.setUdId(sessionUserBean.getUdId());
			formBean.setCircle_Id(sessionUserBean.getCircleId());
			briefingService.insertSkipQuestions(formBean);
		}*/
		if (skipResult>4){
			
			
			formBean.setSkipQuestions(briefingService.getSkipQuestions(sessionUserBean));
			
			questionDto1=formBean.getSkipQuestions();
			if(questionDto1.size()==5)
			{
				arr=questionDto1.get(4);
				formBean.setQuestionId(arr.getQuestionId());
				formBean.setQuestion(arr.getQuestion());
				formBean.setFirstAnswer(arr.getFirstAnswer());
				formBean.setSecondAnswer(arr.getSecondAnswer());
				formBean.setThirdAnswer(arr.getThirdAnswer());
				formBean.setFourthAnswer(arr.getFourthAnswer());
				forward=mapping.findForward("csrQuizFivePage");
			}
			else
			{
				//messages.add("msg1", new ActionMessage("questions.failure"));
				//saveMessages(request, messages);
				forward=mapping.findForward("csrBriefPage");
			}
			
		}
			else if(questionDto.size()>0)
		{
				formBean.setUser_login_id(sessionUserBean.getUserLoginId());
				formBean.setUdId(sessionUserBean.getUdId());
				formBean.setCircle_Id(sessionUserBean.getCircleId());
				formBean.setElementId(Integer.parseInt(sessionUserBean.getElementId()));	
				formBean.setNewAnswer(briefingService.getNewQuestions(formBean));
				
				if (formBean.getNewAnswer()== "" || formBean.getNewAnswer()==null) {
					result=briefingService.insertQuizResult(formBean);
				
					if (questionDto.size()==5)
					{
						arr=questionDto.get(4);
				formBean.setQuestionId(arr.getQuestionId());
				formBean.setQuestion(arr.getQuestion());
				formBean.setFirstAnswer(arr.getFirstAnswer());
				formBean.setSecondAnswer(arr.getSecondAnswer());
				formBean.setThirdAnswer(arr.getThirdAnswer());
				formBean.setFourthAnswer(arr.getFourthAnswer());
				forward=mapping.findForward("csrQuizFivePage");
				}
					else 
					{	
					
						forward=mapping.findForward("csrBriefPage");
					}
					
					
				}
				
				else if(formBean.getNewAnswer()!="")
				{
					
					messages.add("msg1", new ActionMessage("quiz.submit1"));
					saveMessages(request, messages);
					arr=questionDto.get(4);
					formBean.setQuestionId(arr.getQuestionId());
					formBean.setQuestion(arr.getQuestion());
					formBean.setFirstAnswer(arr.getFirstAnswer());
					formBean.setSecondAnswer(arr.getSecondAnswer());
					formBean.setThirdAnswer(arr.getThirdAnswer());
					formBean.setFourthAnswer(arr.getFourthAnswer());
					forward=mapping.findForward("csrQuizNewFourPage");
				}
				
			else  if(result == questionDto.size())
			{
				messages.add("msg1", new ActionMessage("quiz.submit"));
				saveMessages(request, messages);
				forward=mapping.findForward("questionPage");
			}
			
			
			}
		else  if(result >=5)
		{
			messages.add("msg1", new ActionMessage("quiz.submit"));
			saveMessages(request, messages);
			forward=mapping.findForward("questionPage");
		}
		else 
		{	
		
			forward=mapping.findForward("csrBriefPage");
		}
		
		
}catch(Exception e){
			logger.error("Exception occured while updating the briefiysong");
		}
		
	return forward;
	}	

public ActionForward fourthSkip(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		HttpSession session = request.getSession();
		session = request.getSession(true);
		KmUserMstr sessionUserBean =
			(KmUserMstr) session.getAttribute("USER_INFO");
		KmBriefingMstrFormBean formBean = (KmBriefingMstrFormBean) form;
		 
		 KmBriefingMstrService briefingService = new KmBriefingMstrServiceImpl();
		 int result=0;
		 CSRQuestionDto arr;
		 ActionErrors errors = new ActionErrors();
			ActionMessages messages = new ActionMessages();
			ActionForward forward = new ActionForward(); // return value

	try{	
		
		formBean.setUser_login_id(sessionUserBean.getUserLoginId());
		formBean.setUdId(sessionUserBean.getUdId());
		formBean.setCircle_Id(sessionUserBean.getCircleId());
		formBean.setElementId(Integer.parseInt(sessionUserBean.getElementId()));
		result=briefingService.insertSkipQuizResult(formBean);
			formBean.setQuestions(briefingService.getQuestions(sessionUserBean));
		
		ArrayList<CSRQuestionDto> questionDto=new ArrayList<CSRQuestionDto>();
		questionDto=formBean.getQuestions();
		
		if(questionDto.size()==5){
			arr=questionDto.get(4);
			formBean.setQuestionId(arr.getQuestionId());
			
			formBean.setQuestion(arr.getQuestion());
			formBean.setFirstAnswer(arr.getFirstAnswer());
			formBean.setSecondAnswer(arr.getSecondAnswer());
			formBean.setThirdAnswer(arr.getThirdAnswer());
			formBean.setFourthAnswer(arr.getFourthAnswer());
			forward=mapping.findForward("csrQuizFivePage");
			}
			else
			{
					forward=mapping.findForward("csrBriefPage");
			}
		
		
		
}catch(Exception e){
			logger.error("Exception occured while updating the briefiysong");
		}
		
	return forward;
	}	



public ActionForward fiveQuestion(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		HttpSession session = request.getSession();
		session = request.getSession(true);
		KmUserMstr sessionUserBean =
			(KmUserMstr) session.getAttribute("USER_INFO");
		KmBriefingMstrFormBean formBean = (KmBriefingMstrFormBean) form;
		 
		 KmBriefingMstrService briefingService = new KmBriefingMstrServiceImpl();
		 int result=0;
		 CSRQuestionDto arr;
	try{	
		
			formBean.setUser_login_id(sessionUserBean.getUserLoginId());
		formBean.setUdId(sessionUserBean.getUdId());
		formBean.setCircle_Id(sessionUserBean.getCircleId());
		formBean.setElementId(Integer.parseInt(sessionUserBean.getElementId()));
		result=briefingService.insertQuizResult(formBean);
			
		
}catch(Exception e){
			logger.error("Exception occured while updating the briefiysong");
		}
		
	return mapping.findForward("csrBriefPage");
	}	

public ActionForward fiveSkip(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		HttpSession session = request.getSession();
		session = request.getSession(true);
		KmUserMstr sessionUserBean =
			(KmUserMstr) session.getAttribute("USER_INFO");
		KmBriefingMstrFormBean formBean = (KmBriefingMstrFormBean) form;
		 
		 KmBriefingMstrService briefingService = new KmBriefingMstrServiceImpl();
		 int result=0;
		 CSRQuestionDto arr;
	try{	
		
		formBean.setUser_login_id(sessionUserBean.getUserLoginId());
		formBean.setUdId(sessionUserBean.getUdId());
		formBean.setCircle_Id(sessionUserBean.getCircleId());
		formBean.setElementId(Integer.parseInt(sessionUserBean.getElementId()));
		result=briefingService.insertSkipQuizResult(formBean);
		
		
}catch(Exception e){
			logger.error("Exception occured while updating the briefiysong");
		}
		
	return mapping.findForward("csrBriefPage");
	}	



}
