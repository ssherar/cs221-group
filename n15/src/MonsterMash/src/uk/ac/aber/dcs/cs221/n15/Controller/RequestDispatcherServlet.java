package uk.ac.aber.dcs.cs221.n15.Controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import uk.ac.aber.dcs.cs221.n15.Model.Friend;
import uk.ac.aber.dcs.cs221.n15.Model.User;


@WebServlet(name = "RequestDispatcherServlet", urlPatterns = { "/RequestDispatcherServlet" })
public class RequestDispatcherServlet extends HttpServlet{

	private User user;
	private RequestDAO rdao = new RequestDAO();
	private HttpSession s;
	
	/*
	 * To send a request :
	 * action=send
	 * targetid (string)
	 * type (int)
	 * 
	 * To respond to a request :
	 * action=accept/decline
	 * requestid (int)
	 * type (int)
	 * 
	 * To dismiss a notification/request
	 * action=dismiss
	 * requestid (int)
	 * 
	 * To pick monster:
	 * action=picked
	 * pickedid (String)
	 * 
	 * Optional:
	 * content = string
	 * 
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		s = req.getSession(false);
		user = (User) s.getAttribute("currentUser");
		//If user is not logged in, redirects to login page
		if(user==null) resp.sendRedirect("index.jsp");
		
		String action = req.getParameter("action");
		String reqId = req.getParameter("requestid");
		int requestId = reqId==null ? 0 : Integer.parseInt(reqId);
		String tNr = req.getParameter("type");
		RequestType type = RequestType.values()[tNr==null ? 0 : Integer.parseInt(tNr)];
		String targetId = req.getParameter("targetid");
		
		if(action.equals("send")){
			
			switch(type){
			case FRIEND_REQUEST:
				sendFriendRequest(targetId);
				resp.sendRedirect("friends.jsp");
				break;
			case OFFER_FIGHT:
				//in this case targetid is id of a monster, not user!
				Request fr = new Request(null, targetId, RequestType.OFFER_FIGHT);
				s.setAttribute("pendingRequest", fr);
				resp.sendRedirect("picker.jsp");
			}
		}else if(action.equals("accept")){
			
			switch(type){
			case ACCEPTED_FRIENDSHIP:
				acceptFriendRequest(requestId);
				resp.sendRedirect("friends.jsp");
				break;
			case ACCEPTED_FIGHT:
				break;
			case ACCEPT_BREED_OFFER:
				break;
			}
		}else if(action.equals("decline")){
			switch(type){
			case DECLINED_FRIENDSHIP:
				//Decline Friend request
				declineFriendRequest(requestId);
				resp.sendRedirect("friends.jsp");
				break;
			case DECLINED_FIGHT:
				break;
			}
		}else if(action.equals("dismiss")){
			Request r = rdao.getRequest(requestId);
			rdao.deleteRequest(requestId);
			switch(r.getType()){
			case ACCEPTED_FRIENDSHIP:
			case DECLINED_FRIENDSHIP:
				resp.sendRedirect("friends.jsp");
				break;
			case ACCEPTED_FIGHT:
			case DECLINED_FIGHT:
				resp.sendRedirect("fight.jsp");
				break;
			case ACCEPT_BREED_OFFER:
				resp.sendRedirect("breed.jsp");
			}
			
			
			
		}else if(action.equals("picked")){
			Request pendingRequest = (Request)s.getAttribute("pendingRequest");
			if(pendingRequest==null) return;
			switch(pendingRequest.getType()){
			case OFFER_FIGHT:
				String pickedMonsterId = req.getParameter("pickedid");
				rdao.createRequest(pickedMonsterId, pendingRequest.getTargetID(), 
						RequestType.OFFER_FIGHT, null);
				resp.sendRedirect("fights.jsp");
			}
			
		}
		
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doGet(req, resp);
	}
	
	
	public void sendFriendRequest(String friendId) {
		rdao.createRequest(user.getId(), friendId, RequestType.FRIEND_REQUEST, null);
	}
	
	public void acceptFriendRequest(int requestId) {
		rdao.updateRequestType(requestId, RequestType.ACCEPTED_FRIENDSHIP);
		UserDAO udao = new UserDAO();
		Request r = rdao.getRequest(requestId);
		udao.addFriendship(user.getId(), r.getSourceID());
		//Refreshing list of friends
		User rld = udao.findUser(user.getId());
		s.setAttribute("currentUser", rld);
		ArrayList<Friend> friends = udao.getFriends(rld);
		s.setAttribute("friends", friends);
	}
	
	public void declineFriendRequest(int requestId) {
		rdao.updateRequestType(requestId, RequestType.DECLINED_FRIENDSHIP);
	}
	
	public void sendFightRequest(String monsterID, String friendMonsterID) {
		
	}
	
	public void acceptFightRequest(int requestId) {
		
	}
	
	public void declineFightRequests(int requestId) {
		
	}
	
	public void sendBreedRequest(String monsterID, String friendMonsterID) {
		
	}
	
	public void acceptBreedRequest(int requestId) {
		
	}
	
	public void rejectBreedRequest(int requestId) {
		
	}
	
	public void sendBuyRequest(String monsterID) {
		
	}
	
	public void acceptBuyRequest(int requestId) {
		
	}
	
	public void declineBuyRequest(int requestId) {
		
	}

	
}
