package it.polito.ezshop.unitTests;

import static org.junit.Assert.*;

import java.time.LocalDate;


import org.junit.Test;

import it.polito.ezshop.model.BalanceOperation;


public class testBalanceOperation {
	private LocalDate data=LocalDate.now();
	@Test
	public void test0() {
		boolean thrown=false;
		  try {
			  BalanceOperation t=new  BalanceOperation(-1, data, 0.1,"return");
			  } catch (IllegalArgumentException e) {
			    thrown = true;
			  }
			  assertTrue(thrown);
	}
	@Test
	public void test1() {
		boolean thrown=false;
		  try {
			  BalanceOperation t=new  BalanceOperation(1, null, 0.1,"return");
			  } catch (IllegalArgumentException e) {
			    thrown = true;
			  }
			  assertTrue(thrown);
	}
	@Test
	public void test2() {
		boolean thrown=false;
		  try {
			  BalanceOperation t=new  BalanceOperation(1, data, -0.1,"return");
			  } catch (IllegalArgumentException e) {
			    thrown = true;
			  }
			  assertFalse(thrown);
	}
	@Test
	public void test3() {
		boolean thrown=false;
		  try {
			  BalanceOperation t=new  BalanceOperation(1, data, 1.01,"");
			  } catch (IllegalArgumentException e) {
			    thrown = true;
			  }
			  assertTrue(thrown);
	}
	@Test
	public void test4() {
		assertNotNull(new BalanceOperation(1, data, 1.01,"return"));

	}
}
