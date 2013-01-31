/**
 * 
 */
package uk.ac.aber.dcs.cs221.n15.Model;

import java.util.List;

import com.sun.jersey.core.impl.provider.entity.XMLJAXBElementProvider.App;

import uk.ac.aber.dcs.cs221.n15.Controller.MonsterDAO;
import uk.ac.aber.dcs.cs221.n15.Controller.Request;
import uk.ac.aber.dcs.cs221.n15.Controller.RequestDAO;
import uk.ac.aber.dcs.cs221.n15.Controller.RequestType;

/**
 * @author kamil
 *
 */
public class NotificationManager {

	private User user;
	private RequestDAO rdao;
	StringBuilder builder;
	
	public NotificationManager(User u){
		this.user = u;
	}
	
	/*
	 * Returns notifications for given user, converted to HTML code.
	 * Type specifies what type of requests are to be displayed as notifications.
	 */
	public String getNotifications(RequestType type){
		this.builder = new StringBuilder();
		this.rdao = new RequestDAO();
		List<Request> requests = rdao.getRequests(user, type);
		switch(type){
		case FRIEND_REQUEST:
			for(Request r : requests){
				System.out.println("zero!");
				builder.append(processFriendRequest(r));
			}
			break;
		
		case ACCEPTED_FRIENDSHIP:
			for(Request r : requests){
				if(r.getTargetID().equals(user.getId())) continue;
				else builder.append(processAcceptedFriendship(r));
			}
			break;
		
		case DECLINED_FRIENDSHIP:
			for(Request r : requests){
				if(r.getTargetID().equals(user.getId())) continue;
				else builder.append(processDeclinedFriendship(r));
			}	
			break;			
		case OFFER_FIGHT:
			for(Request r : requests){
				builder.append(processFightOffer(r));
			}	
			if(builder.length()==0){
				builder.append("<p>No notifications to display.</p>");
			}
			break;
		case FIGHT_RESOLVED:
			for(Request r : requests){
				//if user is target and seen is 2 - continue
				//if user is source and seen is 1 continue
				//else display
				if(r.getTargetID().contains(user.getId()+".")){
					if(r.getSeen()==2) continue;
				}else{
					if(r.getSeen()==1) continue;
				}
				
				builder.append(processFightResolved(r));
			}	
			if(builder.length()==0){
				builder.append("<p>No notifications to display.</p>");
			}
			break;
		case DECLINED_FIGHT:
			for(Request r : requests){
				if(r.getTargetID().contains(user.getId()+".")) continue;
				builder.append(processDeclinedFight(r));
			}	
			break;
		case ACCEPT_BREED_OFFER:
			for(Request r : requests){
				if((r.getTargetID().contains(user.getId()+"."))==false) continue;
				builder.append(processAcceptedBreeding(r));
			}	
			if(builder.length()==0){
				builder.append("<p>No notifications to display.</p>");
			}
			break;
		case BUY_MONSTER:
			for(Request r : requests){
				if((r.getTargetID().contains(user.getId()+"."))==false) continue;
				builder.append(processBuyRequest(r));
			}	
			if(builder.length()==0){
				builder.append("<p>No notifications to display.</p>");
			}
			break;
		}
		
		return builder.toString();
	}
	
	private String processFriendRequest(Request r){
		StringBuilder sb = new StringBuilder();
		//If the user sent the request
		if(r.getSourceID().equals(user.getId())){
			sb.append("<div class=\"request_window\">");
			sb.append("Sent invitation to ");
			sb.append(r.getTargetID().substring(r.getTargetID().lastIndexOf(".")+1));
			sb.append(",<br/> waiting for response.</div>");
		}else{
			//If the user received the request
			sb.append("<div class=\"request_window\">");
			sb.append(r.getSourceID().substring(r.getSourceID().lastIndexOf(".")+1));
			sb.append(" wants to be friends with you!<br/>");
			String acceptLink = "RequestDispatcherServlet?action=accept&type=1&requestid="+r.getId();
			String declineLink = "RequestDispatcherServlet?action=decline&type=2&requestid="+r.getId();
			sb.append("<a href=\""+acceptLink+"\">accept </a>");
			sb.append("|<a href=\""+declineLink+"\"> decline </a></div>");
			
		}
	return sb.toString();
	}
	
