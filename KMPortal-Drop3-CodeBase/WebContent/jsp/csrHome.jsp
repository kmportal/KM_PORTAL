<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@page import="com.ibm.km.dto.KmUserMstr"%>
<%@page import="com.ibm.km.forms.KmCSRHomeBean"%>
<%@page import="java.util.*"%>

<script type="text/javascript"> 
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


<logic:notEmpty name="CSR_HOME_BEAN" property="circleCategoryMap">
<bean:define id="circleCategoryMap" name="CSR_HOME_BEAN" property="circleCategoryMap"  type="java.util.HashMap" scope="session" />
</logic:notEmpty>
<bean:define id="csrHomeBean" name="CSR_HOME_BEAN"  type="com.ibm.km.forms.KmCSRHomeBean" scope="session" /> 
<bean:define id="kmUserBean" name="USER_INFO"  type="com.ibm.km.dto.KmUserMstr" scope="session" />
<bean:define id="alertMessage" name="csrHomeBean" property="alertMessage" type="java.lang.String"/>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 

<% int a=0;%>
<div id="sop-bd-tabs">
		    		<logic:notEmpty name="circleCategoryMap">
		    		<ul class="list3 clearfix">
		    		
					<logic:iterate id="element" name="circleCategoryMap" indexId="ctr">
						<bean:define name="element" property="key" id="categoryElement" />
						<bean:define name="categoryElement" property="value" id="categoryElementValue" />
						<logic:notEqual name="categoryElementValue" value="<%= csrHomeBean.getCircleId() %>">
						 <li><a id='#sop-tab<%=ctr+1 %>' href='#sop-tab<%=ctr+1 %>' class='sop-tab<%=ctr+1 %>' onclick="location.href = $(this).attr('id');location.href.split('#')[1];return false"> <bean:write name="categoryElement" property="label" /></a></li>
                          <html:hidden property="tabName" styleId="tabId" value="" />
                    		     
                          </logic:notEqual>
					</logic:iterate>	
					</ul>
					 
					<div class="box4">
					<logic:iterate id="element" name="circleCategoryMap" indexId="ctr">
           		 		<div class="content-upload sop-bd-tabs four-column1 clearfix" id="sop-tab<%=ctr+1%>">
           		 		
           		 		<% int subCats=0;  %>
                        <logic:iterate id="listElement1" name="element" property="value" indexId="listCtr1">
                         <% 
                         	subCats++; 
                         %>
                        </logic:iterate>
           		 		<%

           		 		int columnIndex = 0;
           		 		if (subCats < 1000){ 
           		 		        
                				 double total=subCats/8.0;
                				 int count=(int)Math.ceil(total); 
                				 for(int b=1;b<=count;b++) {
                				  int subset = 3%b; 
                				  subset++;
                			 %>
           		 		
            	             <div class="column<%=subset%> column">
                						<ul class="list11 box8">
                						<% 
                						int flag = 0;
                						%>
                							<logic:iterate id="listElement" name="element" property="value" type="org.apache.struts.util.LabelValueBean" offset='<%=(columnIndex+"")%>' >
                							<% 
                							if( flag++ == 8)
                							{
                							flag = 0;
                							break;
                							}
                							columnIndex++;
//System.out.println("element name 111111:"+listElement.getLabel());	
//                							if(listCtr>8*flag && listCtr<=8*(flag+1)){
                							{
                							%>
				                  				<logic:iterate id="document" name="CSR_HOME_BEAN" property="documentList" indexId="docCtr">
				                  					<logic:equal name="document" property="elementId" value='<%=listElement.getValue() %>'>
				                  						<%
				                  						String temp = listElement.getLabel();
				                  						if(temp != null && temp.length() > 30)
				                  						temp = temp.substring(0,29)+"...";
				                  						//System.out.println("element name :"+temp);			                  						 %>
				                  						<logic:equal name="document" property="elementLevelId" value="0"> 
				                  							<li><a href='<bean:write name="document" property="documentViewUrl"/>' ><span class="inner" title="<bean:write name="listElement" property="label" />"><%=temp %></span></a></li>
				                  						</logic:equal>
				                  						<logic:notEqual name="document" property="elementLevelId" value="0">
				                  							<li><a href='kmDynamicIndexPage.do?methodName=loadSubCategories&firstIteration=false&categoryId=<bean:write name="listElement" property="value" />'><span class="inner" title="<bean:write name="listElement" property="label" />"><%=temp %></span></a></li>
				                  						</logic:notEqual>
				                  					</logic:equal>
				                  				</logic:iterate>
											<%
											} 
											%>
											
											</logic:iterate>
										</ul>
								   </div>
							<%
							
							// flag++;
							
							}  } 
							
							%>					                
				                
				                
				               
              		</div>
              		</logic:iterate>
              	</div>
					</logic:notEmpty>
					
				</div>
	      


  <script type="text/javascript"> 
$(document).ready(function() {
var flagtab =false;
 //alert(window.location.href);
 
$("li a").click(function(event){
 var tem = window.location.href.split('#');
	   if(tem !=null && tem !="" && tem.length>1) {
	   //alert(tem[1]);
	   flagtab =true;
               $("ul.list3 li a").removeClass("active"); //Remove any "active" class
               
               $('.'+tem[1]).addClass("active").show(); //Add "active" class to selected tab
               $(".sop-bd-tabs").hide(); //Hide all tab content
               var activeTab = $('.'+tem[1]).attr("href"); //Find the href attribute value to identify the active tab + content
               $(activeTab).fadeIn("fast"); //Fade in the active ID content
                //alert(tem[1]);
               //return false;
              

	   }else {
	    
	   }
});
	
	   if(flagtab) {
	   //When page loads...
       $(".sop-bd-tabs").hide(); //Hide all content
       $("ul.list3 li a:first").addClass("active").show(); //Activate first tab
       $(".sop-bd-tabs:first").show(); //Show first tab content
 }
 var temp = window.location.href.split('#'); 
	//alert(temp.length) 
	if(temp !=null && temp !="" && temp.length==1){
	$('.sop-tab1').addClass("active").show(); //Add "active" class to selected tab
               $(".sop-bd-tabs").hide(); //Hide all tab content
               var activeTab = $('.sop-tab1').attr("href"); //Find the href attribute value to identify the active tab + content
               $(activeTab).fadeIn("fast");
              // return false;
	}else if(!flagtab) {
	//alert('jhh');
	 $("ul.list3 li a").removeClass("active"); //Remove any "active" class
               //alert(tem[1]);
               $('.'+temp[1]).addClass("active").show(); //Add "active" class to selected tab
               $(".sop-bd-tabs").hide(); //Hide all tab content
               var activeTab = $('.'+temp[1]).attr("href"); //Find the href attribute value to identify the active tab + content
               $(activeTab).fadeIn("fast");
               //return false;
	}
      
 });
</script> 
 

