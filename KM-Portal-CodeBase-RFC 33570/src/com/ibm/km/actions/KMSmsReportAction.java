package com.ibm.km.actions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;

import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.dto.QuizReportDto;
import com.ibm.km.dto.SMSReportDTO;
import com.ibm.km.forms.KMSmsReportFormBean;
import com.ibm.km.forms.KmQuizReportFormBean;
import com.ibm.km.services.KmConfigureSMSDataService;
import com.ibm.km.services.KmElementMstrService;
import com.ibm.km.services.impl.KmConfigureSMSDataServiceImpl;
import com.ibm.km.services.impl.KmElementMstrServiceImpl;

public class KMSmsReportAction extends DispatchAction {
	private static final Logger logger;
	static {
		logger = Logger.getLogger(KMSmsReportAction.class);
	}

	public ActionForward getSmsReport(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		logger.info("Proceeding to retrieve SMS Report");
		KMSmsReportFormBean formBean = (KMSmsReportFormBean) form;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		
		String date = sdf.format(new java.util.Date());
		date = date.substring(0,10);		
		formBean.setFromDate(date);
		formBean.setToDate(date);
		return mapping.findForward("retrieveReport");
		
	}
	public ActionForward retrieveSMSReport(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		KMSmsReportFormBean formBean = (KMSmsReportFormBean) form;
		KmUserMstr sessionUserBean = (KmUserMstr) request.getSession()
				.getAttribute("USER_INFO");
		logger.info("In method retrieveSMSReport to retrieve SMS Report");
		KmConfigureSMSDataService kmConfigureSMSDataService = new KmConfigureSMSDataServiceImpl();
		ArrayList<SMSReportDTO> smsReportList = new ArrayList<SMSReportDTO>();
		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();
		ActionForward forward = new ActionForward(); // return value
		logger.info("Reports to be retrieved for Date Range ::: "+formBean.getFromDate()+" ### "+formBean.getToDate());
		try {

			smsReportList = kmConfigureSMSDataService.getSmsReport(formBean.getFromDate(), formBean.getToDate());
			if (smsReportList.size() > 0) {
				for (int x = 0; x < smsReportList.size(); x++)
					logger.info(smsReportList.get(x).getMobileNo() + " :: "
							+ smsReportList.get(x).getSmsContent()+" :: "+smsReportList.get(x).getSmsCategory());
				formBean.setSmsReportList(smsReportList);
				request.setAttribute("smsReportList", smsReportList);
				
			} else {
				formBean.setSmsReportList(null);
				request.setAttribute("smsReportList", null);
				messages.add("msg1", new ActionMessage("no.sms.report.found"));
				saveMessages(request, messages);
			}

		} catch (Exception e) {

			logger.error("Exception occured in getSmsReport method "
					+ e.getMessage());

		}
		return mapping.findForward("initSmsReport");
		
	}

	public ActionForward importExcel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		KMSmsReportFormBean formBean = (KMSmsReportFormBean) form;
		KmUserMstr sessionUserBean = (KmUserMstr) request.getSession()
				.getAttribute("USER_INFO");
		KmConfigureSMSDataService kmConfigureSMSDataService = new KmConfigureSMSDataServiceImpl();
		ArrayList<SMSReportDTO> smsReportList = new ArrayList<SMSReportDTO>();
		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();
		ActionForward forward = new ActionForward(); // return value
		logger.info("Reports to be retrieved for Date Range ::: "+formBean.getFromDate()+" #### "+formBean.getToDate());
		try {

			smsReportList = kmConfigureSMSDataService.getSmsReport(formBean.getFromDate(), formBean.getToDate());
			request.setAttribute("smsReportList", smsReportList);
		} catch (Exception e) {

			logger.error("Exception occured in getSmsReport method "
					+ e.getMessage());

		}

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("content-Disposition","attachment;filename=QuizReport.xls");
		
		return mapping.findForward("smsReportExcel");
	}
	

}
