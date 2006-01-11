/*
 * Created on May 1, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.actions;

import java.util.List;

import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.forms.*;
import com.ibm.km.services.KmFavoritesService;
import com.ibm.km.services.impl.KmFavoritesServiceImpl;

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

/**
 * @author varunagg
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class KmViewFavoritesAction extends DispatchAction {
	
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(KmViewFavoritesAction.class);
	}
	
	public ActionForward showFavorites(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
				
		KmLoginFormBean kmLoginFormBean=new KmLoginFormBean();
		
		KmFavoritesService favoritesService=new KmFavoritesServiceImpl();
		HttpSession session = request.getSession();
		KmUserMstr sessionUserBean = (KmUserMstr) session.getAttribute("USER_INFO");
		try{
			String circleId=(String) session.getAttribute("CURRENT_CIRCLE_ID");
			if(circleId==null){
				circleId=sessionUserBean.getElementId();
			}
			List favoriteList = favoritesService.showFavoritesService(sessionUserBean.getUserId(),circleId);
			//session.setAttribute("favoriteList", favoriteList);
			request.setAttribute("favoriteList", favoriteList);
			return mapping.findForward("viewFavorites");
		} catch(Exception e) {
			
			logger.error("Exception occured while listing feedbacks :"+e);
			return mapping.findForward("viewFavorites");
		}
	}
}
