<%@page contentType="text/html" pageEncoding="UTF-8"%>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Monster Mash - Login</title>
<link rel="stylesheet" type="text/css" href="style.css" />
<link href='http://fonts.googleapis.com/css?family=Eater|Skranji|Sanchez|Piedra|Carter+One|Slackey' rel='stylesheet' type='text/css' />
<script>
window.onload = function() {
	document.getElementById('email_field').onkeypress = keyPressed;
	document.getElementById('password_field').onkeypress = keyPressed;
};

function keyPressed(e) {
	var key = e.keyCode;
	if (key == 13) {
		loginForm.submit()
	}
}
</script>
</head>
<body>
<br></br>
<div id="login_page_content">


<img src="img/logo.png" />

<center>
<% HttpSession s = request.getSession();
s.removeAttribute("currentUser");
String message = "";
if(s != null) {
	if(s.getAttribute("message") != null) {
		%>
		<%= s.getAttribute("message") %>
		<%
		s.removeAttribute("message");
	}
}
%>
<form action="LoginServlet" method="post" name="loginForm">
<br/><br/>
		<table>
			<tr>
			<td>login: </td>
			<td><input id="email_field" type="text" name="email"></input></td>
			</tr>
			<tr>
			<td>password: </td>
			<td><input id="password_field" type="password" name="password"></input></td>
			</tr>
			<tr>
			<td>server: </td>
			<td><select name="server" style="width:153px;margin:2px;"><option>local</option><option>...</option></select></td></tr>
		</table>
		<br/> 
		<a class="title" href="JAVASCRIPT:loginForm.submit()">LOGIN</a><br />
		<a class="slackey" href="register.jsp">CREATE NEW ACCOUNT</a>
	<!-- 	<input type="button" name="dupa" value="CREATE NEW ACCOUNT"/> -->
</form>
</center>


</div>	


</body>
</html>
