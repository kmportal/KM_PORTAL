package com.ibm.km.common;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.search.CachingWrapperFilter;
import org.apache.lucene.search.Filter;

import com.ibm.km.dao.impl.KmDocumentMstrDaoImpl;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;
import com.ibm.ws.cache.config.ElementHandler;

public class Utility {
public static String getNetworkFaultIndexPath(){
	
	Calendar cal = GregorianCalendar.getInstance();
	StringBuffer path = new StringBuffer(PropertyReader.getAppValue("network.fault.index.path")).append("/").append(cal.get(cal.YEAR)).append("-").append(cal.get(cal.MONTH)+1).append("-").append(cal.get(cal.DATE));
	return path.toString();
}

public static String getCurrentTime(){
	
	Calendar cal = GregorianCalendar.getInstance();
	StringBuffer path = new StringBuffer("").append(cal.get(cal.DATE)).append("/").append(cal.get(cal.MONTH)+1).append("/").append(cal.get(cal.YEAR)).append(" ").append(cal.get(cal.HOUR_OF_DAY)).append(":").append(cal.get(cal.MINUTE)).append(":").append(cal.get(cal.SECOND));
	return path.toString();
}

public static String getCurrentTime_(){
	
	Calendar cal = GregorianCalendar.getInstance();
	String month=changeFormat(cal.get(cal.MONTH)+1);
	String day=changeFormat(cal.get(cal.DATE));
	String hr=changeFormat(cal.get(cal.HOUR_OF_DAY));
	String min=changeFormat(cal.get(cal.MINUTE));
	String sec=changeFormat(cal.get(cal.SECOND));
		
	StringBuffer path = new StringBuffer("").append(cal.get(cal.YEAR)).append("-").append(month).append("-").append(day).append(" ").append(hr).append(":").append(min).append(":").append(sec);
	return path.toString();
}
public static String changeFormat(int in){
	String  val="";
	if(in < 10)
		 val ="0"+in ;
	else
		val =""+in ;
	
return val;	
}

 public static java.sql.Date getSqlDateFromString(String strDate,String strFormat) {
	SimpleDateFormat sdf;
	try {
		sdf = new SimpleDateFormat(strFormat);  
		return new java.sql.Date(sdf.parse(strDate).getTime());
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		System.out.println("Exception occured while parsign date"+e);
		return null;
	} 
}

 public static java.sql.Date addDay(java.sql.Date dt, int days) {
	 Calendar cal = Calendar.getInstance();    
	 cal.setTime(dt); 
	 cal.add(Calendar.DATE, days);
	 return new java.sql.Date(cal.getTimeInMillis());
	 }
	 
 public static String getDocumentViewURL(String documentId, int docType) {
	 String docUrl = "";
	 switch(docType)
		{
		  case 0 : 							docUrl = "documentAction.do?methodName=displayDocument";
		  									break;	
		  case Constants.DOC_TYPE_FILE : 	docUrl = "documentAction.do?methodName=displayDocument";
											break;	
		  case Constants.DOC_TYPE_PRODUCT : docUrl = "productUpload.do?methodName=viewProductDetails";
		  									break;	
		  case Constants.DOC_TYPE_SOP : 	docUrl = "sopUpload.do?methodName=viewSopDetails";
		  									break;	
		  case Constants.DOC_TYPE_SOP_BD : 	docUrl = "sopBDUpload.do?methodName=viewSopBDDetails";
											break;	
		  case Constants.DOC_TYPE_RC : 		docUrl = "rcContentUpload.do?methodName=viewRCData";
		  									break;	
		  case Constants.DOC_TYPE_BP : 		docUrl = "bpUpload.do?methodName=viewBPDetails";
			 								break;	
		  case Constants.DOC_TYPE_DTH : 	docUrl = "offerUpload.do?methodName=viewDTHOffer";
		  									break;
		  default : 							docUrl = "documentAction.do?methodName=displayDocument";
		}
	 
	 docUrl += "&docID=" + documentId;
	 return docUrl;
 }
 
 public static String getDocumentViewURL(String documentId) {
	 int docType = 0;
	 try {
		docType = new KmDocumentMstrDaoImpl().getDocumentType(documentId);
	} catch (KmException e) {
		e.printStackTrace();
	} catch (DAOException e) {
		e.printStackTrace();
	}
	 return  getDocumentViewURL(documentId , docType);
 } 
 public static String encodeContent(String content) {	
	 content = content.replaceAll("<script", "< script").replaceAll("</script", "< /script").replaceAll("<SCRIPT", "< SCRIPT").replaceAll("</SCRIPT", "< /SCRIPT");
	 content = content.replaceAll(";script", "; script").replaceAll(";/script", "; /script").replaceAll(";SCRIPT", "; SCRIPT").replaceAll(";/SCRIPT", "; /SCRIPT");
	 content = convert2ValidUTF8(content);
	 return  ((content.replaceAll(">"," &gt; ")).replaceAll("<"," &lt; ")).replaceAll("&","&amp;"); 
 }
 
