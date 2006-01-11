package com.ibm.km.dao;

import java.util.ArrayList;

import com.ibm.km.exception.DAOException;
import com.ibm.km.dto.BulkUploadDetailsDTO;
import com.ibm.km.dto.KmFileDto;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.exception.KmException;

/**
 * @author Aman
 */
public interface BulkUploadDetailsDao {
	
		public ArrayList<BulkUploadDetailsDTO> bulkDistDeatils(ArrayList<BulkUploadDetailsDTO> listBulkDto,KmUserMstr userBean)throws DAOException;
		
		public ArrayList<BulkUploadDetailsDTO> bulkARCDetails(ArrayList<BulkUploadDetailsDTO> listBulkDto,KmUserMstr userBean)throws DAOException;
		
		public ArrayList<BulkUploadDetailsDTO> bulkCoordinatorDetails(ArrayList<BulkUploadDetailsDTO> listBulkDto,KmUserMstr userBean)throws DAOException;
		
		public boolean isDuplicateDetail(BulkUploadDetailsDTO dto1)throws DAOException;
		
		public boolean isDuplicateARC(BulkUploadDetailsDTO dto1)throws DAOException;
		
		public boolean isDuplicateCoordinatorDetails(BulkUploadDetailsDTO dto1)throws DAOException;
		
		public ArrayList<BulkUploadDetailsDTO> getPath()throws DAOException;

		public ArrayList<BulkUploadDetailsDTO> bulkVasDetails(
				ArrayList<BulkUploadDetailsDTO> listBulkDto, KmUserMstr userBean) throws DAOException;

}
