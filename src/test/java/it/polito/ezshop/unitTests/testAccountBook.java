package it.polito.ezshop.unitTests;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import it.polito.ezshop.data.BalanceOperation;
import it.polito.ezshop.model.AccountBook;


public class testAccountBook {

	@Test
	public void test0() {
		boolean thrown=false;
		  try {
			  AccountBook t=new AccountBook(-1,  new ArrayList<BalanceOperation>());
			  } catch (IllegalArgumentException e) {
			    thrown = true;
			  }
			  assertTrue(thrown);
	}
	@Test
	public void test1() {
		boolean thrown=false;
		  try {
			  AccountBook t=new AccountBook(1, null);
			  } catch (IllegalArgumentException e) {
			    thrown = true;
			  }
			  assertTrue(thrown);
	}
	@Test
	public void test2() {
		assertNotNull(new AccountBook (1,  new ArrayList<BalanceOperation>()));

	}
	AccountBook t=new AccountBook(1, new ArrayList<BalanceOperation>());
	@Test
	public void test3() {
		assertNotNull(t.listTransactionsInRange(LocalDate.of(2000, 1, 1), LocalDate.now()));

	}
}
