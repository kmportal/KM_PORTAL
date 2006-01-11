<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested" %>


<%@page import="java.util.ArrayList"%>
<%@page import="com.ibm.km.dto.KmBPUploadDto"%><script language="javascript">
	var path = '<%=request.getContextPath()%>';
	var port = '<%= request.getServerPort()%>';
	var serverName = '<%=request.getServerName() %>';
</script>

<script language="javascript">

if (!String.prototype.trim) {
 String.prototype.trim = function() {
  return this.replace(/^\s+|\s+$/g,'');
 }
}


function formValidate(){
var docPath = "";
	    for(var ii=2; ii<=9; ii++)
	    {
	      if("" != document.getElementById("label_"+ii).title)
	      { 
	         docPath = docPath +"/"+ document.getElementById("label_"+ii).title ;
	      } 	      
	    }
	    
	     document.getElementById("elementFolderPath").value = initialElementPath +docPath;
	//$id("parentId").value=parentId;
	if(validate()){
		document.kmBPUploadFormBean.methodName.value="insert";
		document.kmBPUploadFormBean.submit();
	}
}
function validate(){

		var selectedCircles='';
		var obj = document.getElementById('createMultiple');
		if("" == document.getElementById("label_4").title)
	      { 
	         alert("Content can only be uploaded atleast below Category level");
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
  

		var topic = document.kmBPUploadFormBean.topic.value;
		if(topic.trim() == "")
		{
			alert("Please Enter the Topic");
			document.kmBPUploadFormBean.topic.focus();
			return false;
		}   
		
		var today = new Date();
		var dd = today.getDate();
		var mm = today.getMonth()+1;//January is 0!
		var yyyy = today.getFullYear();
		if(dd<10){dd='0'+dd}
		if(mm<10){mm='0'+mm}
		//var curr_dt=mm+'/'+yyyy+'/'+dd;	
		var curr_dt=yyyy+'-'+mm+'-'+dd;	
		curr_dt = mm+'/'+dd+'/'+yyyy;
		//alert(document.kmBPUploadFormBean.fromDate.value);
		//alert(curr_dt);
		if(document.kmBPUploadFormBean.fromDate.value == "")
		{
			alert("Please enter the From Date");
			document.kmBPUploadFormBean.fromDate.focus();
			return false;			
		}
		else if(document.kmBPUploadFormBean.fromDate.value < curr_dt)
		{
			alert("From Date cannot be a past date ");
			return false;			
		}else if(document.kmBPUploadFormBean.toDate.value == "")
		{
			alert("Please enter the To Date");
			document.kmBPUploadFormBean.toDate.focus();
			return false;			
		}
		else if(document.kmBPUploadFormBean.toDate.value < document.kmBPUploadFormBean.fromDate.value)
		{
			alert("From Date should not be greater than To Date ");
			return false;			
		}
		for (var i=0;i<counter ;i++){
		if(document.getElementsByName("subheaders["+i+"].content")[0] != null && document.getElementsByName("subheaders["+i+"].content")[0].value == ""){
			alert("Enter All Fields");
			document.getElementsByName("subheaders["+i+"].content")[0].focus();
			return false;
			}
		}
		return true;
}

function clearForm()
{
 document.forms[0].reset();
 document.forms[0].topic.value = "";
 document.forms[0].initialSelectBox.value =  "";
 return false;
}
</script>

<html:form action="/bpUpload" method="post">
<html:hidden property="methodName"/>
<html:hidden property="elementFolderPath" styleId="elementFolderPath"/>
<div class="content clearfix">
    <div class="left-side clearfix">
      <div class="box2">
        <div class="content-upload">
          <h1><span class="text">Bill Upload</span></h1>
          <div class="bill-upload-head clearfix">
            <div class="fll">
              <div class="inner-breadcrumbs">
                <jsp:include page="PathSelection.jsp"></jsp:include>
              </div>
              <br>
        
		<div align="right" id="bpDivId" style="visibility:hidden">
				<a href="bpUpload.do?methodName=getBillPlanHits" id="bpUploadAnId">Get Top 15 plans</a>
              </div>
        
            </div>
        

          </div>
          <div class="bill-upload-head2 box3 form1">
            <ul class="list2 clearfix">
              <li class="clearfix" style="margin-bottom:0px;"> <span class="text5 fll">Topic</span>
                <p class="fll clearfix" style="margin-right:65px;"> <span class="common-textbox-l"> <span class="common-textbox-r">
                  <html:text property="topic" styleClass="textbox5" maxlength="500"></html:text>
                  </span> </span> </p>
                <span class="text5 fll">From Date</span>
                <html:text property="fromDate" readonly="true" size="14" styleClass="tcal calender1 fll" style="margin-right: 45px;" />
                <span class="text5 fll"> To Date</span>
                <html:text property="toDate" readonly="true" size="14" styleClass="tcal calender1 fll" />
                 </li>
            </ul>
          </div>
			
          <logic:iterate name="kmBPUploadFormBean" property="headers" id="headers" indexId="ctr" type="com.ibm.km.dto.KmBPUploadDto">
          	 <bean:define id="subheaders" name="kmBPUploadFormBean"/>
            <logic:equal name="headers" property="categoryId" value="0">
            	<div class="box1 header1" style="padding-left:12px;">
            		<ul class="list1 clearfix">
              			<li class=""><bean:write name="headers" property="headerName"/></li>
            		</ul>
          		</div>
            <ul class="list2 form1">
            <logic:iterate name="subheaders" property="headers" scope="page" id="subheaders" indexId="i" >
            	
            	
            	<logic:equal name="subheaders" property="categoryId" value='<%=headers.getHeaderId()%>'>
            	<script type="text/javascript"> var counter=<%=i+1%></script>
					
					<% String liClass;
						if(i%2 == 0)
							liClass = "clearfix padd10-0";
						else
							liClass = "clearfix alt padd10-0";
					%>
					<li class='<%=liClass %>'>
					
              		<span class="text2 fll width360" style="font-weight:normal; color:#181818;"><bean:write name="subheaders" property="headerName"/><FONT color="red"> *</FONT></span>
              		<p class="clearfix fll"> <span class="textbox6"> <span class="textbox6-inner">
              		<html:text name="subheaders" property="headerName" style="display:none" indexed="true"></html:text>
                	<html:text name="subheaders" property="headerId" style="display:none" indexed="true"></html:text>
                	<html:text name="subheaders" property="content" styleClass="textbox1" indexed="true" maxlength="1000"></html:text>
                	
                	</span> </span> </p>
              		            	
            		
            		</li>
            		
            	</logic:equal>
            	 
            </logic:iterate>
           </ul>
            </logic:equal>
            
            </logic:iterate>
          
          <div class="button-area">
            <div class="button"> <a href="#" class="red-btn" onclick="formValidate();">Submit</a></div> 
            
            <div class="button"> <a href="#" class="red-btn" onclick="clearForm();">Clear</a></div> &nbsp;
            
<!--               <input type="image" src="images/clear.jpg" style="margin-right:10px;" class="btnActive" onclick="return clearForm();" styleClass="red-btn fll"/> --> 
          </div>
        </div>
      </div>
      <jsp:include page="Disclaminer.jsp"></jsp:include>
    </div>
    
  </div>
</html:form>

