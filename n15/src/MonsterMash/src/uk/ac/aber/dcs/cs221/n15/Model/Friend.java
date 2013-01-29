package uk.ac.aber.dcs.cs221.n15.Model;

public class Friend {

	private String id;
	private String name;
	private int numberOfMonsters;
	private int money;
	
	public Friend(){
		
	}
	public Friend(String id, int money, int nOfMonsters){
		this.id = id;
		this.name = id.substring(4);
		numberOfMonsters = nOfMonsters;
		this.money = money;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumberOfMonsters() {
		return numberOfMonsters;
	}

	public void setNumberOfMonsters(int numberOfMonsters) {
		this.numberOfMonsters = numberOfMonsters;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}
}
