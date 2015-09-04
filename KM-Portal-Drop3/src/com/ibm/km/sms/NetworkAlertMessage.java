package com.ibm.km.sms;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import com.ibm.km.actions.KmNetworkErrorAction;
import com.ibm.km.common.PropertyReader;
import com.ibm.km.dto.KmAlertMstr;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.dto.NetworkErrorLogDto;
import com.ibm.km.networkfault.IndexLocation;
import com.ibm.km.services.KmNetworkErrorLogService;
import com.ibm.km.services.KmUserMstrService;
import com.ibm.km.services.impl.KmNetworkErrorLogServiceImpl;
import com.ibm.km.services.impl.KmUserMstrServiceImpl;

 public class NetworkAlertMessage implements Runnable 
 {
	private static Logger logger = Logger.getLogger(NetworkAlertMessage.class.getName());

	String csdUsers="";
	String smsText="";
	String circleKey="";
		
	public NetworkAlertMessage()
	{
		super();
	}
	public NetworkAlertMessage(String csdUsers, String smsText,String circleKey)
	{
		this.csdUsers = csdUsers;
		this.smsText = smsText;
		this.circleKey = circleKey;
	}

	public void handleSMSRequest(NetworkErrorLogDto networkErrorLogDto)
	{
	  KmNetworkErrorLogService kmNetworkErrorLogService = new KmNetworkErrorLogServiceImpl();
      try
      {
			// Fetching CSD user for the circle
			csdUsers = kmNetworkErrorLogService.selectCSDUsers(networkErrorLogDto.getCircleID());

			if (!"".equals(csdUsers)) {

				// SMS text to be sent to CSD Users
				smsText = networkErrorLogDto.getProblemDesc()
						+ ". Areas Affected: "
						+ networkErrorLogDto.getAreaAffected() + ". TAT: "
						+ networkErrorLogDto.getResoTATHH() + "Hrs "
						+ networkErrorLogDto.getResoTATMM() + "Mins.";
				smsText = smsText.replace("&", "&amp;");

				logger.info("SMS to be sent : " + smsText);

				// Getting Circle Key to be appended into SMS
				 circleKey=kmNetworkErrorLogService.getCircle(networkErrorLogDto.getCircleID());


				//Sending SMSs to CSD users
				 NetworkAlertMessage smsSenderThread = new NetworkAlertMessage(csdUsers,smsText,circleKey);
				 new Thread(smsSenderThread).start();
			}
			else
			{
				logger.info("No CSD users found.");
			}
      }catch (Exception e) {
    	  e.printStackTrace();
    	  
	}
  }
	 
	// Sending SMS on separate thread.
		public void run()
		{
			SendSMSXML sendSMSXml = new SendSMSXML();
			String mobileNos[] = csdUsers.split(",");
			String cirKey=circleKey;
			//logger.info("Sending SMSs to : " + csdUsers);
			for(int ii=0;ii<mobileNos.length; ii++)
			{
				if(!"".equals(mobileNos[ii].trim()))
				{
				//	//System.out.println("Sending SMSs to CSD Mobile : "+mobileNos[ii]); 
					sendSMSXml.sendSms(mobileNos[ii],smsText,cirKey);
				}
			}
			//logger.info("SMS sent to : " + csdUsers);
		}
		
	 
 }