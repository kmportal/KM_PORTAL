<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<bean:define id="kmUserBean" name="USER_INFO"
	type="com.ibm.km.dto.KmUserMstr" scope="session" />
<html>
<body>
<html:form action="/sopUpload">
	<html:hidden property="methodName" value="insertSOP" />
	<html:hidden property="createdBy" styleId="createdBy" />
	<html:hidden property="token" styleId="token" />
	<html:hidden property="parentId" styleId="parentId" />
	<html:hidden property="elementLevel" styleId="elementLevel" />
	<html:hidden property="elementFolderPath" styleId="elementFolderPath" />
	<html:hidden property="sopPathId" styleId="sopPathId" />
	<input type=hidden name="circleElementId" />

	<div class="box2">
	<div class="content-upload">
	<h1>SOP Upload</h1>
	<h3 align="center"><FONT color="red"><html:errors /></FONT></h3>
	<div class="path-selection clearfix"><!--  include here --> 
		<jsp:include page="lobWiseUpload.jsp"></jsp:include>
	</div>

	<br>
	<br>
	<div class="bill-upload-head2 box3 form1">
	<ul class="list2 clearfix">
		<li class="clearfix padd10-0"><span style="margin-right: 5px;"
			class="text5 fll"><bean:message key="product.upload.topic" /></span>
		<p class="fll clearfix" style="margin-right: 25px;"><span
			class="common-textbox-l"> <span class="common-textbox-r">
		<input type="text" name="topic" value="" class="textbox6" id="topic"
			style="width: 270px;" maxlength="500"> </span> </span></p>
		<span class="text5 fll"><bean:message
			key="createOffer.startDate" /></span> <html:text
			styleClass="tcal calender1 fll" property="publishDt" readonly="true"
			style="margin-right:15px;" /> <span class="text5 fll"><bean:message
			key="createOffer.endDate" /></span> <html:text
			styleClass="tcal calender1 fll" property="endDt" readonly="true" />
		</li>
	</ul>
	</div>

	<div class="box1 header1">
	<ul class="list1 clearfix">
		<li style="width: 330px;"><bean:message
			key="product.upload.header" /></li>
		<li><bean:message key="product.upload.content" /></li>
	</ul>
	</div>

		<bean:define id="headerText" name="kmSopUploadFormBean"	property="header" type="java.lang.String[]" /> 
		<bean:define id="contentText" name="kmSopUploadFormBean" property="content"	type="java.lang.String[]" />
		<bean:define id="plainContentText"	name="kmSopUploadFormBean" property="plainContent" type="java.lang.String[]" />

	<ul class="list2 clearfix form1" id="contentList">
		<logic:iterate name="headerText" id="headers" indexId="rowCount">
			<%
				String cssName = "clearfix padd10-0";				
				if( rowCount%2==1)
				{			
				cssName = "clearfix padd10-0 alt";
				}	
				%>
			<li class="<%=cssName%>">
				<p class="clearfix fll margin-r20">
					<span class="textbox6">
						<span class="textbox6-inner"> 
							<html:text property="header" styleClass="textbox7" value="<%= headerText[rowCount] %>" /> 
						</span> 
					</span>
				</p>
				<p class="clearfix fll margin-r20"> 
				<span>
				<html:textarea property="content" style="overflow:hidden" styleClass="textarea2 fll"   value="<%= contentText[rowCount] %>"></html:textarea>
			<!-- <iframe id="iView<%=rowCount%>" name=c Class="textarea2 fll" frameborder='0'></iframe>
	                <script type="text/javascript">
	                	Init(document.iView<%=rowCount%>);
	                </script>-->
	              <!--  <html:hidden property="content"	styleId='<%=("content"+rowCount)%>' value="" />--> 
	               <html:hidden	property="plainContent" styleId='<%=("plainContent"+rowCount)%>'value="" /></span></p>
	       </li>
		</logic:iterate>

	</ul>
	</div>
	<a class="add-more-btn flr" style="margin-right: 5px;" onclick="javascript: addMoreContentFields();">&nbsp;</a> <br>
	
	
	<br>
	<jsp:include page="Disclaminer.jsp"></jsp:include></div>


	<div class="box2" style="margin-top: 15px;">
	<div class="content-upload">
	<h1>SOP's</h1>

	<ul class="list2 clearfix form1">
		<li class="clearfix padd10-0">
			<span style="margin-right: 5px;" class="text2 fll"><strong><bean:message key="sop.product.header" /></strong></span>
			<p class="clearfix fll margin-r10">
				<span class="textbox6">
					<span class="textbox6-inner"> 
						<html:text property="productHeader" styleClass="textbox8" />
					</span> 
				</span>
			</p>
		<span style="margin-right: 5px;" class="text2 fll"><strong><bean:message key="sop.product.path" /> </strong></span>
			<p class="clearfix fll margin-r10">
				<span class="textbox6">
					<span class="textbox6-inner"> 
						<html:text property="productPath" styleId="productPath" styleClass="textbox8" readonly="true" />
					</span>
				</span> 
						<html:button property="browseLinkBtn" value="Browse..." onclick="browseSopPath()" />
			</p>
		<br>
		</li>
		<li class="clearfix"><a class="add-more-btn flr" style="margin-top: 2px;" onclick="addSOPLink();"></a></li>
	</ul>


	<div class="box1 header1">
	<ul class="list1 clearfix">
		<li style="width: 320px;">Product Header</li>
		<li>Product Path</li>
	</ul>
	</div>

	<div align="left">
	<table id="sopLinkTable" width="100%">

	</table>
	</div>

	</div>
	</div>

	<div class="button-area">
	<div class="button"><a class="red-btn" onclick="submitForm();"><b>Submit</b></a></div>
		<!--<div class="button"><input class =""red-btn" type="submit" value="Submit" onclick="submitForm();/></div>-->
	
	<div class="button"><a class="red-btn" onclick="clearForm();"><b>clear</b></a></div>
	</div>


