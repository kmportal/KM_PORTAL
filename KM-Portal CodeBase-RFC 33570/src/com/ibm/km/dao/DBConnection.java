package com.ibm.km.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.ibm.km.common.PropertyReader;
import com.ibm.km.exception.DAOException;

/** 
 * The DBConnection class, this class is used to get a DB connection using a datasource
 * This class can be used to connect to any datasource, not just Oracle, so the name is a misnomer for this version
 * In case you want this  class to use in your project, replace BarringException with your own exception class 
 * @author Rohit Dhal, adapted by Puneet Lakhina for Code Generator.
 * @date 21-Mar-2007
 */

public class DBConnection {
	
	
	private static Logger logger = Logger.getLogger(DBConnection.class);
	private static DataSource mem_o_datasource;
	private static DataSource hrms_datasource;
	private static int counter=0;

	
	/**
	 * The default private constructor.Use getDBConnection() to get a connection
	 *
	 */
	public DBConnection() {
	}
	/**
	 * This method does the lookup of the datasource and store it in a memeber variable to be 
	 * used later , to avoid doing lookups again and again
	 * @exception com.ibm.km.exception.DAOException This exception is thrown in case data source cannot be looked up
	 */
	private  static void getDataSource() throws com.ibm.km.exception.DAOException {
		try {
			synchronized (DBConnection.class){
				if (mem_o_datasource == null) {
					// TODO Read the data source name from a property file.
				//	String lookupname = "KMJNDI";
					InitialContext loc_o_ic = new InitialContext();
					//mem_o_datasource = (DataSource) loc_o_ic.lookup(lookupname);
					mem_o_datasource =
						(DataSource) loc_o_ic.lookup(
							PropertyReader.getValue("DATASOURCE_LOOKUP_NAME"));					

				}
			}
			//TODO Log that data source has been looked up
		} catch (NamingException namingException) {
			//TODO LOG this 
			logger.error("Lookup of Data Source Failed. Reason:" + namingException.getMessage());
			com.ibm.km.exception.DAOException exception =
				new com.ibm.km.exception.DAOException("Exception Occured while data source lookup.",namingException);
			throw exception;
		}
	}
	
	/**
	 * This method does the lookup of the HRMS datasource and store it in a memeber variable to be 
	 * used later , to avoid doing lookups again and again
	 * @exception com.ibm.km.exception.DAOException This exception is thrown in case data source cannot be looked up
	 */
	private  static void getHRMSDataSource() throws com.ibm.km.exception.DAOException {
		try {
			synchronized (DBConnection.class){
				if (hrms_datasource == null) {
					InitialContext loc_o_ic = new InitialContext();
					hrms_datasource =(DataSource) loc_o_ic.lookup(PropertyReader.getValue("OLMSDS_LOOKUP_NAME"));					
				}
			}
		} catch (NamingException namingException) {
			logger.error("Lookup of LDAP Data Source Failed. Reason:" + namingException.getMessage());
			com.ibm.km.exception.DAOException exception =
			new com.ibm.km.exception.DAOException("Exception Occured while data source lookup.",namingException);
			throw exception;
		}
	}
	
	/**
	 * This method returns the db connection using a datasource
	 * @return Connection the db connection instance
	 * @exception com.ibm.km.exception.DAOException This exception is thrown in case connection is not established
	 */
	public static  Connection getDBConnection() throws com.ibm.km.exception.DAOException {
	//	logger.info("Request for connection received");
		Connection dbConnection = null;
		try {

	//		logger.info("Counter in getDBConnection : "+counter);
			counter++;

			if (mem_o_datasource == null) {
					getDataSource();
				}
				dbConnection =
				mem_o_datasource.getConnection();
		// TODO Log that connection has been established
		} catch (SQLException sqlException) {
			// TODO LOG THIS
			logger.error("Could Not Obtain Connection. Reason:" + sqlException.getMessage() + ". Error Code:" + sqlException.getErrorCode());
			com.ibm.km.exception.DAOException exception =
				new com.ibm.km.exception.DAOException("Exception Occured while obtaining Connection.",sqlException);
			throw exception;
		}
	//	logger.info("Connection Obtained.");
		return dbConnection;
	}
	
	/**
	 * This method returns the db connection using a datasource
	 * @return Connection the db connection instance
	 * @exception com.ibm.km.exception.DAOException This exception is thrown in case connection is not established
	 */
	public static  Connection getOLMSConnection() throws com.ibm.km.exception.DAOException {
		logger.info("Request for connection received");
		Connection oracleConnection = null;
		try {
			
			if (hrms_datasource == null) {
				getHRMSDataSource();
				}
			oracleConnection =
				hrms_datasource.getConnection();
			logger.info("Connection Obtained.");
		} catch (SQLException sqlException) {
			logger.error("Couldn't connected to HRMS server. Reason:" + sqlException.getMessage() + ". Error Code:" + sqlException.getErrorCode());
			com.ibm.km.exception.DAOException exception =
				new com.ibm.km.exception.DAOException("Couldn't connected to HRMS server.");
			throw exception;
		}
		
		return oracleConnection;
	}
	
	public static void releaseResources(
		Connection connection,
		Statement statement,
		ResultSet resultSet)
		throws DAOException {
//		logger.info("Request for releasing resources recieved");
		try {
			
		
						counter--;
// 		logger.info("Counter in releaseResources: "+counter);

			if (resultSet != null) {
				resultSet.close();
				resultSet = null;
			}
			if (statement != null) {
				statement.close();
				statement = null;
			}
			if (connection != null) {
				connection.close();
				connection = null;
			}

		} catch (SQLException sqlException) {
			logger.error("Closing of Resources Failed. Reason:" + sqlException.getMessage()+". Error Code:"+ sqlException.getErrorCode());
			throw new DAOException("errors.dbconnection.close_connection");
		}

	}
	

}
