package uk.ac.aber.dcs.cs221.n15.Controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestDispatcher extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}
	
	public void requestDispatcher(String userID) {
		
	}
	
	public void sendFriendRequest(String freindID) {
		
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
