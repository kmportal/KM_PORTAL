/*
 * Created on May 19, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.actions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;

import com.ibm.km.dao.KmOfferUploadDao;
import com.ibm.km.dao.impl.KmOfferUploadDaoImpl;
import com.ibm.km.dto.EmployeeAppreciationDTO;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.dto.OfferDetailsDTO;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.KmEmployeeAppreciationFormBean;
import com.ibm.km.forms.KmOfferUploadFormBean;
import com.ibm.km.forms.KmProductUploadFormBean;
import com.ibm.km.services.KmDocumentService;
import com.ibm.km.services.KmEmployeeAppreciationService;
import com.ibm.km.services.KmOfferUploadService;
import com.ibm.km.services.KmProductUploadService;
import com.ibm.km.services.impl.KmDocumentServiceImpl;
import com.ibm.km.services.impl.KmEmployeeAppreciationServiceImpl;
import com.ibm.km.services.impl.KmOfferUploadServiceImpl;
import com.ibm.km.services.impl.KmProductUploadServiceImpl;

/**
 * @author Kundan Kumar
 * @since 9th Oct, 2012
 */
public class KmOfferUploadAction extends DispatchAction {
	private static final Logger logger;
	static {
		logger = Logger.getLogger(KmOfferUploadAction.class);
	}

