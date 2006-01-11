package com.ibm.km.services.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts.upload.FormFile;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.ibm.appsecure.exception.ValidationException;
import com.ibm.appsecure.service.GSDService;
import com.ibm.appsecure.util.Encryption;
import com.ibm.appsecure.util.IEncryption;
import org.apache.poi.ss.usermodel.Workbook;

import com.ibm.km.actions.BulkUploadDetailsAction;
import com.ibm.km.common.Constants;
import com.ibm.km.common.PropertyReader;
import com.ibm.km.dao.BulkUploadDetailsDao;
import com.ibm.km.dao.BulkUserDao;
import com.ibm.km.dao.DBConnection;
import com.ibm.km.dao.KmUserMstrDao;
import com.ibm.km.dao.impl.BulkUploadDetailsDaoImpl;
import com.ibm.km.dao.impl.BulkUserDaoImpl;
import com.ibm.km.dao.impl.KmUserMstrDaoImpl;
import com.ibm.km.dto.BulkMsgDto;
import com.ibm.km.dto.BulkUploadDetailsDTO;
import com.ibm.km.dto.KmFileDto;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;
import com.ibm.km.exception.KmUserMstrDaoException;
import com.ibm.km.forms.BulkUploadDetailsFormBean;
import com.ibm.km.services.BulkUploadDetailsService;
import com.ibm.km.services.BulkUserService;
import com.ibm.km.services.KmUserMstrService;

/**
 * @author Aman
 */
public class BulkUploadDetailsServiceImpl implements BulkUploadDetailsService {

	private static Logger logger = Logger
			.getLogger(BulkUploadDetailsServiceImpl.class.getName());

