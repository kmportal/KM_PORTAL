package com.ibm.km.services.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;


import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.ibm.km.dao.KmCsrLatestUpdatesDao;
import com.ibm.km.dao.impl.KmCsrLatestUpdatesDaoImpl;
import com.ibm.km.dao.impl.KmDocumentMstrDaoImpl;
import com.ibm.km.dto.KmCsrLatestUpdatesDto;
import com.ibm.km.dto.KmElementMstr;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;
import com.ibm.km.services.KmCsrLatestUpdatesService;
import com.ibm.km.services.KmElementMstrService;

public class KmCsrLatestUpdatesServiceImpl implements KmCsrLatestUpdatesService{

	private static Logger logger = Logger.getLogger(KmCsrLatestUpdatesServiceImpl.class);

	public ArrayList initGetUpdates(String lobId, String circleId) throws DAOException{
		ArrayList list = new ArrayList();
		
		KmCsrLatestUpdatesDao dao = new KmCsrLatestUpdatesDaoImpl();
		list = dao.initGetUpdates(lobId, circleId);
		
		return list;
	}
	public JSONObject getElementsAsJson(String lobId, String circleId) throws Exception {
		ArrayList list = new ArrayList();
		KmCsrLatestUpdatesDao dao = new KmCsrLatestUpdatesDaoImpl();
		list = dao.initGetUpdates(lobId, circleId);
		
		JSONObject json=new JSONObject();
		JSONArray jsonItems=new JSONArray();
		String level=null;
		//List list = getAllChildren(parentId);
		for (Iterator iter=list.iterator(); iter.hasNext();) {
			KmCsrLatestUpdatesDto dto=(KmCsrLatestUpdatesDto)iter.next();
			jsonItems.put(dto.toJSONObject());
			//level=dto.getElementLevel();
		}
		json.put("elements", jsonItems);
		//json.put("level", level);
		return json;

	}
	
	public int insertLatestUpdates(KmCsrLatestUpdatesDto dto) throws KmException{
		int recInserted=0;
		try
		{
			KmCsrLatestUpdatesDao dao = new KmCsrLatestUpdatesDaoImpl();
			recInserted =  dao.insertLatestUpdates(dto);
		}
		catch (Exception e) {
			logger.error(e.getMessage());
			throw new KmException(e.getMessage());
		}
		return recInserted;
	}
	
	
	
}
