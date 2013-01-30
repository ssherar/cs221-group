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
	int id;
	
	@Column(name="sourceId")
	private String sourceID;
	
	@Column(name="targetId")
	private String targetID;
	
	@Column(name="type")
	private RequestType type;
	
	@Column(name="content")
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
	
	public Request(){
		
	}
	
	public Request(String sourceId, String targetID, RequestType type){
		this.sourceID = sourceId;
		this.targetID = targetID;
		this.type = type;
	}

	public String convertToNotification(){
		return null;
	}

	public String getSourceID() {
		return sourceID;
	}



	public void setSourceID(String sourceID) {
		this.sourceID = sourceID;
	}



	public String getTargetID() {
		return targetID;
	}



	public void setTargetID(String targetID) {
		this.targetID = targetID;
	}



	public RequestType getType() {
		return type;
	}



	public void setType(RequestType type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getSeen() {
		return seen;
	}

	public void setSeen(int seen) {
		this.seen = seen;
	}
}
