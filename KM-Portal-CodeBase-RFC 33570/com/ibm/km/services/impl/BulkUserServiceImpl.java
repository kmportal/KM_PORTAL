/*
 * Created on Nov 26, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.services.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.ibm.appsecure.exception.ValidationException;
import com.ibm.appsecure.service.GSDService;
import com.ibm.appsecure.util.Encryption;
import com.ibm.appsecure.util.IEncryption;

import com.ibm.km.common.PropertyReader;
import com.ibm.km.common.Utility;
import com.ibm.km.dao.BulkUserDao;
import com.ibm.km.dao.DBConnection;
import com.ibm.km.dao.KmUserMstrDao;
import com.ibm.km.dao.impl.BulkUserDaoImpl;
import com.ibm.km.dao.impl.KmUserMstrDaoImpl;
import com.ibm.km.dto.KmFileDto;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;
import com.ibm.km.exception.KmUserMstrDaoException;
import com.ibm.km.services.BulkUserService;
import com.ibm.km.services.KmUserMstrService;

/**
 * @author Anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class BulkUserServiceImpl implements BulkUserService {
	
	private static final Logger logger;
	static {
		//logger = Logger.getLogger(BulkUserServiceImpl.class);
	 logger = Logger.getLogger("DEFAULT_LOGGER");
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	StringBuffer loginIdsNotcreated = new StringBuffer("");
	String error="";
	
	String fileName = "";
	List<KmFileDto> fileDTO1 = null;
	File errorLog=null;
	File excelFile=null;
	FileWriter writer=null;
    PrintWriter out= null;	
    FileReader f=null;
	BufferedReader br=null;
	int usersCreated=0;
	
	int usersDeleted=0;
	

    public void bulkUpload() throws IOException {

		logger.info("inside bulkUpload::::::");

		try {
			BulkUserDao dao=new BulkUserDaoImpl();
			//BulkUserDao dao = new BulkUserDao();
			fileDTO1 = dao.getBulkUploadDetails();
			logger.info("fetching the files from database Uploaded File no:****"+fileDTO1.size());
			// System.out.println("11111111111111111INDISE DTO NULL"+fileDTO.getFileType());
			for(KmFileDto fileDTO:fileDTO1)
			{
				
			
			if (fileDTO == null) {

				logger.info("No Files present for processing");
				logger.info("22222222222222INDISE DTO NULL"
						+ fileDTO.getFileType());
				break;
				
			} else {
				logger.info("File Path::"
						+ fileDTO.getFilePath().substring(0,
								fileDTO.getFilePath().lastIndexOf("."))
						+ ".log");
			/**
				String filepath1=fileDTO.getFilePath().substring(0,
								fileDTO.getFilePath().lastIndexOf("."))+ ".log";
			fileName=filepath1.substring(fileDTO.getFilePath().lastIndexOf("/") + 1, fileDTO.getFilePath().length());
				//fileName = fileDTO.getFilePath().substring(
					//	fileDTO.getFilePath().lastIndexOf("/") + 1,
						//fileDTO.getFilePath().length());
			**/
			errorLog=new File(fileDTO.getFilePath().substring(0,fileDTO.getFilePath().lastIndexOf("."))+".log");
				//errorLog = new File(PropertyReader.getString("path.file")+fileName);
			logger.info("Error log file"+errorLog);	
			fileDTO.setErrorFile(errorLog);
				writer = new FileWriter(errorLog);
				out = new PrintWriter(writer);
				fileName=fileDTO.getFilePath().substring(fileDTO.getFilePath().lastIndexOf("/")+1,fileDTO.getFilePath().length());		
				excelFile = new File(fileDTO.getFilePath());
				
				logger.info("fileDTO.getFilePath():::::::::::"
						+ fileDTO.getFilePath());
				excelFile = new File(fileDTO.getFilePath());

				f = new FileReader(excelFile);
				br = new BufferedReader(f);
				logger.info("ABOUT TO ");

				if (fileDTO.getFileType().equals("D")) {

					logger.info("bulkDelete::::::::::::");
					bulkDelete(fileDTO);
					
				} else if (fileDTO.getFileType().equals("U")) {
					logger.info("bulkUpdate::::::::::::");
					bulkUpdate(fileDTO);
					
				} else if (fileDTO.getFileType().equals("C")) {
					logger.info("bulkInsert::::::::::::");
					bulkInsert(fileDTO);
					
				}

			}
		}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception occured while fetching bulk user file from the DB"
					+ e.getMessage(),e);
		}
	}

	
	
	/**
	public void bulkUpload()
		throws IOException, Exception {
	
		
			File errorLog=null;
			FileWriter writer=null;
		    PrintWriter out= null;	
		    List<KmFileDto> fileDTO1 = null;
		    int j = 0;
			int usersCreated=0;
			KmUserMstrService userService=new KmUserMstrServiceImpl();
			KmUserMstrDao userDao=new KmUserMstrDaoImpl();
			IEncryption enc_dec = new Encryption();
			//KmFileDto fileDTO=new KmFileDto();
			int usersUpdated=0;
			File excelFile=null;
			FileReader f=null;
			BufferedReader br=null;
			String fileName="";
			
			try
			{
				BulkUserDao dao=new BulkUserDaoImpl();
				fileDTO1 = dao.getBulkUploadDetails();
				
				logger.info("fileDTO1.length"+fileDTO1.size());
				for(KmFileDto fileDTO:fileDTO1)
				{
			try{
				
				//fileDTO=dao.getBulkUploadDetails();
				
				if(fileDTO==null){
						return ;
					}else{
						errorLog=new File(fileDTO.getFilePath().substring(0,fileDTO.getFilePath().lastIndexOf("."))+".log");
						
						fileDTO.setErrorFile(errorLog);
						writer= new FileWriter(errorLog);
						out = new PrintWriter(writer);
						logger.info("fileDTO.getFileType()33333333333"+fileDTO.getFileType());
						if(fileDTO.getFileType().equals("D")){
						bulkDelete(fileDTO);
						}
						if(fileDTO.getFileType().equals("U")){
							bulkUpdate(fileDTO);
						}
					fileName=fileDTO.getFilePath().substring(fileDTO.getFilePath().lastIndexOf("/")+1,fileDTO.getFilePath().length());		
					excelFile = new File(fileDTO.getFilePath());
					
					f = new FileReader(excelFile);
					br = new BufferedReader(f);
					}	
			}
			catch(Exception e){
				e.printStackTrace();
				logger.info("Exception occured while fetching bulk user file from the DB");
			}
		
		try {
	
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
			String fav_category_name = "";
			String element_id = "";
			String partner_name="";
			
			j=0;
			while ((line = br.readLine()) != null) {
				//logger.info("MAIN IFFF row stringggggggggggggg"+br.readLine());
				String[] columnValues = new String[24];
				// Updated against defect Id MASDB00105265
				j++;
				if(j==1)
					continue;
				//if(j==1002){
					//j--;
					//break;
				//}	
				try {
					
					String rowString = line;
					logger.info("MAIN IFFF row string"+rowString);
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
						             
					}
					// Changes made Against defect Id MASDB00105230 and MASDB00105213
				/**	comment made by vishwas its check non necessary data
				 * if(columnValues[1]==null||columnValues[2]==null||columnValues[4]==null||columnValues[5]==null||columnValues[6]==null||columnValues[7]==null||columnValues[1].equals("")||columnValues[2].equals("")||columnValues[4].equals("")||columnValues[5].equals("")||columnValues[6].equals("")||columnValues[7].equals("")){
						if(columnValues[1]==null||columnValues[1].equals("")){
							user_login_id="USER";
						}
						else{
							user_login_id=columnValues[1].trim();
						}
						logger.info(user_login_id + " NOT PROCESSED : All mandatory fields are not filled");
						out.println(user_login_id + " NOT PROCESSED : All mandatory fields are not filled");
						continue;
					}
**/
			/**		if (columnValues[1] == null || columnValues[2] == null
							|| columnValues[4] == null
							|| columnValues[6] == null
							|| columnValues[1].equals("")
							|| columnValues[2].equals("")
							|| columnValues[4].equals("")
							|| columnValues[6].equals("")

					) {

						logger.info("MAIN IFFF CONDI");
						// if (columnValues[1] == null ||
						// columnValues[1].equals("")) {
						logger.info(user_login_id
								+ " NOT PROCESSED : All mandatory fields are not filled");
						continue;
					}
					user_login_id = columnValues[1].toUpperCase().trim();
					user_fname = columnValues[2];
					user_mname = columnValues[3];
					user_lname = columnValues[4];
					user_mobile_number = columnValues[5];
					user_emailId = columnValues[6];
					fav_category_name = columnValues[8];
					partner_name=columnValues[7];
					if(partner_name==null){
						partner_name="";
					}
					if (fav_category_name == null)
						fav_category_name = "";
					
					element_id = fileDTO.getElementId();
					user_password = enc_dec.decrypt(PropertyReader.getAppValue("default.password"));
					//Element id of the logged in user from the session
					
					

					//GSD validation 
					GSDService gSDService = new GSDService();
					IEncryption encrypt = new Encryption();
					gSDService.validateCredentials(
						user_login_id,
						user_password);
					user_password = encrypt.generateDigest(user_password);
					
					KmUserMstr user=new KmUserMstr();
					user.setUserLoginId(user_login_id.trim());
					if(user_fname!=null)
						user.setUserFname(user_fname.trim());
					if(user_mname!=null)
						user.setUserMname(user_mname.trim());
					if(user_lname!=null)
						user.setUserLname(user_lname.trim());
					user.setUserMobileNumber(user_mobile_number);
					if(user_emailId!=null)
						user.setUserEmailid(user_emailId.trim());
					if(!emailValidation(user.getUserEmailid())){
						logger.info(user_login_id + "IS NOT CREATED :Validation Exception in Email Id ");
						out.println(user_login_id + "IS NOT CREATED :Validation Exception in Email Id ");
						continue;
					}
					if(user_password!=null)
						user.setUserPassword(user_password.trim());
					user.setElementId(element_id);
					user.setFavCategoryName(fav_category_name);
					user.setPartnerName(partner_name);	
					user.setCreatedBy(fileDTO.getUploadedBy());	
					KmUserMstr userData=userService.checkUserLoginId(user_login_id);
					logger.info(user_login_id + "  userData  File Name : "+userData);
					//Changes made against Defect ID MASDB00105205
					
					if (userData==null)
					{
						
						usersCreated+=userService.insertUserData(user);	
						
						logger.info(user_login_id + "  IS CREATED   File Name : "+fileName);
						out.println(user_login_id + "  IS CREATED ");
							
					}	
		/**			else if(userData.getElementId().equals(fileDTO.getElementId())){
						logger.info(userData.getElementId() + " File data and user data element id : "+fileDTO.getElementId());
						usersUpdated+=userDao.bulkUpdate(user);	
						
						logger.info(user_login_id + "  IS UPDATED   File Name : "+fileName);	
						out.println(user_login_id + "  IS UPDATED");						
					}
				**/
				/**	else if (userData.getElementId().equals(fileDTO.getElementId())) {

						System.out
								.println("userData.getElementId().DTO.getElementId::::::::::::::::::::");
						// usersUpdated += userDao.bulkUpdate(user);
						logger.info(user_login_id + "  already exist");
						out.println(user_login_id + "  already exist");

					}
					else{
						logger.info(userData.getElementId() + " File data and user data element id : "+fileDTO.getElementId());
						logger.info(user_login_id + "  NOT PROCESSED :  User belongs to different circle    File Name : "+fileName);	
						out.println(user_login_id + "  NOT PROCESSED : User belongs to different circle");
					}
					
			
					
					

				} catch (ValidationException validationExp) {
					
					status="P";
					logger.error(
						"Validation Exception in userloginid : "
							+ user_login_id
							+ "   : "
							+ validationExp);
					out.println(
							"Validation Exception in userloginid : "
								+ user_login_id
								+ "   : "
								+ validationExp);
					//validationExp.printStackTrace();
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
				} catch (KmException e) {
					e.printStackTrace();
					status="P";
					logger.error(
						"Exception in userloginid : "
							+ user_login_id
							+ "   : "
							+ e);
					out.println(
							"Exception in userloginid : "
								+ user_login_id
								+ "   : "
								+ e);
				//	e.printStackTrace();
					continue;
					//return false;
				  				
				} catch (Exception e) {
					e.printStackTrace();
					status="P";
					logger.error(
						"Exception in userloginid : "
							+ user_login_id
							+ "   : "
							+ e);
					logger.error(
							"Exception in userloginid : "
								+ user_login_id
								+ "   : "
								+ e);
					continue;
					//return false;
				}  
				finally{
					
					
					DBConnection.releaseResources(con,ps,rs);
					
				} 

			}

		} catch (Exception e) {
			
			e.printStackTrace();
			status="F";
			error=getStackTrace(e);
			error=e.getClass().toString()+" :  Error Code "+e.getLocalizedMessage();
			
			logger.info("Exception occured while uploading the page " + e);
		}
		
		finally{
			/*
			 * Stroing the file details in DB
			*/
		/**	if(fileDTO.getFileId()==null){
				
				return;
			}
	try{
		if(!fileDTO.getFileType().equals("D"))
		{
			out.close();
			writer.close();
			//BulkUserDao dao=new BulkUserDaoImpl();
			KmFileDto file=new KmFileDto();
			
			file.setUsersCreated(usersCreated+"");
			
			
			file.setUsersUpdated("0");
			
			logger.info("hiiiiiiiiiiiiiiiiiiiiiiiiiiii"+usersCreated+":"+usersUpdated);
			file.setUsersDeleted("0");
			//file.setFileType("C");
			file.setStatus("P");
			file.setTotalUsers((--j)+"");
			file.setErrorMessage(error);
			file.setFileId(fileDTO.getFileId());
			dao.updateFileStatus(file);
		}	
		}
			catch(Exception e){
				logger.error("Exception occured while updating the bulk file status");
			}
	
		} 
			}
				}
				catch(Exception e)
			{
				e.printStackTrace();
					System.out.println("exception in bulkUpload"+e);
			}
	//	dto.setStatus(status);
	//	return dto;
		

	}
	**/
    
    public void bulkInsert(KmFileDto DTO) {
    	int j = 0;
    	String status="P";
    	try {

			String line = "";
			String user_login_id = "";
			String user_fname = "";
			String user_mname = "";
			String user_lname = "";
			String user_mobile_number = "";
			String user_emailId = "";
			String user_password = "";
			String fav_category_name = "";
			String element_id = "";
			String partner_name = "";
			String pbx_id = "";
			String business_segment = "";
			String role = "";
			String process = "";
			String activity = "";
			String location = "";
			int fav_cat_Id = 0;
			
			BulkUserDao dao=new BulkUserDaoImpl();
			IEncryption enc_dec = new Encryption();
			KmUserMstrService userService=new KmUserMstrServiceImpl();
			
			while ((line = br.readLine()) != null) {
				String[] columnValues = new String[24];
				j++;
				if (j == 1)
					continue;
				//if (j == 1002) {
					//j--;
					//break;
				//}
				try {
					String rowString = line;
					logger.info("MAIN IFFF CONDI"+rowString);
					boolean wasDelimiter = true;
					String token = null;
					StringTokenizer st = new StringTokenizer(rowString, ",",
							true);
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
					}

					//added by vishwas
					System.out.println( "user id length "+columnValues[1].length());
					if(columnValues[1].length()<8)
					{
						out.println(user_login_id + "  NOT PROCESSED : user id less then 8 char");	
					}
					if (Utility.isEmptyOrContainsSpecial(columnValues[1]) || Utility.isEmptyOrContainsSpecial(columnValues[2])
							||Utility.isEmptyOrContainsSpecial(columnValues[4])
							||columnValues[6].equals("")
							||Utility.isSpecial(columnValues[0])
							||Utility.isSpecial(columnValues[3])
							||Utility.isSpecial(columnValues[5])
							||Utility.isSpecial(columnValues[7])
					) {
						

						logger.info("MAIN IFFF CONDI");
						// if (columnValues[1] == null ||
						// columnValues[1].equals("")) {
						logger.info(user_login_id
								+ " NOT PROCESSED : All mandatory fields are not filled or contains special characters");
						out.println(user_login_id + "  NOT PROCESSED : All mandatory fields are not filled or contains special characters");
						continue;
					}
					//end by vishwas
					
					 else {

						// edited by ashutosh due to login id
						// if(columnValues[1].length()<=Integer.parseInt(PropertyReader.getString("OLM_LENGTH"))){
						// logger.info(user_login_id +
						// " NOT PROCESSED : OLMID Less then 8 characters");
						// out.println(user_login_id +
						// " NOT PROCESSED : OLMID Less then 8 characters");
						// continue;
						// }
						user_login_id = columnValues[1].toUpperCase().trim();

						if (columnValues[8] == null
								|| columnValues[8].equals("")) {

							fav_cat_Id = 0;
							logger.info(user_login_id
									+ " PROCESS IS BLANK FOR THIS USER");
						} else {
							fav_cat_Id = CheckFavCatId(columnValues[8]);
							logger.info(fav_cat_Id
									+ " PROCESS fav_cat_Id ");
						}

					}

					user_fname = columnValues[2];
					user_mname = columnValues[3];
					user_lname = columnValues[4];
					user_mobile_number = columnValues[5];
					user_emailId = columnValues[6];
					partner_name = columnValues[7];
					pbx_id = columnValues[9];
					business_segment = columnValues[10];
					role = columnValues[11];
					activity = columnValues[12];
					location = columnValues[13];

					if (user_mname == null || user_mname == " ") {
						user_mname = "";
					}

					if (user_mobile_number == null || user_mobile_number == " ") {
						user_mobile_number = "";
					}
					if (partner_name == null || partner_name == " ") {
						partner_name = "";
					}

					if (pbx_id == null || pbx_id == " ") {
						pbx_id = "";
					}
					if (business_segment == null || business_segment == " ") {
						business_segment = "";
					}
					if (role == null || role == " ") {
						role = "";
					}
					if (activity == null || activity == " ") {
						activity = "";
					}
					if (location == null || location == " ") {
						location = "";
					}

					element_id = DTO.getElementId();
					//passward value "9JyRLx3FI57aGynRfRXiWw==" welcome1a
					user_password = enc_dec.decrypt(("9JyRLx3FI57aGynRfRXiWw=="));
					// Element id of the logged in user from the session

					// GSD validation
					// GSDService gSDService = new GSDService();
					IEncryption encrypt = new Encryption();
					// gSDService.validateCredentials(user_login_id,
					// user_password);
					user_password = encrypt.generateDigest(user_password);

					if(user_login_id.trim().length()<8)
					{
						logger.info(user_login_id
								+ "IS NOT CREATED :user_login_id not less than 8 ");
						out.println(user_login_id
								+ "IS NOT CREATED :user_login_id not less than 8 "+user_login_id);
						continue;
					}
					KmUserMstr user = new KmUserMstr();
					user.setUserLoginId(user_login_id.trim());
					if (user_fname != null)
						user.setUserFname(user_fname.trim());
					if (user_mname != null)
						user.setUserMname(user_mname.trim());
					if (user_lname != null)
						user.setUserLname(user_lname.trim());
					user.setUserMobileNumber(user_mobile_number);
					if (user_emailId != null) {
						user.setUserEmailid(user_emailId.trim());
						if (!emailValidation(user.getUserEmailid())) {
							logger.info(user_login_id
									+ "IS NOT CREATED :Validation Exception in Email Id ");
							out.println(user_login_id
									+ "IS NOT CREATED :Validation Exception in Email Id ");
							continue;
						}
					}
					if (user_password != null)
						user.setUserPassword(user_password.trim());
					user.setElementId(element_id);
					user.setFavCatId(fav_cat_Id);
					// user.setFavCategoryName(fav_category_name);
					user.setPartnerName(partner_name);
					user.setCreatedBy(DTO.getUploadedBy());
					user.setPbxId(pbx_id);
					user.setBusinessSegment(business_segment);
					user.setRole(role);
					user.setActivity(activity);
					user.setLocation(location);

					KmUserMstr userData = userService.checkUserLoginId(user_login_id);
					// Changes made against Defect ID MASDB00105205
					if(user_login_id.length()>7)
					{
					if (userData == null) {

						System.out
								.println("userData == null::::::::::::::::::::");
						usersCreated += userService.insertUserData(user);
						logger.info(user_login_id
								+ "  IS CREATED   File Name : " + fileName+":"+usersCreated);
						out.println(user_login_id
								+ "  IS CREATED   File Name : " + fileName);
					} else if (userData.getElementId().equals(
							DTO.getElementId())) {

						System.out
								.println("userData.getElementId().DTO.getElementId::::::::::::::::::::");
						// usersUpdated += userDao.bulkUpdate(user);
						logger.info(user_login_id + "  already exist");
						out.println(user_login_id + "  already exist");

					} else {
						logger.info(user_login_id
								+ "  NOT PROCESSED :  User belongs to different circle    File Name : "
								+ fileName);
						out.println(user_login_id
								+ "  NOT PROCESSED : User belongs to different circle  File Name : "
								+ fileName);
					}
					}
					else
					{
						out.println(
								 "  user id lenth shoud be greter than 8 "
								+ user_login_id);
					}

				} catch (Exception e) {

					e.printStackTrace();
					status = "F";
					logger.error("Exception in userloginid : " + user_login_id
							+ "   : " + e);
					continue;
					// return false;
				}

			}

		} catch (Exception e) {

			e.printStackTrace();
			status = "F";
			// error = getStackTrace(e);
			error = e.getClass().toString() + " :  Error Code "
					+ e.getLocalizedMessage();

			logger.info("Exception occured while uploading the page " + e);
			return;
		} finally {

			logger.info("Updating the file master for status of processing");

			if (DTO.getFileId() == null) {

				return;
			}
			try {

				out.close();
				writer.close();
				BulkUserDao dao=new BulkUserDaoImpl();
				KmFileDto file = new KmFileDto();
				file.setUsersCreated(usersCreated + "");
				file.setUsersUpdated("0");
				logger.info(" usersUpdated data"+":"+"usersCreated"+usersCreated);
				file.setUsersDeleted("0");
				file.setStatus(status);
				file.setTotalUsers((--j) + "");
				file.setErrorMessage(error);
				file.setFileId(DTO.getFileId());
				dao.updateFileStatus(file);
				logger.info("All Insert Files got executed!!");
			} catch (Exception e) {
				logger.error("Exception occured while updating the bulk file status");
			}

		}
	}

    
    
		public String getStackTrace(Exception ex) {
				java.io.StringWriter out = new java.io.StringWriter();
				ex.printStackTrace(new java.io.PrintWriter(out));
				String stackTrace = out.toString();
 
				return stackTrace;
			}
			public boolean isEmailAddress(String objInput)
			{
				
				
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

@SuppressWarnings("finally")
public KmFileDto bulkDelete(KmFileDto DTO)
			throws IOException, Exception{
				
	ResourceBundle bundle =
							ResourceBundle.getBundle("ApplicationResources");
	KmUserMstrService userService=new KmUserMstrServiceImpl(); 						
	StringBuffer userNotDeleted=new StringBuffer("");
	int usersDeleted=0;
	String status="S";
	String fileId="";
	FileWriter writer=null;
	PrintWriter out= null;	
	KmFileDto dto=new KmFileDto();
	int j = 0;						
	try{
			
		
		writer= new FileWriter(DTO.getErrorFile());
		out = new PrintWriter(writer);
		File excelFile = new File(DTO.getFilePath());
		FileReader f = new FileReader(excelFile);
		BufferedReader br = new BufferedReader(f);
		String fileName=DTO.getFilePath().substring(DTO.getFilePath().lastIndexOf("/"),DTO.getFilePath().length());
		String line="";
		String error="";
		String userId="";
		String userId2="";
		boolean incorrect=false;
		while ((line = br.readLine()) != null) {
			try{
				j++;
				if(j==1)
					continue;
				
				
				//if(j==1002){
					//j--;
					//break;
			//	}
				
				try{
					//Updated against defect id : MASDB05270
					userId=line.toUpperCase().trim();
					userId2=userId.substring(0, userId.indexOf(","));
					logger.info("user Id----------------------"+userId2);
					/**
					line=userId.substring(0,userId.indexOf(","));
					if(line!=null ){
						if(!line.equals("")){
							
							if(incorrect==false){
							logger.error("Incorrect format");
							
							out.println("Incorrect format");
							
							}
							incorrect =true;
							continue;
						}
						
					}
					**/
						
					
				}
				catch(Exception e){
					
				}
				
				KmUserMstr userData=userService.checkUserLoginId(userId2);
				if (userData==null)
				{
				
					logger.error("USER ID :" + userId + " DOES NOT EXISTS ");
					
					out.println("USER ID :" + userId + " DOES NOT EXISTS");
					
					continue;
				}else if(userData.getElementId().equals(DTO.getElementId())){
					usersDeleted+=userService.deleteUserService(userId2);
					if(usersDeleted==0)
					{
						logger.info(userId2 + "  Already DELETED    File Name : "+fileName+":"+usersDeleted);	
						out.println(userId2 + "  ALREADY DELETED ");	
					}
					else
					{
					logger.info(userId2 + "  IS DELETED    File Name : "+fileName+":"+usersDeleted);	
					out.println(userId2 + "  IS DELETED ");	
					}
					}
				else{
					logger.info(userId2 + "  NOT PROCESSED : User belongs to different circle   File Name : "+fileName);	
					out.println(userId2 + "  NOT PROCESSED : User belongs to different circle");	
				}
			}catch(Exception e){
				userNotDeleted.append(userId).append(",");		
				status="P";
				e.printStackTrace();
				continue;
			}
			finally{
				continue;
				
			}
			
		}	
		
	}catch(Exception e){
		error=getStackTrace(e);
		status="F";
		
	}
	finally{
		
		try{
			out.close();
			writer.close();
			BulkUserDao dao=new BulkUserDaoImpl();
			KmFileDto file=new KmFileDto();
			file.setUsersCreated("0");
			file.setUsersUpdated("0");
			logger.info("  IS DELETED    File Name1d : "+usersDeleted);	
			file.setUsersDeleted(usersDeleted+"");
			file.setFileType("D");
			file.setStatus("P");
			file.setTotalUsers((--j)+"");
			file.setErrorMessage(error);
			logger.info("  IS DELETED    File Named : "+usersDeleted);
			file.setFileId(DTO.getFileId());
			logger.info("  IS DELETED    File Name f: "+usersDeleted);
			dao.updateFileStatus(file);

		} 
		catch(Exception e){
			e.printStackTrace();
		}
	}
	dto.setStatus(status);
	
	return dto;
	
	
}


	private static Connection getConnection() throws KmUserMstrDaoException {

		logger.info(
			"Entered getConnection for operation on table:KM_USER_MSTR");

		try {
			return DBConnection.getDBConnection();
		} catch (DAOException e) {
			e.printStackTrace();
			logger.info("Exception Occured while obtaining connection.");

			throw new KmUserMstrDaoException(
				"Exception while trying to obtain a connection",
				e);
		}

	}

	/* (non-Javadoc)
	 * @see com.ibm.km.services.BulkUserService#getBulkUploadDetails(java.lang.String)
	 */
	public ArrayList<KmFileDto> getBulkUploadDetails() throws Exception {
		BulkUserDao dao=new BulkUserDaoImpl();
		return dao.getBulkUploadDetails();
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.services.BulkUserService#uploadFile(com.ibm.km.dto.KmFileDto)
	 */
	public int uploadFile(KmFileDto fileDto) throws KmException {
		BulkUserDao dao=new BulkUserDaoImpl();
		return dao.bulkUserUploadFile(fileDto);
		
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.services.BulkUserService#getBulkDeleteDetails(java.lang.String)
	 */
	public ArrayList getBulkDeleteDetails(String string) throws KmException {
		BulkUserDao dao=new BulkUserDaoImpl();
		return dao.getBulkDeleteDetails(string);
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.services.BulkUserService#updateFileStatus(int)
	 */
	public void updateFileStatus(KmFileDto dto) throws KmException {
		BulkUserDao dao=new BulkUserDaoImpl();
		dao.updateFileStatus(dto);
		
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.services.BulkUserService#bulkDelete(java.lang.String)
	 */
	public KmFileDto bulkDelete(String filePath) throws IOException, KmException {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList getBulkUserFiles(String circleId,String date) throws KmException {
		BulkUserDao dao= new BulkUserDaoImpl();
		return dao.getBulkUserFiles(circleId,date);
	}
	public boolean emailValidation(String emailId) 
	   {
	      //Input the string for validation
	      String email = emailId;

	      //Set the email pattern string
	      Pattern p = Pattern.compile(".+@.+\\.[a-z]+");

	      //Match the given string with the pattern
	      Matcher m = p.matcher(email);

	      //check whether match is found 
	      boolean matchFound = m.matches();

	      if (matchFound)
	       return true;
	      else
	        return false;
	   }
	
	public void bulkUpdate(KmFileDto DTO) {
		
		
		String status="P";
		int usersUpdated=0;
		FileWriter writer=null;
		PrintWriter out= null;	
		
		String line = "";
		String user_login_id = "";
		String user_fname = "";
		String user_mname = "";
		String user_lname = "";
		String user_mobile_number = "";
		String user_emailId = "";
		
		
		String partner_name = "";
		String pbx_id = "";
		String business_segment = "";
		String role = "";
		
		String activity = "";
		String location = "";
		int fav_cat_Id = 0;
		int j = 0;
		try {
			KmUserMstrService userService=new KmUserMstrServiceImpl(); 						
			KmUserMstrDao userDao=new KmUserMstrDaoImpl();
			
			logger.info("MAIN log file for updatation"+DTO.getErrorFile());
			writer= new FileWriter(DTO.getErrorFile());
			out = new PrintWriter(writer);
			File excelFile = new File(DTO.getFilePath());
			FileReader f = new FileReader(excelFile);
			BufferedReader br = new BufferedReader(f);
			String fileName=DTO.getFilePath().substring(DTO.getFilePath().lastIndexOf("/"),DTO.getFilePath().length());
			//String line="";
			
			
			
				while ((line = br.readLine()) != null) {
				String[] columnValues = new String[24];
				j++;
				if (j == 1)
					continue;
				//if (j == 1002) {
					//j--;
					//break;
				//}
				try {
					String rowString = line;

					boolean wasDelimiter = true;
					String token = null;
					StringTokenizer st = new StringTokenizer(rowString, ",",
							true);
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
					}

					if (Utility.isEmptyOrContainsSpecial(columnValues[1]) || Utility.isEmptyOrContainsSpecial(columnValues[2])
							||Utility.isEmptyOrContainsSpecial(columnValues[4])
							||columnValues[6].equals("")
							||Utility.isSpecial(columnValues[0])
							||Utility.isSpecial(columnValues[3])
							||Utility.isSpecial(columnValues[5])
							||Utility.isSpecial(columnValues[7])
					) {
						

						logger.info("MAIN IFFF CONDI");
						// if (columnValues[1] == null ||
						// columnValues[1].equals("")) {
						logger.info(user_login_id
								+ " NOT PROCESSED : All mandatory fields are not filled or contains special characters");
						out.println(user_login_id + "  NOT PROCESSED : All mandatory fields are not filled or contains special characters");
					} else {

						// edited by ashutosh due to login id
						// if(columnValues[1].length()<=Integer.parseInt(PropertyReader.getString("OLM_LENGTH"))){
						// logger.info(user_login_id +
						// " NOT PROCESSED : OLMID Less then 8 characters");
						// out.println(user_login_id +
						// " NOT PROCESSED : OLMID Less then 8 characters");
						// continue;
						// }
						user_login_id = columnValues[1].toUpperCase().trim();

						if (columnValues[8] == null
								|| columnValues[8].equals("")) {
							
							logger.info("FAV IFFFFFFFFFF"+columnValues[8]);

							fav_cat_Id = 0;
							logger.info(user_login_id
									+ " PROCESS IS BLANK FOR THIS USER");
						} else {
							
							logger.info("FAV ELSEEEEEEEEE"+columnValues[8]);
							fav_cat_Id = CheckFavCatId(columnValues[8]);
							//fav_category_name=columnValues[8];
						}

					}					
					
					user_fname = columnValues[2];
					user_mname = columnValues[3];
					user_lname = columnValues[4];
					user_mobile_number = columnValues[5];
					user_emailId = columnValues[6];
					partner_name = columnValues[7];
					pbx_id = columnValues[9];
					business_segment = columnValues[10];
					role = columnValues[11];
					activity = columnValues[12];
					location = columnValues[13];

					if (user_mname == null || user_mname == " ") {
						user_mname = "";
					}

					if (user_mobile_number == null || user_mobile_number == " ") {
						user_mobile_number = "";
					}
					if (partner_name == null || partner_name == " ") {
						partner_name = "";
					}

					if (pbx_id == null || pbx_id == " ") {
						pbx_id = "";
					}
					if (business_segment == null || business_segment == " ") {
						business_segment = "";
					}
					if (role == null || role == " ") {
						role = "";
					}
					if (activity == null || activity == " ") {
						activity = "";
					}
					if (location == null || location == " ") {
						location = "";
					}

					
					
					
					
					
					
					
//
//					role = columnValues[3];
//					business_segment = columnValues[4];
//					// fav_category_name = columnValues[5];
//					activity = columnValues[6];
//					partner_name = columnValues[7];
//					location = columnValues[8];
//					user_mobile_number = columnValues[9];
//					user_emailId = columnValues[10];
//
//					if (role == null || role == " ") {
//						role = "";
//					}
//					if (business_segment == null || business_segment == " ") {
//						business_segment = "";
//					}
//
//					// if (fav_category_name == null || fav_category_name ==
//					// " "){
//					// fav_category_name = "";
//					// }
//					if (activity == null || activity == " ") {
//						activity = "";
//					}
//					if (partner_name == null || partner_name == " ") {
//						partner_name = "";
//					}
//					if (location == null || location == " ") {
//						location = "";
//					}
//					if (user_mobile_number == null || user_mobile_number == " ") {
//						user_mobile_number = "";
//					}

					KmUserMstr user = new KmUserMstr();
					user.setUserLoginId(user_login_id.trim());
					user.setUserMobileNumber(user_mobile_number);
					if (user_emailId != null) {
						user.setUserEmailid(user_emailId.trim());
						if (!emailValidation(user.getUserEmailid())) {
							logger.info(user_login_id
									+ "IS NOT CREATED :Validation Exception in Email Id ");
							out.println(user_login_id
									+ "IS NOT CREATED :Validation Exception in Email Id ");
							continue;
						}
					}
					user.setUserFname(user_fname);
					user.setUserMname(user_mname);
					user.setUserLname(user_lname);					
					user.setPartnerName(partner_name);
					user.setFavCatId(fav_cat_Id);
					//user.setFavCategoryName(fav_category_name);
					user.setUpdatedBy(DTO.getUploadedBy());//
					user.setPbxId(pbx_id);
					user.setBusinessSegment(business_segment);
					user.setRole(role);
					user.setActivity(activity);
					user.setLocation(location);
					logger.info("user_login_id::::::::::::::::"
							+ user_login_id);
					KmUserMstr userData = userService
							.checkUserLoginId(user_login_id);
					if (userData == null) {
						logger.info("userData == null::::::::::::");
						logger.info(user_login_id
								+ "  is not there for updation");
						out.println(user_login_id
								+ "  is not there for updation");
					} 
					
					
					else{
						
						// new code start
						if (DTO.getUploadedBy().trim() != null) {
                          logger.info("111111111111111111fileDTO.getUploadedBy().trim()"+DTO.getUploadedBy().trim());
							KmFileDto fileDTO1; // = new KmFileDto();
							fileDTO1 = checkLevelId(DTO.getUploadedBy().trim());

							if (fileDTO1.getElement_level_id()==1) {
								logger.info("222222222222222222222");
								// simply update the records WITH THE ELEMENT ID
								// fileDTO.getElementId().trim()
								
								user.setElementId(DTO.getElementId().trim());
								usersUpdated += userDao.bulkUpdate(user);
								logger.info(user_login_id
										+ "  IS UPDATED   File Name : usersUpdated" + fileName+":"+usersUpdated);
								
								
								out.println(user_login_id
										+ "  IS UPDATED   File Name : " + fileName);
									
								
							} else if (fileDTO1.getElement_level_id()==2) {
								logger.info("3333333333333333333");

								KmFileDto fileDTO2; // = new KmFileDto();

								fileDTO2 = checkParentId(userData
										.getElementId().trim());

								if (fileDTO2.getParent_id()==fileDTO1.getElementId1()) {
									// within same lob
									// fileDTO.getElementId().trim() have to be
									// changed as circle
									
									logger.info("444444444444444444444");
									user.setElementId(DTO.getElementId().trim());
									usersUpdated += userDao.bulkUpdate(user);
									logger.info(user_login_id
											+ "  IS UPDATED   File Name : " + fileName);
									out.println(user_login_id
											+ "  IS UPDATED   File Name : " + fileName);
									
								} else {
									
									
									logger.info("5555555555555555555555555");
									logger.info(user_login_id
											+ "  NOT PROCESSED :  User belongs to different LOB    File Name : "
											+ fileName);
									out.println(user_login_id
											+ "  NOT PROCESSED : User belongs to different LOB");

									// of other lob
								}
							} else if (fileDTO1.getElement_level_id()==3) {
								
								logger.info("66666666666666666666666");

								logger.info("userData.getElementId():::::::::"+userData.getElementId());
								logger.info("fileDTO1.getElementId1():::::::::"+fileDTO1.getElementId1());
								//if (userData.getElementId().trim().equals(fileDTO1.getElementId1())) {
									if(Integer.parseInt(userData.getElementId())==fileDTO1.getElementId1()){
									// update data without any circle changes
									logger.info("777777777777777777777777777777");

									user.setElementId(DTO.getElementId().trim());
									usersUpdated += userDao.bulkUpdate(user);
									logger.info(user_login_id
											+ "  IS UPDATED   File Name : " + fileName);
									out.println(user_login_id
											+ "  IS UPDATED   File Name : " + fileName);
									
								}
									
									else {
										
										
										logger.info("888888888888888888888888888888888");
										logger.info(user_login_id
												+ "  NOT PROCESSED :  User belongs to different CIRCLE    File Name : "
												+ fileName);
										out.println(user_login_id
												+ "  NOT PROCESSED : User belongs to different CIRCLE");

										// of other lob
									}	
											}

							else {
								
								logger.info("99999999999999999999999999999");
								logger.info(user_login_id
										+ "  NOT PROCESSED : User belongs to SOME OTHER ADMIN    File Name : "
										+ fileName);
								out.println(user_login_id
										+ "  NOT PROCESSED User belongs to SOME OTHER ADMIN   :");
							}
						}
						
						
					}

				} catch (Exception e) {

					e.printStackTrace();
					status = "F";
					logger.error("Exception in userloginid : " + user_login_id
							+ "   : " + e);
					continue;
					// return false;
				}

			}

		} catch (Exception e) {

			e.printStackTrace();
			status = "F";
			// error = getStackTrace(e);
			error = e.getClass().toString() + " :  Error Code "
					+ e.getLocalizedMessage();
			logger.info("Exception occured while uploading the page " + e);
			return;
		} finally {

			logger.info("Updating the file master for status of processing");

			if (DTO.getFileId() == null) {

				return;
			}
			try {

				out.close();
				writer.close();
				logger.info("hiiiiiiiiiiiiiiiiiiiiiiii");
				//out.close();
				//writer.close();
				BulkUserDao dao=new BulkUserDaoImpl();
			//	logger.info("***************111111111111*************");
				KmFileDto file1 = new KmFileDto();
				//logger.info("***************222222222*************");
				file1.setUsersCreated("0");
				//logger.info("***************333333333333*************");
				file1.setUsersUpdated(usersUpdated + "");
				logger.info("***************444444444444*************"+usersUpdated);
				file1.setUsersDeleted("0");
				//logger.info("***************55555555555555*************");
				// file.setFileType("U");
				file1.setStatus(status);
				//logger.info("***************666666666666*************");
				file1.setTotalUsers((--j) + "");
				//logger.info("***************7777777777777*************");
				file1.setErrorMessage(error);
				//logger.info("***************8888888888888*************");
				file1.setFileId(DTO.getFileId());
				logger.info("***************99999999999999*************"+file1);
				dao.updateFileStatus(file1);
				logger.info("***************10 10 10 10 *************");
				logger.info("All Update Files got executed!!");

			} catch (Exception e) {
				logger.error("Exception occured while updating the bulk file status");
				e.printStackTrace();
			}

		}
	}
	public KmFileDto checkLevelId(String user_id)throws Exception {		
		//SELECT b.ELEMENT_ID,b.ELEMENT_LEVEL_ID FROM KM_USER_MSTR a, KM_ELEMENT_MSTR b WHERE a.ELEMENT_ID= b.ELEMENT_ID
		 //and a.USER_ID=10027
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;		
		KmFileDto dto = null;
		try {
			con = DBConnection.getDBConnection();
			//con = DatabaseConnection.getDb2DbConnection();
			String query="";
			query="SELECT b.ELEMENT_ID,b.ELEMENT_LEVEL_ID FROM KM_USER_MSTR a, KM_ELEMENT_MSTR b WHERE a.ELEMENT_ID= b.ELEMENT_ID and a.USER_ID=? with ur";
			ps =con.prepareStatement(query);
			ps.setInt(1, Integer.valueOf(user_id.trim()));
			//System.out.println(user_id.trim()+"testingggg"+query);
			rs = ps.executeQuery();
			//System.out.println("helloooooooooooooooo");
			if (rs.next()) {				
			//System.out.println("hello"+rs.getInt("ELEMENT_ID"));
				dto = new KmFileDto();
				dto.setElementId1(rs.getInt("ELEMENT_ID"));	
				dto.setElement_level_id(rs.getInt("ELEMENT_LEVEL_ID"));
			}		
			return dto;

		} catch (Exception e) {

			e.printStackTrace();
				logger.error("Exception occured while find." + "Exception Message: " + e.getMessage());
			throw new Exception("Exception occured while  checking LevelId :  "+ e.getMessage(),e);
		} finally {
			try {
				DBConnection.releaseResources(con, ps, rs);
			} catch (Exception e) {
				logger.error("DAOException occured while while  checking LevelId."+ "Exception Message: "+ e.getMessage());
				throw new Exception(e.getMessage(), e);
			}
		}

}
	public KmFileDto checkParentId(String element_id)
	throws Exception {
		//SELECT PARENT_ID  FROM KM_ELEMENT_MSTR WHERE ELEMENT_ID=12049
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		KmFileDto dto = null;
		try {
			
			con = DBConnection.getDBConnection();
			//con = DatabaseConnection.getDb2DbConnection();
			ps =con.prepareStatement("SELECT PARENT_ID  FROM KM_ELEMENT_MSTR WHERE ELEMENT_ID=? with ur");
			ps.setString(1, element_id.trim());
			rs = ps.executeQuery();
			if (rs.next()) {
				
				dto = new KmFileDto();
				dto.setParent_id(rs.getInt("PARENT_ID"));	
			}			
			return dto;

		} catch (Exception e) {

			e.printStackTrace();
				logger.error("Exception occured while find." + "Exception Message: " + e.getMessage());
			throw new Exception("Exception occured while checking Parent Id  in DB :  "+ e.getMessage(),e);
		} finally {
			try {
				DBConnection.releaseResources(con, ps, rs);
			} catch (Exception e) {
				logger.error("DAOException occured while while checking Parent Id  in DB."+ "Exception Message: "+ e.getMessage());
				throw new Exception(e.getMessage(), e);
			}
		}

	
	}

	public int CheckFavCatId(String process_id)
	throws Exception {
logger.info("Entered CheckFavCatId for table:KM_ELEMENT_MSTR");

Connection con = null;
PreparedStatement pst = null;
ResultSet rs = null;
int favCatId=0;
try {
	StringBuffer query = new StringBuffer("SELECT ELEMENT_ID FROM KM_ELEMENT_MSTR U WHERE U.ELEMENT_NAME = ? AND ELEMENT_LEVEL_ID=4 ");
	con = DBConnection.getDBConnection();
	//con = DatabaseConnection.getDb2DbConnection();
	pst = con.prepareStatement(query.append(" with ur ").toString());
	logger.info(process_id.trim()+"Entered CheckFavCatId for table:KM_ELEMENT_MSTR"+query+":"+process_id.trim());
	pst.setString(1,process_id.trim());
	rs = pst.executeQuery();
	if (rs.next()) {
		
		//System.out.println("INSIDE RS:::CheckFavCatId:::");
		
		favCatId =rs.getInt("ELEMENT_ID");
		//System.out.println("favCatId inside new dao method"+favCatId);
	
	}
} catch (SQLException e) {
	logger.error(e);
	logger.error("SQL Exception occured while CheckFavCatId."
			+ "Exception Message: " + e.getMessage());
	throw new Exception("SQLException: " + e.getMessage(), e);
}  catch (Exception e) {

	logger.error("Exception occured while CheckFavCatId."
			+ "Exception Message: " + e.getMessage());
	throw new Exception("Exception: " + e.getMessage(), e);
} finally {
	try {
		DBConnection.releaseResources(con, pst, rs);
	} catch (Exception e) {
		logger.error("DAO Exception occured while CheckFavCatId."
				+ "Exception Message: " + e.getMessage());
		throw new Exception("DAO Exception: " + e.getMessage(), e);
	}
}
return favCatId;
}

}