// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   KmSearchAction.java

package com.ibm.km.actions;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import com.ibm.km.common.Constants;
import com.ibm.km.common.ResourceProperty;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.ibm.km.common.PropertyReader;
import com.ibm.km.dto.KmElementMstr;
import com.ibm.km.dto.KmSearch;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.KmCSRHomeBean;
import com.ibm.km.forms.KmDocumentHitsCountFormBean;
import com.ibm.km.forms.KmSearchFormBean;
import com.ibm.km.forms.KmSubCategoryMstrFormBean;
import com.ibm.km.services.KmDocumentHitsCountService;
import com.ibm.km.services.KmDocumentService;
import com.ibm.km.services.KmElementMstrService;
import com.ibm.km.services.KmLinkMstrService;
import com.ibm.km.services.KmScrollerMstrService;
import com.ibm.km.services.KmSearchService;
import com.ibm.km.services.impl.KeywordMgmtServiceImpl;
import com.ibm.km.services.impl.KmDocumentHitsCountServiceImpl;
import com.ibm.km.services.impl.KmDocumentServiceImpl;
import com.ibm.km.services.impl.KmElementMstrServiceImpl;
import com.ibm.km.services.impl.KmLinkMstrServiceImpl;
import com.ibm.km.services.impl.KmScrollerMstrServiceImpl;
import com.ibm.km.services.impl.KmSearchServiceImpl;
import com.ibm.km.services.impl.KmSubCategoryMstrServiceImpl;

public class KmSearchAction extends DispatchAction
{

    public KmSearchAction()
    {
        logger = Logger.getLogger(KmSearchAction.class);
    }

    public ActionForward loadCircle(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        KmSearchFormBean kmSearchFormBean = (KmSearchFormBean)form;
        KmElementMstrService eleService = new KmElementMstrServiceImpl();
        HttpSession session = request.getSession();
        KmCSRHomeBean csrHomeBean = (KmCSRHomeBean)session.getAttribute("CSR_HOME_BEAN");
        KmUserMstr userBean = (KmUserMstr)session.getAttribute("USER_INFO");
        try
        {
            logger.info(" Entered into the loadCircle method of KmSearchAction: " + userBean.getUserLoginId());
            String lobId = kmSearchFormBean.getSelectedLob();
             if(lobId.equals(null) || lobId.equals("")){
            	lobId = request.getParameter("lobId");
            }
            //System.out.println("lobId.."+lobId);
            session.setAttribute("CURRENT_LOB_ID", lobId);
            csrHomeBean.setLobId(lobId);
            csrHomeBean.setCirclelist(eleService.getAllChildren(lobId));
            if(request.getAttribute("fromHome") != null)
                csrHomeBean.setCircleId(userBean.getElementId());
            
            // Check added to check java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
            if(csrHomeBean.getCirclelist() != null && csrHomeBean.getCirclelist().size() > 0) {
                csrHomeBean.setCircleId(((KmElementMstr)csrHomeBean.getCirclelist().get(0)).getElementId());
            }

            session.setAttribute("CURRENT_CIRCLE_ID", csrHomeBean.getCircleId());
            logger.info(" CURRENT_LOB_ID: " + lobId + " CURRENT_CIRCLE_ID "  + csrHomeBean.getCircleId() );
            
            KmElementMstr panElement = eleService.getPanChild(lobId);
            if(panElement != null)
            {
                String panCircle = panElement.getElementId();
                csrHomeBean.setPanIndiaCategoryMap(eleService.retrieveCategoryMap(panCircle, null));
                csrHomeBean.setCircleCategoryMap(eleService.retrieveCategoryMap(csrHomeBean.getCircleId(), userBean.getFavCategoryId()));
                session.setAttribute("CURRENT_PAN_ID", panCircle);
            } else
            {
                csrHomeBean.setPanIndiaCategoryMap(null);
                csrHomeBean.setCircleCategoryMap(eleService.retrieveCategoryMap(csrHomeBean.getCircleId(), userBean.getFavCategoryId()));
                session.setAttribute("CURRENT_PAN_ID", "");
            }
            KmScrollerMstrService scrollerService = new KmScrollerMstrServiceImpl();
            csrHomeBean.setMessage(scrollerService.getScrollerMessage(csrHomeBean.getCircleId()));
            
            
            ArrayList linksList = new ArrayList();
            ArrayList topBarLinks = new ArrayList();
			ArrayList bottomBarLinks = new ArrayList();
			
			if(userBean.getKmActorId().equals(Constants.CIRCLE_CSR) || userBean.getKmActorId().equals(Constants.CATEGORY_CSR)){
				
				KmLinkMstrService linkMstrService = new KmLinkMstrServiceImpl();
				linksList = linkMstrService.viewLinks(lobId);
				KmDocumentHitsCountFormBean bean = new KmDocumentHitsCountFormBean();
				bean.setLobId(Integer.parseInt(lobId));
				KmDocumentHitsCountService documentHitsService = new KmDocumentHitsCountServiceImpl();
				topBarLinks = documentHitsService.getTopBarLinks(bean);
				bottomBarLinks = documentHitsService.getBottomBarLinks(bean);
			}
			
			session.setAttribute("LINKS_LIST",linksList);
/*			session.setAttribute("TOPBAR_LINKS",topBarLinks);
			session.setAttribute("BOTTOMBAR_LINKS",bottomBarLinks);
*/
			
            KmDocumentService documentService = new KmDocumentServiceImpl();
            ArrayList sopArray = documentService.getTopDocuments(csrHomeBean.getCircleId(), Constants.DOC_TYPE_SOP+"");
            if(sopArray != null && sopArray.size() > 0)
            session.setAttribute("TOPBAR_LINKS", sopArray);
            ArrayList productArray = documentService.getTopDocuments(csrHomeBean.getCircleId(), Constants.DOC_TYPE_PRODUCT+"");
            if(productArray != null && productArray.size() > 0)
            session.setAttribute("BOTTOMBAR_LINKS", productArray);
            
			request.setAttribute("circleID", csrHomeBean.getCircleId());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            logger.info(e);
        }
        //return mapping.findForward("initSearch");
        return mapping.findForward("viewCSRHome");
    }

