
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@page import="com.ibm.km.dto.KmUserMstr"%>


<LINK href="jsp/theme/css.css" rel="stylesheet" type="text/css">
<LINK href="jsp/theme/SelfcareIIStyleSheet.css" rel="stylesheet" type="text/css">
<LINK href="jsp/theme/airtel.css" rel="stylesheet" type="text/css">
<logic:notEmpty name="CSR_HOME_BEAN" property="circleCategoryMap">
<bean:define id="circleCategoryMap" name="CSR_HOME_BEAN" property="circleCategoryMap"  type="java.util.HashMap" scope="session" />
</logic:notEmpty>
<bean:define id="csrHomeBean" name="CSR_HOME_BEAN"  type="com.ibm.km.forms.KmCSRHomeBean" scope="session" /> 
<bean:define id="kmUserBean" name="USER_INFO"  type="com.ibm.km.dto.KmUserMstr" scope="session" />
<bean:define id="alertMessage" name="csrHomeBean" property="alertMessage" type="java.lang.String"/>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 

<script >
	var oPopup = window.createPopup();
	
	function ReplaceAll(Source,stringToFind,stringToReplace){

  		var temp = Source;

    	var index = temp.indexOf(stringToFind);

       	 	while(index != -1){

            	temp = temp.replace(stringToFind,stringToReplace);

            	index = temp.indexOf(stringToFind);

       	 	}

       return temp;

	}
	function ButtonClick()
	{
		var c="Roaming";
		var d="Recharges";
		var oPopBody = oPopup.document.body;
		oPopBody.style.backgroundColor = "#afdfd8";
		oPopBody.style.border = "solid black 0px";
		oPopBody.innerHTML = '<span style="FONT-SIZE: 11px; COLOR: #666666; LINE-HEIGHT: 15px; PADDING-TOP: 1px; font-family:Arial, Helvetica, sans-serif; padding-left:0px;">'+c+'<br/>'+d+'<br/>'+'</span>';
		oPopup.show(tempX, tempY, 70, 35, document.body);
//		oPopup.show(x, y, w, h, b);
	}
	
	  function hidePopup()
	{
		oPopup.hide();
	}
	function showScrollerMessage(msg)
	{
		
		var message='<div style="margin-top:2px;" ><font face=arial size=2 color="white"><B><bean:write name="csrHomeBean" property="message" /></B></font></font></DIV>';
		document.getElementById('slider').innerHTML=setMessage(message);
	//	timer();
		scroll();
		if(msg!='')
			alert(msg);
	}

	/*function timer(){
	
	window.setTimeout('timer()',1260000);
	}*/


function openPopUp(val){

var url='csrAction.do?methodName=getSubCategories&catId='+val.styleId;
window.open(url,'_blank',"resizable=yes,toolbar=no,scrollbars=yes,menubar=no,status=no,directories=no,width=255,height=125,left=390,top=340");
}
function closeChild()
{

}

</script>




<script language="javascript">
<!--

var state;
var divid;
var start;
var end;
var noOfTags;


function showhide(layer_ref1,subCatCount) {
start=0;
divid=layer_ref1+'_'+start;

end=parseInt(subCatCount)-8;
noOfTags=parseInt(end)/2;
state=document.getElementById(divid).style.display;


if (state == 'block') {
state = 'none';
}
else {
state = 'block';
}
if (document.all) { //IS IE 4 or 5 (or 6 beta)
	
	for(var k=start;k<end;k++){
	
		if(document.getElementById(layer_ref1 +'_'+ k)!='null')
			eval( "document.all." + layer_ref1 +'_'+ k + ".style.display = state");
	}
	for(var k=0;k<end;k++){
		var j=8+k;
		
		document.getElementById('tr_'+layer_ref1+'_'+j).style.display=state;
		k++;
	}
}
if (document.layers) { //IS NETSCAPE 4 or below

}
if (document.getElementById &&!document.all) {
alert("Inside")
hza = document.getElementById(layer_ref);
hza.style.display = state;
}
}
//-->
</script>


