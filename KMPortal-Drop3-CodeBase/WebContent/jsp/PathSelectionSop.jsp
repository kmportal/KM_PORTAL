
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<bean:define id="kmUserBean1" name="USER_INFO"  type="com.ibm.km.dto.KmUserMstr" scope="session" />

<script language="javascript">
	var path = '<%=request.getContextPath()%>';
	var port = '<%= request.getServerPort()%>';
	var serverName = '<%=request.getServerName() %>';

var initialElementPath='<%=kmUserBean1.getInitialElementPath()%>';
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

var level= initialLevel;


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
	parentId=$id("initialSelectBox").value;
	
	if (parentId!="") {
	
		var selectedItem = document.getElementById('initialSelectBox').options[document.getElementById('initialSelectBox').selectedIndex].text;
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
	    if(currentElementLevel == 2)
	    {
	    	url=path+"/elementAction.do?methodName=loadPANElementDropDown&ParentId="+parentId+"&Dummy="+new Date().getTime();
	    }
	    
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
		        var text="Select "+elementLevelNames[level];
		        $id("elementLabel").innerHTML = text ;
		    }
		    
        }
       }
        
    }
   
 function switchElement(selectedLabel)
  {
  	var labelParent = "";
  	var labelId;
  	var currentElementLevel = level;
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
	     if( ( currentElementLevel - 2 ) == 2)
	    {
	    	url=path+"/elementAction.do?methodName=loadPANElementDropDown&ParentId="+labelParent+"&Dummy="+new Date().getTime();
	    }
	    
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
             <table border="0" width="95%" align="center" cellpadding="0" cellspacing="0">
              <tr>
               <td  width="70%" colspan="2" > 
	             <div class="fll" style="height:40px;margin-top: 2px;"><div class="inner-breadcrumbs">
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
                <td width="15%" rowspan="2" align="right" valign="top">              		 
                </td>
	            <td width="15%" rowspan="2" align="left" valign="top">
             	  
               </td>
              </tr>  

              <tr>
               <td width="20%" nowrap="nowrap" valign="top" align="left">    
	            <div class="fll">
	            <p class="clearfix fll"> 	            
		            <div id="elementLabel"> Select <script
							type="text/javascript">document.write(elementLevelNames[initialLevel]);</script>
						 </div>
	            
	            </div>
              </td>
              <td width="40%" nowrap="nowrap"  valign="top" align="left">
                <div class="fll">
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
            </tr>
           </table>	
<script language="javascript">           

</script>