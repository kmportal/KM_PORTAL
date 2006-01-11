<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<bean:define id="kmUserBean" name="USER_INFO"  type="com.ibm.km.dto.KmUserMstr" scope="session" />
<script language="javascript"><!--
var selectedItem='';
 
 function validate()
 {
 	var reg=/^[0-9a-zA-Z ]*$/;
    var isHeader = false;
    
    var obj = document.getElementById('createMultiple');
    
    var selectedCircles='';
    
	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth()+1;
	var yyyy = today.getFullYear();
	if(dd<10){dd='0'+dd}
	if(mm<10){mm='0'+mm}
	var curr_dt=yyyy+'-'+mm+'-'+dd;
		
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
  
   // Validate Main Header
    for (var ii=0; ii<document.forms[0].header.length; ii++) 
	   { 
	  
	   if(document.forms[0].header[ii].value != "")
	    {
	        isHeader = true;
		   // document.forms[0].content[ii].value=arr[ii].document.body.innerHTML;
		    //document.forms[0].plainContent[ii].value=arr[ii].document.body.innerText;
		    
		    document.forms[0].content[ii].value=document.getElementsByName("content")[ii].value;
	    	document.forms[0].plainContent[ii].value=document.getElementsByName("plainContent")[ii].value;
	    	
  			if("" == document.forms[0].content[ii].value)
			{
			 alert("Please enter Content.");
			 document.forms[0].content[ii].focus();
			 return false;	
		    }
		   		    
		}    
	  }
	  
	// Validate and set content for VAS Tab
	for ( ii=0; ii<document.forms[0].headerVas.length; ii++) 
	   {  
	   	   	
	    if( "" != document.forms[0].headerVas[ii].value)
	    {
		    document.forms[0].contentVas[ii].value=document.getElementsByName("contentVas")[ii].value;
		    document.forms[0].plainContentVas[ii].value=document.getElementsByName("plainContentVas")[ii].value;
		    if("" == document.forms[0].contentVas[ii].value)
			{
			 alert("Please enter Content.");
			 document.forms[0].contentVas[ii].focus();
			 return false;	
		    }	 
		}    
	  }	  

	// Validate and set content for Voice Tab
	for ( ii=0; ii<document.forms[0].headerVoice.length; ii++) 
	   {  
	   //alert("arrVoice[ii].document.body.innerHTML  "+document.getElementsByName("contentVoice")[ii].value+"  arrVoice[ii].document.body.innerText "+document.getElementsByName("plainContentVoice")[ii].value);
	    if( "" != document.forms[0].headerVoice[ii].value)
	    {
		   // document.forms[0].contentVoice[ii].value=arrVoice[ii].document.body.innerHTML;
		    //document.forms[0].plainContentVoice[ii].value=arrVoice[ii].document.body.innerText;
		    document.forms[0].contentVoice[ii].value=document.getElementsByName("contentVoice")[ii].value;
		    document.forms[0].plainContentVoice[ii].value=document.getElementsByName("plainContentVoice")[ii].value;
  			if("" == document.forms[0].contentVoice[ii].value)
			{
			
			 alert("Please enter Content.");
			 document.forms[0].contentVoice[ii].focus();
			 return false;	
		    }		    
		}    
	  }	  

	// Validate and set content for Mo Tab
	for ( ii=0; ii<document.forms[0].headerMo.length; ii++) 
	   {  
	   // alert("arrVoice[ii].document.body.innerHTML  "+document.getElementsByName("contentMo")[ii].value+"  arrVoice[ii].document.body.innerText "+document.getElementsByName("plainContentMo")[ii].value);
	    if( "" != document.forms[0].headerMo[ii].value)
	    {
		   // document.forms[0].contentMo[ii].value=arrMo[ii].document.body.innerHTML;
		   // document.forms[0].plainContentMo[ii].value=arrMo[ii].document.body.innerText;
		   
		   document.forms[0].contentMo[ii].value=document.getElementsByName("contentMo")[ii].value;
		   document.forms[0].plainContentMo[ii].value=document.getElementsByName("plainContentMo")[ii].value;
  			if("" == document.forms[0].contentMo[ii].value)
			{
			
			 alert("Please enter Content.");
			 document.forms[0].contentMo[ii].focus();
			 return false;	
		    }		    
		}    
	  }	  
	
	// Validate and set content for CNN Tab
	for ( ii=0; ii<document.forms[0].headerCNN.length; ii++) 
	   {  
	  // alert("arrVoice[ii].document.body.innerHTML  "+document.getElementsByName("contentCNN")[ii].value+"  arrVoice[ii].document.body.innerText "+document.getElementsByName("plainContentMo")[ii].value);
	   
	    if( "" != document.forms[0].headerCNN[ii].value)
	    {
		    //document.forms[0].contentCNN[ii].value=arrCNN[ii].document.body.innerHTML;
		    //document.forms[0].plainContentCNN[ii].value=arrCNN[ii].document.body.innerText;
		    
		    document.forms[0].contentCNN[ii].value=document.getElementsByName("contentCNN")[ii].value;
		    document.forms[0].plainContentCNN[ii].value=document.getElementsByName("plainContentCNN")[ii].value;
		    
  			if("" == document.forms[0].contentCNN[ii].value)
			{
			
			 alert("Please enter Content.");
			 document.forms[0].contentCNN[ii].focus();
			 return false;	
		    }		    
		}    
	  }	  
	
	// Validate and set content for ALive Tab
	for ( ii=0; ii<document.forms[0].headerALive.length; ii++) 
	   {  
	   //alert("arrVoice[ii].document.body.innerHTML  "+document.getElementsByName("contentALive")[ii].value+"  arrVoice[ii].document.body.innerText "+document.getElementsByName("plainContentALive")[ii].value);
	    if( "" != document.forms[0].headerALive[ii].value)
	    {
		    //document.forms[0].contentALive[ii].value=arrALive[ii].document.body.innerHTML;
		    //document.forms[0].plainContentALive[ii].value=arrALive[ii].document.body.innerText;
		    
		    document.forms[0].contentALive[ii].value=document.getElementsByName("contentCNN")[ii].value;
		    document.forms[0].plainContentALive[ii].value=document.getElementsByName("plainContentCNN")[ii].value;
  			if("" == document.forms[0].contentALive[ii].value)
			{
			
			 alert("Please enter Content.");
			 document.forms[0].contentALive[ii].focus();
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
	   	
	    for(var ii=2; ii<=9; ii++)
	    {
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
		
		   document.forms[0].methodName.value="insertSOPBD";
		   document.forms[0].submit();
		}
		else
		{		    
			return false;
		}
		
		return false;
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
	    arrVas[ii].document.body.innerHTML="";
		arrVoice[ii].document.body.innerHTML="";
		arrMo[ii].document.body.innerHTML="";
		arrCNN[ii].document.body.innerHTML="";
		arrALive[ii].document.body.innerHTML="";
	  }
}	


  
 --></script>


<script type="text/javascript" src="jScripts/sopbd.js"></script>

<html:form action="/sopBDUpload" >
	<html:hidden property="methodName" value="insertSOPBD"/>
	<html:hidden property="createdBy" styleId="createdBy"/>
	<html:hidden property="token" styleId="token"/>
	<html:hidden property="parentId" styleId="parentId"/>
	<html:hidden property="elementLevel" styleId="elementLevel"/>
    <html:hidden property="elementFolderPath" styleId="elementFolderPath"/>
    <html:hidden property="sopPathId" styleId="sopPathId"/>
    
	 <div class="box2">
	        <div class="content-upload">
	          <h1>SOP BD Upload </h1>
	          <h3 align="center"> <FONT color="red"><html:errors/></FONT></h3>
                <div class="path-selection clearfix"> 
            			<!--  include here -->
         			 <jsp:include page="lobWiseUpload.jsp"></jsp:include>
      			</div>
      			<br>
         <div class="bill-upload-head2 box3 form1">
           <ul class="list2 clearfix">
           <li class="clearfix padd10-0">
	            <span style="margin-right:5px;" class="text5 fll"><bean:message key="product.upload.topic"/></span>
	              <p class="fll clearfix" style="margin-right:10px;"> <span class="common-textbox-l"> <span class="common-textbox-r">
                 <html:text property="topic"  styleClass="textbox" style="width:270px;" maxlength="500"/></span> </span> </p>
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
  
                  
            <bean:define id="headerText" name="kmSopBDUploadFormBean" property="header" type="java.lang.String[]" />
			<bean:define id="contentText" name="kmSopBDUploadFormBean" property="content" type="java.lang.String[]" />
			<bean:define id="plainContentText" name="kmSopBDUploadFormBean" property="plainContent" type="java.lang.String[]" />
			
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
	                <p class="clearfix fll margin-r20"> 
				<span>
	               <html:textarea property="content" style="overflow:hidden" styleClass="textarea2 fll" value="<%= contentText[rowCount] %>"></html:textarea>
	                
	                <!--<iframe id="iView<%=rowCount%>" name=c Class="textarea2 fll" frameborder='0'></iframe>
	                <script type="text/javascript">
	                	Init(document.iView<%=rowCount%>);
	                </script>
	                <html:hidden property="content" styleId='<%="content"+rowCount%>' value=""/>-->
	                <html:hidden property="plainContent" styleId='<%="plainContent"+rowCount%>' value=""/>  </span></p>            
          		</li>  
 		     </logic:iterate>	
		   </ul>	 
        <br>
             <a class="add-more-btn flr"  style="margin-right: 5px;"  style="margin-right: 5px;" onclick="addMoreContentFields()"></a>
             <br><br><br>
        </div>
        	<jsp:include page="Disclaminer.jsp"></jsp:include>
    </div>
      
      
          
     <div id="sop-bd-tabs">
		        <ul class="list3 clearfix">
		      	<li><a href="#sop-tab1">VAS</a></li>
		        <li><a href="#sop-tab2">Voice &amp; SMS</a></li>
		        <li><a href="#sop-tab3">MO &amp; NOP</a></li>
		        <li><a href="#sop-tab4">case not known</a></li>
		        <li><a href="#sop-tab5">airtel live / wap</a></li>
		        </ul>
      <div class="box2" style="padding-top:22px;">
           <div id="sop-tab1" class="content-upload sop-bd-tabs">
           
           	<div class="box1 header1">
	            <ul class="list1 clearfix">
	              <li style="width:330px;"><bean:message key="product.upload.header"/></li>
	              <li><bean:message key="product.upload.content"/></li>
	            </ul>
	        </div>        
  
                  
            <bean:define id="headerVasText" name="kmSopBDUploadFormBean" property="headerVas" type="java.lang.String[]" />
			<bean:define id="contentVasText" name="kmSopBDUploadFormBean" property="contentVas" type="java.lang.String[]" />
			<bean:define id="plainContentVasText" name="kmSopBDUploadFormBean" property="plainContentVas" type="java.lang.String[]" />
			
		    <ul class="list2 clearfix form1" id="contentListVAS" >
			  <logic:iterate name="headerVasText" id="headers" indexId="rowCount" >
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
	                <html:text property="headerVas"  styleClass="textbox7" value="<%= headerVasText[rowCount] %>"/> 
	                </span> </span> </p>
	                <p class="clearfix fll margin-r20"> 
				<span>
	                <html:textarea property="contentVas" style="overflow:hidden" styleClass="textarea2 fll" value="<%= contentVasText[rowCount] %>"></html:textarea>
	                <!--<iframe id="iViewVas<%=rowCount%>" name=c Class="textarea2 fll" frameborder='0'></iframe>
	                <script type="text/javascript">
	                	InitVas(document.iViewVas<%=rowCount%>);
	                </script>
	                <html:hidden property="contentVas" styleId='<%="contentVas"+rowCount%>' value=""/>              
	                --><html:hidden property="plainContentVas" styleId='<%="plainContentVas"+rowCount%>' value=""/>     </span></p>         
          		</li>  
 		     </logic:iterate>	
		   </ul>
		   <br>
		     <a class="add-more-btn flr"  style="margin-right: 5px;" onclick="addMoreVASContentFields()"></a>
		   <br><br><br> 
          </div>


		<div id="sop-tab2" class="content-upload sop-bd-tabs">
           
           	<div class="box1 header1">
	            <ul class="list1 clearfix">
	              <li style="width:330px;"><bean:message key="product.upload.header"/></li>
	              <li><bean:message key="product.upload.content"/></li>
	            </ul>
	        </div>        
  
                  
            <bean:define id="headerVoiceText" name="kmSopBDUploadFormBean" property="headerVoice" type="java.lang.String[]" />
			<bean:define id="contentVoiceText" name="kmSopBDUploadFormBean" property="contentVoice" type="java.lang.String[]" />
			<bean:define id="plainContentVoiceText" name="kmSopBDUploadFormBean" property="plainContentVoice" type="java.lang.String[]" />
			
		    <ul class="list2 clearfix form1" id="contentListVOICE" >
			  <logic:iterate name="headerVoiceText" id="headers" indexId="rowCount" >
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
	                <html:text property="headerVoice"  styleClass="textbox7" value="<%= headerVoiceText[rowCount] %>"/> 
	                </span> </span> </p>
	                <p class="clearfix fll margin-r20"> 
				<span>
	                 <html:textarea property="contentVoice" style="overflow:hidden" styleClass="textarea2 fll" value="<%= contentVoiceText[rowCount] %>"></html:textarea>
	                
	                
	                
	                <!--
	                <iframe id="iViewVoice<%=rowCount%>" name=c Class="textarea2 fll" frameborder='0'></iframe>
	                <script type="text/javascript">
	                	InitVoice(document.iViewVoice<%=rowCount%>);
	                </script>
	                <html:hidden property="contentVoice" styleId='<%="contentVoice"+rowCount%>' value=""/>              
	                --><html:hidden property="plainContentVoice" styleId='<%="plainContentVoice"+rowCount%>' value=""/>  </span></p>            
          		</li>  
 		     </logic:iterate>	
		   </ul>
		   	<br>
		     <a class="add-more-btn flr" style="margin-right: 5px;"  onclick="addMoreVoiceContentFields()">&nbsp;</a>
		    <br><br> <br>
          </div>

		<div id="sop-tab3" class="content-upload sop-bd-tabs">
           
           	<div class="box1 header1">
	            <ul class="list1 clearfix">
	              <li style="width:330px;"><bean:message key="product.upload.header"/></li>
	              <li><bean:message key="product.upload.content"/></li>
	            </ul>
	        </div>        
  
                  
            <bean:define id="headerMoText" name="kmSopBDUploadFormBean" property="headerMo" type="java.lang.String[]" />
			<bean:define id="contentMoText" name="kmSopBDUploadFormBean" property="contentMo" type="java.lang.String[]" />
			<bean:define id="plainContentMoText" name="kmSopBDUploadFormBean" property="plainContentMo" type="java.lang.String[]" />
			
		    <ul class="list2 clearfix form1" id="contentListMO" >
			  <logic:iterate name="headerMoText" id="headers" indexId="rowCount" >
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
	                <html:text property="headerMo"  styleClass="textbox7" value="<%= headerMoText[rowCount] %>"/> 
	                </span> </span> </p>
	                <p class="clearfix fll margin-r20"> 
				<span>
	                 <html:textarea property="contentMo" style="overflow:hidden" styleClass="textarea2 fll" value="<%= contentMoText[rowCount] %>"></html:textarea>
	                <!--<iframe id="iViewMo<%=rowCount%>" name=c Class="textarea2 fll" frameborder='0'></iframe>
	                <script type="text/javascript">
	                	InitMo(document.iViewMo<%=rowCount%>);
	                </script>
	                <html:hidden property="contentMo" styleId='<%="contentMo"+rowCount%>' value=""/>              
	                --><html:hidden property="plainContentMo" styleId='<%="plainContentMo"+rowCount%>' value=""/>      </span></p>        
          		</li>  
 		     </logic:iterate>	
		   </ul>
		   <br>
		     <a class="add-more-btn flr"  style="margin-right: 5px;"  onclick="addMoreMoContentFields()">&nbsp;</a>
		   <br><br> <br>
          </div>
          
          
          <div id="sop-tab4" class="content-upload sop-bd-tabs">
           
           	<div class="box1 header1">
	            <ul class="list1 clearfix">
	              <li style="width:330px;"><bean:message key="product.upload.header"/></li>
	              <li><bean:message key="product.upload.content"/></li>
	            </ul>
	        </div>        
  
                  
            <bean:define id="headerCNNText" name="kmSopBDUploadFormBean" property="headerCNN" type="java.lang.String[]" />
			<bean:define id="contentCNNText" name="kmSopBDUploadFormBean" property="contentCNN" type="java.lang.String[]" />
			<bean:define id="plainContentCNNText" name="kmSopBDUploadFormBean" property="plainContentCNN" type="java.lang.String[]" />
			
		    <ul class="list2 clearfix form1" id="contentListCNN" >
			  <logic:iterate name="headerCNNText" id="headers" indexId="rowCount" >
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
	                <html:text property="headerCNN"  styleClass="textbox7" value="<%= headerCNNText[rowCount] %>"/> 
	                </span> </span> </p>
	                <p class="clearfix fll margin-r20"> 
				<span>
	                <html:textarea property="contentCNN" style="overflow:hidden" styleClass="textarea2 fll" value="<%= contentCNNText[rowCount] %>"></html:textarea>
	                
	                <!--<iframe id="iViewCNN<%=rowCount%>" name=c Class="textarea2 fll" frameborder='0'></iframe>
	                <script type="text/javascript">
	                	InitCNN(document.iViewCNN<%=rowCount%>);
	                </script>
	                <html:hidden property="contentCNN" styleId='<%="contentCNN"+rowCount%>' value=""/>              
	                --><html:hidden property="plainContentCNN" styleId='<%="plainContentCNN"+rowCount%>' value=""/>  </span></p>            
          		</li>  
 		     </logic:iterate>	
		   </ul>
			<br>
		     <a class="add-more-btn flr"  style="margin-right: 5px;"  onclick="addMoreCNNContentFields()">&nbsp;</a>
		   <br><br> <br>
          </div>
          
          
          
          <div id="sop-tab5" class="content-upload sop-bd-tabs">
           
           	<div class="box1 header1">
	            <ul class="list1 clearfix">
	              <li style="width:330px;"><bean:message key="product.upload.header"/></li>
	              <li><bean:message key="product.upload.content"/></li>
	            </ul>
	        </div>        
  
                  
            <bean:define id="headerALiveText" name="kmSopBDUploadFormBean" property="headerALive" type="java.lang.String[]" />
			<bean:define id="contentALiveText" name="kmSopBDUploadFormBean" property="contentALive" type="java.lang.String[]" />
			<bean:define id="plainContentALiveText" name="kmSopBDUploadFormBean" property="plainContentALive" type="java.lang.String[]" />
			
		    <ul class="list2 clearfix form1" id="contentListALIVE" >
			  <logic:iterate name="headerALiveText" id="headers" indexId="rowCount" >
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
	                <html:text property="headerALive"  styleClass="textbox7" value="<%= headerALiveText[rowCount] %>"/> 
	                </span> </span> </p>
	                <p class="clearfix fll margin-r20"> 
				<span>
	                <html:textarea property="contentALive" style="overflow:hidden" styleClass="textarea2 fll" value="<%= contentALiveText[rowCount] %>"></html:textarea>
	                <!--<iframe id="iViewALive<%=rowCount%>" name=c Class="textarea2 fll" frameborder='0'></iframe>
	                <script type="text/javascript">
	                	InitALive(document.iViewALive<%=rowCount%>);
	                </script>
	                <html:hidden property="contentALive" styleId='<%="contentALive"+rowCount%>' value=""/>  -->            
	                <html:hidden property="plainContentALive" styleId='<%="plainContentALive"+rowCount%>' value=""/> </span></p>             
          		</li>  
 		     </logic:iterate>	
		   </ul>
		   <br>
		     <a class="add-more-btn flr"  style="margin-right: 5px;"  onclick="addMoreAliveContentFields()">&nbsp;</a>
		   <br><br> <br>
          </div>
        </div>
      </div>
            <br>
            <br>
          <div class="button-area">
            <div class="button">
              <input class="submit-btn1 red-btn" name="" value="" onclick="return submitForm();" />
            </div>
            <div class="button"> <a  class="red-btn" onclick="return clearForm();">clear</a> </div>
          </div>
           
          
</html:form> 
<script type="text/javascript">
$(document).ready(function() {
	   
	   //When page loads...
       $(".sop-bd-tabs").hide(); //Hide all content
       $("ul.list3 li a:first").addClass("active").show(); //Activate first tab
       $(".sop-bd-tabs:first").show(); //Show first tab content

       //On Click Event
       $("ul.list3 li a").click(function() {

               $("ul.list3 li a").removeClass("active"); //Remove any "active" class
               $(this).addClass("active"); //Add "active" class to selected tab
               $(".sop-bd-tabs").hide(); //Hide all tab content

               var activeTab = $(this).attr("href"); //Find the href attribute value to identify the active tab + content
               $(activeTab).fadeIn("fast"); //Fade in the active ID content
               return false;
       });
});
</script>