<script language="JavaScript">
<!--
function mmLoadMenus() {
  if (window.mm_menu_1203194112_0) return;
        window.mm_menu_1203194112_0 = new Menu("root",80,17,"Arial, Helvetica, sans-serif",11,"#FFFFFF","#FFFFFF","#1B1B1B","#434343","left","middle",3,0,1000,-5,7,true,false,true,0,false,false);
  mm_menu_1203194112_0.addMenuItem("1","location='#'");
  mm_menu_1203194112_0.addMenuItem("2","location='#'");
  mm_menu_1203194112_0.addMenuItem("3","location='#'");
  mm_menu_1203194112_0.addMenuItem("4","location='#'");
   mm_menu_1203194112_0.hideOnMouseOut=true;
   mm_menu_1203194112_0.bgColor='#555555';
   mm_menu_1203194112_0.menuBorder=0;
   mm_menu_1203194112_0.menuLiteBgColor='#FFFFFF';
   mm_menu_1203194112_0.menuBorderBgColor='#777777';
  window.mm_menu_1203194404_0 = new Menu("root",80,17,"Arial, Helvetica, sans-serif",11,"#FFFFFF","#FFFFFF","#1B1B1B","#434343","left","middle",3,0,1000,-5,7,true,false,true,0,false,false);
  mm_menu_1203194404_0.addMenuItem("1","location='#'");
  mm_menu_1203194404_0.addMenuItem("2","location='#'");
  mm_menu_1203194404_0.addMenuItem("3","location='#'");
  mm_menu_1203194404_0.addMenuItem("4","location='#'");
   mm_menu_1203194404_0.hideOnMouseOut=true;
   mm_menu_1203194404_0.bgColor='#555555';
   mm_menu_1203194404_0.menuBorder=0;
   mm_menu_1203194404_0.menuLiteBgColor='#FFFFFF';
   mm_menu_1203194404_0.menuBorderBgColor='#777777';
  window.mm_menu_1203194455_0 = new Menu("root",80,17,"Arial, Helvetica, sans-serif",11,"#FFFFFF","#FFFFFF","#1B1B1B","#434343","left","middle",3,0,1000,-5,7,true,false,true,0,false,false);
  mm_menu_1203194455_0.addMenuItem("1","location='#'");
  mm_menu_1203194455_0.addMenuItem("2","location='#'");
  mm_menu_1203194455_0.addMenuItem("3","location='#'");
  mm_menu_1203194455_0.addMenuItem("4","location='#'");
   mm_menu_1203194455_0.hideOnMouseOut=true;
   mm_menu_1203194455_0.bgColor='#555555';
   mm_menu_1203194455_0.menuBorder=0;
   mm_menu_1203194455_0.menuLiteBgColor='#FFFFFF';
   mm_menu_1203194455_0.menuBorderBgColor='#777777';
  window.mm_menu_1203194540_0 = new Menu("root",80,17,"Arial, Helvetica, sans-serif",11,"#FFFFFF","#FFFFFF","#1B1B1B","#434343","left","middle",3,0,1000,-5,7,true,false,true,0,false,false);
  mm_menu_1203194540_0.addMenuItem("1","location='#'");
  mm_menu_1203194540_0.addMenuItem("2","location='#'");
  mm_menu_1203194540_0.addMenuItem("3","location='#'");
  mm_menu_1203194540_0.addMenuItem("4","location='#'");
   mm_menu_1203194540_0.hideOnMouseOut=true;
   mm_menu_1203194540_0.bgColor='#555555';
   mm_menu_1203194540_0.menuBorder=0;
   mm_menu_1203194540_0.menuLiteBgColor='#FFFFFF';
   mm_menu_1203194540_0.menuBorderBgColor='#777777';
  window.mm_menu_1203194623_0 = new Menu("root",80,17,"Arial, Helvetica, sans-serif",11,"#FFFFFF","#FFFFFF","#1B1B1B","#434343","left","middle",3,0,1000,-5,7,true,false,true,0,false,false);
  mm_menu_1203194623_0.addMenuItem("1","location='#'");
  mm_menu_1203194623_0.addMenuItem("2","location='#'");
  mm_menu_1203194623_0.addMenuItem("3","location='#'");
  mm_menu_1203194623_0.addMenuItem("4","location='#'");
   mm_menu_1203194623_0.hideOnMouseOut=true;
   mm_menu_1203194623_0.bgColor='#555555';
   mm_menu_1203194623_0.menuBorder=0;
   mm_menu_1203194623_0.menuLiteBgColor='#FFFFFF';
   mm_menu_1203194623_0.menuBorderBgColor='#777777';
  window.mm_menu_1203194705_0 = new Menu("root",60,17,"Arial, Helvetica, sans-serif",11,"#FFFFFF","#FFFFFF","#1B1B1B","#434343","left","middle",3,0,1000,-5,7,true,false,true,0,false,false);
  mm_menu_1203194705_0.addMenuItem("1","location='#'");
  mm_menu_1203194705_0.addMenuItem("2","location='#'");
  mm_menu_1203194705_0.addMenuItem("3","location='#'");
  mm_menu_1203194705_0.addMenuItem("4","location='#'");
   mm_menu_1203194705_0.hideOnMouseOut=true;
   mm_menu_1203194705_0.bgColor='#555555';
   mm_menu_1203194705_0.menuBorder=0;
   mm_menu_1203194705_0.menuLiteBgColor='#FFFFFF';
   mm_menu_1203194705_0.menuBorderBgColor='#777777';

mm_menu_1203194705_0.writeMenus();
} // mmLoadMenus()
//-->
</script>
<script language="JavaScript" src="jScripts/mm_menu.js"></script>


