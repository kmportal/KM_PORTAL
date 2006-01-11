package com.ibm.km.scheduler;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import com.ibm.km.common.PropertyReader;
import com.ibm.km.dto.ApproveFileDto;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.exception.KmException;
import com.ibm.km.services.ApproveFileService;
import com.ibm.km.services.BulkUserService;
import com.ibm.km.services.KmElementMstrService;
import com.ibm.km.services.KmUserMstrService;
import com.ibm.km.services.impl.ApproveFileServiceImpl;
import com.ibm.km.services.impl.BulkUserServiceImpl;
import com.ibm.km.services.impl.KmElementMstrServiceImpl;
import com.ibm.km.services.impl.KmUserMstrServiceImpl;


/**
 *
 * @author Sumit Sethi
 *
 */
public class SchedulerTrigger {
	Logger logger = Logger.getLogger("DEFAULT_LOGGER");
	//Logger logger = Logger.getLogger(SchedulerTrigger.class);
	/**
	 * method which will pick up all the events which are in pending status when scheduler runs
	 * 
	 * @throws KmException
	 * @throws ParseException
	 */
	
	//added by vishwas
/**
	public static void main(String args[]){
		
	SchedulerTrigger utility=new SchedulerTrigger();
		try {
			utility.triggergf();
		} catch (Exception e) {
			
			e.printStackTrace();
		} 
		
		
	}**/
	//end by vishwas
	
	public void trigger() throws  ParseException {
		
		
	
		logger.info("--------Enter in trigger method-------");
		
		List checkApproveFiles = new ArrayList();
		ApproveFileService service = new ApproveFileServiceImpl();
		KmElementMstrService elementService = new KmElementMstrServiceImpl();
		KmUserMstrService userService = new KmUserMstrServiceImpl();
		String elementId;
		String circleId;
		String circleName="";
		String approver="";
		String publishedBy="";
		BulkUserService bulkUserService=new BulkUserServiceImpl();
		logger.info("*************************aaaaaaaaaaaa***************");	
		
		

		try {
		
			
			try {
				Date d=new Date();
				//int time=d.getHours();
				//int time=d.getMinutes();
			
				/*if(time==0){
					bulkUserService.bulkUpload();
				}*/
				// Bulk upload process modified for multiple time execution  
				String iBulkUploadprocessTime[] = PropertyReader.getAppValue("bulkUploadprocessTime").trim().split(",");
				logger.info("--------Enter in trigger method-------"+iBulkUploadprocessTime);
				//for(int ii=0; ii<iBulkUploadprocessTime.length; ii++)
				{
					logger.info("--------Enter in trigger method-------");
					//int iTime= Integer.parseInt(iBulkUploadprocessTime[ii]);
					//logger.info("--------Enter in trigger method-------"+iTime);
					//if(time==iTime){
						
						bulkUserService.bulkUpload();
				//	}
				}
				
				
				} catch (IOException e1) {
				
				
				logger.error("Error Occur during bulk user upload");
			}
			
			

			checkApproveFiles = service.checkApprovedFiles();
			logger.info("--------Enter in trigger method9-------"+checkApproveFiles);
			//	logger.info("--------checking pending records---------- ");
			KmUserMstr userMstr = null;
			for (Iterator itr = checkApproveFiles.iterator(); itr.hasNext();) {
				ApproveFileDto approveDto =
					(ApproveFileDto) itr.next();
				elementId = approveDto.getElementId();
				//circleId = elementService.getCircleId(elementId);
				circleId  = elementService.extractCircleId(approveDto.getDocCompletePath(), 3);
				circleName=elementService.getElementName(circleId);
				/*String fName=(userService.getUserService(approveDto.getUploadedBy())).getUserFname();
				String lName=(userService.getUserService(approveDto.getUploadedBy())).getUserLname();*/
				
				/*userMstr = userService.getUserService(userId)
				publishedBy=fName+" "+lName;*/
				
				publishedBy = approveDto.getUploadedUserName();
				String uploadedDt=approveDto.getUploadedDate().toString().substring(0,10);
				int escCount = approveDto.getEscalationCount();
				KmUserMstr manager = null;

				manager = userService.getManagers(circleId, escCount);
				approver=userService.getApprover(circleId);
				if(escCount==2){
					manager = userService.getManagers(circleId,1);	
					approver=manager.getUserFname()+" "+manager.getUserLname();		
					manager=userService.getManagers(circleId,2);	
				}
				if (manager == null) {
					//logger.info("-------Manager not Existing---------- for Circle Id "+ circleId);
					continue;

				}

				if (escCount == 1 || escCount == 2) {/*
					if (manager.getUserEmailid() != null) {
						sendMail(
							manager.getUserEmailid(),
							approveDto.getDocumentId(),
							manager.getUserFname()+" "+manager.getUserLname(),
							approveDto.getDocName(),circleName,approver,publishedBy,uploadedDt);

						service.updateEscalationTime(
							approveDto.getDocumentId(),
							manager.getUserId());
					}

				*/}

			}
			

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("exception in bulkuplode"+e);
			//throw new KmException(e.getMessage(), e);
		}
		
		logger.info("-------Out of Scheduler Trigger---------------- ");
	}

	/**
	 * 
	 * @param managerEmailId The Manager Email Id
	 * @param documentId The Document Id
	 */
	public void sendMail(
		String managerEmailId,
		String documentId,
		String userFirstName,String docDisplayName,String circleName,String approver,String publishedBy,String uploadedDt) {
		
		ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
		String subject = "Document Pending for your approval in iPortal";

		try {
			Properties prop = System.getProperties();
			prop.put("mail.smtp.host", PropertyReader.getAppValue("login.smtp"));
			Session ses = Session.getDefaultInstance(prop, null);
			MimeMessage msg = new MimeMessage(ses);
			msg.setFrom(new InternetAddress(PropertyReader.getAppValue("login.email")));
			msg.addRecipient(
				Message.RecipientType.TO,
				new InternetAddress(managerEmailId));
			msg.setSubject(subject);
			StringBuffer strMessage = new StringBuffer();
			String txtMessage = null;
			strMessage.append("Dear " + userFirstName + ", \n\n");
			strMessage.append("The document Uploaded on iPortal is pending approval from : "+approver);
			strMessage.append("\n");
			strMessage.append("\nDate             : "+uploadedDt);
			strMessage.append("\nDocument Name    : "+docDisplayName);
			strMessage.append("\nPublished By     : "+publishedBy);
			strMessage.append("\nCircle Name      : "+circleName);

			strMessage.append(
				"\n\n/** This is an Auto generated email.Please do not reply to this.**/");
			txtMessage = strMessage.toString();
			
			msg.setText(txtMessage);
			msg.setText(txtMessage);
			msg.setSentDate(new java.util.Date());
			Transport.send(msg);
		
		} catch (javax.mail.internet.AddressException ae) {
			ae.printStackTrace();
		} catch (javax.mail.MessagingException me) {
			me.printStackTrace();
		}
	}

	

}