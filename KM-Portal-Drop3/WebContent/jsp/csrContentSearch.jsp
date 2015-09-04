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
	var keyword = document.getElementById("text").value ;
		
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
			}
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

<script language="JavaScript" src="jScripts/KmValidations.js"	type="text/javascript"	></script> 

<SCRIPT><!--

function setFocus()
{
	document.kmSearchFormBean2.keyword.focus();
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


function formSubmit2()
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
     var contentString = trims(document.getElementById("text").value);
     
     // iPortal_Enhancement bug MASDB00178001 resolved.
     while( contentString.indexOf("  ") !=-1) 
     {
     	contentString = contentString.replace("  "," ");
     }
	if((contentString=="")&&(contentString.length==0))
  	{
		alert("Please enter any keyword for searching..");
		document.kmSearchFormBean2.keyword.focus();
		return false;
	}
    // iPortal_Enhancements changes for complete string search; CSR20111025-00-06938:BFR2
	if(contentString.charAt(0) == "\"" || contentString.charAt(contentString.length-1) == "\"")
	{
	document.kmSearchFormBean2.searchContentHidden.value = contentString ;
	}
	else
	{	
	document.kmSearchFormBean2.searchContentHidden.value = "\""+ contentString +"\"";
	}
	// End of Change
	document.kmSearchFormBean2.searchCircleId.value=document.getElementById("currentCircleId").value;
	/*if(document.kmSearchFormBean2.searchMode[2].checked)
	{
	alert("55555555555");
	 document.getElementById("searchModeChecked").value = 2;
	 document.kmSearchFormBean2.methodName.value="searchLocation";
	 document.kmSearchFormBean2.submit();
	}*/
	if(document.kmSearchFormBean2.searchMode[1].checked)
	{
	  document.getElementById("searchModeChecked").value = 1;
	 document.kmSearchFormBean2.methodName.value="searchLocation";
	  document.kmSearchFormBean2.submit();
	}
	else 
	{
	  document.getElementById("searchModeChecked").value = 0;
	  document.kmSearchFormBean2.methodName.value="search";
	  document.kmSearchFormBean2.submit();
	}
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
        formSubmit2();        
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

function selectSuggestion(selectedItem,event)
  {
   if (event.keyCode == 13)
    {
      document.getElementById("text").value=selectedItem;
      formSubmit2();
    }	
} 
function selectSuggestion(selectedItem)
{
      document.getElementById("text").value=selectedItem;
      formSubmit2();
}

	function returnToTextBox(event)
	{
	alert("hiding box");
		if (event.keyCode == 38)
 		{
 		alert("enter pressed");
   		var obj = document.getElementById('suggestionBox');
		if(obj.options[0].selected == true)
		{
		alert("selecrted");
  		document.getElementById("text").focus();
  		hideSuggestionBox();
   		}
 	  }	 
	}


  function hideSuggestionBox()
 {
    document.getElementById("suggestionBox").style.display = 'none';
 }  
 
 function  disableOption()
 {
 document.kmSearchFormBean2.searchMode[1].checked=false;
 document.kmSearchFormBean2.searchMode[0].checked=false;
 }
--></SCRIPT> 

<html:form action="/kmSearch" name="kmSearchFormBean2" type="com.ibm.km.forms.KmSearchFormBean">
	<html:hidden  property="methodName" value="search" />
	<html:hidden  property="searchContentHidden" />
	<html:hidden  property="searchCircleId" />
	<html:hidden  property="selectedLob" />
	<html:hidden  property="searchType" />
	<html:hidden  styleId ="searchModeChecked"  property="searchModeChecked" />

		
	<bean:define id="kmSearchFormBean2" name = "kmSearchFormBean" ></bean:define>	
    <logic:notEmpty name="msg" scope="request">
		<ul class="list2 form1">	
				<li class="clearfix">							

								<table width="100%" border="0" cellpadding="0" cellspacing="0" class="text">
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
          <h1>Content Search</h1> 
          <ul class="list2 form1" >
            <li class="clearfix">		
             	<p class="clearfix fll margin-r10"> 
				<html:text  property="keyword"  styleId="text" name="kmSearchFormBean2" onfocus="if (this.value==this.defaultValue) this.value = ''" onblur="if (this.value=='') this.value = this.defaultValue"
             	 style="height:20px; width: 250px; border-color: grey; border-width:1px;  padding-left:3px; padding-top:4px; " maxlength="50" onkeyup="javascript:getSuggestions();" 
             	  onkeypress="return submitSearch(event);" onkeydown="selectSearchText(event);"  />
             	  <div style="position: absolute; left: 14px; width: 300px; z-index: 5; top:28%"><a href="javascript:;" style="float:right; padding:0 0 0 0;">&nbsp;&nbsp;<img src="common/images/go.jpg" onclick="return formSubmit2();" alt="go" width="37" height="30" border="0" align="right" /></a></div>
             	</p>
				<div  style="position: absolute; left: 14px; top:97px; width: 300px; z-index: 5;">
				  <html:select  property="dummy" styleId="suggestionBox" styleClass="clearfix fll margin-r20"  style="display:none ; width: 256px;" 
					 size ="10"  onkeypress="selectSuggestion(this.value, event);" onclick="return selectSuggestion(this.value);" onkeyup="returnToTextBox(event)">           
		            </html:select>
		        </div> 
               <div class="flr"  style="position: absolute; left: 348px; top:42px; width: 410px;">
				<fieldset>
			     <legend><b style="font-size: small; color: red;"></b></legend>
			      <table border="0">
			      <tr>
			      <td colspan="6">
			      <b><html:checkbox property="dateCheck" onclick="clearDates()">&nbsp;&nbsp;Search by date</html:checkbox></b>
			      </td>
			      </tr>
			      
			      <tr style="height: 20px "><td><label class="label2" style="padding-right:1px;">From Date</label></td>
            		   <td width="10px"><div id="fromDate"></div></td>
            		   <td><html:text  styleClass="tcal calender2 fll" property="searchFromDt"  onblur="checkSearchByDate()"   readonly="readonly" style="margin-right:15px;" />
                    
                    </td>
                    
                    <td><label class="label2"  style="padding-right:1px;">To Date</label></td>
                    <td width="10px"><div id="toDate"></div></td>
                    <td><html:text styleClass="tcal calender2 fll"property="searchToDt" onblur="checkSearchByDate()"   readonly="readonly"  />
                    
                    </td>
                </tr>
                <tr>
                <td colspan="6" height="20px" ><!--<html:radio property="searchMode" value="" />&nbsp;&nbsp;All &nbsp;&nbsp;&nbsp;&nbsp;-->
                <html:radio property="searchMode" value="" />&nbsp;&nbsp;SOP &nbsp;&nbsp;&nbsp;&nbsp;
                <html:radio property="searchMode" value="" />&nbsp;&nbsp;Network fault </td>
                <script>
                var dfdsfs=  '<%=request.getAttribute("SEARCH_MODE")%>';
                </script>
                 <% 
                 
                 if(request.getAttribute("SEARCH_MODE")== null || request.getAttribute("SEARCH_MODE")== "")
                 {%>
                 <script language="JavaScript" >
                          disableOption();
							</script>
                 <%
                 }
                  if(request.getAttribute("SEARCH_MODE")== "SOP")
						  	{%>
						  	<script language="JavaScript" >
                          	document.kmSearchFormBean2.searchMode[0].checked=true;
							</script>
						  <%}
						  else if(request.getAttribute("SEARCH_MODE")== "networkSearch")
						  	{%>
						  	<script language="JavaScript" >
                          	document.kmSearchFormBean2.searchMode[1].checked=true;
							</script>
						  <%}%>  
                
                </tr>
                </table>
  			 </fieldset>
  			 </div>
            </li>
           </ul>               
      </div>
      <br><br><br><br><br>
       <!--<br>    
       <div class="button-area" >
         <div class="button"><input class="submit-btn1 red-btn" name="" value="" onclick="return formSubmit2();" /></div>
       </div>
      <br>
     -->
     </div>
     <div class="boxt2">
        <div class="content-upload clearfix">
          <h1 class="clearfix" style="margin-bottom:0px;"><span class="text">Search Result</span></h1>
          <div class="content-table-type2 clearfix">
          		
	<table width="100%" border="0" cellspacing="0" cellpadding="2" style="table-layout: fixed;">
		
		 <tr class="first1">
                <td width="5%" align="left" valign="top"><bean:message key="viewAllFiles.SNO" /></td>
                <td width="35%" align="left" valign="top"><bean:message
				key="viewAllFiles.DocumentName" /> </td>
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
				
				<%
				String cssName = "";				
				if( i%2==1)
				{			
				cssName = "alt";
				}	
				%>
				<tr class="<%=cssName%>" >	
					<TD align="left" valign="top" ><%=(i.intValue() + 1)%>.</TD>
					<TD align="left" valign="top" class="start" style="word-wrap: break-word">

					<A HREF='<bean:write name="report" property="documentViewer"/>&docID=<bean:write name="report" property="documentId"/>&searchKeyword=<bean:write name="kmSearchFormBean2" property="keyword"/>' class="Red11" >
					<bean:write name="report" property="documentDisplayName" /></A>
					</TD>
					<TD align="left" valign="top" style="word-wrap: break-word"><bean:write name="report" property="documentStringPath" /></TD>
					<TD align="left" valign="top" style="word-wrap: break-word"><bean:write name="report" property="uploadedDate" /></TD>
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
	
	</div>
        </div>
      </div>
	
</html:form>
