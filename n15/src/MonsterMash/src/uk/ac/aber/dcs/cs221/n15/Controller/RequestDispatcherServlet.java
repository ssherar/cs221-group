package uk.ac.aber.dcs.cs221.n15.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import uk.ac.aber.dcs.cs221.n15.Model.Friend;
import uk.ac.aber.dcs.cs221.n15.Model.Monster;
import uk.ac.aber.dcs.cs221.n15.Model.User;
import uk.ac.aber.dcs.cs221.n15.View.LoginServlet;


@WebServlet(name = "RequestDispatcherServlet", urlPatterns = { "/RequestDispatcherServlet" })
public class RequestDispatcherServlet extends HttpServlet{

	private User user;
	private RequestDAO rdao = new RequestDAO();
	private HttpSession s;
	
	/**
	 * Handles all requests for breeding, buying, fighting and friendships. The main entry
	 * point for the file.
	 * 
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
	 * To remove a Friend
	 * action=removefriend
	 * targetid (String)
	 * 
	 * Optional:
	 * content = string
	 * 
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
				break;
			case BUY_MONSTER:
				rdao.createRequest(user.getId(), targetId, RequestType.BUY_MONSTER, null);
				buyMonster(targetId);
				resp.sendRedirect("myfarm.jsp");
				break;
			}
		}else if(action.equals("accept")){
			
			switch(type){
			case ACCEPTED_FRIENDSHIP:
				acceptFriendRequest(requestId);
				resp.sendRedirect("friends.jsp");
				break;
			case ACCEPTED_FIGHT:
				acceptFightRequest(requestId);
				resp.sendRedirect("fights.jsp");
				break;
			case ACCEPT_BREED_OFFER:
				//in this case user clicked on monster to breed with
				//now he needs to pick his monster and then breeding is to be resolved
				Request fr = new Request(null, targetId, RequestType.ACCEPT_BREED_OFFER);
				s.setAttribute("pendingRequest", fr);
				resp.sendRedirect("picker.jsp");
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
				declineFightRequests(requestId);
				resp.sendRedirect("fights.jsp");
				break;
			}
		}else if(action.equals("dismiss")){
			Request r = rdao.getRequest(requestId);
			switch(r.getType()){
			case ACCEPTED_FRIENDSHIP:
			case DECLINED_FRIENDSHIP:
				rdao.deleteRequest(requestId);
				resp.sendRedirect("friends.jsp");
				break;
			case DECLINED_FIGHT:
				rdao.deleteRequest(requestId);
				resp.sendRedirect("fights.jsp");
				break;
			case ACCEPT_BREED_OFFER:
				rdao.deleteRequest(requestId);
				resp.sendRedirect("breed.jsp");
				break;
			case BUY_MONSTER:
				rdao.deleteRequest(requestId);
				resp.sendRedirect("shop.jsp");
				break;
			case FIGHT_RESOLVED:
				dismissResolved(requestId);
				resp.sendRedirect("fights.jsp");
				
			}
			
			
		}else if(action.equals("picked")){
			Request pendingRequest = (Request)s.getAttribute("pendingRequest");
			if(pendingRequest==null) return;
			pendingRequest.setSeen(0);
			String pickedMonsterId = req.getParameter("pickedid");
			switch(pendingRequest.getType()){
			case OFFER_FIGHT:
				rdao.createRequest(pickedMonsterId, pendingRequest.getTargetID(), 
						RequestType.OFFER_FIGHT, null);
				resp.sendRedirect("fights.jsp");
				break;
			case ACCEPT_BREED_OFFER:
				pendingRequest.setSourceID(pickedMonsterId);
				acceptBreedOffer(pendingRequest);
				rdao.persistRequest(pendingRequest);
				resp.sendRedirect("myfarm.jsp");
			}
			
			
		}else if(action.equals("removefriend")){
			UserDAO udao = new UserDAO();
			udao.removeFriendship(user.getId(), targetId);
			resp.sendRedirect("friends.jsp");
		}
		
	}

	@Override
	/**
	 * Sends the data to doGet(request, response)
	 * @see(RequestDispatcherServlet#doGet(req, resp)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doGet(req, resp);
	}
	
	
	/**
	 * Dismisses the resolved request in the database by deleting.
	 * 
	 * This only deletes when both users have seen the notificaton
	 * @param requestId the record identifier
	 */
	private void dismissResolved(int requestId){
		Request r = rdao.getRequest(requestId);
		if(r==null) System.out.println("IN dismissResolved request is null! req id = "+requestId);
		//check if user is target or source
		//check seen field from the request
		//check if should be removed
		//yes - remove , no - change seen
		
		int seen = r.getSeen();
		
		//If current user is the source
		if(r.getSourceID().contains(user.getId()+".")){
			if(seen==2) rdao.deleteRequest(requestId);
		    else rdao.changeSeen(requestId, 1);
		}else{
		//If current user is the target
			if(seen==1) rdao.deleteRequest(requestId);
		    else rdao.changeSeen(requestId, 2);
		}
		
	}
	
	/**
	 * Creates a request which is identified as a RequestType.FRIEND_REQUEST
	 * in the database.
	 * @param friendId the friends unique identifier
	 */
	public void sendFriendRequest(String friendId) {
		rdao.createRequest(user.getId(), friendId, RequestType.FRIEND_REQUEST, null);
	}
	
