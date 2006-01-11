package com.ibm.km.actions;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;


import com.ibm.km.dto.KmSubCategoryMstr;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.forms.KmLoginFormBean;
import com.ibm.km.forms.KmSubCategoryMstrFormBean;
import com.ibm.km.services.KmCategoryMstrService;
import com.ibm.km.services.KmCircleMstrService;
import com.ibm.km.services.KmSubCategoryMstrService;
import com.ibm.km.services.impl.KmCategoryMstrServiceImpl;
import com.ibm.km.services.impl.KmCircleMstrServiceImpl;
import com.ibm.km.services.impl.KmSubCategoryMstrServiceImpl;

import javax.servlet.http.*;

/**
 * @version 	1.0
 * @author		Anil
 */
public class KmSubCategoryMstrAction extends DispatchAction {

	/**
	 * Logger for the class.
	**/
	private static final Logger logger;

	static {

		logger = Logger.getLogger(KmSubCategoryMstrAction.class);
	}

	//Initializing Create Sub-category page by loading all circles
	public ActionForward init(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		
		
		ActionErrors errors = new ActionErrors();
		ActionForward forward = new ActionForward(); // return value
		KmSubCategoryMstrFormBean kmSubCategoryMstrFormBean =
			(KmSubCategoryMstrFormBean) form;
			HttpSession session = request.getSession();
					KmUserMstr sessionUserBean = (KmUserMstr) session.getAttribute("USER_INFO");
		KmCircleMstrService circleService=new KmCircleMstrServiceImpl();
		
		KmSubCategoryMstrService kmSubCategoryMstrService=new KmSubCategoryMstrServiceImpl();
		try{
					logger.info(sessionUserBean.getUserLoginId()+ " Entered into the init method of KmSubCategoryMstrAction");
					//Calling servive that retrieves all circles
					
					kmSubCategoryMstrFormBean.setCircleList((ArrayList) circleService.retrieveAllCircles());
					
					return mapping.findForward("addSubCategory");
		}
		catch(Exception e){
			logger.error("Exception occured while loading circles :"+e);
			return mapping.findForward("addSubCategory");			
		}
	
	}
	//Loads all categories under the selected circle
	public ActionForward loadCategory(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		
		
		ActionErrors errors = new ActionErrors();
		ActionMessages messages=new ActionMessages();
		ActionForward forward = new ActionForward(); // return value
		KmSubCategoryMstrFormBean kmSubCategoryMstrFormBean =
			(KmSubCategoryMstrFormBean) form;
		KmLoginFormBean kmLoginFormBean=new KmLoginFormBean();
		KmSubCategoryMstr subCategoryDTO=new KmSubCategoryMstr();
		ArrayList circleList = new ArrayList();
		KmCircleMstrService circleService=new KmCircleMstrServiceImpl();
		KmCategoryMstrService categoryService=new KmCategoryMstrServiceImpl();
		KmSubCategoryMstrService kmSubCategoryMstrService=new KmSubCategoryMstrServiceImpl();
		HttpSession session = request.getSession();
		KmUserMstr sessionUserBean = (KmUserMstr) session.getAttribute("USER_INFO");	
		try{
				logger.info(sessionUserBean.getUserLoginId()+ " Entered into the loadCategory method of KmSubCategoryMstrAction");
				String circleId=kmSubCategoryMstrFormBean.getCircleId();
				String[] circleIdArray = {circleId};
				session.setAttribute("circleId",circleId);
					
				//Calling service that retrieves all circles
					
				kmSubCategoryMstrFormBean.setCircleList((ArrayList) circleService.retrieveAllCircles());
					
				//Calling servive that retrieves all categories for the under the given circle
					
				kmSubCategoryMstrFormBean.setCategoryList((ArrayList) categoryService.retrieveCategoryService(circleIdArray));
				return mapping.findForward("addSubCategory");
		}catch(Exception e){
			logger.error("Exception occured while loading sub-category "+e);
			return mapping.findForward("addSubCategory");
		}
	
	}
	//Creates a new Sub-category after selecting a circle and category inside the circle
	public ActionForward insert(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		
		
		ActionErrors errors = new ActionErrors();
		ActionMessages messages=new ActionMessages();
		ActionForward forward = new ActionForward(); // return value
		KmSubCategoryMstrFormBean kmSubCategoryMstrFormBean =
			(KmSubCategoryMstrFormBean) form;
		KmLoginFormBean kmLoginFormBean=new KmLoginFormBean();
		KmSubCategoryMstr subCategoryDTO=new KmSubCategoryMstr();
		ArrayList circleList = new ArrayList();
		KmCircleMstrService circleService=new KmCircleMstrServiceImpl();
		KmCategoryMstrService categoryService=new KmCategoryMstrServiceImpl();
		KmSubCategoryMstrService kmSubCategoryMstrService=new KmSubCategoryMstrServiceImpl();
		HttpSession session = request.getSession();
		KmUserMstr sessionUserBean = (KmUserMstr) session.getAttribute("USER_INFO");	
		try{
					
	
			logger.info(sessionUserBean.getUserLoginId()+ " Entered into the insert method of KmSubCategoryMstrAction");
					kmSubCategoryMstrFormBean.setCreatedBy(sessionUserBean.getUserId());
					String[] circleIdArray = {kmSubCategoryMstrFormBean.getCircleId()};
					
					//Populating DTO
					
					subCategoryDTO.setCreatedBy(Integer.parseInt(kmSubCategoryMstrFormBean.getCreatedBy()));
					subCategoryDTO.setUpdatedBy(Integer.parseInt(kmSubCategoryMstrFormBean.getCreatedBy()));
					subCategoryDTO.setSubCategoryName(kmSubCategoryMstrFormBean.getSubCategoryName());
					subCategoryDTO.setStatus("A");
					subCategoryDTO.setCircleId(Integer.parseInt(kmSubCategoryMstrFormBean.getCircleId()));
					subCategoryDTO.setCategoryId(Integer.parseInt(kmSubCategoryMstrFormBean.getCategoryId()));
					subCategoryDTO.setSubCategoryDesc(kmSubCategoryMstrFormBean.getSubCategoryDesc());
					/*Create sub category service */
					String subCategoryName=kmSubCategoryMstrFormBean.getSubCategoryName();
					String categoryId=kmSubCategoryMstrFormBean.getCategoryId();
					boolean isDuplicate = kmSubCategoryMstrService.checkOnSubCategoryNameService(subCategoryName,categoryId);
					if(isDuplicate){
							messages.add("msg1",new ActionMessage("subcategory.duplicate"));
							kmSubCategoryMstrFormBean.setSubCategoryStatus(kmSubCategoryMstrFormBean.getSubCategoryName()+" Already Exists Under the Selected Category");
							logger.info("Sub-Category Already Exists");
							kmSubCategoryMstrFormBean.setSubCategoryName("");
							kmSubCategoryMstrFormBean.setSubCategoryDesc("");
							//Calling servive that retrieves all circles
							kmSubCategoryMstrFormBean.setCircleList((ArrayList) circleService.retrieveAllCircles());	
							//Calling servive that retrieves the categories under the selected circle
							kmSubCategoryMstrFormBean.setCategoryList((ArrayList) categoryService.retrieveCategoryService(circleIdArray));					
					}
					else{
					
							kmSubCategoryMstrService.createSubCategoryService(subCategoryDTO);				
							errors.add("errors", new ActionError("category.create"));
							kmSubCategoryMstrFormBean.setSubCategoryStatus(kmSubCategoryMstrFormBean.getSubCategoryName()+" Successfully Created");
							messages.add("msg2",new ActionMessage("subcategory.created"));
							logger.info("A Sub-category is Created");
							kmSubCategoryMstrFormBean.setCircleId("-1");
							kmSubCategoryMstrFormBean.setCategoryId("-1");
							kmSubCategoryMstrFormBean.setSubCategoryName("");
							kmSubCategoryMstrFormBean.setSubCategoryDesc("");
							//Calling servive that retrieves all circles
							 kmSubCategoryMstrFormBean.setCircleList((ArrayList) circleService.retrieveAllCircles());
					}
					saveMessages(request,messages);
					
					return mapping.findForward("addSubCategory");

		}catch(Exception e){
					logger.error("Exception occured while creating sub-category");
					return mapping.findForward("failure");
		}
				
}	

					
}
	

