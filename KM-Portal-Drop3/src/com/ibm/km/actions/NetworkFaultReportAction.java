package com.ibm.km.actions;

/**
 * @author Ajil Chandran
 * Created On  02 Dec 2010
 */
import java.io.IOException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import org.json.JSONObject;

import com.ibm.km.common.PropertyReader;

import com.ibm.km.dto.KmElementMstr;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.dto.NetworkFaultReportDto;
import com.ibm.km.exception.KmException;

import com.ibm.km.forms.NetworkFaultReportForm;
import com.ibm.km.services.KmElementMstrService;

import com.ibm.km.services.NetworkFaultReportService;
import com.ibm.km.services.impl.KmElementMstrServiceImpl;

import com.ibm.km.services.impl.NetworkFaultReportServiceImpl;

public class NetworkFaultReportAction extends DispatchAction {
	/**
	 * Logger for the class.
	 */
	private static final Logger logger;

	static {

		logger = Logger.getLogger(NetworkFaultReportAction.class.getName());
	}
	private static String INIT_NETWORK_REPORT = "initNetworkReport";
	private static String EXPORT_NETWORK_EXCEL_REPORT = "networkFaultExcelReport";
	
	private static String INIT_IMPACT_REPORT = "initImpactReport";
	private static String EXPORT_IMPACT_EXCEL_REPORT = "networkImpactExcelReport";

	/**
	 * Initailizes the Network Fault Report Page depending upon the type of user
	 * (Super_admin or Circle Admin)
	 */
	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		NetworkFaultReportForm formBean = (NetworkFaultReportForm) form;
		////System.out.println("inside init method of NetworkFaultReportAction");
		KmElementMstrService kmElementService = new KmElementMstrServiceImpl();
		NetworkFaultReportService service = new NetworkFaultReportServiceImpl();
		HttpSession session = request.getSession();
		KmUserMstr userBean = (KmUserMstr) session.getAttribute("USER_INFO");
		int circleId = 0;
		try {
			logger
					.info(userBean.getUserLoginId()
							+ " Entered into the init method of NetworkFaultReportAction");
			if (formBean.getMethodName().equals("init")) {
				session.setAttribute("FAULT_REPORT", null);
				circleId = Integer.parseInt(userBean.getElementId().trim());
				formBean.setInitialSelectBox("-1");
				formBean.setReportType("0");
				formBean.setDate("");
				
			}
			formBean.setInitialSelectBox("-1");
				List firstDropDown;
				firstDropDown = kmElementService.getChildren(userBean
						.getElementId());
				if (firstDropDown != null && firstDropDown.size() != 0) {
					formBean.setInitialLevel(((KmElementMstr) firstDropDown
							.get(0)).getElementLevel());
				} else {

					int initialLevel = Integer.parseInt(kmElementService
							.getElementLevelId(userBean.getElementId()));
					initialLevel++;
					formBean.setInitialLevel(initialLevel + "");
				}
				formBean.setParentId(userBean.getElementId());

				request.setAttribute("elementLevelNames", kmElementService
						.getAllElementLevelNames());
				request.setAttribute("allParentIdString", kmElementService
						.getAllParentIdString("1", userBean.getElementId()));
				request.setAttribute("firstList", firstDropDown);
				request.setAttribute("superAdmin", "superadmin");
			
			if (userBean.getKmActorId().equals(
					PropertyReader.getAppValue("CircleAdmin"))||userBean.getKmActorId().equals(
							PropertyReader.getAppValue("TSGUser"))) {
					formBean.setParentId(userBean.getElementId());
					formBean.setInitialLevel("9");
				/**
				 *  InitialLevel is set into junk value (9) which is used to make the LOB,CIRCLE DropDownList invisible for Circle Admin 
				 */
					request.setAttribute("superAdmin", null);
			}

			forward = mapping.findForward(INIT_NETWORK_REPORT);

		} catch (KmException e) {

			logger.error("Exception occured while fetching the dropdown in init method");

		} catch (Exception e) {
			logger
					.error("Exception occured while initializing the init method in NetworkFaultReportAction");
		}

