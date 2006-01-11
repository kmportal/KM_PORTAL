/*
 * author>>aman
 */
package com.ibm.km.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.ibm.km.dao.BulkUploadDetailsDao;
import com.ibm.km.dao.BulkUserDao;
import com.ibm.km.dao.DBConnection;
import com.ibm.km.dto.BulkUploadDetailsDTO;
import com.ibm.km.dto.KmFileDto;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class BulkUploadDetailsDaoImpl implements BulkUploadDetailsDao {

	Logger logger = Logger.getLogger(BulkUploadDetailsDaoImpl.class);

	private static java.sql.Date getSqlDateFromString(String strDate,
			String strFormat) {

		SimpleDateFormat sdf;
		try {
			sdf = new SimpleDateFormat(strFormat);
			return new java.sql.Date(sdf.parse(strDate).getTime());
		} catch (ParseException e) {
			return null;
		}
	}

	// ARC Details Upload

	public ArrayList<BulkUploadDetailsDTO> bulkARCDetails(
			ArrayList<BulkUploadDetailsDTO> listBulkDto, KmUserMstr userBean)
			throws DAOException {

		Connection con = null;
		PreparedStatement psInsertDetails = null;
		PreparedStatement psDeleteDetails = null;
		ArrayList<BulkUploadDetailsDTO> returnBulkMstrDTO = new ArrayList<BulkUploadDetailsDTO>();

		try {
			BulkUploadDetailsDTO bulkMstrDTO = new BulkUploadDetailsDTO();
			con = DBConnection.getDBConnection();

			psInsertDetails = con
					.prepareStatement("INSERT INTO KM_ARC_DETAILS(CIRCLE_NAME, CITY_NAME, ARC_STORE, PINCODE, ADDRESS, TIMING, AVAILABILITY_OF_CCDC_MACHINE, AVAILABILITY_OF_DOCTOR_SIM, CREATED_DT, CREATED_BY, STATUS, ARCOR, ZONE, SRNO) VALUES(? , ? , ? , ? , ? , ? , ? , ? , current timestamp, ? , 'A', ? , ?,NEXT VALUE FOR KM_ARC_SEQ ) WITH UR");

			psDeleteDetails = con
					.prepareStatement("UPDATE KM_ARC_DETAILS SET STATUS='D', UPDATED_DT=current timestamp, UPDATED_BY=? WHERE CIRCLE_NAME=? and  CITY_NAME=? and  ARC_STORE=? and  PINCODE=? and  ADDRESS=? and  TIMING=? and  AVAILABILITY_OF_CCDC_MACHINE=? and  AVAILABILITY_OF_DOCTOR_SIM=? and  ARCOR=? and  ZONE=? and STATUS='A' WITH UR");

			for (Iterator<BulkUploadDetailsDTO> itr = listBulkDto.iterator(); itr
					.hasNext();) {
				try {
					BulkUploadDetailsDTO dto1;
					dto1 = (BulkUploadDetailsDTO) itr.next();

					if (!(dto1.getAction().equalsIgnoreCase("c") || dto1
							.getAction().equalsIgnoreCase("d"))) {
						logger
								.info("Enter either c or d for CREATION/DELETION |");
						dto1
								.setMessage("Enter either c or d for CREATION/DELETION | ");
						dto1.setErrFlag(true);
						returnBulkMstrDTO.add(dto1);
						continue;
					}
					logger.info(" $$$$$$$$$$$$$$$$  "+dto1.getCircle());
					if (dto1.getCircle() == null || dto1.getCity() == null || dto1.getArc() == null || dto1.getPinCode() == null	|| dto1.getAddress() == null
							|| dto1.getTimings() == null || dto1.getAvailabilityMc() == null ||dto1.getAvailabilitySim() == null ||dto1.getArcOr() == null ||dto1.getRequestorLoc() == null
							||dto1.getZone() == null){

						logger
								.info("Please fill all the required details |");
						dto1
								.setMessage("Please fill all the required details | ");
						dto1.setErrFlag(true);
						returnBulkMstrDTO.add(dto1);
						continue;
					}
					if (dto1.isErrFlag()) {
						/*
						 * if(!(dto1.getAction().equalsIgnoreCase("c") ||
						 * dto1.getAction().equalsIgnoreCase("d") ) ) {
						 * dto1.setMessage
						 * ("Enter either c or d for CREATION/DELETION | ");
						 * dto1.setErrFlag(true); returnBulkMstrDTO.add(dto1);
						 * continue; }
						 */

						// validations to be added for blank fields
						/*
						 * if(dto1.getRsuCode().equals("") ||
						 * dto1.getCityZoneCode().equals("")) {dto1.setMessage(
						 * "RSU Code and CityZone Code are mandatory fields |");
						 * dto1.setErrFlag(true); returnBulkMstrDTO.add(dto1);
						 * continue; }
						 */
					}

					else if (!dto1.isErrFlag()
							&& dto1.getAction().equalsIgnoreCase("c")) {

						// validation to be added for duplicate records

						if (isDuplicateARC(dto1)) {
							logger
									.info("in dupliacte ARC check::::::::::::::::::::::::::::");
							dto1.setMessage("ARC details Already Exists.");
							dto1.setErrFlag(true);
							returnBulkMstrDTO.add(dto1);
							continue;
						}

						else {
							psInsertDetails.setString(1, dto1.getCircle()
									.toUpperCase());
							psInsertDetails.setString(2, dto1.getCity()
									.toUpperCase());
							psInsertDetails.setString(3, dto1.getArc()
									.toUpperCase());
							psInsertDetails.setString(4, dto1.getPinCode());
							psInsertDetails.setString(5, dto1.getAddress()
									.toUpperCase());
							psInsertDetails.setString(6, dto1.getTimings());
							psInsertDetails.setString(7, dto1
									.getAvailabilityMc().toUpperCase());
							psInsertDetails.setString(8, dto1
									.getAvailabilitySim().toUpperCase());
							psInsertDetails.setString(9, userBean
									.getUserLoginId());
							psInsertDetails.setString(10, dto1.getArcOr()
									.toUpperCase());
							psInsertDetails.setString(11, dto1.getZone()
									.toUpperCase());

							psInsertDetails.executeUpdate();
							dto1.setMessage("Details Inserted Successfully.");
							returnBulkMstrDTO.add(dto1);
							continue;
						}
					}

					else if (!dto1.isErrFlag()
							&& dto1.getAction().equalsIgnoreCase("d")) {

						if (!isDuplicateARC(dto1)) {
							dto1.setMessage("Details Doesnt Exist.");
							dto1.setErrFlag(true);
							returnBulkMstrDTO.add(dto1);
							continue;
						}

						else if (isDuplicateARC(dto1)) {

							psDeleteDetails.setString(1, userBean
									.getUserLoginId());
							psDeleteDetails.setString(2, dto1.getCircle()
									.toUpperCase());
							psDeleteDetails.setString(3, dto1.getCity()
									.toUpperCase());
							psDeleteDetails.setString(4, dto1.getArc()
									.toUpperCase());
							psDeleteDetails.setString(5, dto1.getPinCode());
							psDeleteDetails.setString(6, dto1.getAddress()
									.toUpperCase());
							psDeleteDetails.setString(7, dto1.getTimings());
							psDeleteDetails.setString(8, dto1
									.getAvailabilityMc().toUpperCase());
							psDeleteDetails.setString(9, dto1
									.getAvailabilitySim().toUpperCase());
							psDeleteDetails.setString(10, dto1.getArcOr()
									.toUpperCase());
							psDeleteDetails.setString(11, dto1.getZone()
									.toUpperCase());

							psDeleteDetails.executeUpdate();
							bulkMstrDTO = new BulkUploadDetailsDTO();

							dto1.setMessage("Details deleted successfully");

							returnBulkMstrDTO.add(dto1);
							continue;

						}

						else {
							dto1.setMessage("Details could not be deleted.");
							returnBulkMstrDTO.add(dto1);
							continue;
						}
					}

					// }
				}

				catch (Exception e) {
					// dto1 to be updated in returnBulkMstrDTO
					BulkUploadDetailsDTO bulkDto = new BulkUploadDetailsDTO();
					bulkDto.setMessage("Details not uploaded.");
					returnBulkMstrDTO.add(bulkDto);
					e.printStackTrace();
				}

			} // END Iterator

		}

		catch (DAOException e) {
			logger.error("Error while uploading ARC details::", e);
			e.printStackTrace();
		}

		catch (SQLException e) {
			e.printStackTrace();
			logger.error("Error while uploading::", e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				DBConnection.releaseResources(con, psInsertDetails, null);
				DBConnection.releaseResources(con, psDeleteDetails, null);
			} catch (Exception e) {
				throw new DAOException(e.getMessage(), e);
			}
		}

		return returnBulkMstrDTO;

	}

	@Override
	public ArrayList<BulkUploadDetailsDTO> bulkCoordinatorDetails(
			ArrayList<BulkUploadDetailsDTO> listBulkDto, KmUserMstr userBean)
			throws DAOException {
		Connection con = null;
		PreparedStatement psInsertDetails = null;
		PreparedStatement psDeleteDetails = null;
		ArrayList<BulkUploadDetailsDTO> returnBulkMstrDTO = new ArrayList<BulkUploadDetailsDTO>();

		try {
			BulkUploadDetailsDTO bulkMstrDTO = new BulkUploadDetailsDTO();
			con = DBConnection.getDBConnection();

			psInsertDetails = con
					.prepareStatement("INSERT INTO KM_COORDINATOR_DETAILS(CIRCLE_NAME, COMPANY_NAME, COMPANY_SPOC_NAME, COMPANY_SPOC_EMAILID, COMPANY_SPOC_CONTACTNO, RM_NAME, RM_EMAIL_ID, RM_CONTACTNO, REQUESTOR, REQUESTOR_LOCATION, REQUESTOR_CONT_NO, REQUESTED_DATE, CREATED_DT, CREATED_BY, STATUS,SRNO) VALUES(?, ?, ? , ?, ?, ?, ?, ? ,?, ?, ?,?, current timestamp, ?,'A',NEXT VALUE FOR KM_CORDS_SEQ) WITH UR");

			psDeleteDetails = con
					.prepareStatement("UPDATE KM_COORDINATOR_DETAILS SET STATUS='D', UPDATED_DT=current timestamp, UPDATED_BY=? WHERE CIRCLE_NAME=? and  COMPANY_NAME=? and  COMPANY_SPOC_NAME=? and COMPANY_SPOC_EMAILID=? and  COMPANY_SPOC_CONTACTNO=? and RM_NAME=? and RM_EMAIL_ID=? and  RM_CONTACTNO=?  and REQUESTOR=? and REQUESTOR_LOCATION=? and  REQUESTOR_CONT_NO=? and  REQUESTED_DATE=? and STATUS='A' WITH UR");

			for (Iterator<BulkUploadDetailsDTO> itr = listBulkDto.iterator(); itr
					.hasNext();) {
				try {
					BulkUploadDetailsDTO dto1;
					dto1 = (BulkUploadDetailsDTO) itr.next();

					if (!(dto1.getAction().equalsIgnoreCase("c") || dto1
							.getAction().equalsIgnoreCase("d"))) {
						logger
								.info("Enter either c or d for CREATION/DELETION |");
						dto1
								.setMessage("Enter either c or d for CREATION/DELETION | ");
						dto1.setErrFlag(true);
						returnBulkMstrDTO.add(dto1);
						continue;
					}
					logger.info(" $$$$$$$$$$$$$$$$  "+dto1.getCircle());
					if (dto1.getCircle() == null || dto1.getCompany() == null || dto1.getSpoc() == null || dto1.getSpocMail() == null	|| dto1.getSpocPhone() == null
							|| dto1.getRm() == null || dto1.getRmMail() == null ||dto1.getRmPh() == null ||dto1.getRequestor() == null ||dto1.getRequestorLoc() == null
							||dto1.getTsm() == null ||dto1.getTsmMail() == null	||dto1.getTsmContact() == null ||dto1.getSrManager() == null ||dto1.getSrManagerMail() == null
							||dto1.getRequestorPh() == null ||dto1.getReqOn() == null){

						logger
								.info("Please fill all the required details |");
						dto1
								.setMessage("Please fill all the required details | ");
						dto1.setErrFlag(true);
						returnBulkMstrDTO.add(dto1);
						continue;
					}
					if (dto1.isErrFlag()) {
						/*
						 * if(!(dto1.getAction().equalsIgnoreCase("c") ||
						 * dto1.getAction().equalsIgnoreCase("d") ) ) {
						 * dto1.setMessage
						 * ("Enter either c or d for CREATION/DELETION | ");
						 * returnBulkMstrDTO.add(dto1); continue; }
						 */

						// validations to be added for blank fields
						/*
						 * if(dto1.getRsuCode().equals("") ||
						 * dto1.getCityZoneCode().equals("")) {dto1.setMessage(
						 * "RSU Code and CityZone Code are mandatory fields |");
						 * dto1.setErrFlag(true); returnBulkMstrDTO.add(dto1);
						 * continue; }
						 */
					}

					else if (!dto1.isErrFlag()
							&& dto1.getAction().equalsIgnoreCase("c")) {

						// validation to be added for duplicate records

						if (isDuplicateCoordinatorDetails(dto1)) {
							logger
									.info("in dupliacte details check::::::::::::::::::::::::::::");
							dto1.setMessage("Details Already Exists.");
							dto1.setErrFlag(true);
							returnBulkMstrDTO.add(dto1);
							continue;
						}

						else {

							psInsertDetails.setString(1, dto1.getCircle()
									.toUpperCase());
							psInsertDetails.setString(2, dto1.getCompany()
									.toUpperCase());
							psInsertDetails.setString(3, dto1.getSpoc()
									.toUpperCase());
							psInsertDetails.setString(4, dto1.getSpocMail());
							psInsertDetails.setString(5, dto1.getSpocPhone());
							psInsertDetails.setString(6, dto1.getRm()
									.toUpperCase());
							psInsertDetails.setString(7, dto1.getRmMail());
							psInsertDetails.setString(8, dto1.getRmPh());
							psInsertDetails.setString(9, dto1.getRequestor()
									.toUpperCase());
							psInsertDetails.setString(10, dto1
									.getRequestorLoc().toUpperCase());
							psInsertDetails
									.setString(11, dto1.getRequestorPh());
							psInsertDetails.setString(12, dto1.getReqOn()
									.toString().trim());

							// psInsertDetails.setDate(12,
							// getSqlDateFromString(dto1.getReqOn(),"dd-MM-yyyy"
							// ));
							psInsertDetails.setString(13, userBean
									.getUserLoginId());

							psInsertDetails.executeUpdate();
							dto1.setMessage("Details Inserted Successfully.");
							returnBulkMstrDTO.add(dto1);
							continue;
						}
					}

					else if (!dto1.isErrFlag()
							&& dto1.getAction().equalsIgnoreCase("d")) {

						if (!isDuplicateCoordinatorDetails(dto1)) {
							dto1.setMessage("Details Doesnt Exist.");
							dto1.setErrFlag(true);
							returnBulkMstrDTO.add(dto1);
							continue;
						}

						else if (isDuplicateCoordinatorDetails(dto1)) {

							psDeleteDetails.setString(1, userBean
									.getUserLoginId());
							psDeleteDetails.setString(2, dto1.getCircle()
									.toUpperCase());
							psDeleteDetails.setString(3, dto1.getCompany()
									.toUpperCase());
							psDeleteDetails.setString(4, dto1.getSpoc()
									.toUpperCase());
							psDeleteDetails.setString(5, dto1.getSpocMail());
							psDeleteDetails.setString(6, dto1.getSpocPhone());
							psDeleteDetails.setString(7, dto1.getRm()
									.toUpperCase());
							psDeleteDetails.setString(8, dto1.getRmMail());
							psDeleteDetails.setString(9, dto1.getRmPh());
							psDeleteDetails.setString(10, dto1.getRequestor()
									.toUpperCase());
							psDeleteDetails.setString(11, dto1
									.getRequestorLoc().toUpperCase());
							psDeleteDetails
									.setString(12, dto1.getRequestorPh());
							psDeleteDetails.setString(13, dto1.getReqOn());

							psDeleteDetails.executeUpdate();
							bulkMstrDTO = new BulkUploadDetailsDTO();

							dto1.setMessage("Details deleted successfully");

							returnBulkMstrDTO.add(dto1);
							continue;

						}

						else {
							dto1.setMessage("Details could not be deleted.");
							returnBulkMstrDTO.add(dto1);
							continue;
						}
					}

				}

				catch (Exception e) {
					// dto1 to be updated in returnBulkMstrDTO
					BulkUploadDetailsDTO bulkDto = new BulkUploadDetailsDTO();
					bulkDto.setMessage("Details not uploaded.");
					returnBulkMstrDTO.add(bulkDto);
					e.printStackTrace();
				}

			} // END Iterator

		}

		catch (DAOException e) {
			logger.error("Error while uploading Coordinator details::", e);
			e.printStackTrace();
		}

		catch (SQLException e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				DBConnection.releaseResources(con, psInsertDetails, null);
				DBConnection.releaseResources(con, psDeleteDetails, null);
			} catch (Exception e) {
				logger.error("Error while uploading::", e);
				throw new DAOException(e.getMessage(), e);
			}
		}

		return returnBulkMstrDTO;
	}

	@Override
	public ArrayList<BulkUploadDetailsDTO> bulkDistDeatils(
			ArrayList<BulkUploadDetailsDTO> listBulkDto, KmUserMstr userBean)
			throws DAOException {

		Connection con = null;
		PreparedStatement psInsertDetails = null;
		PreparedStatement psDeleteDetails = null;
		ArrayList<BulkUploadDetailsDTO> returnBulkMstrDTO = new ArrayList<BulkUploadDetailsDTO>();
		logger.info("inside bulkDistDeatils method:::::");

		try {
			BulkUploadDetailsDTO bulkMstrDTO = new BulkUploadDetailsDTO();
			con = DBConnection.getDBConnection();

			psInsertDetails = con
					.prepareStatement("INSERT INTO KM_DIST_DETAILS (PINCODE,STATE_NAME,CITY_NAME,PIN_CATEGORY,AREA,HUB,CIRCLE,SFANDSSDNAME,SFANDSSDMAILID,SFANDSSDCONTACTNO,TSM_NAME,TSM_MAIL_ID,TSM_CONTACT_NO,SR_MANAGER,SR_MANAGER_MAIL_ID,SR_MANAGER_CONTACT_NO,ASM_NAME,ASM_MAIL_ID,ASM_CONTACT_NO,CREATED_DT,CREATED_BY,STATUS,SRNO) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,current timestamp,?,'A',NEXT VALUE FOR KM_DIST_SEQ) WITH UR");

			psDeleteDetails = con
					.prepareStatement("UPDATE KM_DIST_DETAILS SET STATUS='D', UPDATED_DT=current timestamp, UPDATED_BY=? WHERE PINCODE=? and STATE_NAME=? and  CITY_NAME=? and PIN_CATEGORY=? and AREA=? and HUB=? and CIRCLE=? and SFANDSSDNAME=? and SFANDSSDMAILID=? and SFANDSSDCONTACTNO=? and TSM_NAME=? and TSM_MAIL_ID=? and TSM_CONTACT_NO=? and SR_MANAGER=? and SR_MANAGER_MAIL_ID=? and SR_MANAGER_CONTACT_NO=? and ASM_NAME=? and ASM_MAIL_ID=? and ASM_CONTACT_NO=? and STATUS='A' WITH UR");

			for (Iterator<BulkUploadDetailsDTO> itr = listBulkDto.iterator(); itr
					.hasNext();) {
				try {
					BulkUploadDetailsDTO dto1;
					dto1 = (BulkUploadDetailsDTO) itr.next();

					if (!(dto1.getAction().equalsIgnoreCase("c") || dto1
							.getAction().equalsIgnoreCase("d"))) {
						logger
								.info("Enter either c or d for CREATION/DELETION |");
						dto1
								.setMessage("Enter either c or d for CREATION/DELETION | ");
						dto1.setErrFlag(true);
						returnBulkMstrDTO.add(dto1);
						continue;
					}
					logger.info(" $$$$$$$$$$$$$$$$  "+dto1.getPinCode());
					if (dto1.getPinCode() == null || dto1.getState() == null || dto1.getCity() == null || dto1.getPinCategory() == null	|| dto1.getArea() == null
							|| dto1.getHub() == null || dto1.getCircle() == null ||dto1.getSfssdName() == null ||dto1.getSfssdMail() == null ||dto1.getSfssdContact() == null
							||dto1.getTsm() == null ||dto1.getTsmMail() == null	||dto1.getTsmContact() == null ||dto1.getSrManager() == null ||dto1.getSrManagerMail() == null
							||dto1.getSrManagerContact() == null ||dto1.getAsm() == null	||dto1.getAsmMail() == null ||dto1.getAsmContact() == null) {

						logger
								.info("Please fill all the required details |");
						dto1
								.setMessage("Please fill all the required details | ");
						dto1.setErrFlag(true);
						returnBulkMstrDTO.add(dto1);
						continue;
					}
				
					if (dto1.isErrFlag()) {
						/*
						 * if(!(dto1.getAction().equalsIgnoreCase("c")) ||
						 * !(dto1.getAction().equalsIgnoreCase("d") ) ) {
						 * dto1.setMessage
						 * ("Enter either c or d for CREATION/DELETION | ");
						 * logger.info("asa:::::enter c or d");
						 * dto1.setErrFlag(true); returnBulkMstrDTO.add(dto1);
						 * continue; }
						 */

						if (!(dto1.getAction().equalsIgnoreCase("c") || dto1
								.getAction().equalsIgnoreCase("d"))) {
							dto1
									.setMessage("Enter either c or d for CREATION/DELETION | ");
							dto1.setErrFlag(true);
							returnBulkMstrDTO.add(dto1);
							continue;
						}

						// validations to be added for blank fields

						/*
						 * if(dto1.getRsuCode().equals("") ||
						 * dto1.getCityZoneCode().equals("")) {dto1.setMessage(
						 * "RSU Code and CityZone Code are mandatory fields |");
						 * dto1.setErrFlag(true); returnBulkMstrDTO.add(dto1);
						 * continue; }
						 */
					}

					else if (!dto1.isErrFlag()
							&& dto1.getAction().equalsIgnoreCase("c")) {

						// validation to be added for duplicate records

						if (isDuplicateDetail(dto1)) {
							logger
									.info("in dupliacte Details check::::::::::::::::::::::::::::");
							dto1.setMessage("Details Already Exists.");
							dto1.setErrFlag(true);
							returnBulkMstrDTO.add(dto1);
							continue;
						}

						else

						{
							psInsertDetails.setString(1, dto1.getPinCode());
							psInsertDetails.setString(2, dto1.getState()
									.toUpperCase());
							psInsertDetails.setString(3, dto1.getCity()
									.toUpperCase());
							psInsertDetails.setString(4, dto1.getPinCategory()
									.toUpperCase());
							psInsertDetails.setString(5, dto1.getArea()
									.toUpperCase());
							psInsertDetails.setString(6, dto1.getHub()
									.toUpperCase());
							psInsertDetails.setString(7, dto1.getCircle()
									.toUpperCase());
							psInsertDetails.setString(8, dto1.getSfssdName()
									.toUpperCase());
							psInsertDetails.setString(9, dto1.getSfssdMail());
							psInsertDetails.setString(10, dto1
									.getSfssdContact());
							psInsertDetails.setString(11, dto1.getTsm()
									.toUpperCase());
							psInsertDetails.setString(12, dto1.getTsmMail());
							psInsertDetails.setString(13, dto1.getTsmContact());
							psInsertDetails.setString(14, dto1.getSrManager()
									.toUpperCase());
							psInsertDetails.setString(15, dto1
									.getSrManagerMail());
							psInsertDetails.setString(16, dto1
									.getSrManagerContact());
							psInsertDetails.setString(17, dto1.getAsm()
									.toUpperCase());
							psInsertDetails.setString(18, dto1.getAsmMail());
							psInsertDetails.setString(19, dto1.getAsmContact());
							psInsertDetails.setString(20, userBean
									.getUserLoginId());

							psInsertDetails.executeUpdate();
							dto1.setMessage("Details Inserted Successfully.");

							returnBulkMstrDTO.add(dto1);
							continue;
						}
					}

					else if (!dto1.isErrFlag()
							&& dto1.getAction().equalsIgnoreCase("d")) {

						if (!isDuplicateDetail(dto1)) {
							dto1.setMessage("Details Doesnt Exist.");
							dto1.setErrFlag(true);
							returnBulkMstrDTO.add(dto1);
							continue;
						}

						else if (isDuplicateDetail(dto1)) {

							psDeleteDetails.setString(1, userBean
									.getUserLoginId());
							psDeleteDetails.setString(2, dto1.getPinCode());
							psDeleteDetails.setString(3, dto1.getState()
									.toUpperCase());
							psDeleteDetails.setString(4, dto1.getCity()
									.toUpperCase());
							psDeleteDetails.setString(5, dto1.getPinCategory()
									.toUpperCase());
							psDeleteDetails.setString(6, dto1.getArea()
									.toUpperCase());
							psDeleteDetails.setString(7, dto1.getHub()
									.toUpperCase());
							psDeleteDetails.setString(8, dto1.getCircle()
									.toUpperCase());
							psDeleteDetails.setString(9, dto1.getSfssdName()
									.toUpperCase());
							psDeleteDetails.setString(10, dto1.getSfssdMail());
							psDeleteDetails.setString(11, dto1
									.getSfssdContact());
							psDeleteDetails.setString(12, dto1.getTsm()
									.toUpperCase());
							psDeleteDetails.setString(13, dto1.getTsmMail());
							psDeleteDetails.setString(14, dto1.getTsmContact());
							psDeleteDetails.setString(15, dto1.getSrManager()
									.toUpperCase());
							psDeleteDetails.setString(16, dto1
									.getSrManagerMail());
							psDeleteDetails.setString(17, dto1
									.getSrManagerContact());
							psDeleteDetails.setString(18, dto1.getAsm()
									.toUpperCase());
							psDeleteDetails.setString(19, dto1.getAsmMail());
							psDeleteDetails.setString(20, dto1.getAsmContact());

							psDeleteDetails.executeUpdate();
							// bulkMstrDTO = new BulkUploadDetailsDTO();
							logger
									.info("exucution finished  ::::::::::::::::;");
							dto1.setMessage("Details deleted successfully");

							con.commit();
							logger.info("commiteedddd :::::::::::::;;;;");

							returnBulkMstrDTO.add(dto1);
							continue;

						}

						else {
							dto1.setMessage("Details could not be deleted.");
							logger
									.info("could not be delted  ::::::::::::::::::::;;");
							returnBulkMstrDTO.add(dto1);
							continue;
						}
					}

					// }
				}

				catch (Exception e) {
					// dto1 to be updated in returnBulkMstrDTO
					BulkUploadDetailsDTO bulkDto = new BulkUploadDetailsDTO();
					bulkDto.setMessage("Details not uploaded.");
					returnBulkMstrDTO.add(bulkDto);
					e.printStackTrace();
				}

			} // END Iterator

		}

		catch (DAOException e) {
			logger.error("Error while uploading dist details::", e);
			e.printStackTrace();
		}

		catch (SQLException e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {

				DBConnection.releaseResources(con, psInsertDetails, null);
				DBConnection.releaseResources(con, psDeleteDetails, null);
			} catch (Exception e) {
				logger.error("Error while uploading::", e);
				throw new DAOException(e.getMessage(), e);
			}
		}

		return returnBulkMstrDTO;

	}

	public boolean isDuplicateDetail(BulkUploadDetailsDTO dto1)
			throws DAOException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExist = false;

		String duplicateCheck = "SELECT * FROM KM_DIST_DETAILS WHERE PINCODE=? and STATE_NAME=? and CITY_NAME=? and PIN_CATEGORY=? and AREA=? and HUB=? and CIRCLE=? and SFANDSSDNAME=? and SFANDSSDMAILID=? and SFANDSSDCONTACTNO=? and TSM_NAME=? and TSM_MAIL_ID=? and TSM_CONTACT_NO=? and SR_MANAGER=? and SR_MANAGER_MAIL_ID=? and SR_MANAGER_CONTACT_NO=? and ASM_NAME=? and ASM_MAIL_ID=? and ASM_CONTACT_NO=? and STATUS='A' WITH UR";

		try {
			con = DBConnection.getDBConnection();

			ps = con.prepareStatement(duplicateCheck);

			ps.setString(1, dto1.getPinCode());
			ps.setString(2, dto1.getState().toUpperCase());
			ps.setString(3, dto1.getCity().toUpperCase());
			ps.setString(4, dto1.getPinCategory().toUpperCase());
			ps.setString(5, dto1.getArea().toUpperCase());
			ps.setString(6, dto1.getHub().toUpperCase());
			ps.setString(7, dto1.getCircle().toUpperCase());
			ps.setString(8, dto1.getSfssdName().toUpperCase());
			ps.setString(9, dto1.getSfssdMail());
			ps.setString(10, dto1.getSfssdContact());
			ps.setString(11, dto1.getTsm().toUpperCase());
			ps.setString(12, dto1.getTsmMail());
			ps.setString(13, dto1.getTsmContact());
			ps.setString(14, dto1.getSrManager().toUpperCase());
			ps.setString(15, dto1.getSrManagerMail());
			ps.setString(16, dto1.getSrManagerContact());
			ps.setString(17, dto1.getAsm().toUpperCase());
			ps.setString(18, dto1.getAsmMail());
			ps.setString(19, dto1.getAsmContact());

			rs = ps.executeQuery();

			if (rs == null) {
				logger.info("No duplicate Details");
				isExist = false;
			}

			else if (rs.next()) {
				logger.info("Details exists");
				isExist = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				DBConnection.releaseResources(con, ps, rs);
			} catch (Exception e) {
				throw new DAOException(e.getMessage(), e);
			}
		}
		return isExist;
	}

	public boolean isDuplicateARC(BulkUploadDetailsDTO dto1)
			throws DAOException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExist = false;

		String duplicateCheck = "SELECT * FROM KM_ARC_DETAILS WHERE CIRCLE_NAME=? and  CITY_NAME=? and  ARC_STORE=? and  PINCODE=? and  ADDRESS=? and  TIMING=? and  AVAILABILITY_OF_CCDC_MACHINE=? and  AVAILABILITY_OF_DOCTOR_SIM=? and  ARCOR=? and  ZONE=? and  STATUS='A' WITH UR";

		try {
			con = DBConnection.getDBConnection();

			ps = con.prepareStatement(duplicateCheck);

			ps.setString(1, dto1.getCircle().toUpperCase());
			ps.setString(2, dto1.getCity().toUpperCase());
			ps.setString(3, dto1.getArc().toUpperCase());
			ps.setString(4, dto1.getPinCode());
			ps.setString(5, dto1.getAddress().toUpperCase());
			ps.setString(6, dto1.getTimings());
			ps.setString(7, dto1.getAvailabilityMc().toUpperCase());
			ps.setString(8, dto1.getAvailabilitySim().toUpperCase());
			ps.setString(9, dto1.getArcOr().toUpperCase());
			ps.setString(10, dto1.getZone().toUpperCase());

			rs = ps.executeQuery();

			if (rs == null) {
				logger.info("No duplicate Details");
				isExist = false;
			}

			else if (rs.next()) {
				logger.info("Details exists");
				isExist = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				DBConnection.releaseResources(con, ps, rs);
			} catch (Exception e) {
				throw new DAOException(e.getMessage(), e);
			}
		}
		return isExist;
	}

	public boolean isDuplicateCoordinatorDetails(BulkUploadDetailsDTO dto1)
			throws DAOException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExist = false;

		String duplicateCheck = "SELECT * FROM KM_COORDINATOR_DETAILS WHERE CIRCLE_NAME=? and  COMPANY_NAME=? and  COMPANY_SPOC_NAME=? and  COMPANY_SPOC_EMAILID=? and  COMPANY_SPOC_CONTACTNO=? and  RM_NAME=? and  RM_EMAIL_ID=? and  RM_CONTACTNO=? and  REQUESTOR=? and  REQUESTOR_LOCATION=? and  REQUESTOR_CONT_NO=? and  REQUESTED_DATE=?  and  STATUS='A' WITH UR";

		try {
			con = DBConnection.getDBConnection();

			ps = con.prepareStatement(duplicateCheck);

			ps.setString(1, dto1.getCircle().toUpperCase());
			ps.setString(2, dto1.getCompany().toUpperCase());
			ps.setString(3, dto1.getSpoc().toUpperCase());
			ps.setString(4, dto1.getSpocMail());
			ps.setString(5, dto1.getSpocPhone());
			ps.setString(6, dto1.getRm().toUpperCase());
			ps.setString(7, dto1.getRmMail());
			ps.setString(8, dto1.getRmPh());
			ps.setString(9, dto1.getRequestor().toUpperCase());
			ps.setString(10, dto1.getRequestorLoc().toUpperCase());
			ps.setString(11, dto1.getRequestorPh());
			ps.setString(12, dto1.getReqOn());

			rs = ps.executeQuery();

			if (rs == null) {
				logger.info("No duplicate Details");
				isExist = false;
			}

			else if (rs.next()) {
				logger.info("Details exists");
				isExist = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				DBConnection.releaseResources(con, ps, rs);
			} catch (Exception e) {
				throw new DAOException(e.getMessage(), e);
			}
		}
		return isExist;
	}

	public ArrayList<BulkUploadDetailsDTO> getPath() throws DAOException {
		logger.info("inside getPath method ::::");
		BulkUploadDetailsDTO bulkUploadDetailsDTO = null;
		ArrayList<BulkUploadDetailsDTO> bulkUploadList = new ArrayList<BulkUploadDetailsDTO>();
		String path = "";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String selectPath = "SELECT PATH,FLAG FROM KM_PATH_MSTR with ur";

		try {
			con = DBConnection.getDBConnection();
			ps = con.prepareStatement(selectPath);
			rs = ps.executeQuery();

			if (rs == null) {
				logger.info("asa::Path not found");

			}

			while (rs.next()) {
				bulkUploadDetailsDTO = new BulkUploadDetailsDTO();
				bulkUploadDetailsDTO.setBulkUploadPath(rs.getString("PATH"));
				bulkUploadDetailsDTO.setBulkUploadflag(rs.getString("FLAG"));

				logger.info("asa:::bulkUploadPath::DB::"
						+ bulkUploadDetailsDTO.getBulkUploadPath());
				logger.info("asa:::bulkUploadFlag::DB::"
						+ bulkUploadDetailsDTO.getBulkUploadflag());
				logger.info("asa:::Path found");
				bulkUploadList.add(bulkUploadDetailsDTO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error while fetching path for Upload Details", e);
		} finally {
			try {
				DBConnection.releaseResources(con, ps, rs);
			} catch (Exception e) {
				logger.error("Error while fetching path for Upload details::",
						e);
				throw new DAOException(e.getMessage(), e);
			}
		}

		return bulkUploadList;
	}

	public ArrayList<BulkUploadDetailsDTO> bulkVasDetails(ArrayList<BulkUploadDetailsDTO> listBulkDto, KmUserMstr userBean)	throws DAOException 
	{

		Connection con = null;
		PreparedStatement psInsertDetails = null;
		PreparedStatement psDeleteDetails = null;
		ArrayList<BulkUploadDetailsDTO> returnBulkMstrDTO = new ArrayList<BulkUploadDetailsDTO>();
		logger.debug("Inside bulkVasDetails");
		try {
			BulkUploadDetailsDTO bulkMstrDTO = new BulkUploadDetailsDTO();
			con = DBConnection.getDBConnection();

			psInsertDetails = con.prepareStatement("INSERT INTO KM_VAS_DETAIL(VASNAME, ACTIVATIONCODE, DEACTIVATIONCODE, CHARGES, BENEFITS, CONSTRUCT,SRNO,STATUS,CREATED_DT,CREATED_BY) VALUES(? , ? , ? , ? , ? , ? , NEXT VALUE FOR KM_VAS_SEQ,'A',current timestamp,? ) WITH UR");
			
			psDeleteDetails = con.prepareStatement("UPDATE KM_VAS_DETAIL SET STATUS='D', UPDATED_DT=current timestamp, UPDATED_BY=? WHERE VASNAME=? and ACTIVATIONCODE=? and  DEACTIVATIONCODE=? and CHARGES=? and BENEFITS=? and CONSTRUCT=? and STATUS='A' WITH UR");

			
			
			for (Iterator<BulkUploadDetailsDTO> itr = listBulkDto.iterator(); itr
					.hasNext();) {
				try {
					BulkUploadDetailsDTO dto1;
					dto1 = (BulkUploadDetailsDTO) itr.next();
					
					if (!(dto1.getAction().equalsIgnoreCase("c") || dto1.getAction().equalsIgnoreCase("d"))) {
						logger.info("Enter either c or d for CREATION/DELETION |");
						dto1.setMessage("Enter either c or d for CREATION/DELETION | ");
						dto1.setErrFlag(true);
						returnBulkMstrDTO.add(dto1);
						continue;
					}
					
					logger.info("asa:::dto1.getDeactivationCode()::"+dto1.getDeactivationCode());
					if(dto1.getVasName()==null || dto1.getActivationCode()==null || dto1.getDeactivationCode()==null || dto1.getCharges()==null|| dto1.getBenefits()==null|| dto1.getConstruct()==null)
					{
						
						logger.info("Please fill all the required details |");
						dto1.setMessage("Please fill all the required details | ");
						dto1.setErrFlag(true);
						returnBulkMstrDTO.add(dto1);
						continue;
					}
					
					if (dto1.isErrFlag()) {
						
						
						logger.info("asa:::isErrFlag");
						/*
						 * if(!(dto1.getAction().equalsIgnoreCase("c") ||
						 * dto1.getAction().equalsIgnoreCase("d") ) ) {
						 * dto1.setMessage
						 * ("Enter either c or d for CREATION/DELETION | ");
						 * dto1.setErrFlag(true); returnBulkMstrDTO.add(dto1);
						 * continue; }
						 */

						// validations to be added for blank fields
						/*
						 * if(dto1.getRsuCode().equals("") ||
						 * dto1.getCityZoneCode().equals("")) {dto1.setMessage(
						 * "RSU Code and CityZone Code are mandatory fields |");
						 * dto1.setErrFlag(true); returnBulkMstrDTO.add(dto1);
						 * continue; }
						 */
					}

					else if (!dto1.isErrFlag() && dto1.getAction().equalsIgnoreCase("c")) {


					// validation to be added for duplicate records
					if (isDuplicateVAS(dto1)) {
							logger.info("in dupliacte Vas check::::::::::::::::::::::::::::");
							dto1.setMessage("VAS details Already Exists.");
							dto1.setErrFlag(true);
							returnBulkMstrDTO.add(dto1);
							continue;
						}

						else {
								logger.debug("Inserting VAS Details");
							psInsertDetails.setString(1, dto1.getVasName().toUpperCase());
							psInsertDetails.setString(2, dto1.getActivationCode().toUpperCase());
							psInsertDetails.setString(3, dto1.getDeactivationCode().toUpperCase());
							psInsertDetails.setInt(4, Integer.parseInt(dto1.getCharges()));
							psInsertDetails.setString(5, dto1.getBenefits().toUpperCase());
							psInsertDetails.setString(6, dto1.getConstruct().toUpperCase());
							psInsertDetails.setString(7, userBean.getUserLoginId());
							psInsertDetails.executeUpdate();
							dto1.setMessage("Details Inserted Successfully.");
							returnBulkMstrDTO.add(dto1);
							continue;
						}
					}
					
					else if (!dto1.isErrFlag() && dto1.getAction().equalsIgnoreCase("d")) {

						if (!isDuplicateVAS(dto1)) {
							dto1.setMessage("Details Doesnt Exist.");
							dto1.setErrFlag(true);
							returnBulkMstrDTO.add(dto1);
							continue;
						}

						else if (isDuplicateVAS(dto1)) {

							psDeleteDetails.setString(1, userBean.getUserLoginId());
							psDeleteDetails.setString(2,  dto1.getVasName().toUpperCase());
							psDeleteDetails.setString(3, dto1.getActivationCode().toUpperCase());
							psDeleteDetails.setString(4, dto1.getDeactivationCode().toUpperCase());
							psDeleteDetails.setInt(5, Integer.parseInt(dto1.getCharges()));
							psDeleteDetails.setString(6, dto1.getBenefits().toUpperCase());
							psDeleteDetails.setString(7, dto1.getConstruct().toUpperCase());

							psDeleteDetails.executeUpdate();
							bulkMstrDTO = new BulkUploadDetailsDTO();

							dto1.setMessage("Details deleted successfully");

							returnBulkMstrDTO.add(dto1);
							continue;

						}

						else {
							dto1.setMessage("Details could not be deleted.");
							returnBulkMstrDTO.add(dto1);
							continue;
						}
					}
					
				}

				catch (Exception e) {
					// dto1 to be updated in returnBulkMstrDTO
					BulkUploadDetailsDTO bulkDto = new BulkUploadDetailsDTO();
					bulkDto.setMessage("Details not uploaded.");
					returnBulkMstrDTO.add(bulkDto);
					e.printStackTrace();
				}

			} // END Iterator

		}

		catch (DAOException e) {
			logger.error("Error while uploading VAS details::", e);
			e.printStackTrace();
		}

		catch (SQLException e) {
			e.printStackTrace();
			logger.error("Error while uploading::", e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				DBConnection.releaseResources(con, psInsertDetails, null);
				DBConnection.releaseResources(con, psDeleteDetails, null);
			} catch (Exception e) {
				throw new DAOException(e.getMessage(), e);
			}
		}
		
		for(int x = 0; x<returnBulkMstrDTO.size();x++ )
		{
			
		}
		logger.debug("################## inserted  VAS Details");
		return returnBulkMstrDTO;

	}

	public boolean isDuplicateVAS(BulkUploadDetailsDTO dto1)
			throws DAOException {
			logger.debug("Checking duplicate");
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExist = false;

		String duplicateCheck = "SELECT * FROM KM_VAS_DETAIL WHERE VASNAME=? and  ACTIVATIONCODE=? and  DEACTIVATIONCODE=? and  CHARGES=? and  BENEFITS=? and  CONSTRUCT=? and STATUS='A' WITH UR";
		try {
			con = DBConnection.getDBConnection();

			ps = con.prepareStatement(duplicateCheck);

			ps.setString(1, dto1.getVasName().toUpperCase());
			ps.setString(2, dto1.getActivationCode().toUpperCase());
			ps.setString(3, dto1.getDeactivationCode().toUpperCase());
			ps.setInt(4, Integer.parseInt(dto1.getCharges()));
			ps.setString(5, dto1.getBenefits().toUpperCase());
			ps.setString(6, dto1.getConstruct().toUpperCase());

			rs = ps.executeQuery();

			if (rs == null) {
				logger.info("No duplicate Details");
				isExist = false;
			}

			else if (rs.next()) {
				logger.info("Details exists");
				isExist = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				DBConnection.releaseResources(con, ps, rs);
			} catch (Exception e) {
				throw new DAOException(e.getMessage(), e);
			}
		}
		logger.debug("Record Exists :"+isExist);
		return isExist;
	}

}
