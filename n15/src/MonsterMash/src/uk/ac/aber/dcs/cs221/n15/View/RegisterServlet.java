package uk.ac.aber.dcs.cs221.n15.View;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import uk.ac.aber.dcs.cs221.n15.Controller.UserDAO;
import uk.ac.aber.dcs.cs221.n15.Controller.Validator;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet(name = "RegisterServlet", urlPatterns = { "/RegisterServlet" })
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	UserDAO dao;
	HttpSession session;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterServlet() {
        super();
        // TODO Auto-generated constructor stub
        dao = new UserDAO();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username, password, confirmPassword, hashedPassword;
		username = request.getParameter("email");
		password = request.getParameter("password");
		confirmPassword = request.getParameter("confirmpassword");
		session = request.getSession();
		
		if(dao.usernameExists(username)) {
			String message = "Such username already exists.";
			session.setAttribute("message", message);
			response.sendRedirect("register.jsp");
		}else if(!password.equals(confirmPassword)) {
			String message = "Passwords are not equal. Try again.";
			session.setAttribute("message", message);
			response.sendRedirect("register.jsp");
		}else{
		
		hashedPassword = Validator.toMD5(password);
		dao.createUser(username, hashedPassword);
		
		request.getSession().setAttribute("message", "Congratulations! You have sucessfully signed up! Please login to continue to the dashboard!");
		response.sendRedirect("index.jsp");
		}
	}

}
