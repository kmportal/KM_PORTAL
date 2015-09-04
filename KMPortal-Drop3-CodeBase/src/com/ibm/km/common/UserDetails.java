/**
 * 
 */
package com.ibm.km.common;

import javax.servlet.http.HttpServletRequest;

import com.ibm.km.dto.KmUserMstr;

/**
 * @author Kumar Saurabh
 *
 */
public class UserDetails 
{
	/**
	 * Method to return user Id of the logged in user
	 * @param request userLoginId of the user
	 * @return
	 */
	public static String getUserLoginId(HttpServletRequest request)
	{
		String userLoginId = "";
		/*
		 * Added on 13th March 2008
		 * Checking whether user is CRM User
		 */
		if(request.getSession().getAttribute("CRM_USER_NAME")!=null) {
			return (String)request.getSession().getAttribute("CRM_USER_NAME");
		}
		if(request.getSession().getAttribute("USER_INFO")!=null)
		{
			KmUserMstr userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
			userLoginId =  userBean.getUserLoginId();
		}
		return userLoginId;
	}
	
	/**
	 * Method to return user Id of the logged in user
	 * @param request
	 * @return UserBean containing all the details
	 */
	public static KmUserMstr getUserDetails(HttpServletRequest request)
	{
		KmUserMstr userBean = new KmUserMstr();
		if(request.getSession().getAttribute("USER_INFO")!=null)
		{
			userBean = (KmUserMstr)request.getSession().getAttribute("USER_INFO");
		}
		return userBean;
	}

}