<BODY class="mLeft10 mTop2 mRight10"   bgcolor="#f6f6f6">
<!-- Code change after UAT Observation  -->

<% int no=8; %>
  <table width="100%" class="mLeft5" align="center" cellspacing="0" cellpadding="0">
		<!--<tr>
	      <td width="100%" height="40" align="left" valign="top" class="pTop5" id="slider"  ><script src="jScripts/iecodea.js" type="text/javascript"></script>
	      </td>
	    </tr>
		--><tr align="left">
			<td width="831">
		    	<table width="100%"  border="0" cellspacing="0" cellpadding="0">
		    		<tr class="height160" valign="top">
		    		<logic:notEmpty name="circleCategoryMap">
					<logic:iterate id="element" name="circleCategoryMap" indexId="ctr">
					
						<bean:define name="element" property="key" id="categoryElement" />
						<bean:define name="categoryElement" property="value" id="categoryElementValue" />
						<logic:notEqual name="categoryElementValue" value="<%= csrHomeBean.getCategoryId() %>">
						<%if(ctr.intValue()%3==0&&ctr.intValue()!=0){%>
							</tr>
							<tr class="height160" valign="top">
						<%	}	%>
						
                        <td width="277">
	                        <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                            	<tr>
                                	<td colspan="2" class="heading_tex"><img src="images/red_arrow.jpg" alt="" width="4" height="4" /> <a  href='documentAction.do?methodName=openSubCategoryTree&isCat=Y&subCatId=<bean:write name="categoryElement" property="value" />'><FONT COLOR="#008FC0"><bean:write name="categoryElement" property="label" /></FONT></a></td>
                                </tr>
                             <!--  Delete Image display row from here -->
                                <% int subCats=0; String listCount="";%>
                                <logic:iterate id="listElement1" name="element" property="value" indexId="listCtr1">
                                <% subCats++; %>
                                </logic:iterate>
								<logic:iterate id="listElement" name="element" property="value" indexId="listCtr">
									<%if(listCtr.intValue()%2==0 && listCtr.intValue()<no){%>
										<tr class="height19">
											<td width="50%" class="arial11grey"><img src="./images/blue-bulet.jpg" width="7" height="7" /> 
											<a class="arial2black" href='documentAction.do?methodName=openSubCategoryTree&subCatId=<bean:write name="listElement" property="value" />'><bean:write name="listElement" property="label" /></a></td>
									<%	}	%>
									<%if(listCtr.intValue()%2==1  && listCtr.intValue()<no ){%>
											<td width="50%" class="arial11grey"><img src="./images/blue-bulet.jpg" width="7" height="7" /> <a class="arial2black" href='documentAction.do?methodName=openSubCategoryTree&subCatId=<bean:write name="listElement" property="value"/>'><bean:write name="listElement" property="label" /></a></td>
										</tr>
									<%	}	    %>
									<%  if(listCtr.intValue()==no ){        %>
									<tr >
								<td></td>
								<td class="red_more"  ><br><a href="#name" onmouseover="showhide('more_div<%=ctr %>','<%= subCats %>');">More...... </a> </td>
								
								</tr>
								<% }  %>
								<% listCount=(listCtr.intValue()-8)+""; %>
								
								<%if(listCtr.intValue()%2==0 && listCtr.intValue()>=no){%>
								    
										<tr class="height19"  style="display :none;"   id='tr_more_div<%=ctr %>_<%= listCtr %>' >
										
											<td width="50%" class="arial11grey"><div id='more_div<%=ctr %>_<%=listCount %>' style="display: none;" ><img src="./images/blue-bulet.jpg" width="7" height="7" /> <a class="arial2black" href='documentAction.do?methodName=openSubCategoryTree&subCatId=<bean:write name="listElement" property="value" />'><bean:write name="listElement" property="label" /></a></div></td>
									<%	}	%>
									<%if(listCtr.intValue()%2==1  && listCtr.intValue()>=no ){%>
											<td width="50%" class="arial11grey"><div id="more_div<%=ctr %>_<%=listCount %>" style="display: none;" ><img src="./images/blue-bulet.jpg" width="7" height="7" /> <a class="arial2black" href='documentAction.do?methodName=openSubCategoryTree&subCatId=<bean:write name="listElement" property="value"/>'><bean:write name="listElement" property="label" /></a></div></td>
										
										</tr>
										
								
									<%	}	    %>
									
								</logic:iterate>
								
                            </table>
                        </td>
                        </logic:notEqual>
					</logic:iterate>	
					</logic:notEmpty>
					</tr>				
				</table>
			</td>
		</tr>
	</table>
</BODY>

