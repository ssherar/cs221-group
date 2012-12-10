package uk.ac.aber.dcs.cs221.n15.Controller;

import javax.annotation.ManagedBean;
import javax.ejb.Stateless;
import javax.persistence.*;

import uk.ac.aber.dcs.cs221.n15.Model.*;

@ManagedBean
public class UserDAO {
	
	@PersistenceContext(unitName="MonsterMash")
	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("MonsterMash");
	
	public boolean userExists(User user) {
		/*Query q = emf.createEntityManager()
						.createNamedQuery("checkExists")
						.setParameter("username", user.getUsername())
						.setParameter("password", user.getPassword());*/
		
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
}