	/**
	 * Actions the request by changing the request to an RequestType.ACCEPTED_FRIENDSHIP,
	 * and also adds the friendship in the database and refreshes the session variable which holds the User model
	 * 
	 * @param requestId the requests unique identifier.
	 */
	public void acceptFriendRequest(int requestId) {
		rdao.updateRequestType(requestId, RequestType.ACCEPTED_FRIENDSHIP);
		UserDAO udao = new UserDAO();
		Request r = rdao.getRequest(requestId);
		udao.addFriendship(user.getId(), r.getSourceID());
	}
	
	/**
	 * Updates the request by setting the request type to a RequestType.DECLINED_FRIENDSHIP
	 * @param requestId
	 */
	public void declineFriendRequest(int requestId) {
		rdao.updateRequestType(requestId, RequestType.DECLINED_FRIENDSHIP);
	}
	
	/**
	 * Accepts the request, and then fights the monsters.
	 * 
	 * It retrieves both monsters unique identifier, and then fights them using {@link MonsterDAO#fight(monster1, monster2)}.
	 * When the winning monster is returned, the looser gets removed from the database, and
	 * the prize money gets added to the user. Finally it sets the Request to Request.FIGHT_RESOLVED.
	 * 
	 * @param requestId the request UID
	 */
	public void acceptFightRequest(int requestId) {
		//If request is accepted, the fight is resolved and changes made in a database.
		Request r = rdao.getRequest(requestId);
		MonsterDAO mdao = new MonsterDAO();
		Monster sourceMonster = mdao.findMonster(r.getSourceID());
		Monster targetMonster = mdao.findMonster(r.getTargetID());
		Monster winner = mdao.fight(sourceMonster, targetMonster);
		
		String looserId = winner.getId().equals(r.getSourceID()) ? 
				r.getTargetID() : r.getSourceID();
		Monster looser = mdao.findMonster(looserId);
		
		//winner's hp is updated
		mdao.updateHealth(winner.getId(), winner.getHealth());
		
		//owner gets prize
		UserDAO udao = new UserDAO();
		User u = udao.findUser(winner.getOwnerId());
		udao.changeMoney(u, mdao.calculatePrize(looser));
		
		rdao.updateRequestType(r.getId(), RequestType.FIGHT_RESOLVED, winner.getId());
		
		//looser is removed.
		mdao.wipeMonster(looserId);
		
		//reloading list of monsters
		LoginServlet.reloadMonsters(s, user.getId());
	}
	
	public void declineFightRequests(int requestId) {
		rdao.updateRequestType(requestId, RequestType.DECLINED_FIGHT);
	}
	
	/**
	 * Accepts the breed request, and then breeds the monsters together.
	 * 
	 * It retrieves the monsters UID from the request database, and then breeds the monsters
	 * together by using the {@link MonsterDAO#breed(monster1, monster2)}, which returns the children,
	 * which is passed to the Source user, and then adds the breed price amount to the Target users
	 * coffers. It finally reloads the user from the database and adds it to the session variable
	 * @param r
	 */
	public void acceptBreedOffer(Request r) {
		MonsterDAO mdao = new MonsterDAO();
		//Source monster, i. e. "mother".
		//Children will go to source monster's owner,
		//While target's owner will get the money
		Monster sourceMonster = mdao.findMonster(r.getSourceID());
		Monster targetMonster = mdao.findMonster(r.getTargetID());
		if(!targetMonster.isForBreeding()) return;
		//Here children get evaluated
		List<Monster> children = mdao.breed(sourceMonster, targetMonster);
		
		System.out.println("Children:");
		for(Monster m : children) System.out.print(m.toString());
		
		UserDAO udao = new UserDAO();
		User sourceUser = udao.findUser(sourceMonster.getOwnerId());
		User targetUser = udao.findUser(targetMonster.getOwnerId());
		
		//And payment. 
		//(checking if user can afford when loading links to breed,
		//but we will double-check here)
		int price = targetMonster.getBreedPrice();
		if(sourceUser.getMoney()<price) return;
		udao.changeMoney(targetUser, price);
		udao.changeMoney(sourceUser, -price);
				
		//Saving new monsters to database.
		for(Monster child : children) mdao.persistMonster(child);
		System.out.print("Children should be in the database");
		LoginServlet.reloadMonsters(s, user.getId());
		
		//and reloading the user
		User rld = udao.findUser(user.getId());
		s.setAttribute("currentUser", rld);
		
	}
	
	/**
	 * Handles the transfer of monsters between users, while checking if the user has
	 * enough money for the transaction.
	 * @param monsterId
	 */
	public void buyMonster(String monsterId) {
		//transfer money
		//and change owner and id of the monster
		MonsterDAO mdao = new MonsterDAO();
		Monster monsterToBuy = mdao.findMonster(monsterId);
		
		//Checking if user can afford
		if(user.getMoney()<monsterToBuy.getSalePrice()) return;
			
		//and transfers money
		UserDAO udao = new UserDAO();
		User seller = udao.findUser(monsterToBuy.getOwnerId());
		int price = monsterToBuy.getSalePrice();
		
		udao.changeMoney(seller, price);
		udao.changeMoney(user, -price);
		
		//and changes the owner
		mdao.changeOwner(monsterId, user.getId());
		
		//and reloading the user and monsters
		User rld = udao.findUser(user.getId());
		s.setAttribute("currentUser", rld);
		LoginServlet.reloadMonsters(s, user.getId());
		
	}
}
