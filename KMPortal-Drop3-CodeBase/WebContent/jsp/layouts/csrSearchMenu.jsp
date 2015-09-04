
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html:html>
<HEAD>
<%@ page 
language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"

%>
<% String flag= (String)request.getAttribute("csrFirstLogin") ; %>
      <% if(flag==null) { flag="false" ; } 
       if(!flag.equals("true")) { %>
<logic:notEmpty name="CSR_HOME_BEAN" >      
<bean:define id="csrHomeBean" name="CSR_HOME_BEAN"  type="com.ibm.km.forms.KmCSRHomeBean" scope="session" />
<logic:notEmpty name="CSR_HOME_BEAN" property="circlelist">
<bean:define id="allCircleList" name="CSR_HOME_BEAN"  property="circlelist" type="java.util.ArrayList" scope="session" />
</logic:notEmpty>



<logic:notEmpty name="CSR_HOME_BEAN" property="categoryList">
<bean:define id="categories" name="CSR_HOME_BEAN"  property="categoryList" type="java.util.ArrayList" scope="session" />
</logic:notEmpty>

</logic:notEmpty>
<% } %>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<%// set this page to not be cached by the browser
response.setHeader("Pragma", "no-cache");
response.setDateHeader("Expires", -1);
response.setHeader("Cache-Control", "no-cache");
%>
<TITLE></TITLE>
<script language="javascript">
	var path = '<%=request.getContextPath()%>';
	var port = '<%= request.getServerPort()%>';
	var serverName = '<%=request.getServerName() %>';
</script>

<script language="JavaScript" src="jScripts/KmValidations.js" type="text/javascript"> </script>


<LINK href="./theme/css.css" rel="stylesheet" type="text/css">
<LINK href="./theme/SelfcareIIStyleSheet.css" rel="stylesheet" type="text/css">
<LINK href="./jsp/theme/airtel.css" rel="stylesheet" type="text/css">
<SCRIPT>

function newXMLHttpRequest() {

    var xmlreq = false;

    if (window.XMLHttpRequest) {
        // Create XMLHttpRequest object in non-Microsoft browsers
        xmlreq = new XMLHttpRequest();

    } else if (window.ActiveXObject) {

        // Create XMLHttpRequest via MS ActiveX
        try {
            // Try to create XMLHttpRequest in later versions
            // of Internet Explorer

            xmlreq = new ActiveXObject("Msxml2.XMLHTTP");

        } catch (e1) {

            // Failed to create required ActiveXObject

            try {
                // Try version supported by older versions
                // of Internet Explorer

                xmlreq = new ActiveXObject("Microsoft.XMLHTTP");

            } catch (e2) {

                // Unable to create an XMLHttpRequest with ActiveX
            }
        }
    }

    return xmlreq;
}

function getSuggestions()
{
	var keyword = trim(document.getElementById("text").value);
		
	 if (keyword != "") {
		req = newXMLHttpRequest();
	    
	    if (!req) {
	        alert("Your browser does not support AJAX! " +
	              "Please check the KM requirements and contact your System Administrator");
	        return;
	    } 
	    req.onreadystatechange = returnJson;	    
	    var url=path+"/documentAction.do?methodName=getSuggestions&keyword="+keyword;
	    req.open("GET", url, true);
	    req.send(null);
	  }
	  else
	  {
	  	hideSuggestionBox()
	  }
}

function returnJson() {
    if (req.readyState == 4) {
        if (req.status == 200) {
            var json = eval('(' + req.responseText + ')');
			var keywordList = json.keywordList;
			cleanSelectBox();
			hideSuggestionBox();	
		if (keywordList.length > 0) {
			var s= document.getElementById('suggestionBox');
            for (var i = 0; i < keywordList.length; i++) {
            		s.options[s.options.length]= new Option(keywordList[i].keyword,keywordList[i].keyword);   
		        }
            document.getElementById("suggestionBox").style.display = 'inline';
			//document.kmDocumentMstrForm.dummy.options[0].selected=true;
			//document.kmDocumentMstrForm.dummy.options[0].focus();
			}
        }
    }
   
}

