<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    import="uk.ac.aber.dcs.cs221.n15.Model.*"
    import="java.util.*"
    import="java.text.DateFormat"
    import="uk.ac.aber.dcs.cs221.n15.Controller.*" %>
 
<% HttpSession s = request.getSession(false);
User user = (User)(s.getAttribute("currentUser"));
List<Monster> monsters = (List<Monster>)(s.getAttribute("monsters"));

if(user == null) {
	response.sendRedirect("index.jsp"); 
} 

 if(monsters==null) monsters = new ArrayList<Monster>();
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
					<%= monsters.size() %>
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
				<% //for (Fight fight : fights) {%>
				<div class="request_window">Your monster has returned from breeding. You now have a new monster!</div>
				<!-- Needs split to check which monster is whos -->
				<% //} %>
				</div>
				
				<div>
				<p class="title_half" >Requests</p>
				<%//for (BreedRequests) { //TODO: Replace with getRequests() 
					//Monster attmob = mdao.getMonster(req.getAttackingMonster());
					//Monster defmob = mdao.getMonster(req.getDefendingMonster());
					Monster mob1 = new Monster();
					mob1.setName("Test");
					Monster mob2 = new Monster();
					mob2.setName("Feck");%>
				<div class="breed_window">
				<div class="monster_window">
					<div class="monster_description">
					<p class="monster_name"><%= mob1.getName() %></p>
					Born: <%= "NEVER"/*DateFormat.getInstance().format(mob1.getDob())*/ %><br/>
					<table class="monster_stats">
						<tr><td>health:</td><td><%= mob1.getHealth() %></td></tr>
						<tr><td>strength:</td><td><%= mob1.getStrength() %></td></tr>
						<tr><td>aggression:</td><td><%= mob1.getAggression() %></td></tr>
						<tr><td>fertility:</td><td><%= mob1.getFertility() %></td></tr>
					</table>
					</div>
					<div class="monster_actions_menu">
						fight prize: $200 <br />
						<a>view your farm</a>
					</div>
				</div>
				<div class="monster_window">
					<div class="monster_description">
					<p class="monster_name"><%= mob2.getName() %></p>
					Born: <%= "NEVER"/*DateFormat.getInstance().format(mob2.getDob())*/ %><br/>
					<table class="monster_stats">
						<tr><td>health:</td><td><%= mob2.getHealth() %></td></tr>
						<tr><td>strength:</td><td><%= mob2.getStrength() %></td></tr>
						<tr><td>aggression:</td><td><%= mob2.getAggression() %></td></tr>
						<tr><td>fertility:</td><td><%= mob2.getFertility() %></td></tr>
					</table>
					</div>
					<div class="monster_actions_menu">
						fight prize: $200
						<br />
						<a>view friends profile</a>
					</div>
				</div>
				<div class="accept_decline">
				<a>accept </a>|<a> decline</a>
				</div>
				</div>
				
				<% //} %>
				</div>
				
			</div>
		</center>
</body>
</html>