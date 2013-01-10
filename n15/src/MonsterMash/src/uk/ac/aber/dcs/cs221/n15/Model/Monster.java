package uk.ac.aber.dcs.cs221.n15.Model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the monsters database table.
 * 
 */
@Entity
@Table(name="monsters")
@NamedQueries({
	@NamedQuery(name="loadMonsters", query="SELECT m FROM Monster m WHERE m.ownerId=:uid")
})
public class Monster{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private String id;

	@Column(name="aggression")
	private int aggression;

	@Column(name="color")
	private String color;

    @Temporal( TemporalType.TIMESTAMP)
    @Column(name="dob")
	private Date dob;

    @Column(name="fertility")
	private int fertility;

    @Column(name="gender")
	private char gender;

    @Column(name="health")
	private int health;

	@Column(name="name")
	private String name;
	
	@Column(name="strength")
	private int strength;

	/*//bi-directional many-to-one association to User
    @ManyToOne
	@JoinColumn(name="owner_id")
	private User user;*/
	
	@Column(name="owner")
	private String ownerId;
	

	public Monster() {
    }
	
	public Monster(String name, String ownerId) {
		this.name = name;
		this.ownerId = ownerId;
		this.id = ownerId + "." + name;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getAggression() {
		return this.aggression;
	}

	public void setAggression(int aggression) {
		this.aggression = aggression;
	}

	public String getColor() {
		return this.color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Date getDob() {
		return this.dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public int getFertility() {
		return this.fertility;
	}

	public void setFertility(int fertility) {
		this.fertility = fertility;
	}

	public char getGender() {
		return this.gender;
	}

	public void setGender(char gender) {
		this.gender = gender;
	}

	public int getHealth() {
		return this.health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String monsterName) {
		this.name = monsterName;
	}

	public int getStrength() {
		return this.strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	/*public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}*/
	
	 public String getOwnerId() {
			return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
}