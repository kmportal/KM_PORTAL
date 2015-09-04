<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<LINK href="./theme/css.css" rel="stylesheet" type="text/css">
<LINK href=./jsp/theme/css.css rel="stylesheet" type="text/css">
<script language="javascript">
	var path = '<%=request.getContextPath()%>';
	var port = '<%= request.getServerPort()%>';
	var serverName = '<%=request.getServerName() %>';
</script>
<SCRIPT language="JavaScript">
var req=null; 
var ctr=1;

var levelCount=0;
var initialElementPath='<bean:write scope="request" name="allParentIdString"/>';
var elementPath=initialElementPath;
var initialParentId=<bean:write property="parentId" name="networkFaultReportForm"/>;
var parentId=initialParentId;
var initialLevel=<bean:write property="initialLevel" name="networkFaultReportForm"/>;
var parentLevel=initialLevel-1;
var elementLevelNames=new Array();
var childLevel;
var lk=0;
var emptyChildFlag=0;
// for super admin
//if(initialLevel!="9")
//{
<logic:iterate name="elementLevelNames" scope="request" id="levelName" >
	elementLevelNames[lk]='<bean:write name="levelName"/>';
	lk++;
</logic:iterate>
var level=elementLevelNames[initialLevel];

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


//<!--Change start by Vishi   -->
function loadDropdown(e)
//<!--Change end by Vishi   -->
{
	//e= e || event;
	//alert("The source element's id is: "+ (e.srcElement || e.target).id);
	//alert("Element Path0: "+elementPath);
	var tempLevelCount=levelCount;
	
	if ((e.srcElement || e.target).id!="initialSelectBox") {
		var elementId=(e.srcElement || e.target).id;
		var currentElementLevel=elementId.substring(6);
		var table=$id("table_0");
		parentLevel=currentElementLevel;
		//alert("Parent level: "+parentLevel);
		//var levelCount=$id("levelCount").value;
		//alert("currentElementLevel:  "+currentElementLevel);
		//alert("levelCount: "+levelCount);
		for (i=parseInt(currentElementLevel)+1;i<=parseInt(levelCount);i++) {
			//alert("i="+i);
			table.tBodies[0].removeChild($id("tr_level_"+i));
			//alert("cropping element Path: "+elementPath);
			elementPath=elementPath.substring(0,elementPath.lastIndexOf("/"));
			tempLevelCount--;
		}
		if(emptyChildFlag==1){
			//alert("cropping element Path for null children: "+elementPath);
			elementPath=elementPath.substring(0,elementPath.lastIndexOf("/"));
			emptyChildFlag=0;
		}
	/*	if(levelCount>=3)
		{ 
		return;
		}
	*/	//alert("Final Element Path after cropping: "+elementPath);
	}
	else if ((e.srcElement || e.target).id=="initialSelectBox"){
		var table=$id("table_0");
		elementPath=initialElementPath;
		parentLevel=initialLevel;
		//alert("parent level: "+parentLevel);
		//alert("levelCount"+$id("levelCount").value);
		//alert("initialLevel: "+initialLevel);
		//alert("initialElementPath: "+initialElementPath);
		for (i=initialLevel+1;i<=levelCount;i++) {
			table.tBodies[0].removeChild($id("tr_level_"+i));
			tempLevelCount--;
		}
	}
	levelCount=tempLevelCount;
	//alert("Final Level count after cropping element Path: "+levelCount);
	
	parentId=$id((e.srcElement || e.target).id).value;
	
	if (parentId!="") {
		//Obtain an XMLHttpRequest instance
	    req = newXMLHttpRequest();
	    //alert (req);
	    if (!req) {
	        alert("Your browser does not support AJAX! Add Files module is accessible only by browsers that support AJAX. " +
	              "Please check the KM requirements and contact your System Administrator");
	        return;
	    }
	    
	    req.onreadystatechange = returnJson;
	    elementPath=elementPath+"/"+parentId;
	    //alert("Submitting with element Path: "+elementPath);
	    var url=path+"/userReport.do?methodName=loadElementDropDown&ParentId="+parentId+"&Dummy="+new Date().getTime();
	    //alert(url);
	    	if(levelCount>2)
		{ 
		return;
		}
	    req.open("GET", url, true);
	    req.send(null);
	}else if(parentId==""){
		
		parentLevel=parseInt(parentLevel)-1;
		if((e.srcElement || e.target).id=='initialSelectBox'){
			parentId=initialParentId;
		}else if(parentLevel==initialLevel){
			parentId=$id("initialSelectBox").value;
		}else{
			parentId=$id("level_"+parentLevel).value;
		}
	}
}
// }// end of if(initialLevel!="3")
function returnJson() {
    if (req.readyState == 4) {
        if (req.status == 200) {
        	//alert(req.responseText);
            var json = eval('(' + req.responseText + ')');
            
			var elements = json.elements;
			level = json.level;
			childLevel=level;
			//alert("Done setting values");
			if (elements.length!=0) {
	            var addOption = function (value, text, selectBox){
	                var optn = document.createElement("OPTION");
	                optn.text = text;
	                optn.value = value;
	                selectBox.options.add(optn);
	            }
	          
	            var form=document.forms[0];
	            
	            var id="level_"+level;
	            levelCount=level;
	            //alert(levelCount);
	            var name="element_"+(++ctr);
	            var selectDropDown = document.createElement("SELECT");
	            selectDropDown.setAttribute("id",id);
	            selectDropDown.setAttribute("name",name);
	            selectDropDown.setAttribute("multiple",true);
	            selectDropDown.setAttribute("size",5);
	           //  alert(id);
	            
				if (selectDropDown.addEventListener){
					selectDropDown.addEventListener('change', loadDropdown, false); 
				} else if (selectDropDown.attachEvent){
					selectDropDown.attachEvent('onchange', loadDropdown);
				}           
				addOption("",'All Circle', selectDropDown);
				for (var i = 0; i < elements.length; i++) {
		            addOption(elements[i].elementId, elements[i].elementName, selectDropDown);
		        }
		        var table=$id("table_0");
		        var tr=document.createElement("tr");
		        var td1=document.createElement("td");
		        var td2=document.createElement("td");
		        var st=document.createElement("strong"); 
		        var trId="tr_"+id;
		        tr.setAttribute("id",trId);
		        td2.appendChild(selectDropDown);
		        var text="Select "+elementLevelNames[level]+" ";
		        var newNode = document.createTextNode(text);
		        td1.setAttribute("align","left");
		        st.appendChild(newNode); 
		        td1.appendChild(st);
   		        table.tBodies[0].appendChild(tr);
		        tr.appendChild(td1);
		        tr.appendChild(td2);
		        emptyChildFlag=0;
		    }else{
				childLevel=parseInt(levelCount)+1;
				emptyChildFlag=1;
		    }
        }
    }
}
function $id(id) {
	return document.getElementById(id);
}

