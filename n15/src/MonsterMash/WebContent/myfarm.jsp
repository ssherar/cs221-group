<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    import="uk.ac.aber.dcs.cs221.n15.Model.*"
    import="uk.ac.aber.dcs.cs221.n15.Controller.MonsterDAO"
    import="java.util.*"
    import="java.text.DateFormat" %>
 
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
<script>
function rename(id) {
	var newname = prompt("What would you like to rename your monster to?", "");
	if (newname != null && newname != "") {
		//CallRename
		document.location = "MonsterEditServlet?type=rename&monsterId="+id+"&newName="+newname;
	}	
}

function changeBreedPrice(id) {
	var newPrice = prompt("What price would you like to set for breeding?", "");
	if(newPrice != null && newPrice != "" && !isNaN(newPrice)) {
		document.location = "MonsterEditServlet?type=breedPrice&monsterId=" + id + "&newPrice="+newPrice;
	}
}

function changeBuyPrice(id) {
	var newPrice = prompt("What price would you like to set for selling?", "");
	if(newPrice != null && newPrice != "" && !isNaN(newPrice)) {
		document.location = "MonsterEditServlet?type=buyPrice&monsterId=" + id + "&newPrice="+newPrice;
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
				
			
			
			<p class="title">My Monster Farm</p>
			<% MonsterDAO mdao = new MonsterDAO(); %>
			<% if(monsters!=null) for(Monster m : monsters) {%>
			
			<div class="monster_window">
				<div class="monster_description">
					<p class="monster_name"><%= m.getName() %>
				
					<% int age = mdao.calculateDaysDifference(m.getDob()); %>
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
					<a href="JAVASCRIPT:rename('<%= m.getId() %>')">rename</a><br/>
					<a href="JAVASCRIPT:changeBreedPrice('<%= m.getId() %>')">change breed price</a><br />
					<a href="JAVASCRIPT:changeBuyPrice('<%= m.getId() %>')">change buy price</a></br />
					<% boolean forSale = m.isForSale(), forBreeding = m.isForBreeding(); %>
					<a href="MonsterEditServlet?type=changeSaleOffer&monsterId=<%= m.getId() %>&value=<%= !forSale %>"><%= (forSale) ? "revoke " : "" %>offer for sale</a><br/>
					<a href="MonsterEditServlet?type=changeBreedOffer&monsterId=<%= m.getId() %>&value=<%= !forBreeding %>"> <%= (forBreeding) ? "revoke " : "" %>offer for breeding</a><br/>
					<br/>
					fight prize: $<%= mdao.calculatePrize(m) %><br />
					breed price: $<%= m.getBreedPrice() %><br />
					sell price: $<%= m.getSalePrice() %>
				</div>
			
			
			</div>
			<% } %>
			
			
		</div>
	</center>
</body>
</html>