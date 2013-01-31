<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    import="uk.ac.aber.dcs.cs221.n15.Model.*"
    import="java.util.*"
    import="java.text.DateFormat"
    import="uk.ac.aber.dcs.cs221.n15.Controller.*" %>
 
<% HttpSession s = request.getSession(false);
if(s==null){
	response.sendRedirect("index.jsp"); 
	return;
}
User user = (User)(s.getAttribute("currentUser"));
ArrayList<Friend> friends = (ArrayList<Friend>)(s.getAttribute("friends"));
if(user == null) {
	response.sendRedirect("index.jsp"); 
	return;
} 
if(friends==null) friends = new ArrayList<Friend>();
%>

<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Monster mash</title>
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
				
				<div>
				<p class="title_half">Finished Breeding</p>
				<%
				NotificationManager nm = new NotificationManager(user);
				%>
				<%=nm.getNotifications(RequestType.ACCEPT_BREED_OFFER) %>
				</div>
				
				<p class="title_half">Offers available for you</p>
				
				<%
				List<Monster> monstersForBreeding = new ArrayList<Monster>();
				UserDAO udao = new UserDAO();
				MonsterDAO mdao = new MonsterDAO();
				for(Friend f : friends){
					monstersForBreeding.addAll(udao.getMonstersForBreeding(f.getId()));
					if(monstersForBreeding.size()>60) break;
				}
				
				
				for(Monster m : monstersForBreeding){%>
				
				<div class="monster_window">
				<div class="monster_description">
					<p class="monster_name"><%= m.getName() %>
					<%
					int age = mdao.calculateDaysDifference(m.getDob());
					%>
					</p>
					Age: <%=age  %> 
					<%= age==1 ? "day<br/>" : "days<br/>" %>
					<table class="monster_stats">
						<tr><td>health:</td><td><%= m.getHealth() %></td></tr>
						<tr><td>strength:</td><td><%= m.getStrength() %></td></tr>
						<tr><td>aggression:</td><td><%= m.getAggression() %></td></tr>
						<tr><td>fertility:</td><td><%= m.getFertility() %></td></tr>
					</table>
					
				</div>
				
				<div class="monster_actions_menu">
					
					<a href="RequestDispatcherServlet?action=accept&type=7&targetid=<%=m.getId() %>">
					BREED</a><br/>
					<br/>
					price: $<%= m.getBreedPrice()%><br />
				</div>
			
			
			</div>
			<%} %>
					
				<% if(monstersForBreeding.size()==0) {%>
				
				<p>Your friends don't offer any monsters for breeding at the moment.</p>		
				
				<% } %>	
								
			</div>
		</center>
</body>
</html>