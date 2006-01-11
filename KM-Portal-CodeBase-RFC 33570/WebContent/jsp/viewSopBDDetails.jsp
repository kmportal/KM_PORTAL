
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<script type="text/javascript" src="jScripts/favorites.js"></script>
<%@page import="java.io.PrintWriter"%>
<LINK href="theme/text.css" rel="stylesheet" type="text/css"/>
<bean:define id="kmUserBean" name="USER_INFO"  type="com.ibm.km.dto.KmUserMstr" scope="session" /> 
<bean:define id="kmSopBDUploadFormBean" name="kmSopBDUploadFormBean" type="com.ibm.km.forms.KmSopBDUploadFormBean" scope="request" />
<center><b><FONT color="red"><html:errors/></FONT></b></center>
  <% if(null != request.getAttribute("statusMessage")) out.print(request.getAttribute("statusMessage")); %>
	<logic:notEmpty name="kmSopBDUploadFormBean" property="topic">
	 <bean:define id="headerText" name="kmSopBDUploadFormBean" property="header" type="java.lang.String[]" />
	 <bean:define id="contentText" name="kmSopBDUploadFormBean" property="content" type="java.lang.String[]" />
<div id="mainContent">
	 <div class="box2">
    	<div class="content-upload">
    	 <% if ( kmUserBean.isCsr()) { %>
    	    <span id="fav">
						<%
  			String docId = kmSopBDUploadFormBean.getDocId();
  			session.setAttribute("docId", docId);
  			 %>
  			 <jsp:include page="AddToFavourite.jsp"></jsp:include>
  				
  			 </span>
  		   <%} %>
    	 <h1 class="clearfix" style="margin-bottom:0px;">
    	 	<span class="text" style="font-weight:normal; width:670px; float:left;"><%= kmSopBDUploadFormBean.getTopic() %>	</span>
         </h1></div> 
         <div class="customer-called">
         
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
   
    <div id="sop-inner-tabs">
          <ul class="list3 clearfix">
            <li><a href="#sop-tab1">VAS</a></li>
            <li><a href="#sop-tab2">Voice &amp; SMS</a></li>
            <li><a href="#sop-tab3">MO &amp; NOP</a></li>
            <li><a href="#sop-tab4">case not known</a></li>
            <li><a href="#sop-tab5">airtel live / wap</a></li>
          </ul>
      <div class="box4">
         <div class="content-upload sop-bd-tabs clearfix" id="sop-tab1">

            <h2 class="new">Account Related</h2>
            <table class="table12"  style="table-layout: fixed;">
            	<thead>
                	<tr>
                    	<th class="first"> <bean:message key="product.upload.header"/></th>
                        <th> <bean:message key="product.upload.content"/></th>
                    </tr>
                </thead>
                <tbody>
                
            <bean:define id="headerVasText" name="kmSopBDUploadFormBean" property="headerVas"  type="java.lang.String[]" />
 			<bean:define id="contentVasText" name="kmSopBDUploadFormBean" property="contentVas"  type="java.lang.String[]" />
	 		
	 		<logic:iterate name="headerVasText" id="headers" indexId="rowCount" >
				<%
				String cssName = "";				
				if( rowCount%2==1)
				{			
				cssName = "alt";
				}	
				%>
				<tr class="<%=cssName%>">	
                    	<td style="word-wrap: break-word"><%= headerVasText[rowCount] %></td>
                        <td style="word-wrap: break-word"><%= contentVasText[rowCount] %></td>
                </tr>
			</logic:iterate>
           </tbody>
          </table>
        </div>

        <div class="content-upload sop-bd-tabs clearfix" id="sop-tab2">

            <h2 class="new">Account Related</h2>
            <table class="table12"  style="table-layout: fixed;">
            	<thead>
                	<tr>
                    	<th class="first"><bean:message key="product.upload.header"/></th>
                        <th><bean:message key="product.upload.content"/></th>
                    </tr>
                </thead>
                <tbody>

	 <bean:define id="headerVoiceText" name="kmSopBDUploadFormBean" property="headerVoice" type="java.lang.String[]" />
			 <bean:define id="contentVoiceText" name="kmSopBDUploadFormBean" property="contentVoice" type="java.lang.String[]" />
	 		
	 		<logic:iterate name="headerVoiceText" id="headers" indexId="rowCount" >			
				<%
				String cssName = "";				
				if( rowCount%2==1)
				{			
				cssName = "alt";
				}	
				%>
				<tr class="<%=cssName%>">	
                    	<td style="word-wrap: break-word"><%= headerVoiceText[rowCount] %></td>
                        <td style="word-wrap: break-word"><%= contentVoiceText[rowCount] %></td>
                </tr>
			</logic:iterate>
				
                </tbody>
            </table>
        </div>
         
        <div class="content-upload sop-bd-tabs clearfix" id="sop-tab3">

            <h2 class="new">Account Related</h2>
            <table class="table12"  style="table-layout: fixed;">
            	<thead>
                	<tr>
                    	<th class="first"><bean:message key="product.upload.header"/></th>
                        <th><bean:message key="product.upload.content"/></th>
                    </tr>
                </thead>
                <tbody>
            
             <bean:define id="headerMoText" name="kmSopBDUploadFormBean" property="headerMo" type="java.lang.String[]" />
			 <bean:define id="contentMoText" name="kmSopBDUploadFormBean" property="contentMo" type="java.lang.String[]" />
	 		
	 		<logic:iterate name="headerMoText" id="headers" indexId="rowCount" >
		    	<%
				String cssName = "";				
				if( rowCount%2==1)
				{			
				cssName = "alt";
				}	
				%>
				<tr class="<%=cssName%>">	
                    	<td style="word-wrap: break-word"><%= headerMoText[rowCount] %></td>
                        <td style="word-wrap: break-word"><%= contentMoText[rowCount]%></td>
                </tr>
			</logic:iterate>

                </tbody>
            </table>
        </div>
       
        <div class="content-upload sop-bd-tabs clearfix" id="sop-tab4">

            <h2 class="new">Account Related</h2>
            <table class="table12"  style="table-layout: fixed;">
            	<thead>
                	<tr>
                    	<th class="first"><bean:message key="product.upload.header"/></th>
                        <th><bean:message key="product.upload.content"/></th>
                    </tr>
                </thead>
                <tbody>
                <bean:define id="headerCNNText" name="kmSopBDUploadFormBean" property="headerCNN" type="java.lang.String[]" />
			    <bean:define id="contentCNNText" name="kmSopBDUploadFormBean" property="contentCNN" type="java.lang.String[]" />
		 		<logic:iterate name="headerCNNText" id="headers" indexId="rowCount" >	
					<%
					String cssName = "";				
					if( rowCount%2==1)
					{			
					cssName = "alt";
					}	
					%>
					<tr class="<%=cssName%>">	
	                    	<td style="word-wrap: break-word"><%= headerCNNText[rowCount] %></td>
	                        <td style="word-wrap: break-word"><%= contentCNNText[rowCount] %></td>
	                </tr>
				</logic:iterate>

                </tbody>
            </table>
            
            </div>
            <div class="content-upload sop-bd-tabs clearfix" id="sop-tab5">

            <h2 class="new">Account Related</h2>
            <table class="table12"  style="table-layout: fixed;">
            	<thead>
                	<tr>
                    	<th class="first"><bean:message key="product.upload.header"/></th>
                        <th><bean:message key="product.upload.content"/></th>
                    </tr>
                </thead>
                <tbody>
                <bean:define id="headerALiveText" name="kmSopBDUploadFormBean" property="headerALive" type="java.lang.String[]" />
			 	<bean:define id="contentALiveText" name="kmSopBDUploadFormBean" property="contentALive" type="java.lang.String[]" />
	 			<logic:iterate name="headerALiveText" id="headers" indexId="rowCount" >
					<%
					String cssName = "";				
					if( rowCount%2==1)
					{			
					cssName = "alt";
					}	
					%>
					<tr class="<%=cssName%>">	
	                    	<td style="word-wrap: break-word"><%= headerALiveText[rowCount] %></td>
	                        <td style="word-wrap: break-word"><%= contentALiveText[rowCount] %></td>
	                </tr>
				</logic:iterate>
                </tbody>
            </table>
          </div>
         </div>
        </div>
        <% if ( kmUserBean.isCsr()) {         %>
        <div id="stayConnectLabel" style="height: 30px">&nbsp;</div>
      	<div class="button-area2 clearfix" id="stayConnectDiv">
    	 <div class="button2"><img src="common/images/button-stay-red.jpg" alt="stay" onclick="addToStay('documentAction.do?methodName=addToStay&viewerAction=sopBDUploadConnect.do&viewerMethodName=viewSopBDDetails&docID=<%= kmSopBDUploadFormBean.getDocId()%>')" width="76" height="35" border="0" /></div>  
    	 <div class="button2"><img src="common/images/button-connect.jpg" alt="connect" onclick=connect('<%= session.getAttribute("stayURL")%>') width="76" height="35" border="0" /></div>
     	</div>
       <%}else
        //if ( kmUserBean.getKmActorId().equals("1") || kmUserBean.getKmActorId().equals("5")) 
       {
        %>
	    <div class="button-area2 clearfix">
     	  <div class="button2"><a href="sopBDUpload.do?methodName=viewEditSopBDDetails&docID=<%= kmSopBDUploadFormBean.getDocId()%>"  class="red-btn" >Edit</a></div>    
   	   </div>
   	    <%}%>
     <div id="multiDocViewer">      
     </div>
     
     </div>
</logic:notEmpty>   
<script type="text/javascript">
highlight(document.getElementById("mainContent"),'<%=request.getParameter("searchKeyword") %>' , "HL");
$(document).ready(function() {
	   
	   //When page loads...
       $(".sop-bd-tabs").hide(); //Hide all content
       $("ul.list3 li a:first").addClass("active").show(); //Activate first tab
       $(".sop-bd-tabs:first").show(); //Show first tab content

       //On Click Event
       $("ul.list3 li a").click(function() {

               $("ul.list3 li a").removeClass("active"); //Remove any "active" class
               $(this).addClass("active"); //Add "active" class to selected tab
               $(".sop-bd-tabs").hide(); //Hide all tab content

               var activeTab = $(this).attr("href"); //Find the href attribute value to identify the active tab + content
               $(activeTab).fadeIn("fast"); //Fade in the active ID content
               return false;
       });
});
</script>