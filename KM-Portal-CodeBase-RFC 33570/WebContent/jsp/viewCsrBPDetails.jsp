<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<head>
<style>
.table-wrapper { 
    overflow-x:scroll;
    overflow-y:visible;
    width:560px;
    margin-left: 200px;
}
td, th {
    padding: 2px 2px;
    width: 140px;
}
td:first-child {
    position: absolute;    
    left: 5px
 }
 
.bill-go-view-top {
    padding:0;
    margin:0;
    list-style-type:none;
    width:1500px;
}
.bill-go-view-top li {
    width:280px;
    float:left;
}    


</style>
</head>
<script type="text/javascript" src="common/js/jquery.mousewheel.js"></script>
<!-- the jScrollPane script -->
<script type="text/javascript" src="common/js/jquery.jscrollpane.min.js"></script>
<script type="text/javascript">
			$(function()
			{
				$('.scroll-pane').jScrollPane(
				);
			});
</script>
        
<script type="text/javascript">
            $(document).ready(function(){
                $(".content-table tr:odd").addClass("odd");
                $(".content-table tr:not(.odd)").addClass("even");  
            });
			
			$(document).ready(function(){
                $(".content-table-type2 tr:odd").addClass("odd");
                $(".content-table-type2 tr:not(.odd)").addClass("even");  
            });
</script>
<script type="text/javascript">
function keySearch(){
		if(document.kmBPUploadFormBean.searchKey.value == "")
		{
			alert("Please Enter Search Key");
			return false;
		}
		document.kmBPUploadFormBean.isTop15.value = "N";
		document.kmBPUploadFormBean.methodName.value="csrViewBPDetails";
}
function showTop15()
	{
	document.kmBPUploadFormBean.isTop15.value = "Y";
	document.kmBPUploadFormBean.methodName.value="csrViewBPDetails";
	
	}



</script>
		<!-- 	<div class="bill-view-go">
				<html:form action="/bpUpload" method="post">
				<html:hidden property="methodName"/><html:hidden property="isTop15"/>
				 <table cellpadding="0" cellspacing="0" border="0"><tr><td class="bill-view-go-input-bg" nowrap="nowrap" valign="middle"> 
				    <html:text property="searchKey" style="border:none; width:263px; height:49px; margin:0; padding:30px 10px 10px 10px; background:none;" />
						<input name="" type="image" src="common/images/button-go.jpg" onclick="return keySearch();"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<html:submit  property="top15" value="Top 15" onclick="showTop15();" ></html:submit>
						</td></tr>
						
						</table> 
				</html:form>
			</div>  -->
			<div class="bill-view-go">
				<html:form action="/bpUpload" method="post">
				<html:hidden property="methodName"/><html:hidden property="isTop15"/>
					<ul class="bill-go-view-top">
						<li class="bill-view-go-input-bg"><html:text property="searchKey" style="border:none; width:263px; height:49px; margin:0; padding:15px 10px 10px 10px; background:none;" /></li>
						<li style="vertical-align: top"><input name="" type="image" src="common/images/button-go.jpg" onclick="return keySearch();"/> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
						<input name="" type="image" src="images\Top15PostPaid.jpg" onclick="showTop15();" align="middle" style="vertical-align: top" /></li>
						
					</ul>
				</html:form>
			</div>
		   		<%
		   		int size = 4;
	   		 %>
	   		<div></div>
	   		<br>
	   		<div></div>
   <div class="boxt2" >
   
    <div class="content-upload clearfix" >
      <h1 style="margin-bottom:0px;"><span class="text">Bill View </span></h1>
	</div>
	<div class="content-upload clearfix" >
<div class="content-table-type2 clearfix" >
     <div class=" table-wrapper " >
		<table width="100%" border="0" cellspacing="0" cellpadding="0" style="table-layout: fixed;" >
		  <logic:notEmpty name="kmBPUploadFormBean" property="billPlansList">
			<tr class="first" style="word-wrap: break-word">
		   		<logic:notEmpty name="kmBPUploadFormBean" property="headers"> 
		   			<td width="150px" align="left" valign="top" nowrap="nowrap" >Plan Name</td>	
		  				<logic:iterate id="billPlansData" name="kmBPUploadFormBean" property="billPlansList" type="java.util.HashMap" indexId="ctr" >
		    				<td width="150px" align="left" valign="top"><%=billPlansData.get("TOPIC")%> </td>
		  				</logic:iterate>
		  		</logic:notEmpty>
	  		</tr>
  			
			<logic:notEmpty name="kmBPUploadFormBean" property="headers"> 
  			<logic:iterate id="headers" name="kmBPUploadFormBean" property="headers" type="com.ibm.km.dto.KmBPUploadDto" indexId="i">
    
    			<%String styleClass =""; %>
    			<logic:equal name="headers" property="categoryId" value="0">
    			<tr class='first' style="word-wrap: break-word;" >
    			</logic:equal>
    			<logic:notEqual name="headers" property="categoryId" value="0">
    			<tr  style="word-wrap: break-word;" >
    			</logic:notEqual>
    			
    			<td  align="left"><bean:write name="headers" property="headerName"/></td>
     			<logic:iterate id="billPlansData" name="kmBPUploadFormBean" property="billPlansList" type="java.util.HashMap" indexId="ctr" >
					<logic:notEqual name="headers" property="categoryId" value="0">
  	    				<td align="left" class="start" ><%
  	    				String temp = "";
  	    				if (billPlansData.get(""+headers.getHeaderId() ) != null)
  	    				temp = (String) billPlansData.get(""+headers.getHeaderId());
	  	    				out.print(temp);
  	    				%></td>
  	    				
					</logic:notEqual>
					<logic:equal name="headers" property="categoryId" value="0">
  	    				<td style="height: 25px"> </td>
					</logic:equal>
  
  				</logic:iterate>
  				</tr>
  			</logic:iterate>  
  		 </logic:notEmpty>
  		 </logic:notEmpty>
  		 <logic:empty name="kmBPUploadFormBean" property="billPlansList"><font color="red">No Records Found</font></logic:empty>
  		</table>
	</div>
   </div></div>
 </div>
  