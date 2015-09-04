/*
 * Created on Nov 5, 2007
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author avanagar
 * @version 2.0
 * 
 */
public class ArcLibrary {

	protected static final int ONE_DAY = 1000 * 60 * 60 * 24;

	/**
		 * This method returns current Date Time.
		 * @param String dateFormat
	*/

	public static String getCurrentDate(String dateFormat) {
		DateFormat df = null;
		Date curDate = null;
		try {
			df = new SimpleDateFormat(dateFormat);
			long currentTime = System.currentTimeMillis();
			curDate = new Date(currentTime);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return df.format(curDate);
	}

	public static Date getCurrentDate() {
		return new Date(System.currentTimeMillis());
	}

	//	Difference between two dates returns boolean
	public boolean dateDifference(String date1, String date2) {

		try {
			DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
			DateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");

			java.util.Date earlyDate = df1.parse(date1);
			java.util.Date lastDate = df1.parse(date2);

			int days = (int) (lastDate.getTime() - earlyDate.getTime()) / ONE_DAY;

			if (days < 7 && days >= 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	
	

	//Return no. of days in a month
	public static int daysInMonth(GregorianCalendar c) {
		int[] daysInMonths = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		daysInMonths[1] += c.isLeapYear(c.get(GregorianCalendar.YEAR)) ? 1 : 0;
		return daysInMonths[c.get(GregorianCalendar.MONTH)];

	}

	//	Change String date to gregorian
	public static void dateToGregorian(String date1) {
		GregorianCalendar gc = new GregorianCalendar();
		try {
			DateFormat df1 = new SimpleDateFormat("MMM/yyyy");
			java.util.Date earlyDate = df1.parse(date1);

			gc.setTime(earlyDate);

		} catch (Exception e) {
		}

	}

	public static String getPreviousMonth() {
		DateFormat df = null;
		Date curDate = null;
		String MMMYYYY = null;
		try {
			df = new SimpleDateFormat("MMM/yyyy");
			long currentTime = System.currentTimeMillis();
			curDate = new Date(currentTime);
			MMMYYYY = df.format(curDate);
			String MMM = MMMYYYY.substring(0, 3);
			String YYYY = MMMYYYY.substring(4, 8);
			if (MMM.equalsIgnoreCase("FEB")) {
				MMM = "JAN";
			}
			if (MMM.equalsIgnoreCase("MAR")) {
				MMM = "FEB";
			}
			if (MMM.equalsIgnoreCase("APR")) {
				MMM = "MAR";
			}
			if (MMM.equalsIgnoreCase("MAY")) {
				MMM = "APR";
			}
			if (MMM.equalsIgnoreCase("JUN")) {
				MMM = "MAY";
			}
			if (MMM.equalsIgnoreCase("JUL")) {
				MMM = "JUN";
			}
			if (MMM.equalsIgnoreCase("AUG")) {
				MMM = "JUL";
			}
			if (MMM.equalsIgnoreCase("SEP")) {
				MMM = "AUG";
			}
			if (MMM.equalsIgnoreCase("OCT")) {
				MMM = "SEP";
			}
			if (MMM.equalsIgnoreCase("NOV")) {
				MMM = "OCT";
			}
			if (MMM.equalsIgnoreCase("DEC")) {
				MMM = "NOV";
			}
			if (MMM.equalsIgnoreCase("JAN")) {
				MMM = "DEC";
				int y = Integer.parseInt(YYYY);
				YYYY = new Integer(y - 1).toString();
			}
			MMMYYYY = MMM + "/" + YYYY;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return MMMYYYY;
	}
	
	public boolean isInteger(String check)
		{
			String theInput = check.trim();
			int theLength = theInput.length();

			for (int i = 0 ; i < theLength ; i++)
			{
				if (theInput.charAt(i) < '0' || theInput.charAt(i) > '9')
				{
					 //the text field has a non numeric entry
					return(false);
				}
			}// for ends

			return (true);
		}// function isInteger ends

}
