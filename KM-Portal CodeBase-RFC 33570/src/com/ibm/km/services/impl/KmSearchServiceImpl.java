/*
 * Created on Apr 30, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import com.ibm.km.common.Constants;
import com.ibm.km.common.ResourceProperty;
import com.ibm.km.dto.KmSearchDetailsDTO;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Hits;
import org.apache.lucene.store.AlreadyClosedException;

import com.ibm.km.common.PropertyReader;
import com.ibm.km.dao.KmSearchDao;
import com.ibm.km.dao.impl.KmSearchDaoImpl;
import com.ibm.km.dto.KmDocumentMstr;
import com.ibm.km.dto.KmElementMstr;
import com.ibm.km.dto.KmSearch;
import com.ibm.km.dto.NetworkErrorLogDto;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;
import com.ibm.km.networkfault.SearchFault;
import com.ibm.km.search.SearchFiles;
import com.ibm.km.forms.KmDocumentMstrFormBean;
import com.ibm.km.services.KmElementMstrService;
import com.ibm.km.services.KmSearchService;

/**
 * @author Anil
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class KmSearchServiceImpl implements KmSearchService {
	private static Logger logger = Logger.getLogger(KmSearchServiceImpl.class
			.getName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.km.services.KmSearchService#search(com.ibm.km.dto.KmSearch)
	 */
	public ArrayList search(KmSearch dto) throws KmException {
		KmSearchDao dao = new KmSearchDaoImpl();
		return dao.search(dto);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.km.services.KmSearchService#search(com.ibm.km.dto.KmSearch)
	 */
	public ArrayList contentSearch(KmSearch dto) throws KmException {
		ArrayList documentList = new ArrayList();
		
		try {
			SearchFiles searchObject = new SearchFiles();
			Hits documentHits = searchObject.search(dto.getKeyword());
			// ResourceBundle bundle =
			// ResourceBundle.getBundle("ApplicationResources");
			KmElementMstrService elementService = new KmElementMstrServiceImpl();
			int docSize = 0;
			if (documentHits != null) {

				docSize = documentHits.length();
				logger.info("docsize :: "+docSize);
				// String[] documentId=new String[documentHits.length()];
				/*
				 * String[] documentId = new String[docSize];
				 * 
				 * for (int start = 0; start < docSize; start++) { Document doc
				 * = documentHits.doc(start); documentId[start] =
				 * doc.get("documentId");
				 * 
				 * 
				 * }
				 */
				String[] documentIds = new String[docSize];
				String[] circleDocumentIds = null;
				String[] pandocumentIds = null;
				KmDocumentMstr document = null;
				ArrayList circleDocumentList = new ArrayList();
				ArrayList pandocumentList = new ArrayList();
				for (int start = 0; start < docSize; start++) {
					Document doc = documentHits.doc(start);
					documentIds[start] = doc.get("documentId");
					//logger.info("documentIds[start] :: "+documentIds[start]);
					//logger.info("Element ID :"+dto.getElementId()+" :: Circle Id :: "+doc.get("circleId")+" ## "+(dto.getElementId()).equals(doc.get("circleId")));
					try {
						if ((dto.getElementId()).equals(doc.get("circleId"))) {
						//	logger.info("Equal !! ");
							// if((dto.getElementId()).equals(elementService.
							// extractCircleId(doc.get("path"),5))){
							circleDocumentList.add(doc.get("documentId"));
							

						} else if ((dto.getPanCircle()).equals(doc
								.get("circleId"))) {
							// }else
							// if((dto.getPanCircle()).equals(elementService
							// .extractCircleId(doc.get("path"),5))){
							pandocumentList.add(doc.get("documentId"));
						}

					}
					
					
					catch (Exception e) {
						// logger.error(e);
						 e.printStackTrace();
					}
				}
				//logger.info(circleDocumentList.size()+" #### ");
				//logger.info(pandocumentList.size()+" !!! ");
				/*
				 * circleDocumentIds=(String[])circleDocumentList.toArray(new
				 * String[circleDocumentList.size()]);
				 * pandocumentIds=(String[])pandocumentList.toArray(new
				 * String[pandocumentList.size()]);
				 */

				if (docSize != 0) {
					KmSearchDao dao = new KmSearchDaoImpl();
					dto.setMaxFiles(Integer.parseInt(PropertyReader
							.getAppValue("maxno.files")));

					if ((dto.getActorId().equals(Constants.SUPER_ADMIN) || dto
							.getActorId().equals(Constants.LOB_ADMIN))) {
						documentList = dao.contentSearch(dto, documentIds);
					} else {
						if (dto.getActorId().equals(Constants.CIRCLE_ADMIN)
								|| dto.getActorId().equals(
										Constants.CIRCLE_USER)
								|| dto.getActorId()
										.equals(Constants.CIRCLE_CSR)
								|| dto.getActorId().equals(
										Constants.CATEGORY_CSR)) {

							if (circleDocumentList.size() == 0) {
								//logger.info(" NULL ");
								documentList = null;

							} else {
								//logger.info(" $%^%$^%$%^ ");
								circleDocumentIds = (String[]) circleDocumentList
										.toArray(new String[circleDocumentList
												.size()]);
								documentList = dao.contentSearch(dto,
										circleDocumentIds);

							}
						}

						if ((dto.getActorId().equals(Constants.CIRCLE_CSR) || dto
								.getActorId().equals(Constants.CATEGORY_CSR))
								&& (pandocumentList.size() != 0)
								&& !dto.getElementId().equals(
										dto.getPanCircle())) {

							dto.setElementId(dto.getPanCircle());

							dto.setDocumentList(documentList);

							pandocumentIds = (String[]) pandocumentList
									.toArray(new String[pandocumentList.size()]);
							documentList = dao.contentSearch(dto,
									pandocumentIds);

						}
					}
				}
			}

		} catch (AlreadyClosedException ex) {

			ex.printStackTrace();
		} catch (KmException e) {
			e.printStackTrace();
			throw new KmException("SQLException: " + e.getMessage(), e);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return documentList;
	}

	public ArrayList contentSearchCSR(KmSearch dto) throws KmException {
		ArrayList documentList = null;
		try {
			SearchFiles searchObject = new SearchFiles();
			Hits documentHits = searchObject.searchNew(dto.getKeyword(), dto
					.getElementId());
			Hits panDocumentHits = searchObject.searchNew(dto.getKeyword(), dto
					.getPanCircle());
			if (documentHits != null || panDocumentHits != null) {

				int docSize = (documentHits == null) ? 0 : documentHits
						.length();
				int panDocSize = (panDocumentHits == null) ? 0
						: panDocumentHits.length();

				// String [] documentIds=new String [docSize];
				String[] circleDocumentIds = new String[docSize];
				String[] pandocumentIds = new String[panDocSize];
				for (int start = 0; start < docSize; start++) {
					Document doc = documentHits.doc(start);
					// System.out.println("***********************"+doc);
					circleDocumentIds[start] = doc.get("documentId");

				}
				for (int start = 0; start < panDocSize; start++) {
					Document doc = panDocumentHits.doc(start);
					// System.out.println("***********************"+doc);
					pandocumentIds[start] = doc.get("documentId");
				}
				if (docSize + panDocSize > 0) {
					KmSearchDao dao = new KmSearchDaoImpl();
					dto.setMaxFiles(Integer.parseInt(PropertyReader
							.getAppValue("maxno.files")));
					if (docSize > 0)
						documentList = dao
								.contentSearch(dto, circleDocumentIds);
					if (panDocSize > 0) {
						dto.setElementId(dto.getPanCircle());
						dto.setDocumentList(documentList);
						documentList = dao.contentSearch(dto, pandocumentIds);
					}

				}
			}

		} catch (AlreadyClosedException ex) {

			ex.printStackTrace();
		} catch (KmException e) {
			throw new KmException("SQLException: " + e.getMessage(), e);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return documentList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.km.services.KmSearchService#getChange(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	public ArrayList getChange(String Id, String condition, String circleId)
			throws KmException {
		KmSearchDao searchDao = new KmSearchDaoImpl();
		ArrayList getChangeValues = null;
		try {
			getChangeValues = searchDao.getChange(Id, condition, circleId);
		} catch (Exception e) {
			if (e instanceof DAOException) {
				// logger.error("DAOException occured in getChange():" +
				// e.getMessage());
				// throw new HBOException(e.getMessage());
			} else {
				// logger.error("Exception occured in getChange):" +
				// e.getMessage());
			}
		}
		return getChangeValues;
	}

	/* km phase2 csrKeyword Search */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibm.km.services.KmSearchService#csrSearch(com.ibm.km.dto.KmSearch)
	 */
	public ArrayList csrSearch(KmSearch dto) throws KmException {
		KmSearchDao dao = new KmSearchDaoImpl();
		return dao.csrSearch(dto);
	}

	public ArrayList contentSearchAdmin(KmSearch dto) throws KmException {
		ArrayList documentList = new ArrayList();

		try {
			SearchFiles searchObject = new SearchFiles();
			if (dto.getActorId().equals(
					PropertyReader.getAppValue("Superadmin"))
					|| dto.getActorId().equals(
							PropertyReader.getAppValue("LOBAdmin"))) {
				KmElementMstrService elementService = new KmElementMstrServiceImpl();
				ArrayList circleList = elementService.getAllChildren(dto
						.getElementId(), "3");
				if (circleList != null)
					for (Iterator itr = circleList.iterator(); itr.hasNext();) {
						KmElementMstr element = (KmElementMstr) itr.next();
						List docList = (ArrayList) getDocumentIds(dto
								.getKeyword(), element.getElementId(),
								searchObject);
						if (docList != null)
							documentList.addAll(docList);
					}
			} else {
				documentList = getDocumentIds(dto.getKeyword(), dto
						.getElementId(), searchObject);
			}

			if (documentList != null) {

				String[] documentIds = (String[]) documentList
						.toArray(new String[documentList.size()]);
				documentList = new ArrayList();
				KmSearchDao dao = new KmSearchDaoImpl();
				dto.setMaxFiles(Integer.parseInt(PropertyReader
						.getAppValue("maxno.files")));
				documentList = dao.contentSearch(dto, documentIds);

			}

		}

		catch (Exception e) {

			e.printStackTrace();
		}

		return documentList;
	}

	public ArrayList getDocumentIds(String keyword, String circleId,
			SearchFiles searchObject) {
		ArrayList documentList = null;
		try {

			Hits documentHits = searchObject.searchNew(keyword, circleId);

			if (documentHits != null) {
				documentList = new ArrayList();
				int docSize = documentHits.length();
				for (int start = 0; start < docSize; start++) {
					Document doc = documentHits.doc(start);
					documentList.add(doc.get("documentId"));
				}
			}

		} catch (AlreadyClosedException ex) {
			ex.printStackTrace();

			logger.error(ex);
		} catch (KmException e) {
			e.printStackTrace();
			logger.error(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}

		return documentList;
	}

	public ArrayList getDocumentIdCir(String keyword, String circleId) {
		ArrayList documentList = null;
		try {

			SearchFiles searchObject = new SearchFiles();

			Hits documentHits = searchObject.search(keyword);

			if (documentHits != null) {
				documentList = new ArrayList();
				int docSize = documentHits.length();
				for (int start = 0; start < docSize; start++) {
					Document doc = documentHits.doc(start);
					documentList.add(doc.get("documentId"));
				}
			}

		} catch (AlreadyClosedException ex) {
			ex.printStackTrace();

			logger.error(ex);
		} catch (KmException e) {
			e.printStackTrace();
			logger.error(e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}

		return documentList;
	}

	public List locationSearch(KmSearch dto) throws KmException {
		List networkFaultList = new ArrayList();
		NetworkErrorLogDto networkErrorLogDto = null;
		try {
			Set<NetworkErrorLogDto> networkFaultSet = new TreeSet<NetworkErrorLogDto>();

			Hits hits = new SearchFault().search(dto.getKeyword().trim());
			if (hits == null || hits.length() == 0) {
				return networkFaultList;
			}
			for (int index = 0; index < hits.length(); index++) {
				networkErrorLogDto = new NetworkErrorLogDto();
				org.apache.lucene.document.Document documentNew = hits
						.doc(index);

				networkErrorLogDto.setProblemId(documentNew.get("problemId"));
				networkErrorLogDto.setCircleName(documentNew.get("circleName"));
				networkErrorLogDto.setAreaAffected(documentNew
						.get("areaAffected"));
				networkErrorLogDto.setProblemDesc(documentNew
						.get("problemDesc"));
				networkErrorLogDto.setLoggingTime(documentNew
						.get("loggingTime"));
				networkErrorLogDto.setTat(documentNew.get("tat"));

				networkFaultList.add(networkErrorLogDto);
			}

			// Collections.sort(networkFaultList);
			/*
			 * networkFaultDtoMap.put(new
			 * Integer(Integer.parseInt(networkErrorLogDto.getProblemId())),
			 * networkErrorLogDto);
			 * 
			 * Set keySet = networkFaultDtoMap.keySet();
			 * //System.out.println("k size "+keySet.size());
			 * 
			 * Iterator itr = networkFaultDtoMap.keySet().iterator(); while
			 * (itr.hasNext()) { Object serviceMapKey = itr.next(); if
			 * (networkFaultDtoMap.get(serviceMapKey) != null) {
			 * //System.out.println(
			 * ((NetworkErrorLogDto)networkFaultDtoMap.get(
			 * serviceMapKey)).getProblemId() ) ; } }
			 */

		} catch (AlreadyClosedException ex) {

			ex.printStackTrace();
		} catch (KmException e) {
			throw new KmException("SQLException: " + e.getMessage(), e);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return networkFaultList;
	}

	public ArrayList<KmSearchDetailsDTO> searchDetails(String keyword,
			String mainOptionValue, String selectedSubOptionValue, int actorId,
			String loginId) throws KmException {

		KmSearchDao dao = new KmSearchDaoImpl();

		return dao.searchDetails(keyword, mainOptionValue,
				selectedSubOptionValue, actorId, loginId);

	}

	public ArrayList<KmSearchDetailsDTO> editDetails(String keyword,
			String mainOption, String subOption, int serialNo)
			throws KmException {

		KmSearchDao dao = new KmSearchDaoImpl();

		return dao.editDetails(keyword, mainOption, subOption, serialNo);

	}

	public String updateDetails(String keyword, String mainOption,
			KmDocumentMstrFormBean formBean, int serialNo) throws KmException {

		KmSearchDao dao = new KmSearchDaoImpl();

		return dao.updateDetails(keyword, mainOption, formBean, serialNo);

	}

	public String deleteDetails(String mainOption, int serialNo)
			throws KmException {

		KmSearchDao dao = new KmSearchDaoImpl();

		return dao.deleteDetails(mainOption, serialNo);

	}

	public ArrayList<KmSearchDetailsDTO> getConfigurableColumnList(
			String tableName) throws KmException {

		KmSearchDao dao = new KmSearchDaoImpl();

		return dao.getConfigurableColumnList(tableName);

	}

	public Map<String, Object> sendSms(String mainOption, int serialNo)
			throws KmException {

		KmSearchDao dao = new KmSearchDaoImpl();
		return dao.sendSms(mainOption, serialNo);
	}

	public int insertSMSDetails(String olmId,String userLoginId, String mobileNo,
			String smsTemplate, String mainOption, String circleId,
			String partner, String location,String status,String udId) throws KmException {

		KmSearchDao dao = new KmSearchDaoImpl();
		return dao.insertSMSDetails(olmId,userLoginId, mobileNo, smsTemplate,
				mainOption, circleId, partner, location,status,udId);
	}

	@Override
	public KmSearchDetailsDTO getUserDetailForSMS(String userLoginId)
			throws KmException {
		// TODO Auto-generated method stub
		KmSearchDao dao = new KmSearchDaoImpl();
		return dao.getUserDetailForSMS(userLoginId);
	}

}
