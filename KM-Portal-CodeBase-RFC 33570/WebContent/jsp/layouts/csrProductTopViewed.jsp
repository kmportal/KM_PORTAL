
<%@page import="java.util.ArrayList"%>
<%@page import="com.ibm.km.forms.KmDocumentHitsCountFormBean"%>
<%@page import="com.ibm.km.dto.KmDocumentMstr"%>

<%@page import="com.ibm.km.common.Utility"%><script language="javascript">
function clickBottomLinks(url){ 
		window.location.href = url;
		//window.open("documentAction.do?methodName=displayDocument&docID="+url,'_blank',"resizable=yes,location=yes,toolbar=no,scrollbars=yes,menubar=no,status=no,directories=no,width=750,height=600,left=10,top=10");
}
</script>

<bean:define id="KmDocumentHitsList" name="BOTTOMBAR_LINKS"  type="java.util.ArrayList" scope="session" />
<% ArrayList<KmDocumentMstr> links = (ArrayList<KmDocumentMstr>)session.getAttribute("BOTTOMBAR_LINKS"); 
%>

<div class="navigation-new clearfix" style="height:25%; margin:0px; padding:5px 0px 0px 36px; background-image:url(/KM/common/images/banner1-nav-bg.gif); background-repeat:no-repeat; background-size:100%; ">
      	<p class="fll" >most viewed Product files ></p>
      	<ul class="navi fll clearfix" >      	
		<% 
				if(links != null)
		for(int i=0; i<links.size();i++){ 
        	KmDocumentMstr km1 = (KmDocumentMstr)links.get(i); 
        	String docDisplayText = km1.getElementName();
        	if (docDisplayText.length()>12) docDisplayText = docDisplayText.substring(0,12)+".."; %>        	
      		<li title="<%=km1.getElementName()+ " (Hits:" + km1.getNumberOfHits() + ")"%>">      		
			  <a href="javascript:clickBottomLinks('<%=Utility.getDocumentViewURL(""+km1.getDocumentId(), km1.getDocType())%>');"> <%=docDisplayText%></a>
      		</li>
        
        <% if((i+1)!= links.size()){ %>
            <li class="divider">|</li>
            <%} %>
  
        <%} %>
      	</ul>
      </div>