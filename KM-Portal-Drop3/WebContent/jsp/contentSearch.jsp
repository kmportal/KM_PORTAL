<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<style>
.first1
{
background-color:#cccccc;
font-size : 12px;
}
</style>
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

function selectSuggestion(selectedItem,event)
  {
   if (event.keyCode == 13)
    {
      document.getElementById("keyword").value=selectedItem;
      contentsubmit();
    }	
} 
function selectSuggestion(selectedItem)
{
      document.getElementById("keyword").value=selectedItem;
      contentsubmit();
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



/*
function selectSuggestion(selectedItem)
  {
	document.getElementById("keyword").value=selectedItem;
	document.getElementById("suggestionBox").style.display = 'none';
  } */

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
 }}}
 
 if (flag==1)
 {
 alert("Please enter any keyword for searching..");
 return false;
  }
  else
  return true;          
}	  
</script>
<html:errors />

<script language="JavaScript" src="jScripts/KmValidations.js"
	type="text/javascript"	>
</script> 



<SCRIPT>

function setFocus()
{
	document.kmDocumentMstrForm.keyword.focus();
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

function contentsubmit()
{ 
   if(document.forms[0].dateCheck.checked)
   { 
    var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth()+1;
	var yyyy = today.getFullYear();
	if(dd<10){dd='0'+dd}
	if(mm<10){mm='0'+mm}
	var curr_dt=yyyy+'-'+mm+'-'+dd;
    
	if(document.forms[0].searchFromDt.value == "")
		{
			alert("Please enter From Date.");
			return false;			
		}
		else if(document.forms[0].searchFromDt.value > curr_dt)
		{
			alert("From Date cannot be greater than today.");
			return false;			
		}
		else if(document.forms[0].searchToDt.value == "")
		{
			alert("Please enter To Date.");
			return false;			
		}
		else if(document.forms[0].searchToDt.value < document.forms[0].searchFromDt.value)
		{
			alert("From Date should not be greater than To Date.");
			return false;			
		}
		
		/*
		else if(document.forms[0].searchToDt.value < curr_dt)
		{
			alert("To Date cannot be a past date (day before today).");
			return false;			
		}	
		*/
				
    }


    var keywordString = document.kmDocumentMstrForm.keyword.value;
     // iPortal_Enhancement bug MASDB00178001 resolved.
     while( keywordString.indexOf("  ") !=-1) 
     {
     	keywordString = keywordString.replace("  "," ");
     }
     keywordString = document.kmDocumentMstrForm.keyword.value ;
     //---------------- 
    
	 var x =dotandspace(keywordString);
	 if(x==false)
 		{
 		document.kmDocumentMstrForm.keyword.focus();
		return false;
 		}
	if(keywordString=="")
	{	
		alert("Please enter any keyword for searching.");
		document.kmDocumentMstrForm.keyword.focus();
		return false;
	}
	// iPortal_Enhancements changes for complete string search; CSR20111025-00-06938:BFR2
	if(keywordString.charAt(0) == "\"" || keywordString.charAt(keywordString.length-1) == "\"")
	{
	document.kmDocumentMstrForm.searchContentHidden.value = keywordString ;
	}
	else
	{	
	document.kmDocumentMstrForm.searchContentHidden.value = "\"" + keywordString +"\"";
	}
	// End of Change
	document.kmDocumentMstrForm.methodName.value="contentSearch";
	document.kmDocumentMstrForm.submit();
	return true;
	
}

function clearDates()
{ 
  if(!document.forms[0].dateCheck.checked)
  { 
    document.forms[0].searchFromDt.value = "";
    document.forms[0].searchToDt.value = "";
    document.getElementById("fromDate").innerHTML = "";
    document.getElementById("toDate").innerHTML = "";
  }
  else
  {
  document.getElementById("fromDate").innerHTML = "<font color=red>*</font>";
  document.getElementById("toDate").innerHTML = "<font color=red>*</font>";
  }
}

function checkSearchByDate()
{ 
  if(document.forms[0].searchFromDt.value != "" || document.forms[0].searchToDt.value != "")
  { 
    document.forms[0].dateCheck.checked = true;
    document.getElementById("fromDate").innerHTML = "<font color=red>*</font>";
    document.getElementById("toDate").innerHTML = "<font color=red>*</font>";
  }
}
function submitSearch(event) {
    if (event.keyCode == 13)
    {
        contentsubmit();        
    }
     return true;
}

function selectSearchText(event) 
{
    if (event.keyCode == 40)
    {
     document.getElementById("suggestionBox").focus();
    }
     return true;
}

</SCRIPT> 
<html:form action="/documentAction">
	<html:hidden name="kmDocumentMstrForm" property="methodName" /> 
	<html:hidden name="kmDocumentMstrForm" property="searchContentHidden" />
	<html:hidden property="selectedDocumentId"/>
	<html:hidden property="selectedDocumentPath"/>	
		
    <logic:notEmpty name="msg" scope="request">
		<ul class="list2 form1">	
				<li class="clearfix">							
								<table width="100%" border="0" cellpadding="5" cellspacing="0" class="text">
									<tr align="left">
										<td class=""><font color="#0B8E7C"><strong>
										<html:messages id="msg" message="true">
               								<bean:write name="msg"/>  
             							</html:messages></strong></font></td>
									</tr>
								</table>								
					</li>
		</ul>
   	</logic:notEmpty>
         
      <div class="box2">
        <div class="content-upload">
          <h1 >Content Search</h1> 
          <ul class="list2 form1" >
            <li class="clearfix" style="margin-top: 18px;">	
             	<p class="clearfix fll margin-r10">
             	  <html:text property="keyword" styleId="keyword" onfocus="if (this.value==this.defaultValue) this.value = ''" onblur="if (this.value=='') this.value = this.defaultValue" name="kmDocumentMstrForm" maxlength="50" onkeyup="javascript:getSuggestions();" onkeypress="return submitSearch(event);" onkeydown="selectSearchText(event);" 
             	             style="height:20px; width: 250px; border-color: #0000CC; border-width:1px; border-style:inset; padding-left:3px; padding-top:6px; font-size:12px; color:#2f2f2f;"/>	
             	   <a href="javascript:;" style="float:right;float:clear;padding:1px 1px 0 0;">&nbsp;&nbsp;<img src="common/images/go.jpg" onclick="return contentsubmit();" alt="go" width="37" height="27" border="0"  /></a>
             	</p>

				<div style="position: absolute; left: 14px; top:82px; width: 300px; z-index: 5;">
				  <html:select  property="dummy" styleId="suggestionBox" styleClass="clearfix fll margin-r20"  style="display:none; width: 256px;" 
					 size ="10" onkeypress="selectSuggestion(this.value, event);" onclick="return selectSuggestion(this.value);" onkeyup="returnToTextBox(event)">           
		            </html:select>
		        </div> 
              
		     </li>
			<li class="clearfix">	
				<div class="flr" style="position: absolute; left: 348px; top:42px; width: 400px;" >
					<fieldset>
				     <legend><b style="font-size: medium; color: red;"></b></legend>
				      <table >
				      <tr>
				      <td colspan="6">
				      <b><html:checkbox property="dateCheck" onclick="clearDates()">&nbsp;&nbsp;Search by date</html:checkbox></b>
				      </td>
				      </tr>
				      <tr><td><label class="label2" style="padding-right:1px;">From Date</label></td>
	            		   <td width="10px"><div id="fromDate"></div></td>
	            		   <td><html:text  styleClass="tcal calender2 fll" property="searchFromDt"  onblur="checkSearchByDate()"   readonly="readonly" style="margin-right:15px;" />
	                    </td>
	                    <td><label class="label2"  style="padding-right:1px;">To Date</label></td>
	                    <td width="10px"><div id="toDate"></div></td>
	                    <td height="40"><html:text styleClass="tcal calender2 fll" property="searchToDt" onblur="checkSearchByDate()"   readonly="readonly"  />
	                    </td>
	                </tr></table>
	  			 </fieldset>
  			 </div>
         </li>
       </ul>
        
      </div>
       <br>
       <!--    
       <div class="button-area" >
         <div class="button"><input class="submit-btn1 red-btn" name="" value="" onclick="return contentsubmit();" /></div>
       </div>
      --><br>
     </div>
     <br>
     <div class="boxt2">
        <div class="content-upload clearfix">
          <h1 class="clearfix" style="margin-bottom:0px;"><span class="text">Search Result</span> </h1>
          <div class="content-table-type2 clearfix">
          		
	<logic:notEqual name="kmDocumentMstrForm" property="initStatus" value="true">
	
		<table width="100%" border="0" cellspacing="0" cellpadding="2" style="table-layout: fixed;" >
		
		 <tr class="first1">
                <td width="5%" align="left" valign="top"><bean:message key="viewAllFiles.SNO" /></td>
                <td width="35%" align="left" valign="top"><bean:message
				key="viewAllFiles.DocumentName" /></td>
                <td width="40%" align="left" valign="top"><bean:message
				key="viewAllFiles.path" /></td>
				<td width="20%" align="left" valign="top"><bean:message
				key="viewAllFiles.UploadedDate" /></td>
                
              </tr>
		
		<!--
		<tr>
			<td  height="35" width="146"><span class="mLeft5 mTop5"><bean:message key="viewAllFiles.SNO" /></span></td>
			
			<td  height="35" width="227"><span class="mLeft5 mTop5"><bean:message
				key="viewAllFiles.DocumentName" /></span></td>
			
			<td  height="35" width="190"><span class="mLeft5 mTop5"><bean:message
				key="viewAllFiles.path" /></span></td>
			<td  height="35" width="110"><span class="mLeft5 mTop5"><bean:message
				key="viewAllFiles.approvalStatus" /></span></td>	
			<td  height="35" width="291"><span class="mLeft5 mTop5"><bean:message
				key="viewAllFiles.UploadedDate" /></span></td>
			<td  width="343"><span class="mLeft5 mTop5"><bean:message
				key="viewAllFiles.ApprovalDate" /></span></td>
		</tr>
		-->
		
		<logic:empty name="documentList" >
			<TR class="lightBg">
				<TD colspan="4" class="error" align="center"><FONT color="red"><bean:message
					key="viewAllFiles.NotFound" /></FONT></TD>
			</TR>
		</logic:empty>
			
		<logic:notEmpty name="documentList" >
			<logic:iterate name="documentList" type="com.ibm.km.dto.KmDocumentMstr" id="report" indexId="i">
			<%	String cssName = "";				
				if( i%2==1)
				{			
				cssName = "alt";
				}	
				%>
				<tr class="<%=cssName%>"  style="word-wrap: break-word">	
					<TD align="left" valign="top" ><%=(i.intValue() + 1)%>.</TD>
					<TD align="left" valign="top" class="start"  style="word-wrap: break-word">
						<A HREF='<bean:write name="report" property="documentViewer"/>&docID=<bean:write name="report" property="documentId"/>&searchKeyword=<bean:write name="kmDocumentMstrForm" property="keyword"/>' class="Red11" ><bean:write name="report"
						property="documentDisplayName" /></A>					</TD>
					<TD align="left" valign="top"  style="word-wrap: break-word"><bean:write name="report" property="documentStringPath" /></TD>
					<TD align="left" valign="top"  style="word-wrap: break-word"><bean:write name="report" property="uploadedDate" /></TD>
					<!--<TD   align="center" ><span class="mLeft5 mTop5 mBot5"><bean:write name="report"
						property="approvalStatus" /></span></TD>	
					<TD   align="center" ><span class="mLeft5 mTop5 mBot5"><bean:write name="report"
						property="uploadedDate" /></span></TD>
					<TD   align="center" ><span class="mLeft5 mTop5 mBot5"><bean:write name="report"
						property="approvalRejectionDate" /></span></TD>
				-->
				</TR>
			</logic:iterate>  
		</logic:notEmpty>
		</table>
	
	</logic:notEqual>
	
	</div>
        </div>
      </div>
	
</html:form>

