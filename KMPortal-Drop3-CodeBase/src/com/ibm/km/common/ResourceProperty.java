package com.ibm.km.common;

import java.io.FileInputStream;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ResourceProperty {


	protected static Logger logger = Logger.getLogger(ResourceProperty.class.getName());
	               
	private static String path = ""; 
	
	
	              
	// private static Resources instance = null;
	private static Properties applicationResources = new Properties();
	
	private static FileInputStream fis  = null;
	private static String bundleName;

//	 Last access time for properties file
	private static Date lastAccessedDate = new Date();

	// Referesh Interval in minutes
	private static final int REFERESH_INTERVAL = 1;

	// To refresh the bundle 
	private static boolean refreshFromBundle = true;

	
	
	static
	{
		try
		{
			logger.info("************static block here******************");
		applicationResources.clear();
//		setBundleName("/home/appsmf/config/VirtualizationResources");
		setBundleName(path+"RechargeCore");
		fis = new FileInputStream(bundleName);
		applicationResources.load(fis);
		}
		catch(Exception ex)
		{
			//System.out.println("*******************&&&&&&&&");
		}
	}

	
	
	
	public static void setBundleName(String string) {
		bundleName = string.replace('.', '/') + ".properties";
		try {
			fis = new FileInputStream(bundleName);
			applicationResources.load(fis);
		} catch (Exception e) {
			logger.error("Error in configuring properties file name : "+ Utility.getStackTrace(e));
		}
	}

	
	public static String getCoreResourceBundleValuez(String propertyKey)	 {

			try {
				Date currentDate = new Date();
				if ((currentDate.getTime() - lastAccessedDate.getTime()) > 1 * 1000 * 60 * REFERESH_INTERVAL && refreshFromBundle) {

				//System.out.println("1111111111111-----------------Loading the loader.........................." + REFERESH_INTERVAL);
				applicationResources.clear();
//				setBundleName("/home/appsmf/config/VirtualizationResources");
				setBundleName(path+"RechargeCore");
				fis = new FileInputStream(bundleName);
				applicationResources.load(fis);
				lastAccessedDate = currentDate;
			}

				//System.out.println("222222222222--------------Loading the loader............hahahahha..............");
				
				
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		return applicationResources.getProperty(propertyKey);
	}

	
	}
	

	

