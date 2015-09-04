package com.ibm.km.actions;


import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.forms.KmCompanyWisePlanFormBean;
import com.ibm.km.services.KmCompanyWiseBillPlanService;
import com.ibm.km.services.impl.KmCompanyWiseBillPlanServiceImpl;
import com.ibm.km.common.Constants;

/**
 * KmCompanyWisePlanAction
 * @author Anveeksha & Neeraj
 * For viewing bill plan rates based on user entered search
 * for Waiver Matrix Upload
 */

public class KmCompanyWisePlanAction extends DispatchAction{

	/**
	 * Logger for the class.
	**/
	private static final Logger logger;
	
	static {

		logger = Logger.getLogger(KmCompanyWisePlanAction.class);
	}

	/**
	 * Initializes the view bill plan page. 
	 * Circle User can search for companies , view bill plans for the company and see rate details of the bill plans
	 **/
	public ActionForward initViewCompanyWisePlan(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)throws Exception{
			KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
				ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
				KmCompanyWisePlanFormBean formBean = (KmCompanyWisePlanFormBean) form;
				formBean.setCheckForCompanyList(0);
				formBean.setCheckForDetails(0);
				formBean.setBillPlanSearchCompId(0);
				formBean.setCompanySearchValue("");
				if(userBean.getKmActorId().equals(Constants.CIRCLE_CSR)||userBean.getKmActorId().equals(Constants.CATEGORY_CSR)){
					return mapping.findForward("kmCompanyWisePlanCSR");
				}
				else{
					if(userBean.isRestricted()==false){
						request.setAttribute("RES", bundle.getString("excel.authorization.error"));
						return mapping.findForward("home");
					}
					return mapping.findForward("kmCompanyWisePlanAction");
				}	
			}
	
	/**
	 * Creates list of companies based on user entered search text 
	 **/
	public ActionForward listCompany(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)throws Exception{
		
				KmCompanyWisePlanFormBean formBean = (KmCompanyWisePlanFormBean) form;
				KmCompanyWiseBillPlanService serviceObj = new KmCompanyWiseBillPlanServiceImpl();
				formBean.setBillPlanSearchCompId(0);
				ActionErrors errors= new ActionErrors();
				KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
				
				try
				{
					formBean.setCompList(serviceObj.getCompanyList(formBean.getCompanySearchValue()));
				}
				catch(Exception e)
				{
					errors.add("", new ActionError("excel.error1"));
					saveErrors(request,errors);
					
				}
				finally{
					formBean.setCheckForCompanyList(1);
					formBean.setCheckForDetails(0);
					if(userBean.getKmActorId().equals(Constants.CIRCLE_CSR)||userBean.getKmActorId().equals(Constants.CATEGORY_CSR)){
						return mapping.findForward("kmCompanyWisePlanCSR");
					}
					else{
						return mapping.findForward("kmCompanyWisePlanAction");
					}	
				}
				
			}
	
	/**
	 * Shows list of bill plans for selected company 
	 **/
	public ActionForward listCompanyDetailsAndPlans(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)throws Exception{
			KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
		
				KmCompanyWisePlanFormBean formBean = (KmCompanyWisePlanFormBean) form;
				KmCompanyWiseBillPlanService serviceObj = new KmCompanyWiseBillPlanServiceImpl();
				
				ActionErrors errors= new ActionErrors();
				try
				{
					formBean.setCompanyDetailsList((serviceObj.getCompanyDetails(formBean.getBillPlanSearchCompId())));
					formBean.setPlanList(serviceObj.getBillPlanList(formBean.getBillPlanSearchCompId()));
				}
				catch(Exception e)
				{
					errors.add("", new ActionError("excel.error1"));
					saveErrors(request,errors);
						
				}
				finally{
					formBean.setCheckForDetails(1);
					if(userBean.getKmActorId().equals(Constants.CIRCLE_CSR)||userBean.getKmActorId().equals(Constants.CATEGORY_CSR)){
						return mapping.findForward("kmCompanyWisePlanCSR");
					}
					else{
						return mapping.findForward("kmCompanyWisePlanAction");
					}	
				}
				
			}
	
	
	/**
	 * gets rate details for selected bill plan
	 **/
	public ActionForward viewBillPlanRateDetails(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)throws Exception{
							
				KmCompanyWisePlanFormBean formBean = (KmCompanyWisePlanFormBean) form;
				KmCompanyWiseBillPlanService serviceObj = new KmCompanyWiseBillPlanServiceImpl();
				
				ActionErrors errors= new ActionErrors();
				try
				{
					formBean.setBillPlanRateDetail(serviceObj.getBillPlanRateDetails(request.getParameter("plan")));
				}
				catch(Exception e)
				{
					errors.add("", new ActionError("excel.error1"));
					saveErrors(request,errors);
				}
				
				return mapping.findForward("kmPlanRate");
			
			}
	
	
}