</script>
<SCRIPT>

function listUser()
{

if(validate())
		{ 	$id("parentId").value=parentId;
        	$id("elementLevel").value=parentLevel;
    	
     		var urlString = '<%=request.getContextPath() %>'+'/networkFaultReport.do?';
    		if(initialLevel=="9"){
    			//alert( "inside if initialLevel- 9 " );
   				document.networkFaultReportForm.initialSelectBox.value="notRequiredForCircleAdmin";
     			}
     		
    		var intSelectBox= document.networkFaultReportForm.initialSelectBox.value;
     		if( intSelectBox!= "" && intSelectBox!="notRequiredForCircleAdmin"){
  				// alert("inside value empty");
  
    				selectbox=document.getElementById("level_3");
		
					var circles='';
	
					for(i=selectbox.options.length-1;i>0;i--)
						{
	
						if(selectbox.options[i].selected){	
						//alert(selectbox.options[i].value)
							//	document.networkFaultReportForm.circles[j].value  = selectbox.options[i].value;
		
							 circles = circles+selectbox.options[i].value+',';
							}
						}   
				}



    //document.networkFaultReportForm.action = urlString  
   // document.networkFaultReportForm.initialSelectBox.value="3" ;
    document.networkFaultReportForm.circles.value = circles;
	document.networkFaultReportForm.methodName.value="fetchImpactReport"; 
	document.networkFaultReportForm.method="POST";
	document.networkFaultReportForm.submit();   
	}
	//window.location.href="csrFileReport.do?methodName=agentWiseReport";
else {
return false;
}
}

