<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<script language="javascript">
var selectedItem='';

	function formValidate(){
		var docPath = "";
		for(var ii=2; ii<=9; ii++){
			if("" != document.getElementById("label_"+ii).title){ 
				docPath = docPath +"/"+ document.getElementById("label_"+ii).title ;
				} 	      
		}
		    
		document.getElementById("elementFolderPath").value = initialElementPath +docPath;
		$id("parentId").value=parentId;
		var isPostpaid = false;
		for( ii=2; ii<=9; ii++){   
			if("" != document.getElementById("label_"+ii).title){ 
		      	if(selectedItem.indexOf("POSTPAID") > -1 || selectedItem.indexOf("postpaid") > -1 || selectedItem.indexOf("Postpaid") > -1 || (document.getElementById("label_4").innerHTML).indexOf("Postpaid") > -1 ){ 
			          isPostpaid =  true;
		         }	         
		      } 	      
		    }
		    if(isPostpaid){
		   		alert("RC content upload is not allowed for Postpaid Category.\nSelect other Category.");
			 	return false;
			}
			if(validate()){
				document.forms[0].methodName.value = "insertRCData";
				document.forms[0].submit();
				return true;
			}
		 	else{
		   		return false;
		 	}
			return true;
		}
	
	function validate(){
		var reg=/^[0-9a-zA-Z. ]*$/;
		var regNumOnly = /^[0-9]*$/;
		
		var obj = document.getElementById('createMultiple');
		var contentFlag = false,contentRegFlag = false;
	   
	    var selectedCircles='';
	    if(null != document.getElementById('createMultiple')  && null != document.getElementById("label_3")){ 
	  	 	for (var i = 1; i < obj.options.length; i++){ 
	          if(obj.options[i].selected){
	           selectedCircles = selectedCircles+","+obj.options[i].value ;
	          }
	       }
		if('' == selectedCircles){ 
			for ( i = 1; i < obj.options.length; i++){ 
		         if(obj.options[i].value == document.getElementById("label_3").title) {
		             obj.options[i].selected = true;
		           }
		        }
		    }
	    }
		if("" == document.getElementById("label_4").title){ 
			alert("Content can only be Uploaded atleast below Category level");
			return false;
			} 
		if(trim(document.forms[0].topic.value) == ""){
				alert("Please Enter the Topic");
				return false;
		}   
		if(trim(document.forms[0].rechargeValue.value) == ""){
				alert("Please Enter the RC Value");
				return false;
		}   
			if( document.forms[0].rechargeValue.value ==0){
				alert("RC Value cann't be Zero");
				document.forms[0].rechargeValue.select();
				document.forms[0].rechargeValue.focus();
				return false;
		} 
		if(!regNumOnly.test(trim(document.forms[0].rechargeValue.value))) {
			document.forms[0].rechargeValue.focus();
			document.forms[0].rechargeValue.select();
			alert("Only Numeric Charcters allowed in RC Value.");
			return false;
		}
		if(trim(document.forms[0].selectedRcCategory.value) == "-1"){
	    	alert("Please Select an RC Category");
	    	return false;
	    }
		var today = new Date();
		var dd = today.getDate();
		var mm = today.getMonth()+1;//January is 0!
		var yyyy = today.getFullYear();
		if(dd<10){
			dd='0'+dd
		}
		if(mm<10){
			mm='0'+mm
		}
		var curr_dt=yyyy+'-'+mm+'-'+dd;
		if(document.forms[0].startDt.value == ""){
			alert("Please enter the Start Date");
			return false;			
		}
		else if(document.forms[0].startDt.value < curr_dt){
			alert("Start Date cannot be a past date (day before today) ");
			return false;			
		}
		else if(document.forms[0].endDt.value == ""){
			alert("Please enter the End Date");
			return false;			
		}
		else if(document.forms[0].endDt.value < document.forms[0].startDt.value)
			{
				alert("Start Date should not be greater than End Date ");
				return false;			
			}
			
		for (var ii=0; ii<fieldCount; ii++){ 
			obj = document.getElementsByName("report["+ii+"].content")[0];
	    	if( "" == trim(obj.value)){
				alert("Please enter value for "+document.getElementsByName("report["+ii+"].header")[0].value);
				contentFlag=true; 
				obj.focus();
				return false;
			}
		    
		    /*    
		    if( "" != trim(obj.value))
		       {		    
			     if(!reg.test(obj.value))
				 {
				 alert("Special characters not allowed in "+document.getElementsByName("report["+ii+"].header")[0].value);
				 contentFlag=true; 
			     obj.focus();
			     return false;
				 	
			    }
			   } */ 
		    
		       
		  }
	 	if(contentFlag)
	 	{
	 		alert("Please enter all the Content fields.");
	 		return false;
	 	}
	 	
	 	if(contentRegFlag)
	 	{
	 		 alert("Special characters not allowed in Content.");
	 		 return false;
	 	}
		
		return true;
	}
	
	function trim(string){
		while (string.substring(0,1) == ' ') {
			string = string.substring(1,string.length);
		}
		while (string.substring(string.length-1,string.length) == ' ') {
			string = string.substring(0,string.length-1);
		}
		return string;
	}
	function clearForm(){
		document.forms[0].reset();
		document.forms[0].topic.value = "";
		document.forms[0].rechargeValue.value = "";
		document.forms[0].selectedCombo.value =  "-1";
		return false;
	}

