package uk.ac.aber.dcs.cs221.n15.View;

import java.io.IOException;
import uk.ac.aber.dcs.cs221.n15.Controller.*;
import uk.ac.aber.dcs.cs221.n15.Model.*;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.sql.*;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet(name = "RegisterServlet", urlPatterns = { "/RegisterServlet" })
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	UserDAO dao;
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
		
		if(dao.usernameExists(username)) {
			response.sendRedirect("register.jsp");
		}
		
		if(!password.equals(confirmPassword)) {
			response.sendRedirect("register.jsp");
		}
		
		hashedPassword = Validator.toMD5(password);
		
		String sql = "INSERT INTO users VALUES (NULL, '"+username+"', '"+hashedPassword+"')";
		System.out.println(sql);
	
			InitialContext ctx;
			try {
				ctx = new InitialContext();
				DataSource ds = (DataSource) ctx.lookup("jdbc/mysql");
				Connection conn = ds.getConnection();
				Statement stmt = conn.createStatement();
				stmt.execute(sql);
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		request.getSession().setAttribute("message", "Congratulations! You have sucessfully signed up! Please login to continue to the dashboard!");
		response.sendRedirect("index.jsp");
	}

}