</html:form>
</body>
</html>
<script language="javascript">
var selectedItem='';
 function validate()
 {
 	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth()+1;
	var yyyy = today.getFullYear();
	if(dd<10){dd='0'+dd}
	if(mm<10){mm='0'+mm}
	var curr_dt=yyyy+'-'+mm+'-'+dd;
		
	var reg=/^[0-9a-zA-Z ]*$/;
    var isHeader = false;	
    var obj = document.getElementById('createMultiple');
    var selectedCircles='';
    var arr = new Array();
 	var arrIndex = 0;
    
	if(document.forms[0].publishDt.value == "")
		{
			alert("Please enter Publish Start Date.");
			return false;			
		}
		else if(document.forms[0].publishDt.value < curr_dt)
		{
			alert("Publish Start Date cannot be a past date (day before today).");
			return false;			
		}else if(document.forms[0].endDt.value == "")
		{
			alert("Please enter Publish End Date.");
			return false;			
		}
		else if(document.forms[0].endDt.value < curr_dt)
		{
			alert("Publish End Date cannot be a past date (day before today).");
			return false;			
		}
		else if(document.forms[0].endDt.value < document.forms[0].publishDt.value)
		{
			alert("Publish Start Date should not be greater than Publish End Date.");
			return false;			
		}	
		   
    if(document.getElementById('createMultiple')!= null   && document.getElementById("label_3") != null)
	{ 
  	 	for (var i = 1; i < obj.options.length; i++) 
  	 	{ 
          if(obj.options[i].selected) 
           {
           selectedCircles = selectedCircles+","+obj.options[i].value ;
           }
        }
                	    
	    if('' == selectedCircles)
	    { 
	    	for ( i = 1; i < obj.options.length; i++) 
	  	 	{  
	          if(obj.options[i].value == document.getElementById("label_3").title) 
	           {
	             obj.options[i].selected = true;
	           }
	        }
	    }
	    return true;
    }
   

     for (var ii=0; ii<document.forms[0].header.length; ii++) 
	   {  
	   
	   
	    document.forms[0].content[ii].value=document.getElementsByName("content")[ii].value;
	    document.forms[0].plainContent[ii].value=document.getElementsByName("plainContent")[ii].value;
	    
	    //alert(" ### "+document.forms[0].content[ii].value+" $$$$ "+document.forms[0].plainContent[ii].value);
	    if(document.forms[0].header[ii].value!= "" )
	    {
	        isHeader = true;
	        
  			if("" == document.forms[0].content[ii].value)
			{
			 alert("Please enter Content for header : "+ document.forms[0].header[ii].value );
			 return false;	
		    }	else{
			return true;
			} 	
		
		}  
	 
	  }
	  
	  if(!isHeader)
	    {
	      alert("Please enter Header.");
	      document.forms[0].header[0].focus();
	      return false;
	    }
	    
	   if( "" != document.forms[0].productHeader.value)
	    {
	      return addSOPLink();	     
		}
		
		  return true; 
 } 
 
 function copyValue(f) {
    f.elements.EditorValue.value = "" + myEditor.document.body.innerHTML + "";
   
  }
 
 
 function submitForm()
	{
	   	var docPath = "";
 		var isCategorySelected = false;
 			   	
	   	if("" == document.forms[0].topic.value)
	   	{
	   	     alert("Please enter topic.");
			 document.forms[0].topic.focus();
			 return false;	
	   	}
	   //alert("Topic is ::"+document.forms[0].topic.value);
		for(var zz=2; zz<=9; zz++)
	    {
	      if("" != document.getElementById("label_"+zz).title)
	      { 
	          if(zz >= 4)
	         {
	          isCategorySelected =  true;
	         }
	         docPath = docPath +"/"+ document.getElementById("label_"+zz).title ;
	         //alert("docPath = "+docPath);
	      } 
	    }
	    
	     document.getElementById("elementFolderPath").value = initialElementPath +docPath;
	    if(!isCategorySelected)
		{
		 alert("Please select any Category or Sub Category.");
		 return false;
		}
		 if(validate())
		{
			
		   document.forms[0].submit();
		   return true;
		}
		else
		{		    
			return false;
		}		

	return true;
 }
	
 function addMoreContentFields()
 {
 	var arrIndex = 0;
 	//alert("Adding more fields");
    var ul = document.getElementById("contentList");
    var li = document.createElement("li");
    var liLength = ul.getElementsByTagName("li").length;
    var liClass = "clearfix padd10-0";
    
    if( liLength == 30)
    {
       alert("Maximum 30 rows can be added");
       return false;
    }   
    
    
    if( liLength %2 ==1)
    {
       liClass = "clearfix padd10-0 alt";
    }
    li.setAttribute("class", liClass);
   var contentText = "";                           
   contentText += "<ul class='list2 clearfix form1'><p class='clearfix fll margin-r20'> <span class='textbox6'> <span class='textbox6-inner'>";
   contentText += "<input type='text' name='header' value='' class='textbox7'/> ";
   contentText += "</span> </span> </p>";
 // contentText += "<iframe id='iView" + arrIndex + "' name=c Class='textarea2 fll' frameborder='0'></iframe>";
   contentText += "<p class='clearfix fll margin-r20'><span><input type='textarea' name='content' id ='content'+arrIndex+ class='textarea2 fll' />"  ;
   //contentText += "<textarea name='content'  class='textarea2 fll'></textarea> <a href='#' class='edit-icon flr'>&nbsp;</a>";
   //contentText += "<input type='hidden' name='content' value='' id='content" + arrIndex + "'/>";
   contentText += "<input type='hidden' name='plainContent' value='' id='plainContent' + arrIndex + /></span></p></ul>";
    li.innerHTML = contentText;
    
    ul.appendChild(li); 
    var iViewObj = "iView"+arrIndex;
    var winObj = window.document;
    var obj = winObj.getElementById(iViewObj);
    InitEditModeFrames(obj);
 }