		return forward;
	}

	public ActionForward networkFaultExcelReport(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger
				.info(" Entered into the networkFaultExcelReport method of NetworkFaultReportAction");
		////System.out.println("inside excel report");
		ActionForward forward = mapping
				.findForward(EXPORT_NETWORK_EXCEL_REPORT);
		try {

			HttpSession session = request.getSession();
			KmUserMstr userBean = (KmUserMstr) session.getAttribute("USER_INFO");
			List faultReport = (List) session.getAttribute("FAULT_REPORT");
			request.setAttribute("FAULT_REPORT", session
					.getAttribute("FAULT_REPORT"));
			if (userBean.getKmActorId().equals(
					PropertyReader.getAppValue("CircleAdmin"))
					|| userBean.getKmActorId().equals(
							PropertyReader.getAppValue("TSGUser"))) {
				request.setAttribute("superAdmin", null);
			} else {
				request.setAttribute("superAdmin", "superadmin");
			}
		} catch (Exception e) {
			logger
					.error("Exception occured while creating Excel Report NetworkFaultReportAction");
		}

		return forward;
	}

	public ActionForward fetchNetworkReport(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// ActionErrors errors = new ActionErrors();

		NetworkFaultReportForm networkFaultReportForm = (NetworkFaultReportForm) form;
		NetworkFaultReportService service = new NetworkFaultReportServiceImpl();
		HttpSession session = request.getSession();
		KmUserMstr sessionUserBean = (KmUserMstr) session
				.getAttribute("USER_INFO");
		KmElementMstrService elementService = new KmElementMstrServiceImpl();
		NetworkFaultReportDto dto = new NetworkFaultReportDto();
		List faultReport = null;
		List circleList = new ArrayList();
		String[] circles = null;
		// ArrayList userList=null;
		try {
			logger
					.info(" Entered into the fetchNetworkReport method of NetworkFaultReportAction");

			
			BeanUtils.copyProperties(dto, networkFaultReportForm);
			if (networkFaultReportForm.getElementLevel().equals("3")) {
				/**
				 * Search on Selected Circles under LOB
				 */
				circles = networkFaultReportForm.getCircles().split(",");
				faultReport = service.fetchNetworkFaultReportAll(circles, dto);
				
			} else if (networkFaultReportForm.getElementLevel().equals("1")) {
				/**
				 * search on all circles
				 */
				faultReport = service.fetchNetworkFaultReportAll(circles, dto);
				
			} else if (networkFaultReportForm.getElementLevel().equals("2")) {
				/**
				 * search on All circles under a LOB
				 */
				circleList = elementService.getChildrenIds(
						networkFaultReportForm.getParentId(), "3");
				circles = (String[]) circleList.toArray(new String[circleList
						.size()]);
				faultReport = service.fetchNetworkFaultReportAll(circles, dto);
			}

			else if (networkFaultReportForm.getElementLevel().equals("8")) {
				/**
				 * user is circle admin
				 */
				
				String[] circle = { networkFaultReportForm.getParentId() };
				dto.setElementLevel("2"); 
				faultReport = service.fetchNetworkFaultReportAll(circle, dto);

			}

			networkFaultReportForm.setElementId(networkFaultReportForm
					.getParentId());
			session.setAttribute("FAULT_REPORT", faultReport);

			String elementId = networkFaultReportForm.getParentId();
			String date = networkFaultReportForm.getDate();
			String path = "";
			path = elementService.getAllParentNameString(sessionUserBean
					.getElementId(), elementId);
			if(networkFaultReportForm.getElementLevel().equals("3")){
				path=path.substring(0, path.lastIndexOf(">"));
			}
			
			
			networkFaultReportForm.setElementPath(path);
			if (networkFaultReportForm.getReportType().equals("1")) {
				networkFaultReportForm.setReportName("Daily Report :  " + date);
			} else {
				Date currentDate = Calendar.getInstance().getTime();
				DateFormat formatter = new SimpleDateFormat("MMM-yyyy");
				String   mtdDate= formatter.format(currentDate);    
				networkFaultReportForm.setReportName("MTD Report :  "+mtdDate );
				
			}
			
			//networkFaultReportForm.setInitialLevel("");

		} catch (KmException e) {

			logger
					.error("Exception occured while fetching Network Fault Report ");

		} catch (Exception e) {
			logger
					.error("Exception occured while executing fetchNetworkReport method in NetworkFaultReportAction");

		}

		return init(mapping, networkFaultReportForm, request, response);
	}

	public ActionForward loadElementDropDown(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String parentId = request.getParameter("ParentId");
		KmElementMstrService kmElementService = new KmElementMstrServiceImpl();

		try {
			JSONObject json = kmElementService.getElementsAsJsonNoPan(parentId);
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
	 * Initailizes the Network Impact Report Page depending upon the type of user
	 * (Super_admin or Circle Admin)
	 */
	public ActionForward initImpactReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward forward = null;
		NetworkFaultReportForm formBean = (NetworkFaultReportForm) form;
		
		KmElementMstrService kmElementService = new KmElementMstrServiceImpl();
	NetworkFaultReportService service = new NetworkFaultReportServiceImpl();
		HttpSession session = request.getSession();
		KmUserMstr userBean = (KmUserMstr) session.getAttribute("USER_INFO");
		int circleId = 0;
		try {logger	.info(userBean.getUserLoginId()
							+ " Entered into the initImpactReport method of NetworkFaultReportAction");

		
		
		
		
		if (formBean.getMethodName().equals("initImpactReport")) {
				session.setAttribute("IMPACT_REPORT", null);
				circleId = Integer.parseInt(userBean.getElementId().trim());
				formBean.setInitialSelectBox("-1");
				formBean.setReportType("0");
				formBean.setDate("");
			}
			formBean.setInitialSelectBox("-1");

			{	List firstDropDown;
				firstDropDown = kmElementService.getChildren(userBean
						.getElementId());
				if (firstDropDown != null && firstDropDown.size() != 0) {
					formBean.setInitialLevel(((KmElementMstr) firstDropDown
							.get(0)).getElementLevel());
				} else {
					int initialLevel = Integer.parseInt(kmElementService
							.getElementLevelId(userBean.getElementId()));
					initialLevel++;
					formBean.setInitialLevel(initialLevel + "");
				}
				formBean.setParentId(userBean.getElementId());
				request.setAttribute("elementLevelNames", kmElementService
						.getAllElementLevelNames());
				request.setAttribute("allParentIdString", kmElementService
						.getAllParentIdString("1", userBean.getElementId()));
				request.setAttribute("firstList", firstDropDown);
				request.setAttribute("superAdmin", "superadmin");
			}
					
			if (userBean.getKmActorId().equals(
					PropertyReader.getAppValue("CircleAdmin"))||userBean.getKmActorId().equals(
							PropertyReader.getAppValue("TSGUser"))) {
					formBean.setParentId(userBean.getElementId());
					formBean.setInitialLevel("9");
				/**
				 *  InitialLevel is set into junk value (9) which is used to make the LOB,CIRCLE DropDownList invisible for Circle Admin and TSGUser
				 */
					request.setAttribute("superAdmin", null);
			}
			

			forward = mapping.findForward(INIT_IMPACT_REPORT);

		} catch (KmException e) {

			logger.error("Exception occured while fetching the dropdown in initImpactReport method");

		} catch (Exception e) {
			logger		.error("Exception occured while initializing the initImpactReport method in NetworkFaultReportAction");
		}

		return forward;
	}
	public ActionForward fetchImpactReport(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// ActionErrors errors = new ActionErrors();

		NetworkFaultReportForm networkFaultReportForm = (NetworkFaultReportForm) form;
		NetworkFaultReportService service = new NetworkFaultReportServiceImpl();
		HttpSession session = request.getSession();
		KmUserMstr sessionUserBean = (KmUserMstr) session.getAttribute("USER_INFO");
		KmElementMstrService elementService = new KmElementMstrServiceImpl();
		NetworkFaultReportDto dto = new NetworkFaultReportDto();
		List faultReport = null;
		List circleList = new ArrayList();
		String[] circles = null;
		// ArrayList userList=null;
		try {
			logger.info(" Entered into the fetchImpactReport method of NetworkFaultReportAction");
		
			BeanUtils.copyProperties(dto, networkFaultReportForm);
			if (networkFaultReportForm.getElementLevel().equals("3")) {
				/**
				 * search on selected circles inside a single LOB
				 */ 
				circles = networkFaultReportForm.getCircles().split(",");
				faultReport = service.fetchImpactReportAll(circles, dto);
		
			} else if (networkFaultReportForm.getElementLevel().equals("1")) {

				/**
				 * search on all circles
				 */
				faultReport = service.fetchImpactReportAll(circles, dto);
				
			} else if (networkFaultReportForm.getElementLevel().equals("2")) {

				/**
				 * search on all circle under a LOB
				 */
				circleList = elementService.getChildrenIds(
						networkFaultReportForm.getParentId(), "3");
				circles = (String[]) circleList.toArray(new String[circleList.size()]);
				faultReport = service.fetchImpactReportAll(circles, dto);
			}

			else if (networkFaultReportForm.getElementLevel().equals("8")) {
				/**
				 * User is Circle Admin , search on his circle ..  circle id is his parent id 
				 */
				String[] circle = { networkFaultReportForm.getParentId() };
				dto.setElementLevel("2");
				faultReport = service.fetchImpactReportAll(circle, dto);

			}

			networkFaultReportForm.setElementId(networkFaultReportForm
					.getParentId());
			session.setAttribute("IMPACT_REPORT", faultReport);

			String elementId = networkFaultReportForm.getParentId();
			String date = networkFaultReportForm.getDate();
			String path = "";
			path = elementService.getAllParentNameString(sessionUserBean
					.getElementId(), elementId);
			if(networkFaultReportForm.getElementLevel().equals("3")){
				path=path.substring(0, path.lastIndexOf(">"));
			}
			networkFaultReportForm.setElementPath(path);
			if (networkFaultReportForm.getReportType().equals("1")) {
				networkFaultReportForm.setReportName("Daily Report :  " + date);
			} else {
				Date currentDate = Calendar.getInstance().getTime();
				DateFormat formatter = new SimpleDateFormat("MMM-yyyy");
				String   mtdDate= formatter.format(currentDate);    
				networkFaultReportForm.setReportName("MTD Report :  "+mtdDate );
			
			}
			
			networkFaultReportForm.setInitialLevel("");

		} catch (KmException e) {

			logger
					.error("Exception occured while fetching Network Impact Report ");

		} catch (Exception e) {
			logger
					.error("Exception occured while executing fetchImpactReport method in NetworkFaultReportAction");

		}

		return initImpactReport(mapping, networkFaultReportForm, request, response);
	}

	public ActionForward networkImpactExcelReport(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		ActionForward forward = mapping	.findForward(EXPORT_IMPACT_EXCEL_REPORT);
		try {logger
			.info(" Entered into the networkImpactExcelReport method of NetworkFaultReportAction");
			HttpSession session = request.getSession();
			request.setAttribute("IMPACT_REPORT", session.getAttribute("IMPACT_REPORT"));
		} catch (Exception e) {
			logger.error("Exception occured while creating Impact Excel Report NetworkFaultReportAction");
		}

		return forward;
	}
	
	
}
