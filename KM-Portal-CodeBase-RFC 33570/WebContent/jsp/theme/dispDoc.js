// dispDoc.js

var req=null; 

function newXMLHttpRequest() {

    var xmlreq = false;

    if (window.XMLHttpRequest) {
        // Create XMLHttpRequest object in non-Microsoft browsers
        xmlreq = new XMLHttpRequest();

    } else if (window.ActiveXObject) {

        // Create XMLHttpRequest via MS ActiveX
        try {
            // Try to create XMLHttpRequest in later versions
            // of Internet Explorer

            xmlreq = new ActiveXObject("Msxml2.XMLHTTP");

        } catch (e1) {

            // Failed to create required ActiveXObject

            try {
                // Try version supported by older versions
                // of Internet Explorer

                xmlreq = new ActiveXObject("Microsoft.XMLHTTP");

            } catch (e2) {

                // Unable to create an XMLHttpRequest with ActiveX
            }
        }
    }
    return xmlreq;
}

var errors=false;

function addToFavorites(checkbox,docId) {
	//alert(docId);
	req = newXMLHttpRequest();
    //alert (req);
    if (!req) {
        alert("Your browser does not support AJAX! Favorites Module is accessible only by browsers that support AJAX. " +
              "Please check the KM requirements and contact your System Administrator");
        return;
    }
	
	var url="";
	
	//alert (path);
	
	if (checkbox.checked) {
		url = path+"/documentAction.do?methodName=addToFavorites&docId="+docId;
	}
	else {
		url = path+"/documentAction.do?methodName=deleteFavorites&docId="+docId;
	}
	//alert (url);
	req.onreadystatechange = favoritesDone;
	req.open("GET", url, false);
	req.send(null);
	return !errors;
}

function favoritesDone() {
	if (req.readyState == 4) {
        if (req.status == 200) {
	        alert(req.responseText);
        	var status = eval(req.responseText);
        	if (status=='1') {
        		if ($id("favorite").checked) {
        			$id("fav").style.fontWeight="bold";
        		}
        		else {
        			$id("fav").style.fontWeight="normal";
        		}
        	}
        	else {
        		if (!$id("favorite").checked) {
        			$id("fav").style.fontWeight="bold";
        		}
        		else {
        			$id("fav").style.fontWeight="normal";
        		}
        	}
        }
	}
}

function $id(id) {
	return document.getElementById(id);
}