function InitEditModeFrames(s)
{
	var iFrm = s.contentWindow;
	Init(iFrm);
}

 
 function listCreation(data){
  //http://developer.apple.com/internet/webcontent/dom2i.html
	var newLI = document.createElement("li");
	var newText = newLI.createTextNode(data);
		
	var pTag = document.getElementById("contentList");
	var ulTag = pTag.getElementsByTagName("ul");

//	var referenceLI= ulTag.getElementsByTagName("li").item(listCount);
//	listCount++;

	ulTag.appendChild(newLI);
}
 
 function addSOPLink()
 {
   if (document.forms[0].productHeader.value == '')
   {
    alert("Please enter Product Header.");
    document.forms[0].productHeader.focus();
    return false;
   }
   
   var reg=/^[0-9a-zA-Z ]*$/;
   
   if(!reg.test(document.forms[0].productHeader.value))
	{
	 alert("Special characters not allowed in Product Header.");
	 document.forms[0].productHeader.select();
	 return false;	
    }
		    
   if (document.forms[0].productPath.value == '')
   {
    alert("Please select Product Path.");
    document.forms[0].productPath.focus();
    return false;
   }
   
   var theTable = document.getElementById("sopLinkTable");   
   var newRow , newCell , ii ;
   var rowNum = theTable.rows.length;
       ii = rowNum - 1;
       
    newRow = theTable.insertRow(rowNum);
 
    
    newCell = newRow.insertCell(0);
    newCell.innerHTML = "<p class='clearfix fll'> &nbsp; &nbsp; &nbsp; &nbsp; <input type='text' name='productHeaders' class='textbox7' style='background-color: #f7f7f7'  value='"+ document.forms[0].productHeader.value  +"' title='"+document.forms[0].productHeader.value +"' readonly='true' > </p>";   
    document.forms[0].productHeader.value  = '';
	
	newCell = newRow.insertCell(1);
    newCell.innerHTML = "<p class='clearfix fll'> <input type='text' name='productPathsLabels' class='textbox7' style='background-color: #f7f7f7' value='"+document.forms[0].productPath.value +"' readonly='true'  title='"+document.forms[0].productPath.value +"'><input type='hidden' name='productPaths' value='"+document.forms[0].sopPathId.value +"'  />  </p>";    
    document.forms[0].productPath.value = '';
    
    newCell = newRow.insertCell(2);
    newCell.innerHTML = "<a class='remove-btn flr' name='removeSopBtn' onclick='removeSopLink(this)'>&nbsp;</a>";   
}
 

 function removeSopLink(obj)
 { 
   for (var ii=0; ii<document.forms[0].elements.length; ii++) 
   {  
    if(obj == document.getElementsByName("removeSopBtn")[ii] )
    {
	    document.getElementsByName("header")[ii].value = "";
	    document.getElementsByName("content")[ii].value= "";	
	    document.getElementsByName("plainContent")[ii].value= "";	
	
	  var theTable = document.getElementById("sopLinkTable");   
      theTable.deleteRow(ii); 
    }
   }	   
 }
 
 function browseSopPath()
 {
  window.open('sopUpload.do?methodName=viewDocumentLinks','childWindow','left=150,top=300, width=780,height=180');   
    
 }
 

 function Init(s)
{
s.document.designMode = 'On';
arr[arrIndex]=s;
arrIndex++;
}
 
function clearForm()
{
  	 document.forms[0].reset();
 	 for (var ii=0; ii<document.forms[0].header.length; ii++) 
	  {  
	    document.forms[0].header[ii].value = "";
	    document.forms[0].content[ii].value = "";
	    document.forms[0].plainContent[ii].value = "";
	    arr[ii].document.body.innerHTML = "";
	  }
	  return true;
}
function Lobmsg()
{
if(document.forms[0].dateCheck.checked)
{

var con=confirm("Are u sure to upload document Lob wise");
//alert("vishwas"+con);
document.forms[0].methodName.value= "insertSOPLobWise&ParentId="+con;
document.forms[0].submit();
return false;
}
}

</script>
