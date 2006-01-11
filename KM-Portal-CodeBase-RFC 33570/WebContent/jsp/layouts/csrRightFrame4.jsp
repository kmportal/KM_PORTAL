<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@page import="com.ibm.km.dto.KmCsrLatestUpdatesDto"%>
<%@page import="com.ibm.km.common.Utility"%><link rel="stylesheet" href="common/css/tipsy.css" type="text/css" />
<script type="text/javascript" src="common/js/jquery.tipsy.js"></script>
<script type="text/javascript" src="common/js/jquery.jscrollpane.min.js"></script> 
<link  href="<%=request.getContextPath()%>/common/css/jquery.jscrollpane.css" rel="stylesheet" type="text/css" media="all" />
<script type="text/javascript" src="<%=request.getContextPath()%>/common/js/jquery.mousewheel.js"></script>
<%@page import="org.json.JSONObject"%>
<script language="javascript">
var path = '<%=request.getContextPath()%>';
var elements;
var json;
function loadUpdates(){
	req = newXMLHttpRequest();
	
	var url=path+"/csrLatestUpdates.do?methodName=initGetUpdates";
	req.open("GET", url, true);
	req.send();
	req.onreadystatechange =function(){
	if(req.readyState == 4 && req.status == 200) {
	json = eval('(' + req.responseText + ')');
	elements = json.elements;
	var length = elements.length;
	for (var i = 0; i < length; i++) {

		var documentId = elements[i].documentId;
		var docType = elements[i].docType;
		var strForContent = "<span class='highlight'>"+elements[i].updateTitle+" "+"</span>"+elements[i].updateBrief;
		var li = document.createElement("li");
		if(i%2 == 1){
			li.setAttribute('class', 'alt');
		}

		var a = document.createElement("a");

		li.setAttribute('id', 'li_content'+i);
		a.setAttribute('href',elements[i].documentViewUrl);
		a.setAttribute('class','east');
		a.setAttribute('title', '<h1>'+elements[i].updateTitle+'</h1><p>'+elements[i].updateDesc+'</p>');
		
		document.getElementById("latestUpdatesUL").appendChild(li); 
		document.getElementById("li_content"+i).appendChild(a);
		a.innerHTML = strForContent;
	}
	}
  }
}
function newXMLHttpRequest() {

    var xmlreq = false;

    if (window.XMLHttpRequest) {
        xmlreq = new XMLHttpRequest();

    } else if (window.ActiveXObject) {

        try {
            xmlreq = new ActiveXObject("Msxml2.XMLHTTP");

        } catch (e1) {
            try {
                xmlreq = new ActiveXObject("Microsoft.XMLHTTP");
            } catch (e2) {
            }
        }
    }
    return xmlreq;
}
</script>

<h2>latest updates</h2>
        <div id="main" class="scroll-pane">
	        <ul id="latestUpdatesUL" class="list7"></ul>
        </div>	
<script type='text/javascript'>
       
  $(function() {
    // alert();
    setTimeout("$(function(){$('#example-1').tipsy();$('#north').tipsy({gravity: 'n'});$('.south').tipsy({gravity: 's'});$('.east').tipsy({gravity: 'e'});$('#west').tipsy({gravity: 'w'});$('#auto-gravity').tipsy({gravity: $.fn.tipsy.autoNS});$('#example-fade').tipsy({fade: true});$('#example-custom-attribute').tipsy({span: 'id'});$('#example-callback').tipsy({title: function() { return this.getAttribute('original-title').toUpperCase(); } });$('#example-fallback').tipsy({fallback: 'Wheres my tooltip yo?' });$('#example-html').tipsy({html: true });});", 3000);
    $('#example-1').tipsy();

    $('#north').tipsy({gravity: 'n'});
    $('.south').tipsy({gravity: 's'});
    $('.east').tipsy({gravity: 'e'});
    $('#west').tipsy({gravity: 'w'});
 
    $('#auto-gravity').tipsy({gravity: $.fn.tipsy.autoNS});

    $('#example-fade').tipsy({fade: true});

    $('#example-custom-attribute').tipsy({span: 'id'});
    $('#example-callback').tipsy({title: function() { return this.getAttribute('original-title').toUpperCase(); } });
    $('#example-fallback').tipsy({fallback: "Where's my tooltip yo'?" });

    $('#example-html').tipsy({html: true });

  });
</script>

<script type="text/javascript">
			setTimeout("$(function(){$('.scroll-pane').jScrollPane();});", 3000);
			$(function()
			{
				$('.scroll-pane').jScrollPane();
			});
</script>        

        	<script language="javascript">
  				loadUpdates();
    		</script>