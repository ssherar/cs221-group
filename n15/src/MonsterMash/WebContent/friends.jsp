<%@page import="uk.ac.aber.dcs.cs221.n15.Controller.RequestType"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    import="uk.ac.aber.dcs.cs221.n15.Model.*"
    import="java.util.*"
    import="java.text.DateFormat" %>
 
<% HttpSession s = request.getSession(false);
if(s==null){
	response.sendRedirect("index.jsp"); 
	return;
}
User user = (User)(s.getAttribute("currentUser"));
List<Monster> monsters = (List<Monster>)(s.getAttribute("monsters"));
ArrayList<Friend> friends = (ArrayList<Friend>)(s.getAttribute("friends"));
if(user == null) {
	response.sendRedirect("index.jsp"); 
	return;
} 
if(friends==null) friends = new ArrayList<Friend>();
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
			<td align="left"><img src="img/logo.png"  height="70px"></img></td>
			
			
			
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
					$<%= user.getMoney() %>
					</div>
					
					<div id="login_info">
					Logged as: <a href="edituser.jsp"><%= user.getUsername() %></a>
					<a href="LoginServlet?logout"><img id="logout_icon" src="img/logout.png"  height="15px" /></a>
					</div>
					
					
				</div>
				<br/>
				<hr class="horizontal_spacer" />
							
			<div id="friends_div" class="vertical_half" >
			<p class="title_half">Friends</p>
			
			<% if(friends.size()==0) {%>
			
			<p>You don't have any friends. Use search option to find friend's profile.</p>		
			
			<% } %>	
						
			<% for(Friend friend : friends) {%>			
			<div class="friend_window">
				<h5><a href="profile.jsp?id=<%=friend.getId() %>"><%= friend.getName() %></a></h5>
				<p>
				<img src="img/monster_icon.png"  height="15px" /><%= friend.getNumberOfMonsters() %>
				<img src="img/pouch_icon.png"  height="15px" /> $<%= friend.getMoney() %>
				</p>
			</div>
			<% } %>
			
			</div>
			
			
			
			<div id="request_div" class="vertical_half">
			<p class="title_half">Find a friend</p>
			<form action="profile.jsp">
				<input name="id" type="text" size="30" value="Type friend's username" onClick="if(this.value == 'Type friend\'s username') {this.value='';}"/>
				<input type="submit" value="find"/>
			</form>
			<p class="title_half">Requests</p>
			
			<%
				NotificationManager nm = new NotificationManager(user);
			%>
			<%=nm.getNotifications(RequestType.FRIEND_REQUEST) %>
			<%=nm.getNotifications(RequestType.ACCEPTED_FRIENDSHIP) %>
			<%=nm.getNotifications(RequestType.DECLINED_FRIENDSHIP) %>
			
			
			
			</div>
			
			
			<br/><br/>
			
			
		</div>
	</center>
</body>
</html>