<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page 
language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"
import="com.ibm.km.dto.KmUserMstr,java.util.ArrayList,java.util.Iterator,java.util.HashMap"
%>
<%@page import="java.util.Map"%>
<%@page import="java.util.LinkedHashMap"%>
<bean:define id="kmUserBean" name="USER_INFO"  type="KmUserMstr" scope="session" />

<script language="javascript">
	var path = '<%=request.getContextPath()%>';
	var port = '<%= request.getServerPort()%>';
	var serverName = '<%=request.getServerName() %>';

function newXMLHttpRequests() {

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

// function to handle alphabet key events to get suggestions
function getSuggestionsMenu()
{
	var keyword = trims(document.getElementById("searchKeyword").value);
		
	 if (keyword != "") {
		req = newXMLHttpRequests();
	    
	    if (!req) {
	        alert("Your browser does not support AJAX! " +
	              "Please check the KM requirements and contact your System Administrator");
	        return;
	    } 
	    req.onreadystatechange = returnJsons;	    
	    var url=path+"/documentAction.do?methodName=getSuggestions&keyword="+keyword;
	    req.open("GET", url, true);
	    req.send(null);
	  }
	  else
	  {
	  	hideSuggestionBoxs();
	  }
}

function returnJsons() {
    if (req.readyState == 4) {
        if (req.status == 200) {
            var json = eval('(' + req.responseText + ')');
			var keywordList = json.keywordList;
			cleanSelectBoxs();
			hideSuggestionBoxs();	
		if (keywordList.length > 0) {
			var s= document.getElementById('suggestionBoxMenu');
            for (var i = 0; i < keywordList.length; i++) {
            		s.options[s.options.length]= new Option(keywordList[i].keyword,keywordList[i].keyword);   
		        }
            document.getElementById("suggestionBoxMenu").style.display = 'inline';
			}
        }
    }
   
}
  
function selectSuggestionMenu(selectedItem,event)
{

   if (event.keyCode == 13)
  {
    document.getElementById("searchKeyword").value=selectedItem;
    submitSearchMenu();
  }	
} 
  //Function to populate searct text box with selected suggestion on mouse left click
function selectSuggestionMenu(selectedItem)
{
    document.getElementById("searchKeyword").value=selectedItem;
    hideSuggestionBoxs();
}

function returnToTextBoxMenu(event)
{
  if (event.keyCode == 38)
    {
      var obj = document.getElementById('suggestionBoxMenu');
	  if(obj.options[0].selected == true)
	  {
     	document.getElementById("searchKeyword").focus();
     	hideSuggestionBoxs();
      }
    }	 
}
  
  // function to handle Up and Down key event
  function selectSearchTextMenu(event) 
{
    if (event.keyCode == 40)
    {
     document.getElementById("suggestionBoxMenu").focus();
    }
     return true;
}

//function to handle Enter key event
function submitSearchMenus(event) {
    if (event.keyCode == 13)
    {
        submitSearchMenu();        
    }
     return true;
}

 function cleanSelectBoxs()
  {
  	var obj = document.getElementById('suggestionBoxMenu');
    if (obj == null) return;
	if (obj.options == null) return;
	while (obj.options.length > 0) {
		obj.remove(0);
	}
  }
 function hideSuggestionBoxs()
 {
    document.getElementById("suggestionBoxMenu").style.display = 'none';
 } 
function dotandspaces(txtboxvalue)
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
<script language="JavaScript" src="jScripts/KmValidations.js"	type="text/javascript"	>
</script> 

<SCRIPT>

function setFocuss()
{
	document.kmDocumentMstrForm.searchKeyword.focus();
}

function trims(string) 
{

 while (string.substring(0,1) == ' ') {
    string = string.substring(1,string.length);
  }
  while (string.substring(string.length-1,string.length) == ' ') {
    string = string.substring(0,string.length-1);
  }
return string;
}

function submitSearchMenu(){
  var keywordString = document.getElementById("searchKeyword").value;
  
  keywordString = trims(keywordString);
 if("" == keywordString)
 {
   return false;
 }
 // iPortal_Enhancements changes for complete string search; CSR20111025-00-06938:BFR2
	if(keywordString.charAt(0) == "\"" || keywordString.charAt(keywordString.length-1) == "\"")
	{
	//To be Sent as it is
	}
	else
	{	
	keywordString = "\""+ keywordString +"\"";
	}
	//commented by shiva
	//Changed for "string which was having # was not properly searched, it was getting truncated."
	//window.location.href="documentAction.do?methodName=contentSearch&keywordMenu="+keywordString;
	// End of Change
	document.getElementById("kmDocumentMstrFormMainSearch").keywordMenu.value = keywordString;
	document.getElementById("kmDocumentMstrFormMainSearch").submit();
 
 //return true;
 }
var mainOption="";
function SearchDetails()
{
	document.getElementById("methodName").value="";
	var keywordString = document.getElementById("searchKeyword").value;
	keywordString = trims(keywordString);
	//alert("keywordString :"+keywordString +" mainOption :"+mainOption+" sub option :"+document.getElementById("selectedSubOptionValue").value);
	 if(mainOption == "" && document.getElementById("selectedSubOptionValue").value !="" )
	 {
	 alert("Please select Distributor/ARC/Co-ords/Others!");
	 return false;
	 }
	 if(("" == keywordString || keywordString == "Search Content") && mainOption == "")
	 {
	   if(keywordString.charAt(0) == "\"" || keywordString.charAt(keywordString.length-1) == "\"")
    	{
	//To be Sent as it is
	    }
	else
	{	
	keywordString = "\""+ keywordString +"\"";
	}
	document.getElementById("keywordMenu").value = keywordString;
	document.getElementById("methodName").value="contentSearch";
	document.getElementById("kmDocumentMstrFormMainSearch").submit();
	 return true;
	 }
	else if(("" == keywordString || keywordString == "Search Content") && mainOption != "")
	 {
	 // put message to enter some content to search
	 alert("Please enter text to be search !!!");
	 return false;
	 }
	else if(mainOption !="")/**********Search for Dist,Coords,ARC  start here*******/ 
	 {
	   if(mainOption!="pack" && document.getElementById("selectedSubOptionValue").value =="")
 		{
	 		alert("Please select search option !");
		 	return false;
	 	}
	 	else
	 	{
	 	document.getElementById("mainOptionValue").value= mainOption;
	 	document.getElementById("keywordMenu").value = keywordString;
		document.getElementById("kmDocumentMstrFormMainSearch").action = "/KM/documentAction.do?methodName=searchDetails";
		document.getElementById("kmDocumentMstrFormMainSearch").submit();
		return true;
	 	}
	 } /**********Search for Dist,Coords,ARC  Ends here*******/
			/****searching for content stsart here*************************/
	if(keywordString.charAt(0) == "\"" || keywordString.charAt(keywordString.length-1) == "\"")
	{
	//To be Sent as it is
	}
	else
	{	
	keywordString = "\""+ keywordString +"\"";
	}
	document.getElementById("keywordMenu").value = keywordString;
	document.getElementById("methodName").value="contentSearch";
	document.getElementById("kmDocumentMstrFormMainSearch").submit();
 
 return true;
	/*******************************/
}

function setSelectedSubOption(val)
{
 document.getElementById("selectedSubOptionValue").value = val;
}

function populateSubOption(val)
{
	mainOption = val;
	setSelectedSubOption("");
	if(mainOption == "dist")
	document.getElementById("subOptionDiv").innerHTML ="<input type='radio' name='selectedSubOption' value='pincode' onclick = 'setSelectedSubOption(this.value)'>PIN <input type='radio' name='selectedSubOption' value='area' onclick = 'setSelectedSubOption(this.value)'>Area";
	else if(mainOption == "arc")
	document.getElementById("subOptionDiv").innerHTML ="<input type='radio' name='selectedSubOption' value='pincode' onclick = 'setSelectedSubOption(this.value)'>PIN <input type='radio' name='selectedSubOption' value='area' onclick = 'setSelectedSubOption(this.value)'>Area";
	else if(mainOption == "coords")
	document.getElementById("subOptionDiv").innerHTML ="<input type='radio' name='selectedSubOption' value='spocname' onclick = 'setSelectedSubOption(this.value)'>Spoc Name";
	else if(mainOption == "vas")
	document.getElementById("subOptionDiv").innerHTML ="<input type='radio' name='selectedSubOption' value='acode' onclick = 'setSelectedSubOption(this.value)'>ACode <input type='radio' name='selectedSubOption' value='dcode' onclick = 'setSelectedSubOption(this.value)'>DCode";
}
</SCRIPT> 
<style >
.legend
{
color:white;
background-color:#DB0909;
font-size:12px;
display:inline-block;
padding-left:3px;
}
input[type="radio"] {                        
border: 0px !important;
background-color: transparent;
}
</style>
<form name="kmDocumentMstrFormMainSearch" id="kmDocumentMstrFormMainSearch" method="post" action="/KM/documentAction.do">
<input type="hidden" name="methodName" value="contentSearch" id="methodName">
<input type="hidden" name="keywordMenu" id="keywordMenu" value="">
<input type="hidden" name="mainOptionValue" id="mainOptionValue" value="">
<input type="hidden" name="selectedSubOptionValue" id="selectedSubOptionValue" value="">
</form>

<!-- Adding by RAM -->
<fieldset>
  <ul>
  <div class="search-box">
  <ul>
	<li><input name="Name"  onfocus="if (this.value==this.defaultValue) this.value = ''" onblur="if (this.value=='') this.value = this.defaultValue" 
	type="text" id="searchKeyword" value="Search Content" onkeyup="javascript:getSuggestionsMenu();" onkeydown="return selectSearchTextMenu(event);" onkeypress="return submitSearchMenus(event);" style="background:none; border:none; width:142px; height=29;padding:6px 8px 6px 8px; margin:0 0 0 0;" />
	</li>
	<!-- <li><a href="javascript:;" style="float:right; padding:1px 1px 0 0;">
		<img src="images/go.jpg" alt="go" onclick="submitSearchMenu()" width="27" height="29" border="0" align="right" />
	</a></li>-->
	<li class="clearfix alt">
		<div  style="margin:0px 0px 0px -20px; z-index: 9999;  position:relative" id="suggestionBoxDiv">
			<select id="suggestionBoxMenu" style="display:none;z-index: 9999; width: 180px;" 
			 size ="10"  onkeypress = "selectSuggestionMenu(this.value, event);"  onclick="return selectSuggestionMenu(this.value);" onkeyup="returnToTextBoxMenu(event)">           
            </select>
		</div>
	</li>
  </ul>
</div>
  
  <li>
	<font size="1">
	<input type="radio" name="mainOption" id="mainOption"  value="dist" onclick="populateSubOption(this.value)">Distributor
	<input type="radio" name="mainOption" id="mainOption" value="arc" onclick="populateSubOption(this.value)">ARC
	<input type="radio" name="mainOption" id="mainOption" value="coords" onclick="populateSubOption(this.value)">Co-ords<br>
	<input type="radio" name="mainOption" id="mainOption" value="vas" onclick="populateSubOption(this.value)">Others<br><br>
	</font>
	</li>
	
	<li>
	<font size="1">
<fieldset>
<legend>Search Option :</legend>
<div id="subOptionDiv">
<input type="radio" name="selectedSubOption" id="selectedSubOption" value="pincode" onclick = "setSelectedSubOption(this.value)">PIN
<input type="radio" name="selectedSubOption" id="selectedSubOption" value="area" onclick = "setSelectedSubOption(this.value)">Area
</div>
</fieldset>
</font>
	</li>
<a href="javascript:;" style="float:right; padding:1px 1px 0 0;">
		<img src="images/go.jpg" alt="go" onclick="javascript : return SearchDetails();" width="27" height="29" border="0" align="right" />
	</a>	
  </ul>
</fieldset>
<br>
<!--  End of Adding by RAM -->

<h1>&nbsp;</h1>
<% String flag= (String)request.getAttribute("FirstLogin") ; %>
      <% if(flag==null) { flag="false" ; } 
       if(!flag.equals("true")) { %>                 
		   <ul class="rite-side">       
		   <logic:iterate id="moduleMap" name="kmUserBean" property="moduleList">
		   	<% java.util.HashMap mMap=(java.util.HashMap)moduleMap;%>
			  <% if(kmUserBean.isRestricted()==true){ %>
		  	     <li><a href='<bean:write name="moduleMap" property="MODULE_URL"/>'><bean:write name="moduleMap" property="MODULE_NAME"/></a></li>
         	  <%} else{ if(mMap.get("STATUS").equals("A") || mMap.get("STATUS").equals("B")){ %>
            	 <li><a href='<bean:write name="moduleMap" property="MODULE_URL"/>'><bean:write name="moduleMap" property="MODULE_NAME"/></a></li>
          	  <%} } %>
		  </logic:iterate>
		  </ul>		 
    <% } %>