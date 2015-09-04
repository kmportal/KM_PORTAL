package com.ibm.km.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.km.exception.DAOException;
import com.ibm.km.exception.KmException;
import com.ibm.km.services.KeywordMgmtService;
import com.ibm.km.services.KmBannerUploadService;
import com.ibm.km.services.impl.KeywordMgmtServiceImpl;
import com.ibm.km.services.impl.KmBannerUploadServiceImpl;

/**
 * Servlet implementation class BannerImageServlet
 */
public class BannerImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BannerImageServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		
		super.init(config);
		
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

//		System.out.println("******************** Inside INIT ******************");
		KeywordMgmtService kservice = new KeywordMgmtServiceImpl();
		try {
			kservice.insert();
		} catch (KmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String reqImage = request.getParameter("image");
		if(reqImage == null)
			reqImage="";
		if(reqImage.equals("csrHomePage") || reqImage.equals("loginPage") ) {
			KmBannerUploadService service = new KmBannerUploadServiceImpl();
			try{
			InputStream inputStream = null;
			inputStream = service.retrieveBanner(reqImage);
			if(inputStream != null) {
				response.setContentType("image/jpeg");   
				ServletOutputStream out = response.getOutputStream();   
				int size = inputStream.available();   
				byte[] content = new byte[size]; 
				inputStream.read(content);   
				out.write(content);   
				inputStream.close();   
				out.close();  
				}			
		}catch(DAOException daoe){
			daoe.printStackTrace();
		}catch(FileNotFoundException fnfe){
			fnfe.printStackTrace();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	
		}
			
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	/*public void destroy(ServletConfig config) throws ServletException {
		KeyChar.setRootNull();
		System.out.println("\nDestroying........");
	}*/
	
}
