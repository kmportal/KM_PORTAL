<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<bean:define id="kmUserBean1" name="USER_INFO"  type="com.ibm.km.dto.KmUserMstr" scope="session" />

<script language="javascript">
var selectedItem='';
var path = '<%=request.getContextPath()%>';
var port = '<%= request.getServerPort()%>';
var serverName = '<%=request.getServerName() %>';

var initialElementPath=<%=kmUserBean1.getInitialElementPath()%>;
var initialParentId=<%=kmUserBean1.getElementId()%>;
var parentId=initialParentId;
var initialLevel=<%=kmUserBean1.getInitialParentId()%>;

var elementLevelNames=new Array();

elementLevelNames[0] = ' Document';
elementLevelNames[1] = ' Country';
elementLevelNames[2] = ' LOB';
elementLevelNames[3] = ' Circle';
elementLevelNames[4] = ' Category';
elementLevelNames[5] = ' Sub-Category';
elementLevelNames[6] = ' SubSub-Category';
elementLevelNames[7] = ' SSS Category';
elementLevelNames[8] = ' SSSS Category';
elementLevelNames[9] = ' SSSSS Category';

var selectedElementLevelNames=new Array();
selectedElementLevelNames[0] = '';
selectedElementLevelNames[1] = '';
selectedElementLevelNames[2] = '';
selectedElementLevelNames[3] = '';
selectedElementLevelNames[4] = '';
selectedElementLevelNames[5] = '';
selectedElementLevelNames[6] = '';
selectedElementLevelNames[7] = '';
selectedElementLevelNames[8] = '';
selectedElementLevelNames[9] = '';

