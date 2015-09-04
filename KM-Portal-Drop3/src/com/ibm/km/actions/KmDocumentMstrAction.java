package com.ibm.km.actions;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import com.ibm.km.dto.KmSearchDetailsDTO;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import org.json.JSONArray;
import org.json.JSONObject;

import com.ibm.km.common.FileCopy;
import com.ibm.km.common.KmDisplayFile;
import com.ibm.km.common.PropertyReader;
import com.ibm.km.common.UserDetails;
import com.ibm.km.common.Utility;
import com.ibm.km.dto.KmAlertMstr;
import com.ibm.km.dto.KmDocumentMstr;
import com.ibm.km.dto.KmElementMstr;
import com.ibm.km.dto.KmSearch;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.KmCSRHomeBean;
import com.ibm.km.forms.KmDocumentMstrFormBean;
import com.ibm.km.forms.KmMoveDocumentFormBean;
import com.ibm.km.search.DeleteFiles;
import com.ibm.km.services.KmAlertMstrService;
import com.ibm.km.services.KmCategoryMstrService;
import com.ibm.km.services.KmCircleMstrService;
import com.ibm.km.services.KmDocumentService;
import com.ibm.km.services.KmElementMstrService;
import com.ibm.km.services.KmSearchService;
import com.ibm.km.services.KmSubCategoryMstrService;
import com.ibm.km.services.LoginService;
import com.ibm.km.services.impl.KeywordMgmtServiceImpl;
import com.ibm.km.services.impl.KmAlertMstrServiceImpl;
import com.ibm.km.services.impl.KmCategoryMstrServiceImpl;
import com.ibm.km.services.impl.KmCircleMstrServiceImpl;
import com.ibm.km.services.impl.KmDocumentServiceImpl;
import com.ibm.km.services.impl.KmElementMstrServiceImpl;
import com.ibm.km.services.impl.KmSearchServiceImpl;
import com.ibm.km.services.impl.KmSubCategoryMstrServiceImpl;
import com.ibm.km.services.impl.LoginServiceImpl;
import com.ibm.km.sms.SendSMSXML;
import com.ibm.km.common.Constants;

/**
 * @version 1.0edi
 * @author Anil
 */

public class KmDocumentMstrAction extends DispatchAction {
	/**
	 * Logger for the class.
	 **/
	private static final Logger logger;

	static {

		logger = Logger.getLogger(KmDocumentMstrAction.class);
	}

