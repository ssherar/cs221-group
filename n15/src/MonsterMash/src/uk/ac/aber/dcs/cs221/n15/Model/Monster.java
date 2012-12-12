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
public class Monster implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private int id;

	@Column(name="agression")
	private int agression;

	@Column(name="color")
	private String color;

    @Temporal( TemporalType.TIMESTAMP)
    @Column(name="dob")
	private Date dob;

    @Column(name="fertility")
	private int fertility;

    @Column(name="gender")
	private String gender;

    @Column(name="health")
	private int health;

	@Column(name="monster_name")
	private String monsterName;
	
	@Column(name="strength")
	private int strength;

	/*//bi-directional many-to-one association to User
    @ManyToOne
	@JoinColumn(name="owner_id")
	private User user;*/
	
	@Column(name="owner_id")
	private int ownerId;
	
    public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	public Monster() {
    }

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAgression() {
		return this.agression;
	}

	public void setAgression(int agression) {
		this.agression = agression;
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

	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getHealth() {
		return this.health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public String getMonsterName() {
		return this.monsterName;
	}

	public void setMonsterName(String monsterName) {
		this.monsterName = monsterName;
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
}