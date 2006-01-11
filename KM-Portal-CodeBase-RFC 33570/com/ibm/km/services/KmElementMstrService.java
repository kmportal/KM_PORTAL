/*
 * Created on May 15, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import com.ibm.km.dto.KmElementMstr;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.dto.QuizReportDto;
import com.ibm.km.exception.KmException;

/**
 * @author Anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface KmElementMstrService {
	
	/*
	 * Method to return Circle Element ID & Circle Name in a Map
	 */
	
	 public HashMap<String, String> getAllCircleDesc()    throws KmException;
	 
	/**
	 * Method to retrieve children
	 * @param parentId
	 * @return
	 * @throws KmException
	 */
	//added by vishwas for get all lob
	 public HashMap<String, String>  getAllLobDesc() throws KmException;
	 //end by vishwas
	 
	 public ArrayList getChildren(String parentId) throws KmException;
	

	public Map<String, String> getElementPathList(String initialSelectedPath, String[] multipleCircles) throws KmException ;

	/**
	 * Method to retrieve children
	 * @param parentId
	 * @return
	 * @throws KmException
	 */
	// Bug resolved : MASDB00103891
	public ArrayList getChildren(String parentId, String elementLevelId) throws KmException;
	/**
	 * Method to retrieve all children based on parentId
	 * @param parentId
	 * @return
	 * @throws KmException
	 */
	public ArrayList getAllChildren(String parentId) throws KmException;
	
	//Addded by vishwas on 3/09/2014 for lob child and circle
	public ArrayList getAllChildrenUD(String parentId) throws KmException;
	public ArrayList getAllChildrencircleUD(String parentId) throws KmException;
	
	//end by vishwas
	/**
	 * Method to retrieve all children based on parent Id ,level Id
	 * @param parentId
	 * @param levelId
	 * @return
	 * @throws KmException
	 */
	public ArrayList getAllChildren(String parentId, String levelId) throws KmException;
	
	/**
	 * method to retrieve children with path
	 * @param parentId
	 * @param root
	 * @return
	 * @throws KmException
	 */
	public ArrayList getChildrenWithPath(String parentId, String root) throws KmException;
	
	/**
	 * Method to retrieve all Children with path
	 * @param parentId
	 * @param root
	 * @return
	 * @throws KmException
	 */
	public ArrayList getAllChildrenWithPath(String parentId, String root) throws KmException;
	
	/**
	 * Method to get Pan Child
	 * @param parentId
	 * @return
	 * @throws KmException
	 */
	public KmElementMstr getPanChild(String parentId) throws KmException;
	
	//added by vishwas
	public ArrayList getPanChild1(String parentId) throws KmException;
	//end by vishwas for method correction

	/**
	 * Method to get element level Name
	 * @param elementLevelId
	 * @return
	 * @throws KmException
	 */
	public String getElementLevelName(String elementLevelId) throws KmException;

	/**
	 * Method to get Element Level id
	 * @param elementId
	 * @return
	 * @throws KmException
	 */
	public String getElementLevelId(String elementId) throws KmException;

	/**
	 * Method to get  Child Elements   
	 * @param parentId
	 * @return JSONObject
	 * @throws Exception
	 */
	public JSONObject getElementsAsJson(String parentId) throws Exception;
	/**
	 * Method to get  Child Elements   
	 * @param parentId
	 * @return JSONObject
	 * @throws Exception
	 */
	
	public JSONObject getPANElementsAsJson(String parentId) throws Exception ;
	
	/**
	 * Method to get Document Paths
	 */
	public JSONObject getDocPathAsJson(String parentId) throws Exception;
	
	
	public JSONObject getElementsAsJsonNoPan(String parentId) throws Exception;


	/**
	 * method to retrieve all parent Id
	 * @param rootId
	 * @param elementId
	 * @return
	 * @throws KmException
	 */
	public String getAllParentIdString(String rootId, String elementId) throws KmException;
	
    /**
     * method to get all element level Name
     * @return
     * @throws KmException
     */
	public List getAllElementLevelNames() throws KmException;

	/**
	 * Method to get all parent Name
	 * @param rootId
	 * @param elementId
	 * @return
	 * @throws KmException
	 */
	public String getAllParentNameString(String rootId, String elementId) throws KmException;
	
	/**
	 * Method to get All Parent Id
	 * @param elementId
	 * @return
	 * @throws KmException
	 */
	public String getParentId(String elementId) throws KmException;
	
    /**
     * Method to Create Element
     * @param elementMstrDTO
     * @return
     * @throws KmException
     */
	public String createElement(KmElementMstr elementMstrDTO)throws KmException;
	
	/**
	 * Method to check Existing Element
	 * @param elementName
	 * @param parentId
	 * @return
	 * @throws KmException
	 */
	public String[] checkExistingElement(String elementName, String parentId)throws KmException;


	
	/**
	 * Method to move elements in DB
	 * @param movedDocumentList
	 * @param parentId
	 * @return
	 * @throws KmException
	 */
	public boolean moveElementsInDB(String[] movedDocumentList, String parentId)throws KmException;


	/**
	 * Method to get Element by Name
	 * @param elementId
	 * @return
	 * @throws KmException
	 */
	//added by vishwas
	public String[] getDisplayName(String[] movedDocumentList)throws KmException;
	public boolean copyElementsInDB(String[] movedDocumentList, String parentId,String elementid)throws KmException;
	//end y vishwas
	
	public String getElementName(String elementId)throws KmException;

	/**
	 * Method to change Element Level
	 * @param movedElementList
	 * @param levelDiff
	 * @return
	 * @throws KmException
	 */
	public boolean changeAllElementLevel(String[] movedElementList, int levelDiff) throws KmException;

	/**
	 * Method to move Elements
	 * @param movedElementList
	 * @param oldPath
	 * @param newPath
	 * @return
	 * @throws KmException
	 */
	public boolean moveElementsInFS(String[] movedElementList, String oldPath, String newPath) throws KmException;

	/**
	 * Method to get Children Id
	 * @param string
	 * @return
	 * @throws KmException
	 */
	public String[] getChildrenIds(String string)throws KmException;
	
	/**
	 * Method to get Children Id
	 * @param string
	 * @return
	 * @throws KmException
	 */
	public ArrayList getChildrenIds(String string,String elementLevel)throws KmException;
	
	
	/**
	 * Method to Edit Elements
	 * @param dto
	 * @throws KmException
	 */
	public void editElement(KmElementMstr dto) throws KmException;

	/**
	 * Method to get Element Details
	 * @param elementId
	 * @return
	 * @throws KmException
	 */
	public KmElementMstr getElementDto(String elementId)throws KmException;

	/**
	 * Method to Delete Elements
	 * @param elementId
	 * @param updatedBy
	 * @throws KmException
	 */
	public void deleteElement(String elementId, String updatedBy)throws KmException;

	/**
	 * Method to get Elements
	 * @param element
	 * @return
	 * @throws KmException
	 */
	public String[] getElements(String element)throws KmException;
	
	/**
	 * Method to get Elements
	 * @param element
	 * @return
	 * @throws KmException
	 */
	public String[] getElements(String[] element)throws KmException;

	/**
	 * Method to delete Elements
	 * @param elements
	 * @param updatedBy
	 * @throws KmException
	 */
	public void deleteElements(String[] elements, String updatedBy)throws KmException;

	/**
	 * Method to get Element Id
	 * @param documentId
	 * @return
	 * @throws KmException
	 */
	public String getElementId(String documentId)throws KmException;
	
	/**
	 * Method to get Circle Id
	 * @param elementId
	 * @return
	 * @throws KmException
	 */
	 public String getCircleId(String elementId)throws KmException;

	/**
	 * Method to get Documents
	 * @param movedElementList
	 * @return
	 * @throws KmException
	 */
	 public String[] getDocs(String[] movedElementList)throws KmException;

	/**
	 * Method to extract circle Id
	 * @param string
	 * @param level
	 * @return
	 * @throws KmException
	 */
	 public String extractCircleId(String string,int level)throws KmException;

	/**
	 * Method to get all children records
	 * @param elementId
	 * @param elementLevel
	 * @return
	 * @throws KmException
	 */
	 public ArrayList getAllChildrenRec(String elementId, String elementLevel) throws KmException;
	 
	/**
	 * Method to retrieve Category map
	 * @param circleId
	 * @param favCategoryId
	 * @return
	 * @throws KmException
	 */ 
	public HashMap retrieveCategoryMap(String circleId,String favCategoryId)throws KmException;
	
	/**
	 * Method to retrieve Category map
	 * @param circleId
	 *
	 * @return
	 * @throws KmException
	 */ 
	public HashMap retrieveSubCategoryMap(String elementId,String parentLevel, String childLevel)throws KmException;


	public ArrayList getAllChildrenNoPan(String parentId, String elementLevelId) throws KmException;

/*
 * @param circleId
 * return whether the circle is restricted or not
 */
	public boolean getCircleStatus(String circleId)throws KmException;


	public KmElementMstr getCompleteElementDetails(String subCategoryId) throws KmException;
	
	public ArrayList<Integer> getAllElementsAsPerLevel(int levelId) throws KmException;

	public String copyElement(String elementId, String path,String userId) throws KmException;
	
	 public KmElementMstr getElementDetails(int elementId) throws KmException;;
	 public ArrayList<KmElementMstr> getAllCircles() throws KmException;
	 public ArrayList<KmElementMstr> findElementsInCircle(int elementId, String favCategoryId) throws KmException;
	 public String updateElementSequence(String idAndValues) throws KmException;

	public String getServiceIdPath(String id)throws KmException;

	public String getServicePath(String id) throws KmException;

	public ArrayList<String> getServiceIds(String parentId)throws KmException;

	public ArrayList<QuizReportDto> getReport(KmUserMstr sessionUserBean)throws KmException;

	public ArrayList getCreateUserChildren(String elementId)throws KmException;

	public ArrayList<QuizReportDto> getQuizReport()throws KmException;
}
