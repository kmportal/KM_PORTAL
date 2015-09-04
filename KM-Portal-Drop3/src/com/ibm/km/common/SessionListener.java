/*
 * Created on Jun 13, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.common;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.apache.log4j.Logger;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.services.KmUserMstrService;
import com.ibm.km.services.impl.KmUserMstrServiceImpl;
//import java.util.Enumeration;

/**
 * @author 
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SessionListener  implements HttpSessionListener {
	private static final Logger logger;
	static {
		logger = Logger.getLogger(SessionListener.class);
	}
	
	public void sessionCreated(HttpSessionEvent arg0) {
		
		    logger.info("Session Created.  session id : "+arg0.getSession().getId());
	}

	public void sessionDestroyed(HttpSessionEvent arg0) {
		 logger.info("Session Destroyed. session id : "+arg0.getSession().getId());
		KmUserMstr sessionUserBean =
			(KmUserMstr) arg0.getSession().getAttribute("USER_INFO");
		
		String actorId ="";
		
	/*	Enumeration <String> enumer=arg0.getSession().getAttributeNames();
		while(enumer.hasMoreElements())
		 logger.info(enumer.nextElement().toString()); */
		try {
			if(sessionUserBean!=null) {
				actorId= sessionUserBean.getKmActorId();
				logger.info("actorID------" + actorId);
				
				/*if (actorId.equals(Constants.CIRCLE_CSR) || actorId.equals(Constants.CATEGORY_CSR)) {
					sessionUserBean.setUserLoginStatus("N");
					KmUserMstrService userService= new KmUserMstrServiceImpl();
					userService.updateUserStatus(sessionUserBean);	
				  logger.info("inside................/errors/sessionTimeout_CSR.jsp");
		
				} else {*/
				//	sessionUserBean.setUserLoginStatus("N");
				//	sessionUserBean.setSessionID(arg0.getSession().getId());
				//	KmUserMstrService userService= new KmUserMstrServiceImpl();
				//	userService.updateUserStatus(sessionUserBean);
					logger.info("inside................/errors/sessionTimeout.jsp ");
			/*
				}*/
			
			}	
		} catch (Exception e)
		 {
		 	e.printStackTrace();
			// TODO: handle exception
		}
	}
}
