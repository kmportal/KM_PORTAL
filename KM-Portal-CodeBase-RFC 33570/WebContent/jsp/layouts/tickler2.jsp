<% String messages = (String) session.getAttribute("tickler2");
			if(messages==null ||messages.equals("")){
				messages="Welcome To iPortal.";
			}
			
%>			


<%= messages %>