	public BulkMsgDto uploadMstr(FormFile myfile,
			BulkUploadDetailsFormBean formBean, KmUserMstr userBean)
			throws FileNotFoundException, KmException {
		BulkUploadDetailsDao dao = new BulkUploadDetailsDaoImpl();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH-mm-ss");
		String date = sdf.format(new java.util.Date());
		File newFile = null;
		BulkMsgDto bulkMsgDto = new BulkMsgDto();
		// BulkUploadDetailsDTO bulkUploadDetailsDTO= new
		// BulkUploadDetailsDTO();
		ResourceBundle bundle = ResourceBundle
				.getBundle("ApplicationResources");

		// String logFilePath =
		// PropertyReader.getAppValue("km.upload.file.logpath");
		String logFilePath = bundle.getString("csv.path");
		ArrayList<BulkUploadDetailsDTO> listBulkDto = new ArrayList<BulkUploadDetailsDTO>();
		ArrayList<BulkUploadDetailsDTO> listPath = new ArrayList<BulkUploadDetailsDTO>();// added
		InputStream inp = null;																							// by
																						// aman
																					// for
																							// fetching
																							// path
																							// from
																							// DB
		BulkUploadDetailsDao bulkDao = new BulkUploadDetailsDaoImpl();
		// String uploadType=formBean.getUploadType();
		String uploadPath = "";
		String flag = "";
		String path = "";

		try {
			byte[] fileData = myfile.getFileData();

			// added by aman for fetching path from DB(19/11/14)

			logger.info("fetching path and flag");
			listPath = getPath();
			logger.info("fetched path and flag");

			/*
			 * BulkUploadDetailsDTO dto1 = null;
			 * uploadPath=dto1.getBulkUploadPath();
			 * flag=dto1.getBulkUploadPath();
			 * 
			 * logger.info("asa:::::uploadPath::"+uploadPath);
			 * logger.info("asa:::::flag::"+flag);
			 */

			for (Iterator<BulkUploadDetailsDTO> itr = listPath.iterator(); itr
					.hasNext();) {
				try {
					BulkUploadDetailsDTO dto1;
					dto1 = (BulkUploadDetailsDTO) itr.next();
					uploadPath = dto1.getBulkUploadPath();
					flag = dto1.getBulkUploadflag();
					logger.info("asa:::::uploadPath::" + uploadPath);
					logger.info("asa:::::flag::" + flag);

				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Error while fetching path and flag", e);
				}
			}

			// String path= PropertyReader.getAppValue("path.uploadedTempFile")+
			// new java.util.Date().getTime()+"_"+myfile.getFileName() ;

			if (flag.equalsIgnoreCase("Y")) {
				logger.info("setting path as per DB");
				// String filePath = bundle.getString("csv.path") + "/" +
				// fileName;
				path = uploadPath + new java.util.Date().getTime() + "_"
						+ myfile.getFileName();
				// path= uploadPath +myfile.getFileName() ;
				logger.info("path" + path);
			} else if (flag.equalsIgnoreCase("N")) {
				logger.info("setting path as per properties file");
				path = bundle.getString("csv.path") + "/"
						+ new java.util.Date().getTime() + "_"
						+ myfile.getFileName();
				// path= bundle.getString("csv.path")+ "/" +myfile.getFileName()
				// ;
				logger.info("path" + path);
			} else {
				logger.info("setting path as per DB:::in else block");
				path = uploadPath + myfile.getFileName();
				logger.info("path" + path);
			}

			// end of changes by aman

			newFile = new File(path);

			String fileName = myfile.getFileName();
			String fileExtn = fileName.substring(fileName.indexOf(".") + 1);
			fileName = fileName.substring(0, fileName.indexOf("."));
			fileName = fileName + "_" + date + "." + fileExtn;

			RandomAccessFile raf = new RandomAccessFile(newFile, "rw");
			raf.write(fileData);
			raf.close();

			inp = new FileInputStream(path);// check
			Workbook wb_xssf; // Declare XSSF WorkBook
			Workbook wb_hssf; // Declare HSSF WorkBook
			Sheet sheet = null;

			if (!(fileExtn.equalsIgnoreCase("xlsx") || fileExtn
					.equalsIgnoreCase("xls"))) {
				bulkMsgDto.setMessage(bundle
						.getString("km.bulk.upload.invalid.excel"));
				return bulkMsgDto;

			}

			if (fileExtn.equalsIgnoreCase("xlsx")) {
				wb_xssf = new XSSFWorkbook(path);
				System.out.println("xlsx=" + wb_xssf.getSheetName(0));
				sheet = wb_xssf.getSheetAt(0);
			}
			if (fileExtn.equalsIgnoreCase("xls")) {
				POIFSFileSystem fs = new POIFSFileSystem(inp);
				wb_hssf = new HSSFWorkbook(fs);
				System.out.println("xls=" + wb_hssf.getSheetName(0));
				sheet = wb_hssf.getSheetAt(0);
			}
		
			logger.info("asa::last row no:::" + sheet.getLastRowNum());
			Iterator rows = sheet.rowIterator();
			int totalrows = sheet.getLastRowNum() + 1;

			int rowNumber = -1;

			if (totalrows < 3) {
				System.out.println("row empty");

				bulkMsgDto.setMessage(bundle
						.getString("BULK_UPLOAD_BLANK_EXCEL"));
				return bulkMsgDto;
			}

			while (rows.hasNext()) {

				Row row = (Row) rows.next();

				logger.info(" row.getPhysicalNumberOfCells() "
						+ row.getPhysicalNumberOfCells());
				logger.info("totalrows::::::::::::::::::" + totalrows);
				rowNumber = row.getRowNum();

				if (rowNumber == 0) {
					if (formBean.getDocType().equals("1")) // dist details
					{
						if (row.getPhysicalNumberOfCells() != Constants.DIST_UPLOAD) {
							bulkMsgDto.setMessage("Invalid Excel");
							bulkMsgDto
									.setMsgId(Constants.BULK_UPLOAD_INVALID_EXCEL);
							return bulkMsgDto;
						} else {
							Iterator cells = row.cellIterator();
							int counter = 0;
							while (cells.hasNext()) {
								Cell cell = (Cell) cells.next();
								String cellValue = null;
								switch (cell.getCellType()) {
								case Cell.CELL_TYPE_NUMERIC:
									cellValue = String.valueOf((long) cell
											.getNumericCellValue());
									break;
								case Cell.CELL_TYPE_STRING:
									cellValue = cell.getStringCellValue();
									break;
								}
								if (cellValue == null) {
									bulkMsgDto
											.setMessage("Header(s) are not correct !!");
									bulkMsgDto
											.setMsgId(Constants.BULK_UPLOAD_INVALID_HEADER);
									return bulkMsgDto;
								}

								if (counter == 0
										&& !cellValue.equals("Pin_code")) {
									bulkMsgDto
											.setMessage("Header(s) are not correct !!");
									bulkMsgDto
											.setMsgId(Constants.BULK_UPLOAD_INVALID_HEADER);
									return bulkMsgDto;
								}
								counter++;
							}
						}
					}

					else if (formBean.getDocType().equals("2")) // arc
					{
						if (row.getPhysicalNumberOfCells() != Constants.ARC_UPLOAD) {
							logger
									.info("row.getPhysicalNumberOfCells()::::::::"
											+ row.getPhysicalNumberOfCells());
							logger.info("Invalid excel");
							bulkMsgDto.setMessage("Invalid Excel");
							bulkMsgDto
									.setMsgId(Constants.BULK_UPLOAD_INVALID_EXCEL);
							return bulkMsgDto;
						} else {
							Iterator cells = row.cellIterator();
							int counter = 0;
							while (cells.hasNext()) {
								Cell cell = (Cell) cells.next();
								String cellValue = null;
								switch (cell.getCellType()) {
								case Cell.CELL_TYPE_NUMERIC:
									cellValue = String.valueOf((long) cell
											.getNumericCellValue());
									break;
								case Cell.CELL_TYPE_STRING:
									cellValue = cell.getStringCellValue();
									break;
								}
								if (cellValue == null) {
									bulkMsgDto
											.setMessage("Header(s) are not correct !!");
									bulkMsgDto
											.setMsgId(Constants.BULK_UPLOAD_INVALID_HEADER);
									return bulkMsgDto;
								}

								if (counter == 0 && !cellValue.equals("Circle")) {
									bulkMsgDto
											.setMessage("Header(s) are not correct !!");
									bulkMsgDto
											.setMsgId(Constants.BULK_UPLOAD_INVALID_HEADER);
									return bulkMsgDto;
								}
								counter++;
							}
						}
					} else if (formBean.getDocType().equals("3"))// coordinator
																	// list
					{
						if (row.getPhysicalNumberOfCells() != Constants.COORDINATOR_UPLOAD) {
							logger
									.info("row.getPhysicalNumberOfCells()::::::::"
											+ row.getPhysicalNumberOfCells());
							logger.info("Invalid excel");
							bulkMsgDto.setMessage("Invalid Excel");
							bulkMsgDto
									.setMsgId(Constants.BULK_UPLOAD_INVALID_EXCEL);
							return bulkMsgDto;
						} else {
							int counter = 0;
							Iterator cells = row.cellIterator();
							while (cells.hasNext()) {
								Cell cell = (Cell) cells.next();
								String cellValue = null;
								switch (cell.getCellType()) {
								case Cell.CELL_TYPE_NUMERIC:
									cellValue = String.valueOf((long) cell
											.getNumericCellValue());
									break;
								case Cell.CELL_TYPE_STRING:
									cellValue = cell.getStringCellValue();
									break;
								}

								if (cellValue == null) {
									bulkMsgDto
											.setMessage("Header(s) are not correct !!");
									bulkMsgDto
											.setMsgId(Constants.BULK_UPLOAD_INVALID_HEADER);
									return bulkMsgDto;
								}
								if (counter == 0 && !cellValue.equals("Circle")) {
									bulkMsgDto
											.setMessage("Header(s) are not correct !!");
									bulkMsgDto
											.setMsgId(Constants.BULK_UPLOAD_INVALID_HEADER);
									return bulkMsgDto;
								}
								counter++;
							}
						}
					} else if (formBean.getDocType().equals("4")) // vas details
					{
						if (row.getPhysicalNumberOfCells() != Constants.VAS_UPLOAD) {
							logger
									.info("row.getPhysicalNumberOfCells()::::::::"
											+ row.getPhysicalNumberOfCells());
							logger.info("Invalid excel");
							bulkMsgDto.setMessage("Invalid Excel");
							bulkMsgDto
									.setMsgId(Constants.BULK_UPLOAD_INVALID_EXCEL);
							return bulkMsgDto;
						} else {
							Iterator cells = row.cellIterator();
							int counter = 0;
							while (cells.hasNext()) {
								Cell cell = (Cell) cells.next();
								String cellValue = null;
								switch (cell.getCellType()) {
								case Cell.CELL_TYPE_NUMERIC:
									cellValue = String.valueOf((long) cell
											.getNumericCellValue());
									break;
								case Cell.CELL_TYPE_STRING:
									cellValue = cell.getStringCellValue();
									break;
								}
								if (cellValue == null) {
									bulkMsgDto
											.setMessage("Header(s) are not correct !!");
									bulkMsgDto
											.setMsgId(Constants.BULK_UPLOAD_INVALID_HEADER);
									return bulkMsgDto;
								}

								/*if (counter == 0 && !cellValue.equals("Circle")) {
									bulkMsgDto
											.setMessage("Header(s) are not correct !!");
									bulkMsgDto
											.setMsgId(Constants.BULK_UPLOAD_INVALID_HEADER);
									return bulkMsgDto;
								}*/
								counter++;
							}
						}
					}

					// else
					if (totalrows == 2) {
						bulkMsgDto.setMessage("Blank Excel");
						bulkMsgDto.setMsgId(Constants.BULK_UPLOAD_BLANK_EXCEL);
						return bulkMsgDto;
					} else
						continue;
				}

				if (rowNumber > 0) // Starting parsing excel after 2nd row
				{
					Iterator cells = row.cellIterator();
					BulkUploadDetailsDTO bulkUploadDetailsDTO = new BulkUploadDetailsDTO();
					int columnIndex = 0;
					int cellNo = 0;

					if (cells != null) {
						logger.info("cell is not null");

						while (cells.hasNext()) {
							cellNo++;
							Cell cell = (Cell) cells.next();
							columnIndex = cell.getColumnIndex();

							String cellValue = null;
							switch (cell.getCellType()) {
							case Cell.CELL_TYPE_NUMERIC:
								cellValue = String.valueOf((long) cell
										.getNumericCellValue());
								break;
							case Cell.CELL_TYPE_STRING:
								cellValue = cell.getStringCellValue();
								break;
							}
							if (cellValue != null) {
								cellValue = cellValue.trim();
							} else
								cellValue = "";

							// docType Check

							if (formBean.getDocType().equals("1")) // dist
																	// details
							{
								switch (columnIndex) {
								case 0:
									bulkUploadDetailsDTO.setPinCode(cellValue);
									break;
								case 1:
									bulkUploadDetailsDTO.setState(cellValue);
									break;
								case 2:
									bulkUploadDetailsDTO.setCity(cellValue);
									break;
								case 3:
									bulkUploadDetailsDTO
											.setPinCategory(cellValue);
									break;
								case 4:
									bulkUploadDetailsDTO.setArea(cellValue);
									break;
								case 5:
									bulkUploadDetailsDTO.setHub(cellValue);
									break;
								case 6:
									bulkUploadDetailsDTO.setCircle(cellValue);
									break;
								case 7:
									bulkUploadDetailsDTO
											.setSfssdName(cellValue);
									break;
								case 8:
									bulkUploadDetailsDTO
											.setSfssdMail(cellValue);
									break;
								case 9:
									bulkUploadDetailsDTO
											.setSfssdContact(cellValue);
									break;
								case 10:
									bulkUploadDetailsDTO.setTsm(cellValue);
									break;
								case 11:
									bulkUploadDetailsDTO.setTsmMail(cellValue);
									break;
								case 12:
									bulkUploadDetailsDTO
											.setTsmContact(cellValue);
									break;
								case 13:
									bulkUploadDetailsDTO
											.setSrManager(cellValue);
									break;
								case 14:
									bulkUploadDetailsDTO
											.setSrManagerMail(cellValue);
									break;
								case 15:
									bulkUploadDetailsDTO
											.setSrManagerContact(cellValue);
									break;
								case 16:
									bulkUploadDetailsDTO.setAsm(cellValue);
									break;
								case 17:
									bulkUploadDetailsDTO.setAsmMail(cellValue);
									break;
								case 18:
									bulkUploadDetailsDTO
											.setAsmContact(cellValue);
									break;
								case 19:
									bulkUploadDetailsDTO.setAction(cellValue);
									break;
								case 20:
									bulkUploadDetailsDTO.setAction(cellValue);
									break;

								}
							} else if (formBean.getDocType().equals("2")) // arc
							{
								switch (columnIndex) {
								case 0:
									bulkUploadDetailsDTO.setCircle(cellValue);
									break;
								case 1:
									bulkUploadDetailsDTO.setZone(cellValue);
									break;
								case 2:
									bulkUploadDetailsDTO.setArcOr(cellValue);
									break;
								case 3:
									bulkUploadDetailsDTO.setCity(cellValue);
									break;
								case 4:
									bulkUploadDetailsDTO.setArc(cellValue);
									break;
								case 5:
									bulkUploadDetailsDTO.setAddress(cellValue);
									break;
								case 6:
									bulkUploadDetailsDTO
									.setPinCode(cellValue);
									break;
								case 7:
									bulkUploadDetailsDTO.setTimings(cellValue);
									break;
								case 8:
									bulkUploadDetailsDTO
											.setAvailabilityMc(cellValue);
									break;
								case 9:
									bulkUploadDetailsDTO
									.setAvailabilitySim(cellValue);
									break;
								
									
								case 10:
									bulkUploadDetailsDTO.setAction(cellValue);
									break;

								}
							} else if (formBean.getDocType().equals("3")) // coordinator
																			// list
							{
								switch (columnIndex) {
								case 0:
									bulkUploadDetailsDTO.setCircle(cellValue);
									break;
								case 1:
									bulkUploadDetailsDTO.setCompany(cellValue);
									break;
								case 2:
									bulkUploadDetailsDTO.setSpoc(cellValue);
									break;
								case 3:
									bulkUploadDetailsDTO.setSpocMail(cellValue);
									break;
								case 4:
									bulkUploadDetailsDTO
											.setSpocPhone(cellValue);
									break;
								case 5:
									bulkUploadDetailsDTO.setRm(cellValue);
									break;
								case 6:
									bulkUploadDetailsDTO.setRmMail(cellValue);
									break;
								case 7:
									bulkUploadDetailsDTO.setRmPh(cellValue);
									break;
								case 8:
									bulkUploadDetailsDTO
											.setRequestor(cellValue);
									break;
								case 9:
									bulkUploadDetailsDTO
											.setRequestorLoc(cellValue);
									break;
								case 10:
									bulkUploadDetailsDTO
											.setRequestorPh(cellValue);
									break;
								case 11:
									bulkUploadDetailsDTO.setReqOn(cellValue);
									break;
								case 12:
									bulkUploadDetailsDTO.setAction(cellValue);
									break;
								}
							}else if (formBean.getDocType().equals("4")) // vas details
							{
								switch (columnIndex) {
								case 0:
									bulkUploadDetailsDTO.setVasName(cellValue);
									break;
								case 1:
									bulkUploadDetailsDTO.setActivationCode(cellValue);
									break;
								case 2:
									bulkUploadDetailsDTO.setDeactivationCode(cellValue);
									break;
								case 3:
									bulkUploadDetailsDTO.setCharges(cellValue);
									break;
								case 4:
									bulkUploadDetailsDTO.setBenefits(cellValue);
									break;
								case 5:
									bulkUploadDetailsDTO.setConstruct(cellValue);
									break;
								case 6:
									bulkUploadDetailsDTO.setAction(cellValue);
									break;
								}
							}
							
						}// Adding single row to a dto
						bulkUploadDetailsDTO.setRowNumber(rowNumber);
						// bulkDto = validateDto(bulkDto, mstrTypeSelected);
						listBulkDto.add(bulkUploadDetailsDTO);
						// inserting and deleting

					}

				}// parsing ends

			}
			if (formBean.getDocType().equals("1")) // dist details
			{
				logger.info("asa:::::dist upload:::service:::");
				listBulkDto = bulkDao.bulkDistDeatils(listBulkDto, userBean);
			}

			if (formBean.getDocType().equals("2")) // arc
			{
				logger.info("asa:::::arc upload:::service:::");
				listBulkDto = bulkDao.bulkARCDetails(listBulkDto, userBean);
			}
			if (formBean.getDocType().equals("3"))// coordinator
			{
				logger.info("asa:::::cordinator upload:::service:::");
				listBulkDto = bulkDao.bulkCoordinatorDetails(listBulkDto,
						userBean);
			}
			if (formBean.getDocType().equals("4")) // vas detailss
			{
				logger.info("asa:::::Vas Details upload:::service:::");
				listBulkDto = bulkDao.bulkVasDetails(listBulkDto, userBean);
			}

			SimpleDateFormat otdf = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
			// logFilePath = logFilePath+ "bulkUploadLog" + otdf.format(new
			// java.util.Date()) + ".csv" ;
			logFilePath = logFilePath + "/bulkUploadLog"
					+ otdf.format(new java.util.Date()) + ".csv";
			logger.info("logFilePath::::::" + logFilePath);

			String docType = formBean.getDocType();

			bulkMsgDto = writeLogs(listBulkDto, logFilePath, docType);

			bulkMsgDto.setMsgId(Constants.BULK_UPLOAD_SUCCESS);
			// PropertyReader.getAppValue("km.upload.file.logpath");

		}

		catch (FileNotFoundException e) {
			e.printStackTrace();

			bulkMsgDto.setMsgId(Constants.BULK_UPLOAD_FAIL);
			logger.info("Error occurred while finding the file");
			logger.error("Error while uploading::", e);
			throw new KmException("Exception Occurred while finding file.");
		}

		catch (Exception e) {
			e.printStackTrace();
			bulkMsgDto.setMsgId(Constants.BULK_UPLOAD_FAIL);
			logger.info("Error occurred while reading the file");
			logger.error("Error while uploading::", e);
			throw new KmException("Exception Occurred in uploading file");
		}

		finally {
			try {
				inp.close();
				newFile.delete();
			} catch (Exception e) {
				e.printStackTrace();
				// bulkMsgDto.setMsgId(Constants.BULK_UPLOAD_FAIL);
				logger.error("Error while uploading::", e);
				logger.info("Error while deleting the file");
			}
		}
		return bulkMsgDto;
	}

