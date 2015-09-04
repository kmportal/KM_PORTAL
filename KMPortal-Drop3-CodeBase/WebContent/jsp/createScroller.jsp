<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="com.ibm.km.dto.KmScrollerMstr" %>
<LINK href=./jsp/theme/css.css rel="stylesheet" type="text/css">
<LINK href="theme/text.css" rel="stylesheet" type="text/css">
<script language="JavaScript" src="jScripts/KmValidations.js" type="text/javascript"> </script>
<script language="javascript">
	var path = '<%=request.getContextPath()%>';
	var port = '<%= request.getServerPort()%>';
	var serverName = '<%=request.getServerName() %>';
</script>

<script language="JavaScript" type="text/javascript">
function formValidate()
{
		
		var today = new Date();
		var dd = today.getDate();
		var mm = today.getMonth()+1;//January is 0!
		var yyyy = today.getFullYear();
		if(dd<10){dd='0'+dd}
		if(mm<10){mm='0'+mm}
		var curr_dt=yyyy+'-'+mm+'-'+dd;
		
        var reg=/^[0-9a-zA-Z_@*!$#%?><=,.:;|+*-{}}([/\] ]*$/;
		
		if(document.kmScrollerMstrFormBean.message.value=="")
		{
        alert("Please Enter the Message");
        return false;
        }else if(document.kmScrollerMstrFormBean.startDate.value < curr_dt || document.kmScrollerMstrFormBean.endDate.value < curr_dt)
		{
			alert("Date Cannot Be Past Date" );
			return false;
		}
		else if((document.kmScrollerMstrFormBean.startDate.value) > (document.kmScrollerMstrFormBean.endDate.value))
		{
			alert("EndDate should not be less than StartDate" );
			return false;
		}else if(!reg.test(document.forms[0].message.value))
		{
		alert("Please avoid using apostrophes in message box");
		return false;	
	   }	
        else{
		//document.forms[0].circleElementId.value = document.getElementById("level_3").value;
		document.forms[0].methodName.value = "insert";

		document.forms[0].submit();
		}
}

function resetFields()
{	
   if(document.forms[0].kmActorId.value=='1'||document.forms[0].kmActorId.value=='5')
    {
	document.forms[0].initialSelectBox.value="-1";
	}
	document.forms[0].message.value="";
	document.forms[0].message.focus();
	return false;
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

</script>

<script language="JavaScript" type="text/JavaScript">
function limitText(textArea, length) {
if (textArea.value.length > length) {
alert ("Please limit comments length to "+length+" characters.");
textArea.value = textArea.value.substr(0,length);
}
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
elementLevelNames[6] = 'SubSub-Category';
elementLevelNames[7] = 'SSS Category';
elementLevelNames[8] = 'SSSS Category';
elementLevelNames[9] = 'SSSSS Category';

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
//<!--Change start by Vishi   -->
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
//		alert("parentId:"+parentId);
		document.forms[0].circleElementId.value = parentId;
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
		
	    var url=path+"/kmScrollerMstr.do?methodName=loadElementDropDown&ParentId="+parentId+"&Dummy="+new Date().getTime();
	    //alert(url);
	    req.open("GET", url, true);
	    req.send(null);
	}else if(parentId==""){
		//showTextBoxes();
		//changeTrLabel(parentLevel);
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
			
			
			//Bug resolved 
			if(window.element){
			}
			else{
				level1=parentLevel;
			}
			childLevel=level;
			var maxlevel=elementLevelNames.length-1;
			var currentLevel=parseInt(parentLevel)+1;
			
		
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
				addOption("-2",'ALL', selectDropDown);
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
		        td1.setAttribute("height",30);
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

<html:form action="/kmScrollerMstr"  >
<html:hidden name="kmScrollerMstrFormBean" property="methodName" value="insert" />
<html:hidden name="kmScrollerMstrFormBean" property="kmActorId"/>
<html:hidden name="kmScrollerMstrFormBean" property="viewEditFlag"/>
<html:hidden name="kmScrollerMstrFormBean" property="circleElementId"/>

	<div class="box2">
     <div class="content-upload">
	<H1><bean:write name="kmScrollerMstrFormBean" property="viewEditFlag" /></H1>
	<TABLE  width="100%" align="center" cellspacing="0"> 
			<TBODY>
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
		</TBODY>
	</TABLE>
		
			
		<ul class="list2 form1">
		
		<logic:notEqual name="kmScrollerMstrFormBean" property="kmActorId" value="2">
			<logic:notEqual name="kmScrollerMstrFormBean" property ="viewEditFlag" value="Edit Scroller">
				<li class="clearfix">	
	                    <table id="table_0" >
							<tr>
						  <td height="30"> <span class="text2 fll width160">Select <script type="text/javascript">document.write(elementLevelNames[initialLevel]);</script>
							</span>		</td> 
							  <td>
							  	<!--Change start by Vishi   -->
							  	<html:select property="initialSelectBox" styleId="initialSelectBox"  styleClass="select1"  onchange="javascript:loadDropdown(event);" >
							  	<!--Change start by Vishi   -->
				               <logic:equal name="kmScrollerMstrFormBean" property="kmActorId" value="1">
				               	<html:option value="-1">Pan India</html:option>
				              </logic:equal>
				              <logic:equal name="kmScrollerMstrFormBean" property="kmActorId" value="5">
				               	<html:option value="-1">ALL</html:option>
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
			</logic:notEqual>
		
			<li class="clearfix alt">
			<span class="text2 fll width160"><strong><bean:message key="scroller.message"/></strong></span>
				 <p class="clearfix fll margin-r20"> <span class="textbox6-inner">
				 <html:textarea styleClass="textarea1" property="message" name="kmScrollerMstrFormBean" cols="35" rows="3" onkeypress="return handleEnter(this, event)" onkeydown="limitText(this,250);"></html:textarea>
				<html:hidden property="messageId"/>
				 </span> </p>
			</li>
			
			<li class="clearfix" >
				<span class="text2 fll" style="width:160px"><strong><bean:message key="scroller.startDate"/></strong></span>							
				<span class="text2 fll width180">
				<input type="text" class="tcal calender2 fll"  readonly="readonly" name="startDate" value="<bean:write property='startDate' name='kmScrollerMstrFormBean'/>"/> </span>
				<span class="text2 fll" style="margin-left:40px;width:160px "><strong><bean:message key="scroller.endDate"/></strong></span>			
				<span class="text2 fll width180">
				<input type="text" class="tcal calender2 fll"  readonly="readonly"  name="endDate" value="<bean:write property='endDate' name='kmScrollerMstrFormBean'/>"/> </span>
			</li>
        </ul>

 		<div class="button-area">
       <div class="button"><input class="submit-btn1 red-btn" name="" value="" onclick="javascript:return formValidate();" />       </div>
       <div class="button"> <a  class="red-btn" onclick="return resetFields();">clear</a>        </div>
      </div>
      
      
	</div>
	<jsp:include page="Disclaminer.jsp"></jsp:include>
	</div>
</html:form>