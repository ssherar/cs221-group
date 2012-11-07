package example;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("logout") != null) {
			HttpSession session = request.getSession();
			if(session.getAttribute("currentUser") != null) {
				session.setAttribute("currentUser", null);
				response.sendRedirect("login.jsp");
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			UserBean user = new UserBean();
			user.setUserName(request.getParameter("un"));
			user.setPassword(request.getParameter("pw"));
			/*if(user.getUsername().equals("sam") && user.getPassword().equals("password")) {
				user.setValid(true);
			} else {
				user.setValid(false);
			}*/
			user = UserDAO.login(user);
			if(user.isValid()) {
				HttpSession session = request.getSession();
				session.setAttribute("currentUser", user);
				response.sendRedirect("loggedin.jsp");
			} else {
				response.getWriter().write("Not Valid");
			}
		} catch(Exception e) {
			System.out.println(e);
		}
	}

}
