package uk.ac.aber.dcs.cs221.n15.Model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the users database table.
 * 
 */
@Entity
@Table(name="users")
@NamedQueries({
	@NamedQuery(name="checkExists", query="SELECT u FROM User u WHERE u.username=:username AND u.password=:password")
	/*,
	@NamedQuery(name="createUser", query="INSERT INTO users VALUES(NULL, ':username', ':passsword')"),
	@NamedQuery(name="resetPassword", query="UPDATE users SET password=':password' WHERE email=':username'")*/
})
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id")
	private int id;
	
	@Column(name="email")
	private String username;
	
	@Column(name="password")
	private String password;

    public User() {
    }

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}