/*
 * Created on Dec 16, 2010
 *
 */

package com.ibm.km.sms;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;
import org.eclipse.wst.common.internal.emf.utilities.PasswordEncoderDecoder;

import com.ibm.appsecure.util.Encryption;
import com.ibm.appsecure.util.IEncryption;
import com.ibm.km.common.Constants;
import com.ibm.km.common.PropertyReader;
import com.ibm.km.dao.impl.ApproveFileDaoImpl;
import com.ibm.misc.BASE64Encoder;

/**
 * This Class had method to send SMS as XML object over HTTP connection
 * @author Kundan
 *
 */
public class SendSMSXML {
	static PostMethod postMethod = null;
	static HttpClient client = null;
	Logger logger = Logger.getLogger(SendSMSXML.class);

	/**
	 * This method creates an XML string of destination Address & SMS text, initialize connection from
	 * MB (Message Broker) and creates a POST method to send XML packets over HTTP connection.
	 * @param mobileNo
	 * @param message
	 */
	public  void sendSms(String mobileNo, String message,String circleKey) {

		try {
			String placeHolderString;
			//System.out.println("circleKey........."+circleKey);
			//System.out.println("smssenderaddress......."+circleKey+PropertyReader.getValue("sms.xml.mb.sender").trim());
			IEncryption enc_dec = new Encryption();
			PasswordEncDec pwd=new PasswordEncDec();
			String ip =PropertyReader.getValue("sms.xml.mb.ip").trim();
			String port = PropertyReader.getValue("sms.xml.mb.port").trim();
			String userId = PropertyReader.getValue("sms.xml.mb.userid").trim();
			String password = pwd.decPassword((PropertyReader.getValue("sms.xml.mb.pass").trim()));
			System.out.println("Password after decypt" +password);
			//System.out.println("mine pwd" +enc_dec.decrypt("240-216-96-138-223-30-157-171"));
			String smsSender = circleKey+PropertyReader.getValue("sms.xml.mb.sender").trim(); 
			StringBuffer sbXmlMsg = new StringBuffer("");
			sbXmlMsg.append("<?xml version=\"1.0\" encoding=\"US-ASCII\"?><message>");
			sbXmlMsg.append("<sms type=\"mt\"><destination");
			sbXmlMsg.append("><address><number type=\"national\">");
			sbXmlMsg.append(mobileNo);
			sbXmlMsg.append("</number></address></destination><source><address>");
			sbXmlMsg.append("<alphanumeric>"+smsSender+"</alphanumeric>");
			sbXmlMsg.append("</address></source>");
			sbXmlMsg.append("<rsr type=\"all\" />");
			sbXmlMsg.append("<ud type=\"text\" encoding=\"default\">");
			sbXmlMsg.append(new StringBuffer()).append((message));
			sbXmlMsg.append("</ud></sms></message>");
			placeHolderString = sbXmlMsg.toString();

				/* Obtaining connection from First Hope */
			getConnection(ip, Integer.parseInt(port), userId, password);

			/* Sending SMS using XML over HTTP */
			StringRequestEntity requestEntity =	new StringRequestEntity(placeHolderString);
			postMethod.setRequestEntity(requestEntity);
			client.executeMethod(postMethod);
			String responseString = postMethod.getResponseBodyAsString();
			//System.out.println("responseString:" + responseString);


		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println("Exception occured while Sending SMS : "+ e.getMessage());
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
	}

	/**
	 * This method initialize a client and a connection from Message Broker
	 * @param mb_ip
	 * @param mb_port
	 * @param userId
	 * @param password
	 * @return
	 */
	private void getConnection(String mb_ip,int mb_port,String userId,String password)
	{
		client = new HttpClient();
		client.setHttpConnectionManager(new MultiThreadedHttpConnectionManager());
		client.getHostConfiguration();
		String url ="http"+"://"+mb_ip+ ":" +mb_port;

		try {
			postMethod = new PostMethod(url);
			postMethod.setRequestHeader("Content-Type", "text/xml");
			postMethod.setRequestHeader("Authorization", "Basic " + encode(userId + ":" + password));

		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println("Exception occured while obtaining connection: "+ e.getMessage());
		}
	}

	/**
	 * Encoding message broker credentials
	 * @param userId:password
	 * @return Encoded userId & password
	 */
	private String encode(String source) {
		BASE64Encoder enc = new BASE64Encoder();
		return (enc.encode(source.getBytes()));
	}
	
	public  String sendSms(String mobileNo, String message) {
		String status = "";
		try {
			String placeHolderString;
			/*if(mobileNo.equals("") || mobileNo == null){
				status = Constants.SEND_SMS_STATUS_FAIL;
				return status;
				
			}*/
				
			//System.out.println("circleKey........."+circleKey);
			//System.out.println("smssenderaddress......."+circleKey+PropertyReader.getValue("sms.xml.mb.sender").trim());
			PasswordEncDec pwd=new PasswordEncDec();
			IEncryption enc_dec = new Encryption();
			logger.info(":: Sending SMS "+message+" to "+mobileNo);
			String ip =PropertyReader.getValue("sms.xml.mb.ip").trim();
			String port = PropertyReader.getValue("sms.xml.mb.port").trim();
			String userId = PropertyReader.getValue("sms.xml.mb.userid").trim();
			String password = pwd.decPassword((PropertyReader.getValue("sms.xml.mb.pass").trim()));
			logger.info("Password after decypt" +password);
			//String password = PropertyReader.getValue("sms.xml.mb.pass").trim();
			//System.out.println("mine pwd" +enc_dec.decrypt("240-216-96-138-223-30-157-171"));
			String smsSender = PropertyReader.getValue("sms.xml.mb.sender").trim(); 
			StringBuffer sbXmlMsg = new StringBuffer("");
			sbXmlMsg.append("<?xml version=\"1.0\" encoding=\"US-ASCII\"?><message>");
			sbXmlMsg.append("<sms type=\"mt\"><destination");
			sbXmlMsg.append("><address><number type=\"national\">");
			sbXmlMsg.append(mobileNo);
			sbXmlMsg.append("</number></address></destination><source><address>");
			sbXmlMsg.append("<alphanumeric>"+smsSender+"</alphanumeric>");
			sbXmlMsg.append("</address></source>");
			sbXmlMsg.append("<rsr type=\"all\" />");
			sbXmlMsg.append("<ud type=\"text\" encoding=\"default\">");
			sbXmlMsg.append(new StringBuffer()).append((message));
			sbXmlMsg.append("</ud></sms></message>");
			placeHolderString = sbXmlMsg.toString();
			logger.info("Final Message ::: "+placeHolderString);

				/* Obtaining connection from First Hope */
			getConnection(ip, Integer.parseInt(port), userId, password);
			logger.info("Got connected");

			/* Sending SMS using XML over HTTP */
			StringRequestEntity requestEntity =	new StringRequestEntity(placeHolderString);
			postMethod.setRequestEntity(requestEntity);
			client.executeMethod(postMethod);
			//Print Response Body
			String responseString = postMethod.getResponseBodyAsString();
			logger.info("responseString:" + responseString);
			if(responseString != null && responseString.equalsIgnoreCase(Constants.RESPONSE_STRING_OK))
				status=Constants.SEND_SMS_STATUS_SUCCESS;
			else
				status=Constants.SEND_SMS_STATUS_FAIL;


		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception occured while Sending SMS : "+ e.getMessage());
			status =  Constants.SEND_SMS_STATUS_NOT_CONNECTED;
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return status;
	}
}
