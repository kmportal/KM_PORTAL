<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import = "java.util.*" %>
<%@ page import = "java.text.SimpleDateFormat" %>


<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="common/css/style.css" type="text/css" rel="stylesheet" />
<link href="common/font/stylesheet.css" rel="stylesheet" type="text/css" />

<script type="text/javascript"> 
function formValidate(){
	if(validate())
	{
		document.forms[0].methodName.value = "viewRcCsr";
		//document.forms[0].submit();
		return true;
	}
	else
	   return false;
}
	
function validate()
{
  var regNumOnly = /^[0-9]*$/;
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
		    if(trim(document.forms[0].selectedRcCategory.value) == "-1"){
		    	alert("Please Select an RC Category");
		    	return false;
		    }
		    
   var chks = document.forms[0].checkedCombos;
	
	if (chks){ //checkbox(s) exists
		var checked = false;
		if (chks.length){ //multiple checkboxes
			var len = chks.length;
			for (var i=0; i<len; i++){
				if (chks[i].checked){
					checked = true;
					break;
				}
			}
		}
		else{ //single checkbox only
			checked = chks.checked;
		}
		if (!checked){
			alert("Please Check Atleast One Combo");
			return false;
		}
	}
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

function submitSearchValue(event)
{
   if (event.keyCode == 13)
    {
     if(!formValidate())
     {
      return false;
     }
    }
    return true;
}	
function selectCombos(){
//alert("helllo");
	for(var i=0; i<counter;i++){
	//alert(document.kmRCContentUploadFormBean.checkedCombos[i].value);
		document.kmRCContentUploadFormBean.checkedCombos[i].checked=true;
	}	        
}
  //selectCombos();
</script>
<html:form action="/rcContentUpload" method="POST" >

<html:hidden property="methodName" value="viewRcCsr"/>
<html:hidden property="firstView"/>
     
      <div class="boxt15 clearfix">
      <div class="bill-view-go fll">
 <ul id="rc-value">

   <li class="bill-view-go-input-bg">&nbsp;
    <html:text property="rechargeValue"  onkeypress="return submitSearchValue(event);" style="border:none; width:70px; height:40px; margin:0; padding:15px 10px 0px 15px; background:none;" styleId="rechargeValue"/>
   </li>
   <li><input onclick="return formValidate();" type="image" src="common/images/button-go.jpg" name=""></li>
 </ul>

</div>
<span class="text5 fll" style="margin-right:36px;"><strong>Select RC Type <FONT color="red"> *</FONT></strong></span>
						<bean:define id="categoryList" name="rcCategoryList"  type="java.util.ArrayList"/>  
								<html:select property="selectedRcCategory" styleClass="select1 fll width180">
								<html:option value="-1">Select RC Type</html:option>
								<logic:notEmpty name="categoryList">
								<logic:iterate name="categoryList" id="rcCat" indexId="i" type="com.ibm.km.dto.KmRcCategoryDTO">
									<html:option value='<%=rcCat.getRcTypeId()%>'> <%=rcCat.getRcType()%> / <%=rcCat.getRcSubType()%></html:option>
								</logic:iterate>
								</logic:notEmpty>
								</html:select>
		<div id="blue-chkbox" class=" clearfix ">
        	<div class="box4 clearfix">

       <ul class="list17 fll">
  
   <bean:define id="elementList" name="comboList"  type="java.util.ArrayList" scope="request"/>         
       <logic:notEmpty name="elementList">       	
        <li class="clearfix alt">
         <table>
          <logic:iterate name="elementList" id="report" indexId="i" type="com.ibm.km.dto.KmRcTypeDTO">
         <script type="text/javascript"> var counter=<%=i+1%></script>
         
              	<%
					if (i % 2 == 0) {
				%>
			<tr>
          	 <td> <html:multibox  property="checkedCombos" styleClass="checbox2" value='<%=report.getRCTypeId()+""%>' />
       		 <%=report.getRCTypeValue() %>  </td>          
				
          		<%
					} else {
				%>
			

          	 <td> <html:multibox  property="checkedCombos" styleClass="checbox2" value='<%=report.getRCTypeId()+""%>' />
       		 <%=report.getRCTypeValue() %>  </td>	</tr>
				

          	<%
				 	}
			%>
        
        </logic:iterate>
       </table>      		
      
      
         </li>
        </logic:notEmpty>	 
         
       </ul>
            </div>
        </div>
      </div>
      
      
      <div class="boxt2">
        <div class="content-upload clearfix">
          <h1 class="clearfix" style="margin-bottom:0px;"><span class="text">Recharges</span> </h1>
          <!-- <div class="content-table-type2 clearfix"> -->
          <div class="content-table-type2 clearfix">
            <table width="100%" border="0" cellspacing="0" cellpadding="0" style="table-layout: fixed;" >
              
              <bean:define id="headerList" name="fieldList"  type="java.util.ArrayList"/>
              <bean:define id="valuesList" name="valueList"  type="java.util.ArrayList" />
              
              <logic:notEmpty name="valuesList">
              
              <tr>
                <td align="left" valign="top"><strong>Header</strong></td>
                <td align="left" valign="top"><strong>Normal Base Offer Benefits</strong></td>
                <td align="left" valign="top"><strong>Normal Base Validity</strong></td>
                <td align="left" valign="top"><strong>Start Date</strong></td>
                <td align="left" valign="top"><strong>End Date</strong></td>
                </tr><tr></tr>
                 <logic:iterate name="valuesList" id="valueListElement" indexId="j" type="java.util.HashMap">
                 
                 <%String color;
                 if(valueListElement.get("expired").equals("Y")){
                 	color = "red";
                 }
                 else{
                 	color = "black";
                 }
                 %>
                <tr><td align="left" valign="top"><a href = "rcContentUpload.do?methodName=viewRCData&expired=<%=valueListElement.get("expired")%>&docID=<%=valueListElement.get("docId")%>"><font color=<%=color%>><%=valueListElement.get("-1") %></a></td>
                <logic:iterate name="headerList" id="report" indexId="i" type="com.ibm.km.dto.KmRcHeaderDTO">
                <%if(report.getRCHeader().equals("Normal Base Offer Benefits")||report.getRCHeader().equals("Normal Base Validity")){ %>
                <td align="left" valign="top"><font color=<%=color%>><%=valueListElement.get(report.getRCHeader())%></td>
                <%} %>
                </logic:iterate>
                <td><font color=<%=color%>><%=valueListElement.get("startDate")%></td>
                <td><font color=<%=color%>><%=valueListElement.get("endDate")%></td>
                </tr>
                </logic:iterate>
               
              </logic:notEmpty >  
            
            	<logic:empty name="valuesList">
            		<font color="red"><bean:write property="firstView" name="kmRCContentUploadFormBean"/></font>
            	</logic:empty>
             
              
            </table>
          </div>
        </div>
      </div>
   
   

</html:form>
<logic:empty name="valuesList">
<script type="text/javascript"> selectCombos();</script>
</logic:empty>
