package uk.ac.aber.dcs.cs221.n15.Controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import uk.ac.aber.dcs.cs221.n15.Model.Friend;
import uk.ac.aber.dcs.cs221.n15.Model.Monster;
import uk.ac.aber.dcs.cs221.n15.Model.User;

@ManagedBean
public class UserDAO {
	
	@PersistenceContext(unitName="MonsterMash")
	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("MonsterMash");

	
	public boolean userExists(String userId) {
		
		EntityManager em = emf.createEntityManager();
		User u = (User) em.find(User.class, userId);
		return u==null ? false : true;
		
	}
	
	public boolean usernameExists(String username) {
		Query q = emf.createEntityManager().createNamedQuery("usernameExists")
						.setParameter("username", username);
		if(q.getResultList().size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Monster> loadMonsters(String ownerId) {
		if(ownerId.charAt(3)!='.') ownerId = "loc." + ownerId;
		TypedQuery<Monster> q = (TypedQuery<Monster>) emf.createEntityManager().createNativeQuery("SELECT * FROM monsters WHERE owner = '"+ownerId+"'", Monster.class);
		List<Monster> ret = q.getResultList();
		MonsterDAO mdao = new MonsterDAO();
		mdao.ageMonsters(ret);
		return ret;
	}
	
	public List<Monster> getMonstersForBreeding(String ownerId) {
		if(ownerId.charAt(3)!='.') ownerId = "loc." + ownerId;
		TypedQuery<Monster> q = (TypedQuery<Monster>) emf.createEntityManager().createNativeQuery("SELECT * FROM monsters WHERE owner = '"+ownerId+"' AND isForBreeding = 1", Monster.class);
		List<Monster> ret = q.getResultList();
		MonsterDAO mdao = new MonsterDAO();
		mdao.ageMonsters(ret);
		return ret;
	}
	
	public List<Monster> getMonstersForSale(String ownerId) {
		if(ownerId.charAt(3)!='.') ownerId = "loc." + ownerId;
		TypedQuery<Monster> q = (TypedQuery<Monster>) emf.createEntityManager().createNativeQuery("SELECT * FROM monsters WHERE owner = '"+ownerId+"' AND isForSale = 1", Monster.class);
		List<Monster> ret = q.getResultList();
		MonsterDAO mdao = new MonsterDAO();
		mdao.ageMonsters(ret);
		return ret;
	}
	
	public int countMonsters(User user){
		Query query= emf.createEntityManager().createNativeQuery("SELECT COUNT(*) FROM monsters WHERE owner = '"+user.getId()+"'");
		Long count = (Long)(query.getSingleResult());
		return count.intValue();
	}
	
	
	public boolean createUser(String uname, String pass){
		try{
			EntityManager em = emf.createEntityManager();
			User user = new User(uname, pass);
			
			//Generating start monster for the user
			MonsterDAO mdao = new MonsterDAO();
			mdao.generateMonster(user.getId(), "MyFirstMonster");
			
			user.setMoney(600);
			user.setFriends("");
			
			UserTransaction transaction = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
			transaction.begin();
			em.persist(user);
			transaction.commit();
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}	
	}
	
	public User authenticateUser(String username, String password){
		try{
			EntityManager em = emf.createEntityManager();
			User u = em.find(User.class, "loc."+username);
			if(u==null || !u.getPassword().equals(password)) return null;
			return u;
		}catch(Exception ex){
			ex.printStackTrace();
		}	
		return null;
	}
	
	public User findUser(String id){
		try{
			EntityManager em = emf.createEntityManager();
			User u = em.find(User.class, id);
			return u;
		}catch(Exception ex){
			ex.printStackTrace();
		}	
		return null;
	}
	
	public User[] retrieveFriends(User user){
		EntityManager em = emf.createEntityManager();
		String[] ids = user.getFriends().split(";");
		User[] users = new User[ids.length];
		for(int i = 0; i < ids.length; i++){
			if(ids[i].length()>1) 
			users[i] = em.find(User.class, ids[i]);
		}
		return users;
	}
	
	public ArrayList<Friend> getFriends(User u){
		EntityManager em = emf.createEntityManager();
		ArrayList<Friend> friends = new ArrayList<Friend>();
		String flist = u.getFriends();
		if(flist.length()==0){
			return friends;
		}
		String[] ids = u.getFriends().split(";");
		for(String id : ids){
			User f = em.find(User.class, id);
			friends.add(new Friend(f.getId(), f.getMoney(), countMonsters(f)));
		}
		return friends;
	}
	
	/*
	 * Returns true if two users are friends, i. e. when one has the other one 
	 * in his friends list. If not, returns false.
	 */
	public boolean checkFriendship(String userIdOne, String userIdTwo){
		EntityManager em = emf.createEntityManager();
		User friend1 = em.find(User.class, userIdOne);
		if(friend1==null) return false;
		if(friend1.getFriends().contains(userIdTwo)) return true;
		return false;
	}
	
	public boolean addFriendship(String userIdOne,String userIdTwo){
		try {
			EntityManager em = emf.createEntityManager();
			UserTransaction transaction = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
			transaction.begin();
			User one = em.find(User.class, userIdOne);
			User two = em.find(User.class, userIdTwo);
			if(one==null || two==null) return false;
			
			one.setFriends(one.getFriends()+two.getId()+";");
			two.setFriends(two.getFriends()+one.getId()+";");
			transaction.commit();

		} catch(Exception ex) {
			System.out.print(ex);
			return false;
		}
		return true;
	}
	
	public boolean updateUser(String username, String password) {
		try {
			EntityManager em = emf.createEntityManager();
			UserTransaction transaction = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
			transaction.begin();
			User u = em.find(User.class, username);
			if(u == null) return false;
			u.setPassword(password);
			em.merge(u);
			transaction.commit();

		} catch(Exception ex) {
			System.out.print(ex);
			return false;
		}
		return true;
	}
	
	public boolean changeMoney(User user, int value) {
		try {
			EntityManager em = emf.createEntityManager();
			UserTransaction transaction = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
			transaction.begin();
			User u = em.find(User.class, user.getId());
			if(u == null) return false;
			u.setMoney(u.getMoney()+value);
			em.merge(u);
			transaction.commit();

		} catch(Exception ex) {
			System.out.print(ex);
			return false;
		}
		return true;
	}
	
	public List<User> getHighscores(int limit) {
		String sql = "SELECT * FROM users ORDER BY money DESC LIMIT " + limit;
		Query q = emf.createEntityManager().createNativeQuery(sql, User.class);
		if (q.getResultList().size() > 0) {
			return (List<User>)q.getResultList();
		} else {
			return new ArrayList<User>();
		}
	}

	public User reloadUser(String userId){
		EntityManager em = emf.createEntityManager();
		return em.find(User.class, userId);
	}
	
	public void deleteUser(User u) {
		String userId = u.getId();
		try {
			
			EntityManager em = emf.createEntityManager();
			MonsterDAO mdao = new MonsterDAO();
			List<Monster> monsters = this.loadMonsters(userId);
			System.out.println("Amount of monsters " + monsters.size());
			for(Monster m : monsters) {
				System.out.println(m.getId());
				mdao.wipeMonster(m.getId());
			}
			
			UserTransaction transaction = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
			List<Friend> friends = this.getFriends(u);
			for(Friend f : friends) {
				transaction.begin();
				Query qFind = em.createNativeQuery("SELECT friends FROM users WHERE id = '"+f.getId()+"'");
				String friendString = (String) qFind.getSingleResult();
				friendString = friendString.replace(userId+";", "");
				Query qUpdate = em.createNativeQuery("UPDATE users SET friends = '"+friendString +"' WHERE id = '" + f.getId() +"'");
				qUpdate.executeUpdate();
				transaction.commit();
			}
			transaction.begin();
			String sql = "DELETE FROM users WHERE id = '"+userId+"'";
			Query qDelete = em.createNativeQuery(sql);
			qDelete.executeUpdate();
			transaction.commit();
			// retrieve all monsters from the database
			// remove all requests with these monsters in
			// delete monsters
			// reove id from names
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public boolean removeFriendship(String friendId1, String friendId2){
		try{
		EntityManager em = emf.createEntityManager();
		UserTransaction transaction = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
		
		if(!checkFriendship(friendId1, friendId2)) return false;
		
		transaction.begin();
		
		Query qFind = em.createNativeQuery("SELECT friends FROM users WHERE id = '"+friendId1+"'");
		String friendString = (String) qFind.getSingleResult();
		friendString = friendString.replace(friendId2+";", "");
		Query qUpdate = em.createNativeQuery("UPDATE users SET friends = '"+friendString +"' WHERE id = '" + friendId1 +"'");
		qUpdate.executeUpdate();
		
		qFind = em.createNativeQuery("SELECT friends FROM users WHERE id = '"+friendId2+"'");
		friendString = (String) qFind.getSingleResult();
		friendString = friendString.replace(friendId1+";", "");
		qUpdate = em.createNativeQuery("UPDATE users SET friends = '"+friendString +"' WHERE id = '" + friendId2 +"'");
		qUpdate.executeUpdate();	
		
		transaction.commit();
		return true;
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}
}
