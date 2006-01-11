
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/kmTags.tld" prefix="kmTags" %>
<html:html>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<%@ page 
language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"
import="com.ibm.km.dto.KmUserMstr,com.ibm.km.dto.KmDocumentMstr"
%>
<bean:define id="kmUserBean" name="USER_INFO"  type="KmUserMstr" scope="session" />
<logic:present name="subCatId" scope="request" >
<bean:define id="subCategoryId" name="subCatId"  scope="request" />
</logic:present>
<logic:notEmpty name="CSR_HOME_BEAN">
	

<bean:define id="csrDocumentList" name="CSR_HOME_BEAN" property="documentList"  type="java.util.ArrayList" scope="session" />

</logic:notEmpty>
<script language="JavaScript" src="././jScripts/ftiens4.js"></script>
<script language="JavaScript" src="././jScripts/ua.js"></script>
<script language="JavaScript" src="././jScripts/treeFunctions.js"></script>
<LINK href="./theme/css.css" rel="stylesheet" type="text/css">
<LINK href="./theme/SelfcareIIStyleSheet.css" rel="stylesheet" type="text/css">
<LINK href="./jsp/theme/airtel.css" rel="stylesheet" type="text/css">

<script>


				USETEXTLINKS = 1
				STARTALLOPEN = 0
				USEFRAMES = 0
				USEICONS = 0
				WRAPTEXT = 1
				PRESERVESTATE = 1
				HIGHLIGHT = 1
					foldersTree = gFld("", "");		
					E<bean:write name="CSR_HOME_BEAN" property="categoryId"/> = insFld(foldersTree, gFld('<font size=2 color=blue><bean:write name="CSR_HOME_BEAN" property="categoryName"/></font>', 'javascript:undefined'));
</script>
<SCRIPT>
   // The openFolderInTree function open all children nodes of the specified 
   // node.  Note that in order to open a folder, we need to open the parent
   // folders all the way to the root.  (Of course, this does not affect 
   // selection highlight.)

   function openFolderInTree(folderObj) {
 

	
    folderObj.forceOpeningOfAncestorFolders();
	   if (!folderObj.isOpen)
	   //var e = window.event.srcElement;
    	 clickOnNodeObj(folderObj);
    	 //alert(document.getElementsById("3732").offsetTop);
    	 
    	
	

    	// document.getElementById("mydiv").scrollTop=document.getElementByT("mydiv").offsetTop
    	 highlightObjLink(folderObj)
    	 //document.get
    	// document.getElementById('vscrollbar').scrollIntoView(); 
    	// folderObj.focus()
    	 //folderObj.click()
    	//window.scrollTo(1000,document.getElementById("vscrollbar").scrollTop);
    	
    	 //folderObj.focus();
    	 
    	
   	 
   } 
   
function scroll(){

	var div = document.getElementById("mydiv")
	
	//var id = data.selectedNodeID.value;
	// selectedNode = document.getElementById(id);
	// pos=getPos(selectedNode).y
	 div.scrollTop=div.scrollBottom
}
function getPos(elm) {
	for(var
		zx=zy=0;elm!=null;zx+=elm.offsetLeft,zy+=elm.offsetTop,elm=elm.offsetParent);

	return {x:zx,y:zy}
}
 
   
</SCRIPT>

<SCRIPT>


</SCRIPT>

<TITLE></TITLE>
	<kmTags:tree>
	</kmTags:tree>	
</HEAD>
 
<BODY  >
<table width="97%" border="0" cellspacing="0" cellpadding="0"
	style="PADDING-RIGHT: 0px; PADDING-LEFT: 0px; PADDING-TOP: 0px">

	<tr>
		<td height="29"
			style="background-image: url(./images/left-nav-heading-bg.jpg); background-repeat: repeat-x; padding-left: 5px"
			width="118"><span class="whttext-new"> <img src="./images/link.jpg"
			alt="" width="10" height="15" /> &nbsp;&nbsp;KnowledgeTree</span></td>
		<td height="29"
			style="background-image: url(./images/left-nav-heading-bg.jpg); background-repeat: repeat-x; padding-left: 5px"
			width="91"><span class="whttext-new"></span></td>
	</tr>
</table>
<DIV  id="mydiv"  class="scrollacc"   style="OVERFLOW: auto; WIDTH: 97%;  background-color:#D2EBF4" align="left">
<table width="97%" border="0" cellspacing="0" cellpadding="0"
	style="PADDING-RIGHT: 0px; PADDING-LEFT: 0px; PADDING-TOP: 0px" bgcolor="#D2EBF4">

	
	

	<tr>
		<td bgcolor="#D2EBF4" class="aria_11_black" width="118"><a href="#"
			class="green"
			onClick="javascript:expandTree(E<bean:write name="CSR_HOME_BEAN" property="categoryId"/>);">Expand
		All</a> &nbsp; <a href="#" class="green"
			onClick="javascript:collapseTree(1);">Collapse All</a></td>
	</tr>
	<tr>
		<td bgcolor="#D2EBF4" class="aria_11_black" width="118"><span class="aria_11_black"><span
			class="leftMenuText"><script>initializeDocument();</script></span></span></td>
	</tr>

	<logic:present name="subCategoryId">
		<script language="JavaScript">
						collapseTree(1);
						
						
						openFolderInTree(E<bean:write name="subCategoryId"/>);
						
						
	</script>
	</logic:present>
</table>
</DIV>
</BODY>
</html:html>

