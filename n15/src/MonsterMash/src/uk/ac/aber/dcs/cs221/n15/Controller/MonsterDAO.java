package uk.ac.aber.dcs.cs221.n15.Controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.math.*;

import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.UserTransaction;

import uk.ac.aber.dcs.cs221.n15.Model.Monster;



public class MonsterDAO {
	private static double CHILD_PERCENT = 1.05;
	private static double MATURE_PERCENT = 1.02;
	
	@PersistenceContext(unitName="MonsterMash")
	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("MonsterMash");
	private EntityManager em;
	
	public MonsterDAO(){
	}
	
	public void createMonster(String name, String ownerId){
		em = emf.createEntityManager();
		Random r = new Random();
		Monster m = new Monster("" +r.nextInt(), ownerId);
		m.setDob(new Date());
		m.setAggression(r.nextInt(100)+1);
		m.setStrength(r.nextInt(100)+1);
		m.setFertility(r.nextInt(100)+1);
		m.setHealth(r.nextInt(100)+1);
		m.setGender(r.nextBoolean() ? 'M' : 'F');

		try{
			UserTransaction transaction = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
			transaction.begin();
			em.persist(m);
			transaction.commit();
		}catch(Exception ex){ 
			
		}
		
	}
	
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
			nm.setColor(m.getColor());
			nm.setDob(m.getDob());
			nm.setFertility(m.getFertility());
			nm.setOwnerId(m.getOwnerId());
			nm.setGender(m.getGender());
			nm.setHealth(m.getHealth());
			nm.setStrength(m.getStrength());
			nm.setName(newName);
			nm.setId(nm.getOwnerId()+"."+nm.getName());
			
			
			m.setName(newName);
			em.persist(nm);
			em.remove(m);
			transaction.commit();
			
		}catch(Exception ex){
			System.out.println(ex);
		}
	}
	
//	public void age(Monster monster) {
//		Date now = new Date();
//		Random r = new Random();
//		int strength, aggression, fertility, health, scalar;
//		long ageMilliseconds;
//		boolean edited = false;
//		//TypedQuery<Monster> q = (TypedQuery<Monster>) emf.createEntityManager().createNativeQuery("SELECT * FROM monsters WHERE id = '"+monsterID+"' LIMIT 1", Monster.class);
//		//Monster monster = q.getSingleResult();
//		
//		ageMilliseconds = now.getTime() - monster.getDob().getTime();
//		
//		if(ageMilliseconds < 432000000) { //TODO: Fix without milliseconds
//			scalar = (int) (ageMilliseconds / 20000000);
//			strength = r.nextInt() * scalar;
//			aggression = r.nextInt() * scalar;
//			fertility = r.nextInt() * scalar;
//			
//			monster.setStrength(monster.getStrength() + strength);
//			monster.setAggression(monster.getAggression() + aggression);
//			monster.setFertility(monster.getFertility() + fertility);
//			edited = true;
//			
//		} else if(ageMilliseconds > 1036800000) {
//			scalar = -4;
//			strength = r.nextInt() * scalar;
//			aggression = r.nextInt() * scalar;
//			fertility = r.nextInt() * scalar;
//			health = r.nextInt() * scalar;
//			
//			monster.setStrength(monster.getStrength() + strength);
//			monster.setAggression(monster.getAggression() + aggression);
//			monster.setFertility(monster.getFertility() + fertility);
//			monster.setHealth(monster.getHealth() + health);
//			edited = true;
//		}
//		
//		if(edited) {
//			try {
//				UserTransaction transaction = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
//				transaction.begin();
//				em.persist(monster);
//				transaction.commit();
//			} catch(Exception ex) {
//				
//			}
//		}
//	}
	
	public void ageMonsters(List<Monster> monsters) {
		for(Monster m : monsters) {
			this.age(m);
		}
	}
	
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
		
		ciStr = monster.getStrength();
		ciAgg = monster.getAggression();
		ciFert = monster.getFertility();
		ciHealth = monster.getHealth();
		
		if(daysOld <= 5) {
			ciStr = baseStr * Math.pow(this.CHILD_PERCENT, daysOld);
			ciAgg = baseAgg * Math.pow(this.CHILD_PERCENT, daysOld);
			ciFert = baseFert * Math.pow(this.CHILD_PERCENT, daysOld);
		}
		
		if(daysOld <= 12 && daysOld > 5) {
			int matureAge = daysOld - 5;
			ciStr += baseStr * Math.pow(this.MATURE_PERCENT, matureAge);
			ciAgg += baseAgg * Math.pow(this.MATURE_PERCENT, matureAge);
		}
		
		if(daysOld > 12) {
			
		}
	}
	
	public int calculateDaysDifference(Date start, Date end) {
		Calendar startCal = new GregorianCalendar();
		Calendar endCal = new GregorianCalendar();

		startCal.setTime(start);
		endCal.setTime(end);
		
		endCal.add(Calendar.YEAR, -startCal.get(Calendar.YEAR));
		endCal.add(Calendar.MONTH, -startCal.get(Calendar.MONTH));
		endCal.add(Calendar.DATE, -startCal.get(Calendar.DATE));
		
		return endCal.get(Calendar.DAY_OF_YEAR);
	}
	
}