    public ActionForward loadCategory(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        KmSearchFormBean kmSearchFormBean = (KmSearchFormBean)form;
        HttpSession session = request.getSession();
        KmCSRHomeBean csrHomeBean = (KmCSRHomeBean)session.getAttribute("CSR_HOME_BEAN");
        KmUserMstr userBean = (KmUserMstr)session.getAttribute("USER_INFO");
        try
        {
            logger.info(" Entered into the loadCategory method1 of KmSearchAction and searchCircleID:" + kmSearchFormBean.getSearchCircleId());
            csrHomeBean.setCircleId(kmSearchFormBean.getSearchCircleId());
            session.setAttribute("CURRENT_CIRCLE_ID", kmSearchFormBean.getSearchCircleId());
        }
        catch(Exception e)
        {
        	e.printStackTrace();
            logger.info(e);
        }
        if(request.getAttribute("DOCUMENT_LIST") == null)
            return mapping.findForward("initSearch");
        if(request.getAttribute("DOCUMENT_LIST").toString().equals("ERROR"))
        {
            request.setAttribute("DOCUMENT_LIST", null);
            return mapping.findForward("csrContentSearch");
        } else
        {
            return mapping.findForward("csrContentSearch");
        }
    }

    public ActionForward loadSubCategory(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ActionErrors errors = new ActionErrors();
        ActionForward forward = new ActionForward();
        KmSearchFormBean kmSearchFormBean = (KmSearchFormBean)form;
        KmSearchService service = new KmSearchServiceImpl();
        KmElementMstrService eleService = new KmElementMstrServiceImpl();
        HttpSession session = request.getSession();
        KmCSRHomeBean csrHomeBean = (KmCSRHomeBean)session.getAttribute("CSR_HOME_BEAN");
        KmUserMstr userBean = (KmUserMstr)session.getAttribute("USER_INFO");
        KmSubCategoryMstrFormBean subCategoryBean = new KmSubCategoryMstrFormBean();
        try
        {
            kmSearchFormBean.setSubCategoryId("");
            logger.info((new StringBuilder("Category ID: ")).append(kmSearchFormBean.getCategoryId()).append("fsdfds").toString());
            if(kmSearchFormBean.getCategoryId().equals(""))
            {
                kmSearchFormBean.setSubCategoryId("");
                csrHomeBean.setSubCategoryList(null);
                csrHomeBean.setSubSubCategoryList(null);
            } else
            {
                kmSearchFormBean.setSubCategoryId("");
                csrHomeBean.setSubCategoryList(eleService.getChildren(kmSearchFormBean.getCategoryId()));
                csrHomeBean.setSubSubCategoryList(null);
            }
        }
        catch(Exception e)
        {
        	e.printStackTrace();
            logger.info(e);
        }
        return mapping.findForward("initSearch");
    }

