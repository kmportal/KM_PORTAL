<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<script type="text/javascript" src="common/js/jquery-1.8.2.min.js"></script>
<script language="JavaScript" type="text/javascript">
	function validate()
	{
		var fileName = document.forms[0].newFile.value;
		var extn = fileName.substring(fileName.lastIndexOf(".")+1);
		
		if(document.KmBannerUploadFormBean.initialSelectBox.value == ""){
			alert("Please select Banner Page !");
			return false;
		}
		if(document.forms[0].newFile.value == ""){
			alert("Please select an image");
			return false;
		}
		if(extn.toUpperCase() == "JPG" || extn.toUpperCase() == "GIF")
		{
			document.KmBannerUploadFormBean.methodName.value = "uploadBanner";
			document.KmBannerUploadFormBean.submit();
			return true;
		}
		else
		// Change Made Against DefectId MASDB00105316
		{  
			alert("Only jpg or gif images can be uploaded.");
			return false;
		}

}
function clearFields(){
	document.KmBannerUploadFormBean.initialSelectBox.value = "";
	document.KmBannerUploadFormBean.reset();
		return false;
	}
</script>
      <div class="breadcrump"></div>
      <div class="box2">
        <div class="content-upload">
<html:form action="/bannerUpload" enctype="multipart/form-data" >
<html:hidden property="methodName"/>       
          <h1>Banner Upload</h1>
          <html:messages id="msg" message="true">
                 				<bean:write name="msg"/>  
                          
             				</html:messages>
           <font color=red> <html:errors /></font>  				
          <ul class="list2 form1 banner-upload-p">
       	  <li class="clearfix padd10-0">
          	<span class="text2 fll">Select a Page</span>
            <html:select property="initialSelectBox" styleId="initialSelectBox" styleClass="select1">
                 <html:option value="" >Select Banner Page</html:option>
                  <html:option value="loginPage">Login Page</html:option>
                   <html:option value="csrHomePage">CSR Home Page</html:option>
                        </html:select>
          </li>
          <li class="clearfix padd10-0 alt">
          	<span class="text2 fll">Banner Image</span>
            <html:file style="border:solid 1px #000;width:246px;height:20px;background-color:#ffffff;" property="newFile"/><p><p>
              <div id="imageDisclaimer" class="flr" style="margin-top: 10px;"> 
              <h6>Supported Image: jpg or gif; Size maximum: 900 KB &nbsp;&nbsp;&nbsp;&nbsp; <br>Dimention: Login Page: height 328px, width 665px <br>&nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;CSR Home Page: height 160px, width 758px</h6></div>
              
          </li>
                   
           <li class="clearfix padd10-0">
          	<span class="text2 fll">&nbsp;</span>
            <input class="red-btn fll" src="images/submit.jpg" style="margin-right:10px;" name="" type="image" value="submit" alt="submit" onclick="javascript:return validate();"/>
            <input class="red-btn fll" src="images/clear.jpg" style="margin-right:10px;" name="" type="image" value="clear" alt="clear" onclick="javascript:return clearFields();"/>
            <!--<a href="javascript:return clear();" class="red-btn fll">clear</a>
          --></li>
            
          </ul>
 </html:form>         
        </div>
      </div>

