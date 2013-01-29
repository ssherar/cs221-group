package uk.ac.aber.dcs.cs221.n15.View;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import uk.ac.aber.dcs.cs221.n15.Controller.Validator;
import uk.ac.aber.dcs.cs221.n15.Model.User;

/**
 * Servlet implementation class EditUserServlet
 */
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
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Start");
		HttpSession s = request.getSession(false);
		User u = (User) s.getAttribute("user");
		System.out.println(u.getPassword());
		String message;
		
		String currentPassword = Validator.toMD5((String) request.getParameter("curPass"));
		String newPassword = Validator.toMD5((String) request.getParameter("newPass"));
		String confirmPassword = Validator.toMD5((String) request.getParameter("confPass"));
		
		if(newPassword.equals(confirmPassword)) {
			
		} else {
			message = "Your new password was not confirmed, please try again!";
		}
	}

}

