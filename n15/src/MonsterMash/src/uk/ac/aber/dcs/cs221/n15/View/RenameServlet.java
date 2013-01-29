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
@WebServlet(name = "RenameServlet", urlPatterns = { "/RenameServlet" })
public class RenameServlet extends HttpServlet {

	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("START!");
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
		MonsterDAO mdao = new MonsterDAO();
		mdao.renameMonster(monsterId, newName);
		System.out.println("Should be renamed!");
		
		//Reloading list of monsters
		User user = (User) s.getAttribute("currentUser");
		UserDAO dao = new UserDAO();
		List<Monster> nmonsters = dao.loadMonsters(user.getId());
		s.setAttribute("monsters", nmonsters);
		
		//Redirecting to farm page
		resp.sendRedirect("myfarm.jsp");
	}

}
