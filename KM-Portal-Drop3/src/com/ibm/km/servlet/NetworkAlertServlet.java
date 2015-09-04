package com.ibm.km.servlet;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.ibm.km.common.PropertyReader;
import com.ibm.km.dao.impl.KmPopulateUserDaoImpl;
import com.ibm.km.dto.KmAlertMstr;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.dto.NetworkErrorLogDto;
import com.ibm.km.networkfault.IndexLocation;
import com.ibm.km.services.KmNetworkErrorLogService;
import com.ibm.km.services.KmUserMstrService;
import com.ibm.km.services.impl.KmNetworkErrorLogServiceImpl;
import com.ibm.km.services.impl.KmUserMstrServiceImpl;
import com.ibm.km.sms.NetworkAlertMessage;

 public class NetworkAlertServlet extends HttpServlet implements Servlet {
	private static final long serialVersionUID = 1L;

	private static final Logger logger;

	static {
		logger = Logger.getLogger(NetworkAlertServlet.class);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		try {
			String responseMsg = handleRequest(request);
			logger.info(responseMsg);
			if(!"INVALID_USER".equals(responseMsg))
			{
					out.print(responseMsg);
			}
		}catch(Exception e){
			logger.info("Exception occured while creating alert : "+e);
			e.printStackTrace();
			out.print("Some problems occured while creating the alert message. Please Try Again.");
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		try {
			String responseMsg = handleRequest(request);
			logger.info(responseMsg);
			if(!"INVALID_USER".equals(responseMsg))
			{
					out.print(responseMsg);
			}
		}catch(Exception e){
			
			logger.info("Exception occured in doPost while creating alert : "+e);
			
			System.out.println(e);
			e.printStackTrace();
			out.print("Some problems occured while creating the alert message. Please Try Again.");
		}
	}
	
	private  String handleRequest(HttpServletRequest request) {

		String responseMsg = "";
		final String NAMSGNOTAUTH = "Unauthorised Access";
		final String NAMSGNOTVALID = "Invalid Message, pls send msg as\n NT Alert_description:Area_Affected:HH:MM";
		try 
		{
			System.out.println("inside handleRequest method of NetworkAlertServlet... ");
			
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();				
			Document doc = db.parse(request.getInputStream());                
			doc.getDocumentElement().normalize();
			
			String mobileNumber = doc.getElementsByTagName("number").item(0).getTextContent();
			String message = doc.getElementsByTagName("ud").item(0).getTextContent();
			  
			
			/*String mobileNumber = request.getParameter("mobileNumber");
			String message = request.getParameter("message");*/
			
			
			
			String userId =  request.getParameter("userId");
			String password =request.getParameter("password");
						
			String mbUserID = PropertyReader.getAppValue("NA.USERID");
			String mbPassword = PropertyReader.getAppValue("NA.PASSWORD");
			
			logger.info("Network Alert received from Mobile Number : "+  mobileNumber);
			logger.info("Alert Message : "+  message);
			logger.debug("User ID : "+  userId);
//			logger.debug("Password : "+  password);
			
			KmAlertMstr alertDto= null;
			String keyword = PropertyReader.getAppValue("NA.KEYWORD");
			int messageLengthMax = Integer.parseInt(PropertyReader.getAppValue("message.length"));
			String separator = PropertyReader.getAppValue("message.separator");
			
			if ( !mbUserID.equals(userId) || !mbPassword.equals(password) )
			{
				return NAMSGNOTAUTH;
			}
			if (null == mobileNumber || "".equals(mobileNumber))
			{
				return "Mobile Number not obtained";
			}
			if(message==null || "".equals(message) || message.length() <= keyword.length())
			{
				return NAMSGNOTVALID;
			}
			
			message = message.substring(keyword.length()+1).trim();
			if("".equals(message)){
				return NAMSGNOTVALID;
			}
			if(message.length()> messageLengthMax){
				return "Message Length Exceeds Maximum Character Limit["+messageLengthMax+"]";
		    }
									
			mobileNumber=mobileNumber.trim();
			if(mobileNumber.startsWith("91") && mobileNumber.length() > 10){
				mobileNumber=mobileNumber.substring(2, mobileNumber.length());
			}else if(mobileNumber.startsWith("0")){
				mobileNumber=mobileNumber.substring(1, mobileNumber.length());
			}
			
			logger.debug("mobileNumber after trim: "+  mobileNumber);

			KmUserMstrService userService = new KmUserMstrServiceImpl();
			KmUserMstr userDto=userService.userIdFromMobile(mobileNumber,"8");
			if(userDto!=null)
			{
				alertDto = new KmAlertMstr();
				alertDto.setCreatedBy(userDto.getUserId());
				String messageTokens[] = message.split(separator);
				
				if(messageTokens.length == 4)
				{
						String messageDesc = messageTokens[0].trim();
						String affectedArea = messageTokens[1].trim();
						String tatHH = messageTokens[2].trim();
						String tatMM = messageTokens[3].trim();
						//String tatHHMM[] = messageTokens[2].trim().split(separator);
						try{
						 Integer.parseInt(tatHH);
						 Integer.parseInt(tatMM);
						}catch (Exception e) {
							return "TAT Hours and Minutes should be in digits";
						}
						
						if("".equals(tatHH))
						{
						   tatHH = "0";
						}
						if(tatHH.length()==1)
						{
						   tatHH = "0"+tatHH;
						}
						if("".equals(tatMM))
						{
							tatMM = "0";
						}						
						if(tatMM.length()==1)
						{
							tatMM = "0"+tatMM;
						}

						if(!"".equals(messageDesc) && !"".equals(affectedArea) && !"".equals(tatHH) && !"".equals(tatMM) )
						{
							KmNetworkErrorLogService kmNetworkErrorLogService = new KmNetworkErrorLogServiceImpl();
							NetworkErrorLogDto networkErrorLogDto = new NetworkErrorLogDto();

							networkErrorLogDto.setProblemDesc(messageDesc);
							networkErrorLogDto.setAreaAffected(affectedArea);
							networkErrorLogDto.setResoTATHH(tatHH);
							networkErrorLogDto.setResoTATMM(tatMM);
							networkErrorLogDto.setCircleID(Integer.parseInt(userDto.getElementId()));
							networkErrorLogDto.setCircleName(userDto.getCircleName());
							networkErrorLogDto.setStatus("I");

							////System.out.println("Circle ID : "+networkErrorLogDto.getCircleID());

							// Insert Network Alert Message into DB
							int logId = kmNetworkErrorLogService.insertNetworkErrorLogService(networkErrorLogDto);

							NetworkAlertMessage nm = new NetworkAlertMessage();								
							nm.handleSMSRequest(networkErrorLogDto);
							
							// Insert Network Alert Message into DB

							networkErrorLogDto.setProblemId(logId+"");

							if (logId > 0) {
								logger.info("Network Alert Message inserted.");
	
								// writing index into index file.
								try {
									new IndexLocation().initNetworkFaultIndex(networkErrorLogDto, false);
								} catch (Exception e) {
									e.printStackTrace();
								}
							
							}
							
						}
						else
						{
							return NAMSGNOTVALID;
						}
						
						return "Hi "+userDto.getUserFname()+", Network alert Message sent";
					}
					else
					{
						return NAMSGNOTVALID;
					}
				}
				else{
					return "INVALID_USER";
				}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(e);

			logger.info("Exception occured while creating alert : "+e);
			responseMsg = "Some problems occured while creating the alert message. Please Try Again.";
		}
    return responseMsg;
	}
}