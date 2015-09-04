
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<LINK href="./jsp/theme/css.css" rel="stylesheet" type="text/css">


  <table width="100%" border="0" cellspacing="0" cellpadding="0">
		    	<tr>
		    		<td >
					</td>
			    </tr>
			  </table>
  <table width="100%" class="mLeft5" align="left" cellspacing="0" cellpadding="0">
    <tr >
      <td width="100%" height="378" align="left" valign="top" class="pRight10 pTop5">
<table width="75%" class="mLeft5" align="center" cellspacing="0" cellpadding="0" >
	<tr><td></td></tr>
	<tr><td></td></tr>
	<tr><td></td></tr>		
    <tr align="center">
    		<td width="831"><span class="heading">Knowledge Management Portal: View Document</span></td>
    </tr>
</table>
  <table width="98%" align="center" border="0" cellspacing="0" cellpadding="0">
					
					<tr>
						 <TD colspan="4" align="left" height="20">
							 <logic:present name="kmWhatsNewFormBean">
								 
								<logic:notEqual name="CURRENT_PAGE" value="DOCUMENT_ERROR"> 
								 <iframe src='.<bean:write name="kmWhatsNewFormBean" property="filePath"/>' width="100%" height="550" align="right"></iframe> 
							 	<!--<IFRAME src="docHome.jsp" width="100%" height="500" align="right" ></IFRAME>-->
							  </logic:notEqual>
							  <logic:equal name="CURRENT_PAGE" value="DOCUMENT_ERROR">
								<FONT color="red"> FILE NOT FOUND </FONT>
							  </logic:equal>
							  </logic:present>
						 </TD>
                    </tr>
				  

		    </table>
		    
			</td>
    </tr>
  </table>
