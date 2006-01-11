

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="com.ibm.km.forms.KmDocumentMstrFormBean" %>

<bean:define id="kmUserBean" name="USER_INFO"  type="com.ibm.km.dto.KmUserMstr" scope="session" />

<LINK href="./jsp/theme/css.css" rel="stylesheet" type="text/css">
<script language="javascript">
var path = '<%=request.getContextPath()%>';
</script>
<SCRIPT language="JavaScript">
// dispDoc.js

var req=null; 

function newXMLHttpRequest() {

    var xmlreq = false;

    if (window.XMLHttpRequest) {
        // Create XMLHttpRequest object in non-Microsoft browsers
        xmlreq = new XMLHttpRequest();

    } else if (window.ActiveXObject) {

        // Create XMLHttpRequest via MS ActiveX
        try {
            // Try to create XMLHttpRequest in later versions
            // of Internet Explorer

            xmlreq = new ActiveXObject("Msxml2.XMLHTTP");

        } catch (e1) {

            // Failed to create required ActiveXObject

            try {
                // Try version supported by older versions
                // of Internet Explorer

                xmlreq = new ActiveXObject("Microsoft.XMLHTTP");

            } catch (e2) {

                // Unable to create an XMLHttpRequest with ActiveX
            }
        }
    }
    return xmlreq;
}

var errors=false;

function addToFavorites(checkbox,docId) {
	//alert(docId);
	req = newXMLHttpRequest();
    //alert (req);
    if (!req) {
        alert("Your browser does not support AJAX! Favorites Module is accessible only by browsers that support AJAX. " +
              "Please check the KM requirements and contact your System Administrator");
        return;
    }
	
	var url="";
	
	//alert (path);
	
	if (checkbox.checked) {
		url = path+"/documentAction.do?methodName=addToFavorites&docId="+docId;
	}
	else {
		url = path+"/documentAction.do?methodName=deleteFavorites&docId="+docId;
	}
	//alert (url);
	req.onreadystatechange = favoritesDone;
	req.open("GET", url, false);
	req.send(null);
	return !errors;
}

function favoritesDone() {
	if (req.readyState == 4) {
        if (req.status == 200) {
	      var status = eval(req.responseText);
        	if (status=='1') {
        		if ($id("favorite").checked) {
        			$id("fav").style.fontWeight="bold";
        		}
        		else {
        			$id("fav").style.fontWeight="normal";
        		}
        	}
        	else {
        		if (!$id("favorite").checked) {
        			$id("fav").style.fontWeight="bold";
        		}
        		else {
        			$id("fav").style.fontWeight="normal";
        		}
        	}
        }
	}
}

function $id(id) {
	
	return document.getElementById(id);
}

function disableDropDowns(){
var restricted='<%= kmUserBean.isRestricted()  %>'
var actorId='<%= kmUserBean.getKmActorId()  %>' 
		if(restricted=='true'&& (actorId == '4' || actorId== '6')){
			
			document.getElementById("currentCircleId").disabled='true';	
			document.getElementById("currentLobId").disabled='true';	
		
		}

}

/* disabling printing options */
function do_err()
    {
        return true
    }
onerror=do_err;

function no_cp()
    {
     // clipboardData.clearData();setTimeout("no_cp()",100)
    }
no_cp();



</SCRIPT>

  <table width="100%" border="0" cellspacing="0" cellpadding="0">
		    	<tr>
		    		<td >
					</td>
			    </tr>
			  </table>
  <table width="100%" class="mLeft5" align="left" cellspacing="0" cellpadding="0">
    <tr >
      <td width="100%" height="378" align="left" valign="top" class="pRight10 pTop5">
<table width="98%" align="center" border="0" cellspacing="0" cellpadding="0" class="mTop5">
					
					<tr>
						 <TD colspan="4" align="left" height="20" class="border">
							 <logic:present name="kmDocumentMstrForm">
								 <logic:notEmpty name="CURRENT_PAGE">
								<logic:notEqual name="CURRENT_PAGE" value="DOCUMENT_ERROR"> 
    			<logic:equal name="LOGIN_ACTOR_ID" value="4" scope="session">
    			  <span id="fav">
					<%
  					String docId = (String)request.getAttribute("docID");
  					session.setAttribute("docId", docId);
  					%>
  					 <jsp:include page="AddToFavourite.jsp"></jsp:include>
  				  </span>
				</logic:equal>
    			<logic:equal name="LOGIN_ACTOR_ID" value="6" scope="session">
	    			<span id="fav">
					<%
  					String docId = (String)request.getAttribute("docID");
  					session.setAttribute("docId", docId);
  					%>
  					 <jsp:include page="AddToFavourite.jsp"></jsp:include>
  				    </span>
				</logic:equal>
				
<script type="text/javascript" src="jScripts/favorites.js"></script>
				
					    							    		
								 <%-- <iframe src='.<bean:write name="kmDocumentMstrForm" property="filePath"/>' width="100%" height="640" align="right" frameborder='0'></iframe> 
							 	  --%><!--<IFRAME src="docHome.jsp" width="100%" height="500" align="right" ></IFRAME>-->
							  <iframe src="viewDocAttachment.do?filepath=<bean:write name="kmDocumentMstrForm" property="filePath"/>" width="100%" height="640" align="right" frameborder='0'></iframe> 
							 
							  </logic:notEqual>
							  </logic:notEmpty>
							  <logic:notEmpty name="CURRENT_PAGE">
							  <logic:equal name="CURRENT_PAGE" value="DOCUMENT_ERROR">
								<FONT color="red">&nbsp;FILE NOT FOUND.</FONT>
							  </logic:equal>
							  </logic:notEmpty>
							  <script language="javascript">
							  	  if ('<bean:write name="kmDocumentMstrForm" property="favorite"/>'=='true') {
							  	  	$id("favorite").checked="true";
							  	  	$id("fav").style.fontWeight="bold";
							  	  }
							  </script>
							  
							  
							  </logic:present>
						 </TD>
                    </tr>
				  

		    </table>
		    
			</td>
    </tr>
  </table>
