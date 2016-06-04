package de.hm.edu.carads.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.hm.edu.carads.db.DatabaseController;
import de.hm.edu.carads.db.DatabaseControllerImpl;
import de.hm.edu.carads.db.ModelCollection;
import de.hm.edu.carads.db.util.DatabaseFactory;
import de.hm.edu.carads.models.User;
import de.hm.edu.carads.transaction.Credidentials;

public class RealmControllerTest {

	private static String USER_ID = "123";
	private static String USER_NAME = "test@test.de";
	
	private DatabaseController dbController = new DatabaseControllerImpl(
			DatabaseFactory.INST_TEST);
	
	RealmController rc = new RealmControllerImpl(dbController);
	
	@Test
	public void addUserTest() throws Exception{
		rc.addUser(makeNewUser());
		
		assertEquals(1, dbController.getAllEntities(ModelCollection.REALM).size());
	}
	
	@Test
	public void getUserTest() throws Exception{
		rc.addUser(makeNewUser());
		User user = rc.getUser(USER_NAME);
		assertEquals(USER_NAME, user.getUsername());
	}
	
	@Test
	public void changePasswordTest() throws Exception{
		rc.addUser(makeNewUser());
		User user = rc.getUser(USER_NAME);
		
		rc.changeCredidentials(user.getId(), new Credidentials("password", "asd"));
	}
	
	private User makeNewUser(){
		return new User(USER_NAME, "password", "driver", USER_ID);
	}
}
