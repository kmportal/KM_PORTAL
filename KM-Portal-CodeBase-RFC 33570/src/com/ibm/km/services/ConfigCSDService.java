package com.ibm.km.services;
/**
 * @author Ajil Chandran
 * Created On  11 Nov 2010
 */
import java.util.List;

import com.ibm.km.dto.ConfigCSDDto;
import com.ibm.km.exception.KmException;

public interface ConfigCSDService {

	public int insertCsd (ConfigCSDDto configCSDDto) throws KmException;
	
	public List fetchCsdUsers(int circleId) throws KmException;
	
	public int removeCsd(String[] csdList, String circleId,String status) throws KmException;
	/*
	 * in this method instead of deletion we changes the status to 'D'
	 */
}
