/*
 * Created on Nov 27, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.actions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.json.JSONObject;

import com.ibm.km.dto.FileReportDto;
import com.ibm.km.dto.KmElementMstr;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.KmFileReportFormBean;
import com.ibm.km.forms.KmUserReportFormBean;
import com.ibm.km.services.KmCategoryMstrService;
import com.ibm.km.services.KmElementMstrService;
import com.ibm.km.services.KmFileReportService;
import com.ibm.km.services.KmUserMstrService;
import com.ibm.km.services.KmUserReportService;
import com.ibm.km.services.impl.KmCategoryMstrServiceImpl;
import com.ibm.km.services.impl.KmElementMstrServiceImpl;
import com.ibm.km.services.impl.KmFileReportServiceImpl;
import com.ibm.km.services.impl.KmUserMstrServiceImpl;
import com.ibm.km.services.impl.KmUserReportServiceImpl;


/**
 * @author Harpreet 
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class KmUserReportAction  extends DispatchAction {

	/**
	 * Logger for the class.
	**/
	private static final Logger logger;

	static {
		logger = Logger.getLogger(KmUserReportAction.class);
	}
	
	public ActionForward initStatusRpt(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
					
					//		ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
							KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
					
							KmUserReportFormBean formBean = (KmUserReportFormBean)form;
							//KmFileReportFormBean formBean = (KmFileReportFormBean)form;
							KmElementMstrService kmElementService = new KmElementMstrServiceImpl();
							logger.info(userBean.getUserLoginId() + " Eentered initStatusReport method");
							//formBean.reset(mapping,request);
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
							formBean.setInitialSelectBox("-1");
										
											GregorianCalendar gc = new GregorianCalendar();
							            	String currDate = sdf.format(new java.util.Date());
											currDate = currDate.substring(0,10);
											String lastmonth="";
											gc.add(GregorianCalendar.DATE, -7);
											Date date1 = gc.getTime();
											String lastWeakDate  = sdf.format(date1);
											lastWeakDate = lastWeakDate.substring(0,10);
									        formBean.setFromDate(currDate);
									        formBean.setToDate(currDate);
									   
							
							try {
							
								
								List firstDropDown;
						 
									firstDropDown = kmElementService.getChildren(userBean.getElementId());
								    								
								if  (firstDropDown!=null && firstDropDown.size()!=0){
									 formBean.setInitialLevel(((KmElementMstr)firstDropDown.get(0)).getElementLevel());
								}
								else{
						
									int initialLevel=Integer.parseInt(kmElementService.getElementLevelId(userBean.getElementId()));
									initialLevel++;
									formBean.setInitialLevel(initialLevel+"");
								}
								formBean.setParentId(userBean.getElementId());
								request.setAttribute("elementLevelNames",kmElementService.getAllElementLevelNames());
								request.setAttribute("allParentIdString",kmElementService.getAllParentIdString("1",userBean.getElementId()));
								request.setAttribute("firstList",firstDropDown);
							} catch (KmException e) {
								logger.error("KmException in Loading page for Init Status Report: "+e.getMessage());
					
							} catch (Exception e) {
								logger.error("KmException in Loading page for Init Status Report: "+e.getMessage());
					
							}

							logger.info(userBean.getUserLoginId() + " exited Init Status Report");
							return mapping.findForward("userLoginReport");
				
					
						}					
	public ActionForward initReport(
						ActionMapping mapping,
						ActionForm form,
						HttpServletRequest request,
						HttpServletResponse response)
						throws Exception {
					
							ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
							KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
					
							KmUserReportFormBean formBean = (KmUserReportFormBean)form;
							logger.info(userBean.getUserLoginId() + " Eentered initreport method");
						//	formBean.reset(mapping,request);
							ArrayList partnerList = new ArrayList();
							formBean.setInitialSelectBox("-1");
							formBean.setSelectedPartnerName("");
							try {
							
								KmElementMstrService kmElementService = new KmElementMstrServiceImpl();
								List firstDropDown;
								KmUserMstrService kmUserMstrService=new KmUserMstrServiceImpl();
							 ArrayList agentList=(ArrayList)request.getAttribute("AGENT_LIST");
							  	firstDropDown = kmElementService.getChildren(userBean.getElementId());
								partnerList=kmUserMstrService.getPartnerName();
											formBean.setPartnerList(partnerList);
											//kmUserMstrformBean.setPartnerList(partnerList);			
								if (firstDropDown!=null && firstDropDown.size()!=0){
									formBean.setInitialLevel(((KmElementMstr)firstDropDown.get(0)).getElementLevel());
								}
								else{
						
									int initialLevel=Integer.parseInt(kmElementService.getElementLevelId(userBean.getElementId()));
									initialLevel++;
									formBean.setInitialLevel(initialLevel+"");
								}
								formBean.setParentId(userBean.getElementId());
								request.setAttribute("elementLevelNames",kmElementService.getAllElementLevelNames());
								request.setAttribute("allParentIdString",kmElementService.getAllParentIdString("1",userBean.getElementId()));
								request.setAttribute("firstList",firstDropDown);
							} catch (KmException e) {
								logger.error("KmException in Loading page for Init Report: "+e.getMessage());
					
							} catch (Exception e) {
								logger.error("Exception in Loading page for InitReport: "+e.getMessage());
					
							}

							logger.info(userBean.getUserLoginId() + " exited initreport method");
							return mapping.findForward("viewAgentWiseReport");
				
					
						}	
	public ActionForward loadElementDropDown(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response){
			String parentId = request.getParameter("ParentId");
			KmElementMstrService kmElementService = new KmElementMstrServiceImpl();

			try {
				JSONObject json = kmElementService.getElementsAsJsonNoPan(parentId);
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
	
				/**
				* Loads all categories under the selected circle for super admin user
				**/
				public ActionForward loadCategory(
					ActionMapping mapping,
					ActionForm form,
					HttpServletRequest request,
					HttpServletResponse response)
					throws Exception {

					ActionErrors errors = new ActionErrors();
					ActionForward forward = new ActionForward(); // return value
					KmFileReportFormBean KmFileReportFormBean =
						(KmFileReportFormBean) form;
						KmFileReportService kmFileReportService= new KmFileReportServiceImpl();
						KmCategoryMstrService kmCategoryService=new KmCategoryMstrServiceImpl();
					try{
						String circleId=KmFileReportFormBean.getCircleId();
						String[] circleIdArray = {circleId};
					//Loading the categories
						KmFileReportFormBean.setCategoryList(kmCategoryService.retrieveCategoryService(circleIdArray));
						//Retrieving circle wise files for Super Admin 
				
					}catch(Exception e){
						logger.error("Exception occured while loading categories and listing circle wise files:"+e);
					}return mapping.findForward("viewAgentWiseReport");
				}
				
	public ActionForward userLoginReport(
								ActionMapping mapping,
								ActionForm form,
								HttpServletRequest request,
								HttpServletResponse response)
								throws Exception {
									ActionErrors errors = new ActionErrors();
									ActionForward forward = new ActionForward(); // return value
									KmUserReportFormBean kmUserReportFormBean = (KmUserReportFormBean) form;
									KmUserReportService kmUserReportService= new KmUserReportServiceImpl();
									HttpSession session = request.getSession();
									KmUserMstr sessionUserBean = (KmUserMstr) session.getAttribute("USER_INFO");
									KmElementMstrService elementService = new KmElementMstrServiceImpl();
									
									ArrayList userList=null;
								try {
									
								
										logger.info( "Entered into the userLoginReport of KmReportMstrAction ");
							
										String elementId=kmUserReportFormBean.getParentId();
									    
										
										//edited by ashutosh
								   		//String toDate=kmUserReportFormBean.getToDate();
									   // String fromDate=kmUserReportFormBean.getFromDate();
									    String path="";
									  
									    
									    path=elementService.getAllParentNameString(sessionUserBean.getElementId(),elementId);
									   
									    kmUserReportFormBean.setElementPath(path);
									   // kmUserReportFormBean.setToDate1(toDate);
									//kmUserReportFormBean.setFromDate1(fromDate);
									
										userList=kmUserReportService.getUserLoginReport(elementId,kmUserReportFormBean.getElementLevel());
								        
								        if(userList.equals(null))
								        {
											logger.info( "Userlist Returned Null");
								        }
										
										
									kmUserReportFormBean.setElementId(kmUserReportFormBean.getParentId());
									request.setAttribute("LOGGED_IN_USER_LIST",userList);
									
									logger.info( "Exited from the  KmUserReportAction");
									//kmUserReportFormBean.setUserLoginList(userList);
									kmUserReportFormBean.setInitialLevel("");
			
									} catch (Exception e) {
										
									logger.error("Exception occured while listing report :"+e);
							
									}
							
									return initStatusRpt(mapping,kmUserReportFormBean,request,response);

								}		
		
	public ActionForward loginExcelReport(
									ActionMapping mapping,
									ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)
									throws Exception {
										ActionErrors errors = new ActionErrors();
										ActionForward forward = new ActionForward(); // return value
										KmUserReportFormBean kmUserReportFormBean = (KmUserReportFormBean) form;
										KmUserReportService kmUserReportService= new KmUserReportServiceImpl();
										HttpSession session = request.getSession();
										KmUserMstr sessionUserBean = (KmUserMstr) session.getAttribute("USER_INFO");
										KmElementMstrService elementService = new KmElementMstrServiceImpl();
										ArrayList userList=null;
									try {
									
								
											logger.info( "Entered into the loginExcelReport of KmReportMstrAction ");
							
											String elementId=kmUserReportFormBean.getElementId();
									
								   			
											//String eleLevelId=elementService.getElementLevelId(elementId);
								    //edited by ashutosh
											//String partner=kmUserReportFormBean.getPartnerName();
											//String toDate=kmUserReportFormBean.getToDate1();
											//String fromDate=kmUserReportFormBean.getFromDate1();
										
										String path="";
										
									    path=elementService.getAllParentNameString(sessionUserBean.getElementId(),elementId);
										 kmUserReportFormBean.setElementPath(path);
										userList=kmUserReportService.getUserLoginReport(elementId,kmUserReportFormBean.getElementLevel());
								        request.setAttribute("LOGGED_IN_USER_LIST",userList);
								       
									
										logger.info( "Exited from the  loginExcelReport");
										//kmUserReportFormBean.setUserLoginList(userList);
							
			
										} catch (Exception e) {
											e.printStackTrace();
										logger.error("Exception occured while listing report :"+e);
							
										}
							
										return mapping.findForward("loginExcelReport");
										

									}		
		
	public ActionForward circleWiseReport(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {

			
			KmUserReportFormBean formBean = (KmUserReportFormBean) form;
			KmUserReportService kmUserReportService= new KmUserReportServiceImpl();
			HttpSession session = request.getSession();
			KmUserMstr sessionUserBean = (KmUserMstr) session.getAttribute("USER_INFO");
			ArrayList circleList=null;
			try {
				
				
				logger.info("Inside circleWiseReport method");
				circleList=kmUserReportService.getcircleWisereport(sessionUserBean.getElementId(),sessionUserBean.getKmActorId(),formBean.getDate());
				
				formBean.setCircleWiseReport(circleList);
				formBean.setInitStatus("false");
						
				}
					
			catch (Exception e) {
				e.printStackTrace();
					logger.error("Exception occured while listing report :"+e);
							}
				

			return mapping.findForward("circleWiseReport");
                
			}
	
	
	public ActionForward initCircleWiseReport(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
				throws Exception {
		
	    KmUserReportFormBean formBean = (KmUserReportFormBean) form;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		try {
			
			GregorianCalendar gc = new GregorianCalendar();
        	String currDate = sdf.format(new java.util.Date());
			currDate = currDate.substring(0,10);
			
			formBean.setDate(currDate);
			formBean.setInitStatus("true");
		logger.info("Inside initCircleWiseReport method");
		
		logger.info("Exit from initCircleWiseReport method");
			}
					
		catch (Exception e) {
		logger.error("Exception occured while listing report :"+e);
		}
	return mapping.findForward("circleWiseReport");
       
	}		
	public ActionForward circleWiseExcelReport(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
				throws Exception {
		ActionErrors errors = new ActionErrors();
		ActionForward forward = new ActionForward(); // return value
	    KmUserReportFormBean formBean = (KmUserReportFormBean) form;
		KmUserReportService kmUserReportService= new KmUserReportServiceImpl();
		HttpSession session = request.getSession();
		KmUserMstr sessionUserBean = (KmUserMstr) session.getAttribute("USER_INFO");
		FileReportDto dto=null;
		ArrayList circleList=null;
		try {
		
		logger.info("Inside circleWiseExcelReport method");
		circleList=kmUserReportService.getcircleWisereport(sessionUserBean.getElementId(),sessionUserBean.getKmActorId(),formBean.getDate());
		request.setAttribute("CIRCLE_WISE_REPORT",circleList);
		logger.info("Exit from circleWiseExcelReport method");
			}
					
		catch (Exception e) {
		logger.error("Exception occured while listing report :"+e);
		}
	return mapping.findForward("circleWiseExcelReport");
       
	}		
	
	public ActionForward agentWiseReport(
								ActionMapping mapping,
								ActionForm form,
								HttpServletRequest request,
								HttpServletResponse response)
								throws Exception {
									ActionErrors errors = new ActionErrors();
									ActionForward forward = new ActionForward(); // return value
									KmUserReportFormBean kmUserReportFormBean = (KmUserReportFormBean) form;
									KmUserReportService kmUserReportService= new KmUserReportServiceImpl();
									//KmSubCategoryMstrService subCatService=new KmSubCategoryMstrServiceImpl();
									HttpSession session = request.getSession();
									KmUserMstr userBean = (KmUserMstr)session.getAttribute("USER_INFO");
									KmElementMstrService elementService = new KmElementMstrServiceImpl();
									ArrayList agentList=null;
								try {
									
								
										logger.info( "Entered into the agentwise report of KmReportMstrAction ");
							
									   String elementId=kmUserReportFormBean.getParentId();
									   String eleLevelId=elementService.getElementLevelId(elementId);
									   kmUserReportFormBean.setPartnerName(kmUserReportFormBean.getSelectedPartnerName());
									   String partner=kmUserReportFormBean.getPartnerName();
									   kmUserReportFormBean.setParentId1(elementId);
									   String path="";
									  
									   path=elementService.getAllParentNameString(userBean.getElementId(),elementId);
									   kmUserReportFormBean.setDocumentPath(path);
									   agentList=kmUserReportService.getAgentIdWiseReport(elementId,eleLevelId,partner);
								  
								     
											logger.info( "Exited from the agentwise report of KmReportMstrAction");
								   
								   				
									request.setAttribute("AGENT_LIST",agentList);
								    
							
			
									} catch (Exception e) {
										e.printStackTrace();
									logger.error("Exception occured while listing report :"+e);
							
									}
							
									return initReport(mapping,kmUserReportFormBean,request,response);

								}
	
	public ActionForward agentWiseExcelReport(
								ActionMapping mapping,
								ActionForm form,
								HttpServletRequest request,
								HttpServletResponse response)
								throws Exception {
										ActionErrors errors = new ActionErrors();
										ActionForward forward = new ActionForward(); // return value
										KmUserReportFormBean kmUserReportFormBean = (KmUserReportFormBean) form;
										KmUserReportService kmUserReportService= new KmUserReportServiceImpl();
										//KmSubCategoryMstrService subCatService=new KmSubCategoryMstrServiceImpl();
										HttpSession session = request.getSession();
										KmUserMstr userBean = (KmUserMstr)session.getAttribute("USER_INFO");
										KmElementMstrService elementService = new KmElementMstrServiceImpl();
										ArrayList agentList=null;
										try {
									
								
															logger.info( "Entered into the agentwiseExcel report of KmReportMstrAction ");
							
														String elementId=kmUserReportFormBean.getParentId1();									                  		   
														String eleLevelId=elementService.getElementLevelId(elementId);												           										    
														String partner=kmUserReportFormBean.getPartnerName();
														String path="";
														
														path=elementService.getAllParentNameString(userBean.getElementId(),elementId);
														kmUserReportFormBean.setDocumentPath(path);
														agentList=kmUserReportService.getAgentIdWiseReport(elementId,eleLevelId,partner);
								  
														
															logger.info( "Exited from the agentwise report of KmReportMstrAction");
														
								   				
															request.setAttribute("AGENT_LIST",agentList);
								    
							
			
														} catch (Exception e) {
															
														logger.error("Exception occured while listing report :"+e);
							
														}
			
														return mapping.findForward("agentWiseExcelReport" );

													}

	public ActionForward lockedUserReport(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
				KmUserReportFormBean kmUserReportFormBean = (KmUserReportFormBean) form;
				KmUserReportService kmUserReportService= new KmUserReportServiceImpl();
				ArrayList userList=null;
				KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
			try {
				
			
					logger.info( "Entered into the lockedUserReport of KmReportMstrAction ");
					userList=kmUserReportService.getLockedUserReport(userBean.getElementId());
			        
			        if(userList.equals(null))
			        {
						logger.info( "Userlist Returned Null");
			        }
					
					
				
				request.setAttribute("LOCKED_USER_LIST",userList);
				
				logger.info( "Exited from the  KmUserReportAction");
				//kmUserReportFormBean.setUserLoginList(userList);
				kmUserReportFormBean.setInitialLevel("");

				} catch (Exception e) {
					
				logger.error("Exception occured while listing report :"+e);
		
				}
		
				return mapping.findForward("lockedUserReport");

			}		

public ActionForward lockedUserReportExcel(
				ActionMapping mapping,
				ActionForm form,
				HttpServletRequest request,
				HttpServletResponse response)
				throws Exception {
				
					KmUserReportService kmUserReportService= new KmUserReportServiceImpl();
					ArrayList userList=null;
					KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
				try {
				
			
						logger.info( "Entered into the lockedUserReportExcel of KmReportMstrAction ");
		
					userList=kmUserReportService.getLockedUserReport(userBean.getElementId());
			        request.setAttribute("LOCKED_USER_LIST",userList);
			       
				
					logger.info( "Exited from the  loginExcelReport");
					//kmUserReportFormBean.setUserLoginList(userList);
		

					} catch (Exception e) {
						e.printStackTrace();
					logger.error("Exception occured while listing report :"+e);
		
					}
		
					return mapping.findForward("lockedUserReportExcel");
					

				}		

}
	