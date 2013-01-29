package uk.ac.aber.dcs.cs221.n15.View;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.annotation.WebServlet;

import uk.ac.aber.dcs.cs221.n15.Controller.UserDAO;
import uk.ac.aber.dcs.cs221.n15.Controller.Validator;
import uk.ac.aber.dcs.cs221.n15.Model.User;

import java.io.PrintWriter;

/**
 * Servlet implementation class EditUserServlet
 */
@WebServlet(name = "EditUserServlet", urlPatterns = { "/EditUserServlet" })
public class EditUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditUserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserDAO udao = new UserDAO();
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("currentUser");
		String message = "";
		
		String currentPassword = Validator.toMD5((String) request.getParameter("curPass"));
		String newPassword = Validator.toMD5((String) request.getParameter("newPass"));
		String confirmPassword = Validator.toMD5((String) request.getParameter("confPass"));
		
		if(udao.authenticateUser(user.getUsername(), currentPassword) != null) {
			if(newPassword.equals(confirmPassword)) {
				boolean retVal = udao.updateUser(user.getId(), newPassword);
				if(retVal) {
					message = "Password updated sucessfully";
				} else {
					message = "Oops! Something went wrong our side, please try again!";
				}
			} else {
				message = "The new password was not the same as the confirmed password! Please try again.";
			}
		} else {
			message = "The current password was incorrect!";
		}
		session.setAttribute("message", message);
		response.sendRedirect("edituser.jsp");
	}

}
