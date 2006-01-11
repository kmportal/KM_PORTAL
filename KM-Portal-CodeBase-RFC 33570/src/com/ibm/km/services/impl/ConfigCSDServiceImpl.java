package com.ibm.km.services.impl;
/**
 * @author Ajil Chandran
 * Created On  11 Nov 2010
 */
import java.util.List;

import org.apache.log4j.Logger;

import com.ibm.km.actions.KmTSGAction;
import com.ibm.km.dao.ConfigCSDDao;
import com.ibm.km.dao.impl.ConfigCSDDaoImpl;
import com.ibm.km.dto.ConfigCSDDto;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;
import com.ibm.km.services.ConfigCSDService;

public class ConfigCSDServiceImpl implements ConfigCSDService{
	private static final Logger logger;

	static {

		logger = Logger.getLogger(ConfigCSDServiceImpl.class.getName());
	}
	public List fetchCsdUsers(int circleId) throws KmException {
		ConfigCSDDao dao= new ConfigCSDDaoImpl();
		List csdNumbersList = null;
		try{
		csdNumbersList	=dao.fetchCsdUsers(circleId);
		}catch (DAOException e) {
			throw new KmException("Exception: " + e.getMessage(), e);
		}catch (Exception e) {
			throw new KmException("Exception: " + e.getMessage(), e);
		}
		return csdNumbersList;
	}

	public int insertCsd(ConfigCSDDto configCSDDto) throws KmException {
		int result=0;
		ConfigCSDDao dao= new ConfigCSDDaoImpl();
		// check if the user already exixts in other circle
		try{
		result=dao.insertCsd(configCSDDto);
		}
		catch (DAOException e) {
			throw new KmException("Exception: " + e.getMessage(), e);
		}catch (Exception e) {
			throw new KmException("Exception: " + e.getMessage(), e);
		}
		return  result;
		
	}

	public int removeCsd(String[] csdList, String circleId, String status) throws KmException {
		ConfigCSDDao dao= new ConfigCSDDaoImpl();
		int result=0;
		try{
		result=dao.updateCsd(csdList, circleId, status);
		}
		catch (DAOException e) {
			throw new KmException("Exception: " + e.getMessage(), e);
		}catch (Exception e) {
			throw new KmException("Exception: " + e.getMessage(), e);
		}
		return result;
	}

}
