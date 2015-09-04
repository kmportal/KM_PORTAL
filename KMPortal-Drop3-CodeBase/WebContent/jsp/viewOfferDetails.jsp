<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@page import="java.io.PrintWriter"%>
<LINK href="theme/text.css" rel="stylesheet" type="text/css">

<html:form action="/offerUpload" >
	<html:hidden property="methodName" value="viewOfferDetails"/>
	<html:hidden property="createdBy" styleId="createdBy"/>
	<html:hidden property="kmActorId"/>
	<bean:define id="KmOfferUploadFormBean" name="kmOfferUploadForm" type="com.ibm.km.forms.KmOfferUploadFormBean" scope="request" />
<div id="mainContent">
	<div class="box2">
        <div class="content-upload clearfix">
           <h1 class="clearfix"><span class="text">DTH Offer Details</span><span class="icon flr"></span></h1>
            <center>
              <strong>
	          	 <html:messages id="msg" message="true">
	                 <bean:write name="msg"/> 
	             </html:messages>
         	 </strong>
           <FONT color="red"><html:errors/></FONT></center>
           <ul class="table-data clearfix">
 			</ul>
 			
            <ul class="table-data clearfix" style="word-wrap: break-word">
	            <li>Bucket</li>
		        <li class="last"><bean:write name="kmOfferUploadFormBean" property="bucketDesc" /></li>
 			</ul>
 			<ul class="table-data clearfix" style="word-wrap: break-word">
	          	<li>Offer Title</li>
		        <li class="last"><bean:write name="kmOfferUploadFormBean" property="offerTitle" /></li>
		    </ul>    
		    
		    <ul class="table-data clearfix" style="word-wrap: break-word">
          		<li>Offer Description</li>
		        <li class="last"><bean:write name="kmOfferUploadFormBean" property="offerDesc" /></li>
	        </ul>
	        <ul class="table-data clearfix">
	          	<li>Bucket Start Date</li>
		        <li class="last"><bean:write name="kmOfferUploadFormBean" property="startDate" /></li>
		     </ul>
	         <ul class="table-data clearfix">
	          	<li>Bucket End Date</li>
		         <li class="last"><bean:write name="kmOfferUploadFormBean" property="endDate" /></li>
		     </ul>  
        </div>
     </div>
</div>
</html:form>

 
<script type="text/javascript">
highlight(document.getElementById("mainContent"),'<%=request.getParameter("searchKeyword") %>' , "HL");
</script>
