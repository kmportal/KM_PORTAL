<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%// set this page to not be cached by the browser
response.setHeader("Pragma", "no-cache");
response.setDateHeader("Expires", -1);
response.setHeader("Cache-Control", "no-cache");
%>


<script language="javascript">
	var path = '<%=request.getContextPath()%>';
	var port = '<%= request.getServerPort()%>';
	var serverName = '<%=request.getServerName() %>';
</script>


<script>

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

function getSuggestionsMenu()
{
var a1=document.getElementById("searchTextMenu");
	
if (a1!=null){		
	 if (keyword != "") {
	 var keyword = trims(a1.value);
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
	  return true;
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
			//document.kmDocumentMstrForm.dummy.options[0].selected=true;
			//document.kmDocumentMstrForm.dummy.options[0].focus();
			}
        }
    }
   
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

function formSubmits()
{      
     
     var contentString = trims(document.getElementById("keywordForDetails").value);
     // iPortal_Enhancement bug MASDB00178001 resolved.
     while( contentString.indexOf("  ") !=-1) 
     {
     	contentString = contentString.replace("  "," ");
     }
     
     var x =dotandspaces(contentString);
	 if(x==false)
 		{
 		document.getElementById("kmDocumentMstrFormMainSearch").keywordForDetails.focus();
		return false;
 		}
	if((contentString=="")&&(contentString.length==0))
  	{	
		alert("Please enter any keyword for searching..");
		document.getElementById("kmDocumentMstrFormMainSearch").keywordForDetails.focus();
		return false;
	}
    // iPortal_Enhancements changes for complete string search; CSR20111025-00-06938:BFR2
	if(contentString.charAt(0) == "\"" || contentString.charAt(contentString.length-1) == "\"")
	{
	document.kmRightSearchFormBean1.searchContentHidden.value = contentString ;
	}
	else
	{	
	document.kmRightSearchFormBean1.searchContentHidden.value = "\""+ contentString +"\"";
	}
	// End of Change
	document.kmRightSearchFormBean1.searchCircleId.value=document.getElementById("currentCircleId").value;
    //mainOption == 'SOP'  1 || mainOption == 'netWorkFault'  2
	if(mainOption == 'netWorkFault')
	{
	 document.kmRightSearchFormBean1.searchModeChecked.value = 2;
	 document.kmRightSearchFormBean1.methodName.value="searchLocation";
	// document.kmRightSearchFormBean1.keywordMenu.value=contentString;
	 document.kmRightSearchFormBean1.submit();
	 
	}
	else if(mainOption == 'SOP')
	{
	  document.kmRightSearchFormBean1.searchModeChecked.value = 1;
	  document.kmRightSearchFormBean1.methodName.value="search";
	 // document.kmRightSearchFormBean1.keywordMenu.value=contentString;
	  document.kmRightSearchFormBean1.submit();
	  
	}
	else 
	{
	  document.kmRightSearchFormBean1.searchModeChecked.value = 0;
	  document.kmRightSearchFormBean1.methodName.value="search";
	  document.kmRightSearchFormBean1.submit();
	  
	}
	return true;
	
}

function submitSearchMenus(event) {
    if (event.keyCode == 13)
    {
       // formSubmits();    
       SearchDetails(event);
       return false;
         
    }
     return true;
}


function submitSearchMenu(event) {
    if (event.keyCode == 13)
    {
        formSubmits();        
    }
     return true;
}
function selectSearchTextMenu(event) 
{
    if (event.keyCode == 40)
    {
     document.getElementById("suggestionBoxMenu").focus();
    }
     return true;
}

function selectSuggestionMenu(selectedItem,event)
{

   if (event.keyCode == 13)
  {
    document.getElementById("searchTextMenu").value=selectedItem;
    formSubmits();
  }	
} 
  
function selectSuggestionMenu(selectedItem)
{
      document.getElementById("searchTextMenu").value=selectedItem;
      formSubmits();
}

