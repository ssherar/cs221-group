package uk.ac.aber.dcs.cs221.n15.View;



import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import uk.ac.aber.dcs.cs221.n15.Controller.UserDAO;
import uk.ac.aber.dcs.cs221.n15.Model.Monster;
import uk.ac.aber.dcs.cs221.n15.Model.User;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet(name = "LoginServlet", urlPatterns = { "/LoginServlet" })
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private UserDAO dao;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        dao = new UserDAO();
        
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String hashedPassword = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(request.getParameter("password").getBytes());
			hashedPassword = new BigInteger(1, md.digest()).toString(16);
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		String uname = request.getParameter("email");
		User user = dao.authenticateUser(uname, hashedPassword);
		
		if(user!=null) {
			//Calculating monster's parameter according to age
			HttpSession session = request.getSession();
			LoginServlet.reloadMonsters(session, user.getId());
			session.setAttribute("currentUser", user.getId());
			response.sendRedirect("myfarm.jsp");
		} else {
			HttpSession session = request.getSession();
			session.setAttribute("message", "The username/password is not correct. Please try again!");
			response.sendRedirect("index.jsp");
		}
		
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("logout") != null) {
			HttpSession session = request.getSession();
			if(session.getAttribute("currentUser") != null) {
				session.removeAttribute("currentUser");
				session.invalidate();
				response.sendRedirect("index.jsp");
			}
		} else {
			response.sendRedirect("index.jsp");
		}
	}
	
	/**
	 * Monsters are stored in session variable,
	 * however sometimes they need to be refreshed.
	 * That's what the method does.
	 */
	public static void reloadMonsters(HttpSession s, String userId){
		UserDAO dao = new UserDAO();
		List<Monster>  monsters = dao.loadMonsters(userId);
		s.setAttribute("monsters", monsters);
		s.setAttribute("numberOfMonsters", monsters.size());
	}

}