var level= initialLevel;
var documentPathLabel='';
var documentPathId='';
function selectPath()
{
    var isPathSelected = false;
    var docPath = "";
	var isSOP = false;
	   	
    for(var ii=2; ii<=9; ii++)
    {
      if("" != document.getElementById("label_"+ii).title)
      { 
         if(selectedItem.indexOf("SOP") > -1 || (document.getElementById("label_4").innerHTML).indexOf("SOP") > -1 )
         {
          isSOP =  true;
         }
         docPath = docPath +"/"+ document.getElementById("label_"+ii).title ;
      } 	
     
      if(""!= selectedElementLevelNames[ii])
      { 
       documentPathLabel = documentPathLabel + "/"+ selectedElementLevelNames[ii];
      }     
       
    }
	
		
		
	obj = document.getElementById('createMultiple');
 	for (var i = 1; i < obj.options.length; i++) 
 	{ 
       if(obj.options[i].selected)
       {
         if("" == obj.options[i].value)
         {
         	documentPathId = initialElementPath +docPath
         }
         else
         {
         	documentPathId = initialElementPath +docPath+"/"+obj.options[i].value;
         }
         documentPathLabel = documentPathLabel+"/"+obj.options[i].text;
         isPathSelected = true;
         window.opener.document.getElementById("productPath").value = documentPathLabel;
         window.opener.document.getElementById("sopPathId").value = document.getElementById('createMultiple').options[document.getElementById('createMultiple').selectedIndex].value;
         window.close();
       } 
    }
    
    if (!isPathSelected)
    {
      alert("Please select SOP path.");
      return false;
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

function loadDropdown()
{
	var currentElementLevel = level;
	
		if(currentElementLevel < 3)
		{
			documentPathLabel = '';
		}
	parentId=$id("initialSelectBox").value;
	
	if (parentId!="") {
	
		selectedItem = document.getElementById('initialSelectBox').options[document.getElementById('initialSelectBox').selectedIndex].text;
		
		
		selectedElementLevelNames[currentElementLevel] = selectedItem;
	    
	//	documentPathLabel = documentPathLabel + "/"+ selectedItem;
		
		selectedItem = selectedItem + " >> ";
		var labelUpdate = "label_"+currentElementLevel;
		$id(labelUpdate).innerHTML = selectedItem; 
		$id(labelUpdate).style.display = 'inline';
		$id(labelUpdate).title = parentId;
			
	    req = newXMLHttpRequest();
	    if (!req) {
	        alert("Your browser does not support AJAX! Add Files module is accessible only by browsers that support AJAX. " +
	              "Please check the KM requirements and contact your System Administrator");
	        return;
	    }
	    
	    req.onreadystatechange = returnJson;
	    var url=path+"/elementAction.do?methodName=loadElementDropDown&ParentId="+parentId+"&Dummy="+new Date().getTime();
	    req.open("GET", url, true);
	    req.send(null);
	}
	
	if(currentElementLevel > 3 )
	{	 
	    req = newXMLHttpRequest();
	    req.onreadystatechange = populateDocPath;
	    url=path+"/elementAction.do?methodName=loadDocPath&ParentId="+parentId+"&Dummy="+new Date().getTime();
	    req.open("GET", url, true);
	    req.send(null);
	
	}
	
}

function returnJson() {
    if (req.readyState == 4) {
        if (req.status == 200) {
            var json = eval('(' + req.responseText + ')');
            
			var elements = json.elements;
			
		if (elements.length!=0)
		 {
			level = json.level;
			var maxlevel=elementLevelNames.length-1;
			var currentLevel=parseInt(level)+1;
			if(currentLevel>maxlevel)
			{
			 	alert("Element cannot be Selected Beyond the Maximum Level.");
			 	return;
			}
						 
	         		var addOption = function (value, text, selectBox){
	                var optn = document.createElement("OPTION");
	                optn.text = text;
	                optn.value = value;
	                selectBox.options.add(optn);
	            	}
	                
	            var selectDropDown = document.getElementById("initialSelectBox");
	            
	            cleanSelectBox("initialSelectBox");
	            var opt = "Select" + elementLevelNames[level];
				addOption("",opt, selectDropDown);
				for (var i = 0; i < elements.length; i++) {
		            addOption(elements[i].elementId, elements[i].elementName, selectDropDown);
		        }
		        
		        var text="Select"+elementLevelNames[level];
		        $id("elementLabel").innerHTML = text ;
		    }
		    
        }
       }
    }
  
  
 function populateDocPath() {    
    if (req.readyState == 4) {
        if (req.status == 200) {        
            var json = eval('(' + req.responseText + ')');
            
			var elements = json.elements;
			
		if (elements.length!=0)
		 {			
				document.getElementById("circleLabel").innerHTML = "Select Document ";
		        document.getElementById("circleLabel").style.display="inline";
		        document.getElementById("circleSelect").style.display="inline";

	         		var addOption = function (value, text, selectBox){
	                var optn = document.createElement("OPTION");
	                optn.text = text;
	                optn.value = value;
	                selectBox.options.add(optn);
	            	}
	                
	                var circelDropDown = document.getElementById("createMultiple");
	          		        
		        	cleanSelectBox("createMultiple");
		        	addOption("-1",'Select Document', circelDropDown);
		        	for (var j = 0; j < elements.length; j++) {
		            addOption(elements[j].documentId, elements[j].documentName, circelDropDown);
		          }
		}		
       else
       {
		document.getElementById("circleLabel").innerHTML = "";
		document.getElementById("circleLabel").style.display = "none";
		document.getElementById("circleSelect").style.display = "none";
	   }
	}
  }
 }
    
   
 function switchElement(selectedLabel)
  {
  	var labelParent = "";
  	var labelId;
  	selectedLabel = selectedLabel.id;
  	var len = selectedLabel.length;
  	var labelNo = selectedLabel.substring((len-1),len);
  	var parentLabelNo = parseInt(labelNo)-1 ;
  	labelParent = "label_"+parentLabelNo;
  	
  	if(labelParent == "label_1")
  		labelParent = 1;
  	else if(initialLevel == 3 && labelParent == "label_2" )
  		labelParent = initialParentId;
  	else if(initialLevel == 4 && labelParent == "label_3" )
  		labelParent = initialParentId;
  	else
  		labelParent = $id(labelParent).title;
  	
	if (labelParent!="") {
	
		for(var i=labelNo ; i <= 9 ; i++)
		{
			labelId = "label_" + i ;
			$id(labelId).innerHTML = ""; 
			$id(labelId).style.display = 'none';
			$id(labelId).title = "";	
		}
			
	    req = newXMLHttpRequest();
	    if (!req) {
	        alert("Your browser does not support AJAX! Add Files module is accessible only by browsers that support AJAX. " +
	              "Please check the KM requirements and contact your System Administrator");
	        return;
	    }
	    
	    req.onreadystatechange = returnJson;
	    var url=path+"/elementAction.do?methodName=loadElementDropDown&ParentId="+labelParent+"&Dummy="+new Date().getTime();
	    req.open("GET", url, true);
	    req.send(null);
	}
  	
  }
  
   function cleanSelectBox(selectBox)
  {
  	var obj = document.getElementById(selectBox);
    if (obj == null) return;
	if (obj.options == null) return;
	while (obj.options.length > 0) {
		obj.remove(0);
	}
  }
  
function $id(id) {
	return document.getElementById(id);
}



</script>
<body leftmargin="0" rightmargin="0" topmargin="0" background="0" >
<html:form action="/sopUpload">

  <div class="box2" style="margin-right: 0px;">
        <div class="content-upload">
          <h1>Select Path</h1>
             <table border="0" width="100%" align="left" cellspacing="0" cellpadding="0">
               <tr> 
              	<td rowspan="5" width="1%">&nbsp;            
              	</td>
              </tr>
              <tr>
               <td colspan="4" height="40px"> &nbsp;   
	             <div class="fll"><div class="inner-breadcrumbs">

	              <p>
		           <label id="label_2" title="" onclick="switchElement(this)" style="display:none"> </label> 
				   <label id="label_3" title="" onclick="switchElement(this)" style="display:none"> </label> 
				   <label id="label_4" title="" onclick="switchElement(this)" style="display:none"> </label> 
				   <label id="label_5" title="" onclick="switchElement(this)" style="display:none"> </label> 
				   <label id="label_6" title="" onclick="switchElement(this)" style="display:none"> </label> 
				   <label id="label_7" title="" onclick="switchElement(this)" style="display:none"> </label> 
				   <label id="label_8" title="" onclick="switchElement(this)" style="display:none"> </label> 
				   <label id="label_9" title="" onclick="switchElement(this)" style="display:none"> </label>
			  	 </p>
	            </div>
	            </div>
	            </td>	            
              </tr>  

              <tr>
               <td width="20%" nowrap="nowrap" valign="top" align="left">    
	            <div class="fll">
	            <p class="clearfix fll"> 	<!--  Label for LOB, circle, Cat -->     
		            <div id="elementLabel"> Select  <script
							type="text/javascript">document.write(elementLevelNames[initialLevel]);</script>
						 </div>
	            </p>
	            </div>
              </td>
              <td width="20%" nowrap="nowrap"  valign="top" align="left">
                <div class="fll"> <!--  select Combo for LOB, circle, Cat -->
            		<html:select property="initialSelectBox" styleId="initialSelectBox" 
					onchange="javascript:loadDropdown();">
						<html:option value=""> Select <script type="text/javascript">
						document.write(elementLevelNames[initialLevel]);</script></html:option>
						<logic:present name="firstDropDown" scope="session">
							<html:options collection="firstDropDown" property="elementId"
								labelProperty="elementName" />
						</logic:present>
					</html:select>
					</div>
              </td>
              
              <td width="15%" nowrap="nowrap" align="left" valign="top">
              		 <p align="right" class="clearfix fll" style="margin-right: 5px;"> <label  id="circleLabel" title="Select Document " style="display:none;">
              		 </label>  </p>&nbsp;
              </td>
              <td align="left" valign="top">	
             	  <div class="clearfix fll"> <!--  select SOP Path -->
		             <div id="circleSelect"  style="display:none;">
						<html:select property="createMultiple" styleId="createMultiple">
						</html:select>
					</div>
           		 </div>
               </td>
            </tr>
            <tr> <td colspan="4" height="5">&nbsp;</td>
            </tr>
            <tr> <td> </td><td></td>
             <td height="25" align="left" colspan="2">	            
        	    <div class="button"><a class="red-btn" onclick="selectPath();" ><b>Select</b></a></div>
		      </div>   
             </td>
            </tr>
            
              
      
      
            <tr> <td colspan="5" height="15">&nbsp;</td>
            </tr>
           </table>  
                  
    </div>
  </div>
 </html:form>          
</body> 