	private static String convert2ValidUTF8(String str) {
		try {
			byte[] data = str.getBytes("UTF-8");
			 str = new String(data, "UTF-8");
			 data = str.getBytes("UTF-8");
			 for (int i = 0; i < data.length; i++) {
				 boolean b =	Character.isDefined(new Byte(data[i]).intValue());
					 if(b == false) 
					 {
						 System.out.println("INVALID Character found:" + str.charAt(i) + " Value: " + new Byte(data[i]).intValue() );
						 if(str.charAt(i) == '’') {
							str = str.replace('’', '\'');
						 }
						 else
						 {
							 str = str.replace(str.charAt(i), ' ');
						 }
						 str = convert2ValidUTF8(str);
						 break;
					 }
			    }

				 
		} catch (Exception e) {
			 System.out.println("<<Error while UTF-8 String validation  >> " + e);
		}
		return str;
	}

 public static String decodeContent(String content) {	
	 content =  ((content.replaceAll(" &gt; ",">")).replaceAll(" &lt; ","<")).replaceAll("&amp;","&");
	 content = (content.replaceAll("&gt;",">")).replaceAll("&lt;","<");
	 return content;
 }

 public static ArrayList<String> getMatch(String value)throws KmException{
	 ArrayList<String> arr = new ArrayList<String>();
	 ElementHandler handler = new ElementHandler();
	 KeyChar kc = KeyChar.getRoot();
	 if(kc != null) {
		 arr = kc.match(value);
			System.out.println("OUT:" + arr);
	 return arr;
	 }
//	 handler.addRules((RuleHandler)(Object) value);
	 return arr;
 }

 public static void insertWords(ArrayList<String> value)throws KmException{
	 if(value == null || value.size() == 0) return;
	 KeyChar kc = KeyChar.getRoot();
	 for(int i = 0; i < value.size(); i++) {
		 String temp = value.get(i);
		 if(kc == null) 
			 kc = new KeyChar(temp);
		 else
			 kc.addNode(temp);
	 }
//	 cacheWrapper.equals(value);
//	 handler.addRules((RuleHandler)(Object) cacheWrapper);
//	 handler.finished();
 }

 	//added by Beeru on 16 Apr 2014
	public static String getStackTrace(Exception e)
	{
	    StringWriter sWriter = new StringWriter();
	    PrintWriter pWriter = new PrintWriter(sWriter);
	    e.printStackTrace(pWriter);
	    return sWriter.toString();
	}
	
	public static boolean isEmptyOrContainsSpecial(String s) {
		//System.out.println("test hai"+s);
	     if (s == null || isSpecial(s)) {
	    	// System.out.println("test hai true "+s);
	    	 return true;
	        
	     }
	    // System.out.println("test hai false "+s);
	     return false;
	}
	
	public static boolean isEmpty(String s) {
	     if (s == null || s.trim().isEmpty()) {
	         return true;
	     }
	return false;
	}
	
	public static boolean isSpecial(String s) {
	     if (s!=null) {	    	 
	    	 
	    	 Pattern p = Pattern.compile("[^a-z0-9A-Z ]");
	    	 Matcher m = p.matcher(s);
	    	 boolean b = m.find();
	    	// System.out.println("Strin value special"+ b);
	         return b;
	     }
	return false;
	}
	
	public static String getRandomCode(){

		final String dCase = "abcdefghijklmnopqrstuvwxyz";
	    final String uCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	    final String sChar = "$?*/\\<>^'";
	    final String intChar = "0123456789";
	    Random r = new Random();
	    String pass = "";
	    
	    while (pass.length () != 8){
            int rPick = r.nextInt(4);
            if (rPick == 0){
                int spot = r.nextInt(25);
                pass += dCase.charAt(spot);
            } else if (rPick == 1) {
                int spot = r.nextInt (25);
                pass += uCase.charAt(spot);
            } else if (rPick == 2) {
                int spot = r.nextInt (7);
                pass += sChar.charAt(spot);
            } else if (rPick == 3){
                int spot = r.nextInt (9);
                pass += intChar.charAt (spot);
            }
        }
        return pass;
	}
}

