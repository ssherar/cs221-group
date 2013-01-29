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

	@SuppressWarnings("unchecked")
	public List<Monster> loadMonsters(String ownerId) {
		if(ownerId.charAt(3)!='.') ownerId = "loc." + ownerId;
		TypedQuery<Monster> q = (TypedQuery<Monster>) emf.createEntityManager().createNativeQuery("SELECT * FROM monsters WHERE owner = '"+ownerId+"'", Monster.class);
		List<Monster> ret = q.getResultList();
		return ret;
	}
	
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
			if(u==null) return null;
			u.setPassword(""); //obscuring the password
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
			users[i] = em.find(User.class, ids[i]);
		}
		return users;
	}
	
	public ArrayList<Friend> getFriends(User u){
		EntityManager em = emf.createEntityManager();
		ArrayList<Friend> friends = new ArrayList<Friend>();
		String flist = u.getFriends();
		if(flist.length()==0){
			System.out.println("NO FRIENDS!");
			return friends;
		}
		String[] ids = u.getFriends().split(";");
		for(String id : ids){
			User f = em.find(User.class, id);
<<<<<<< HEAD
<<<<<<< HEAD
			friends.add(new Friend(f.getId(), f.getMoney(), countMonsters(f)));
		}
		
=======
			friends.add(new Friend(f.getId(), f.getMoney(), this.countMonsters(f)));
		}

>>>>>>> f547cf4ee7be8f87dcb432447bc6b5348bf7d011
=======
			friends.add(new Friend(f.getId(), f.getMoney(), countMonsters(f)));
		}
		
		for(Friend f : friends){
			System.out.println("Name: "+f.getName()+" Money: "+f.getMoney());
		}
>>>>>>> a0d2540713da680db872603edb533eb9d1e72b9e
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

}
