package com.ibm.km.actions;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFObjectData;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Row;


import com.ibm.km.common.Constants;
import com.ibm.km.dao.KmSchemesAndBenefitsDao;
import com.ibm.km.dao.impl.KmSchemesAndBenefitsDaoImpl;
import com.ibm.km.dto.KmSchemesAndBenefitsDto;




import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

public class ReadExcel {
	
	private static Logger logger = Logger.getLogger(ReadExcel.class.getName());
	
//----------------------------------Parsing Excel File-------------------------------//
    public ArrayList readFromExcelFile(String filename) throws Exception {
    	
        ArrayList sheetData = new ArrayList();
        FileInputStream fis = null;
        
        try {
            fis = new FileInputStream(filename);
            HSSFWorkbook workbook = new HSSFWorkbook(fis);
            HSSFSheet sheet = workbook.getSheetAt(0);
            List<HSSFObjectData> embedObj = workbook.getAllEmbeddedObjects();
            int noOfEmbedObj = embedObj.size();
            logger.info("Number of embeded objects        ------       "+noOfEmbedObj);
            if(noOfEmbedObj == 0){
            int noOfRows = sheet.getLastRowNum();
           
            Iterator rows = sheet.rowIterator();
            while (rows.hasNext()) {
                HSSFRow row = (HSSFRow) rows.next();
                Iterator cells = row.cellIterator();
                List data = new ArrayList();
                
                while (cells.hasNext()) {
                    HSSFCell cell = (HSSFCell) cells.next();
                    if(cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                    	data.add(cell.getRichStringCellValue().getString());
                    } else if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                    	data.add(String.valueOf(cell.getNumericCellValue()));
                    }
                }
                sheetData.add(data);
            }
            logger.info("Sheet Data ----->"+sheetData);
            logger.info("Reading Sheet Data Successful");
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
      return sheetData;
    }

//----------------------------------To validate Total No of Rows-------------------------------//
     public boolean validateExcelRows(ArrayList<List> sheetToInsert) {
    	boolean flag = true;
    	int maxRowNo = Constants.MAXRECORDPERUPLOAD+2;
    	int minRowNo = Constants.MINRECORDPERUPLOAD+2;
    	
    	try{
    		if (sheetToInsert.size() > maxRowNo || sheetToInsert.size() < minRowNo) {
	        	flag = false;
	        }
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
		return flag;
    }
     
//----------------------------------To validate Total No of Columns-------------------------------//
     public boolean validateExcelColumns(ArrayList<List> sheetToInsert) {
    	boolean flag = true;
    	int coloumnNo = Constants.MAXCOLUMNPERUPLOAD;
    	
    	try{
    		if (flag == true){
    			Iterator iterator=sheetToInsert.iterator();
    			int i = 0;
            	while(iterator.hasNext())
            	{
            		ArrayList fieldToInsert=(ArrayList) iterator.next();
            		if(i != 0 && i != 1){
	            		if (fieldToInsert.size() != coloumnNo) {
	            			flag = false;
	                    }
            		}
            		i++;
            	}
    		}
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
		return flag;
    }


//----------------------------------Sheet for Excel Upload-------------------------------//
    public boolean getSheetForExcelUpload(KmSchemesAndBenefitsDto iPortalDTO) {
    	KmSchemesAndBenefitsDao iPortalDaoImpl = new KmSchemesAndBenefitsDaoImpl();
		boolean flag = true;
    	
		try{
			List sheetToInsert = iPortalDTO.getSheetList();
			String insertedBy = iPortalDTO.getInsertedBy();
	    	Iterator iterator = sheetToInsert.iterator();
		    int i=0;
		    int idCounter = 0;
		    while(iterator.hasNext())
		    {
		    	KmSchemesAndBenefitsDto iPortalDTOvar = new KmSchemesAndBenefitsDto();
				ArrayList fieldToInsert=(ArrayList) iterator.next();
				iPortalDTOvar = getRecordForExcelUpload(fieldToInsert,i);
				iPortalDTOvar.setInsertedBy(insertedBy);
				
		    	if(i!=0 && i!=1){
					iPortalDTOvar.setSrNo(++idCounter);
		    		flag = iPortalDaoImpl.insertDetails(iPortalDTOvar);
					if (flag != true) {
						break;
					}
		    	}
		    	i++;
		    }
		}
		catch(Exception e){
			e.printStackTrace();
		}
    	return flag;
    }
    
//----------------------------------Record of Excel Upload-------------------------------//
    public KmSchemesAndBenefitsDto getRecordForExcelUpload(ArrayList<String> fieldToInsert,int position) {
    	
    	KmSchemesAndBenefitsDto iPortalDTO = new KmSchemesAndBenefitsDto();
    	
    	try{
    		int counter = Constants.HEADERCOUNT;
    		Set<Integer> counter2 = new HashSet<Integer>();
    		
    		for (int i = 0; i< counter; i++){
    			counter2.add(i);
    		}
    		
		    if(!counter2.contains(position))
		    {
		    	String type = validateForXSS(fieldToInsert.get(1));
		    	iPortalDTO.setType(type);
		    	
		    	String description = validateForXSS(fieldToInsert.get(2));
		    	iPortalDTO.setDescription(description);
		    	/*iPortalDTO.setDisplayFlag(fieldToInsert.get(3));*/
		    }
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    	
    	return iPortalDTO;
    }
    
    public String validateForXSS(String value) {
    	 if (value.contains("<")){
    		 value = value.replace("<", " ");
    	 }
    	 if(value.contains(">")){
    		 value = value.replace(">", " ");
    	 }
    	 
		return value;
    }
 
}

