package uk.ac.aber.dcs.cs221.n15.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the users database table.
 * 
 */
@Entity
@Table(name="users")
@NamedQueries({
	@NamedQuery(name="checkExists", query="SELECT u FROM User u WHERE u.username=:username AND u.password=:password"),
	@NamedQuery(name="usernameExists", query="SELECT u FROM User u WHERE u.username=:username")
})
public class User {
	@Id
	@Column(name="id")
	/**
	 * The id of the user
	 */
	private String id;

	@Column(name="username")
	/**
	 * The username of the user
	 */
	private String username;
	
	@Column(name="password")
	/**
	 * The password of the user
	 */
	private String password;
	
	@Column(name="money")
	/**
	 * The amount of money the user has
	 */
	private int money;
	
	@Column(name="friends")
	/**
	 * The friends list of the user
	 */
	private String friends;
	

	/**
	 * Instantiates a user
	 */
    public User() {
    }
    
    /**
     * Instantiates a user
     * 
     * @param username The username of the user
     * @param password The password of the user
     */
    public User(String username, String password){
    	this.username = username;
    	this.password = password;
    	this.id = "loc."+username;
    	this.money = 150;
    	friends = "";
    }

    /**
     * Gets the id of the user
     * 
     * @return The id of the user
     */
	public String getId() {
		return this.id;
	}

	/**
	 * Sets the id of the user
	 * 
	 * @param id The id of the user
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the username of the user
	 * 
	 * @return The username of the user
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * Sets the username of the user
	 * 
	 * @param username The username of the user
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets the password of the user
	 * 
	 * @return The password of the user
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * Sets the password of the user
	 * 
	 * @param password The password of the user
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets the money of the user
	 * 
	 * @return The money of the user
	 */
	public int getMoney() {
		return money;
	}

	/**
	 * Sets the money of the user
	 * 
	 * @param money The money of the user
	 */
	public void setMoney(int money) {
		this.money = money;
	}

	/**
	 * Gets the users friends list
	 * 
	 * @return The users friends list
	 */
	public String getFriends() {
		return friends;
	}

	/**
	 * Sets the users friends list
	 * 
	 * @param friends The users friends list
	 */
	public void setFriends(String friends) {
		this.friends = friends;
	}
	
	
	
}