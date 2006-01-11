<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@page import="java.io.PrintWriter"%>
<%@page import="com.ibm.km.common.Utility"%><LINK href="theme/text.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
 var arr = new Array();
 var arrIndex = 0;
 function validate()
 {
    var reg=/^[0-9a-zA-Z ]*$/;
    var tobeUpdate = false;
 	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth()+1;
	var yyyy = today.getFullYear();
	if(dd<10){dd='0'+dd}
	if(mm<10){mm='0'+mm}
	var curr_dt=yyyy+'-'+mm+'-'+dd;
		
    var isHeader = false;	
    var obj = document.getElementById('createMultiple');
    var selectedCircles='';
    
	if(document.forms[0].publishDt.value == "")
		{
			alert("Please enter Publish Start Date.");
			return false;			
		}
		else if(document.forms[0].endDt.value == "")
		{
			alert("Please enter Publish End Date.");
			return false;			
		}		
		else if(document.forms[0].endDt.value < document.forms[0].publishDt.value)
		{
			alert("Publish Start Date should not be greater than Publish End Date.");
			return false;			
		}	
		
		
   
    for (var ii=0; ii<document.forms[0].header.length; ii++) 
	   {  
	    if( "" != document.forms[0].header[ii].value)
	    {
	        tobeUpdate = true;
	        
		    document.forms[0].content[ii].value=arr[ii].document.body.innerHTML;
		    document.forms[0].plainContent[ii].value=arr[ii].document.body.innerText;
  			
  			if("" == document.forms[0].content[ii].value)
			{
			 alert("Please enter Content.");
			 return false;	
		    }		    
		}    
	  }
	  
	  
	   for ( ii=0; ii<document.forms[0].productHeader.length; ii++) 
	   {  
	    if( "" != document.forms[0].productHeader[ii].value)
	    {
		    if("" == document.forms[0].productPath[ii].value)
			{
			 alert("Please enter Product Path.");
			 document.forms[0].productPath[ii].focus();
			 document.forms[0].productPath[ii].select();
			 return false;	
		    }
		    
		    if(!reg.test(document.forms[0].productPath[ii].value))
			{
			 alert("Special characters not allowed in Product Path.");
			 document.forms[0].productPath[ii].focus();
			 document.forms[0].productPath[ii].select();
			 return false;	
		    }
		}    
	  }
       
	    if( "" != document.forms[0].productHeader.value)
	    {
	      addSOPLink();	  
		}		
		
		
   return true; 
}
 
