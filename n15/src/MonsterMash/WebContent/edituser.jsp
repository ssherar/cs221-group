<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    import="uk.ac.aber.dcs.cs221.n15.Model.*"
    import="uk.ac.aber.dcs.cs221.n15.Controller.*"
    import="java.util.*"
    import="java.text.DateFormat"%>
    
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
%>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" href="style.css" />
<link href='http://fonts.googleapis.com/css?family=Eater|Skranji|Sanchez|Piedra|Carter+One|Slackey' rel='stylesheet' type='text/css' />
<script type="text/javascript" src="js/validation.js"></script>
<title>Monster Mash</title>
<script>
function deleteUser() {
	if(confirm("Are you sure that you want to delete your account? This action is unreversable")) {
		deleteAccountForm.submit();
	}
}
</script>
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
				Logged as:<a href="edituser.jsp"><%= user.getUsername() %></a> 
				<a href="LoginServlet?logout"><img id="logout_icon" src="img/logout.png"  height="15px" /></a>
				</div>
					
					
			</div>
			<br/>
			<hr class="horizontal_spacer" />
			
			<p class="title">Edit Account</p>
			<%
			if(s != null) {
				if(s.getAttribute("message") != null) {
					%>
					<%= s.getAttribute("message") %>
					<%
					s.removeAttribute("message");
				}
			}
			%>
			<form action="EditUserServlet/edit" method="post" name="editForm">
			<table>
			<tr><td>current password: </td><td><input type="password" name="curPass"></input></td></tr>
			<tr><td>new password: </td><td><input type="password" name="newPass" class="password_confirm" data-minlength="6"></input></td></tr>
			<tr><td>confirm password: </td><td><input type="password" name="confPass" class="password_confirm" data-minlength="6"></input></td></tr>
			</table>
			<a class="title_half" href="JAVASCRIPT:validate(editForm);" >CHANGE</a>
			</form>
			<form action="EditUserServlet/delete" method="post" name="deleteAccountForm"> 
				<a href="JAVASCRIPT:deleteUser();">delete your account</a>
			</form>
		</div>
		</center>
</body>
</html>