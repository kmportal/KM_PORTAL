/*
 * Created on May 1, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.actions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.forms.*;
import com.ibm.km.services.KmDocumentViewsService;
import com.ibm.km.services.KmElementMstrService;
import com.ibm.km.services.KmFavoritesService;
import com.ibm.km.services.KmUserMstrService;
import com.ibm.km.services.impl.KmDocumentViewsServiceImpl;
import com.ibm.km.services.impl.KmElementMstrServiceImpl;
import com.ibm.km.services.impl.KmFavoritesServiceImpl;
import com.ibm.km.services.impl.KmUserMstrServiceImpl;

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
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.config.FormBeanConfig;

/**
 * @author varunagg
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class KmDocumentViewsAction extends DispatchAction {
	
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(KmDocumentViewsAction.class);
	}
	public ActionForward initDocumentViews(
				ActionMapping mapping,
				ActionForm form,
				HttpServletRequest request,
				HttpServletResponse response)
				throws Exception{
					KmFileReportFormBean formBean= (KmFileReportFormBean) form;
					formBean.setFromDate("");
					formBean.setToDate("");
					formBean.setShowFile("false");
					formBean.setStatus("init");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
							GregorianCalendar gc = new GregorianCalendar();
							GregorianCalendar gc1 = new GregorianCalendar();
							String currDate = sdf.format(new java.util.Date());
							currDate = currDate.substring(0,10);
							String lastmonth="";
							

							gc.add(GregorianCalendar.DATE, -29);
							Date date1 = gc.getTime();
							lastmonth  = sdf.format(date1);
							lastmonth = lastmonth.substring(0,10);
							//request.setAttribute("CURRENT_DT",currDate);
							//request.setAttribute("LAST_MONTH",lastmonth);
							formBean.setFromDate(currDate);
							formBean.setToDate(currDate);
							  
				return mapping.findForward("documentViews");	
					
				}
	
	public ActionForward showDocumentViews(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
				
		ActionErrors errors=new ActionErrors();
		KmDocumentViewsService documentViewsService=new KmDocumentViewsServiceImpl();
		HttpSession session = request.getSession();
		KmUserMstr sessionUserBean = (KmUserMstr) session.getAttribute("USER_INFO");
		KmFileReportFormBean formBean= (KmFileReportFormBean) form;
		KmElementMstrService elementService=new KmElementMstrServiceImpl();
		KmUserMstrService userService=new KmUserMstrServiceImpl();
		ArrayList circleList=new ArrayList();
		List documentViewsList = null;
		try{
			String circleId=(String) session.getAttribute("CURRENT_CIRCLE_ID");
			if(circleId==null){
				circleId=sessionUserBean.getElementId();
			}
			
			/* Changed by anil to show the most viewed documents for selected circle  */
			//String favCategoryId=userService.getFavCategory(sessionUserBean.getUserId());
			
		
		//	if(sessionUserBean.getKmActorId().equals(Constants.CIRCLE_CSR)){
				
				documentViewsList = documentViewsService.showDocumentViews(circleId,formBean.getFromDate(),formBean.getToDate());
		//	}
			
			
		/*	else if(sessionUserBean.getKmActorId().equals(Constants.CATEGORY_CSR)){
				if(favCategoryId==null||favCategoryId.equals("")){
					errors.add("",new ActionError("favCat.error"));
					saveErrors(request,errors);
					return mapping.findForward("documentViews");
				}
				documentViewsList = documentViewsService.showDocumentViews(favCategoryId,formBean.getFromDate(),formBean.getToDate());
			}*/
		//	documentViewsList = documentViewsService.showDocumentViews(sessionUserBean.getElementId());
			request.setAttribute("documentViewsList", documentViewsList);
			
			formBean.setStatus("");
			return mapping.findForward("documentViews");
		} catch(Exception e) {
			e.printStackTrace();
			logger.error("Exception occured while viewing documents :"+e);
			return mapping.findForward("documentViews");
		}
	}
}
