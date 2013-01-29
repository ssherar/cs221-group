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
	
	public NotificationManager(User u){
		this.user = u;
	}
	
	/*
	 * Returns notifications for given user, converted to HTML code.
	 * Type specifies what type of requests are to be displayed as notifications.
	 */
	public String getNotifications(RequestType type){
		StringBuilder sb = new StringBuilder();
		RequestDAO rdao = new RequestDAO();
		List<Request> requests = rdao.getRequests(user, type);
		
		if(type == RequestType.FRIEND_REQUEST)
		for(Request r : requests){
			//If the user sent the request
			if(r.getSourceID().equals(user.getId())){
				sb.append("<div class=\"request_window\">");
				sb.append("Sent invitation to ");
				String[] arr = r.getTargetID().split(".");
				sb.append(r.getTargetID().substring(r.getTargetID().lastIndexOf(".")+1));
				sb.append(",<br/> waiting for response.</div>");
			}else{
				//If the user received the request
				sb.append("<div class=\"request_window\">");
				String[] arr = r.getSourceID().split(".");
				sb.append(r.getTargetID().substring(r.getTargetID().lastIndexOf(".")+1));
				sb.append("wants to be friends with you!<br/>");
				String acceptLink = "RequestDispatcherServlet?action=respond&type=1&requestid="+r.getId();
				String declineLink = "RequestDispatcherServlet?action=respond&type=2&requestid="+r.getId();
				sb.append("<a href=\""+acceptLink+"\">accept </a>");
				sb.append("|<a href=\""+declineLink+"\">decline </a></div>");
				
			}
			
		}
		return sb.toString();
	}
}