</script>

<html:form action="/rcContentUpload" method="POST" >
<html:hidden property="parentId" styleId="parentId"/>
<html:hidden property="methodName" value="insertRCData"/>
<html:hidden property="elementLevel" styleId="elementLevel"/>
<html:hidden property="elementFolderPath" styleId="elementFolderPath"/>
<div class="box2">
	<div class="content-upload">
		<h1>Create Recharges</h1>
		<div class="bill-upload-head clearfix">
			<jsp:include page="PathSelection.jsp"></jsp:include>
		</div>
	</div>
	 <div class="bill-upload-head2 box3 form1">
		<ul class="list2 clearfix">
              <li class="clearfix" style="margin-bottom:0px;"> 
              <span class="text5 fll" style="margin-right:20px;"><strong><bean:message key="product.upload.topic"/></strong></span>
                <p class="fll clearfix" > <span class="common-textbox-l"> <span class="common-textbox-r">
                  <html:text styleId="topic" styleClass="textbox19" property="topic" maxlength="500"/>                  
                  </span> </span></p>
                  <span class="text5 fll" style="margin-right:20px;margin-left:10px"><strong>Select RC Type <FONT color="red"> *</FONT></strong></span>
                  <bean:define id="categoryList" name="rcCategoryList"  type="java.util.ArrayList"/>  
								<html:select property="selectedRcCategory" styleClass="select1 fll width180">
								<html:option value="-1">Select RC Type</html:option>
								<logic:notEmpty name="categoryList">
								<logic:iterate name="categoryList" id="rcCat" indexId="i" type="com.ibm.km.dto.KmRcCategoryDTO">
									<html:option value='<%=rcCat.getRcTypeId()%>'> <%=rcCat.getRcType()%> / <%=rcCat.getRcSubType()%></html:option>
								</logic:iterate>
								</logic:notEmpty>
								</html:select>
              <span class="text5 fll" style="margin-right:15px;margin-left:20px"><strong><bean:message key="rc.value"/></strong></span>
                <p class="fll clearfix" style="margin-right:20px;"> <span class="common-textbox-l"> <span class="common-textbox-r">
                  <html:text styleId="rechargeValue" styleClass="textbox19"  property="rechargeValue" maxlength="10"/>
                  </span> </span> </p>
            </li> 
	       <li class="clearfix" style="margin-bottom:0px;"> 
                <span class="text5 fll"><bean:message key="scroller.startDate"/></span>   
                <html:text styleClass="tcal calender1 fll" property="startDt" readonly="true"
			style="margin-right:15px;" />
                <span class="text5 fll"><bean:message key="scroller.endDate"/></span>
                <html:text
			styleClass="tcal calender1 fll" property="endDt" readonly="true" />
                  <span class="text5 fll" style="margin-left:55px;"><strong>Combo</strong></span>
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
              <li style="width:250px;">Header</li>
              <li style="width:250px;">Value</li>
              <li>Send in SMS</li>
            </ul>
          </div>
          <bean:define id="headerList" name="fieldList"  type="java.util.ArrayList"/> 
          
          <ul class="list2 clearfix form1">
          
          <logic:iterate name="headerList" id="report" indexId="i" type="com.ibm.km.dto.KmRcHeaderDTO">
  				<%
					if (i % 2 == 0) {
				%>
		
          	<li class="clearfix">
				<%
					} else {
				%>
				
          	<li class="clearfix alt">
          		<%
				 	}
				 %>
                <script type="text/javascript"> var fieldCount = '<%= i+1 %>';  
                </script>				 				 
                <p class="clearfix fll margin-r20">
                   	  
                <span class="text2 fll" style="width:180px; font-weight:normal; color:#181818;">
              	<html:text styleId="content" indexed="true" styleClass ="textbox11" property="header" name="report" value="<%=report.getRCHeader()%>"  maxlength="1000" readonly="true" />
                </span><FONT color="red"> *</FONT>
                </p> 
                <p class="clearfix fll margin-r20">
                	<span class="textbox6">
                    	<span class="textbox6-inner">
                       	<html:text styleId="content" indexed="true" styleClass ="textbox11" property="content" name="report" value=""  maxlength="1000" />
                        </span>
                    </span>
                </p>
                <!-- Added by Abu for configurable SMS -->
                <p>
                <html:checkbox name="report" property="isConfigurable" styleId="isConfigurable" indexed="true" value="Y"/>
                </p>
                <!-- Configurable SMS ends here  -->                                
             </li>
           </logic:iterate> 
           
         </ul> 
         <br>
         	<jsp:include page="Disclaminer.jsp"></jsp:include>
        </div>
        
<div class="button-area"  >
        <input class="red-btn fll"  style="margin-right:20px;" value="Submit" alt="Submit" onclick="javascript:return formValidate();" readonly="readonly"/>
       <input class="red-btn fll"  style="margin-right:10px;" value="Clear" alt="Clear" onclick="return clearForm();" readonly="readonly"/>
</div>
        


</html:form>