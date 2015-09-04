/*
 * Created on Jun 3, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.km.common;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Logger;

import com.ibm.km.dto.DocumentTreeElement;
import com.ibm.km.forms.KmCSRHomeBean;
/**
 * @author Naman
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DocumentTreeTag extends BodyTagSupport{
	
	
	private static final Logger logger;

	static {

		logger = Logger.getLogger(DocumentTreeTag.class);
	}

	public int doStartTag() throws JspTagException {
		JspWriter out = pageContext.getOut();
		StringBuffer msg = new StringBuffer();
		msg.append(" <script language='JavaScript'>");
		HttpServletRequest request =
			(HttpServletRequest) pageContext.getRequest();
		HttpSession session = (HttpSession) pageContext.getSession();
		try {
			msg.append(displayContent(((KmCSRHomeBean) session.getAttribute("CSR_HOME_BEAN")).getDocumentList()));
			msg.append("</script>");
			//logger.info("Output is " + msg);
			out.write(msg.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("JspWriter not there: " + e);
		}
		return SKIP_BODY;
	}
	
/*	public StringBuffer displayContent(
		ArrayList childrenList)
		throws Exception {
			StringBuffer output= new StringBuffer();
			if(childrenList!=null){
				for (Iterator i = childrenList.iterator();i.hasNext();) {
					DocumentTreeElement element = (DocumentTreeElement)i.next();
					logger.info("Inserting node:"+element.getElementLevelId()+"element name ="+element.getElementName()+"  documentid = "+element.getDocumentId());
					//Code Change after UAT observation
					
					if(element.getElementLevelId().intValue()!=0){
						output.append("E"+element.getElementId()+"=insFld(E"+element.getParentId()+", gFld('<span class=submenutext>"+element.getElementName()+"</span>', 'javascript:undefined'));");	
					}else{
						output.append("E"+element.getElementId()+"=insDoc(E"+element.getParentId()+", gLnk('S','<span style=padding-left:5px>"+element.getElementName()+"</span>', 'documentAction.do?methodName=displayDocument&docID="+element.getDocumentId()+"'));");
					}
					
					output.append(displayContent((ArrayList)element.getChildrenList()));
				}
			}
			return output;
		}	*/
		public StringBuffer displayContent(ArrayList childrenList)
				throws Exception {
					StringBuffer output= new StringBuffer();
					boolean first=true;
					if(childrenList!=null){
						for (Iterator i = childrenList.iterator();i.hasNext();) {
							DocumentTreeElement element = (DocumentTreeElement)i.next();
							if(first==true){
								try{
								element = (DocumentTreeElement)i.next();
								}
								catch (Exception e) {
									logger.info("Next element cannot be obtained");
								}
							}
							first=false;
							
							//Code Change after UAT observation
					
							if(element.getElementLevelId().intValue()!=0){
								output.append("E"+element.getElementId()+"=insFld(E"+element.getParentId()+", gFld('<font color=black><span id = "+element.getElementId()+">"+""+element.getElementName()+"</span></font>', 'javascript:undefined'));");
							}else{
								output.append("E"+element.getElementId()+"=insDoc(E"+element.getParentId()+", gLnk('S','<font color=red><span style=padding-left:5px "+element.getElementId()+">"+element.getElementName()+"</span></font>', 'documentAction.do?methodName=displayDocument&docID="+element.getDocumentId()+"'));");
								//output.append("E"+element.getElementId()+"=insDoc(E"+element.getParentId()+", gLnk('S','<span style=padding-left:5px>"+element.getElementName()+"</span>', 'javascript:displayDocument("+element.getDocumentId()+",this)'));");
							}
							//logger.info(output);
							//output.append(displayContent((ArrayList)element.getChildrenList()));
						}
					}
					return output;
	}		
}
