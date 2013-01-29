package uk.ac.aber.dcs.cs221.n15.Controller;

import java.util.List;

import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.UserTransaction;

import uk.ac.aber.dcs.cs221.n15.Model.Monster;
import uk.ac.aber.dcs.cs221.n15.Model.User;

public class RequestDAO {

	@PersistenceContext(unitName="MonsterMash")
	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("MonsterMash");
	private EntityManager em;
	
	/*
	 * Creates a request and persists it. If a request does not have content, 
	 * last parameter should be null.
	 */
	public void createRequest(String sourceId, String targetID, RequestType type, String content){
		em = emf.createEntityManager();
		Request r = new Request(sourceId, targetID, type);
		if(content!=null) r.setContent(content);
		try{
			UserTransaction transaction = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
			transaction.begin();
			em.persist(r);
			transaction.commit();
		}catch(Exception ex){ 
			
		}
	}
	
	/*
	 * Returns list of request of given type for given user.
	 */
	public List<Request> getRequests(User user, RequestType type){
		
		String numType = Integer.toString(type.ordinal());
		TypedQuery<Request> q = (TypedQuery<Request>) 
				emf.createEntityManager().createNativeQuery("SELECT * FROM requests WHERE (sourceId = '"+user.getId()+"' OR targetId= '"+user.getId()+"') AND type = '"+numType+"'", Request.class);
		return q.getResultList();
	}
	
}
