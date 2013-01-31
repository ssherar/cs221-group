package uk.ac.aber.dcs.cs221.n15.Controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import uk.ac.aber.dcs.cs221.n15.Model.Monster;



public class MonsterDAO {
	
	/**
	 * The value used for improving monster performance at the child stage
	 */
	private static double CHILD_PERCENT = 1.05;
	
	/**
	 * The value used for improving monster performance at the mature stage
	 */
	private static double MATURE_PERCENT = 1.02;

	@PersistenceContext(unitName="MonsterMash")
	/**
	 * The entity manager factory for creating entity managers
	 */
	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("MonsterMash");
	/**
	 * The entity manage for handling persistence
	 */
	private EntityManager em;

	/**
	 * Instantiates the MonsterDAO
	 */
	public MonsterDAO(){
	}

	/**
	 * Persists a specified monster
	 * 
	 * @param m The monster to persist
	 */
	public void persistMonster(Monster m){
		try{
			em = emf.createEntityManager();
			UserTransaction transaction = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
			transaction.begin();
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.MILLISECOND, 0);
			m.setDob(cal.getTime());

			em.persist(m);
			transaction.commit();
		}catch(Exception ex){ 
			System.out.println(ex);
		}
	}

	/**
	 * Finds a monster
	 * 
	 * @param monsterId The id of the monster to find
	 * @return The monster
	 */
	public Monster findMonster(String monsterId){
		em = emf.createEntityManager();
		Monster m = em.find(Monster.class, monsterId);
		if(m==null) System.out.println("M is NULL!");
		age(m);
		return m;
	}

	/**
	 * Renames a monster, and any recurrence of the name in the database
	 * 
	 * @param monsterID The id of the monster to rename
	 * @param newName The new name of the monster
	 */
	public void renameMonster(String monsterID, String newName){

		try{

			UserTransaction transaction = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
			transaction.begin();

			em = emf.createEntityManager();
			Monster m = em.find(Monster.class, monsterID);
			if(m==null){
				System.out.println("Did not find the monster of id :" + monsterID);
				return;
			}
			Monster nm = new Monster();
			nm.setAggression(m.getAggression());
			nm.setDob(m.getDob());
			nm.setFertility(m.getFertility());
			nm.setOwnerId(m.getOwnerId());
			nm.setHealth(m.getHealth());
			nm.setStrength(m.getStrength());
			nm.setName(newName);
			nm.setId(nm.getOwnerId()+"."+nm.getName());
			nm.setBreedPrice(m.getBreedPrice());
			nm.setSalePrice(m.getSalePrice());
			nm.setIsForBreeding(m.getIsForBreeding());
			nm.setIsForSale(m.getIsForSale());

			m.setName(newName);
			em.persist(nm);
			em.remove(m);
			transaction.commit();
			this.changeRequestMonsterId(monsterID, nm.getId());

		}catch(Exception ex){
			System.out.println(ex);
		}
	}
	
	/**
	 * Generates a random monster and persists it.
	 * (All base parameters are between 20 and 70).
	 * @param userId Owner of the new monster
	 * @param monsterName
	 */
	public Monster generateMonster(String userId, String monsterName){
		//Checking if such user exists.
		UserDAO udao = new UserDAO();
		if(udao.userExists(userId)) return null;
		
		Monster m = new Monster(monsterName, userId);
		m.setAggression(Validator.rand(20, 70));
		m.setStrength(Validator.rand(20, 70));
		m.setFertility(Validator.rand(20, 70));
		m.setHealth(100);
		m.setIsForBreeding(0);
		m.setIsForSale(0);
		m.setSalePrice(this.calculatePrize(m));
		m.setBreedPrice(m.getSalePrice()/2);
		
		this.persistMonster(m);
		return m;
	}
	
	
	/**
	 * Changes the owner of the monster, and any recurrence of that in the database
	 * 
	 * @param monsterID The id of the monster to change owner
	 * @param toUserID The user id to change to
	 */
	public void changeOwner(String monsterID, String toUserID){

		try{

			UserTransaction transaction = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
			transaction.begin();

			em = emf.createEntityManager();
			Monster m = em.find(Monster.class, monsterID);
			if(m==null){
				System.out.println("Did not find the monster of id :" + monsterID);
				return;
			}
			
			Monster nm = new Monster();
			nm.setAggression(m.getAggression());
			nm.setDob(m.getDob());
			nm.setFertility(m.getFertility());
			nm.setOwnerId(toUserID);
			nm.setHealth(m.getHealth());
			nm.setStrength(m.getStrength());
			nm.setName(m.getName());
			nm.setId(nm.getOwnerId()+"."+nm.getName());
			nm.setBreedPrice(m.getBreedPrice());
			nm.setSalePrice(m.getSalePrice());
			nm.setIsForBreeding(m.getIsForBreeding());
			nm.setIsForSale(m.getIsForSale());

			em.persist(nm);
			em.remove(m);
			transaction.commit();
			this.changeRequestMonsterId(monsterID, nm.getId());

		}catch(Exception ex){
			System.out.println(ex);
		}
	}
	
	/**
	 * Changes the breed price of a specified monster, and any recurrence of it in the database
	 * 
	 * @param mID The id of the monster to change breed price
	 * @param price The price to change to
	 */
	public void changeBreedPrice(String mID, int price) {
		try {
			UserTransaction transaction = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
			transaction.begin();
			em = emf.createEntityManager();
			Monster m = em.find(Monster.class, mID);
			if(m == null) return;
			
			m.setBreedPrice(price);
			em.merge(m);
			transaction.commit();
			
		} catch(Exception ex) {
			
		}
	}
	
	/**
	 * Changes the buy price of a specified monster, and any recurrence of it in the database
	 * 
	 * @param mID The id of the monster to change buy price
	 * @param price The price to change to
	 */
	public void changeBuyPrice(String mID, int price) {
		try {
			UserTransaction transaction = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
			transaction.begin();
			em = emf.createEntityManager();
			Monster m = em.find(Monster.class, mID);
			if(m == null) return;
			
			m.setSalePrice(price);
			em.merge(m);
			transaction.commit();
			
		} catch(Exception ex) {
			
		}
	}
	
	/**
	 * Changes the breed flag of a specified monster, and any recurrence of it in the database
	 * 
	 * @param mID The id of the monster to change breed flag
	 * @param flag The breed flag to change to
	 */
	public void changeBreedFlag(String mID, boolean flag) {
		try {
			UserTransaction transaction = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
			transaction.begin();
			em = emf.createEntityManager();
			Monster m = em.find(Monster.class, mID);
			if(m == null) return;
			
			m.setIsForBreeding((flag) ? 1 : 0);
			em.merge(m);
			transaction.commit();
			
		} catch(Exception ex) {
			
		}
	}
	
	/**
	 * Changes the sale flag of a specified monster, and any recurrence of it in the database
	 * 
	 * @param mID The id of the monster to change sale flag
	 * @param flag The sale flag to change to
	 */
	public void changeSaleFlag(String mID, boolean flag) {
		try {
			UserTransaction transaction = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
			transaction.begin();
			em = emf.createEntityManager();
			Monster m = em.find(Monster.class, mID);
			if(m == null) return;
			
			m.setIsForSale((flag) ? 1 : 0);
			em.merge(m);
			transaction.commit();
			
		} catch(Exception ex) {
			
		}
	}

	/**
	 * Ages a list of monsters
	 * 
	 * @param monsters The list of monsters to age
	 */
	public void ageMonsters(List<Monster> monsters) {
		for(Monster m : monsters) {
			this.age(m);
		}
	}

	/**
	 * Ages a specified monster, young monsters grow fast in all strength, aggression and fertility,
	 * mature monsters grow slowly in strength and aggression, and elderly monsters lose health as they age
	 * 
	 * @param monster
	 */
	public void age(Monster monster) {
		Date date = new Date();
		Date bornDate = monster.getDob();
		int daysOld = this.calculateDaysDifference(bornDate, date);
		int baseStr, baseAgg, baseFert, baseHealth;
		double ciStr, ciAgg, ciFert, ciHealth;

		baseStr = monster.getStrength();
		baseAgg = monster.getAggression();
		baseFert = monster.getFertility();
		baseHealth = monster.getHealth();

		ciStr = baseHealth;
		ciAgg = baseAgg;
		ciFert = baseFert;
		ciHealth = baseHealth;

		int youngAge = (daysOld > 5) ? 5 : daysOld;

		ciStr = baseStr * Math.pow(CHILD_PERCENT, youngAge);
		ciAgg = baseAgg * Math.pow(CHILD_PERCENT, youngAge);
		ciFert = baseFert * Math.pow(CHILD_PERCENT, youngAge);

		if(daysOld > 5) {
			int matureAge = ((daysOld > 12) ? 12 : daysOld) - 5;
			ciStr = ciStr * Math.pow(MATURE_PERCENT, matureAge);
			ciAgg = ciAgg * Math.pow(MATURE_PERCENT, matureAge);
		}

		if(daysOld > 12) {
			int elderlyAge = daysOld - 12;
			for(int i = 0; i < elderlyAge; i++) {
				double avg = (baseStr + baseAgg + baseFert) / 3;
				ciHealth -= 15.00 - (avg / 7.00);
			}
		}

		monster.setAggression((int) ciAgg);
		monster.setFertility((int) ciFert);
		monster.setHealth((int) ciHealth);
		monster.setStrength((int) ciStr);
	}

	/*public int calculateDaysDifference(Date start, Date end) {
		Calendar startCal = new GregorianCalendar();
		Calendar endCal = new GregorianCalendar();

		startCal.setTime(start);
		endCal.setTime(end);

		endCal.add(Calendar.YEAR, -startCal.get(Calendar.YEAR));
		endCal.add(Calendar.MONTH, -startCal.get(Calendar.MONTH));
		endCal.add(Calendar.DATE, -startCal.get(Calendar.DATE));

		return endCal.get(Calendar.DAY_OF_YEAR);
	}*/
	
	/**
	 * Calculates the difference in days between 2 dates
	 * 
	 * @param start The first date
	 * @param end The second date
	 * 
	 * @return The difference in days
	 */
	public int calculateDaysDifference(Date start, Date end) {
		long mStart = start.getTime();
		long mEnd = end.getTime();
		long difference = mEnd - mStart;
		double retVal = (difference / (1000 * 60 * 60 * 24));
		System.out.println(retVal);
		return (int) (difference / (1000 * 60 * 60 * 24));
	}

	/**
	 * Calculates the difference in days between a date and right now
	 * 
	 * @param start The first date
	 * @return The difference in days
	 */
	public int calculateDaysDifference(Date start) {
		return calculateDaysDifference(start, new Date());
	}

	/**
	 * Fights two specified monsters
	 * 
	 * @param monster1 The first monster to fight
	 * @param monster2 The second monster to fight
	 * @return The monster that wins
	 */
	// TODO: refactor
	public Monster fight(Monster monster1, Monster monster2) {
		int monsterOne;
		int monsterTwo;

		do{		
			int m1hp = monster1.getHealth() / 20;
			int m2hp = monster2.getHealth() / 20;

			int m1Strength = ((monster1.getStrength() * monster1.getAggression()) / 1000);
			int m2Strength = ((monster2.getStrength() * monster2.getAggression()) / 1000);

			int m1luck = Validator.rand(1, 10);
			int m2luck = Validator.rand(1, 10);

			monsterOne = (m1Strength + m1hp + m1luck);
			monsterTwo = (m2Strength + m2hp + m2luck);
			System.out.println("" + monsterOne + " : " + monsterTwo);
		}while(monsterOne==monsterTwo);

		if(monsterOne > monsterTwo ) {
			monster1.setHealth((int) (monster1.getHealth() * (1- (monsterTwo / 100.00) )));
			return monster1;
		} else {
			monster2.setHealth((int) (monster2.getHealth() * (1- (monsterOne / 100.00) )));
			return monster2;
		}

	}

	/**
	 * Calculates the price of a specified monster
	 * 
	 * @param monster The monster to calculate the prize of
	 * @return A sum of the monster stats
	 */
	public int calculatePrize(Monster monster) {
		return (monster.getAggression() + monster.getFertility() + monster.getHealth() + monster.getStrength());
	}

	/**
	 * When a monster dies, it needs to be removed from the database,
	 * and that is what the method does.
	 * 
	 * @param The monster to wipe
	 */
	public void wipeMonster(String monsterId){
		try{
			em = emf.createEntityManager();
			UserTransaction transaction = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
			transaction.begin();
			String q = "DELETE FROM requests WHERE (sourceId = '"+monsterId+"' OR targetId = '"+monsterId+"') AND type !='6' ";
			Query query= emf.createEntityManager().createNativeQuery(q);
			query.executeUpdate();
			q = "DELETE FROM monsters WHERE id = '"+monsterId+"'" ;
			query= emf.createEntityManager().createNativeQuery(q);
			query.executeUpdate();
			transaction.commit();
		}catch(Exception ex){ 
			ex.printStackTrace();
		}
	}

	/**
	 * Updates the health of a specified monster
	 * 
	 * @param monsterId The id of the monster to update the health of
	 * @param health The new health
	 */
	public void updateHealth(String monsterId, int health){
		try{
			em = emf.createEntityManager();
			UserTransaction transaction = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
			transaction.begin();
			Monster m = em.find(Monster.class, monsterId);
			m.setHealth(health);
			transaction.commit();
		}catch(Exception ex){ 

		}
	}


	/**
	 * Breeds two monsters
	 * 
	 * @param monster1 The first monster to breed
	 * @param monster2 The second monster to breed
	 * @return A list of the child monsters
	 */
	public List<Monster> breed(Monster monster1, Monster monster2) {
		List<Monster> children = new ArrayList<Monster>();
		int mOneFert = monster1.getFertility();
		int mTwoFert = monster2.getFertility();
		int avgFert = (mOneFert + mTwoFert) / 2;
		int noChildren = (Validator.rand(20, avgFert)/20);
		int minStr = Math.min(monster1.getStrength(), monster2.getStrength()), maxStr = Math.max(monster1.getStrength(), monster2.getStrength());
		int minFert = Math.min(monster1.getFertility(), monster2.getFertility()), maxFert = Math.max(monster2.getFertility(), monster1.getFertility());
		int minAgg = Math.min(monster1.getAggression(), monster2.getAggression()), maxAgg = Math.max(monster2.getAggression(), monster1.getAggression());
		maxStr = Math.min(70, maxStr);
		maxAgg = Math.min(70, maxAgg);
		maxFert = Math.min(70, maxFert);
		
		String name = "M"+Validator.rand(0, 1000);
		
		for(int i = 0; i < noChildren; i++) {
			int strChance, aggChance, fertChance;
			Monster child = new Monster();
			
			child.setName(name+"_"+i);
			strChance = Validator.rand(1,4);
			aggChance = Validator.rand(1,4);
			fertChance = Validator.rand(1,4);
			System.out.println(strChance + " " + aggChance + " " + fertChance);
			switch(strChance) {
			case 1:
				child.setStrength(minStr);
				break;
			case 2:
				child.setStrength(maxStr);
				break;
			case 3:
				child.setStrength(Validator.rand(20, 70));
				break;
			}

			switch(aggChance) {
			case 1:
				child.setAggression(minAgg);
				break;
			case 2:
				child.setAggression(maxAgg);
				break;
			case 3:
				child.setAggression(Validator.rand(20, 70));
				break;
			}

			switch(fertChance) {
			case 1:
				child.setFertility(minFert);
				break;
			case 2:
				child.setFertility(maxFert);
				break;
			case 3:
				child.setFertility(Validator.rand(20, 70));
				break;
			}

			child.setHealth(100);
			child.setOwnerId(monster1.getOwnerId());
			child.setId(child.getOwnerId()+"."+child.getName());
			children.add(child);
		}
		return children;
	}
	
	/**
	 * Changes the id of any requests to a new id
	 * 
	 * @param fromID The old monsters id
	 * @param toID The new monsters is
	 */
	public void changeRequestMonsterId(String fromID, String toID) {
		EntityManager em = emf.createEntityManager();
		Query qSource = em.createNativeQuery("UPDATE requests SET sourceId = '"+toID+"' where sourceId = '"+fromID+"'");
		Query qTarget = em.createNamedQuery("UPDATE requests SET targetId = '"+toID+"' where targetId = '"+fromID+"'");
		qSource.executeUpdate();
		qTarget.executeUpdate();
	}
}

