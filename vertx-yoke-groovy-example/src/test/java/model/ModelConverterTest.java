package model;

import static model.ModelConverter.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ModelConverterTest {

	
	@Test
	public void testCreateJsonUser() {
		User user = createTestUser();
		String jsonResult = createJson(user);
		assertNotNull(jsonResult);
		assertTrue(jsonResult.contains(user.getfName()));
	}

	@Test
	public void testCreateJsonListOfUser() {
		
		User user1 = new User();
		user1.setfName("UserName1");
		
		User user2 = new User();
		user2.setfName("UserName2");
		
		List<User> users = new ArrayList<User>();
		
		users.add(user1);
		users.add(user2);
		
		String jsonResult = createJson(users);
		assertNotNull(jsonResult);
		
		assertTrue(jsonResult.contains(user1.getfName()));
		assertTrue(jsonResult.contains(user2.getfName()));
		
	}

	@Test
	public void testCreateUser() {
		User user = createTestUser();
		String jsonResult = createJson(user);
		
		User restoredUser = createUser(jsonResult);
		
		assertEquals(user, restoredUser);
	}

	private User createTestUser() {
		User user = new User();
		user.setfName("UserName");
		return user;
	}

}
