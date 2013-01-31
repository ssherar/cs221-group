package uk.ac.aber.dcs.cs221.n15.Controller;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="requests")
public class Request {
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	/**
	 * The id of the request
	 */
	int id;
	
	@Column(name="sourceId")
	/**
	 * The sender user id of the request
	 */
	private String sourceID;
	
	@Column(name="targetId")
	/**
	 * The target user id of the request
	 */
	private String targetID;
	
	@Column(name="type")
	/**
	 * The type of the request
	 */
	private RequestType type;
	
	@Column(name="content")
	/**
	 * The content of the request
	 */
	private String content;
	
	/**
	 * Specifies whether the request has been seen by both players that it concerns.
	 * 1 - only seen by source player
	 * 2 - only seen by target player
	 * 3 - seen by both (in this case it gets deleted immediately.
	 * Only concerns FIGHT_RESOLVED and BREED_RESOLVED
	 */
	@Column(name="seen")
	private int seen;
	
	/**
	 * Instantiates a request
	 */
	public Request(){
		
	}
	
	/**
	 * Instantiates a request
	 * @param sourceId The source user id of the request
	 * @param targetID The target user id of the request
	 * @param type The type of the request
	 */
	public Request(String sourceId, String targetID, RequestType type){
		this.sourceID = sourceId;
		this.targetID = targetID;
		this.type = type;
	}

	/**
	 * Gets the source user id
	 * @return The source user id
	 */
	public String getSourceID() {
		return sourceID;
	}



	/**
	 * Sets the source user id
	 * @param sourceID The source user id
	 */
	public void setSourceID(String sourceID) {
		this.sourceID = sourceID;
	}


	/**
	 * Gets the target user id
	 * @return The target user id
	 */
	public String getTargetID() {
		return targetID;
	}


	/**
	 * Sets the target user id
	 * @param targetID The target user id
	 */
	public void setTargetID(String targetID) {
		this.targetID = targetID;
	}


	/**
	 * Gets the type of the request
	 * @return The type of the request
	 */
	public RequestType getType() {
		return type;
	}


	/**
	 * Sets the type of the request
	 * @param type The type of the request
	 */
	public void setType(RequestType type) {
		this.type = type;
	}

	/**
	 * Gets the id of the request
	 * @return The id of the request
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the id of the request
	 * @param id The id of the request
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the content of the request
	 * @return The content of the request
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Sets the content of the request
	 * @param content The content of the request
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * Gets the seen value of the request
	 * @return The seen value of the request
	 */
	public int getSeen() {
		return seen;
	}
	
	/**
	 * Sets the seen value of the request
	 * @param seen The seen value of the request
	 */
	public void setSeen(int seen) {
		this.seen = seen;
	}
}
