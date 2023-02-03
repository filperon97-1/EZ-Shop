package it.polito.ezshop.unitTests;

import static org.junit.Assert.*;

import org.junit.Test;

import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.model.Customer;
import it.polito.ezshop.model.User;

public class testUser {

	@Test
	public void test0() {
		boolean thrown=false;
		  try {
			  User h=new User(-1,  "username", "password","Cashier");
			  } catch (InvalidUsernameException | InvalidPasswordException | InvalidRoleException | IllegalArgumentException e) {
			    thrown = true;
			  }
			  assertTrue(thrown);
	}
	@Test
	public void test1() {
		boolean thrown=false;
		  try {
			  User h=new User(1,  "", "password","Cashier");
			  } catch (InvalidUsernameException | InvalidPasswordException | InvalidRoleException e) {
			    thrown = true;
			  }
			  assertTrue(thrown);
	}
	@Test
	public void test2() {
		boolean thrown=false;
		  try {
			  User h=new User(1,  "username", "","Cashier");
			  } catch (InvalidUsernameException | InvalidPasswordException | InvalidRoleException e) {
			    thrown = true;
			  }
			  assertTrue(thrown);
	}
	@Test
	public void test3() {
		boolean thrown=false;
		  try {
			  User h=new User(1,  "username", "password",null);
			  } catch (InvalidUsernameException | InvalidPasswordException | InvalidRoleException e) {
			    thrown = true;
			  }
			  assertTrue(thrown);
	}
	@Test
	public void test4() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		
		assertNotNull(new User(1,  "username", "password","Cashier"));

	}

}
