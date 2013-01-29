package uk.ac.aber.dcs.cs221.n15.Controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import uk.ac.aber.dcs.cs221.n15.Model.User;


@WebServlet(name = "RequestDispatcherServlet", urlPatterns = { "/RequestDispatcherServlet" })
public class RequestDispatcherServlet extends HttpServlet{

	User user;
	/*
	 * To send a request :
	 * action=send
	 * targetid (string)
	 * type (int)
	 * 
	 * To respond to a request :
	 * action=response
	 * requestid (int)
	 * type (int)
	 * 
	 * Optional:
	 * 
	 * content = string
	 * 
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		HttpSession s = req.getSession(false);
		user = (User) s.getAttribute("currentUser");
		if(user==null) resp.sendRedirect("index.jsp");
		
		String action = req.getParameter("action");
		if(action.equals("send")){
			String targetId = req.getParameter("targetid");
			int type = Integer.parseInt(req.getParameter("type"));
			switch(type){
			case 0:
				//Friend request
				sendFriendRequest(targetId);
				resp.sendRedirect("friends.jsp");
			}
		}
		
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}
	
	public void requestDispatcher(String userID) {
		
	}
	
	public void sendFriendRequest(String friendId) {
		RequestDAO rdao = new RequestDAO();
		rdao.createRequest(user.getId(), friendId, RequestType.FRIEND_REQUEST, null);
	}
	
	public void acceptFriendRequest(int requestId) {
		
	}
	
	public void declineFriendRequest(int requestId) {
		
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
