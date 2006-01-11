<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<LINK href="./theme/css.css" rel="stylesheet" type="text/css">
<LINK href=./jsp/theme/css.css rel="stylesheet" type="text/css">
<logic:notEmpty name="DOCUMENT_LIST">
<bean:define id="documentList" name="DOCUMENT_LIST" type="java.util.ArrayList" scope="request"  />
</logic:notEmpty>

<script language="javascript">
	var path = '<%=request.getContextPath()%>';
	var port = '<%= request.getServerPort()%>';
	var serverName = '<%=request.getServerName() %>';
</script>

<script>

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

function selectSearchText(event) 
{	
    if (event.keyCode == 40)
    {
     document.getElementById("suggestionBox").focus();
    }
       return true;
}

function submitSearch(event) {
    if (event.keyCode == 13)
    {
        formSubmit1();        
    }
     return true;
}

function getSuggestions()
{
	var keyword = trim(document.getElementById("keyword").value);
		
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
	  	hideSuggestionBox();
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

  
function selectSuggestionClick(selectedItem)
{     
      document.getElementById("keyword").value=selectedItem;
       formSubmit1();
}

function selectSuggestion(selectedItem,event)
{
   if (event.keyCode == 13)
  {
    document.getElementById("keyword").value=selectedItem;
    formSubmit1();
  }	
} 

function returnToTextBox(event)
{
  if (event.keyCode == 38)
    {
      var obj = document.getElementById('suggestionBox');
	  if(obj.options[0].selected == true)
	  {
     	document.getElementById("keyword").focus();
     	hideSuggestionBox();
      }
    }	 
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
 }
 } 
 if (flag==1)
 {
 alert("Please enter any keyword for searching..");
 return false;
  }
 }
 }        
</script>

<script language="JavaScript" src="jScripts/KmValidations.js"
	type="text/javascript"	>
</script> 


<SCRIPT>

