package com.ibm.km.actions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.ibm.km.common.Utility;
import com.ibm.km.dao.DBConnection;
import com.ibm.km.dto.KmDocumentMstr;
import com.ibm.km.dto.KmDynamicIndexedPageDto;
import com.ibm.km.dto.KmElementMstr;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.forms.KmDynamicIndexPageFormBean;
import com.ibm.km.services.KmDocumentService;
import com.ibm.km.services.KmDynamicIndexPageService;
import com.ibm.km.services.KmElementMstrService;
import com.ibm.km.services.impl.KmDocumentServiceImpl;
import com.ibm.km.services.impl.KmDynamicIndexPageServiceImpl;
import com.ibm.km.services.impl.KmElementMstrServiceImpl;

public class KmDynamicIndexPageAction extends DispatchAction{
	
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(KmDynamicIndexPageAction.class);
	}
	
	public ActionForward loadCategories(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		
		KmDynamicIndexPageFormBean formBean = (KmDynamicIndexPageFormBean)form ;
		ArrayList categories = new ArrayList();
		String firstiteration = "true";
		HttpSession session = request.getSession();
//		KmUserMstr userBean = (KmUserMstr)session.getAttribute("USER_INFO");
		String elementId = request.getParameter("categoryId");
		
		String actualElementId = request.getParameter("elementId");
		
		if(elementId == null) {
		KmElementMstrService sr = new KmElementMstrServiceImpl();
		String lobId = (String)session.getAttribute("CURRENT_LOB_ID");
		KmElementMstr dto = sr.getPanChild(lobId);
		elementId = dto.getElementId();
		}
		
//		logger.info("LOB=="+userBean.getLob());
		KmElementMstrService elementMstrService = new KmElementMstrServiceImpl();
		//System.out.println("categoryId in loadCategories=="+userBean.getElementId());
//		logger.info("Loading Categories: " + userBean.getElementId());
		logger.info("Category .. elementId: " + elementId);
		KmDynamicIndexPageService service = new KmDynamicIndexPageServiceImpl();
		categories = service.loadCategories(Integer.parseInt(elementId),firstiteration);
		
// change to display documemnt link in top menu	
		if(categories.size() == 0){
			// Validate if its a document with level 0
			KmDocumentService docService = new KmDocumentServiceImpl();
			 KmDocumentMstr documentDto = docService.getDocumentByElementId(elementId);
			 if(documentDto != null) {
				 // It is document with level 0; forward the request to its view type.
				 String url = Utility.getDocumentViewURL(documentDto.getDocumentId(), documentDto.getDocType());
				 request.setAttribute("url", url);
				 return new ActionForward(url, true);  // Its a document therefore forward to display the document
			 }
		}
		
		formBean.setCategories(categories);		// Its a category, so forward to display category and its children
		ArrayList<KmDynamicIndexedPageDto> orderedCategories =  new ArrayList<KmDynamicIndexedPageDto>();
		
		if(actualElementId!=null &&categories!=null )
		{
			int ii=1;
			for(Iterator itr= categories.iterator() ; itr.hasNext();)
			{
				KmDynamicIndexedPageDto kmDynamicIndexedPageDto = (KmDynamicIndexedPageDto)itr.next();
				
				if(actualElementId.equals(kmDynamicIndexedPageDto.getCategoryId()))
				{
					orderedCategories.add(0,kmDynamicIndexedPageDto);
				}
				else
				{
					orderedCategories.add(kmDynamicIndexedPageDto);
				}
			}
			 formBean.setCategories(orderedCategories);	
		}
		return mapping.findForward("success");
	}
	
	public ActionForward loadSubCategories(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
	
		String categoryId = request.getParameter("categoryId");
		String firstiteration = "true";
		if(request.getParameter("firstIteration") != null)
			firstiteration = request.getParameter("firstIteration");
		ArrayList subCategories = new ArrayList();
		KmDynamicIndexPageFormBean formBean = (KmDynamicIndexPageFormBean)form ;
/*		if(categoryId.contains("\'")){
			categoryId = categoryId.substring(1,categoryId.indexOf("\'",2));
		}
*/
		//System.out.println("categoryId in loadSubcategories=="+categoryId);
		KmDynamicIndexPageService service = new KmDynamicIndexPageServiceImpl();
		subCategories = service.loadCategories(Integer.parseInt(categoryId),firstiteration);
		
		if(subCategories.size() == 0 || ((KmDynamicIndexedPageDto)subCategories.get(0)).getCategories().size() == 0){
			// Validate if its a document with level 0
			KmDocumentService docService = new KmDocumentServiceImpl();
			 KmDocumentMstr documentDto = docService.getDocumentByElementId(categoryId);
			 if(documentDto != null) {
				 // It is document with level 0; forward the request to its view type.
				 String url = Utility.getDocumentViewURL(documentDto.getDocumentId(), documentDto.getDocType());
				 request.setAttribute("url", url);
				 return new ActionForward(url, true);  
			 }
			return mapping.findForward("comingsoon");
		}
		formBean.setCategories(subCategories);

		
		return mapping.findForward("success");
	}
	public ActionForward loadCategoryDescription(			
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {

		String query = "select Element_Desc from km_element_mstr where element_Id=?";
		String result = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
		conn = DBConnection.getDBConnection();
		pstmt = conn.prepareStatement(query);
		pstmt.setInt(1,Integer.parseInt(request.getParameter("elementId")));
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()){
			//System.out.println("rs.getString =="+rs.getString("Element_desc"));
			result = rs.getString("Element_desc");
		}
		} catch (Exception e) {

			
			e.printStackTrace();
			throw new Exception("Exception occured while updating file status  " + e.getMessage(),	e);
		} finally {
			try {
				DBConnection.releaseResources(conn, pstmt, null);
			} catch (Exception e) {
					e.printStackTrace();
					throw new Exception(e.getMessage(), e);
			}
		}
		
		
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Content-Type", "text/plain");
		response.getWriter().print(result);
		return null;
	}
		
}
