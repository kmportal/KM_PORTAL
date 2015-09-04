package com.ibm.km.services.impl;

import java.util.ArrayList;

import com.ibm.km.dao.KmDynamicIndexPageDao;
import com.ibm.km.dao.impl.KmDynamicIndexPageDaoImpl;
import com.ibm.km.services.KmDynamicIndexPageService;

public class KmDynamicIndexPageServiceImpl implements KmDynamicIndexPageService{
	
	public ArrayList loadCategories(int categoryId,String firstiteration) throws Exception{
		ArrayList categories = null;
		
		KmDynamicIndexPageDao dao = new KmDynamicIndexPageDaoImpl();
		categories = dao.loadCategories(categoryId,firstiteration);
		
		
		return categories;
		
		
	}

}
