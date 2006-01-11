package com.ibm.km.dto;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class SOPDetails extends ActionForm {


	
	private static final long serialVersionUID = 1L;
	
	private String header="";	
	private String content="";
	 
	private String imageTitle="";
	private String imageName="";
	
	private String sopHeader="";	
	private String sopPath="";
	
	private FormFile imageFile = null;
	
	public FormFile getImageFile() {
		return imageFile;
	}
	public void setImageFile(FormFile imageFile) {
		this.imageFile = imageFile;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getImageTitle() {
		return imageTitle;
	}
	public void setImageTitle(String imageTitle) {
		this.imageTitle = imageTitle;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public String getSopHeader() {
		return sopHeader;
	}
	public void setSopHeader(String sopHeader) {
		this.sopHeader = sopHeader;
	}
	
}