function selectSuggestion(selectedItem)
  {
	document.getElementById("text").value=selectedItem;
	document.getElementById("suggestionBox").style.display = 'none';
	document.kmDocumentMstrForm.keyword.focus();
  } 

 function cleanSelectBox()
  {
  	var obj = document.getElementById('suggestionBox');
    if (obj == null) return;
	if (obj.options == null) return;
	while (obj.options.length > 0) {
		obj.remove(0);
	}
  }
	
 function hideSuggestionBox()
 {
    document.getElementById("suggestionBox").style.display = 'none';
 } 



function csrHomeSubmit(cirId){
	
	window.location.href="csrAction.do?methodName=viewCSRHome&circleId="+cirId;
	
}
//Added by Anil for Phase 2
function getCircles(lobId){
	
	document.kmSearchFormBean.selectedLob.value=lobId;
	
	document.kmSearchFormBean.methodName.value="loadCircle";
	document.kmSearchFormBean.submit();
}

function loadSubCategory()
{
	
	document.kmSearchFormBean.methodName.value="loadSubCategory";
	document.kmSearchFormBean.submit();
}
function loadSubSubCategory()
{
	
	document.kmSearchFormBean.methodName.value="loadSubSubCategory";
	document.kmSearchFormBean.submit();
}


function trim(string) 
{

 while (string.substring(0,1) == ' ') {
    string = string.substring(1,string.length);
  }
  while (string.substring(string.length-1,string.length) == ' ') {
    string = string.substring(0,string.length-1);
  }
return string;
}

function formSubmit()
{      
     var contentString = trim(document.kmSearchFormBean.keyword.value);
     
     // iPortal_Enhancement bug MASDB00178001 resolved.
     while( contentString.indexOf("  ") !=-1) 
     {
     	contentString = contentString.replace("  "," ");
     }
     
     var x =dotandspace(contentString);
	 if(x==false)
 		{
 		document.kmSearchFormBean.keyword.focus();
		return false;
 		}

	if((contentString=="")&&(contentString.length==0))
  	{	
		alert("Please enter any keyword for searching..");
		document.kmSearchFormBean.keyword.focus();
		return false;
	}
    
    // iPortal_Enhancements changes for complete string search; CSR20111025-00-06938:BFR2
	if(contentString.charAt(0) == "\"" || contentString.charAt(contentString.length-1) == "\"")
	{
	document.kmSearchFormBean.searchContentHidden.value = contentString ;
	}
	else
	{	
	document.kmSearchFormBean.searchContentHidden.value = "\""+ contentString +"\"";
	}
	// End of Change
	document.kmSearchFormBean.searchCircleId.value=document.getElementById("currentCircleId").value;
		
	if(document.forms[0].searchMode[1].checked)
	{
	 document.kmSearchFormBean.methodName.value="searchLocation";
	 return true;
	}
	else
	{
	 document.kmSearchFormBean.methodName.value="search";
	  return true;
	}
}
function handleEnter (field, event) {
		var keyCode = event.keyCode ? event.keyCode : event.which ? event.which : event.charCode;
		
		if (keyCode == 13) {
		var contentString = trim(document.kmSearchFormBean.keyword.value);
		if((contentString=="")&&(contentString.length==0))
  	{	
		alert("Please enter any keyword for searching..");
		document.kmSearchFormBean.keyword.focus();
		return false;
	}else{
		formSubmit();
		}
		// onkeypress="formSubmit()" modified as suggested by Adil in phase-4, for search on Enter key press
		//	var i;
		//	for (i = 0; i < field.form.elements.length; i++)
		//		if (field == field.form.elements[i])
		//			break;
		//	i = (i + 1) % field.form.elements.length;
		//	field.form.elements[i].focus();
		//	return false;
		} 
		else
		return true;
	}    
	
function dotandspace(txtboxvalue)
{

 var flag=0;
 var strText = txtboxvalue;
 if (strText!="")
 {
 var strArr = new Array();
 strArr = strText.split(" ");
 for(var i = 0; i < strArr.length ; i++)
 {
 if(strArr[i] == "")
 {
  flag=1;              
  break;
 }}}  
 
 if (flag==1)
 {
 alert("Please enter any keyword for searching..");
 return false;
  }
  else
  return true;   
}	



function openMainPage(elementId){


window.location.href="documentAction.do?methodName=openSubCategoryTree&isCat=Y&subCatId="+elementId;

}  

</SCRIPT>
</HEAD>

<BODY>

<SCRIPT>


