<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<html:html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=request.getContextPath()%>/common/css/style.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath()%>/common/font/stylesheet.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/common/css/tipsy.css" rel="stylesheet" type="text/css" />
<link  href="<%=request.getContextPath()%>/common/css/jquery.jscrollpane.css" rel="stylesheet" type="text/css" media="all" />
<script type="text/javascript" src="<%=request.getContextPath()%>/common/js/jquery-1.8.2.min.js"></script>
<link rel="stylesheet" href="common/css/tipsy.css" type="text/css" />
<script type="text/javascript" src="common/js/jquery.tipsy.js"></script>
<script type="text/javascript" src="common/js/stayConnect.js"></script>   
<script type="text/javascript" src="<%=request.getContextPath()%>/common/js/jquery.tools.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/common/js/jquery.mousewheel.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/common/js/jquery.jscrollpane.min.js"></script>

<link rel="stylesheet" type="text/css" href="cal/tcal.css" />
<script type="text/javascript" src="cal/tcal.js"></script> 



<title><tiles:getAsString name="title" /></title>
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<div class="wrapper">
		<tiles:insert attribute="header" />
		 <div class="navigation">
		   		<tiles:insert attribute="topMenu" />
		 </div>
		 <div class="starting-line">
		 	<marquee>		
				<tiles:insert attribute="tickler1" />
			</marquee>
		</div>
		<div class="content clearfix">    	
    		<div class="left-side clearfix">      	
      			<div class="content-upload clearfix">		
					<div id="banner1">		              
						<tiles:insert attribute="csrBanner" />
						<tiles:insert attribute="topSopViewed" />						
					</div>
						<div id="mainContent">  
							<tiles:insert attribute="body" />
						</div> 
		 		</div>		 	
		 	</div>			 	
		 	
		 	<div class="right-side clearfix pos-rel common-widget" style="padding-top:75px;">		 
		 		<div id="select-widget" class="box5 form1">		 		
		 			<tiles:insert attribute="csrRightFrame1" /> <!-- LOB and Circle Selection -->		 		
		 		</div>		 		
		 		<div class="box2">
		 		<div class="search-box-outer">		 			
		 				<tiles:insert attribute="csrRightFrame3" /> <!-- CSR Content Search -->		 			
		 			</div>		 		
		 			<tiles:insert attribute="csrRightFrame2" /> <!-- CSR Menus -->		 			
		 					 		
		 		</div>		 		
		 		<div id="latest-updates" class="box4">		 		
		 			<tiles:insert attribute="csrRightFrame4" /> <!-- CSR Latest Updates -->		 		
		 		</div> 
		 	</div>			 	
	 </div>	
	 
	 <div id="banner1">		              
			<tiles:insert attribute="topProductViewed" />						
	 </div>
	 
	 <div class="starting-line"> 
	    <marquee>		
			<tiles:insert attribute="tickler2" />		
		</marquee>
	 </div>
	 <div class="footer">	   
	  	 <tiles:insert attribute="footer" />
     </div>
</div>		 
	   <tiles:insert attribute="quicklinks" />
</body>
</html:html>

