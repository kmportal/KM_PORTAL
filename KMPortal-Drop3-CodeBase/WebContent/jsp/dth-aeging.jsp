<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<script type="text/javascript" src="common/js/jquery.mousewheel.js"></script>
<!-- the jScrollPane script -->
<script type="text/javascript" src="common/js/jquery.jscrollpane.min.js"></script>
<script type="text/javascript">
var A_TCALCONF = {
	'cssprefix'  : 'tcal',
	'months'     : ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],
	'weekdays'   : ['Su', 'Mo', 'Tu', 'We', 'Th', 'Fr', 'Sa'],
	'longwdays'  : ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'],
	'yearscroll' : true, // show year scroller
	'weekstart'  : 0, // first day of week: 0-Su or 1-Mo
	'prevyear'   : 'Previous Year',
	'nextyear'   : 'Next Year',
	'prevmonth'  : 'Previous Month',
	'nextmonth'  : 'Next Month',
	'format'     : 'm-d-Y' // 'd-m-Y', Y-m-d', 'l, F jS Y'
};


			$(function()
			{
				$('.scroll-pane').jScrollPane(
				);
			});
		</script>
<script type="text/javascript">
            $(document).ready(function(){
                $(".content-table tr:odd").addClass("odd");
                $(".content-table tr:not(.odd)").addClass("even");  
            });
			
			$(document).ready(function(){
                $(".content-table-type2 tr:odd").addClass("odd");
                $(".content-table-type2 tr:not(.odd)").addClass("even");  
            });
</script>
<script type="text/javascript">
function getSubmit(){
	document.kmViewDthAegingFormBean.methodName.value="getBucketDetails";
	document.kmViewDthAegingFormBean.submit();

}
</script> 
<html:form action="/viewDthAeging">
	<html:hidden property="methodName"/>
	<div class="boxt2">
    	<div class="content-upload clearfix" style="padding: 10px;"> 
      		<div class="fll clearfix">
      			<p class="fll" style="padding:2px 5px 0px 0px; margin-left: 10px;">Bucket freezing date</p>
      			<p class="freez-date" style="margin-top: -5px;"><bean:write name="kmViewDthAegingFormBean" property="bucketFreezingDate"/></p>
			</div>
			<div class="fll clearfix">
  				<p class="fll" style="padding:2px 5px 0px 0px; margin-left: 40px;">Suspension date</p>
  				<p class="fll" style="padding:2px 5px 0px 0px; margin-top: -5px;"><html:text property="aegingFromSuspDate"  readonly="true" styleClass="tcal calender2 fll" /></p>
			</div>
			<p class="fll" style="padding:02px 5px 0px 0px; margin-top: -5px;"> <a onclick="getSubmit();" class="go-button3" style="background:url(/KM/common/images/go-button3.gif) top left no-repeat; width:32px; height:24px; display:block;">&nbsp;</a> </p>
    		
  		</div>
  	</div>
   
     <div class="content-upload clearfix"> 
      <div class="clearfix aeging-custom">
	      <div class="fll">      
	       <fieldset style="width: 300px;  margin-left: 18px; margin-top: 2px;">
		    <legend><p>Aging of the customer</p></legend>
	           <div class="fll" style="margin-right:40px;">
	            <div class="">
	                <ul class="clearfix">
	                  <li class="clearfix">
	                    <label class="label-new"  style="margin:  5px 10px ;">From Bucket Freezing Date  : &nbsp; 
	                       	<bean:write name="kmViewDthAegingFormBean" property="fromDate"/> </label>
	                
	                    <label class="label-new"  style="margin:  5px 10px ;">From Current Date  : &nbsp; 
	                    	<bean:write name="kmViewDthAegingFormBean" property="toDate"/> </label>
	                  </li>
	                </ul>
	            </div> 
	          </div>
	        </fieldset>
	       </div> 
	       <div class="flr" style="margin: 30px 120px 0px 0px;">
	           <label class="label-new"  style="margin:  5px 5px ;"><b>Bucket in which customer is lying  : &nbsp; 
	          		<bean:write name="kmViewDthAegingFormBean" property="bucketForCustomer"/>          </b>	
	       </div>
       </div>  
      </div>
      <div class="dth-aeging">
            <h1 style="margin-bottom:0px;"><span class="text">DTH Aging</span></h1>
            <div class="content-table-type2 clearfix">
              <table width="100%" border="0" cellspacing="0" cellpadding="0" style="table-layout: fixed;" align="left">
                <tr class="first" style="word-wrap: break-word" >
                  <td width="10%" align="left" valign="top">SN</td>
                  <td width="35%" align="left" valign="top">Offers</td>
                  <td width="55%" align="left" valign="top">Description</td>
                </tr>
                <logic:notEmpty name="kmViewDthAegingFormBean" property="offerList">
                <logic:iterate id="offerDetailsDTO" name="kmViewDthAegingFormBean" property="offerList" scope="session" indexId="ctr">
                <tr style="word-wrap: break-word" >
                  <td width="10%" align="left" valign="top"><%=ctr+1 %></td>
                  <td width="35%" align="left" valign="top"><bean:write name="offerDetailsDTO" property="offerTitle"/></td>
                  <td width="55%" align="left" valign="top"><bean:write name="offerDetailsDTO" property="offerDesc"/></td>
                </tr>
                </logic:iterate>
                </logic:notEmpty>
                
              </table>
            </div>
      </div>

    
</html:form>
  



