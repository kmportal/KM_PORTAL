/**
	 * Created by Saanya for Self Care
*/
package com.ibm.km.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.ibm.km.common.Constants;
import com.ibm.km.dao.KmSchemesAndBenefitsDao;
import com.ibm.km.dao.impl.KmSchemesAndBenefitsDaoImpl;

import com.ibm.km.dto.KmSchemesAndBenefitsDto;
import com.ibm.km.forms.KmSchemesAndBenefitsDetailsFormBean;

public class KmDownloadSchemesAndBenefitsAction extends Action {

	private static Logger logger = Logger.getLogger(KmDownloadSchemesAndBenefitsAction.class.getName());
	
	/**
	 * This function is used for downloading excel
	 * 
	 * @param mapping
	 *            defines a path that is matched against the request URI of the
	 *            incoming request and usually specifies the fully qualified
	 *            class name of an Action class.
	 * @param form
	 *            class makes it easy to store and validate the data for your
	 *            application's input forms.
	 * @param request
	 *            This interface is for getting data from the client to service
	 *            the request
	 * @param response
	 *            Interface for sending MIME data to the client.
	 * 
	 * @return Forward
	 * @throws Exception
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionErrors errors = new ActionErrors();
		ActionForward forward = new ActionForward();
		ActionMessages messages= new ActionMessages();

		boolean status = true;

		try {
			KmSchemesAndBenefitsDetailsFormBean formBean = (KmSchemesAndBenefitsDetailsFormBean) form;
			KmSchemesAndBenefitsDto dto = new KmSchemesAndBenefitsDto();
			KmSchemesAndBenefitsDao dao = new KmSchemesAndBenefitsDaoImpl();

			if (formBean != null) {
				List recordList = dao.getDetails(dto);
				/* ------checking if record list is not empty------- */
				status = checkForBlankData(recordList);
				if (status) {
					/* ------writing record list in excel file------- */
					String filePath = request.getSession().getServletContext().getRealPath(Constants.FILE_NAME);
					String fileName = Constants.FILE_NAME;
					boolean flag = writeExcel(recordList, filePath);

					/* ------down loading record list excel file------- */
					if (flag) {
						File f = new File(filePath);

						String fileType = fileName.substring(fileName
								.indexOf(".") + 1, fileName.length());
						if (fileType.trim().equalsIgnoreCase("xls")) {
							response.setContentType("application/vnd.ms-excel");
						}

						response.setContentLength((int) f.length());
						String name = fileName.substring(0, fileName.length());

						response.setHeader("Pragma", "public");
						response.setHeader("Content-Disposition","attachment; filename=" + name);
						response.setHeader("Cache-Control", "max-age=0");
						byte[] buf = new byte[8192];
						FileInputStream inStream = new FileInputStream(f);
						OutputStream outStream = response.getOutputStream();
						int sizeRead = 0;
						while ((sizeRead = inStream.read(buf, 0, buf.length)) > 0) {
							outStream.write(buf, 0, sizeRead);
						}
						outStream.close();
						inStream.close();
					}
				}
				else{
					messages.add("msg1",new ActionMessage("excel.noRecords.error"));
					saveMessages(request, messages);
				}
			} else {
				forward = mapping.findForward("Success");
			}
		}

		catch (Exception e) {
			errors.add("appErr", new ActionMessage(""));
			saveErrors(request, errors);
			// forward = mapping.findForward("failure");
			e.printStackTrace();
		}
		return forward;
	}

	/*
	 * ---------------------------------Check for No Records-----------------------------------
	 */
	boolean checkForBlankData(List recordList) {
		boolean status = true;
		Iterator<KmSchemesAndBenefitsDto> iterator = recordList.iterator();

		if (!iterator.hasNext()) {
			status = false;
		} else {
			status = true;
		}
		return status;
	}

	/*
	 * ---------------------------------Write Record List Values in Excel-----------------------------------
	 */
	public boolean writeExcel(List recordList, String filePath) {

		boolean flag = false;
		HSSFWorkbook workbook = new HSSFWorkbook();
		int maxRows = Constants.MAXRECORDPERUPLOAD;
		String fileName = Constants.FILE_NAME;
		String worksheetName = Constants.WORKSHEET_NAME;

		try {
			HSSFSheet sheet = workbook.createSheet(worksheetName);
			/* ------Limiting rows of excel to maxRows------- */
			int size = recordList.size();
			if (size > maxRows) {
				size = maxRows;
			}
			int counter = Constants.HEADERCOUNT + 1;

			Map<Integer, Object[]> data = new HashMap<Integer, Object[]>();

			/* ------Setting column names------- */
			data.put(1, new Object[] { "IPortal Self Care - Manual SMS Data" });
			/*data.put(2, new Object[] { "SR.No", "Scheme Type", "SMS from Edu Script", "Display Flag" });*/
			data.put(2, new Object[] { "SR.No", "Scheme Type", "SMS from Edu Script" });

			/* ------Setting rows------- */
			Iterator<KmSchemesAndBenefitsDto> iterator = recordList.iterator();
			while (iterator.hasNext()) {
				if (counter <= size + Constants.HEADERCOUNT) {
					int sno = counter - Constants.HEADERCOUNT;
					KmSchemesAndBenefitsDto iPortalDTO = iterator.next();
					String type = iPortalDTO.getType();
					String description = iPortalDTO.getDescription();
					String displayFlag = iPortalDTO.getDisplayFlag();

					data.put(counter, new Object[] { sno, type, description,displayFlag });
					data.put(counter, new Object[] { sno, type, description });
					counter++;
				}
			}

			Set<Integer> keyset = data.keySet();
			int rownum = 0;
			for (Integer key : keyset) {
				HSSFRow row = sheet.createRow(rownum++);

				Object[] objArr = data.get(key);
				int cellnum = 0;
				for (Object obj : objArr) {
					HSSFCell cell = row.createCell((short) cellnum);

					if (obj instanceof Date)
						cell.setCellValue((Date) obj);
					else if (obj instanceof Boolean)
						cell.setCellValue((Boolean) obj);
					else if (obj instanceof String)
						cell.setCellValue((String) obj);
					else if (obj instanceof Integer)
						cell.setCellValue((Integer) obj);
					else if (obj instanceof Double)
						cell.setCellValue((Double) obj);

					/* ------Formatting rows of Excel------- */
					HSSFCellStyle cellStyle = workbook.createCellStyle();
					/*cellStyle.setBorderBottom((short) 1);
					cellStyle.setBorderLeft((short) 1);
					cellStyle.setBorderRight((short) 1);
					cellStyle.setBorderTop((short) 1);*/

					/* ------Formatting colms of Excel------- */
					if (rownum == 1) {
						CellRangeAddress region = new CellRangeAddress(0, 0, 0, (Constants.MAXCOLUMNPERUPLOAD-1));
						sheet.addMergedRegion(region);
						cellStyle.setFillForegroundColor(HSSFColor.CORAL.index);
						cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
						cellStyle.setBorderBottom((short) 1);
						cellStyle.setBorderLeft((short) 1);
						cellStyle.setBorderRight((short) 1);
						cellStyle.setBorderTop((short) 1);
					}
					if (rownum == 2) {
						sheet.autoSizeColumn((short) cellnum);
						cellStyle.setFillForegroundColor(HSSFColor.CORAL.index);
						cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
						cellStyle.setBorderBottom((short) 1);
						cellStyle.setBorderLeft((short) 1);
						cellStyle.setBorderRight((short) 1);
						cellStyle.setBorderTop((short) 1);
					} else {
						sheet.autoSizeColumn((short) cellnum);
					}
					cell.setCellStyle(cellStyle);

					cellnum++;
				}
			}

			/* ------Writing excel in specified Location------- */
			FileOutputStream out = new FileOutputStream(new File(filePath));
			workbook.write(out);
			out.close();
			logger.info("Excel written successfully");
			flag = true;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

}