	public ActionForward init(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
				request.getSession().setAttribute("SAVE_OFFER_DATA", "true");
				KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
				KmOfferUploadFormBean offerUploadformBean = (KmOfferUploadFormBean)form;
				logger.info(userBean.getUserLoginId() + " entered init method for DTH offer upload page.");
				offerUploadformBean.reset(mapping,request);
				
				offerUploadformBean.setCreatedBy(userBean.getElementId());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
				
				String date = sdf.format(new java.util.Date());
				date = date.substring(0,10);		
				if(offerUploadformBean.getStartDate().equals(""))
					offerUploadformBean.setStartDate(date);
				if(offerUploadformBean.getEndDate().equals(""))
					offerUploadformBean.setEndDate(date);
				
				
				KmOfferUploadService kmOfferUploadService = new KmOfferUploadServiceImpl();
				offerUploadformBean.setBucketList(kmOfferUploadService.getBucketList());
				logger.info(userBean.getUserLoginId() + " exited init method for DTH offer upload page.");
				return mapping.findForward("initialize");
				
		}
	public ActionForward insert(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception { 
		KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
		logger.info(userBean.getUserLoginId() + " entered insert method for DTH offer upload page.");
		
		if ( request.getSession().getAttribute("SAVE_OFFER_DATA") == null ) {
			  return init(mapping, form, request, response);
			}
		request.getSession().setAttribute("SAVE_OFFER_DATA", null);
		
		ActionErrors errors = new ActionErrors();
		ActionForward forward = new ActionForward(); // return value
		KmOfferUploadFormBean kmOfferUploadForm = (KmOfferUploadFormBean)form;
		kmOfferUploadForm.setCreatedBy(userBean.getUserId());
		ActionMessages messages = new ActionMessages();
		KmOfferUploadService kmOfferUploadService = new KmOfferUploadServiceImpl();
		int offerId = 0; 
		try {
			    offerId = kmOfferUploadService.insertOffer(kmOfferUploadForm);
			    
				messages.add("msg1",new ActionMessage("offer.uploaded"));
				kmOfferUploadForm.setOfferId(offerId);
				saveMessages(request, messages);		
		} catch (Exception e) {
		  e.printStackTrace();
			logger.error("Exception occured while creating DTH Offer :" + e);
			
			errors.add("errors",new ActionError("offer.not.uploaded"));
			
			saveErrors(request,errors);
			
		}
		return viewOfferDetails(mapping, form, request, response);
	}
	public ActionForward viewOfferDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	throws Exception { 
		KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
		ActionErrors errors = new ActionErrors();
		KmOfferUploadFormBean kmOfferUploadForm = (KmOfferUploadFormBean)form;
		//System.out.println("offerId : "+ kmOfferUploadForm.getOfferId());
		KmOfferUploadDao kmOfferUploadDao = new KmOfferUploadDaoImpl();
		ArrayList<OfferDetailsDTO> OfferDetailsDtoList =  kmOfferUploadDao.getOfferList(kmOfferUploadForm.getOfferId());
		OfferDetailsDTO offerDetailsDTO = null;
		Iterator<OfferDetailsDTO> itr = OfferDetailsDtoList.iterator();
		while(itr.hasNext())
		{
			offerDetailsDTO = itr.next(); 
		}
	    BeanUtils.copyProperties(kmOfferUploadForm, offerDetailsDTO);
		kmOfferUploadForm.setKmActorId(userBean.getKmActorId());
		request.setAttribute("kmOfferUploadForm",kmOfferUploadForm);
             return mapping.findForward("viewOfferDetails");	       
	}
	
	public ActionForward viewEditOfferDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	throws Exception { 
		KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
		request.getSession().setAttribute("UPDATE_DTH_OFFER", "true");
		logger.info(userBean.getUserLoginId() + " entered into DTH offer viewEditOfferDetails method.");
		KmOfferUploadFormBean kmOfferUploadForm = (KmOfferUploadFormBean)form;
		//System.out.println("offerId : "+ kmOfferUploadForm.getOfferId());
		KmOfferUploadDao kmOfferUploadDao = new KmOfferUploadDaoImpl();
		ArrayList<OfferDetailsDTO> OfferDetailsDtoList =  kmOfferUploadDao.getOfferList(kmOfferUploadForm.getOfferId());
		OfferDetailsDTO offerDetailsDTO = null;
		Iterator<OfferDetailsDTO> itr = OfferDetailsDtoList.iterator();
		while(itr.hasNext())
		{
			offerDetailsDTO = itr.next(); 
		}
	    BeanUtils.copyProperties(kmOfferUploadForm, offerDetailsDTO);
	    KmOfferUploadService kmOfferUploadService = new KmOfferUploadServiceImpl();
	    kmOfferUploadForm.setBucketList(kmOfferUploadService.getBucketList());
		kmOfferUploadForm.setKmActorId(userBean.getKmActorId());
		request.setAttribute("kmOfferUploadForm",kmOfferUploadForm);

		return mapping.findForward("viewEditOfferDetails");	     
	}
	
	
	public ActionForward updateOffer(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)throws Exception { 
		KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
		logger.info(userBean.getUserLoginId() + " entered into DTH offer update method.");
		
		if ( request.getSession().getAttribute("UPDATE_DTH_OFFER") == null ) {
			  return init(mapping, form, request, response);
			}
		request.getSession().setAttribute("UPDATE_DTH_OFFER", null);
		
		ActionErrors errors = new ActionErrors();
		ActionForward forward = new ActionForward(); 
		KmOfferUploadFormBean kmOfferUploadForm = (KmOfferUploadFormBean)form;
		
		//System.out.println("getOfferId : "+kmOfferUploadForm.getOfferId());
		ActionMessages messages = new ActionMessages();
		KmOfferUploadService kmOfferUploadService = new KmOfferUploadServiceImpl();
		int offerId = 0; 
		try {
			    offerId = kmOfferUploadService.updateOffer(kmOfferUploadForm);
			    
				messages.add("msg1",new ActionMessage("offer.updated"));
				kmOfferUploadForm.setOfferId(offerId);
				saveMessages(request, messages);		
		} catch (Exception e) {
		  e.printStackTrace();
			logger.error("Exception occured while updating DTH Offer :" + e);
			errors.add("errors",new ActionError("offer.not.updated"));
		}
		logger.info(userBean.getUserLoginId() + " exited from DTH offer update method.");
		return viewOfferDetails(mapping, form, request, response);
	}
	
		
}