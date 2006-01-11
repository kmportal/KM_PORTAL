/*
 * Created on Jun 11, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.common;

import org.apache.struts.util.LabelValueBean;

/**
 * @author namangup
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MyLabelValueBean extends LabelValueBean {

	/**
	 * @param arg0
	 * @param arg1
	 */
	public MyLabelValueBean(String arg0, String arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}
	
	

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object labelValueBean) {
		boolean status=true;
		MyLabelValueBean bean= (MyLabelValueBean)labelValueBean;
		////System.out.println("This.value: "+this.getValue()+" This.label: "+this.getLabel());
		////System.out.println("My.value: "+bean.getValue()+" My.label: "+bean.getLabel());
		if(!this.getLabel().equals(bean.getLabel()))
			status=false;
		else if(!this.getValue().equals(bean.getValue()))
			status=false;
		
		
		return status;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		// TODO Auto-generated method stub
		////System.out.println(this.getLabel().hashCode()+this.getValue().hashCode());
		return this.getLabel().hashCode()+this.getValue().hashCode();
	}

}
