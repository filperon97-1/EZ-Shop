package it.polito.ezshop.unitTests;

import static org.junit.Assert.*;

import org.junit.Test;

import it.polito.ezshop.model.CreditCard;

public class testCreditCard {

	@Test
	public void test0() {
		boolean thrown=false;
		  try {
			  new CreditCard("",  1.2);
			  } catch (IllegalArgumentException e) {
			    thrown = true;
			  }
			  assertTrue(thrown);
	}
	@Test
	public void test1() {
		boolean thrown=false;
		  try {
			  new CreditCard("creditCard",  -1.2);
			  } catch (IllegalArgumentException e) {
			    thrown = true;
			  }
			  assertTrue(thrown);
	}
	@Test
	public void test2() {
		assertNotNull(new CreditCard ("creditCard",  1.2));

	}
}
