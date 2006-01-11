package com.ibm.km.services;

import java.util.ArrayList;

import org.json.JSONObject;

import com.ibm.km.dto.KmCsrLatestUpdatesDto;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;

public interface KmCsrLatestUpdatesService {
	
	public ArrayList initGetUpdates(String lobId, String circleId) throws DAOException;
	
	public JSONObject getElementsAsJson(String lobId, String circleId) throws Exception;
	
	public int insertLatestUpdates(KmCsrLatestUpdatesDto dto) throws KmException;

}
