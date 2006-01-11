/*
 * Created on Feb 14, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.services.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import com.ibm.km.actions.KmAddNewFileAction;
import com.ibm.km.dao.ApproveFileDao;
import com.ibm.km.dao.impl.ApproveFileDaoImpl;
import com.ibm.km.dto.ApproveFileDto;
import com.ibm.km.exception.KmException;
import com.ibm.km.services.ApproveFileService;

/**
 * @author Vipin
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ApproveFileServiceImpl implements ApproveFileService {
	
	/* (non-Javadoc)
	 * @see com.ibm.km.services.ApproveFileService#approveFiles(java.lang.String[])
	 */
	public void approveFiles(
		String[] fileIds,
		String[] commentList,
		String userId)
		throws KmException {
		ApproveFileDao dao = new ApproveFileDaoImpl();
		dao.deactivateOldFile(fileIds);
		dao.approveFile(fileIds, commentList, userId);
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.services.ApproveFileService#approveFiles(java.lang.String[])
	 */
	public void rejectFiles(
		String[] fileIds,
		String[] commentList,
		String userId)
		throws KmException {
		ApproveFileDao dao = new ApproveFileDaoImpl();
		dao.rejectFile(fileIds, commentList, userId);
	}

	/* (non-Javadoc)
	 * @see com.ibm.km.services.ApproveFileService#getFileList(java.lang.String, java.lang.String)
	 */
	public ArrayList getFileList(String userId)
		throws KmException {

		ApproveFileDao dao = new ApproveFileDaoImpl();
		return dao.getFileList(userId);
	}



	/* (non-Javadoc)
	 * @see com.ibm.km.services.ApproveFileService#approveFiles(java.lang.String[])
	 */
	public void updateEscalationTime(String documentId, String approverId)
		throws KmException {
		int monthDays [] = new int[12];
		monthDays[0]=31;
		monthDays[1]=29;
		monthDays[2]=31;
		monthDays[3]=30;
		monthDays[4]=31;
		monthDays[5]=30;
		monthDays[6]=31;
		monthDays[7]=31;
		monthDays[8]=30;
		monthDays[9]=31;
		monthDays[10]=30;
		monthDays[11]=31;
		ApproveFileDao dao = new ApproveFileDaoImpl();
		ApproveFileDto approveddto = dao.getUploadedTime(documentId);
		KmAddNewFileAction addAction = new KmAddNewFileAction();
		ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
		
		int ESCALATION_HOUR_1=Integer.parseInt(bundle.getString("ESCALATION_HOUR_1"));
		int ESCALATION_HOUR_2=Integer.parseInt(bundle.getString("ESCALATION_HOUR_2"));
		int HOUR_1=1;
		int HOUR_2=2;
		
		int DAY_1=1;
		int MINUTE_0 = 0;

		int escalationHrs;
		int escalationCount = approveddto.getEscalationCount();
		Timestamp escTime;
	
		// if the escalating count is 0 then, add 5 hours to the escalation Time
		if (escalationCount == 0) {
			escalationHrs = ESCALATION_HOUR_1;
			escTime = approveddto.getUploadedDate();
		} else if (
		//if the escalating count is 1 or 2 or 3 then, add 2 hours to the new escalation Time
			escalationCount == 1
				|| escalationCount == 2
				|| escalationCount == 3) {
			escalationHrs = ESCALATION_HOUR_2;
			escTime = approveddto.getEscalationTime();
		} else {
			return;
		}
		Calendar calendar = Calendar.getInstance();

		int day = Integer.parseInt(escTime.toString().substring(8, 10));
		
		final int startingTime = 9;  // Starting Time
		
		final int endingTime = 18;   // Ending Time
		String escalationTime = "";
		int hours = escTime.getHours();
		int minutes = escTime.getMinutes();
		int months = escTime.getMonth() + 1;
		int year = escTime.getYear() + 1900;
		int month = months - 1;
		String lastDay = "";

		// checking Last day of the month
		if (month == 0
			|| month == 2
			|| month == 4
			|| month == 6
			|| month == 7
			|| month == 9
			|| month == 11) {
			lastDay = "31";
		} else if (month == 3 || month == 5 || month == 8 || month == 10) {
			if (day > 30 || day < 1) {
				lastDay = "30";
			} //checking condition for leap year   
		} else {
			if ((year % 100 == 0) && (year % 400 == 0) || year % 4 == 0 ) {
				lastDay = "29";
			} else if (day < 1 || day > 28) {
				monthDays[1]=28;
				lastDay = "28";
			}
		}
		int nextDay = day;
	
		
		
		/*else if (lday ==30  && day == 30) {
			nextDay = DAY_1;
		} else if (lday == 29 && day == 29) {
			nextDay = DAY_1;
		} else if (lday == 28 && day == 28) {
			nextDay = DAY_1;
		} else {
			nextDay = day + DAY_1;
		}*/

		calendar.setTime(approveddto.getUploadedDate());

		int diff = endingTime - hours;
		int temp = escalationHrs - diff;

		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		
		// checking all possible conditions for FRIDAY, increment day by 3 if uploading time is after 1 pm on friday.
		if (dayOfWeek == Calendar.FRIDAY) {
			if (hours < startingTime) {
				hours = startingTime + escalationHrs;
				nextDay = day;
			}else if(diff<=0 &&  minutes>0 ){
				nextDay = nextDay + HOUR_2;
				hours=startingTime+escalationHrs;
			}
			 else if ((diff == escalationHrs && minutes == 0) || diff > escalationHrs) {
				hours = hours + escalationHrs;
				nextDay = day;
			} else if (diff < escalationHrs && diff>0) {
				nextDay = nextDay + HOUR_2;
				hours = startingTime + temp;
			} else if (diff == escalationHrs && minutes>0 ) {
				hours = startingTime;
				nextDay = nextDay + HOUR_2;
			} else {
				nextDay = nextDay + HOUR_2;
				hours = startingTime + escalationHrs;
				minutes = MINUTE_0;
			}
		} 
		// checking condition for SATURDAY, increment day by 2 and escalating time will be 9 am on monday
		else if (dayOfWeek == Calendar.SATURDAY) {
			nextDay = nextDay + HOUR_1;
			minutes =MINUTE_0;
			hours = startingTime + escalationHrs;
		} 
		
		//checking condition for SUNDAY, increment day by 1 and escalating time will be 9 am on monday
		else if (dayOfWeek == Calendar.SUNDAY) { 
			//nextDay=
			hours = startingTime + escalationHrs;
			minutes = MINUTE_0;
		} else {   
			if (hours < startingTime) {  // checking conditions for Other Days
					hours = startingTime + escalationHrs;
					nextDay = day;
				}else if(diff<=0 &&  minutes>0 ){
				//	nextDay = day + 1;
					hours=startingTime+escalationHrs;
					minutes = MINUTE_0;
				}
				 else if ((diff == escalationHrs && minutes == 0) || diff > escalationHrs) {
					hours = hours + escalationHrs;
					nextDay = day;
				} else if (diff < escalationHrs && diff>0) {
				//	nextDay = day + 1;
					hours = startingTime + temp;
				} else if (diff == escalationHrs && minutes>0 ) {
					hours = startingTime;
				//	nextDay = day + 1;
				} else {
				//	nextDay = day + 1;
					hours = startingTime + escalationHrs;
					minutes = MINUTE_0;
				}
		}
		
		// if the day is last day of the month then, next day with be the first day of next month
		if (nextDay >= monthDays[month]) {
			
			if(nextDay > monthDays[month])
				nextDay = monthDays[month] -day;
			else
				nextDay = DAY_1;
		} 
		String months1 = months + "";
		String minutes1 = minutes + "";
		String day1 = nextDay + "";
		String hours1 = hours + "";
		
		// if month is december then, increment year by 1 and make month as jan.
		if (months == 12) {
			months1 = "01";
			year = year + 1;
		}

		// when minute is less than 10, put 0 as prefix
		if (minutes < 10) {  
			minutes1 = "0" + minutes;
		}
		
		// when hour is less than 10, put 0 as prefix
		if (hours < 10) {
			hours1 = "0" + hours;
		}
		
		// calculating Scheduling Time
		escalationTime =
			year
				+ "-"
				+ months1
				+ "-"
				+ day1
				+ " "
				+ hours1
				+ ":"
				+ minutes1
				+ ":00";
		escalationCount++;
		dao.updateEscalationTime(documentId, escalationTime, escalationCount,approverId);

	}

	/* (non-Javadoc)
	 * @see com.ibm.km.services.ApproveFileService#approveFiles(java.lang.String[])
	 */
	public List checkApprovedFiles() throws KmException {
		ApproveFileDao dao = new ApproveFileDaoImpl();
		return dao.checkApprovedFiles();
	}

}
