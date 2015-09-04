
<%
//  			String docId =	(String)request.getSession().getAttribute("docId");
  	
  			String docId =	(String)request.getAttribute("docID");
  			if(docId == null)
  			docId = (String) request.getParameter("docID");
  			if(docId == null)
  			docId = (String)request.getSession().getAttribute("docId");
  			boolean found = false;
  			String addFav = "display: none" ;
  			String remFav  = "display: none";
  			ArrayList<String> fav = new ArrayList<String>();
			fav = (ArrayList<String>) request.getSession().getAttribute("favList");  	
			found = fav.contains(docId);	
			
  			/**  for(int i=0 ; i< fav.size();i++) {
  			if(fav.get(i).toString().equalsIgnoreCase(docId)){
  			found = true;  			
  			break;
  			}
  			else{  			
  			found = false;
  			}
  			} **/
  			
  			 %>
  			<%
  			if(found)
  			{
  			remFav = "display: block";
  			
  			 %>
          
  			<%
  			}else{
  				addFav = "display: block";
  			 %>
  			  
  				<%} %>
  				 
<%@page import="java.util.ArrayList"%><span class="icon flr"  id="favRem" style="<%=remFav%>">
				<img src="common/images/favorite-remove.gif" onclick="javascript:removeFromFavorites(this.id);" align="right" title="Click to remove from Favorites" alt="Click to remove from Favorites" id="<%=docId%>" ></img>
  			</span>
  			 <span class="icon flr"  id="favAdd" style="<%=addFav%>">
				<img  src="common/images/favorite-add.gif" onclick="javascript:addToFavorites(this.id);" align="right"  title="Click to add to Favorites" alt="Click to add to Favorites"  id="<%=docId%>" ></img>
  			</span>
  			
  			
  			