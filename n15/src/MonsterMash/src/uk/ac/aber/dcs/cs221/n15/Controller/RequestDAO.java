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
	
	public void persistRequest(Request r){
		em = emf.createEntityManager();
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
		
		int n = type.ordinal();
		if(n >2 && n<9){
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
	
	public Request getRequest(int requestId){
		try{
			em = emf.createEntityManager();
			Request r = em.find(Request.class, requestId);
			return r;
		}catch(Exception ex){ 
			System.out.print(ex);
			return null;
		}
	}
	
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
			System.out.print(ex);
			return false;
		}
	}
	
	public boolean requestExists(String sourceId, String targetId, RequestType type){
		String q = "SELECT COUNT(*) FROM requests WHERE sourceId = '"+sourceId+"' AND targetId = '"+targetId+"' AND type = "+type.ordinal();
		Query query= emf.createEntityManager().createNativeQuery(q);
		Long count = (Long)(query.getSingleResult());
		return count==0? false :true;
	}
}
