<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    import="uk.ac.aber.dcs.cs221.n15.Model.*"
    import="java.util.*"
    import="java.text.DateFormat"
    import="uk.ac.aber.dcs.cs221.n15.Controller.*" %>
 
<% HttpSession s = request.getSession(false);
User user = (User)(s.getAttribute("currentUser"));
List<Monster> monsters = (List<Monster>)(s.getAttribute("monsters"));
List<Fight> fights = (List<Fight>)(s.getAttribute("fights"));
if(user == null) {
	response.sendRedirect("index.jsp"); 
} 

 if(monsters==null) monsters = new ArrayList<Monster>();
 if(fights==null)  fights = new ArrayList<Fight>();
 
MonsterDAO mdao = new MonsterDAO();


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
				<p class="title_half" >Requests</p>
				<% ArrayList<MonsterFightRequests> reqs = new ArrayList<MonsterFightRequests>();
				MonsterFightRequests test = new MonsterFightRequests();
				reqs.add(test);
				for (MonsterFightRequests req : reqs) { //TODO: Replace with getRequests() 
					//Monster attmob = mdao.getMonster(req.getAttackingMonster());
					//Monster defmob = mdao.getMonster(req.getDefendingMonster());
					Monster attmob = new Monster();
					attmob.setName("Test");
					Monster defmob = new Monster();
					defmob.setName("Feck");%>
				<div class="monster_window">
					<div class="monster_description">
					<p class="monster_name"><%= attmob.getName() %></p>
					Born: <%= "NEVER"/*DateFormat.getInstance().format(attmob.getDob())*/ %><br/>
					<table class="monster_stats">
						<tr><td>health:</td><td><%= attmob.getHealth() %></td></tr>
						<tr><td>strength:</td><td><%= attmob.getStrength() %></td></tr>
						<tr><td>aggression:</td><td><%= attmob.getAggression() %></td></tr>
						<tr><td>fertility:</td><td><%= attmob.getFertility() %></td></tr>
					</table>
					</div>
					<div class="monster_actions_menu">fight prize: $200</div>
				</div>
				<div class="vs_text">VS</div>
				<div class="monster_window">
					<div class="monster_description">
					<p class="monster_name"><%= defmob.getName() %></p>
					Born: <%= "NEVER"/*DateFormat.getInstance().format(defmob.getDob())*/ %><br/>
					<table class="monster_stats">
						<tr><td>health:</td><td><%= defmob.getHealth() %></td></tr>
						<tr><td>strength:</td><td><%= defmob.getStrength() %></td></tr>
						<tr><td>aggression:</td><td><%= defmob.getAggression() %></td></tr>
						<tr><td>fertility:</td><td><%= defmob.getFertility() %></td></tr>
					</table>
					</div>
					<div class="monster_actions_menu">fight prize: $200</div>
				</div>
				<div class="accept_decline">
				<a>accept </a>|<a> decline</a>
				</div>
				<% } %>
				</div>
				<div>
				<p class="title_half">Finished Battles</p>
				<% for (Fight fight : fights) {%>
				<% /*TODO:BattleStats*/ %>
				<% } %>
				<p>ATT lost!</p>
				<div class="monster_window">
					<div class="monster_description">
					<p class="monster_name"><%= "ATT" %></p>
					Born: <%= "NEVER"/*DateFormat.getInstance().format(attmob.getDob())*/ %><br/>
					<table class="monster_stats">
						<tr><td>health:</td><td><%= 0 %></td></tr>
						<tr><td>strength:</td><td><%= 1 %></td></tr>
						<tr><td>aggression:</td><td><%= 1 %></td></tr>
						<tr><td>fertility:</td><td><%= 1 %></td></tr>
					</table>
					</div>
					<div class="monster_actions_menu">fight prize: $200</div>
				</div>
				<div class="vs_text">VS</div>
				<div class="monster_window">
					<div class="monster_description">
					<p class="monster_name"><%= "DEF" %></p>
					Born: <%= "NEVER"/*DateFormat.getInstance().format(defmob.getDob())*/ %><br/>
					<table class="monster_stats">
						<tr><td>health:</td><td><%= 20 %></td></tr>
						<tr><td>strength:</td><td><%= 1 %></td></tr>
						<tr><td>aggression:</td><td><%= 1 %></td></tr>
						<tr><td>fertility:</td><td><%= 1 %></td></tr>
					</table>
					</div>
					<div class="monster_actions_menu">fight prize: $200</div>
				</div>
				</div>
			</div>
		</center>
</body>
</html>