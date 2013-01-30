/**
 * 
 */
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
 * @author kamil
 *
 */
@WebServlet(name = "RenameServlet", urlPatterns = { "/MonsterEditServlet" })
public class MonsterEditServlet extends HttpServlet {

	MonsterDAO mdao;
	
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
		}
		//Redirecting to farm page
		resp.sendRedirect("myfarm.jsp");
	}
	
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
		this.reloadMonsters(s);
	}
	
	private void reloadMonsters(HttpSession s) {
		User user = (User) s.getAttribute("currentUser");
		UserDAO dao = new UserDAO();
		List<Monster> nmonsters = dao.loadMonsters(user.getId());
		s.setAttribute("monsters", nmonsters);
	}
	
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
		this.reloadMonsters(s);
	}
	
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
}
