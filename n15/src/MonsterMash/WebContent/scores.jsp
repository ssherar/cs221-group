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
UserDAO dao = new UserDAO();
User user = dao.findUser((String)s.getAttribute("currentUser"));

if(user == null) {
	response.sendRedirect("index.jsp"); 
	return;
} 
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
					$<%= user.getMoney() %>
					</div>
					
					<div id="login_info">
					Logged as: <a href="edituser.jsp"><%= user.getUsername() %></a>
					<a href="LoginServlet?logout"><img id="logout_icon" src="img/logout.png"  height="15px" /></a>
					</div>
					
					
				</div>
				<br/>
				<hr class="horizontal_spacer" />
				<p class="title">Rich List</p>
				
				<table id="rich_table">
				
				<%
				StringBuilder sb = new StringBuilder();
				
				sb.append("<thead id=\"rich_thead\"><tr><td>Position</td><td>Username</td><td>Money</td><td>Monsters</td></tr></thead>");
				ArrayList<Friend> fs = new ArrayList<Friend>();
				fs.addAll((ArrayList<Friend>)(dao.getFriends(user)));
				fs.add(new Friend(user.getId(),user.getMoney(), (Integer)s.getAttribute("numberOfMonsters")));
				
				Collections.sort(fs);
				Collections.reverse(fs);
	
				int i = 0;
				for(Friend f : fs){
					i++;
					if(f.getId().equals(user.getId())){
						sb.append("<tr><td style=\"width:10%\"><b>").append(i)
						.append("</b></td><td style=\"width:50%\"><a href=\"profile.jsp?id=\"")
						.append(f.getId()).append("\"><b>").append(f.getName()).append("</b></a></td>")
						.append("<td style=\"width:20%\"><b>$").append(f.getMoney())
						.append("</b></td><td style=\"width:20%\"><b>").append(f.getNumberOfMonsters())
						.append("</b></td></tr>");
					}else{
						sb.append("<tr><td style=\"width:10%\">").append(i)
						.append("</td><td style=\"width:50%\"><a href=\"profile.jsp?id=\"")
						.append(f.getId()).append("\">").append(f.getName()).append("</a></td>")
						.append("<td style=\"width:20%\">$").append(f.getMoney())
						.append("</td><td style=\"width:20%\">").append(f.getNumberOfMonsters())
						.append("</td></tr>");
					}
					
					
				}
				
				%>
				<%=sb.toString() %>
				</table>
				
				</div>
		</center>
	</body>
</html>