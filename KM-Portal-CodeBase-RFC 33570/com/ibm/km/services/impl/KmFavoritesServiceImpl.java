/*
 * Created on May 2, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.services.impl;

import java.util.List;

import com.ibm.km.dao.KmFavoritesDao;
import com.ibm.km.dao.impl.KmFavoritesDaoImpl;
import com.ibm.km.exception.KmException;
import com.ibm.km.services.KmFavoritesService;

/**
 * @author varunagg
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class KmFavoritesServiceImpl implements KmFavoritesService{
	
	public List showFavoritesService(String userId,String circleId) throws KmException {
		KmFavoritesDao dao = new KmFavoritesDaoImpl();
		List favoritesList = dao.getFavorites(Integer.parseInt(userId), circleId);
		return favoritesList;
	}
}
