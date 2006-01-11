
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<LINK href=./jsp/theme/css.css rel="stylesheet" type="text/css">
<LINK href="theme/text.css" rel="stylesheet" type="text/css">
<script language="JavaScript">




 function validateAdd(){
 	document.getElementById("myDiv").innerHTML="";
 	var result= true;
 	var msg=null; 
 	var mob=document.forms[0].mobileNumber.value;
	var len=mob.length;
 	var regmob=/^[0-9]*$/;
 	
 	for(var i = 0; i < document.all.mySelect.options.length; i++) {
	if(document.all.mySelect.options[i].value == mob) {
			//
		//	sdValues.push(oList.options[i].value);
			msg="Number Already Exists";
			result= false;
			}
	}
 	
	if(!regmob.test(document.forms[0].mobileNumber.value)){
		msg="Please Enter Valid Mobile Number";
		result= false;	
	}
	//Bug resolved MASDB00064245
	if(mob=='00000000000' || mob=='0000000000'){
		msg= "Please Enter Valid Mobile Number";
		result= false;	
	}
	if(len<10)
	{					
				msg="Please Enter 10 Digit Mobile Number";
				result= false;
			
	
	} 
		
		
		
		
	if (result== false){
	document.forms[0].mobileNumber.focus();
	document.forms[0].mobileNumber.value="";
	alert(msg);
	}	
		
	if(result== true){

	addUsingAjax();
	//	addCsd();

		}	
 }   
    
 
   
    function addCsd() {
    mobile= document.getElementById("mobileNumber").value
        var newOption = document.createElement('<option value='+mobile+'>');
        document.all.mySelect.options.add(newOption);
        newOption.innerText = mobile;
        document.getElementById("mobileNumber").value="";

    }
    
   function removeItem(selectbox)
{ c=0;
var i;


//var url = "initCsd.do?methodName=removeCsdUser&mobileNumber="+mobileNumber;
var url = "initCsd.do?methodName=removeCsdUser";
for(i=selectbox.options.length-1;i>=0;i--)
{ 
if(selectbox.options[i].selected){
var mobileNumber = selectbox.options[i].value;
 url=url+"&mobileNumbers="+mobileNumber;
// selectbox.remove(i);

 c++;
}

}


 if(c==0) url=null;

 return(url);
} 
    
  function removeCsd() {
 //       document.all.mySelect.options.remove(0);
  
 
 url=removeItem(document.all.mySelect);
 if(url==null)
 {
  alert("Select a Number from CSD User List");
 }
 else{
  	if(confirm("Do you want to remove CSD users."))
   	{
	
 	if (window.XMLHttpRequest)
  		{ // code for IE7+, Firefox, Chrome, Opera, Safari
  		xmlhttp=new XMLHttpRequest();
  		}
	else
  		{ // code for IE6, IE5
 		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
 		 }
	 xmlhttp.onreadystatechange=function()
  		{
  			if (xmlhttp.readyState==4 && xmlhttp.status==200)
   				 { 
   					
  					  document.getElementById("myDiv").innerHTML=xmlhttp.responseText;
  					  msg="CSD User(s) Removed";
   					result=xmlhttp.responseText;
   					 if(msg==result){
  					    removefromList(document.all.mySelect);
  					  }
   				
   				 }
 		 } 
	xmlhttp.open("POST",url,true);
	xmlhttp.send(null);
  }
  }
  
 }
   
 function removefromList(selectbox){
 	for(i=selectbox.options.length-1;i>=0;i--)
		{ 
			if(selectbox.options[i].selected){
 			selectbox.remove(i);

			}

		}
 }  
   
   function submitForm(){
   
   var selectbox = document.getElementById('mySelect');
   alert('Hi')
   var urlString = '<%=request.getContextPath() %>'+'/updateCsd.do?';
   
   
  
   for(i=selectbox.options.length-1;i>=0;i--)
	{
		alert(selectbox.options[i].value)
		urlString = urlString+'mobileNumbers='+selectbox.options[i].value+'&';
	} 
   	alert(urlString);
   	document.configCSDForm.action = urlString;
   }
   
 // add csd using ajax
 
function addUsingAjax(){
	
	if (window.XMLHttpRequest)
  		{ // code for IE7+, Firefox, Chrome, Opera, Safari
  		xmlhttp=new XMLHttpRequest();
  		}
	else
  		{ // code for IE6, IE5
 		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
 		 }
  
 	var mobileNumber = document.forms[0].mobileNumber.value;
	var url = "initCsd.do?methodName=addCsdUser&mobileNumber="+mobileNumber;
	xmlhttp.onreadystatechange=function()
  		{
  			if (xmlhttp.readyState==4 && xmlhttp.status==200)
   				 { 
   					msg="User added successfully";
   					result=xmlhttp.responseText;
  					  document.getElementById("myDiv").innerHTML=xmlhttp.responseText;
  					  if(msg==result){
  					  addCsd();
  					  }
   				 }
 		 } 
	xmlhttp.open("POST",url,true);
	xmlhttp.send(null);

}   

</script>
<script type="text/javascript">

function stopRKey(evt) {
  var evt = (evt) ? evt : ((event) ? event : null);
  var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null);
  if ((evt.keyCode == 13) && (node.type=="text"))  {return false;}
}

document.onkeypress = stopRKey;

</script> 
    


	<html:form action="/initCsd"  >
	
	<div class="box2">
     <div class="content-upload">
	<h1><bean:message key="CSD.Configuration.title" /></h1>
	
	<table align="center" >
	
		<tbody>
		
		<tr>
				<td colspan="3" align="center" class="error">
					<strong> 
          	<!-- 		<html:messages id="msg" message="true">
                 		<bean:write name="msg"/>  
             		</html:messages>  -->
             		
			<div id="myDiv"></div>
		
             		
            		</strong>
            	</td>
			</tr>

			<tr align="center">
				<td colspan="3" align="center" class="error" >
					<strong> 
          			<html:errors/><br>
				<br>
				</strong>
            	</td>
			</tr>

				
				</tbody>
		</table>
<ul class="list2 form1">
			<li class="clearfix alt">
			<span class="text2 fll width160"><strong><bean:message key="CSD.Configuration.mobileNumber" /></strong></span>
				  <p class="clearfix fll margin-r20"> <span class="textbox6"> <span class="textbox6-inner">
				 <html:text property="mobileNumber" styleId="mobileNumber" style="width: 150px" maxlength="10" styleClass="textbox7" ></html:text>
				</span></span></p>
				 <p class="clearfix fll margin-r20">
				  <html:button property="" value="Add" styleClass="red-btn" onclick="validateAdd();">  </html:button> 
				 </p>
				</li>
				
			<li class="clearfix">
			<span class="text2 fll width160"><strong><bean:message
					key="CSD.Configuration.userList" /></strong></span>
		
				<html:select styleId="mySelect" name="configCSDForm"
					property="csdUserList" multiple="multiple" size="15"
					style="width: 150px" >
					<html:optionsCollection name="CSD_USER_LIST" label="csdUserNumber" value="csdUserNumber"/>
				</html:select>	
				 <p class="clearfix fll margin-r20">
				  <html:button property="" value="Remove" styleClass="red-btn" onclick="removeCsd();">  </html:button> 
				 </p>
				</li>
			</ul>
		

		</div>
		</div>
</html:form>
	

