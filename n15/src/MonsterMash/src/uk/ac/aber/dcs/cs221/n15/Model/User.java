package uk.ac.aber.dcs.cs221.n15.Model;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


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
	private String id;

	@Column(name="username")
	private String username;
	
	@Column(name="password")
	private String password;
	
	@Column(name="money")
	private int money;

	//bi-directional many-to-one association to Monster
	//@OneToMany(mappedBy="user")
	//private List<Monster> monsters;

    public User() {
    }
    
    public User(String username, String password){
    	this.username = username;
    	this.password = password;
    	this.id = "loc."+username;
    	this.money = 150;
    }

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
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

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	/*public List<Monster> getMonsters() {
		return this.monsters;
	}

	public void setMonsters(List<Monster> monsters) {
		this.monsters = monsters;
	}*/
	
	
	
}