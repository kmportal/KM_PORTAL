package com.ibm.km.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.ibm.km.dao.DBConnection;
import com.ibm.km.dto.BulkUploadDetailsDTO;
import com.ibm.km.exception.DAOException;

public class DocumentPathUtil {
	
	Logger logger = Logger.getLogger(DocumentPathUtil.class);
	
	public ArrayList<BulkUploadDetailsDTO> getPath() throws DAOException {
		
		BulkUploadDetailsDTO bulkUploadDetailsDTO = null;
		ArrayList<BulkUploadDetailsDTO> bulkUploadList = new ArrayList<BulkUploadDetailsDTO>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String selectPath = "select LOOKUP_VALUE,LOOKUP_FLAG from KM_LOOKUP_MSTR where LOOKUP_KEY ='DOCS_PATH' with ur";

		try {
			con = DBConnection.getDBConnection();
			ps = con.prepareStatement(selectPath);
			rs = ps.executeQuery();

			if (rs == null) {
				logger.info("DocumentPathUtil :: Path not found");
			}

			while (rs.next()) {
				bulkUploadDetailsDTO = new BulkUploadDetailsDTO();
				bulkUploadDetailsDTO.setBulkUploadPath(rs.getString("LOOKUP_VALUE"));
				bulkUploadDetailsDTO.setBulkUploadflag(rs.getString("LOOKUP_FLAG"));

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

}
