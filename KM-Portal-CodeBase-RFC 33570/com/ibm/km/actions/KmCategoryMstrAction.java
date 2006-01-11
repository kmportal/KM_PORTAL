package com.ibm.km.actions;

import java.io.File;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;

import com.ibm.km.dto.KmCategoryMstr;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.forms.KmCategoryMstrFormBean;
import com.ibm.km.forms.KmLoginFormBean;
import com.ibm.km.services.KmCategoryMstrService;
import com.ibm.km.services.KmCircleMstrService;
import com.ibm.km.services.impl.KmCategoryMstrServiceImpl;
import com.ibm.km.services.impl.KmCircleMstrServiceImpl;

/**
 * @version 	1.0
 * @author		Anil
 */
public class KmCategoryMstrAction extends DispatchAction {

	/**
	 * Logger for the class.
	**/
	private static final Logger logger;

	static {

		logger = Logger.getLogger(KmCategoryMstrAction.class);
	}
	/**
	 * Initializes Create Category
	 **/

	public ActionForward init(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
			KmCategoryMstrFormBean kmCategoryMstrFormBean =
			(KmCategoryMstrFormBean) form;
			KmCircleMstrService circleService=new KmCircleMstrServiceImpl();
			KmCategoryMstrService service=new KmCategoryMstrServiceImpl();
			KmUserMstr sessionUserBean = (KmUserMstr) request.getSession().getAttribute("USER_INFO");
			logger.info(sessionUserBean.getUserLoginId()+ " Entered in to the init method of KmCategoryMstrAction");
			
			//Loads all the circles in the dropdown 
			kmCategoryMstrFormBean.setCircleList((ArrayList) circleService.retrieveAllCircles());
								return mapping.findForward("addCategory");
		}
	/*
	 * Creates a new Category
	 */
	
	
	public ActionForward insert(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
				
				ActionErrors errors = new ActionErrors();
						ActionForward forward = new ActionForward(); // return value
						KmCategoryMstrFormBean kmCategoryMstrFormBean =
							(KmCategoryMstrFormBean) form;
						KmLoginFormBean kmLoginFormBean=new KmLoginFormBean();
						KmCategoryMstr categoryDTO=new KmCategoryMstr();
						ArrayList circleList = new ArrayList();
						ActionMessages messages=new ActionMessages();
						KmCategoryMstrService kmCategoryMstrService=new KmCategoryMstrServiceImpl();
						KmCircleMstrService circleService=new KmCircleMstrServiceImpl();
						HttpSession session = request.getSession();
						KmUserMstr sessionUserBean = (KmUserMstr) session.getAttribute("USER_INFO");
						try{
								logger.info(sessionUserBean.getUserLoginId()+ " Entered in to the insert method of KmCategoryMstrAction");
								
								File f=new File("");
								
								kmCategoryMstrFormBean.setCreatedBy(sessionUserBean.getUserId());
								String categoryName=kmCategoryMstrFormBean.getCategoryName();
								String circleId=kmCategoryMstrFormBean.getCircleId();
								boolean isDuplicate = kmCategoryMstrService.checkOnCategoryNameService(categoryName,circleId);
								FormFile file = kmCategoryMstrFormBean.getNewFile();
								ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
								logger.info("File : "+file.getFileName());
								String fileName = file.getFileName();
								String extn = fileName.substring(fileName.indexOf(".")+1);	
								logger.info("File Name = "+fileName);
								logger.info("fileExtension :"+extn);
								/* Checking for maximum allowed categories*/
								int numCats=kmCategoryMstrService.getNoOfCategories(kmCategoryMstrFormBean.getCircleId());
								if(numCats>=9){
									messages.add("msg4",new ActionMessage("category.maximum"));	
									
									kmCategoryMstrFormBean.setCategoryDesc("");
									kmCategoryMstrFormBean.setCategoryName("");
								}
								else if(isDuplicate){
																	
									messages.add("msg1",new ActionMessage("category.duplicate"));
									kmCategoryMstrFormBean.setCategoryStatus(kmCategoryMstrFormBean.getCategoryName()+" Already Exists Under the Selected Circle");
									logger.info("Category Already Exists");
									kmCategoryMstrFormBean.setCategoryName("");
								}
								else if(!extn.equals("gif")&&!extn.equals("")){
									messages.add("msg5",new ActionMessage("category.fileType"));	
								}
								
								else{
									
										//Populating DTO
										String path=(new File (".")).getAbsolutePath();

										logger.info("Real Path"+path);		
										//categoryDTO.setPath(request.getContextPath()+"/WebContent/images/");
										categoryDTO.setCreatedBy(Integer.parseInt(kmCategoryMstrFormBean.getCreatedBy()));
										categoryDTO.setUpdatedBy(Integer.parseInt(kmCategoryMstrFormBean.getCreatedBy()));
										categoryDTO.setStatus("A");
										categoryDTO.setCircleId(Integer.parseInt(kmCategoryMstrFormBean.getCircleId()));
										categoryDTO.setCategoryName(kmCategoryMstrFormBean.getCategoryName());
										categoryDTO.setCategoryDesc(kmCategoryMstrFormBean.getCategoryDesc());
										categoryDTO.setFile(file);
										// Calling create category service
										kmCategoryMstrService.createCategoryService(categoryDTO);
										kmCategoryMstrFormBean.setCategoryStatus(kmCategoryMstrFormBean.getCategoryName()+" Successfully Created");
										messages.add("msg2",new ActionMessage("category.created"));
										logger.info("A New Category is Created");
										errors.add("errors", new ActionError("category.create"));
										kmCategoryMstrFormBean.setCircleId("-1");
										kmCategoryMstrFormBean.setCategoryDesc("");
										kmCategoryMstrFormBean.setCategoryName("");
//										kmCategoryMstrFormBean.reset(mapping,request);
										//Calling service to get all the circles
									
								}	
								saveMessages(request,messages);
								kmCategoryMstrFormBean.setCircleList((ArrayList) circleService.retrieveAllCircles());
					
									return mapping.findForward("addCategory");
	
						}catch(Exception e){
							
							logger.error("Exception occured while creating category :"+e);
							return mapping.findForward("failure");
						}
			}
	
}
////