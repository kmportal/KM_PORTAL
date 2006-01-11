/*
 * Created on May 26, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.common;

/**
 * @author Anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

	import java.io.File;
	import java.io.FileReader;
	import java.io.FileWriter;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.ibm.km.services.impl.LoginServiceImpl;

	public class FileCopy {
		private static final Logger logger;

		static {

			logger = Logger.getLogger(FileCopy.class);
		}

	  public void copy(String sourceFile, String destinationFile) throws IOException {
		  try
		  {
		File inputFile = new File(sourceFile);
		File outputFile = new File(destinationFile);

		FileReader in = new FileReader(inputFile);
		FileWriter out = new FileWriter(outputFile);
		int c;

		while ((c = in.read()) != -1)
		  out.write(c);
	
		in.close();
		out.close();
		  }
		  catch(Exception e)
		  {
			  logger.error("*************Exception in document copy ************"+Utility.getStackTrace(e));
		  }
	  }
	  
	  //added by vishwas
	  public String copy1(String sourceFile, String destinationFile) throws IOException {
		String success="";
		//System.out.println("logicccccccccccccccccccccccc: sourceFile"+sourceFile);
		//System.out.println("logicccccccccccccccccccccccc: destinationFile"+destinationFile);
		logger.info("logicccccccccccccccccccccccc:sourceFile "+sourceFile);
		logger.info("logicccccccccccccccccccccccc: destinationFile"+destinationFile);
		try
		  {
		File inputFile = new File(sourceFile);
		File outputFile = new File(destinationFile);
		if(inputFile.exists())
		{
		FileReader in = new FileReader(inputFile);
		FileWriter out = new FileWriter(outputFile);
		int c;
		int a;

		//System.out.println("logicccccccccccccccccccccccc: "+in.read());
		
		if((a=in.read())==-1)
{
	success="blank"	;
}
else
{
	success="exist"	;
	out.write(a);
	while ((c = in.read()) != -1)
		  out.write(c);
	
		in.close();
		out.close();
}
		}
		else{
			success="blank"	;
			logger.info("file not exist on given path"+inputFile);
		}
}
		  catch(Exception e)
		  {
			  logger.error("*************Exception in document copy ************"+Utility.getStackTrace(e));
		  }
	 return success;
	  }

	  //end by vishwas
	  
	 public static void createNewFolder(String newPath , String folderPath){
		 try {
			 logger.info("newPath :: "+newPath+" folderPath:: "+folderPath);
			 String createNewPath ="1";
			 String [] path = newPath.split("/");
			 File file = new File(folderPath+'/'+newPath);
			 if(!file.exists()) {
				 for(int tempPath =1; tempPath <path.length; tempPath++) {
					 createNewPath = createNewPath+'/'+path[tempPath];
					 file = new File(folderPath+'/'+createNewPath);
					 if(!file.exists()) {
						 String newpa =folderPath+createNewPath;
						 file = new File(newpa);
						 file.mkdir();
					 }
					 
				 }
			 }
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		 
	 }
	 
	
	}

           


