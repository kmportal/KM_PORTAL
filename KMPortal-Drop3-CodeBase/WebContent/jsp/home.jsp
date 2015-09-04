<Script>
$(document).ready(function() {
	$(".overlay-trigger").overlay({
		absolute: true,
		close: 'a.close-button1',
		closeOnClick: true,
		expose: { 
		        color: '#000000', 
		        loadSpeed: 200, 
		        opacity: 0.7 
		    },
		top: 'center',
		
		left: 'center'
	});
	
	
});


if ("" != '<%=request.getAttribute("warn")%>' && 'null' != '<%=request.getAttribute("warn")%>')
   alert('<%=request.getAttribute("warn")%>');
   
if ("" != '<%=request.getAttribute("RES")%>' && 'null' != '<%=request.getAttribute("RES")%>')
   alert('<%=request.getAttribute("RES")%>');
   
	function showExpiredDocs()
	{
		var count = <%=request.getAttribute("docCount") %>
		if(count != null && count!=0){
		$(function(){ // On DOM ready
    	$('#openOnLoad').click();
		});
		}
	} 
showExpiredDocs();
</Script>
<link href="common/css/style.css" type="text/css" rel="stylesheet" />
 <div class="wrapper-body">
   <div class="box2">
      <div class="content-upload">
        <h1>Knowledge Management Portal</h1>          
 
  		<div class="inner-content">  
  		     <br><br>
			<div style = "font-family:Arial, Helvetica, sans-serif;"> 
				<b>Knowledge Management Portal is the central repository for all the documents required by CSR for Customer Help.</b>
			</div>
			<br>
			<div>			
 			   <ul class="list2 form1">		
		 			<li class="clearfix">
		 				<b>There are following types of Users:</b>		 			
 					</li>
 					<li class="clearfix">
		 				<b>1. Super Admin</b>		 			
 					</li>
 					
 					<li class="clearfix">
		 			  	Super admin will be able to create the document hierarchy(Category/Sub-Category) for all the circles. This user will have access to create circle Approver/User. He/she will be able to see Central reports. 		 			
 					</li>
 					<li class="clearfix">
		 				<b>2. Circle Admin/Approval</b>		 			
 					</li>
 					<li class="clearfix">
		 			 	This user will have approval rights for files uploaded by Circle Users. There shall be one approver for each of the circle.	 			
 					</li>
 					<li class="clearfix">
		 				<b>3. Circle User</b>		 			
 					</li>
 					<li class="clearfix">
		 				This user shall have the provision to add and edit content as per the predecided heirarchy/directory structure in the system.	 			
 					</li>
 					<li class="clearfix">
		 				<b>4. CSR </b>		 			
 					</li>
 					<li class="clearfix">
		 				These users have right to access the site content. They can send feedback also. 			
 					</li> 					
 					<li class="clearfix">
		 			<b>5. LOB Admin </b>		 			
 					</li>
 					<li class="clearfix">
		 				LOB admin will be able to create the document hierarchy(Category/Sub-Category) for their own LOB circles. This user will have access to create all type users below LOB admin. He/she will be able to see LOB level reports.	 			
 					</li>
 					<li class="clearfix">
		 			<b>6. Category CSR </b>		 			
 					</li>
 					<li class="clearfix">
		 				These users are CSRs with expertise in specific category. 		 			
 					</li>
 					<li class="clearfix">
		 			<b>7. Report User </b>		 			
 					</li>
 					<li class="clearfix">
		 				These users can view all type reports only at super admin level. 	 			
 					</li>
 					<li class="clearfix">
		 			<b>8. TSG Admin </b>		 			
 					</li>
 					<li class="clearfix">
		 				These users can add/remove CSD users and add network fault. They can also view network fault reports.	 			
 					</li>
 				</ul>
 			</div>
 		</div>	 			                   
     </div>
    </div>
   </div>

<div id="popup1" class="pop-up1" >
	<a href="#" class="close-button1"></a>
	<div class="popup-inner">
	<p>Number of Documents to be expired in next 7 days : <%=request.getAttribute("docCount") %></p>
	</div>
</div>
<a href="#" class="overlay-trigger" rel="#popup1" id="openOnLoad"></a>

