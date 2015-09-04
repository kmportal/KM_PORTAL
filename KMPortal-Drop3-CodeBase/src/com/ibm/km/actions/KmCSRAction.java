// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   KmCSRAction.java

package com.ibm.km.actions;

import com.ibm.km.common.Constants;
import com.ibm.km.common.PropertyReader;
import com.ibm.km.dto.KmElementMstr;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.KmCSRHomeBean;
import com.ibm.km.services.*;
import com.ibm.km.services.impl.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.servlet.http.*;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.apache.struts.actions.DispatchAction;

public class KmCSRAction extends DispatchAction
{
	
  	public KmCSRAction()
    {
        forward = new ActionForward();
    }

    public ActionForward viewCSRHome(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
{
    logger.info("Displaying CSR Home Page: CSRAction");
    HttpSession session = request.getSession();
    request.setAttribute("CURRENT_PAGE", "CSRHOME");
    session = request.getSession(true);
    KmUserMstr sessionUserBean = (KmUserMstr)session.getAttribute("USER_INFO");
    String favCategoryId = sessionUserBean.getFavCategoryId();
    if(sessionUserBean == null)
        logger.info("Session is invalid");
    try
    {
        String circleElementId = sessionUserBean.getElementId();
		logger.info("Getting CircleId as:" + request.getAttribute("circleID"));

        session.setAttribute("briefing", "2");
        KmElementMstrService elementService = new KmElementMstrServiceImpl();
        KmCSRHomeBean csrHomeBean = (KmCSRHomeBean)session.getAttribute("CSR_HOME_BEAN");

		logger.info(request.getParameter("fromHome")+"circleElementId:" +circleElementId);

        if(request.getParameter("fromHome") != null)
        { // This block gets executed when click on Home link.
            String lobId = elementService.getParentId(circleElementId);
            csrHomeBean = initializeCSRHomeBean(lobId, circleElementId, favCategoryId, sessionUserBean.isRestricted());
            if(lobId != null && !lobId.equals(""))
            	session.setAttribute("CURRENT_LOB_ID", lobId);
            	
/*                session.setAttribute("tickler1", csrHomeBean.getMessage());
			session.setAttribute("tickler2", csrHomeBean.getCircleScroller());
*/                session.setAttribute("tickler1", csrHomeBean.getTopScroller());
			session.setAttribute("tickler2", csrHomeBean.getBottomScroller());
			
            session.setAttribute("CURRENT_CIRCLE_ID", circleElementId);
            session.setAttribute("CSR_HOME_BEAN", csrHomeBean);
            session.setAttribute("CURRENT_PAN_ID", elementService.getPanChild(lobId).getElementId());

    		// Changes to show Top most viewed SOP and Product Files
            KmDocumentService documentService = new KmDocumentServiceImpl();
       		 ArrayList sopArray = null;
       		logger.info("circleElementId:sop array if condition===========================================");
       		 if(PropertyReader.getAppValue("most.viewed.include.panindia") != null && PropertyReader.getAppValue("most.viewed.include.panindia").equals("true"))
 			{
 				logger.info("circleElementId:sop array if condition" +csrHomeBean.getPanCircleId()+":"+circleElementId+":"+Constants.DOC_TYPE_SOP+":"+Constants.DOC_TYPE_SOP_BD);
 				sopArray = documentService.getTopDocuments(csrHomeBean.getPanCircleId()  + "," + circleElementId, Constants.DOC_TYPE_SOP+"," +Constants.DOC_TYPE_SOP_BD );
 			}
 				else
 				{
 					logger.info("circleElementId:sop array else condition" +csrHomeBean.getPanCircleId()+":"+circleElementId+":"+Constants.DOC_TYPE_SOP+":"+Constants.DOC_TYPE_SOP_BD);
 					sopArray = documentService.getTopDocuments(circleElementId, Constants.DOC_TYPE_SOP+"," +Constants.DOC_TYPE_SOP_BD );
 				}
             session.setAttribute("TOPBAR_LINKS", sopArray);
             ArrayList productArray = documentService.getTopDocuments(circleElementId, Constants.DOC_TYPE_PRODUCT+"");
             session.setAttribute("BOTTOMBAR_LINKS", productArray);
            
        } else
        {
        	logger.info("circleElementId:sop array if condition========22===================================");
        	if(csrHomeBean == null)
                csrHomeBean = initializeCSRHomeBean((String)session.getAttribute("CURRENT_LOB_ID"), circleElementId, favCategoryId, sessionUserBean.isRestricted());

            String currentCircleId = request.getParameter("circleId");
            if(currentCircleId == null)
            	currentCircleId = sessionUserBean.getElementId();

            String lobSelectionCircleId =  (String) request.getAttribute("circleID");
            
            if(lobSelectionCircleId != null && !lobSelectionCircleId.equals(""))
            	currentCircleId = lobSelectionCircleId;

            session.setAttribute("CURRENT_CIRCLE_ID", currentCircleId);
//            logger.info("22222222222222");
/*                KmDocumentService documentService = new KmDocumentServiceImpl();
            ArrayList sopArray = documentService.getTopDocuments(circleElementId, Constants.DOC_TYPE_SOP+"");
            session.setAttribute("TOPBAR_LINKS", sopArray);
            ArrayList productArray = documentService.getTopDocuments(circleElementId, Constants.DOC_TYPE_PRODUCT+"");
            session.setAttribute("BOTTOMBAR_LINKS", productArray);
            
*/
//            logger.info("csrHomeBean:" + csrHomeBean + " currentCircleId:" + currentCircleId + " favCategoryId:" + favCategoryId);
//            if(currentCircleId != null && !currentCircleId.equals("") && !csrHomeBean.getCircleId().equals(currentCircleId))
            csrHomeBean = resetCSRHomeBean(csrHomeBean, currentCircleId, favCategoryId, request);
            session.setAttribute("CSR_HOME_BEAN", csrHomeBean);
            request.setAttribute("SEARCH_TYPE", "search");

			/*                session.setAttribute("tickler1", csrHomeBean.getMessage());
			session.setAttribute("tickler2", csrHomeBean.getCircleScroller());
*/                session.setAttribute("tickler1", csrHomeBean.getTopScroller());
			session.setAttribute("tickler2", csrHomeBean.getBottomScroller());
        }
        forward = mapping.findForward(CSRHOME_SUCCESS);
    }
    catch(NumberFormatException e)
    {
    	e.printStackTrace();
        logger.error((new StringBuilder("NumberFormat Exception occurs in viewCSRHome: ")).append(e.getMessage()).toString());
        forward = mapping.findForward(CSR_FAILURE);
    }
    catch(KmException e)
    {
    	e.printStackTrace();
        logger.error((new StringBuilder(" KMException occurs in viewCSRHome: ")).append(e.getMessage()).toString());
        forward = mapping.findForward(CSR_FAILURE);
    }
    catch(Exception e)
    {
        e.printStackTrace();
    }
    return forward;
}

    
    //added by viswas
    public ActionForward viewCSRHome1(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
{
    logger.info("Displaying CSR Home Page:for UD CSRAction");
    logger.info("Displaying CSR Home Page:for UD CSRAction=====================================");
   
    HttpSession session = request.getSession();
    request.setAttribute("CURRENT_PAGE", "CSRHOME");
    session = request.getSession(true);
    KmUserMstr sessionUserBean = (KmUserMstr)session.getAttribute("USER_INFO");
    String favCategoryId = sessionUserBean.getFavCategoryId();
    if(sessionUserBean == null)
        logger.info("Session is invalid");
    try
    {
        String circleElementId = sessionUserBean.getElementId();
		logger.info("Getting CircleId as:" + request.getAttribute("circleID"));
		ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
	  	String postpoidolmid=bundle.getString("postpoidolmid");
	  	String prepoidolmid=bundle.getString("prepoidolmid");
	  	String UDlobId=bundle.getString("UDlobId");
	   
        session.setAttribute("briefing", "2");
        KmElementMstrService elementService = new KmElementMstrServiceImpl();
        KmCSRHomeBean csrHomeBean = (KmCSRHomeBean)session.getAttribute("CSR_HOME_BEAN");
       // System.out.println("circleElementId:sop array if condition========22===================================");
		logger.info(request.getParameter("fromHome")+"circleElementId:" +circleElementId);
		 String postpiad=(String)session.getAttribute("udUserOLMIDPost1");
  	   String prepaid=(String)session.getAttribute("udUserOLMIDPre");
  	 
  	
  	
  	String ud=(String)session.getAttribute("ud");
  	logger.info("UDDD"+ud+"pstpiad==="+postpiad+"postpoidolmid==="+postpoidolmid+"lobId===="+"UDlobId"+UDlobId);
  	System.out.println("UDDD"+ud+"pstpiad==="+postpiad+"postpoidolmid==="+postpoidolmid+"lobId===="+"UDlobId"+UDlobId);
   String lobId="";
    if(ud!=null || ud!=""){
   
    	if(("UDlogin").equals(ud))
     {
    	if(postpiad !=null && postpiad.equals(postpoidolmid))
    	{
    		lobId=UDlobId;
    	}
    		if(prepaid!=null && prepaid.equals(prepoidolmid))
    		{
    			lobId=UDlobId;
    		}
    		}
    }
    // System.out.println("LOb Iddddddddddd"+lobId);
     if(request.getParameter("fromHome") != null)
        { // This block gets executed when click on Home link.
             lobId = elementService.getParentId(circleElementId);
          
         //  System.out.println("circleElementId:sop array if condition========22=5=================================="+postpiad+":"+prepaid+":"+ud);  
           if(lobId != null && !lobId.equals(""))
           	session.setAttribute("CURRENT_LOB_ID", lobId);
           if(("UDlogin").equals(ud))
            {
        	 //  System.out.println("circleElementId:sop array if condition========224==================================="+postpiad+":"+prepaid+":"+ud);
        	   //for UD intregation
            	  csrHomeBean = initializeCSRHomeBean(lobId, circleElementId, favCategoryId, sessionUserBean.isRestricted(),postpiad,prepaid);
            }
          
           
            	
/*                session.setAttribute("tickler1", csrHomeBean.getMessage());
			session.setAttribute("tickler2", csrHomeBean.getCircleScroller());
*/                session.setAttribute("tickler1", csrHomeBean.getTopScroller());
			session.setAttribute("tickler2", csrHomeBean.getBottomScroller());
			
            session.setAttribute("CURRENT_CIRCLE_ID", circleElementId);
            session.setAttribute("CSR_HOME_BEAN", csrHomeBean);
            session.setAttribute("CURRENT_PAN_ID", elementService.getPanChild(lobId).getElementId());

    		// Changes to show Top most viewed SOP and Product Files
            KmDocumentService documentService = new KmDocumentServiceImpl();
       		 ArrayList sopArray = null;
       		logger.info("circleElementId:sop array if condition===========================================");
       		 if(PropertyReader.getAppValue("most.viewed.include.panindia") != null && PropertyReader.getAppValue("most.viewed.include.panindia").equals("true"))
 			{
 				logger.info("circleElementId:sop array if condition" +csrHomeBean.getPanCircleId()+":"+circleElementId+":"+Constants.DOC_TYPE_SOP+":"+Constants.DOC_TYPE_SOP_BD);
 				sopArray = documentService.getTopDocuments(csrHomeBean.getPanCircleId()  + "," + circleElementId, Constants.DOC_TYPE_SOP+"," +Constants.DOC_TYPE_SOP_BD );
 			}
 				else
 				{
 					logger.info("circleElementId:sop array else condition" +csrHomeBean.getPanCircleId()+":"+circleElementId+":"+Constants.DOC_TYPE_SOP+":"+Constants.DOC_TYPE_SOP_BD);
 					sopArray = documentService.getTopDocuments(circleElementId, Constants.DOC_TYPE_SOP+"," +Constants.DOC_TYPE_SOP_BD );
 				}
             session.setAttribute("TOPBAR_LINKS", sopArray);
             ArrayList productArray = documentService.getTopDocuments(circleElementId, Constants.DOC_TYPE_PRODUCT+"");
             session.setAttribute("BOTTOMBAR_LINKS", productArray);
            
        } else
        {
        	logger.info(ud+"circleElementId:sop array if condition========22==================================="+(String)session.getAttribute("CURRENT_LOB_ID")+":"+("UDlogin").equals(ud));
        	
        	 if(("UDlogin").equals(ud))
             {
         	   System.out.println("circleElementId:sop array if condition========224=5=================================="+postpiad+":"+prepaid+":"+ud+":"+lobId);
         	   //for UD intregation
         	  if(csrHomeBean == null)
         	   csrHomeBean = initializeCSRHomeBean(lobId, circleElementId, favCategoryId, sessionUserBean.isRestricted(),postpiad,prepaid);
             }
            else
            {
        	if(csrHomeBean == null)
                csrHomeBean = initializeCSRHomeBean((String)session.getAttribute("CURRENT_LOB_ID"), circleElementId, favCategoryId, sessionUserBean.isRestricted());
            }
            String currentCircleId = request.getParameter("circleId");
            if(currentCircleId == null)
            	currentCircleId = sessionUserBean.getElementId();

            String lobSelectionCircleId =  (String) request.getAttribute("circleID");
            
            if(lobSelectionCircleId != null && !lobSelectionCircleId.equals(""))
            	currentCircleId = lobSelectionCircleId;

            session.setAttribute("CURRENT_CIRCLE_ID", currentCircleId);
//            logger.info("22222222222222");
/*                KmDocumentService documentService = new KmDocumentServiceImpl();
            ArrayList sopArray = documentService.getTopDocuments(circleElementId, Constants.DOC_TYPE_SOP+"");
            session.setAttribute("TOPBAR_LINKS", sopArray);
            ArrayList productArray = documentService.getTopDocuments(circleElementId, Constants.DOC_TYPE_PRODUCT+"");
            session.setAttribute("BOTTOMBAR_LINKS", productArray);
            
*/
//            logger.info("csrHomeBean:" + csrHomeBean + " currentCircleId:" + currentCircleId + " favCategoryId:" + favCategoryId);
//            if(currentCircleId != null && !currentCircleId.equals("") && !csrHomeBean.getCircleId().equals(currentCircleId))
            csrHomeBean = resetCSRHomeBean(csrHomeBean, currentCircleId, favCategoryId, request);
            session.setAttribute("CSR_HOME_BEAN", csrHomeBean);
            request.setAttribute("SEARCH_TYPE", "search");

			/*                session.setAttribute("tickler1", csrHomeBean.getMessage());
			session.setAttribute("tickler2", csrHomeBean.getCircleScroller());
*/                session.setAttribute("tickler1", csrHomeBean.getTopScroller());
			session.setAttribute("tickler2", csrHomeBean.getBottomScroller());
        }
        forward = mapping.findForward(CSRHOME_SUCCESS);
    }
    catch(NumberFormatException e)
    {
    	e.printStackTrace();
        logger.error((new StringBuilder("NumberFormat Exception occurs in viewCSRHome: ")).append(e.getMessage()).toString());
        forward = mapping.findForward(CSR_FAILURE);
    }
    catch(KmException e)
    {
    	e.printStackTrace();
        logger.error((new StringBuilder(" KMException occurs in viewCSRHome: ")).append(e.getMessage()).toString());
        forward = mapping.findForward(CSR_FAILURE);
    }
    catch(Exception e)
    {
        e.printStackTrace();
    }
    return forward;
}
    //end by viswas
    public KmCSRHomeBean initializeCSRHomeBean(String lobId, String circleId, String favCategoryId, boolean isRestricted)
        throws KmException
    {
        logger.info("initializeCSRHomeBean: LOB:" + lobId + "   circleId" + circleId);
        KmCSRHomeBean csrHomeBean = new KmCSRHomeBean();
        logger.info("initializeCSRHomeBean: LOB:" + lobId + "   circleId" + circleId +"UD==" +csrHomeBean.getUD());
        try
        {
        	
            KmElementMstrService elementService = new KmElementMstrServiceImpl();
            String parentId = elementService.getParentId(circleId);
            String panChildId = null;
            
            KmElementMstr panChild = elementService.getPanChild(parentId);
            if(panChild != null)
            panChildId = panChild.getElementId();
            String logic=csrHomeBean.getUD();
          
           
           
            if(isRestricted)
            {
                ArrayList lobList = elementService.getAllChildren("1");
                KmElementMstr element = new KmElementMstr();
                for(Iterator iter = lobList.iterator(); iter.hasNext();)
                {
                    element = (KmElementMstr)iter.next();
                    if(element.getElementId().equals(lobId))
                        break;
                }

                lobList = new ArrayList();
                lobList.add(element);
                csrHomeBean.setLobList(lobList);
            } else
            {
                csrHomeBean.setLobList(elementService.getAllChildren("1"));
            }
            csrHomeBean.setCircleId(circleId);
            if(isRestricted)
            {
                ArrayList circleList = elementService.getAllChildren(lobId);
                KmElementMstr element = new KmElementMstr();
                for(Iterator iter = circleList.iterator(); iter.hasNext();)
                {
                    element = (KmElementMstr)iter.next();
                    if(element.getElementId().equals(circleId))
                        break;
                }

                circleList = new ArrayList();
                circleList.add(element);
                csrHomeBean.setCirclelist(circleList);
            } else
            {
                csrHomeBean.setCirclelist(elementService.getAllChildren(lobId));
            }
           
            csrHomeBean.setCircleName(getCircleName(circleId, csrHomeBean.getCirclelist()));
            csrHomeBean.setPanCircleId(panChildId);
            csrHomeBean.setLobId(parentId);
            csrHomeBean.setCategoryList(elementService.getChildren(circleId));
            if(panChildId != null)
            csrHomeBean.setPanIndiaCategoryMap(elementService.retrieveCategoryMap(panChildId, null));
            csrHomeBean.setCircleCategoryMap(elementService.retrieveCategoryMap(circleId, favCategoryId));
            //System.out.println(" category map********"+csrHomeBean.getCircleCategoryMap());
// SET Scroller
            KmScrollerMstrService scrollerService = new KmScrollerMstrServiceImpl();
//            csrHomeBean.setCircleScroller(scrollerService.getScrollerMessage(circleId));
            csrHomeBean.setBottomScroller(scrollerService.getScrollerMessage(circleId));
            String topSroller = scrollerService.getScrollerMessage("1");
            topSroller = topSroller + scrollerService.getScrollerMessage(lobId);
//            csrHomeBean.setMessage(lobSroller);
            csrHomeBean.setTopScroller(topSroller);
            
            int circleIdArray[] = {
                Integer.parseInt(circleId)
            };
            KmDocumentService documentService = new KmDocumentServiceImpl();
            csrHomeBean.setDocumentList(documentService.retrieveCSRDocumentList(circleIdArray));
        }
        catch(NumberFormatException e)
        {
            logger.error((new StringBuilder("NumberFormat Exception occurs in initializeCSRHomeBean: ")).append(e.getMessage()).toString());
            e.printStackTrace();
        }
        catch(Exception e)
        {
            logger.error((new StringBuilder(" Exception occurs in initializeCSRHomeBean: ")).append(e.getMessage()).toString());
            e.printStackTrace();
        }
        return csrHomeBean;
    }

    
    //added by vishwas for Ud intregation
    public KmCSRHomeBean initializeCSRHomeBean(String lobId, String circleId, String favCategoryId, boolean isRestricted,String Postpaid,String prepaid)
    throws KmException
{
    logger.info("initializeCSRHomeBean: LOB:" + lobId + "   circleId" + circleId);
    KmCSRHomeBean csrHomeBean = new KmCSRHomeBean();
    logger.info("initializeCSRHomeBean: LOB:" + lobId + "   circleId" + circleId +"UD==" +csrHomeBean.getUD());
    try
    {
    	
        KmElementMstrService elementService = new KmElementMstrServiceImpl();
        String parentId = elementService.getParentId(circleId);
        String panChildId = null;
        
        KmElementMstr panChild = elementService.getPanChild(parentId);
        if(panChild != null)
        panChildId = panChild.getElementId();
        String logic=csrHomeBean.getUD();
       
        ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
      	String postpoidolmid=bundle.getString("postpoidolmid");
      	String prepoidolmid=bundle.getString("prepoidolmid");
      	String UDlobId=bundle.getString("UDlobId");
       
        
    	   
    	   if(("B0071323_POST").equals(Postpaid))
    	   {
    		  // System.out.println("UD testingggggggggggg3");
    		   csrHomeBean.setLobList(elementService.getAllChildrenUD("B0071323_POST"));
    	   csrHomeBean.setCirclelist(elementService.getAllChildrencircleUD("B0071323_POST"));
    	   }
    	  if(("B0071323_PRE").equals(prepaid))
    	   {
    		  // System.out.println("UD testingggggggggggg4");
    		   csrHomeBean.setLobList(elementService.getAllChildrenUD("B0071323_PRE"));
    		   csrHomeBean.setCirclelist(elementService.getAllChildrencircleUD("B0071323_PRE"));
    		   
    	   }
       
    	 // System.out.println(csrHomeBean.getCirclelist().size()+"circle name");
    	 
    	  csrHomeBean.setCircleId(circleId);
        csrHomeBean.setCircleName(getCircleName(circleId, csrHomeBean.getCirclelist()));
        csrHomeBean.setPanCircleId(panChildId);
        csrHomeBean.setLobId(parentId);
        csrHomeBean.setCategoryList(elementService.getChildren(circleId));
        logger.info("panchilddddddddd"+panChildId);
        logger.info("testin ud====================="+((KmElementMstr)csrHomeBean.getCirclelist().get(0)).getElementId());
       // if(panChildId != null)
        csrHomeBean.setPanIndiaCategoryMap(elementService.retrieveCategoryMap(((KmElementMstr)csrHomeBean.getCirclelist().get(0)).getElementId(), null));
        logger.info("testin ud=====================+++"+((KmElementMstr)csrHomeBean.getCirclelist().get(0)).getElementId());
         csrHomeBean.setCircleCategoryMap(elementService.retrieveCategoryMap(((KmElementMstr)csrHomeBean.getCirclelist().get(0)).getElementId(), favCategoryId));
       // System.out.println(" category map********"+csrHomeBean.getCircleCategoryMap());
//SET Scroller
        KmScrollerMstrService scrollerService = new KmScrollerMstrServiceImpl();
//        csrHomeBean.setCircleScroller(scrollerService.getScrollerMessage(circleId));
        csrHomeBean.setBottomScroller(scrollerService.getScrollerMessage(circleId));
        String topSroller = scrollerService.getScrollerMessage("1");
       // topSroller = topSroller + scrollerService.getScrollerMessage(lobId);
//        csrHomeBean.setMessage(lobSroller);
        csrHomeBean.setTopScroller(topSroller);
        
        int circleIdArray[] = {
            Integer.parseInt(circleId)
        };
        KmDocumentService documentService = new KmDocumentServiceImpl();
        csrHomeBean.setDocumentList(documentService.retrieveCSRDocumentList(circleIdArray));
    }
    catch(NumberFormatException e)
    {
        logger.error((new StringBuilder("NumberFormat Exception occurs in initializeCSRHomeBean: ")).append(e.getMessage()).toString());
        e.printStackTrace();
    }
    catch(Exception e)
    {
        logger.error((new StringBuilder(" Exception occurs in initializeCSRHomeBean: ")).append(e.getMessage()).toString());
        e.printStackTrace();
    }
    return csrHomeBean;
}


    
    //end by vishwas for Ud intregation
    private String getCircleName(String circleId, ArrayList list)
    {
        String circleName = "";
        for(Iterator i = list.iterator(); i.hasNext();)
        {
            KmElementMstr element = (KmElementMstr)i.next();
            if(circleId.equals(element.getElementId()))
                circleName = element.getElementName();
        }

        return circleName;
    }

    public KmCSRHomeBean resetCSRHomeBean(KmCSRHomeBean csrHomeBean, String currentCircleId, String favCategoryId, HttpServletRequest request)
        throws KmException
    {
        KmElementMstrService elementService = new KmElementMstrServiceImpl();
        KmDocumentService documentService = new KmDocumentServiceImpl();
        try
        {
            logger.info((new StringBuilder("Resetting the Category Map and Document List for Circle: ")).append(currentCircleId).toString());
            csrHomeBean.setCircleId(currentCircleId);
            csrHomeBean.setCategoryList(elementService.getChildren(currentCircleId));
            csrHomeBean.setCircleName(getCircleName(currentCircleId, csrHomeBean.getCirclelist()));
            csrHomeBean.setSubCategoryList(null);
            csrHomeBean.setCircleCategoryMap(elementService.retrieveCategoryMap(currentCircleId, favCategoryId));
            int circleIdArray[] = {
                Integer.parseInt(currentCircleId)
            };
            csrHomeBean.setDocumentList(documentService.retrieveCSRDocumentList(circleIdArray));
/*            KmScrollerMstrService scrollerService = new KmScrollerMstrServiceImpl();
            csrHomeBean.setMessage(scrollerService.getScrollerMessage(currentCircleId));
*/
            HttpSession session = request.getSession();
            KmUserMstr sessionUserBean = (KmUserMstr)session.getAttribute("USER_INFO");
            boolean isRestricted = sessionUserBean.isRestricted();
    		String lobId =""; 
    		 String postpiad=(String)session.getAttribute("udUserOLMIDPost1");
        	   String prepaid=(String)session.getAttribute("udUserOLMIDPre");
        	    String ud=(String)session.getAttribute("ud");
        	    if(("UDlogin").equals(ud))
                {
               	if(("B0071323_POST").equals(postpiad))
               	{
               		lobId="185794";
               	}
               		if(("B0071323_PRE").equals(prepaid))
               		{
               			lobId="185794";
               		}
               	 //csrHomeBean = 
               	initializeCSRHomeBean(lobId, currentCircleId, favCategoryId, sessionUserBean.isRestricted(),postpiad,prepaid);	
                }
        	    else
        	    {
    		lobId=(String)session.getAttribute("CURRENT_LOB_ID");
        	    	//if(lobId != null && !lobId.equals(""))
    		initializeCSRHomeBean(lobId, currentCircleId, favCategoryId, isRestricted);
        	    }
   		
    	//System.out.println("lobbbbbb"+lobId);
        	    //	KmElementMstr panChild = elementService.getPanChild(lobId);
    		ArrayList<KmElementMstr> al=elementService.getPanChild1(lobId);
    		String tempPanChildAndCircleId = currentCircleId;
    		for(KmElementMstr kem:al)
    		{
    			logger.info(tempPanChildAndCircleId+"==========ffffffff==================="+ kem.getElementId());
    			if(kem != null) {
        			tempPanChildAndCircleId = tempPanChildAndCircleId + " , "+ kem.getElementId();
    		}
    		}
    		//if(panChild != null) {
    			//tempPanChildAndCircleId = tempPanChildAndCircleId + " , "+ panChild.getElementId();
    		//}
    		
    		// Changes to show Top most viewed SOP and Product Files
    		 ArrayList sopArray = null;
			//System.out.println("for check link data==============="+tempPanChildAndCircleId+":"+Constants.DOC_TYPE_SOP+":"+Constants.DOC_TYPE_SOP_BD);
    		 if(PropertyReader.getAppValue("most.viewed.include.panindia") != null && PropertyReader.getAppValue("most.viewed.include.panindia").equals("true"))
				sopArray = documentService.getTopDocuments(tempPanChildAndCircleId, Constants.DOC_TYPE_SOP+"," +Constants.DOC_TYPE_SOP_BD );
			else
				//System.out.println("for check link data2==============="+currentCircleId+":"+Constants.DOC_TYPE_SOP+":"+Constants.DOC_TYPE_SOP_BD);
				sopArray = documentService.getTopDocuments(currentCircleId, Constants.DOC_TYPE_SOP+"," +Constants.DOC_TYPE_SOP_BD );
			
            session.setAttribute("TOPBAR_LINKS", sopArray);
            ArrayList productArray = documentService.getTopDocuments(currentCircleId, Constants.DOC_TYPE_PRODUCT+"");
            session.setAttribute("BOTTOMBAR_LINKS", productArray);

            
         // SET Scroller
            KmScrollerMstrService scrollerService = new KmScrollerMstrServiceImpl();
//            csrHomeBean.setCircleScroller(scrollerService.getScrollerMessage(circleId));
            csrHomeBean.setBottomScroller(scrollerService.getScrollerMessage(currentCircleId));
            String topSroller = scrollerService.getScrollerMessage("1");
            topSroller = topSroller + scrollerService.getScrollerMessage(lobId);
//            csrHomeBean.setMessage(lobSroller);
            csrHomeBean.setTopScroller(topSroller);
            
        }
        catch(NumberFormatException e)
        {
        	e.printStackTrace();
            logger.error((new StringBuilder("NumberFormat Exception occurs in resetCSRHomeBean: ")).append(e.getMessage()).toString());
        }
        catch(Exception e)
        {
        	e.printStackTrace();
            logger.error((new StringBuilder("Exception occurs in resetCSRHomeBean: ")).append(e.getMessage()).toString());
        }
        return csrHomeBean;
    }

    public void resetCSRHomeBean(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws KmException
    {
        if(request.getParameter("CIRCLE_ID") != null)
        {
            String circleId = request.getParameter("CIRCLE_ID");
            int circleIdArray[] = {
                Integer.parseInt(circleId)
            };
            KmCSRHomeBean csrHomeBean = new KmCSRHomeBean();
            KmDocumentService documentService = new KmDocumentServiceImpl();
            csrHomeBean.setDocumentList(documentService.retrieveCSRDocumentList(circleIdArray));
        } else
        {
            logger.info("request parameter CIRCLE_ID is null");
        }
    }

    public ActionForward getSubCategories(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws KmException
    {
        String catId = request.getParameter("catId");
        KmElementMstrService service = new KmElementMstrServiceImpl();
        ArrayList subCatList = service.getAllChildren(catId);
        ArrayList newSubCatList = new ArrayList();
        int j = 1;
        for(Iterator iter = subCatList.iterator(); iter.hasNext();)
        {
            KmElementMstr element = (KmElementMstr)iter.next();
            if(j > 8)
                newSubCatList.add(element);
            j++;
        }

        request.setAttribute("SUB_CATEGORY_LIST", newSubCatList);
        return mapping.findForward("displaySubCategories");
    }

    public static Logger logger = Logger.getLogger(KmCSRAction.class);
    private ActionForward forward;
    private static String CSRHOME_SUCCESS = "csrHome";
    private static String CSR_FAILURE = "csrFailure";

}
