/*
 * Created on May 15, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ibm.km.dto.KmElementMstr;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.dto.QuizReportDto;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;

/**
 * @author Anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface KmElementMstrDao {

	public HashMap<String, String> getAllCircleDesc()    throws KmException;
	/**
	 * @param parentId
	 */
	//added by vishwas for getting all Lob
	public HashMap<String, String> getAllLobDesc()    throws KmException;
	//end by vishwas
	public ArrayList getChildren(String parentId) throws KmException;
	
	/**
	 * @param parentId, elementLevelId
	 */
	public ArrayList getChildren(String parentId, String elementLevelId) throws KmException;	

	/**
	 * @param parentId
	*/
	public ArrayList getAllChildren(String parentId) throws KmException;
	
	//added by vishwas for UD lob
	public ArrayList getAllChildrenUD(String parentId) throws KmException;
	public ArrayList getAllChildrencircleUD(String parentId) throws KmException;
	//END by vishwas for UD Lob child
	
	public ArrayList getAllPANChildren(String parentId)   throws KmException;

	public ArrayList getCreateUserChildren(String parentId) throws KmException;
	
	/**
	 * @param parentId
	 */
	public ArrayList getChildrenWithPath(String parentId, String root) throws KmException;

	/**
	 * @param parentId
	*/
	public ArrayList getAllChildrenWithPath(String parentId, String root) throws KmException;	
	
	 public ArrayList getDocList(String parentId)    throws KmException;

	/**
	 * @param elementId
	 * @return elementLevelName
	 */
	public String getElementLevelName(String elementLevelId) throws KmException;

	/**
	 * @param elementId
	 * @return elementLevelId
	 */
	public String getElementLevelId(String elementId) throws KmException;

	/**
	 * @param elementId
	 * @return
	 */
	public String getAllParentIdString(String rootId, String elementId) throws KmException;
	
	public List getElementLevelNames() throws KmException;

	/**
	 * @param elementId
	 * @return
	 */
	public String getAllParentNameString(String rootId, String elementId) throws KmException;
	/**
	 * @param elementId
	 * @return
	 */
	public String getParentId(String elementId)throws KmException;

	/**
	 * @param parentId
	 * @return
	 */
	public KmElementMstr getPanChild(String parentId)throws KmException;

	
//added by vishwas
	public ArrayList<KmElementMstr> getPanChild1(String parentId)throws KmException;
	//end by vishwas
	/**
	 * @param elementMstrDTO
	 * @return
	 */
	public String insertElement(KmElementMstr elementMstrDTO)throws KmException;

	/**
	 * @param parentId
	 * @return
	 */
	public ArrayList getAllDocuments(String parentId)throws KmException;

	/**
	 * @param elementName
	 * @param parentId
	 * @return
	 */
	public String[] checkExistingElement(String elementName, String parentId)throws KmException;



	/**
	 * @param movedDocumentList
	 * @param parentId
	 * @param documentPath
	 * @return
	 */
	public boolean moveElements(String[] movedDocumentList, String parentId)throws KmException;
//added by vishwas for copy document
	public boolean insertCopyElements(String[] movedDocumentList, String parentId ,String elementid)throws KmException;
	
	//end by vishwas for copy document


	/**
	 * @param elementId
	 * @return 
	 */
	//added by vishwas
	public String[] DisplayName(String[] movedDocumentList) throws KmException;
	// end by vishwas
	public String getElementName(String elementId) throws KmException;

	/**
	 * @param string
	 * @return
	 */
	public ArrayList getAllLevelChildren(String string) throws KmException;

	/**
	 * @param childrenList
	 * @param levelDiff
	 */
	public void updateElementLevel(ArrayList childrenList, int levelDiff) throws KmException;

	/**
	 * @param elementId
	 * @return
	 */
	public String[] getChildrenIds(String elementId) throws KmException;


	/**
	 * @param elementId
	 * @return
	 */
	public ArrayList getChildrenIds(String elementId,String elementLevel) throws KmException;

	
	/**
	 * @param parentId
	 * @param levelId
	 * @return
	 */
	public ArrayList getAllChildren(String parentId, String levelId) throws KmException;

	/**
	 * @param dto
	 */
	public void editElement(KmElementMstr dto)throws KmException;



	/**
	 * @param elementId
	 * @return
	 */
	public KmElementMstr getElemetDto(String elementId)throws KmException;

	/**
	 * @param elementId
	 * @return
	 */
	public String[] getElements(String elementId)throws KmException;

	/**
	 * @param elements
	 */
	public String[] getElements(String[] elements)throws KmException;

	/**
	 * @param elements
	 * @param updatedBy
	 */
	public void deleteElements(String[] elements, String updatedBy)throws KmException;

	/**
	 * @param documentId
	 * @return
	 */
	public String getElementId(String documentId)throws KmException;

	/**
	 * @param elementId
	 * @return
	 */
	public String getCircleId(String elementId)throws KmException;

	/**
	 * @param movedElementList
	 * @return
	 */
	public String[] getDocs(String[] movedElementList)throws KmException;
	/**
	 * @param parentId
	 * @return
	 */

	public ArrayList getAllChildrenRec(String parentId, String elementLevel)throws KmException;

	/**
	 * @param circleId
	 * @return
	 */
	public HashMap getCategoryMapElements(String circleId, String favCategoryId) throws KmException;
	/**
	 * @param circleId
	 * @return
	 */
	public HashMap getSubCategoryMapElements(String elementId,int parentLevel, int childLevel) throws KmException;

	public ArrayList getAllChildrenNoPan(String parentId, String elementLevelId)throws KmException;

	public boolean getCircleStatus(String circleId) throws KmException;

	public KmElementMstr getCompleteElementDetails(String elementId)throws DAOException;
	
	public ArrayList<Integer> getAllElementsAsPerLevel(int levelId) throws KmException;

    public ArrayList getAllChildrenElements(String parentId) throws KmException;
    
    public KmElementMstr getElementDetails(int elementId) throws KmException;

    public void updateLevel(int elementId, int newLevel) throws KmException ;
    public ArrayList<KmElementMstr> getAllCircles() throws KmException;
    public ArrayList<KmElementMstr> findElementsInCircle(int elementId, String favCategoryId) throws KmException;
    public String updateElementSequence(String idAndValues) throws KmException;
	public ArrayList<String> getServiceIds(String elementId)throws KmException;
	public String getServicePath(String elementId)throws KmException;
	public String getServiceIdPath(String elementId)throws KmException;
	public ArrayList<QuizReportDto> getReport(KmUserMstr sessionUserBean)throws KmException;
	public ArrayList<QuizReportDto> getQuizReport()throws KmException;

}
