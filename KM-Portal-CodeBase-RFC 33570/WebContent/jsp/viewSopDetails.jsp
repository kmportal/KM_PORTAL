<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@page import="com.ibm.km.common.Utility"%><script type="text/javascript" src="jScripts/favorites.js"></script>
<%@page import="java.io.PrintWriter"%>
<LINK href="theme/text.css" rel="stylesheet" type="text/css" /> 
<bean:define id="kmUserBean" name="USER_INFO"  type="com.ibm.km.dto.KmUserMstr" scope="session" /> 
<bean:define id="kmSopUploadFormBean" name="kmSopUploadFormBean" type="com.ibm.km.forms.KmSopUploadFormBean" scope="request" />
	<center><b><FONT color="red"><html:errors/></FONT></b></center>
	<% if(null != request.getAttribute("statusMessage")) out.print(request.getAttribute("statusMessage")); %>
	<logic:notEmpty name="kmSopUploadFormBean" property="topic">
	<div id="mainContent">
	 <div class="box2">
    	<div class="content-upload">
    	<% if ( kmUserBean.isCsr()) { %>
    	       <span id="fav">
						<%
  			String docId = kmSopUploadFormBean.getDocId();
  			session.setAttribute("docId", docId);
  			 %>
  			 <jsp:include page="AddToFavourite.jsp"></jsp:include>
  				
  			 </span>
  		  <%} %>	
    	 <h1 class="clearfix" style="margin-bottom:0px;">
    	 	<span class="text" style="font-weight:normal; width:670px; float:left;"><%= kmSopUploadFormBean.getTopic() %>	</span>
         </h1></div> 
         <div class="customer-called">
          <bean:define id="headerText" name="kmSopUploadFormBean" property="header" type="java.lang.String[]" />
		  <bean:define id="contentText" name="kmSopUploadFormBean" property="content" type="java.lang.String[]" />
	 	
         	<logic:iterate name="headerText" id="headers" indexId="rowCount" >
         	 <%if(contentText[rowCount].length() > 0 || headerText[rowCount].length() > 0) { %>
		      <ul class="table-data clearfix" style="word-wrap: break-word">
				<li><%= headerText[rowCount] %></li>
				<li class="last"><%=contentText[rowCount] %></li>
			  </ul>
		     <%}%>
            </logic:iterate>
        </div>
     </div>
     
     
     <p></p>
     <br>
     
     
     <bean:define id="sopLinkHeaderText" name="kmSopUploadFormBean" property="productHeaders" type="java.lang.String[]" />
	 <bean:define id="sopLinkPathText" name="kmSopUploadFormBean" property="productPaths" type="java.lang.String[]" />
	 <bean:define id="sopLinkPathDescText" name="kmSopUploadFormBean" property="productPathsLabels" type="java.lang.String[]" />
     
     <logic:notEmpty name="sopLinkHeaderText" >
    
        <div class="box2">
         <div class="content-upload">
    	 	<h1 class="clearfix" style="margin-bottom:0px;">
    	 		<span class="text" style="font-weight:normal; width:165px; float:left;">Related Documents </span>
    	 	</h1>
    	 </div> 
         <div class="section clearfix">
           <ul class="list2 clearfix form1">
	 		<logic:iterate name="sopLinkHeaderText" id="headers" indexId="rowCount" >
	 		<%	
	 		String cssName = "clearfix";				
			if( rowCount%2==1)
			{			
			cssName = "clearfix alt";
			}	
			%>
	           	<li  class="<%=cssName %>"> <br>
	           	<A href='<%=Utility.getDocumentViewURL(""+sopLinkPathText[rowCount])%>' class="Red11"><%= sopLinkHeaderText[rowCount] %></A></li>
             </logic:iterate>
             </ul>	
		    </div>		      
        </div>
     </logic:notEmpty>
     
      <div class="thumb-area clearfix">
      
      	<bean:define id="imageTitle" name="kmSopUploadFormBean" property="imageTitle" type="java.lang.String[]" />
	 	<bean:define id="imageName" name="kmSopUploadFormBean" property="imageName" type="java.lang.String[]" />
	 	 <logic:iterate name="imageTitle" id="images" indexId="rowCount" >
				  <div>
				  <img src='<%=request.getContextPath()+"/ImageProviderServlet?requestType=productImage&imagePath="+imageName[rowCount]%>'   />
				  <p><%=imageTitle[rowCount]%></p></div>
				  <br>
		    </logic:iterate>   	
	 </div>
         
       <% if ( kmUserBean.isCsr()) { %>
        <div id="stayConnectLabel" style="height: 30px">&nbsp;</div>
      	<div class="button-area2 clearfix" id="stayConnectDiv">
    	  <div class="button2"><img src="common/images/button-stay-red.jpg" alt="stay" onclick="addToStay('documentAction.do?methodName=addToStay&viewerAction=sopUploadConnect.do&viewerMethodName=viewSopDetails&docID=<%= kmSopUploadFormBean.getDocId()%>')" width="76" height="35" border="0" /></div>  
    	  <div class="button2"><img src="common/images/button-connect.jpg" alt="connect" onclick=connect('<%= session.getAttribute("stayURL")%>') width="76" height="35" border="0" /></div>
     	</div>
       <%}else
        
     //  if ( kmUserBean.getKmActorId().equals("1") || kmUserBean.getKmActorId().equals("5")) 
       {
        %>
     	<div class="button-area2 clearfix">
     	  <div class="button2"><a href="sopUpload.do?methodName=viewEditSopDetails&docID=<%= kmSopUploadFormBean.getDocId()%>"  class="red-btn" >Edit</a></div>    
   	    </div>
   	   <%}%>
     <div id="multiDocViewer">     
     </div>
     </div>
</logic:notEmpty>	


 
<script>  
 highlight(document.getElementById("mainContent"),'<%=request.getParameter("searchKeyword") %>' , "HL");
</script>