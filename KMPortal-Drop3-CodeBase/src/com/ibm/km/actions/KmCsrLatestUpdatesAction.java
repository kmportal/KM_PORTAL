package com.ibm.km.actions;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.json.JSONObject;

import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.forms.KmCSRHomeBean;
import com.ibm.km.forms.KmCsrLatestUpdatesFormBean;
import com.ibm.km.services.KmCsrLatestUpdatesService;
import com.ibm.km.services.KmElementMstrService;
import com.ibm.km.services.impl.KmCsrLatestUpdatesServiceImpl;
import com.ibm.km.services.impl.KmElementMstrServiceImpl;

public class KmCsrLatestUpdatesAction extends DispatchAction{
	
    public static Logger logger = Logger.getLogger(KmCsrLatestUpdatesAction.class);

	public ActionForward initGetUpdates(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		/* Added by Parnika for LOBwise Latest Updates */
		
		HttpSession session = request.getSession();	
		String lobId = (String)session.getAttribute("CURRENT_LOB_ID");
//added by vishwas for UD intregation
		 String postpiad=(String)session.getAttribute("udUserOLMIDPost1");
    	   String prepaid=(String)session.getAttribute("udUserOLMIDPre");
    	   
       String ud=(String)session.getAttribute("ud");
       if(ud!=null || ud!=""){
           if(("UDlogin").equals(ud))
            {
           	if((postpiad!=null) && postpiad.equals("B0071323_POST"))
           	{
           		lobId="185794";
           	}
           		if((prepaid!=null) && prepaid.equals("B0071323_PRE") )
           		{
           			lobId="185794";
           		}
           		}
           }
       //end by vishwas for UD intregation
		
		String circleId = (String)session.getAttribute("CURRENT_CIRCLE_ID");

		if(circleId == null) {
	        KmCSRHomeBean csrHomeBean = (KmCSRHomeBean)session.getAttribute("CSR_HOME_BEAN");
	        if(csrHomeBean != null) {
	        	circleId = csrHomeBean.getCircleId();
	        	if(circleId != null) 
	    	        session.setAttribute("CURRENT_CIRCLE_ID", csrHomeBean.getCircleId());
	        }
		}
		
		logger.info("lobId:" + lobId + " CircleID:" + circleId);
		/*End of changes by Parnika */
		
		ArrayList updatesList = new ArrayList();
		KmCsrLatestUpdatesFormBean formBean = (KmCsrLatestUpdatesFormBean)form;
		////System.out.println("Inside initGetUpdates!!");
		KmCsrLatestUpdatesService service = new KmCsrLatestUpdatesServiceImpl();
		updatesList = service.initGetUpdates(lobId, circleId);
		formBean.setUpdatesList(updatesList);
		JSONObject json = service.getElementsAsJson(lobId, circleId);
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Content-Type", "application/x-json");
		response.getWriter().print(json);
		
		request.setAttribute("LATEST_UPDATES",updatesList);
		////System.out.println("request.getAttr=="+request.getSession().getAttribute("LATEST_UPDATES"));
		return null;
	}
		
		

}
