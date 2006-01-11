<%
try{
%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<logic:notEmpty name="HIT_COUNT_LIST">
<bean:define  id="hitCountList" name="HIT_COUNT_LIST" type="java.util.ArrayList" scope="request"/>

</logic:notEmpty>
<SCRIPT language="Javascript">

function formSubmit() {
    var circleId=document.hitCountReportBean.initialSelectBox.value;
	$id("parentId").value=parentId;		
	document.hitCountReportBean.methodName.value="hitCountReport";
	//document.hitCountReportBean.submit();
	return true;	 
 }

	var path = '<%=request.getContextPath()%>';
	var port = '<%= request.getServerPort()%>';
	var serverName = '<%=request.getServerName() %>';


var req=null; 
var ctr=1;

var levelCount=0;
var initialElementPath='<bean:write scope="request" name="allParentIdString"/>';
var elementPath=initialElementPath;
var initialParentId=<bean:write property="parentId" name="hitCountReportBean"/>;
var parentId=initialParentId;
var initialLevel=<bean:write property="initialLevel" name="hitCountReportBean"/>;
var parentLevel=initialLevel-1;
var elementLevelNames=new Array();
var childLevel;
var lk=0;
var emptyChildFlag=0;

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
		//alert("Final Element Path after cropping: "+elementPath);
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
	    var url=path+"/elementAction.do?methodName=loadElementDropDown&ParentId="+parentId+"&Dummy="+new Date().getTime();
	    //alert(url);
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
	            selectDropDown.setAttribute("class","select1");
				if (selectDropDown.addEventListener){
					selectDropDown.addEventListener('change', loadDropdown, false); 
				} else if (selectDropDown.attachEvent){
					selectDropDown.attachEvent('onchange', loadDropdown);
				}
				addOption("",'Select', selectDropDown);
				for (var i = 0; i < elements.length; i++) {
		            addOption(elements[i].elementId, elements[i].elementName, selectDropDown);
		        }
		        var table=$id("table_0");
		        var tr=document.createElement("tr");
		        var td1=document.createElement("td");
		        var td2=document.createElement("td");
		        var bold=document.createElement("b");
		        var trId="tr_"+id;
		        tr.setAttribute("id",trId);
		        td2.appendChild(selectDropDown);
		        var text="Select "+elementLevelNames[level];
		        var newNode = document.createTextNode(text);
		        td1.setAttribute("align","left");
		        td1.setAttribute("width","160px");
		        td1.setAttribute("class","clearfix");
		        bold.appendChild(newNode);
		        td1.appendChild(bold);
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
function importExcel(){
	$id("parentId").value=parentId;
	document.hitCountReportBean.methodName.value="HitCountExcelReport";
	document.hitCountReportBean.submit();
}
</script>

<html:form action="/hitCountReport">
<html:hidden property="methodName" name="hitCountReportBean"/>
<html:hidden property="documentPath" styleId="documentPath" name="hitCountReportBean"/>
<html:hidden property="elementId" name="hitCountReportBean"/>
<html:hidden property="parentId" styleId="parentId"/>
<html:hidden property="elementPath" name="hitCountReportBean" />

 <div class="content-upload">
  <div class="box2">
		<h1>Document Hits Count Report</h1> 

	   <table align="center">
		    <tr>
				<td colspan="2" align="center" class="error">
					<strong> 
          			<html:messages id="msg" message="true">
                 		<bean:write name="msg"/>  
             		</html:messages>
            		</strong>
            	</td>
			</tr>
				<tr>
				<td colspan="2" align="center" class="error">
					<strong> 
          			<html:errors/>
            		</strong>
            	</td>
			</tr>
			
		</table>
			<ul class="list2 form1">
			  <li class="clearfix">	
				<span class="text2 fll" style="width:177px; "><b>Select Date </b></span>
				<span class="text2 fll width180">
   				  <input type="text" class="tcal calender2 fll"  readonly="readonly" name="date" value="<bean:write property='date' name='hitCountReportBean'/>"/>
                </span>
              </li>
            </ul>
                     
              <table id="table_0" class="form1"  cellspacing="15px" style="margin-top: -5px;">
                   <tbody>
					<tr> 
					  <td class="width160 text2" align="left" valign="middle"><b>Select <script type="text/javascript">document.write(elementLevelNames[initialLevel]);</script></b></td>
					  <td align="left" class="pBot2">
	                       <!--Change start by Vishi   -->
	                       	<html:select property="initialSelectBox" styleClass="select1"  styleId="initialSelectBox" onchange="javascript:loadDropdown(event);">
	                       	<!--Change end by Vishi   -->
	                        	<html:option value="">Select</html:option>
	                        	<logic:present name="firstList" scope="request">
	                        	<html:options collection="firstList" property="elementId" labelProperty="elementName" />
	                        	</logic:present>
	                        </html:select>	                      
                      </td>
                    </tr>
                   </tbody>
                  </table>
                  <br>
				 <div class="fll" style="margin-left: 200px; margin-bottom: 20px;">	                    
                      <input class="submit-btn1 red-btn" type="submit" value="" onclick="return formSubmit();" />
                 </div>     
                 
      <table width="100%"  cellpadding="0" cellspacing="0">
        <logic:present name="HIT_COUNT_LIST">
   		   <tr align="right" >
				<td width="147" class="pLeft10 pTop2"></td>
				<td class="pTop2" colspan="5"><img  src="images/excel.gif" width="39" height="35" border="0" onclick="importExcel();" />&nbsp; &nbsp;</td>
           </tr>	     
           <tr class="lightBg">
		     <td  colspan="10" align="left" height="40"><FONT size="2"><B>Search Location : </B><bean:write name="hitCountReportBean" property="elementPath"/></FONT></td>
		   </tr>
		   <tr class="textwhite">
			<td width="10%"  style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5">SNo.</span> </td>
			<td width="40%" style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class="mTop5">Document Name</span></td>
			<td width="40%" style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class="mTop5">Document Path</span></td>
			<td width="10%" style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class="mTop5">No. of Hits</span></td>
		   </tr>
		<logic:empty name="HIT_COUNT_LIST"  >
			<TR class="lightBg">
				<TD colspan="2" class="error" align="left"><FONT color="red"><bean:message	key="viewAllFiles.NotFound" /></FONT></TD>
			</TR>
		</logic:empty>
		
		<logic:notEmpty name="HIT_COUNT_LIST">
				<logic:iterate name="hitCountList" id="hitReportList" indexId="i" type="com.ibm.km.dto.KmHitCountReportDto">
			
			<%
				String cssName = "";				
				if( i%2==1)
				{			
				cssName = "alt";
				}	
				%>
		    <tr class="<%=cssName%>">					
			<TD>&nbsp;<%=(i.intValue() + 1)%>.&nbsp;</TD> 
			<TD height="35"><bean:write name="hitReportList" property="documentName" /></TD>
			<TD><bean:write name="hitReportList" property="documentPath" /></TD>
			<TD><bean:write name="hitReportList" property="noHits" /></TD>
			
			</tr>
			</logic:iterate>
		</logic:notEmpty>
		</logic:present>
		</table>
    </div>
   </div>
	</html:form>

<%}catch(Exception e){
e.printStackTrace();
} %>