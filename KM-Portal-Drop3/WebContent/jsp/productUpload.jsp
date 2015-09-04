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

function InitInternal(s)
{
	s.designMode = 'On';
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
    
	
		var reg=/^[0-9a-zA-Z ]*$/;
		var isHeader = false;
   		var obj = document.getElementById('createMultiple');
	    var selectedCircles='';
    
    
		
		if(document.forms[0].topic.value == "")
		{
			alert("Please Enter Topic.");
			document.forms[0].topic.focus();
			return false;
		}
		    
    
	if(null != document.getElementById('createMultiple')  && null != document.getElementById("label_3"))
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
    }
       
	   for (var ii=0; ii<document.forms[0].header.length; ii++) 
	   {  
	       
	    document.forms[0].content[ii].value = document.getElementsByName("content")[ii].value;
	    document.forms[0].plainContent[ii].value = document.getElementsByName("plainContent")[ii].value;
	    if( "" != document.forms[0].header[ii].value)
	    {
	       isHeader = true;
	       
	       if("" == document.forms[0].content[ii].value)
			{
			 alert("Please enter Content for header : "+ document.forms[0].header[ii].value );
			 return false;	
		    }	
	       
	       
	       
	       		
		}    
	  }
 
	
	   if(document.forms[0].publishDt.value == "")
		{
			alert("Please enter Start date.");
			return false;			
		}
		else if(document.forms[0].publishDt.value < curr_dt)
		{
			alert("Start Date cannot be a past date (day before today).");
			return false;			
		}else if(document.forms[0].endDt.value == "")
		{
			alert("Please enter End date.");
			return false;			
		}
		else if(document.forms[0].endDt.value < curr_dt)
		{
			alert("End Date cannot be a past date (day before today).");
			return false;			
		}
		else if(document.forms[0].endDt.value < document.forms[0].publishDt.value)
		{
			alert("Start Date should not be greater than End Date.");
			return false;			
		}	
		
		if(!isHeader)
	    {
	      alert("Please enter value for Header.");
	      document.forms[0].header[0].focus();
	      return false;
	    }
	    
		return true;
 }
 
 function submitForm()
	{ 
	  var docPath = "";
	  var isCategorySelected = false;
	         
		for(var ii=2; ii<=9; ii++)
	    {
	      if(undefined != document.getElementsByName("productImageList["+(ii-2)+"].imageTitle")[0])
    	  { 
			if(! validateImage(document.getElementsByName("productImageList["+(ii-2)+"].imageFile")[0]))
			{
			  return false;
			}
	      }
	      
	      if("" != document.getElementById("label_"+ii).title)
	      { 
	         if(ii >= 4)
	         {
	          isCategorySelected =  true;
	         }
	         docPath = docPath +"/"+ document.getElementById("label_"+ii).title ;
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
		}
		else
		{
			return false;
		}
	}

function InitEditModeFrames(s)
{
	var iFrm = s.contentWindow;
	Init(iFrm);
}


 function addMoreContentFields()
 {
    var ul = document.getElementById("contentUL");
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

    var contentText = "<p class='clearfix fll margin-r20'> <span class='textbox6'> <span class='textbox6-inner'><input type='text' name='header' value='' class='textbox7'></span> </span> </p>";
    	//contentText = contentText + "<iframe id='iView"+arrIndex+"' name=c Class='textarea2 fll' frameborder='0'></iframe>";
    	contentText += "<p class='clearfix fll margin-r20'><span><input type='' name='content' id ='content'+arrIndex+ class='textarea2 fll'/>"  ;
    	//contentText = contentText +  " <input type='hidden' name='content' value='' id='content"+arrIndex+"'>";
    	contentText = contentText +  " <input type='hidden' name='plainContent' value='' id='plainContent"+arrIndex+"'></span><p>";
    	
    li.innerHTML = contentText;
    ul.appendChild(li); 
    var iViewObj = "iView"+arrIndex;
    var winObj = window.document;
    var obj = winObj.getElementById(iViewObj);
    InitEditModeFrames(obj);
 }

  var imgCount = 0;
 
 function addMoreImageFields()
 {  
    var ul = document.getElementById("imageUL");
    var li = document.createElement("li");
    if(ul.getElementsByTagName("li").length>4)
    {
      alert("Maximum only 5 images can be uploaded");
      return false;
    }

	var liClass = "";
	if(imgCount % 2 )
    liClass = "clearfix";
    else
    liClass = "clearfix alt";
    li.setAttribute("class", liClass);
 	imgCount++;

    var contentText = "<span class='text2 fll' style='margin-right:10px;'><strong>Title</strong></span><p class='clearfix fll margin-r20'> <span class='textbox6'><input type='text' name='productImageList["+ul.getElementsByTagName("li").length+"].imageTitle' value='' class='textbox7'></span></p><span class='text2 fll' style='margin-left:10px; margin-right:5px;'><strong>Attach an image</strong></span><p class='clearfix fll '><span><input type='file' name='productImageList["+ul.getElementsByTagName("li").length+"].imageFile' value='' onchange='validateImage(this)'></span></p><br>";
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
function validateImage(obj)
 {  
   for (var ii=0; ii<2; ii++) 
   {  
	    if(obj == document.getElementsByName("productImageList["+ii+"].imageFile")[0] && "" != obj.value  )
	    {
		  var imageName = document.getElementsByName("productImageList["+ii+"].imageFile")[0].value;
		  var lastIndex = imageName.lastIndexOf(".");
		  var imageType = imageName.substring(lastIndex);
		  var reg=/^[0-9a-zA-Z_. ]*$/;
		  
		  var ilastIndex = imageName.lastIndexOf("\\");
		  var actualImageName = imageName.substring(ilastIndex+1);		 
		  
			if(!reg.test(actualImageName)){
				alert("Image Name can have only Alpha-Numeric, space and underscore");
				obj.select();
			    return false;		
			}
		
			  if(imageType == ".jpg" || imageType == ".JPG" ||  imageType == ".gif" || imageType == ".GIF")
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
</script>
<html:form action="/productUpload" enctype="multipart/form-data"> 
	<html:hidden property="methodName" value="insertUpdate"/>
	<html:hidden property="userAction" value="insert"/>
	<html:hidden property="createdBy" styleId="createdBy"/>
	<html:hidden property="token" styleId="token"/>
    <html:hidden property="elementFolderPath" styleId="elementFolderPath"/>
    
	  <div class="box2">
       <div class="content-upload">
         <h1>Content/Product upload</h1>
         <strong>
	          	 <html:messages id="msg" message="true">
	                 <bean:write name="msg"/> 
	             </html:messages>
         </strong>
            
         <h3 align="center"> <FONT color="red"><html:errors/></FONT></h3>
             <div class="path-selection clearfix"> 
         			<!--  include here -->
      			 <jsp:include page="PathSelection.jsp"></jsp:include>
   			</div>
          <br>
         <div class="bill-upload-head2 box3 form1">
          <ul class="list2 clearfix">
           <li class="clearfix padd10-0">
                 <span style="margin-right:5px;" class="text5 fll"><bean:message key="product.upload.topic"/></span>
	              <p class="fll clearfix" style="margin-right:25px;"> <span class="common-textbox-l"> <span class="common-textbox-r">
                  <input type="text" name="topic" value="" class="textbox6" id="topic" style="width: 250px;" maxlength="500" > </span> </span> </p>
                  
                <span class="text5 fll"><bean:message key="createOffer.startDate"/></span>
                <html:text  styleClass="tcal calender1 fll" property="publishDt"  readonly="true" style="margin-right:15px;" />
                <span class="text5 fll"><bean:message key="createOffer.endDate"/></span>
                <html:text styleClass="tcal calender1 fll" property="endDt" readonly="true"  />
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
			
	      <ul class="list2 clearfix form1" id="contentUL" >
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
	                <p class="clearfix fll margin-r20"> 
				<span>
	                <html:textarea property="content" style="overflow:hidden" styleClass="textarea2 fll" value="<%= contentText[rowCount] %>"></html:textarea>
	               <html:hidden property="plainContent" styleId='<%="plainContent"+rowCount%>' value=""/></span></p>
          		</li>  
 		     </logic:iterate>	
		   </ul>	 
		    <a class="add-more-btn flr" style="margin-right: 5px;" onclick="addMoreContentFields()">&nbsp;</a>
		  <BR><BR>
		  
        </div>
        <jsp:include page="Disclaminer.jsp"></jsp:include>
        </div>
        

	
	   <div class="box2" style="margin-top:15px; position: relative;">
        <div class="content-upload" >
          <h1><bean:message key="product.upload.images"/></h1>
          
          <ul class="list2 clearfix form1" id="imageUL">
            <logic:iterate id="productImageList" name="kmProductUploadFormBean" indexId="rowCount" property="productImageList">  
			<% if (rowCount<1)
			{
			%>
          	<li class="clearfix">
                <span class="text2 fll" style="margin-right:10px;"><strong><bean:message key="product.upload.imageTitle"/></strong></span>
                 <p class="clearfix fll margin-r20"> <span class="textbox6">
	                <html:text name="productImageList"  property="imageTitle"  styleClass="textbox7" indexed="true"  /> 
	                </span>
	             </p>	                
                <span class="text2 fll" style="margin-left:10px; margin-right:5px;"><strong><bean:message key="product.upload.attachImage"/></strong></span>
                <p class="clearfix fll ">
                	<span ><html:file name="productImageList" value="" property="imageFile" onchange="validateImage(this)" indexed="true" /></span>
                </p>
            </li>
            <%
			}
			%>
        </logic:iterate>  
        
          </ul>
                      <a class="add-more-btn flr" style="margin-right: 5px;" onclick="addMoreImageFields()">&nbsp;</a><br>
                      <span style="font-size:12px; vertical-align: bottom; margin-top: 10px;">Supported Image: jpg or gif; Size maximum: 2 MB</span>
        </div>   
       </div>  
       
       <br><p>
          
      <div class="button-area">
               <div class="button"> <a  class="red-btn" onclick="javascript:return submitForm();">submit</a></div>
       <div class="button"> <a  class="red-btn" onclick="return clearForm();">clear</a></div>
      </div>
      
</html:form>