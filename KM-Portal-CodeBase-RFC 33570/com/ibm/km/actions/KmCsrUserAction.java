package com.ibm.km.actions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;

import org.apache.log4j.Logger;
import com.ibm.km.dao.impl.KmCsrDaoImpl;
import com.ibm.km.dto.KmElementMstr;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.KmAddFileFormBean;
import com.ibm.km.forms.KmCSRuserFormBean;
import com.ibm.km.forms.KmElementMstrFormBean;
import com.ibm.km.forms.KmUserMstrFormBean;
import com.ibm.km.services.KmElementMstrService;
import com.ibm.km.services.KmUserMstrService;
import com.ibm.km.services.impl.KmElementMstrServiceImpl;
import com.ibm.km.services.impl.KmUserMstrServiceImpl;
import com.mail.*;
import com.sun.mail.smtp.SMTPTransport;
import com.ibm.km.forms.KmCSRuserFormBean;


public class KmCsrUserAction extends DispatchAction{
	private static Logger logger = Logger.getLogger(KmCsrUserAction.class.getName());

	public ActionForward init(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response)
			{
		ActionForward forward = new ActionForward(); // return value
		HttpSession session = request.getSession();
		ActionErrors errors = new ActionErrors();
		KmCSRuserFormBean kmCSRuserFormBean = (KmCSRuserFormBean) form;
		KmUserMstr sessionUserBean = (KmUserMstr) session
				.getAttribute("USER_INFO");
		sessionUserBean.getCategoryId();

		/*
		 * KmUserMstrService createUserService = new KmUserMstrServiceImpl();
		 * KmElementMstrService elementService = new KmElementMstrServiceImpl();
		 * KmElementMstrFormBean elementBean = new KmElementMstrFormBean();
		 */

		KmCsrDaoImpl dao = new KmCsrDaoImpl();
		ArrayList<HashMap<String,String>> eList = new ArrayList<HashMap<String,String>>();
		ArrayList<String> emailIdList = new ArrayList<String>(); 
		ArrayList<String> descList = new ArrayList<String>(); 
		try {
			eList = dao.getEmailIdList();
			
			for(int i=0;i<eList.size();i++){
				HashMap<String,String> map = new HashMap<String,String>();
				map = eList.get(i);
				emailIdList.add(map.get("emailid"));
				logger.info("The Email    ----     "+map.get("emailid"));
				descList.add(map.get("desc"));
				logger.info("The Desc    ----     "+map.get("desc"));
			}
		} catch (KmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		kmCSRuserFormBean.setEmailIdList(emailIdList);
		kmCSRuserFormBean.setDescList(descList);
		request.setAttribute("emailList", emailIdList);
		request.setAttribute("descList", descList);
		
		return mapping.findForward("initialize");
	}
	
	public ActionForward unspecified(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		
		HttpSession session = request.getSession();
		KmCSRuserFormBean kmCSRuserFormBean = (KmCSRuserFormBean) form;
		ActionForward forward = new ActionForward(); // return value
		String csrfFlag = session.getAttribute("csrfFlag").toString();
		String csrf= kmCSRuserFormBean.getCsrf();
		logger.info("CSRF from session    ----    "+csrfFlag+"      CSRF from formbean    -----    "+csrf);
		if (!csrfFlag.equals(csrf)) {
			logger.info("CSRF Breach Logout------->");
			forward = mapping.findForward("logoutSuccess");
			return (forward);
		}

		
		//System.out.println("Hello");
		
		ActionErrors errors = new ActionErrors();
		KmUserMstr sessionUserBean =
			(KmUserMstr) session.getAttribute("USER_INFO");
		sessionUserBean.getCategoryId();
		
		/*KmUserMstrService createUserService = new KmUserMstrServiceImpl();
		KmElementMstrService elementService = new KmElementMstrServiceImpl();
		KmElementMstrFormBean elementBean = new KmElementMstrFormBean();*/
		
		KmCsrDaoImpl dao=new KmCsrDaoImpl();
		
		/*ArrayList emailIdList = new ArrayList();
		try {
			emailIdList = dao.getEmailIdList();
		} catch (KmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		kmCSRuserFormBean.setEmailIdList(emailIdList);*/
		
		ArrayList elementList = new ArrayList();
		
		
		logger.info("Hi"+ sessionUserBean.getCategoryId());
		ActionMessages messages=new ActionMessages();
		//ActionMessages usermessages=new ActionMessages();
		logger.info("OLM_ID "+ kmCSRuserFormBean.getOlm_id());
		
		
		boolean validity= dao.isValidOlmId(kmCSRuserFormBean.getOlm_id());
		if(validity)
		{
			
			logger.info("User is valid"+ validity);
			logger.info("The email  reieved from page     -----------      "+kmCSRuserFormBean.getSelectedEmail());
			dao.insert(kmCSRuserFormBean);
		}
		else
		{
			logger.info("validity"+validity);
			ActionMessage msg1=new ActionMessage("csruser.invalid.olmid");
			messages.add("messages", msg1);
			saveMessages(request, messages);	
			ArrayList<HashMap<String,String>> eList = new ArrayList<HashMap<String,String>>();
			ArrayList<String> emailIdList = new ArrayList<String>(); 
			ArrayList<String> descList = new ArrayList<String>(); 
			try {
				eList = dao.getEmailIdList();
				
				for(int i=0;i<eList.size();i++){
					HashMap<String,String> map = new HashMap<String,String>();
					map = eList.get(i);
					emailIdList.add(map.get("emailid"));
					descList.add(map.get("desc"));
				}
			} catch (KmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			kmCSRuserFormBean.setEmailIdList(emailIdList);
			kmCSRuserFormBean.setDescList(descList);
			request.setAttribute("emailList", emailIdList);
			request.setAttribute("descList", descList);
			request.setAttribute("messages", messages);
			//logger.info("gadha");
			return mapping.findForward("initialize");			
		}
		
		
			
		
		String strSubject = "Comments";
		String strMessage = null;
		String txtMessage = null;
		StringBuffer sbMessage = new StringBuffer();
                
	    logger.info("The email  reieved from page     -----------      "+kmCSRuserFormBean.getSelectedEmail());
		if(!kmCSRuserFormBean.getSelectedEmail().equalsIgnoreCase("")) {
		sbMessage.append("Dear " + kmCSRuserFormBean.getOlm_id()
									+ ", \n\n");
							sbMessage.append("Your comments : " + kmCSRuserFormBean.getComment());

		sbMessage.append(strMessage +"\n");
	
		sbMessage.append("\nRegards ");
	        sbMessage.append("\nKM Administartor ");
		sbMessage.append("\n\n/** This is an Auto generated email.Please do not reply to this.**/");
		txtMessage = sbMessage.toString();
		
		try {
			Properties prop = System.getProperties();
			ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
			String strHost = bundle.getString("mail.xml.mb.ip");
			
			String strFromEmail = bundle.getString("sender.email");

			/*prop.put("mail.xml.mb.port", strHost);
			System.out.println("propertiesssssssss and ippppppppppppp "+prop + strHost);*/
			Session ses = Session.getDefaultInstance(prop, null);
			prop.put("mail.smtp.host", strHost);
			MimeMessage msgs = new MimeMessage(ses);
			msgs.setFrom(new InternetAddress(strFromEmail));
			msgs.addRecipient(Message.RecipientType.TO,
					new InternetAddress(kmCSRuserFormBean.getSelectedEmail()));
		   logger.info("kmCSRuserFormBean.getEmail())"+kmCSRuserFormBean.getEmail());
			msgs.setSubject(strSubject);
			msgs.setText(txtMessage);
			msgs.setSentDate(new Date());
			logger.info("ewfhikjhewfqjfweojfeoefjoqefj");
			Transport.send(msgs);
			//logger.info("@6926196291@@@@@@");
			messages.add("messages", new ActionMessage("csruser.success.message"));
			saveMessages(request,messages);
			
		} catch (javax.mail.internet.AddressException ae) {
			messages.add("messages", new ActionMessage("csruser.smtp.adderror"));
			saveMessages(request, messages);
			logger.error("AddressException occurs in execute of Login Action: "+ ae.getMessage());
		} catch (javax.mail.MessagingException me) {
			messages.add("messages", new ActionMessage("csruser.smtp.error"));
			saveMessages(request, messages);
			logger.error("MessagingException occurs in execute of Login Action: "+ me.getMessage());
		}
		catch(Exception e){
			logger.error("Exception occurs in sending Email: "+ e.getMessage());
			messages.add("messages", new ActionMessage("csruser.smtp.neterror"));
			saveMessages(request, messages);
			
		}
	}ArrayList<HashMap<String,String>> eList = new ArrayList<HashMap<String,String>>();
	ArrayList<String> emailIdList = new ArrayList<String>(); 
	ArrayList<String> descList = new ArrayList<String>(); 
	try {
		eList = dao.getEmailIdList();
		
		for(int i=0;i<eList.size();i++){
			HashMap<String,String> map = new HashMap<String,String>();
			map = eList.get(i);
			emailIdList.add(map.get("emailid"));
			descList.add(map.get("desc"));
		}
	} catch (KmException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	kmCSRuserFormBean.setEmailIdList(emailIdList);
	kmCSRuserFormBean.setDescList(descList);
	request.setAttribute("emailList", emailIdList);
	request.setAttribute("descList", descList);
	request.setAttribute("messages", messages);
	return mapping.findForward("initialize");
	}
}

