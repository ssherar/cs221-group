package uk.ac.aber.dcs.cs221.n15.Controller;

import java.util.Date;
import java.util.Random;
import java.util.logging.Logger;

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
		em = emf.createEntityManager();
	}
	
	public void createMonster(String name, String ownerId){
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
	
	public void age(String monsterID) {
		Date now = new Date();
		long age;
		TypedQuery<Monster> q = (TypedQuery<Monster>) emf.createEntityManager().createNativeQuery("SELECT * FROM monsters WHERE id = '"+monsterID+"' LIMIT 1", Monster.class);
		Monster monster = q.getSingleResult();
		age = now.getTime() - monster.getDob().getTime();
	}
}
