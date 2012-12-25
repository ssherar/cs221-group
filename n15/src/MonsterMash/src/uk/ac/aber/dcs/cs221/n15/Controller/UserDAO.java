package uk.ac.aber.dcs.cs221.n15.Controller;

import java.util.logging.Logger; 
import java.util.List;

import javax.annotation.ManagedBean;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.*;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import uk.ac.aber.dcs.cs221.n15.Model.*;

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
	
	public void insert(String username, String password) {
		String sql = "INSERT INTO users VALUES (null, '"+username+"' '"+password+"')";
		System.out.println(sql);
		try {
			UserTransaction transaction = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction") ;
			transaction.begin();
			Query q = emf.createEntityManager().createNativeQuery(sql);
			emf.createEntityManager().getTransaction().begin();
			q.executeUpdate();
			emf.createEntityManager().getTransaction().commit();
			transaction.commit();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HeuristicMixedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HeuristicRollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Monster> loadMonsters(String username) {
		System.out.println(username);
		//TypedQuery<Monster> q = (TypedQuery<Monster>) emf.createEntityManager().createNativeQuery("SELECT * FROM monsters WHERE owner_id = (SELECT id FROM users WHERE email='"+username+"')", Monster.class);
		//List<Monster> ret = q.getResultList();
		return null;
	}
	
	public void test(){
		try{
			EntityManager em = emf.createEntityManager();
			User testUser = new User("kamil", "test");
			UserTransaction transaction = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
			transaction.begin();
			em.persist(testUser);
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
}
