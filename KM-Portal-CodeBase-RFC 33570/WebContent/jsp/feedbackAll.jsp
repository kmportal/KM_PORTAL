<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<SCRIPT>



function timer(){
	getAlerts()
	window.setTimeout('timer()',30000)
	}

function getAlerts()
{
	var url="kmAlertMstr.do?methodName=view";
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
function getOnChange()
{
	if (req.readyState==4 || req.readyState=="complete") {
	if(req.responseText=="none")
	{
	
	}
	else{
	//alert("YOU HAVE A NEW MESSAGE");
	document.getElementById("alert").value = req.responseText;
	
	}
}
}

function viewResponse()
{

window.location.href="viewFeedbackResp.do?methodName=initResp";
}
function viewResponseAll()
{

window.location.href="viewFeedbackResp.do?methodName=initRespAll";
}


</SCRIPT>

<script language="JavaScript" src="jScripts/KmValidations.js"
	type="text/javascript">
</script>
<script language="javascript">  
var circle;
function formValidate(){
	$id("parentId").value=parentId;
	$id("elementLevel").value=parseInt(parentLevel)+1;
	if(document.kmFeedbackMstrFormBean.comment.value==""||isEmpty(document.kmFeedbackMstrFormBean.comment)){
		alert("Please Enter Comment");
		document.kmFeedbackMstrFormBean.comment.focus();
		return false;
	}else{
		document.kmFeedbackMstrFormBean.methodName.value = "insert";
	}
}
function validate(){
	var form=document.forms[0];
	if(form.comment.value==""){
		alert("Please Enter Comment");
		form.comment.focus();
		return false;
	}	
	if(isEmpty(form.comment)){
		alert("Please Enter Comment");
		form.comment.value="";
		form.comment.focus();
		return false;
	}
	return true;
}
</script>
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
var initialParentId=<bean:write property="parentId" name="kmFeedbackMstrFormBean"/>;
var parentId=initialParentId;
var initialLevel=<bean:write property="initialLevel" name="kmFeedbackMstrFormBean"/>;
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

function loadDropdown(selectBox,e)
{
//alert("Inside loadDropDown");
	e= e || event;
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
			table.removeChild($id("tr_level_"+i));
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
			table.removeChild($id("tr_level_"+i));
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
            //alert("inside returnjson");
			var elements = json.elements;
			level = json.level;
			childLevel=level;
			//alert("inside returnjson"+level);
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
	            selectDropDown.setAttribute("class","width160 select1");
				if (selectDropDown.addEventListener){
					selectDropDown.addEventListener('change', loadDropdown, false); 
				} else if (selectDropDown.attachEvent){
					selectDropDown.attachEvent('onchange', loadDropdown);
				}
				addOption("",'Select', selectDropDown);
				for (var i = 0; i < elements.length; i++) {
		            addOption(elements[i].elementId, elements[i].elementName, selectDropDown);
		        }
		        //alert("creating elements1"+level);
		        var ul=$id("table_0");
		        var li=document.createElement("li");
		        
		        //var td1=document.createElement("td");
		        //var td2=document.createElement("td");
		        var trId="tr_"+id;
		        li.setAttribute("id",trId);
		        li.setAttribute("class","clearfix");
		        
		        div=document.createElement("div");
		        div2=document.createElement("div");
		        div.setAttribute("class","width160 fll");		        
		        //li.appendChild(selectDropDown);
		        //alert("creating elements2"+level);
		        //td2.appendChild(selectDropDown);
		        var text="Select "+elementLevelNames[level];
		        var newNode = document.createTextNode(text);
		        // alert("creating elements3");
		        //td1.setAttribute("align","right");
		        //td1.appendChild(newNode);
		        div.appendChild(newNode);
		        li.appendChild(div);
		        div2.appendChild(selectDropDown);
		        li.appendChild(div2);
		        //alert("creating elements4");
   		        //ul.appendChild(li);
   		        ul.insertBefore(li,ul.children[level-2]);
   		        //alert("creating elements5");
		        //tr.appendChild(td1);
		        //tr.appendChild(td2);
		        emptyChildFlag=0;
		        //alert("creating elements");
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

<script language="JavaScript" type="text/JavaScript">
function limitText(textArea, length) {
if (textArea.value.length > length) {
alert ("Please limit comments length to "+length+" characters.");
textArea.value = textArea.value.substr(0,length);
}
}
</script>



<h2 class="new">feedback</h2>
      <div id="feedback-tab" style="margin-bottom: 0px " >
      	<ul class="list12 clearfix" style="margin-bottom: 0px">
        	<li><a href="#feedback-tab1">Give Feedback</a></li>
            <li><a href="#feedback-tab2">My Feedback</a></li>
            <li><a href="#feedback-tab3">All Feedback</a></li>
        </ul>
      </div>
        
<div class="box9">
 <div id="feedback-tab1" class="feedback-common inner clearfix">  
  <html:form action="/kmFeedbackMstr" >
	<html:hidden property="methodName" value="insert"/> 
	<html:hidden property="parentId" styleId="parentId"/>
	<html:hidden property="levelCount" styleId="levelCount"/>
	<html:hidden property="elementLevel" styleId="elementLevel"/>
	<ul id="table_0" class="form1">
		<li id="" class="clearfix">
			<div class="width160 fll">
				<label>Select <script type="text/javascript">document.write(elementLevelNames[initialLevel]);</script> </label>
			</div>
			<div>
				<html:select property="initialSelectBox" styleClass="width160 select1" styleId="initialSelectBox" onchange="javascript:loadDropdown(this);">
                        	<html:option value="">Select</html:option>
                        	<logic:present name="firstList" scope="request">
                        	<html:options collection="firstList" property="elementId" labelProperty="elementName" />
                        	</logic:present>
        		</html:select>
        	</div>
		</li>
		
		<li id="" class="clearfix"  >
			<div class="width160 fll">
             <label >OLM ID</label>
            </div>
            <div> 
 			  <html:textarea styleClass="textarea2" property="olm_id" name="kmFeedbackMstrFormBean" readonly="true" disabled="true"></html:textarea>
 		    </div>	
        </li>
		
		
		
	
		
		
		
		<li id="" class="clearfix"  >
			<div class="width160 fll">
             <label >Your comments<span class="required">*</span>:</label>
            </div>
            <div> 
 			  <html:textarea styleClass="textarea2" property="comment" name="kmFeedbackMstrFormBean" cols="25" rows="8" onkeydown="limitText(this,350);"></html:textarea>
 		    </div>	
        </li>
		<li class="clearfix">
		<div class="button" style="margin-left: 160px">
			<input src="images/submit.jpg" type="image" onclick="return formValidate();"/>
		</div>
		</li>
	
						<li>	<html:messages id="msg" message="true">
               				<bean:write name="msg"/>  
                    		</html:messages>
						</li>
						<li>
								<html:errors/>
						</li>
		</ul>
	</html:form>
</div>

<div id="feedback-tab2" class="feedback-common inner clearfix">
	<table class="table10" cellspacing="3px">
		<thead>
		   	<tr>
		       	<th class="first" style="width: 40px"><bean:message key="feedback.SNO" /></th>
		           <th class="second" style="width: 180px">Feedback Comment</th>
		           
		           <th class="third" style="width: 90px">OLM ID</th>
		           
		           <th class="fourth"  style="width: 170px">Feedback Response</th>
		           <th class="fifth" style="width: 90px">Feedback Created Date </th>
		           <th class="sixth" style="width: 90px">Feedback Updated Date </th>
		           <th class="seventh" style="width: 90px">Status(I/R)</th>
	       </tr>
	   </thead>
	</table>
	<div class="table-box">
       
          <div class="scroll-pane1" style="width: 100%;height: 100% ;overflow:scroll">
            <table class="table11"  cellspacing="3px">
            <tbody>
            <logic:notEmpty name="kmFeedbackMstrFormBean" property="feedbackListMy">
				<logic:iterate name="kmFeedbackMstrFormBean" property="feedbackListMy" id="feedbackResp" indexId="i">
				<%
				String cssName = "clearfix";				
				if( i%2==1)
				{			
				cssName = "clearfix alt";
				}	
				%>
					<tr class="<%=cssName%>">
						<td class="first" style="width: 40px;"><%=(i.intValue() + 1)%>.</TD>
						<td class="second" style="width: 180px ;"><bean:write name="feedbackResp" property="comment" /></TD>
						
						<td class="third" style="width: 90px;"><bean:write name="feedbackResp" property="olm_id" /></TD>
						
						<td class="fourth" style="width: 170px;"><bean:write name="feedbackResp" property="feedbackResponse" /></TD>
						<td class="fifth" style="width: 90px;"><bean:write name="feedbackResp" property="createdDate" /></TD>
						<td class="sixth" style="width: 90px;"><bean:write name="feedbackResp" property="updatedDate" /></TD>
						<td class="seventh" style="width: 90px;"><bean:write name="feedbackResp" property="readStatus" /></TD>
					</tr>
				</logic:iterate>
			</logic:notEmpty>
            </tbody>
           </table>
         </div>
      
   </div>
</div>
         <div id="feedback-tab3" class="feedback-common inner clearfix">
             <table class="table10" cellspacing="3px">
                    	<thead>
                        	<tr>
                            	<th class="first" style="width: 40px"><bean:message key="feedback.SNO" /></th>
                                <th class="second" style="width: 180px">Feedback Comment</th>
                                
                                <th class="third" style="width: 90px">OLM ID</th>
                                
                                <th class="fourth"  style="width: 170px">Feedback Response</th>
                                <th class="fifth" style="width: 90px">Feedback Created Date </th>
                                <th class="sixth" style="width: 90px">Feedback Updated Date </th>
                                <th class="seventh" style="width: 90px">Status(I/R)</th>
                            </tr>
                        </thead>
            </table>
            <div class="table-box">
       			
          			<div class="scroll-pane2" style="width: 100%;height: 100% ;overflow:scroll">
            			<table class="table11"  cellspacing="3px">
            				<tbody>
                    	<logic:notEmpty name="kmFeedbackMstrFormBean" property="feedbackListAll">
							<logic:iterate name="kmFeedbackMstrFormBean" property="feedbackListAll" id="feedbackResp" indexId="i">
								<%
									String cssName = "clearfix";				
									if( i%2==1)
									{			
									cssName = "clearfix alt";
									}	
									%>
									<TR class="<%=cssName%>">
									<td class="first" style="width: 40px;"><%=(i.intValue() + 1)%>.</TD>
									<td class="second" style="width: 180px;"><bean:write name="feedbackResp" property="comment" /></TD>
									
									<td class="third" style="width: 90px;"><bean:write name="feedbackResp" property="olm_id" /></TD>
									
									<td class="fourth"  style="width: 170px;"><bean:write name="feedbackResp" property="feedbackResponse" /></TD>
									<td class="fifth" style="width: 90px;"><bean:write name="feedbackResp" property="createdDate" /></TD>
									<td class="sixth" style="width: 90px;"><bean:write name="feedbackResp" property="updatedDate" /></TD>
									<td class="seventh" style="width: 90px;"><bean:write name="feedbackResp" property="readStatus" /></TD>
								</tr>


							</logic:iterate>
						</logic:notEmpty>
                      </tbody>
                    </table>
                   </div>
                  
                </div>
        </div>
</div>
<script type="text/javascript">
			$(function()
			{
				//alert("1")
				//$('.scroll-pane').jScrollPane();
			});
		
		</script> 
<script type="text/javascript">
$(document).ready(function() {
	   
	   //When page loads...
       $(".feedback-common").hide(); //Hide all content
       $("ul.list12 li a:first").addClass("active").show(); //Activate first tab
       $(".feedback-common:first").show(); //Show first tab content

       //On Click Event
       $("ul.list12 li a").click(function() {
			
               $("ul.list12 li a").removeClass("active"); //Remove any "active" class
               $(this).addClass("active"); //Add "active" class to selected tab
               $(".feedback-common").hide(); //Hide all tab content

               var activeTab = $(this).attr("href"); //Find the href attribute value to identify the active tab + content
               $(activeTab).fadeIn("fast"); //Fade in the active ID content
               return false;

       });


});
</script>