<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    import="uk.ac.aber.dcs.cs221.n15.Model.*"
    import="uk.ac.aber.dcs.cs221.n15.Controller.*"
    import="java.util.*"
    import="java.text.DateFormat" %>
 
<% HttpSession s = request.getSession(false);
if(s==null){
	response.sendRedirect("index.jsp"); 
	return;
}
User user = (User)(s.getAttribute("currentUser"));
if(user == null) {
	response.sendRedirect("index.jsp"); 
	return;
} 
UserDAO udao = new UserDAO();
String f_id = request.getParameter("id");
if(f_id.charAt(3)!='.') f_id = "loc." + f_id;
User friend = udao.findUser(f_id);
List<Monster> friendsMonsters = udao.loadMonsters(friend.getUsername());
%>




<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><%= friend.getUsername() %> - Monster mash</title>
<link rel="stylesheet" type="text/css" href="style.css" />
<link href='http://fonts.googleapis.com/css?family=Eater|Skranji|Sanchez|Piedra|Carter+One|Slackey' rel='stylesheet' type='text/css' />
</head>
<body>
	<center>
		<div id="main">
			<div id="upper_div">

			<table width="100%">
			<tr>
			<td align="left"><img src="img/mmlogo.png"  height="70px"></img></td>
			
			
			
			<td valign="bottom">
			<div id="menu_items">
			<a class="menu_link" href="friends.jsp"><img src="img/friends.png"  height="46px"/></a>
			<a class="menu_link" href="myfarm.jsp"><img src="img/home.png"  height="50px"/></a>
			<a class="menu_link" href="fights.jsp"> <img src="img/fight2.png"  height="48px"/></a>
			<a class="menu_link" href="shop.jsp"><img src="img/shop.png"  height="45px"/></a>
			<a class="menu_link" href="breed.jsp"><img src="img/breed.png"  height="47px"/></a>
			<a class="menu_link" href="scores.jsp"><img src="img/trophy.png"  height="48px"/></a>
			</div>
			</td>
			
			
			</tr>
			</table>

				

			
			</div>
			<hr class="horizontal_spacer" />
			<div id="notice_box">
			
					<div class="notice_stats">
					<img src="img/monster_icon.png"  height="15px" />
					<%= s.getAttribute("numberOfMonsters") %>
					</div>
					<div class="notice_stats">
					<img src="img/pouch_icon.png"  height="15px" />
					<%= user.getMoney() %>
					</div>
					
					<div id="login_info">
					Logged as: <a href="edituser.jsp"><%= user.getUsername() %></a> 
					<a href="LoginServlet?logout"><img id="logout_icon" src="img/logout.png"  height="15px" /></a>
					</div>
					
				</div>
				<br/>
				<hr class="horizontal_spacer" />
				
				
			<p class="profile_title"><%= friend.getUsername() %></p>
			<p class="align_left">Wealth: <%= friend.getMoney() %></p>
			<% boolean isFriend = udao.checkFriendship(user.getId(), f_id); %>
			<% 
			String message; 
			if(isFriend){
				message = "<p class=\"align_right\"><a>Remove from friends</a></p>";
			}else{
				RequestDAO rdao = new RequestDAO();
				if(rdao.requestExists(user.getId(), f_id, RequestType.FRIEND_REQUEST)){
					message = "<p class=\"align_right\">Friendship request sent.</p>";
				}else{
					if(f_id.equals(user.getId())){
						message = "<p class=\"align_right\">That's you!</p>";
					}else{
						String url ="RequestDispatcherServlet?action=send&targetid="+f_id+"&type=0";
						message = "<p class=\"align_right\"><a href=\""+url+
								"\">Send friendship request</a></p>";	
					}
				}	
			}	
			%>
			
			<%=message  %>					
						
			<% if(friendsMonsters!=null){
				MonsterDAO mdao = new MonsterDAO();
				
				for(Monster m : friendsMonsters){%>
			
			<div class="monster_window">
				<div class="monster_description">
					<p class="monster_name"><%= m.getName() %>
					</p>
					Age: <%= mdao.calculateDaysDifference(m.getDob()) %> day(s)<br/>
					<table class="monster_stats">
						<tr><td>health:</td><td><%= m.getHealth() %></td></tr>
						<tr><td>strength:</td><td><%= m.getStrength() %></td></tr>
						<tr><td>aggression:</td><td><%= m.getAggression() %></td></tr>
						<tr><td>fertility:</td><td><%= m.getFertility() %></td></tr>
					</table>
				</div>
				<div class="monster_actions_menu">
					fight prize: $<%= mdao.calculatePrize(m) %><br />
					breeding price: $<%= m.getBreedPrice() %><br />
					sale price: $<%= m.getSalePrice() %><br />
					<br />
					<% if(isFriend) { %>
						<% String fightUrl = "RequestDispatcherServlet?action=send&type=3&targetid="+m.getId(); %>
						<a href="<%=fightUrl %>">challenge to fight</a><br/>
						<% boolean forSale = m.isForSale(), forBreeding = m.isForBreeding(); %>
						<% if(forSale) { %>
							<a>buy this monster</a><br/>
						<% } %>
						<% if(forBreeding) { %>
							<a>breed with this monster</a><br />
						<% } %>
					<% } %>
				</div>			
			</div>
			<% } }else{%>
			<p>User has no monsters.</p>
			<%} %>
			
			
			
			<br/><br/>
			
			
		</div>
	</center>
</body>
</html>