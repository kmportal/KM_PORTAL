<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<script language="javascript">

if (!String.prototype.trim) {
 String.prototype.trim = function() {
  return this.replace(/^\s+|\s+$/g,'');
 }
}

function Update(){
	if(validate()){
	document.kmBPUploadFormBean.methodName.value="updateBPDetails";
	document.kmBPUploadFormBean.submit();
	}
}
	
function validate(){
		var topic = document.kmBPUploadFormBean.topic.value;
		if(topic.trim() == "")
		{
			alert("Please Enter the Topic");
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
		if(document.kmBPUploadFormBean.fromDate.value == "")
		{
			alert("Please enter the From Date");
			return false;			
		}
		else if(document.kmBPUploadFormBean.toDate.value == "")
		{
			alert("Please enter the To Date");
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
document.kmBPUploadFormBean.topic.value = "";
 var con = document.kmBPUploadFormBean.content;
 for(var i=0; i < con.length; i++)
 {
 		 con[i].value="";
 }
 	document.kmBPUploadFormBean.topic.value = "";
 
 return false;
}
</script>
<bean:define id="kmBPUploadFormBean" name="KMBPUPLOADFORMBEAN" type="com.ibm.km.forms.KmBPUploadFormBean" scope="request"/>
<html:form action="/bpUpload" method="post">
<html:hidden property="methodName"/>
<html:hidden property="elementFolderPath" styleId="elementFolderPath"/>
<div class="content clearfix">
    <div class="left-side clearfix">
      <div class="box2">
        <div class="content-upload">
          <h1><span class="text">Edit Bill Plan</span></h1>
          <div class="bill-upload-head clearfix">
            <div class="fll">
              <div class="inner-breadcrumbs">
                
              </div>
            </div>

          </div>
          <div class="bill-upload-head2 box3 form1">
            <ul class="list2 clearfix">
              <li class="clearfix" style="margin-bottom:0px;"> <span class="text5 fll">Topic</span>
                <p class="fll clearfix" style="margin-right:25px;"> <span class="common-textbox-l"> <span class="common-textbox-r">
                  <html:text property="topic" styleClass="textbox5" styleId="topic" maxlength="500"></html:text>
                  </span> </span> </p>
                <span class="text5 fll" style="margin-left: 20px;">From</span>
                <html:text property="fromDate" readonly="true" size="14" styleClass="tcal calender1 fll" />
                 
                <span class="text5 fll" style="margin-left: 20px;">&nbsp;&nbsp;&nbsp;&nbsp;To</span>
                <html:text property="toDate" readonly="true" size="14" styleClass="tcal calender1 fll" />
                 
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
         
            <logic:iterate name="subheaders" property="dataList" scope="page" id="subheaders" indexId="i" >
            	<logic:equal name="subheaders" property="categoryId" value='<%=headers.getHeaderId()%>'>
			
					   <ul class="list2 form1">
					   <script type="text/javascript"> var counter=<%=i+1%></script>
					<% String liClass;
						if(i%2 == 0)
							liClass = "clearfix padd10-0";
						else
							liClass = "clearfix alt padd10-0";
					%>
					<li class='<%=liClass %>'>
					<!-- <input class="checkbox1" type="checkbox" /> -->
					
              		<span class="text2 fll width360" style="font-weight:normal; color:#181818;"><bean:write name="subheaders" property="headerName"/><FONT color="red"> *</FONT></span>
              		<p class="clearfix fll"> <span class="textbox6"> <span class="textbox6-inner">
              		<html:text name="subheaders" property="headerId" style="display:none" indexed="true"></html:text>
                	<html:text name="subheaders" property="headerName" style="display:none" indexed="true"></html:text>
                	<html:text name="subheaders" property="content" styleClass="textbox1" indexed="true" maxlength="1000" styleId="content"></html:text>
                	</span> </span> </p>
              		           	
            		
            		</li>
            		</ul>
            	</logic:equal>
            	 
            </logic:iterate>
           
            </logic:equal>
            
            </logic:iterate>
          
          <div class="button-area">
          
<!--             <div class="button">
               <html:button property="Submit" value="Update" styleClass="red-btn" onclick="return Update();">  </html:button> </div>&nbsp;
     -->
               
               <div class="button"> <a href="#" class="red-btn" onclick="Update();">Update</a></div> &nbsp;

               <div class="button"> <a href="#" class="red-btn" onclick="clearForm();">Clear</a></div> &nbsp;
               
<!--             	<input type="image" src="images/clear.jpg" style="margin-right:10px;" class="btnActive" onclick="return clearForm();" styleClass="red-btn fll"/> -->
 
          </div>
        </div>
      </div>
    </div>
    
  </div>
</html:form>