function validate()
	{   
		myselect=document.getElementById("reportType");
		if(myselect.options[1].selected){
				var today = new Date();
				var dd = today.getDate();
				var mm = today.getMonth()+1;//January is 0!
				var yyyy = today.getFullYear();
				if(dd<10){dd='0'+dd}
				if(mm<10){mm='0'+mm}
				var curr_dt=yyyy+'-'+mm+'-'+dd;
				var date=document.getElementById("date_field").value;
					if(date==''){
						alert("Please Select Date");
		 				return false;
					}
					if(date > curr_dt){
						alert("Selected Date Cannot be a future date");
		 				return false;
					}
		
		}
	else{
		var today = new Date();
		var dd = today.getDate();
		var mm = today.getMonth()+1;//January is 0!
		var yyyy = today.getFullYear();
		if(dd<10){dd='0'+dd}
		if(mm<10){mm='0'+mm}
		var curr_dt=yyyy+'-'+mm+'-'+dd;
	document.getElementById("date_field").value=curr_dt;
	}
	
		return true;
			
	}
	
function showDate(){

mydiv = document.getElementById("dateSelection");
myselect=document.getElementById("reportType");
if (myselect.options[1].selected==true){
	//assigning current date in yyyy-mm-dd format
	if(document.getElementById("date_field").value=="")
	{
 	var currentDate = new Date();
 	var d = new Date();
	var curr_date = currentDate.getDate();
	var curr_month = currentDate.getMonth();
	curr_month++;
	if(curr_date<10){curr_date='0'+curr_date}
		if(curr_month<10){curr_month='0'+curr_month}
	var curr_year = currentDate.getFullYear();
	currentDate = curr_year + "-" + curr_month  + "-" + curr_date;
	// daily report
	document.getElementById("date_field").value=currentDate;
	}

	mydiv.style.display = "block"; //to show it
	// alert(currentDate);
}
else {
mydiv.style.display = "none"; //to hide it
document.getElementById("date_field").value="";
}

}	

function $id(id) {
	return document.getElementById(id);
}

function importExcel(){
 $id("parentId").value=parentId;
document.networkFaultReportForm.methodName.value="networkImpactExcelReport";
document.networkFaultReportForm.submit();
}

</SCRIPT>

<script language="JavaScript" src="jScripts/KmValidations.js"
	type="text/javascript"> </script>

<script language="JavaScript" type="text/javascript"> </script>
<html:form action="/networkFaultReport" > 
	<html:hidden property="methodName" /> 
	<html:hidden property="parentId" styleId="parentId"/>
	<html:hidden property="levelCount" styleId="levelCount"/>
	<html:hidden property="elementLevel" styleId="elementLevel"/>
	<html:hidden property="initStatus" />
	<html:hidden property="elementId"/>

	
	<html:hidden property="circles" />
	<div class="box2">
        <div class="content-upload">

