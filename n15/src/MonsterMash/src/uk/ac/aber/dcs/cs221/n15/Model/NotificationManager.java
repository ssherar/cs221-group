/**
 * 
 */
package uk.ac.aber.dcs.cs221.n15.Model;

import java.util.List;

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
		System.out.println("Found: " + requests.size());
		switch(type.ordinal()){
		case 0:
			for(Request r : requests){
				System.out.println("zero!");
				builder.append(processFriendRequest(r));
			}
			System.out.println("one!");
			break;
		
		case 1:
			for(Request r : requests){
				if(r.getTargetID().equals(user.getId())) continue;
				else builder.append(processAcceptedFriendship(r));
			}
			System.out.println("two!");
			break;
		
		case 2:
			for(Request r : requests){
				if(r.getTargetID().equals(user.getId())) continue;
				else builder.append(processDeclinedFriendship(r));
			}	
			System.out.println("three!");
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
}
