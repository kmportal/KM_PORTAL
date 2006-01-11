/*
 * Created on Nov 18, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.common;

import java.io.File;

/**
 * @author Anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */


	public class Delete {
	  public void delete(String directory) {
	  	
	  	try{
	  	
		String fileName = directory;
		//System.out.println("Directory : "+directory);
		// A File object to represent the filename
		File f = new File(fileName);

		// Make sure the file or directory exists and isn't write protected
		if (!f.exists())
		  throw new IllegalArgumentException(
			  "Delete: no such file or directory: " + fileName);

		if (!f.canWrite())
		  throw new IllegalArgumentException("Delete: write protected: "
			  + fileName);

		// If it is a directory, make sure it is empty
		if (f.isDirectory()) {
		  File [] files=f.listFiles();	
		  String[] fileNumber=f.list();
		  int i;
		  for(i=0;i<fileNumber.length;i++){
		  files[i].delete();
		  }
		 // if (files.length > 0)
		//	throw new IllegalArgumentException(
		//		"Delete: directory not empty: " + fileName);
		}

		// Attempt to delete it
		boolean success = f.delete();
		
		if (!success)
		  throw new IllegalArgumentException("Delete: deletion failed");
		}
			  catch(Exception e){
				e.printStackTrace();
			  }
	 
	  }
	}

