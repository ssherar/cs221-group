package uk.ac.aber.dcs.cs221.n15.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import uk.ac.aber.dcs.cs221.n15.Controller.MonsterDAO;
import uk.ac.aber.dcs.cs221.n15.Controller.UserDAO;
import uk.ac.aber.dcs.cs221.n15.Model.Monster;
import uk.ac.aber.dcs.cs221.n15.Model.User;

/**
 * Handles the editing of the monsters attributes,
 * such as the name and prices of breeding and sale.
 *
 */
@WebServlet(name = "RenameServlet", urlPatterns = { "/MonsterEditServlet" })
public class MonsterEditServlet extends HttpServlet {
	/**
	 * The Data Access Object which manipulates the data in the monsters table/
	 */
	MonsterDAO mdao;
	
	/**
	 * The main entry point into the servlet, this handles which attribute we are editing,
	 * and then passes the data to the right method
	 * 
	 * @see MonsterEditServlet#rename(req, resp)
	 * @see MonsterEditServlet#changeBreedPirce(req, resp)
	 * @see MonsterEditServlet#changeBuyPrice(req, resp)
	 * @see MonsterEditServlet#changeSaleFlag(req, resp)
	 * @see MonsterEditServlet#changeBreedFlag(req, resp)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String type = req.getParameter("type");
		mdao = new MonsterDAO();
		if(type.equals("rename")) {
			this.rename(req,resp);
		} else if(type.equals("breedPrice")) {
			this.changeBreedPrice(req, resp);
		} else if(type.equals("buyPrice")) {
			this.changeBuyPrice(req,resp);
		} else if(type.equals("changeSaleOffer")) {
			this.changeSaleFlag(req, resp);
		} else if(type.equals("changeBreedOffer")) {
			this.changeBreedFlag(req, resp);
		}
		//Redirecting to farm page
		resp.sendRedirect("myfarm.jsp");
	}
	
	/**
	 * Renames the monster according to what the user has specified
	 * 
	 * @param req the HTTP Servlet Request
	 * @param resp the HTTP Servlet Response
	 * @throws IOException If there is a problem handling the session
	 */
	private void rename(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HttpSession s = req.getSession(false);
		String monsterId = (String) req.getParameter("monsterId");
		String newName = (String) req.getParameter("newName");
				
		//Checking if logged user is allowed to change the name of that monster
		List<Monster> monsters = (List<Monster>)(s.getAttribute("monsters"));
		boolean valid = false;
		for(Monster m : monsters){
			System.out.println("ID: "+m.getId()+" requested: "+monsterId);
			if(m.getId().equals(monsterId)){
				valid = true;
				break;
			}
		}
		if(!valid){
			return;
		}
		
		//Changing name in the database
		mdao.renameMonster(monsterId, newName);
		System.out.println("Should be renamed!");
		
		//Reloading list of monsters
	}
	
	
	/**
	 * Changes the breed price of the monster.
	 * 
	 * @param req the HTTP Servlet Request
	 * @param resp the HTTP Servlet Response
	 * @throws IOException If there is a problem handling the session
	 */
	public void changeBreedPrice(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HttpSession s = req.getSession();
		int price = Integer.parseInt(req.getParameter("newPrice"));
		String monsterId = req.getParameter("monsterId");
		
		List<Monster> monsters = (List<Monster>)(s.getAttribute("monsters"));
		boolean valid = false;
		for(Monster m : monsters) {
			if(m.getId().equals(monsterId)) {
				valid = true;
				break;
			}
		}
		if(!valid) return;
		
		mdao.changeBreedPrice(monsterId, price);
	}
	
	/**
	 * Changes the Buy price of the monster.
	 * 
	 * @param req the HTTP Servlet Request
	 * @param resp the HTTP Servlet Response
	 * @throws IOException If there is a problem handling the session
	 */
	public void changeBuyPrice(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HttpSession s = req.getSession();
		int price = Integer.parseInt(req.getParameter("newPrice"));
		String monsterId = req.getParameter("monsterId");
		
		List<Monster> monsters = (List<Monster>)(s.getAttribute("monsters"));
		boolean valid = false;
		for(Monster m : monsters) {
			if(m.getId().equals(monsterId)) {
				valid = true;
				break;
			}
		}
		if(!valid) return;
		mdao.changeBuyPrice(monsterId, price);
	}
	
	/**
	 * Changes the state of the buy flag on the monster
	 * 
	 * @param req the HTTP Servlet Request
	 * @param resp the HTTP Servlet Response
	 * @throws IOException If there is a problem handling the session
	 */
	public void changeSaleFlag(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HttpSession s = req.getSession();
		boolean flag = Boolean.parseBoolean(req.getParameter("value"));
		String monsterId = req.getParameter("monsterId");
		
		List<Monster> monsters = (List<Monster>)(s.getAttribute("monsters"));
		boolean valid = false;
		for(Monster m : monsters) {
			if(m.getId().equals(monsterId)) {
				valid = true;
				break;
			}
		}
		if(!valid) return;
		mdao.changeSaleFlag(monsterId, flag);
	}
	
	/**
	 * Changes the state of the breed flag on the monster
	 * 
	 * @param req the HTTP Servlet Request
	 * @param resp the HTTP Servlet Response
	 * @throws IOException If there is a problem handling the session
	 */
	public void changeBreedFlag(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HttpSession s = req.getSession();
		boolean flag = Boolean.parseBoolean(req.getParameter("value"));
		String monsterId = req.getParameter("monsterId");
		
		List<Monster> monsters = (List<Monster>)(s.getAttribute("monsters"));
		boolean valid = false;
		for(Monster m : monsters) {
			if(m.getId().equals(monsterId)) {
				valid = true;
				break;
			}
		}
		if(!valid) return;
		mdao.changeBreedFlag(monsterId, flag);
	}
}
