<%@page contentType="text/html" pageEncoding="UTF-8"%>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Monster Mash - Registration</title>
<link rel="stylesheet" type="text/css" href="style.css" />
<link href='http://fonts.googleapis.com/css?family=Eater|Skranji|Sanchez|Piedra|Carter+One|Slackey' rel='stylesheet' type='text/css' />
<script type="text/javascript" src="js/validation.js"></script>
</head>
<body>
<br></br>
<div id="login_page_content">


<img src="img/mmlogo.png" />

<center>

<% HttpSession s = request.getSession();
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

<form action="RegisterServlet" method="post" name="registerForm" onsubmit="return false;">
<br/><br/>
		<table>
			<tr>
			<td>email address: </td>
			<td><input type="text" name="email" class="required" data-minlength="6"></input></td>
			</tr>
			<tr>
			<td>password: </td>
			<td><input type="password" name="password" class="required password_confirm"  data-minlength="6"></input></td>
			</tr>
			<tr>
			<td>confirm password: </td>
			<td><input type="password" name="confirmpassword" class="required password_confirm" data-minlength="6"></input></td>
			</tr>
		</table>
		<br/> 
<!-- 		<input class="title" type="submit"/> -->
		<a class="title" href="JAVASCRIPT:validate(registerForm);" >REGISTER</a><br  />
</form>
</center>


</div>	


</body>
</html>
