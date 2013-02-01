package uk.ac.aber.dcs.cs221.n15.Controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.UserTransaction;

import uk.ac.aber.dcs.cs221.n15.Model.Friend;
import uk.ac.aber.dcs.cs221.n15.Model.Monster;
import uk.ac.aber.dcs.cs221.n15.Model.User;

@ManagedBean
public class UserDAO {
	
	@PersistenceContext(unitName="MonsterMash")
	/**
	 * The entity manager factor for creating entity managers
	 */
	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("MonsterMash");

	/**
	 * Checks whether a user exists
	 * 
	 * @param userId The id of the user
	 * @return If the user exists
	 */
	public boolean userExists(String userId) {
		
		EntityManager em = emf.createEntityManager();
		User u = (User) em.find(User.class, userId);
		return u==null ? false : true;
		
	}
	
	/**
	 * Checks whether a username exists
	 * 
	 * @param username The username of the user
	 * @return If the username exists
	 */
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
	/**
	 * Loads a certain users monsters
	 * 
	 * @param ownerId The id of the owner
	 * @return A list of the owned monsters
	 */
	public List<Monster> loadMonsters(String ownerId) {
		if(ownerId.charAt(3)!='.') ownerId = "loc." + ownerId;
		TypedQuery<Monster> q = (TypedQuery<Monster>) emf.createEntityManager().createNativeQuery("SELECT * FROM monsters WHERE owner = '"+ownerId+"'", Monster.class);
		List<Monster> ret = q.getResultList();
		MonsterDAO mdao = new MonsterDAO();
		mdao.ageMonsters(ret);
		return ret;
	}
	
	/**
	 * Loads a certain user monsters that are breeding
	 * 
	 * @param ownerId The id of the user
	 * @return A list of monsters that are breeding
	 */
	public List<Monster> getMonstersForBreeding(String ownerId) {
		if(ownerId.charAt(3)!='.') ownerId = "loc." + ownerId;
		TypedQuery<Monster> q = (TypedQuery<Monster>) emf.createEntityManager().createNativeQuery("SELECT * FROM monsters WHERE owner = '"+ownerId+"' AND isForBreeding = 1", Monster.class);
		List<Monster> ret = q.getResultList();
		MonsterDAO mdao = new MonsterDAO();
		mdao.ageMonsters(ret);
		return ret;
	}
	
	/**
	 * Loads a certain user monsters that are for sale
	 * 
	 * @param ownerId The id of the suer
	 * @return A list of monsters that are for sale
	 */
	public List<Monster> getMonstersForSale(String ownerId) {
		if(ownerId.charAt(3)!='.') ownerId = "loc." + ownerId;
		TypedQuery<Monster> q = (TypedQuery<Monster>) emf.createEntityManager().createNativeQuery("SELECT * FROM monsters WHERE owner = '"+ownerId+"' AND isForSale = 1", Monster.class);
		List<Monster> ret = q.getResultList();
		MonsterDAO mdao = new MonsterDAO();
		mdao.ageMonsters(ret);
		return ret;
	}
	
	/**
	 * Counts the amount of monsters a user has
	 * 
	 * @param user The user
	 * @return The amount of monsters a user has
	 */
	public int countMonsters(User user){
		Query query= emf.createEntityManager().createNativeQuery("SELECT COUNT(*) FROM monsters WHERE owner = '"+user.getId()+"'");
		Long count = (Long)(query.getSingleResult());
		return count.intValue();
	}
	
	/**
	 * Creates a username with a specified username and password
	 * 
	 * @param uname The username
	 * @param pass The password
	 * @return Whether it succeeded
	 */
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
	
	/**
	 * Authenticates a user
	 * 
	 * @param username The username
	 * @param password The password
	 * @return The user if it was successful, null otherwise
	 */
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
	
	/**
	 * Finds a user
	 * 
	 * @param id The id of the user
	 * @return The user
	 */
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
	
	/**
	 * Gets a users friends
	 * 
	 * @param u The user
	 * @return An ArrayList of the users friends
	 */
	public ArrayList<Friend> getFriends(User u){
		EntityManager em = emf.createEntityManager();
		ArrayList<Friend> friends = new ArrayList<Friend>();
		
		String flist ="";
		
		try{
		UserTransaction transaction = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
		transaction.begin();
		Query qFind = em.createNativeQuery("SELECT friends FROM users WHERE id = '"+u.getId()+"'");
		flist = (String) qFind.getSingleResult();
		transaction.commit();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		if(flist.length()==0){
			return friends;
		}
		String[] ids = flist.split(";");
		for(String id : ids){
			User f = em.find(User.class, id);
			friends.add(new Friend(f.getId(), f.getMoney(), countMonsters(f)));
		}
		return friends;
	}
	
	/**
	 * Returns true if two users are friends, i. e. when one has the other one 
	 * in his friends list. If not, returns false.
	 * 
	 * @param userIdOne The first user
	 * @param userIdTwo The second user
	 * @return If they are friends
	 */
	public boolean checkFriendship(String userIdOne, String userIdTwo){
		EntityManager em = emf.createEntityManager();
		User friend1 = em.find(User.class, userIdOne);
		if(friend1==null) return false;
		if(friend1.getFriends().contains(userIdTwo)) return true;
		return false;
	}
	
	/**
	 * Adds two people as friends
	 * 
	 * @param userIdOne The first user
	 * @param userIdTwo The second user
	 * @return If the query succeeded
	 */
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
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Updates a user
	 * 
	 * @param username The username of the user
	 * @param password The password of the user
	 * @return Whether the query succeeded
	 */
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
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Change the amount of money the user has
	 * 
	 * @param user The user
	 * @param value The amount to change it to
	 * @return Whether the query succeeded
	 */
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
			ex.printStackTrace();
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
			for(Monster m : monsters) {
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
	
	public boolean removeFriendship(String friendId1, String friendId2) {
		try {
			EntityManager em = emf.createEntityManager();
			UserTransaction transaction = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
			transaction.begin();
			User friend1 = em.find(User.class, friendId1);
			User friend2 = em.find(User.class, friendId2);
			String f1Friends = friend1.getFriends().replace(friendId2 + ";", "");
			String f2Friends = friend2.getFriends().replace(friendId1 + ";", "");
			friend1.setFriends(f1Friends);
			friend2.setFriends(f2Friends);
			em.merge(friend1);
			em.merge(friend2);
			transaction.commit();
		} catch(Exception ex) {
			return false;
		}
		return true;
	}
}
