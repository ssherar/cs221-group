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
	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("MonsterMash");

	
	/**
	 * Checks if the user exists depending on a users email and password
	 * combination.
	 * @param user The user model
	 * @deprecated since v0.0.1
	 * @return true if exists, false otherwise.
	 */
	public boolean userExists(User user) {
		
		String sql = "SELECT * FROM users where users.email='"+user.getUsername()+"'";
		Query q = emf.createEntityManager().createNamedQuery("checkExists")
					.setParameter("username", user.getUsername())
					.setParameter("password", user.getPassword());
		
		if(q.getResultList().size() > 0) {
			return true;
		} else {
			return false;
		}
		
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
	
	/**
	 * Retrieves the monsters listed by a users id. It also checks if it's a
	 * local username, and if so adds our identifier loc. 
	 * @param ownerId the primary key of the database
	 * @return the list of monsters. Can be null if there is no monsters.
	 */
	@SuppressWarnings("unchecked")
	public List<Monster> loadMonsters(String ownerId) {
		if(ownerId.charAt(3)!='.') ownerId = "loc." + ownerId;
		TypedQuery<Monster> q = (TypedQuery<Monster>) emf.createEntityManager().createNativeQuery("SELECT * FROM monsters WHERE owner = '"+ownerId+"'", Monster.class);
		List<Monster> ret = q.getResultList();
		return ret;
	}
	
	/**
	 * Retrieves the amount of monsters a user has.
	 * @param user the user model
	 * @return the integer value of the count.
	 */
	public int countMonsters(User user){
		Query query= emf.createEntityManager().createNativeQuery("SELECT COUNT(*) FROM monsters WHERE owner = '"+user.getId()+"'");
		Long count = (Long)(query.getSingleResult());
		return count.intValue();
	}
	
	public void test(){
		try{
			EntityManager em = emf.createEntityManager();
			
			User kamil = em.find(User.class, "loc.kamil");
			UserTransaction transaction = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
			transaction.begin();
			kamil.setFriends("user1;user2");
			transaction.commit();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	
	/**
	 * Creates a new user.
	 * @param uname the username of the user
	 * @param pass the password of the user
	 */
	public void createUser(String uname, String pass){
		try{
			EntityManager em = emf.createEntityManager();
			User user = new User(uname, pass);
			UserTransaction transaction = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
			transaction.begin();
			em.persist(user);
			transaction.commit();
		}catch(Exception ex){
			ex.printStackTrace();
		}	
	}
	
	/**
	 * Checks if a user exists with the username and password given.
	 * @param username the username
	 * @param password the password
	 * @return the model User if it exists in the database, null otherwise.
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
	 * Updates a users record and changes the password.
	 * @param id the primary key of the record
	 * @param password the password to be changed to
	 * @return false if user doesn't exist, true otherwise.
	 */
	public boolean updateUser(String id, String password) {
		try {
			EntityManager em = emf.createEntityManager();
			UserTransaction transaction = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
			transaction.begin();
			User u = em.find(User.class, id);
			if(u == null) return false;
			u.setPassword(password);
			em.merge(u);
			transaction.commit();
			
		} catch(Exception ex) {
			return false;
		}
		return true;
	}
	
	/**
	 * Finds a user with the given id. 
	 * @param id the id of the record
	 * @return the user object if exists, null otherwise
	 */
	public User findUser(String id){
		try{
			EntityManager em = emf.createEntityManager();
			User u = em.find(User.class, id);
			if(u==null) return null;
			u.setPassword(""); //obscuring the password
			return u;
		}catch(Exception ex){
			ex.printStackTrace();
		}	
		return null;
	}
	
	/**
	 * Retrieves the serialized list of friends from the
	 * users database, and returns the user objects of each
	 * friend.
	 * @param user the user model we are using to find friends
	 * @return an array of Users.
	 */
	public User[] retrieveFriends(User user){
		EntityManager em = emf.createEntityManager();
		String[] ids = user.getFriends().split(";");
		User[] users = new User[ids.length];
		for(int i = 0; i < ids.length; i++){
			users[i] = em.find(User.class, ids[i]);
		}
		return users;
	}
	
	/**
	 * Retrieves the list of friends from the database, and
	 * populates the data into an array list.
	 * @param u
	 * @return
	 */
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
			friends.add(new Friend(f.getId(), f.getMoney(), this.countMonsters(f)));
		}

		return friends;
	}
	
	/**
	 * Checks if two users are friends.
	 * @param userIdOne the id of the first user
	 * @param userIdTwo the id of the second user
	 * @return true if they are, false otherwise
	 */
	public boolean checkFriendship(String userIdOne, String userIdTwo){
		EntityManager em = emf.createEntityManager();
		User friend1 = em.find(User.class, userIdOne);
		if(friend1==null) return false;
		if(friend1.getFriends().contains(userIdTwo)) return true;
		return false;
	}

}
