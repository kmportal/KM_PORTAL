<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<script language="JavaScript" src="jScripts/KmValidations.js" type="text/javascript"> </script>

<script language="javascript">  
 
 
 function validate()
 {
		var today = new Date();
		var dd = today.getDate();
		var mm = today.getMonth()+1;
		var yyyy = today.getFullYear();
		if(dd<10){dd='0'+dd}
		if(mm<10){mm='0'+mm}
		var curr_dt=yyyy+'-'+mm+'-'+dd;
		
	    
    if("" == document.forms[0].bucketId.value)
	{
	 alert("Please Select Bucket.");
	 document.forms[0].bucketId.focus();
	 return false;	
    }
       
        if(document.forms[0].startDate.value == "")
		{
			alert("Please enter Bucket Start Date.");
			return false;			
		}
		else if(document.forms[0].endDate.value == "")
		{
			alert("Please enter Bucket End Date.");
			return false;			
		}		
		else if(document.forms[0].endDate.value < document.forms[0].startDate.value)
		{
			alert("Bucket Start Date should not be greater than Bucket End Date.");
			return false;			
		}	
		
    if("" == document.forms[0].offerTitle.value)
	{
	 alert("Please enter Offer Title.");
	 document.forms[0].offerTitle.focus();
	 return false;	
    }		
	    
	if("" == document.forms[0].offerDesc.value)
	{
	 alert("Please enter Offer Description.");
	 document.forms[0].offerDesc.focus();
	 return false;	
    }
    return true;
  }
function submitForm()
 { 
   if(validate())
   {
    document.forms[0].methodName.value ="updateOffer";
   	document.forms[0].submit();
   }
   else
   {
    return false;
   }
 }
 
 
 function clearForm()
 {
    document.forms[0].bucketId.value = "";
	document.forms[0].startDate.value = "";
	document.forms[0].endDate.value = "";
	document.forms[0].offerTitle.value = "";
	document.forms[0].offerDesc.value = "";
    return true;
  }
  
 function setBucket() 
 {
  document.forms[0].bucketDesc.value =   document.forms[0].bucketId[document.forms[0].bucketId.selectedIndex].text ;
 }
 
</script>
<html:form action="/offerUpload" >
	<html:hidden property="methodName" value="updateOffer"/>
	<html:hidden property="createdBy" styleId="createdBy"/>	
	<html:hidden property="bucketDesc" />
	<html:hidden property="offerId" />	
               <center> <strong><FONT color="red"><html:errors/></FONT>
                        <html:messages id="msg" message="true">
                 			<bean:write name="msg"/>  
	             		</html:messages></strong></center>
      <div class="box2">
        <div class="content-upload">
          <h1>Edit : DTH Offer</h1> 
          <ul class="list2 form1">
       	  <li class="clearfix">
          	<span class="text2 fll width160"><strong><bean:message key="createOffer.selectBucket" /></strong> </span>
            <html:select property="bucketId" name="kmOfferUploadFormBean" styleClass="select1" onchange="setBucket()">
								<html:option value="">Select Bucket</html:option>
								<logic:notEmpty name="kmOfferUploadFormBean" property="bucketList">
									<bean:define id="buckets" name="kmOfferUploadFormBean"	property="bucketList" /> 
									<html:options labelProperty="bucketDesc" property="bucketId" collection="buckets" />
								</logic:notEmpty>
			</html:select>
          </li>
          <li class="clearfix alt">
          	<span class="text2 fll width160"><strong>Bucket Start Date <font color=red>*</font> </strong> </span>
          	<html:text property="startDate" styleClass="tcal calender1 fll" readonly="true"/>
          </li>
          <li class="clearfix">
			<span class="text2 fll width160"><strong>Bucket End Date<font color=red>*</font></strong> </span>
			<html:text property="endDate"  styleClass="tcal calender1 fll" readonly="true"/>
          </li>                 
          
          <li class="clearfix alt">
          	<span class="text2 fll width160"><strong><bean:message key="createOffer.offerTitle" /></strong> </span>
            <p class="clearfix fll margin-r20" > <span class="textbox6" > <span class="textbox6-inner">
                <html:text styleClass="textbox10" property="offerTitle" maxlength='200' />
                </span> </span> </p>
          </li>
          <li class="clearfix">
          	<span class="text2 fll width160"><strong><bean:message key="createOffer.offerDesc" /></strong> </span>
			 <html:textarea styleClass="textarea1 fll" property="offerDesc"></html:textarea>
          </li>
         </ul>
       </div>
         <jsp:include page="Disclaminer.jsp"></jsp:include>
    </div>   
       
        <div class="button-area">
            <div class="button">
              <html:button property="Submit" value="Update" styleClass="red-btn" onclick="return submitForm();">  </html:button> 
            </div>
            <div class="button"> <a  class="red-btn" onclick="return clearForm();">clear</a> </div>
       </div>
      
</html:form>
