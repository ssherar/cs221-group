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
	public List<Monster> loadMonsters(String username) {
		System.out.println(username);
		TypedQuery<Monster> q = (TypedQuery<Monster>) emf.createEntityManager().createNativeQuery("SELECT * FROM monsters WHERE owner = '"+username+"'", Monster.class);
		List<Monster> ret = q.getResultList();
		return ret;
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
	
	public User findUser(String username, String password){
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
		String[] ids = u.getFriends().split(";");
		ArrayList<Friend> friends = new ArrayList<Friend>();
		for(String id : ids){
			System.out.println(id);
			User f = em.find(User.class, id);
			friends.add(new Friend(f.getId(), f.getMoney()));
		}
		
		for(Friend f : friends){
			System.out.println("Name: "+f.getName()+" Money: "+f.getMoney());
		}
		return friends;
	}
}
