/*
 * Created on Nov 26, 2010
 *
 */
package com.ibm.km.actions;

import java.lang.reflect.InvocationTargetException;

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

import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.dto.NetworkErrorLogDto;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.KmNetworkErrorFormBean;
import com.ibm.km.networkfault.IndexLocation;
import com.ibm.km.services.KmNetworkErrorLogService;
import com.ibm.km.services.impl.KmNetworkErrorLogServiceImpl;
import com.ibm.km.sms.SendSMSXML;

/**
 * KmUserMstrAction : This action class receives input from TSG user
 * and inseart into database, index all the locations into index file
 * and send SMSs to configured CSDs. 
 * 
 * @author Kundan
 * 
 */
public class KmNetworkErrorAction extends DispatchAction implements Runnable {

	/**
	 * Logger for the class.
	 */
	private static final Logger logger;
	String csdUsers="";
	String smsText="";
	String circleKey="";
	static {

		logger = Logger.getLogger(KmNetworkErrorAction.class);
	}

	/* Local Variables */
	private static String INITLOADFORM = "initLoadForm";

	public KmNetworkErrorAction() {
	}

	static int loginCounter = 0;

	/**
	 * Initailizes the Create User page. For SuperAdmin uploads all the circles
	 * and for CircleApprover shows the different user types
	 */
	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionForward forward = new ActionForward(); // return value
		HttpSession session = request.getSession();
		KmNetworkErrorFormBean kmNetworkErrorFormBean = (KmNetworkErrorFormBean) form;

		KmUserMstr sessionUserBean = (KmUserMstr) session
				.getAttribute("USER_INFO");

