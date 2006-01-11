/*
 * Created on May 19, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.actions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import org.json.JSONObject;

import com.ibm.km.common.ImageCopy;
import com.ibm.km.dto.KmElementMstr;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.KmCSRHomeBean;
import com.ibm.km.forms.KmElementMstrFormBean;
import com.ibm.km.forms.KmMoveElementFormBean;
import com.ibm.km.search.DeleteFiles;
import com.ibm.km.services.KmDocumentService;
import com.ibm.km.services.KmElementMstrService;
import com.ibm.km.services.KmUserMstrService;
import com.ibm.km.services.impl.KmDocumentServiceImpl;
import com.ibm.km.services.impl.KmElementMstrServiceImpl;
import com.ibm.km.services.impl.KmUserMstrServiceImpl;

/**
 * @author namangup
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class KmElementMstrAction extends DispatchAction {
	/**
	 * Logger for the class.
	**/
	private static final Logger logger;

	static {

		logger = Logger.getLogger(KmElementMstrAction.class);
	}

	/**
	 *Initailizes the Create User page. For SuperAdmin uploads all the circles and for CircleApprover shows the different user types
	**/
	public ActionForward init(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
				ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
				KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
				KmElementMstrFormBean formBean = (KmElementMstrFormBean)form;
				logger.info(userBean.getUserLoginId() + " entered initElement method");
				formBean.reset(mapping,request);
				try {
					
					KmElementMstrService kmElementService = new KmElementMstrServiceImpl();
					List firstDropDown;
					if (userBean.getKmActorId().equals(bundle.getString("LOBAdmin"))||userBean.getKmActorId().equals(bundle.getString("Superadmin"))) {
						firstDropDown = kmElementService.getAllChildren(userBean.getElementId());
					}else{
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
					formBean.setParentId(userBean.getElementId());
					formBean.setCreateMultiple(null);
					formBean.setElementPath("");
					request.setAttribute("elementLevelNames",kmElementService.getAllElementLevelNames());
					request.setAttribute("allParentIdString",kmElementService.getAllParentIdString("1",userBean.getElementId()));
					request.setAttribute("firstList",firstDropDown);
					
				} catch (KmException e) {
					logger.error("KmException in Loading page for Init Create ELement: "+e.getMessage());
					
				} catch (Exception e) {
					logger.error("Exception in Loading page for Init Create ELement: "+e.getMessage());
					
				}

				logger.info(userBean.getUserLoginId() + " exited init Create Element method");
				return mapping.findForward("initialize");
				
		}
	public ActionForward loadElementDropDown(ActionMapping mapping,ActionForm form,
		HttpServletRequest request,HttpServletResponse response){
		String parentId = request.getParameter("ParentId");
		KmElementMstrService kmElementService = new KmElementMstrServiceImpl();

		try {
			request.setAttribute("FILE_COUNT_DTO",null);
			JSONObject json = kmElementService.getElementsAsJson(parentId);
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Content-Type", "application/x-json");
			response.getWriter().print(json);		
		} catch (IOException e) {
			logger.error("IOException in Loading Element Dropdown: "+e.getMessage());
			
		} catch (Exception e) {
			logger.error("Exception in Loading Element Dropdown: "+e.getMessage());
			
		}
		
		return null;
	}
	
	public ActionForward loadPANElementDropDown(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response){
			String parentId = request.getParameter("ParentId");
			KmElementMstrService kmElementService = new KmElementMstrServiceImpl();

			try {
				request.setAttribute("FILE_COUNT_DTO",null);
				JSONObject json = kmElementService.getPANElementsAsJson(parentId);
				response.setHeader("Cache-Control", "no-cache");
				response.setHeader("Content-Type", "application/x-json");
				response.getWriter().print(json);		
			} catch (IOException e) {
				logger.error("IOException in Loading Element Dropdown: "+e.getMessage());
				
			} catch (Exception e) {
				logger.error("Exception in Loading Element Dropdown: "+e.getMessage());
				
			}
			
			return null;
		}
	
	public ActionForward loadDocPath(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response){
			String parentId = request.getParameter("ParentId");
			KmElementMstrService kmElementService = new KmElementMstrServiceImpl();

			try {
				request.setAttribute("FILE_COUNT_DTO",null);
				JSONObject json = kmElementService.getDocPathAsJson(parentId);
				response.setHeader("Cache-Control", "no-cache");
				response.setHeader("Content-Type", "application/x-json");
				response.getWriter().print(json);		
			} catch (IOException e) {
				logger.error("IOException in Loading Element Dropdown: "+e.getMessage());
				
			} catch (Exception e) {
				logger.error("Exception in Loading Element Dropdown: "+e.getMessage());
				
			}
			
			return null;
		}
	
	public ActionForward insert(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
				KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
				ActionErrors errors = new ActionErrors();
				ActionForward forward = new ActionForward(); // return value
				KmElementMstrFormBean kmElementMstrformBean = (KmElementMstrFormBean)form;
				KmElementMstr elementDTO = new KmElementMstr();
				ActionMessages messages = new ActionMessages();
				KmElementMstrService kmElementMstrService = new KmElementMstrServiceImpl();
				HttpSession session = request.getSession();
				KmUserMstr sessionUserBean =(KmUserMstr) session.getAttribute("USER_INFO");
				ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
				try {
					logger.info(
						sessionUserBean.getUserLoginId()
							+ " Entered in to the insert method of KmCategoryMstrAction");
					
					/*
					 * BFR10 CSR07203 Element Creation for Multiple Circles - Karan
					 */	
				String[] multipleCircles = kmElementMstrformBean.getCreateMultiple();
				
				if(multipleCircles != null)
				{
					int elementLevel = Integer.parseInt(kmElementMstrformBean.getElementLevel()),successMessageCtr=0;
					String parentId,elementFolderPath = "";
					String[] elementIds = kmElementMstrformBean.getElementFolderPath().split("/");
					String[] elementNames = new String[elementLevel - 3];
					boolean isValid;
					for(int x = 0 ; x < (elementLevel - 4) ; x++ )
					{
						elementNames[x]= kmElementMstrService.getElementName(elementIds[x+3]);	
					}
					elementNames[elementNames.length - 1] = kmElementMstrformBean.getElementName();	
					
					for(int i =0 ; i< multipleCircles.length ; i++)
					{
						parentId = multipleCircles[i];
						elementFolderPath = "1/" + elementIds[1] + "/" + parentId + "/";
						isValid = false;	
						for(int j = 0 ; j <= (elementLevel - 4) ; j++)	
						{
							String[] elementStatus =
								kmElementMstrService.checkExistingElement(elementNames[j],parentId);
							
							if(elementStatus[0].equals("true"))
							{
								parentId = elementStatus[1];
								elementFolderPath = elementFolderPath + parentId + "/";
								if(j == (elementLevel - 4))
									isValid = false;
								else
									isValid = true;
							}
							else
							{	
								if(j == (elementLevel - 4))
									isValid = true;
								else
									isValid = false;
								break;
							}	
								
						}//Checking Path for 1 Circle at a time
						if (isValid) {
							//System.out.println("\nElement Folder Path : "+elementFolderPath + "\n");
							kmElementMstrformBean.setParentId(parentId);
							elementDTO=setElementDTO(kmElementMstrformBean);
							elementDTO.setCreatedBy(userBean.getUserId());
							elementDTO.setUpdatedBy(userBean.getUserId());	
							String elementId=kmElementMstrService.createElement(elementDTO);
							String path=bundle.getString("folder.path")+elementFolderPath;
							String elementPath=path+elementId;
							File f = new File(elementPath);
							f.mkdirs();  
							++successMessageCtr;
						} 
					}//Bulk Category and Sub-Categories Creation for Multiple Circles	
					
					if(successMessageCtr > 0)
					{
						messages.add("msg2",new ActionMessage("element.created"));
						logger.info("A New Element is Created");
					}
					else
					{
						messages.add("msg1",new ActionMessage("element.duplicate",kmElementMstrformBean.getElementName()));
						logger.info("Element Already Exists");
					}
					
				}//Should Be Executed if Multiple Circles are Selected
				
			  else
				{
				String isDuplicate = "false";
				String[] elemStatus  =
				kmElementMstrService.checkExistingElement(kmElementMstrformBean.getElementName(),kmElementMstrformBean.getParentId());
				  isDuplicate = elemStatus[0];
				  
			if (isDuplicate.equals("true")) {
				messages.add("msg1",new ActionMessage("element.duplicate",kmElementMstrformBean.getElementName()));
				logger.info("Element Already Exists");

			} else {
			
				if(kmElementMstrformBean.getElementName().equals("")){
					return init(mapping, form, request, response);
				}
				elementDTO=setElementDTO(kmElementMstrformBean);
				elementDTO.setCreatedBy(userBean.getUserId());
				elementDTO.setUpdatedBy(userBean.getUserId());	
				String elementId=kmElementMstrService.createElement(elementDTO);
				String path=bundle.getString("folder.path")+kmElementMstrformBean.getElementFolderPath();
				String elementPath=path+"/"+elementId;
				File f = new File(elementPath);
				f.mkdirs();  
				messages.add("msg2",new ActionMessage("element.created"));
				logger.info("A New Element is Created");
			  }
				  
		   }//Should Be Executed if Multiple Circles are not Selected
				
				saveMessages(request, messages);
				return init(mapping, form, request, response);
				} catch (Exception e) {
				
					logger.error("Exception occured while creating category :" + e);
					e.printStackTrace();
					return init(mapping, form, request, response);
				}
			}//insert
	/**
	 * @param kmElementMstrformBean
	 * @return
	 */
	private KmElementMstr setElementDTO(KmElementMstrFormBean kmElementMstrformBean) {
		KmElementMstr elementDTO=new KmElementMstr();

		elementDTO.setElementName(kmElementMstrformBean.getElementName());
		elementDTO.setElementDesc(kmElementMstrformBean.getElementDesc());
		elementDTO.setParentId(kmElementMstrformBean.getParentId());
		elementDTO.setElementLevel(kmElementMstrformBean.getElementLevel());
		//logger.info("Parent Id: "+elementDTO.getParentId());
		elementDTO.setPanStatus("N");
		elementDTO.setStatus("A");
		
		//elementDTO.setFile(file);
		return elementDTO;
	}
	
	public ActionForward initMove(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
				KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
				KmMoveElementFormBean formBean = (KmMoveElementFormBean)form;
				formBean.reset(mapping,request);
				logger.info("Init Button Type: "+formBean.getButtonType());
				logger.info(userBean.getUserLoginId() + " entered initMove method");
				try {
					KmElementMstrService kmElementService = new KmElementMstrServiceImpl();
					List firstDropDown = kmElementService.getChildren(userBean.getElementId());
					if (firstDropDown!=null && firstDropDown.size()!=0){
						formBean.setInitialLevel(((KmElementMstr)firstDropDown.get(0)).getElementLevel());
					}
					if(request.getAttribute("ELEMENT_LIST")==null)
						formBean.setInitStatus("true");	
					else
					formBean.setInitStatus("false");
					formBean.setParentId(userBean.getElementId());
					request.setAttribute("elementLevelNames",kmElementService.getAllElementLevelNames());
					request.setAttribute("allParentIdString",kmElementService.getAllParentIdString("1",userBean.getElementId()));
					request.setAttribute("firstList",firstDropDown);
					formBean.setMoveElementList(null);
				} catch (KmException e) {
					logger.error("Exception in Loading page for document move "+e.getMessage());
					
				}
				logger.info(userBean.getUserLoginId() + " exited initMove ");
				return mapping.findForward("initMove");
		}
	
	public ActionForward listElements(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
				KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
				KmMoveElementFormBean formBean = (KmMoveElementFormBean)form;
				logger.info(userBean.getUserLoginId() + " entered listElements method");
				KmElementMstrService elementService=new KmElementMstrServiceImpl();
				ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
				try {
					formBean.setOldPath(formBean.getElementPath());
					formBean.setOldElementLevel(formBean.getElementLevel());
					String parentId=formBean.getParentId();
					
					formBean.setInitStatus("");
					ArrayList elementList=null;
					if (userBean.getKmActorId().equals(bundle.getString("LOBAdmin"))||userBean.getKmActorId().equals(bundle.getString("Superadmin"))) {
						elementList=elementService.getAllChildrenWithPath(parentId, userBean.getElementId());
					} 
					else {
						elementList=elementService.getChildrenWithPath(parentId, userBean.getElementId());
					}
					if(elementList.size()>0){
						formBean.setButtonType("move");
					}else{
						formBean.setButtonType("list");
					}
					request.setAttribute("ELEMENT_LIST",elementList);
				} catch (KmException e) {
					logger.error("Exception in listing ELEMENTS in move elements "+e.getMessage());
				
				}
				logger.info(userBean.getUserLoginId() + " exited elementListing ");
				return initMove(mapping, formBean, request, response);
		}
	
	public ActionForward moveElements(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
				ActionMessages messages=new ActionMessages();
				KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
				KmMoveElementFormBean formBean = (KmMoveElementFormBean)form;
				KmElementMstrService kmElementService =null;
				logger.info(userBean.getUserLoginId() + " entered moveElement method");
				String parentId=formBean.getParentId();
				String levelId=formBean.getElementLevel();
				String oldLevelId=formBean.getOldElementLevel();
				int levelDiff=Integer.parseInt(levelId)-Integer.parseInt(oldLevelId);
				logger.info("Parent Id: "+parentId+" Level Id: "+levelId);
				String[] movedElementList=formBean.getMoveElementList();
				String oldPath= formBean.getOldPath();
				String newPath =formBean.getElementPath();	
				logger.info("New Path : "+newPath);
				KmUserMstrService userService=new KmUserMstrServiceImpl();	
				KmDocumentService documentService=new KmDocumentServiceImpl();		
			
				try {
					String flag="MOVED";
					Date dt1=new Date();
					
					int noOfUsers=0;
					boolean isFavourite=false;
					for(int i=0;i<movedElementList.length;i++){
						
						logger.info(movedElementList[i]);
						 noOfUsers= userService.noOfElementUsers(movedElementList[i]);
						isFavourite=userService.checkForFavourite(movedElementList[i]);
						logger.info("No.OfEleemnt Users = "+noOfUsers+"     "+isFavourite);;
						if(noOfUsers>0 || isFavourite==true){
							flag="NOT_MOVED";
							break;
						}
					}
					if(flag.equals("NOT_MOVED")){
						messages.add("msg",new ActionMessage("moveElement.not.possible"));
						logger.info("Element cannot be moved because users are created for inner elements");
						saveMessages(request,messages);
						return initMove(mapping, formBean, request, response);
					}
					if(movedElementList!=null){
						kmElementService = new KmElementMstrServiceImpl();
						boolean success=kmElementService.moveElementsInFS(movedElementList, oldPath, newPath);
						if(success){
							success=kmElementService.moveElementsInDB(movedElementList,parentId);
							success=kmElementService.changeAllElementLevel(movedElementList, levelDiff);
						//	String[] documentElements=kmElementService.getDocs(movedElementList);
							Date dt4=new Date();
							String[] allDocuments=kmElementService.getElements(movedElementList);
							if(allDocuments!=null){
								//for(int k=0;k<allDocuments.length;k++){
									
									String[] alldocumentPaths=documentService.changeDocumentPathsInDb(allDocuments);
								//	success=documentService.updateDocumentPaths(allDocuments,alldocumentPaths);
									success=true;
								//	if(success==false)
								//		break;
							//	}
							}
							
							
							if(success){
								messages.add("msg",new ActionMessage("moveElement.success"));
														
							}else{
								messages.add("msg",new ActionMessage("moveElement.databaseFailure"));
								kmElementService.moveElementsInFS(movedElementList, newPath, oldPath);
							}
						}else{
							messages.add("msg",new ActionMessage("moveElement.networkFailure"));
						}
					}else{
						messages.add("msg",new ActionMessage("moveElement.noElement"));
					}
					formBean.setButtonType("list");
					formBean.setInitStatus("true");
					formBean.setMoveElementList(null);	
					Date dt2=new Date();	
				
					
				}catch (KmException e) {
					logger.error("Exception occurs in moveElements: "+ e.getMessage());
					formBean.setButtonType("list");
					formBean.setInitStatus("true");
					formBean.setMoveElementList(null);
					logger.info("Exception in moving document "+e.getMessage());
					messages.add("msg1",new ActionMessage("moveElement.databaseFailure"));
					kmElementService.moveElementsInFS(movedElementList, newPath, oldPath);
				}
				saveMessages(request,messages);
				logger.info(userBean.getUserLoginId() + " exited moveElement method");
				return initMove(mapping, formBean, request, response);
		}
	
	/* 
	 * BRF11 CSR 7203 Admin -> Sub-Category Movement & Copy (new) , copyElements method added - Karan
	 */
	
	
	public ActionForward copyElements(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
				ActionMessages messages=new ActionMessages();
				KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
				KmMoveElementFormBean formBean = (KmMoveElementFormBean)form;
				KmElementMstrService kmElementService = new KmElementMstrServiceImpl();
				logger.info(userBean.getUserLoginId() + " entered copyElement method");
				String parentId=formBean.getParentId();
				String levelId=formBean.getElementLevel();
				String oldLevelId=formBean.getOldElementLevel();
				//int levelDiff=Integer.parseInt(levelId)-Integer.parseInt(oldLevelId);
				logger.info("Parent Id: "+parentId+" Level Id: "+levelId);
				String[] movedElementList=formBean.getMoveElementList();
				String oldPath= formBean.getOldPath();
				String newPath =formBean.getElementPath();	
				//logger.info("New Path : "+newPath);
				//KmUserMstrService userService=new KmUserMstrServiceImpl();	
				//KmDocumentService documentService=new KmDocumentServiceImpl();		
			
				try {
					
					logger.info("parentId "+parentId);
					logger.info("levelId "+levelId);
					logger.info("oldLevelId "+oldLevelId);
					logger.info("oldPath "+oldPath);
					logger.info("newPath "+newPath);
					
					for(int i=0;i<movedElementList.length;i++)
					{
						logger.info("movedElementList "+movedElementList[i]);
						kmElementService.copyElement(movedElementList[0], parentId,userBean.getUserId());
					}
						messages.add("msg",new ActionMessage("copyElement.success"));
						
					
				}catch (KmException e) {
					e.printStackTrace();
					logger.error("Exception occurs in copyElements: "+ e.getMessage());
					formBean.setButtonType("list");
					formBean.setInitStatus("true");
					formBean.setMoveElementList(null);
					logger.info("Exception in coping document "+e.getMessage());
					messages.add("msg1",new ActionMessage("moveElement.databaseFailure"));
					kmElementService.moveElementsInFS(movedElementList, newPath, oldPath);
				}
				saveMessages(request,messages);
				logger.info(userBean.getUserLoginId() + " exited copyElement method");
				return initMove(mapping, formBean, request, response);
		}
	
	public ActionForward initViewEdit(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
				ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
				KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
				KmElementMstrFormBean formBean = (KmElementMstrFormBean)form;
				logger.info(userBean.getUserLoginId() + " entered initViewEdit method");
				formBean.reset(mapping,request);
				try {
					KmElementMstrService kmElementService = new KmElementMstrServiceImpl();
					List firstDropDown;
					if (userBean.getKmActorId().equals(bundle.getString("LOBAdmin"))||userBean.getKmActorId().equals(bundle.getString("Superadmin"))) {
						firstDropDown = kmElementService.getAllChildren(userBean.getElementId());
					}else{
						firstDropDown = kmElementService.getChildren(userBean.getElementId());
					}					
					if (firstDropDown!=null && firstDropDown.size()!=0){
						formBean.setInitialLevel(((KmElementMstr)firstDropDown.get(0)).getElementLevel());
					}
					if(request.getAttribute("ELEMENT_LIST")==null)
						formBean.setInitStatus("true");	
					else
						formBean.setInitStatus("false");	
					
					formBean.setParentId(userBean.getElementId());
					
					//System.out.println("\n formBean.getElementPath() "+formBean.getElementPath()+"\n");
					
					if( formBean.getElementPath().equals("") )
					{
						formBean.setDisplayCircle(false);
					}
					else
					{
						String[] elementIds = formBean.getElementPath().split("/");	
						JSONObject json = kmElementService.getElementsAsJson(elementIds[1]);
						
						request.setAttribute("js", json);
						formBean.setDisplayCircle(true);
						
					}
					
					request.setAttribute("elementLevelNames",kmElementService.getAllElementLevelNames());
					request.setAttribute("allParentIdString",kmElementService.getAllParentIdString("1",userBean.getElementId()));
					request.setAttribute("firstList",firstDropDown);
				} catch (KmException e) {
					
					logger.error("KmException in Loading page for Init Create ELement: "+e.getMessage());
					
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Exception in Loading page for Init Create ELement: "+e.getMessage());
					
				}

				logger.info(userBean.getUserLoginId() + " exited initViewEdit method");
				return mapping.findForward("viewElements");
				
		}
	public ActionForward viewElements(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
				KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
				KmElementMstrFormBean formBean = (KmElementMstrFormBean)form;
				logger.info(userBean.getUserLoginId() + " entered viewElements method");
				KmElementMstrService elementService=new KmElementMstrServiceImpl();
				ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
				try {
					
					String parentId=formBean.getParentId();
					logger.info(" ParentId :"+parentId) ;
					ArrayList elementList=null;
					if (userBean.getKmActorId().equals(bundle.getString("LOBAdmin"))||userBean.getKmActorId().equals(bundle.getString("Superadmin"))) {
						elementList=elementService.getAllChildrenWithPath(parentId, userBean.getElementId());
					} 
					else {
						elementList=elementService.getChildrenWithPath(parentId, userBean.getElementId());
					}
					
					request.setAttribute("ELEMENT_LIST",elementList);
					
				} catch (KmException e) {
					logger.error("Exception in listing ELEMENTS  "+e.getMessage());
				
				}
				logger.info(userBean.getUserLoginId() + " exited elementListing ");
				return initViewEdit(mapping, formBean, request, response);
		}
	public ActionForward initEditElement(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
				ActionMessages messages =new ActionMessages();
				ActionErrors errors = new ActionErrors();
				ActionForward forward = new ActionForward();
				HttpSession session = request.getSession();
				KmElementMstrFormBean formBean = (KmElementMstrFormBean) form;
				KmElementMstrService service=new KmElementMstrServiceImpl();
				KmUserMstr sessionUserBean = (KmUserMstr) session.getAttribute("USER_INFO");
				KmElementMstr dto=new KmElementMstr();
				
				try {
					logger.info(sessionUserBean.getUserLoginId()+" Entered into the initEditElement method of KmDocumentMstrAction");
					String elementId=formBean.getSelectedElementId();
					dto=service.getElementDto(elementId);
					formBean.setElementId(dto.getElementId());
					formBean.setElementName(dto.getElementName());
					formBean.setElementDesc(dto.getElementDesc());
					formBean.setElementStringPath(dto.getElementStringPath());
					formBean.setOldElementName(dto.getElementName());
					forward=mapping.findForward("editElement");
					
				}catch (Exception ex) {
					
					logger.error("Exception in deleting the requested Document: " + ex.getMessage());
					forward = mapping.findForward("editElement");
					} 

				return (forward);
				//return init(mapping,formBean,request,response);			
			} 		
	public ActionForward editElement(
				ActionMapping mapping,
				ActionForm form,
				HttpServletRequest request,
				HttpServletResponse response)
				throws Exception {
					ActionMessages messages =new ActionMessages();
					ActionErrors errors = new ActionErrors();
					ActionForward forward = new ActionForward();
					HttpSession session = request.getSession();
					KmElementMstrFormBean formBean = (KmElementMstrFormBean) form;
					KmElementMstrService service= new KmElementMstrServiceImpl();
					KmUserMstr sessionUserBean = (KmUserMstr) session.getAttribute("USER_INFO");
					KmElementMstr dto=new KmElementMstr();
					ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
					//List docList=null;
					try {
						logger.info(sessionUserBean.getUserLoginId()+" Entered into the editDocument method of KmDocumentMstrAction");
						String imagePath=bundle.getString("image.path");
						dto.setElementId(formBean.getElementId());
						dto.setElementDesc(formBean.getElementDesc());
						dto.setElementName(formBean.getElementName());
						service.editElement(dto);
						messages.add("msg1",new ActionMessage("element.update"));
						logger.info("Element Details edited successfully");
						if(!formBean.getElementName().equals(formBean.getOldElementName())){
							ImageCopy imgCopy=new ImageCopy();
							String imgPath=bundle.getString("image.path");
							StringBuffer path=new StringBuffer(request.getSession().getServletContext().getRealPath("/images/image/"));
							String oldImage=path.toString()+"img_"+formBean.getOldElementName()+".gif";
							String newImage=path+"img_"+formBean.getElementName()+".gif";
							logger.info(oldImage);
							boolean imageCopied=imgCopy.copy(oldImage,newImage);
						}
						saveMessages(request,messages);
					}catch (Exception ex) {
						
						errors.add("errors",new ActionError("element.update.error"));
						logger.error("Exception in editing the requested element: " + ex.getMessage());
						saveErrors(request, errors);
					} 
				
						return initViewEdit(mapping,formBean,request,response);			
					
				} 	

	public ActionForward deleteElement(
				ActionMapping mapping,
				ActionForm form,
				HttpServletRequest request,
				HttpServletResponse response)
				throws Exception {
					ActionMessages messages =new ActionMessages();
					ActionErrors errors = new ActionErrors();
					ActionForward forward = new ActionForward();
					HttpSession session = request.getSession();
					KmElementMstrFormBean formBean = (KmElementMstrFormBean) form;
					KmElementMstrService service= new KmElementMstrServiceImpl();
					KmUserMstr sessionUserBean = (KmUserMstr) session.getAttribute("USER_INFO");
					KmElementMstr dto=new KmElementMstr();
					KmUserMstrService userService = new KmUserMstrServiceImpl();
					ArrayList docList=null;
					try {
						logger.info(sessionUserBean.getUserLoginId()+" Entered into the deleteElement method of KmDocumentMstrAction");
						String elementId=formBean.getSelectedElementId();
						String updatedBy=sessionUserBean.getUserId();
						logger.info("Element Id of the Element to be deleted : "+elementId);
						int elementLevel=Integer.parseInt(service.getElementLevelId(elementId));
						if(elementLevel<=3){
							logger.info("Deletion cannot be made at a level at or before 3");
							// Bug solved :MASDB00064408 
							messages.add("msg11",new ActionMessage("element.deletetion.not.possible"));
							saveMessages(request,messages);
							return initViewEdit(mapping,formBean,request,response);	
						}
						
						/*
						 * BFR10 CSR07203 Element Deletion for Multiple Circles - Karan
						 */
						
						String[] multipleCircles = formBean.getCreateMultiple();
						int noOfUsers;
						boolean isFavourite;
						String elementPath = formBean.getElementPath();
						
						/*//System.out.println("\nelementPath "+elementPath);
						//System.out.println("\nelementLevel "+elementLevel);*/
						
						if(multipleCircles != null)
						{
							int successMessageCtr=0;
							String parentId;
							String[] elementIds = elementPath.split("/");
							
							/*for(int i = 0 ; i<elementIds.length ; i++)
							{
								//System.out.println("\nelementIds "+elementIds[i]);
							}*/
							String[] elementNames = new String[elementLevel - 3];
							boolean isValid;
								
							for(int x = 0 ; x < (elementLevel - 4) ; x++ )
							{
								elementNames[x]= service.getElementName(elementIds[x+3]);	
							}
							
							elementNames[elementNames.length - 1] = service.getElementName(elementId);
							
							/*for(int i = 0 ; i<elementNames.length ; i++)
							{
								//System.out.println("\nelementNames"+elementNames[i]);
							}*/
							
							for(int i =0 ; i< multipleCircles.length ; i++)
							{
								parentId = multipleCircles[i];
								isValid = false;	
								for(int j = 0 ; j <= (elementLevel - 4) ; j++)	
								{
											
									String[] elementStatus =
										service.checkExistingElement(elementNames[j],parentId);
									
									if(elementStatus[0].equals("true"))
									{
										parentId = elementStatus[1];
										if(j == (elementLevel - 4))
											isValid = true;
										else
											isValid = false;
									}
									else
									{	
										isValid = false;
										break;
									}	
										
								}//Checking Path for 1 Circle at a time
								if (isValid) 
								{
									elementId = parentId ;
									//System.out.println("\n ElementId To Be Deleted "+elementId);
									noOfUsers= userService.noOfElementUsers(elementId);
									isFavourite=userService.checkForFavourite(elementId);
									logger.info("No.OfEleemnt Users = "+noOfUsers+"     "+isFavourite);
									if(noOfUsers>0 || isFavourite==true)
									{
									  logger.info("Element cannot be deleted because user exists for the element");
									}
									else
									{
									  service.deleteElement(elementId,updatedBy);
									  ++successMessageCtr;
									}
								} 
							}//Bulk Category and Sub-Categories Creation for Multiple Circles	
							
							if(successMessageCtr > 0)
							{
								messages.add("msg11",new ActionMessage("element.deleted"));
								logger.info("Element deleted successfully");
							}
							else
							{
								messages.add("msg11",new ActionMessage("element.not.deleted"));
								logger.info("Element cannot be deleted because user exists for the element");
							}
							
						}//Should Be Executed if Multiple Circles are Selected
						
						else
						{
							noOfUsers= userService.noOfElementUsers(elementId);
							isFavourite=userService.checkForFavourite(elementId);
							logger.info("No.of Element Users = "+noOfUsers+"     "+isFavourite);
							if(noOfUsers>0 || isFavourite==true)
							{
							 messages.add("msg11",new ActionMessage("element.not.deleted"));
							 logger.info("Element cannot be deleted because user exists for the element");
							}
							else
							{
							 service.deleteElement(elementId,updatedBy);
							 messages.add("msg11",new ActionMessage("element.deleted"));
							 logger.info("Element deleted successfully");
						   }
						
						}//Should Be Executed if Multiple Circles are Not Selected
						
						formBean.setElementPath("");
						saveMessages(request,messages);
					}catch (Exception ex) {
						
						errors.add("errors",new ActionError("element.delete.error"));
						logger.error("Exception in deleting element: " + ex.getMessage());
						ex.printStackTrace();
						saveErrors(request, errors);
					} 
				
						return initViewEdit(mapping,formBean,request,response);			
					
				} 		
	// Adding by RAM
	public ActionForward initManageElementOrder(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response){
		HttpSession session = request.getSession();
		KmElementMstrFormBean formBean = (KmElementMstrFormBean) form;
		KmElementMstrService service= new KmElementMstrServiceImpl();
		KmUserMstr sessionUserBean = (KmUserMstr) session.getAttribute("USER_INFO");
		ArrayList<KmElementMstr> circleList= new ArrayList<KmElementMstr>();
		try {
			formBean.setMessage("");
			formBean.setElementsUnderCircle(null);
			formBean.setCircleList(null);
			formBean.setCircle(null);			
			logger.info(sessionUserBean.getUserLoginId()+" Entered into the initManageElementOrder method of KmDocumentMstrAction");
			String elementId=formBean.getSelectedElementId();
			String updatedBy=sessionUserBean.getUserId();
			circleList = service.getAllCircles();
			formBean.setCircleList(circleList);
		}
		catch(Exception ex)
		{
			logger.error("Exception in initiating manage element: " + ex.getMessage());
			ex.printStackTrace();
		}
		return mapping.findForward("manageElementSequence");
		} 
	public ActionForward findElementsInCircle(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response){
		HttpSession session = request.getSession();
		session = request.getSession(true);
		KmElementMstrFormBean formBean = (KmElementMstrFormBean) form;
		KmElementMstrService service= new KmElementMstrServiceImpl();
		KmUserMstr sessionUserBean = (KmUserMstr) session.getAttribute("USER_INFO");
		ArrayList<KmElementMstr> circleList= new ArrayList<KmElementMstr>();
		ArrayList<KmElementMstr> elementsUnderCircle= new ArrayList<KmElementMstr>();
		try {
			formBean.setMessage("");
			formBean.setElementsUnderCircle(null);
			formBean.setCircleList(null);
			logger.info(sessionUserBean.getUserLoginId()+" Entered into the findElementsInCircle method of KmDocumentMstrAction");
			String circleSelected=formBean.getCircle();
			logger.info("circle selected :"+circleSelected);
			String updatedBy=sessionUserBean.getUserId();
			circleList = service.getAllCircles();
			String favCategoryId = sessionUserBean.getFavCategoryId();
			elementsUnderCircle = service.findElementsInCircle(Integer.parseInt(circleSelected),favCategoryId);
			formBean.setCircleList(circleList);
			formBean.setElementsUnderCircle(elementsUnderCircle);
		}
		catch(Exception ex)
		{
			logger.error("Exception in finding elements under a circle element: " + ex.getMessage());
			ex.printStackTrace();
		}
		return mapping.findForward("manageElementSequence");
		}
	
	public ActionForward updateElementSequence(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response){
		HttpSession session = request.getSession();
		//KmElementMstrFormBean formBean = (KmElementMstrFormBean) form;
		KmElementMstrFormBean formBean = new KmElementMstrFormBean();
		KmElementMstrService service= new KmElementMstrServiceImpl();
		KmUserMstr sessionUserBean = (KmUserMstr) session.getAttribute("USER_INFO");
		String msg=null;
		String totalValues=null;
		try {
			totalValues = (String) request.getParameter("totalValues");
			logger.info(sessionUserBean.getUserLoginId()+" Entered into the updateElementSequence method of KmDocumentMstrAction");
			logger.info("totalValues :"+totalValues);
			msg=service.updateElementSequence(totalValues);
			formBean.setMessage("");
			formBean.setElementsUnderCircle(null);
			formBean.setCircleList(null);
			if(msg.equalsIgnoreCase("success"))formBean.setMessage("Sequence Updated Successfully !!");
			else formBean.setMessage("Sequence not Updated !! Please try again");
			logger.info("******** returning from action as message :"+formBean.getMessage());
		}
		catch(Exception ex)
		{
			logger.error("Exception in updating sequence of elements under a circle element: " + ex.getMessage());
			ex.printStackTrace();
		}
		return mapping.findForward("manageElementSequenceMsg");
		}
	// end of adding by RAM
}
