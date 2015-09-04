/**
 * 
 */
package com.ibm.km.common;

public class Constants 
{
	public static final int DOC_TYPE_FILE = 1;
	public static final int DOC_TYPE_PRODUCT = 2;
	public static final int DOC_TYPE_SOP = 3;
	public static final int DOC_TYPE_SOP_BD = 4;
	public static final int DOC_TYPE_RC = 5;
	public static final int DOC_TYPE_BP = 6;
	public static final int DOC_TYPE_DTH = 7;
	public static final String SUPER_ADMIN  =  "1";
	public static final String CIRCLE_ADMIN =  "2";
	public static final String CIRCLE_USER  =  "3";
	public static final String CIRCLE_CSR   =  "4";
	public static final String LOB_ADMIN    =  "5";
	public static final String CATEGORY_CSR =  "6";
	public static final String REPORT_ADMIN =  "7";
	public static final String TSG_USER     =  "8";
	public static final String PAN_INDIA_SCROLLER = "1";
	public static final String TOP_LINKS = "1";
	public static final String BULK_USER_CREATION = "1";
	public static final String BULK_USER_UPDATION = "2";
	public static final String BULK_USER_DELETION = "3";
	public static final int BULK_UPLOAD_INVALID_EXCEL = 0;
	public static final int BULK_UPLOAD_INVALID_HEADER = 3;
	//added by aman
	public static final int BULK_UPLOAD_BLANK_EXCEL = 1;
	public static final int BULK_UPLOAD_FAIL = 5;
	public static final int BULK_UPLOAD_SUCCESS = 2;
	public static final int INVALID_FILESIZE = 15;
	
	public static final int DIST_UPLOAD = 20;
	public static final int ARC_UPLOAD = 10;
	public static final int COORDINATOR_UPLOAD = 13;
//end by aman
	
	public static final int VAS_UPLOAD = 7;
	
	public static final String RESPONSE_STRING_OK = "OK";
	public static final String SEND_SMS_STATUS_SUCCESS = "SMS Successfully send ";
	public static final String SEND_SMS_STATUS_FAIL = "Failed to send SMS";
	public static final String SEND_SMS_STATUS_NOT_CONNECTED = "Could Not Connect to SMS Server";
	public static final String CONFIGUEABLE_DATA_NOT_FOUND = "No Configurable Data to send SMS:";
}