function returnToTextBoxMenu(event)
{
  if (event.keyCode == 38)
    {
      var obj = document.getElementById('suggestionBoxMenu');
	  if(obj.options[0].selected == true)
	  {
     	document.getElementById("searchTextMenu").focus();
     	hideSuggestionBoxs();
      }
    }	 
}
	
</script>


<script language="JavaScript" src="jScripts/KmValidations.js"
	type="text/javascript"	>
</script> 



<SCRIPT>

function setFocuss()
{
	document.kmDocumentMstrForm.keyword.focus();
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




</SCRIPT> 
<script language="javascript">
var mainOption="";
function SearchDetails(e)
{


	 var keywordString = document.getElementById("keywordForDetails").value;
	 keywordString = trims(keywordString);
	 
	 if(e.target == document.getElementById("keywordForDetails") || e.srcElement ==document.getElementById("keywordForDetails") )
		{
		  document.kmRightSearchFormBean1.searchContentHidden.value=keywordString;
        }
	 if(mainOption == "" && document.getElementById("selectedSubOptionValue").value !="" )
	 {
	 alert("Please select Distributor/ARC/Co-ords/Others!");
	 return false;
	 }
	 if(("" == keywordString || keywordString == "Search Content" || keywordString == "Search Details") && mainOption == "")
	 {
	   formSubmits();
	 }
	 else if(("" == keywordString || keywordString == "Search Content" || keywordString == "Search Details") && mainOption != "")
	 {
	 alert("Please enter text to be search !!!");
	 return false;
	 }
	 else if(mainOption !="")
	 {
	 	if((mainOption!='pack' && mainOption!='SOP' && mainOption!='netWorkFault') && document.getElementById("selectedSubOptionValue").value =="")
 			{
	 			alert("Please select search option !");
		 		return false;
	 		}
	 		else if(mainOption =='SOP' || mainOption=='netWorkFault')
	 		{
			 	/*if(keywordString.charAt(0) == "\"" || keywordString.charAt(keywordString.length-1) == "\"")
				{
				}
				else
				{	
				keywordString = "\""+ keywordString +"\"";
				}
				document.getElementById("kmDocumentMstrFormMainSearch").keywordMenu.value = keywordString;
				document.getElementById("kmDocumentMstrFormMainSearch").methodName.value = "contentSearch"; 
				document.getElementById("kmDocumentMstrFormMainSearch").submit();
		 		return true;*/
		 		formSubmits();
	 		}
	 		else
	 		{  
	 		   // alert(keywordString);
 				document.getElementById("kmDocumentMstrFormMainSearch").mainOptionValue.value= mainOption;
 				document.getElementById("kmDocumentMstrFormMainSearch").keywordMenu.value = keywordString;
				document.getElementById("kmDocumentMstrFormMainSearch").methodName.value = "searchDetails"; 
				document.getElementById("kmDocumentMstrFormMainSearch").submit();
				return true;
			}
		}
		
		
		formSubmits();
		/*if(keywordString.charAt(0) == "\"" || keywordString.charAt(keywordString.length-1) == "\"")
		{
		}
		else
		{	
		keywordString = "\""+ keywordString +"\"";
		}
		document.getElementById("kmDocumentMstrFormMainSearch").keywordMenu.value = keywordString;
		document.getElementById("kmDocumentMstrFormMainSearch").methodName.value = "contentSearch"; 
		document.getElementById("kmDocumentMstrFormMainSearch").submit();
 		return true;*/
}

function setSelectedSubOption(val)
{
 document.getElementById("kmDocumentMstrFormMainSearch").selectedSubOptionValue.value = val;
}

function populateSubOption(val)
{
/*************************/
var radiosOfForm1 = document.getElementById("kmDocumentMstrFormMainSearch").mainOption;
var radiosOfForm2 = document.getElementById("forms2").mainOption;
var selectedSubOption = document.getElementById("kmDocumentMstrFormMainSearch").selectedSubOption;
if(val =='SOP' || val== 'netWorkFault')
	{
	mainOption = val;
	for(var i=0;i< radiosOfForm1.length;i++)
		{
		if(radiosOfForm1[i].checked)
		{
		radiosOfForm1[i].checked =false;
		}
		}
		for(var k=0;k< selectedSubOption.length;k++)
		{
		if(selectedSubOption[k].checked)
		{
		selectedSubOption[k].checked =false;
		}
		}
	}
	
	if(val =='dist' || val== 'arc' || val== 'coords' || val== 'pack' || val=='vas' || val=='snb')
	{
	for(var j=0;j< radiosOfForm2.length;j++)
		{
			if(radiosOfForm2[j].checked)
			{
			radiosOfForm2[j].checked =false;
			}
		}
	}
/**************************/


	mainOption = val;
	setSelectedSubOption("");
	//var mainOption1= document.querySelector('input[name="mainOption"]:checked').value;
	if(mainOption == "dist")
	document.getElementById("subOptionDiv").innerHTML ="<input type='radio' name='selectedSubOption' value='pincode' onclick = 'setSelectedSubOption(this.value)'>PIN <input type='radio' name='selectedSubOption' value='area' onclick = 'setSelectedSubOption(this.value)'>Area";
	else if(mainOption == "arc")
	document.getElementById("subOptionDiv").innerHTML ="<input type='radio' name='selectedSubOption' value='pincode' onclick = 'setSelectedSubOption(this.value)'>PIN <input type='radio' name='selectedSubOption' value='area' onclick = 'setSelectedSubOption(this.value)'>Area";
	else if(mainOption == "coords")
	document.getElementById("subOptionDiv").innerHTML ="<input type='radio' name='selectedSubOption' value='spocname' onclick = 'setSelectedSubOption(this.value)'>Spoc Name";
	else if(mainOption == "pack")
	document.getElementById("subOptionDiv").innerHTML = "";
	else if(mainOption == "vas")
	document.getElementById("subOptionDiv").innerHTML ="<input type='radio' name='selectedSubOption' value='vasName' onclick = 'setSelectedSubOption(this.value)'>Vas Name ";
	/* Added by Saanya for Scheams & Benefits */
	else if(mainOption == "snb")
	document.getElementById("subOptionDiv").innerHTML ="<input type='radio' name='selectedSubOption' value='type' onclick = 'setSelectedSubOption(this.value)'>Type";
}
</script>

     <!-- Adding by RAM -->
     <style>
     input[type="radio"] {                        
	border: 0px !important;
	background-color: transparent;
	}
     </style>
<form name="kmDocumentMstrFormMainSearch" id="kmDocumentMstrFormMainSearch" method="post" action="/KM/documentAction.do">
<input type="hidden" name="methodName" value="contentSearch">
<input type="hidden" name="keywordMenu" id=" keywordMenu" value="">
<input type="hidden" name="mainOptionValue" id="mainOptionValue" value="">
<input type="hidden" name="selectedSubOptionValue" id="selectedSubOptionValue" value="">
<div id="right-frame2">
<div class="search-box">
  <ul>
	<li><input name="Name"  onfocus="if (this.value==this.defaultValue) this.value = ''" onblur="if (this.value=='') this.value = this.defaultValue" 
	type="text" id="keywordForDetails" value="Search Details" onkeyup="javascript:getSuggestionsMenu();" onkeydown="return selectSearchTextMenu(event);" onkeypress="return submitSearchMenus(event);" style="background:none; border:none; width:142px; height=29;padding:6px 8px 6px 8px; margin:0 0 0 0;" />
	</li>
	<li>
	<font size="1"><br>
	<input type="radio" name="mainOption" id="mainOption"  value="dist" onclick="populateSubOption(this.value)">Distributor
	<input type="radio" name="mainOption" id="mainOption" value="arc" onclick="populateSubOption(this.value)">ARC
	<input type="radio" name="mainOption" id="mainOption" value="coords" onclick="populateSubOption(this.value)">Co-ords
	<!-- Added by Saanya for Schemes & Benefits -->
	<input type="radio" name="mainOption" id="mainOption" value="snb" onclick="populateSubOption(this.value)">Self Care
	<input type="radio" name="mainOption" id="mainOption" value="vas" onclick="populateSubOption(this.value)">Others
	<!-- <input type="radio" name="mainOption" id="mainOption" value="pack" onclick="populateSubOption(this.value)">Pack -->
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
  </ul>


<br>
</div>
</form>
<!--  End of Adding by RAM -->
<br><br><br><br>
<html:form action="/kmSearch" name="kmRightSearchFormBean1" type="com.ibm.km.forms.KmSearchFormBean" styleId="forms2">
<fieldset>
<html:hidden  property="methodName" value="search" />
<!-- iPortal_Enhancements changes for complete string search; CSR20111025-00-06938:BFR2 -->
 <html:hidden  property="searchContentHidden" />
 <!--  End of Change  -->
<html:hidden property="searchCircleId" />
<html:hidden property="selectedLob" />
<html:hidden  property="searchType" />
<html:hidden styleId="searchModeChecked"  property="searchModeChecked" />
 <!--  End of Change  -->


       <!-- <div class="search-box">
            <ul class="clearfix">
              <li>
                <html:text property="keyword" styleId="searchTextMenu" maxlength="50" onfocus="if (this.value==this.defaultValue) this.value = ''"
                onkeyup="getSuggestionsMenu();" onkeydown="return selectSearchTextMenu(event);" onkeypress="return submitSearchMenu(event);" 
                onblur="if (this.value=='') this.value = this.defaultValue"  value="Search Content" style="background:none; border:none; width:142px; padding:6px 8px 6px 8px; margin:0 0 0 0;" />
              </li>
              <li><a href="javascript:;" style="float:right; padding:1px 1px 0 0;"><img src="common/images/go.jpg" onclick="return formSubmits();" alt="go" width="27" height="29" border="0" align="right" /></a></li>
            <li class="clearfix alt">
           
              <div style="margin:29px 0px 0px -206px; z-index: 5; position:absolute;" id="suggestionBoxDiv">
			<select id="suggestionBoxMenu" name="suggestionBoxMenu" style="display:none;z-index: 50; width: 180px;" 
			 size ="10" onkeypress="selectSuggestionMenu(this.value, event);"  onclick="return selectSuggestionMenu(this.value);" onkeyup="returnToTextBoxMenu(event)">      
            </select>
          </div>
	     </li>
        </ul>
       </div>-->
       <ul class="list6 clearfix form1" style="z-index: 1;">
            <!--   <li class="clearfix label1" >
                <html:radio property="searchMode" value="0" /> All
              </li> name="mainOption" id="mainOption" value="pack" onclick="populateSubOption(this.value)"-->
              <li class="clearfix last label1" >  
              <!--   <html:radio property="searchMode" value="1" /> SOP&nbsp; -->
              <input type="radio" name="mainOption" id="mainOption"  value="SOP" onclick="populateSubOption(this.value)">SOP
              </li>
              <li class="clearfix last label1">
              <!--  <html:radio property="searchMode" value="2" /> Network fault -->
              <input type="radio" name="mainOption" id="mainOption"  value="netWorkFault" onclick="populateSubOption(this.value)">Network fault
              </li>
            </ul>
            </fieldset>
            <a href="javascript:;" style="float:right; padding:1px 1px 0 0;">
		<img src="images/go.jpg" alt="go" onclick="javascript : return SearchDetails(event);" width="27" height="29" border="0" align="right" />
	</a>	
	<br>
            <br>
            
            </div>
       </html:form>      