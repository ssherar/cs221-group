package uk.ac.aber.dcs.cs221.n15.View;



import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
import uk.ac.aber.dcs.cs221.n15.Model.Friend;
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
        // TODO Auto-generated constructor stub
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String uname = request.getParameter("email");
		User user = dao.authenticateUser(uname, hashedPassword);
		
		if(user!=null) {
			List<Monster>  monsters = dao.loadMonsters(uname);
			ArrayList<Friend> friends = dao.getFriends(user);
			HttpSession session = request.getSession();
			session.setAttribute("currentUser", user);
			session.setAttribute("monsters", monsters);
			session.setAttribute("friends", friends);
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
			if(request.getParameter("test") != null) {
				dao.test();
				MonsterDAO mdao = new MonsterDAO();
				mdao.createMonster("CookieMonster", "kamil");
			}
			response.sendRedirect("index.jsp");
		}
	}

}
