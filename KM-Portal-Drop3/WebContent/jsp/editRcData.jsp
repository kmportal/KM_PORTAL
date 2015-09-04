<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<script language="javascript">
var fieldCount = 0;
function clearForm()
{
 var con = document.forms[0].content;
 for(var i=0; i < con.length; i++)
 {
 		 con[i].value="";
 }
	document.forms[0].topic.value = "";
 	document.forms[0].rechargeValue.value = "";
 	document.forms[0].selectedCombo.value =  "-1";
 
 return false;
}

function formValidate(){

	if(validate())
	{
		document.forms[0].methodName.value = "updateRCData";
		document.forms[0].submit();
		return true;
	}
	 else
	 {
	   return false;
	 }
	
}

function validate(){

	var reg=/^[0-9a-zA-Z. ]*$/;
	var regNumOnly = /^[0-9]*$/;
	
	var obj = document.getElementById('createMultiple');
	var contentFlag = false,contentRegFlag = false;
   
   
    
	      
	   if(trim(document.forms[0].topic.value) == "")
		{
			alert("Please Enter the Topic");
			return false;
		}   
	      
	  if(trim(document.forms[0].rechargeValue.value) == "")
		{
			alert("Please Enter the RC Value");
			return false;
		}    
		
	
		if(!regNumOnly.test(trim(document.forms[0].rechargeValue.value)))
			 {
			 	document.forms[0].rechargeValue.focus();
			 	document.forms[0].rechargeValue.select();
			 	alert("Only Numeric Charcters allowed in RC Value.");
 		 		return false;
			 	
		    }
		
	 var today = new Date();
		var dd = today.getDate();
		var mm = today.getMonth()+1;//January is 0!
		var yyyy = today.getFullYear();
		if(dd<10){dd='0'+dd}
		if(mm<10){mm='0'+mm}
		var curr_dt=yyyy+'-'+mm+'-'+dd;	
	
	if(document.forms[0].startDt.value == "")
		{
			alert("Please enter the Start Date");
			return false;			
		}
		else if(document.forms[0].endDt.value == "")
		{
			alert("Please enter the End Date");
			return false;			
		}
		else if(document.forms[0].endDt.value < document.forms[0].startDt.value)
		{
			alert("Start Date should not be greater than End Date ");
			return false;			
		}
		
	if(document.forms[0].selectedCombo.value == "-1")
		{
			alert("Please Select the Combo");
			return false;
		}	
		     

 
    for (var ii=0; ii<fieldCount; ii++) 
	   { 
	  obj = document.getElementsByName("report["+ii+"].content")[0];
	      
		   if( "" == trim(obj.value))
		    {
		     contentFlag=true; 
		     obj.focus();
		    } 
	    
	       if( "" != trim(obj.value))
	       {
		     if(!reg.test(obj.value))
			 {
			 contentRegFlag = true;
			 obj.focus();
			 obj.select();
			 	
		    }
		  }    
	  } 
 
 	if(contentFlag)
 	{
 		alert("Please enter all the Content fields.");
 		return false;
 	}
 	

// 	if(contentRegFlag)
// 	{
// 		 alert("Special characters not allowed in Content.");
// 		 return false;
// 	}
	
	return true;
}

	function trim(string) 
	{

 	while (string.substring(0,1) == ' ') {
    string = string.substring(1,string.length);
  	}
  	while (string.substring(string.length-1,string.length) == ' ') {
    string = string.substring(0,string.length-1);
  	}
	return string;
	}


</script>

<html:form action="/rcContentUpload" method="POST" >
<html:hidden property="methodName" value="updateRCData"/>
<html:hidden property="documentId" styleId="documentId"/>
<bean:define id="kmRCContentUploadFormBean" name="kmRCContentUploadFormBean"  type="com.ibm.km.forms.KmRCContentUploadFormBean" scope="request" />

<input type="hidden" name="createMultiple" value="<%= ((String[])(kmRCContentUploadFormBean.getCreateMultiple()))[0] %>" id="createMultiple">