    public ActionForward loadSubSubCategory(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ActionErrors errors = new ActionErrors();
        ActionForward forward = new ActionForward();
        KmSearchFormBean kmSearchFormBean = (KmSearchFormBean)form;
        KmSearchService service = new KmSearchServiceImpl();
        KmElementMstrService eleService = new KmElementMstrServiceImpl();
        HttpSession session = request.getSession();
        KmCSRHomeBean csrHomeBean = (KmCSRHomeBean)session.getAttribute("CSR_HOME_BEAN");
        KmUserMstr userBean = (KmUserMstr)session.getAttribute("USER_INFO");
        KmSubCategoryMstrFormBean subCategoryBean = new KmSubCategoryMstrFormBean();
        try
        {
            logger.info(" Entered into the loadSubCategory method of KmSearchAction ");
            kmSearchFormBean.setSubSubCategoryId("");
            if(kmSearchFormBean.getSubCategoryId().equals(""))
            {
                kmSearchFormBean.setSubSubCategoryId("");
                csrHomeBean.setSubSubCategoryList(null);
            } else
            {
                kmSearchFormBean.setSubSubCategoryId("");
                csrHomeBean.setSubSubCategoryList(eleService.getChildren(kmSearchFormBean.getSubCategoryId()));
            }
        }
        catch(Exception e)
        {
        	e.printStackTrace();
            logger.info(e);
        }
        return mapping.findForward("initSearch");
    }

