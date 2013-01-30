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
	
	public void persistMonster(Monster m){
		try{
			em = emf.createEntityManager();
			UserTransaction transaction = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
			transaction.begin();
			Date dob = m.getDob();
			Calendar cal = Calendar.getInstance();
			cal.setTime(dob);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.MILLISECOND, 0);
			m.setDob(cal.getTime());
			
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
		
		ciStr = baseHealth;
		ciAgg = baseAgg;
		ciFert = baseFert;
		ciHealth = baseHealth;
		
		int youngAge = (daysOld > 5) ? 5 : daysOld;
		
		ciStr = baseStr * Math.pow(this.CHILD_PERCENT, youngAge);
		ciAgg = baseAgg * Math.pow(this.CHILD_PERCENT, youngAge);
		ciFert = baseFert * Math.pow(this.CHILD_PERCENT, youngAge);
		
		if(daysOld > 5) {
			int matureAge = ((daysOld > 12) ? 12 : daysOld) - 5;
			ciStr = ciStr * Math.pow(this.MATURE_PERCENT, matureAge);
			ciAgg = ciAgg * Math.pow(this.MATURE_PERCENT, matureAge);
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
	
	public int calculateDaysDifference(Date start) {
		return calculateDaysDifference(start, new Date());
	}
	
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
	
	public int calculatePrize(Monster monster) {
		return (monster.getAggression() + monster.getFertility() + monster.getHealth() + monster.getStrength());
	}
}
