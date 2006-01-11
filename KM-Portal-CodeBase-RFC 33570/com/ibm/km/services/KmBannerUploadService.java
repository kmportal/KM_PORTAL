package com.ibm.km.services;

import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;

import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;

public interface KmBannerUploadService {
	
	public int uploadBanner(byte[] data,String fileName, String bannerPage)throws KmException,DAOException;

	public InputStream retrieveBanner(String bannerPage) throws KmException,DAOException;
	
	
}