    public ActionForward getCategory(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        HttpSession session = request.getSession();
        KmUserMstr userBean = (KmUserMstr)session.getAttribute("USER_INFO");
        try
        {
            Document document = DocumentHelper.createDocument();
            Element root = document.addElement("options");
            String Id = "";
            String cond = "";
            Id = request.getParameter("Id");
            cond = request.getParameter("cond");
            ActionForward forward = new ActionForward();
            KmSearchService masterService = new KmSearchServiceImpl();
            KmSearchFormBean formBean = (KmSearchFormBean)form;
            ArrayList arrGetValue = new ArrayList();
            arrGetValue = masterService.getChange(Id, cond, formBean.getCategoryId());
            logger.info((new StringBuilder("FormBean CatId: ")).append(formBean.getCategoryId()).append(" Id: ").append(Id).toString());
            if(cond.equalsIgnoreCase("subcategory"))
            {
                logger.info((new StringBuilder("ArrayList Size: ")).append(arrGetValue.size()).toString());
                if(arrGetValue != null && arrGetValue.size() != 0 && Id != null)
                {
                    for(int counter = 0; counter < arrGetValue.size(); counter++)
                    {
                        Element optionElement = root.addElement("option");
                        formBean = (KmSearchFormBean)arrGetValue.get(counter);
                        if(formBean != null)
                        {
                            optionElement.addAttribute("value", formBean.getSubCategoryId());
                            optionElement.addAttribute("text", formBean.getSubCategoryName());
                        } else
                        {
                            optionElement.addAttribute("value", "None");
                            optionElement.addAttribute("text", "None");
                        }
                    }

                } else
                {
                    Element optionElement = root.addElement("option");
                    optionElement.addAttribute("value", "");
                    optionElement.addAttribute("text", "");
                }
            }
            response.setContentType("text/xml");
            response.setHeader("Cache-Control", "No-Cache");
            PrintWriter out = response.getWriter();
            OutputFormat outputFormat = OutputFormat.createCompactFormat();
            XMLWriter writer = new XMLWriter(out);
            writer.write(document);
            writer.println();
            writer.flush();
            out.flush();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
            logger.info(e);
        }
        return null;
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ActionErrors errors;
        KmSearchFormBean kmSearchFormBean;
        KmElementMstrService elementService;
        HttpSession session;
        KmUserMstr userBean;
        KmCSRHomeBean csrHomeBean;
        errors = new ActionErrors();
        ActionForward forward = new ActionForward();
        kmSearchFormBean = (KmSearchFormBean)form;
        // iPortal_Enhancements changes for complete string search; CSR20111025-00-06938:BFR2
        kmSearchFormBean.setKeyword(kmSearchFormBean.getSearchContentHidden());
        // End of Change

      //  System.out.println("Search Mode Checked: "+kmSearchFormBean.getSearchModeChecked());
        
        
        
        
       // KmSearchService service = new KmSearchServiceImpl();
       // com.ibm.km.services.KmSubCategoryMstrService subCatService = new KmSubCategoryMstrServiceImpl();
        elementService = new KmElementMstrServiceImpl();
        session = request.getSession();
        userBean = (KmUserMstr)session.getAttribute("USER_INFO");
        csrHomeBean = (KmCSRHomeBean)session.getAttribute("CSR_HOME_BEAN");
        ArrayList documentList = new ArrayList();
        KmSearch dto;
        
    
        logger.info((new StringBuilder(String.valueOf(userBean.getUserLoginId()))).append(" Entered into the search method of KmSearchAction ").toString());
        dto = new KmSearch();
        
        if("on".equals(kmSearchFormBean.getDateCheck()))
		{
        	dto.setFromDate(kmSearchFormBean.getSearchFromDt());
        	dto.setToDate(kmSearchFormBean.getSearchToDt());
        	dto.setDateCheck(kmSearchFormBean.getDateCheck());
		}
        
        String panCircle = (String)session.getAttribute("CURRENT_PAN_ID");
        if(panCircle == null)
            panCircle = csrHomeBean.getPanCircleId();
        dto.setPanCircle(panCircle);
        if(kmSearchFormBean.getKeyword() == null || kmSearchFormBean.getKeyword().equals(""))
        {
            request.setAttribute("DOCUMENT_LIST", null);
            return mapping.findForward("csrContentSearch");
        }
        try
        {
            dto.setKeyword(kmSearchFormBean.getKeyword());
            dto.setSearchType("CSR_CONTENT");
            dto.setElementId(csrHomeBean.getCircleId());
            dto.setActorId(userBean.getKmActorId());
            KmSearchService searchService = new KmSearchServiceImpl();
            dto.setCircleId(userBean.getCircleId());
           // ArrayList documentList;
            dto.setSearchModeChecked(kmSearchFormBean.getSearchModeChecked());
            
            	
            	if(PropertyReader.getAppValue("SINGLE.INDEX").equals("Y")) {
                    documentList = searchService.contentSearch(dto);
                }else {
                    documentList = searchService.contentSearchCSR(dto);
                }
            
            
          //Code Added by Karan BFR28 iPortal Project 07203
			
			String keyword = dto.getKeyword();
			keyword = keyword.substring(1,(keyword.length()-1));
			
			if(documentList != null)
			{ if(documentList.size() > 0)
				{
				KeywordMgmtServiceImpl keyService = new KeywordMgmtServiceImpl();
					try{	
						keyService.insertKeyword(keyword);
					  }catch (Exception e) {
						  e.printStackTrace();
						logger.info("duplicate string found while registering keyword");
				    }
				}
			}	
			
			//Code Addition ends here
            
            kmSearchFormBean.setCategoryId("");
            kmSearchFormBean.setSubCategoryId("");
            kmSearchFormBean.setKeyword(keyword);
            kmSearchFormBean.setSubSubCategoryId("");
            kmSearchFormBean.setDateCheck("");
            kmSearchFormBean.setSearchFromDt(kmSearchFormBean.getSearchFromDt());
            kmSearchFormBean.setSearchToDt(kmSearchFormBean.getSearchToDt());
            csrHomeBean.setCategoryList(elementService.getChildren(csrHomeBean.getCircleId()));
            csrHomeBean.setSubCategoryList(null);
            csrHomeBean.setSubSubCategoryList(null);
            if(documentList == null)
                request.setAttribute("DOCUMENT_LIST", "ERROR");
            else
                request.setAttribute("DOCUMENT_LIST", documentList);
            
            if(request.getAttribute("DOCUMENT_LIST")==null)
            	kmSearchFormBean.setInitStatus("true");	
			else
				kmSearchFormBean.setInitStatus("false");
            
        }
        catch(KmException e)
        {
            //e.printStackTrace();
            errors.add("msg", new ActionError("contentSearch.exception"));
            saveErrors(request, errors);
           // ActionForward forward;
            if(userBean.getKmActorId().equals(Constants.CIRCLE_CSR) || userBean.getKmActorId().equals(Constants.CATEGORY_CSR))
                forward = mapping.findForward("csrContentSearch");
            else
                forward = mapping.findForward("searchFiles");
            request.setAttribute("DOCUMENT_LIST", "ERROR");
        }
        catch(Exception e)
        {
           // e.printStackTrace();
            request.setAttribute("DOCUMENT_LIST", "ERROR");
            logger.info(e);
        }
        return loadCategory(mapping, kmSearchFormBean, request, response);
    }
    
