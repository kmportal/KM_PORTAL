/*
 * Created on Nov 26, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.struts.upload.FormFile;

import com.ibm.km.dto.BulkMsgDto;
import com.ibm.km.dto.BulkUploadDetailsDTO;
import com.ibm.km.dto.KmFileDto;
import com.ibm.km.dto.KmUserMstr;
import com.ibm.km.exception.KmException;
import com.ibm.km.forms.BulkUploadDetailsFormBean;

/**
 * @author Anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface BulkUploadDetailsService {
	
	public BulkMsgDto uploadMstr(FormFile myfile ,BulkUploadDetailsFormBean formbean,KmUserMstr userBean )throws FileNotFoundException,KmException;
	public ArrayList<BulkUploadDetailsDTO> getPath() throws KmException;
	 

}