<html:hidden property="elementFolderPath" styleId="elementFolderPath"/>

     <div class="breadcrump">      
      <div class="box2">
        <div class="content-upload">
          <h1>Edit Recharges</h1>
          
        
        </div>
          
          <div class="bill-upload-head2 box3 form1">
            <ul class="list2 clearfix">
              <li class="clearfix" style="margin-bottom:0px;"> 
              <span class="text5 fll" style="margin-right:36px;"><strong>Topic<FONT color="red"> *</FONT></strong></span>
                <p class="fll clearfix" style="margin-right:180px;"> <span class="common-textbox-l"> <span class="common-textbox-r">
                  <html:text styleId="topic" styleClass="textbox5" property="topic" maxlength="500"/>
                  </span> </span> </p>
            <span class="text5 fll" style="margin-right:9px;"><strong>RC Value<FONT color="red"> *</FONT></strong></span>
              <p class="fll clearfix" style="margin-right:25px;"> <span class="common-textbox-l"> <span class="common-textbox-r">
                  <html:text styleId="rechargeValue" styleClass="textbox5" property="rechargeValue" maxlength="10"/>
                  </span> </span> </p>
            </li> 
	       <li class="clearfix" style="margin-bottom:0px;"> 
                <span class="text5 fll"><strong>Start Date<FONT color="red"> *</FONT></strong></span>
                <html:text  styleClass="tcal calender1 fll" property="startDt"  readonly="readonly" style="margin-right:15px;" />
                <span class="text5 fll">&nbsp;&nbsp;<strong>End Date<FONT color="red"> *</FONT></strong></span>
                <html:text styleClass="tcal calender1 fll" property="endDt" readonly="readonly"  />
                  <span class="text5 fll" style="margin-left:55px;"><strong>Combo</strong><FONT color="red"> *</FONT> </span>
					<bean:define id="elementList" name="comboList"  type="java.util.ArrayList"/> 
		               <html:select property="selectedCombo">
							<logic:notEmpty name="elementList">
							<html:option value="-1">Select Combo</html:option>
								<logic:iterate name="elementList" id="report" indexId="i" type="com.ibm.km.dto.KmRcTypeDTO">
									<html:option  value='<%=report.getRCTypeId()+""%>'> <%=report.getRCTypeValue()  %></html:option>
								</logic:iterate>
							  </logic:notEmpty>	
					  </html:select>
               
                </li>
            </ul>
          </div>
          <div class="box1 header1">
            <ul class="list1 clearfix">
              <li style="width:170px;">Header</li>
              <li>Value</li>
            </ul>
          </div>
          
          <ul class="list2 clearfix form1">
           <bean:define id="headerTexts" name="kmRCContentUploadFormBean" property="headers" type="java.lang.String[]" />
	 <bean:define id="contentTexts" name="kmRCContentUploadFormBean" property="contents" type="java.lang.String[]" />
	 		
          
          <logic:iterate name="headerTexts" id="report" indexId="rowCount" >
  				<%
					if (rowCount % 2 == 0) {
				%>
          	<li class="clearfix">
				<%
					} else {
				%>
          	<li class="clearfix alt">
          		<%
				 	}
				 %>
				 
				 <script type="text/javascript"> var fieldCount = <%= rowCount+1 %>  </script>
				 
				
				<p class="clearfix fll margin-r20">
                 	<html:hidden  styleId="header"  indexed="true" property="header" name="report"  value='<%= headerTexts[rowCount] %>'  />  
                	<span class="text2 fll" style="width:120px; font-weight:normal; color:#181818;">
                	<%= headerTexts[rowCount] %><FONT color="red"> *</FONT></span>
                </p>
                
				<p class="clearfix fll margin-r20">
                	<span class="textbox6">
                    	<span class="textbox6-inner">
                       	<html:text styleId="content" indexed="true" styleClass ="textbox77" property="content" name="report" value='<%= contentTexts[rowCount] %>'  maxlength="1000"/>
                        </span>
                    </span>
                </p>			
			                 </li>
					
					
			</logic:iterate>
			

         <li class="clearfix" style="padding-left:150px;">
            <input class="red-btn fll" style="margin-right:20px;" value="Submit" alt="Submit" onclick="return formValidate();" readonly="readonly"/> 
            <input class="red-btn fll"  style="margin-right:10px;"  value="Clear" alt="Clear" onclick="return clearForm();" readonly="readonly"/>
          </li>
          </ul>   
          <!--<div class="button"> <a  class="red-btn" onclick="return clearForm();">clear</a> </div>
          </div> 
        -->
        </div>
          <jsp:include page="Disclaminer.jsp"></jsp:include>
       </div>
        
</html:form>