function setFocus()
{
	document.kmDocumentMstrForm.keyword.focus();
	
}
function formSubmit1()
{
	var x =dotandspace(document.kmDocumentMstrForm.keyword.value);
	 if(x==false)
 		{
 		document.kmDocumentMstrForm.keyword.focus();
		return false;
 		}
	if((document.kmDocumentMstrForm.keyword.value=="")&&(document.kmDocumentMstrForm.keyword.value.length==0))
	{	
		alert("Please enter any keyword for searching..");
		document.kmDocumentMstrForm.keyword.focus();
		return false;
	}
	
	document.kmDocumentMstrForm.methodName.value="keywordSearch";
	document.kmDocumentMstrForm.submit();	
	return true;
}
function deleteSubmit(documentId)
{
	if(confirm("Do you want to delete the file ? ")){
		document.kmDocumentMstrForm.selectedDocumentId.value=documentId;
		document.kmDocumentMstrForm.methodName.value= "deleteDocument";
		
	}	
	else
	{
		document.kmDocumentMstrForm.methodName.value="keywordSearch";
	}
	document.kmDocumentMstrForm.submit();
} 
function displaySubmit(documentPath)
{
	
	document.kmDocumentMstrForm.selectedDocumentPath.value=documentPath;
	document.kmDocumentMstrForm.methodName.value= "displayDocument";
	document.kmDocumentMstrForm.submit();
	
}  
function editSubmit(documentId)
{
	document.kmDocumentMstrForm.selectedDocumentId.value=documentId;
	document.kmDocumentMstrForm.methodName.value= "initEditDocument";
	document.kmDocumentMstrForm.submit();
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

</SCRIPT>
<html:form action="/documentAction" >
	<html:hidden property="methodName" value="keywordSearch"/> 
	<html:hidden property="selectedDocumentId"/>
	<html:hidden property="selectedDocumentPath"/>
	<html:hidden property="oldKeyword" />
	<div class="box2">
        <div class="content-upload">
			<H1>Keyword Search</H1>
		<ul class="list2 form1">
	<li class="clearfix">
			<span class="text2 fll width160"><strong><bean:message key="viewAllFiles.keywordSerach" /></strong></span>
				<p class="clearfix fll margin-r20"> <span class="textbox6"> <span class="textbox6-inner"><html:text styleId="keyword" styleClass="textbox7" property="keyword" name="kmDocumentMstrForm" maxlength="50" onkeydown="return selectSearchText(event);"   onkeyup="javascript:getSuggestions();" onkeypress="return submitSearch(event);" /></span></span></p>
				</li>
			
	<li class="clearfix" style="padding-left:170px;">
					<div style="margin:-12px 0px 0px 5px;">
					<html:select property="dummy" styleId="suggestionBox" style="display:none " 
					 size ="10" onclick="return selectSuggestionClick(this.value); " onkeypress = "selectSuggestion(this.value, event);" onkeyup="returnToTextBox(event)">    
				                     
		            </html:select>
		            </div>
	</li>	
	</ul>
	<ul class="list2 form1">

		<li class="clearfix" style="padding-left:170px;">
			<span class="text2 fll">&nbsp;</span>
				<img  src="images/submit.jpg" onclick="return formSubmit1();"/>		
		</li>
		    <logic:notEmpty name="msg" scope="request">
				<li>	<table>	<tr>
						<td colspan="2" class="pTop4 pLeft10">
							
								<table width="100%" border="0" cellpadding="5" cellspacing="0" class="text">
									<tr align="left">
										<td class=""><font color="#0B8E7C"><strong>
										<html:messages id="msg" message="true">
               								<bean:write name="msg"/>  
             							</html:messages></strong></font></td>
									</tr>
								</table>
						</td>
					</tr>
					</table>
					</li>
			</logic:notEmpty>
	</ul>
	<logic:notEqual name="kmDocumentMstrForm" property="initStatus" value="true">
	<div class="content-upload">
			<H1>Search Result</H1>
		</div>

		<table width="100%" cellpadding="3" border="0"  style="table-layout: fixed;">
		<tr>
			<td width="5%" style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5"><bean:message key="viewAllFiles.SNO" /></span></td>
			
			<td width="25%" style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5"><bean:message
				key="viewAllFiles.DocumentName" /></span></td>
			
			<td width="25%" style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5"><bean:message
				key="viewAllFiles.path" /></span></td>
			<td width="15%" style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5"><bean:message
				key="viewAllFiles.approvalStatus" /></span></td>	
			<td width="15%" style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5"><bean:message
				key="viewAllFiles.UploadedDate" /></span></td>
			<td width="15%" style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5"><bean:message
				key="viewAllFiles.ApprovalDate" /></span></td>
		<!--	<td bgcolor="a90000" align="center"></td>
			<td bgcolor="a90000" align="center">&nbsp;</td>
			<td bgcolor="a90000" align="center">&nbsp;</td> -->
		</tr>
		<logic:empty name="documentList" >
			<TR class="lightBg">
				<TD colspan="2" class="error" align="left"><FONT color="red"><bean:message
					key="viewAllFiles.NotFound" /></FONT></TD>
			</TR>
		</logic:empty>
			
		<logic:notEmpty name="documentList" >
			<logic:iterate name="documentList" type="com.ibm.km.dto.KmDocumentMstr" id="report" indexId="i">
				<tr  align="left"  >
					
					<TD   align="center" ><span class="mLeft5 mTop5 mBot5"><%=(i.intValue() + 1)%>.</span></TD>
					<TD   align="left" style="word-wrap: break-word"><span class="mLeft5 mTop5 mBot5"><A HREF='<bean:write name="report" property="documentViewer"/>&docID=<bean:write name="report" property="documentId"/>' class="Red11" ><bean:write name="report"
						property="documentDisplayName" /></A></span></TD>
					<TD   align="left" style="word-wrap: break-word"><span class="mLeft5 mTop5 mBot5"><bean:write name="report"
						property="documentStringPath" /></span></TD>
					<TD   align="center" ><span class="mLeft5 mTop5 mBot5"><bean:write name="report"
						property="approvalStatus" /></span></TD>	
					<TD   align="center" ><span class="mLeft5 mTop5 mBot5"><bean:write name="report"
						property="uploadedDate" /></span></TD>
					<TD   align="center" ><span class="mLeft5 mTop5 mBot5"><bean:write name="report"
						property="approvalRejectionDate" /></span></TD>
			<!--		<TD class="text" align="center"><A Href="#" onclick="javascript: editSubmit('<bean:write name="report" property="documentId"/>');"><FONT color="red"><U><bean:message key="editDocument.edit"/></U></FONT></A></TD>
					<TD class="text" align="center"><A Href="#" onclick="javascript: deleteSubmit('<bean:write name="report" property="documentId"/>');"><FONT color="red"><U><bean:message key="viewAllFiles.delete" /></U></FONT></A></TD>  -->
					
				</TR>
			</logic:iterate>  
		</logic:notEmpty>
		</table>
	</logic:notEqual>
	<br>
<script>
setFocus();
</script>
</div>
</div>	
</html:form>

