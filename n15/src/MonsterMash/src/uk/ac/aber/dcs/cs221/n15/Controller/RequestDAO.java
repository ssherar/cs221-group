package uk.ac.aber.dcs.cs221.n15.Controller;

import java.util.List;

import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.UserTransaction;

import uk.ac.aber.dcs.cs221.n15.Model.User;

public class RequestDAO {

	@PersistenceContext(unitName="MonsterMash")
	/**
	 * The entity manager factory for creating entity managers
	 */
	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("MonsterMash");
	/**
	 * The entity manager for handling persistence
	 */
	private EntityManager em;
	
	/**
	 * Creates a request and persists it. If a request does not have content, 
	 * last parameter should be null.
	 * 
	 * @param sourceId The user that sent the request
	 * @param targetID The user to receive the request
	 * @param type The type of the request
	 * @param content The content of the request
	 */
	public void createRequest(String sourceId, String targetID, RequestType type, String content){
		em = emf.createEntityManager();
		Request r = new Request(sourceId, targetID, type);
		r.setSeen(0);
		if(content!=null) r.setContent(content);
		try{
			UserTransaction transaction = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
			transaction.begin();
			em.persist(r);
			transaction.commit();
		}catch(Exception ex){ 
			
		}
	}
	
	/**
	 * Persists a request to the database
	 * 
	 * @param r The request to persist
	 */
	public void persistRequest(Request r){
		em = emf.createEntityManager();
		try{
			UserTransaction transaction = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
			transaction.begin();
			em.persist(r);
			transaction.commit();
		}catch(Exception ex){ 
			ex.printStackTrace();
		}
	}
	
	/**
	 * Returns list of request of given type for given user.
	 * 
	 * @param user The user to get requests for
	 * @param type The type of requests to retrieve
	 * @return A list of requests of given type for given user
	 */
	public List<Request> getRequests(User user, RequestType type){
		
		int n = type.ordinal();
		if(n >2){
			//Where monster id is stored, instead of user id
			TypedQuery<Request> q = (TypedQuery<Request>) 
					emf.createEntityManager().createNativeQuery("SELECT * FROM requests WHERE (sourceId like '"+user.getId()+".%' OR targetId like '"+user.getId()+".%') AND type = '"+n+"'", Request.class);
			return q.getResultList();
		}else{
			TypedQuery<Request> q = (TypedQuery<Request>) 
					emf.createEntityManager().createNativeQuery("SELECT * FROM requests WHERE (sourceId = '"+user.getId()+"' OR targetId= '"+user.getId()+"') AND type = '"+n+"'", Request.class);
			return q.getResultList();
		}
		
		
	}
	
	/**
	 * Updates a the type of a current request
	 * 
	 * @param requestId The id of the current request
	 * @param type The new type of the request
	 */
	public void updateRequestType(int requestId, RequestType type){
		try{
			em = emf.createEntityManager();
			UserTransaction transaction = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
			transaction.begin();
			Request r = em.find(Request.class, requestId);
			r.setType(type);
			transaction.commit();
		}catch(Exception ex){ 
			
		}
	}
	
	/**
	 * Updates the type of a current request, and content
	 * 
	 * @param requestId The id of the current request
	 * @param type The new type of the request
	 * @param cont The new content of the request
	 */
	public void updateRequestType(int requestId, RequestType type, String cont){
		try{
			em = emf.createEntityManager();
			UserTransaction transaction = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
			transaction.begin();
			Request r = em.find(Request.class, requestId);
			r.setType(type);
			r.setContent(cont);
			transaction.commit();
		}catch(Exception ex){ 
			
		}
	}
	
	/**
	 * Deletes a request from the database
	 * 
	 * @param requestId The id of the request to delete
	 */
	public void deleteRequest(int requestId){
		try{
			em = emf.createEntityManager();
			UserTransaction transaction = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
			transaction.begin();
			Request r = em.find(Request.class, requestId);
			em.remove(r);
			transaction.commit();
		}catch(Exception ex){ 
			
		}
	}
	
	/**
	 * Gets a request with a specified id
	 * 
	 * @param requestId The id of the request to get
	 * @return The request with given id
	 */
	public Request getRequest(int requestId){
		try{
			em = emf.createEntityManager();
			Request r = em.find(Request.class, requestId);
			return r;
		}catch(Exception ex){ 
			ex.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Changes if a request has been seen
	 * 
	 * @param requestId The id of the request to change
	 * @param value The value to set seen to
	 * @return If the process was successful
	 */
	public boolean changeSeen(int requestId, int value){
		try{
			em = emf.createEntityManager();
			UserTransaction transaction = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
			transaction.begin();
			Request r = em.find(Request.class, requestId);
			r.setSeen(value);
			transaction.commit();
			return true;
		}catch(Exception ex){ 
			ex.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Checks if a request exists
	 * 
	 * @param sourceId The user sending the request
	 * @param targetId The user receiving the request
	 * @param type The type of the request
	 * @return If the request exists
	 */
	public boolean requestExists(String sourceId, String targetId, RequestType type){
		String q = "SELECT COUNT(*) FROM requests WHERE sourceId = '"+sourceId+"' AND targetId = '"+targetId+"' AND type = "+type.ordinal();
		Query query= emf.createEntityManager().createNativeQuery(q);
		Long count = (Long)(query.getSingleResult());
		return count==0? false :true;
	}
	
	public boolean requestExists(String sourceId, RequestType type) {
		String q = "SELECT COUNT(*) FROM requests WHERE (sourceId = '" + sourceId + "' OR targetId = '" + sourceId + "') AND type = " + type.ordinal();
		System.out.println(q);
		Query query = emf.createEntityManager().createNativeQuery(q);
		long count = (Long)(query.getSingleResult());
		return (count == 0) ? false : true;
	}
}
