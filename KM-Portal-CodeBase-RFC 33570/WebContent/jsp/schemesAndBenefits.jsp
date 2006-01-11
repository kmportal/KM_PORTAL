<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<html:html>
<HEAD>
<LINK href="theme/text.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="common/css/alternative.css">
<link rel="stylesheet" type="text/css" href="common/css/displaytag.css">
<link rel="stylesheet" type="text/css" href="common/css/maven-base.css">
<link rel="stylesheet" type="text/css" href="common/css/maven-theme.css">
<link rel="stylesheet" type="text/css" href="common/css/print.css">
<link rel="stylesheet" type="text/css" href="common/css/screen.css">
<link rel="stylesheet" type="text/css" href="common/css/site.css">
<link rel="stylesheet" type="text/css" href="common/css/teststyles.css">

<TITLE>Schemes and Benefits Upload</TITLE>
<script language="javascript" src="jScripts/KmValidations.js">
var path = '<%=request.getContextPath()%>';
var port = '<%= request.getServerPort()%>';
var serverName = '<%=request.getServerName() %>';

</script>

<script language="JavaScript" type="text/JavaScript">
function limitText(textArea, length) {
if (textArea.value.length > length) {
alert ("Please limit comments length to "+length+" characters.");
textArea.value = textArea.value.substr(0,length);
}
}

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
</script>
</HEAD>
<script type="text/javascript">
function downloadForm(){
 	document.forms[0].method = "POST"; 
	document.forms[0].action ="/KM/downloadSchemesAndBenefits.do";
	document.forms[0].submit(); 
} 

function uploadForm(){
 	var result = validateFile();
	if (result == 0){
		var ans = confirm("All Previous records will be deleted and new records will be uploaded. Do you want to continue?")
 		if(ans == true){
 			document.forms[1].method = "POST"; 
			document.forms[1].action ="/KM/uploadSchemesAndBenefits.do";
			document.forms[1].submit(); 
 		}
	}
} 
          
function validateFile()
{   
	var a=1;
    var  file =document.getElementById("uploadFile").value;
    if(file == ""){
        alert("Please upload a file.");
		a=1;
		return a;
    } 
    var start = file.length-3;
    var end = file.length;
    var checkforxt= file.substring(start,end);
    var checkforName= file.substring(0,start-1);
	var str ="xls";
    var originalArray = str.split(",");
    var flgo=0;
	for(var j=0; j < originalArray.length;j++) {
		if (originalArray[j] == checkforxt) {
		flgo = 1;
		}
	}
	if(flgo == 0)
	{
	alert("Only " + originalArray.toString() + " attachments are allowed");
	a=1;
	return a;
	}
	
	if(file.indexOf('&') > 1){
	alert("File name should not contain following special characters - &,.,%,#");
	a=1;
	return a;
	}
	if(checkforName.indexOf('.') > 1){
	alert("File name should not contain following special characters - &,.,%,#");
	a=1;
	return a;
	}
	if(file.indexOf('%') > 1){
	alert("File name should not contain following special characters - &,.,%,#");
	a=1;
	return a;
	}
	if(file.indexOf('#') > 1){
	alert("File name should not contain following special characters - &,.,%,#");
	a=1;
	return a;
	}

    a=0;
	return a;	
}
		
</script>	
<BODY>
	<div class="box2">
	     <div class="content-upload">
				<H1>Self Care - Manual SMS Data</H1>
           		<div>
					<center>
							<display:table id="data" name="kmSchemesAndBenefitsDetailsFormBean.recordList" requestURI="/viewSchemesAndBenefits.do" pagesize="10" export="false">
						          
						            <display:column property="id" title="S.No" sortable="true" />
						            <display:column property="type" title="Type" sortable="true"/>
						            <display:column property="description" title="Description" sortable="true"/> 
						           <%--  <display:column property="displayFlag" title="Is Configurable" sortable="true"/> --%>
						           
									<display:setProperty name="export.excel.filename" value="Report_Excel.xls"/>
							        <display:setProperty name="export.xml" value="false"/>
							        <display:setProperty name="export.csv" value="false"/>
							        <display:setProperty name="export.pdf" value="false"/>
							        <display:setProperty name="export.rtf" value="false"/>
							        
							        <display:setProperty name="paging.banner.placement" value="bottom"/>
							        <display:setProperty name="footer" value="bottom"/>
							        
									<display:setProperty name="basic.msg.empty_list" value="<font color='red'> <b>No records found.</b></font>"/>      
						    
						     </display:table>
					</center>
				</div>
				
				<div style="clear: both;">&nbsp;</div>
				
				<logic:notEmpty  name="kmSchemesAndBenefitsDetailsFormBean" property="recordList">
				<div class="box2">
					<br>
					<center>
					<html:form action="/downloadSchemesAndBenefits.do" >
						 <input type="submit" id="Download" value="Export to Excel" onclick="downloadForm();"/>
					</html:form>
					</center>
				</div>
				</logic:notEmpty>
				
				<div class="box2">
					<div style="clear: both;">&nbsp;</div>
					<center>
					<html:form action="/uploadSchemesAndBenefits.do"  enctype="multipart/form-data" >
						<b>Upload Changes :</b>
						<input type="file" name="uploadFile" id="uploadFile" />
						<html:button value="Upload" property="Upload" onclick="uploadForm();"></html:button>
						<br>
						<font color="red"><strong>
         					<html:messages id="msg" message="true">
                				<bean:write name="msg"/>  
            				</html:messages>
           				</strong></font>
					</html:form>
					</center>
				</div>
		</div>
	</div>
</html:html>