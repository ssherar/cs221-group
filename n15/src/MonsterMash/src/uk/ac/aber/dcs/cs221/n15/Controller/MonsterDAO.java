package uk.ac.aber.dcs.cs221.n15.Controller;

import java.util.Date;
import java.util.Random;

import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.UserTransaction;

import uk.ac.aber.dcs.cs221.n15.Model.Monster;



public class MonsterDAO {

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
	
	public void age(String monsterID) {
		Date now = new Date();
		Random r = new Random();
		int strength, aggression, fertility, health, scalar;
		long ageMilliseconds;
		boolean edited = false;
		TypedQuery<Monster> q = (TypedQuery<Monster>) emf.createEntityManager().createNativeQuery("SELECT * FROM monsters WHERE id = '"+monsterID+"' LIMIT 1", Monster.class);
		Monster monster = q.getSingleResult();
		
		ageMilliseconds = now.getTime() - monster.getDob().getTime();
		scalar = (int) (ageMilliseconds / 20000000);
		
		strength = r.nextInt() * scalar;
		aggression = r.nextInt() * scalar;
		fertility = r.nextInt() * scalar;
		health = r.nextInt() * scalar;
		
		if(ageMilliseconds < 432000000) { //TODO: Fix without milliseconds
			scalar = (int) (ageMilliseconds / 20000000);
			strength = r.nextInt() * scalar;
			aggression = r.nextInt() * scalar;
			fertility = r.nextInt() * scalar;
			health = r.nextInt() * scalar;
			
			monster.setStrength(monster.getStrength() + strength);
			monster.setAggression(monster.getAggression() + aggression);
			monster.setFertility(monster.getFertility() + fertility);
			edited = true;
			
		} else if(ageMilliseconds > 1036800000) {
			scalar = -4;
			strength = r.nextInt() * scalar;
			aggression = r.nextInt() * scalar;
			fertility = r.nextInt() * scalar;
			health = r.nextInt() * scalar;
			
			monster.setStrength(monster.getStrength() + strength);
			monster.setAggression(monster.getAggression() + aggression);
			monster.setFertility(monster.getFertility() + fertility);
			monster.setHealth(monster.getHealth() + health);
			edited = true;
		}
		
		if(edited) {
			try {
				UserTransaction transaction = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
				transaction.begin();
				em.persist(monster);
				transaction.commit();
			} catch(Exception ex) {
				
			}
		}
	}
}
