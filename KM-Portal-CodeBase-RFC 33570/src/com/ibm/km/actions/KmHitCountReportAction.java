/*
 * Created on Nov 27, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.actions;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;

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

import com.ibm.km.dto.KmElementMstr;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.exception.KmException;
import com.ibm.km.services.KmElementMstrService;
import com.ibm.km.services.KmHitCountReportService;
import com.ibm.km.services.impl.KmElementMstrServiceImpl;
import com.ibm.km.services.impl.KmHitCountReportServiceImpl;

import com.ibm.km.forms.HitCountReportBean;
import com.ibm.km.forms.KmUserReportFormBean;
/**
 * @author Atul
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class KmHitCountReportAction extends DispatchAction {
	
	private static final Logger logger;
	
		static {
			logger = Logger.getLogger(KmHitCountReportAction.class);
		}
	public ActionForward hitCount(
					ActionMapping mapping,
					ActionForm form,
					HttpServletRequest request,
					HttpServletResponse response)
					throws Exception{
						
			HitCountReportBean formBean= (HitCountReportBean) form;
			KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
			ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
			KmElementMstrService kmElementService = new KmElementMstrServiceImpl();
					formBean.setInitialSelectBox("-1");
					List firstDropDown=null;
					try {		
						
						if (userBean.getKmActorId().equals(bundle.getString("LOBAdmin"))||userBean.getKmActorId().equals(bundle.getString("Superadmin"))) {
							firstDropDown = kmElementService.getAllChildren(userBean.getElementId());
						} 
						else {
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
						if(request.getParameter("methodName").equals("hitCount")){
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
							GregorianCalendar gc = new GregorianCalendar();
				        	String currDate = sdf.format(new java.util.Date());
							currDate = currDate.substring(0,10);
							formBean.setDate(currDate);
						}
						formBean.setParentId(userBean.getElementId());			
						request.setAttribute("elementLevelNames",kmElementService.getAllElementLevelNames());
						request.setAttribute("allParentIdString",kmElementService.getAllParentIdString("1",userBean.getElementId()));
						request.setAttribute("firstList",firstDropDown);

					} catch (KmException e) {
						logger.error("Exception in Loading page for Upload File: "+e.getMessage());
					}
			return mapping.findForward("hitCount");	
			
		}
	public ActionForward hitCountReport(
						ActionMapping mapping,
						ActionForm form,
						HttpServletRequest request,
						HttpServletResponse response)
						throws Exception{
						
				HitCountReportBean formBean= (HitCountReportBean) form;
				KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
				ArrayList alHitReport=null;
				KmHitCountReportService hitCountService = new KmHitCountReportServiceImpl();
				KmElementMstrService elementService = new KmElementMstrServiceImpl();

					try {
						//System.out.println("inside:::::::::::try");
						String elementId=formBean.getParentId();
						//System.out.println("elementId:::::::::::try"+elementId);
						formBean.setParentId1(formBean.getParentId());
						 String path="";
					     path=elementService.getAllParentNameString(userBean.getElementId(),elementId);
					     //System.out.println("path:::::::::::try"+path);
					     formBean.setElementPath(path);
					   alHitReport= hitCountService.hitCountReport(formBean.getParentId(),formBean.getDate());
					   //formBean.setAlHitReport(alHitReport);
					   request.setAttribute("HIT_COUNT_LIST",alHitReport);
							   
						} catch (Exception e) {
							logger.error("Exception in Hit Count Report: "+e.getMessage());
						}
				//return mapping.findForward("hitCount");	
				  return hitCount(mapping,form, request,response);
			}
			
	/* Added by harpreet kmphase II view hit count report exported into excel */

		public ActionForward HitCountExcelReport(
				ActionMapping mapping,
				ActionForm form,
				HttpServletRequest request,
				HttpServletResponse response)
				throws Exception {
				ActionErrors errors = new ActionErrors();
				ActionForward forward = new ActionForward(); // return value
				HitCountReportBean hitCountReportBean = (HitCountReportBean) form;
				ArrayList alHitReport=null;
				KmHitCountReportService hitCountService = new KmHitCountReportServiceImpl();
				try {
					
					
				logger.info( "Entered into the HitCountExcelReport  of KmHitCountReportAction");
				alHitReport= hitCountService.hitCountReport(hitCountReportBean.getParentId1(),hitCountReportBean.getDate());
				 //formBean.setAlHitReport(alHitReport);
				request.setAttribute("HIT_COUNT_LIST",alHitReport);
		
				} catch (Exception e) {
				
				logger.error("Exception occured while listing report :"+e);
				}
				return mapping.findForward("hitCountExcelReport" );
			}		

}
