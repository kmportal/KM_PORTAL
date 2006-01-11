<% String messages = (String) session.getAttribute("tickler1");
			if(messages==null ||messages.equals("")){
				messages="Welcome To iPortal.";
			}
			if(messages.equals("Welcome To iPortal.Welcome To iPortal."))
			{
				messages="Welcome To iPortal.";
			}
			
%>			


<%= messages %>