<h1><bean:message key="networkImpactReport.title" /> </h1>
	<table width="100%" class="mTop30" align="center">
		
            <tr align="left">
				<td colspan="4">
				<table id="table_0"  cellpadding="10px" cellspacing="10px">
					<tbody>
				<logic:notPresent name="superAdmin" scope="request">
				<html:hidden property="initialSelectBox" styleId="initialSelectBox"/>
				</logic:notPresent>
				<logic:present name="superAdmin" scope="request">
						<tr>
							<td style="width:160px" align="left"><strong>
								<bean:message	key="networkImpactReport.select.lob" /></strong> </td>
							<td style="width:238px" align="left" class="pBot2"><span class="width150">
							<!--Change start by Vishi   -->
							<html:select property="initialSelectBox" styleId="initialSelectBox"  onchange="javascript:loadDropdown(this);">
							<!--Change end by Vishi   -->
								<html:option value=""><bean:message key="networkFaultReport.select.allLob" /></html:option>
								<logic:present name="firstList" scope="request">
									<html:options collection="firstList" property="elementId"
										labelProperty="elementName" />
								</logic:present>
							</html:select> </span></td>
						</tr>
					</logic:present>									
					</tbody>
				</table>
				</td>
			</tr>
					
				  	<tr>
						<td colspan="2" class="pTop4 pLeft10">
							  	<table width="100%" border="0" cellpadding="5" cellspacing="0" class="text">
									<tr align="left">
										<td class=""><font color="#0B8E7C"><strong>
										<html:messages id="msg" message="true">
               								<bean:write name="msg"/>  
             							</html:messages></strong></font></td>
									</tr>
									<tr><div  id="myDiv"></div></tr>
								</table>
							
						</td>
					</tr>
					<tr>
						<td colspan="2" class="pTop4 pLeft10">
								<table width="100%" border="0" cellpadding="5" cellspacing="0" class="text">
									<tr align="left">
										<td class=""><font color="#0B8E7C"><strong>
										<html:errors/></strong></font>
									</tr>
								
								</table>
								
						</td>	
			
       </table> 
			
			<ul class="list2 form1">
			<li class="clearfix alt">
			<span class="text2 fll width160"><strong><bean:message key="networkImpactReport.report.type" /> </strong></span>&nbsp;&nbsp;
				<html:select styleId="reportType" property="reportType" style="width:125px" onchange="showDate();">
					<html:option value="0" >MTD</html:option>
					<html:option value="1">Daily</html:option>
				</html:select> 
				</li>
			
			<li class="clearfix">
		<div id="dateSelection"><span class="text2 fll"  style="margin-right: 103px;"><strong><bean:message key="networkImpactReport.select.date" /> </strong></span>
				
			<input type="text" class="tcal calender2 fll" id="date_field" readonly="readonly" name="date" value="<bean:write property='date' name='networkFaultReportForm'/>"/>
		</div>
			
				</li>
		<li class="clearfix alt" style="padding-left:170px;">
			<span class="text2 fll">&nbsp;&nbsp;&nbsp;&nbsp;</span><INPUT type="Image"
				src="images/submit.jpg" onclick="return listUser();""/>
		</li></ul>

       
 <script>
	showDate();
 </script>  
       </div>
       </div>
</html:form>
<table width="80%" class="mTop30" align="center">
	<logic:present name="IMPACT_REPORT">	
	
	

	
				<tr align="right" >
							<td width="147" class="pLeft10 pTop2"></td>
							<td width="636" class="pTop2" colspan="5"><img  src="images/excel.gif" width="59" height="35" border="0" onclick="importExcel();" /></td>
        </tr>	
				
				
				
				<tr>
					<td  colspan="7" align="left" width="90%"><B><bean:message key="networkImpactReport.search.Location"/> :&nbsp;&nbsp;<bean:write name="networkFaultReportForm" property="elementPath" />
		&nbsp;&nbsp;&nbsp;  <bean:write name="networkFaultReportForm" property="reportName" /></B>
		</td>				
				</tr>
				
				
				<tr>

			<td
				style=" width:35px; background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;"
				height="35"><span class="mLeft5 mTop5">S.No</span></td>
			<td
				style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;"
				height="35"><span class="mLeft5 mTop5">Circle </span></td>
			
			<td
				style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;"
				height="35"><span class="mLeft5 mTop5">Total Time (HH:MM)</span></td>
		</tr>
              	 <logic:notEmpty name="IMPACT_REPORT">
              	 <bean:define id="userList" name="IMPACT_REPORT" type="java.util.ArrayList"  />
				<logic:iterate name="userList" id="file" indexId="i" type="com.ibm.km.dto.NetworkFaultReportDto">
			
				

					<TR class="lightBg">

					<TD width="35px" class="text" align="left"><%=(i.intValue() + 1)%>.</TD>

					<TD width="80px" class="text" align="left"><bean:write
						name="file" property="circleName" /></TD>
								
					<TD width="75px" class="text" align="left"><bean:write
						name="file" property="tatHours" />:<bean:write name="file"
						property="tatMinutes" /></TD>


				</TR>
				</logic:iterate>
						
		</logic:notEmpty>
			<logic:empty name="IMPACT_REPORT"  >
				<TR class="lightBg">
				<TD colspan="2" class="error" align="left"><FONT color="red"><bean:message
					key="networkImpactReport.noRecords" /></FONT></TD>
			</TR>
			</logic:empty>
		
		</logic:present>	
	</table>