	public BulkMsgDto writeLogs(ArrayList<BulkUploadDetailsDTO> msgListBulkDto,
			String errLogFileName, String docType) throws KmException {
		boolean isError = false;
		BulkMsgDto bulkMsgDto = new BulkMsgDto();

		try {

			bulkMsgDto.setFilePath(errLogFileName);
			if (msgListBulkDto.size() > 0) {
				FileWriter fw = new FileWriter(errLogFileName);
				PrintWriter pw = new PrintWriter(fw);
				BulkUploadDetailsDTO bulkErrDto;

				pw.print("Row No");
				pw.print(",");
				pw.print("Upload Status");
				pw.println(",");
				Iterator iter = msgListBulkDto.iterator();

				while (iter.hasNext()) {

					bulkErrDto = (BulkUploadDetailsDTO) iter.next();
					pw.print(bulkErrDto.getRowNumber());
					pw.print(",");
					pw.print(bulkErrDto.getMessage());
					pw.println(",");

				}

				pw.flush(); // Flush the output to the file
				pw.close();
				fw.close();
			}

		} catch (IOException io) {
			io.printStackTrace();
			bulkMsgDto.setMessage("Error while File creation");
			logger.info("IO Exception occurred while writing logs to csv");
			logger.error("IO Exception occurred while writing logs to csv::",
					io);
			throw new KmException(
					"IO Exception occurred while writting logs to csv");
		} catch (Exception e) {
			e.printStackTrace();
			bulkMsgDto.setMessage("Error while File creation");
			logger.error(
					"Exception occurred while writing error logs to csv::", e);
			logger.info("Exception occurred while writing error logs to csv");
			throw new KmException(
					"Exception occurred while writing logs to csv");
		}

		return bulkMsgDto;

	}

	public ArrayList<BulkUploadDetailsDTO> getPath() throws KmException {
		String path = "";

		BulkUploadDetailsDao dao = new BulkUploadDetailsDaoImpl();
		ArrayList<BulkUploadDetailsDTO> bulkUploadList = new ArrayList<BulkUploadDetailsDTO>();
		try {
			bulkUploadList = dao.getPath();
		} catch (Exception e) {

			e.printStackTrace();
			logger.error("Error while fetching path::::getpath method::", e);
			throw new KmException("Error while fetching path::::getpath method");
		}
		return bulkUploadList;
	}

}
