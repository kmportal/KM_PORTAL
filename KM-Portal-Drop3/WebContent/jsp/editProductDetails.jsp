<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<script language="javascript"> 
 var arr = new Array();
 var arrIndex = 0;
 function Init(s)
{
s.document.designMode = 'On';
arr[arrIndex]=s;
arrIndex++;
}
function validate()
{  
 	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth()+1;
	var yyyy = today.getFullYear();
	if(dd<10){dd='0'+dd}
	if(mm<10){mm='0'+mm}
	var curr_dt=yyyy+'-'+mm+'-'+dd;
		
	if(document.forms[0].publishDt.value == "")
		{
			alert("Please enter Start Date.");
			return false;			
		}
		else if(document.forms[0].endDt.value == "")
		{
			alert("Please enter End Date.");
			return false;			
		}
		else if(document.forms[0].endDt.value < document.forms[0].publishDt.value)
		{
			alert("Start Date should not be greater than End Date.");
			return false;			
		}	
		

    if(document.getElementById("productImageTable").getElementsByTagName("TR").length>4)
    {
      if("" != document.getElementsByName("productImageList[0].imageTitle")[0].value)
      { 
          alert("Maximum only 5 images can be uploaded");  
          document.getElementsByName("productImageList[0].imageTitle")[0].value ="";
          return false;
      }
      
      if("" != document.getElementsByName("productImageList[0].imageFile")[0].value)
      {
           alert("Maximum only 5 images can be uploaded"); 
           document.getElementsByName("productImageList[0].imageFile")[0].value ="";
           return false;
      }
    }
		var isHeader = false;
		if(document.forms[0].topic.value == "")
		{
			alert("Please Enter Topic.");
			return false;
		}
		    
	   for (var ii=0; ii<document.forms[0].header.length; ii++) 
	   {  
	    if( "" != document.forms[0].header[ii].value)
	    {
	       isHeader = true;
	       document.forms[0].content[ii].value = arr[ii].document.body.innerHTML;	 
	       document.forms[0].plainContent[ii].value = arr[ii].document.body.innerText;	 
		  
		    if("" == document.forms[0].content[ii].value)
			{
			 alert("Please enter Content for header : "+ document.forms[0].header[ii].value);
			 return false;	
		    }
		}    
	  }
   		
		if(!isHeader)
	    {
	      alert("Please enter value for Header.");
	      document.forms[0].header[0].focus();
	      return false;
	    }
	    
		return true;
 }
 
 function updateProductDetails()
	{
		if(validate())
		{
			document.forms[0].methodName.value ="updateProduct";
			document.forms[0].submit();
		}
		else
		{
			return false;
		}
	}
	
    
    
    
function validateImage(obj)
 {  
   for (var ii=0; ii<2; ii++) 
   {  
	    if(obj == document.getElementsByName("productImageList["+ii+"].imageFile")[0] && "" != obj.value  )
	    {
		  var imageName = document.getElementsByName("productImageList["+ii+"].imageFile")[0].value;
		  var lastIndex = imageName.lastIndexOf(".");
		  var imageType = imageName.substring(lastIndex);
			  if(imageType == ".jpg" || imageType == ".JPG" ||  imageType == ".bmp")
			  {
			   return true;
			  }
			  else
			  {			   
			   alert("Please select only jpg or bmp employee image.");
			   obj.select();
			   return false;		 
			  }
	    }
   }
   return true;
 }
  
function InitEditModeFrames(s)
{
	var iFrm = s.contentWindow;
	Init(iFrm);
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
    var contentText = "<br> <p class='clearfix fll margin-r20'> <span class='textbox6'> <span class='textbox6-inner'><input type='text' name='header' value='' class='textbox7'></span> </span> </p>";
    	contentText = contentText + "<iframe id='iView"+arrIndex+"' name=c Class='textarea2 fll' frameborder='0'></iframe>";
    	contentText = contentText +  " <input type='hidden' name='content' value='' id='content"+arrIndex+"'>";
    	contentText = contentText +  " <input type='hidden' name='plainContent' value='' id='plainContent"+arrIndex+"'><p>";

    li.innerHTML = contentText;
    ul.appendChild(li); 
    var iViewObj = "iView"+arrIndex;
    var winObj = window.document;
    var obj = winObj.getElementById(iViewObj);
    InitEditModeFrames(obj);
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
    
    var contentText = "<span class='text2 fll' style='margin-right:10px;'><strong>Title</strong></span><p class='clearfix fll margin-r20'> <span class='textbox6'><input type='text' name='productImageList["+ul.getElementsByTagName("li").length+"].imageTitle' value='' class='textbox7'></span></p><span class='text2 fll' style='margin-left:40px; margin-right:10px;'><strong>Attach an image</strong></span><p class='clearfix fll '><span><input type='file' name='productImageList["+ul.getElementsByTagName("li").length+"].imageFile' value='' onchange='validateImage(this)'></span></p>";
    li.innerHTML = contentText;
    ul.appendChild(li);  
}	             

