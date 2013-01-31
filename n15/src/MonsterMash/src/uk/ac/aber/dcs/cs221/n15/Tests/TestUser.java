package uk.ac.aber.dcs.cs221.n15.Tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import uk.ac.aber.dcs.cs221.n15.Controller.UserDAO;
import uk.ac.aber.dcs.cs221.n15.Model.User;

public class TestUser {

	private UserDAO udao;
	private User user1;
	private User user2;
	
	@Before
	public void setup()
	{
		udao = new UserDAO();
	}
	

	@Test
	public void testUsernameExists() {
		user1 = new User();
		user1.setUsername("trevor");
		assertFalse(user1.getUsername(), true);
		
		user2 = new User();
		user2.setUsername("user1");
		assertTrue(user2.getUsername(), true);
	}

	@Test
	public void testLoadMonsters() {
		assertTrue(udao.loadMonsters("loc.user1") != null);	
	}

	@Test
	public void testCountMonsters() {
		User user = new User();
		assertTrue(udao.countMonsters(user) > 0);
	}

	@Test
	public void testCreateUser() {
		assertTrue(udao.createUser("grant@abc.com", "543210"));	
	}

	@Test
	public void testAuthenticateUser() {
		assertTrue(udao.authenticateUser("user1", "password") != null);
		assertTrue(udao.authenticateUser("username", "password") == null);
	}

	@Test
	public void testFindUser() {
		assertTrue(udao.findUser("loc.user1") != null);
		assertTrue(udao.findUser("123435") ==  null);
	}

	@Test
	public void testRetrieveFriends() {
		User user = new User("goat@abc.com", "123456");
		assertTrue(udao.retrieveFriends(user) == null);
		
		User user2 = new User("user1", "password");
		assertTrue(udao.retrieveFriends(user2) != null);
	}


	@Test
	public void testCheckFriendship() {
		assertFalse(udao.checkFriendship("loc.user1", "loc.goat"));
		assertTrue(udao.checkFriendship("loc.user3", "loc.user5"));
	}

	@Test
	public void testAddFriendship() {
		assertTrue(udao.addFriendship("user1", "user2"));
	}

	@Test
	public void testUpdateUser() {
		assertTrue(udao.updateUser("user1", "passwords"));
	}

}
