package it.polito.ezshop.unitTests;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;

import it.polito.ezshop.exceptions.InvalidCustomerCardException;
import it.polito.ezshop.model.Customer;
import it.polito.ezshop.model.LoyaltyCard;


public class testCustomer {
	@Test
	public void test0() {
		boolean thrown=false;
		  try {
			  Customer h=new Customer(-1,  "name", "card");
			  } catch (IllegalArgumentException e) {
			    thrown = true;
			  }
			  assertTrue(thrown);
	}
	@Test
	public void test1() {
		boolean thrown=false;
		  try {
			  Customer h=new Customer(1,  "", "card");
			  } catch (IllegalArgumentException e) {
			    thrown = true;
			  }
			  assertTrue(thrown);
	}/*
	@Test
	public void test2() {
		boolean thrown=false;
		  try {
			  Customer h=new Customer(1,  "name", "");
			  } catch (IllegalArgumentException e) {
			    thrown = true;
			  }
			  assertTrue(thrown);
	}*/
	@Test
	public void test3() {
		
		assertNotNull(new Customer(1,  "name", "card"));

	}
	
}