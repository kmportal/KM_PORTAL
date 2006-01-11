<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="com.ibm.km.dto.KmScrollerMstr" %>

<script language="javascript">
	var path = '<%=request.getContextPath()%>';
	var port = '<%= request.getServerPort()%>';
	var serverName = '<%=request.getServerName() %>';
	var currentLevel;
</script>

<script language="JavaScript" type="text/javascript">
var count;
function method(){
count= parseInt(document.kmScrollerMstrFormBean.scrollerCount.value);
}
function formValidate()
{
		var reg=/^[0-9a-zA-Z_@*!$#%?><=,.:;|+*-{}(}[/\] ]*$/;
	/*	if(document.forms[0].kmActorId.value=="1" ){
			
			if(document.forms[0].initialSelectBox.value=="-1"){
				alert("Please Select the LOB");
				return false;	
			}
	
		}	*/			
			
		if(document.forms[0].startDate.value == "")
		{
			alert("Please Select Start Date" );
			return false;
		}
		if(document.forms[0].endDate.value == "")
		{
			alert("Please Select End Date" );
			return false;
		}
		if(document.forms[0].startDate.value > document.forms[0].endDate.value)
		{
			alert("Start Date Cannot Be Greater Than End Date" );
			return false;
		}
		
		var today = new Date();
		var dd = today.getDate();
		var mm = today.getMonth()+1;//January is 0!
		var yyyy = today.getFullYear();
		if(dd<10){dd='0'+dd}
		if(mm<10){mm='0'+mm}
		var curr_dt=yyyy+'-'+mm+'-'+dd;
		var kmAct = document.forms[0].kmActorId.value;
		
		if(currentLevel == 4 && kmAct == "1")
		{
		  document.forms[0].circleElementId.value = document.getElementById("level_3").value;		
		}  
		 document.forms[0].action="/KM/kmScrollerMstr.do?methodName=viewScroller";
		 document.forms[0].method="post";
		 document.forms[0].submit();
		

}


function scrollerEdit(msgId,message,text,edit,done)
{
 
 if(document.getElementById(edit).value=="edit"){
	
 document.getElementById(edit).value="done"
 document.getElementById(text).disabled=false;
 document.getElementById(edit).disabled = true;
 document.getElementById(done).disabled = false;
  for(var i=1;i<=count;i++)
   {
    if(i!==text)
      {
       document.getElementById("edit" +i).disabled = true;
      }
   }
   document.getElementById(edit).disabled=false;
   }
   else{
   var reg=/^[0-9a-zA-Z_@*!$#%?><=,.:;|+*-{}(}[/\] ]*$/;
   	var msg = document.getElementById(text).value;
   	if(msg=="")
   	{
   	alert("Blank Scroller is not Accepted");
   	return false;
   	}else if(!reg.test(msg))
		{
		alert("Please avoid using apostrophes in message box");
		return false;	
	   }	
   	else{
   	 document.getElementById(edit).value="edit"
	var url="kmScrollerMstr.do?methodName=editScroller&&messageId="+msgId+"&message="+msg;
	if(window.XmlHttpRequest) {
		req = new XmlHttpRequest();
		
	}else if(window.ActiveXObject) {
		req=new ActiveXObject("Microsoft.XMLHTTP");
		
	}
	if(req==null) {
		alert("Browser Doesnt Support AJAX");
		return;
	}
	req.onreadystatechange = getOnChange;
	req.open("POST",url,true);
	req.send();
   }
   }
 }

 function scrollerDone(msgId,message,row)
{
	var msg = document.getElementById(row).value;
	var url="kmScrollerMstr.do?methodName=editScroller&&messageId="+msgId+"&message="+msg;
	if(window.XmlHttpRequest) {
		req = new XmlHttpRequest();
		
	}else if(window.ActiveXObject) {
		req=new ActiveXObject("Microsoft.XMLHTTP");
		
	}
	if(req==null) {
		alert("Browser Doesnt Support AJAX");
		return;
	}
	req.onreadystatechange = getOnChange;
	req.open("POST",url,true);
	req.send();
	
}

function scrollerDelete(msgId,row)
{    
var answer = confirm("Do you want continue?")
if (answer)
{
	document.forms[0].action="/KM/kmScrollerMstr.do?methodName=delete&messageId="+msgId;
 	document.forms[0].submit();
}
else{}
}
function getOnChange()
{
	var scrollerMessage;
	if (req.readyState==4 || req.readyState=="complete") 
	{
		var alertResponse=req.responseText;
		
		if(parseInt(alertResponse)== 0){
			alert("Scroller not edited. Please edit again");
		}
	 for(var i=1;i<=count;i++)
   {
    
     document.getElementById("edit" +i).disabled = false;
     document.getElementById("done" +i).disabled = true;
     document.getElementById(i).disabled=true;
   }
	
	}
	

}


</script>

<script language="JavaScript" type="text/JavaScript">
function limitText(textArea, length) {
if (textArea.value.length > length) {
alert ("Please limit comments length to "+length+" characters.");
textArea.value = textArea.value.substr(0,length);
}
}
 function handleEnter (field, event) {
		var keyCode = event.keyCode ? event.keyCode : event.which ? event.which : event.charCode;
		if (keyCode == 13) {
			var i;
			for (i = 0; i < field.form.elements.length; i++)
				if (field == field.form.elements[i])
					break;
			i = (i + 1) % field.form.elements.length;
			return false;
		} 
		else
		return true;
}

var req=null; 
var ctr=1;

var levelCount=0;
var initialElementPath=<%= request.getAttribute("allParentIdString") %>
var elementPath = initialElementPath;
var initialParentId=<bean:write property="parentId" name="kmScrollerMstrFormBean"/>;
var parentId=initialParentId;
var initialLevel=<bean:write property="initialLevel" name="kmScrollerMstrFormBean"/>;
var parentLevel=initialLevel-1;
var elementLevelNames=new Array();
var childLevel;
var lk=0;
var emptyChildFlag=0;
var flag=0;

elementLevelNames[0] = 'Document';
elementLevelNames[1] = 'Country';
elementLevelNames[2] = 'LOB';
elementLevelNames[3] = 'Circle';
elementLevelNames[4] = 'Category';
elementLevelNames[5] = 'Sub-Category';
elementLevelNames[5] = 'SubSub-Category';

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
	flag=0;
	var countLevel=levelCount;
	e= e || event;
	var tempLevelCount=levelCount;
	
	if ((e.srcElement || e.target).id!="initialSelectBox") {
		var elementId=(e.srcElement || e.target).id;
		var currentElementLevel=elementId.substring(6);
		var table=$id("table_0");
		parentLevel=currentElementLevel;
		for (i=parseInt(currentElementLevel)+1;i<=parseInt(levelCount);i++) {
			table.tBodies[0].removeChild($id("tr_level_"+i));
			elementPath=elementPath.substring(0,elementPath.lastIndexOf("/"));
			tempLevelCount--;
		}
		if(emptyChildFlag==1){
			elementPath=elementPath.substring(0,elementPath.lastIndexOf("/"));
			emptyChildFlag=0;
		}
	}
	else if ((e.srcElement || e.target).id=="initialSelectBox"){
		var table=$id("table_0");
		elementPath=initialElementPath;
		parentLevel=initialLevel;
		for (i=initialLevel+1;i<=levelCount;i++) {
			table.tBodies[0].removeChild($id("tr_level_"+i));
			tempLevelCount--;
		}
	}
	levelCount=tempLevelCount;
	
	parentId=$id((e.srcElement || e.target).id).value;
		
	if (parentId!="") {
		req = newXMLHttpRequest();
	    if (!req) {
	        alert("Your browser does not support AJAX! Add Files module is accessible only by browsers that support AJAX. " +
	              "Please check the KM requirements and contact your System Administrator");
	        return;
	    }
	    
	    req.onreadystatechange = returnJson;
	    elementPath=elementPath+"/"+parentId;
	    var url=path+"/kmScrollerMstr.do?methodName=loadElementDropDown&ParentId="+parentId+"&Dummy="+new Date().getTime();
	    
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
            var json = eval('(' + req.responseText + ')');
            
			var elements = json.elements;
			level = json.level;
			
			
			//Bug resolved 
			if(window.element){
			}
			else{
				level1=parentLevel;
			}
			childLevel=level;
			var maxlevel=elementLevelNames.length-1;
			currentLevel=parseInt(parentLevel)+1;
			
			if(currentLevel>3)
			{
			 	return;
			}
			
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
				addOption("-2",'ALL Circles', selectDropDown);
				for (var i = 0; i < elements.length; i++) {
		            addOption(elements[i].elementId, elements[i].elementName, selectDropDown);
		        }
		        var table=$id("table_0");
		        var tr=document.createElement("tr");
		        var sp=document.createElement("span");
		        sp.setAttribute("class","text2 fll width160");
		        var td1=document.createElement("td");
		        var td2=document.createElement("td");
		        var trId="tr_"+id;
		        tr.setAttribute("id",trId);
		        td2.appendChild(selectDropDown);
		        var text="Select "+elementLevelNames[level];
		        var newNode = document.createTextNode(text);
		        sp.appendChild(newNode);
		        td1.appendChild(sp);
   		        table.tBodies[0].appendChild(tr);
		        tr.appendChild(td1);
		        tr.appendChild(td2);
		        emptyChildFlag=0;
		    }else{
				levelCount=level1;
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
<html:form action="/kmScrollerMstr" > 
 <html:hidden name="kmScrollerMstrFormBean" property="kmActorId" />
 <html:hidden property="viewEditFlag" />
 <html:hidden name="kmScrollerMstrFormBean" property="scrollerCount" />
 <html:hidden name="kmScrollerMstrFormBean" property="circleElementId"/>
 <div class="box2">
  <div class="content-upload">
   <h1><bean:message key="scroller.view" /></h1>
	<TABLE align="center"> 
	   <TBODY>
			<tr>
				<td colspan="2" align="center" class="error"><strong><html:messages id="msg" message="true"><bean:write name="msg"/></html:messages></strong></td>
			</tr>
			<tr>
			 	<td colspan="2" align="center" class="error"><strong><html:errors/></strong></td>
			</tr>
		</TBODY>
	</TABLE	>
	<ul class="list2 form1">
	  <logic:notEqual name="kmScrollerMstrFormBean" property="kmActorId" value="2">
		<li class="clearfix">
		  <table id="table_0">
			<tr>
			  <td><span class="text2 fll width160">Select <script type="text/javascript">document.write(elementLevelNames[initialLevel]);</script></span> </td>
			  <td> <!--Change start by Vishi   -->
			  <html:select property="initialSelectBox" styleId="initialSelectBox" onchange="javascript:loadDropdown(event);" styleClass="select1">
			  <!--Change end by Vishi   -->
		             <logic:equal name="kmScrollerMstrFormBean" property="kmActorId" value="1">
		                  	<html:option value="-1">Pan India</html:option>
		             </logic:equal>
		             <logic:equal name="kmScrollerMstrFormBean" property="kmActorId" value="5">
		                	<html:option value="-1">ALL Circles </html:option>
		             </logic:equal>
		             <logic:present name="firstList" scope="request">
		                   	<html:options collection="firstList" property="elementId" labelProperty="elementName" />
		             </logic:present>
		           </html:select>
		      </td>
		    </tr>
	     </table>    
		</li>
	  </logic:notEqual>	
	  <li class="clearfix alt" >
			<span class="text2 fll" style="width:165px"><strong><bean:message key="scroller.startDate"/></strong></span>							
				<span class="text2 fll width180">
				<input type="text" class="tcal calender2 fll" readonly="readonly" name="startDate" value="<bean:write property='startDate' name='kmScrollerMstrFormBean'/>"/> </span>
			<span class="text2 fll" style="margin-left:40px;width:165px "><strong><bean:message key="scroller.endDate"/></strong></span>			
				<span class="text2 fll width180">
				<input type="text" class="tcal calender2 fll" readonly="readonly" name="endDate" value="<bean:write property='endDate' name='kmScrollerMstrFormBean'/>"/> </span>
		</li>
		<li class="clearfix" style="padding-left:170px;">
			<span class="text2 fll">&nbsp;</span>
				<img src="images/submit.jpg"  onclick="return formValidate();"/>
              </li>
		</ul>
	<table class="mTop30" >
		<logic:notEqual name="kmScrollerMstrFormBean" property="initStatus" value="true">
			<tr class="textwhite">
				<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5"><bean:message key="feedback.SNO" /></span></td>
				<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5">Scroller Message</span></td>
				<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5">Start Date</span></td>
				<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5">End Date</span></td>
			</tr>
			<logic:notEmpty name="kmScrollerMstrFormBean" property="scrollerList">
 				<logic:iterate name="kmScrollerMstrFormBean" property="scrollerList" indexId="i" id="report">
		  		 <% if (i % 2 == 0) {%>
				  <tr style="background-color: #FAF8F8;">  
				 <%} else {%>
          	     <tr>
            	 <%} %>
					<TD class="text" align="center" width="20" height="67"><%=(i.intValue() + 1)%>.</TD>
					<TD align="left" height="67"><html:textarea rows="3" styleId='<%=(i.intValue() + 1)+""%>'  cols="30" name="report" property="message" disabled="true" onkeypress="return handleEnter(this, event)" onkeydown="limitText(this,250);" /></td>
					<TD class="text" align="center" width="158" height="67"><bean:write	name="report" property="startDate" /></TD>
					<TD class="text" align="center" width="158" height="67"><bean:write	name="report" property="endDate" /></TD>
				    <TD height="67"><input type="button" class="red-btn fll" id='<%="edit"+(i.intValue() + 1)+""%>' value="edit"  onclick="javascipt:scrollerEdit('<bean:write name="report" property="messageId" />','<bean:write name="report" property="message" />','<%=(i.intValue() + 1)%>','<%="edit"+(i.intValue() + 1)%>','<%="done"+(i.intValue() + 1)%>');"></TD>
					<TD height="67"><input type="button"  class="red-btn fll" style="visibility:hidden;" disabled id='<%="done"+(i.intValue() + 1)+""%>' value="done" onclick="scrollerDone('<bean:write name="report" property="messageId" />','<bean:write name="report" property="message" />','<%=(i.intValue() + 1)%>','<%="done"+(i.intValue() + 1)%>');" ></TD>
					<TD height="67"><input type="button" class="red-btn fll"  id='<%="delete"+(i.intValue() + 1)+""%>' value="delete"  onclick="javascipt:scrollerDelete('<bean:write name="report" property="messageId" />','<%=(i.intValue() + 1)%>')"></TD>	

					<script language="javascript">
							var endDate = '<bean:write name="report" property="endDate" />';
		  					var today = new Date();
							var dd = today.getDate();
							var mm = today.getMonth()+1;//January is 0!
							var yyyy = today.getFullYear();
							if(dd<10){dd='0'+dd}
							if(mm<10){mm='0'+mm}
							var curr_dt=yyyy+'-'+mm+'-'+dd;
				
						if(endDate < curr_dt)
						{
						 document.getElementById('<%="edit"+(i.intValue() + 1)+""%>').style.display = 'none';
						  document.getElementById('<%="delete"+(i.intValue() + 1)+""%>').style.display = 'none';		
						}	
			
				   </script>
				 </TR>
			  </logic:iterate>
			</logic:notEmpty>
			<logic:empty name="kmScrollerMstrFormBean" property="scrollerList">
				<TR class="lightBg">
				<TD colspan="2" class="error" align="left"><FONT color="red"><bean:message key="viewAllFiles.NotFound" /></FONT></TD>
				</TR>
	  		</logic:empty>	
	 </logic:notEqual>
	</table>
	<script language="javascript">
	method();
	</script>
 </div>
</div>
</html:form>