	private String processAcceptedFriendship(Request r){
		StringBuilder sb = new StringBuilder();
		
		sb.append("<div class=\"request_window\">");
		sb.append(r.getTargetID().substring(r.getTargetID().lastIndexOf(".")+1));
		sb.append(" accepted your invitation.");
		sb.append("<a href=\"RequestDispatcherServlet?action=dismiss&requestid="+r.getId()+"\">");
		sb.append("<img src=\"img/close.png\" width=\"10px\" /></a></div>");
		return sb.toString();
	}
	
	private String processDeclinedFriendship(Request r){
		StringBuilder sb = new StringBuilder();
		sb.append("<div class=\"request_window\">");
		sb.append(r.getTargetID().substring(r.getTargetID().lastIndexOf(".")+1));
		sb.append(" declined your invitation.");
		sb.append("<a href=\"RequestDispatcherServlet?action=dismiss&requestid="+r.getId()+"\">");
		sb.append("<img src=\"img/close.png\" width=\"10px\" /></a></div>");
		return sb.toString();
	}
	
	private String processFightOffer(Request r){
		StringBuilder sb = new StringBuilder();
		
		MonsterDAO mdao = new MonsterDAO();
		Monster mine, foe;
		if(r.getSourceID().contains(user.getId())){
			mine = mdao.findMonster(r.getSourceID());
			foe = mdao.findMonster(r.getTargetID());
		}else{
			mine = mdao.findMonster(r.getTargetID());	
			foe = mdao.findMonster(r.getSourceID());					
		}
		
		//My monster window
		sb.append("<div class=\"fight_window\">");
		sb.append("<div class=\"monster_window\"><div class=\"monster_description\">" +
				"<p class=\"monster_name\">")
		.append(mine.getName()).append("</p>Age:")
		.append(mdao.calculateDaysDifference(mine.getDob()))
		.append("<br/><table class=\"monster_stats\">")
		.append("<tr><td>health:</td><td>"+mine.getHealth()+"</td></tr>")
		.append("<tr><td>strength:</td><td>"+mine.getStrength()+"</td></tr>")
		.append("<tr><td>aggression:</td><td>"+mine.getAggression()+"</td></tr>")
		.append("<tr><td>fertility:</td><td>"+mine.getFertility()+"</td></tr>")
		.append("</table></div><div class=\"monster_actions_menu\">")
		.append("fight prize: $"+mdao.calculatePrize(mine)+ "<br/>")
		.append("<a href=\"myfarm.jsp\">view your farm</a></div></div>");
		sb.append("<div class=\"vs_text\">VS</div>");

		//Enemy monster window
		String profileUrl = "profile.jsp?id="+foe.getOwnerId();
		sb.append("<div class=\"monster_window\"><div class=\"monster_description\">" +
				"<p class=\"monster_name\">")
		.append(foe.getName()).append("</p>Age:")
		.append(mdao.calculateDaysDifference(foe.getDob()))
		.append("<br/><table class=\"monster_stats\">")
		.append("<tr><td>health:</td><td>"+foe.getHealth()+"</td></tr>")
		.append("<tr><td>strength:</td><td>"+foe.getStrength()+"</td></tr>")
		.append("<tr><td>aggression:</td><td>"+foe.getAggression()+"</td></tr>")
		.append("<tr><td>fertility:</td><td>"+foe.getFertility()+"</td></tr>")
		.append("</table></div><div class=\"monster_actions_menu\">")
		.append("fight prize: $"+mdao.calculatePrize(foe)+ "<br/>")
		.append("<a href=\""+profileUrl+"\">view friends profile</a></div></div>");
		
		//If user sent the request, he should see a confirmation that it is pending.
		if(r.getSourceID().contains(user.getId())){
			sb.append("<p>Request sent, waiting for response.</p>");
		}else{
		//If user received the request, he should see two links, to accept and to decline
			String acceptLink = "RequestDispatcherServlet?action=accept&type=4&requestid="+r.getId();
			String declineLink = "RequestDispatcherServlet?action=decline&type=5&requestid="+r.getId();
			sb.append("<div class=\"accept_decline\"><a href=\""+acceptLink+"\">accept </a> | " +
					"<a href=\""+declineLink+"\">decline </a></div>");
		}
		sb.append("</div>");
		
		return sb.toString();
	}
	