function clearForm()
{
  	 document.forms[0].reset();
  	 
	 for (var ii=0; ii<document.forms[0].content.length; ii++) 
	   {  
	    document.forms[0].content[ii].value = "";
	    document.forms[0].plainContent[ii].value = "";
	    arr[ii].document.body.innerHTML = "";
	    
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
  
</script>
    <bean:define id="kmProductUploadFormBean" name="kmProductUploadFormBean" type="com.ibm.km.forms.KmProductUploadFormBean" scope="request" />
	
<html:form action="/productUpload" enctype="multipart/form-data" > 
	<html:hidden property="methodName" value="updateProduct"/>
	<html:hidden property="createdBy" styleId="createdBy"/>
    <html:hidden property="elementFolderPath" styleId="elementFolderPath"/>
    <html:hidden property="docId" value="<%= kmProductUploadFormBean.getDocId()%>" />
  

	<html:hidden property="xmlFileName" value="<%= kmProductUploadFormBean.getXmlFileName()%>" />
	
	  <div class="box2">
       <div class="content-upload">
         <h1>Edit : Content/Product</h1>
         <strong>
	          	 <html:messages id="msg" message="true">
	                 <bean:write name="msg"/> 
	             </html:messages>
         </strong>
            
         <h3 align="center"> <FONT color="red"><html:errors/></FONT></h3>
           
         <div class="bill-upload-head2 box3 form1">  
          <ul class="list2 clearfix form1">
           <li class="clearfix padd52-0"> 
              <span style="margin-right:5px;" class="text5 fll"><strong><bean:message key="product.upload.topic"/></strong> </span>
              <p class="clearfix fll" style="margin-right:25px;"> <span class="common-textbox-l"> <span class="common-textbox-r">
                 <html:text property="topic"  styleClass="textbox6" style="width:270px;" maxlength="500" /></span> </span> </p>
                <span class="text5 fll"><b><bean:message key="createOffer.startDate"/></b></span>
                <html:text  styleClass="tcal calender1 fll" property="publishDt"  readonly="readonly" style="margin-right:15px;" />
                <span class="text5 fll" style="margin-right: 0px;"><b><bean:message key="createOffer.endDate"/></b></span>
                <html:text styleClass="tcal calender1 flr" property="endDt" readonly="readonly"  />
              </li> 
          </ul>
        </div>  
        
        
         <div class="box1 header1">
            <ul class="list1 clearfix">
              <li style="width:330px;"><bean:message key="product.upload.header"/></li>
              <li><bean:message key="product.upload.content"/></li>
            </ul>
         </div>  
         
                  
            <bean:define id="headerText" name="kmProductUploadFormBean" property="header" type="java.lang.String[]" />
			<bean:define id="contentText" name="kmProductUploadFormBean" property="content" type="java.lang.String[]" />
			<bean:define id="plainContentText" name="kmProductUploadFormBean" property="plainContent" type="java.lang.String[]" />
			
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
	                <html:text property="header"  styleClass="textbox7" value="<%= headerText[rowCount] %>"/> 
	                </span> </span> </p>
			         <iframe id="iView<%=rowCount%>" name=c Class="textarea2 fll" src="ImageProviderServlet?requestType=xmlFile&docPath=<%=kmProductUploadFormBean.getXmlFileName()%>&tagId=<%=rowCount%>&tagName=CONTENT" frameborder='0'></iframe>
	                   	<script type="text/javascript">
	                		Init(document.iView<%=rowCount%>);
	                	</script>
	                 <input type="hidden" name="content" value="" id='<%="content"+rowCount%>'>
	                 <input type="hidden" name="plainContent" value="" id='<%="plainContent"+rowCount%>'>
	                
          		<br>
			</li>  
 		     </logic:iterate>	
		   </ul>
		   <span class="flr"> <a class="add-more-btn flr" style="margin-right: 5px;" onclick="addMoreContentFields()">&nbsp;</a> </span>
		   <br>	<br>
		    <jsp:include page="Disclaminer.jsp"></jsp:include>
        </div>
        </div>
        

	
	   <div class="box2" style="margin-top:15px;">
        <div class="content-upload">
          <h1>Edit/<bean:message key="product.upload.images"/></h1>
          
          <div class="thumb-area clearfix">
      
      		<bean:define id="imageTitle" name="kmProductUploadFormBean" property="imageTitle" type="java.lang.String[]" />
	 		<bean:define id="imageName" name="kmProductUploadFormBean" property="imageName" type="java.lang.String[]" />
	 	    <table  id="productImageTable">
	 	     
	 	     <logic:iterate name="imageTitle" id="images" indexId="rowCount" >
		 		<TR class="clearfix padd10-0">				
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
            <logic:iterate id="productImageList" name="kmProductUploadFormBean" indexId="rowCount" property="productImageList">  
			<% if (rowCount<1)
			{
			%>
          	<li class="clearfix">
                <span class="text2 fll" style="margin-right:10px;"><strong><bean:message key="product.upload.imageTitle"/></strong></span>
                 <p class="clearfix fll margin-r20"> <span class="textbox6">
	                <html:text name="productImageList"  property="imageTitle" value="" styleClass="textbox7" indexed="true"  /> 
	                </span>
	             </p>	                
                <span class="text2 fll" style="margin-left:40px; margin-right:10px;"><strong><bean:message key="product.upload.attachImage"/></strong></span>
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
       
       <br><br><p>
      <div class="button-area">
       <div class="button"><input class="red-btn" name="" value="Update" onclick="javascript:return updateProductDetails();" /></div>
       <div class="button"> <a  class="red-btn" onclick="return clearForm();">clear</a></div>
      </div>
      
</html:form>
