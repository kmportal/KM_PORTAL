/*
 * Created on Oct 13, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.common;

/**
 * @author Anil
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.ibm.appsecure.exception.ValidationException;
import com.ibm.appsecure.service.GSDService;
import com.ibm.appsecure.util.Encryption;
import com.ibm.appsecure.util.IEncryption;
import com.ibm.km.dao.DBConnection;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;
import com.ibm.km.exception.KmUserMstrDaoException;
import com.ibm.km.services.KmUserMstrService;
import com.ibm.km.services.impl.KmUserMstrServiceImpl;

public class CsvReader {

	private static final Logger logger;
	static {
		logger = Logger.getLogger(CsvReader.class);
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	StringBuffer loginIdsNotcreated = new StringBuffer("");
	String error="";
	String status="S";
	public String bulkUpload(String filePath, String userId, String elementId)
		throws IOException, KmException {
		// TODO Auto-generated method stub
		ResourceBundle bundle =
			ResourceBundle.getBundle("ApplicationResources");

		try {

			
			File excelFile = new File(filePath);
			FileReader f = new FileReader(excelFile);
			BufferedReader br = new BufferedReader(f);
			Connection con = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			String line = "";
			String user_login_id = "";
			String user_fname = "";
			String user_mname = "";
			String user_lname = "";
			String user_mobile_number = "";
			String user_emailId = "";
			String user_password = "";
			String user_psswrd_expry_dt = "";
			String created_dt = "";
			String created_by = "100"; //User Id of the logged in user
		
			String fav_category_name = "";
			String element_id = "";
			
			KmUserMstrService userService = new KmUserMstrServiceImpl();
		//	Map hmp = null;
			String[] columnValues = new String[24];
			int j=0;
			while ((line = br.readLine()) != null) {
				j++;
				if(j==1)
					continue;
				try {
					String rowString = line;
					boolean wasDelimiter = true;
					String token = null;
					StringTokenizer st =
						new StringTokenizer(rowString, ",", true);
					int i = 1;
					while (st.hasMoreTokens()) {
						token = st.nextToken();
						if (token.equals(",")) {
							if (wasDelimiter) {
								token = "";
							} else {
								token = null;
							}
							wasDelimiter = true;
						} else {
							wasDelimiter = false;
						}
						if (token != null) {
							columnValues[i] = token.trim();
							i++;
						}
						//columnValues[i]	=st.nextToken().trim();
						//i++;             
					}

					user_login_id = columnValues[1].toUpperCase();
					//Check for duplicate user id
					if (userService.checkDuplicateUserLogin(user_login_id)) {
						logger.error(
							"USER ID :" + user_login_id + " ALREADY EXISTS");
						loginIdsNotcreated.append(user_login_id).append(",");
						continue;
					}
					user_fname = columnValues[2];
					user_mname = columnValues[3];

					user_lname = columnValues[4];
					user_mobile_number = columnValues[5];
					user_emailId = columnValues[6];
					
					

					// Email Id validation
					
				/*	if(isEmailAddress(user_emailId)==false){
						status="P";
						loginIdsNotcreated.append(user_login_id).append(",");
						continue;
					}*/
                      
					fav_category_name = columnValues[7];
					if (fav_category_name == null)
						fav_category_name = "";
					fav_category_name = fav_category_name.trim().toUpperCase();
					element_id = elementId;
					user_password = bundle.getString("default.password");
					//Element id of the logged in user from the session
					//	user_alerts = columnValues[22];
					//	alert_id = columnValues[23];
					//System.out.println();

					//GSD validation 
					GSDService gSDService = new GSDService();
					IEncryption encrypt = new Encryption();
					gSDService.validateCredentials(
						user_login_id,
						user_password);
					user_password = encrypt.generateDigest(user_password);
					//System.out.println(user_password);
					String query = "";
					if (fav_category_name.trim() == "") {

						query =
							"insert into  KM_USER_MSTR values(NEXTVAL FOR  KM_USER_ID_SEQ,?, "
								+ " ?,?,?,?,?,?, "
								+ " current timestamp + 45 days, current timestamp, "
								+ " ?,null,null,'A',1,1,4,2,0,null,'N', "
								+ " null, "
								+ " ?,null,null )";

					} else {
						query =
							"insert into  KM_USER_MSTR values(NEXTVAL FOR  KM_USER_ID_SEQ,?, "
								+ " ?,?,?,?,?,?, "
								+ " current timestamp + 45 days, current timestamp, "
								+ " ?,null,null,'A',1,1,6,2,0,null,'N', "
								+ " (select element_id  from  km_element_mstr where parent_id = ? and upper(ELEMENT_NAME) = ?), "
								+ " ?,null,null )";
					}

					con = getConnection();
					con.setAutoCommit(false);
					ps = con.prepareStatement(query+ " with ur");
					int paramCount = 0;

					//	Preparing statement

					ps.setString(1, user_login_id.trim());
					ps.setString(2, user_fname.trim());
					ps.setString(3, user_mname.trim());
					ps.setString(4, user_lname);
					ps.setString(5, user_mobile_number);
					ps.setString(6, user_emailId.trim());
					ps.setString(7, user_password.trim());
					ps.setInt(8, Integer.parseInt(userId));
					if (fav_category_name.trim() == "") {
						ps.setString(9, element_id);
					} else {

						ps.setString(9, element_id.trim());
						ps.setString(10, fav_category_name.trim());
						ps.setString(11, element_id);
					}
					//	Executing querry

					ps.executeUpdate();
					con.commit();
					//System.out.println(user_login_id + "  IS CREATED ");

				} catch (ValidationException validationExp) {
					loginIdsNotcreated.append(user_login_id).append(",");
					status="P";
					logger.error(
						"Validation Exception in userloginid : "
							+ user_login_id
							+ "   : "
							+ validationExp);
					validationExp.printStackTrace();
					logger.error(
						"createUser method : caught ValidationException for GSD : "
							+ validationExp.getMessageId());
					// Fixed for Defect ID MASDB00070389
					//errors.add("errors.userName", new ActionError(validationExp
					//.getMessageId()));
					//createUserFormBean.setUserPassword("");
					//saveErrors(request, errors);
					//forward = mapping.findForward(CREATEUSER_FAILURE);
					continue;
					//return false;
				} catch (Exception e) {
					loginIdsNotcreated.append(user_login_id).append(",");
					status="P";
					logger.error(
						"Exception in userloginid : "
							+ user_login_id
							+ "   : "
							+ e);
					e.printStackTrace();
					continue;
					//return false;
				}
				finally{
					
					DBConnection.releaseResources(con,ps,rs);
					logger.info("USERS NOT CREATED : "+loginIdsNotcreated);
				}

			}

		} catch (Exception e) {
			
			status="F";
			error=getStackTrace(e);
			error=e.getClass().toString()+" :  Error Code "+e.getLocalizedMessage();
			//System.out.println("Error : "+error);
			logger.info("Exception occured while uploading the page " + e);
		}
		finally{
			/*
			 * Stroing the file details in DB
			*/
			Connection con1=null;
			PreparedStatement ps1=null;
			String loginIds="";
			String sql="INSERT INTO  KM_BULK_UPLOAD_FILES VALUES (NEXTVAL FOR  KM_FILE_ID_SEQ,?,current timestamp,?,?,?)";		
					try {
						con1=DBConnection.getDBConnection();
						ps1 = con1.prepareStatement(sql+" with ur");
						ps1.setString(1,filePath.substring((filePath.lastIndexOf("/")+1),filePath.length()));
						if(loginIdsNotcreated.length()>0)
							loginIds=loginIdsNotcreated.toString().substring(0,(loginIdsNotcreated.toString().length()-1));
						ps1.setString(2,loginIds);
						ps1.setString(3,status);
						ps1.setString(4,error);
						ps1.executeUpdate();
					} /*catch (SQLException e) {
						e.printStackTrace();
						throw new KmException("SQLException occured while stroing CSV file details in DB : " + e.getMessage(), e);
					}*/ catch (Exception e) {
						
						e.printStackTrace();
						//	logger.severe("Exception occured while find." + "Exception Message: " + e.getMessage());
						throw new KmException("Exception occured while stroing CSV file details in DB :  " + e.getMessage(), e);
					}
					finally{
						try {
							DBConnection.releaseResources(con1, ps1, null);
						} catch (DAOException e) {
							logger.info("Error while releasing connection",e);
							e.printStackTrace();
						}
						
					}
		}
		
		return status;

	}
	/*	public boolean bulkUpload(String filePath, String userId, String elementId)
			throws IOException, KmException {
			// TODO Auto-generated method stub
	
			try {
	
				//System.out.println(
					">>>>>>>>>>>>>>>>>>>>>>>>>>>>"
						+ userId
						+ ">>>>>>>>>"
						+ elementId);
				File excelFile = new File(filePath);
				FileReader f = new FileReader(excelFile);
				BufferedReader br = new BufferedReader(f);
				Connection con = null;
				PreparedStatement ps = null;
				ResultSet rs = null;
				String line = "";
				String user_login_id = "";
				String user_fname = "";
				String user_mname = "";
				String user_lname = "";
				String user_mobile_number = "";
				String user_emailId = "";
				String user_password = "";
				String user_psswrd_expry_dt = "";
				String created_dt = "";
				String created_by = "100"; //User Id of the logged in user
				String updated_dt = "";
				String updated_by = "";
				String status = "";
				String circle_id = "";
				String group_id = "";
				String km_actor_id = "";
				String km_owner_id = "";
				String login_attempted = "";
				String last_login_time = "";
				String user_login_status = "";
				String fav_category_name = "";
				String element_id = "";
				String user_alerts = "";
				String alert_id = "";
				KmUserMstrService userService = new KmUserMstrServiceImpl();
				Map hmp = null;
				String[] columnValues = new String[24];
				while ((line = br.readLine()) != null) {
					try {
						String rowString = line;
						boolean wasDelimiter = true;
						String token = null;
						StringTokenizer st =
							new StringTokenizer(rowString, ",", true);
						int i = 0;
						while (st.hasMoreTokens()) {
							token = st.nextToken();
							if (token.equals(",")) {
								if (wasDelimiter) {
									token = "";
								} else {
									token = null;
								}
								wasDelimiter = true;
							} else {
								wasDelimiter = false;
							}
							if (token != null) {
								columnValues[i] = token.trim();
								i++;
							}
							//columnValues[i]	=st.nextToken().trim();
							//i++;             
						}
	
						user_login_id = columnValues[0].toUpperCase();
	
						if (userService.checkDuplicateUserLogin(user_login_id)) {
							logger.error(
								"USER ID :" + user_login_id + " ALREADY EXISTS");
							continue;
						}
						user_fname = columnValues[1];
						user_mname = columnValues[2];
	
						user_lname = columnValues[3];
						user_mobile_number = columnValues[4];
						user_emailId = columnValues[5];
						user_password = columnValues[6];
						user_psswrd_expry_dt = columnValues[7];
						created_dt = columnValues[8];
						created_by = userId; //User Id of the logged in user 
						updated_dt = columnValues[10];
						updated_by = columnValues[11];
						status = columnValues[12];
						circle_id = columnValues[13];
						group_id = columnValues[14];
						km_actor_id = columnValues[15];
						km_owner_id = columnValues[16];
						login_attempted = columnValues[17];
						last_login_time = columnValues[18];
						user_login_status = columnValues[19];
						fav_category_name = columnValues[20].trim();
						element_id = elementId;
						//Element id of the logged in user from the session
						user_alerts = columnValues[22];
						alert_id = columnValues[23];
						//System.out.println();
	
						//GSD validation 
						GSDService gSDService = new GSDService();
						IEncryption encrypt = new Encryption();
						gSDService.validateCredentials(
							user_login_id,
							user_password);
						user_password = encrypt.generateDigest(user_password);
						//System.out.println(user_password);
						String query = "";
						if (fav_category_name.trim() == "") {
	
							query =
								"insert into  KM_USER_MSTR values(NEXTVAL FOR KM_USER_ID_SEQ,?, "
									+ " ?,?,?,?,?,?, "
									+ " current timestamp + 45 days, current timestamp, "
									+ " ?,null,null,'A',1,1,4,2,0,null,'N', "
									+ " null, "
									+ " ?,null,null )";
	
						} else {
							query =
								"insert into  KM_USER_MSTR values(NEXTVAL FOR  KM_USER_ID_SEQ,?, "
									+ " ?,?,?,?,?,?, "
									+ " current timestamp + 45 days, current timestamp, "
									+ " ?,null,null,'A',1,1,6,2,0,null,'N', "
									+ " (select element_id  from  km_element_mstr where parent_id = ? and ELEMENT_NAME = ?), "
									+ " ?,null,null )";
						}
	
						con = getConnection();
						con.setAutoCommit(false);
						ps = con.prepareStatement(query);
						int paramCount = 0;
	
						//	Preparing statement
	
						ps.setString(1, user_login_id.trim());
						ps.setString(2, user_fname.trim());
						ps.setString(3, user_mname.trim());
						ps.setString(4, user_lname);
						ps.setString(5, user_mobile_number);
						ps.setString(6, user_emailId.trim());
						ps.setString(7, user_password.trim());
						ps.setInt(8, Integer.parseInt(userId));
						if (fav_category_name.trim() == "") {
							ps.setString(9, element_id);
						} else {
	
							ps.setString(9, element_id.trim());
							ps.setString(10, fav_category_name.trim());
							ps.setString(11, element_id);
						}
						//	Executing querry
	
						ps.executeUpdate();
						con.commit();
						//System.out.println(user_login_id + "  IS CREATED ");
	
					} catch (ValidationException validationExp) {
						logger.error(
							"Validation Exception in userloginid : "
								+ user_login_id
								+ "   : "
								+ validationExp);
						validationExp.printStackTrace();
						logger.error(
							"createUser method : caught ValidationException for GSD : "
								+ validationExp.getMessageId());
						// Fixed for Defect ID MASDB00070389
						//errors.add("errors.userName", new ActionError(validationExp
						//.getMessageId()));
						//createUserFormBean.setUserPassword("");
						//saveErrors(request, errors);
						//forward = mapping.findForward(CREATEUSER_FAILURE);
						return false;
					} catch (Exception e) {
						logger.error(
							"Exception in userloginid : "
								+ user_login_id
								+ "   : "
								+ e);
						e.printStackTrace();
						return false;
					}
	
				}
	
			} catch (Exception e) {
				logger.info("Exception occured while uploading the page " + e);
			}
			return true;
	
		}  */
		public String getStackTrace(Exception ex) {
				java.io.StringWriter out = new java.io.StringWriter();
				ex.printStackTrace(new java.io.PrintWriter(out));
				String stackTrace = out.toString();
 
				return stackTrace;
			}
			public boolean isEmailAddress(String objInput)
			{
				
				//System.out.println("EMAIL CHECKING");
				if (objInput.equals(""))
				{
					return false;
				}

				String theInput = objInput.trim();
				int theLength = theInput.length();

				// there must be >= 1 character before @, so
				// start looking from character[1]
				// (i.e. second character in the text field)
				// look for '@'
				int i = 1;
				int cnt = 0;

				for(int j=0;j<theLength;j++)
				{
					if (theInput.charAt(j) == '@')
						cnt += 1;
				}

				if (cnt != 1)
				{
					//This cant be a email address
					return(false);
				}

				while ((i < theLength) && (theInput.charAt(i) != '@'))
				{
					// search till the last character
					i++ ;
				}

				if ((i >= theLength) || (theInput.charAt(i) != '@'))
				{
					// did not find the '@' character hence can't be email address.
					return (false);
				}
				else
				{
					// go 2 characters forward so that '@' and . are not simultaneous.
					i += 2;
				}

				// look for . (dot)
				while ((i < theLength) && (theInput.charAt(i) != '.'))
				{
					// keep searching for '.'
					i++ ;
				}

				// there must be at least one character after the '.'
				if ((i >= theLength - 1) || (theInput.charAt(i) != '.'))
				{
					// didn't find a '.' so its not a valid email ID
					return (false);
				}
				if(hasSpecialCharactersEmail(objInput))
				{
					return false;
				}
				else
				{
					// finally its got to be email ID
					return true;
				}
			}
			public boolean hasSpecialCharactersEmail(String field){   
			String SpecialCharacters="`~!$^&*()=+><{}[]+|=?':;\\\" ";
			if (field.length() >= 0)	{
			for(int i=0; i<SpecialCharacters.length(); i++)	{
				if(field.indexOf(SpecialCharacters.substring(i, 1))>= 0)	{ 
					return true;
			}
		}
		return false;
	}	
	return false;
}


	private static Connection getConnection() throws KmUserMstrDaoException {

		logger.info(
			"Entered getConnection for operation on table:KM_USER_MSTR");

		try {
			return DBConnection.getDBConnection();
		} catch (DAOException e) {

			logger.info("Exception Occured while obtaining connection.");

			throw new KmUserMstrDaoException(
				"Exception while trying to obtain a connection",
				e);
		}

	}
	

}