function updateSopDetails()
{
	   	if("" == document.forms[0].topic.value)
	   	{
	   	     alert("Please enter value for topic.");
			 document.forms[0].topic.focus();
			 return false;	
	   	}
	  	  
		if(validate())
		{
		   document.forms[0].submit();
		}
		else
		{		    
			return false;
		}		
}

 function addMoreContentFields()
 {
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
   contentText += "<p class='clearfix fll margin-r20'> <span class='textbox6'> <span class='textbox6-inner'>";
   contentText += "<input type='text' name='header' value='' class='textbox7'/> ";
   contentText += "</span> </span> </p>";
   contentText += "<iframe id='iView" + arrIndex + "' name=c Class='textarea2 fll' frameborder='0'></iframe>";
   contentText += "<input type='hidden' name='content' value='' id='content" + arrIndex + "'/>";
   contentText += "<input type='hidden' name='plainContent' value='' id='plainContent" + arrIndex + "'/>";
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
 
function Init(s)
{
	s.document.designMode = 'On';
	arr[arrIndex]=s;
	arrIndex++;
}
 function browseSopPath()
 {
  window.open('sopUpload.do?methodName=viewDocumentLinks','childWindow','left=150,top=300, width=800,height=200');   
    
 }
function addSOPLink()
 {
   if (document.forms[0].productHeader.value == '')
   {
    alert("Please enter value for Product Header.");
    document.forms[0].productHeader.focus();
    return false;
   }
   if (document.forms[0].productPath.value == '')
   {
    alert("Please select value for Product Path.");
    document.forms[0].productPath.focus();
    return false;
   }
   
   var theTable = document.getElementById("sopLinkTable");   
   var newRow , newCell , ii ;
   var rowNum = theTable.rows.length;
       ii = rowNum - 1;
       
    newRow = theTable.insertRow(rowNum);
 
    
    newCell = newRow.insertCell(0);
    newCell.innerHTML = "<p class='clearfix fll'>  &nbsp;&nbsp; <input type='text' name='productHeaders' class='textbox7'  value='"+ document.forms[0].productHeader.value  +"'  title='"+document.forms[0].productHeader.value +"' readonly='true' > ";   
    document.forms[0].productHeader.value  = '';
	
	newCell = newRow.insertCell(1);
    newCell.innerHTML = "<p class='clearfix fll'> <input type='text' name='productPathsLabels' class='textbox7' value='"+document.forms[0].productPath.value +"'  readonly='true'  title='"+document.forms[0].productPath.value +"'><input type='hidden' name='productPaths' value='"+document.forms[0].sopPathId.value +"' /> ";    
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

function removeImage(obj)
 { 
   for (var ii=0; ii<document.getElementById("productImageTable").getElementsByTagName("TR").length; ii++) 
   {  
    if(obj == document.getElementsByName("removeImageBtn")[ii] )
    {
	  var theTable = document.getElementById("productImageTable");   
      theTable.deleteRow(ii); 
    }
   }	   
 }
 function addMoreImageFields()
 {   
	var ul = document.getElementById("imageUL");
  	var trCount = document.getElementById("productImageTable").getElementsByTagName("TR").length;
    var liCount = ul.getElementsByTagName("li").length ;
    var imgCount = trCount+liCount;
    if(imgCount >= 5)
    { 
		  alert("Maximum only 5 images can be uploaded");
		  return false;
    }
    var li = document.createElement("li");
	var liClass = "";
	if(imgCount % 2 )
    liClass = "clearfix";
    else
    liClass = "clearfix alt";
    li.setAttribute("class", liClass);
 	imgCount++;
    
    var contentText = "<span class='text2 fll' style='margin-right:10px;'><strong>Title</strong></span><p class='clearfix fll margin-r20'> <span class='textbox6'><input type='text' name='productImageList["+ul.getElementsByTagName("li").length+"].imageTitle' value='' class='textbox7'></span></p><span class='text2 fll' style='margin-left:10px; margin-right:10px;'><strong>Attach an image</strong></span><p class='clearfix fll '><span><input type='file' name='productImageList["+ul.getElementsByTagName("li").length+"].imageFile' value='' onchange='validateImage(this)'></span></p>";
    li.innerHTML = contentText;
    ul.appendChild(li);  
}	             
</script>
<bean:define id="kmSopUploadFormBean" name="kmSopUploadFormBean" type="com.ibm.km.forms.KmSopUploadFormBean" scope="request" />
  
<html:form action="/sopUpload" enctype="multipart/form-data">
<html:hidden property="methodName" value="updateSOP"/>
<html:hidden property="createdBy" styleId="createdBy"/>
<html:hidden property="xmlFileName" value="<%= kmSopUploadFormBean.getXmlFileName()%>" />
<html:hidden property="docId" value="<%= kmSopUploadFormBean.getDocId()%>" />
<html:hidden property="sopPathId" styleId="sopPathId"/>
<html:hidden property="kmActorId"/>

 
  <div class="box2">
  <div class="content-upload">
  <h1>Update SOP Details</h1>
    <FONT color="red"><html:errors/></FONT>
    <TABLE align="center" border="0"  cellpadding="0" cellspacing="0"> 
		<TBODY>
		 <tr>
			<td  align="center" class="error">
				<strong> 
		          	 <html:messages id="msg" message="true">
		                 <bean:write name="msg"/> 
		             </html:messages>
	            </strong>
            </td>
		   </tr>
		</TBODY>
	</TABLE>	
  
     <bean:define id="headerText" name="kmSopUploadFormBean" property="header" type="java.lang.String[]" />
	 <bean:define id="contentText" name="kmSopUploadFormBean" property="content" type="java.lang.String[]" />
	 <bean:define id="plainContentText" name="kmSopUploadFormBean" property="plainContent" type="java.lang.String[]" />

        <div class="bill-upload-head2 box3 form1">  
          <ul class="list2 clearfix form1">
           <li class="clearfix padd52-0"> 
              <span style="margin-right:5px;" class="text5 fll"><strong><bean:message key="product.upload.topic"/></strong> </span>
              <p class="clearfix fll" style="margin-right:25px;"> <span class="common-textbox-l"> <span class="common-textbox-r">
                 <html:text property="topic"  styleClass="textbox6" style="width:270px;" maxlength="500"/></span> </span> </p>
                <span class="text5 fll"><bean:message key="createOffer.startDate"/></span>
                <html:text  styleClass="tcal calender1 fll" property="publishDt"  readonly="readonly" style="margin-right:15px;" />
                 
                <span class="text5 fll"><bean:message key="createOffer.endDate"/></span>
                <html:text styleClass="tcal calender1 fll" property="endDt" readonly="readonly"  />
                 
              </li> 
          </ul>
        </div>  
          
	      <div class="box1 header1">
	            <ul class="list1 clearfix">
	              <li style="width:330px;"><bean:message key="product.upload.header"/></li>
	              <li><bean:message key="product.upload.content"/></li>
	            </ul>
	      </div>  
		
		<ul class="list2 clearfix form1" id="contentList" >
			  <logic:iterate name="headerText" id="headers" indexId="rowCount" >
				<%
				String cssName = "clearfix padd10-0";				
				if( rowCount%2==1)
				{			
				cssName = "clearfix padd10-0 alt";
				}	
				%>
				<li class="<%=cssName%>">	
				<br>                           
	              <p class="clearfix fll margin-r20"> <span class="textbox6"> <span class="textbox6-inner">
	                <html:text property="header"  styleClass="textbox7" value="<%= headerText[rowCount]  %>"/> 
	                </span> </span> </p>
	                 <iframe id="iView<%=rowCount%>" name=c Class="textarea2 fll" src="ImageProviderServlet?requestType=xmlFile&docPath=<%=kmSopUploadFormBean.getXmlFileName()%>&tagId=<%=rowCount%>&tagName=CONTENT" frameborder='0'></iframe>
	                   	<script type="text/javascript">
	                		Init(document.iView<%=rowCount%>);
	                	</script>
	                <input type="hidden" name="content" value="" id='<%="content"+rowCount%>'>
	                <input type="hidden" name="plainContent" value="" id='<%="plainContent"+rowCount%>'>
	                
          		</li>  
 		     </logic:iterate>	
		    
		   </ul>	
		  
    <TABLE align="center" width="100%"> 
	 <TBODY>	
	   <TR >
		<td width="100%" ><a class="add-more-btn flr" onclick="addMoreContentFields()">&nbsp;</a>
		<br><br>
		    <jsp:include page="Disclaminer.jsp"></jsp:include>
		</td>
	  
      </TR>		   	
	 </TBODY>
	</TABLE>  
  </DIV>
  </DIV>		
	<div class="box2" style="margin-top:15px;">
        <div class="content-upload">
          <h1>SOP's</h1>
          
          <ul class="list2 clearfix form1">
            <li class="clearfix padd10-0">
            	<span style="margin-right:5px;" class="text2 fll"><strong><bean:message key="sop.product.header"/></strong></span>
              <p class="clearfix fll margin-r20"> <span class="textbox6"> <span class="textbox6-inner">
                <html:text property="productHeader" styleClass="textbox8" size="10"/>
                </span> </span> </p>
                <span style="margin-right:5px;" class="text2 fll"><strong><bean:message key="sop.product.path"/> </strong></span>
                <p class="clearfix fll margin-r20"> <span class="textbox6"> <span class="textbox6-inner">
                    <html:text property="productPath" styleId="productPath" styleClass="textbox8" size="10" readonly="true"/>        
                    </span> </span><html:button property="browseLinkBtn" value="..." onclick="browseSopPath()"/>
                     </p>
              <a class="add-more-btn2 flr" onclick="addSOPLink()">&nbsp;</a> </li>
          </ul>
          
          
          
		   
          <div class="box1 header1">
            <ul class="list1 clearfix">
              <li style="width: 320px;">Product Header</li> <li>Product Path</li>
            </ul>
          </div>
       
        <bean:define id="sopLinkHeaderText" name="kmSopUploadFormBean" property="productHeaders" type="java.lang.String[]" />
	    <bean:define id="sopLinkPathText" name="kmSopUploadFormBean" property="productPaths" type="java.lang.String[]" />
	 	<bean:define id="productPathsLabels" name="kmSopUploadFormBean" property="productPathsLabels" type="java.lang.String[]" />
	 	 
	 	   
	 	  
         <div align="left"> 
         <table id="sopLinkTable" width="100%">
          
          	<logic:notEmpty name="sopLinkHeaderText" >
			<logic:iterate name="sopLinkHeaderText" id="headers" indexId="rowCount" >
				<%
				String cssName = "clearfix padd10-0";				
				if( rowCount%2==1)
				{			
				cssName = "clearfix padd10-0 alt";
				}	
				%>
				<TR class="<%=cssName%>">						
			     <TD class="text" align="left">&nbsp;&nbsp;
			      <html:text property="productHeaders"  styleClass='textbox7'  value="<%= sopLinkHeaderText[rowCount] %>"   title="<%= sopLinkHeaderText[rowCount] %>"  readonly="true"/>
			     </TD>
			     <TD class="text" align="left" colspan="2"><html:text styleClass='textbox7' property="productPathsLabels" size="50" value="<%= productPathsLabels[rowCount] %>"   title="<%= productPathsLabels[rowCount] %>"  readonly="true"/>
					 <input type='hidden' name='productPaths' value="<%= sopLinkPathText[rowCount] %>" />  
					 <a class='remove-btn flr' name='removeSopBtn' onclick='removeSopLink(this)'>&nbsp;</a>
			     </TD>
				</TR>	
			</logic:iterate>
		   </logic:notEmpty>
		   
          
          </table>
         </div> 
        </div>   
       </div>
      
        <div class="box2" style="margin-top:15px;">
        <div class="content-upload">
          <h1>Edit/<bean:message key="product.upload.images"/></h1>
          	<div class="thumb-area clearfix">
      	 		<bean:define id="imageTitle" name="kmSopUploadFormBean" property="imageTitle" type="java.lang.String[]" />
	 			<bean:define id="imageName" name="kmSopUploadFormBean" property="imageName" type="java.lang.String[]" />
	 	    	<table  id="productImageTable">
	 	     	   <logic:iterate name="imageTitle" id="images" indexId="rowCount" >
		 				<TR class="clearfix ">				
				     		<TD class="text" align="left" width="100%" >
				 		  		<div class="thumb"><img src='<%=request.getContextPath()+"/ImageProviderServlet?requestType=productImage&imagePath="+imageName[rowCount]%>'  />
						  			<p><%=imageTitle[rowCount]%><a class='remove-btn flr' name='removeImageBtn' onclick='removeImage(this)'>&nbsp;</a></p></div>
						         <html:hidden property="displayedImageTitle" value="<%=imageTitle[rowCount]%>"/>
				   				 <html:hidden property="displayedImagePath" value="<%=imageName[rowCount]%>"/>
					         </TD>
		         </TR>
		        </logic:iterate>
	    	</table>   		
     	 </div>
      
          <ul class="list2 clearfix form1" id="imageUL">
            <logic:iterate id="productImageList" name="kmSopUploadFormBean" indexId="rowCount" property="productImageList">  
			<% if (rowCount<1)
			{
			%>
          	<li class="clearfix">
                <span class="text2 fll" style="margin-right:10px; margin-down:5px;"><strong><bean:message key="product.upload.imageTitle"/></strong></span>
                 <p class="clearfix fll margin-r20"> <span class="textbox6">
	                <html:text name="productImageList"  property="imageTitle" value="" styleClass="textbox7" indexed="true"  /> 
	                </span>
	             </p>	                
                <span class="text2 fll" style="margin-left:10px; margin-right:5px; margin-down:5px;"><strong><bean:message key="product.upload.attachImage"/></strong></span>
                <p class="clearfix fll ">
                	<span >
                	<html:file name="productImageList" value="" property="imageFile" onchange="validateImage(this)" indexed="true" /> 
                	</span>
                </p>
            </li>
            <%
			}
			%>
        </logic:iterate>  
       </ul>
       
       <h5> &nbsp;&nbsp; &nbsp;Supported Image: jpg or gif; Size maximum: 2 MB</h5>
       <a class="add-more-btn flr" style="margin-right: 5px;" onclick="addMoreImageFields()"></a>&nbsp;&nbsp;<br><br>
          
      </div>   
     </div>  
           <div class="button-area">
            <div class="button">
              <html:button property="Submit" value="Update" styleClass="red-btn" onclick="return updateSopDetails();">  </html:button>              
            </div>
            <div class="button"> <a  class="red-btn" onclick="return clearForm();">clear</a> </div>
          </div>
           
           
</html:form>