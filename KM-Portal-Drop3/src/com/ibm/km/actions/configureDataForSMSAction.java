package com.ibm.km.actions;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;

import com.ibm.km.dto.KmConfigureDataForSMSDto;
import com.ibm.km.dto.KmUserMstr;

import com.ibm.km.forms.configureSMSDataFormBean;
import com.ibm.km.services.KmConfigureSMSDataService;

import com.ibm.km.services.impl.KmConfigureSMSDataServiceImpl;


public class configureDataForSMSAction extends DispatchAction {
	private static Logger logger = Logger
			.getLogger(configureDataForSMSAction.class.getName());

	public ActionForward initConfigureData(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ActionForward forward = new ActionForward(); // return value
		KmConfigureSMSDataService configureSMSDataService = new KmConfigureSMSDataServiceImpl();
		KmUserMstr userBean = (KmUserMstr) request.getSession().getAttribute(
				"USER_INFO");
		HttpSession session = request.getSession();
		configureSMSDataFormBean formBean = (configureSMSDataFormBean)form;
		logger.debug("configuring....");
		ArrayList<KmConfigureDataForSMSDto> docTypeList = new ArrayList<KmConfigureDataForSMSDto>();
		docTypeList = configureSMSDataService.getDocTypeList();
		session.setAttribute("docTypeList", docTypeList);
		formBean.setDocTypeList(docTypeList);
		return mapping.findForward("configure");
	}

	public ActionForward fetchColumnForSMS(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		logger.info("fetchColumnForSMS....");
		ActionForward forward = new ActionForward(); // return value
		KmUserMstr userBean = (KmUserMstr) request.getSession().getAttribute("USER_INFO");
		configureSMSDataFormBean formBean = (configureSMSDataFormBean)form;
		KmConfigureDataForSMSDto  dto = new KmConfigureDataForSMSDto();
		HttpSession session = request.getSession();
		KmConfigureSMSDataService configureSMSDataService = new KmConfigureSMSDataServiceImpl();
		
		String selectedVal = formBean.getSelectValue();
		logger.info("Selected Value = "+selectedVal);
		String tableName = "";
		if(selectedVal.equals("Distributor Details"))
			tableName = "KM_DIST_DETAILS";
		else if(selectedVal.equals("ARC Details"))
			tableName="KM_ARC_DETAILS";
		else if(selectedVal.equals("Coordinator Details"))
			tableName = "KM_COORDINATOR_DETAILS";
		else if(selectedVal.equals("Other SMS"))
			tableName = "KM_VAS_DETAIL";
		
		logger.info("Data to be fetched from :: "+tableName);
		ArrayList<KmConfigureDataForSMSDto> columnList = new ArrayList<KmConfigureDataForSMSDto>();
		columnList = (ArrayList<KmConfigureDataForSMSDto>)configureSMSDataService.columnListForTable(tableName);
		request.setAttribute("columnList", columnList);
		formBean.setColumnList(columnList);
		//dto.setColumnList(columnList);
		for(int x = 0; x < columnList.size(); x++){
			logger.info(columnList.get(x).getColumnName()+" "+columnList.get(x).getIsConfigurable());
		}
		
		formBean.setSelectValue(tableName);
		formBean.setDocTypeList((ArrayList<KmConfigureDataForSMSDto>) session.getAttribute("docTypeList"));
		
		return mapping.findForward("success");
	}
	
	public ActionForward updateConfigurableColumnsForSMS(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ActionForward forward = new ActionForward(); // return value
		logger.debug("updateConfigurableColumnsForSMS....");
		
		ActionMessages messages = new ActionMessages();
		configureSMSDataFormBean formBean = (configureSMSDataFormBean)form;
		KmConfigureSMSDataService configureSMSDataService = new KmConfigureSMSDataServiceImpl();
		String tableName = formBean.getSelectValue();
		//logger.info("ID :"+formBean.getIdFlag());
		String idFlag = request.getParameter("idFlag");
		logger.info(idFlag);
		logger.info("table Name :::: "+formBean.getSelectValue());
		Integer status = configureSMSDataService.updateConfigurableColumnsForSMS(idFlag, tableName);
		if(status == 1)
			messages.add("msg",new ActionMessage("km.sms.configurable.column.update.success"));
		else
			messages.add("msg",new ActionMessage("km.sms.configurable.column.update.failure"));
		saveMessages(request,messages);
		return mapping.findForward("updateSuccess");
	}
}
