package uk.ac.aber.dcs.cs221.n15.Controller;

public abstract class Request {
	protected String sourceID;
	protected String targetID;
	protected boolean waitingForResponse;
	protected RequestType type;
	
	public abstract String convertToNotification();
}
