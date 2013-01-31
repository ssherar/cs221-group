package uk.ac.aber.dcs.cs221.n15.Model;

public class Friend implements Comparable{

	/**
	 * The id of the friend
	 */
	private String id;
	
	/**
	 * The name of the friend
	 */
	private String name;
	
	/**
	 * The number of monsters the friend has
	 */
	private int numberOfMonsters;
	
	/**
	 * The amount of money the friend has
	 */
	private int money;
	
	/**
	 * Instantiates the Friend
	 */
	public Friend(){
		
	}
	
	/**
	 * Instantiates the Friend
	 * 
	 * @param id The id of the friend
	 * @param money The amount of money the friend has
	 * @param nOfMonsters The number of monsters the friend has
	 */
	public Friend(String id, int money, int nOfMonsters){
		this.id = id;
		this.name = id.substring(4);
		numberOfMonsters = nOfMonsters;
		this.money = money;
	}

	/**
	 * Gets the id of the friend
	 * 
	 * @return The id of the friend
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id of the friend
	 * 
	 * @param id The id of the friend
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the name of the friend
	 * 
	 * @return The name of the friend
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the friend
	 * 
	 * @param name The name of the friend
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the number of monsters the friend has
	 * 
	 * @return The number of monsters the friend has
	 */
	public int getNumberOfMonsters() {
		return numberOfMonsters;
	}

	/**
	 * Sets the number of monsters the friend has
	 * 
	 * @param numberOfMonsters The number of monsters the friend has
	 */
	public void setNumberOfMonsters(int numberOfMonsters) {
		this.numberOfMonsters = numberOfMonsters;
	}

	/**
	 * Gets the amount of money the friend has
	 * 
	 * @return The amount of money the friend has
	 */
	public int getMoney() {
		return money;
	}

	/**
	 * Sets the amount of money the friend has
	 * 
	 * @param money The amount of money the friend has
	 */
	public void setMoney(int money) {
		this.money = money;
	}

	@Override
	public int compareTo(Object arg0) {
		Friend other = (Friend) arg0;
		if(this.numberOfMonsters<other.getNumberOfMonsters()) return -1;
		if(this.numberOfMonsters==other.getNumberOfMonsters()) return 0;
		return 1;
	}
}
