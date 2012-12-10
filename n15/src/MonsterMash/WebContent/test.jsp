<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.Connection"%>
<%@page import="javax.sql.DataSource"%>
<%@page import="javax.naming.InitialContext"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Insert title here</title>
</head>
<body>
<%
InitialContext ctx = new InitialContext();
DataSource ds = (DataSource) ctx.lookup("jdbc/mysql");
Connection connection = ds.getConnection();

if (connection == null)
{
    throw new SQLException("Error establishing connection!");
}
String query = "SELECT * FROM user";

PreparedStatement statement = connection.prepareStatement(query);
ResultSet rs = statement.executeQuery();

while (rs.next())
{
    out.print(rs.getString("email") + "<br>");
}
%>

</body>
</html>