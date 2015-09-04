
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

import com.ibm.km.dto.CSRQuestionDto;
import com.ibm.km.dto.KmBriefingMstr;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.dto.QuizReportDto;
import com.ibm.km.forms.KmBriefingMstrFormBean;
import com.ibm.km.forms.KmLoginFormBean;
import com.ibm.km.forms.KmQuizReportFormBean;
import com.ibm.km.services.KmBriefingMstrService;
import com.ibm.km.services.KmCircleMstrService;
import com.ibm.km.services.KmElementMstrService;
import com.ibm.km.services.impl.KmBriefingMstrServiceImpl;
import com.ibm.km.services.impl.KmCircleMstrServiceImpl;
import com.ibm.km.services.impl.KmElementMstrServiceImpl;
import com.ibm.km.common.Constants;

/**
 * @version 	1.0
 * @author		Anil
 */
public class KmQuizReportAction extends DispatchAction {

	/**
	 * Logger for the class.
	**/
	private static final Logger logger;
	
	

	static {

		logger = Logger.getLogger(KmQuizReportAction.class);
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
		
		KmQuizReportFormBean formBean =(KmQuizReportFormBean) form;
		KmUserMstr sessionUserBean =(KmUserMstr) request.getSession().getAttribute("USER_INFO");
		KmElementMstrService elementService = new KmElementMstrServiceImpl();
		ArrayList<QuizReportDto> reportList=new ArrayList<QuizReportDto>();
		 ActionErrors errors = new ActionErrors();
			ActionMessages messages = new ActionMessages();
			ActionForward forward = new ActionForward(); // return value
		try {
			
			reportList=elementService.getReport(sessionUserBean);
			if(reportList.size()>0)
			{
				request.setAttribute("REPORT_LIST",reportList);
			}
			else
			{
				messages.add("msg1", new ActionMessage("quiz.report"));
				saveMessages(request, messages);
			}
						
		} catch (Exception e) {

			logger.error("Exception occured in init method " + e.getMessage());

		}
		return mapping.findForward("initReportBriefing");
	}
	
	public ActionForward initReport(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
			
			KmQuizReportFormBean formBean =(KmQuizReportFormBean) form;
			KmUserMstr sessionUserBean =(KmUserMstr) request.getSession().getAttribute("USER_INFO");
			KmElementMstrService elementService = new KmElementMstrServiceImpl();
			ArrayList<QuizReportDto> reportList=new ArrayList<QuizReportDto>();
			 ActionErrors errors = new ActionErrors();
				ActionMessages messages = new ActionMessages();
				ActionForward forward = new ActionForward(); // return value

			try {

				reportList=elementService.getQuizReport();
				if(reportList.size()>0){
				request.setAttribute("QUIZ_REPORT",reportList);
				}
				else
				{
					messages.add("msg1", new ActionMessage("quiz.report"));
					saveMessages(request, messages);
				}
							
			} catch (Exception e) {

				logger.error("Exception occured in init method " + e.getMessage());

			}
			return mapping.findForward("initQuizReport");
		}
	
	public ActionForward importExcel(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
			
			KmQuizReportFormBean formBean =(KmQuizReportFormBean) form;
			KmUserMstr sessionUserBean =(KmUserMstr) request.getSession().getAttribute("USER_INFO");
			KmElementMstrService elementService = new KmElementMstrServiceImpl();
			ArrayList<QuizReportDto> reportList=new ArrayList<QuizReportDto>();
			 ActionErrors errors = new ActionErrors();
				ActionMessages messages = new ActionMessages();
				ActionForward forward = new ActionForward(); // return value

			try {

				reportList=elementService.getQuizReport();
				
				request.setAttribute("quizList",reportList);
				
				
							
			} catch (Exception e) {

				logger.error("Exception occured in init method " + e.getMessage());

			}
			
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("content-Disposition","attachment;filename=QuizReport.xls");
			
			return mapping.findForward("quizReportExcel");
		}


}

