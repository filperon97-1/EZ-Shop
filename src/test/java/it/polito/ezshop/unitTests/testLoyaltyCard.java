package it.polito.ezshop.unitTests;

import static org.junit.Assert.*;

import java.time.LocalDate;

import it.polito.ezshop.exceptions.InvalidCustomerCardException;
import it.polito.ezshop.model.LoyaltyCard;
import org.junit.Test;

public class testLoyaltyCard {
	@Test
	public void test0() {
		boolean thrown=false;
		  try {
			  LoyaltyCard h=new LoyaltyCard("",  1);
			  } catch (InvalidCustomerCardException e) {
			    thrown = true;
			  }
			  assertTrue(thrown);
	}
	@Test
	public void test1() {
		boolean thrown=false;
		  try {
			  LoyaltyCard h=new LoyaltyCard("id",  -1);
			  } catch (InvalidCustomerCardException e) {
			    thrown = true;
			  }
			  assertTrue(thrown);
	}
	@Test
	public void test2() throws InvalidCustomerCardException {
		
		assertNotNull(new LoyaltyCard("0123456789",  1));

	}
	@Test
	public void test3() {
		boolean thrown=false;
		  try {
			  LoyaltyCard h=new LoyaltyCard("");
			  } catch (InvalidCustomerCardException e) {
			    thrown = true;
			  }
			  assertTrue(thrown);
	}
	@Test
	public void test4() throws InvalidCustomerCardException {
		
		assertNotNull(new LoyaltyCard("0123456789"));

	}
	public void test5() {
		assertFalse(LoyaltyCard.isValidCard(""));
	}
	@Test
	public void test6() throws InvalidCustomerCardException {
		
		assertTrue(LoyaltyCard.isValidCard("0123456789"));

	}
	
	
}