    public ActionForward searchLocation(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
{
    	logger.info("asa::::::::::searchLocation");
    ActionErrors errors;
    KmSearchFormBean kmSearchFormBean;
    KmElementMstrService elementService;
    HttpSession session;
    KmUserMstr userBean;
    KmCSRHomeBean csrHomeBean;
    errors = new ActionErrors();
    ActionForward forward = new ActionForward();
    kmSearchFormBean = (KmSearchFormBean)form;
    KmSearchService service = new KmSearchServiceImpl();
    com.ibm.km.services.KmSubCategoryMstrService subCatService = new KmSubCategoryMstrServiceImpl();
    elementService = new KmElementMstrServiceImpl();
    session = request.getSession();
    userBean = (KmUserMstr)session.getAttribute("USER_INFO");
    csrHomeBean = (KmCSRHomeBean)session.getAttribute("CSR_HOME_BEAN");
    
    
    ArrayList documentList = new ArrayList();
    
    logger.info("asa::content::"+kmSearchFormBean.getSearchContentHidden());
    KmSearch dto;
    logger.info(" Entered into the search method of KmSearchAction: " + userBean.getUserLoginId() );
    dto = new KmSearch();
    String panCircle = (String)session.getAttribute("CURRENT_PAN_ID");
    kmSearchFormBean.setKeyword(kmSearchFormBean.getSearchContentHidden());
    logger.info("asa:::::"+kmSearchFormBean.getKeyword());
    if(panCircle == null)
        panCircle = csrHomeBean.getPanCircleId();
    dto.setPanCircle(panCircle);
    if(kmSearchFormBean.getKeyword() == null || kmSearchFormBean.getKeyword().equals(""))
    {
    	logger.info("if:::asa::::kmSearchFormBean.getKeyword()::");
        request.setAttribute("DOCUMENT_LIST", null);
        return mapping.findForward("csrNetworkFaultSearch");
    }
    
  
    try
    {
    	logger.info("asa::::kmSearchFormBean.getKeyword()::"+kmSearchFormBean.getKeyword());
        dto.setKeyword(kmSearchFormBean.getKeyword());
        dto.setElementId(csrHomeBean.getCircleId());
        dto.setSearchType("CSR_CONTENT");
        logger.info((new StringBuilder("Circle for Content search ")).append(dto.getElementId()).toString());
        dto.setActorId(userBean.getKmActorId());
        KmSearchService searchService = new KmSearchServiceImpl();
        dto.setCircleId(userBean.getCircleId());
       // ArrayList documentList;
       
       List networkFaultList = searchService.locationSearch(dto);
             
       
        	
        kmSearchFormBean.setCategoryId("");
        kmSearchFormBean.setSubCategoryId("");
        kmSearchFormBean.setKeyword("");
        kmSearchFormBean.setSubSubCategoryId("");
        csrHomeBean.setCategoryList(elementService.getChildren(csrHomeBean.getCircleId()));
        csrHomeBean.setSubCategoryList(null);
        csrHomeBean.setSubSubCategoryList(null);
        if(documentList == null)
            request.setAttribute("DOCUMENT_LIST", "ERROR");
        else
            request.setAttribute("DOCUMENT_LIST", networkFaultList);
        kmSearchFormBean.setSearchMode("LocationSearch");
    }
    catch(KmException e)
    {
        e.printStackTrace();
        errors.add("msg", new ActionError("contentSearch.exception"));
        saveErrors(request, errors);
       // ActionForward forward;
        if(userBean.getKmActorId().equals(Constants.CIRCLE_CSR) || userBean.getKmActorId().equals(Constants.CATEGORY_CSR))
            forward = mapping.findForward("csrContentSearch");
        else
            forward = mapping.findForward("searchFiles");
        request.setAttribute("DOCUMENT_LIST", "ERROR");
    }
    catch(Exception e)
    {
        e.printStackTrace();
        request.setAttribute("DOCUMENT_LIST", "ERROR");
        logger.info(e);
    }
    return loadNetworkFaultPage(mapping, kmSearchFormBean, request, response);
}

    public ActionForward loadNetworkFaultPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
{
    KmSearchFormBean kmSearchFormBean = (KmSearchFormBean)form;
    HttpSession session = request.getSession();
    KmCSRHomeBean csrHomeBean = (KmCSRHomeBean)session.getAttribute("CSR_HOME_BEAN");
    KmUserMstr userBean = (KmUserMstr)session.getAttribute("USER_INFO");
    try
    {
        logger.info((new StringBuilder(String.valueOf(userBean.getUserLoginId()))).append(" Entered into the loadCategory method3 of KmSearchAction ").toString());
        logger.info(kmSearchFormBean.getSearchCircleId());
        csrHomeBean.setCircleId(kmSearchFormBean.getSearchCircleId());
        session.setAttribute("CURRENT_CIRCLE_ID", kmSearchFormBean.getSearchCircleId());
        request.setAttribute("SEARCH_MODE", kmSearchFormBean.getSearchMode());
    }
    catch(Exception e)
    {
    	e.printStackTrace();
        logger.info(e);
    }
    if(request.getAttribute("DOCUMENT_LIST") == null)
        return mapping.findForward("initSearch");
    if(request.getAttribute("DOCUMENT_LIST").toString().equals("ERROR"))
    {
        request.setAttribute("DOCUMENT_LIST", null);
        return mapping.findForward("csrNetworkFaultSearch");
    } else
    {
    	//System.out.println("------->");
        return mapping.findForward("csrNetworkFaultSearch");
    }
}
    
    public KmSearch beanToDto(KmSearchFormBean formBean)
    {
        KmSearch dto = new KmSearch();
        return dto;
    }

    Logger logger;
}