		logger.info(sessionUserBean.getUserLoginId()
				+ " Entered into the init method of KmNetworkErrorAction");
		kmNetworkErrorFormBean.reset(mapping, request);
		forward = mapping.findForward(INITLOADFORM);
		return forward;
	}

	/**
	 * Creates new users. For SuperAdmin user CircleApprovers can be created.
	 * For CircleApprovers Circle users or CSRs
	 */
	public ActionForward insert(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionForward forward = new ActionForward(); // return value
		HttpSession session = request.getSession();
		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();
		KmNetworkErrorFormBean kmNetworkErrorFormBean = (KmNetworkErrorFormBean) form;
		KmUserMstr sessionUserBean = (KmUserMstr) session
				.getAttribute("USER_INFO");

		KmNetworkErrorLogService kmNetworkErrorLogService = new KmNetworkErrorLogServiceImpl();

		NetworkErrorLogDto networkErrorLogDto = new NetworkErrorLogDto();
		logger.info(sessionUserBean.getUserLoginId()
				+ " Entered into the insert method of KmNetworkErrorAction");

		// kmUserMstrFormBean.setCreatedBy(sessionUserBean.getUserId());

		// Setting the actor id for the user in the bean
		//System.out.println("prob :" + kmNetworkErrorFormBean.getProblemDesc()
			//	+ " area : " + kmNetworkErrorFormBean.getAreaAffected());
		try {
			String searchCharsProbDesc = "`~$^*=+><{}[]|=?'\"#_@!\\";
			String searchCharsAreaAfct = "`~$^*=+><{}[]|=?'\"#_@";
			boolean isValidInput = true;
			if ("".equals(kmNetworkErrorFormBean.getProblemDesc())
					|| hasSpecialChars(kmNetworkErrorFormBean.getProblemDesc(),
							searchCharsProbDesc)) {
				errors.add("errors.problem_desc_invalid", new ActionError(
						"errors.problem_desc_invalid"));
				isValidInput = false;
			}
			if ("".equals(kmNetworkErrorFormBean.getAreaAffected())
					|| hasSpecialChars(
							kmNetworkErrorFormBean.getAreaAffected(),
							searchCharsAreaAfct)) {
				errors.add("errors.area_affected_invalid", new ActionError(
						"errors.area_affected_invalid"));
				isValidInput = false;
			}
			if (!"".equals(kmNetworkErrorFormBean.getResoTATHH())) {
				if (!isInteger(kmNetworkErrorFormBean.getResoTATHH())) {
					errors.add("errors.resolutionTATHH", new ActionError(
							"errors.resolutionTATHH"));
					isValidInput = false;
				} else if (!(Integer.parseInt(kmNetworkErrorFormBean
						.getResoTATHH()) >= 0)) {
					errors.add("errors.resolutionTATHH", new ActionError(
							"errors.resolutionTATHH"));
					isValidInput = false;
				}
			}

			if (!"".equals(kmNetworkErrorFormBean.getResoTATMM())) {
				if (!isInteger(kmNetworkErrorFormBean.getResoTATMM())) {
					errors.add("errors.resolutionTATMM", new ActionError(
							"errors.resolutionTATMM"));
					isValidInput = false;
				} else if ((Integer.parseInt(kmNetworkErrorFormBean
						.getResoTATMM()) < 0 || Integer
						.parseInt(kmNetworkErrorFormBean.getResoTATMM()) >= 60)) {
					errors.add("errors.resolutionTATMM", new ActionError(
							"errors.resolutionTATMM"));
					isValidInput = false;
				}
			}

			if (!isValidInput) {
				saveErrors(request, errors);
				return mapping.findForward(INITLOADFORM);
			}

			networkErrorLogDto.setProblemDesc(kmNetworkErrorFormBean.getProblemDesc().trim());
			networkErrorLogDto.setAreaAffected(kmNetworkErrorFormBean.getAreaAffected().trim());

			if ("".equals(kmNetworkErrorFormBean.getResoTATHH())) {
				kmNetworkErrorFormBean.setResoTATHH("0");
			}
			if ("".equals(kmNetworkErrorFormBean.getResoTATMM())) {
				kmNetworkErrorFormBean.setResoTATMM("0");
			}
			networkErrorLogDto.setResoTATHH(kmNetworkErrorFormBean.getResoTATHH().trim());
			networkErrorLogDto.setResoTATMM(kmNetworkErrorFormBean.getResoTATMM().trim());
			networkErrorLogDto.setCircleID(Integer.parseInt(sessionUserBean.getElementId()));
			
			
			//System.out.println("Circle NAME : "+sessionUserBean.getElementName());
			networkErrorLogDto.setCircleName(sessionUserBean.getElementName());			
			networkErrorLogDto.setStatus("I");

			int logId = kmNetworkErrorLogService.insertNetworkErrorLogService(networkErrorLogDto);

			networkErrorLogDto.setProblemId(logId+"");
			
			if (logId > 0) {
				//System.out.println("rec inserted.");

				
				
				
				try {
						// writing index into index file.
					new IndexLocation().initNetworkFaultIndex(networkErrorLogDto, false);
				} catch (Exception e) {
					e.printStackTrace();
				}
				messages.add("msg1", new ActionMessage("networkFault.Logged"));
				saveMessages(request, messages);

				// Fetching CSD user for the circle 
				csdUsers = kmNetworkErrorLogService
						.selectCSDUsers(Integer.parseInt(sessionUserBean
								.getElementId()));

				if (!"".equals(csdUsers)) {
					if (networkErrorLogDto.getProblemDesc().lastIndexOf(".") == networkErrorLogDto
							.getProblemDesc().length() - 1) {
						networkErrorLogDto.setProblemDesc(networkErrorLogDto
								.getProblemDesc().substring(
										0,
										kmNetworkErrorFormBean.getProblemDesc()
												.length() - 1));
					}
					if (networkErrorLogDto.getAreaAffected().lastIndexOf(".") == networkErrorLogDto
							.getAreaAffected().length() - 1) {
						networkErrorLogDto
								.setAreaAffected(networkErrorLogDto
										.getAreaAffected().substring(
												0,
												kmNetworkErrorFormBean
														.getAreaAffected()
														.length() - 1));
					}

					

					// SMS text to be sent to CSD Users
					smsText = networkErrorLogDto.getProblemDesc()
							+ ". Areas Affected: "
							+ networkErrorLogDto.getAreaAffected() + ". TAT: "
							+ networkErrorLogDto.getResoTATHH() + "Hrs "
							+ networkErrorLogDto.getResoTATMM() + "Mins.";
					smsText = smsText.replace("&", "&amp;");
					
					logger.info("SMS to be sent : " + smsText);
					
					
					//new change starts
					//System.out.println("circleid...."+networkErrorLogDto.getCircleID());
					 circleKey=kmNetworkErrorLogService.getCircle(networkErrorLogDto.getCircleID());
					// new change ends
					
					//Sending SMSs to CSD users
					KmNetworkErrorAction smsSenderThread = new KmNetworkErrorAction(csdUsers,smsText,circleKey);
					new Thread(smsSenderThread).start();
				}
				kmNetworkErrorFormBean.reset(mapping, request);
			} else {
				//System.out.println("rec not inserted.");
				errors.add("errors.networkFaultLoggingFailed", new ActionError(
						"errors.networkFaultLoggingFailed"));
				saveErrors(request, errors);
			}

			forward = mapping.findForward(INITLOADFORM);
		} catch (Exception e) {
			e.printStackTrace();
			errors.add("errors.networkFaultLoggingFailed", new ActionError(
					"errors.networkFaultLoggingFailed"));
			saveErrors(request, errors);
			if (e instanceof IllegalAccessException) {
				logger
						.error("createUser() method : caught IllegalAccessException Exception while using BeanUtils.copyProperties()");
			}
			if (e instanceof InvocationTargetException) {
				logger
						.error("createUser() method : caught InvocationTargetException Exception while using BeanUtils.copyProperties()");
			}
			if (e instanceof KmException) {

				logger
						.error("createUser() method : caught KmException Exception while logging Network Fault");
			}
			logger.error("Exception From KmNetworkErrorAction : " + e);

			forward = mapping.findForward(INITLOADFORM);

		}
		return (forward);

	}
	
	public KmNetworkErrorAction(String csdUsers, String smsText,String circleKey)
	{
		this.csdUsers = csdUsers;
		this.smsText = smsText;
		this.circleKey = circleKey;
	}
	
	// Sending SMS on seperate thread.
	public void run()
	{
		SendSMSXML sendSMSXml = new SendSMSXML(); 
		String mobileNos[] = csdUsers.split(",");
		String cirKey=circleKey;
		logger.info("Sending SMSs to : " + csdUsers);
		for(int ii=0;ii<mobileNos.length; ii++)
		{
			if(!"".equals(mobileNos[ii].trim()))
			{
			sendSMSXml.sendSms(mobileNos[ii],smsText,cirKey);
			}
		}
		logger.info("SMS sent to : " + csdUsers);
	}

	private boolean hasSpecialChars(String userInput, String specialChars) {
		boolean flag = false;
		// String searchCharsProbDesc="`~$^*=+><{}[]|=?'\"#_@!\\";
		for (int ii = 0; ii < specialChars.length(); ii++) {
			if (userInput.indexOf(specialChars.charAt(ii)) >= 0) {
				return true;
			}
		}
		return flag;
	}

	private boolean isInteger(String userInput) {

		String digits = "0123456789";
		for (int ii = 0; ii < userInput.length(); ii++) {
			if (digits.indexOf(userInput.charAt(ii)) < 0) {
				return false;
			}
		}
		return true;
	}

}
