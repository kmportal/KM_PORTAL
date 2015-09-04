<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@page import="java.io.PrintWriter"%>

<script type="text/javascript">

 function validate()
 {
    var reg=/^[0-9a-zA-Z ]*$/;
    var tobeUpdate = true;
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
		else if(document.forms[0].endDt.value == "")
		{
			alert("Please enter Publish End Date.");
			return false;			
		}
		else if(document.forms[0].endDt.value < document.forms[0].publishDt.value)
		{
			alert("Publish Start Date should not be greater than End Date.");
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
			 document.forms[0].content[ii].focus();
			 return false;	
		    }		    
		}    
	  }
	
	  if( undefined != document.forms[0].headerVoice)
	  {
		  for (ii=0; ii<document.forms[0].headerVas.length; ii++) 
		  {  
		    if( "" != document.forms[0].headerVas[ii].value)
		    {	       
			    document.forms[0].contentVas[ii].value=arrVas[ii].document.body.innerHTML;  			
			    document.forms[0].plainContentVas[ii].value=arrVas[ii].document.body.innerText;  			
	  			if("" == document.forms[0].contentVas[ii].value)
				{
				 alert("Please enter Content for VAS.");
				 document.forms[0].contentVas[ii].focus();
				 return false;	
			    }		    
			} 
		  }
	  }
	  if( undefined != document.forms[0].headerVoice)
	  {
		  for (ii=0; ii<document.forms[0].headerVoice.length; ii++) 
		  {  
				 document.forms[0].contentVoice[ii].value=arrVoice[ii].document.body.innerHTML;
				 document.forms[0].plainContentVoice[ii].value=arrVoice[ii].document.body.innerText;
		  }
	  }
	  if( undefined != document.forms[0].headerMo)
	  {
		  for (ii=0; ii<document.forms[0].headerMo.length; ii++) 
		  {  
				 document.forms[0].contentMo[ii].value=arrMo[ii].document.body.innerHTML;
				 document.forms[0].plainContentMo[ii].value=arrMo[ii].document.body.innerText;
		  }
	  }
	  if( undefined != document.forms[0].headerCNN)
	  {
		  for (ii=0; ii<document.forms[0].headerCNN.length; ii++) 
		  {  
				 document.forms[0].contentCNN[ii].value=arrCNN[ii].document.body.innerHTML;
				 document.forms[0].plainContentCNN[ii].value=arrCNN[ii].document.body.innerText;
		  }
	  }
	  if( undefined != document.forms[0].headerALive)
	  {
		  for (ii=0; ii<document.forms[0].headerALive.length; ii++) 
		  {  
				 document.forms[0].contentALive[ii].value=arrALive[ii].document.body.innerHTML;
				 document.forms[0].plainContentALive[ii].value=arrALive[ii].document.body.innerText;
		  }  
	  }
	  if(!tobeUpdate)
	    {
	      alert("Please enter value for Header.");
	      document.forms[0].header.focus();
	      return false;
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

</script>

<script type="text/javascript" src="jScripts/sopbd.js"></script>
<bean:define id="kmSopBDUploadFormBean" name="kmSopBDUploadFormBean" type="com.ibm.km.forms.KmSopBDUploadFormBean" scope="request" />
  
<html:form action="/sopBDUpload" >
<html:hidden property="methodName" value="updateSOPBD"/>
<html:hidden property="createdBy" styleId="createdBy"/>
<html:hidden property="xmlFileName" value="<%= kmSopBDUploadFormBean.getXmlFileName()%>" />
<html:hidden property="sopPathId" styleId="sopPathId"/>
<html:hidden property="kmActorId"/>
<html:hidden property="docId" value="<%= kmSopBDUploadFormBean.getDocId()%>" />
 
  <div class="box2" >
  <div class="content-upload">
  <h1>Edit : SOP BD Details</h1>
        <FONT color="red"><html:errors/></FONT>
	    <TABLE align="center" border="0" cellpadding="0" cellspacing="0"> 
			<TBODY>	<tr><td  align="center" class="error"><strong> <html:messages id="msg" message="true">
			         <bean:write name="msg"/>  </html:messages> </strong> </td></tr></TBODY>
		</TABLE>  
     <bean:define id="headerText" name="kmSopBDUploadFormBean" property="header" type="java.lang.String[]" />
	 <bean:define id="contentText" name="kmSopBDUploadFormBean" property="content" type="java.lang.String[]" />
	 <bean:define id="plainContentText" name="kmSopBDUploadFormBean" property="plainContent" type="java.lang.String[]" />
	 
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
	                <html:text property="header"  styleClass="textbox7" value="<%= headerText[rowCount] %>"/> 
	                </span> </span> </p>
	                 <iframe id="iView<%=rowCount%>" name=c Class="textarea2 fll" src="ImageProviderServlet?requestType=xmlFile&docPath=<%=kmSopBDUploadFormBean.getXmlFileName()%>&tagId=<%=rowCount%>&tagName=CONTENT" frameborder='0'></iframe>

 
	                   	<script type="text/javascript">
	                		Init(document.iView<%=rowCount%>);
	                	</script>
	                <input type="hidden" name="content" value="" id='<%="content"+rowCount%>' >
	                <input type="hidden" name="plainContent" value="" id='<%="plainContent"+rowCount%>' >
          		</li>  
 		     </logic:iterate>	
		   </ul>	
			<br>
		     <a class="add-more-btn flr" onclick="addMoreContentFields()">&nbsp;</a>
		     <br><br>		
		    <jsp:include page="Disclaminer.jsp"></jsp:include>		
  </DIV>
  </DIV>		
	

           
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
	                <iframe id="iViewVas<%=rowCount%>" name=c Class="textarea2 fll" src="ImageProviderServlet?requestType=xmlFile&docPath=<%=kmSopBDUploadFormBean.getXmlFileName()%>&tagId=<%=rowCount%>&tagName=VASCONTENT" frameborder='0'></iframe>
	                <script type="text/javascript">
	                	InitVas(document.iViewVas<%=rowCount%>);
	                </script>
	                <input type="hidden" name="contentVas" id='<%="contentVas"+rowCount%>' value=""/>              
	                <input type="hidden" name="plainContentVas" id='<%="plainContentVas"+rowCount%>' value=""/>              
          		</li>  
 		     </logic:iterate>	
		   </ul>
			<br>
		     <a class="add-more-btn flr" onclick="addMoreVASContentFields()">&nbsp;</a>
		     <br><br>

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
	                <iframe id="iViewVoice<%=rowCount%>" name=c Class="textarea2 fll"   src="ImageProviderServlet?requestType=xmlFile&docPath=<%=kmSopBDUploadFormBean.getXmlFileName()%>&tagId=<%=rowCount%>&tagName=VOICECONTENT" frameborder='0'></iframe>
	                <script type="text/javascript">
	                	InitVoice(document.iViewVoice<%=rowCount%>);
	                </script>
	                <html:hidden property="contentVoice" styleId='<%="contentVoice"+rowCount%>' value=""/>              
	                <html:hidden property="plainContentVoice" styleId='<%="plainContentVoice"+rowCount%>' value=""/>              
          		</li>  
 		     </logic:iterate>	
		   </ul>
			<br>
		     <a class="add-more-btn flr" onclick="addMoreVoiceContentFields()">&nbsp;</a>
			<br><br>
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
	                <iframe id="iViewMo<%=rowCount%>" name=c Class="textarea2 fll"   src="ImageProviderServlet?requestType=xmlFile&docPath=<%=kmSopBDUploadFormBean.getXmlFileName()%>&tagId=<%=rowCount%>&tagName=MOCONTENT" frameborder='0'></iframe>
	                <script type="text/javascript">
	                	InitMo(document.iViewMo<%=rowCount%>);
	                </script>
	                <html:hidden property="contentMo" styleId='<%="contentMo"+rowCount%>' value=""/>              
	                <html:hidden property="plainContentMo" styleId='<%="plainContentMo"+rowCount%>' value=""/>              
          		</li>  
 		     </logic:iterate>	
		   </ul>
			<br>
		     <a class="add-more-btn flr" onclick="addMoreMoContentFields()">&nbsp;</a>
			<br><br>
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
	                <iframe id="iViewCNN<%=rowCount%>" name=c Class="textarea2 fll"   src="ImageProviderServlet?requestType=xmlFile&docPath=<%=kmSopBDUploadFormBean.getXmlFileName()%>&tagId=<%=rowCount%>&tagName=CNNCONTENT" frameborder='0'></iframe>
	                <script type="text/javascript">
	                	InitCNN(document.iViewCNN<%=rowCount%>);
	                </script>
	                <html:hidden property="contentCNN" styleId='<%="contentCNN"+rowCount%>' value=""/>              
	                <html:hidden property="plainContentCNN" styleId='<%="plainContentCNN"+rowCount%>' value=""/>              
          		</li>  
 		     </logic:iterate>	
		   </ul>
			<br>
		     <a class="add-more-btn flr" onclick="addMoreCNNContentFields()">&nbsp;</a>
			<br><br>
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
	                <iframe id="iViewALive<%=rowCount%>" name=c Class="textarea2 fll"   src="ImageProviderServlet?requestType=xmlFile&docPath=<%=kmSopBDUploadFormBean.getXmlFileName()%>&tagId=<%=rowCount%>&tagName=ALIVECONTENT" frameborder='0'></iframe>
	                <script type="text/javascript">
	                	InitALive(document.iViewALive<%=rowCount%>);
	                </script>
	                <html:hidden property="contentALive" styleId='<%="contentALive"+rowCount%>' value=""/>              
	                <html:hidden property="plainContentALive" styleId='<%="plainContentALive"+rowCount%>' value=""/>              
          		</li>  
 		     </logic:iterate>	
		   </ul>
			<br>
		     <a class="add-more-btn flr" onclick="addMoreAliveContentFields()">&nbsp;</a>
		     <br><br>
          </div>
        </div>
      </div>
          
           <div class="button-area">
            <div class="button">
                <html:button property="Submit" value="Update" styleClass="red-btn" onclick="return updateSopDetails();">  </html:button> 
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