</SCRIPT>
<html:form action="/kmSearch">
<html:hidden name="kmSearchFormBean" property="methodName" value="search" />
<!-- iPortal_Enhancements changes for complete string search; CSR20111025-00-06938:BFR2 -->
 <html:hidden name="kmSearchFormBean" property="searchContentHidden" />
 <!--  End of Change  -->
<html:hidden property="searchCircleId" />
<html:hidden property="selectedLob" />
<html:hidden name="kmSearchFormBean" property="searchType" /> 

<% String flag1= (String)request.getAttribute("csrFirstLogin") ; %>
      <% if(flag1==null) { flag1="false" ; } 
       if(!flag1.equals("true")) { %>


	<table width="202" border="0" align="left" cellpadding="0"
		cellspacing="0">

		<tr>
			
			<td align="left" style="background-image: url(./images/user-name.gif); background-repeat: repeat-x" width="95">&nbsp;&nbsp;&nbsp;<html:select  styleId="currentCircleId" property="circleId"
					name="csrHomeBean" onchange="return csrHomeSubmit(this.value);">
					<logic:notEmpty name="CSR_HOME_BEAN" property="circlelist">
					<html:options labelProperty="elementName" property="elementId"
						collection="allCircleList" />
						</logic:notEmpty>
			
				</html:select></td>
			<td height="20" align="left"
				style="background-image:url(./images/user-name.gif); background-repeat:repeat-x; padding-left:15px;" width="166">
				<span class="whttext-new"></span></td>
			
			
				
			
		</tr>
		<tr>
			<td height="20" align="left" colspan="2"
				style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x; padding-left:5px;" width="95"><span
				class="whttext-new"> <img src="./images/document-icon.jpg"
				alt="" width="15" height="12" /></span><span 
				class="whttext-new">Search </span></td>
				
			
		</tr>
		<tr>
			<td height="40" colspan="2" valign="top"
				style="background-image:url(images/left-nav-bg.jpg); background-position:top left; background-repeat:repeat-x; background-color:#FFFFFF;">
			<table width="182" border="0" align="center" cellpadding="0"
				cellspacing="0">
				<tr>
					<td height="10" width="182" colspan="2">
						<html:radio property="searchMode" value="" />Content <html:radio property="searchMode"  value=""/>Network Fault
                          	<% if( request.getAttribute("SEARCH_MODE")!= null)
                          	{ %>
                          	<script language="JavaScript" >
                          	document.forms[0].searchMode[1].checked=true;
						    </script>
						  <%}
						  else
						  	{%>
						  	<script language="JavaScript" >
                          	document.forms[0].searchMode[0].checked=true;
							</script>
						  <%}%>
					</td>
				</tr>
				<tr>
					<td height="30" class="arial11grey" width="20">Search</td>
					<td height="30">&nbsp;<html:text name="kmSearchFormBean"
						property="keyword" styleId="text" onkeyup="javascript:getSuggestions();" onkeypress="return handleEnter(this, event);" /></td>
						<td height="28"  ><input
						type="image" onclick="return formSubmit();" id="imageField"
						src="images/srch-btn.gif" /></td>
				</tr>
				
				<tr align="center">
				<td> </td>
				<td align="left">
					<html:select property="dummy" styleId="suggestionBox" style="display:none " 
					 size ="10" onchange="selectSuggestion(this.value); ">           
		            </html:select>
		 		</td> 
		          
				</tr>
				
				
			</table>
			</td></tr>
			<tr align="center">
				<td height="20" align="center"
				style="background-image:url(./images/user-name.gif); background-repeat:repeat-x; padding-left:0px;" width="190px">
				<span class="whttext-new"><%= "Category Links" %> </span></td>
			
			</tr>
			<tr><td></td></tr>
			
			
			<logic:notEmpty name="categories">
				<logic:iterate id="category" name ="categories" indexId="catId">
				<tr ><td align="center" colspan="3">
				 <input type="button"   style="border-bottom-style: hidden;color: white;font-weight: bold;background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;width: 190px;font-size: 10px; "  value="<bean:write name="category" property="elementName" />" onclick="JavaScript: openMainPage('<bean:write name="category" property="elementId" />')"/>  </td>
				</tr>
				</logic:iterate>
				</logic:notEmpty>
			
		
	</TABLE>
	<% } %>
</html:form>
</BODY>

</html:html>
