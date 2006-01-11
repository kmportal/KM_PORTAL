/*
 * Created on Jul 14, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.common;

/**
 * @author 
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
import java.io.*;

public class ImageCopy {

public boolean copy(String source,String destination){
 
	 try { 
    
	  File sourceFile=new File(source);
	  BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFile), 4096);
		   File targetFile = new File(destination); 
		   BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(targetFile), 4096);
		   int theChar;
		   while ((theChar = bis.read()) != -1) {
			  bos.write(theChar);
		   }
		bos.close();
		bis.close();
		
        return true;
  }
  catch (Exception ex) {
   
   ex.printStackTrace();
   return false;
  }  
   } 
}

