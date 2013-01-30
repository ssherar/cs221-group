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
	
	@Column(name="lockMonster")
	private int locked;
	
	@Column(name="breedPrice")
	private int breedPrice;
	
	@Column(name="isForBreeding", columnDefinition= "TINYINT(1)")
	private boolean isForBreeding;
	
	@Column(name="isForSale", columnDefinition= "TINYINT(1)")
	private boolean isForSale;
	
	@Column(name="salePrice")
	private int salePrice;

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
		if(aggression > 100) {
			aggression = 100;
		} else if(aggression < 0) {
			aggression = 0;
		}
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
		if(fertility > 100) {
			fertility = 100;
		} else if(fertility < 0) {
			fertility = 0;
		}
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
		if(health > 100) {
			health = 100;
		} else if(health < 0) {
			health = 0;
		}
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
		if(strength > 100) {
			strength = 100;
		} else if(strength < 0) {
			strength = 0;
		}
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
	
	public int getLocked() {
		return locked;
	}

	public void setLocked(int locked) {
		this.locked = locked;
	}
	
	public int getBreedPrice() {
		return breedPrice;
	}
	
	public void setBreedPrice(int breedPrice) {
		this.breedPrice = breedPrice;
	}
	
	public static String parseNameFromId(String monsterId){
		int lastPoint = monsterId.lastIndexOf(".");
		return monsterId.substring(lastPoint+1);
	}
	
	@Override
	public String toString() {
		return "Monster " + this.name + ": Str " + this.strength + " Agg " + this.aggression
				 + "Fert " + this.fertility + " Health " + this.health;
	}

	public boolean isForBreeding() {
		return isForBreeding;
	}

	public void setForBreeding(boolean isForBreeding) {
		this.isForBreeding = isForBreeding;
	}

	public boolean isForSale() {
		return isForSale;
	}

	public void setForSale(boolean isForSale) {
		this.isForSale = isForSale;
	}

	public int getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(int salePrice) {
		this.salePrice = salePrice;
	}
}