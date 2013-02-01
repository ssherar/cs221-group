package uk.ac.aber.dcs.cs221.n15.Controller;

/**
 * Gives a standard type for all different types of requests, 
 * which are saves as Int(11) in the database.
 */
public enum RequestType {
	FRIEND_REQUEST,
	ACCEPTED_FRIENDSHIP,
	DECLINED_FRIENDSHIP,
	OFFER_FIGHT,
	ACCEPTED_FIGHT,
	DECLINED_FIGHT,
	FIGHT_RESOLVED, //content is the winner's id.
	ACCEPT_BREED_OFFER,
	BREEDING_RESOLVED,
	BUY_MONSTER;
	
}