	/**
	 * Initializes the search file page by loading all the circles for super
	 * admin. For Circle approvers and circle users it loads all categories of
	 * the curresponding circle
	 **/
	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionForward forward = new ActionForward(); // return value
		KmDocumentMstrFormBean formBean = (KmDocumentMstrFormBean) form;
		ResourceBundle bundle = ResourceBundle
				.getBundle("ApplicationResources");
		HttpSession session = request.getSession();
		KmUserMstr userBean = (KmUserMstr) session.getAttribute("USER_INFO");
		try {

			logger.info(userBean.getUserLoginId()
					+ " Entered into the init method of KmDocumentMstrAction");
			session.setAttribute("SEARCH_TYPE", "ADMIN_ELEMENT");

			/* CSRF Implementation */
			saveToken(request);

			KmElementMstrService kmElementService = new KmElementMstrServiceImpl();
			List firstDropDown;
			if (userBean.getKmActorId().equals(bundle.getString("LOBAdmin"))
					|| userBean.getKmActorId().equals(
							bundle.getString("Superadmin"))) {
				firstDropDown = kmElementService.getAllChildren(userBean
						.getElementId());
			} else {
				firstDropDown = kmElementService.getChildren(userBean
						.getElementId());
			}
			if (firstDropDown != null && firstDropDown.size() != 0) {
				formBean.setInitialLevel(((KmElementMstr) firstDropDown.get(0))
						.getElementLevel());
			} else {

				int initialLevel = Integer.parseInt(kmElementService
						.getElementLevelId(userBean.getElementId()));
				initialLevel++;
				formBean.setInitialLevel(initialLevel + "");
			}
			formBean.setParentId(userBean.getElementId());
			if (userBean.getKmActorId().equals(Constants.SUPER_ADMIN)
					|| userBean.getKmActorId().equals(Constants.CIRCLE_ADMIN)
					|| userBean.getKmActorId().equals(Constants.LOB_ADMIN)) {
				formBean.setDownloadAccess("Y");
			} else {
				formBean.setDownloadAccess("N");
			}

			request.setAttribute("elementLevelNames", kmElementService
					.getAllElementLevelNames());
			request.setAttribute("allParentIdString", kmElementService
					.getAllParentIdString("1", userBean.getElementId()));
			request.setAttribute("firstList", firstDropDown);
			if (request.getAttribute("FILE_LIST") == null)
				formBean.setInitStatus("true");
			else
				formBean.setInitStatus("false");
			logger.info(formBean.getInitStatus());
			forward = mapping.findForward("viewFiles");
		} catch (Exception e) {

			logger
					.error("Exception occured while initializing the search file page :"
							+ e);

		}
		// Finish with
		return (forward);

	}

	public ActionForward viewFiles(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionErrors errors = new ActionErrors();
		ActionForward forward = new ActionForward();
		KmDocumentMstrFormBean formBean = (KmDocumentMstrFormBean) form;
		HttpSession session = request.getSession();
		KmUserMstr sessionUserBean = (KmUserMstr) session
				.getAttribute("USER_INFO");
		KmElementMstrService eleService = new KmElementMstrServiceImpl();
		KmSearchService service = new KmSearchServiceImpl();
		ArrayList fileList = new ArrayList();
		KmSearch searchDto = new KmSearch();
		try {

			// Security finding trans token impl...
			/*
			 * if(!isTokenValid(request)) { logger.info("Token Invalid ...");
			 * forward = mapping.findForward("viewDocument");
			 * errors.add("errors.incorrectPassword",new
			 * ActionError("msg.security.id021")); saveErrors(request, errors);
			 * return forward; }
			 */

			logger
					.info(sessionUserBean.getUserLoginId()
							+ " Entered into the viewFiles method of KmDocumentMstrAction");
			logger.info("INSIDE VIEW FILES");
			String parentId = formBean.getParentId();
			formBean.setKmActorId(sessionUserBean.getKmActorId());
			String elementPath = null;
			if (!sessionUserBean.getElementId().equals(parentId)) {
				elementPath = eleService.getAllParentNameString(
						(sessionUserBean.getElementId()), parentId);
				logger.info("The element path is before:" + elementPath);
				elementPath = elementPath.substring(
						elementPath.indexOf(">") + 1, elementPath.length());
				logger.info("The element path is:" + elementPath);
			}
			formBean.setElementPath(elementPath);

			// formBean.setElementPath(eleService.getAllParentNameString(
			// sessionUserBean.getElementId(),parentId));
			logger.info("elementId   " + parentId);
			searchDto.setElementId(parentId);
			searchDto.setActorId(sessionUserBean.getKmActorId());
			searchDto.setKeyword("showAllDocuments");
			fileList = service.search(searchDto);
			request.setAttribute("FILE_LIST", fileList);
			formBean.setSelectedParentId(formBean.getParentId());
			formBean.setInitialSelectBox("-1");
			logger.info("Listing the files");

		} catch (Exception e) {
			logger.error("Exception occured while listing files :" + e);

		}
		// Finish with
		return init(mapping, formBean, request, response);

	}

	public ActionForward displayDocument(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ActionErrors errors = new ActionErrors();
		ActionForward forward = new ActionForward();
		HttpSession session = request.getSession();
		KmUserMstr sessionUserBean = (KmUserMstr) session
				.getAttribute("USER_INFO");
		KmDocumentMstrFormBean formBean = (KmDocumentMstrFormBean) form;
		KmDocumentMstr documentBean = new KmDocumentMstr();
		KmDisplayFile fileDisplay = new KmDisplayFile();
		ResourceBundle bundle = ResourceBundle
				.getBundle("ApplicationResources");
		StringBuffer displayFilePath = new StringBuffer(bundle
				.getString("folder.path"));
		KmDocumentService service = new KmDocumentServiceImpl();
		String documentPath = "";

		StringBuffer tempFilePath;
		String returnPath = null;
		try {
			logger
					.info(sessionUserBean.getUserLoginId()
							+ " Entered into the displayDocument method of KmDocumentMstrAction");
			String[] documentID = new String[1];
			//session.setAttribute("LOGIN_ACTOR_ID",sessionUserBean.getKmActorId
			// ());
			String loginActorId = sessionUserBean.getKmActorId();
			String userId = sessionUserBean.getUserId();
			formBean.setLoginActorId(loginActorId);

			if (request.getAttribute("DOCUMENT") != null) {
				documentBean = (KmDocumentMstr) request
						.getAttribute("DOCUMENT");
				documentID[0] = documentBean.getDocumentId();
				documentPath = documentBean.getDocumentPath() + "/"
						+ documentBean.getDocumentName();
				request.setAttribute("subCatId", (String) request
						.getAttribute("subCatId"));
			} else if (request.getParameter("docID") != null) {
				documentID[0] = request.getParameter("docID").toString();
				documentPath = service.getDocPath(documentID[0]);
			}

			// Changes made to improve performance
			if (documentID[0] == null) {
				return null;
			}

			request.setAttribute("DOCUMENT_LIST", null);
			displayFilePath = new StringBuffer(bundle.getString("folder.path"));
			displayFilePath.append(documentPath);
			// File f= new File(displayFilePath.toString());

			logger.info("Document Path :" + displayFilePath);
			request.setAttribute("File_Data", returnFile(displayFilePath
					.toString()));
			tempFilePath = new StringBuffer(request.getSession()
					.getServletContext().getRealPath("/Docs/"));
			returnPath = fileDisplay.displayFile(displayFilePath.toString(),
					tempFilePath.toString());
			String favoriteStatus = ""
					+ service.checkForFavorite(sessionUserBean.getUserId(),
							documentID[0]);
			formBean.setFavorite(favoriteStatus);
			request.setAttribute("docID", documentID[0]);
			request.setAttribute("CURRENT_PAGE", "DOCUMENT_HOME");
			// request.setAttribute("docID",request.getParameter("docID"));
			if (loginActorId.equals(Constants.CIRCLE_CSR)
					|| loginActorId.equals(Constants.CATEGORY_CSR))
				forward = mapping.findForward("csrViewDocument");
			else
				forward = mapping.findForward("viewDocument");
			formBean.setFilePath(returnPath);
			logger.info("RETTurn File Path : " + formBean.getFilePath());
			if (returnPath == null) {
				logger.error("No File Found in displayFile Method");
				errors.add("name", new ActionError("km.file.exception"));
				saveErrors(request, errors);
				logger.info("Restuen path is null");
				request.setAttribute("CURRENT_PAGE", "DOCUMENT_ERROR");
				if (loginActorId.equals(Constants.CIRCLE_CSR)
						|| loginActorId.equals(Constants.CATEGORY_CSR))
					forward = mapping.findForward("csrViewDocument");
				else
					forward = mapping.findForward("viewDocument");
			} else {
				if (loginActorId.equals(Constants.CIRCLE_CSR)
						|| loginActorId.equals(Constants.CATEGORY_CSR)) {
					documentBean = service.increaseDocHitCount(documentID[0],
							userId);
				}
			}
			response.setHeader("Cache-Control", "No-Cache");
			request.setAttribute("DOCUMENT_PATH", returnPath);

			// long diff=new Date().getTime()-dt1.getTime();
			// logger.info("Execution Time : "+diff);
		} catch (Exception e) {
			e.printStackTrace();
			logger
					.error("IOException occured in displayFile "
							+ e.getMessage());
			errors.add("name", new ActionError("km.file.exception"));
			saveErrors(request, errors);
			request.setAttribute("CURRENT_PAGE", "DOCUMENT_ERROR");
			if (formBean.getLoginActorId().equals(Constants.CIRCLE_CSR))
				forward = mapping.findForward("csrViewDocument");
			else
				forward = mapping.findForward("viewDocument");
		}
		session.setAttribute("LOGIN_ACTOR_ID", sessionUserBean.getKmActorId());
		return forward;
	}

	public ActionForward showHelpDocument(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ActionErrors errors = new ActionErrors();
		ActionForward forward = new ActionForward();
		HttpSession session = request.getSession();
		KmUserMstr sessionUserBean = (KmUserMstr) session
				.getAttribute("USER_INFO");
		KmDocumentMstrFormBean formBean = (KmDocumentMstrFormBean) form;
		KmDisplayFile fileDisplay = new KmDisplayFile();

		String filePath = "";
		String returnPath = "";
		String tempFilePath = "";
		File f = null;
		try {
			filePath = PropertyReader.getAppValue("folder.path_help")
					+ sessionUserBean.getKmActorId();
			File directory = new File(filePath);
			// //System.out.println(filePath);
			f = directory.listFiles()[0];
			tempFilePath = request.getSession().getServletContext()
					.getRealPath("/Docs/");
			returnPath = fileDisplay.displayFile(f.getPath().toString(),
					tempFilePath.toString());

			logger.info("Document Path :" + f.getAbsolutePath());

			if (sessionUserBean.getKmActorId().equals(Constants.CIRCLE_CSR)
					|| sessionUserBean.getKmActorId().equals(
							Constants.CATEGORY_CSR))
				forward = mapping.findForward("csrViewHelp");
			else
				forward = mapping.findForward("adminViewHelp");
			formBean.setFilePath(returnPath);

			response.setHeader("Cache-Control", "No-Cache");
			request.setAttribute("DOCUMENT_PATH", returnPath);
			request.setAttribute("CURRENT_PAGE", "DOCUMENT_HOME");

			// long diff=new Date().getTime()-dt1.getTime();
			// logger.info("Execution Time : "+diff);
		} catch (Exception e) {
			e.printStackTrace();
			logger
					.error("IOException occured in displayFile "
							+ e.getMessage());
			errors.add("name", new ActionError("km.file.exception"));
			saveErrors(request, errors);
			request.setAttribute("CURRENT_PAGE", "DOCUMENT_ERROR");
			if (sessionUserBean.getKmActorId().equals(Constants.CIRCLE_CSR))
				forward = mapping.findForward("csrViewDocument");
			else
				forward = mapping.findForward("viewDocument");
		} finally {

		}
		session.setAttribute("LOGIN_ACTOR_ID", sessionUserBean.getKmActorId());
		return forward;
	}

	public ActionForward displayVersions(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		KmDocumentService service = new KmDocumentServiceImpl();
		HttpSession session = request.getSession();
		KmUserMstr userBean = (KmUserMstr) session.getAttribute("USER_INFO");

		ArrayList documentList = new ArrayList();
		try {
			logger
					.info(userBean.getUserLoginId()
							+ " Entered into the displayVersions method of KmDocumentMstrAction ");

			String documentId = request.getParameter("docID").toString();
			logger.info("DOCC ID::" + documentId);
			documentList = service.getDocumentVersions(documentId);
			request.setAttribute("DOCUMENT_LIST", documentList);
			request.setAttribute("DOCUMENT_ID", documentId);
		} catch (Exception e) {

			logger.error(" Exception occurs in displayVersions: "
					+ e.getMessage());
			// Report the error using the appropriate name and ID.
			// errors.add("name", new ActionError("id"));

		}

		return mapping.findForward("listVersions");

	}

	/**
	 *Delete the file logically by settindg its status to Inactive
	 **/

	public ActionForward deleteDocument(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionMessages messages = new ActionMessages();
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		KmDocumentMstrFormBean formBean = (KmDocumentMstrFormBean) form;
		KmDocumentService kmDocumentService = new KmDocumentServiceImpl();
		KmUserMstr sessionUserBean = (KmUserMstr) session
				.getAttribute("USER_INFO");
		String searchType = (String) session.getAttribute("SEARCH_TYPE");
		try {
			logger
					.info(sessionUserBean.getUserLoginId()
							+ " Entered into the deleteDocument method of KmDocumentMstrAction");

			formBean.setInitStatus("true");

			String[] checkedDocs = formBean.getCheckedDocs();

			// Calling delete file service

			for (int i = 0; i < checkedDocs.length; i++)
				kmDocumentService.deleteFileService(checkedDocs[i],
						sessionUserBean.getUserId());

			// Retrieving the file list after deletion

			// Deleteing the document from the lucene index file

			DeleteFiles delDocument = new DeleteFiles();

			for (int i = 0; i < checkedDocs.length; i++)
				delDocument.deleteFiles(checkedDocs[i]);

			messages.add("msg1", new ActionMessage("file.deleted"));

			// formBean.setFileList(kmDocumentService.viewFileService(formBean.
			// getSelectedParentId()));
			saveMessages(request, messages);
			logger.info("File Deleted Successfully");

		} catch (Exception ex) {
			errors.add("errors", new ActionError("document.delete.error"));
			saveErrors(request, errors);
			logger.error("Exception in deleting the requested Document: "
					+ ex.getMessage());
		}
		if (searchType.equals("ADMIN_KEYWORD")) {
			KmSearchService searchService = new KmSearchServiceImpl();
			KmSearch dto = new KmSearch();
			dto = (KmSearch) session.getAttribute("DTO");
			request.setAttribute("DOCUMENT_LIST", searchService.search(dto));
			return keywordSearch(mapping, form, request, response);
		} else if (searchType.equals("ADMIN_CONTENT")) {
			KmSearchService searchService = new KmSearchServiceImpl();
			KmSearch dto = new KmSearch();
			dto = (KmSearch) session.getAttribute("DTO");
			request.setAttribute("DOCUMENT_LIST", searchService.search(dto));
			return contentSearch(mapping, form, request, response);
		} else {
			return init(mapping, formBean, request, response);
		}
	}

	/*
	 * public ActionForward downloadDocument( ActionMapping mapping, ActionForm
	 * form, HttpServletRequest request, HttpServletResponse response) throws
	 * Exception { ActionMessages messages =new ActionMessages(); ActionErrors
	 * errors = new ActionErrors(); ActionForward forward = new ActionForward();
	 * HttpSession session = request.getSession(); KmDocumentMstrFormBean
	 * formBean = (KmDocumentMstrFormBean) form; KmDocumentService
	 * kmDocumentService= new KmDocumentServiceImpl(); KmUserMstr
	 * sessionUserBean = (KmUserMstr) session.getAttribute("USER_INFO"); String
	 * searchType=(String)session.getAttribute("SEARCH_TYPE"); ResourceBundle
	 * bundle = ResourceBundle.getBundle("ApplicationResources"); KmDisplayFile
	 * fileDisplay = new KmDisplayFile(); Delete del=new Delete(); ArrayList
	 * docList=null; try {logger.info(sessionUserBean.getUserLoginId()+
	 * " Entered into the DownloadDocument method of KmDocumentMstrAction");
	 * //System.out.println("Document Id : "+formBean.getSelectedDocumentId());
	 * String
	 * documentPath=kmDocumentService.getDocPath(formBean.getSelectedDocumentId
	 * ()); request.setAttribute("DOCUMENT_LIST",null); StringBuffer
	 * displayFilePath = new StringBuffer(bundle.getString("folder.path"));
	 * displayFilePath.append(documentPath);
	 * logger.info("Document Path :"+displayFilePath);
	 * request.setAttribute("File_Data",returnFile(displayFilePath.toString()));
	 * StringBuffer tempFilePath = new
	 * StringBuffer(request.getSession().getServletContext
	 * ().getRealPath("/Docs/")); del.delete(tempFilePath.toString()); File dir
	 * = new File(tempFilePath.toString()); dir.mkdirs(); String returnPath =
	 * fileDisplay
	 * .displayFile(displayFilePath.toString(),tempFilePath.toString());
	 * formBean.setFilePath("/KM"+returnPath);
	 * request.setAttribute("FILE_PATH","/KM"+returnPath);
	 * 
	 * 
	 * //System.out.println("Document Path : "+returnPath);
	 * messages.add("msg1",new ActionMessage("file.deleted"));
	 * 
	 * 
	 * saveMessages(request,messages);
	 * logger.info("File Downloaded Successfully Successfully");
	 * 
	 * }catch (Exception ex) { errors.add("errors",new
	 * ActionError("document.download.error")); saveErrors(request,errors);
	 * logger.error("Exception in deleting the requested Document: " +
	 * ex.getMessage()); } return mapping.findForward("downloadDocument"); }
	 */
	/**
	 * Initializes the content search page
	 **/
	public ActionForward initKeywordSearch(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ActionForward forward = new ActionForward();
		HttpSession session = request.getSession();
		KmDocumentMstrFormBean kmDocumentMstrFormBean = (KmDocumentMstrFormBean) form;
		KmUserMstr sessionUserBean = (KmUserMstr) session
				.getAttribute("USER_INFO");
		try {
			logger
					.info(sessionUserBean.getUserLoginId()
							+ " Entered into the initKeywordSearch method of KmDocumentMstrAction");
			session.setAttribute("SEARCH_TYPE", "ADMIN_KEYWORD");
			kmDocumentMstrFormBean.setInitStatus("true");
			kmDocumentMstrFormBean.setKeyword("");
			kmDocumentMstrFormBean.setFileList(null);
			if (sessionUserBean.getKmActorId().equals(Constants.CIRCLE_CSR)
					|| sessionUserBean.getKmActorId().equals(
							Constants.CATEGORY_CSR)) {
				forward = mapping.findForward("csrKeywordSearch");
			} else {

				forward = mapping.findForward("keywordSearch");
			}
		} catch (Exception ex) {

			logger.error("Exception in opening the keyword search page "
					+ ex.getMessage());
			if (sessionUserBean.getKmActorId().equals(Constants.CIRCLE_CSR)
					|| sessionUserBean.getKmActorId().equals(
							Constants.CATEGORY_CSR)) {
				forward = mapping.findForward("csrKeywordSearch");
			} else {

				forward = mapping.findForward("keywordSearch");
			}
		}
		return (forward);
	}

	/**
	 * Search the files using the keywords. The entered keyword matches the
	 * keyword field avlue associated with each file and lists all the matched
	 * files
	 **/
	public ActionForward keywordSearch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = new ActionForward();
		HttpSession session = request.getSession();
		KmDocumentMstrFormBean formBean = (KmDocumentMstrFormBean) form;
		KmUserMstr sessionUserBean = (KmUserMstr) session
				.getAttribute("USER_INFO");
		KmSearch searchDto = new KmSearch();
		logger.info("Inside keyword search");
		ArrayList documentList = null;
		try {
			logger
					.info(sessionUserBean.getUserLoginId()
							+ " Entered into the keywordSearch method of KmDocumentMstrAction");
			formBean.setInitStatus("false");
			String circleId = (String) session
					.getAttribute("CURRENT_CIRCLE_ID");
			if (circleId != null) {
				searchDto.setElementId(circleId);
			} else {

				searchDto.setElementId(sessionUserBean.getElementId());
			}
			searchDto.setKeyword(formBean.getKeyword());
			if (sessionUserBean.getKmActorId().equals(Constants.CIRCLE_CSR)
					|| sessionUserBean.getKmActorId().equals(
							Constants.CATEGORY_CSR)) {
				searchDto.setSearchType("CSR_KEYWORD");
			} else {
				searchDto.setSearchType("ADMIN_KEYWORD");
			}

			KmSearchService searchService = new KmSearchServiceImpl();
			documentList = searchService.search(searchDto);

			// Code Added by Karan BFR28 iPortal Project 07203

			if (documentList != null) {
				if (documentList.size() > 0) {
					KeywordMgmtServiceImpl keyService = new KeywordMgmtServiceImpl();
					try {
						keyService.insertKeyword(searchDto.getKeyword());
					} catch (Exception e) {
						logger
								.info("duplicate string found while registering keyword");
					}
				}
			}

			// Code Addition ends Here
			request.setAttribute("DOCUMENT_LIST", documentList);
			session.setAttribute("DOC_LIST", documentList);
			session.setAttribute("DTO", searchDto);
			if (sessionUserBean.getKmActorId().equals(Constants.CIRCLE_CSR)
					|| sessionUserBean.getKmActorId().equals(
							Constants.CATEGORY_CSR)) {
				forward = mapping.findForward("csrKeywordSearch");
			} else {

				forward = mapping.findForward("keywordSearch");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Exception while listing the Documents: "
					+ ex.getMessage());
			if (sessionUserBean.getKmActorId().equals(Constants.CIRCLE_CSR)
					|| sessionUserBean.getKmActorId().equals(
							Constants.CATEGORY_CSR)) {
				forward = mapping.findForward("csrKeywordSearch");
			} else {

				forward = mapping.findForward("keywordSearch");
			}
		}

		return (forward);
	}

	/**
	 * Initializes the content search page
	 **/
	public ActionForward initContentSearch(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ActionForward forward = new ActionForward();
		HttpSession session = request.getSession();
		KmDocumentMstrFormBean kmDocumentMstrFormBean = (KmDocumentMstrFormBean) form;
		KmUserMstr sessionUserBean = (KmUserMstr) session
				.getAttribute("USER_INFO");
		try {
			logger
					.info(sessionUserBean.getUserLoginId()
							+ " Entered into the initContentSearch method of KmDocumentMstrAction");

			// Bug Resolved MASDB00064343 and MASDB00064503

			session.setAttribute("SEARCH_TYPE", "ADMIN_CONTENT");
			kmDocumentMstrFormBean.setInitStatus("true");
			kmDocumentMstrFormBean.setDocumentKeyword("");
			kmDocumentMstrFormBean.setKeyword("");
			kmDocumentMstrFormBean.setMessage("");
			kmDocumentMstrFormBean.setFileList(null);
			/*
			 * if(sessionUserBean.getKmActorId().equals(Constants.CIRCLE_CSR)||sessionUserBean
			 * .getKmActorId().equals(Constants.CATEGORY_CSR)){
			 * forward=mapping.findForward("csrContentSearch"); }else{
			 */
			forward = mapping.findForward("searchFiles");
			// }
		} catch (Exception ex) {

			logger.error("Exception in opening the keyword search page "
					+ ex.getMessage());
			if (sessionUserBean.getKmActorId().equals(Constants.CIRCLE_CSR)
					|| sessionUserBean.getKmActorId().equals(
							Constants.CATEGORY_CSR)) {
				forward = mapping.findForward("csrContentSearch");
			} else {
				forward = mapping.findForward("searchFiles");
			}
		}
		return (forward);
	}

	/**
	 * Searches the document contents for the given keyword and lists all the
	 * documents which contains the given keyword(Apache Lucene))
	 **/
	public ActionForward contentSearch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// java.util.Date dt1 = new java.util.Date();
		
		ActionErrors errors = new ActionErrors();
		ActionForward forward = new ActionForward();
		HttpSession session = request.getSession();
		KmDocumentMstrFormBean formBean = (KmDocumentMstrFormBean) form;
		KmUserMstr sessionUserBean = (KmUserMstr) session
				.getAttribute("USER_INFO");
		KmSearch searchDto = new KmSearch();
		logger.info("Inside content search : " + formBean.getDateCheck());
		ArrayList documentList = null;
		try {
			logger
					.info(sessionUserBean.getUserLoginId()
							+ " Entered into the contentSearch method of KmDocumentMstrAction");
			// Bug Resolved MASDB00064343 and MASDB00064503. Some changes were
			// required in StrutsConfig.xml and tiles-def.xml
			formBean.setInitStatus("false");

			searchDto.setActorId(sessionUserBean.getKmActorId());
			searchDto.setElementId(sessionUserBean.getElementId());

			if ("on".equals(formBean.getDateCheck())) {
				searchDto.setFromDate(formBean.getSearchFromDt());
				searchDto.setToDate(formBean.getSearchToDt());
				searchDto.setDateCheck(formBean.getDateCheck());
			}

			if (null != request.getParameter("keywordMenu")) {
				searchDto
						.setKeyword(request.getParameter("keywordMenu").trim());
			
			} else {
	
				searchDto.setKeyword(formBean.getSearchContentHidden().trim());
			}
			formBean.setMessage("CONTENT_SEARCH");
			searchDto.setSearchType("ADMIN_CONTENT");

			if (formBean.getSearchModeChecked() != null)
				searchDto.setSearchModeChecked(formBean.getSearchModeChecked());
			else
				searchDto.setSearchModeChecked("0");

			KmSearchService searchService = new KmSearchServiceImpl();
			if (PropertyReader.getAppValue("SINGLE.INDEX").equals("Y")) {
				documentList = searchService.contentSearch(searchDto);
			} else {
				documentList = searchService.contentSearchAdmin(searchDto);
			}

			// Code Added by Karan BFR28 iPortal Project 07203
			String keyword = searchDto.getKeyword();
			keyword = keyword.substring(1, (keyword.length() - 1));
			// System.out.println("\nKeyword:" + keyword);
				// java.util.Date dt2 = new java.util.Date();
			if (documentList != null) {
				if (documentList.size() > 0) {
					KeywordMgmtServiceImpl keyService = new KeywordMgmtServiceImpl();
					try {

						keyService.insertKeyword(keyword);
					} catch (Exception e) {
						logger
								.info("duplicate string found while registering keyword");
					}
				}
			}
			// java.util.Date dt3 = new java.util.Date();
			// Code Addition ends here
		
			request.setAttribute("DOCUMENT_LIST", documentList);

			// TODO remove below from session setting
			/*
			 * session.setAttribute("DOC_LIST",documentList);
			 * session.setAttribute("DTO",searchDto);
			 */

			formBean.setKeyword(keyword);

			formBean.setDateCheck("");
			formBean.setSearchFromDt(formBean.getSearchFromDt());
			formBean.setSearchToDt(formBean.getSearchToDt());
			if (sessionUserBean.isCsr()) {
				forward = mapping.findForward("csrContentSearch");
			} else {
				forward = mapping.findForward("searchFiles");
			}

			// java.util.Date dt4 = new java.util.Date();
			/*
			 * System.out.println("Total Time:" + (dt4.getTime() -
			 * dt1.getTime()) );
			 * 
			 * System.out.println("1 stage time:" + (dt2.getTime() -
			 * dt1.getTime()) ); System.out.println("2 stage time:" +
			 * (dt3.getTime() - dt1.getTime()) );
			 */
		}

		catch (KmException se) {
			se.printStackTrace();

			if (sessionUserBean.isCsr()) {
				forward = mapping.findForward("csrContentSearch");
			} else {
				forward = mapping.findForward("searchFiles");
			}
			errors.add("error1", new ActionError("search.error"));
			saveErrors(request, errors);
		} catch (Exception ex) {

			logger.error("Exception while listing the Documents in content search: "
					+ ex.getMessage());
			ex.printStackTrace();
			if (sessionUserBean.isCsr()) {
				forward = mapping.findForward("csrContentSearch");
			} else {
				forward = mapping.findForward("searchFiles");
			}
		}

		return (forward);
	}

	/**** Adding by ram ******/
	public ActionForward searchDetails(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		KmDocumentMstrFormBean formBean = (KmDocumentMstrFormBean) form;
		KmUserMstr sessionUserBean = (KmUserMstr) session
				.getAttribute("USER_INFO");
		KmSearch searchDto = new KmSearch();
		ArrayList<KmSearchDetailsDTO> searchDetailsList = null;
		ArrayList<KmSearchDetailsDTO> configurableColumnList = null;
		String forward = "";
		String mobileNo = "";
		StringBuffer sb = new StringBuffer();
		try {
			formBean.setMessage("");
			formBean.setSearchDetailsList(null);
			String tableName = "";

			logger
					.info("User Id :"
							+ sessionUserBean.getUserLoginId()
							+ "Actor Id : "
							+ sessionUserBean.getKmActorId()
							+ "Entered into the searchDetails method of KmDocumentMstrAction");
			formBean.setInitStatus("false");
			searchDto.setActorId(sessionUserBean.getKmActorId());
			formBean.setActorId(Integer
					.parseInt(sessionUserBean.getElementId()));
			searchDto.setElementId(sessionUserBean.getElementId());
			if (null != request.getParameter("keywordMenu")) {
				searchDto
						.setKeyword(request.getParameter("keywordMenu").trim());
				logger
						.info("searchDto.setKeyword() :"
								+ searchDto.getKeyword());
				logger
						.info("mainOptionValue :"
								+ formBean.getMainOptionValue());
				logger.info(" selectedSubOptionValue :"
						+ formBean.getSelectedSubOptionValue());

				formBean.setKeyword(searchDto.getKeyword());
				KmSearchService searchService = new KmSearchServiceImpl();
				searchDetailsList = searchService.searchDetails(searchDto
						.getKeyword(), formBean.getMainOptionValue(), formBean
						.getSelectedSubOptionValue(), Integer
						.parseInt(sessionUserBean.getKmActorId()),
						sessionUserBean.getUserLoginId());

				logger.info("Find details size :" + searchDetailsList.size());
				// request.setAttribute("SEARCH_LIST",searchDetailsList);
				formBean.setSearchDetailsList(searchDetailsList);
				formBean.setConfigurableColumnList(configurableColumnList);

			}
			/*
			 * if(searchDetailsList.size() > 0 ){ KmSearchDetailsDTO dto =null;
			 * formBean.setSearchDetailsList(searchDetailsList);
			 * logger.info("********** records found  ********************");
			 * //request.setAttribute("SEARCH_LIST",searchDetailsList);
			 * Iterator<KmSearchDetailsDTO> itr =
			 * searchDetailsList.listIterator(); while (itr.hasNext()) { dto =
			 * itr.next();
			 * logger.info("circle :"+dto.getCircle()+" city :"+dto.getCity
			 * ()+" -------"); } }
			 */
		} catch (Exception ex) {
			logger.error("Exception while listing the search details: "
					+ ex.getMessage());
		}
		logger.info("formBean.getActorId()  :" + formBean.getActorId());
		if (formBean.getActorId() == 1)
			forward = "searchDetails";
		else
			forward = "csrSearch";
		logger.info("msuisdn number" + sessionUserBean.getMsisdn());
		if (sessionUserBean.getMsisdn() != null
				&& sessionUserBean.getMsisdn().length() > 0) {
					mobileNo = sessionUserBean.getMsisdn();
			//mobileNo = "9910003468";
			logger.info("Mobile Number coming after UD Login " + mobileNo);
			request.setAttribute("mobileNo", mobileNo);
			formBean.setMobileNo(mobileNo);
		} else {
			//mobileNo = "9910003468";
			logger.info("Mobile Number needs to be manually entered " + mobileNo);
			request.setAttribute("mobileNo", "");
			formBean.setMobileNo("");
		}
		System.out.println("**********************"+request.getAttribute("mobileNo"));
		return mapping.findForward(forward);
	}

	public ActionForward editDetails(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		KmDocumentMstrFormBean formBean = (KmDocumentMstrFormBean) form;
		KmUserMstr sessionUserBean = (KmUserMstr) session
				.getAttribute("USER_INFO");
		KmSearch searchDto = new KmSearch();
		String keyword, mainOption, subOption;
		int serialNo;
		String srNo;
		ArrayList<KmSearchDetailsDTO> searchDetailsList = null;
		try {
			formBean.setMessage("");
			formBean.setSearchDetailsList(null);
			logger
					.info(sessionUserBean.getUserLoginId()
							+ " Entered into the editDetails method of KmDocumentMstrAction");
			formBean.setInitStatus("false");
			searchDto.setActorId(sessionUserBean.getKmActorId());
			searchDto.setElementId(sessionUserBean.getElementId());
			keyword = (String) request.getParameter("keyword");
			mainOption = (String) request.getParameter("mainOptionValue");
			subOption = (String) request.getParameter("subOptionValue");
			srNo = (String) request.getParameter("srno");
			serialNo = Integer.parseInt(srNo);
			logger.info("Search Criteria: keyword:" + keyword
					+ " mainOption  :" + mainOption + " subOption :"
					+ subOption + " serial no :" + serialNo);
			KmSearchService searchService = new KmSearchServiceImpl();
			searchDetailsList = searchService.editDetails(keyword, mainOption,
					subOption, serialNo);
			logger.info("Search Records Found :" + searchDetailsList.size());
			formBean.setSearchDetailsList(searchDetailsList);
		} catch (Exception ex) {
			logger.info("Exception  :" + ex.getMessage());
			ex.printStackTrace();
		}
		return mapping.findForward("editDetails");
	}

	public ActionForward updateDetails(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		KmDocumentMstrFormBean formBean = (KmDocumentMstrFormBean) form;
		KmUserMstr sessionUserBean = (KmUserMstr) session
				.getAttribute("USER_INFO");
		KmSearch searchDto = new KmSearch();
		String keyword, mainOption, subOption;
		int serialNo;
		String srNo;
		String result = "";
		try {
			formBean.setMessage("");
			formBean.setSearchDetailsList(null);
			logger
					.info(sessionUserBean.getUserLoginId()
							+ " Entered into the updateDetails method of KmDocumentMstrAction");
			formBean.setInitStatus("false");
			searchDto.setActorId(sessionUserBean.getKmActorId());
			searchDto.setElementId(sessionUserBean.getElementId());
			keyword = formBean.getKeyword();// (String)
			// request.getParameter("keyword");
			mainOption = formBean.getMainOptionValue();// (String)
			// request.getParameter
			// ("mainOptionValue");
			subOption = formBean.getSelectedSubOptionValue();// (String)
			// request.
			// getParameter(
			// "subOptionValue"
			// );
			serialNo = formBean.getSrNo(); // Integer.parseInt(srNo);
			logger.info("circle :" + formBean.getCircle() + " zone :"
					+ formBean.getZone() + " city :" + formBean.getCity()
					+ " arcor :" + formBean.getArcOr());
			formBean.setUserLoginId(sessionUserBean.getUserLoginId());
			logger.info("Search Criteria: keyword:" + keyword
					+ " mainOption  :" + mainOption + " subOption :"
					+ subOption + " serial no :" + serialNo);
			KmSearchService searchService = new KmSearchServiceImpl();
			result = searchService.updateDetails(keyword, mainOption, formBean,
					serialNo);
			formBean.setMessage(result);
			formBean.setSearchDetailsList(null);
		} catch (Exception ex) {
			logger.info("Exception  :" + ex.getMessage());
			ex.printStackTrace();
		}
		return mapping.findForward("editDetails");
	}

	public ActionForward deleteDetails(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		KmDocumentMstrFormBean formBean = (KmDocumentMstrFormBean) form;
		KmUserMstr sessionUserBean = (KmUserMstr) session
				.getAttribute("USER_INFO");
		KmSearch searchDto = new KmSearch();
		String keyword, mainOption, subOption;
		int serialNo;
		String srNo;
		String result = "";
		try {
			formBean.setMessage("");
			formBean.setSearchDetailsList(null);
			logger
					.info(sessionUserBean.getUserLoginId()
							+ " Entered into the deleteDetails method of KmDocumentMstrAction");
			formBean.setInitStatus("false");
			searchDto.setActorId(sessionUserBean.getKmActorId());
			searchDto.setElementId(sessionUserBean.getElementId());
			keyword = formBean.getKeyword();// (String)
			// request.getParameter("keyword");
			mainOption = (String) request.getParameter("mainOptionValue");// (
			// String
			// )
			// request
			// .
			// getParameter
			// (
			// "mainOptionValue"
			// )
			// ;
			srNo = (String) request.getParameter("srno");
			serialNo = Integer.parseInt(srNo);
			formBean.setUserLoginId(sessionUserBean.getUserLoginId());
			logger.info("Search Criteria: keyword:" + keyword
					+ " mainOption  :" + " serial no :" + serialNo);
			KmSearchService searchService = new KmSearchServiceImpl();
			result = searchService.deleteDetails(mainOption, serialNo);
			logger.info("Deletion result in action :" + result);
			formBean.setMessage(result);
			formBean.setSearchDetailsList(null);
		} catch (Exception ex) {
			logger.info("Exception  :" + ex.getMessage());
			ex.printStackTrace();
		}
		return mapping.findForward("editDetails");
	}

	/***** End of adding by RAM *******************/
	public static String returnFile(String filename)
			throws FileNotFoundException, IOException {
		InputStream in = null;
		String data = "";
		try {
			in = new BufferedInputStream(new FileInputStream(filename));
			byte[] buf = new byte[4 * 1024]; // 4K buffer

			int bytesRead;
			while ((bytesRead = in.read(buf)) != -1) {
				// out.write(buf, 0, bytesRead);
				data += (char) bytesRead;
			}
		} catch (Exception e) {
			// e.printStackTrace();
			logger.info("Exception while writing file data");

		} finally {
			if (in != null)
				in.close();
		}
		return data;
	}

	public ActionForward addToFavorites(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		KmUserMstr userBean = UserDetails.getUserDetails(request);
		KmDocumentService kmDocumentService = new KmDocumentServiceImpl();
		String userId = userBean.getUserId();
		// System.out.println("userId?????? "+userId);
		String docId = request.getParameter("docId");
		// System.out.println("docId?????? "+docId);
		int status = kmDocumentService.addToFavorites(userId, docId);
		// System.out.println("status"+status);
		logger.info(docId + " added in the list:");

		ArrayList<String> favList = new ArrayList<String>();
		favList = (ArrayList) request.getSession().getAttribute("favList");
		if (favList != null) {
			favList.add(docId);
			request.getSession().setAttribute("favList", favList);
			logger.info(" added in the existing list");
		} else {
			ArrayList<String> favNewList = new ArrayList<String>();
			favNewList.add(docId);
			request.getSession().setAttribute("favList", favNewList);
			logger.info(" created a new list");
		}
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Content-Type", "text");
		response.getWriter().print(status);
		return null;
	}

	public ActionForward addToStay(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String viewerAction = request.getParameter("viewerAction");
		String viewerMethodName = request.getParameter("viewerMethodName");
		String xmlFileName = request.getParameter("xmlFileName");
		String docId = request.getParameter("docID");

		String stayURL = viewerAction + "?methodName=" + viewerMethodName
				+ "&docID=" + docId;
		HttpSession session = request.getSession();
		session.setAttribute("stayURL", stayURL);
		// session.setAttribute("stayDocId", stayURL);
		// System.out.println("stayURL : "+stayURL);
		return null;
	}

	public ActionForward deleteFavorites(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		KmUserMstr userBean = UserDetails.getUserDetails(request);
		KmDocumentService kmDocumentService = new KmDocumentServiceImpl();
		String userId = userBean.getUserId();
		String docId = request.getParameter("docId");
		int status = kmDocumentService.deleteFavorites(userId, docId);
		// System.out.println("status"+status);
		ArrayList favList = new ArrayList();
		favList = (ArrayList) request.getSession().getAttribute("favList");
		if (favList != null) {
			logger.info(docId + " removed from favList:"
					+ favList.remove(docId));
			request.getSession().setAttribute("favList", favList);
		}
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Content-Type", "text");
		response.getWriter().print(status);
		return null;
	}

	public ActionForward openSubCategoryTree(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession();
		KmCSRHomeBean csrHomeBean = (KmCSRHomeBean) session
				.getAttribute("CSR_HOME_BEAN");
		ActionForward forward = null;
		KmDocumentService documentService = new KmDocumentServiceImpl();
		KmElementMstrService elementService = new KmElementMstrServiceImpl();
		String subCategoryId = (String) request.getParameter("subCatId");
		request.setAttribute("subCatId", subCategoryId);
		String circleId = (String) request.getParameter("circleId");

		KmUserMstr sessionUserBean = (KmUserMstr) session
				.getAttribute("USER_INFO");
		KmDocumentService docService = new KmDocumentServiceImpl();
		HashMap circleCategoryMap = new HashMap();

		try {

			/*
			 * try{
			 * 
			 * longtimegap=(System.currentTimeMillis()-sessionUserBean.
			 * getAlertUpdateTime())/1000; int
			 * alertCheckGap=60Integer.parseInt(PropertyReader
			 * .getAppValue("ALERT_CHECK_INTERVAL"));
			 * csrHomeBean.setAlertMessage(""); if(timegap>=alertCheckGap){
			 * KmAlertMstrService alertService = new KmAlertMstrServiceImpl();
			 * KmAlertMstr alertDto=new KmAlertMstr();
			 * alertDto.setCircleId(sessionUserBean.getElementId());
			 * alertDto.setUserId
			 * (Integer.parseInt(sessionUserBean.getUserId()));
			 * csrHomeBean.setAlertMessage
			 * (alertService.getAlertMessage(alertDto));
			 * sessionUserBean.setAlertUpdateTime(System.currentTimeMillis()); }
			 * }catch (Exception e) {logger.error(
			 * "Exception occured while fetching alert during page refresh"+e);
			 * }
			 */

			String currentCircleId = (String) session
					.getAttribute("CURRENT_CIRCLE_ID");
			String panStatus = (String) request.getParameter("panStatus");
			String favCategoryId = sessionUserBean.getFavCategoryId();
			// Modified by Anil : To open the immediate document child of the
			// category/subcategory
			KmElementMstr element = elementService
					.getCompleteElementDetails(subCategoryId);
			KmDocumentMstr document = new KmDocumentMstr();
			String elementLevelId = element.getElementLevel();

			if (elementLevelId.equals("0")) {
				document.setDocumentId(element.getDocumentId());
				document.setDocumentName(element.getDocumentName());
				document.setDocumentPath(element.getDocumentPath());
				request.setAttribute("DOCUMENT", document);
				displayDocument(mapping, form, request, response);
				// System.out.println("Opening the document");
				return mapping.findForward("csrViewDocument");
			}
			document = docService.getSingleDoc(subCategoryId);
			request.setAttribute("DOCUMENT", document);
			if (document != null) {

				displayDocument(mapping, form, request, response);
				return mapping.findForward("csrViewDocument");
			}

			/* Changes made for phase 3 */

			int parentLevelId = Integer.parseInt(elementLevelId) + 1;
			int childLevelId = Integer.parseInt(elementLevelId) + 2;
			circleCategoryMap = elementService.retrieveSubCategoryMap(
					subCategoryId, parentLevelId + "", childLevelId + "");

			csrHomeBean.setCircleCategoryMap(circleCategoryMap);
			forward = mapping.findForward("csrCategoryDocHome");

			int[] circleIdArray = { Integer.parseInt(subCategoryId) };
			csrHomeBean.setDocumentList(documentService
					.retrieveCSRDocumentList(circleIdArray));
			csrHomeBean.setCategoryId(subCategoryId);
			csrHomeBean.setCategoryName(elementService
					.getElementName(subCategoryId));
			/* Changes made for phase 3 */

			if (panStatus != null) {

				/* Changes in KM Phase 2: PAN India for all businesses */
				String panChildId = (String) session
						.getAttribute("CURRENT_PAN_ID");
				String parentId = elementService.getParentId(circleId);
				if (panChildId == null)
					panChildId = elementService.getPanChild(parentId)
							.getElementId();

				currentCircleId = panChildId;

				csrHomeBean.setCircleId(panChildId);
				csrHomeBean.setCircleName(elementService
						.getElementName(panChildId));
				csrHomeBean.setCategoryList(elementService
						.getChildren(panChildId));

				session.setAttribute("CURRENT_CIRCLE_ID", currentCircleId);
				session.setAttribute("CSR_HOME_BEAN", csrHomeBean);

			} else if (currentCircleId != null) {

				csrHomeBean.setCircleId(currentCircleId);
				csrHomeBean.setCircleName(elementService
						.getElementName(currentCircleId));
				session.setAttribute("CSR_HOME_BEAN", csrHomeBean);

			} else if (circleId != null) {
				KmCSRAction csrAction = new KmCSRAction();
				session.setAttribute("CSR_HOME_BEAN", csrAction
						.resetCSRHomeBean(csrHomeBean, circleId, favCategoryId,
								request));
			}

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			logger
					.error("NumberFormat Exception occurs in openSubCategoryTree: "
							+ e.getMessage());
		} catch (KmException e) {
			// TODO Auto-generated catch block
			logger.error(" Exception occurs in openSubCategoryTree: "
					+ e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" Exception occurs in openSubCategoryTree: "
					+ e.getMessage());
		}
		// return mapping.findForward("csrDocMenuHome");
		// System.gc();
		return forward;
	}

	public ActionForward initMove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		KmUserMstr userBean = (KmUserMstr) request.getSession().getAttribute(
				"USER_INFO");
		KmMoveDocumentFormBean formBean = (KmMoveDocumentFormBean) form;
		formBean.reset(mapping, request);
		logger.info("Init Button Type: " + formBean.getButtonType());
		logger.info(userBean.getUserLoginId() + " entered initMove method");
		try {
			KmElementMstrService kmElementService = new KmElementMstrServiceImpl();
			List firstDropDown = kmElementService.getChildren(userBean
					.getElementId());
			if (firstDropDown != null && firstDropDown.size() != 0) {
				formBean.setInitialLevel(((KmElementMstr) firstDropDown.get(0))
						.getElementLevel());
				// System.out.println("testingggggggggggggggg"+formBean.
				// getInitialLevel());
			} else {

				int initialLevel = Integer.parseInt(kmElementService
						.getElementLevelId(userBean.getElementId()));
				initialLevel++;
				formBean.setInitialLevel(initialLevel + "");
			}
			formBean.setParentId(userBean.getElementId());
			if (request.getAttribute("DOCUMENT_LIST") == null) {
				formBean.setInitStatus("true");
				formBean.setButtonType("list"); // added by beeru on 25 apr 2014
			} else {
				formBean.setInitStatus("false");
			}
			request.setAttribute("elementLevelNames", kmElementService
					.getAllElementLevelNames());
			request.setAttribute("allParentIdString", kmElementService
					.getAllParentIdString("1", userBean.getElementId()));
			request.setAttribute("firstList", firstDropDown);
		} catch (KmException e) {
			logger.error("Exception in Loading page for document move "
					+ e.getMessage());

		}
		logger.info(userBean.getUserLoginId() + " exited initMove ");
		return mapping.findForward("initMove");
	}

	public ActionForward loadElementDropDown(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String parentId = request.getParameter("ParentId");
		KmElementMstrService kmElementService = new KmElementMstrServiceImpl();

		try {
			JSONObject json = kmElementService.getElementsAsJson(parentId);
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Content-Type", "application/x-json");
			response.getWriter().print(json);
		} catch (IOException e) {
			logger.error("IOException in Loading Element Dropdown: "
					+ e.getMessage());

		} catch (Exception e) {
			logger.error("Exception in Loading Element Dropdown: "
					+ e.getMessage());

		}

		return null;
	}

	public ActionForward listDocuments(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		KmUserMstr userBean = (KmUserMstr) request.getSession().getAttribute(
				"USER_INFO");
		KmMoveDocumentFormBean formBean = (KmMoveDocumentFormBean) form;
		logger
				.info(userBean.getUserLoginId()
						+ " entered listDocuments method");
		KmDocumentService documentService = new KmDocumentServiceImpl();
		try {
			formBean.setOldPath(formBean.getDocumentPath());
			String parentId = formBean.getParentId();
			formBean.setInitStatus("");
			ArrayList documentList = documentService.viewFileService(userBean
					.getElementId(), parentId);
			if (documentList.size() > 0) {
				formBean.setButtonType("move");
			} else {
				formBean.setButtonType("list");
			}

			request.setAttribute("DOCUMENT_LIST", documentList);
		} catch (KmException e) {
			logger.error("Exception in listing documents document move "
					+ e.getMessage());

		}
		logger.info(userBean.getUserLoginId() + " exited documentListing ");
		return initMove(mapping, formBean, request, response);
	}

	public ActionForward moveDocuments(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionMessages messages = new ActionMessages();
		KmUserMstr userBean = (KmUserMstr) request.getSession().getAttribute(
				"USER_INFO");
		KmMoveDocumentFormBean formBean = (KmMoveDocumentFormBean) form;
		logger
				.info(userBean.getUserLoginId()
						+ " entered listDocuments method");

		// addedby vishwas for move document
		boolean success = false;
		try {
			String parentId = formBean.getParentId();
			String[] movedDocumentList = formBean.getMoveDocumentList();
			String oldPath = formBean.getOldPath();
			String newPath = formBean.getDocumentPath();
			logger.info("Old: " + oldPath + " New: " + newPath);

			KmElementMstrService kmElementService = new KmElementMstrServiceImpl();
			KmDocumentService documentService = new KmDocumentServiceImpl();
			if (movedDocumentList != null) {
				// added by Beeru on 11-06- 2014 for folder creating when folder
				// does'nt exit
				ResourceBundle bundle = ResourceBundle
						.getBundle("ApplicationResources");
				String path = bundle.getString("folder.path");
				// added by vishwas for move document
				FileCopy.createNewFolder(newPath, path); // end
				List<String> al = new ArrayList<String>();
				//System.out.println("movedDocumentList"+movedDocumentList[0]+":"
				// +movedDocumentList[1]);
				al = documentService.changePath1(movedDocumentList, oldPath,
						newPath);
				logger.info("document id which is moved" + al.size());
				String[] movedDocumentList2 = new String[al.size()];
				movedDocumentList2 = al.toArray(movedDocumentList2);

				logger.info("document id which is moved"
						+ movedDocumentList2.length);
				//System.out.println("hello array"+al.toString()+":"+al.size());
				// boolean
				// success=documentService.changePath(movedDocumentList,oldPath
				// ,newPath);
				// for(String s:al)
				String displayname[] = kmElementService
						.getDisplayName(movedDocumentList);

				//System.out.println("hello"+al.get(i).toString()+":"+al.get(i))
				// ;
				if (movedDocumentList2.length > 0) {
					success = kmElementService.moveElementsInDB(
							movedDocumentList2, parentId);
					// System.out.println("hello testing "+success);
					if (success) {
						logger.info("moveDocument.success");

						messages.add("msg", new ActionMessage(
								"moveDocument.success"));

					} else {
						logger.error("moveDocument.databaseFailure");
						messages.add("msg", new ActionMessage(
								"moveDocument.databaseFailure"));
						documentService.changePath(movedDocumentList, newPath,
								oldPath);
					}

				} else {
					messages.add("msg",
							new ActionMessage("moveDocument.Nofile"));
				}

				/**
				 * if(success){
				 * success=kmElementService.moveElementsInDB(movedDocumentList
				 * ,parentId); if(success){ logger.info("moveDocument.success");
				 * 
				 * messages.add("msg",new
				 * ActionMessage("moveDocument.success"));
				 * 
				 * }else{ logger.error("moveDocument.databaseFailure");
				 * messages.add("msg",new
				 * ActionMessage("moveDocument.databaseFailure"));
				 * documentService
				 * .changePath(movedDocumentList,newPath,oldPath); }
				 * 
				 * }else{ logger.error("moveDocument.networkFailure");
				 * messages.add("msg",new
				 * ActionMessage("moveDocument.networkFailure")); }
				 **/
				// end by vishwas
			} else {
				logger.error("moveDocument.noDocument");
				messages.add("msg",
						new ActionMessage("moveDocument.noDocument"));
			}
		} catch (KmException e) {
			// e.printStackTrace();
			logger.info("Exception in listing documents document move "
					+ Utility.getStackTrace(e));
			logger.error(e.getMessage());

			messages.add("msg1", new ActionMessage(
					"moveDocument.networkFailure"));
		}
		String dummy[] = new String[1];
		dummy[0] = "";

		formBean.setButtonType("list");
		formBean.setInitStatus("true");
		formBean.setMoveDocumentList(dummy);

		saveMessages(request, messages);
		logger.info(userBean.getUserLoginId() + " exited documentListing ");
		return initMove(mapping, formBean, request, response);
	}

	public ActionForward initEditDocument(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ActionForward forward = new ActionForward();
		HttpSession session = request.getSession();
		KmDocumentMstrFormBean formBean = (KmDocumentMstrFormBean) form;
		KmDocumentService kmDocumentService = new KmDocumentServiceImpl();
		KmUserMstr sessionUserBean = (KmUserMstr) session
				.getAttribute("USER_INFO");
		KmDocumentMstr dto = new KmDocumentMstr();

		try {
			logger
					.info(sessionUserBean.getUserLoginId()
							+ " Entered into the deleteDocument method of KmDocumentMstrAction");
			String documentId = formBean.getSelectedDocumentId();
			dto = kmDocumentService.getDocumentDto(documentId);
			formBean.setDocumentId(dto.getDocumentId());
			formBean.setDocName(dto.getDocName());
			formBean.setDocumentDisplayName(dto.getDocumentDisplayName());
			formBean.setKeyword(dto.getKeyword());
			formBean.setDocumentDesc(dto.getDocumentDesc());
			formBean.setDocumentStringPath(dto.getDocumentStringPath());
			formBean.setOldKeyword(dto.getKeyword());
			// Edit for Publish end date = Added by Atul
			formBean.setPublishEndDt(dto.getPublishingEndDate());
			// Ended by Atul
			logger.info("File Details edited successfully");
			forward = mapping.findForward("editDocumentDetails");

		} catch (Exception ex) {

			logger.error("Exception in deleting the requested Document: "
					+ ex.getMessage());
			forward = mapping.findForward("editDocumentDetails");
		}

		return (forward);
		// return init(mapping,formBean,request,response);
	}

	public ActionForward editDocument(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionMessages messages = new ActionMessages();
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		KmDocumentMstrFormBean formBean = (KmDocumentMstrFormBean) form;
		KmDocumentService kmDocumentService = new KmDocumentServiceImpl();
		KmElementMstrService elementService = new KmElementMstrServiceImpl();
		KmUserMstr sessionUserBean = (KmUserMstr) session
				.getAttribute("USER_INFO");
		KmDocumentMstr dto = new KmDocumentMstr();
		KmElementMstr element = new KmElementMstr();
		String searchType = (String) session.getAttribute("SEARCH_TYPE");
		ArrayList docList = null;
		try {
			logger
					.info(sessionUserBean.getUserLoginId()
							+ " Entered into the editDocument method of KmDocumentMstrAction");
			docList = (ArrayList) session.getAttribute("DOC_LIST");
			logger.info(searchType);
			dto.setDocumentId(formBean.getDocumentId());
			dto.setDocumentDesc(formBean.getDocumentDesc());
			dto.setKeyword(formBean.getKeyword());
			// Added by Atul
			dto.setPublishEndDt(formBean.getPublishEndDt());
			// Ended by Atul
			dto.setDocumentDisplayName(formBean.getDocumentDisplayName());
			kmDocumentService.editDocument(dto);
			// KM Phase 2 : Editing the element title

			element.setElementId(kmDocumentService.getElementId(dto
					.getDocumentId()));
			element.setElementName(formBean.getDocumentDisplayName());
			elementService.editElement(element);
			messages.add("msg1", new ActionMessage("document.update"));
			logger.info("File Details edited successfully");
			saveMessages(request, messages);
		} catch (Exception ex) {
			errors.add("errors", new ActionError("document.update.error"));
			logger.error("Exception in deleting the requested Document: "
					+ ex.getMessage());
			saveErrors(request, errors);
		}
		if (searchType.equals("ADMIN_KEYWORD")) {
			KmSearchService searchService = new KmSearchServiceImpl();
			KmSearch DTO = new KmSearch();
			DTO = (KmSearch) session.getAttribute("DTO");
			DTO.setKeyword(formBean.getKeyword());
			request.setAttribute("DOCUMENT_LIST", searchService.search(DTO));
			return mapping.findForward("keywordSearch");
		} else if (searchType.equals("ADMIN_CONTENT")) {
			request.setAttribute("DOCUMENT_LIST", docList);
			return mapping.findForward("searchFiles");
		} else {
			return init(mapping, formBean, request, response);
		}
	}

	public ActionForward downloadDocument(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ActionErrors errors = new ActionErrors();
		ActionForward forward = new ActionForward();
		HttpSession session = request.getSession();
		KmUserMstr sessionUserBean = (KmUserMstr) session
				.getAttribute("USER_INFO");
		KmDocumentMstrFormBean formBean = (KmDocumentMstrFormBean) form;
		KmDisplayFile fileDisplay = new KmDisplayFile();
		ResourceBundle bundle = ResourceBundle
				.getBundle("ApplicationResources");
		StringBuffer displayFilePath = new StringBuffer(bundle
				.getString("folder.path"));
		String documentPath = "";

		StringBuffer tempFilePath;
		String returnPath = null;
		try {
			logger
					.info(sessionUserBean.getUserLoginId()
							+ " Entered into the displayDocument method of KmDocumentMstrAction");
			String[] documentID = new String[1];
			session.setAttribute("LOGIN_ACTOR_ID", sessionUserBean
					.getKmActorId());
			String loginActorId = sessionUserBean.getKmActorId();
			formBean.setLoginActorId(loginActorId);
			documentID[0] = request.getParameter("docID").toString();
			logger.info("DOCUMENT ID[0]=" + documentID[0]);

			KmDocumentService service = new KmDocumentServiceImpl();
			// Changes made to improve performance
			// String docName=service.getDocumentName(documentID)[0];
			// String elementId=service.getElementId(documentID[0]);
			// if(documentPath.equals("")||documentPath==null){

			// documentPath=service.getDocumentPath(elementId);

			// }
			KmDocumentMstr document = service.getDocumentDetails(documentID[0]);
			documentPath = document.getDocumentPath();
			String extn = documentPath.substring(
					(documentPath.lastIndexOf(".") + 1), documentPath.length())
					.toLowerCase();
			request.setAttribute("DOCUMENT_LIST", null);
			displayFilePath = new StringBuffer(bundle.getString("folder.path"));
			displayFilePath.append(documentPath);
			File f = new File(displayFilePath.toString());

			logger.info("Document Path :" + displayFilePath);
			request.setAttribute("File_Data", returnFile(displayFilePath
					.toString()));
			tempFilePath = new StringBuffer(request.getSession()
					.getServletContext().getRealPath("/Docs/"));
			returnPath = fileDisplay.displayFile(displayFilePath.toString(),
					tempFilePath.toString());
			// System.out.println(extn);
			/* added by anil */
			if (extn.equals("pdf")) {
				response.setContentType("application/pdf");
				// response.setHeader ("Content-Disposition",
				// "attachment; filename=\"DB2 700_20081229_19-15-34.pdf\"");
			} else if (extn.equals("doc")) {
				response.setContentType("application/msword");
				// response.setHeader ("Content-Disposition",
				// "attachment; filename=\"doc.html\"");
			} else if (extn.equals("xls")) {
				response.setContentType("application/vnd.ms-excel");
				// response.setHeader ("Content-Disposition",
				// "attachment; filename=\"DB2 700_20081229_19-15-34.xls\"");
			}

			else if (extn.equals("txt")) {
				response.setContentType("text/plain");
				// response.setHeader ("Content-Disposition",
				// "attachment; filename=\"DB2 700_20081229_19-15-34.txt\"");
			} else {
				response.setContentType("application/octet-stream");
				// response.setHeader ("Content-Disposition",
				// "attachment; filename=\"DB2 700_20081229_19-15-34.txt\"");

			}
			response.setHeader("Cache-Control", "no-cache");

			log.debug("Entered DownloadServlet");
			if (document == null || returnPath == null) {
				logger.error("No File Found in displayFile Method");
				errors.add("name", new ActionError("km.file.exception"));
				saveErrors(request, errors);
				logger.info("Restuen path is null");
				request.setAttribute("CURRENT_PAGE", "DOCUMENT_ERROR");
				return mapping.findForward("downloadDocument");

			} else {

				String fileName = documentPath.substring((documentPath
						.lastIndexOf("/") + 1), documentPath.length());
				File file = new File(tempFilePath.toString());
				response.setContentLength((int) f.length());
				response.setHeader("Content-Disposition",
						"attachment; filename=" + document.getDocName());
				response.setHeader("Cache-Control", "no-cache");
				OutputStream outStream = response.getOutputStream();
				FileInputStream inStream = new FileInputStream(f);
				try {

					byte[] buf = new byte[8192];

					int sizeRead = 0;

					while ((sizeRead = inStream.read(buf, 0, buf.length)) > 0) {
						// log.debug("size:"+sizeRead);
						outStream.write(buf, 0, sizeRead);
					}
					inStream.close();
					outStream.close();
				} catch (IllegalStateException ignoredException) {
					if (outStream != null) {
						outStream.close();
					}
					if (inStream != null) {
						inStream.close();
					}
					logger.error(ignoredException);
				}
				forward = mapping.findForward("downloadDocument");

				request.setAttribute("CURRENT_PAGE", "DOCUMENT_HOME");
			}

		} catch (Exception e) {

			logger
					.error("IOException occured in displayFile "
							+ e.getMessage());
			errors.add("name", new ActionError("km.file.exception"));
			saveErrors(request, errors);
			request.setAttribute("CURRENT_PAGE", "DOCUMENT_ERROR");
			return mapping.findForward("downloadDocument");
		}

		return null;
	}

	/* KM PHASE II new module for init of archived documents */

	public ActionForward initArchived(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = new ActionForward();
		KmDocumentMstrFormBean formBean = (KmDocumentMstrFormBean) form;
		ResourceBundle bundle = ResourceBundle
				.getBundle("ApplicationResources");
		HttpSession session = request.getSession();
		KmUserMstr userBean = (KmUserMstr) session.getAttribute("USER_INFO");
		try {

			logger
					.info(userBean.getUserLoginId()
							+ " Entered into the initArchived method of KmDocumentMstrAction");
			session.setAttribute("SEARCH_TYPE", "ADMIN_ELEMENT");
			KmElementMstrService kmElementService = new KmElementMstrServiceImpl();
			List firstDropDown;
			if (userBean.getKmActorId().equals(bundle.getString("LOBAdmin"))
					|| userBean.getKmActorId().equals(
							bundle.getString("Superadmin"))) {
				firstDropDown = kmElementService.getAllChildren(userBean
						.getElementId());
			} else {
				firstDropDown = kmElementService.getChildren(userBean
						.getElementId());
			}
			if (firstDropDown != null && firstDropDown.size() != 0) {
				formBean.setInitialLevel(((KmElementMstr) firstDropDown.get(0))
						.getElementLevel());
			} else {

				int initialLevel = Integer.parseInt(kmElementService
						.getElementLevelId(userBean.getElementId()));
				initialLevel++;
				formBean.setInitialLevel(initialLevel + "");
			}
			formBean.setParentId(userBean.getElementId());
			request.setAttribute("elementLevelNames", kmElementService
					.getAllElementLevelNames());
			request.setAttribute("allParentIdString", kmElementService
					.getAllParentIdString("1", userBean.getElementId()));
			request.setAttribute("firstList", firstDropDown);
			if (request.getAttribute("FILE_LIST") == null)
				formBean.setInitStatus("true");
			else
				formBean.setInitStatus("false");
			logger.info(formBean.getInitStatus());
			forward = mapping.findForward("viewArchivedFiles");
		} catch (Exception e) {

			logger
					.error("Exception occured while initializing the archived search file page :"
							+ e);

		}

		return (forward);

	}

	/* KM PHASE II new module for viewing archived documents */
	public ActionForward viewArchivedFile(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		KmDocumentMstrFormBean formBean = (KmDocumentMstrFormBean) form;
		HttpSession session = request.getSession();
		KmUserMstr sessionUserBean = (KmUserMstr) session
				.getAttribute("USER_INFO");
		KmElementMstrService eleService = new KmElementMstrServiceImpl();
		KmDocumentService service = new KmDocumentServiceImpl();
		ArrayList fileList = new ArrayList();
		KmDocumentMstr asearchDto = new KmDocumentMstr();
		try {
			logger
					.info(sessionUserBean.getUserLoginId()
							+ " Entered into the viewArchivedFile method of KmDocumentMstrAction");
			logger.info("INSIDE VIEW FILES");
			String parentId = formBean.getParentId();
			formBean.setKmActorId(sessionUserBean.getKmActorId());
			String elementPath = null;
			if (!sessionUserBean.getElementId().equals(parentId)) {
				elementPath = eleService.getAllParentNameString(
						(sessionUserBean.getElementId()), parentId);
				logger.info("The element path is before:" + elementPath);
				elementPath = elementPath.substring(
						elementPath.indexOf(">") + 1, elementPath.length());
				logger.info("The element path is:" + elementPath);
			}
			formBean.setElementPath(elementPath);

			// formBean.setElementPath(eleService.getAllParentNameString(
			// sessionUserBean.getElementId(),parentId));
			logger.info("elementId   " + parentId);
			asearchDto.setElementId(parentId);
			asearchDto.setKeyword("showArchivedDocuments");
			fileList = service.archivedSearch(asearchDto);
			request.setAttribute("FILE_LIST", fileList);
			formBean.setSelectedParentId(formBean.getParentId());
			formBean.setInitialSelectBox("-1");
			logger.info("Listing the files");

		} catch (Exception e) {
			logger.error("Exception occured while listing files :" + e);

		}

		return initArchived(mapping, formBean, request, response);

	}

	/** Initializes the csr Keyword search page **/
	public ActionForward initCsrKeywordSearch(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		KmDocumentMstrFormBean kmDocumentMstrFormBean = (KmDocumentMstrFormBean) form;
		KmUserMstr sessionUserBean = (KmUserMstr) session
				.getAttribute("USER_INFO");
		ResourceBundle bundle = ResourceBundle
				.getBundle("ApplicationResources");
		KmElementMstrService kmElementService = new KmElementMstrServiceImpl();
		kmDocumentMstrFormBean.setInitialSelectBox("-1");
		List firstDropDown = null;
		try {
			logger
					.info(sessionUserBean.getUserLoginId()
							+ " Entered into the initKeywordSearch method of KmDocumentMstrAction");
			// session.setAttribute("SEARCH_TYPE","ADMIN_KEYWORD");
			if (sessionUserBean.getKmActorId().equals(
					bundle.getString("LOBAdmin"))
					|| sessionUserBean.getKmActorId().equals(
							bundle.getString("Superadmin"))) {
				firstDropDown = kmElementService.getAllChildren(sessionUserBean
						.getElementId());
			} else {
				firstDropDown = kmElementService.getChildren(sessionUserBean
						.getElementId());
			}

			if (firstDropDown != null && firstDropDown.size() != 0) {

				kmDocumentMstrFormBean
						.setInitialLevel(((KmElementMstr) firstDropDown.get(0))
								.getElementLevel());
			} else {

				int initialLevel = Integer.parseInt(kmElementService
						.getElementLevelId(sessionUserBean.getElementId()));
				initialLevel++;
				kmDocumentMstrFormBean.setInitialLevel(initialLevel + "");
			}

			kmDocumentMstrFormBean.setKeyword("");
			kmDocumentMstrFormBean.setFileList(null);
			kmDocumentMstrFormBean.setParentId(sessionUserBean.getElementId());
			request.setAttribute("elementLevelNames", kmElementService
					.getAllElementLevelNames());
			request.setAttribute("allParentIdString", kmElementService
					.getAllParentIdString("1", sessionUserBean.getElementId()));
			request.setAttribute("firstList", firstDropDown);
			kmDocumentMstrFormBean.setInitStatus("true");
		} catch (Exception ex) {
			logger.error("Exception in opening the keyword search page "
					+ ex.getMessage());
		}
		return mapping.findForward("csrKeywordSearch");

	}

	/**
	 * Search the files using the keywords for CSR . The entered keyword matches
	 * the keyword field avlue associated with each file and lists all the
	 * matched files
	 **/

	public ActionForward csrKeywordSearch(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		KmDocumentMstrFormBean formBean = (KmDocumentMstrFormBean) form;
		KmSearchService searchService = new KmSearchServiceImpl();
		KmUserMstr sessionUserBean = (KmUserMstr) session
				.getAttribute("USER_INFO");
		KmSearch searchDto = new KmSearch();
		KmElementMstrService kmElementService = new KmElementMstrServiceImpl();
		logger.info("Inside keyword search");
		ArrayList documentList = null;
		try {
			logger
					.info(sessionUserBean.getUserLoginId()
							+ " Entered into the keywordSearch method of KmDocumentMstrAction");

			searchDto.setParentId(formBean.getParentId());
			searchDto.setKeyword(formBean.getKeyword());
			documentList = searchService.csrSearch(searchDto);
			formBean.setDocList(documentList);
			formBean.setInitStatus("false");
			request.setAttribute("DOCUMENT_LIST", documentList);
			request.setAttribute("elementLevelNames", kmElementService
					.getAllElementLevelNames());
			request.setAttribute("allParentIdString", kmElementService
					.getAllParentIdString("1", sessionUserBean.getElementId()));

		} catch (Exception ex) {
			logger.error("Exception while listing the Documents: "
					+ ex.getMessage());
		}
		return initCsrKeywordSearch(mapping, formBean, request, response);

	}

	public ActionForward getSuggestions(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		KeywordMgmtServiceImpl keyService = new KeywordMgmtServiceImpl();
		String keyword = request.getParameter("keyword");
		ArrayList<String> keywordList;
		JSONObject json = new JSONObject();
		try {
			keywordList = keyService.getMatchingKeywords(keyword);
			JSONArray jsonItems = new JSONArray();
			for (Iterator iter = keywordList.iterator(); iter.hasNext();) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("keyword", iter.next());
				jsonItems.put(jsonObj);
			}
			json.put("keywordList", jsonItems);

			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Content-Type", "application/x-json");
			response.getWriter().print(json);
		} catch (IOException e) {
			logger.error("IOException in getting Suggestions : "
					+ e.getMessage());

		} catch (Exception e) {
			logger.error("Exception in getting Suggestions: " + e.getMessage());

		}

		return null;
	}// getSuggestions

	// added by for copy document
	public ActionForward copyDocuments(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionMessages messages = new ActionMessages();
		KmUserMstr userBean = (KmUserMstr) request.getSession().getAttribute(
				"USER_INFO");
		KmMoveDocumentFormBean formBean = (KmMoveDocumentFormBean) form;
		logger
				.info(userBean.getUserLoginId()
						+ " entered listDocuments method");

		// addedby vishwas for move document
		boolean success = false;
		try {
			String parentId = formBean.getParentId();
			String[] movedDocumentList = formBean.getMoveDocumentList();
			String oldPath = formBean.getOldPath();
			String newPath = formBean.getDocumentPath();
			logger.info("Old: " + oldPath + " New: " + newPath);

			KmElementMstrService kmElementService = new KmElementMstrServiceImpl();
			KmDocumentService documentService = new KmDocumentServiceImpl();
			if (movedDocumentList != null) {
				// added by Beeru on 11-06- 2014 for folder creating when folder
				// does'nt exit
				ResourceBundle bundle = ResourceBundle
						.getBundle("ApplicationResources");
				String path = bundle.getString("folder.path");
				// added by vishwas for move document
				FileCopy.createNewFolder(newPath, path); // end
				List<Object> al = new ArrayList<Object>();
				ArrayList<String> all = new ArrayList<String>();
				//System.out.println("movedDocumentList"+movedDocumentList[0]+":"
				// +movedDocumentList[1]);
				String elementid = documentService.getElementId();
				// String
				// dublicate=documentService.dublicate(movedDocumentList,newPath
				// ,elementid);
				al = documentService.copyPath1(movedDocumentList, oldPath,
						newPath, elementid);
				all = (ArrayList<String>) al.get(0);
				Boolean dublicate = (Boolean) al.get(1);
				//System.out.println(dublicate+"complet logiccccccccccc"+all.size
				// ());

				logger.info("document id which is moved" + all.size());
				String[] movedDocumentList2 = new String[all.size()];
				movedDocumentList2 = all.toArray(movedDocumentList2);

				logger.info("document id which is moved"
						+ movedDocumentList2.length);
				//System.out.println("hello array"+al.toString()+":"+al.size());
				// boolean
				// success=documentService.changePath(movedDocumentList,oldPath
				// ,newPath);
				// for(String s:al)
				String displayname[] = kmElementService
						.getDisplayName(movedDocumentList);

				//System.out.println("hello"+al.get(i).toString()+":"+al.get(i))
				// ;
				if (movedDocumentList2.length > 0) {
					if (dublicate != true) {
						success = kmElementService.copyElementsInDB(
								movedDocumentList2, parentId, elementid);
					}
					// System.out.println("hello testing "+success);
					if (success) {
						logger.info("moveDocument.copy");

						messages.add("msg", new ActionMessage(
								"moveDocument.copy"));

					} else {
						logger.error("moveDocument.databaseFailure");
						messages.add("msg", new ActionMessage(
								"moveDocument.databaseFailure"));
						documentService.changePath(movedDocumentList, newPath,
								oldPath);
					}

				} else {
					messages.add("msg", new ActionMessage(
							"moveDocument.Nofilecopy"));
				}

				/**
				 * if(success){
				 * success=kmElementService.moveElementsInDB(movedDocumentList
				 * ,parentId); if(success){ logger.info("moveDocument.success");
				 * 
				 * messages.add("msg",new
				 * ActionMessage("moveDocument.success"));
				 * 
				 * }else{ logger.error("moveDocument.databaseFailure");
				 * messages.add("msg",new
				 * ActionMessage("moveDocument.databaseFailure"));
				 * documentService
				 * .changePath(movedDocumentList,newPath,oldPath); }
				 * 
				 * }else{ logger.error("moveDocument.networkFailure");
				 * messages.add("msg",new
				 * ActionMessage("moveDocument.networkFailure")); }
				 **/
				// end by vishwas
			} else {
				logger.error("moveDocument.noDocumentCopy");
				messages.add("msg", new ActionMessage(
						"moveDocument.noDocumentCopy"));
			}
		} catch (KmException e) {
			// e.printStackTrace();
			logger.info("Exception in listing documents document move "
					+ Utility.getStackTrace(e));
			logger.error(e.getMessage());

			messages.add("msg1", new ActionMessage(
					"moveDocument.networkFailureCopy"));
		}
		String dummy[] = new String[1];
		dummy[0] = "";

		formBean.setButtonType("list");
		formBean.setInitStatus("true");
		formBean.setMoveDocumentList(dummy);

		saveMessages(request, messages);
		logger.info(userBean.getUserLoginId() + " exited documentListing ");
		return initMove(mapping, formBean, request, response);
	}

	// end by vishwas for copy document

	public ActionForward sendSMS(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		KmDocumentMstrFormBean formBean = (KmDocumentMstrFormBean) form;
		
		KmUserMstr sessionUserBean = (KmUserMstr) session
				.getAttribute("USER_INFO");
		KmSearch searchDto = new KmSearch();

		KmSearchService searchService = new KmSearchServiceImpl();
		String keyword, mainOption, subOption;
		int serialNo;
		String srNo;
		String mobileNo = "";
		String fwdStatus = "";
		String status ="";
		int result = 0;
		KmSearchDetailsDTO kmSearchDTO =  new KmSearchDetailsDTO();
		try {
			formBean.setMessage("");
			formBean.setSearchDetailsList(null);
			logger
					.info(sessionUserBean.getUserLoginId()
							+ " Entered into the send SMS method of KmDocumentMstrAction");
			// formBean.setInitStatus("false");
			searchDto.setActorId(sessionUserBean.getKmActorId());
			searchDto.setElementId(sessionUserBean.getElementId());
			keyword = (String) request.getParameter("keyword");
			mainOption = (String) request.getParameter("mainOptionValue");
			subOption = (String) request.getParameter("subOptionValue");
			srNo = (String) request.getParameter("srno");
			serialNo = Integer.parseInt(srNo);

			logger.info("Search Criteria: keyword:" + keyword
					+ " mainOption  :" + mainOption + " subOption :"
					+ subOption + " serial no :" + serialNo);

			Map<String, Object> mp = new HashMap<String, Object>();
			mp = searchService.sendSms(mainOption, serialNo);
			String smsTemplate = "Please find the requested details : ";
			logger.info(" Size !!!  " + mp.size());
			for (String key : mp.keySet()) {
				logger.info(" " + key + " " + mp.get(key));
				smsTemplate = smsTemplate + key + " " + mp.get(key) + " | ";

			}
			logger.debug("Sms to be send as ::: " + smsTemplate);
			SendSMSXML sendSMSXml = new SendSMSXML();
			logger.info("########## Mobile Number ::::::::: "+request.getParameter("mobileNo"));
			if(!request.getParameter("mobileNo").equals("") || request.getParameter("mobileNo") != null){
				//if(!formBean.getMobileNo().equals("")||formBean.getMobileNo()!= null){
					logger.info("if blockl ");
				//mobileNo = formBean.getMobileNo();
					mobileNo = request.getParameter("mobileNo");
				}
				
			else {
				logger.info("else block ");
				//mobileNo = request.getParameter("mobileNo");
				mobileNo = formBean.getMobileNo();
			}
			logger.info("Mobile Number !! : " + mobileNo);
			try {
				status = sendSMSXml.sendSms(mobileNo, smsTemplate);
				logger.info("Status : " + status);
				if (status.equals(Constants.SEND_SMS_STATUS_SUCCESS)) {
					logger.info("Hurray !!! msg gone @@@@@@@@@");
					
					kmSearchDTO = searchService.getUserDetailForSMS(sessionUserBean.getUserLoginId());
					logger.info("Sender :: "+sessionUserBean.getUserLoginId()+" ## Mobile Number :"+mobileNo+" ## smsTemplate : "+smsTemplate
								+" ## mainOption "+mainOption+" ## Circle ID = "+sessionUserBean.getCircleId()+" ## Partner : "+kmSearchDTO.getPartner()+
								" ## Location :: "+kmSearchDTO.getLocation());
						String udId = sessionUserBean.getUdId();
						//String udId = "A1247263";
						logger.info("UD Id is ::: "+udId);
						if(!udId.equals("") &&  udId != null){
							logger.info("Sending UDID .... ");
							result = searchService.insertSMSDetails(kmSearchDTO.getOlmId(),sessionUserBean.getUserLoginId(), mobileNo, smsTemplate, mainOption, sessionUserBean.getCircleId(),kmSearchDTO.getPartner(),kmSearchDTO.getLocation(),"Success",udId);
						}else{
							logger.info("Sending user login id .... ");
							result = searchService.insertSMSDetails(kmSearchDTO.getOlmId(),sessionUserBean.getUserLoginId(), mobileNo, smsTemplate, mainOption, sessionUserBean.getCircleId(),kmSearchDTO.getPartner(),kmSearchDTO.getLocation(),"Success","");
						}
						logger.info("SMS Data Inserted in database" +result);
						formBean.setSmsStatus("SMS '" + smsTemplate	+ "' sent to " + mobileNo);
					}

				else if (status.equals(Constants.SEND_SMS_STATUS_FAIL)) {
					logger.info(":((((((( msg didnt go !!!!!!!");
					formBean.setSmsStatus("SMS could not be sent");
				}
				else if (status.equals(Constants.SEND_SMS_STATUS_NOT_CONNECTED)) {
					logger.info(":((((((( msg didnt go due to network error !!!!!!!");
					formBean.setSmsStatus("SMS could not be sent.."+ Constants.SEND_SMS_STATUS_NOT_CONNECTED);
				}
		} catch (Exception ex) {
			logger.info("Exception  :" + ex.getMessage());
			formBean.setSmsStatus("SMS could not be sent due to some network issue");
			ex.printStackTrace();
		}

		
	}
		catch(KmException km){
			logger.info("No Configurable Data  :" + km.getMessage());
			km.printStackTrace();
			formBean.setSmsStatus(Constants.CONFIGUEABLE_DATA_NOT_FOUND);
		}
		catch (Exception ex) {
			logger.info("Exception  :" + ex.getMessage());
			ex.printStackTrace();
				formBean.setSmsStatus("Could not send sms due to invalid mobile number");
			 
		}
		if (formBean.getActorId() == 1)
			fwdStatus = "adminSmsPage";
		else
			fwdStatus = "csrSmsPage";
		return mapping.findForward(fwdStatus);
	}

}
