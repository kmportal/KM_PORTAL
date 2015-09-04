	

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<script language="javascript">


function myPopup(url,windowname,w,h,x,y){
		var req = '<%= request.getAttribute("warn")%>';
		if(req!='' && req !='null')
		alert(req);
	<% request.setAttribute("warn",(String)request.getAttribute("warn"));%>
	
	if (window.showModalDialog) {
		window.open(url,'dummydate',"resizable=yes,toolbar=no,scrollbars=yes,menubar=no,status=no,directories=no,modal=yes,location=no,width="+w+",height="+h+",left=5,top=5");
		//window.showModalDialog(url,'dummydate',"dialogWidth:1000px;dialogHeight:1000px");
	} else {
		window.showModalDialog(url,'dummydate',"dialogWidth:1000px;dialogHeight:1000px");
		//window.open(url,'dummydate',"resizable=yes,toolbar=no,scrollbars=yes,menubar=no,status=no,directories=no,modal=yes,location=no,width="+w+",height="+h+",left=5,top=5");
	}

}





</script>

