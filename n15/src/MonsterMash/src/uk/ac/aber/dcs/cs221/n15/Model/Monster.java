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
	/**
	 * The id of the monster
	 */
	private String id;

	@Column(name="aggression")
	/**
	 * The aggression of the monster
	 */
	private int aggression;

    @Temporal( TemporalType.TIMESTAMP)
    @Column(name="dob")
    /**
     * The date of birth of the monster
     */
	private Date dob;

    @Column(name="fertility")
    /**
     * The fertility of the monster
     */
	private int fertility;

    @Column(name="health")
    /**
     * The health of the monster
     */
	private int health;

	@Column(name="name")
	/**
	 * The name of the monster
	 */
	private String name;
	
	@Column(name="strength")
	/**
	 * The strength of the monster
	 */
	private int strength;
	
	@Column(name="breedPrice")
	/**
	 * The breed price of the monster
	 */
	private int breedPrice;
	
	@Column(name="isForBreeding")
	/**
	 * If the monster is for breeding
	 */
	private int isForBreeding;
	
	@Column(name="isForSale")
	/**
	 * If the monster is for sale
	 */
	private int isForSale;
	
	@Column(name="salePrice")
	/**
	 * The sale price of the monster
	 */
	private int salePrice;

	/*//bi-directional many-to-one association to User
    @ManyToOne
	@JoinColumn(name="owner_id")
	private User user;*/

	@Column(name="owner")
	/**
	 * The owner id of the monster
	 */
	private String ownerId;
	
	/**
	 * Instantiates a new monster
	 */
	public Monster() {
    }
	
	/**
	 * Instantiates a new monster
	 * 
	 * @param name The name of the monster
	 * @param ownerId The ownerId of the monster
	 */
	public Monster(String name, String ownerId) {
		this.name = name;
		this.ownerId = ownerId;
		this.id = ownerId + "." + name;
	}

	/**
	 * Gets the monsters ID
	 * 
	 * @return The monsters ID
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Sets the monsters ID
	 * 
	 * @param id The monsters ID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the monsters aggression
	 * 
	 * @return The monsters aggression
	 */
	public int getAggression() {
		return this.aggression;
	}

	/**
	 * Sets the monsters aggression. It also checks
	 * if the value passed in is between the range
	 * 0 to 100
	 * 
	 * @param aggression The monsters aggression
	 */
	public void setAggression(int aggression) {
		if(aggression > 100) {
			aggression = 100;
		} else if(aggression < 0) {
			aggression = 0;
		}
		this.aggression = aggression;
	}

	/**
	 * Gets the monsters date of birth
	 * 
	 * @return The monsters date of birth
	 */
	public Date getDob() {
		return this.dob;
	}

	/**
	 * Sets the monsters date of birth
	 * 
	 * @param dob The monsters date of birth
	 */
	public void setDob(Date dob) {
		this.dob = dob;
	}

	/**
	 * Gets the monsters fertility
	 * 
	 * @return The monsters fertility
	 */
	public int getFertility() {
		return this.fertility;
	}

	/**
	 * Sets the monsters fertility
	 * 
	 * @param fertility The monsters fertility
	 */
	public void setFertility(int fertility) {
		if(fertility > 100) {
			fertility = 100;
		} else if(fertility < 0) {
			fertility = 0;
		}
		this.fertility = fertility;
	}

	/**
	 * Gets the monsters health
	 * 
	 * @return The monsters health
	 */
	public int getHealth() {
		return this.health;
	}

	/**
	 * Sets the monsters health. It also checks
	 * if the value passed in is between the range
	 * 0 to 100
	 * 
	 * @param health The monsters health
	 */
	public void setHealth(int health) {
		if(health > 100) {
			health = 100;
		} else if(health < 0) {
			health = 0;
		}
		this.health = health;
	}

	/**
	 * Gets the monsters name
	 * 
	 * @return The monsters name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the monsters name
	 * 
	 * @param monsterName The monsters name
	 */
	public void setName(String monsterName) {
		this.name = monsterName;
	}
	
	/**
	 * Gets the monsters strength
	 * 
	 * @return The monsters strength
	 */
	public int getStrength() {
		return this.strength;
	}

	/**
	 * Sets the monsters strength. It also checks
	 * if the value passed in is between the range
	 * 0 to 100
	 * 
	 * @param strength The monsters strength
	 */
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
	
	/**
	 * Gets the owners ID
	 * 
	 * @return The owners ID
	 */
	public String getOwnerId() {
			return ownerId;
	}

	/**
	 * Sets the monsters owner ID
	 * 
	 * @param ownerId The monsters owners ID
	 */
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	
	/**
	 * Gets the monsters breed price
	 * 
	 * @return The monsters breed price
	 */
	public int getBreedPrice() {
		return breedPrice;
	}
	
	/**
	 * Sets the monsters breed price
	 * 
	 * @param breedPrice The monsters breed price
	 */
	public void setBreedPrice(int breedPrice) {
		this.breedPrice = breedPrice;
	}
	
	/**
	 * Gets the monsters sale price
	 * 
	 * @return The monsters sale price
	 */
	public int getSalePrice() {
		return salePrice;
	}

	/**
	 * Sets the monsters sale price
	 * 
	 * @param salePrice The monsters sale price
	 */
	public void setSalePrice(int salePrice) {
		this.salePrice = salePrice;
	}

	/**
	 * Gets the monsters breeding availability
	 * 
	 * @return The monsters breeding availability
	 */
	public int getIsForBreeding() {
		return isForBreeding;
	}
	
	/**
	 * Sets the monsters breeding availability
	 * 
	 * @param isForBreeding The monsters breeding availability
	 */
	public void setIsForBreeding(int isForBreeding) {
		this.isForBreeding = isForBreeding;
	}
	
	/**
	 * Checks if the monster is available for breeding
	 * 
	 * @return If the monster is available for breeding (i.e. getIsForBreeding() == 1)
	 */
	public boolean isForBreeding() {
		return (this.isForBreeding == 1);
	}

	/**
	 * Gets the monsters sale availability
	 * 
	 * @return The monsters sale availability
	 */
	public int getIsForSale() {
		return isForSale;
	}

	/**
	 * Sets the monsters sale availability
	 * 
	 * @param isForSale The monsters sale availability
	 */
	public void setIsForSale(int isForSale) {
		this.isForSale = isForSale;
	}
	
	/**
	 * Checks if the monster is available for sale
	 * 
	 * @return If the monster is available for sale (i.e. getIsForSale() == 1)
	 */
	public boolean isForSale() {
		return (this.isForSale == 1);
	}
	
	/**
	 * A static method to get a monsters name from it's ID
	 * 
	 * @param monsterId The ID of the monster
	 * @return The name of the monster
	 */
	public static String parseNameFromId(String monsterId){
		int lastPoint = monsterId.lastIndexOf(".");
		return monsterId.substring(lastPoint+1);
	}
	
	@Override
	public String toString() {
		return "Monster " + this.name + ": Str " + this.strength + " Agg " + this.aggression
				 + "Fert " + this.fertility + " Health " + this.health;
	}


}