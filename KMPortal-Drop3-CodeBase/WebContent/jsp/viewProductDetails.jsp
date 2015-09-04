<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<script type="text/javascript" src="jScripts/favorites.js"></script>
<%@page import="java.io.PrintWriter"%>
<bean:define id="kmUserBean" name="USER_INFO"  type="com.ibm.km.dto.KmUserMstr" scope="session" />  
<LINK href="theme/text.css" rel="stylesheet" type="text/css">
	<bean:define id="kmProductUploadFormBean" name="kmProductUploadFormBean" type="com.ibm.km.forms.KmProductUploadFormBean" scope="request" />
	<center><b><FONT color="red"><html:errors/></FONT></b></center>
	
	<% if(null != request.getAttribute("statusMessage")) out.print(request.getAttribute("statusMessage")); %>
	 
	<logic:notEmpty name="kmProductUploadFormBean" property="topic">
<div id="mainContent">
	<div class="box2">
        <div class="content-upload"> 	    
     		<% if ( kmUserBean.isCsr()) { %>
            <span id="fav">
				<%
	  				String docId = kmProductUploadFormBean.getDocId();
	  				session.setAttribute("docId", docId);
	  			 %>
	  			 <jsp:include page="AddToFavourite.jsp"></jsp:include>  				
  			 </span>
  		    <%} %>
  		    <h1 class="clearfix" style="margin-bottom:0px;"><bean:write name="kmProductUploadFormBean" property="topic" /></h1>
	   </div>
	   		<bean:define id="headerText" name="kmProductUploadFormBean" property="header" type="java.lang.String[]" />
			<bean:define id="contentText" name="kmProductUploadFormBean" property="content" type="java.lang.String[]" />
	 		 <div class="customer-called">
	 			<logic:iterate name="headerText" id="headers" indexId="rowCount" >
	 			<%
	 			if(contentText[rowCount].length() > 0 || headerText[rowCount].length() > 0) {
	 			 %>
	 			<ul class="table-data clearfix" style="word-wrap: break-word">
					<li><%= headerText[rowCount] %></li>
					<li class="last"><%= contentText[rowCount] %></li>
			    </ul>
			    <%}%>
			</logic:iterate>        
           </div> 
     </div>



     <div class="thumb-area clearfix">
      
      	<bean:define id="imageTitle" name="kmProductUploadFormBean" property="imageTitle" type="java.lang.String[]" />
	 	<bean:define id="imageName" name="kmProductUploadFormBean" property="imageName" type="java.lang.String[]" />
	 	
	 	<logic:iterate name="imageTitle" id="images" indexId="rowCount" >
			  <div><img src='<%=request.getContextPath()+"/ImageProviderServlet?requestType=productImage&imagePath="+imageName[rowCount]%>'   /><p><%=imageTitle[rowCount]%></p></div>
			  <br>
	    </logic:iterate>   		
      </div>
     
       <% if ( kmUserBean.isCsr()) { %>
        <div id="stayConnectLabel" style="height: 30px">&nbsp;</div>
      	<div class="button-area2 clearfix" id="stayConnectDiv">
    	 <div class="button2"><img src="common/images/button-stay-red.jpg" alt="stay" onclick="addToStay('documentAction.do?methodName=addToStay&viewerAction=productUploadConnect.do&viewerMethodName=viewProductDetails&docID=<%= kmProductUploadFormBean.getDocId()%>')" width="76" height="35" border="0" /></div>  
    	 <div class="button2"><img src="common/images/button-connect.jpg" alt="connect" onclick=connect('<%= session.getAttribute("stayURL")%>') width="76" height="35" border="0" /></div>
     	</div>
       <%}else{%>
     	<div class="button-area2 clearfix">
     	 <div class="button2"><a href="productUpload.do?methodName=viewEditProductDetails&docID=<%= kmProductUploadFormBean.getDocId()%>"  class="red-btn" >Edit</a></div>
   	    </div>
   	   <%}%>
   	   
   	   
  <div id="multiDocViewer"></div>
  </div>
</logic:notEmpty>       
<script type="text/javascript">
highlight(document.getElementById("mainContent"),'<%=request.getParameter("searchKeyword") %>' , "HL");
</script>