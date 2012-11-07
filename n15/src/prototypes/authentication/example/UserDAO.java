package example;
import java.sql.*;

public class UserDAO {
	
	public static UserBean login(UserBean user) {
		Statement stmt = null;
		String username = user.getUsername();
		String password = user.getPassword();
		
		String query = String.format("SELECT * FROM users WHERE username = '%s' AND password='%s'", username, password);
		
		System.out.println(query);
		try {
			Class.forName("org.postgresql.Driver");
			Connection con = DriverManager.getConnection("jdbc:postgresql://localhost/cs221", "cs221_user", "");
			ResultSet resultsSet = null;
			System.out.println(con);
			stmt = con.createStatement();
			resultsSet = stmt.executeQuery(query);
			System.out.println(resultsSet.toString());
			if(resultsSet.next()) {
				user.setValid(true);
			} else {
				user.setValid(false);
			}
		} catch (SQLException e) {
			System.err.println(e);
		} catch (ClassNotFoundException e) {
			System.out.println(e);
		}
		return user;
	}
}
