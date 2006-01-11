package com.ibm.km.services.impl;

import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;

import com.ibm.km.dao.KmBannerUploadDao;
import com.ibm.km.dao.impl.KmBannerUploadDaoImpl;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;
import com.ibm.km.services.KmBannerUploadService;

public class KmBannerUploadServiceImpl implements KmBannerUploadService{

	public int uploadBanner(byte[] data,String fileName, String bannerPage) throws KmException,DAOException{
		
		KmBannerUploadDao dao = new KmBannerUploadDaoImpl();
		return dao.uploadBanner(data, fileName, bannerPage);

	}

	public InputStream retrieveBanner(String bannerPage) throws KmException,DAOException{
		
		KmBannerUploadDao dao = new KmBannerUploadDaoImpl();
		return dao.retrieveBanner(bannerPage);
	}
	

}
