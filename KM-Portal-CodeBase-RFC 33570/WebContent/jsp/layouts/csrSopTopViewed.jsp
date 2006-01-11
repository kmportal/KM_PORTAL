<%@page import="java.util.ArrayList"%>
<%@page import="com.ibm.km.forms.KmDocumentHitsCountFormBean"%>
<%@page import="com.ibm.km.dto.KmDocumentMstr"%>


<%@page import="com.ibm.km.common.Utility"%><script language="javascript">
function clickTopLinks(url){
		window.location.href = url;
}
</script>

<% ArrayList links = (ArrayList)session.getAttribute("TOPBAR_LINKS"); 
%>

<div class="navigation-new clearfix" style="height:25%; margin:0px; padding:5px 0px 0px 36px; background-image:url(/KM/common/images/banner1-nav-bg.gif); background-repeat:no-repeat; background-size:100%; ">
      	<p class="fll" >most viewed SOP files ></p>
      	<ul class="navi fll clearfix" >
		<%
		if(links != null)
		for(int i=0; i<links.size();i++){ 
        	KmDocumentMstr km = (KmDocumentMstr)links.get(i); 
			String docDisplayText = km.getElementName();
        	if (docDisplayText.length()>12) docDisplayText = docDisplayText.substring(0,12)+".."; %>
      		<li title="<%=km.getElementName()+ " (Hits:" + km.getNumberOfHits() + ")"%>"><a href="javascript:clickTopLinks('<%=Utility.getDocumentViewURL(""+km.getDocumentId(), km.getDocType())%>');"><%=docDisplayText%> </a></li>
        <% if((i+1)!= links.size()){ %>
            <li class="divider">|</li>
            <%} %>
  
        <%} %>
      	</ul>
      </div>