	private String processFightResolved(Request r){
		StringBuilder sb = new StringBuilder();
		
		String winnerName = Monster.parseNameFromId(r.getContent());
		String sourceName = Monster.parseNameFromId(r.getSourceID());
		String targetName = Monster.parseNameFromId(r.getTargetID());
		
		sb.append("<div class=\"request_window\">");
		sb.append("<p>Fight between ").append(sourceName)
		.append(" and ").append(targetName).append(".<br/>");
		
		//If user's monster won
		if(r.getContent().contains(user.getId()+".")){
			sb.append("Congratulations! Your monster (").append(Monster.parseNameFromId(r.getSourceID()))
			.append(") won!</p>");
		}else{
			sb.append("Your monster (").append(winnerName.equals(sourceName) ? targetName : sourceName)
			.append(") lost the fight and died.</p>");
		}		
		
		sb.append("<a href=\"RequestDispatcherServlet?action=dismiss&requestid="+r.getId()+"\">");
		sb.append("<img src=\"img/close.png\" width=\"10px\" /></a></div>");
		
		return sb.toString();
	}
	
	private String processDeclinedFight(Request r){
		StringBuilder sb = new StringBuilder();
		String ownerName;
		MonsterDAO mdao = new MonsterDAO();
		Monster m = mdao.findMonster(r.getTargetID());
		ownerName = m.getOwnerId().substring(4);
		
		String sourceName = Monster.parseNameFromId(r.getSourceID());
		String targetName = Monster.parseNameFromId(r.getTargetID());
		
		sb.append("<div class=\"request_window\">");
		sb.append("<p>Fight between ").append(sourceName)
		.append(" and ").append(targetName).append(".<br/>");
		sb.append("<p>").append(ownerName).append(" declined your fight offer.");
		sb.append("<a href=\"RequestDispatcherServlet?action=dismiss&requestid="+r.getId()+"\">");
		sb.append("<img src=\"img/close.png\" width=\"10px\" /></a></div>");
		
		return sb.toString();
	}
	
	public String processAcceptedBreeding(Request r){
		StringBuilder sb = new StringBuilder();
		
		sb.append("<div class=\"request_window\">");
		sb.append("Someone used your monster for breeding, you earned some cash.");
		sb.append("<a href=\"RequestDispatcherServlet?action=dismiss&requestid="+r.getId()+"\">");
		sb.append("<img src=\"img/close.png\" width=\"10px\" /></a></div>");
		return sb.toString();
	}
	
	public String processBuyRequest(Request r){
		StringBuilder sb = new StringBuilder();
		
		String monsterName = Monster.parseNameFromId(r.getTargetID());
		
		sb.append("<div class=\"request_window\">");
		sb.append("Someone bought your monster ("+monsterName+"), you earned some cash!");
		sb.append("<a href=\"RequestDispatcherServlet?action=dismiss&requestid="+r.getId()+"\">");
		sb.append("<img src=\"img/close.png\" width=\"10px\" /></a></div>");
		return sb.toString();
	}
	
}
