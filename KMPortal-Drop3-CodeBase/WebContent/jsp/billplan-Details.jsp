<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@page import="com.ibm.km.dto.KmBPUploadDto"%><script type="text/javascript" src="jScripts/favorites.js"></script>
<%request.setAttribute("KMBPUPLOADDTO",request.getAttribute("KMBPUPLOADDTO")); %>
<center><b><FONT color="red"><html:errors/></FONT></b></center>
<div id="mainContent">
<html:form action="/bpUpload" method="post">
<div class="content clearfix">
    <div class="left-side clearfix">
      <div class="box2">
        <div class="content-upload">
        <logic:notEmpty name="kmBPUploadFormBean" property="dataList">
       
          <h1><span class="text"> <bean:write name="kmBPUploadFormBean" property="topic"/> </span>
             <span id="fav">
						<%
				KmBPUploadDto kmbpdto = (KmBPUploadDto)request.getAttribute("KMBPUPLOADDTO");	
  			String docId = kmbpdto.getDocumentId();
  			session.setAttribute("docId", docId);
  			 %>
  			 <jsp:include page="AddToFavourite.jsp"></jsp:include>
  				
  			 </span>
          
          </h1>
  	    
<!--          <div class="bill-upload-head2 box3 form1">
             <ul class="list2 clearfix">
              <li class="clearfix" style="margin-bottom:0px;">                 
                <span class="width85 text5 fll" style="font-size:14px;">Start Date :</span>
                <span class="width120 text5 fll" style="font-size:14px;"><bean:write name="kmBPUploadFormBean" property="fromDate"/></span>
                
                <span class="width85 text5 fll" style="font-size:14px;">End Date :</span>
                <span class="width120 text5 fll" style="font-size:14px;"><bean:write name="kmBPUploadFormBean" property="toDate"/></span>
               
              </li>
            </ul>
          </div>
-->
			
          <logic:iterate name="kmBPUploadFormBean" property="headers" id="headers" indexId="ctr" type="com.ibm.km.dto.KmBPUploadDto">
          	 <bean:define id="subheaders" name="kmBPUploadFormBean"/>
            <logic:equal name="headers" property="categoryId" value="0">
            	<div class="box1 header1" style="padding-left:12px;">
            		<ul class="list1 clearfix">
              			<li class=""><span class="text2 fll" style="font-size:14px;" ><bean:write name="headers" property="headerName"/></span></li>
              			
            		</ul>
          		</div>
					<table class="list2 form1" width=100% style="table-layout: fixed;" cellspacing="1" cellpadding="3">
            <logic:iterate name="subheaders" property="dataList" scope="page" id="subheaders" indexId="i" >
            	<% String liClass;
						if(i%2 == 0)
							liClass = "clearfix";
						else
							liClass = "clearfix alt";
				%>
            	<logic:equal name="subheaders" property="categoryId" value='<%=headers.getHeaderId()%>'>
					<tr class='<%=liClass %>' style="word-wrap: break-word" >
					<td style="word-wrap: break-word; font-size:16px;font-family:Arial, Helvetica, sans-serif;" width=30%>	
              		<bean:write name="subheaders" property="headerName"/>
                	</td>
					<td style="word-wrap: break-word; font-size:13px;font-family:Arial, Helvetica, sans-serif;" width=70%>
                	<bean:write name="subheaders" property="content"/>
					</td>
            		</tr>
            	</logic:equal>
            	 
            </logic:iterate>
            		</table>
           
            </logic:equal>
            
            </logic:iterate>
          
			</logic:notEmpty>
        </div>
      </div>
    </div>
    
  </div>
</html:form>
  
<bean:define id="kmUserBean" name="USER_INFO"  type="com.ibm.km.dto.KmUserMstr" scope="session" />
<logic:notEmpty name="kmBPUploadFormBean" property="dataList">
     <p><br>
     <div id="multiDocViewer"> 
     
     </div>
     	<% 
        if (!kmUserBean.isCsr())
        { 
        %>
        <div class="button-area2 clearfix">
     	  <div class="button2"><a href="bpUpload.do?methodName=editBPDetails&docID=<bean:write name="kmBPUploadFormBean" property="documentId"/>"  class="red-btn" >Edit</a></div>    
   	   </div>
       <%
        }
        else
        {
       %>
        <div id="stayConnectLabel" style="height: 30px">&nbsp;</div>
      	<div class="button-area2 clearfix" id="stayConnectDiv">
    	 <div class="button2"><img src="common/images/button-stay-red.jpg" alt="stay" onclick="addToStay('documentAction.do?methodName=addToStay&viewerAction=bpUploadConnect.do&viewerMethodName=viewBPDetails&docID=<bean:write name="kmBPUploadFormBean" property="documentId"/>')" width="76" height="35" border="0" /></div>  
    	 <div class="button2"><img src="common/images/button-connect.jpg" alt="connect" onclick=connect('<%= session.getAttribute("stayURL")%>') width="76" height="35" border="0" /></div>
     	</div>
   	   <%
   	   }
       %>
       </logic:notEmpty>
 </div>     
   
<script type="text/javascript">
highlight(document.getElementById("mainContent"),'<%=request.getParameter("searchKeyword") %>' , "HL");
</script>
  
  
